package net.jdebate.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LiteralSet")
public class DBLiteralSet {

    @Id
    private int id;

    @ManyToMany
    private Set<DBLiteralRef> literals;

    @ManyToMany
    private Collection<DBLiteralSet> extensions = new ArrayList<DBLiteralSet>();

    @ManyToOne
    private DBClause clause;

    public DBLiteralSet() {
        literals = new HashSet<DBLiteralRef>();
    }

    public DBLiteralSet(Collection<DBLiteralRef> literals) {
        this.literals = new HashSet<DBLiteralRef>(literals);
    }

    public DBClause getClause() {
        return clause;
    }

    public void setClause(DBClause clause) {
        this.clause = clause;
    }

    public int getId() {
        return id;
    }

    public Set<DBLiteralRef> getLiterals() {
        return literals;
    }

    public Collection<DBLiteralSet> getExtensions() {
        return extensions;
    }
}
