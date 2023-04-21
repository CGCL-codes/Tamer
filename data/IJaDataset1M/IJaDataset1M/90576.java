package oqube.bytes.loading;

import oqube.bytes.ClassFile;

/**
 * A class for handling generation of class files to 
 * stable storage.
 */
public interface ClassFileHandler {

    /**
     * Called by class generators for handling generated
     * class files
     */
    public void handle(String name, ClassFile cf);
}
