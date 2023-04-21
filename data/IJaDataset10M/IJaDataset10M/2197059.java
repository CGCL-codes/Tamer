package com.cirnoworks.fisce.audio.nh;

import com.cirnoworks.fisce.audio.FiScEAudioToolkit;
import com.cirnoworks.fisce.intf.IThread;
import com.cirnoworks.fisce.intf.NativeHandlerTemplate;
import com.cirnoworks.fisce.intf.VMCriticalException;
import com.cirnoworks.fisce.intf.VMException;

public class SoundEffectRelease extends NativeHandlerTemplate {

    private final FiScEAudioToolkit toolkit;

    public SoundEffectRelease(FiScEAudioToolkit toolkit) {
        this.toolkit = toolkit;
    }

    public void dealNative(int[] args, IThread thread) throws VMException, VMCriticalException {
        toolkit.releaseSoundEffect(args[0]);
    }

    public String getUniqueName() {
        return "com/cirnoworks/audio/SoundEffect.release.(Ljava/lang/String;)V";
    }
}
