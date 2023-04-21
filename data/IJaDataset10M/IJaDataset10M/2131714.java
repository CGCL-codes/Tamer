package ac.jp.u_tokyo.SyncLib.Dictionary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ac.jp.u_tokyo.SyncLib.Helper;
import ac.jp.u_tokyo.SyncLib.IDSync;
import ac.jp.u_tokyo.SyncLib.Mod;
import ac.jp.u_tokyo.SyncLib.PrimMod;
import ac.jp.u_tokyo.SyncLib.Sync;
import ac.jp.u_tokyo.SyncLib.SynchronizationFailedException;
import ac.jp.u_tokyo.SyncLib.Dictionary.DictMod.*;

/**
 * p1[k]=p2 & p3[k]=p4 & p1\k=p3\k
 * 
 * @author XiongYingfei
 */
public class StaticExchange<Key> implements Sync {

    private ExchangeHelper<Key> _helper;

    private Key _key;

    public StaticExchange(Key key, int priority1, int priority2, int priority3, int priority4) {
        super();
        _helper = new ExchangeHelper<Key>(priority1, priority2, priority3, priority4);
        _key = key;
    }

    public StaticExchange(Key key) {
        super();
        _key = key;
    }

    public void cancelRevertPoint() {
    }

    public void revert() {
    }

    public void setRevertPoint() {
    }

    public int getParaCount() {
        return 4;
    }

    public int getPriority(List<Mod> modifications) {
        return PRIORITY_DETERMINED;
    }

    public int getPriority(List<Object> values, List<Mod> modifications) {
        return PRIORITY_DETERMINED;
    }

    @SuppressWarnings("unchecked")
    public List<Mod> synchronize(List<Mod> modifications) throws SynchronizationFailedException {
        if (PrimMod.NULL.equals(modifications.get(0)) || PrimMod.NULL.equals(modifications.get(2))) {
            return createNullMod(modifications);
        }
        assert modifications.get(0) == null || modifications.get(0) instanceof DictMod;
        assert modifications.get(2) == null || modifications.get(2) instanceof DictMod;
        final DictMod<Key, Object> dictMod1 = (DictMod) modifications.get(0);
        final DictMod<Key, Object> dictMod2 = (DictMod) modifications.get(2);
        final Mod singleMod1 = modifications.get(1);
        final Mod singleMod2 = modifications.get(3);
        return _helper.staticSynchronize(_key, dictMod1, dictMod2, singleMod1, singleMod2);
    }

    private List<Mod> createNullMod(List<Mod> mods) throws SynchronizationFailedException {
        for (int i = 0; i <= 3; i++) {
            if (PrimMod.NULL.isConflict(mods.get(i))) throw new SynchronizationFailedException();
        }
        return Helper.toArrayList((Mod) PrimMod.NULL, PrimMod.NULL, PrimMod.NULL, PrimMod.NULL);
    }

    public List<Mod> resynchronize(List<Object> values, List<Mod> modifications) throws SynchronizationFailedException {
        if (PrimMod.NULL.equals(modifications.get(0)) || PrimMod.NULL.equals(modifications.get(2))) {
            return createNullMod(modifications);
        }
        assert modifications.get(0) == null || modifications.get(0) instanceof DictMod;
        assert modifications.get(2) == null || modifications.get(2) instanceof DictMod;
        DictMod<Key, Object> dictMod1 = (DictMod) modifications.get(0);
        DictMod<Key, Object> dictMod2 = (DictMod) modifications.get(2);
        Map<Key, Object> map1 = (Map<Key, Object>) values.get(0);
        Map<Key, Object> map2 = (Map<Key, Object>) values.get(2);
        final Mod singleMod1 = modifications.get(1);
        final Mod singleMod2 = modifications.get(3);
        final Object v1 = values.get(1);
        final Object v2 = values.get(3);
        return _helper.resynchronize(_key, dictMod1, dictMod2, map1, map2, singleMod1, singleMod2, v1, v2);
    }

    public void resetPriority(int priority1, int priority2, int priority3, int priority4) {
        _helper._priority1 = priority1;
        _helper._priority2 = priority2;
        _helper._priority3 = priority3;
        _helper._priority4 = priority4;
    }
}
