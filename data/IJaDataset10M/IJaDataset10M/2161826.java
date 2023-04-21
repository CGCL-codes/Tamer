package cwi.GraphXML.Elements;

import cwi.GraphXML.ParserError;
import org.w3c.dom.*;
import java.awt.geom.AffineTransform;
import java.util.StringTokenizer;

/**
* Transformation matrix. Note that the class will return the full matrix (3x3 or 4x4),
* although the user specifies the affine part only.
*
* @see Reference
* @author Ivan Herman
*/
public class Transform implements java.io.Serializable {

    private double[] matrix = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };

    /**
    * Return the full, 4x4 matrix
    */
    public double[] get3Dmatrix() {
        return matrix;
    }

    /**
    * Return a 3x3 submatrix, to be used for 3D transform.
    */
    public double[] get2Dmatrix() {
        double[] retval = new double[] { matrix[0], matrix[1], matrix[3], matrix[4], matrix[5], matrix[7], matrix[12], matrix[13], matrix[15] };
        return retval;
    }

    /**
    * Return a Java2D affine transform
    */
    public AffineTransform get2DAffineTransform() {
        return new AffineTransform(matrix[0], matrix[1], matrix[3], matrix[4], matrix[5], matrix[7]);
    }

    /**
    * Retrieve the representation of a position from a node, and returns
    * a corresponding class instance. 
    *
    * @param node The node containing the reference.
    * @param  parser the parser in charge; used to generate error messages
    * @return new Reference class, or null
    */
    public static Transform create(Node node, ParserError parserError) {
        Transform retval = new Transform();
        NamedNodeMap map = node.getAttributes();
        int length = map.getLength();
        for (int i = 0; i < length; i++) {
            Node attribute = map.item(i);
            if (attribute.getNodeName().equals("matrix") != true) continue;
            try {
                StringTokenizer matrixString = new StringTokenizer(attribute.getNodeValue());
                int num = matrixString.countTokens();
                if (num == (2 * 3)) {
                    retval.matrix[0] = Double.parseDouble(matrixString.nextToken());
                    retval.matrix[1] = Double.parseDouble(matrixString.nextToken());
                    retval.matrix[3] = Double.parseDouble(matrixString.nextToken());
                    retval.matrix[4] = Double.parseDouble(matrixString.nextToken());
                    retval.matrix[5] = Double.parseDouble(matrixString.nextToken());
                    retval.matrix[7] = Double.parseDouble(matrixString.nextToken());
                } else if (num == (3 * 4)) {
                    for (int j = 0; j < (3 * 4); j++) {
                        retval.matrix[j] = Double.parseDouble(matrixString.nextToken());
                    }
                } else {
                    parserError.fireParserWarning("Incorrect number of values in element " + node.getNodeName());
                    return null;
                }
                return retval;
            } catch (NumberFormatException e) {
                parserError.fireParserWarning("In element " + node.getNodeName() + "; " + e.getMessage());
                return null;
            }
        }
        return retval;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("[");
        for (int j = 0; j < 4; j++) {
            buffer.append("[");
            for (int i = 0; i < 4; i++) {
                buffer.append(matrix[j * 4 + i]);
                if (i != 3) {
                    buffer.append(",");
                }
            }
            buffer.append("]");
            if (j != 3) buffer.append(",");
        }
        buffer.append("]");
        return buffer.toString();
    }
}
