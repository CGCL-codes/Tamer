package sun.java2d.cmm.lcms;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.color.CMMException;
import sun.java2d.cmm.ColorTransform;
import sun.java2d.cmm.PCMM;
import sun.java2d.cmm.lcms.LCMS;
import sun.java2d.cmm.lcms.LCMSTransform;

public class LCMS implements PCMM {

    public native long loadProfile(byte[] data);

    public native void freeProfile(long profileID);

    public native synchronized int getProfileSize(long profileID);

    public native synchronized void getProfileData(long profileID, byte[] data);

    public native synchronized int getTagSize(long profileID, int tagSignature);

    public native synchronized void getTagData(long profileID, int tagSignature, byte[] data);

    public native synchronized void setTagData(long profileID, int tagSignature, byte[] data);

    public static native long getProfileID(ICC_Profile profile);

    public static native long createNativeTransform(long[] profileIDs, int renderType, Object disposerRef);

    /**
     * Constructs ColorTransform object corresponding to an ICC_profile
     */
    public ColorTransform createTransform(ICC_Profile profile, int renderType, int transformType) {
        return new LCMSTransform(profile, renderType, renderType);
    }

    /**
     * Constructs an ColorTransform object from a list of ColorTransform
     * objects
     */
    public synchronized ColorTransform createTransform(ColorTransform[] transforms) {
        return new LCMSTransform(transforms);
    }

    public static native void colorConvert(LCMSTransform trans, LCMSImageLayout src, LCMSImageLayout dest);

    public static native void freeTransform(long ID);

    public static native void initLCMS(Class Trans, Class IL, Class Pf);

    static {
        java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

            public Object run() {
                System.loadLibrary("awt");
                System.loadLibrary("lcms");
                return null;
            }
        });
        initLCMS(LCMSTransform.class, LCMSImageLayout.class, ICC_Profile.class);
    }
}
