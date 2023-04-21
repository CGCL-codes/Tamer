package org.escapek.core.nodes.entities;

import java.util.Date;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SNAPSHOT")
public class Snapshot {

    private Long Id;

    private Date timestamp;

    private Database database;

    private HashSet stats = new HashSet();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @ManyToOne
    @JoinColumn(name = "database_id")
    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public HashSet getStats() {
        return stats;
    }

    public void setStats(HashSet stats) {
        this.stats = stats;
    }
}
