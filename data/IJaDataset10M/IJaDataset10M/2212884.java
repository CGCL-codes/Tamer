package org.blackdog.type;

import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.blackdog.type.scan.ScanProperties;
import org.siberia.type.SibList;
import org.siberia.type.annotation.bean.Bean;

/**
 *
 * Scannable implementation of a PlayList
 *
 * @author alexis
 */
@Bean(name = "scannable playlist", internationalizationRef = "org.blackdog.rc.i18n.type.PlayList", expert = true, hidden = true, preferred = true, propertiesClassLimit = Object.class, methodsClassLimit = Object.class)
public class ScannablePlayList extends PlayList implements Scannable {

    /** indicate if it is scannable */
    private boolean scannable = true;

    /** scan properties related to this playlist */
    private ScanProperties scanProperties = null;

    /** Creates a new instance of ScannablePlayList */
    public ScannablePlayList() {
        super();
        try {
            this.setContentItemAsChild(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        this.addHierarchicalListenerOnContentItems = false;
    }

    /**
     * return true if the object can be scanned
     * 
     * @return a boolean
     */
    public boolean isScannable() {
        return this.scannable;
    }

    /**
     * indicate if the object can be scanned
     * 
     * @param scannable a boolean
     */
    public void setScannable(boolean scannable) {
        this.scannable = scannable;
    }

    /**
     * return the ScanProperties related to this item
     * 
     * @return a ScanProperties
     */
    public ScanProperties getScanProperties() {
        return this.scanProperties;
    }

    /**
     * initialize the ScanProperties related to this item
     * 
     * @param properties a ScanProperties
     */
    public void setScanProperties(ScanProperties properties) {
        this.scanProperties = properties;
    }

    /** return the item contained in the list linked to the given url
     *  @param url an URL
     *  @return a SongItem or null if no item found
     */
    public SongItem getItemLinkedTo(URL url) {
        SongItem item = null;
        if (url != null) {
            for (int i = 0; i < this.size(); i++) {
                Object current = this.get(i);
                if (current instanceof SongItem) {
                    URL currentUrl = ((SongItem) current).getValue();
                    if (url.equals(currentUrl)) {
                        item = (SongItem) current;
                        break;
                    }
                }
            }
        }
        return item;
    }
}
