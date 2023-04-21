package sk.MichalKatrik.Android.RadioFMUnofficial;

public class MyData {

    public LoadPlaylistTask task = null;

    public PlaylistItem plItem = null;

    public OnAirItem oaItem = null;

    public boolean playlistLoaded = false;

    public boolean onAirLoaded = false;

    public MyData() {
        plItem = new PlaylistItem();
        oaItem = new OnAirItem();
    }
}
