package org.escapek.core.cmdb.entities;

import java.util.Date;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author  i67i-24
 */
@Entity
@Table(name = "SNAPSHOT")
public class Snapshot {

    private Long Id;

    private Date timestamp;

    private Database database;

    private HashSet stats = new HashSet();

    /**
	 * @return  the id
	 * @uml.property  name="id"
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return Id;
    }

    /**
	 * @param id  the id to set
	 * @uml.property  name="id"
	 */
    public void setId(Long id) {
        this.Id = id;
    }

    /**
	 * @return  the timestamp
	 * @uml.property  name="timestamp"
	 */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
	 * @param timestamp  the timestamp to set
	 * @uml.property  name="timestamp"
	 */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
	 * @return  the database
	 * @uml.property  name="database"
	 */
    @ManyToOne
    @JoinColumn(name = "database_id")
    public Database getDatabase() {
        return database;
    }

    /**
	 * @param database  the database to set
	 * @uml.property  name="database"
	 */
    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
	 * @return  the stats
	 * @uml.property  name="stats"
	 */
    public HashSet getStats() {
        return stats;
    }

    /**
	 * @param stats  the stats to set
	 * @uml.property  name="stats"
	 */
    public void setStats(HashSet stats) {
        this.stats = stats;
    }
}
