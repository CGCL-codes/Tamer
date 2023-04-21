package org.tritonus.lowlevel.alsa;

import org.tritonus.share.TDebug;

/**	Object carrying a snd_mixer_t.
 */
public class AlsaMixer {

    long m_lNativeHandle;

    static {
        Alsa.loadNativeLibrary();
        if (TDebug.TraceAlsaMixerNative) {
            setTrace(true);
        }
    }

    public AlsaMixer(String strMixerName) throws Exception {
        if (open(0) < 0) {
            throw new Exception();
        }
        if (attach(strMixerName) < 0) {
            close();
            throw new Exception();
        }
        if (register() < 0) {
            close();
            throw new Exception();
        }
        if (load() < 0) {
            close();
            throw new Exception();
        }
    }

    /**	Calls snd_mixer_open().
	 */
    private native int open(int nMode);

    /**	Calls snd_mixer_attach().
	 */
    private native int attach(String strCardName);

    /**	Calls snd_mixer_selem_register(.., NULL, NULL).
		This is a hack, taken over from alsamixer.
	*/
    private native int register();

    /**	Calls snd_mixer_load().
	 */
    private native int load();

    /**	Calls snd_mixer_free().
	 */
    private native int free();

    /**	Calls snd_mixer_close().
	 */
    public native int close();

    /**
	   The caller has to allocate the indices and names arrays big
	   enough to hold information on all controls. If the retrieving
	   of controls is successful, a positive number (the number of
	   controls) is returned. If the arrays are not big enough, -1
	   is returned. In this case, it's the task of the caller to allocate
	   bigger arrays and try again.
	   Both arrays must be of the same size.

	   Calls snd_mixer_first_elem() and snd_mixer_elem_next().
	 */
    public native int readControlList(int[] anIndices, String[] astrNames);

    public static native void setTrace(boolean bTrace);
}
