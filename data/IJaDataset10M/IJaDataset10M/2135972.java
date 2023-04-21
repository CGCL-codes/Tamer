package ch.unibe.jexample.internal.util;

import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import ch.unibe.jexample.internal.Example;
import ch.unibe.jexample.internal.ExampleGraph;

public abstract class Util {

    public static class NotCloneable {

        public final String name;

        public NotCloneable(String name) {
            this.name = name;
        }
    }

    public static class IsCloneable implements Cloneable {

        public final String name;

        public IsCloneable(String name) {
            this.name = name;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return new IsCloneable("clone of " + name);
        }
    }

    public static Example runExample(Class<?> container, String name) {
        ExampleGraph g = new ExampleGraph();
        try {
            g.add(container);
        } catch (JExampleError ex) {
            throw new RuntimeException(ex);
        }
        Example e = g.findExample(container, name);
        e.run(new RunNotifier());
        return e;
    }

    public static Result runAllExamples(Class<?>... constainers) {
        ExampleGraph g = new ExampleGraph();
        try {
            return g.runJExample(constainers);
        } catch (JExampleError ex) {
            throw new RuntimeException(ex);
        }
    }
}
