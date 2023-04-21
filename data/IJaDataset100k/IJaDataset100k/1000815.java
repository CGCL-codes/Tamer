package icescrum2.webservice.impl.wrappers;

import icescrum2.dao.model.IEntity;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "collection")
public class ListWrapper<E extends IEntity> {

    private Collection<E> items;

    public ListWrapper() {
    }

    @XmlElement(name = "item")
    public Collection<E> getItems() {
        return items;
    }

    public void setItems(Collection<E> i) {
        items = i;
    }
}
