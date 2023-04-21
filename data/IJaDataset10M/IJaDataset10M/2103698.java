package com.ideo.jso.minifier;

import java.io.InputStream;
import java.io.Writer;

/**
 * This interface represents the contract for a CSS minimifier
 * Implements this method to write your own minimifier, then bind it in the configuration file.
 * 
 * @author Julien Maupoux
 *
 */
public interface ICssMinifier {

    public void minify(InputStream in, Writer out) throws Exception;
}
