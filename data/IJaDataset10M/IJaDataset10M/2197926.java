package de.bloxel.math;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Equivalence;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

public class HashSetVolume<T> implements BloxelVolume<T> {

    static final class Position {

        private final float x, y, z;

        public Position(final float x, final float y, final float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Position)) {
                return false;
            }
            final Position other = (Position) obj;
            if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
                return false;
            }
            if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
                return false;
            }
            if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Float.floatToIntBits(x);
            result = prime * result + Float.floatToIntBits(y);
            result = prime * result + Float.floatToIntBits(z);
            return result;
        }
    }

    private final Map<Position, T> data = new HashMap<Position, T>();

    final float elementSize;

    final BoundingBox bb;

    public HashSetVolume(final Vector3f center, final float dimension, final float elementSize) {
        this.elementSize = elementSize;
        this.bb = new BoundingBox(center, dimension, dimension, dimension);
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public boolean contains(final float x, final float y, final float z) {
        return data.containsKey(new Position(x, y, z));
    }

    @Override
    public T getBloxel(final float x, final float y, final float z) {
        return getVoxel(x, y, z);
    }

    public Collection<T> getBloxels() {
        return data.values();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bb;
    }

    @Override
    public float getElementSize() {
        return elementSize;
    }

    private T getVoxel(final float x, final float y, final float z) {
        return data.get(new Position(x, y, z));
    }

    @Override
    public boolean isEmpty(final float x, final float y, final float z) {
        return getVoxel(x, y, z) == null;
    }

    @Override
    public void pack(final Equivalence<T> equivalence) {
    }

    @Override
    public void removeBloxel(final float x, final float y, final float z) {
        setVoxel(x, y, z, null);
    }

    @Override
    public void setBloxel(final float x, final float y, final float z, final T data) {
        setVoxel(x, y, z, data);
    }

    private void setVoxel(final float x, final float y, final float z, final T t) {
        if (t == null) {
            data.remove(new Position(x, y, z));
        } else {
            data.put(new Position(x, y, z), t);
        }
    }

    @Override
    public int size() {
        return data.size();
    }
}
