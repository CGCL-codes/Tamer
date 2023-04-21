package org.nbn.ontodas.jsonclasses;

public class GeneProduct {

    int id;

    int species_id;

    int dbxref_id;

    String full_name;

    String symbol;

    public GeneProduct(int id, int species_id, int dbxref_id, String full_name, String symbol) {
        super();
        this.id = id;
        this.species_id = species_id;
        this.dbxref_id = dbxref_id;
        this.full_name = full_name;
        this.symbol = symbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(int species_id) {
        this.species_id = species_id;
    }

    public int getDbxref_id() {
        return dbxref_id;
    }

    public void setDbxref_id(int dbxref_id) {
        this.dbxref_id = dbxref_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
