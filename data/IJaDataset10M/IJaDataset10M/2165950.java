package net.sf.refactorit.netbeans.common.vfs;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.netbeans.common.projectoptions.PathUtil;
import net.sf.refactorit.vfs.FileChangeListener;
import net.sf.refactorit.vfs.FileChangeMonitor;
import net.sf.refactorit.vfs.Source;
import org.apache.log4j.Logger;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Monitors the SourcePath of a Netbeans project for changes that
 * RefactorIt should be informed of.
 *
 * @author  Juri Reinsalu
 * @author  risto
 */
public class NBFileChangeMonitor extends FileChangeMonitor {

    private static final Logger log = Logger.getLogger(NBFileChangeMonitor.class);

    org.openide.filesystems.FileChangeListener openIDEAdapter = new OpenIDEListenerAdapter();

    FileObject[] rootPaths;

    NBSourcePath sourcePath;

    private Source[] rootSourcesLastTime = null;

    private List registeredDirectories = new ArrayList(100);

    public NBFileChangeMonitor(NBSourcePath sourcePath, FileObject[] rootPaths) {
        this.sourcePath = sourcePath;
        this.rootPaths = rootPaths;
    }

    private void registerForEvents() {
        Object ideProject = sourcePath.getIdeProjectReference().get();
        if (ideProject == null) {
            return;
        }
        Set sourceDirectories = new HashSet(40);
        for (int i = 0; i < rootPaths.length; i++) {
            sourcePath.collectDirectories(rootPaths[i], sourceDirectories, PathUtil.getInstance().getIgnoreListFilter(ideProject));
        }
        Iterator i = sourceDirectories.iterator();
        while (i.hasNext()) {
            ((FileObject) i.next()).addFileChangeListener(openIDEAdapter);
        }
    }

    private void deregisterForEvents() {
        for (Iterator iterator = registeredDirectories.iterator(); iterator.hasNext(); ) {
            FileObject fileObject = (FileObject) iterator.next();
            fileObject.removeFileChangeListener(openIDEAdapter);
            iterator.remove();
        }
    }

    public void addFileChangeListener(FileChangeListener listener) {
        if (listeners.isEmpty()) {
            registerForEvents();
        }
        super.addFileChangeListener(listener);
    }

    public void removeFileChangeListener(FileChangeListener listener) {
        super.removeFileChangeListener(listener);
        if (listeners.isEmpty()) {
            deregisterForEvents();
        }
    }

    public boolean hasPossiblePendingEvents() {
        return false;
    }

    public void trigger(net.sf.refactorit.classmodel.Project project) {
        if (someFilesystemCapabilitiesChangedSinceLastTime(project)) {
            fireUnknownChangesHappened();
        }
    }

    private boolean someFilesystemCapabilitiesChangedSinceLastTime(Project project) {
        Source[] lastRootSources = rootSourcesLastTime;
        Source[] rootSources = project.getPaths().getSourcePath().getRootSources();
        rootSourcesLastTime = rootSources;
        return (!Arrays.equals(rootSources, lastRootSources)) && rootSourcesLastTime != null;
    }

    private class OpenIDEListenerAdapter implements org.openide.filesystems.FileChangeListener {

        private NBSource getSource(FileObject file) {
            final FileObject fileParent = file.getParent();
            if (fileParent != null) {
                NBSource parent = NBSource.findSource(fileParent);
                if (parent != null) {
                    return NBSource.getSource(file);
                }
            } else {
                log.warn("No parent for fileObject: " + file);
            }
            return NBSource.getSource(file);
        }

        public void fileFolderCreated(FileEvent fe) {
            Object ideProject = sourcePath.getIdeProjectReference().get();
            if (ideProject == null) {
                return;
            }
            FileObject dir = fe.getFile();
            NBSource dirSource = getSource(dir);
            if (sourcePath.shouldIterateInto(dirSource, PathUtil.getInstance().getIgnoreListFilter(ideProject))) {
                dir.addFileChangeListener(openIDEAdapter);
            }
            List list = new ArrayList();
            sourcePath.iterateDirectory(dirSource, list, PathUtil.getInstance().getIgnoreListFilter(ideProject), null);
            Iterator i = list.iterator();
            while (i.hasNext()) {
                NBFileChangeMonitor.this.fireFileCreatedEvent((Source) i.next());
            }
        }

        public void fileDataCreated(FileEvent fe) {
            final FileObject fo = fe.getFile();
            NBSource newFile = getSource(fo);
            if (sourcePath.fileAcceptedByName(newFile)) {
                NBFileChangeMonitor.this.fireFileCreatedEvent(newFile);
            }
        }

        public void fileChanged(FileEvent fe) {
            final FileObject changedFile = fe.getFile();
            NBSource file = getSource(changedFile);
            if (sourcePath.fileAcceptedByName(file)) {
                NBFileChangeMonitor.this.fireFileContentsChangedEvent(file);
            }
        }

        public void fileDeleted(FileEvent fe) {
            FileObject deletedFile = fe.getFile();
            NBSource oldFile = getSource(deletedFile);
            if (deletedFile.isData()) {
                if (sourcePath.fileAcceptedByName(oldFile)) {
                    NBFileChangeMonitor.this.fireFileDeletedEvent(oldFile);
                }
            }
        }

        public void fileRenamed(FileRenameEvent fr) {
            FileObject newFileObject = fr.getFile();
            NBSource newFile = getSource(newFileObject);
            String oldName = fr.getName() + '.' + fr.getExt();
            NBFileChangeMonitor.this.fireFileRenameEvent(newFile, oldName);
        }

        public void fileAttributeChanged(FileAttributeEvent fae) {
        }
    }
}
