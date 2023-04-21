package com.android1.amarena2d.nodes.layer;

import com.android1.amarena2d.commons.collections.FixedSizeArray;
import com.android1.amarena2d.nodes.LayerNode;
import com.android1.amarena2d.nodes.LeafEntity;
import com.android1.amarena2d.nodes.behavior.delegates.TransformDelegate;
import com.badlogic.gdx.math.Vector2;

public class ParallaxNode extends LayerNode {

    protected FixedSizeArray<ParallaxChild> parallaxChildren;

    protected final Vector2 lastPos = new Vector2();

    public ParallaxNode(int maxSize) {
        this(maxSize, 0, 0);
    }

    public ParallaxNode(int maxSize, float x, float y) {
        parallaxChildren = new FixedSizeArray<ParallaxChild>(maxSize);
        ensureChildren(maxSize);
        setXY(x, y);
    }

    @Override
    public void setXY(float x, float y) {
        super.setXY(x, y);
        updatePos(getX(), getY());
    }

    @Override
    public void add(LeafEntity e) {
        addParallaxNode(e, 1F, 1F);
        super.add(e);
    }

    public void add(LeafEntity e, final float scalex, final float scaley) {
        addParallaxNode(e, scalex, scaley);
        super.add(e);
    }

    public void add(LeafEntity e, final float scale) {
        addParallaxNode(e, scale, scale);
        super.add(e);
    }

    @Override
    public void add(LeafEntity... e) {
        for (int i = 0; i < e.length; i++) {
            addParallaxNode(e[i], 1F, 1F);
        }
        super.add(e);
    }

    @Override
    public void add(LeafEntity e, int zOrder) {
        addParallaxNode(e, 1F, 1F);
        super.add(e, zOrder);
    }

    @Override
    public void remove(LeafEntity e) {
        removeParallaxChild(e);
        super.remove(e);
    }

    @Override
    public void remove(LeafEntity e, boolean doDispose) {
        removeParallaxChild(e);
        super.remove(e, doDispose);
    }

    @Override
    public void render() {
        if (!init || !active || !visible) return;
        if (super.children != null) if (renderChildsFirst) {
            renderChilds();
            onRender();
        } else {
            onRender();
            renderChilds();
        } else onRender();
        onRenderEnd();
    }

    @Override
    public TransformDelegate transform() {
        throw new UnsupportedOperationException();
    }

    private void addParallaxNode(final LeafEntity leafEntity, final float scaleX, final float scaleY) {
        ParallaxChild parallaxNode = new ParallaxChild(leafEntity, scaleX, scaleY);
        parallaxChildren.add(parallaxNode);
    }

    private void updatePos(final float x, final float y) {
        final float diffx = x - lastPos.x;
        final float diffy = y - lastPos.y;
        lastPos.set(x, y);
        if (parallaxChildren != null) {
            final int size = parallaxChildren.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final ParallaxChild e = parallaxChildren.get(i);
                    if (e != null) {
                        e.addXY(diffx, diffy);
                    }
                }
            }
        }
    }

    private void removeParallaxChild(LeafEntity leafEntity) {
        if (parallaxChildren != null) {
            final int size = parallaxChildren.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final ParallaxChild e = parallaxChildren.get(i);
                    if (e != null) {
                        if (e.entity == leafEntity) {
                            parallaxChildren.remove(e);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static final class ParallaxChild {

        public final LeafEntity entity;

        public float scaleX;

        public float scaleY;

        public ParallaxChild(final LeafEntity entity, float scaleX, float scaleY) {
            this.entity = entity;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }

        public void addXY(final float x, final float y) {
            entity.addXY(x * scaleX, y * scaleY);
        }
    }
}
