package de.fzi.wikipipes.example;

import de.fzi.wikipipes.IWikiRepository;
import de.fzi.wikipipes.impl.filesystem.WikiRepositoryFS;
import de.fzi.wikipipes.impl.mediawiki.WikiRepositoryMW;
import de.fzi.wikipipes.util.SyncManager;

public class DownloadOntoworld {

    public static void main(String[] args) {
        String sourceURL = "http://ontoworld.org/wiki";
        IWikiRepository source = new WikiRepositoryMW(sourceURL, null, null, "english");
        IWikiRepository target = new WikiRepositoryFS("./target/ontoworld");
        System.out.println("Start copying...");
        SyncManager.copyWiki(source, target);
        System.out.println("Done.");
    }
}
