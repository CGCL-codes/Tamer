package org.rakiura.rak;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a mutable ModuleInfo object. This class is to be 
 * used by module tools manipulating directly the ModuleInfo data.
 * 
 *<br><br>
 * MutableModuleInfo.java<br>
 * Created: Thu Nov 15 21:52:49 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.5 $ $Date: 2002/02/07 22:55:39 $
 */
public class MutableModuleInfo extends ModuleInfo {

    /**
   * Creates a new <code>MutableModuleInfo</code> instance.
   *
   * @param aData a <code>ModuleInfo</code> value
   */
    public MutableModuleInfo(final ModuleInfo aData) {
        this.name = aData.name;
        this.longname = aData.longname;
        if (aData.version != null) {
            this.version = (CRA) aData.version.clone();
        }
        this.vendor = aData.vendor;
        this.summary = aData.summary;
        this.group = aData.group;
        this.packager = aData.packager;
        this.copyright = aData.copyright;
        this.dependencies = (Dependency[]) aData.dependencies.clone();
        this.executable = new HashMap(aData.executable);
        this.defaultExecutable = aData.defaultExecutable;
        this.jarURLs = (String[]) aData.jarURLs.clone();
        this.rakURLs = (String[]) aData.rakURLs.clone();
    }

    /**
   * Sets the <code>name</code> value.
   * @param aName a <code>String</code> value
   */
    public void setName(final String aName) {
        this.name = aName;
    }

    /**
   * Sets the <code>version</code> value.
   * @param aVersion a <code>CRA</code> value
   */
    public void setVersion(final CRA aVersion) {
        this.version = aVersion;
    }

    /**
   * Sets the <code>vendor</code> value.
   * @param aVendor a <code>WebLink</code> value
   */
    public void setVendor(final WebLink aVendor) {
        this.vendor = aVendor;
    }

    /**
   * Sets the <code>jar-urls</code> value.
   * @param aUrls a <code>String[]</code> value
   */
    public void setJarURL(final String[] aUrls) {
        this.jarURLs = aUrls;
    }

    /**
   * Sets the <code>rak-urls</code> value.
   * @param aUrls a <code>String[]</code> value
   */
    public void setRakURL(final String[] aUrls) {
        this.rakURLs = aUrls;
    }

    /**
   * Sets the <code>longname</code> value.
   * @param aLName a <code>String</code> value
   */
    public void setLongname(final String aLName) {
        this.longname = aLName;
    }

    /**
   * Sets the <code>summary</code> value.
   * @param aSummary a <code>String</code> value
   */
    public void setSummary(final String aSummary) {
        this.summary = aSummary;
    }

    /**
   * Sets the <code>packager</code> value.
   * @param aPackager a <code>WebLink</code> value
   */
    public void setPackager(final WebLink aPackager) {
        this.packager = aPackager;
    }

    /**
   * Sets the <code>group</code> value.
   * @param aGroup a <code>String</code> value
   */
    public void setGroup(final String aGroup) {
        this.group = aGroup;
    }

    /**
   * Sets the <code>copyright</code> value.
   * @param aCpr a <code>String</code> value
   */
    public void setCopyright(final String aCpr) {
        this.copyright = aCpr;
    }

    /**
   * Sets the defaultExecutable value.
   * @param anAlias a <code>String</code> value
   */
    public void setDefaultExecutable(final String anAlias) {
        this.defaultExecutable = anAlias;
    }

    /**
   * Sets executables value (the alias->classname mapping).
   * @param aMap a <code>Map</code> value
   */
    public void setExecutable(final Map aMap) {
        this.executable = aMap;
    }

    /**
   * Sets dependency array of this module.
   * @param aDeps a <code>Dependency[]</code> value
   */
    public void setDependencies(final Dependency[] aDeps) {
        this.dependencies = aDeps;
    }
}
