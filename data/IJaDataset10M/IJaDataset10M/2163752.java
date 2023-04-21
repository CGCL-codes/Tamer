package org.gnupaste.external.hosters;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * This class must be implemented by all hoster-plugins,
 * which use the extensionpoint de.gnuyork.gnupaste.hosters
 * @author Jakob Eichler
 *
 */
public interface IHosterPlugin {

    /**
	 * @param text
	 * 				text that should be hosted
	 * @param fileExtension
	 * 				the fileExtension of the file which contains the text to paste.
	 * 				for example "java" or "xml".
	 * @return
	 * 				the {@link URL} to find the hosted text
	 * @throws IOException 
	 */
    public String hostText(String text, SyntaxType syntaxType, Map<String, String> options) throws IOException;

    /**
	 * 
	 * @param file
	 * 				a file that should be hosted
	 * @return
	 * 				the {@link URL} to find the hosted files
	 * @throws IOException 
	 */
    public String hostFile(File file, SyntaxType syntax, Map<String, String> options) throws IOException;

    /**
	 * 
	 * @param file
	 * 				files that should be hosted
	 * @return
	 * 				the {@link URL} to find the hosted files
	 * @throws IOException 
	 */
    public String hostFile(List<File> files, SyntaxType syntax, Map<String, String> options) throws IOException;

    /**
	 * @return
	 * 			A link to the terms of use page of the hoster.
	 */
    public String getAGBLink();
}
