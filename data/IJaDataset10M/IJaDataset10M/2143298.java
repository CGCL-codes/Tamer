package com.google.gwt.dev;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.jjs.PermutationResult;
import com.google.gwt.dev.util.FileBackedObject;

/**
 * Represents a facility that can compile an individual {@link Permutation}.
 * Instances of PermutationWorker are expected to be created via
 * {@link PermutationWorkerFactory#getWorkers(int)}.
 */
interface PermutationWorker {

    /**
   * Compile a single permutation. The {@link com.google.gwt.dev.jjs.UnifiedAst}
   * will have been provided to {@link PermutationWorkerFactory#getWorkers}
   * method. The compiled PermutationResult will be returned via the
   * <code>resultFile</code> parameter.
   * 
   * @throws TransientWorkerException if the Permutation should be tried again
   *           on another worker
   * @throws UnableToCompleteException if the compile fails for any reason
   */
    void compile(TreeLogger logger, Permutation permutation, FileBackedObject<PermutationResult> resultFile) throws TransientWorkerException, UnableToCompleteException;

    /**
   * Returns a human-readable description of the worker instance. This may be
   * used for error reporting.
   */
    String getName();

    /**
   * Release any resources associated with the worker.
   */
    void shutdown();
}
