package org.osmorc.facet;

import com.intellij.openapi.application.Application;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class OsmorcFacetRefactoringListenerProvider implements RefactoringElementListenerProvider {

    private OsmorcFacetUtil osmorcFacetUtil;

    private Application application;

    public OsmorcFacetRefactoringListenerProvider(final OsmorcFacetUtil osmorcFacetUtil, final Application application) {
        this.osmorcFacetUtil = osmorcFacetUtil;
        this.application = application;
    }

    @Nullable
    public RefactoringElementListener getListener(final PsiElement element) {
        if (element instanceof PsiClass && osmorcFacetUtil.hasOsmorcFacet(element)) {
            OsmorcFacet osmorcFacet = osmorcFacetUtil.getOsmorcFacet(element);
            OsmorcFacetConfiguration osmorcFacetConfiguration = osmorcFacet.getConfiguration();
            PsiClass psiClass = (PsiClass) element;
            if (osmorcFacetConfiguration.isOsmorcControlsManifest() && osmorcFacetConfiguration.getBundleActivator() != null && osmorcFacetConfiguration.getBundleActivator().equals(psiClass.getQualifiedName())) {
                return new ActivatorClassRefactoringListener(osmorcFacetConfiguration, application);
            }
        }
        return null;
    }

    private static final class ActivatorClassRefactoringListener implements RefactoringElementListener {

        private OsmorcFacetConfiguration osmorcFacetConfiguration;

        private Application application;

        private ActivatorClassRefactoringListener(final OsmorcFacetConfiguration osmorcFacetConfiguration, final Application application) {
            this.osmorcFacetConfiguration = osmorcFacetConfiguration;
            this.application = application;
        }

        public void elementMoved(@NotNull final PsiElement newElement) {
            updateActivatorName(newElement);
        }

        public void elementRenamed(@NotNull final PsiElement newElement) {
            updateActivatorName(newElement);
        }

        private void updateActivatorName(@NotNull final PsiElement newElement) {
            application.runWriteAction(new Runnable() {

                public void run() {
                    osmorcFacetConfiguration.setBundleActivator(((PsiClass) newElement).getQualifiedName());
                }
            });
        }
    }
}
