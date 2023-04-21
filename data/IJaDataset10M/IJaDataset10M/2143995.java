package de.frostcode.visualmon.probe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.concurrent.Immutable;
import javax.swing.filechooser.FileSystemView;
import de.frostcode.visualmon.probe.JamonMonitor.JamonGetter;

/**
 * Utility facade class for probes.
 */
@Immutable
public final class Probes {

    private Probes() {
        throw new UnsupportedOperationException();
    }

    /**
   * Returns a collection of all default probes (memory, filesystem, and so on).
   * @return the default probes
   */
    public static Collection<Probe> getDefaultProbes() {
        List<Probe> probes = new ArrayList<Probe>();
        probes.add(new HeapMemory());
        probes.add(new PermGenMemory());
        probes.add(new CpuLoad());
        probes.add(new Threads());
        if (!System.getProperty("os.name").startsWith("Win")) probes.add(new SystemLoad());
        for (File file : File.listRoots()) {
            if (file.isDirectory()) probes.add(new FilesystemSpace(file));
        }
        probes.add(new FilesystemSpace(new File(System.getProperty("java.io.tmpdir"))));
        probes.add(new FilesystemSpace(FileSystemView.getFileSystemView().getHomeDirectory()));
        probes.add(new JamonMonitor(JamonMonitor.ALL_PAGES, JamonGetter.AVG, new LocalizedString() {

            @Override
            public String get(final Locale locale) {
                return ResourceBundle.getBundle("de.frostcode.visualmon.stats", locale).getString("jamon.allpages.avg");
            }
        }, false));
        probes.add(new JamonMonitor(JamonMonitor.ALL_PAGES, JamonGetter.HITS, new LocalizedString() {

            @Override
            public String get(final Locale locale) {
                return ResourceBundle.getBundle("de.frostcode.visualmon.stats", locale).getString("jamon.allpages.hits");
            }
        }, true));
        return probes;
    }
}
