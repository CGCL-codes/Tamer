package org.fgraph.steps;

import java.io.Serializable;
import java.util.*;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 *  Set of utility functions that takes a starting step
 *  and produces zero to many result child steps.
 *
 *  <p>Unless otherwise specified, all methods return serializable
 *  functions as long as they're given serializable parameters.</p>
 *
 *  @version   $Revision: 562 $
 *  @author    Paul Speed
 */
public class StepFunctions {

    /**
     *  Chains two functions together such the the individual element
     *  results from the first function are passed one at a time to the
     *  second function.  All of the result Iterables are aggregated.
     *  Note: The argument ordering here is the opposite of of Functions.compose().
     */
    public static <A, B, C> StepFunction<A, C> chain(StepFunction<A, ? extends B> f1, StepFunction<B, C> f2) {
        return new ChainFunction<A, B, C>(f1, f2);
    }

    /**
     *  Creates a function that concatenates the output of two other
     *  functions provided the common apply() input value.
     */
    public static <S, T> StepFunction<S, T> concat(StepFunction<S, T> f1, StepFunction<S, T> f2) {
        return new ConcatFunction<S, T>(f1, f2);
    }

    /**
     *  Creates a function that returns a fixed set of results no matter
     *  the input.
     */
    public static <S, T> StepFunction<S, T> constant(T... values) {
        List<T> list;
        if (values == null) list = Collections.singletonList(null); else list = Arrays.asList(values);
        return new ConstantFunction<S, T>(list);
    }

    /**
     *  Creates a StepFunction adapter that transforms its input to a
     *  singleton Collection wrapped output using the supplied transform function.
     *  In other words, the actual processing is basically:
     *  Collections.singleton( transform.apply(value) )
     *
     *  <p>For the typical use-case, it is better to call the two-arg
     *  transform method that takes a delegate step function.  This function
     *  is for composing chains where an input function may not be known
     *  or available at creation.  There is additional overhead to create
     *  the one-per result singleton Iterables that is avoided in the two-arg
     *  version.</p>
     */
    public static <S, T> StepFunction<S, T> transform(Function<S, T> transform) {
        return new TransformDecorator<S, T>(transform);
    }

    /**
     *  Creates a StepFunction adapter that filters its input
     *  by passing it through the specified predicate.  If the source
     *  passes the filter then it is wrapped in a Collection.sinleton()
     *  for return, otherwise an empty Iterable is returned.
     *
     *  <p>For the typical use-case, it is better to call the two-arg
     *  filter() method that takes a delegate function.  This function is
     *  for composing chains where an input function may not be known or
     *  or available at creation.  There is additional overhead to create
     *  the one-per result singleton Iterables that is avoided in the two-arg
     *  version.</p>
     */
    public static <T> StepFunction<T, T> filter(Predicate<T> filter) {
        return new FilterDecorator<T>(filter);
    }

    /**
     *  Creates a function that adapts non-Step based results into
     *  root-level Step results.
     */
    public static <T> StepFunction<T, Step<T>> wrapSteps() {
        return wrapSteps((Step) null);
    }

    /**
     *  Creates a function that adapts non-Step based results into
     *  Step results with the supplied constant parent.
     */
    public static <T> StepFunction<T, Step<T>> wrapSteps(Step parent) {
        return transform(new WrapStep<T>(parent));
    }

    /**
     *  Creates a function that adapts the results of the specified
     *  delegate function into Steps using the specific constant
     *  parent.  This is the preferred method of wrapping if another
     *  function is producing the non-step results, versus the single-arg
     *  wrapSteps which is useful at the head of a chain.
     */
    public static <S, T> StepFunction<S, Step<T>> wrapSteps(StepFunction<S, T> delegate, Step parent) {
        if (delegate == null) throw new IllegalArgumentException("delegate cannot be null.");
        return transform(delegate, new WrapStep<T>(parent));
    }

    /**
     *  Creates a function that adapts the results of the specified
     *  delegate function into Steps using the Step supplied to apply()
     *  as the parent and its value as the value passed to the delegate
     *  function.  This is useful for adapting non-step based functions
     *  into step-based chains where the full path is maintained.
     */
    public static <S, T> StepFunction<Step<S>, Step<T>> adaptToSteps(StepFunction<S, T> delegate) {
        return new StepAdapter<S, T>(delegate);
    }

    /**
     *  Creates a function that adapts a Step-based function for non-step
     *  usage by wrapping any applied objects in a DefaultStep and unwrapping
     *  any returned results.  In other words, a function that takes a Step<S> and produces
     *  Step<T>'s can be adapted into a function that taks <S> and produces
     *  <T>.  This effectively equivalent to unwrapSteps( chain( wrapSteps, delegate ) )
     */
    public static <S, T> StepFunction<S, T> adaptFromSteps(StepFunction<Step<S>, Step<T>> delegate) {
        return unwrapSteps(chain(StepFunctions.<S>wrapSteps(), delegate));
    }

    /**
     *  Creates a function that unwraps Step-based results into
     *  their component values.
     */
    public static <T> StepFunction<Step<T>, T> unwrapSteps() {
        return transform(new UnwrapStep<T>());
    }

    /**
     *  Creates a function that unwraps the results of the
     *  specified delegate function into the Step component values.
     */
    public static <S, T> StepFunction<S, T> unwrapSteps(StepFunction<S, ? extends Step<T>> delegate) {
        return transform(delegate, new UnwrapStep<T>());
    }

    /**
     *  Creates a StepFunction that filters the results of a delegate
     *  StepFunction using the supplied predicate filter.
     */
    public static <S, T> StepFunction<S, T> filter(StepFunction<S, T> delegate, Predicate<? super T> filter) {
        return new FilterFunction<S, T>(delegate, filter);
    }

    /**
     *  Creates a StepFunction that transforms the results of a delegate
     *  StepFunction using the supplied transform function.
     */
    public static <A, B, C> StepFunction<A, C> transform(StepFunction<A, B> delegate, Function<? super B, C> transform) {
        return new TransformFunction<A, B, C>(delegate, transform);
    }

    /**
     *  Executes one of two delegate functions depending on the result
     *  of a predicate applied to the source object.
     */
    public static <S, T> StepFunction<S, T> branch(Predicate<? super S> condition, StepFunction<S, T> doTrue, StepFunction<S, T> doFalse) {
        return new ConditionalDelegator<S, T>(condition, doTrue, doFalse);
    }

    /**
     *  Convenience method that returns empty results for any source matching
     *  the supplied predicate and leaves those results out of any other
     *  adjacency sets.  This is useful for pruning traversal hierarchies.
     *  This is the equivalent of branch( condition, null, delegate ) with a
     *  filter( branch, not condition )
     */
    public static <S, T extends S> StepFunction<S, T> prune(StepFunction<S, T> delegate, Predicate<? super S> condition) {
        StepFunction<S, T> b = branch(condition, null, delegate);
        return filter(b, Predicates.not(condition));
    }

    /**
     *  Wraps the non-step elements of the specified Iterable
     *  in the DefaultStep with the supplied Step as a parent.
     *  This is used by other function implementations to adapt non-step
     *  based access into steps.
     */
    public static <T> Iterable<Step<T>> doWrapSteps(Step parent, Iterable<T> children) {
        return Iterables.transform(children, new WrapStep<T>(parent));
    }

    /**
     *  Returns a raw function that can accept a single Step<T> and return
     *  a unwrapped single T value.
     */
    public static <T> Function<Step<T>, T> unwrapStep() {
        return new UnwrapStep<T>();
    }

    /**
     *  Returns a raw function that can accept a single T and return
     *  a wrapped root Step<T> value.
     */
    public static <T> Function<T, Step<T>> wrapStep() {
        return new WrapStep<T>(null);
    }

    /**
     *  Returns a raw function that can accept a single T and return
     *  a wrapped Step<T> value with the specified parent.
     */
    public static <T> Function<T, Step<T>> wrapStep(Step parent) {
        return new WrapStep<T>(parent);
    }

    private static class WrapStep<T> implements Function<T, Step<T>>, Serializable {

        static final long serialVersionUID = 1L;

        private Step parent;

        public WrapStep(Step parent) {
            this.parent = parent;
        }

        public Step<T> apply(T value) {
            return new DefaultStep<T>(parent, value);
        }

        public String toString() {
            if (parent == null) return "WrapStep[]";
            return "WrapStep[parent=" + parent + "]";
        }
    }

    private static class UnwrapStep<T> implements Function<Step<T>, T>, Serializable {

        static final long serialVersionUID = 1L;

        public UnwrapStep() {
        }

        public T apply(Step<T> step) {
            return step.getValue();
        }

        public String toString() {
            return "UnwrapStep[]";
        }
    }

    /**
     *  Wraps a delegate function to adapt it into a
     *  step-based StepFunction.
     */
    private static class StepAdapter<S, T> implements StepFunction<Step<S>, Step<T>>, Serializable {

        static final long serialVersionUID = 1L;

        private Function<S, ? extends Iterable<T>> delegate;

        public StepAdapter(Function<S, ? extends Iterable<T>> delegate) {
            if (delegate == null) throw new IllegalArgumentException("delegate cannot be null.");
            this.delegate = delegate;
        }

        public Iterable<Step<T>> apply(Step<S> step) {
            return Iterables.transform(delegate.apply(step.getValue()), new WrapStep<T>(step));
        }

        public String toString() {
            return "StepAdapter[" + delegate + "]";
        }
    }

    private static class ConstantFunction<S, T> implements StepFunction<S, T>, Serializable {

        static final long serialVersionUID = 1L;

        private List<T> values;

        public ConstantFunction(List<T> values) {
            this.values = values;
        }

        public Iterable<T> apply(S object) {
            return values;
        }

        public String toString() {
            return "Constant[" + values + "]";
        }
    }

    /**
     *  Executes a delegate function based on the result of a Predicate.
     *  Either the true function or the false function is executed.
     *  If no function exists for a particular branch then an empty
     *  Iterable is returned.
     */
    private static class ConditionalDelegator<S, T> implements StepFunction<S, T>, Serializable {

        static final long serialVersionUID = 1L;

        private Predicate<? super S> condition;

        private Function<S, ? extends Iterable<T>> trueDelegate;

        private Function<S, ? extends Iterable<T>> falseDelegate;

        public ConditionalDelegator(Predicate<? super S> condition, Function<S, ? extends Iterable<T>> trueDelegate, Function<S, ? extends Iterable<T>> falseDelegate) {
            this.condition = condition;
            this.trueDelegate = trueDelegate;
            this.falseDelegate = falseDelegate;
        }

        public Iterable<T> apply(S object) {
            if (condition == null || condition.apply(object)) {
                if (trueDelegate != null) return trueDelegate.apply(object);
            } else {
                if (falseDelegate != null) return falseDelegate.apply(object);
            }
            return Collections.emptySet();
        }

        public String toString() {
            return "Branch[condition=" + condition + ", true:" + trueDelegate + ", false:" + falseDelegate + "]";
        }
    }

    /**
     *  Step function that delegates to another function but
     *  wraps its result in a singleton collection to meet the
     *  StepFunction contract.
     */
    private static class TransformDecorator<S, T> implements StepFunction<S, T>, Serializable {

        static final long serialVersionUID = 1L;

        private Function<S, T> transform;

        public TransformDecorator(Function<S, T> transform) {
            if (transform == null) throw new IllegalArgumentException("transform cannot be null.");
            this.transform = transform;
        }

        public Iterable<T> apply(S object) {
            T result = transform.apply(object);
            return Collections.singleton(result);
        }

        public String toString() {
            return "Transform[" + transform + "]";
        }
    }

    private static class FilterDecorator<T> implements StepFunction<T, T>, Serializable {

        static final long serialVersionUID = 1L;

        private Predicate<T> filter;

        public FilterDecorator(Predicate<T> filter) {
            if (filter == null) throw new IllegalArgumentException("filter cannot be null.");
            this.filter = filter;
        }

        public Iterable<T> apply(T object) {
            if (!filter.apply(object)) return Collections.emptySet();
            return Collections.singleton(object);
        }

        public String toString() {
            return "Filter[" + filter + "]";
        }
    }

    private static class FilterFunction<S, T> implements StepFunction<S, T>, Serializable {

        static final long serialVersionUID = 1L;

        private Function<S, ? extends Iterable<T>> delegate;

        private Predicate<? super T> filter;

        public FilterFunction(Function<S, ? extends Iterable<T>> delegate, Predicate<? super T> filter) {
            if (delegate == null) throw new IllegalArgumentException("delegate cannot be null.");
            if (filter == null) throw new IllegalArgumentException("filter cannot be null.");
            this.delegate = delegate;
            this.filter = filter;
        }

        public Iterable<T> apply(S object) {
            Iterable<T> results = delegate.apply(object);
            return Iterables.filter(results, filter);
        }

        public String toString() {
            return "Filter[" + delegate + " : " + filter + "]";
        }
    }

    private static class TransformFunction<A, B, C> implements StepFunction<A, C>, Serializable {

        static final long serialVersionUID = 1L;

        private Function<A, ? extends Iterable<B>> delegate;

        private Function<? super B, C> transform;

        public TransformFunction(Function<A, ? extends Iterable<B>> delegate, Function<? super B, C> transform) {
            if (delegate == null) throw new IllegalArgumentException("delegate cannot be null.");
            if (transform == null) throw new IllegalArgumentException("transform cannot be null.");
            this.delegate = delegate;
            this.transform = transform;
        }

        public Iterable<C> apply(A object) {
            Iterable<B> results = delegate.apply(object);
            return Iterables.transform(results, transform);
        }

        public String toString() {
            return "Transform[" + delegate + " : " + transform + "]";
        }
    }

    private static class ConcatFunction<S, T> implements StepFunction<S, T>, Serializable {

        static final long serialVersionUID = 1L;

        private Function<S, ? extends Iterable<T>> f1;

        private Function<S, ? extends Iterable<T>> f2;

        public ConcatFunction(Function<S, ? extends Iterable<T>> f1, Function<S, ? extends Iterable<T>> f2) {
            if (f1 == null || f2 == null) throw new IllegalArgumentException("Supplied functions cannot be null.");
            this.f1 = f1;
            this.f2 = f2;
        }

        public Iterable<T> apply(S object) {
            Iterable<T> r1 = f1.apply(object);
            Iterable<T> r2 = f2.apply(object);
            return Iterables.concat(r1, r2);
        }

        public String toString() {
            return "Concat[" + f1 + " + " + f2 + "]";
        }
    }
}
