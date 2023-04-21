package com.loribel.commons.abstraction;

import java.io.File;
import java.io.Reader;

/**
 * Abstraction of a String content.
 * 
 * @author Gregory Borelli
 */
public interface GB_StringContentSet extends GB_StringContent {

    void setContentFromFile(File a_file, String a_encoding);

    void setContentFromReader(Reader a_reader);

    void setContentFromResource(Class a_class, String a_name, String a_encoding);

    void setContentFromString(String a_content);

    void setContentFromUrl(String a_url);
}
