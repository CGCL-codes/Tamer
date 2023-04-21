package models;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Author extends Model {

    @Required
    public String username;

    @Required
    public String password;

    @Required
    public String email;

    public String toString() {
        return username;
    }
}
