package backend.parser.tf;

import java.util.LinkedList;

/**
 * @author Benny
 * @version 1.00 05.10.2004
 */
class GeneObject {

    private String acc_nr = new String();

    private String name = new String();

    private String tax_id = new String();

    private Integer IId = null;

    private String description = new String();

    private LinkedList<String> synonyms = null;

    private LinkedList<String[]> links = null;

    private LinkedList<String> encoded_factors = null;

    protected void setAcc_nr(String acc_nr) {
        this.acc_nr = acc_nr;
    }

    protected void setIId(Integer i) {
        this.IId = i;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setSynonyms(LinkedList<String> synonyms) {
        this.synonyms = synonyms;
    }

    protected void setTax_id(String tax_id) {
        this.tax_id = tax_id;
    }

    protected void setExternalDatabaseLinks(LinkedList<String[]> links) {
        this.links = links;
    }

    protected void setEncodedFactor(LinkedList<String> encoded_factors) {
        this.encoded_factors = encoded_factors;
    }

    protected void setExplicitName(String explicit_name) {
        synonyms.add(explicit_name);
    }

    protected void setDescription(String description) {
        this.description = description;
        ;
    }

    protected String getAcc_nr() {
        return acc_nr;
    }

    protected Integer getIId() {
        return IId;
    }

    protected String getName() {
        return name;
    }

    protected LinkedList<String> getSynonyms() {
        return synonyms;
    }

    protected String getTax_id() {
        return tax_id;
    }

    protected LinkedList<String[]> getExternalDatabaseLinks() {
        return this.links;
    }

    protected LinkedList<String> getEncodedFactor() {
        return encoded_factors;
    }

    protected String getDescription() {
        return description;
    }
}
