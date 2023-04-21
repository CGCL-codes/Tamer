package duos.model.cdm.data;

import java.util.SortedSet;
import duos.model.cdm.ident.User;
import duos.model.cdm.ident.access.AccessRole;

/**
 * <h2>Summary</h2><br /> {@code DataObject} serves as a primary unit of reference,
 * within DUOS. Every object that may be retrieved with a DUOS {@link DataStore}
 * will be represented with a DUOS {@code DataObject}.<br />
 * <br />
 * 
 * <h2>Usage</h2><br />
 * 
 * <h3>Membership</h3> Every {@code DataObject} will be a member of at least one
 * {@link DataSet}
 * 
 * <h3>Serialization</h3> Every {@code DataObject} is represented with a
 * <em class="concept">globally unique</em> {@code SerializationID}
 * 
 * @author Sean Champ, <spchamp@users.sourceforge.net>
 * 
 */
public abstract class AbstractDataObject<C extends DataObjectClass> {

    protected SerializationID serializationID;

    public AbstractDataObject() {
    }

    public SerializationID getSerializationID() {
        return serializationID;
    }

    public abstract java.util.Hashtable<AccessRole<User, AbstractDataObject<C>>, SortedSet<User>> getUsers();
}
