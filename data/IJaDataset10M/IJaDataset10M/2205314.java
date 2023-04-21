package edu.princeton.wordnet.pojos;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * WordNetPojos @author Bernard Bou
 */
@Entity
@Table(name = "lexdomains")
public class Lexdomain implements java.io.Serializable {

    private short lexdomainid;

    private String lexdomainname;

    private Character pos;

    private Set<Synset> synsets = new HashSet<Synset>(0);

    public Lexdomain() {
    }

    public Lexdomain(short lexdomainid) {
        this.lexdomainid = lexdomainid;
    }

    public Lexdomain(short lexdomainid, String lexdomainname, Character pos, Set<Synset> synsets) {
        this.lexdomainid = lexdomainid;
        this.lexdomainname = lexdomainname;
        this.pos = pos;
        this.synsets = synsets;
    }

    @Id
    @Column(name = "lexdomainid", unique = true, nullable = false)
    public short getLexdomainid() {
        return this.lexdomainid;
    }

    public void setLexdomainid(short lexdomainid) {
        this.lexdomainid = lexdomainid;
    }

    @Column(name = "lexdomainname", length = 32)
    public String getLexdomainname() {
        return this.lexdomainname;
    }

    public void setLexdomainname(String lexdomainname) {
        this.lexdomainname = lexdomainname;
    }

    @Column(name = "pos", length = 1)
    public Character getPos() {
        return this.pos;
    }

    public void setPos(Character pos) {
        this.pos = pos;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lexdomain")
    public Set<Synset> getSynsets() {
        return this.synsets;
    }

    public void setSynsets(Set<Synset> synsets) {
        this.synsets = synsets;
    }
}
