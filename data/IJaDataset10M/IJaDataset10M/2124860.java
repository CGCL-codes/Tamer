package jasa.architecture;

import jason.asSyntax.*;

/**
 * Structure that associates a manual (ontology) with its ontological annotation
 * @author Alice Grandi
 */
public class ManInfo {

    private Term annot;

    private String name;

    public ManInfo(String name, Atom ontShortName) {
        annot = ASSyntax.createStructure("o", ontShortName);
        this.name = name;
    }

    /**
	 * @return the ontology name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the ontology annotation
	 */
    public Term getAnnot() {
        return annot;
    }
}
