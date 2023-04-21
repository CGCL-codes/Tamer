package net.jdebate.db;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "Clause")
public class DBClause {

    @Id
    private int id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private DBLiteralSet literals;

    @OneToOne
    private DBPoll poll;

    public DBClause() {
    }

    public DBClause(DBPoll poll, DBLiteralSet literals) {
        this.poll = poll;
        this.literals = literals;
    }

    public int getId() {
        return id;
    }

    public DBLiteralSet getLiterals() {
        return literals;
    }

    public DBPoll getPoll() {
        return poll;
    }
}
