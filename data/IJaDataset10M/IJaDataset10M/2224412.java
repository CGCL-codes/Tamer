package ch.bbv.dnm.users.lw;

/** 
 * The visitor class implements the visitor pattern for the graph of all 
 * data object classes defined in the package. The list of all relevant
 * classes is generated from the MDA model.
 */
public interface LwVisitor {

    /** 
   * Returns the current depth in the graph when traversing it with the 
   * visitor. The depth is a positive integer with a minimal value of 1.
   * @return the current depth of the visitor in the graph.
   */
    int getDepth();

    /** 
   * Sets the current depth in the graph when traversing it with the visitor. 
   * The depth is a positive integer with a minimal value of 1.
   * @param depth current depth of the visitor in the traversed graph.
   */
    void setDepth(int depth);

    /** 
   * Visits the class UserLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(UserLw item);

    /** 
   * Visits the class RoleLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(RoleLw item);

    /** 
   * Visits the class UnitLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(UnitLw item);

    /** 
   * Visits the class DomainLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(DomainLw item);

    /** 
   * Visits the class ProviderLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(ProviderLw item);

    /** 
   * Visits the class RealmLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(RealmLw item);

    /** 
   * Visits the class NamedEntityLw as part of the visitor pattern.
   * @param item item to visit.
   */
    void visit(NamedEntityLw item);
}
