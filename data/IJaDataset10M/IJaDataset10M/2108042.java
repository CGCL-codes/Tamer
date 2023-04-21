package uk.ac.essex.ia.opview.xml.param;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.essex.ia.opview.xml.Input;
import java.awt.image.renderable.ParameterBlock;

/**
 * <br>
 * Date: 15-Jul-2002 <br>
 *
 * @author Laurence Smith
 *
 *         You should have received a copy of GNU public license with this code.
 *         If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class FloatParameterReader extends ParameterReader {

    /** The logger */
    private static final transient Log logger = LogFactory.getLog(FloatParameterReader.class);

    /** @param parameterBlock */
    public void updateParameters(ParameterBlock parameterBlock, Input input) {
        try {
            Float num = new Float(input.getContent());
            parameterBlock.add(num.intValue());
        } catch (NumberFormatException e) {
            if (logger.isDebugEnabled()) {
                logger.error("ERROR: ", e);
            }
            logger.warn("Failed to read float - content is = " + input.getContent());
        }
    }
}
