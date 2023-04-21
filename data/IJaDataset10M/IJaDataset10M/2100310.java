package ac.jp.u_tokyo.SyncLib.Tests;

import static ac.jp.u_tokyo.SyncLib.Helper.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ac.jp.u_tokyo.SyncLib.IDSync;
import ac.jp.u_tokyo.SyncLib.Mod;
import ac.jp.u_tokyo.SyncLib.SynchronizationFailedException;

public class IDSyncTest {

    IDSync _sync = new IDSync(true);

    IDSync _vsync = new IDSync(false);

    @Test
    public void testSyncNulls() throws SynchronizationFailedException {
        final ArrayList<Mod> nulls = toArrayList(null, null);
        final List result = _sync.synchronize(nulls);
        assertEquals(nulls, result);
    }

    @Test
    public void testResyncNulls() throws SynchronizationFailedException {
        ArrayList nulls = toArrayList(null, null);
        final List result = _sync.resynchronize(nulls, nulls);
        assertEquals(nulls, result);
    }

    @Test
    public void testResync1to2() throws SynchronizationFailedException {
        final ArrayList<Mod> nulls = toArrayList(null, null);
        ArrayList values = toArrayList(1, 2);
        final List result = _sync.resynchronize(values, nulls);
        assertEquals(createPrimitiveModList(1, 1), result);
    }

    @Test
    public void testResync2to1() throws SynchronizationFailedException {
        final ArrayList<Mod> nulls = toArrayList(null, null);
        ArrayList values = toArrayList(1, 2);
        final List result = _vsync.resynchronize(values, nulls);
        assertEquals(createPrimitiveModList(2, 2), result);
    }

    @Test
    public void testKeepMod() throws SynchronizationFailedException {
        List<Mod> prims = createPrimitiveModList(3, null);
        ArrayList values = toArrayList(1, 2);
        final List result = _sync.resynchronize(values, prims);
        assertEquals(createPrimitiveModList(3, 3), result);
    }

    @Test(expected = SynchronizationFailedException.class)
    public void testConflictMod() throws SynchronizationFailedException {
        List<Mod> prims = createPrimitiveModList(3, 4);
        ArrayList values = toArrayList(1, 2);
        _sync.resynchronize(values, prims);
    }
}
