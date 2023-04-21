package org.deft.repository.xfsr.processor;

import org.apache.log4j.Logger;
import org.deft.repository.fragment.Folder;
import org.deft.repository.xfsr.XmlFileSystemRepository;

public class FolderProcessor extends FragmentProcessor {

    private Logger logger = Logger.getLogger(FolderProcessor.class);

    public FolderProcessor(Folder folder, XmlFileSystemRepository rep) {
        super(folder, rep);
    }

    public Folder getFolder() {
        return (Folder) fragment;
    }

    public void createFileRepresentation(String filename) {
    }

    public boolean deleteFileRepresentation() {
        return true;
    }

    public void register() {
        super.register();
        fragmentManager.registerFolder(getFolder());
    }
}
