package backend.parser.tp;

import java.util.ArrayList;

public class Gene extends AbstractTPObject {

    private String accession;

    private String species;

    /**
	 * 
	 * @uml.property name="accession_alternative"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    private final ArrayList<String> accession_alternative = new ArrayList<String>();

    /**
	 * 
	 * @uml.property name="gene_products"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    private final ArrayList<String> gene_products = new ArrayList<String>();

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public final ArrayList<String> getAccession_alternative() {
        return accession_alternative;
    }

    public ArrayList<String> getGene_products() {
        return gene_products;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }
}
