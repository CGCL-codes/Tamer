package com.android.hit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Set;

public class ArrayInstance extends Instance {

    private int mType;

    private int mNumEntries;

    private byte[] mData;

    public ArrayInstance(long id, StackTrace stack, int type, int numEntries, byte[] data) {
        mId = id;
        mStack = stack;
        mType = type;
        mNumEntries = numEntries;
        mData = data;
    }

    public final void resolveReferences(State state) {
        if (mType != Types.OBJECT) {
            return;
        }
        int idSize = Types.getTypeSize(mType);
        final int N = mNumEntries;
        ByteArrayInputStream bais = new ByteArrayInputStream(mData);
        DataInputStream dis = new DataInputStream(bais);
        for (int i = 0; i < N; i++) {
            long id;
            try {
                if (idSize == 4) {
                    id = dis.readInt();
                } else {
                    id = dis.readLong();
                }
                Instance instance = state.findReference(id);
                if (instance != null) {
                    instance.addParent(this);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final int getSize() {
        return mData.length;
    }

    @Override
    public final void visit(Set<Instance> resultSet, Filter filter) {
        if (resultSet.contains(this)) {
            return;
        }
        if (null != filter) {
            if (filter.accept(this)) {
                resultSet.add(this);
            }
        } else {
            resultSet.add(this);
        }
        if (mType != Types.OBJECT) {
            return;
        }
        int idSize = Types.getTypeSize(mType);
        final int N = mNumEntries;
        ByteArrayInputStream bais = new ByteArrayInputStream(mData);
        DataInputStream dis = new DataInputStream(bais);
        State state = mHeap.mState;
        for (int i = 0; i < N; i++) {
            long id;
            try {
                if (idSize == 4) {
                    id = dis.readInt();
                } else {
                    id = dis.readLong();
                }
                Instance instance = state.findReference(id);
                if (instance != null) {
                    instance.visit(resultSet, filter);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final String getTypeName() {
        return Types.getTypeName(mType) + "[" + mNumEntries + "]";
    }

    public final String toString() {
        return String.format("%s@0x08x", getTypeName(), mId);
    }

    @Override
    public String describeReferenceTo(long referent) {
        if (mType != Types.OBJECT) {
            return super.describeReferenceTo(referent);
        }
        int idSize = Types.getTypeSize(mType);
        final int N = mNumEntries;
        int numRefs = 0;
        StringBuilder result = new StringBuilder("Elements [");
        ByteArrayInputStream bais = new ByteArrayInputStream(mData);
        DataInputStream dis = new DataInputStream(bais);
        for (int i = 0; i < N; i++) {
            long id;
            try {
                if (idSize == 4) {
                    id = dis.readInt();
                } else {
                    id = dis.readLong();
                }
                if (id == referent) {
                    numRefs++;
                    if (numRefs > 1) {
                        result.append(", ");
                    }
                    result.append(i);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        if (numRefs == 0) {
            return super.describeReferenceTo(referent);
        }
        result.append("]");
        return result.toString();
    }
}
