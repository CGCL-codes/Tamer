package org.dbcooper.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamSource;

/**
 * A template class for executing operations on <code>InputStream</code>s.
 * <p>This class is modeled on the idea of template classes in the Spring Framework.
 * </p>
 * <p>This class separates the resource acquisition and release, as well as exception
 * handling, from the code doing the actual work.
 * The code to do the actual work is provided as a strategy,
 * by way of the <code>InputStreamCallback</code>.
 * </p>
 */
public class InputStreamTemplate {

    private InputStreamSource inputStreamSource;

    public InputStreamTemplate(InputStreamSource inputStreamSource) {
        this.inputStreamSource = inputStreamSource;
    }

    public Object execute(InputStreamCallback callback) {
        InputStream inputStream = null;
        try {
            inputStream = inputStreamSource.getInputStream();
            return callback.doInInputStream(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
