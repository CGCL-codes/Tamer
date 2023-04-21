package org.ogre4j;

import org.xbig.base.*;

public class CompositionTechniqueTranslator extends org.xbig.base.NativeObject implements org.ogre4j.ICompositionTechniqueTranslator {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public CompositionTechniqueTranslator(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected CompositionTechniqueTranslator(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public CompositionTechniqueTranslator(org.xbig.base.WithoutNativeObject val) {
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
    public CompositionTechniqueTranslator() {
        super(new org.xbig.base.InstancePointer(__createCompositionTechniqueTranslator()), false);
    }

    private static native long __createCompositionTechniqueTranslator();

    /** **/
    public void translate(org.ogre4j.IScriptCompiler compiler, org.ogre4j.IAbstractNodePtr node) {
        _translate__ScriptCompilerpAbstractNodePtrR(this.object.pointer, compiler.getInstancePointer().pointer, node.getInstancePointer().pointer);
    }

    private native void _translate__ScriptCompilerpAbstractNodePtrR(long _pointer_, long compiler, long node);
}
