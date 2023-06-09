package org.ogre4j;

import org.xbig.base.*;

public class ScriptCompilerManager extends org.xbig.base.NativeObject implements org.ogre4j.IScriptCompilerManager {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ScriptCompilerManager(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ScriptCompilerManager(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ScriptCompilerManager(org.xbig.base.WithoutNativeObject val) {
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
    public ScriptCompilerManager() {
        super(new org.xbig.base.InstancePointer(__createScriptCompilerManager()), false);
    }

    private static native long __createScriptCompilerManager();

    /** **/
    public void setListener(org.ogre4j.IScriptCompilerListener listener) {
        _setListener__ScriptCompilerListenerp(this.object.pointer, listener.getInstancePointer().pointer);
    }

    private native void _setListener__ScriptCompilerListenerp(long _pointer_, long listener);

    /** **/
    public org.ogre4j.IScriptCompilerListener getListener() {
        return new org.ogre4j.ScriptCompilerListener(new InstancePointer(_getListener(this.object.pointer)));
    }

    private native long _getListener(long _pointer_);

    /** **/
    public void addTranslatorManager(org.ogre4j.IScriptTranslatorManager man) {
        _addTranslatorManager__ScriptTranslatorManagerp(this.object.pointer, man.getInstancePointer().pointer);
    }

    private native void _addTranslatorManager__ScriptTranslatorManagerp(long _pointer_, long man);

    /** **/
    public void removeTranslatorManager(org.ogre4j.IScriptTranslatorManager man) {
        _removeTranslatorManager__ScriptTranslatorManagerp(this.object.pointer, man.getInstancePointer().pointer);
    }

    private native void _removeTranslatorManager__ScriptTranslatorManagerp(long _pointer_, long man);

    /** **/
    public void clearTranslatorManagers() {
        _clearTranslatorManagers(this.object.pointer);
    }

    private native void _clearTranslatorManagers(long _pointer_);

    /** **/
    public org.ogre4j.IScriptTranslator getTranslator(org.ogre4j.IAbstractNodePtr node) {
        return new org.ogre4j.ScriptTranslator(new InstancePointer(_getTranslator__AbstractNodePtrR(this.object.pointer, node.getInstancePointer().pointer)));
    }

    private native long _getTranslator__AbstractNodePtrR(long _pointer_, long node);

    /** **/
    public org.ogre4j.IStringVector getScriptPatterns() {
        return new org.ogre4j.StringVector(new InstancePointer(_getScriptPatterns_const(this.object.pointer)));
    }

    private native long _getScriptPatterns_const(long _pointer_);

    /** **/
    public void parseScript(org.ogre4j.IDataStreamPtr stream, String groupName) {
        _parseScript__DataStreamPtrrStringR(this.object.pointer, stream.getInstancePointer().pointer, groupName);
    }

    private native void _parseScript__DataStreamPtrrStringR(long _pointer_, long stream, String groupName);

    /** **/
    public float getLoadingOrder() {
        return _getLoadingOrder_const(this.object.pointer);
    }

    private native float _getLoadingOrder_const(long _pointer_);

    /** 
    Override standard  retrieval. **/
    public static org.ogre4j.IScriptCompilerManager getSingleton() {
        return new org.ogre4j.ScriptCompilerManager(new InstancePointer(_getSingleton()));
    }

    private static native long _getSingleton();

    /** 
    Override standard  retrieval. **/
    public static org.ogre4j.IScriptCompilerManager getSingletonPtr() {
        return new org.ogre4j.ScriptCompilerManager(new InstancePointer(_getSingletonPtr()));
    }

    private static native long _getSingletonPtr();
}
