package com.google.inject.tools.suite.module;

import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ElementVisitor;
import com.google.inject.tools.suite.Messenger;
import com.google.inject.tools.suite.SampleModuleScenario;
import com.google.inject.tools.suite.code.CodeRunner;
import com.google.inject.tools.suite.module.ModuleContextRepresentationImpl;
import com.google.inject.tools.suite.snippets.BindingCodeLocation;
import com.google.inject.tools.suite.snippets.CodeLocation;
import com.google.inject.tools.suite.snippets.CodeSnippetResult;
import com.google.inject.tools.suite.snippets.ModuleContextSnippet;
import com.google.inject.tools.suite.snippets.problems.CodeProblem;
import junit.framework.TestCase;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test the {@link ModuleContextRepresentationImpl}.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class ModuleContextRepresentationTest extends TestCase {

    public void testModuleContextRepresentation() throws Exception {
        ModuleContextRepresentationImpl moduleContext = new ModuleContextRepresentationImpl("Working Module Context");
        CodeRunner codeRunner = new SimulatedCodeRunner();
        moduleContext.clean(codeRunner);
        codeRunner.run("", true);
        codeRunner.waitFor();
        assertFalse(moduleContext.isDirty());
        assertTrue(moduleContext.getName().equals("Working Module Context"));
        CodeLocation codeLocation = moduleContext.findLocation(SampleModuleScenario.MockInjectedInterface.class.getName(), null);
        assertTrue(codeLocation instanceof BindingCodeLocation);
        BindingCodeLocation bindingLocation = (BindingCodeLocation) codeLocation;
        assertTrue(bindingLocation.bindTo().equals("class " + SampleModuleScenario.MockInjectedInterfaceImpl.class.getName()));
    }

    public static class SimulatedCodeRunner implements CodeRunner {

        private CodeRunListener listener;

        public void addListener(CodeRunListener listener) {
            this.listener = listener;
        }

        public Messenger getMessenger() {
            return new Messenger() {

                public void display(String message) {
                }

                public void logException(String label, Throwable throwable) {
                }

                public void logMessage(String message) {
                }

                public void logCodeRunnerMessage(String message) {
                }

                public void logCodeRunnerException(String label, Throwable throwable) {
                }
            };
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        public boolean isDone(Runnable runnable) {
            return true;
        }

        public void kill() {
        }

        public void kill(Runnable runnable) {
        }

        public void waitFor() {
        }

        public void waitFor(Runnable runnable) {
        }

        public void notifyResult(Runnable runnable, CodeSnippetResult result) {
            listener.acceptCodeRunResult(result);
        }

        public void notifyDone(Runnable runnable) {
        }

        public void queue(Runnable runnable) {
        }

        public void run(String label, boolean backgroundAutomatically) {
            notifyResult(null, simulatedSnippetResult());
        }

        public void run(String label) {
            run(label, true);
        }

        private ModuleContextSnippet.ModuleContextResult simulatedSnippetResult() {
            Map<Key<?>, Binding<?>> bindings = new HashMap<Key<?>, Binding<?>>();
            Binding<?> binding = new MockBinding<com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterface>(com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterface.class, com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterfaceImpl.class);
            bindings.put(Key.get(com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterface.class), binding);
            return new ModuleContextSnippet.ModuleContextResult("Working Module Context", Collections.singleton(new ModuleContextSnippet.ModuleRepresentation(SampleModuleScenario.WorkingModule.class, null, null)), Collections.<CodeProblem>emptySet());
        }

        public static class MockBinding<T> implements Binding<T> {

            public Scope getScope() {
                return null;
            }

            private final Class<T> bindWhat;

            private final Class<? extends T> bindTo;

            public MockBinding(Class<T> bindWhat, Class<? extends T> bindTo) {
                this.bindWhat = bindWhat;
                this.bindTo = bindTo;
            }

            public StackTraceElement getSource() {
                return null;
            }

            public Provider<T> getProvider() {
                return new MockProvider<T>(bindTo);
            }

            public Key<T> getKey() {
                return Key.get(bindWhat);
            }

            public void acceptTargetVisitor(BindingTargetVisitor<? super T, Void> visitor) {
            }

            public static class MockProvider<T> implements Provider<T> {

                private final Class<? extends T> bindsTo;

                public MockProvider(Class<? extends T> bindsTo) {
                    this.bindsTo = bindsTo;
                }

                public T get() {
                    T result;
                    try {
                        result = bindsTo.newInstance();
                    } catch (Exception e) {
                        result = null;
                    }
                    return result;
                }
            }

            public <V> V acceptScopingVisitor(BindingScopingVisitor<V> arg0) {
                return null;
            }

            public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> arg0) {
                return null;
            }

            @SuppressWarnings("hiding")
            public <T> T acceptVisitor(ElementVisitor<T> arg0) {
                return null;
            }
        }
    }
}
