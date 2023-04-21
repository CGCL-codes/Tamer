package i;

import data.sfxinfo_t;

public interface DoomSoundInterface {

    public void I_InitSound();

    public void I_UpdateSound();

    public void I_SubmitSound();

    public void I_ShutdownSound();

    void I_SetChannels();

    public int I_GetSfxLumpNum(sfxinfo_t sfxinfo);

    public int I_StartSound(int id, int vol, int sep, int pitch, int priority);

    public void I_StopSound(int handle);

    public boolean I_SoundIsPlaying(int handle);

    public void I_UpdateSoundParams(int handle, int vol, int sep, int pitch);

    public void I_InitMusic();

    public void I_ShutdownMusic();

    public void I_SetMusicVolume(int volume);

    public void I_PauseSong(int handle);

    public void I_ResumeSong(int handle);

    public int I_RegisterSong(byte[] data);

    public void I_PlaySong(int handle, int looping);

    public void I_StopSong(int handle);

    public void I_UnRegisterSong(int handle);
}
