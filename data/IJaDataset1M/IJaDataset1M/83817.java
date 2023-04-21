package uk.ac.essex.ia.media.geo;

import org.apache.log4j.Category;
import uk.ac.essex.ia.DataTypeConstant;
import uk.ac.essex.ia.Operator;
import uk.ac.essex.ia.media.OperatorImpl;
import uk.ac.essex.ia.media.xml.OperatorDescriptor;
import java.awt.image.renderable.ParameterBlock;
import java.util.Vector;

/**
 * Performs a perspective transform on an image
 *
 * @author Laurence Smith
 *
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class PerspectiveTransformOperator extends OperatorImpl {

    /** The log 4j logger */
    private static transient Category category = Category.getInstance(PerspectiveTransformOperator.class);

    public PerspectiveTransformOperator(OperatorDescriptor operatorType) {
        super(operatorType);
    }

    /**
     * In this function implmentations should handle displaying the {@link #operatorDialog}
     * via its show() method. Also any input parameters should if instantiated be loaded
     * into the dialog prior to it being displayed. These should then be dislpayed after
     * it is complete. NB this is modal dialog so once the show method is called
     * execution will block. After the call the parameters can be got via
     * {@link uk.ac.essex.ia.media.gui.OperatorDialog#getParameter}.
     */
    public void showOperatorDialog() {
        Vector params = inputParameters.getParameters();
        if ((params != null) && (params.size() > 0)) {
        }
        operatorDialog.show();
        if (operatorDialog.isSaved()) {
            category.debug("Parameters saved");
        }
    }

    /**
     * Execute this operator with the input data passed as a parameter
     * @param inData - The input data to this operator
     */
    public Object execute(ParameterBlock inData) throws Exception {
        return null;
    }
}
