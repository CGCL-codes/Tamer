package net.sf.semanticdebug.sembug;

import net.sf.semanticdebug.tag;
import java.util.*;

/**
 * @author Lieven Vaneeckhaute
 */
public class TagHandler {

    public static void main(String[] args) {
        TagHandler objectName;
    }

    public static void out(String out, boolean outcome) {
        System.out.print(out);
        if (outcome) System.out.println(" Success"); else System.out.println(" Failure");
    }

    Collection staticFunctions, activeFunctions, passiveGroups;

    ConstructorCollection constrCollection;

    TagDefinitions definitionTags;

    /**
	 * Create all the lists. 	 
	 */
    TagHandler() {
        staticFunctions = new ArrayList();
        activeFunctions = new ArrayList();
        passiveGroups = new ArrayList();
        constrCollection = new ConstructorCollection();
        definitionTags = new TagDefinitions();
    }

    /**
	 * Gets an iterator to the static tags. Iterator object must be cast to (Tag).
	 * @return the iterator.
	 */
    public Iterator getStatics() {
        return staticFunctions.iterator();
    }

    /**
	 * Gets an itator to the TagGroups used in passive functions.  
	 * @return itator to the TagGroups 
	 */
    public Iterator getGroups() {
        return passiveGroups.iterator();
    }

    /**
	 * Gets a ConstructorCollection object for lookup and adding constructors. 
	 * @return a ConstructorCollection object 
	 */
    public ConstructorCollection getConstructors() {
        return constrCollection;
    }

    /**
	 * Gets an iterator to the tags of active functions. Cast to (Tag).
	 * @return the iterator active functions
	 */
    public Iterator getActives() {
        return activeFunctions.iterator();
    }

    /**
	 * Adds a constructorreference. 
	 * @param constructorReference the constructorreference
	 * @param constructorIns the constructorInstance
	 */
    public void addConstruction(String constructorReference, String constructorIns) {
        constrCollection.add(new Tag(constructorIns, constructorReference));
    }

    /**
	 * Adds a definition. 
	 * @param previousRef the constructorreference
	 * @param code the constructorInstance
	 */
    public void addDefinition(String previousRef, String code, String newStateName) {
        DefinitionTag tag = new DefinitionTag(code, previousRef, newStateName);
        constrCollection.addDefinition(tag);
    }

    /**
	 * Adds a passivetag.
	 * @param passiveReference The reference the passive tag makes. 
	 * @param passiveIns The condition the passive tag contains.
	 */
    public void addPassive(String passiveReference, String passiveIns) {
        TagGroup groep = lookup(passiveReference);
        if (groep == null) {
            groep = new TagGroup(passiveReference);
            passiveGroups.add(groep);
        }
        groep.passiveFunctions.add(new Tag(passiveIns, passiveReference));
    }

    /**
	 * Looks up a taggroup for use in passivetags.  
	 * @param reference
	 * @return
	 */
    TagGroup lookup(String reference) {
        Iterator it = passiveGroups.iterator();
        while (it.hasNext()) {
            TagGroup temp = (TagGroup) it.next();
            if (temp.groupReference.equals(reference)) return temp;
        }
        return null;
    }

    /**
	 * Adds a dynamic active tag.
	 * @param activeReference The reference of the tag.
	 * @param activeIns The condition of the tag.
	 */
    public void addActive(String activeReference, String activeIns) {
        activeFunctions.add(new Tag(activeIns, activeReference));
    }

    /**
	 * Adds a static tag.
	 * @param staticCondition The condition of the tag. 
	 */
    public void addStatic(String staticCondition) {
        staticFunctions.add(new Tag(staticCondition, ""));
    }
}
