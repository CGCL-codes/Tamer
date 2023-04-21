package goodsjpi;

/**
 *  <code>PessimisticMetaobject</code>  for pessimistic scheme of synchronization.<br>
 * Or would it be realistic. <br>
 * Or Multiple Reader, one writer.
 * 
 * @author K.A. Knizhnik
 * @version 1.0
 */
public class PessimisticMetaobject extends BasicMetaobject {

    /**
   *  <code>beginWriteAccess</code> locks the object. That is, the objects storage 
   * manager is requested to objeain the lock from the server.
   *
   * @param obj a <code>Persistent</code> to be modified.
   */
    public void beginWriteAccess(Persistent obj) {
        if ((obj.state & Persistent.XLOCKED) == 0) {
            obj.storage.lock(obj, Protocol.lck_exclusive, Protocol.lckattr_wait);
            obj.state |= Persistent.XLOCKED;
        }
    }

    /**
   *  <code>beginReadAccess</code> is a no-op. So, no locking for read's. Multiple
   * readers, one writer.
   *
   * @param obj a <code>Persistent</code> object to be read.
   */
    public void beginReadAccess(Persistent obj) {
    }

    /**
   *  <code>endAccess</code> adds the object to the cahce managers transaction list
   *
   * @param obj a <code>Persistent</code> value
   */
    public void endAccess(Persistent obj) {
        int state = obj.state;
        if ((state & Persistent.TRANSWRITE) == 0 && (state & (Persistent.DIRTY | Persistent.XLOCKED)) != 0) {
            CacheManager mng = CacheManager.getCacheManager();
            mng.addToTransaction(obj);
        }
    }
}
