package net.sf.wgfa.beans;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author blair
 */
public class EntityIdBean {

    private int isAnon;

    private String id;

    /** Creates a new instance of EntityIdBean */
    public EntityIdBean(Resource r) {
        if (r.isAnon()) {
            id = r.getId().toString();
            isAnon = 1;
        } else {
            id = r.getURI();
            isAnon = 0;
        }
    }

    public int getIsAnon() {
        return isAnon;
    }

    public void setIsAnon(int isAnon) {
        this.isAnon = isAnon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
