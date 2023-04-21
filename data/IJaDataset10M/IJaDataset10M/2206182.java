package Spotify;

public class PlayListFolder extends PlayListElement {

    private PlayListFolder(int nativePtr) {
        super(nativePtr);
    }

    public PlayListElement GetPlayListElement() {
        return (PlayListElement) this;
    }
}
