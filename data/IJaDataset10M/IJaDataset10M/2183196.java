package flj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * A wrapper for functions of arity 1, that decorates them with higher-order functions.
 */
public final class FW<A, B> implements F<A, B> {

    private final F<A, B> f;

    private FW(final F<A, B> f) {
        this.f = f;
    }

    /**
   * Wraps the given function, decorating it with higher-order functions.
   *
   * @param f A function to wrap.
   * @return The given function, wrapped.
   */
    public static <A, B> FW<A, B> $(final F<A, B> f) {
        return new FW<A, B>(f);
    }

    /**
   * First-class constructor for FW.
   *
   * @return A function that wraps a given function.
   */
    public static <A, B> FW<F<A, B>, FW<A, B>> $() {
        return $(new F<F<A, B>, FW<A, B>>() {

            public FW<A, B> f(final F<A, B> f) {
                return $(f);
            }
        });
    }

    /**
   * Function application
   *
   * @param a The <code>A</code> to transform.
   * @return The result of the transformation.
   */
    public B f(final A a) {
        return f.f(a);
    }

    /**
   * Function composition
   *
   * @param g A function to compose with this one.
   * @return The composed function such that this function is applied last.
   */
    public <C> FW<C, B> o(final F<C, A> g) {
        return $(Function.compose(f, g));
    }

    /**
   * First-class function composition
   *
   * @return A function that composes this function with another.
   */
    public <C> FW<F<C, A>, F<C, B>> o_() {
        return $(new F<F<C, A>, F<C, B>>() {

            public F<C, B> f(final F<C, A> g) {
                return $(o(g));
            }
        });
    }

    /**
   * Function composition flipped.
   *
   * @param g A function with which to compose this one.
   * @return The composed function such that this function is applied first.
   */
    public <C> FW<A, C> andThen(final F<B, C> g) {
        return $(Function.compose(g, this));
    }

    /**
   * First-class composition flipped.
   *
   * @return A function that invokes this function and then a given function on the result.
   */
    public <C> FW<F<B, C>, F<A, C>> andThen_() {
        return $(new F<F<B, C>, F<A, C>>() {

            public F<A, C> f(final F<B, C> g) {
                return andThen(g);
            }
        });
    }

    /**
   * Simultaneously covaries and contravaries a function.
   *
   * @return A co- and contravariant function that invokes this function on its argument.
   */
    public FW<? super A, ? extends B> vary() {
        return $(Function.vary(this));
    }

    /**
   * Binds a given function across this function (Reader Monad).
   *
   * @param g A function that takes the return value of this function as an argument, yielding a new function.
   * @return A function that invokes this function on its argument and then the given function on the result.
   */
    public <C> FW<A, C> bind(final F<B, F<A, C>> g) {
        return $(Function.bind(this, g));
    }

    /**
   * First-class function binding.
   *
   * @return A function that binds another function across this function.
   */
    public <C> FW<F<B, F<A, C>>, F<A, C>> bind() {
        return $(new F<F<B, F<A, C>>, F<A, C>>() {

            public F<A, C> f(final F<B, F<A, C>> bff) {
                return bind(bff);
            }
        });
    }

    /**
   * Function application in an environment (Applicative Functor).
   *
   * @param g A function with the same argument type as this function, yielding a function that takes the return
   *          value of this function.
   * @return A new function that invokes the given function on its argument, yielding a new function that is then
   *         applied to the result of applying this function to the argument.
   */
    public <C> FW<A, C> apply(final F<A, F<B, C>> g) {
        return $(Function.apply(g, this));
    }

    /**
   * First-class function application in an environment.
   *
   * @return A function that applies a given function within the environment of this function.
   */
    public <C> FW<F<A, F<B, C>>, F<A, C>> apply() {
        return $(new F<F<A, F<B, C>>, F<A, C>>() {

            public F<A, C> f(final F<A, F<B, C>> aff) {
                return apply(aff);
            }
        });
    }

    /**
   * Applies this function over the arguments of another function.
   *
   * @param g The function over whose arguments to apply this function.
   * @return A new function that invokes this function on its arguments before invoking the given function.
   */
    public <C> FW<A, F<A, C>> on(final F<B, F<B, C>> g) {
        return $(Function.on(g, this));
    }

    /**
   * First-class composition with a function of arity-2
   *
   * @return A function that applies this function over the arguments of a given function.
   */
    public <C> FW<F<B, F<B, C>>, F<A, F<A, C>>> on() {
        return $(new F<F<B, F<B, C>>, F<A, F<A, C>>>() {

            public F<A, F<A, C>> f(final F<B, F<B, C>> bff) {
                return on(bff);
            }
        });
    }

    /**
   * Promotes this function so that it returns its result in a product-1. Kleisli arrow for P1.
   *
   * @return This function promoted to return its result in a product-1.
   */
    public FW<A, P1<B>> lazy() {
        return $(P1.curry(f));
    }

    /**
   * Promotes this function to map over a product-1.
   *
   * @return This function promoted to map over a product-1.
   */
    public FW<P1<A>, P1<B>> mapP1() {
        return $(P1.<A, B>fmap(f));
    }

    /**
   * Promotes this function so that it returns its result in an Option. Kleisli arrow for Option.
   *
   * @return This function promoted to return its result in an Option.
   */
    public FW<A, Option<B>> option() {
        return $(Option.<B>some_()).o(f);
    }

    /**
   * Promotes this function to map over an optional value.
   *
   * @return This function promoted to map over an optional value.
   */
    public FW<Option<A>, Option<B>> mapOption() {
        return $(Option.<A, B>map().f(f));
    }

    /**
   * Promotes this function so that it returns its result in a List. Kleisli arrow for List.
   *
   * @return This function promoted to return its result in a List.
   */
    public FW<A, List<B>> list() {
        return F2W.$$(List.<B>cons()).flip().f(List.<B>nil()).o(f);
    }

    /**
   * Promotes this function to map over a List.
   *
   * @return This function promoted to map over a List.
   */
    public FW<List<A>, List<B>> mapList() {
        return $(List.<A, B>map_().f(f));
    }

    /**
   * Promotes this function so that it returns its result in a Stream. Kleisli arrow for Stream.
   *
   * @return This function promoted to return its result in a Stream.
   */
    public FW<A, Stream<B>> stream() {
        return F2W.$$(Stream.<B>cons()).flip().f(P.p(Stream.<B>nil())).o(f);
    }

    /**
   * Promotes this function to map over a Stream.
   *
   * @return This function promoted to map over a Stream.
   */
    public FW<Stream<A>, Stream<B>> mapStream() {
        return $(Stream.<A, B>map_().f(f));
    }

    /**
   * Promotes this function so that it returns its result in an Array. Kleisli arrow for Array.
   *
   * @return This function promoted to return its result in an Array.
   */
    public FW<A, Array<B>> array() {
        return $(new F<A, Array<B>>() {

            public Array<B> f(final A a) {
                return Array.single(f.f(a));
            }
        });
    }

    /**
   * Promotes this function to map over a Stream.
   *
   * @return This function promoted to map over a Stream.
   */
    public FW<Array<A>, Array<B>> mapArray() {
        return $(Array.<A, B>map().f(f));
    }

    /**
   * Promotes this function so that it returns its result on the left side of an Either.
   * Kleisli arrow for the Either left projection.
   *
   * @return This function promoted to return its result on the left side of an Either.
   */
    public <C> FW<A, Either<B, C>> eitherLeft() {
        return $(Either.<B, C>left_()).o(f);
    }

    /**
   * Promotes this function so that it returns its result on the right side of an Either.
   * Kleisli arrow for the Either right projection.
   *
   * @return This function promoted to return its result on the right side of an Either.
   */
    public <C> FW<A, Either<C, B>> eitherRight() {
        return $(Either.<C, B>right_()).o(f);
    }

    /**
   * Promotes this function to map over the left side of an Either.
   *
   * @return This function promoted to map over the left side of an Either.
   */
    public <X> FW<Either<A, X>, Either<B, X>> mapLeft() {
        return $(Either.<A, X, B>leftMap_().f(f));
    }

    /**
   * Promotes this function to map over the right side of an Either.
   *
   * @return This function promoted to map over the right side of an Either.
   */
    public <X> FW<Either<X, A>, Either<X, B>> mapRight() {
        return $(Either.<X, A, B>rightMap_().f(f));
    }

    /**
   * Returns a function that returns the left side of a given Either, or this function applied to the right side.
   *
   * @return a function that returns the left side of a given Either, or this function applied to the right side.
   */
    public FW<Either<B, A>, B> onLeft() {
        return $(new F<Either<B, A>, B>() {

            public B f(final Either<B, A> either) {
                return either.left().on(f);
            }
        });
    }

    /**
   * Returns a function that returns the right side of a given Either, or this function applied to the left side.
   *
   * @return a function that returns the right side of a given Either, or this function applied to the left side.
   */
    public FW<Either<A, B>, B> onRight() {
        return $(new F<Either<A, B>, B>() {

            public B f(final Either<A, B> either) {
                return either.right().on(f);
            }
        });
    }

    /**
   * Promotes this function to return its value in an Iterable.
   *
   * @return This function promoted to return its value in an Iterable.
   */
    public FW<A, IterableW<B>> iterable() {
        return $(IterableW.<A, B>arrow().f(f));
    }

    /**
   * Promotes this function to map over Iterables.
   *
   * @return This function promoted to map over Iterables.
   */
    public FW<Iterable<A>, IterableW<B>> mapIterable() {
        return $(IterableW.<A, B>map().f(f)).o(IterableW.<A, Iterable<A>>wrap());
    }

    /**
   * Promotes this function to return its value in a NonEmptyList.
   *
   * @return This function promoted to return its value in a NonEmptyList.
   */
    public FW<A, NonEmptyList<B>> nel() {
        return $(NonEmptyList.<B>nel()).o(f);
    }

    /**
   * Promotes this function to map over a NonEmptyList.
   *
   * @return This function promoted to map over a NonEmptyList.
   */
    public FW<NonEmptyList<A>, NonEmptyList<B>> mapNel() {
        return $(new F<NonEmptyList<A>, NonEmptyList<B>>() {

            public NonEmptyList<B> f(final NonEmptyList<A> list) {
                return list.map(f);
            }
        });
    }

    /**
   * Promotes this function to return its value in a Set.
   *
   * @param o An order for the set.
   * @return This function promoted to return its value in a Set.
   */
    public FW<A, Set<B>> set(final Ord<B> o) {
        return $(new F<A, Set<B>>() {

            public Set<B> f(final A a) {
                return Set.single(o, f.f(a));
            }
        });
    }

    /**
   * Promotes this function to map over a Set.
   *
   * @param o An order for the resulting set.
   * @return This function promoted to map over a Set.
   */
    public FW<Set<A>, Set<B>> mapSet(final Ord<B> o) {
        return $(new F<Set<A>, Set<B>>() {

            public Set<B> f(final Set<A> set) {
                return set.map(o, f);
            }
        });
    }

    /**
   * Promotes this function to return its value in a Tree.
   *
   * @return This function promoted to return its value in a Tree.
   */
    public FW<A, Tree<B>> tree() {
        return $(new F<A, Tree<B>>() {

            public Tree<B> f(final A a) {
                return Tree.leaf(f.f(a));
            }
        });
    }

    /**
   * Promotes this function to map over a Tree.
   *
   * @return This function promoted to map over a Tree.
   */
    public FW<Tree<A>, Tree<B>> mapTree() {
        return $(Tree.<A, B>fmap_().f(f));
    }

    /**
   * Returns a function that maps this function over a tree and folds it with the given monoid.
   *
   * @param m The monoid with which to fold the mapped tree.
   * @return a function that maps this function over a tree and folds it with the given monoid.
   */
    public FW<Tree<A>, B> foldMapTree(final Monoid<B> m) {
        return $(Tree.foldMap_(f, m));
    }

    /**
   * Promotes this function to return its value in a TreeZipper.
   *
   * @return This function promoted to return its value in a TreeZipper.
   */
    public FW<A, TreeZipper<B>> treeZipper() {
        return tree().andThen(TreeZipper.<B>fromTree());
    }

    /**
   * Promotes this function to map over a TreeZipper.
   *
   * @return This function promoted to map over a TreeZipper.
   */
    public FW<TreeZipper<A>, TreeZipper<B>> mapTreeZipper() {
        return $(new F<TreeZipper<A>, TreeZipper<B>>() {

            public TreeZipper<B> f(final TreeZipper<A> zipper) {
                return zipper.map(f);
            }
        });
    }

    /**
   * Promotes this function so that it returns its result on the failure side of a Validation.
   * Kleisli arrow for the Validation failure projection.
   *
   * @return This function promoted to return its result on the failure side of a Validation.
   */
    public <C> FW<A, Validation<B, C>> fail() {
        return $(new F<A, Validation<B, C>>() {

            public Validation<B, C> f(final A a) {
                return Validation.fail(f.f(a));
            }
        });
    }

    /**
   * Promotes this function so that it returns its result on the success side of an Validation.
   * Kleisli arrow for the Validation success projection.
   *
   * @return This function promoted to return its result on the success side of an Validation.
   */
    public <C> FW<A, Validation<C, B>> success() {
        return $(new F<A, Validation<C, B>>() {

            public Validation<C, B> f(final A a) {
                return Validation.success(f.f(a));
            }
        });
    }

    /**
   * Promotes this function to map over the failure side of a Validation.
   *
   * @return This function promoted to map over the failure side of a Validation.
   */
    public <X> FW<Validation<A, X>, Validation<B, X>> mapFail() {
        return $(new F<Validation<A, X>, Validation<B, X>>() {

            public Validation<B, X> f(final Validation<A, X> validation) {
                return validation.f().map(f);
            }
        });
    }

    /**
   * Promotes this function to map over the success side of a Validation.
   *
   * @return This function promoted to map over the success side of a Validation.
   */
    public <X> FW<Validation<X, A>, Validation<X, B>> mapSuccess() {
        return $(new F<Validation<X, A>, Validation<X, B>>() {

            public Validation<X, B> f(final Validation<X, A> validation) {
                return validation.map(f);
            }
        });
    }

    /**
   * Returns a function that returns the failure side of a given Validation,
   * or this function applied to the success side.
   *
   * @return a function that returns the failure side of a given Validation,
   *         or this function applied to the success side.
   */
    public FW<Validation<B, A>, B> onFail() {
        return $(new F<Validation<B, A>, B>() {

            public B f(final Validation<B, A> v) {
                return v.f().on(f);
            }
        });
    }

    /**
   * Returns a function that returns the success side of a given Validation,
   * or this function applied to the failure side.
   *
   * @return a function that returns the success side of a given Validation,
   *         or this function applied to the failure side.
   */
    public FW<Validation<A, B>, B> onSuccess() {
        return $(new F<Validation<A, B>, B>() {

            public B f(final Validation<A, B> v) {
                return v.on(f);
            }
        });
    }

    /**
   * Promotes this function to return its value in a Zipper.
   *
   * @return This function promoted to return its value in a Zipper.
   */
    public FW<A, Zipper<B>> zipper() {
        return stream().andThen(new F<Stream<B>, Zipper<B>>() {

            public Zipper<B> f(final Stream<B> stream) {
                return (Zipper<B>) Zipper.fromStream(stream).some();
            }
        });
    }

    /**
   * Promotes this function to map over a Zipper.
   *
   * @return This function promoted to map over a Zipper.
   */
    public FW<Zipper<A>, Zipper<B>> mapZipper() {
        return $(new F<Zipper<A>, Zipper<B>>() {

            public Zipper<B> f(final Zipper<A> zipper) {
                return zipper.map(f);
            }
        });
    }

    /**
   * Promotes this function to map over an Equal as a contravariant functor.
   *
   * @return This function promoted to map over an Equal as a contravariant functor.
   */
    public FW<Equal<B>, Equal<A>> comapEqual() {
        return $(new F<Equal<B>, Equal<A>>() {

            public Equal<A> f(final Equal<B> equal) {
                return equal.comap(f);
            }
        });
    }

    /**
   * Promotes this function to map over a Hash as a contravariant functor.
   *
   * @return This function promoted to map over a Hash as a contravariant functor.
   */
    public FW<Hash<B>, Hash<A>> comapHash() {
        return $(new F<Hash<B>, Hash<A>>() {

            public Hash<A> f(final Hash<B> hash) {
                return hash.comap(f);
            }
        });
    }

    /**
   * Promotes this function to map over a Show as a contravariant functor.
   *
   * @return This function promoted to map over a Show as a contravariant functor.
   */
    public FW<Show<B>, Show<A>> comapShow() {
        return $(new F<Show<B>, Show<A>>() {

            public Show<A> f(final Show<B> s) {
                return s.comap(f);
            }
        });
    }

    /**
   * Promotes this function to map over the first element of a pair.
   *
   * @return This function promoted to map over the first element of a pair.
   */
    public <C> FW<P2<A, C>, P2<B, C>> mapFst() {
        return $(P2.<A, C, B>map1_(f));
    }

    /**
   * Promotes this function to map over the second element of a pair.
   *
   * @return This function promoted to map over the second element of a pair.
   */
    public <C> FW<P2<C, A>, P2<C, B>> mapSnd() {
        return $(P2.<C, A, B>map2_(f));
    }

    /**
   * Promotes this function to map over both elements of a pair.
   *
   * @return This function promoted to map over both elements of a pair.
   */
    public FW<P2<A, A>, P2<B, B>> mapBoth() {
        return $(new F<P2<A, A>, P2<B, B>>() {

            public P2<B, B> f(final P2<A, A> aap2) {
                return P2.map(FW.this, aap2);
            }
        });
    }

    /**
   * Maps this function over a SynchronousQueue.
   *
   * @param as A SynchronousQueue to map this function over.
   * @return A new SynchronousQueue with this function applied to each element.
   */
    public SynchronousQueue<B> map(final SynchronousQueue<A> as) {
        final SynchronousQueue<B> bs = new SynchronousQueue<B>();
        bs.addAll(Stream.iterableStream(as).map(this).toCollection());
        return bs;
    }

    /**
   * Maps this function over a PriorityBlockingQueue.
   *
   * @param as A PriorityBlockingQueue to map this function over.
   * @return A new PriorityBlockingQueue with this function applied to each element.
   */
    public PriorityBlockingQueue<B> map(final PriorityBlockingQueue<A> as) {
        return new PriorityBlockingQueue<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over a LinkedBlockingQueue.
   *
   * @param as A LinkedBlockingQueue to map this function over.
   * @return A new LinkedBlockingQueue with this function applied to each element.
   */
    public LinkedBlockingQueue<B> map(final LinkedBlockingQueue<A> as) {
        return new LinkedBlockingQueue<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over a CopyOnWriteArraySet.
   *
   * @param as A CopyOnWriteArraySet to map this function over.
   * @return A new CopyOnWriteArraySet with this function applied to each element.
   */
    public CopyOnWriteArraySet<B> map(final CopyOnWriteArraySet<A> as) {
        return new CopyOnWriteArraySet<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over a CopyOnWriteArrayList.
   *
   * @param as A CopyOnWriteArrayList to map this function over.
   * @return A new CopyOnWriteArrayList with this function applied to each element.
   */
    public CopyOnWriteArrayList<B> map(final CopyOnWriteArrayList<A> as) {
        return new CopyOnWriteArrayList<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over a ConcurrentLinkedQueue.
   *
   * @param as A ConcurrentLinkedQueue to map this function over.
   * @return A new ConcurrentLinkedQueue with this function applied to each element.
   */
    public ConcurrentLinkedQueue<B> map(final ConcurrentLinkedQueue<A> as) {
        return new ConcurrentLinkedQueue<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over an ArrayBlockingQueue.
   *
   * @param as An ArrayBlockingQueue to map this function over.
   * @return A new ArrayBlockingQueue with this function applied to each element.
   */
    public ArrayBlockingQueue<B> map(final ArrayBlockingQueue<A> as) {
        final ArrayBlockingQueue<B> bs = new ArrayBlockingQueue<B>(as.size());
        bs.addAll(Stream.iterableStream(as).map(this).toCollection());
        return bs;
    }

    /**
   * Maps this function over a TreeSet.
   *
   * @param as A TreeSet to map this function over.
   * @return A new TreeSet with this function applied to each element.
   */
    public TreeSet<B> map(final TreeSet<A> as) {
        return new TreeSet<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over a PriorityQueue.
   *
   * @param as A PriorityQueue to map this function over.
   * @return A new PriorityQueue with this function applied to each element.
   */
    public PriorityQueue<B> map(final PriorityQueue<A> as) {
        return new PriorityQueue<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over a LinkedList.
   *
   * @param as A LinkedList to map this function over.
   * @return A new LinkedList with this function applied to each element.
   */
    public LinkedList<B> map(final LinkedList<A> as) {
        return new LinkedList<B>(Stream.iterableStream(as).map(this).toCollection());
    }

    /**
   * Maps this function over an ArrayList.
   *
   * @param as An ArrayList to map this function over.
   * @return A new ArrayList with this function applied to each element.
   */
    public ArrayList<B> map(final ArrayList<A> as) {
        return new ArrayList<B>(Stream.iterableStream(as).map(this).toCollection());
    }
}
