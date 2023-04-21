package net.sf.joafip.store.entity.objectio;

import java.util.HashMap;
import java.util.Map;
import net.sf.joafip.NotStorableClass;

/**
 * 
 * implemementation using HashMap<br>
 */
@NotStorableClass
public class WeakList {

    private final Map<WeakObjectIdentityKey, WeakObjectIdentityKey> map = new HashMap<WeakObjectIdentityKey, WeakObjectIdentityKey>();

    public void clear() {
        map.clear();
    }

    public WeakObjectIdentityKey remove(final IObjectIdentityKey objectIdentityKey) {
        return map.remove(objectIdentityKey);
    }

    public void add(final WeakObjectIdentityKey weakObjectIdentityKey) {
        map.put(weakObjectIdentityKey, weakObjectIdentityKey);
    }
}
