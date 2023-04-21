package peertrust.filter.interfaces;

/**
 * The ProtectedResourcePolicyFilter has a hastable in which the resource content
 * which the user requested is stored after trust negotiation with the session id
 * as the key. This interface represents this hashtable by offering methods for
 * adding and removing items.
 * @author Sebastian Wittler
 */
public interface ISessionStoreList {

    public void addToList(String key, ISessionStoreItem item);
}
