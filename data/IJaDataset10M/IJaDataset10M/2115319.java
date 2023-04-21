package xbrowser.renderer.custom;

import java.io.*;

/** InputStream that makes copy to file given.
 *
 *@author     Uladzimir_Kavalchuk
 *@created    June 12, 2001
 */
public class XTeeInputStream extends FilterInputStream {

    /**
	 *  Constructor for the TeeInputStream object
	 *
	 *@param  i  Description of Parameter
	 */
    public XTeeInputStream(InputStream i) {
        super(i);
    }

    /**
	 *  Description of the Method
	 *
	 *@return                  Description of the Returned Value
	 *@exception  IOException  Description of Exception
	 */
    public int read() throws IOException {
        int c = super.read();
        check();
        out.write(c);
        return c;
    }

    /**
	 *  Description of the Method
	 *
	 *@param  b                Description of Parameter
	 *@param  off              Description of Parameter
	 *@param  len              Description of Parameter
	 *@return                  Description of the Returned Value
	 *@exception  IOException  Description of Exception
	 */
    public int read(byte b[], int off, int len) throws IOException {
        int c = super.read(b, off, len);
        check();
        if (c > 0) out.write(b, off, c);
        return c;
    }

    private void check() {
        if (out == null) {
            out = nos;
        }
    }

    /**
	 *  Sets the out attribute of the TeeInputStream class
	 *
	 *@param  fileName  The new FileOutputStream value
	 */
    public void setFileOutputStream(OutputStream fos) {
        out = fos;
    }

    public void close() throws IOException {
        super.close();
        check();
        out.flush();
        out.close();
    }

    private OutputStream out;

    private static OutputStream nos = new OutputStream() {

        public void write(int c) {
        }
    };
}
