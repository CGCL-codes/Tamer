package cnaf.sidoc.ide.core.internal.model.federation;

import org.eclipse.wst.xml.search.core.statics.DefaultStaticValueVisitor;
import org.eclipse.wst.xml.search.core.statics.IStaticValueVisitor;
import cnaf.sidoc.ide.core.model.ISIDocCollectionProject;
import cnaf.sidoc.ide.core.util.SIDocModelUtils;

public class StaticValueVisitorLocalAndFederation extends DefaultStaticValueVisitor {

    public static IStaticValueVisitor INSTANCE = new StaticValueVisitorLocalAndFederation();

    public StaticValueVisitorLocalAndFederation() {
        registerProjects();
    }

    private void registerProjects() {
        ISIDocCollectionProject[] projects = SIDocModelUtils.getSIDocModel().getCollectionProjects();
        for (ISIDocCollectionProject project : projects) {
            super.registerValue(project);
        }
    }
}
