package org.tcpfile.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.tcpfile.gui.settingsmanager.SettingContainer;
import org.tcpfile.gui.settingsmanager.settings.SettingAnno;
import org.tcpfile.net.Connection;

public class TrustLevel {

    @SettingAnno(descriptionText = "Allow this Contact to upload Files")
    public boolean mayUploadFiles = true;

    @SettingAnno(descriptionText = "Accept all files directly")
    public boolean acceptUploadsDirectly = false;

    @SettingAnno(descriptionText = "Allow this Contact to see all Shares")
    public boolean maySeeAllShares = false;

    @SettingAnno(descriptionText = "Allow this Contact to download from unfinished downloads")
    public boolean maySeeDownloads = false;

    @SettingAnno(descriptionText = "Dont rehash incoming fileparts coming from this person")
    public boolean dontRehashParts = false;

    public int uploadFileNumberQuota = 100;

    public int uploadFileSizeQuota = 10 * 1024 * 1024;

    private transient SettingContainer sc;

    /**
	 * This user may download stuff with this speed
	 */
    @SettingAnno(descriptionText = "Upload Speed [kb/s]. -1 For default value for upload per connection", intMin = -1, toolTipText = "Override the default speed per connection with this")
    public int uploadSpeedKBs = -1;

    public Contact c;

    private TrustLevel() {
    }

    public TrustLevel(Contact c) {
        this.c = c;
    }

    public String toString() {
        return "may upload Files " + mayUploadFiles + "; accept directly " + acceptUploadsDirectly + "; sees all shares " + maySeeAllShares + " dont rehash parts " + dontRehashParts;
    }

    public SettingContainer getSettingContainer() {
        if (sc == null) sc = SettingContainer.generateContainerWithAnnotations(this, "TrustLevel");
        sc.findSetting("uploadSpeedKBs").addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                int i = (Integer) evt.getNewValue();
                if (i == -1) i = Connection.UploadPerConnection;
                c.getCon().speedByTrustLevel = i;
            }
        });
        return sc;
    }
}
