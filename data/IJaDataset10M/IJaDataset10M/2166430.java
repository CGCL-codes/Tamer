package org.mitre.rt.client.util.smash;

import java.io.File;
import java.util.List;

/**
 *
 * @author BWORRELL
 */
public interface ISmasher {

    public File smash(final List<String> files, final String namespace, final String outputFilename) throws Exception;

    public String getNamespaceURI() throws Exception;
}
