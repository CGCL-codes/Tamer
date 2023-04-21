package models;

import java.util.Date;
import play.data.validation.Required;
import siena.Id;
import siena.Index;
import siena.Model;

public abstract class AbstractUnit extends Model {

    /** The Information Unit id */
    @Id
    public Long id;

    public Date created;

    public Date modificated;

    /** The author of this information unit */
    @Index("author_index")
    @Required
    public User author;
}
