package nl.huub.van.amelsvoort.sound.lwjgl;

import nl.huub.van.amelsvoort.Defines;
import nl.huub.van.amelsvoort.Globals;
import nl.huub.van.amelsvoort.game.Cmd;
import nl.huub.van.amelsvoort.game.cvar_t;
import nl.huub.van.amelsvoort.util.Lib;
import nl.huub.van.amelsvoort.util.Vargs;
import java.nio.*;
import nl.huub.van.amelsvoort.qcommon.Com;
import nl.huub.van.amelsvoort.qcommon.Cvar;
import nl.huub.van.amelsvoort.qcommon.FS;
import nl.huub.van.amelsvoort.qcommon.xcommand_t;
import nl.huub.van.amelsvoort.sound.S;
import nl.huub.van.amelsvoort.sound.Sound;
import nl.huub.van.amelsvoort.sound.WaveLoader;
import nl.huub.van.amelsvoort.sound.sfx_t;
import nl.huub.van.amelsvoort.sound.sfxcache_t;
import nl.huub.van.amelsvoort.spel.HuubModelStatus;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.*;

/**
 * LWJGLSoundImpl
 * 
 * @author dsanders/cwei
 */
public final class LWJGLSoundImpl implements Sound {

    static {
        S.register(new LWJGLSoundImpl());
    }

    ;

    private cvar_t s_volume;

    private IntBuffer buffers = Lib.newIntBuffer(MAX_SFX + STREAM_QUEUE);

    private LWJGLSoundImpl() {
    }

    public boolean Init() {
        try {
            initOpenAL();
            checkError();
        } catch (OpenALException e) {
            Com.Printf(e.getMessage() + '\n');
            return false;
        } catch (Exception e) {
            Com.DPrintf(e.getMessage() + '\n');
            return false;
        }
        s_volume = Cvar.Get("s_volume", "0.7", Defines.CVAR_ARCHIVE);
        AL10.alGenBuffers(buffers);
        int count = Channel.init(buffers);
        Com.Printf("... using " + count + " channels\n");
        AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE_CLAMPED);
        Cmd.AddCommand("play", new xcommand_t() {

            public void execute() {
                Play();
            }
        });
        Cmd.AddCommand("stopsound", new xcommand_t() {

            public void execute() {
                StopAllSounds();
            }
        });
        Cmd.AddCommand("soundlist", new xcommand_t() {

            public void execute() {
                SoundList();
            }
        });
        Cmd.AddCommand("soundinfo", new xcommand_t() {

            public void execute() {
                SoundInfo_f();
            }
        });
        num_sfx = 0;
        Com.Printf("Geluid frequentie : 44100Hz\n");
        StopAllSounds();
        Com.Printf("------------------------------------\n");
        return true;
    }

    private void initOpenAL() throws OpenALException {
        try {
            AL.create();
        } catch (LWJGLException e) {
            throw new OpenALException(e);
        }
        String deviceName = null;
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            deviceName = "DirectSound3D";
        }
        String defaultSpecifier = ALC10.alcGetString(AL.getDevice(), ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
        Com.Printf(os + " using " + ((deviceName == null) ? defaultSpecifier : deviceName) + '\n');
        if (ALC10.alcGetError(AL.getDevice()) != ALC10.ALC_NO_ERROR) {
            Com.DPrintf("Error with SoundDevice");
        }
    }

    void exitOpenAL() {
        AL.destroy();
    }

    private ByteBuffer sfxDataBuffer = Lib.newByteBuffer(2 * 1024 * 1024);

    private void initBuffer(byte[] samples, int bufferId, int freq) {
        ByteBuffer data = sfxDataBuffer.slice();
        data.put(samples).flip();
        AL10.alBufferData(buffers.get(bufferId), AL10.AL_FORMAT_MONO16, data, freq);
    }

    private void checkError() {
        Com.DPrintf("AL Error: " + alErrorString() + '\n');
    }

    private String alErrorString() {
        int error;
        String message = "";
        if ((error = AL10.alGetError()) != AL10.AL_NO_ERROR) {
            switch(error) {
                case AL10.AL_INVALID_OPERATION:
                    message = "invalid operation";
                    break;
                case AL10.AL_INVALID_VALUE:
                    message = "invalid value";
                    break;
                case AL10.AL_INVALID_ENUM:
                    message = "invalid enum";
                    break;
                case AL10.AL_INVALID_NAME:
                    message = "invalid name";
                    break;
                default:
                    message = "" + error;
            }
        }
        return message;
    }

    public void Shutdown() {
        StopAllSounds();
        Channel.shutdown();
        AL10.alDeleteBuffers(buffers);
        exitOpenAL();
        Cmd.RemoveCommand("play");
        Cmd.RemoveCommand("stopsound");
        Cmd.RemoveCommand("soundlist");
        Cmd.RemoveCommand("soundinfo");
        for (int i = 0; i < num_sfx; i++) {
            if (known_sfx[i].name == null) {
                continue;
            }
            known_sfx[i].clear();
        }
        num_sfx = 0;
    }

    public void StartSound(float[] origin, int entnum, int entchannel, sfx_t sfx, float fvol, float attenuation, float timeofs) {
        if (sfx == null) {
            return;
        }
        if (sfx.name.charAt(0) == '*') {
            sfx = RegisterSexedSound(Globals.cl_entities[entnum].current, sfx.name);
        }
        if (LoadSound(sfx) == null) {
            return;
        }
        if (attenuation != Defines.ATTN_STATIC) {
            attenuation *= 0.5f;
        }
        PlaySound.allocate(origin, entnum, entchannel, buffers.get(sfx.bufferId), fvol, attenuation, timeofs);
    }

    private FloatBuffer listenerOrigin = Lib.newFloatBuffer(3);

    private FloatBuffer listenerOrientation = Lib.newFloatBuffer(6);

    public void Update(float[] origin, float[] forward, float[] right, float[] up) {
        Channel.convertVector(origin, listenerOrigin);
        AL10.alListener(AL10.AL_POSITION, listenerOrigin);
        Channel.convertOrientation(forward, up, listenerOrientation);
        AL10.alListener(AL10.AL_ORIENTATION, listenerOrientation);
        AL10.alListenerf(AL10.AL_GAIN, s_volume.value);
        Channel.addLoopSounds();
        Channel.addPlaySounds();
        Channel.playAllSounds(listenerOrigin);
    }

    public void StopAllSounds() {
        AL10.alListenerf(AL10.AL_GAIN, 0);
        PlaySound.reset();
        Channel.reset();
    }

    public String getName() {
        return "lwjgl";
    }

    int s_registration_sequence;

    boolean s_registering;

    public void BeginRegistration() {
        s_registration_sequence++;
        s_registering = true;
    }

    public sfx_t RegisterSound(String name) {
        sfx_t sfx = FindName(name, true);
        sfx.registration_sequence = s_registration_sequence;
        if (!s_registering) {
            LoadSound(sfx);
        }
        return sfx;
    }

    public void EndRegistration() {
        int i;
        sfx_t sfx;
        for (i = 0; i < num_sfx; i++) {
            sfx = known_sfx[i];
            if (sfx.name == null) {
                continue;
            }
            if (sfx.registration_sequence != s_registration_sequence) {
                sfx.clear();
            }
        }
        for (i = 0; i < num_sfx; i++) {
            sfx = known_sfx[i];
            if (sfx.name == null) {
                continue;
            }
            LoadSound(sfx);
        }
        s_registering = false;
    }

    sfx_t RegisterSexedSound(HuubModelStatus ent, String base) {
        sfx_t sfx = null;
        String model = null;
        int n = Globals.CS_PLAYERSKINS + ent.number - 1;
        if (Globals.cl.configstrings[n] != null) {
            int p = Globals.cl.configstrings[n].indexOf('\\');
            if (p >= 0) {
                p++;
                model = Globals.cl.configstrings[n].substring(p);
                p = model.indexOf('/');
                if (p > 0) {
                    model = model.substring(0, p);
                }
            }
        }
        if (model == null || model.length() == 0) {
            model = "mannen";
        }
        String sexedFilename = "#spelers/" + model + "/" + base.substring(1);
        sfx = FindName(sexedFilename, false);
        if (sfx != null) {
            return sfx;
        }
        if (FS.FileLength(sexedFilename.substring(1)) > 0) {
            return RegisterSound(sexedFilename);
        }
        if (model.equalsIgnoreCase("vrouwen")) {
            String femaleFilename = "speler/vrouwen/" + base.substring(1);
            if (FS.FileLength("geluid/" + femaleFilename) > 0) {
                return AliasName(sexedFilename, femaleFilename);
            }
        }
        String maleFilename = "speler/mannen/" + base.substring(1);
        return AliasName(sexedFilename, maleFilename);
    }

    static sfx_t[] known_sfx = new sfx_t[MAX_SFX];

    static {
        for (int i = 0; i < known_sfx.length; i++) {
            known_sfx[i] = new sfx_t();
        }
    }

    static int num_sfx;

    sfx_t FindName(String name, boolean create) {
        int i;
        sfx_t sfx = null;
        if (name == null) {
            Com.Error(Defines.ERR_FATAL, "S_FindName: NULL\n");
        }
        if (name.length() == 0) {
            Com.Error(Defines.ERR_FATAL, "S_FindName: empty name\n");
        }
        if (name.length() >= Defines.MAX_QPATH) {
            Com.Error(Defines.ERR_FATAL, "Sound name too long: " + name);
        }
        for (i = 0; i < num_sfx; i++) {
            if (name.equals(known_sfx[i].name)) {
                return known_sfx[i];
            }
        }
        if (!create) {
            return null;
        }
        for (i = 0; i < num_sfx; i++) {
            if (known_sfx[i].name == null) {
                break;
            }
        }
        if (i == num_sfx) {
            if (num_sfx == MAX_SFX) {
                Com.Error(Defines.ERR_FATAL, "S_FindName: out of sfx_t");
            }
            num_sfx++;
        }
        sfx = known_sfx[i];
        sfx.clear();
        sfx.name = name;
        sfx.registration_sequence = s_registration_sequence;
        sfx.bufferId = i;
        return sfx;
    }

    sfx_t AliasName(String aliasname, String truename) {
        sfx_t sfx = null;
        String s;
        int i;
        s = new String(truename);
        for (i = 0; i < num_sfx; i++) {
            if (known_sfx[i].name == null) {
                break;
            }
        }
        if (i == num_sfx) {
            if (num_sfx == MAX_SFX) {
                Com.Error(Defines.ERR_FATAL, "S_FindName: out of sfx_t");
            }
            num_sfx++;
        }
        sfx = known_sfx[i];
        sfx.clear();
        sfx.name = new String(aliasname);
        sfx.registration_sequence = s_registration_sequence;
        sfx.truename = s;
        sfx.bufferId = i;
        return sfx;
    }

    public sfxcache_t LoadSound(sfx_t s) {
        if (s.isCached) {
            return s.cache;
        }
        sfxcache_t sc = WaveLoader.LoadSound(s);
        if (sc != null) {
            initBuffer(sc.data, s.bufferId, sc.speed);
            s.isCached = true;
            s.cache.data = null;
        }
        return sc;
    }

    public void StartLocalSound(String sound) {
        sfx_t sfx;
        sfx = RegisterSound(sound);
        if (sfx == null) {
            Com.Printf("S_StartLocalSound: can't cache " + sound + "\n");
            return;
        }
        StartSound(null, Globals.cl.playernum + 1, 0, sfx, 1, 1, 0);
    }

    private ShortBuffer streamBuffer = sfxDataBuffer.slice().order(ByteOrder.BIG_ENDIAN).asShortBuffer();

    public void RawSamples(int samples, int rate, int width, int channels, ByteBuffer data) {
        int format;
        if (channels == 2) {
            format = (width == 2) ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_STEREO8;
        } else {
            format = (width == 2) ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_MONO8;
        }
        if (format == AL10.AL_FORMAT_MONO8) {
            ShortBuffer sampleData = streamBuffer;
            int value;
            for (int i = 0; i < samples; i++) {
                value = (data.get(i) & 0xFF) - 128;
                sampleData.put(i, (short) value);
            }
            format = AL10.AL_FORMAT_MONO16;
            width = 2;
            data = sfxDataBuffer.slice();
        }
        Channel.updateStream(data, samples * channels * width, format, rate);
    }

    public void disableStreaming() {
        Channel.disableStreaming();
    }

    void Play() {
        int i;
        String name;
        sfx_t sfx;
        i = 1;
        while (i < Cmd.Argc()) {
            name = new String(Cmd.Argv(i));
            if (name.indexOf('.') == -1) {
                name += ".wav";
            }
            sfx = RegisterSound(name);
            StartSound(null, Globals.cl.playernum + 1, 0, sfx, 1.0f, 1.0f, 0.0f);
            i++;
        }
    }

    void SoundList() {
        int i;
        sfx_t sfx;
        sfxcache_t sc;
        int size, total;
        total = 0;
        for (i = 0; i < num_sfx; i++) {
            sfx = known_sfx[i];
            if (sfx.registration_sequence == 0) {
                continue;
            }
            sc = sfx.cache;
            if (sc != null) {
                size = sc.length * sc.width * (sc.stereo + 1);
                total += size;
                if (sc.loopstart >= 0) {
                    Com.Printf("L");
                } else {
                    Com.Printf(" ");
                }
                Com.Printf("(%2db) %6i : %s\n", new Vargs(3).add(sc.width * 8).add(size).add(sfx.name));
            } else {
                if (sfx.name.charAt(0) == '*') {
                    Com.Printf("  placeholder : " + sfx.name + "\n");
                } else {
                    Com.Printf("  not loaded  : " + sfx.name + "\n");
                }
            }
        }
        Com.Printf("Total resident: " + total + "\n");
    }

    void SoundInfo_f() {
        Com.Printf("%5d stereo\n", new Vargs(1).add(1));
        Com.Printf("%5d samples\n", new Vargs(1).add(22050));
        Com.Printf("%5d samplebits\n", new Vargs(1).add(16));
        Com.Printf("%5d speed\n", new Vargs(1).add(44100));
    }
}
