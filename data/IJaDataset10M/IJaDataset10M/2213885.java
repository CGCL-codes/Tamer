package ti.plato.ui.views.internal.console;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.expressions.ExpressionTagNames;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IPluginContribution;
import ti.plato.ui.views.console.IConsole;
import ti.plato.ui.views.console.IConsolePageParticipant;

public class ConsolePageParticipantExtension implements IPluginContribution {

    private IConfigurationElement fConfig;

    private Expression fEnablementExpression;

    public ConsolePageParticipantExtension(IConfigurationElement config) {
        fConfig = config;
    }

    public String getLocalId() {
        return fConfig.getAttribute("id");
    }

    public String getPluginId() {
        return fConfig.getNamespace();
    }

    public boolean isEnabledFor(IConsole console) throws CoreException {
        EvaluationContext context = new EvaluationContext(null, console);
        EvaluationResult evaluationResult = getEnablementExpression().evaluate(context);
        return evaluationResult == EvaluationResult.TRUE;
    }

    public Expression getEnablementExpression() throws CoreException {
        if (fEnablementExpression == null) {
            IConfigurationElement[] elements = fConfig.getChildren(ExpressionTagNames.ENABLEMENT);
            IConfigurationElement enablement = elements.length > 0 ? elements[0] : null;
            if (enablement != null) {
                fEnablementExpression = ExpressionConverter.getDefault().perform(enablement);
            }
        }
        return fEnablementExpression;
    }

    public IConsolePageParticipant createDelegate() throws CoreException {
        return (IConsolePageParticipant) fConfig.createExecutableExtension("class");
    }
}
