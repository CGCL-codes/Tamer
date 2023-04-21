package com.liferay.util.servlet;

import java.io.IOException;
import javax.servlet.ServletOutputStream;

/**
 * <a href="NullServletOutputStream.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.1 $
 *
 */
public class NullServletOutputStream extends ServletOutputStream {

    public NullServletOutputStream() {
    }

    public void write(int b) throws IOException {
    }
}
