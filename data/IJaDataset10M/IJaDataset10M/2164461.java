package net.sf.picasto;

import java.util.Collection;
import net.sf.picasto.util.Pluggable;

/**
 * @author bauerb
 *
 */
public interface Importer extends Pluggable, PrivateData {

    /**
     * 
     * @param args 
     * @return A collection of the changed items
     */
    public Collection process(String[] args);

    public Collection library();
}
