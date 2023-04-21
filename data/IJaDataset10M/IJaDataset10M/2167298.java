package com.jogamp.gluegen.opengl;

import com.jogamp.gluegen.GlueEmitterControls;
import com.jogamp.gluegen.MethodBinding;
import com.jogamp.gluegen.procaddress.ProcAddressConfiguration;
import com.jogamp.gluegen.runtime.opengl.GLExtensionNames;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

public class GLConfiguration extends ProcAddressConfiguration {

    private List<String> glHeaders = new ArrayList<String>();

    private Set<String> ignoredExtensions = new HashSet<String>();

    private Set<String> extensionsRenamedIntoCore = new HashSet<String>();

    private BuildStaticGLInfo glInfo;

    private Map<String, GLEmitter.BufferObjectKind> bufferObjectKinds = new HashMap<String, GLEmitter.BufferObjectKind>();

    private GLEmitter emitter;

    private Set<String> dropUniqVendorExtensions = new HashSet<String>();

    private boolean autoUnifyExtensions = false;

    private boolean allowNonGLExtensions = false;

    public GLConfiguration(GLEmitter emitter) {
        super();
        this.emitter = emitter;
        try {
            setProcAddressNameExpr("PFN $UPPERCASE({0}) PROC");
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Error configuring ProcAddressNameExpr", e);
        }
    }

    @Override
    protected void dispatch(String cmd, StringTokenizer tok, File file, String filename, int lineNo) throws IOException {
        if (cmd.equalsIgnoreCase("IgnoreExtension")) {
            String sym = readString("IgnoreExtension", tok, filename, lineNo);
            ignoredExtensions.add(sym);
        } else if (cmd.equalsIgnoreCase("RenameExtensionIntoCore")) {
            String sym = readString("RenameExtensionIntoCore", tok, filename, lineNo);
            extensionsRenamedIntoCore.add(sym);
        } else if (cmd.equalsIgnoreCase("AllowNonGLExtensions")) {
            allowNonGLExtensions = readBoolean("AllowNonGLExtensions", tok, filename, lineNo).booleanValue();
        } else if (cmd.equalsIgnoreCase("AutoUnifyExtensions")) {
            autoUnifyExtensions = readBoolean("AutoUnifyExtensions", tok, filename, lineNo).booleanValue();
        } else if (cmd.equalsIgnoreCase("GLHeader")) {
            String sym = readString("GLHeader", tok, filename, lineNo);
            glHeaders.add(sym);
        } else if (cmd.equalsIgnoreCase("BufferObjectKind")) {
            readBufferObjectKind(tok, filename, lineNo);
        } else if (cmd.equalsIgnoreCase("DropUniqVendorExtensions")) {
            String sym = readString("DropUniqVendorExtensions", tok, filename, lineNo);
            dropUniqVendorExtensions.add(sym);
        } else {
            super.dispatch(cmd, tok, file, filename, lineNo);
        }
    }

    protected void readBufferObjectKind(StringTokenizer tok, String filename, int lineNo) {
        try {
            String kindString = tok.nextToken();
            GLEmitter.BufferObjectKind kind = null;
            String target = tok.nextToken();
            if (kindString.equalsIgnoreCase("UnpackPixel")) {
                kind = GLEmitter.BufferObjectKind.UNPACK_PIXEL;
            } else if (kindString.equalsIgnoreCase("PackPixel")) {
                kind = GLEmitter.BufferObjectKind.PACK_PIXEL;
            } else if (kindString.equalsIgnoreCase("Array")) {
                kind = GLEmitter.BufferObjectKind.ARRAY;
            } else if (kindString.equalsIgnoreCase("Element")) {
                kind = GLEmitter.BufferObjectKind.ELEMENT;
            } else {
                throw new RuntimeException("Error parsing \"BufferObjectKind\" command at line " + lineNo + " in file \"" + filename + "\": illegal BufferObjectKind \"" + kindString + "\", expected one of UnpackPixel, PackPixel, Array, or Element");
            }
            bufferObjectKinds.put(target, kind);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Error parsing \"BufferObjectKind\" command at line " + lineNo + " in file \"" + filename + "\"", e);
        }
    }

    /** Overrides javaPrologueForMethod in superclass and
    automatically generates prologue code for functions associated
    with buffer objects. */
    @Override
    public List<String> javaPrologueForMethod(MethodBinding binding, boolean forImplementingMethodCall, boolean eraseBufferAndArrayTypes) {
        List<String> res = super.javaPrologueForMethod(binding, forImplementingMethodCall, eraseBufferAndArrayTypes);
        GLEmitter.BufferObjectKind kind = getBufferObjectKind(binding.getName());
        if (kind != null) {
            ArrayList<String> res2 = new ArrayList<String>();
            if (res != null) {
                res2.addAll(res);
            }
            res = res2;
            String prologue = "check";
            if (kind == GLEmitter.BufferObjectKind.UNPACK_PIXEL) {
                prologue = prologue + "UnpackPBO";
            } else if (kind == GLEmitter.BufferObjectKind.PACK_PIXEL) {
                prologue = prologue + "PackPBO";
            } else if (kind == GLEmitter.BufferObjectKind.ARRAY) {
                prologue = prologue + "ArrayVBO";
            } else if (kind == GLEmitter.BufferObjectKind.ELEMENT) {
                prologue = prologue + "ElementVBO";
            } else {
                throw new RuntimeException("Unknown BufferObjectKind " + kind);
            }
            if (emitter.isBufferObjectMethodBinding(binding)) {
                prologue = prologue + "Enabled";
            } else {
                prologue = prologue + "Disabled";
            }
            prologue = prologue + "(true);";
            res.add(0, prologue);
            if (emitter.isBufferObjectMethodBinding(binding)) {
                for (Iterator<String> iter = res.iterator(); iter.hasNext(); ) {
                    String line = iter.next();
                    if (line.indexOf("Buffers.rangeCheck") >= 0) {
                        iter.remove();
                    }
                }
            }
        }
        return res;
    }

    @Override
    public void dumpIgnores() {
        System.err.println("GL Ignored extensions: ");
        for (String str : ignoredExtensions) {
            System.err.println("\t" + str);
        }
        super.dumpIgnores();
    }

    protected boolean shouldIgnoreExtension(String symbol, boolean criteria) {
        if (criteria && glInfo != null) {
            String extension = glInfo.getExtension(symbol);
            if (extension != null && ignoredExtensions.contains(extension)) {
                return true;
            }
            boolean isGLEnum = GLExtensionNames.isGLEnumeration(symbol);
            boolean isGLFunc = GLExtensionNames.isGLFunction(symbol);
            if (isGLFunc || isGLEnum) {
                if (GLExtensionNames.isExtensionVEN(symbol, isGLFunc)) {
                    String extSuffix = GLExtensionNames.getExtensionSuffix(symbol, isGLFunc);
                    if (getDropUniqVendorExtensions(extSuffix)) {
                        if (DEBUG_IGNORES) {
                            System.err.println("Ignore UniqVendorEXT: " + symbol + ", vendor " + extSuffix);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldIgnoreInInterface(String symbol) {
        return shouldIgnoreInInterface(symbol, true);
    }

    public boolean shouldIgnoreInInterface(String symbol, boolean checkEXT) {
        return shouldIgnoreExtension(symbol, checkEXT) || super.shouldIgnoreInInterface(symbol);
    }

    @Override
    public boolean shouldIgnoreInImpl(String symbol) {
        return shouldIgnoreInImpl(symbol, true);
    }

    public boolean shouldIgnoreInImpl(String symbol, boolean checkEXT) {
        return shouldIgnoreExtension(symbol, checkEXT) || super.shouldIgnoreInImpl(symbol);
    }

    /** Should we automatically ignore extensions that have already been
    fully subsumed into the OpenGL core namespace, and if they have
    not been, indicate which definition is not already in the core? */
    public boolean getAutoUnifyExtensions() {
        return autoUnifyExtensions;
    }

    /** If true, accept all non encapsulated defines and functions,
     * as it is mandatory for GL declarations. */
    public boolean getAllowNonGLExtensions() {
        return allowNonGLExtensions;
    }

    /** shall the non unified (uniq) vendor extensions be dropped ?  */
    public boolean getDropUniqVendorExtensions(String extName) {
        return dropUniqVendorExtensions.contains(extName);
    }

    /** Returns the kind of buffer object this function deals with, or
    null if none. */
    GLEmitter.BufferObjectKind getBufferObjectKind(String name) {
        return bufferObjectKinds.get(name);
    }

    public boolean isBufferObjectFunction(String name) {
        return (getBufferObjectKind(name) != null);
    }

    /** Parses any GL headers specified in the configuration file for
    the purpose of being able to ignore an extension at a time. */
    public void parseGLHeaders(GlueEmitterControls controls) throws IOException {
        if (!glHeaders.isEmpty()) {
            glInfo = new BuildStaticGLInfo();
            for (String file : glHeaders) {
                String fullPath = controls.findHeaderFile(file);
                if (fullPath == null) {
                    throw new IOException("Unable to locate header file \"" + file + "\"");
                }
                glInfo.parse(fullPath);
            }
        }
    }

    /** Returns the information about the association between #defines,
    function symbols and the OpenGL extensions they are defined
    in. */
    public BuildStaticGLInfo getGLInfo() {
        return glInfo;
    }

    /** Returns the OpenGL extensions that should have all of their
    constant definitions and functions renamed into the core
    namespace; for example, glGenFramebuffersEXT to
    glGenFramebuffers and GL_FRAMEBUFFER_EXT to GL_FRAMEBUFFER. */
    public Set<String> getExtensionsRenamedIntoCore() {
        return extensionsRenamedIntoCore;
    }
}
