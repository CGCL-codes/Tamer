package com.sun.j3d.audioengines.javasound;

import java.net.URL;
import java.io.InputStream;
import javax.vecmath.*;
import javax.media.j3d.*;
import com.sun.j3d.audioengines.*;
import java.util.ArrayList;
import java.lang.Thread;

/**
 * The JavaSoundMixer Class defines an audio output device that accesses
 * JavaSound functionality stream data.
 */
public class JavaSoundMixer extends AudioEngine3DL2 {

    static final boolean debugFlag = false;

    static final boolean internalErrors = false;

    void debugPrint(String message) {
        if (debugFlag) System.out.println(message);
    }

    void debugPrintln(String message) {
        if (debugFlag) System.out.println(message);
    }

    static final int ADD_TO_LIST = 1;

    static final int SET_INTO_LIST = 2;

    JSAuralParameters auralParams = null;

    JSThread thread = null;

    protected float deviceGain = 1.0f;

    protected static final int NOT_PAUSED = 0;

    protected static final int PAUSE_PENDING = 1;

    protected static final int PAUSED = 2;

    protected static final int RESUME_PENDING = 3;

    protected int pause = NOT_PAUSED;

    public JavaSoundMixer(PhysicalEnvironment physicalEnvironment) {
        super(physicalEnvironment);
        thread = new JSThread(Thread.currentThread().getThreadGroup(), this);
    }

    /**
     * Query total number of channels available for sound rendering
     * for this audio device.
     * Overridden method from AudioEngine.
     * @return number of maximum voices play simultaneously on JavaSound Mixer.
     */
    public int getTotalChannels() {
        if (thread != null) return thread.getTotalChannels(); else return 32;
    }

    /**
     * Code to initialize the device
     * New interface to mixer/engine specific methods 
     * @return flag: true is initialized sucessfully, false if error
     */
    public boolean initialize() {
        if (thread == null) {
            return false;
        }
        thread.initialize();
        auralParams = new JSAuralParameters();
        if (debugFlag) debugPrintln("JavaSoundMixer: JSStream.initialize returned true");
        return true;
    }

    /**
     * Code to close the device.
     * New interface to mixer/engine specific methods 
     * @return flag: true is closed sucessfully, false if error
     */
    public boolean close() {
        if (thread == null) return false;
        if (thread.close()) {
            if (debugFlag) debugPrintln("JavaSoundMixer: JSStream.close returned true");
            return true;
        } else {
            if (debugFlag) debugPrintln("JavaSoundMixer: JSStream.close returned false");
            return false;
        }
    }

    /**
     * Code to load sound data into a channel of device mixer.
     * Load sound as one or mores sample into the Java Sound Mixer:
     *   a) as either a STREAM or CLIP based on whether cached is enabled
     *   b) positional and directional sounds use three samples per
     *      sound
     * Overriden method from AudioEngine3D.
     *
     * Sound type determines if this is a Background, Point or Cone
     * sound source and thus the JSXxxxSample object type
     * Call JSXxxxxSample.loadSample()
     * If no error
     *     Get the next free index in the samples list.
     *     Store a reference to JSXxxxSample object in samples list.
     * @return index to the sample in samples list.
     */
    public int prepareSound(int soundType, MediaContainer soundData) {
        int index = JSSample.NULL_SAMPLE;
        int methodType = ADD_TO_LIST;
        if (soundData == null) return JSSample.NULL_SAMPLE;
        synchronized (samples) {
            int samplesSize = samples.size();
            index = samplesSize;
            samples.ensureCapacity(index + 1);
            boolean error = false;
            if (soundType == AudioDevice3D.CONE_SOUND) {
                if (debugFlag) debugPrintln("JavaSoundMixer.prepareSound type=CONE");
                JSDirectionalSample dirSample = new JSDirectionalSample();
                error = dirSample.load(soundData);
                if (error) return JSSample.NULL_SAMPLE;
                if (methodType == SET_INTO_LIST) samples.set(index, dirSample); else samples.add(index, dirSample);
                dirSample.setDirtyFlags(0xFFFF);
                dirSample.setSoundType(soundType);
                dirSample.setSoundData(soundData);
            } else if (soundType == AudioDevice3D.POINT_SOUND) {
                if (debugFlag) debugPrintln("JavaSoundMixer.prepareSound type=POINT");
                JSPositionalSample posSample = new JSPositionalSample();
                error = posSample.load(soundData);
                if (error) return JSSample.NULL_SAMPLE;
                if (methodType == SET_INTO_LIST) samples.set(index, posSample); else samples.add(index, posSample);
                posSample.setDirtyFlags(0xFFFF);
                posSample.setSoundType(soundType);
                posSample.setSoundData(soundData);
            } else {
                if (debugFlag) debugPrintln("JavaSoundMixer.prepareSound type=BACKGROUND");
                JSSample sample = null;
                sample = new JSSample();
                error = sample.load(soundData);
                if (error) return JSSample.NULL_SAMPLE;
                if (methodType == SET_INTO_LIST) samples.set(index, sample); else samples.add(index, sample);
                sample.setDirtyFlags(0xFFFF);
                sample.setSoundType(soundType);
                sample.setSoundData(soundData);
            }
        }
        if (debugFlag) {
            debugPrint("               prepareSound type = " + soundType);
            debugPrintln("JavaSoundMixer.prepareSound returned " + index);
        }
        return index;
    }

    /**
     * Clears the fields associated with sample data for this sound.
     * Overriden method from AudioEngine3D.
     */
    public void clearSound(int index) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        sample.clear();
        synchronized (samples) {
            samples.set(index, null);
        }
    }

    /**
     * Save a reference to the local to virtual world coordinate space
     * Overriden method from AudioEngine3D.
     */
    public void setVworldXfrm(int index, Transform3D trans) {
        if (debugFlag) debugPrintln("JavaSoundMixer: setVworldXfrm for index " + index);
        super.setVworldXfrm(index, trans);
        if (debugFlag) {
            double[] matrix = new double[16];
            trans.get(matrix);
            debugPrintln("JavaSoundMixer     column-major transform ");
            debugPrintln("JavaSoundMixer         " + matrix[0] + ", " + matrix[1] + ", " + matrix[2] + ", " + matrix[3]);
            debugPrintln("JavaSoundMixer         " + matrix[4] + ", " + matrix[5] + ", " + matrix[6] + ", " + matrix[7]);
            debugPrintln("JavaSoundMixer         " + matrix[8] + ", " + matrix[9] + ", " + matrix[10] + ", " + matrix[11]);
            debugPrintln("JavaSoundMixer         " + matrix[12] + ", " + matrix[13] + ", " + matrix[14] + ", " + matrix[15]);
        }
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        int soundType = sample.getSoundType();
        if (soundType == AudioDevice3D.CONE_SOUND) {
            JSDirectionalSample dirSample = null;
            if ((dirSample = (JSDirectionalSample) getSample(index)) == null) return;
            dirSample.setXformedDirection();
            dirSample.setXformedPosition();
            dirSample.setVWrldXfrmFlag(true);
        } else if (soundType == AudioDevice3D.POINT_SOUND) {
            JSPositionalSample posSample = null;
            if ((posSample = (JSPositionalSample) getSample(index)) == null) return;
            posSample.setXformedPosition();
            posSample.setVWrldXfrmFlag(true);
        }
        return;
    }

    public void setPosition(int index, Point3d position) {
        if (debugFlag) debugPrintln("JavaSoundMixer: setPosition for index " + index);
        super.setPosition(index, position);
        JSPositionalSample posSample = null;
        if ((posSample = (JSPositionalSample) getSample(index)) == null) return;
        int soundType = posSample.getSoundType();
        if ((soundType == AudioDevice3D.POINT_SOUND) || (soundType == AudioDevice3D.CONE_SOUND)) {
            posSample.setXformedPosition();
        }
        return;
    }

    public void setDirection(int index, Vector3d direction) {
        if (debugFlag) debugPrintln("JavaSoundMixer: setDirection for index " + index);
        super.setDirection(index, direction);
        JSDirectionalSample dirSample = null;
        if ((dirSample = (JSDirectionalSample) getSample(index)) == null) return;
        int soundType = dirSample.getSoundType();
        if (soundType == AudioDevice3D.CONE_SOUND) {
            dirSample.setXformedDirection();
        }
        return;
    }

    public void setReflectionCoefficient(float coefficient) {
        super.setReflectionCoefficient(coefficient);
        auralParams.reverbDirty |= JSAuralParameters.REFLECTION_COEFF_CHANGED;
        return;
    }

    public void setReverbDelay(float reverbDelay) {
        super.setReverbDelay(reverbDelay);
        auralParams.reverbDirty |= JSAuralParameters.REVERB_DELAY_CHANGED;
        return;
    }

    public void setReverbOrder(int reverbOrder) {
        super.setReverbOrder(reverbOrder);
        auralParams.reverbDirty |= JSAuralParameters.REVERB_ORDER_CHANGED;
        return;
    }

    public int startSample(int index) {
        if (debugFlag) debugPrintln("JavaSoundMixer: STARTSample for index " + index);
        JSSample sample = null;
        if (((sample = (JSSample) getSample(index)) == null) || thread == null) return JSSample.NULL_SAMPLE;
        int soundType = sample.getSoundType();
        boolean muted = sample.getMuteFlag();
        if (muted) {
            if (debugFlag) debugPrintln("                            MUTEd start");
            thread.muteSample(sample);
            if (soundType != AudioDevice3D.BACKGROUND_SOUND) setFilter(index, false, Sound.NO_FILTER);
        } else {
            sample.render(sample.getDirtyFlags(), getView(), auralParams);
            this.scaleSampleRate(index, sample.rateRatio);
            if (soundType != AudioDevice3D.BACKGROUND_SOUND) setFilter(index, sample.getFilterFlag(), sample.getFilterFreq());
        }
        boolean startSuccessful;
        startSuccessful = thread.startSample(sample);
        sample.channel.startSample(sample.getLoopCount(), sample.getGain(), 0);
        if (!startSuccessful) {
            if (internalErrors) debugPrintln("JavaSoundMixer: Internal Error startSample for index " + index + " failed");
            return JSSample.NULL_SAMPLE;
        } else {
            if (debugFlag) debugPrintln("                startSample worked, " + "returning " + startSuccessful);
            if (!muted) {
                if (auralParams.reverbDirty > 0) {
                    if (debugFlag) {
                        debugPrintln("startSample: reverb settings are:");
                        debugPrintln("    coeff = " + auralParams.reflectionCoefficient + ", delay = " + auralParams.reverbDelay + ", order = " + auralParams.reverbOrder);
                    }
                    float delayTime = auralParams.reverbDelay * auralParams.rolloff;
                    calcReverb(sample);
                }
                setReverb(sample);
            }
            return index;
        }
    }

    public int stopSample(int index) {
        if (debugFlag) debugPrintln("JavaSoundMixer: STOPSample for index " + index);
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return -1;
        int dataType = sample.getDataType();
        int soundType = sample.getSoundType();
        boolean stopSuccessful = true;
        stopSuccessful = thread.stopSample(sample);
        sample.channel.stopSample();
        if (!stopSuccessful) {
            if (internalErrors) debugPrintln("JavaSoundMixer: Internal Error stopSample(s) for index " + index + " failed");
            return -1;
        } else {
            sample.reset();
            if (debugFlag) debugPrintln("JavaSoundMixer: stopSample for index " + index + " worked, returning " + stopSuccessful);
            return 0;
        }
    }

    public void pauseSample(int index) {
        if (debugFlag) debugPrintln("JavaSoundMixer: PAUSESample for index " + index);
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        thread.pauseSample(sample);
    }

    public void unpauseSample(int index) {
        if (debugFlag) debugPrintln("JavaSoundMixer: UNPAUSESample for index " + index);
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        thread.unpauseSample(sample);
    }

    public void updateSample(int index) {
        if (debugFlag) debugPrintln("JavaSoundMixer: UPDATESample for index " + index);
        JSSample sample = null;
        if (((sample = (JSSample) getSample(index)) == null) || thread == null) return;
        int soundType = sample.getSoundType();
        boolean muted = sample.getMuteFlag();
        if (muted) {
            if (soundType != AudioDevice3D.BACKGROUND_SOUND) setFilter(index, false, Sound.NO_FILTER);
            thread.muteSample(sample);
            if (debugFlag) debugPrintln("   Mute during update");
        } else {
            if (auralParams.reverbDirty > 0) {
                if (debugFlag) {
                    debugPrintln("updateSample: reverb settings are:");
                    debugPrintln("    coeff = " + auralParams.reflectionCoefficient + ", delay = " + auralParams.reverbDelay + ", order = " + auralParams.reverbOrder);
                }
                float delayTime = auralParams.reverbDelay * auralParams.rolloff;
                calcReverb(sample);
            }
            setReverb(sample);
            float reverbGain = 0.0f;
            if (!muted && auralParams.reverbFlag) {
                reverbGain = sample.getGain() * auralParams.reflectionCoefficient;
            }
            sample.render(sample.getDirtyFlags(), getView(), auralParams);
            if (soundType != AudioDevice3D.BACKGROUND_SOUND) setFilter(index, sample.getFilterFlag(), sample.getFilterFreq());
            thread.setSampleGain(sample, auralParams);
            thread.setSampleRate(sample, auralParams);
            thread.setSampleDelay(sample, auralParams);
        }
        return;
    }

    public void muteSample(int index) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        if (debugFlag) debugPrintln("JavaSoundMixer: muteSample");
        sample.setMuteFlag(true);
        thread.muteSample(sample);
        return;
    }

    public void unmuteSample(int index) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        if (debugFlag) debugPrintln("JavaSoundMixer: unmuteSample");
        sample.setMuteFlag(false);
        auralParams.reverbDirty = 0xFFFF;
        sample.setDirtyFlags(0xFFFF);
        thread.unmuteSample(sample);
        return;
    }

    public long getSampleDuration(int index) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return Sample.DURATION_UNKNOWN;
        long duration;
        if (sample != null) duration = sample.getDuration(); else duration = Sample.DURATION_UNKNOWN;
        if (debugFlag) debugPrintln("                return duration " + duration);
        return duration;
    }

    public int getNumberOfChannelsUsed(int index) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return 0; else return getNumberOfChannelsUsed(index, sample.getMuteFlag());
    }

    /**
     * Overriden method from AudioEngine3D.
     */
    public int getNumberOfChannelsUsed(int index, boolean muted) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return 0;
        int soundType = sample.getSoundType();
        int dataType = sample.getDataType();
        if (dataType == JSSample.STREAMING_MIDI_DATA || dataType == JSSample.BUFFERED_MIDI_DATA) return 1;
        if (soundType == BACKGROUND_SOUND) return 1; else return 3;
    }

    public long getStartTime(int index) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return 0L;
        if (sample.channel == null) return 0L;
        return (long) sample.channel.startTime;
    }

    void scaleSampleRate(int index, float scaleFactor) {
        if (debugFlag) debugPrintln("JavaSoundMixer: scaleSampleRate index " + index + ", scale factor = " + scaleFactor);
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null || thread == null) return;
        int dataType = sample.getDataType();
        if (debugFlag) debugPrintln(" scaleSampleRate.dataType = " + dataType + "using sample " + sample + " from samples[" + index + "]");
        int soundType = sample.getSoundType();
        if (dataType == JSSample.STREAMING_AUDIO_DATA || dataType == JSSample.BUFFERED_AUDIO_DATA) {
            thread.setSampleRate(sample, scaleFactor);
        } else if (dataType == JSSample.STREAMING_MIDI_DATA || dataType == JSSample.BUFFERED_MIDI_DATA) {
            thread.setSampleRate(sample, scaleFactor);
        } else {
            if (internalErrors) debugPrintln("JavaSoundMixer: Internal Error scaleSampleRate dataType " + dataType + " invalid");
        }
    }

    void calcReverb(JSSample sample) {
        int dataType = sample.getDataType();
        int soundType = sample.getSoundType();
        float decay = auralParams.decayTime;
        float delay = auralParams.reverbDelay * auralParams.rolloff;
        float reflection = auralParams.reflectionCoefficient;
        int order = auralParams.reverbOrder;
        if (auralParams.reflectionCoefficient == 0.0f || auralParams.reverbCoefficient == 0.0f) auralParams.reverbFlag = false; else {
            auralParams.reverbFlag = true;
            if (order > 0) {
                float clampedTime = order * delay;
                if (clampedTime < decay) decay = clampedTime;
            }
            if (delay < 100.0f) {
                if (decay <= 1500.0f) auralParams.reverbType = 2; else auralParams.reverbType = 4;
            } else if (delay < 500.0f) {
                if (decay <= 1500.0f) auralParams.reverbType = 3; else auralParams.reverbType = 6;
            } else {
                if (decay <= 1500.0f) auralParams.reverbType = 6; else auralParams.reverbType = 5;
            }
        }
        if (debugFlag) debugPrintln("JavaSoundMixer: setReverb for " + sample + ", type = " + auralParams.reverbType + ", flag = " + auralParams.reverbFlag);
        auralParams.reverbDirty = 0;
    }

    void setReverb(JSSample sample) {
        int soundType = sample.getSoundType();
        int dataType = sample.getDataType();
        if ((soundType == AudioDevice3D.CONE_SOUND) || (soundType == AudioDevice3D.POINT_SOUND)) {
            if (debugFlag) debugPrintln("setReverb called with type, on = " + auralParams.reverbType + ", " + auralParams.reverbFlag);
            if (sample == null) return;
            JSPositionalSample posSample = (JSPositionalSample) sample;
            if (posSample.channel == null) return;
            if (dataType == JSSample.STREAMING_AUDIO_DATA) {
                JSStream stream = (JSStream) posSample.channel;
                stream.setSampleReverb(auralParams.reverbType, auralParams.reverbFlag);
            } else if (dataType == JSSample.BUFFERED_AUDIO_DATA) {
                JSClip clip = (JSClip) posSample.channel;
                clip.setSampleReverb(auralParams.reverbType, auralParams.reverbFlag);
            } else {
                if (internalErrors) debugPrintln("JavaSoundMixer: Internal Error setReverb " + "dataType " + dataType + " invalid");
            }
        }
    }

    public void setLoop(int index, int count) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        int dataType = sample.getDataType();
        if (dataType == JSSample.STREAMING_AUDIO_DATA || dataType == JSSample.BUFFERED_AUDIO_DATA) {
            if (count == Sound.INFINITE_LOOPS) {
                count = 0x7FFFFFF;
            }
        }
        super.setLoop(index, count);
        return;
    }

    void setFilter(int index, boolean filterFlag, float filterFreq) {
        JSPositionalSample posSample = null;
        if ((posSample = (JSPositionalSample) getSample(index)) == null) return;
        if (posSample.channel == null) return;
        int dataType = posSample.getDataType();
        if (dataType == JSSample.STREAMING_MIDI_DATA || dataType == JSSample.BUFFERED_MIDI_DATA) {
            return;
        }
        if (dataType == JSSample.BUFFERED_AUDIO_DATA) {
            JSClip clip = (JSClip) posSample.channel;
            clip.setSampleFiltering(filterFlag, filterFreq);
        } else {
            JSStream stream = (JSStream) posSample.channel;
            stream.setSampleFiltering(filterFlag, filterFreq);
        }
        if (debugFlag) {
            debugPrintln("JavaSoundMixer:setFilter " + "of non-backgroundSound by (" + filterFlag + ", " + filterFreq + ")");
        }
    }

    public void setGain(float scaleFactor) {
        float oldDeviceGain = deviceGain;
        float gainFactor = scaleFactor / oldDeviceGain;
        deviceGain = scaleFactor;
        return;
    }

    public void setRateScaleFactor(int index, float rateScaleFactor) {
        JSSample sample = null;
        if ((sample = (JSSample) getSample(index)) == null) return;
        sample.setRateScaleFactor(rateScaleFactor);
        this.scaleSampleRate(index, rateScaleFactor);
    }

    /**
     * Pauses audio device engine without closing the device and associated
     * threads.
     * Causes all cached sounds to be paused and all streaming sounds to be
     * stopped.
     */
    public void pause() {
        pause = PAUSE_PENDING;
        return;
    }

    /**
     * Resumes audio device engine (if previously paused) without reinitializing     * the device.
     * Causes all paused cached sounds to be resumed and all streaming sounds
     * restarted.
     */
    public void resume() {
        pause = RESUME_PENDING;
        return;
    }
}
