package net.sf.mpxj.writer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.planner.PlannerWriter;

/**
 * This class contains utility methods for working with ProjectWriters.
 */
public final class ProjectWriterUtility {

    /**
    * Constructor.
    */
    private ProjectWriterUtility() {
    }

    /**
    * Retrieves a ProjectWriter instance which can write a file of the
    * type specified by the supplied file name. 
    * 
    * @param name file name
    * @return ProjectWriter instance
    */
    public static ProjectWriter getProjectWriter(String name) throws InstantiationException, IllegalAccessException {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException("Filename has no extension: " + name);
        }
        String extension = name.substring(index + 1).toUpperCase();
        Class<? extends ProjectWriter> fileClass = WRITER_MAP.get(extension);
        if (fileClass == null) {
            throw new IllegalArgumentException("Cannot write files of type: " + name);
        }
        ProjectWriter file = fileClass.newInstance();
        return (file);
    }

    /**
    * Retrieves a set containing the file extensions supported by the
    * getProjectWriter method.
    * 
    * @return set of file extensions
    */
    public static Set<String> getSupportedFileExtensions() {
        return (WRITER_MAP.keySet());
    }

    private static final Map<String, Class<? extends ProjectWriter>> WRITER_MAP = new HashMap<String, Class<? extends ProjectWriter>>();

    static {
        WRITER_MAP.put("MPX", MPXWriter.class);
        WRITER_MAP.put("XML", MSPDIWriter.class);
        WRITER_MAP.put("PLANNER", PlannerWriter.class);
    }
}
