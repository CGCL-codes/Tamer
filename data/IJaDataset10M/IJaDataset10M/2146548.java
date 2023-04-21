package net.sf.dozer.util.mapping.vo;

import java.util.List;
import java.util.Set;

/**
 * @author wojtek.kiersztyn
 * @author dominic.peciuch
 * 
 */
public class Individuals {

    private List usernames;

    private Individual individual;

    private String simpleField;

    private String[] secondNames;

    private Aliases aliases;

    private Set addressSet;

    private String[] thirdNames;

    public List getUsernames() {
        return usernames;
    }

    public void setUsernames(List usernames) {
        this.usernames = usernames;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public String getSimpleField() {
        return simpleField;
    }

    public void setSimpleField(String simpleField) {
        this.simpleField = simpleField;
    }

    public String[] getSecondNames() {
        return secondNames;
    }

    public void setSecondNames(String[] secondNames) {
        this.secondNames = secondNames;
    }

    public Aliases getAliases() {
        return aliases;
    }

    public void setAliases(Aliases aliases) {
        this.aliases = aliases;
    }

    public Set getAddressSet() {
        return addressSet;
    }

    public void setAddressSet(Set addressSet) {
        this.addressSet = addressSet;
    }

    public String getThirdNameElement1() {
        return this.thirdNames[0];
    }
}
