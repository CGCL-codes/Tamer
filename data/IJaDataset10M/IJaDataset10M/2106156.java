package uk.org.toot.audio.vstfx;

import java.io.File;
import com.synthbot.audioplugin.vst.vst2.JVstHost2;
import uk.org.toot.audio.core.AudioControls;
import uk.org.toot.audio.spi.AudioControlServiceDescriptor;
import uk.org.toot.control.NativeSupport;
import uk.org.toot.misc.VstHost;
import uk.org.toot.misc.VstNativeSupport;

public class VstEffectControls extends AudioControls implements VstHost {

    private JVstHost2 vstfx;

    private NativeSupport nativeSupport;

    public VstEffectControls(AudioControlServiceDescriptor d) throws Exception {
        super(d.getModuleId(), d.getName());
        vstfx = JVstHost2.newInstance(new File(d.getPluginPath()), 44100, 4410);
        nativeSupport = new VstNativeSupport(this, vstfx);
    }

    public NativeSupport getNativeSupport() {
        return nativeSupport;
    }

    public boolean isPluginParent() {
        return true;
    }

    public JVstHost2 getVst() {
        return vstfx;
    }
}
