package org.peaseplate.domain.lang.command;

import org.peaseplate.BuildContext;
import org.peaseplate.TemplateLocator;
import org.peaseplate.TemplateRuntimeException;

public class ThisCommand extends AbstractCommand implements ICommand {

    public ThisCommand(TemplateLocator locator, int line, int column) {
        super(locator, line, column);
    }

    /**
	 * @see org.peaseplate.domain.lang.command.ICommand#call(BuildContext)
	 */
    public Object call(BuildContext context) throws TemplateRuntimeException {
        return context.getWorkingObject();
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "$this";
    }
}
