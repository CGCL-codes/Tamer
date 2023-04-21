package bg.unisofia.fmi.kanban.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * A common interface for all elements representing buisness logic.
 *
 * @author nikolay.grozev
 */
public interface IModelElement extends IsSerializable {

    Integer getId();

    void setId(Integer id);
}
