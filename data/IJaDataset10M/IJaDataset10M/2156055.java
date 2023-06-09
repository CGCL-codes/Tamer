package org.ogre4j;

import org.xbig.base.*;

public class MaterialSerializer extends org.xbig.base.NativeObject implements org.ogre4j.IMaterialSerializer {

    static {
        System.loadLibrary("ogre4j");
    }

    protected static class AttribParserList extends org.xbig.base.NativeObject implements org.ogre4j.IMaterialSerializer.IAttribParserList {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public AttribParserList(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected AttribParserList(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public AttribParserList(org.xbig.base.WithoutNativeObject val) {
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
    }

    protected static class EffectMap extends org.xbig.base.NativeObject implements org.ogre4j.IMaterialSerializer.IEffectMap {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public EffectMap(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected EffectMap(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public EffectMap(org.xbig.base.WithoutNativeObject val) {
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
        public EffectMap() {
            super(new org.xbig.base.InstancePointer(__createEffectMap()), false);
        }

        private static native long __createEffectMap();

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public int count(org.ogre4j.TextureUnitState.TextureEffectType key) {
            return _count__Ogre_TextureUnitState_TextureEffectTypeR(this.object.pointer, key.getValue());
        }

        private native int _count__Ogre_TextureUnitState_TextureEffectTypeR(long _pointer_, int key);

        /** **/
        public boolean empty() {
            return _empty_const(this.object.pointer);
        }

        private native boolean _empty_const(long _pointer_);

        /** **/
        public int erase(org.ogre4j.TextureUnitState.TextureEffectType key) {
            return _erase__Ogre_TextureUnitState_TextureEffectTypeR(this.object.pointer, key.getValue());
        }

        private native int _erase__Ogre_TextureUnitState_TextureEffectTypeR(long _pointer_, int key);

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

    private static class GpuProgramDefinitionContainer extends org.xbig.base.NativeObject implements org.ogre4j.IMaterialSerializer.IGpuProgramDefinitionContainer {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public GpuProgramDefinitionContainer(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected GpuProgramDefinitionContainer(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public GpuProgramDefinitionContainer(org.xbig.base.WithoutNativeObject val) {
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
        public GpuProgramDefinitionContainer() {
            super(new org.xbig.base.InstancePointer(__createGpuProgramDefinitionContainer()), false);
        }

        private static native long __createGpuProgramDefinitionContainer();

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public int count(String key) {
            return _count__sR(this.object.pointer, key);
        }

        private native int _count__sR(long _pointer_, String key);

        /** **/
        public boolean empty() {
            return _empty_const(this.object.pointer);
        }

        private native boolean _empty_const(long _pointer_);

        /** **/
        public int erase(String key) {
            return _erase__sR(this.object.pointer, key);
        }

        private native int _erase__sR(long _pointer_, String key);

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
    public MaterialSerializer(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected MaterialSerializer(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public MaterialSerializer(org.xbig.base.WithoutNativeObject val) {
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
    default constructor **/
    public MaterialSerializer() {
        super(new org.xbig.base.InstancePointer(__createMaterialSerializer()), false);
    }

    private static native long __createMaterialSerializer();

    /** 
    Queue an in-memory  to the internal buffer for export. **/
    public void queueForExport(org.ogre4j.IMaterialPtr pMat, boolean clearQueued, boolean exportDefaults) {
        _queueForExport__MaterialPtrRbvbv(this.object.pointer, pMat.getInstancePointer().pointer, clearQueued, exportDefaults);
    }

    private native void _queueForExport__MaterialPtrRbvbv(long _pointer_, long pMat, boolean clearQueued, boolean exportDefaults);

    /** 
    Exports queued material(s) to a named material script file. **/
    public void exportQueued(String filename, boolean includeProgDef, String programFilename) {
        _exportQueued__StringRbVStringR(this.object.pointer, filename, includeProgDef, programFilename);
    }

    private native void _exportQueued__StringRbVStringR(long _pointer_, String filename, boolean includeProgDef, String programFilename);

    /** 
    Exports a single in-memory  to the named material script file. **/
    public void exportMaterial(org.ogre4j.IMaterialPtr pMat, String filename, boolean exportDefaults, boolean includeProgDef, String programFilename) {
        _exportMaterial__MaterialPtrRStringRbvbVStringR(this.object.pointer, pMat.getInstancePointer().pointer, filename, exportDefaults, includeProgDef, programFilename);
    }

    private native void _exportMaterial__MaterialPtrRStringRbvbVStringR(long _pointer_, long pMat, String filename, boolean exportDefaults, boolean includeProgDef, String programFilename);

    /** 
    Returns a string representing the parsed material(s) **/
    public String getQueuedAsString() {
        return _getQueuedAsString_const(this.object.pointer);
    }

    private native String _getQueuedAsString_const(long _pointer_);

    /** 
    Clears the internal buffer **/
    public void clearQueue() {
        _clearQueue(this.object.pointer);
    }

    private native void _clearQueue(long _pointer_);

    /** 
    Parses a  script file passed as a stream. **/
    public void parseScript(org.ogre4j.IDataStreamPtr stream, String groupName) {
        _parseScript__DataStreamPtrrStringR(this.object.pointer, stream.getInstancePointer().pointer, groupName);
    }

    private native void _parseScript__DataStreamPtrrStringR(long _pointer_, long stream, String groupName);
}
