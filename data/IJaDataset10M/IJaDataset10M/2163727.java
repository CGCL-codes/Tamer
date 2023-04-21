package jung.ext.dag;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import edu.uci.ics.jung.graph.Vertex;
import jung.ext.predicates.vertex.impl.ExcludesPredicate;
import jung.ext.predicates.vertex.impl.IncludesPredicate;
import jung.ext.predicates.vertex.VertexPredicate;
import jung.ext.predicates.vertex.DefaultVertexPredicate;
import vilaug.exceptions.InitException;

/**
 * The <code>LimitFamilyVertex</code> extends its superclass - that already has
 * functions that have to do with parents, children, ancestors and descendant
 * concepts - with a browsing predicate. This predicate can be set with the 
 * method <code>setBrowsingPredicate</code>.
 * 
 * <p>The browsing predicate limits the search for the methods that investigate
 * ancestry.
 * 
 * @author A.C. van Rossum
 *
 */
public class LimitFamilyVertex extends ReplaceableFamilyVertex {

    private static final String ID = LimitFamilyVertex.class.getName();

    private static VertexPredicate predicate;

    public LimitFamilyVertex() {
        this(VertexType.REPLACEABLE);
    }

    public LimitFamilyVertex(VertexType type) {
        super(type);
    }

    public static void setBrowsingPredicate(VertexPredicate vertexPredicate) {
        if (!(vertexPredicate instanceof DefaultVertexPredicate)) throw new InitException(ID, "setBrowsingPredicate", "VertexPredicate should be from default type.");
        predicate = vertexPredicate;
    }

    public static VertexPredicate getBrowsingPredicate() {
        return predicate;
    }

    protected static DefaultVertexPredicate getDefaultBrowsingPredicate() {
        return (DefaultVertexPredicate) predicate;
    }

    protected static void addIncludingFilter(IncludesPredicate includesPredicate) {
        getDefaultBrowsingPredicate().addPredicate(includesPredicate);
    }

    protected static void addExcludingFilter(ExcludesPredicate excludesPredicate) {
        getDefaultBrowsingPredicate().addPredicate(excludesPredicate);
    }

    protected static void deleteFilters() {
        getDefaultBrowsingPredicate().clear();
    }

    /**
   * The method <code>hasAncestor</code> returns a boolean that 
   * denotes if a certain vertex is an ancestor of this instance. 
   * It does use the available filters setted by the decorators
   * addIncludingFilter or addExcludingFilter.
   * @param ancestor The ancestral vertex as search criterium. 
   * First ancestry is checked, next the filters are applied.
   */
    public boolean hasAncestor(FamilyVertex ancestor) {
        Set parents = this.getPredecessors();
        boolean result = false;
        for (Iterator vsIt = parents.iterator(); vsIt.hasNext(); ) {
            FamilyVertex v = (FamilyVertex) vsIt.next();
            if (v.equals(ancestor)) {
                return true;
            } else if (predicate.evaluateVertex(v)) {
                result = v.hasAncestor(ancestor);
            }
            if (result) return true;
        }
        return false;
    }

    public boolean hasAncestor(FamilyVertex ancestor, FamilyVertex exclude) {
        Set parents = this.getPredecessors();
        boolean result = false;
        for (Iterator vsIt = parents.iterator(); vsIt.hasNext(); ) {
            FamilyVertex v = (FamilyVertex) vsIt.next();
            if (v.equals(ancestor)) {
                return true;
            } else if (predicate.evaluateVertex(v) && (!v.equals(exclude))) {
                result = v.hasAncestor(ancestor, exclude);
            }
            if (result) return true;
        }
        return false;
    }

    public Set returnAncestors(Set potentialAncestors) {
        Set ancestors = new HashSet(1);
        Set parents = this.getPredecessors();
        for (Iterator vsIt = parents.iterator(); vsIt.hasNext(); ) {
            FamilyVertex v = (FamilyVertex) vsIt.next();
            if (potentialAncestors.contains(v)) {
                ancestors.add(v);
            } else if (predicate.evaluateVertex(v)) {
                Set tempAncestors = v.returnAncestors(potentialAncestors);
                if (tempAncestors != null) ancestors.addAll(tempAncestors);
            }
        }
        if (ancestors.size() == 0) return null;
        return ancestors;
    }

    /**
   * With the method <code>setStrangers</code> strangers will be used
   * to exclude some nodes in the browsing functions. 
   * <p>Strangers pose thus limits to the search depth or width. 
   * @param strangers A set of vertices.
   */
    public void setStrangers(Set strangers) {
        addExcludingFilter(new ExcludesPredicate(strangers));
    }

    /**
   * Singular method of <code>setStranger</code>. If there are more
   * strangers than one, use <code>setStrangers</code> in stead.
   * @see setStrangers
   */
    public void setStranger(Vertex stranger) {
        Set strangers = new HashSet(1);
        strangers.add(stranger);
        setStrangers(strangers);
    }

    /**
   * With the method <code>setElderly(Set elderly)</code> the 
   * variable elderly will be set and subsequently used for browsing 
   * functions.
   * <p>Elderly are vertices that pose ancestral requirements. 
   * @param Elders A set of vertices.
   */
    public void setElderly(Set elderly) {
        addExcludingFilter(new ExcludesPredicate(elderly));
    }

    /**
   * Singular method of <code>setElderly(Set elderly)</code>. If there
   * are more elderly than one, use <code>setElderly</code> in stead.
   * @see setElderly(Set elderly)
   */
    public void setElder(Vertex elder) {
        Set elderly = new HashSet(1);
        elderly.add(elder);
        setElderly(elderly);
    }

    public void removeLimits() {
        deleteFilters();
    }
}
