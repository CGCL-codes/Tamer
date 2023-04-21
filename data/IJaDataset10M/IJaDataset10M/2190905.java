package s;

public class DummyMusic implements IMusic {

    @Override
    public void InitMusic() {
    }

    @Override
    public void ShutdownMusic() {
    }

    @Override
    public void SetMusicVolume(int volume) {
    }

    @Override
    public void PauseSong(int handle) {
    }

    @Override
    public void ResumeSong(int handle) {
    }

    @Override
    public int RegisterSong(byte[] data) {
        return 0;
    }

    @Override
    public void PlaySong(int handle, boolean looping) {
    }

    @Override
    public void StopSong(int handle) {
    }

    @Override
    public void UnRegisterSong(int handle) {
    }
}
