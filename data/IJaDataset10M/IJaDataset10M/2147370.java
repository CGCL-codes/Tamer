package dawnland02.data.map.io.reader.sax;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import dawnland02.data.map.io.MatrixXMLTag;
import dawnland02.data.map.model.Matrix;
import dawnland02.data.map.model.MatrixCell;

/**
 * User: Petru Obreja (obrejap@yahoo.com)
 * Date: Sep 16, 2009
 * Time: 8:59:41 PM
 */
public class MatrixXMLSAXHandler extends DefaultHandler {

    private Matrix matrix;

    private MatrixXMLTag currentXMLTag;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentXMLTag = MatrixXMLTag.fromValue(qName);
        if (currentXMLTag == MatrixXMLTag.MATRIX) {
            Integer rows = Integer.valueOf(attributes.getValue(MatrixXMLTag.ROWS_ATTRIBUTE.getValue()));
            Integer columns = Integer.valueOf(attributes.getValue(MatrixXMLTag.COLUMNS_ATTRIBUTE.getValue()));
            matrix = new Matrix(rows, columns);
        } else if (currentXMLTag == MatrixXMLTag.NODE) {
            Integer id = Integer.valueOf(attributes.getValue(MatrixXMLTag.ID_ATTRIBUTE.getValue()));
            Integer x = Integer.valueOf(attributes.getValue(MatrixXMLTag.X_ATTRIBUTE.getValue()));
            Integer y = Integer.valueOf(attributes.getValue(MatrixXMLTag.Y_ATTRIBUTE.getValue()));
            Integer type = Integer.valueOf(attributes.getValue(MatrixXMLTag.TYPE_ATTRIBUTE.getValue()));
            Integer roadLevel = Integer.valueOf(attributes.getValue(MatrixXMLTag.ROAD_LEVEL_ATTRIBUTE.getValue()));
            matrix.addNode(id, x, y, type, roadLevel);
        }
    }

    public Matrix getMatrix() {
        return matrix;
    }
}
