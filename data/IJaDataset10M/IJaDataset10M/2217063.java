package edu.pw.treegrid.server.message;

import java.util.List;
import edu.pw.treegrid.server.model.DomainObject;

/** 
 * @author Piotrek
 */
public class ResponseFetchChilds {

    private List<DomainObject> domainObjects;

    public List<DomainObject> getDomainObjects() {
        return domainObjects;
    }

    public void setDomainObjects(List<DomainObject> domainObjects) {
        this.domainObjects = domainObjects;
    }
}
