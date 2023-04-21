package xades4j.properties.data;

import java.util.Collection;
import xades4j.utils.CollectionUtils;

/**
 *
 * @author Luís
 */
public final class CommitmentTypeData implements PropertyDataObject {

    private String description, uri;

    private Collection<String> objReferences;

    public CommitmentTypeData(String uri) {
        this(uri, null);
    }

    public CommitmentTypeData(String uri, String description) {
        this.uri = uri;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<String> getObjReferences() {
        return objReferences;
    }

    public void setObjReferences(Collection<String> objReferences) {
        this.objReferences = objReferences;
    }

    public void addObjReferences(String objRef) {
        this.objReferences = CollectionUtils.newIfNull(objReferences, 2);
        this.objReferences.add(objRef);
    }

    public String getUri() {
        return uri;
    }
}
