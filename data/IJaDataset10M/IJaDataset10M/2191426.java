package voorraadbeheer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import gilgamesh.annotations.HashCode;
import gilgamesh.beans.AbstractBean;
import gilgamesh.beans.BoundProperty;

@Entity
@HashCode
public class Persoon extends AbstractBean {

    @Id
    private long id;

    @BoundProperty
    private String voornaam;

    @BoundProperty
    private String achternaam;

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
