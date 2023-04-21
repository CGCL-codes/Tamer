package memops.general;

public interface Notifier {

    /**
  * The function to be called after a create/update/delete operation in the API
  *
  * @param object  The object which has been created/updated/deleted.
  */
    void notifyFunc(memops.api.Implementation.MemopsObject object);
}
