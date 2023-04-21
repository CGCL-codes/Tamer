package de.fu.tracebook.core.data;

/**
 * A factory for the DataStorage. It returns an implementation of IDataStorage.
 */
public final class StorageFactory {

    private static IBugManager bugManagerInstance;

    private static IDataStorage instance;

    /**
     * @return Valid instance of IBugManager
     */
    public static IBugManager getBugManager() {
        if (bugManagerInstance == null) bugManagerInstance = new NewBugManager();
        return bugManagerInstance;
    }

    /**
     * Return an implementation of IDataMedia.
     * 
     * @param path
     *            The path of the directory where the media file is.
     * @param name
     *            The name of the media file.
     * @return The IDataMedia object.
     */
    public static IDataMedia getMediaObject(String path, String name) {
        return new NewMedia(path, name);
    }

    /**
     * @return Valid instance of IDataStorage
     */
    public static IDataStorage getStorage() {
        if (instance == null) instance = new NewStorage();
        return instance;
    }

    private StorageFactory() {
    }
}
