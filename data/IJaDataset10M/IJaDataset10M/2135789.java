package com.phloc.commons.tree.utils.walk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.tree.simple.DefaultTree;
import com.phloc.commons.tree.simple.DefaultTreeItem;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.DefaultTreeWithID;

/**
 * Test class for class {@link TreeWalker}.
 * 
 * @author philip
 */
public final class TreeWalkerTest {

    private static final Logger s_aLogger = LoggerFactory.getLogger(TreeWalkerTest.class);

    private static void _fillTree(final DefaultTreeItem<String> aParentItem, final int nLevels, final int nItemsPerLevel) {
        if (nLevels > 0) for (int i = 0; i < nItemsPerLevel; ++i) {
            final DefaultTreeItem<String> aChild = aParentItem.createChildItem("any");
            _fillTree(aChild, nLevels - 1, nItemsPerLevel);
            assertTrue(aChild.isSameOrChildOf(aParentItem));
            assertFalse(aParentItem.isSameOrChildOf(aChild));
        }
    }

    private static DefaultTree<String> _createTree(final int nLevels, final int nItemsPerLevel) {
        final DefaultTree<String> t = new DefaultTree<String>();
        _fillTree(t.getRootItem(), nLevels, nItemsPerLevel);
        return t;
    }

    private static long _powSum(final int nBase, final int y) {
        long ret = 0;
        for (int i = 1; i <= y; ++i) ret += (long) Math.pow(nBase, i);
        return ret;
    }

    @Test
    public void testTreeWalk() {
        final int nItemsPerLevel = 5;
        for (int nLevel = 1; nLevel < 9; ++nLevel) {
            final long nExpected = _powSum(nItemsPerLevel, nLevel);
            s_aLogger.info("Creating tree with " + nExpected + " items");
            final MutableInt mi = new MutableInt();
            TreeWalker.walkTree(_createTree(nLevel, nItemsPerLevel), new DefaultHierarchyWalkerCallback<DefaultTreeItem<String>>() {

                @Override
                public void onItemBeforeChildren(final DefaultTreeItem<String> aItem) {
                    if (getLevel() < 0) throw new IllegalStateException();
                    mi.inc();
                }
            });
            assertEquals(nExpected, mi.intValue());
            mi.set(0);
            TreeWalker.walkSubTree(_createTree(nLevel, nItemsPerLevel).getRootItem(), new DefaultHierarchyWalkerCallback<DefaultTreeItem<String>>() {

                @Override
                public void onItemBeforeChildren(final DefaultTreeItem<String> aItem) {
                    if (getLevel() < 0) throw new IllegalStateException();
                    mi.inc();
                }
            });
            assertEquals(nExpected, mi.intValue());
            mi.set(0);
            TreeWalker.walkTree(_createTree(nLevel, nItemsPerLevel), new DefaultHierarchyWalkerCallback<DefaultTreeItem<String>>() {

                @Override
                public void onItemAfterChildren(final DefaultTreeItem<String> aItem) {
                    if (getLevel() < 0) throw new IllegalStateException();
                    mi.inc();
                }
            });
            assertEquals(nExpected, mi.intValue());
            mi.set(0);
            TreeWalker.walkSubTree(_createTree(nLevel, nItemsPerLevel).getRootItem(), new DefaultHierarchyWalkerCallback<DefaultTreeItem<String>>() {

                @Override
                public void onItemAfterChildren(final DefaultTreeItem<String> aItem) {
                    if (getLevel() < 0) throw new IllegalStateException();
                    mi.inc();
                }
            });
            assertEquals(nExpected, mi.intValue());
        }
    }

    private static void _fillTreeWithID(final DefaultTreeItemWithID<String, Object> aParentItem, final int nLevels, final int nItemsPerLevel) {
        if (nLevels > 0) for (int i = 0; i < nItemsPerLevel; ++i) {
            final DefaultTreeItemWithID<String, Object> aChild = aParentItem.createChildItem(Integer.toString(i), Double.valueOf(Double.POSITIVE_INFINITY));
            _fillTreeWithID(aChild, nLevels - 1, nItemsPerLevel);
            assertTrue(aChild.isSameOrChildOf(aParentItem));
            assertFalse(aParentItem.isSameOrChildOf(aChild));
        }
    }

    private static DefaultTreeWithID<String, Object> _createTreeWithID(final int nLevels, final int nItemsPerLevel) {
        final DefaultTreeWithID<String, Object> t = new DefaultTreeWithID<String, Object>();
        _fillTreeWithID(t.getRootItem(), nLevels, nItemsPerLevel);
        return t;
    }

    @Test
    public void testTreeWithIDWalk() {
        final int nItemsPerLevel = 5;
        for (int nLevel = 1; nLevel < 8; ++nLevel) {
            final long nExpected = _powSum(nItemsPerLevel, nLevel);
            s_aLogger.info("Creating tree with " + nExpected + " items");
            final MutableInt mi = new MutableInt();
            TreeWalker.walkTree(_createTreeWithID(nLevel, nItemsPerLevel), new MockTreeWalkerCallback(mi));
            assertEquals(nExpected, mi.intValue());
            mi.set(0);
            TreeWalker.walkSubTree(_createTreeWithID(nLevel, nItemsPerLevel).getRootItem(), new MockTreeWalkerCallback(mi));
            assertEquals(nExpected, mi.intValue());
            mi.set(0);
            TreeWalker.walkTree(_createTreeWithID(nLevel, nItemsPerLevel), new MockTreeWalkerCallback(mi));
            assertEquals(nExpected, mi.intValue());
            mi.set(0);
            TreeWalker.walkSubTree(_createTreeWithID(nLevel, nItemsPerLevel).getRootItem(), new MockTreeWalkerCallback(mi));
            assertEquals(nExpected, mi.intValue());
            try {
                TreeWalker.walkTree((DefaultTreeWithID<String, Object>) null, new MockTreeWalkerCallback(mi));
                fail();
            } catch (final NullPointerException ex) {
            }
            try {
                TreeWalker.walkSubTree((DefaultTreeItemWithID<String, Object>) null, new MockTreeWalkerCallback(mi));
                fail();
            } catch (final NullPointerException ex) {
            }
            try {
                TreeWalker.walkSubTree(_createTreeWithID(nLevel, nItemsPerLevel).getRootItem(), null);
                fail();
            } catch (final NullPointerException ex) {
            }
        }
    }
}
