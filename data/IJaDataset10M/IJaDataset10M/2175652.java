package org.waveprotocol.wave.model.conversation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import junit.framework.TestCase;
import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.util.SimpleMap;
import org.waveprotocol.wave.model.wave.ObservableMapImpl;
import java.util.Collection;

/**
 * Tests for the {@link AnchorManager}.
 *
 */
public final class AnchorManagerTest extends TestCase {

    private Object parasite;

    private Object[] parasites;

    private SimpleMap<String, Integer> items;

    private AnchorManager<String, Integer, Object> target;

    @Override
    protected void setUp() throws Exception {
        parasite = new Object();
        parasites = new Object[3];
        for (int i = 0; i < parasites.length; i++) {
            parasites[i] = new Object();
        }
        ObservableMapImpl<String, Integer> map = ObservableMapImpl.create();
        items = map;
        target = AnchorManager.<String, Integer, Object>create(map);
    }

    /**
   * Creates a collection of the same implementation type as created by
   * AnchorManager, in order to get .equals() to work.
   *
   * @param xs elements for the collection
   * @return a collection describing {@code xs}.
   */
    private static <T> Collection<T> collection(T... xs) {
        return CollectionUtils.newHashSet(xs);
    }

    private static <T> Collection<T> collection(Collection<T> xs) {
        return CollectionUtils.newHashSet(xs);
    }

    public void testAddItemThenSingleParasiteMakesAttached() {
        items.put("a", 1);
        target.attachParasite("a", parasite);
        assertEquals(collection(parasite), target.getAttached().get(1));
    }

    public void testAddItemThenManyParasitesMakesAttached() {
        items.put("a", 1);
        for (Object parasite : parasites) {
            target.attachParasite("a", parasite);
        }
        assertEquals(collection(parasites), target.getAttached().get(1));
    }

    public void testAddSingleParasiteThenItemMakesAttached() {
        target.attachParasite("a", parasite);
        items.put("a", 1);
        assertEquals(collection(parasite), target.getAttached().get(1));
    }

    public void testAddManyParasitesThenItemMakesAttached() {
        for (Object parasite : parasites) {
            target.attachParasite("a", parasite);
        }
        items.put("a", 1);
        assertEquals(collection(parasites), target.getAttached().get(1));
    }

    public void testAddNoItemParasiteMakesUnattached() {
        target.attachParasite("a", parasite);
        assertEquals(collection(parasite), collection(target.getUnattached()));
    }

    public void testAddNoLocationParasiteMakesUnattached() {
        target.attachParasite(null, parasite);
        assertEquals(collection(parasite), collection(target.getUnattached()));
    }

    public void testAddManyNoItemParasitesMakesUnattached() {
        for (Object parasite : parasites) {
            target.attachParasite("a", parasite);
        }
        assertEquals(collection(parasites), collection(target.getUnattached()));
    }

    public void testAddManyNoLocationParasitesMakesUnattached() {
        for (Object parasite : parasites) {
            target.attachParasite(null, parasite);
        }
        assertEquals(collection(parasites), collection(target.getUnattached()));
    }

    public void testRemoveItemDetaches() {
        items.put("a", 1);
        target.attachParasite("a", parasites[0]);
        target.attachParasite("a", parasites[1]);
        target.attachParasite("b", parasites[2]);
        items.remove("a");
        assertEquals(null, target.getAttached().get(1));
        assertEquals(collection(parasites), CollectionUtils.newHashSet(target.getUnattached()));
    }

    public void testRemoveParasitesRemovesAttached() {
        items.put("a", 1);
        target.attachParasite("a", parasites[0]);
        target.attachParasite("a", parasites[1]);
        target.attachParasite("b", parasites[2]);
        items.remove("a");
        assertEquals(null, target.getAttached().get(1));
        assertEquals(collection(parasites), CollectionUtils.newHashSet(target.getUnattached()));
    }

    /**
   * Creates and attaches a mock listener.
   */
    @SuppressWarnings("unchecked")
    private AnchorManager.Listener<Integer, Object> mockListener() {
        AnchorManager.Listener<Integer, Object> l = mock(AnchorManager.Listener.class);
        target.addListener(l);
        return l;
    }

    public void testAddItemThenSingleParasiteBroadcastsAttachEvents() {
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        items.put("a", 1);
        target.attachParasite("a", parasite);
        verify(listener).onAttached(1, collection(parasite));
        verifyNoMoreInteractions(listener);
    }

    public void testAddParasiteThenItemBroadcastsAttachDetachAttachEvents() {
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        target.attachParasite("a", parasite);
        items.put("a", 1);
        verify(listener).onAttached(null, collection(parasite));
        verify(listener).onDetached(null, collection(parasite));
        verify(listener).onAttached(1, collection(parasite));
        verifyNoMoreInteractions(listener);
    }

    public void testAddParasiteThenOtherItemBroadcastsAttachEvents() {
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        target.attachParasite("a", parasite);
        items.put("b", 1);
        verify(listener).onAttached(null, collection(parasite));
        verifyNoMoreInteractions(listener);
    }

    public void testRemoveAttachedParasiteBroadcastsDetachEvents() {
        target.attachParasite("a", parasite);
        items.put("a", 1);
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        target.detachParasite("a", parasite);
        verify(listener).onDetached(1, collection(parasite));
        verifyNoMoreInteractions(listener);
    }

    public void testRemoveItemBroadcastsDetachAttachEvents() {
        target.attachParasite("a", parasite);
        items.put("a", 1);
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        items.remove("a");
        verify(listener).onDetached(1, collection(parasite));
        verify(listener).onAttached(null, collection(parasite));
        verifyNoMoreInteractions(listener);
    }

    public void testAddItemThenManyParasitesBroadcastsAttachEvents() {
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        items.put("a", 1);
        target.attachParasite("a", parasites[0]);
        target.attachParasite("a", parasites[1]);
        target.attachParasite("b", parasites[2]);
        verify(listener).onAttached(1, collection(parasites[0]));
        verify(listener).onAttached(1, collection(parasites[1]));
        verify(listener).onAttached(null, collection(parasites[2]));
        verifyNoMoreInteractions(listener);
    }

    public void testAddManyParasitesThenItemBroadcastsAttachEvents() {
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        target.attachParasite("a", parasites[0]);
        target.attachParasite("a", parasites[1]);
        target.attachParasite("b", parasites[2]);
        items.put("a", 1);
        verify(listener).onAttached(null, collection(parasites[0]));
        verify(listener).onAttached(null, collection(parasites[1]));
        verify(listener).onAttached(null, collection(parasites[2]));
        verify(listener).onDetached(null, collection(parasites[0], parasites[1]));
        verify(listener).onAttached(1, collection(parasites[0], parasites[1]));
        verifyNoMoreInteractions(listener);
    }

    public void testRemoveItemFromMultiAttachedStateBroadcastsDetachAttachEvents() {
        target.attachParasite("a", parasites[0]);
        target.attachParasite("a", parasites[1]);
        target.attachParasite("b", parasites[2]);
        items.put("a", 1);
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        items.remove("a");
        verify(listener).onDetached(1, collection(parasites[0], parasites[1]));
        verify(listener).onAttached(null, collection(parasites[0], parasites[1]));
        verifyNoMoreInteractions(listener);
    }

    public void testRemoveParasiteFromMultiAttachedStateBroadcastsDetachEvents() {
        target.attachParasite("a", parasites[0]);
        target.attachParasite("a", parasites[1]);
        target.attachParasite("b", parasites[2]);
        items.put("a", 1);
        AnchorManager.Listener<Integer, Object> listener = mockListener();
        target.detachParasite("a", parasites[0]);
        verify(listener).onDetached(1, collection(parasites[0]));
        verifyNoMoreInteractions(listener);
    }
}
