package ac.jp.u_tokyo.SyncLib.dictionaries;

import static ac.jp.u_tokyo.SyncLib.util.Helper.apply;
import static ac.jp.u_tokyo.SyncLib.util.Helper.conflict;
import static ac.jp.u_tokyo.SyncLib.util.Helper.inPlaceApply;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ac.jp.u_tokyo.SyncLib.IDSync;
import ac.jp.u_tokyo.SyncLib.Mod;
import ac.jp.u_tokyo.SyncLib.ModTypeUnsupportedException;
import ac.jp.u_tokyo.SyncLib.NullMod;
import ac.jp.u_tokyo.SyncLib.PrimMod;
import ac.jp.u_tokyo.SyncLib.StateSync;
import ac.jp.u_tokyo.SyncLib.SynchronizationFailedException;
import ac.jp.u_tokyo.SyncLib.Value2Mod;

public abstract class DynamicGetBase<K> extends StateSync {

    protected DynamicGetBase() {
        super(new Object[] { null, null });
    }

    protected static final DictMod NULLDICTMOD = new DictMod(new HashMap(0));

    protected abstract Mod[] internalSync(Mod[] modifications, K oldKey, Map<K, Object> nullableDict, Object value) throws SynchronizationFailedException;

    private Object getValue() {
        return getDict().get(getKey());
    }

    private Map<K, Object> getDict() {
        return (Map<K, Object>) getAttribute(1);
    }

    private K getKey() {
        return (K) getAttribute(0);
    }

    private void setKey(K key) {
        setAttribute(0, key);
    }

    private void modifyDict(Mod<Map<K, Object>> dict) {
        modifyAttribute(1, dict);
    }

    public int getParaCount() {
        return 3;
    }

    public DynamicGetBase(Object[] initialState) {
        super(initialState);
    }

    public Mod[] resynchronize(Object[] values, Mod[] modifications) throws SynchronizationFailedException {
        Mod[] result = internalSync(modifications, (K) values[0], (Map<K, Object>) values[1], values[2]);
        if (values[1] == null && result[1] instanceof NullMod) {
            result[1] = NULLDICTMOD;
        }
        setKey(apply((Mod<K>) result[0], (K) values[0]));
        modifyDict(Value2Mod.findMod(getDict(), (Map<K, Object>) result[1].apply(values[1])));
        return result;
    }

    public Mod[] synchronize(Mod[] modifications) throws SynchronizationFailedException {
        Mod[] result;
        result = internalSync(modifications, getKey(), getDict(), getValue());
        setKey(inPlaceApply((Mod<K>) result[0], getKey()));
        modifyDict(result[1]);
        return result;
    }

    protected Mod[] changeDictAndValueWithFixedKey(Mod[] modifications, final K key, Map<K, Object> dict, Object value, boolean dictOverValue) throws SynchronizationFailedException {
        DictMod<K, Object> dictMod1;
        if (modifications[1] instanceof NullMod) dictMod1 = NULLDICTMOD; else dictMod1 = ((DictMod<K, Object>) modifications[1]);
        Mod mod1 = dictMod1.getMod(key);
        Mod mod2 = modifications[2];
        if (conflict(mod1, mod2)) throw new SynchronizationFailedException();
        Mod vresult = IDSync.mergeWithValue(mod1, mod2, dict.get(key), value, dictOverValue);
        Mod dresult = DictMod.replace(dictMod1, key, vresult);
        if (dresult.equals(NULLDICTMOD) && (modifications[1] instanceof NullMod)) dresult = NullMod.INSTANCE;
        Mod kresult;
        if (dresult.equals(modifications[1]) && vresult.equals(modifications[2])) kresult = modifications[0]; else {
            kresult = new PrimMod<K>(key);
            assert !kresult.isConflict(modifications[0]);
        }
        Mod[] result = new Mod[] { kresult, dresult, vresult };
        return result;
    }
}
