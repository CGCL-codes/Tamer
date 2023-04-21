package com.sun.opengl.util.packrect;

import java.util.*;

/** Packs rectangles supplied by the user (typically representing
    image regions) into a larger backing store rectangle (typically
    representing a large texture). Supports automatic compaction of
    the space on the backing store, and automatic expansion of the
    backing store, when necessary. */
public class RectanglePacker {

    private BackingStoreManager manager;

    private Object backingStore;

    private LevelSet levels;

    private float EXPANSION_FACTOR = 0.5f;

    private float SHRINK_FACTOR = 0.3f;

    private int initialWidth;

    private int initialHeight;

    private int maxWidth = -1;

    private int maxHeight = -1;

    static class RectHComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Rect r1 = (Rect) o1;
            Rect r2 = (Rect) o2;
            return r2.h() - r1.h();
        }

        public boolean equals(Object obj) {
            return this == obj;
        }
    }

    private static final Comparator rectHComparator = new RectHComparator();

    public RectanglePacker(BackingStoreManager manager, int initialWidth, int initialHeight) {
        this.manager = manager;
        levels = new LevelSet(initialWidth, initialHeight);
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
    }

    public Object getBackingStore() {
        if (backingStore == null) {
            backingStore = manager.allocateBackingStore(levels.w(), levels.h());
        }
        return backingStore;
    }

    /** Sets up a maximum width and height for the backing store. These
      are optional and if not specified the backing store will grow as
      necessary. Setting up a maximum width and height introduces the
      possibility that additions will fail; these are handled with the
      BackingStoreManager's allocationFailed notification. */
    public void setMaxSize(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    /** Decides upon an (x, y) position for the given rectangle (leaving
      its width and height unchanged) and places it on the backing
      store. May provoke re-layout of other Rects already added. If
      the BackingStoreManager does not support compaction, and {@link
      BackingStoreManager#preExpand BackingStoreManager.preExpand}
      does not clear enough space for the incoming rectangle, then
      this method will throw a RuntimeException. */
    public void add(Rect rect) throws RuntimeException {
        if (backingStore == null) backingStore = manager.allocateBackingStore(levels.w(), levels.h());
        int attemptNumber = 0;
        boolean tryAgain = false;
        do {
            if (levels.add(rect)) return;
            if (manager.canCompact()) {
                if (levels.compactAndAdd(rect, backingStore, manager)) return;
                tryAgain = manager.preExpand(rect, attemptNumber++);
            } else {
                tryAgain = manager.additionFailed(rect, attemptNumber++);
            }
        } while (tryAgain);
        if (!manager.canCompact()) {
            throw new RuntimeException("BackingStoreManager does not support compaction or expansion, and didn't clear space for new rectangle");
        }
        compactImpl(rect);
        add(rect);
    }

    /** Removes the given rectangle from this RectanglePacker. */
    public void remove(Rect rect) {
        levels.remove(rect);
    }

    /** Visits all Rects contained in this RectanglePacker. */
    public void visit(RectVisitor visitor) {
        levels.visit(visitor);
    }

    /** Returns the vertical fragmentation ratio of this
      RectanglePacker. This is defined as the ratio of the sum of the
      heights of all completely empty Levels divided by the overall
      used height of the LevelSet. A high vertical fragmentation ratio
      indicates that it may be profitable to perform a compaction. */
    public float verticalFragmentationRatio() {
        return levels.verticalFragmentationRatio();
    }

    /** Forces a compaction cycle, which typically results in allocating
      a new backing store and copying all entries to it. */
    public void compact() {
        compactImpl(null);
    }

    private void compactImpl(Rect cause) {
        boolean done = false;
        int newWidth = levels.w();
        int newHeight = levels.h();
        LevelSet nextLevelSet = null;
        int attemptNumber = 0;
        boolean needAdditionFailureNotification = false;
        while (!done) {
            if (cause != null) {
                if (cause.w() > newWidth) {
                    newWidth = cause.w();
                } else {
                    newHeight = (int) (newHeight * (1.0f + EXPANSION_FACTOR));
                }
            }
            needAdditionFailureNotification = false;
            if (maxWidth > 0 && newWidth > maxWidth) {
                newWidth = maxWidth;
                needAdditionFailureNotification = true;
            }
            if (maxHeight > 0 && newHeight > maxHeight) {
                newHeight = maxHeight;
                needAdditionFailureNotification = true;
            }
            nextLevelSet = new LevelSet(newWidth, newHeight);
            List newRects = new ArrayList();
            for (Iterator i1 = levels.iterator(); i1.hasNext(); ) {
                Level level = (Level) i1.next();
                for (Iterator i2 = level.iterator(); i2.hasNext(); ) {
                    Rect cur = (Rect) i2.next();
                    Rect newRect = new Rect(0, 0, cur.w(), cur.h(), null);
                    cur.setNextLocation(newRect);
                    newRect.setNextLocation(cur);
                    newRects.add(newRect);
                }
            }
            Collections.sort(newRects, rectHComparator);
            done = true;
            for (Iterator iter = newRects.iterator(); iter.hasNext(); ) {
                if (!nextLevelSet.add((Rect) iter.next())) {
                    done = false;
                    break;
                }
            }
            if (done && cause != null) {
                if (nextLevelSet.add(cause)) {
                } else {
                    done = false;
                }
            }
            if (!done && needAdditionFailureNotification && cause != null) {
                manager.additionFailed(cause, attemptNumber);
            }
            ++attemptNumber;
        }
        if (nextLevelSet.getUsedHeight() > 0 && nextLevelSet.getUsedHeight() < nextLevelSet.h() * SHRINK_FACTOR) {
            int shrunkHeight = Math.max(initialHeight, (int) (nextLevelSet.getUsedHeight() * (1.0f + EXPANSION_FACTOR)));
            if (maxHeight > 0 && shrunkHeight > maxHeight) {
                shrunkHeight = maxHeight;
            }
            nextLevelSet.setHeight(shrunkHeight);
        }
        if (cause != null) {
            nextLevelSet.remove(cause);
        }
        Object newBackingStore = manager.allocateBackingStore(nextLevelSet.w(), nextLevelSet.h());
        manager.beginMovement(backingStore, newBackingStore);
        for (Iterator i1 = levels.iterator(); i1.hasNext(); ) {
            Level level = (Level) i1.next();
            for (Iterator i2 = level.iterator(); i2.hasNext(); ) {
                Rect cur = (Rect) i2.next();
                manager.move(backingStore, cur, newBackingStore, cur.getNextLocation());
            }
        }
        nextLevelSet.updateRectangleReferences();
        manager.endMovement(backingStore, newBackingStore);
        manager.deleteBackingStore(backingStore);
        backingStore = newBackingStore;
        levels = nextLevelSet;
    }

    /** Clears all Rects contained in this RectanglePacker. */
    public void clear() {
        levels.clear();
    }

    /** Disposes the backing store allocated by the
      BackingStoreManager. This RectanglePacker may no longer be used
      after calling this method. */
    public void dispose() {
        if (backingStore != null) manager.deleteBackingStore(backingStore);
        backingStore = null;
        levels = null;
    }
}
