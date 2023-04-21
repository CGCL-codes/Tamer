package org.objectstyle.woenvironment.pbx;

import java.util.Map;

/**
 * @author tlg
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PBXBuildFile extends PBXItem {

    public static final String _KFILEREF = "fileRef";

    public static final String _KSETTINGS = "settings";

    protected PBXReference fileRef;

    protected Map settings;

    public PBXBuildFile(Object ref) {
        super(ref);
    }

    public void setFileRef(Object fileRef) {
        this.fileRef = (PBXReference) fileRef;
    }

    public PBXReference getFileRef() {
        return this.fileRef;
    }

    public void setSettings(Object settings) {
        this.settings = (Map) settings;
    }

    public Map getSettings() {
        return this.settings;
    }

    public String getPath() {
        return this.fileRef.realPath();
    }
}
