package gnu.jpdf;

import java.io.*;

/**
 * <p>A border around an annotation </p>
 *
 *
 * @author Peter T Mount, http://www.retep.org.uk/pdf/
 * @author Eric Z. Beard, ericzbeard@hotmail.com
 * @author $Author: ezb $
 * @version $Revision: 1.2 $
 */
public class PDFBorder extends PDFObject {

    private static final long serialVersionUID = 1L;

    /**
	 * The style of the border
	 */
    private short style;

    /**
	 * The width of the border
	 */
    private double width;

    /**
	 * This array allows the definition of a dotted line for the border
	 */
    private double dash[];

    /**
	 * Creates a border using the predefined styles in PDFAnnot.
	 * <p>Note: Do not use PDFAnnot.DASHED with this method.
	 * Use the other constructor.
	 *
	 * @param style The style of the border
	 * @param width The width of the border
	 * @see PDFAnnot
	 */
    public PDFBorder(short style, double width) {
        super("/Border");
        this.style = style;
        this.width = width;
    }

    /**
	 * Creates a border of style PDFAnnot.DASHED
	 *
	 * @param width The width of the border
	 * @param dash The line pattern definition
	 */
    public PDFBorder(double width, double dash[]) {
        super("/Border");
        this.style = PDFAnnot.DASHED;
        this.width = width;
        this.dash = dash;
    }

    /**
	 * @param os OutputStream to send the object to
	 * @exception IOException on error
	 */
    public void write(OutputStream os) throws IOException {
        os.write(Integer.toString(objser).getBytes());
        os.write(" 0 obj\n".getBytes());
        os.write("[/S /".getBytes());
        os.write("SDBIU".substring(style, style + 1).getBytes());
        os.write(" /W ".getBytes());
        os.write(Double.toString(width).getBytes());
        if (dash != null) {
            os.write(" /D [".getBytes());
            os.write(Double.toString(dash[0]).getBytes());
            for (int i = 1; i < dash.length; i++) {
                os.write(" ".getBytes());
                os.write(Double.toString(dash[i]).getBytes());
            }
            os.write("] ".getBytes());
        }
        os.write("]\n".getBytes());
        os.write("endobj\n".getBytes());
    }
}
