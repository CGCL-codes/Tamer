package com.google.code.jtracert.traceBuilder.impl;

import com.google.code.jtracert.config.AnalyzeProperties;
import com.google.code.jtracert.model.MethodCall;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilder;
import com.google.code.jtracert.traceBuilder.MethodCallTraceBuilderFactory;
import com.google.code.jtracert.traceBuilder.impl.graph.ClassNameResolverMethodCallGraphVisitor;
import com.google.code.jtracert.traceBuilder.impl.graph.HashCodeBuilderMethodCallGraphVisitor;
import com.google.code.jtracert.traceBuilder.impl.graph.NormalizeMetodCallGraphVisitor;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditFileClient;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditOutClient;
import com.google.code.jtracert.traceBuilder.impl.sdedit.SDEditRtClient;
import com.google.code.jtracert.traceBuilder.impl.sequence.SequenceFileClient;
import com.google.code.jtracert.traceBuilder.impl.sequence.SequenceOutClient;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpClient;
import com.google.code.jtracert.traceBuilder.impl.serializable.SerializableTcpServer;
import com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams.WebSequenceDiagramsFileClient;
import com.google.code.jtracert.traceBuilder.impl.webSequenceDiagrams.WebSequenceDiagramsOutClient;
import com.google.code.jtracert.util.ClassUtils;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.security.ProtectionDomain;
import java.security.CodeSource;
import java.lang.instrument.Instrumentation;
import java.lang.ref.Reference;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
class MethodCallTraceBuilderState {

    public MethodCall methodCall = null;

    public boolean buildingTrace = false;

    public int level = 1;

    public int count = 0;

    /**
     * @return
     */
    @Override
    public String toString() {
        return "MethodCallTraceBuilderState{" + "methodCall=" + methodCall + ", buildingTrace=" + buildingTrace + ", level=" + level + ", count=" + count + '}';
    }
}

/**
 *
 */
class MethodCallTraceBuilderStateThreadLocal extends ThreadLocal<MethodCallTraceBuilderState> {

    /**
     * @return
     */
    @Override
    protected MethodCallTraceBuilderState initialValue() {
        return new MethodCallTraceBuilderState();
    }
}

/**
 * The default thread factory
 */
class JTracertThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(1);

    final ThreadGroup group;

    final AtomicInteger threadNumber = new AtomicInteger(1);

    final String namePrefix;

    /**
     *
     */
    JTracertThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "jTracert-" + poolNumber.getAndIncrement() + "-thread-";
    }

    /**
     * @param r
     * @return
     */
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY) t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}

/**
 *
 */
public class MethodCallTraceBuilderImpl implements MethodCallTraceBuilder {

    private Instrumentation instrumentation;

    private static MethodCallTraceBuilderStateThreadLocal traceBuilderState = new MethodCallTraceBuilderStateThreadLocal();

    private ThreadPoolExecutor executorService;

    private Set<Integer> processedHashCodes;

    public void setBuildingTrace(boolean buildingTrace) {
        MethodCallTraceBuilderState state = traceBuilderState.get();
        state.buildingTrace = buildingTrace;
    }

    /**
     *
     */
    public MethodCallTraceBuilderImpl() {
        processedHashCodes = new HashSet<Integer>();
        executorService = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5, true), new JTracertThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("Shutting down");
                    System.out.println("Active tasks count: " + executorService.getActiveCount());
                }
                for (int i = 0; i < 100; i++) Thread.yield();
                try {
                    executorService.awaitTermination(1L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                executorService.shutdown();
                try {
                    executorService.awaitTermination(5L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("Shut down");
                    System.out.println("Active tasks count: " + executorService.getActiveCount());
                }
                printMemoryHeap();
            }
        }));
    }

    public void enter(String className, String methodName, String methodDescriptor, Object object) {
        enter(className, methodName, methodDescriptor, object, null);
    }

    /**
     * @param className
     * @param methodName
     * @param methodDescriptor
     * @param object
     * @param arguments
     */
    public void enter(String className, String methodName, String methodDescriptor, Object object, Object[] arguments) {
        MethodCallTraceBuilderState state = traceBuilderState.get();
        if (state.buildingTrace) return;
        try {
            state.buildingTrace = true;
            if (methodName.equals(ClassUtils.CONSTRUCTOR_METHOD_NAME) && null != object) {
                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("Entering " + className + "." + methodName + methodDescriptor);
                }
                MethodCall contextMethodCall = state.methodCall;
                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("contextMethodCall=" + contextMethodCall);
                }
                if (null != contextMethodCall && ClassUtils.CONSTRUCTOR_METHOD_NAME.equals(contextMethodCall.getMethodName()) && className.equals(contextMethodCall.getRealClassName())) {
                    contextMethodCall.setObjectHashCode(System.identityHashCode(object));
                }
                newObject(object);
            } else {
                state.level++;
                state.count++;
                if (null != getAnalyzeProperties() && state.count > getAnalyzeProperties().getMaximalTraceLength()) return;
                MethodCall currentMethodCall = new MethodCall();
                MethodCall contextMethodCall = state.methodCall;
                if (null == contextMethodCall) {
                } else {
                    contextMethodCall.addCallee(currentMethodCall);
                }
                state.methodCall = currentMethodCall;
                currentMethodCall.setMethodName(methodName);
                currentMethodCall.setMethodSignature(methodDescriptor);
                if (object == null) {
                    currentMethodCall.setObjectHashCode(className.hashCode());
                } else {
                    currentMethodCall.setObjectHashCode(System.identityHashCode(object));
                }
                if (object == null) {
                    currentMethodCall.setRealClassName(className);
                } else {
                    currentMethodCall.setRealClassName(object.getClass().getName());
                    ProtectionDomain protectionDomain = object.getClass().getProtectionDomain();
                    if (null != protectionDomain) {
                        CodeSource codeSource = protectionDomain.getCodeSource();
                        if (null != codeSource) {
                            URL location = codeSource.getLocation();
                            if (null != location) {
                                currentMethodCall.setJarUrl(location.toString());
                            }
                        }
                    }
                }
            }
            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println(className + "." + methodName + methodDescriptor + " <<<");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            state.buildingTrace = false;
        }
    }

    /**
     * @param methodCall
     */
    private void graphFinished(final MethodCall methodCall) {
        if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
        }
        executorService.execute(new SDEditClientRunnable(methodCall, getAnalyzeProperties()));
    }

    private AnalyzeProperties analyzeProperties;

    /**
     * @return
     */
    public AnalyzeProperties getAnalyzeProperties() {
        return analyzeProperties;
    }

    /**
     * @param analyzeProperties
     */
    public void setAnalyzeProperties(AnalyzeProperties analyzeProperties) {
        this.analyzeProperties = analyzeProperties;
    }

    /**
     *
     */
    private class SDEditClientRunnable implements Runnable {

        private MethodCall methodCall;

        private AnalyzeProperties analyzeProperties;

        /**
         * @param methodCall
         * @param analyzeProperties
         */
        public SDEditClientRunnable(MethodCall methodCall, AnalyzeProperties analyzeProperties) {
            this.methodCall = methodCall;
            this.analyzeProperties = analyzeProperties;
        }

        /**
         *
         */
        public void run() {
            MethodCallTraceBuilderFactory.getMethodCallTraceBuilder().setBuildingTrace(true);
            try {
                long currentTime = System.nanoTime();
                boolean verbose = (null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose());
                verbose = true;
                if (verbose) {
                    System.out.println("Normalizing Call Graph <<<");
                }
                methodCall.accept(new NormalizeMetodCallGraphVisitor());
                if (verbose) {
                    System.out.println("Normalizing Call Graph >>>");
                    System.out.println("Took " + (System.nanoTime() - currentTime) + " nano seconds");
                }
                currentTime = System.nanoTime();
                if (verbose) {
                    System.out.println("Calculating Call Graph Hash <<<");
                }
                int hashCode = methodCall.accept(new HashCodeBuilderMethodCallGraphVisitor());
                if (verbose) {
                    System.out.println("Calculating Call Graph Hash >>>");
                    System.out.println("Took " + (System.nanoTime() - currentTime) + " nano seconds");
                }
                if (!processedHashCodes.contains(hashCode)) {
                    processedHashCodes.add(hashCode);
                    MethodCallProcessor methodCallProcessor = null;
                    if (null != analyzeProperties) {
                        switch(analyzeProperties.getAnalyzerOutput()) {
                            case none:
                                break;
                            case sdEditOut:
                                methodCallProcessor = new SDEditOutClient();
                                break;
                            case sdEditRtClient:
                                methodCallProcessor = new SDEditRtClient();
                                break;
                            case sdEditFileSystem:
                                methodCallProcessor = new SDEditFileClient();
                                break;
                            case sequenceOut:
                                methodCallProcessor = new SequenceOutClient();
                                break;
                            case sequenceFileSystem:
                                methodCallProcessor = new SequenceFileClient();
                                break;
                            case webSequenceDiagramsOut:
                                methodCallProcessor = new WebSequenceDiagramsOutClient();
                                break;
                            case webSequenceDiagramsFileSystem:
                                methodCallProcessor = new WebSequenceDiagramsFileClient();
                                break;
                            case serializableTcpClient:
                                methodCallProcessor = new SerializableTcpClient();
                                break;
                            case serializableTcpServer:
                                methodCallProcessor = SerializableTcpServer.getIstance();
                                break;
                        }
                    }
                    if (null != methodCallProcessor) {
                        methodCallProcessor.setAnalyzeProperties(analyzeProperties);
                        if (analyzeProperties.isShortenClassNames()) {
                            currentTime = System.nanoTime();
                            if (verbose) {
                                System.out.println("Normalize class names <<<");
                            }
                            ClassNameResolverMethodCallGraphVisitor classNameResolver = new ClassNameResolverMethodCallGraphVisitor();
                            methodCall.accept(classNameResolver);
                            classNameResolver.setRenaming();
                            methodCall.accept(classNameResolver);
                            if (verbose) {
                                System.out.println("Normalize class names >>>");
                                System.out.println("Took " + (System.nanoTime() - currentTime) + " nano seconds");
                            }
                        }
                        if (verbose) {
                            System.out.println("Executing process method call for " + methodCall.getRealClassName() + "." + methodCall.getMethodName() + " <<<");
                        }
                        methodCallProcessor.processMethodCall(methodCall);
                        if (verbose) {
                            System.out.println("Executing process method call for " + methodCall.getRealClassName() + "." + methodCall.getMethodName() + " >>>");
                        }
                    }
                }
            } finally {
                MethodCallTraceBuilderFactory.getMethodCallTraceBuilder().setBuildingTrace(false);
            }
        }
    }

    /**
     *
     */
    public void leave() {
        MethodCallTraceBuilderState state = traceBuilderState.get();
        if (null == state) return;
        if (state.buildingTrace) return;
        try {
            state.buildingTrace = true;
            state.level--;
            MethodCall contextMethodCall = state.methodCall;
            if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                System.out.println(contextMethodCall.getRealClassName() + "." + contextMethodCall.getMethodName() + contextMethodCall.getMethodSignature() + " >>>");
            }
            if (null != getAnalyzeProperties() && state.count == getAnalyzeProperties().getMaximalTraceLength()) {
                if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
                    System.out.println("Too large trace detected - trimmed to " + getAnalyzeProperties().getMaximalTraceLength() + " method calls");
                }
                graphFinished(contextMethodCall);
            }
            if (1 == state.level) {
                if (null == getAnalyzeProperties()) {
                    graphFinished(contextMethodCall);
                } else {
                    if (getAnalyzeProperties().isVerbose()) {
                        System.out.println("state.count=" + state.count);
                    }
                    if (state.count < getAnalyzeProperties().getMinimalTraceLength()) {
                        if (getAnalyzeProperties().isVerbose()) {
                            System.out.println("Too short trace detected - will not be processed");
                        }
                    } else {
                        graphFinished(contextMethodCall);
                    }
                }
                traceBuilderState.remove();
            } else {
                if (state.count > getAnalyzeProperties().getMaximalTraceLength()) return;
                state.methodCall = contextMethodCall.getCalleer();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            state.buildingTrace = false;
        }
    }

    /**
     * @param returnValue
     */
    public void leave(Object returnValue) {
        leave();
    }

    /**
     * @param e
     */
    public void exception(Throwable e) {
        leave();
    }

    /**
     * @param methodDescriptor
     */
    @Deprecated
    public void leaveConstructor(String methodDescriptor) {
        leave();
    }

    /**
     * @param className
     * @param methodName
     * @param methodDescriptor
     * @param exception
     */
    @Deprecated
    public void leaveConstructor(String className, String methodName, String methodDescriptor, Throwable exception) {
        exception(exception);
    }

    /**
     * @param className
     * @param methodDescriptor
     */
    public void preEnterConstructor(String className, String methodDescriptor) {
        if ((null != getAnalyzeProperties()) && (getAnalyzeProperties().isVerbose())) {
            System.out.println("Pre entering constructor " + className + ".<init>" + methodDescriptor);
        }
        enter(className, ClassUtils.CONSTRUCTOR_METHOD_NAME, methodDescriptor, null, null);
    }

    /**
     * @param object a newly instantiated object
     * @todo add these objects to ReferenceQueue in order to enable heap profiling
     */
    public void newObject(Object object) {
    }

    public void printMemoryHeap() {
    }

    private List<Reference<Object>> objectReferences = new LinkedList<Reference<Object>>();

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }
}
