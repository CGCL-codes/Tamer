package eu.redseeds.sc.query.ui.view.composite.scSimilarityResult;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import eu.redseeds.scl.model.rsl.IHierarchyAware;

public class OrderSimilaritNameSorterFactory {

    public static ViewerSorter newAscendingSorter() {
        return new OrderAscendingSorter();
    }

    public static ViewerSorter newDescendingSorter() {
        return new OrderDescendingSorter();
    }

    private static class OrderAscendingSorter extends OrderSorter {

        @Override
        protected int compareRequirementMatch(Viewer viewer, IElementMatch em1, IElementMatch em2) {
            return em1.getCurrentElement().getName().compareTo(em2.getCurrentElement().getName());
        }

        @Override
        protected int compareRequirementMatch(Viewer viewer, IHierarchyAware em1, IHierarchyAware em2) {
            return em1.getName().compareTo(em2.getName());
        }
    }

    private static class OrderDescendingSorter extends OrderSorter {

        @Override
        protected int compareRequirementMatch(Viewer viewer, IElementMatch em1, IElementMatch em2) {
            return em2.getCurrentElement().getName().compareTo(em1.getCurrentElement().getName());
        }

        @Override
        protected int compareRequirementMatch(Viewer viewer, IHierarchyAware em1, IHierarchyAware em2) {
            return em2.getName().compareTo(em1.getName());
        }
    }
}
