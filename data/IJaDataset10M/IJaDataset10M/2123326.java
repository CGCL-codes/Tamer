package archmapper.main.conformance.rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.tptp.platform.analysis.codereview.java.CodeReviewResource;
import org.eclipse.tptp.platform.analysis.codereview.java.ast.ASTHelper;
import org.eclipse.tptp.platform.analysis.core.history.AnalysisHistory;
import archmapper.main.conformance.INotifiedAboutAnalyzeEnd;
import archmapper.main.conformance.rulefilter.ComponentRuleFilter;
import archmapper.main.model.architecture.Component;

/**
 * Checks if all components from the architecture are present in the sourcecode
 * 
 * @author mg
 * 
 */
public class AllComponentsPresentRule extends AbstractArchitectureConformanceRule implements INotifiedAboutAnalyzeEnd {

    protected Map<String, Component> components = new HashMap<String, Component>();

    public void preAnalyze() {
        super.preAnalyze();
        if (arch != null) {
            for (Component comp : arch.getComponents()) {
                components.put(comp.getName(), comp);
            }
        }
    }

    @Override
    public void analyze(AnalysisHistory history) {
        CodeReviewResource resource = null;
        List<?> list = null;
        try {
            resource = getCurrentResource(history);
            list = resource.getTypedNodeList(resource.getResourceCompUnit(), ASTNode.TYPE_DECLARATION);
            ASTHelper.satisfy(list, new ComponentRuleFilter(mappingHelper, null, true));
            for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
                TypeDeclaration type = (TypeDeclaration) it.next();
                ITypeBinding binding = type.resolveBinding();
                String compName = mappingHelper.getComponentName(binding);
                components.remove(compName);
            }
        } catch (final RuntimeException e) {
            abortRulesWithError(e);
        }
    }

    public void postAnalyze(AnalysisHistory history) {
        for (Component comp : components.values()) {
            String label = "The component " + comp.getName() + " has no implementation.";
            if (!mappingHelper.isExternalComponent(comp.getName())) {
                generateResultsWithoutSource(history, label, comp.getParent().getAdlFile());
            }
        }
    }
}
