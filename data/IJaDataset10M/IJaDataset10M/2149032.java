package org.eclipse.swt.internal.mozilla;

public class nsIProfile extends nsISupports {

    static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 10;

    public static final String NS_IPROFILE_IID_STR = "02b0625a-e7f3-11d2-9f5a-006008a6efe9";

    public static final nsID NS_IPROFILE_IID = new nsID(NS_IPROFILE_IID_STR);

    public nsIProfile(int address) {
        super(address);
    }

    public int GetProfileCount(int[] aProfileCount) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aProfileCount);
    }

    public int GetProfileList(int[] length, int[] profileNames) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), length, profileNames);
    }

    public int ProfileExists(char[] profileName, boolean[] _retval) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), profileName, _retval);
    }

    public int GetCurrentProfile(int[] aCurrentProfile) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aCurrentProfile);
    }

    public int SetCurrentProfile(char[] aCurrentProfile) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aCurrentProfile);
    }

    public static final int SHUTDOWN_PERSIST = 1;

    public static final int SHUTDOWN_CLEANSE = 2;

    public int ShutDownCurrentProfile(int shutDownType) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), shutDownType);
    }

    public int CreateNewProfile(char[] profileName, char[] nativeProfileDir, char[] langcode, boolean useExistingDir) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), profileName, nativeProfileDir, langcode, useExistingDir);
    }

    public int RenameProfile(char[] oldName, char[] newName) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), oldName, newName);
    }

    public int DeleteProfile(char[] name, boolean canDeleteFiles) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), name, canDeleteFiles);
    }

    public int CloneProfile(char[] profileName) {
        return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), profileName);
    }
}
