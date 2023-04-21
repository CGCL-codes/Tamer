package com.google.gwt.dev.jjs;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.linker.ModuleMetricsArtifact;
import com.google.gwt.core.ext.linker.PrecompilationMetricsArtifact;
import com.google.gwt.dev.Permutation;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.util.DiskCache;
import com.google.gwt.dev.util.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents a unified, non-permutation specific AST. This AST is used to drive
 * per-permutation compiles.
 */
public class UnifiedAst implements Serializable {

    /**
   * Encapsulates the combined programs.
   */
    public static final class AST implements Serializable {

        private final JProgram jProgram;

        private final JsProgram jsProgram;

        public AST(JProgram jProgram, JsProgram jsProgram) {
            this.jProgram = jProgram;
            this.jsProgram = jsProgram;
        }

        public JProgram getJProgram() {
            return jProgram;
        }

        public JsProgram getJsProgram() {
            return jsProgram;
        }
    }

    private static final DiskCache diskCache = DiskCache.INSTANCE;

    /**
   * The original AST; nulled out once consumed (by the first call to
   * {@link #getFreshAst()}.
   */
    private transient AST initialAst;

    /**
   * Metrics for the module load phase. Stored here so they can be written out
   * as artifacts in the compile phase.
   */
    private ModuleMetricsArtifact moduleMetrics;

    /**
   * Used for internal synchronization.
   */
    private transient Object myLockObject = new Object();

    /**
   * The compilation options.
   */
    private final JJSOptions options;

    /**
   * Metrics for the precompilation phase. Stored here so they can be written
   * out as artifacts in the compile phase.
   */
    private PrecompilationMetricsArtifact precompilationMetrics;

    /**
   * The set of all live rebind request types in the AST.
   */
    private final SortedSet<String> rebindRequests;

    /**
   * The serialized AST.
   */
    private transient long serializedAstToken;

    public UnifiedAst(JJSOptions options, AST initialAst, boolean singlePermutation, Set<String> rebindRequests) {
        this.options = new JJSOptionsImpl(options);
        this.initialAst = initialAst;
        this.rebindRequests = Collections.unmodifiableSortedSet(new TreeSet<String>(rebindRequests));
        this.serializedAstToken = singlePermutation ? -1 : diskCache.writeObject(initialAst);
    }

    /**
   * Copy constructor, invalidates the original.
   */
    UnifiedAst(UnifiedAst other) {
        this.options = other.options;
        this.initialAst = other.initialAst;
        other.initialAst = null;
        this.rebindRequests = other.rebindRequests;
        this.serializedAstToken = other.serializedAstToken;
    }

    /**
   * Compiles a particular permutation.
   * 
   * @param logger the logger to use
   * @param permutation the permutation to compile
   * @return the permutation result
   * @throws UnableToCompleteException if an error other than
   *           {@link OutOfMemoryError} occurs
   */
    public PermutationResult compilePermutation(TreeLogger logger, Permutation permutation) throws UnableToCompleteException {
        return JavaToJavaScriptCompiler.compilePermutation(logger, this, permutation);
    }

    /**
   * Return the current AST so that clients can explicitly walk the Java or
   * JavaScript parse trees.
   * 
   * @return the current AST object holding the Java and JavaScript trees.
   */
    public AST getFreshAst() {
        synchronized (myLockObject) {
            if (initialAst != null) {
                AST result = initialAst;
                initialAst = null;
                return result;
            } else {
                if (serializedAstToken < 0) {
                    throw new IllegalStateException("No serialized AST was cached and AST was already consumed.");
                }
                return diskCache.readObject(serializedAstToken, AST.class);
            }
        }
    }

    /**
   * Returns metrics about the module load portion of the build.
   */
    public ModuleMetricsArtifact getModuleMetrics() {
        return moduleMetrics;
    }

    /**
   * Returns the active set of JJS options associated with this compile.
   */
    public JJSOptions getOptions() {
        return new JJSOptionsImpl(options);
    }

    /**
   * Returns metrics about the precompilation portion of the build.
   */
    public PrecompilationMetricsArtifact getPrecompilationMetrics() {
        return precompilationMetrics;
    }

    /**
   * Returns the set of live rebind requests in the AST.
   */
    public SortedSet<String> getRebindRequests() {
        return rebindRequests;
    }

    /**
   * Internally prepares a new AST for compilation if one is not already
   * prepared.
   */
    public void prepare() {
        synchronized (myLockObject) {
            if (initialAst == null) {
                initialAst = diskCache.readObject(serializedAstToken, AST.class);
            }
        }
    }

    /**
   * Save some module load metrics in the AST.
   */
    public void setModuleMetrics(ModuleMetricsArtifact metrics) {
        this.moduleMetrics = metrics;
    }

    /**
   * Save some precompilation metrics in the AST.
   */
    public void setPrecompilationMetrics(PrecompilationMetricsArtifact metrics) {
        this.precompilationMetrics = metrics;
    }

    /**
   * Re-initialize lock object; copy serialized AST straight to cache.
   */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        myLockObject = new Object();
        serializedAstToken = diskCache.transferFromStream(stream);
    }

    /**
   * Force byte serialization of AST before writing.
   */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        if (serializedAstToken >= 0) {
            diskCache.transferToStream(serializedAstToken, stream);
        } else if (initialAst != null) {
            Util.writeObjectToStream(stream, initialAst);
        } else {
            throw new IllegalStateException("No serialized AST was cached and AST was already consumed.");
        }
    }
}
