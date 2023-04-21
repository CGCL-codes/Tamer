package be.oniryx.lean.session;

import be.oniryx.lean.entity.*;
import javax.ejb.Local;
import javax.ejb.Remove;
import java.util.List;
import java.util.HashSet;
import java.util.Collections;
import org.jboss.seam.annotations.Destroy;

/**
 * User: cedric
 * Date: May 11, 2009
 */
@Local
public interface ItemSuggest {

    List<ItemState> getItemStates();

    List<ItemState> getItemStates(Object p);

    Long getId();

    void setId(Long id);

    public Long getItemId();

    void setItemId(Long itemId);

    String getName();

    void setName(String name);

    public void remove();
}
