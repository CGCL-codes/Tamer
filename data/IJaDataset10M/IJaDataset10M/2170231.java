package com.aelitis.azureus.core.speedmanager.impl.v2;

import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.core3.util.SystemTime;
import org.gudy.azureus2.core3.util.RealTimeInfo;
import com.aelitis.azureus.core.speedmanager.SpeedManagerLimitEstimate;
import com.aelitis.azureus.core.speedmanager.SpeedManagerPingMapper;
import com.aelitis.azureus.core.speedmanager.SpeedManager;
import com.aelitis.azureus.core.speedmanager.impl.SpeedManagerAlgorithmProviderAdapter;
import com.aelitis.azureus.core.AzureusCoreFactory;

/**
 * This class is responsible for re-adjusting the limits used by AutoSpeedV2.
 *
 * This class will keep track of the "status" (i.e. seeding, downloading)of the
 * application. It will then re-adjust the MAX limits when it thinks limits
 * are being reached.
 *
 * Here are the rules it will use.
 *
 * #1) When seeding. If the upload is AT_LIMIT for a period of time it will allow
 * that to adjust upward.
 * #2) When downloading. If the download is AT_LIMIT for a period of time it will
 * allow that to adjust upward.
 *
 * #3) When downloading, if a down-tick is detected and the upload is near a limit,
 * it will drop the upload limit to 80% of MAX_UPLOAD.
 *
 * #4) Once that limit is reached it will drop both the upload and download limits together.
 *
 * #5) Seeding mode is triggered when - download bandwidth at LOW - compared to CAPACITY for 5 minutes continously.
 *
 * #6) Download mode is triggered when - download bandwidth reaches MEDIUM - compared to CURRENT_LIMIT for the first time.
 *
 * Rules #5 and #6 favor downloading over seeding.
 *
 */
public class SpeedLimitMonitor implements PSMonitorListener {

    private int uploadLimitMax = SMConst.START_UPLOAD_RATE_MAX;

    private int uploadLimitMin = SMConst.calculateMinUpload(uploadLimitMax);

    private int downloadLimitMax = SMConst.START_DOWNLOAD_RATE_MAX;

    private int downloadLimitMin = SMConst.calculateMinDownload(downloadLimitMax);

    private TransferMode transferMode = new TransferMode();

    private SaturatedMode uploadBandwidthStatus = SaturatedMode.NONE;

    private SaturatedMode downloadBandwidthStatus = SaturatedMode.NONE;

    private SaturatedMode uploadLimitSettingStatus = SaturatedMode.AT_LIMIT;

    private SaturatedMode downloadLimitSettingStatus = SaturatedMode.AT_LIMIT;

    private SpeedLimitConfidence uploadLimitConf = SpeedLimitConfidence.NONE;

    private SpeedLimitConfidence downloadLimitConf = SpeedLimitConfidence.NONE;

    private long clLastIncreaseTime = -1;

    private long clFirstBadPingTime = -1;

    private boolean currTestDone;

    private boolean beginLimitTest;

    private int highestUploadRate = 0;

    private int highestDownloadRate = 0;

    private int preTestUploadCapacity = 5042;

    private int preTestUploadLimit = 5142;

    private int preTestDownloadCapacity = 5042;

    private int preTestDownloadLimit = 5142;

    public static final String UPLOAD_CONF_LIMIT_SETTING = "SpeedLimitMonitor.setting.upload.limit.conf";

    public static final String DOWNLOAD_CONF_LIMIT_SETTING = "SpeedLimitMonitor.setting.download.limit.conf";

    public static final String UPLOAD_CHOKE_PING_COUNT = "SpeedLimitMonitor.setting.choke.ping.count";

    private static final long CONF_LIMIT_TEST_LENGTH = 1000 * 30;

    private boolean isUploadMaxPinned = true;

    private boolean isDownloadMaxPinned = true;

    private long uploadAtLimitStartTime = SystemTime.getCurrentTime();

    private long downloadAtLimitStartTime = SystemTime.getCurrentTime();

    private int uploadChokePingCount = 1;

    private int uploadPinCounter = 0;

    private static final long TIME_AT_LIMIT_BEFORE_UNPINNING = 30 * 1000;

    public static final String USED_UPLOAD_CAPACITY_DOWNLOAD_MODE = "SpeedLimitMonitor.setting.upload.used.download.mode";

    public static final String USED_UPLOAD_CAPACITY_SEEDING_MODE = "SpeedLimitMonitor.setting.upload.used.seeding.mode";

    private float percentUploadCapacityDownloadMode = 0.6f;

    PingSpaceMapper pingMapOfDownloadMode;

    PingSpaceMapper pingMapOfSeedingMode;

    boolean useVariancePingMap = false;

    SpeedManagerPingMapper transientPingMap;

    PingSpaceMon longTermMonitor = new PingSpaceMon();

    LimitControl slider = new LimitControlDropUploadFirst();

    SpeedLimitListener persistentMapListener;

    public SpeedLimitMonitor(SpeedManager sm) {
        longTermMonitor.addListener(this);
        persistentMapListener = new SpeedLimitListener(this);
        sm.addListener(persistentMapListener);
    }

    /**
     * Splitting the limits our from other setting for SpeedManagerAlgorithmTI.
     */
    public void updateSettingsFromCOConfigManager() {
        percentUploadCapacityDownloadMode = (float) COConfigurationManager.getIntParameter(SpeedLimitMonitor.USED_UPLOAD_CAPACITY_DOWNLOAD_MODE, 60) / 100.0f;
        slider.updateSeedSettings(percentUploadCapacityDownloadMode);
    }

    public void updateFromCOConfigManager() {
        uploadLimitMax = COConfigurationManager.getIntParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT);
        uploadLimitMin = SMConst.calculateMinUpload(uploadLimitMax);
        downloadLimitMax = COConfigurationManager.getIntParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT);
        downloadLimitMin = SMConst.calculateMinDownload(downloadLimitMax);
        uploadLimitConf = SpeedLimitConfidence.parseString(COConfigurationManager.getStringParameter(SpeedLimitMonitor.UPLOAD_CONF_LIMIT_SETTING));
        downloadLimitConf = SpeedLimitConfidence.parseString(COConfigurationManager.getStringParameter(SpeedLimitMonitor.DOWNLOAD_CONF_LIMIT_SETTING));
        percentUploadCapacityDownloadMode = (float) COConfigurationManager.getIntParameter(SpeedLimitMonitor.USED_UPLOAD_CAPACITY_DOWNLOAD_MODE, 60) / 100.0f;
        uploadChokePingCount = Math.min(COConfigurationManager.getIntParameter(SpeedLimitMonitor.UPLOAD_CHOKE_PING_COUNT), 30);
        slider.updateLimits(uploadLimitMax, uploadLimitMin, downloadLimitMax, downloadLimitMin);
        slider.updateSeedSettings(percentUploadCapacityDownloadMode);
        if (isSettingDownloadUnlimited()) {
            slider.setDownloadUnlimitedMode(true);
        }
    }

    /**
     * replaces - updateFromCOConfigManager()
     */
    public void readFromPersistentMap() {
        SpeedManager sm = AzureusCoreFactory.getSingleton().getSpeedManager();
        SpeedManagerLimitEstimate uEst = SMConst.filterEstimate(sm.getEstimatedUploadCapacityBytesPerSec(), SMConst.START_UPLOAD_RATE_MAX);
        int upPingMapLimit = uEst.getBytesPerSec();
        if (upPingMapLimit < SMConst.START_UPLOAD_RATE_MAX) {
            uploadLimitMax = SMConst.START_UPLOAD_RATE_MAX;
        } else {
            uploadLimitMax = upPingMapLimit;
        }
        uploadLimitMin = SMConst.calculateMinUpload(uploadLimitMax);
        SpeedManagerLimitEstimate dEst = SMConst.filterEstimate(sm.getEstimatedDownloadCapacityBytesPerSec(), SMConst.START_DOWNLOAD_RATE_MAX);
        int downPingMapLimit = dEst.getBytesPerSec();
        if (isSettingDownloadUnlimited()) {
            slider.setDownloadUnlimitedMode(true);
        } else {
            slider.setDownloadUnlimitedMode(false);
        }
        if (downPingMapLimit < SMConst.START_DOWNLOAD_RATE_MAX) {
            downloadLimitMax = SMConst.START_DOWNLOAD_RATE_MAX;
        } else {
            downloadLimitMax = downPingMapLimit;
        }
        downloadLimitMin = SMConst.calculateMinDownload(downloadLimitMax);
        uploadLimitConf = SpeedLimitConfidence.convertType(uEst.getEstimateType());
        downloadLimitConf = SpeedLimitConfidence.convertType(dEst.getEstimateType());
        percentUploadCapacityDownloadMode = (float) COConfigurationManager.getIntParameter(SpeedLimitMonitor.USED_UPLOAD_CAPACITY_DOWNLOAD_MODE, 60) / 100.0f;
        saveToCOConfiguration();
    }

    public void saveToCOConfiguration() {
        COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT, uploadLimitMax);
        COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT, downloadLimitMax);
        COConfigurationManager.setParameter(SpeedLimitMonitor.UPLOAD_CONF_LIMIT_SETTING, uploadLimitConf.getString());
        COConfigurationManager.setParameter(SpeedLimitMonitor.DOWNLOAD_CONF_LIMIT_SETTING, downloadLimitConf.getString());
        COConfigurationManager.setParameter(SpeedLimitMonitor.UPLOAD_CHOKE_PING_COUNT, uploadChokePingCount);
    }

    private void logPMData(int oRate, SpeedLimitConfidence oConf, int nRate, float nConf, String type) {
    }

    public void logPMDataEx() {
        int tuploadLimitMax = COConfigurationManager.getIntParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT);
        int tdownloadLimitMax = COConfigurationManager.getIntParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT);
        SpeedManager sm = AzureusCoreFactory.getSingleton().getSpeedManager();
        SpeedManagerLimitEstimate dEst = sm.getEstimatedDownloadCapacityBytesPerSec();
        int tmpDMax = dEst.getBytesPerSec();
        float tmpDMaxConf = dEst.getEstimateType();
        SpeedManagerLimitEstimate uEst = sm.getEstimatedUploadCapacityBytesPerSec();
        int tmpUMax = uEst.getBytesPerSec();
        float tmpUMaxConf = uEst.getEstimateType();
        SpeedLimitConfidence tuploadLimitConf = SpeedLimitConfidence.parseString(COConfigurationManager.getStringParameter(SpeedLimitMonitor.UPLOAD_CONF_LIMIT_SETTING));
        SpeedLimitConfidence tdownloadLimitConf = SpeedLimitConfidence.parseString(COConfigurationManager.getStringParameter(SpeedLimitMonitor.DOWNLOAD_CONF_LIMIT_SETTING));
        logPMData(tuploadLimitMax, tuploadLimitConf, tmpUMax, tmpUMaxConf, "check-upload");
        logPMData(tdownloadLimitMax, tdownloadLimitConf, tmpDMax, tmpDMaxConf, "check-download");
    }

    /**
     * The criteria for download being unlimited is if the ConfigPanel has the
     * "download == 0 " && "type==fixed"
     * @return - true
     */
    private boolean isSettingDownloadUnlimited() {
        SpeedManagerAlgorithmProviderAdapter adpter = SMInstance.getInstance().getAdapter();
        SpeedManager sm = adpter.getSpeedManager();
        SpeedManagerLimitEstimate dEst = sm.getEstimatedDownloadCapacityBytesPerSec();
        int rate = dEst.getBytesPerSec();
        float type = dEst.getEstimateType();
        if (rate == 0 && type == SpeedManagerLimitEstimate.TYPE_MANUAL) {
            return true;
        }
        if (rate == 0 && type == SpeedManagerLimitEstimate.TYPE_UNKNOWN) {
            return true;
        }
        return false;
    }

    public void setDownloadBandwidthMode(int rate, int limit) {
        downloadBandwidthStatus = SaturatedMode.getSaturatedMode(rate, limit);
    }

    public void setUploadBandwidthMode(int rate, int limit) {
        uploadBandwidthStatus = SaturatedMode.getSaturatedMode(rate, limit);
    }

    public void setDownloadLimitSettingMode(int currLimit) {
        downloadLimitSettingStatus = SaturatedMode.getSaturatedMode(currLimit, downloadLimitMax);
    }

    public void setUploadLimitSettingMode(int currLimit) {
        if (!transferMode.isDownloadMode()) {
            uploadLimitSettingStatus = SaturatedMode.getSaturatedMode(currLimit, uploadLimitMax);
        } else {
            uploadLimitSettingStatus = SaturatedMode.getSaturatedMode(currLimit, uploadLimitMax);
        }
    }

    public int getUploadMaxLimit() {
        return uploadLimitMax;
    }

    public int getDownloadMaxLimit() {
        return downloadLimitMax;
    }

    public int getUploadMinLimit() {
        return uploadLimitMin;
    }

    public int getDownloadMinLimit() {
        return downloadLimitMin;
    }

    public String getUploadConfidence() {
        return uploadLimitConf.getString();
    }

    public String getDownloadConfidence() {
        return downloadLimitConf.getString();
    }

    public SaturatedMode getDownloadBandwidthMode() {
        return downloadBandwidthStatus;
    }

    public SaturatedMode getUploadBandwidthMode() {
        return uploadBandwidthStatus;
    }

    public SaturatedMode getDownloadLimitSettingMode() {
        return downloadLimitSettingStatus;
    }

    public SaturatedMode getUploadLimitSettingMode() {
        return uploadLimitSettingStatus;
    }

    public void updateTransferMode() {
        transferMode.updateStatus(downloadBandwidthStatus);
    }

    public String getTransferModeAsString() {
        return transferMode.getString();
    }

    public TransferMode getTransferMode() {
        return transferMode;
    }

    /**
     * Are both the upload and download bandwidths usages is low?
     * Otherwise false.
     * @return -
     */
    public boolean bandwidthUsageLow() {
        if (uploadBandwidthStatus.compareTo(SaturatedMode.LOW) <= 0 && downloadBandwidthStatus.compareTo(SaturatedMode.LOW) <= 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return -
     */
    public boolean bandwidthUsageMedium() {
        if (uploadBandwidthStatus.compareTo(SaturatedMode.MED) <= 0 && downloadBandwidthStatus.compareTo(SaturatedMode.MED) <= 0) {
            return true;
        }
        return false;
    }

    /**
     * True if both are at limits.
     * @return - true only if both the upload and download usages are at the limits.
     */
    public boolean bandwidthUsageAtLimit() {
        if (uploadBandwidthStatus.compareTo(SaturatedMode.AT_LIMIT) == 0 && downloadBandwidthStatus.compareTo(SaturatedMode.AT_LIMIT) == 0) {
            return true;
        }
        return false;
    }

    /**
     * True if the upload bandwidth usage is HIGH or AT_LIMIT.
     * @return -
     */
    public boolean isUploadBandwidthUsageHigh() {
        if (uploadBandwidthStatus.compareTo(SaturatedMode.AT_LIMIT) == 0 || uploadBandwidthStatus.compareTo(SaturatedMode.HIGH) == 0) {
            return true;
        }
        return false;
    }

    public boolean isEitherLimitUnpinned() {
        return (!isUploadMaxPinned || !isDownloadMaxPinned);
    }

    /**
     * Does the same as createNewLimit except it drops the upload rate first when in download mode.
     * @param signalStrength -
     * @param multiple -
     * @param currUpLimit -
     * @param currDownLimit -
     * @return  -
     */
    public SMUpdate modifyLimits(float signalStrength, float multiple, int currUpLimit, int currDownLimit) {
        if (isStartLimitTestFlagSet()) {
            SpeedManagerLogger.trace("modifyLimits - startLimitTesting.");
            SMUpdate update = startLimitTesting(currUpLimit, currDownLimit);
            return checkActiveProgressiveDownloadLimit(update);
        }
        if (isEitherLimitUnpinned()) {
            SpeedManagerLogger.trace("modifyLimits - calculateNewUnpinnedLimits");
            SMUpdate update = calculateNewUnpinnedLimits(signalStrength);
            return checkActiveProgressiveDownloadLimit(update);
        }
        slider.updateLimits(uploadLimitMax, uploadLimitMin, downloadLimitMax, downloadLimitMin);
        slider.updateStatus(currUpLimit, uploadBandwidthStatus, currDownLimit, downloadBandwidthStatus, transferMode);
        SMUpdate update = slider.adjust(signalStrength * multiple);
        return checkActiveProgressiveDownloadLimit(update);
    }

    /**
     * If a progressive download is currently active. Then the download limit should
     * not be allowed to go below that limit, regardless of anything else.
     * @param update -
     * @return -
     */
    private SMUpdate checkActiveProgressiveDownloadLimit(SMUpdate update) {
        long prgDownLimit = RealTimeInfo.getProgressiveActiveBytesPerSec();
        if (prgDownLimit == 0) {
            return update;
        }
        final int MULTIPLE = 2;
        if (prgDownLimit * MULTIPLE > update.newDownloadLimit && update.newDownloadLimit != 0) {
            log("Active Progressive download in progress. Overriding limit. curr=" + update.newDownloadLimit + " progDownloadLimit=" + prgDownLimit * MULTIPLE);
            update.newDownloadLimit = (int) prgDownLimit * MULTIPLE;
        }
        return update;
    }

    /**
     * Log debug info needed during beta period.
     */
    private void logPinningInfo() {
        StringBuffer sb = new StringBuffer("pin: ");
        if (isUploadMaxPinned) {
            sb.append("ul-pinned:");
        } else {
            sb.append("ul-unpinned:");
        }
        if (isDownloadMaxPinned) {
            sb.append("dl-pinned:");
        } else {
            sb.append("dl-unpinned:");
        }
        long currTime = SystemTime.getCurrentTime();
        long upWait = currTime - uploadAtLimitStartTime;
        long downWait = currTime - downloadAtLimitStartTime;
        sb.append(upWait).append(":").append(downWait);
        log(sb.toString());
    }

    /**
     *
     * @param signalStrength -
     * @return -
     */
    public SMUpdate calculateNewUnpinnedLimits(float signalStrength) {
        if (signalStrength < 0.0f) {
            isUploadMaxPinned = true;
            isDownloadMaxPinned = true;
        }
        boolean updateUpload = false;
        boolean updateDownload = false;
        if (uploadBandwidthStatus.compareTo(SaturatedMode.AT_LIMIT) == 0 && uploadLimitSettingStatus.compareTo(SaturatedMode.AT_LIMIT) == 0) {
            updateUpload = true;
        }
        if (downloadBandwidthStatus.compareTo(SaturatedMode.AT_LIMIT) == 0 && downloadLimitSettingStatus.compareTo(SaturatedMode.AT_LIMIT) == 0) {
            updateDownload = true;
        }
        boolean uploadChanged = false;
        boolean downloadChanged = false;
        if (updateUpload && !transferMode.isDownloadMode()) {
            uploadPinCounter++;
            if (uploadPinCounter % (Math.ceil(Math.sqrt(uploadChokePingCount))) == 0) {
                uploadLimitMax += calculateUnpinnedStepSize(uploadLimitMax);
                uploadChanged = true;
                COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT, uploadLimitMax);
                COConfigurationManager.setParameter(SpeedLimitMonitor.UPLOAD_CHOKE_PING_COUNT, uploadChokePingCount);
            }
        }
        if (updateDownload && !slider.isDownloadUnlimitedMode()) {
            downloadLimitMax += calculateUnpinnedStepSize(downloadLimitMax);
            downloadChanged = true;
            COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT, downloadLimitMax);
        }
        if (uploadLimitMax > downloadLimitMax) {
            downloadLimitMax = uploadLimitMax;
            downloadChanged = true;
            COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT, downloadLimitMax);
        }
        uploadLimitMin = SMConst.calculateMinUpload(uploadLimitMax);
        downloadLimitMin = SMConst.calculateMinDownload(downloadLimitMax);
        if (slider.isDownloadUnlimitedMode()) {
            SpeedManagerLogger.trace("upload unpinned while download is unlimited.");
            return new SMUpdate(uploadLimitMax, uploadChanged, 0, false);
        }
        return new SMUpdate(uploadLimitMax, uploadChanged, downloadLimitMax, downloadChanged);
    }

    /**
     * If setting is less then 100kBytes take 1 kByte steps.
     * If setting is less then 500kBytes take 5 kByte steps.
     * if setting is larger take 10 kBytes steps.
     * @param currLimitMax - current limit setting.
     * @return - set size for next change.
     */
    private int calculateUnpinnedStepSize(int currLimitMax) {
        if (currLimitMax < 102400) {
            return 1024;
        } else if (currLimitMax < 409600) {
            return 1024 * 5;
        } else if (currLimitMax >= 409600) {
            return 1024 * 10;
        }
        return 1024;
    }

    /**
     * Make a decision about unpinning either the upload or download limit. This is based on the
     * time we are saturating the limit without a down-tick signal.
     */
    public void checkForUnpinningCondition() {
        long currTime = SystemTime.getCurrentTime();
        slider.setDownloadUnlimitedMode(isSettingDownloadUnlimited());
        if (!uploadBandwidthStatus.equals(SaturatedMode.AT_LIMIT) || !uploadLimitSettingStatus.equals(SaturatedMode.AT_LIMIT)) {
            uploadAtLimitStartTime = currTime;
        } else {
            if (uploadAtLimitStartTime + (TIME_AT_LIMIT_BEFORE_UNPINNING * uploadChokePingCount) < currTime) {
                if (isUploadConfidenceLow()) {
                    if (!transferMode.isDownloadMode()) {
                        isUploadMaxPinned = false;
                    }
                } else {
                    if (!isUploadConfidenceAbsolute()) {
                        isUploadMaxPinned = false;
                        SpeedManagerLogger.trace("unpinning the upload max limit!! #choke-pings=" + uploadChokePingCount + ", pin-counter=" + uploadPinCounter);
                    }
                }
            }
        }
        if (!downloadBandwidthStatus.equals(SaturatedMode.AT_LIMIT) || !downloadLimitSettingStatus.equals(SaturatedMode.AT_LIMIT)) {
            downloadAtLimitStartTime = currTime;
        } else {
            if (downloadAtLimitStartTime + TIME_AT_LIMIT_BEFORE_UNPINNING < currTime) {
                if (isDownloadConfidenceLow()) {
                    if (transferMode.isDownloadMode()) {
                        triggerLimitTestingFlag();
                    }
                } else {
                    if (!isDownloadConfidenceAbsolute()) {
                        isDownloadMaxPinned = false;
                        SpeedManagerLogger.trace("unpinning the download max limit!!");
                    }
                }
            }
        }
        logPinningInfo();
    }

    /**
     * If we have a down-tick signal then resetTimer all the counters for increasing the limits.
     */
    public void notifyOfDownSignal() {
        if (!isUploadMaxPinned) {
            uploadChokePingCount++;
            String msg = "pinning the upload max limit, due to downtick signal. #downtick=" + uploadChokePingCount;
            SpeedManagerLogger.trace(msg);
            SMSearchLogger.log(msg);
        }
        if (!isDownloadMaxPinned) {
            String msg = "pinning the download max limit, due to downtick signal.";
            SpeedManagerLogger.trace(msg);
            SMSearchLogger.log(msg);
        }
        resetPinSearch();
    }

    void resetPinSearch() {
        long currTime = SystemTime.getCurrentTime();
        uploadAtLimitStartTime = currTime;
        downloadAtLimitStartTime = currTime;
        isUploadMaxPinned = true;
        isDownloadMaxPinned = true;
    }

    void resetPinSearch(SpeedManagerLimitEstimate estimate) {
        float type = estimate.getEstimateType();
        if (type >= SpeedManagerLimitEstimate.TYPE_CHOKE_ESTIMATED) {
            uploadChokePingCount++;
        }
        resetPinSearch();
    }

    /**
     * Return true if we are confidence testing the limits.
     * @return - SMUpdate
     */
    public boolean isConfTestingLimits() {
        return transferMode.isConfTestingLimits();
    }

    /**
     * Determine if we have low confidence in this limit.
     * @return - true if the confidence setting is LOW or NONE. Otherwise return true.
     */
    public boolean isDownloadConfidenceLow() {
        return (downloadLimitConf.compareTo(SpeedLimitConfidence.MED) < 0);
    }

    public boolean isUploadConfidenceLow() {
        return (uploadLimitConf.compareTo(SpeedLimitConfidence.MED) < 0);
    }

    public boolean isDownloadConfidenceAbsolute() {
        return (downloadLimitConf.compareTo(SpeedLimitConfidence.ABSOLUTE) == 0);
    }

    public boolean isUploadConfidenceAbsolute() {
        return (uploadLimitConf.compareTo(SpeedLimitConfidence.ABSOLUTE) == 0);
    }

    /**
     *
     * @param downloadRate - currentUploadRate in bytes/sec
     * @param uploadRate - currentUploadRate in bytes/sec
     */
    public synchronized void updateLimitTestingData(int downloadRate, int uploadRate) {
        if (downloadRate > highestDownloadRate) {
            highestDownloadRate = downloadRate;
        }
        if (uploadRate > highestUploadRate) {
            highestUploadRate = uploadRate;
        }
        long currTime = SystemTime.getCurrentTime();
        if (currTime > clLastIncreaseTime + CONF_LIMIT_TEST_LENGTH) {
            currTestDone = true;
        }
        if (clFirstBadPingTime != -1) {
            if (currTime > clFirstBadPingTime + CONF_LIMIT_TEST_LENGTH) {
                currTestDone = true;
            }
        }
    }

    /**
     * Convert raw ping value to new metric.
     * @param lastMetric -
     */
    public void updateLimitTestingPing(int lastMetric) {
        if (lastMetric > 500) {
            updateLimitTestingPing(-1.0f);
        }
    }

    /**
     * New metric from the PingMapper is value between -1.0 and +1.0f.
     * @param lastMetric -
     */
    public void updateLimitTestingPing(float lastMetric) {
        if (lastMetric < -0.3f) {
            clFirstBadPingTime = SystemTime.getCurrentTime();
        }
    }

    /**
     * Call this method to start the limit testing.
     * @param currUploadLimit -
     * @param currDownloadLimit -
     * @return - SMUpdate
     */
    public SMUpdate startLimitTesting(int currUploadLimit, int currDownloadLimit) {
        clLastIncreaseTime = SystemTime.getCurrentTime();
        clFirstBadPingTime = -1;
        highestUploadRate = 0;
        highestDownloadRate = 0;
        currTestDone = false;
        beginLimitTest = false;
        preTestUploadLimit = currUploadLimit;
        preTestDownloadLimit = currDownloadLimit;
        SMUpdate retVal;
        if (transferMode.isDownloadMode()) {
            retVal = new SMUpdate(uploadLimitMin, true, Math.round(downloadLimitMax * 1.2f), true);
            preTestDownloadCapacity = downloadLimitMax;
            transferMode.setMode(TransferMode.State.DOWNLOAD_LIMIT_SEARCH);
        } else {
            retVal = new SMUpdate(Math.round(uploadLimitMax * 1.2f), true, downloadLimitMin, true);
            preTestUploadCapacity = uploadLimitMax;
            transferMode.setMode(TransferMode.State.UPLOAD_LIMIT_SEARCH);
        }
        return retVal;
    }

    /**
     * Ramp the upload and download rates higher, so ping-times are relevant.
     * @param uploadLimit -
     * @param downloadLimit -
     * @return -
     */
    public SMUpdate rampTestingLimit(int uploadLimit, int downloadLimit) {
        SMUpdate retVal;
        if (transferMode.getMode() == TransferMode.State.DOWNLOAD_LIMIT_SEARCH && downloadBandwidthStatus.isGreater(SaturatedMode.MED)) {
            downloadLimit *= 1.1f;
            clLastIncreaseTime = SystemTime.getCurrentTime();
            retVal = new SMUpdate(uploadLimit, false, downloadLimit, true);
        } else if (transferMode.getMode() == TransferMode.State.UPLOAD_LIMIT_SEARCH && uploadBandwidthStatus.isGreater(SaturatedMode.MED)) {
            uploadLimit *= 1.1f;
            clLastIncreaseTime = SystemTime.getCurrentTime();
            retVal = new SMUpdate(uploadLimit, true, downloadLimit, false);
        } else {
            retVal = new SMUpdate(uploadLimit, false, downloadLimit, false);
            SpeedManagerLogger.trace("ERROR: rampTestLimit should only be called during limit testing. ");
        }
        return retVal;
    }

    public void triggerLimitTestingFlag() {
        SpeedManagerLogger.trace("triggerd fast limit test.");
        beginLimitTest = true;
        if (useVariancePingMap) {
            SMInstance pm = SMInstance.getInstance();
            SpeedManagerAlgorithmProviderAdapter adapter = pm.getAdapter();
            if (transientPingMap != null) {
                transientPingMap.destroy();
            }
            transientPingMap = adapter.createTransientPingMapper();
        }
    }

    public synchronized boolean isStartLimitTestFlagSet() {
        return beginLimitTest;
    }

    public synchronized boolean isConfLimitTestFinished() {
        return currTestDone;
    }

    public synchronized SMUpdate endLimitTesting(int downloadCapacityGuess, int uploadCapacityGuess) {
        SpeedManagerLogger.trace(" repalce highestDownloadRate: " + highestDownloadRate + " with " + downloadCapacityGuess);
        SpeedManagerLogger.trace(" replace highestUploadRate: " + highestUploadRate + " with " + uploadCapacityGuess);
        highestDownloadRate = downloadCapacityGuess;
        highestUploadRate = uploadCapacityGuess;
        return endLimitTesting();
    }

    /**
     * Call this method to end the limit testing.
     * @return - SMUpdate
     */
    public synchronized SMUpdate endLimitTesting() {
        SMUpdate retVal;
        if (transferMode.getMode() == TransferMode.State.DOWNLOAD_LIMIT_SEARCH) {
            downloadLimitConf = determineConfidenceLevel();
            SpeedManagerLogger.trace("pre-upload-setting=" + preTestUploadCapacity + " up-capacity" + uploadLimitMax + " pre-download-setting=" + preTestDownloadCapacity + " down-capacity=" + downloadLimitMax);
            retVal = new SMUpdate(preTestUploadLimit, true, downloadLimitMax, true);
            transferMode.setMode(TransferMode.State.DOWNLOADING);
        } else if (transferMode.getMode() == TransferMode.State.UPLOAD_LIMIT_SEARCH) {
            uploadLimitConf = determineConfidenceLevel();
            retVal = new SMUpdate(uploadLimitMax, true, downloadLimitMax, true);
            transferMode.setMode(TransferMode.State.SEEDING);
        } else {
            SpeedManagerLogger.log("SpeedLimitMonitor had IllegalState during endLimitTesting.");
            retVal = new SMUpdate(preTestUploadLimit, true, preTestDownloadLimit, true);
        }
        currTestDone = true;
        uploadAtLimitStartTime = SystemTime.getCurrentTime();
        downloadAtLimitStartTime = SystemTime.getCurrentTime();
        return retVal;
    }

    /**
     * After a test is complete determine how condifent the client should be in it
     * based on how different it is from the previous result.  If the new result is within
     * 20% of the old result then give it a MED. If it is great then give it a LOW. 
     * @return - what the new confidence interval should be.
     */
    public SpeedLimitConfidence determineConfidenceLevel() {
        SpeedLimitConfidence retVal = SpeedLimitConfidence.NONE;
        String settingMaxLimitName;
        boolean isDownload;
        String settingConfidenceName;
        int preTestValue;
        int highestValue;
        if (transferMode.getMode() == TransferMode.State.DOWNLOAD_LIMIT_SEARCH) {
            settingConfidenceName = DOWNLOAD_CONF_LIMIT_SETTING;
            settingMaxLimitName = SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT;
            isDownload = true;
            preTestValue = preTestDownloadCapacity;
            highestValue = highestDownloadRate;
        } else if (transferMode.getMode() == TransferMode.State.UPLOAD_LIMIT_SEARCH) {
            settingConfidenceName = UPLOAD_CONF_LIMIT_SETTING;
            settingMaxLimitName = SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT;
            isDownload = false;
            preTestValue = preTestUploadCapacity;
            highestValue = highestUploadRate;
        } else {
            SpeedManagerLogger.log("IllegalState in determineConfidenceLevel(). Setting level to NONE.");
            return SpeedLimitConfidence.NONE;
        }
        boolean hadChockingPing = hadChockingPing();
        float percentDiff = (float) Math.abs(highestValue - preTestValue) / (float) (Math.max(highestValue, preTestValue));
        if (percentDiff < 0.15f && hadChockingPing) {
            retVal = SpeedLimitConfidence.MED;
        } else {
            retVal = SpeedLimitConfidence.LOW;
        }
        COConfigurationManager.setParameter(settingConfidenceName, retVal.getString());
        int newMaxLimitSetting = highestValue;
        COConfigurationManager.setParameter(settingMaxLimitName, newMaxLimitSetting);
        int newMinLimitSetting;
        if (isDownload) {
            newMinLimitSetting = SMConst.calculateMinDownload(newMaxLimitSetting);
        } else {
            newMinLimitSetting = SMConst.calculateMinUpload(newMaxLimitSetting);
        }
        StringBuffer sb = new StringBuffer();
        if (transferMode.getMode() == TransferMode.State.UPLOAD_LIMIT_SEARCH) {
            sb.append("new upload limits: ");
            uploadLimitMax = newMaxLimitSetting;
            uploadLimitMin = newMinLimitSetting;
            if (downloadLimitMax < uploadLimitMax) {
                downloadLimitMax = uploadLimitMax;
                COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT, downloadLimitMax);
            }
            sb.append(uploadLimitMax);
        } else {
            sb.append("new download limits: ");
            downloadLimitMax = newMaxLimitSetting;
            downloadLimitMin = newMinLimitSetting;
            if (uploadLimitMax * 40 < downloadLimitMax) {
                uploadLimitMax = downloadLimitMax / 40;
                COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT, uploadLimitMax);
                uploadLimitMin = SMConst.calculateMinUpload(uploadLimitMax);
            }
            sb.append(downloadLimitMax);
        }
        slider.updateLimits(uploadLimitMax, uploadLimitMin, downloadLimitMax, downloadLimitMin);
        SpeedManagerLogger.trace(sb.toString());
        return retVal;
    }

    /**
     * If the user changes the line capacity settings on the configuration panel and adjustment
     * needs to occur even if the signal is NO-CHANGE-NEEDED. Test for that condition here.
     * @param currUploadLimit  - reported upload capacity from the adapter
     * @param currDownloadLimit - reported download capacity from the adapter.
     * @return - true if the "capacity" is lower then the current limit.
     */
    public boolean areSettingsInSpec(int currUploadLimit, int currDownloadLimit) {
        if (isConfTestingLimits()) {
            return true;
        }
        boolean retVal = true;
        if (currUploadLimit > uploadLimitMax) {
            retVal = false;
        }
        if (currDownloadLimit > downloadLimitMax && slider.isDownloadUnlimitedMode()) {
            retVal = false;
        }
        return retVal;
    }

    private int choseBestLimit(SpeedManagerLimitEstimate estimate, int currMaxLimit, SpeedLimitConfidence currConf) {
        float type = estimate.getEstimateType();
        int estBytesPerSec = estimate.getBytesPerSec();
        int chosenLimit;
        if ((estBytesPerSec < currMaxLimit) && estBytesPerSec < 20480) {
            return currMaxLimit;
        }
        String reason = "";
        if (type == SpeedManagerLimitEstimate.TYPE_MANUAL) {
            chosenLimit = estBytesPerSec;
            reason = "manual";
        } else if (type == SpeedManagerLimitEstimate.TYPE_UNKNOWN) {
            chosenLimit = Math.max(estBytesPerSec, currMaxLimit);
            reason = "unknown";
        } else if (type == SpeedManagerLimitEstimate.TYPE_ESTIMATED) {
            if (estimate.getMetricRating() >= 0.0) {
                return (currMaxLimit);
            } else {
                chosenLimit = estBytesPerSec;
                reason = "estimate and bad metric";
            }
        } else {
            chosenLimit = estBytesPerSec;
        }
        SpeedManagerLogger.trace("bestChosenLimit: reason=" + reason + ",chosenLimit=" + chosenLimit);
        return chosenLimit;
    }

    /**
     * Make some choices about how usable the limits are before passing them on.
     * @param estUp -
     * @param estDown -
     */
    public void setRefLimits(SpeedManagerLimitEstimate estUp, SpeedManagerLimitEstimate estDown) {
        SpeedManagerLimitEstimate up = SMConst.filterEstimate(estUp, SMConst.MIN_UPLOAD_BYTES_PER_SEC);
        int upMax = choseBestLimit(up, uploadLimitMax, uploadLimitConf);
        SpeedManagerLimitEstimate down = SMConst.filterEstimate(estDown, SMConst.MIN_DOWNLOAD_BYTES_PER_SEC);
        int downMax = choseBestLimit(down, downloadLimitMax, downloadLimitConf);
        if (downMax < upMax) {
            SpeedManagerLogger.trace("down max-limit was less then up-max limit. increasing down max-limit. upMax=" + upMax + " downMax=" + downMax);
            downMax = upMax;
        }
        setRefLimits(upMax, downMax);
    }

    public void setRefLimits(int uploadMax, int downloadMax) {
        if ((uploadLimitMax != uploadMax) && (uploadMax > 0)) {
            uploadLimitMax = uploadMax;
            COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT, uploadLimitMax);
        }
        uploadLimitMin = SMConst.calculateMinUpload(uploadMax);
        if ((downloadLimitMax != downloadMax) && (downloadMax > 0)) {
            downloadLimitMax = downloadMax;
            COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT, downloadLimitMax);
        }
        downloadLimitMin = SMConst.calculateMinDownload(downloadMax);
        SpeedManagerLogger.trace("setRefLimits uploadMax=" + uploadMax + " uploadLimitMax=" + uploadLimitMax + ", downloadMax=" + downloadMax + " downloadLimitMax=" + downloadLimitMax);
        slider.updateLimits(uploadLimitMax, uploadLimitMin, downloadLimitMax, downloadLimitMin);
    }

    /**
     * It is likely the user adjusted the "line speed capacity" on the configuration panel.
     * We need to adjust the current limits down to adjust.
     * @param currUploadLimit -
     * @param currDownloadLimit -
     * @return - Updates as needed.
     */
    public SMUpdate adjustLimitsToSpec(int currUploadLimit, int currDownloadLimit) {
        int newUploadLimit = currUploadLimit;
        boolean uploadChanged = false;
        int newDownloadLimit = currDownloadLimit;
        boolean downloadChanged = false;
        StringBuffer reason = new StringBuffer();
        if (currUploadLimit > uploadLimitMax && uploadLimitMax != 0) {
            newUploadLimit = uploadLimitMax;
            uploadChanged = true;
            reason.append(" (a) upload line-speed cap below current limit. ");
        }
        if (uploadLimitMax == 0) {
            reason.append("** uploadLimitMax=0 (Unlimited)! ** ");
        }
        if (currDownloadLimit > downloadLimitMax && !slider.isDownloadUnlimitedMode()) {
            newDownloadLimit = downloadLimitMax;
            downloadChanged = true;
            reason.append(" (b) download line-speed cap below current limit. ");
        }
        if (currUploadLimit < uploadLimitMin) {
            newUploadLimit = uploadLimitMin;
            uploadChanged = true;
            reason.append(" (c) min upload limit raised. ");
        }
        if (currDownloadLimit < downloadLimitMin) {
            newDownloadLimit = downloadLimitMin;
            downloadChanged = true;
            reason.append(" (d)  min download limit raised. ");
        }
        SpeedManagerLogger.trace("Adjusting limits due to out of spec: new-up=" + newUploadLimit + " new-down=" + newDownloadLimit + "  reasons: " + reason.toString());
        return new SMUpdate(newUploadLimit, uploadChanged, newDownloadLimit, downloadChanged);
    }

    protected void log(String str) {
        SpeedManagerLogger.log(str);
    }

    public void initPingSpaceMap(int maxGoodPing, int minBadPing) {
        pingMapOfDownloadMode = new PingSpaceMapper(maxGoodPing, minBadPing);
        pingMapOfSeedingMode = new PingSpaceMapper(maxGoodPing, minBadPing);
        useVariancePingMap = false;
    }

    public void initPingSpaceMap() {
        useVariancePingMap = true;
    }

    /**
     * This is a lot of data, but is important debug info.
     * @param name -
     * @param transEst -
     * @param hadChockPing -
     * @param permEst -
     * @param downMode -
     * @param seedMode -
     */
    public void betaLogPingMapperEstimates(String name, SpeedManagerLimitEstimate transEst, boolean hadChockPing, SpeedManagerLimitEstimate permEst, PingSpaceMapper downMode, PingSpaceMapper seedMode) {
        StringBuffer sb = new StringBuffer("beta-ping-maps-").append(name).append(": ");
        if (transEst != null) {
            int rate = transEst.getBytesPerSec();
            float conf = transEst.getMetricRating();
            sb.append("transient-").append(rate).append("(").append(conf).append(")");
        }
        sb.append(" chockPing=").append(hadChockPing);
        if (permEst != null) {
            int rate = permEst.getBytesPerSec();
            float conf = permEst.getMetricRating();
            sb.append("; perm-").append(rate).append("(").append(conf).append(")");
        }
        if (downMode != null) {
            int rateDown = downMode.guessDownloadLimit();
            int rateUp = downMode.guessUploadLimit();
            boolean downChockPing = downMode.hadChockingPing(true);
            boolean upChockPing = downMode.hadChockingPing(false);
            sb.append("; downMode- ");
            sb.append("rateDown=").append(rateDown).append(" ");
            sb.append("rateUp=").append(rateUp).append(" ");
            sb.append("downChockPing=").append(downChockPing).append(" ");
            sb.append("upChockPing=").append(upChockPing).append(" ");
        }
        if (seedMode != null) {
            int rateDown = seedMode.guessDownloadLimit();
            int rateUp = seedMode.guessUploadLimit();
            boolean downChockPing = seedMode.hadChockingPing(true);
            boolean upChockPing = seedMode.hadChockingPing(false);
            sb.append("; seedMode- ");
            sb.append("rateDown=").append(rateDown).append(" ");
            sb.append("rateUp=").append(rateUp).append(" ");
            sb.append("downChockPing=").append(downChockPing).append(" ");
            sb.append("upChockPing=").append(upChockPing).append(" ");
        }
        SpeedManagerLogger.log(sb.toString());
    }

    public int guessDownloadLimit() {
        if (!useVariancePingMap) {
            return pingMapOfDownloadMode.guessDownloadLimit();
        } else {
            boolean wasChocked = true;
            SpeedManagerLimitEstimate transientEst = null;
            if (transientPingMap != null) {
                transientEst = transientPingMap.getLastBadDownloadLimit();
                if (transientEst == null) {
                    wasChocked = false;
                    transientEst = transientPingMap.getEstimatedDownloadLimit(false);
                }
            }
            SMInstance pm = SMInstance.getInstance();
            SpeedManagerAlgorithmProviderAdapter adapter = pm.getAdapter();
            SpeedManagerPingMapper persistentMap = adapter.getPingMapper();
            SpeedManagerLimitEstimate persistentEst = persistentMap.getEstimatedDownloadLimit(false);
            betaLogPingMapperEstimates("down", transientEst, wasChocked, persistentEst, pingMapOfDownloadMode, pingMapOfSeedingMode);
            if (transientEst != null) {
                return choseBestLimit(transientEst, downloadLimitMax, downloadLimitConf);
            } else {
                return downloadLimitMax;
            }
        }
    }

    public int guessUploadLimit() {
        if (!useVariancePingMap) {
            int dmUpLimitGuess = pingMapOfDownloadMode.guessUploadLimit();
            int smUpLimitGuess = pingMapOfSeedingMode.guessUploadLimit();
            return Math.max(dmUpLimitGuess, smUpLimitGuess);
        } else {
            boolean wasChocked = true;
            SpeedManagerLimitEstimate transientEst = null;
            if (transientPingMap != null) {
                transientEst = transientPingMap.getLastBadUploadLimit();
                if (transientEst == null) {
                    wasChocked = false;
                    transientEst = transientPingMap.getEstimatedUploadLimit(false);
                }
            }
            SMInstance pm = SMInstance.getInstance();
            SpeedManagerAlgorithmProviderAdapter adapter = pm.getAdapter();
            SpeedManagerPingMapper persistentMap = adapter.getPingMapper();
            SpeedManagerLimitEstimate persistentEst = persistentMap.getEstimatedUploadLimit(false);
            betaLogPingMapperEstimates("up", transientEst, wasChocked, persistentEst, pingMapOfDownloadMode, pingMapOfSeedingMode);
            if (transientEst != null) {
                return choseBestLimit(transientEst, uploadLimitMax, uploadLimitConf);
            } else {
                return uploadLimitMax;
            }
        }
    }

    /**
     * Should return true if had a recent chocking ping.
     * @return - true if
     */
    public boolean hadChockingPing() {
        if (!useVariancePingMap) {
            return pingMapOfDownloadMode.hadChockingPing(true);
        } else {
            SpeedManagerPingMapper pm = SMInstance.getInstance().getAdapter().getPingMapper();
            SpeedManagerLimitEstimate dEst = pm.getEstimatedDownloadLimit(true);
            SpeedManagerLimitEstimate uEst = pm.getEstimatedUploadLimit(true);
            boolean hadChokePingUp = (uEst.getEstimateType() == SpeedManagerLimitEstimate.TYPE_CHOKE_ESTIMATED);
            boolean hadChokePingDown = (dEst.getEstimateType() == SpeedManagerLimitEstimate.TYPE_CHOKE_ESTIMATED);
            return (hadChokePingUp || hadChokePingDown);
        }
    }

    /**
     * Just log this data until we decide if it is useful.
     */
    public void logPingMapData() {
        if (!useVariancePingMap) {
            int downLimGuess = pingMapOfDownloadMode.guessDownloadLimit();
            int upLimGuess = pingMapOfDownloadMode.guessUploadLimit();
            int seedingUpLimGuess = pingMapOfSeedingMode.guessUploadLimit();
            StringBuffer sb = new StringBuffer("ping-map: ");
            sb.append(":down=").append(downLimGuess);
            sb.append(":up=").append(upLimGuess);
            sb.append(":(seed)up=").append(seedingUpLimGuess);
            SpeedManagerLogger.log(sb.toString());
        } else {
            SMInstance pm = SMInstance.getInstance();
            SpeedManagerAlgorithmProviderAdapter adapter = pm.getAdapter();
            SpeedManagerPingMapper persistentMap = adapter.getPingMapper();
            SpeedManagerLimitEstimate estUp = persistentMap.getEstimatedUploadLimit(false);
            SpeedManagerLimitEstimate estDown = persistentMap.getEstimatedDownloadLimit(false);
            int downLimGuess = estDown.getBytesPerSec();
            float downConf = estDown.getMetricRating();
            int upLimGuess = estUp.getBytesPerSec();
            float upConf = estUp.getMetricRating();
            String name = persistentMap.getName();
            StringBuffer sb = new StringBuffer("new-ping-map: ");
            sb.append(" name=").append(name);
            sb.append(", down=").append(downLimGuess);
            sb.append(", down-conf=").append(downConf);
            sb.append(", up=").append(upLimGuess);
            sb.append(", up-conf=").append(upConf);
            SpeedManagerLogger.log(sb.toString());
        }
    }

    public void setCurrentTransferRates(int downRate, int upRate) {
        if (pingMapOfDownloadMode != null && pingMapOfSeedingMode != null) {
            pingMapOfDownloadMode.setCurrentTransferRates(downRate, upRate);
            pingMapOfSeedingMode.setCurrentTransferRates(downRate, upRate);
        }
    }

    public void resetPingSpace() {
        if (pingMapOfDownloadMode != null && pingMapOfSeedingMode != null) {
            pingMapOfDownloadMode.reset();
            pingMapOfSeedingMode.reset();
        }
        if (transientPingMap != null) {
            transientPingMap.destroy();
        }
    }

    public void addToPingMapData(int lastMetricValue) {
        String modeStr = getTransferModeAsString();
        if (modeStr.equalsIgnoreCase(TransferMode.State.DOWNLOADING.getString()) || modeStr.equalsIgnoreCase(TransferMode.State.DOWNLOAD_LIMIT_SEARCH.getString())) {
            pingMapOfDownloadMode.addMetricToMap(lastMetricValue);
        } else if (modeStr.equalsIgnoreCase(TransferMode.State.SEEDING.getString()) || modeStr.equalsIgnoreCase(TransferMode.State.UPLOAD_LIMIT_SEARCH.getString())) {
            pingMapOfSeedingMode.addMetricToMap(lastMetricValue);
        }
        updateLimitTestingPing(lastMetricValue);
        longTermMonitor.updateStatus(transferMode);
    }

    public void notifyUpload(SpeedManagerLimitEstimate estimate) {
        int bestLimit = choseBestLimit(estimate, uploadLimitMax, uploadLimitConf);
        SpeedManagerLogger.trace("notifyUpload uploadLimitMax=" + uploadLimitMax);
        tempLogEstimate(estimate);
        if (bestLimit != uploadLimitMax) {
            SpeedManagerLogger.log("persistent PingMap changed upload limit to " + bestLimit);
            resetPinSearch(estimate);
            uploadLimitMax = bestLimit;
            COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_UPLOAD_MAX_LIMIT, uploadLimitMax);
        }
        uploadLimitMin = SMConst.calculateMinUpload(uploadLimitMax);
        slider.updateLimits(uploadLimitMax, uploadLimitMin, downloadLimitMax, downloadLimitMin);
        SMSearchLogger.log("new upload rate: " + uploadLimitMax);
    }

    public void notifyDownload(SpeedManagerLimitEstimate estimate) {
        int bestLimit = choseBestLimit(estimate, downloadLimitMax, downloadLimitConf);
        SpeedManagerLogger.trace("notifyDownload downloadLimitMax=" + downloadLimitMax + " conf=" + downloadLimitConf.getString() + " (" + downloadLimitConf.asEstimateType() + ")");
        tempLogEstimate(estimate);
        if (downloadLimitMax != bestLimit) {
            SpeedManagerLogger.log("persistent PingMap changed download limit to " + bestLimit);
            downloadLimitMax = bestLimit;
            COConfigurationManager.setParameter(SpeedManagerAlgorithmProviderV2.SETTING_DOWNLOAD_MAX_LIMIT, bestLimit);
        }
        downloadLimitMin = SMConst.calculateMinDownload(downloadLimitMax);
        slider.updateLimits(uploadLimitMax, uploadLimitMin, downloadLimitMax, downloadLimitMin);
        if (estimate.getBytesPerSec() != 0) {
            slider.setDownloadUnlimitedMode(false);
        } else {
            slider.setDownloadUnlimitedMode(true);
        }
        SMSearchLogger.log("download " + downloadLimitMax);
    }

    private void tempLogEstimate(SpeedManagerLimitEstimate est) {
        if (est == null) {
            SpeedManagerLogger.trace("notify log: SpeedManagerLimitEstimate was null");
        }
        StringBuffer sb = new StringBuffer();
        float metric = est.getMetricRating();
        float type = est.getEstimateType();
        int rate = est.getBytesPerSec();
        String str = est.getString();
        sb.append("notify log: ").append(str);
        sb.append(" metricRating=").append(metric);
        sb.append(" rate=").append(rate);
        sb.append(" type=").append(type);
        SpeedManagerLogger.trace(sb.toString());
    }
}
