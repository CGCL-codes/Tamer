package org.argouml.uml.cognitive.critics;

import java.util.HashSet;
import java.util.Set;
import org.argouml.cognitive.Critic;
import org.argouml.profile.Profile;

/**
 * Profile which contains the critics that define optional good practices for
 * general UML models
 * 
 * @author maurelio1234
 */
public class ProfileGoodPractices extends Profile {

    private Set<Critic> critics = new HashSet<Critic>();

    /**
     * Default Constructor 
     */
    public ProfileGoodPractices() {
        critics.add(new CrEmptyPackage());
        critics.add(new CrNodesOverlap());
        critics.add(new CrZeroLengthEdge());
        critics.add(new CrCircularComposition());
        critics.add(new CrMissingAttrName());
        critics.add(new CrMissingClassName());
        critics.add(new CrMissingStateName());
        critics.add(new CrMissingOperName());
        critics.add(new CrNonAggDataType());
        critics.add(new CrSubclassReference());
        critics.add(new CrTooManyAssoc());
        critics.add(new CrTooManyAttr());
        critics.add(new CrTooManyOper());
        critics.add(new CrTooManyTransitions());
        critics.add(new CrTooManyStates());
        critics.add(new CrTooManyClasses());
        critics.add(new CrWrongLinkEnds());
        critics.add(new CrUtilityViolated());
        this.setCritics(critics);
    }

    @Override
    public String getDisplayName() {
        return "Critics for Good Practices";
    }

    public String getProfileIdentifier() {
        return "GoodPractices";
    }
}
