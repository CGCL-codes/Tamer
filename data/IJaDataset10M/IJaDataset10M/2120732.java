package com.jme3.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <code>JmeExporter</code> specifies an export implementation for jME3 
 * data.
 */
public interface JmeExporter {

    /**
     * Export the {@link Savable} to an OutputStream.
     * 
     * @param object The savable to export
     * @param f The output stream
     * @return Always returns true. If an error occurs during export, 
     * an exception is thrown
     * @throws IOException If an io exception occurs during export
     */
    public boolean save(Savable object, OutputStream f) throws IOException;

    /**
     * Export the {@link Savable} to a file.
     * 
     * @param object The savable to export
     * @param f The file to export to
     * @return Always returns true. If an error occurs during export, 
     * an exception is thrown
     * @throws IOException If an io exception occurs during export
     */
    public boolean save(Savable object, File f) throws IOException;

    /**
     * Returns the {@link OutputCapsule} for the given savable object.
     * 
     * @param object The object to retrieve an output capsule for.
     * @return  
     */
    public OutputCapsule getCapsule(Savable object);
}
