package go;

import java.util.Set;

public class Term {

    private int id;

    private String accession;

    private String tname;

    private String definition;

    private String comment;

    private Set<String> synonym;

    private Set<Term> is_a;

    private Set<Term> part_of;

    private Set<DBXrefClass> tdbxref;

    private Set<AssociationClass> association;

    public Term() {
    }

    public int getId() {
        return id;
    }

    public String getAccession() {
        return accession;
    }

    public Set<AssociationClass> getAssociation() {
        return association;
    }

    public String getComment() {
        return comment;
    }

    public String getDefinition() {
        return definition;
    }

    public Set<Term> getIs_a() {
        return is_a;
    }

    public Set<Term> getPart_of() {
        return part_of;
    }

    public Set<DBXrefClass> getTdbxref() {
        return tdbxref;
    }

    public Set<String> getSynonym() {
        return synonym;
    }

    public String getTname() {
        return tname;
    }
}
