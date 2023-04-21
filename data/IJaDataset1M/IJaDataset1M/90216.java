package net.sf.cotta.acceptance;

import java.util.ArrayList;
import java.util.List;
import net.sf.cotta.TDirectory;
import net.sf.cotta.TFile;
import net.sf.cotta.TIoException;

public class BehaviourCollector {

    private TDirectory directory;

    private String packageNamePrefix;

    public BehaviourCollector(TDirectory directory, String packageName) {
        this.directory = directory;
        this.packageNamePrefix = packageName.length() == 0 ? "" : packageName + ".";
    }

    public List collectNames() throws TIoException {
        ArrayList result = new ArrayList();
        collectClasses(result);
        collectSubDirectories(result);
        return result;
    }

    private void collectClasses(ArrayList result) throws TIoException {
        TFile[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            TFile file = files[i];
            if (looksLikeBehaviourClassFile(file)) {
                result.add(fullClassName(shortClassName(file.name())));
            }
        }
    }

    private void collectSubDirectories(List result) throws TIoException {
        TDirectory[] directories = directory.listDirs();
        for (int i = 0; i < directories.length; i++) {
            TDirectory directory = directories[i];
            BehaviourCollector behaviourCollector = new BehaviourCollector(directory, packageNamePrefix + directory.name());
            result.addAll(behaviourCollector.collectNames());
        }
    }

    private boolean looksLikeBehaviourClassFile(TFile file) {
        return file.name().endsWith("Behaviour.class");
    }

    private String fullClassName(String shortClassName) {
        return new StringBuffer(packageNamePrefix).append(shortClassName).toString();
    }

    private String shortClassName(String fileName) {
        return fileName.substring(0, fileName.length() - ".class".length());
    }
}
