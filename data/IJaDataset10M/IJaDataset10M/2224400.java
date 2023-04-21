package org.j2eespider.templateeditor.velocityeditor.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * This class is based on project VelocityWebEdit with author permission.
 * http://velocitywebedit.sourceforge.net
 * 
 * @author joe.hudson, bruno.braga
 */
public class StringUtil {

    /**
	 * Return the contents of the Stream as a String.
	 * Note:  If the InputStream represents a null String, the Java implementation will try to read from the stream for a certain amount of time
	 * before timing out.
	 * @param is the InputStream to transform into a String
	 * @return the String representation of the Stream
	 */
    public static String getStringFromStream(InputStream is) throws IOException {
        try {
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[1024];
            StringWriter writer = new StringWriter();
            int bytes_read;
            while ((bytes_read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, bytes_read);
            }
            return (writer.toString());
        } finally {
            if (null != is) is.close();
        }
    }
}
