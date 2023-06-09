package org.ogre4j;

import org.xbig.base.*;

public class ProgressiveMesh extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh {

    static {
        System.loadLibrary("ogre4j");
    }

    public static class PMFaceVertex extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMFaceVertex {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public PMFaceVertex(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected PMFaceVertex(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public PMFaceVertex(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public PMFaceVertex() {
            super(new org.xbig.base.InstancePointer(__createPMFaceVertex()), false);
        }

        private static native long __createPMFaceVertex();

        /** **/
        public int getrealIndex() {
            return _getrealIndex(this.object.pointer);
        }

        private native int _getrealIndex(long _pointer_);

        /** **/
        public void setrealIndex(int _jni_value_) {
            _setrealIndex(this.object.pointer, _jni_value_);
        }

        private native void _setrealIndex(long _pointer_, int _jni_value_);
    }

    protected static class PMTriangle extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMTriangle {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public PMTriangle(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected PMTriangle(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public PMTriangle(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public PMTriangle() {
            super(new org.xbig.base.InstancePointer(__createPMTriangle()), false);
        }

        private static native long __createPMTriangle();

        /** **/
        public void setDetails(int index, org.ogre4j.IProgressiveMesh.IPMFaceVertex v0, org.ogre4j.IProgressiveMesh.IPMFaceVertex v1, org.ogre4j.IProgressiveMesh.IPMFaceVertex v2) {
            _setDetails__ivPMFaceVertexpPMFaceVertexpPMFaceVertexp(this.object.pointer, index, v0.getInstancePointer().pointer, v1.getInstancePointer().pointer, v2.getInstancePointer().pointer);
        }

        private native void _setDetails__ivPMFaceVertexpPMFaceVertexpPMFaceVertexp(long _pointer_, int index, long v0, long v1, long v2);

        /** **/
        public void computeNormal() {
            _computeNormal(this.object.pointer);
        }

        private native void _computeNormal(long _pointer_);

        /** **/
        public void replaceVertex(org.ogre4j.IProgressiveMesh.IPMFaceVertex vold, org.ogre4j.IProgressiveMesh.IPMFaceVertex vnew) {
            _replaceVertex__PMFaceVertexpPMFaceVertexp(this.object.pointer, vold.getInstancePointer().pointer, vnew.getInstancePointer().pointer);
        }

        private native void _replaceVertex__PMFaceVertexpPMFaceVertexp(long _pointer_, long vold, long vnew);

        /** **/
        public boolean hasCommonVertex(org.ogre4j.IProgressiveMesh.IPMVertex v) {
            return _hasCommonVertex__PMVertexp_const(this.object.pointer, v.getInstancePointer().pointer);
        }

        private native boolean _hasCommonVertex__PMVertexp_const(long _pointer_, long v);

        /** **/
        public boolean hasFaceVertex(org.ogre4j.IProgressiveMesh.IPMFaceVertex v) {
            return _hasFaceVertex__PMFaceVertexp_const(this.object.pointer, v.getInstancePointer().pointer);
        }

        private native boolean _hasFaceVertex__PMFaceVertexp_const(long _pointer_, long v);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMFaceVertex getFaceVertexFromCommon(org.ogre4j.IProgressiveMesh.IPMVertex commonVert) {
            return new org.ogre4j.ProgressiveMesh.PMFaceVertex(new InstancePointer(_getFaceVertexFromCommon__PMVertexp(this.object.pointer, commonVert.getInstancePointer().pointer)));
        }

        private native long _getFaceVertexFromCommon__PMVertexp(long _pointer_, long commonVert);

        /** **/
        public void notifyRemoved() {
            _notifyRemoved(this.object.pointer);
        }

        private native void _notifyRemoved(long _pointer_);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMFaceVertex getvertex() {
            return new org.ogre4j.ProgressiveMesh.PMFaceVertex(new InstancePointer(_getvertex(this.object.pointer)));
        }

        private native long _getvertex(long _pointer_);

        /** **/
        public void setvertex(org.ogre4j.IProgressiveMesh.IPMFaceVertex _jni_value_) {
            _setvertex(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setvertex(long _pointer_, long _jni_value_);

        /** **/
        public void getnormal(org.ogre4j.IVector3 returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getnormal(this.object.pointer)), false);
        }

        private native long _getnormal(long _pointer_);

        /** **/
        public void setnormal(org.ogre4j.IVector3 _jni_value_) {
            _setnormal(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setnormal(long _pointer_, long _jni_value_);

        /** **/
        public boolean getremoved() {
            return _getremoved(this.object.pointer);
        }

        private native boolean _getremoved(long _pointer_);

        /** **/
        public void setremoved(boolean _jni_value_) {
            _setremoved(this.object.pointer, _jni_value_);
        }

        private native void _setremoved(long _pointer_, boolean _jni_value_);

        /** **/
        public int getindex() {
            return _getindex(this.object.pointer);
        }

        private native int _getindex(long _pointer_);

        /** **/
        public void setindex(int _jni_value_) {
            _setindex(this.object.pointer, _jni_value_);
        }

        private native void _setindex(long _pointer_, int _jni_value_);
    }

    protected static class PMVertex extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMVertex {

        static {
            System.loadLibrary("ogre4j");
        }

        public static class NeighborList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMVertex.INeighborList {

            static {
                System.loadLibrary("ogre4j");
            }

            /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
            public NeighborList(org.xbig.base.InstancePointer p) {
                super(p);
            }

            /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
            protected NeighborList(org.xbig.base.InstancePointer p, boolean remote) {
                super(p, remote);
            }

            /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
            public NeighborList(org.xbig.base.WithoutNativeObject val) {
                super(val);
            }

            public void delete() {
                if (this.remote) {
                    throw new RuntimeException("can't dispose object created by native library");
                }
                if (!this.deleted) {
                    __delete(object.pointer);
                    this.deleted = true;
                    this.object.pointer = 0;
                }
            }

            public void finalize() {
                if (!this.remote && !this.deleted) {
                    delete();
                }
            }

            private final native void __delete(long _pointer_);

            /** **/
            public NeighborList() {
                super(new org.xbig.base.InstancePointer(__createNeighborList()), false);
            }

            private static native long __createNeighborList();

            /** **/
            public void clear() {
                _clear(this.object.pointer);
            }

            private native void _clear(long _pointer_);

            /** **/
            public int count(org.ogre4j.IProgressiveMesh.IPMVertex key) {
                return _count__Ogre_ProgressiveMesh_PMVertexP(this.object.pointer, key.getInstancePointer().pointer);
            }

            private native int _count__Ogre_ProgressiveMesh_PMVertexP(long _pointer_, long key);

            /** **/
            public boolean empty() {
                return _empty_const(this.object.pointer);
            }

            private native boolean _empty_const(long _pointer_);

            /** **/
            public int erase(org.ogre4j.IProgressiveMesh.IPMVertex key) {
                return _erase__Ogre_ProgressiveMesh_PMVertexP(this.object.pointer, key.getInstancePointer().pointer);
            }

            private native int _erase__Ogre_ProgressiveMesh_PMVertexP(long _pointer_, long key);

            /** **/
            public int max_size() {
                return _max_size_const(this.object.pointer);
            }

            private native int _max_size_const(long _pointer_);

            /** **/
            public int size() {
                return _size_const(this.object.pointer);
            }

            private native int _size_const(long _pointer_);
        }

        public static class DuplicateList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMVertex.IDuplicateList {

            static {
                System.loadLibrary("ogre4j");
            }

            /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
            public DuplicateList(org.xbig.base.InstancePointer p) {
                super(p);
            }

            /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
            protected DuplicateList(org.xbig.base.InstancePointer p, boolean remote) {
                super(p, remote);
            }

            /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
            public DuplicateList(org.xbig.base.WithoutNativeObject val) {
                super(val);
            }

            public void delete() {
                if (this.remote) {
                    throw new RuntimeException("can't dispose object created by native library");
                }
                if (!this.deleted) {
                    __delete(object.pointer);
                    this.deleted = true;
                    this.object.pointer = 0;
                }
            }

            public void finalize() {
                if (!this.remote && !this.deleted) {
                    delete();
                }
            }

            private final native void __delete(long _pointer_);

            /** **/
            public DuplicateList() {
                super(new org.xbig.base.InstancePointer(__createDuplicateList()), false);
            }

            private static native long __createDuplicateList();

            /** **/
            public void clear() {
                _clear(this.object.pointer);
            }

            private native void _clear(long _pointer_);

            /** **/
            public int count(org.ogre4j.IProgressiveMesh.IPMVertex key) {
                return _count__Ogre_ProgressiveMesh_PMVertexP(this.object.pointer, key.getInstancePointer().pointer);
            }

            private native int _count__Ogre_ProgressiveMesh_PMVertexP(long _pointer_, long key);

            /** **/
            public boolean empty() {
                return _empty_const(this.object.pointer);
            }

            private native boolean _empty_const(long _pointer_);

            /** **/
            public int erase(org.ogre4j.IProgressiveMesh.IPMVertex key) {
                return _erase__Ogre_ProgressiveMesh_PMVertexP(this.object.pointer, key.getInstancePointer().pointer);
            }

            private native int _erase__Ogre_ProgressiveMesh_PMVertexP(long _pointer_, long key);

            /** **/
            public int max_size() {
                return _max_size_const(this.object.pointer);
            }

            private native int _max_size_const(long _pointer_);

            /** **/
            public int size() {
                return _size_const(this.object.pointer);
            }

            private native int _size_const(long _pointer_);
        }

        public static class FaceList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMVertex.IFaceList {

            static {
                System.loadLibrary("ogre4j");
            }

            /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
            public FaceList(org.xbig.base.InstancePointer p) {
                super(p);
            }

            /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
            protected FaceList(org.xbig.base.InstancePointer p, boolean remote) {
                super(p, remote);
            }

            /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
            public FaceList(org.xbig.base.WithoutNativeObject val) {
                super(val);
            }

            public void delete() {
                if (this.remote) {
                    throw new RuntimeException("can't dispose object created by native library");
                }
                if (!this.deleted) {
                    __delete(object.pointer);
                    this.deleted = true;
                    this.object.pointer = 0;
                }
            }

            public void finalize() {
                if (!this.remote && !this.deleted) {
                    delete();
                }
            }

            private final native void __delete(long _pointer_);

            /** **/
            public FaceList() {
                super(new org.xbig.base.InstancePointer(__createFaceList()), false);
            }

            private static native long __createFaceList();

            /** **/
            public void clear() {
                _clear(this.object.pointer);
            }

            private native void _clear(long _pointer_);

            /** **/
            public int count(org.ogre4j.IProgressiveMesh.IPMTriangle key) {
                return _count__Ogre_ProgressiveMesh_PMTriangleP(this.object.pointer, key.getInstancePointer().pointer);
            }

            private native int _count__Ogre_ProgressiveMesh_PMTriangleP(long _pointer_, long key);

            /** **/
            public boolean empty() {
                return _empty_const(this.object.pointer);
            }

            private native boolean _empty_const(long _pointer_);

            /** **/
            public int erase(org.ogre4j.IProgressiveMesh.IPMTriangle key) {
                return _erase__Ogre_ProgressiveMesh_PMTriangleP(this.object.pointer, key.getInstancePointer().pointer);
            }

            private native int _erase__Ogre_ProgressiveMesh_PMTriangleP(long _pointer_, long key);

            /** **/
            public int max_size() {
                return _max_size_const(this.object.pointer);
            }

            private native int _max_size_const(long _pointer_);

            /** **/
            public int size() {
                return _size_const(this.object.pointer);
            }

            private native int _size_const(long _pointer_);
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public PMVertex(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected PMVertex(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public PMVertex(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public PMVertex() {
            super(new org.xbig.base.InstancePointer(__createPMVertex()), false);
        }

        private static native long __createPMVertex();

        /** **/
        public void setDetails(org.ogre4j.IVector3 v, int index) {
            _setDetails__Vector3Riv(this.object.pointer, v.getInstancePointer().pointer, index);
        }

        private native void _setDetails__Vector3Riv(long _pointer_, long v, int index);

        /** **/
        public void removeIfNonNeighbor(org.ogre4j.IProgressiveMesh.IPMVertex n) {
            _removeIfNonNeighbor__PMVertexp(this.object.pointer, n.getInstancePointer().pointer);
        }

        private native void _removeIfNonNeighbor__PMVertexp(long _pointer_, long n);

        /** **/
        public boolean isBorder() {
            return _isBorder(this.object.pointer);
        }

        private native boolean _isBorder(long _pointer_);

        /** **/
        public boolean isManifoldEdgeWith(org.ogre4j.IProgressiveMesh.IPMVertex v) {
            return _isManifoldEdgeWith__PMVertexp(this.object.pointer, v.getInstancePointer().pointer);
        }

        private native boolean _isManifoldEdgeWith__PMVertexp(long _pointer_, long v);

        /** **/
        public void notifyRemoved() {
            _notifyRemoved(this.object.pointer);
        }

        private native void _notifyRemoved(long _pointer_);

        /** **/
        public void getposition(org.ogre4j.IVector3 returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getposition(this.object.pointer)), false);
        }

        private native long _getposition(long _pointer_);

        /** **/
        public void setposition(org.ogre4j.IVector3 _jni_value_) {
            _setposition(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setposition(long _pointer_, long _jni_value_);

        /** **/
        public int getindex() {
            return _getindex(this.object.pointer);
        }

        private native int _getindex(long _pointer_);

        /** **/
        public void setindex(int _jni_value_) {
            _setindex(this.object.pointer, _jni_value_);
        }

        private native void _setindex(long _pointer_, int _jni_value_);

        /** **/
        public void getneighbor(org.ogre4j.IProgressiveMesh.IPMVertex.INeighborList returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getneighbor(this.object.pointer)), false);
        }

        private native long _getneighbor(long _pointer_);

        /** **/
        public void setneighbor(org.ogre4j.IProgressiveMesh.IPMVertex.INeighborList _jni_value_) {
            _setneighbor(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setneighbor(long _pointer_, long _jni_value_);

        /** **/
        public void getface(org.ogre4j.IProgressiveMesh.IPMVertex.IFaceList returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getface(this.object.pointer)), false);
        }

        private native long _getface(long _pointer_);

        /** **/
        public void setface(org.ogre4j.IProgressiveMesh.IPMVertex.IFaceList _jni_value_) {
            _setface(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setface(long _pointer_, long _jni_value_);

        /** **/
        public float getcollapseCost() {
            return _getcollapseCost(this.object.pointer);
        }

        private native float _getcollapseCost(long _pointer_);

        /** **/
        public void setcollapseCost(float _jni_value_) {
            _setcollapseCost(this.object.pointer, _jni_value_);
        }

        private native void _setcollapseCost(long _pointer_, float _jni_value_);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMVertex getcollapseTo() {
            return new org.ogre4j.ProgressiveMesh.PMVertex(new InstancePointer(_getcollapseTo(this.object.pointer)));
        }

        private native long _getcollapseTo(long _pointer_);

        /** **/
        public void setcollapseTo(org.ogre4j.IProgressiveMesh.IPMVertex _jni_value_) {
            _setcollapseTo(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setcollapseTo(long _pointer_, long _jni_value_);

        /** **/
        public boolean getremoved() {
            return _getremoved(this.object.pointer);
        }

        private native boolean _getremoved(long _pointer_);

        /** **/
        public void setremoved(boolean _jni_value_) {
            _setremoved(this.object.pointer, _jni_value_);
        }

        private native void _setremoved(long _pointer_, boolean _jni_value_);

        /** **/
        public boolean gettoBeRemoved() {
            return _gettoBeRemoved(this.object.pointer);
        }

        private native boolean _gettoBeRemoved(long _pointer_);

        /** **/
        public void settoBeRemoved(boolean _jni_value_) {
            _settoBeRemoved(this.object.pointer, _jni_value_);
        }

        private native void _settoBeRemoved(long _pointer_, boolean _jni_value_);

        /** **/
        public boolean getseam() {
            return _getseam(this.object.pointer);
        }

        private native boolean _getseam(long _pointer_);

        /** **/
        public void setseam(boolean _jni_value_) {
            _setseam(this.object.pointer, _jni_value_);
        }

        private native void _setseam(long _pointer_, boolean _jni_value_);
    }

    protected static class PMWorkingData extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IPMWorkingData {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public PMWorkingData(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected PMWorkingData(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public PMWorkingData(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public PMWorkingData() {
            super(new org.xbig.base.InstancePointer(__createPMWorkingData()), false);
        }

        private static native long __createPMWorkingData();

        /** **/
        public void getmTriList(org.ogre4j.IProgressiveMesh.ITriangleList returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getmTriList(this.object.pointer)), false);
        }

        private native long _getmTriList(long _pointer_);

        /** **/
        public void setmTriList(org.ogre4j.IProgressiveMesh.ITriangleList _jni_value_) {
            _setmTriList(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setmTriList(long _pointer_, long _jni_value_);

        /** **/
        public void getmFaceVertList(org.ogre4j.IProgressiveMesh.IFaceVertexList returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getmFaceVertList(this.object.pointer)), false);
        }

        private native long _getmFaceVertList(long _pointer_);

        /** **/
        public void setmFaceVertList(org.ogre4j.IProgressiveMesh.IFaceVertexList _jni_value_) {
            _setmFaceVertList(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setmFaceVertList(long _pointer_, long _jni_value_);

        /** **/
        public void getmVertList(org.ogre4j.IProgressiveMesh.ICommonVertexList returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getmVertList(this.object.pointer)), false);
        }

        private native long _getmVertList(long _pointer_);

        /** **/
        public void setmVertList(org.ogre4j.IProgressiveMesh.ICommonVertexList _jni_value_) {
            _setmVertList(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setmVertList(long _pointer_, long _jni_value_);
    }

    public enum VertexReductionQuota implements INativeEnum<VertexReductionQuota> {

        VRQ_CONSTANT(VertexReductionQuotaHelper.ENUM_VALUES[0]), VRQ_PROPORTIONAL(VertexReductionQuotaHelper.ENUM_VALUES[1]);

        private int value;

        VertexReductionQuota(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }

        public VertexReductionQuota getEnum(int val) {
            return toEnum(val);
        }

        public static final VertexReductionQuota toEnum(int retval) {
            if (retval == VRQ_CONSTANT.value) return VertexReductionQuota.VRQ_CONSTANT; else if (retval == VRQ_PROPORTIONAL.value) return VertexReductionQuota.VRQ_PROPORTIONAL;
            throw new RuntimeException("wrong number in jni call for an enum");
        }
    }

    static class VertexReductionQuotaHelper {

        public static final int[] ENUM_VALUES = getEnumValues();

        private static native int[] getEnumValues();
    }

    ;

    public static class LODFaceList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.ILODFaceList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public LODFaceList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected LODFaceList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public LODFaceList(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public LODFaceList() {
            super(new org.xbig.base.InstancePointer(__createLODFaceList()), false);
        }

        private static native long __createLODFaceList();

        /** **/
        public void assign(int num, org.ogre4j.IIndexData val) {
            _assign__ivOgre_IndexDataP(this.object.pointer, num, val.getInstancePointer().pointer);
        }

        private native void _assign__ivOgre_IndexDataP(long _pointer_, int num, long val);

        /** **/
        public org.ogre4j.IIndexData at(int loc) {
            return new org.ogre4j.IndexData(new InstancePointer(_at__iv(this.object.pointer, loc)));
        }

        private native long _at__iv(long _pointer_, int loc);

        /** **/
        public org.ogre4j.IIndexData back() {
            return new org.ogre4j.IndexData(new InstancePointer(_back(this.object.pointer)));
        }

        private native long _back(long _pointer_);

        /** **/
        public int capacity() {
            return _capacity(this.object.pointer);
        }

        private native int _capacity(long _pointer_);

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public boolean empty() {
            return _empty(this.object.pointer);
        }

        private native boolean _empty(long _pointer_);

        /** **/
        public org.ogre4j.IIndexData front() {
            return new org.ogre4j.IndexData(new InstancePointer(_front(this.object.pointer)));
        }

        private native long _front(long _pointer_);

        /** **/
        public int max_size() {
            return _max_size(this.object.pointer);
        }

        private native int _max_size(long _pointer_);

        /** **/
        public void pop_back() {
            _pop_back(this.object.pointer);
        }

        private native void _pop_back(long _pointer_);

        /** **/
        public void push_back(org.ogre4j.IIndexData val) {
            _push_back__Ogre_IndexDataP(this.object.pointer, val.getInstancePointer().pointer);
        }

        private native void _push_back__Ogre_IndexDataP(long _pointer_, long val);

        /** **/
        public void reserve(int size) {
            _reserve__iV(this.object.pointer, size);
        }

        private native void _reserve__iV(long _pointer_, int size);

        /** **/
        public int size() {
            return _size(this.object.pointer);
        }

        private native int _size(long _pointer_);
    }

    protected static class TriangleList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.ITriangleList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public TriangleList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected TriangleList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public TriangleList(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public TriangleList() {
            super(new org.xbig.base.InstancePointer(__createTriangleList()), false);
        }

        private static native long __createTriangleList();

        /** **/
        public void assign(int num, org.ogre4j.IProgressiveMesh.IPMTriangle val) {
            _assign__ivOgre_ProgressiveMesh_PMTriangleR(this.object.pointer, num, val.getInstancePointer().pointer);
        }

        private native void _assign__ivOgre_ProgressiveMesh_PMTriangleR(long _pointer_, int num, long val);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMTriangle at(int loc) {
            return new org.ogre4j.ProgressiveMesh.PMTriangle(new InstancePointer(_at__iv(this.object.pointer, loc)));
        }

        private native long _at__iv(long _pointer_, int loc);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMTriangle back() {
            return new org.ogre4j.ProgressiveMesh.PMTriangle(new InstancePointer(_back(this.object.pointer)));
        }

        private native long _back(long _pointer_);

        /** **/
        public int capacity() {
            return _capacity(this.object.pointer);
        }

        private native int _capacity(long _pointer_);

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public boolean empty() {
            return _empty(this.object.pointer);
        }

        private native boolean _empty(long _pointer_);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMTriangle front() {
            return new org.ogre4j.ProgressiveMesh.PMTriangle(new InstancePointer(_front(this.object.pointer)));
        }

        private native long _front(long _pointer_);

        /** **/
        public int max_size() {
            return _max_size(this.object.pointer);
        }

        private native int _max_size(long _pointer_);

        /** **/
        public void pop_back() {
            _pop_back(this.object.pointer);
        }

        private native void _pop_back(long _pointer_);

        /** **/
        public void push_back(org.ogre4j.IProgressiveMesh.IPMTriangle val) {
            _push_back__Ogre_ProgressiveMesh_PMTriangleR(this.object.pointer, val.getInstancePointer().pointer);
        }

        private native void _push_back__Ogre_ProgressiveMesh_PMTriangleR(long _pointer_, long val);

        /** **/
        public void reserve(int size) {
            _reserve__iV(this.object.pointer, size);
        }

        private native void _reserve__iV(long _pointer_, int size);

        /** **/
        public int size() {
            return _size(this.object.pointer);
        }

        private native int _size(long _pointer_);
    }

    protected static class FaceVertexList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IFaceVertexList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public FaceVertexList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected FaceVertexList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public FaceVertexList(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public FaceVertexList() {
            super(new org.xbig.base.InstancePointer(__createFaceVertexList()), false);
        }

        private static native long __createFaceVertexList();

        /** **/
        public void assign(int num, org.ogre4j.IProgressiveMesh.IPMFaceVertex val) {
            _assign__ivOgre_ProgressiveMesh_PMFaceVertexR(this.object.pointer, num, val.getInstancePointer().pointer);
        }

        private native void _assign__ivOgre_ProgressiveMesh_PMFaceVertexR(long _pointer_, int num, long val);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMFaceVertex at(int loc) {
            return new org.ogre4j.ProgressiveMesh.PMFaceVertex(new InstancePointer(_at__iv(this.object.pointer, loc)));
        }

        private native long _at__iv(long _pointer_, int loc);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMFaceVertex back() {
            return new org.ogre4j.ProgressiveMesh.PMFaceVertex(new InstancePointer(_back(this.object.pointer)));
        }

        private native long _back(long _pointer_);

        /** **/
        public int capacity() {
            return _capacity(this.object.pointer);
        }

        private native int _capacity(long _pointer_);

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public boolean empty() {
            return _empty(this.object.pointer);
        }

        private native boolean _empty(long _pointer_);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMFaceVertex front() {
            return new org.ogre4j.ProgressiveMesh.PMFaceVertex(new InstancePointer(_front(this.object.pointer)));
        }

        private native long _front(long _pointer_);

        /** **/
        public int max_size() {
            return _max_size(this.object.pointer);
        }

        private native int _max_size(long _pointer_);

        /** **/
        public void pop_back() {
            _pop_back(this.object.pointer);
        }

        private native void _pop_back(long _pointer_);

        /** **/
        public void push_back(org.ogre4j.IProgressiveMesh.IPMFaceVertex val) {
            _push_back__Ogre_ProgressiveMesh_PMFaceVertexR(this.object.pointer, val.getInstancePointer().pointer);
        }

        private native void _push_back__Ogre_ProgressiveMesh_PMFaceVertexR(long _pointer_, long val);

        /** **/
        public void reserve(int size) {
            _reserve__iV(this.object.pointer, size);
        }

        private native void _reserve__iV(long _pointer_, int size);

        /** **/
        public int size() {
            return _size(this.object.pointer);
        }

        private native int _size(long _pointer_);
    }

    protected static class CommonVertexList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.ICommonVertexList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public CommonVertexList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected CommonVertexList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public CommonVertexList(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public CommonVertexList() {
            super(new org.xbig.base.InstancePointer(__createCommonVertexList()), false);
        }

        private static native long __createCommonVertexList();

        /** **/
        public void assign(int num, org.ogre4j.IProgressiveMesh.IPMVertex val) {
            _assign__ivOgre_ProgressiveMesh_PMVertexR(this.object.pointer, num, val.getInstancePointer().pointer);
        }

        private native void _assign__ivOgre_ProgressiveMesh_PMVertexR(long _pointer_, int num, long val);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMVertex at(int loc) {
            return new org.ogre4j.ProgressiveMesh.PMVertex(new InstancePointer(_at__iv(this.object.pointer, loc)));
        }

        private native long _at__iv(long _pointer_, int loc);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMVertex back() {
            return new org.ogre4j.ProgressiveMesh.PMVertex(new InstancePointer(_back(this.object.pointer)));
        }

        private native long _back(long _pointer_);

        /** **/
        public int capacity() {
            return _capacity(this.object.pointer);
        }

        private native int _capacity(long _pointer_);

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public boolean empty() {
            return _empty(this.object.pointer);
        }

        private native boolean _empty(long _pointer_);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMVertex front() {
            return new org.ogre4j.ProgressiveMesh.PMVertex(new InstancePointer(_front(this.object.pointer)));
        }

        private native long _front(long _pointer_);

        /** **/
        public int max_size() {
            return _max_size(this.object.pointer);
        }

        private native int _max_size(long _pointer_);

        /** **/
        public void pop_back() {
            _pop_back(this.object.pointer);
        }

        private native void _pop_back(long _pointer_);

        /** **/
        public void push_back(org.ogre4j.IProgressiveMesh.IPMVertex val) {
            _push_back__Ogre_ProgressiveMesh_PMVertexR(this.object.pointer, val.getInstancePointer().pointer);
        }

        private native void _push_back__Ogre_ProgressiveMesh_PMVertexR(long _pointer_, long val);

        /** **/
        public void reserve(int size) {
            _reserve__iV(this.object.pointer, size);
        }

        private native void _reserve__iV(long _pointer_, int size);

        /** **/
        public int size() {
            return _size(this.object.pointer);
        }

        private native int _size(long _pointer_);
    }

    protected static class WorstCostList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IWorstCostList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public WorstCostList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected WorstCostList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public WorstCostList(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public WorstCostList() {
            super(new org.xbig.base.InstancePointer(__createWorstCostList()), false);
        }

        private static native long __createWorstCostList();

        /** **/
        public void assign(int num, float val) {
            _assign__ivFR(this.object.pointer, num, val);
        }

        private native void _assign__ivFR(long _pointer_, int num, float val);

        /** **/
        public FloatPointer at(int loc) {
            return new FloatPointer(new InstancePointer(_at__iv(this.object.pointer, loc)));
        }

        private native long _at__iv(long _pointer_, int loc);

        /** **/
        public FloatPointer back() {
            return new FloatPointer(new InstancePointer(_back(this.object.pointer)));
        }

        private native long _back(long _pointer_);

        /** **/
        public int capacity() {
            return _capacity(this.object.pointer);
        }

        private native int _capacity(long _pointer_);

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public boolean empty() {
            return _empty(this.object.pointer);
        }

        private native boolean _empty(long _pointer_);

        /** **/
        public FloatPointer front() {
            return new FloatPointer(new InstancePointer(_front(this.object.pointer)));
        }

        private native long _front(long _pointer_);

        /** **/
        public int max_size() {
            return _max_size(this.object.pointer);
        }

        private native int _max_size(long _pointer_);

        /** **/
        public void pop_back() {
            _pop_back(this.object.pointer);
        }

        private native void _pop_back(long _pointer_);

        /** **/
        public void push_back(float val) {
            _push_back__FR(this.object.pointer, val);
        }

        private native void _push_back__FR(long _pointer_, float val);

        /** **/
        public void reserve(int size) {
            _reserve__iV(this.object.pointer, size);
        }

        private native void _reserve__iV(long _pointer_, int size);

        /** **/
        public int size() {
            return _size(this.object.pointer);
        }

        private native int _size(long _pointer_);
    }

    protected static class WorkingDataList extends org.xbig.base.NativeObject implements org.ogre4j.IProgressiveMesh.IWorkingDataList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public WorkingDataList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected WorkingDataList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public WorkingDataList(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public WorkingDataList() {
            super(new org.xbig.base.InstancePointer(__createWorkingDataList()), false);
        }

        private static native long __createWorkingDataList();

        /** **/
        public void assign(int num, org.ogre4j.IProgressiveMesh.IPMWorkingData val) {
            _assign__ivOgre_ProgressiveMesh_PMWorkingDataR(this.object.pointer, num, val.getInstancePointer().pointer);
        }

        private native void _assign__ivOgre_ProgressiveMesh_PMWorkingDataR(long _pointer_, int num, long val);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMWorkingData at(int loc) {
            return new org.ogre4j.ProgressiveMesh.PMWorkingData(new InstancePointer(_at__iv(this.object.pointer, loc)));
        }

        private native long _at__iv(long _pointer_, int loc);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMWorkingData back() {
            return new org.ogre4j.ProgressiveMesh.PMWorkingData(new InstancePointer(_back(this.object.pointer)));
        }

        private native long _back(long _pointer_);

        /** **/
        public int capacity() {
            return _capacity(this.object.pointer);
        }

        private native int _capacity(long _pointer_);

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public boolean empty() {
            return _empty(this.object.pointer);
        }

        private native boolean _empty(long _pointer_);

        /** **/
        public org.ogre4j.IProgressiveMesh.IPMWorkingData front() {
            return new org.ogre4j.ProgressiveMesh.PMWorkingData(new InstancePointer(_front(this.object.pointer)));
        }

        private native long _front(long _pointer_);

        /** **/
        public int max_size() {
            return _max_size(this.object.pointer);
        }

        private native int _max_size(long _pointer_);

        /** **/
        public void pop_back() {
            _pop_back(this.object.pointer);
        }

        private native void _pop_back(long _pointer_);

        /** **/
        public void push_back(org.ogre4j.IProgressiveMesh.IPMWorkingData val) {
            _push_back__Ogre_ProgressiveMesh_PMWorkingDataR(this.object.pointer, val.getInstancePointer().pointer);
        }

        private native void _push_back__Ogre_ProgressiveMesh_PMWorkingDataR(long _pointer_, long val);

        /** **/
        public void reserve(int size) {
            _reserve__iV(this.object.pointer, size);
        }

        private native void _reserve__iV(long _pointer_, int size);

        /** **/
        public int size() {
            return _size(this.object.pointer);
        }

        private native int _size(long _pointer_);
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ProgressiveMesh(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ProgressiveMesh(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ProgressiveMesh(org.xbig.base.WithoutNativeObject val) {
        super(val);
    }

    public void delete() {
        if (this.remote) {
            throw new RuntimeException("can't dispose object created by native library");
        }
        if (!this.deleted) {
            __delete(object.pointer);
            this.deleted = true;
            this.object.pointer = 0;
        }
    }

    public void finalize() {
        if (!this.remote && !this.deleted) {
            delete();
        }
    }

    private final native void __delete(long _pointer_);

    /** 
    Constructor, takes the geometry data and index buffer. **/
    public ProgressiveMesh(org.ogre4j.IVertexData vertexData, org.ogre4j.IIndexData indexData) {
        super(new org.xbig.base.InstancePointer(__createProgressiveMesh__VertexDataPIndexDataP(vertexData.getInstancePointer().pointer, indexData.getInstancePointer().pointer)), false);
    }

    private static native long __createProgressiveMesh__VertexDataPIndexDataP(long vertexData, long indexData);

    /** 
    Adds an extra vertex position buffer. **/
    public void addExtraVertexPositionBuffer(org.ogre4j.IVertexData vertexData) {
        _addExtraVertexPositionBuffer__VertexDataP(this.object.pointer, vertexData.getInstancePointer().pointer);
    }

    private native void _addExtraVertexPositionBuffer__VertexDataP(long _pointer_, long vertexData);

    /** 
    Builds the progressive mesh with the specified number of levels. **/
    public void build(int numLevels, org.ogre4j.IProgressiveMesh.ILODFaceList outList, org.ogre4j.ProgressiveMesh.VertexReductionQuota quota, float reductionValue) {
        _build__ushortvLODFaceListpVertexReductionQuotavRealv(this.object.pointer, numLevels, outList.getInstancePointer().pointer, quota.getValue(), reductionValue);
    }

    private native void _build__ushortvLODFaceListpVertexReductionQuotavRealv(long _pointer_, int numLevels, long outList, int quota, float reductionValue);
}
