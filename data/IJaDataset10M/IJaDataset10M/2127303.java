package annone.local.linker;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipException;
import annone.local.Workspace;
import annone.util.AnnoneException;
import annone.util.Text;

public class WorkspaceBuilder implements Runnable {

    private final Workspace workspace;

    private final Set<ComponentRef> componentRefs;

    public WorkspaceBuilder(Workspace workspace) {
        this.workspace = workspace;
        this.componentRefs = new TreeSet<ComponentRef>();
    }

    public Set<ComponentRef> getComponentRefs() {
        return componentRefs;
    }

    public ComponentRef addComponentRef(String library, String name) {
        ComponentRef ref = new ComponentRef(library, name);
        componentRefs.add(ref);
        return ref;
    }

    @Override
    public void run() {
        try {
            List<LibraryBuilder> libraries = getLibraries();
            int ordinal = 0;
            for (LibraryBuilder ll : libraries) {
                ll.setOrdinal(++ordinal);
                ll.run();
            }
        } catch (Throwable xp) {
            throw new AnnoneException(Text.get("Build failed."), xp);
        }
    }

    private List<LibraryBuilder> getLibraries() throws ZipException, IOException {
        File[] libFiles = workspace.getLibrariesDirectory().listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".library");
            }
        });
        File[] srcFiles = workspace.getSourcesDirectory().listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        ArrayList<File> files = new ArrayList<File>();
        if (libFiles != null) files.addAll(Arrays.asList(libFiles));
        if (srcFiles != null) files.addAll(Arrays.asList(srcFiles));
        List<LibraryBuilder> libraries = new ArrayList<LibraryBuilder>();
        LibraryFactory libraryFactory = LibraryFactory.getInstance();
        for (File file : files) libraries.add(new LibraryBuilder(this, libraryFactory.getLibrary(file.toURI())));
        sortLibraries(libraries);
        return libraries;
    }

    private void sortLibraries(List<LibraryBuilder> libraries) {
        while (sortLibraries0(libraries)) ;
    }

    private boolean sortLibraries0(List<LibraryBuilder> libraries) {
        for (int i = 0; i < libraries.size(); i++) {
            LibraryBuilder library = libraries.get(i);
            if (library.getLibrary().getDepends().isEmpty()) continue;
            for (DependEntry depend : library.getLibrary().getDepends()) {
                boolean dependFound = false;
                for (int j = 0; j < libraries.size(); j++) {
                    LibraryBuilder library1 = libraries.get(j);
                    if (depend.getName().equals(library1.getLibrary().getName())) {
                        dependFound = true;
                        if (j > i) {
                            libraries.remove(i);
                            libraries.add(j, library);
                            return true;
                        }
                        break;
                    }
                }
                if (!dependFound) throw new AnnoneException(Text.get("Depend library ''{0}'' not found.", depend.getName()));
            }
        }
        return false;
    }
}
