package net.sf.stump.eclipse.parser;

import java.io.InputStream;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author Joni Freeman
 */
class FileStub extends File {

    private final String path;

    protected FileStub(String path) {
        super(new Path(path), null);
        this.path = path;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public InputStream getContents() {
        String stream = path;
        if (path.startsWith("/")) {
            stream = path.substring(1);
        }
        return PreviewParserTest.class.getResourceAsStream(stream);
    }

    @Override
    public IContainer getParent() {
        return new Folder(new Path("."), null) {

            @Override
            public IResource findMember(String path) {
                return new FileStub(path);
            }
        };
    }

    @Override
    public IProject getProject() {
        return new Project(new Path("."), null) {

            @Override
            public IResource findMember(String path) {
                return new FileStub(path);
            }

            @Override
            public IPath getLocation() {
                return new Path(".");
            }
        };
    }

    @Override
    public IPath getProjectRelativePath() {
        int index = path.lastIndexOf('/');
        if (index == -1) {
            return new Path("");
        }
        return new Path(path.substring(0, index) + "/");
    }
}
