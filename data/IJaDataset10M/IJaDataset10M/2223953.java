package dplayer.scanner;

public class DirectorySettings {

    private String mAbsolutePath;

    private boolean mDefaultFlat;

    public DirectorySettings(final String absolutePath) {
        assert absolutePath != null && absolutePath.length() > 0;
        mAbsolutePath = absolutePath;
    }

    private DirectorySettings() {
    }

    public boolean isDefaultFlat() {
        return mDefaultFlat;
    }

    public void setDefaultFlat(final boolean defaultFlat) {
        mDefaultFlat = defaultFlat;
    }

    public String getAbsolutePath() {
        return mAbsolutePath;
    }

    public static DirectorySettings createEmptySettings() {
        return new DirectorySettings();
    }
}
