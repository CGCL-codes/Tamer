package matrixviewer.view;

import java.awt.Color;
import matrixviewer.model.MatrixDatum;

/**
 *
 * @author bchisham
 */
public interface ColorCoder {

    Color getColor(MatrixDatum datum);
}
