package org.ogre4j;

import org.xbig.base.*;

public class ScriptLexer extends org.xbig.base.NativeObject implements org.ogre4j.IScriptLexer {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ScriptLexer(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ScriptLexer(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ScriptLexer(org.xbig.base.WithoutNativeObject val) {
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
    public ScriptLexer() {
        super(new org.xbig.base.InstancePointer(__createScriptLexer()), false);
    }

    private static native long __createScriptLexer();

    /** 
    Tokenizes the given input and returns the list of tokens found **/
    public void tokenize(org.ogre4j.IScriptTokenListPtr returnValue, String str, String source) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_tokenize__StringRStringR(this.object.pointer, str, source)), false);
    }

    private native long _tokenize__StringRStringR(long _pointer_, String str, String source);
}
