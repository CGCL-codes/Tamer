package net.sourceforge.buildprocess.autodeploy;

import java.util.Iterator;
import java.util.List;
import net.sourceforge.buildprocess.autodeploy.model.Variable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util class to manipulate (search and replace) AutoDeploy variable
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class VariableUtil {

    private static final Log log = LogFactory.getLog(VariableUtil.class);

    /**
    * Replace all given variables in a string
    * 
    * @param source
    *           the source string
    * @param variables
    *           the variables list
    * @return the string with variables replaced
    */
    public static final String replace(String source, List variables) {
        String replaced = source;
        for (Iterator variableIterator = variables.iterator(); variableIterator.hasNext(); ) {
            Variable variable = (Variable) variableIterator.next();
            log.debug("Replace ${" + variable.getName() + "} by " + variable.getValue() + " in " + source);
            replaced = StringUtils.replace(replaced, "${" + variable.getName() + "}", variable.getValue());
        }
        return replaced;
    }
}
