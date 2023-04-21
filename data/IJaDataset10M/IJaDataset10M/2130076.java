package uk.co.ordnancesurvey.rabbitparser.result.visitor.collector.base;

import java.util.HashSet;
import java.util.Set;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.result.BaseParsedResultVisitor;

/**
 * This {@link BaseParsedResultVisitor} collects all the instances of a
 * particular {@link IParsedPart} type in a parsed result and provides method
 * {@link #getFoundParsedParts()} to access them.
 * 
 * Use this visitor by creating a new instance and visiting one or more parsed
 * results. The next time you use {@link #getFoundParsedParts()}, this will
 * return the {@link Set} of {@link IParsedPart}s that are used in the visited
 * parsed results.
 * 
 * @author rdenaux
 * 
 */
public abstract class AbstractParsedPartCollector<PartType extends IParsedPart> extends BaseParsedResultVisitor {

    public Set<PartType> foundParts = new HashSet<PartType>();

    public Set<PartType> getFoundParsedParts() {
        return foundParts;
    }

    /**
     * This method adds aParsedPart to the {@link Set} of {@link #foundParts}.
     * Subclasses can use this method to fill the {@link Set} while visiting the
     * parsed result.
     * 
     * @param aParsedPart
     */
    protected void addPart(PartType aParsedPart) {
        foundParts.add(aParsedPart);
    }
}
