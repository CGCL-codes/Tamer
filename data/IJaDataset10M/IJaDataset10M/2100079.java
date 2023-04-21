package net.sf.sail.emf.launch;

import java.io.File;

public interface ConsoleLogService {

    public void okToDelete();

    public void setBundlePoster(BundlePoster poster);

    public File getLocalFile();

    public void saveSockEntry(String entry);
}
