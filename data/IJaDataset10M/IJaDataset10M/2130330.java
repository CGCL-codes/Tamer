package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sourceforge.plantuml.preproc.Defines;

@Deprecated
public class DirWatcher {

    private final File dir;

    private final Option option;

    private final String pattern;

    private final Map<File, FileWatcher> modifieds = new HashMap<File, FileWatcher>();

    public DirWatcher(File dir, Option option, String pattern) {
        this.dir = dir;
        this.option = option;
        this.pattern = pattern;
    }

    public List<GeneratedImage> buildCreatedFiles() throws IOException, InterruptedException {
        boolean error = false;
        final List<GeneratedImage> result = new ArrayList<GeneratedImage>();
        for (File f : dir.listFiles()) {
            if (error) {
                continue;
            }
            if (f.isFile() == false) {
                continue;
            }
            if (fileToProcess(f.getName()) == false) {
                continue;
            }
            final FileWatcher watcher = modifieds.get(f);
            if (watcher == null || watcher.hasChanged()) {
                final SourceFileReader sourceFileReader = new SourceFileReader(new Defines(), f, option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
                final Set<File> files = new HashSet<File>(sourceFileReader.getIncludedFiles());
                files.add(f);
                for (GeneratedImage g : sourceFileReader.getGeneratedImages()) {
                    result.add(g);
                    if (OptionFlags.getInstance().isFailOnError() && g.isError()) {
                        error = true;
                    }
                }
                modifieds.put(f, new FileWatcher(files));
            }
        }
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

    public File getErrorFile() throws IOException, InterruptedException {
        for (File f : dir.listFiles()) {
            if (f.isFile() == false) {
                continue;
            }
            if (fileToProcess(f.getName()) == false) {
                continue;
            }
            final FileWatcher watcher = modifieds.get(f);
            if (watcher == null || watcher.hasChanged()) {
                final SourceFileReader sourceFileReader = new SourceFileReader(new Defines(), f, option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
                if (sourceFileReader.hasError()) {
                    return f;
                }
            }
        }
        return null;
    }

    private boolean fileToProcess(String name) {
        return name.matches(pattern);
    }

    public final File getDir() {
        return dir;
    }
}
