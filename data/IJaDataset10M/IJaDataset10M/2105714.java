package uk.ac.ebi.intact.model.visitor;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: HierarchyAware.java 10473 2007-11-22 15:55:30Z baranda $
 */
public interface HierarchyAware {

    int getHierarchyLevel();

    void setHierarchyLevel(int hierarchyLevel);

    void nextHierarchyLevel();

    void previousHierarchyLevel();
}
