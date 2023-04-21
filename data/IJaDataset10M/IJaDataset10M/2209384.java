package at.ac.univie.mminf.luceneSKOS.util;

import jena.schemagen;

/**
 * Uses the JENA schema generator to convert the used OWL ontologies/vocabs to corresponding Java classes.
 *
 * @author Bernhard Haslhofer <bernhard.haslhofer@univie.ac.at>
 * @auhtor Niko Popitsch <niko.popitsch@univie.ac.at>
 *
 */
public class SchemaGenerator {

    public static void main(String[] args) {
        args = new String[] { "-i", "src/main/resources/skos.rdf", "--package", "at.ac.univie.mminf.luceneSKOS.skos", "-n", "SKOS", "--ontology", "true", "--owl", "true", "-e", "RDF/XML", "--inference", "true", "-o", "src/main/java/at/ac/univie/mminf/luceneSKOS/skos" };
        System.out.println("publish SKOS vocabulary ...");
        schemagen.main(args);
        System.out.println("vocabularies published.");
    }
}
