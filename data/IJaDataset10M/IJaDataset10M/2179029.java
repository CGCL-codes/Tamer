package net.sourceforge.ondex.parser.biopaxmodel;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Interface for unificationXref ontology class<br>
 * Use the net.sourceforge.ondex.parser.biopaxmodel.biopax_DASH_level2_DOT_owlFactory to create instances of this interface.
 * <p>(URI: http://www.biopax.org/release/biopax-level2.owl#unificationXref)</p>
 * <br>
 * RDF Schema Standard Properties <br>
 * 	comment : A unification defines a reference to an entity in an external resource that has the same biological identity as the referring entity. For example, if one wished to link from a database record, C, describing a chemical compound in a BioPAX data collection to a record, C', describing the same chemical compound in an external database, one would use a unification xref since records C and C' describe the same biological identity. Generally, unification xrefs should be used whenever possible, although there are cases where they might not be useful, such as application to application data exchange.

NOTE: Unification xrefs in physical entities are essential for data integration, but are less important in interactions. This is because unification xrefs on the physical entities in an interaction can be used to compute the equivalence of two interactions of the same type.

NOTE: An xref in a protein pointing to a gene, e.g. in the LocusLink database, would not be a unification xref since the two entities do not have the same biological identity (one is a protein, the other is a gene).  Instead, this link should be a captured as a relationship xref.^^http://www.w3.org/2001/XMLSchema#string <br>
 * <br>
 * <br>
 */
public interface unificationXref extends net.sourceforge.ondex.parser.biopaxmodel.xref, com.ibm.adtech.jastor.Thing {

    /**
	 * The rdf:type for this ontology class
     */
    public static final Resource TYPE = ResourceFactory.createResource("http://www.biopax.org/release/biopax-level2.owl#unificationXref");
}
