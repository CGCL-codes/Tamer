package net.sf.ulmac.core.managers;

import java.util.ArrayList;
import java.util.List;
import net.sf.ulmac.core.factories.MusicLibraryFactory;
import net.sf.ulmac.core.libraries.IMusicLibrary;
import net.sf.ulmac.core.types.MusicLibraryType;
import net.sf.ulmac.core.utils.ILogDevice;

public class LibraryUpdaterManager {

    public LibraryUpdaterManager() {
    }

    public void addLogDevice(ILogDevice logDevice) {
        MusicLibraryFactory.getInstance().addLogDevice(logDevice);
    }

    public List<IMusicLibrary> getMusicLibraries() {
        List<IMusicLibrary> musicLibraries = new ArrayList<IMusicLibrary>();
        for (MusicLibraryType type : MusicLibraryType.values()) {
            IMusicLibrary library = MusicLibraryFactory.getInstance().createMusicLibrary(type);
            if (library != null) {
                musicLibraries.add(library);
            }
        }
        return musicLibraries;
    }

    public void removeLogDevices() {
        MusicLibraryFactory.getInstance().removeLogDevices();
    }
}
