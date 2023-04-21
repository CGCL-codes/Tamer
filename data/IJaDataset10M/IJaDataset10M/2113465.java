package com.crypticbit.ipa.central;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import com.crypticbit.ipa.central.backupfile.BackupFile;
import com.crypticbit.ipa.entity.status.Info;
import com.crypticbit.ipa.entity.status.Manifest;
import com.crypticbit.ipa.entity.status.Status;
import com.crypticbit.ipa.io.parser.plist.PListContainer;
import com.crypticbit.ipa.io.parser.plist.PListFactory;

abstract class IPhoneParser {

    protected static final class NullProgressIndicator implements ProgressIndicator {

        @Override
        public void progressUpdate(final int entry, final int outOf, final String description) {
        }
    }

    private ProgressIndicator progressIndicator;

    protected Map<BackupFile, BackupFileType> backupFileInstanceToType = new HashMap<BackupFile, BackupFileType>();

    protected List<BackupFile> backupFiles = new ArrayList<BackupFile>();

    protected Map<String, Object> globalVars = new HashMap<String, Object>();

    protected IPhoneParser(final ProgressIndicator progressIndicator) throws IOException {
        this.progressIndicator = progressIndicator;
    }

    public abstract IPhone getIphoneConfiguration();

    protected ProgressIndicator getProgressIndicator() {
        return this.progressIndicator;
    }

    protected static BackupConfigurationElements getConfiguration(File sourceDir) throws FileParseException, IOException {
        Manifest manifest = null;
        Info info = null;
        Status status = null;
        try {
            info = findContainerForFile(new File(sourceDir, "Info.plist")).getAsInterface(Info.class);
        } catch (FileNotFoundException e) {
            LogFactory.getLogger().log(Level.INFO, "Info.plist was not found");
        }
        try {
            manifest = findContainerForFile(new File(sourceDir, "Manifest.plist")).getAsInterface(Manifest.class);
        } catch (FileNotFoundException e) {
            LogFactory.getLogger().log(Level.INFO, "Manifest.plist was not found");
        }
        try {
            status = findContainerForFile(new File(sourceDir, "Status.plist")).getAsInterface(Status.class);
        } catch (FileNotFoundException e) {
            LogFactory.getLogger().log(Level.INFO, "Status.plist was not found");
        }
        return new BackupConfigurationElements(manifest, status, info);
    }

    private static PListContainer findContainerForFile(final File file) throws IOException, FileParseException, FileNotFoundException {
        return PListFactory.createParser(null, new FileInputStream(file)).getRootContainer();
    }

    protected Map<String, Object> getGlobalVars(final BackupConfigurationElements elements) {
        this.globalVars = new HashMap<String, Object>();
        if (elements != null) {
            Info elementInfo = elements.getInfo();
            if (elementInfo != null) {
                this.globalVars.put("info.buildVersion", elementInfo.getBuildVersion());
                this.globalVars.put("info.deviceName", elementInfo.getDeviceName());
                this.globalVars.put("info.displayName", elementInfo.getDisplayName());
                this.globalVars.put("info.guid", elementInfo.getGUID());
                this.globalVars.put("info.itunesVersion", elementInfo.getITunesVersion());
                this.globalVars.put("info.lastBackupDate", elementInfo.getLastBackupDate());
                this.globalVars.put("info.phoneNumber", elementInfo.getPhoneNumber());
                this.globalVars.put("info.productType", elementInfo.getProductType());
                this.globalVars.put("info.productVersion", elementInfo.getProductVersion());
                this.globalVars.put("info.serialNumber", elementInfo.getSerialNumber());
                this.globalVars.put("info.targetIdentifier", elementInfo.getTargetIdentifier());
                this.globalVars.put("info.targetType", elementInfo.getTargetType());
                this.globalVars.put("info.uniqueID", elementInfo.getUniqueIdentifier());
            }
        }
        return this.globalVars;
    }
}
