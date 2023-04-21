package es.caib.zkib.datamodel.xml.handler;

import javax.servlet.jsp.el.ELException;
import bsh.EvalError;
import es.caib.zkib.datamodel.DataContext;
import es.caib.zkib.datamodel.xml.Interpreter;
import es.caib.zkib.datamodel.xml.definition.DefinitionInterface;

public abstract class AbstractHandler implements DefinitionInterface {

    String ifString;

    String unlessString;

    public AbstractHandler() {
        super();
    }

    public boolean isSuitable(DataContext ctx) {
        if (unlessString != null) {
            Object obj;
            try {
                obj = Interpreter.evaluate(ctx, unlessString);
            } catch (ELException e) {
                throw new RuntimeException(e);
            }
            if (obj instanceof Boolean && ((Boolean) obj).booleanValue()) return false;
        }
        if (ifString != null) {
            Object obj;
            try {
                obj = Interpreter.evaluate(ctx, ifString);
            } catch (ELException e) {
                throw new RuntimeException(e);
            }
            if (obj instanceof Boolean && !((Boolean) obj).booleanValue()) return false;
        }
        return true;
    }

    /**
	 * @return Returns the ifString.
	 */
    public String getIf() {
        return ifString;
    }

    /**
	 * @param ifString The ifString to set.
	 */
    public void setIf(String ifString) {
        this.ifString = ifString;
    }

    /**
	 * @return Returns the unlessString.
	 */
    public String getUnless() {
        return unlessString;
    }

    /**
	 * @param unlessString The unlessString to set.
	 */
    public void setUnless(String unlessString) {
        this.unlessString = unlessString;
    }
}
