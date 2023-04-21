package eu.redseeds.sc.query.ui.view.composite.scSimilarityResult;

import java.util.List;
import java.util.Set;
import eu.redseeds.scl.model.rsl.IHierarchyAware;
import eu.redseeds.scl.model.sclkernel.SoftwareCaseDTO;

public interface ISimilarGenericElementPathParser {

    public abstract Set<?> getChildrens(IHierarchyAware hierarchyAware);

    public abstract List<IHierarchyAware> getCurentPath(IHierarchyAware element);

    public abstract List<IHierarchyAware> getPastPath(IHierarchyAware element);

    public abstract String getCurentPathAsString(IHierarchyAware element);

    public abstract String getPastPathAsString(IHierarchyAware element);

    public abstract SoftwareCaseDTO getSoftwareCaseDTOForCurrentRequirement();

    public abstract SoftwareCaseDTO getSoftwareCaseDTOForPastRequirement();

    public abstract Set<IHierarchyAware> getTopCurrentPackages();

    public abstract boolean hasChildren(Object object);

    public abstract boolean contains(IHierarchyAware element);

    public abstract boolean contains(IElementMatch elementMatch);
}
