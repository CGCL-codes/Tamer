package net.sourceforge.pmd.ui.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import net.sourceforge.pmd.runtime.PMDRuntimeConstants;
import net.sourceforge.pmd.ui.PMDUiPlugin;
import net.sourceforge.pmd.ui.nls.StringKeys;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * PMDRecord for Files
 * 
 * @author SebastianRaffel ( 16.05.2005 )
 */
public class FileRecord extends PMDRecord {

    private IResource resource;

    private PackageRecord parent;

    /**
     * Constructor (not for use with the Model, no PackageRecord is provided here)
     * 
     * @param javaResource, the given File
     */
    public FileRecord(IResource javaResource) {
        resource = javaResource;
        parent = null;
    }

    /**
     * Constructor (for use with the Model)
     * 
     * @param javaResource
     * @param record
     */
    public FileRecord(IResource javaResource, PackageRecord record) {
        resource = javaResource;
        parent = record;
    }

    public PMDRecord getParent() {
        return parent;
    }

    public PMDRecord[] getChildren() {
        return new PMDRecord[0];
    }

    public IResource getResource() {
        return resource;
    }

    protected PMDRecord[] createChildren() {
        return null;
    }

    /**
     * Checks the File for PMD-Markers
     * 
     * @return true if the File has PMD-Markers, false otherwise
     */
    public boolean hasMarkers() {
        if (resource == null) return false;
        IMarker[] markers = findMarkers();
        if ((markers != null) && (markers.length > 0)) return true;
        return false;
    }

    /**
     * Finds PMD-Markers in the File
     * 
     * @return an Array of Markers or null, if the File doesn't have any Markers
     */
    public IMarker[] findMarkers() {
        IMarker[] markers = null;
        try {
            if (this.resource.isAccessible()) {
                markers = resource.findMarkers(PMDRuntimeConstants.PMD_MARKER, true, IResource.DEPTH_INFINITE);
            }
        } catch (CoreException ce) {
            PMDUiPlugin.getDefault().logError(StringKeys.MSGKEY_ERROR_FIND_MARKER + this.toString(), ce);
        }
        return markers;
    }

    /**
     * Finds Markers, that have a given Attribute with a given Value
     * 
     * @param attributeName
     * @param value
     * @return an Array of Markers or null, if there aren't Markers matching these Attribute and Value
     */
    public IMarker[] findMarkersByAttribute(String attributeName, Object value) {
        IMarker[] markers = findMarkers();
        ArrayList attributeMarkers = new ArrayList();
        try {
            for (int i = 0; i < markers.length; i++) {
                Object val = markers[i].getAttribute(attributeName);
                if ((val != null) && (val.equals(value))) attributeMarkers.add(markers[i]);
            }
        } catch (CoreException ce) {
            PMDUiPlugin.getDefault().logError(StringKeys.MSGKEY_ERROR_FIND_MARKER + this.toString(), ce);
        }
        if (attributeMarkers.isEmpty()) return null;
        IMarker[] markerArray = new IMarker[attributeMarkers.size()];
        attributeMarkers.toArray(markerArray);
        return markerArray;
    }

    /**
     * Calculates the Number of Code-Lines this File has The Function is adapted from the Eclipse Metrics-Plugin available at:
     * http://www.sourceforge.net/projects/metrics
     * 
     * @return the Lines of Code
     */
    public int getLinesOfCode() {
        if (!(resource instanceof IFile)) {
            return 0;
        }
        return 0;
    }

    /**
     * Reads a Resource's File and return the Code as String
     * 
     * @param resource
     * @return a String which is the Files Content
     */
    protected String resourceToString(IResource resource) {
        String fileContents = "";
        try {
            FileReader fileReader = new FileReader(resource.getRawLocation().toFile());
            BufferedReader bReader = new BufferedReader(fileReader);
            while (bReader.ready()) {
                fileContents += bReader.readLine() + "\n";
            }
        } catch (FileNotFoundException fnfe) {
            PMDUiPlugin.getDefault().logError(StringKeys.MSGKEY_ERROR_FILE_NOT_FOUND + resource.toString() + " in " + this.toString(), fnfe);
        } catch (IOException ioe) {
            PMDUiPlugin.getDefault().logError(StringKeys.MSGKEY_ERROR_IO_EXCEPTION + this.toString(), ioe);
        }
        return fileContents;
    }

    /**
     * Gets the Number of Methods, this Class contains
     * 
     * @return the Number of Methods
     */
    public int getNumberOfMethods() {
        return 0;
    }

    public PMDRecord addResource(IResource resource) {
        return null;
    }

    public PMDRecord removeResource(IResource resource) {
        return null;
    }

    public String getName() {
        return resource.getName();
    }

    public int getResourceType() {
        return PMDRecord.TYPE_FILE;
    }
}
