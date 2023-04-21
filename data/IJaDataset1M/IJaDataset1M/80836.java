package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.modes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.TdbSpec;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.IDBReader;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.AbsoluteSaferPeriodCalculator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.ISaferPeriodCalculator;

/**
 * An implementation that looks how many records (if any) have been inserted
 * since <i>f(period)</i> ago. Where <i>f(period)</i> is the "safety period",
 * meaning a time span longer than period to allow for network (or any other
 * reason) delays.
 * 
 * @author CLAISSE
 */
public class TdbModeControllerByRecordCount extends TdbModeControllerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TdbModeControllerByRecordCount.class);

    private final IDBReader attributesReader;

    public TdbModeControllerByRecordCount(final IDBReader attributesReader) {
        super();
        this.attributesReader = attributesReader;
    }

    @Override
    protected int controlPeriodicMode(final ModePeriode modeP, final TdbSpec spec, final ArchivingAttribute attr) throws DevFailed {
        LOGGER.debug("TdbModeControllerByRecordCount.controlPeriodicMode: " + attr.getCompleteName());
        if (modeP == null) {
            LOGGER.debug("TdbModeControllerByRecordCount/controlPeriodicMode/modeP == null!/for attribute|" + attr.getCompleteName() + "|");
            return IModeController.CONTROL_FAILED;
        }
        final int period = modeP.getPeriod();
        final long exportPeriod_millis = spec.getExportPeriod();
        final int exportPeriod = (int) exportPeriod_millis;
        final ISaferPeriodCalculator saferPeriodCalculator = new AbsoluteSaferPeriodCalculator(AbsoluteSaferPeriodCalculator.DEFAULT_AMOUNT, AbsoluteSaferPeriodCalculator.DEFAULT_TYPE);
        final int saferPeriod = saferPeriodCalculator.getSaferPeriod(period + exportPeriod);
        final String completeName = attr.getCompleteName();
        int recordCount;
        try {
            recordCount = attributesReader.getRecordCount(completeName, saferPeriod);
            LOGGER.debug("controlPeriodicMode - recordCount: " + recordCount);
        } catch (final DevFailed e) {
            LOGGER.error("FastMode3Controller/controlPeriodicMode/failed calling getRecordCount for attribute|" + completeName + "|", e);
            return IModeController.CONTROL_FAILED;
        }
        if (recordCount > 0) {
            return IModeController.CONTROL_OK;
        } else {
            return IModeController.CONTROL_KO;
        }
    }
}
