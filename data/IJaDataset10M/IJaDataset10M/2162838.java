package net.sourceforge.hobbes.common;

import java.io.IOException;

/**
 * A <code>ClientCreator</code> is an object which accepts a
 * <code>DocumentSource</code> and creates a <code>DocumentSender</code>.
 * 
 * 
 * @author Daniel M. Hackney
 * @created May 2, 2005
 */
public interface ClientCreator {

    /**
     * Create a new <code>DocumentSender</code>.
     * 
     * @param inListener
     *            <code>DocumentSource</code> to use for this
     *            <code>DocumentSender</code>.
     * @param inName
     *            The name to assign this client.
     */
    public ControllableModel addSource(DocumentSource inSource, String inName) throws IOException;

    public void removeSource(DocumentSource inListener) throws IllegalArgumentException;
}
