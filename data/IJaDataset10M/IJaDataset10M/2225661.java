package de.andreavicentini.visions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import de.andreavicentini.visions.Execution.IActivity;
import de.andreavicentini.visions.Execution.IContext;

public interface Atoms {

    class Equals implements IActivity<Object, Boolean> {

        private final Object token;

        private final boolean positiveComparision;

        public Equals(Object token, boolean positiveComparision) {
            this.token = token;
            this.positiveComparision = positiveComparision;
        }

        public Boolean process(IContext<Object, Boolean> ctx) throws Exception {
            boolean result = ctx.getValue() != null && ctx.getValue().toString().equals(token);
            result = this.positiveComparision ? result : !result;
            return result ? ctx.proceed() : null;
        }
    }

    class And implements IActivity {

        private final List operands = new ArrayList();

        public And add(IActivity operand) {
            this.operands.add(operand);
            return this;
        }

        public Object process(IContext ctx) throws Exception {
            for (Iterator i = this.operands.iterator(); i.hasNext(); ) {
                IActivity element = (IActivity) i.next();
                if (element.process(ctx.copy()) == null) return null;
            }
            return ctx.proceed();
        }
    }

    class StartsWith implements IActivity<String, Object> {

        private final String prefix;

        public StartsWith(String prefix) {
            this.prefix = prefix;
        }

        public Object process(IContext<String, Object> ctx) throws Exception {
            if (ctx.getValue() != null && ctx.getValue().toString().startsWith(prefix)) return ctx.proceed();
            return null;
        }
    }

    class NIL implements IActivity {

        public Object process(IContext ctx) throws Exception {
            return ctx.proceed();
        }
    }

    public class FinishActivity<T, R> implements IActivity<T, R> {

        private final R value;

        public FinishActivity(R value) {
            this.value = value;
        }

        public R process(IContext<T, R> ctx) throws Exception {
            return this.value;
        }
    }

    public class Sort implements IActivity {

        private List elements;

        public Sort(List list) {
            this.elements = list;
        }

        public Sort() {
        }

        public Object process(IContext ctx) throws Exception {
            if (this.elements == null) elements = (List) ctx.getValue();
            Collections.sort(elements);
            ctx.setValue(elements);
            return ctx.proceed();
        }
    }

    class ForEach<J, R> implements Execution.IActivity<Object, R> {

        protected Collection<J> elements;

        public ForEach(Collection<J> elements) {
            this.elements = elements;
        }

        public ForEach() {
        }

        public R process(IContext<Object, R> ctx) throws Exception {
            if (this.elements == null) {
                if (ctx.getValue() instanceof Collection) this.elements = (Collection) ctx.getValue(); else if (ctx.getValue().getClass().isArray()) this.elements = (Collection<J>) Arrays.asList(ctx.getValue());
            }
            iterate(this.elements.iterator(), ctx);
            return null;
        }

        private void iterate(Iterator iterator, IContext<Object, R> ctx) throws Exception {
            if (!iterator.hasNext()) return;
            final Object object = iterator.next();
            useObject(ctx, object);
            ctx.copy().proceed();
            iterate(iterator, ctx);
        }

        protected void useObject(IContext<Object, R> ctx, final Object object) {
            ctx.setValue(object);
        }
    }

    class SpecialForEach<J, R> implements Execution.IActivity<Collection<J>, R> {

        protected Collection<J> elements;

        private final Execution<J, R> subprocess;

        public SpecialForEach(Collection<J> elements, Execution<J, R> subprocess) {
            this.elements = elements;
            this.subprocess = subprocess;
        }

        public SpecialForEach(Execution<J, R> subprocess) {
            this.subprocess = subprocess;
        }

        public R process(IContext<Collection<J>, R> ctx) throws Exception {
            if (this.elements == null) this.elements = ctx.getValue();
            iterate(this.elements.iterator());
            return null;
        }

        private void iterate(Iterator<J> iterator) throws Exception {
            if (!iterator.hasNext()) return;
            final J object = iterator.next();
            this.subprocess.execute(object);
            iterate(iterator);
        }
    }

    class Collect<T> implements Execution.IActivity<T, Collection<T>> {

        private final Collection<T> result;

        public Collect(Collection<T> result) {
            this.result = result;
        }

        public Collection<T> process(IContext<T, Collection<T>> ctx) throws Exception {
            this.result.add(extractValue(ctx));
            return ctx.proceed();
        }

        protected T extractValue(IContext<T, Collection<T>> ctx) {
            return ctx.getValue();
        }
    }
}
