package eu.cherrytree.paj.sound.openal;

import java.util.Vector;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;
import eu.cherrytree.paj.gui.Console;
import eu.cherrytree.paj.math.Vector3d;
import eu.cherrytree.paj.sound.SoundCue;
import eu.cherrytree.paj.sound.SoundInterface;
import eu.cherrytree.paj.sound.SoundSource;
import eu.cherrytree.paj.sound.SoundSource.SoundSourceCreateException;
import eu.cherrytree.paj.sound.SoundStream;

public class OpenALSoundInterface implements SoundInterface {

    private AL al;

    private Vector<OpenALSoundSource> sources = new Vector<OpenALSoundSource>();

    private Vector<OpenALSoundCue> cues = new Vector<OpenALSoundCue>();

    private Vector<OpenALSoundStream> streams = new Vector<OpenALSoundStream>();

    public OpenALSoundInterface() throws SoundInterfaceCreateException {
        initOpenAL();
    }

    @Override
    public void destroy() {
        freeAll();
        exitOpenAl();
    }

    void initOpenAL() throws SoundInterfaceCreateException {
        try {
            ALut.alutInit();
            al = ALFactory.getAL();
            al.alGetError();
        } catch (ExceptionInInitializerError err) {
            throw new SoundInterfaceCreateException("Couldn't create OpenAL Sound Interface. OpenAL is not present on the system!");
        } catch (Exception e) {
            throw new SoundInterfaceCreateException("Couldn't create OpenAL Sound Interface. " + e.getMessage());
        }
    }

    void exitOpenAl() {
        ALut.alutExit();
    }

    @Override
    public SoundSource createSoundSource() {
        al.alGetError();
        OpenALSoundSource ret = null;
        try {
            ret = new OpenALSoundSource(al);
            sources.add(ret);
        } catch (SoundSourceCreateException e) {
            Console.print(e.getMessage());
        }
        return ret;
    }

    @Override
    public SoundCue createSoundCue(String[] files, float[] probabilities) {
        al.alGetError();
        OpenALSoundCue ret = new OpenALSoundCue(files, probabilities, al);
        cues.add(ret);
        return ret;
    }

    @Override
    public SoundStream createSoundStream(String file) {
        al.alGetError();
        OpenALSoundStream ret = new OpenALSoundStream(file, al);
        streams.add(ret);
        return ret;
    }

    @Override
    public boolean isVolumeControlSupported() {
        return true;
    }

    public boolean isStreamVolumeControlSupported() {
        return true;
    }

    @Override
    public boolean isStereoSoundSupported() {
        return true;
    }

    @Override
    public boolean is3dSoundSupported() {
        return true;
    }

    @Override
    public boolean isPostProcessingSupported() {
        return false;
    }

    @Override
    public boolean isSoundDeviceSelectionSupported() {
        return false;
    }

    @Override
    public void freeSoundSource(SoundSource source) {
        source.destroy();
        sources.remove(source);
    }

    @Override
    public void freeSoundCue(SoundCue cue) {
        cue.destroy();
        cues.remove(cue);
    }

    @Override
    public void freeSoundStream(SoundStream stream) {
        stream.destroy();
        streams.remove(stream);
    }

    @Override
    public void freeAllSoundSources() {
        for (int i = 0; i < sources.size(); i++) sources.get(i).destroy();
        sources.clear();
    }

    @Override
    public void freeAllSoundCues() {
        for (int i = 0; i < cues.size(); i++) cues.get(i).destroy();
        cues.clear();
    }

    @Override
    public void freeAllSoundStreams() {
        for (int i = 0; i < streams.size(); i++) streams.get(i).destroy();
        streams.clear();
    }

    @Override
    public void freeAll() {
        freeAllSoundSources();
        freeAllSoundCues();
        freeAllSoundStreams();
    }

    @Override
    public void setListenerLocation(Vector3d location) {
        al.alListener3f(AL.AL_POSITION, (float) location.getX(), (float) location.getY(), (float) location.getZ());
        for (int i = 0; i < sources.size(); i++) {
            if (!sources.get(i).isPositional()) sources.get(i).forceSetLocation(location);
        }
    }

    public void setListenerOrientation(Vector3d direction, Vector3d up) {
        float[] orientation = { direction.getX(), direction.getY(), direction.getZ(), up.getX(), up.getY(), up.getZ() };
        al.alListenerfv(AL.AL_ORIENTATION, orientation, 0);
    }

    @Override
    public void setGlobalVolume(float volume) {
        OpenALSoundSource.globalVolume = volume;
        for (int i = 0; i < sources.size(); i++) sources.get(i).setVolume(sources.get(i).getVolume());
    }

    @Override
    public float getGlobalVolume() {
        return OpenALSoundSource.globalVolume;
    }

    @Override
    public void setStreamGlobalVolume(float volume) {
        OpenALSoundStream.globalVolume = volume;
        for (int i = 0; i < streams.size(); i++) streams.get(i).setVolume(streams.get(i).getVolume());
    }

    @Override
    public float getStreamGlobalVolume() {
        return OpenALSoundStream.globalVolume;
    }
}
