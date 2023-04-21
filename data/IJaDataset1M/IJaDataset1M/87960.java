package nl.huub.van.amelsvoort.sound.joal;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import net.java.games.joal.AL;
import net.java.games.joal.ALC;
import net.java.games.joal.ALException;
import net.java.games.joal.ALFactory;
import net.java.games.joal.eax.EAX;
import net.java.games.joal.eax.EAXFactory;
import net.java.games.joal.util.ALut;
import nl.huub.van.amelsvoort.Defines;
import nl.huub.van.amelsvoort.Globals;
import nl.huub.van.amelsvoort.game.*;
import nl.huub.van.amelsvoort.qcommon.*;
import nl.huub.van.amelsvoort.sound.*;
import nl.huub.van.amelsvoort.spel.HuubModelStatus;
import nl.huub.van.amelsvoort.spel.SpelBasis;
import nl.huub.van.amelsvoort.util.Lib;
import nl.huub.van.amelsvoort.util.Vargs;

/**
 * JOALSoundImpl
 */
public final class JOALSoundImpl implements Sound {

    static {
        S.register(new JOALSoundImpl());
    }

    ;

    static AL al;

    static ALC alc;

    static EAX eax;

    cvar_t s_volume;

    private int[] buffers = new int[MAX_SFX + STREAM_QUEUE];

    private boolean initialized;

    private JOALSoundImpl() {
        this.initialized = false;
    }

    public boolean Init() {
        try {
            if (!initialized) {
                ALut.alutInit();
                initialized = true;
            }
            al = ALFactory.getAL();
            alc = ALFactory.getALC();
            checkError();
            initOpenALExtensions();
        } catch (ALException e) {
            Com.Printf(e.getMessage() + '\n');
            return false;
        } catch (Throwable e) {
            Com.Printf(e.toString() + '\n');
            return false;
        }
        s_volume = Cvar.Get("s_volume", "0.7", Defines.CVAR_ARCHIVE);
        al.alGenBuffers(buffers.length, buffers, 0);
        int count = Channel.init(al, buffers);
        Com.Printf("... using " + count + " channels\n");
        al.alDistanceModel(AL.AL_INVERSE_DISTANCE_CLAMPED);
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
        Com.Println("JOAL geluid frequentie : 44100Hz");
        StopAllSounds();
        Com.Println("------------------------------------");
        return true;
    }

    private void initOpenALExtensions() {
        if (al.alIsExtensionPresent("EAX2.0")) {
            try {
                eax = EAXFactory.getEAX();
                Com.Println("... gebruik EAX2.0");
            } catch (Throwable e) {
                Com.Println(e.getMessage());
                Com.Println("... EAX2.0 niet klaar");
                eax = null;
            }
        } else {
            Com.Println("... EAX2.0 niet gevonden (JOAL)");
            eax = null;
        }
    }

    private ByteBuffer sfxDataBuffer = Lib.newByteBuffer(2 * 1024 * 1024);

    private void initBuffer(byte[] samples, int bufferId, int freq) {
        ByteBuffer data = sfxDataBuffer.slice();
        data.put(samples).flip();
        al.alBufferData(buffers[bufferId], AL.AL_FORMAT_MONO16, data, data.limit(), freq);
    }

    private void checkError() {
        Com.DPrintf("Geluid (AL) fout : " + alErrorString() + '\n');
    }

    private String alErrorString() {
        int error;
        String message = "";
        if ((error = al.alGetError()) != AL.AL_NO_ERROR) {
            switch(error) {
                case AL.AL_INVALID_OPERATION:
                    message = "invalid operation";
                    break;
                case AL.AL_INVALID_VALUE:
                    message = "invalid value";
                    break;
                case AL.AL_INVALID_ENUM:
                    message = "invalid enum";
                    break;
                case AL.AL_INVALID_NAME:
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
        al.alDeleteBuffers(buffers.length, buffers, 0);
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
        PlaySound.allocate(origin, entnum, entchannel, buffers[sfx.bufferId], fvol, attenuation, timeofs);
    }

    private float[] listenerOrigin = { 0, 0, 0 };

    private float[] listenerOrientation = { 0, 0, 0, 0, 0, 0 };

    private IntBuffer eaxEnv = Lib.newIntBuffer(1);

    private int currentEnv = -1;

    private boolean changeEnv = true;

    public void Update(float[] origin, float[] forward, float[] right, float[] up) {
        Channel.convertVector(origin, listenerOrigin);
        al.alListenerfv(AL.AL_POSITION, listenerOrigin, 0);
        Channel.convertOrientation(forward, up, listenerOrientation);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOrientation, 0);
        al.alListenerf(AL.AL_GAIN, s_volume.value);
        if (eax != null) {
            if (currentEnv == -1) {
                eaxEnv.put(0, EAX.EAX_ENVIRONMENT_UNDERWATER);
                eax.EAXSet(EAX.LISTENER, EAX.DSPROPERTY_EAXLISTENER_ENVIRONMENT | EAX.DSPROPERTY_EAXLISTENER_DEFERRED, 0, eaxEnv, 4);
                changeEnv = true;
            }
            if ((SpelBasis.gi.pointcontents.pointcontents(origin) & Defines.MASK_WATER) != 0) {
                changeEnv = currentEnv != EAX.EAX_ENVIRONMENT_UNDERWATER;
                currentEnv = EAX.EAX_ENVIRONMENT_UNDERWATER;
            } else {
                changeEnv = currentEnv != EAX.EAX_ENVIRONMENT_GENERIC;
                currentEnv = EAX.EAX_ENVIRONMENT_GENERIC;
            }
            if (changeEnv) {
                eaxEnv.put(0, currentEnv);
                eax.EAXSet(EAX.LISTENER, EAX.DSPROPERTY_EAXLISTENER_ENVIRONMENT | EAX.DSPROPERTY_EAXLISTENER_DEFERRED, 0, eaxEnv, 4);
            }
        }
        Channel.addLoopSounds();
        Channel.addPlaySounds();
        Channel.playAllSounds(listenerOrigin);
    }

    public void StopAllSounds() {
        al.alListenerf(AL.AL_GAIN, 0);
        PlaySound.reset();
        Channel.reset();
    }

    public String getName() {
        return "joal";
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
        if (model.equalsIgnoreCase("vrouw")) {
            String femaleFilename = "spelers/vrouwen/" + base.substring(1);
            if (FS.FileLength("geluid/" + femaleFilename) > 0) {
                return AliasName(sexedFilename, femaleFilename);
            }
        }
        String maleFilename = "spelers/mannen/" + base.substring(1);
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
        sfx_t sfx = RegisterSound(sound);
        if (sfx == null) {
            Com.Printf("S_StartLocalSound: can't cache " + sound + "\n");
            return;
        }
        StartSound(null, Globals.cl.playernum + 1, 0, sfx, 1, 1, 0.0f);
    }

    private ShortBuffer streamBuffer = sfxDataBuffer.slice().order(ByteOrder.BIG_ENDIAN).asShortBuffer();

    public void RawSamples(int samples, int rate, int width, int channels, ByteBuffer data) {
        int format;
        if (channels == 2) {
            format = (width == 2) ? AL.AL_FORMAT_STEREO16 : AL.AL_FORMAT_STEREO8;
        } else {
            format = (width == 2) ? AL.AL_FORMAT_MONO16 : AL.AL_FORMAT_MONO8;
        }
        if (format == AL.AL_FORMAT_MONO8) {
            ShortBuffer sampleData = streamBuffer;
            int value;
            for (int i = 0; i < samples; i++) {
                value = (data.get(i) & 0xFF) - 128;
                sampleData.put(i, (short) value);
            }
            format = AL.AL_FORMAT_MONO16;
            width = 2;
            data = sfxDataBuffer.slice();
        }
        Channel.updateStream(data, samples * channels * width, format, rate);
    }

    public void disableStreaming() {
        Channel.disableStreaming();
    }

    void Play() {
        int i = 1;
        String name;
        while (i < Cmd.Argc()) {
            name = new String(Cmd.Argv(i));
            if (name.indexOf('.') == -1) {
                name += ".wav";
            }
            RegisterSound(name);
            StartLocalSound(name);
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
