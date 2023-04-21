package test.foo.entities;

import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user
 */
@Entity
public class Command {

    private Long id;

    private String body;

    private Prikaz prikaz;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Prikaz getPrikaz() {
        return prikaz;
    }

    public void setPrikaz(Prikaz order) {
        this.prikaz = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Command other = (Command) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.body == null) ? (other.body != null) : !this.body.equals(other.body)) {
            return false;
        }
        if (this.prikaz != other.prikaz && (this.prikaz == null || !this.prikaz.equals(other.prikaz))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.body != null ? this.body.hashCode() : 0);
        hash = 97 * hash + (this.prikaz != null ? this.prikaz.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Command{" + "id=" + id + " body=" + body + " order=" + prikaz + '}';
    }
}
