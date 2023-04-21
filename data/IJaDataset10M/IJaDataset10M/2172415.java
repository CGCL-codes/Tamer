package com.pcmsolutions.system.audio;

import com.pcmsolutions.smdi.SmdiSampleHeader;
import com.pcmsolutions.system.ZUtilities;
import org.tritonus.zuonics.sampled.AbstractAudioChunk;
import org.tritonus.zuonics.sampled.aiff.AiffINSTChunk;
import org.tritonus.zuonics.sampled.aiff.AiffMARKChunk;
import org.tritonus.zuonics.sampled.wave.WaveSmplChunk;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: paulmeehan
 * Date: 03-Dec-2003
 * Time: 13:10:49
 * To change this template use Options | File Templates.
 */
public class AudioUtilities {

    public static AudioFileFormat.Type defaultAudioFormat = AudioFileFormat.Type.WAVE;

    public static final int maxClipLen = 1024 * 1204 * 16;

    public static final String WAV_EXTENSION = "wav";

    public static final String SAMPLE_NAMING_MODE_SIN = "SIN";

    public static final String SAMPLE_NAMING_MODE_IN = "IN";

    public static final String SAMPLE_NAMING_MODE_SI = "SI";

    public static final String SAMPLE_NAMING_MODE_I = "I";

    public static final String SAMPLE_NAMING_MODE_NI = "NI";

    public static final String SAMPLE_NAMING_MODE_NSI = "NSI";

    public static final String SAMPLE_NAMING_MODE_N = "N";

    public static final Pattern sampleIndexPattern = Pattern.compile("((_[0-9]{4})|([0-9]{4}_))");

    public static final String sampleIndexPrefix = "s";

    private static final DecimalFormat snFormatter = new DecimalFormat("0000");

    public static AudioFormat applyPropertiesToFormat(AudioFormat af, Map<String, Object> props) {
        return new AudioFormat(af.getEncoding(), af.getSampleRate(), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSize(), af.getFrameRate(), af.isBigEndian(), props);
    }

    public static String makeLocalSampleName(Integer sample, String mode) {
        if (mode.equals(SAMPLE_NAMING_MODE_SI)) return sampleIndexPrefix + snFormatter.format(sample); else if (mode.equals(SAMPLE_NAMING_MODE_I)) return snFormatter.format(sample); else throw new IllegalArgumentException("illegal format");
    }

    public static boolean isLegalAudio(String name) {
        return isLegalAudioExtension(ZUtilities.getExtension(name));
    }

    private static DecimalFormat sizeFormatter = new DecimalFormat(".##");

    public static String getFormattedSize(double bytes) {
        if (bytes < 1024) return (int) bytes + " bytes"; else {
            if (bytes < 1048576) return sizeFormatter.format(bytes / 1024) + " Kb"; else return sizeFormatter.format(bytes / 1048576) + " MB";
        }
    }

    public static boolean isLegalAudioExtension(String ext) {
        AudioFileFormat.Type[] types = ZAudioSystem.getAudioTypes();
        for (int i = 0; i < types.length; i++) if (types[i].getExtension().toLowerCase().equals(ext.toLowerCase())) return true;
        return false;
    }

    public static String getLegalAudioExtensionsString() {
        return getLegalAudioExtensionsString(",");
    }

    public static List filterLegalAudioFiles(final List files) {
        final List legalFiles = new ArrayList();
        File f;
        for (Iterator i = files.iterator(); i.hasNext(); ) {
            f = (File) i.next();
            if (isLegalAudio(f.getName())) legalFiles.add(f);
        }
        return legalFiles;
    }

    public static String getLegalAudioExtensionsString(String sep) {
        String outStr = "";
        AudioFileFormat.Type[] types = ZAudioSystem.getAudioTypes();
        for (int i = 0; i < types.length; i++) if (outStr.equals("")) outStr = types[i].getExtension(); else outStr += sep + " " + types[i].getExtension();
        return outStr;
    }

    public static String makeLocalSampleName(Integer sample, String name, String mode) {
        name = ZUtilities.getExternalName(name);
        if (mode.equals(SAMPLE_NAMING_MODE_N)) return name; else if (mode.equals(SAMPLE_NAMING_MODE_SIN)) return sampleIndexPrefix + snFormatter.format(sample) + ZUtilities.STRING_FIELD_SEPERATOR + name; else if (mode.equals(SAMPLE_NAMING_MODE_NI)) return name + ZUtilities.STRING_FIELD_SEPERATOR + snFormatter.format(sample); else if (mode.equals(SAMPLE_NAMING_MODE_NSI)) return name + ZUtilities.STRING_FIELD_SEPERATOR + sampleIndexPrefix + snFormatter.format(sample); else if (mode.equals(SAMPLE_NAMING_MODE_IN)) return snFormatter.format(sample) + ZUtilities.STRING_FIELD_SEPERATOR + name; else return makeLocalSampleName(sample, mode);
    }

    public static int getAudioTypeIndexForExtension(String ext) {
        AudioFileFormat.Type[] types = ZAudioSystem.getAudioTypes();
        for (int i = 0; i < types.length; i++) if (types[i].getExtension().equals(ext)) return i;
        return -1;
    }

    public static AudioFileFormat.Type getAudioTypeForExtension(String ext) {
        AudioFileFormat.Type[] types = ZAudioSystem.getAudioTypes();
        for (int i = 0; i < types.length; i++) if (types[i].getExtension().equals(ext)) return types[i];
        return null;
    }

    public interface AudioSampleLoop {

        int LOOP_FORWARD = 0;

        int LOOP_ALTERNATING = 1;

        int LOOP_BACKWARD = 2;

        int getLoopStart();

        int getLoopEnd();

        int getLoopControl();
    }

    public static AiffMARKChunk.Marker findMarker(AiffMARKChunk mark, int id) {
        AiffMARKChunk.Marker[] markers = mark.getMarkers();
        for (int i = 0; i < markers.length; i++) {
            if (markers[i].getID() == id) return markers[i];
        }
        return null;
    }

    public static Map<String, Object> getChunkPropertiesMap(AbstractAudioChunk[] chunks) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < chunks.length; i++) if (chunks[i].getAudioFormatPropertyKey() != null) map.put(chunks[i].getAudioFormatPropertyKey(), chunks[i]);
        return map;
    }

    public static AbstractAudioChunk[] getSMDIHeaderChunks(final SmdiSampleHeader hdr, AudioFileFormat.Type type) {
        if (type.equals(AudioFileFormat.Type.WAVE)) {
            WaveSmplChunk smpl = new WaveSmplChunk() {

                public int getManufacturer() {
                    return 0;
                }

                public int getProduct() {
                    return 0;
                }

                public int getSamplePeriod() {
                    return hdr.getPeriodInNS();
                }

                public int getMidiUnityNote() {
                    return 60;
                }

                public int getMidiPitchFraction() {
                    return 0;
                }

                public int getSMPTEFormat() {
                    return WaveSmplChunk.SMPTE_FORMAT_NONE;
                }

                public int getSMPTEOffset() {
                    return 0;
                }

                public int getNumSampleLoops() {
                    return 1;
                }

                public WaveSmplChunk.Loop[] getSampleLoops() {
                    return new WaveSmplChunk.Loop[] { new WaveSmplChunk.Loop() {

                        public int getIdentifier() {
                            return 0;
                        }

                        public int getType() {
                            return 0;
                        }

                        public int getStart() {
                            return hdr.getLoopStart();
                        }

                        public int getEnd() {
                            return hdr.getLoopEnd();
                        }

                        public int getFraction() {
                            return 0;
                        }

                        public int getPlayCount() {
                            return 0;
                        }
                    } };
                }

                public byte[] getSamplerData() {
                    return new byte[0];
                }

                public String getAudioFormatPropertyKey() {
                    return WaveSmplChunk.AUDIO_FORMAT_PROPERTIES_KEY;
                }
            };
            return new AbstractAudioChunk[] { smpl };
        }
        if (type.equals(AudioFileFormat.Type.AIFF) || type.equals(AudioFileFormat.Type.AIFC)) {
            AiffMARKChunk mark = new AiffMARKChunk() {

                public int getNumMarkers() {
                    return 2;
                }

                Marker[] markers = new Marker[] { new Marker() {

                    public int getID() {
                        return 1;
                    }

                    public int getPosition() {
                        return hdr.getLoopStart();
                    }

                    public String getName() {
                        return "begin loop";
                    }
                }, new Marker() {

                    public int getID() {
                        return 2;
                    }

                    public int getPosition() {
                        return hdr.getLoopEnd();
                    }

                    public String getName() {
                        return "end loop";
                    }
                } };

                public AiffMARKChunk.Marker[] getMarkers() {
                    return markers;
                }

                public String getAudioFormatPropertyKey() {
                    return AiffMARKChunk.AUDIO_FORMAT_PROPERTIES_KEY;
                }
            };
            AiffINSTChunk inst = new AiffINSTChunk() {

                public int getBaseNote() {
                    return 60;
                }

                public int getDetune() {
                    return 0;
                }

                public int getLowNote() {
                    return 0;
                }

                public int getHighNote() {
                    return 127;
                }

                public int getlowVelocity() {
                    return 1;
                }

                public int getHighVelocity() {
                    return 127;
                }

                public int getGain() {
                    return 0;
                }

                Loop sustainLoop = new Loop() {

                    public int getPlayMode() {
                        return Loop.PLAY_MODE_FORWARD_LOOPING;
                    }

                    public int getBeginMarkerId() {
                        return 1;
                    }

                    public int getEndMarkerId() {
                        return 2;
                    }
                };

                public AiffINSTChunk.Loop getSustainLoop() {
                    return sustainLoop;
                }

                public AiffINSTChunk.Loop getReleaseLoop() {
                    return sustainLoop;
                }

                public String getAudioFormatPropertyKey() {
                    return AiffINSTChunk.AUDIO_FORMAT_PROPERTIES_KEY;
                }
            };
            return new AbstractAudioChunk[] { mark, inst };
        } else return new AbstractAudioChunk[0];
    }

    public static AudioSampleLoop getFirstLoop(AudioFormat f, final int lengthInFrames) {
        Map props = f.properties();
        Object smpl = props.get(WaveSmplChunk.AUDIO_FORMAT_PROPERTIES_KEY);
        if (smpl instanceof WaveSmplChunk) {
            WaveSmplChunk s = (WaveSmplChunk) smpl;
            if (s.getNumSampleLoops() > 0) {
                WaveSmplChunk.Loop[] loops = s.getSampleLoops();
                final int start = loops[0].getStart();
                final int end = loops[0].getEnd();
                final int control = WaveSmplChunk.Loop.TYPE_LOOP_FORWARD;
                return new AudioSampleLoop() {

                    public int getLoopStart() {
                        return start;
                    }

                    public int getLoopEnd() {
                        return end;
                    }

                    public int getLoopControl() {
                        return control;
                    }
                };
            }
        } else {
            Object inst = props.get(AiffINSTChunk.AUDIO_FORMAT_PROPERTIES_KEY);
            Object mark = props.get(AiffMARKChunk.AUDIO_FORMAT_PROPERTIES_KEY);
            if (inst instanceof AiffINSTChunk && mark instanceof AiffMARKChunk) {
                AiffINSTChunk i = (AiffINSTChunk) inst;
                AiffMARKChunk m = (AiffMARKChunk) mark;
                AiffINSTChunk.Loop loop = i.getSustainLoop();
                final AiffMARKChunk.Marker begin = findMarker(m, loop.getBeginMarkerId());
                final AiffMARKChunk.Marker end = findMarker(m, loop.getEndMarkerId());
                final int control;
                if (loop.getPlayMode() == AiffINSTChunk.Loop.PLAY_MODE_BACKWARD_LOOPING) control = AudioSampleLoop.LOOP_BACKWARD; else control = AudioSampleLoop.LOOP_FORWARD;
                if (begin != null && end != null) {
                    return new AudioSampleLoop() {

                        public int getLoopStart() {
                            return begin.getPosition();
                        }

                        public int getLoopEnd() {
                            return end.getPosition();
                        }

                        public int getLoopControl() {
                            return control;
                        }
                    };
                }
            }
        }
        return new AudioSampleLoop() {

            public int getLoopStart() {
                return 0;
            }

            public int getLoopEnd() {
                return 0;
            }

            public int getLoopControl() {
                return 0x7F;
            }
        };
    }
}
