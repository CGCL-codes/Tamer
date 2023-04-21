package com.volantis.testtools.mocks;

import com.volantis.testtools.io.ResourceUtilities;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.testtools.stubs.FileStub;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A mock object that emulates File based methods on an IFile including
 * getContent(). This class also emulates the marker methods on
 * IResource required by ODOMEditorContext.
 */
public class MockFile extends FileStub {

    /**
     * The name of the file.
     */
    private String fileName;

    /**
     * The File representation of the file name.
     */
    private File file;

    /**
     * A List of markers associated with this resource.
     */
    private List markers = new ArrayList();

    /**
     * Construct a new MockFile with the given filename.
     * @param fileName The path of the file relative to the testsuite/unit
     * directory.
     */
    public MockFile(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Create marker of the specified type.
     * @param type The marker type.
     * @return The created markers.
     */
    public IMarker createMarker(String type) throws CoreException {
        IMarker marker = new MockXPathMarker(type);
        markers.add(marker);
        return marker;
    }

    public IMarker[] findMarkers(String type, boolean b, int depth) throws CoreException {
        List foundMarkers = new ArrayList();
        for (int i = 0; i < markers.size(); i++) {
            IMarker marker = (IMarker) markers.get(i);
            if (marker.getType().equals(type)) {
                foundMarkers.add(marker);
            }
        }
        IMarker[] markers = new IMarker[foundMarkers.size()];
        return (IMarker[]) foundMarkers.toArray(markers);
    }

    public IPath getLocation() {
        try {
            if (file == null) {
                createFile();
            }
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
        return new Path(file.getAbsolutePath());
    }

    public IProject getProject() {
        return new MockProject();
    }

    public InputStream getContents() {
        InputStream is = null;
        try {
            if (file == null) {
                createFile();
            }
            is = new FileInputStream(file);
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        }
        return is;
    }

    /**
     * Create the File using the fileName and ResourceUtilities.
     */
    private void createFile() throws IOException {
        file = ResourceUtilities.getResourceAsFile(fileName);
    }
}
