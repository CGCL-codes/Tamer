package org.synthful.jdo.resources;

import java.io.Serializable;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.synthful.jdo.SynJDO;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TextResourceContent extends SynJDO<TextResourceContent> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String id;

    @Persistent
    private Text content;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setContent(Text content) {
        this.content = content;
    }

    public Text getContent() {
        return content;
    }

    public static TextResourceContent exists(PersistenceManager pm, String name) {
        Query query = pm.newQuery(String.class);
        query.setFilter("id == idParam");
        query.declareParameters("String idParam");
        return getFirst(query, name);
    }
}
