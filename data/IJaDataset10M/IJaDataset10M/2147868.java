package es.randres.jdo.client.view.gtable;

import com.google.gwt.view.client.ProvidesKey;
import es.randres.jdo.client.entity.EntityLite;

public class TableKeyProvider<T extends EntityLite> implements ProvidesKey<T> {

    public TableKeyProvider() {
        super();
    }

    public Object getKey(T item) {
        return item.getIdentifier();
    }
}
