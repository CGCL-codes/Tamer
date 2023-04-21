package grammarscope.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

/**
 * <code>ProtoGrammaticalRelation</code> is used to define a standardized, hierarchical set of grammatical relations, together with patterns for identifying
 * them in parse trees.
 * <p>
 * Each <code>GrammaticalRelation</code> has:
 * <ul>
 * <li>A <code>String</code> short name, which should be a lowercase abbreviation of some kind.</li>
 * <li>A <code>String</code> long name, which should be descriptive.</li>
 * <li>A parent in the <code>GrammaticalRelation</code> hierarchy.</li>
 * <li>A <code>Pattern</code> called <code>sourcePattern</code> which matches (parent) nodes from which this <code>GrammaticalRelation</code> could hold. (Note:
 * this is done with the Java regex Pattern <code>matches()</code> predicate: the pattern must match the whole node name, and <code>^</code> or <code>$</code>
 * aren't needed.)</li>
 * <li>A list of zero or more TregexPatterns called <code>targetPatterns</code>, which describe the local tree structure which must hold between the source node
 * and a target node for the <code>GrammaticalRelation</code> to apply. (Note <code>tregex</code> regular expressions match with the <code>find()</code> method
 * - though literal string label descriptions that are not regular expressions must be <code>equals()</code>.)</li>
 * </ul>
 * The <code>targetPatterns</code> associated with a <code>GrammaticalRelation</code> are designed as follows. In order to recognize a grammatical relation X
 * holding between nodes A and B in a parse tree, we want to associate with <code>GrammaticalRelation</code> X a such that:
 * <ul>
 * <li>the root of the pattern matches A, and</li>
 * <li>the pattern includes a special node label, "target", which matches B.</li>
 * </ul>
 * For example, for the grammatical relation <code>PREDICATE</code> which holds between a clause and its primary verb phrase, we might want to use the pattern
 * <code>"S < VP=target"</code>, in which the root will match a clause and the node labeled <code>"target"</code> will match the verb phrase.
 * <p>
 * For a given grammatical relation, the method {@link ProtoGrammaticalRelation#getRelatedNodes <code>getRelatedNodes()</code>} takes a <code>Tree</code> node
 * as an argument and attempts to return other nodes which have this grammatical relation to the argument node. By default, this method operates as follows: it
 * steps through the patterns in the pattern list, trying to match each pattern against the argument node, until it finds some matches. If a pattern matches,
 * all matching nodes (that is, each node which corresponds to node label "target" in some match) are returned as a list; otherwise the next pattern is tried.
 * <p>
 * 
 * @see GrammaticalRelation
 * @author Bill MacCartney
 * @author Galen Andrew (refactoring English-specific stuff)
 * @author Ilya Sherman (refactoring annotation-relation pairing)
 * @author Bernard (this class factored out of GrammaticalRelation)
 */
public class ProtoGrammaticalRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * This function is used to determine whether the GrammaticalRelation in question is one that was created to be a thin wrapper around a String
	 * representation by valueOf(String), or whether it is a full-fledged GrammaticalRelation created by direct invocation of the constructor.
	 * 
	 * @return Whether this relation is just a wrapper created by valueOf(String)
	 */
    public boolean isFromString() {
        return this.longName == null;
    }

    protected String shortName;

    protected String longName;

    protected ProtoGrammaticalRelation parent;

    protected List<ProtoGrammaticalRelation> children = new ArrayList<ProtoGrammaticalRelation>();

    protected Pattern sourcePattern;

    protected List<TregexPattern> targetPatterns = new ArrayList<TregexPattern>();

    protected String specific;

    public ProtoGrammaticalRelation(final String shortName, final String longName, final ProtoGrammaticalRelation parent, final String sourcePattern, final String[] targetPatterns, final String specificString) {
        this.shortName = shortName;
        this.longName = longName;
        this.specific = specificString;
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
        if (sourcePattern != null) {
            try {
                this.sourcePattern = Pattern.compile(sourcePattern);
            } catch (final java.util.regex.PatternSyntaxException e) {
                throw new RuntimeException("Bad pattern: " + sourcePattern);
            }
        } else {
            this.sourcePattern = null;
        }
        if (targetPatterns != null) {
            for (final String pattern : targetPatterns) {
                try {
                    final TregexPattern p = TregexPattern.compile(pattern);
                    this.targetPatterns.add(p);
                } catch (final Exception pe) {
                    throw new RuntimeException("Bad pattern: " + pattern, pe);
                }
            }
        }
    }

    protected void addChild(final ProtoGrammaticalRelation child) {
        this.children.add(child);
    }

    /**
	 * Given a <code>Tree</code> node <code>t</code>, attempts to return a list of nodes to which node <code>t</code> has this grammatical relation.
	 * 
	 * @param t
	 *            Target for finding governors of t related by this GR
	 * @param root
	 *            The root of the Tree
	 * @return Governor nodes to which t bears this GR
	 */
    public Collection<Tree> getRelatedNodes(final Tree t, final Tree root) {
        final Set<Tree> nodeList = new LinkedHashSet<Tree>();
        for (final TregexPattern p : this.targetPatterns) {
            if (root.value() == null) {
                root.setValue("ROOT");
            }
            final TregexMatcher m = p.matcher(root);
            while (m.find()) {
                if (m.getMatch() == t) {
                    nodeList.add(m.getNode("target"));
                }
            }
        }
        return nodeList;
    }

    /**
	 * Returns <code>true</code> iff the value of <code>Tree</code> node <code>t</code> matches the <code>sourcePattern</code> for this
	 * <code>GrammaticalRelation</code>, indicating that this <code>GrammaticalRelation</code> is one that could hold between <code>Tree</code> node
	 * <code>t</code> and some other node.
	 */
    public boolean isApplicable(final Tree t) {
        return t.value() != null && this.sourcePattern != null && this.sourcePattern.matcher(t.value()).matches();
    }

    public boolean isAncestor(final ProtoGrammaticalRelation gr0) {
        ProtoGrammaticalRelation gr = gr0;
        while (gr != null) {
            if (this == gr) return true;
            gr = gr.parent;
        }
        return false;
    }

    /**
	 * Returns short name (abbreviation) for this <code>GrammaticalRelation</code>.
	 */
    @Override
    public final String toString() {
        if (this.specific == null) return this.shortName;
        return this.shortName + '_' + this.specific;
    }

    public String getLongName() {
        return this.longName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getSpecific() {
        return this.specific;
    }

    /**
	 * Returns the parent of this <code>GrammaticalRelation</code>.
	 */
    public ProtoGrammaticalRelation getParent() {
        return this.parent;
    }

    /**
	 * Returns the children of this <code>GrammaticalRelation</code>.
	 */
    public List<? extends ProtoGrammaticalRelation> getChildren() {
        return this.children;
    }

    /**
	 * Returns the source patterns of this <code>GrammaticalRelation</code>.
	 */
    public Pattern getSourcePattern() {
        return this.sourcePattern;
    }

    /**
	 * Returns the target patterns of this <code>GrammaticalRelation</code>.
	 */
    public List<TregexPattern> getTargetPatterns() {
        return this.targetPatterns;
    }

    /**
	 * Compile list of strings into TregexPatterns
	 * 
	 * @param thesePatternStrings
	 *            array of pattern strings
	 * @return list of TregexPattern
	 */
    public static List<TregexPattern> compileTargetPatterns(final String[] thesePatternStrings) {
        final List<TregexPattern> thisPatternArray = new ArrayList<TregexPattern>();
        for (final String thisPatternString : thesePatternStrings) {
            try {
                final TregexPattern thisPattern = TregexPattern.compile(thisPatternString);
                thisPatternArray.add(thisPattern);
            } catch (final Exception pe) {
                throw new RuntimeException("Bad pattern: " + thisPatternString, pe);
            }
        }
        return thisPatternArray;
    }
}
