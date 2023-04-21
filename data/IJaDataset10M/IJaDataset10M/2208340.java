package mp3.busqueda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import mp3.extras.Config;
import mp3.services.Reporter;
import mp3.services.ServiceSetter;

/**
 *
 * @author user
 */
public class SearchingMethodCachedFile extends SearchingMethod {

    private static List<File> cachedFiles = null;

    private static File cached = null;

    private static final Object syncObject = new Object();

    private boolean useMp3Extension = true;

    private File currentDir;

    private int numberFiles;

    public SearchingMethodCachedFile(File dir, List<File> v, FileFilter ff) throws FileNotFoundException, IOException {
        super(dir, v, ff);
        String name = Config.getConfig().get("CacheFile");
        if (name == null || name.isEmpty()) throw new FileNotFoundException(java.util.ResourceBundle.getBundle("Bundle").getString("SearchingMethodCachedFile.Error.CacheNotFound"));
        synchronized (syncObject) {
            File precached = new File(name);
            if (!precached.exists()) throw new FileNotFoundException("Cache file not found");
            if (cached == null || !cached.getAbsolutePath().equals(precached.getAbsolutePath())) {
                cached = precached;
                cachedFiles = null;
            }
        }
        try {
            useMp3Extension = Boolean.parseBoolean(Config.getConfig().get("UseMp3Extension"));
        } catch (Exception ex) {
            useMp3Extension = Config.defaultMp3Extension;
        }
    }

    @Override
    public void doSearch(File dir, List<File> v, FileFilter ff) {
        List<File> copyCachedFiles;
        String line;
        File currentFile;
        numberFiles = 0;
        ((Reporter) ServiceSetter.getServiceSetter().getServiceByName(Reporter.class.getName())).addCount("cach");
        synchronized (syncObject) {
            if (cachedFiles == null) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader(cached));
                    cachedFiles = new ArrayList<File>();
                    while ((line = in.readLine()) != null) {
                        File f = new File(line);
                        if (!f.isAbsolute()) {
                            f = new File(cached.getParentFile().getCanonicalPath() + "/" + line);
                        }
                        f = f.getCanonicalFile();
                        cachedFiles.add(f);
                    }
                    Collections.sort(cachedFiles);
                } catch (IOException ex) {
                    Logger.getLogger(SearchingMethodCachedFile.class.getName()).log(Level.SEVERE, null, ex);
                    wakeup();
                    return;
                }
            }
            copyCachedFiles = new ArrayList<File>(cachedFiles.size());
            Collections.copy(copyCachedFiles, cachedFiles);
        }
        try {
            File canonicalDir = dir.getCanonicalFile();
            int index = binarySearch(copyCachedFiles, canonicalDir);
            while (index < copyCachedFiles.size() && copyCachedFiles.get(index).getAbsolutePath().startsWith(canonicalDir.getAbsolutePath())) {
                currentFile = copyCachedFiles.get(index);
                currentDir = currentFile.getParentFile();
                numberFiles++;
                if (ff.accept(currentFile)) {
                    try {
                        if (useMp3Extension) v.add(ExtensionCacheHandler.getExtensionCacheHandler().cacheGetOrCreate(currentFile, true, false)); else v.add(currentFile);
                    } catch (UnsupportedAudioFileException ex) {
                        Logger.getLogger(SearchingMethodCachedFile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                index++;
            }
            wakeup();
        } catch (IOException ex) {
            Logger.getLogger(SearchingMethodCachedFile.class.getName()).log(Level.SEVERE, null, ex);
            wakeup();
        }
    }

    private int binarySearch(List<File> l, File dir) {
        int low = 0;
        int high = l.size();
        String sdir = dir.getAbsolutePath();
        while (low < high) {
            int mid = (low + high) / 2;
            if (l.get(mid).getAbsolutePath().compareTo(sdir) < 0) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    @Override
    public File getCurrentSearchingDir() {
        return currentDir;
    }

    @Override
    public int getNumberOfFilesCounted() {
        return numberFiles;
    }

    @Override
    public void releaseStaticResources() {
        cached = null;
        cachedFiles = null;
    }

    @Override
    public boolean syncedDelegateNeeded() {
        return false;
    }
}
