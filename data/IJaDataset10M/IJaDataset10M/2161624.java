package de.cowabuja.pawotag.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "Tag.ByTitle", query = "SELECT t " + "FROM Tag t WHERE t.title=:title") })
public class Tag {

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true)
    private String title;

    public Tag() {
    }

    public Tag(String title) {
        setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
