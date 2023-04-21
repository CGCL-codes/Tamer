package jaxlib.closure;

import java.util.Collection;

/**
 * A collection which creates conditions and procedures like <tt>Do</tt> and <tt>If</tt> do for itself.
 * The purpose of this interface is to enable collections to implement those methods without the need to 
 * inherit from <tt>jaxlib.col.XCollection</tt>. <tt>If</tt> and <tt>Do</tt> are detecting this interface and are 
 * redirecting to it.
 * <p>
 * You normally will have no need for this interface, except you are implementing your own collections not 
 * inherited from <tt>XCollection</tt>.
 * </p>
 *
 * @see jaxlib.closure.Closures
 * @see jaxlib.closure.If
 * @see jaxlib.col.XCollection
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: ClosureSupportCollection.java,v 1.2 2004/09/14 19:59:38 joerg_wassmer Exp $
 */
public interface ClosureSupportCollection<E> extends Collection<E> {

    /**
   * Counts up to specified count of elements in this collection, which are matching specified condition.
   * If <tt>stopOnDismatch</tt> is <tt>true</tt>, and this method finds a dismatching element, then <tt>-(count+1)</tt> is returned,
   * where <tt>count</tt> is the count of matches found. Otherwise this method simply returns the number of matches found.
   *
   * @return Count of elements matched specified condition; 
   *         <tt>-(count+1)</tt> if <tt>stopOnDismatch == true</tt> and a mismatching element was found.
   *
   * @param condition       the conditon to test elements against.
   * @param iF              <tt>true</tt> for counting elements matching specified condition;
   *                        <tt>false</tt> for counting dismatches.
   * @param maxCount        <tt>-1</tt> for counting all matches; <tt>>=0</tt> for limiting the count of matches.
   * @param stopOnDismatch  <tt>true</tt> for stop search immediately if a dismatching element has been found.
   *
   * @throws IllegalArgumentException if <tt>maxCount < -1</tt>.
   * @throws RuntimeException         if this collection contains an element the condition can't process.
   * @throws NullPointerException     if <tt>condition == null</tt>.
   *
   * @see jaxlib.closure.ForEach#countUp(Iterable,Filter,boolean,int,boolean)
   *
   * @since JaXLib 1.0
   */
    public int countMatches(Filter<? super E> condition, boolean iF, int maxCount, boolean stopOnDismatch);

    public E findMatch(Filter<? super E> condition, boolean iF);

    /**
   * Processes all elements of this collection through specified procedure, stopping if the procedure returned <tt>false</tt>, or if all
   * elements of this collection have been processed.
   *
   * @return the number of elements of this collection the specified procedure returned <tt>true</tt> for.
   *
   * @param procedure the closure which receives the elements of this collection.
   *
   * @throws RuntimeException     if this collection contains an element the procedure can't process.
   * @throws NullPointerException if <tt>procedure == null</tt>.
   *
   * @see jaxlib.closure.ForEach#proceed(Iterable,Closure)
   *
   * @since JaXLib 1.0
   */
    public int forEach(Closure<? super E> procedure);

    /**
   * Removes up to specified count of elements from this collection, which are matching specified condition (optional operation).
   * If <tt>stopOnDismatch</tt> is <tt>true</tt>, and this method finds a dismatching element, then <tt>-(count+1)</tt> is returned,
   * where <tt>count</tt> is the count of matches removed. Otherwise this method simply returns the number of matches removed.
   *
   * @return Count of elements matched specified condition; 
   *         <tt>-(count+1)</tt> if <tt>stopOnDismatch == true</tt> and a mismatching element was found.
   *
   * @param condition       the conditon to test elements against.
   * @param iF              <tt>true</tt> for removing elements matching specified condition;
   *                        <tt>false</tt> for removing dismatches.
   * @param maxCount        <tt>-1</tt> for removing all matches; <tt>>=0</tt> for limiting the count of matches.
   * @param stopOnDismatch  <tt>true</tt> for stop search immediately if a dismatching element has been found.
   *
   * @throws UnsupportedOperationException  if this collection does not support the <tt>remove</tt> operation.
   * @throws IllegalArgumentException       if <tt>maxCount < -1</tt>.
   * @throws RuntimeException               if this collection contains an element the condition can't process.
   * @throws NullPointerException           if <tt>condition == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public int removeMatches(Filter<? super E> condition, boolean iF, int maxCount, boolean stopOnDismatch);

    /**
   * Returns a procedure which returns <tt>true</tt> always and redirects to the <tt>add(Object element)</tt> method of this 
   * collection (optional operation).
   * <p><b>
   * This method should never call <tt>jaxlib.closure.Closures#addTo(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>add</tt> operation.
   *
   * @see #add(Object)
   * @see jaxlib.closure.Closures#addTo(Collection)
   *
   * @since JaXLib 1.0
   */
    public Closure<? extends E> doAdd();

    public Closure<? extends E> doAddIfAbsent();

    /**
   * Returns a procedure which returns <tt>true</tt> always and redirects to the <tt>remove(Object element)</tt> method of this 
   * collection (optional operation).
   * <p><b>
   * This method should never call <tt>jaxlib.closure.Closures#removeFrom(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>remove</tt> operation.
   *
   * @see #remove(Object)
   * @see jaxlib.closure.Closures#removeFrom(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Closure<T> doRemove();

    /**
   * Returns a procedure which returns <tt>true</tt> always and removes each element from this collection the argument given to the 
   * procedure is equal to.
   * <p><b>
   * This method should never call <tt>jaxlib.closure.Closures#removeEachFrom(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>remove</tt> operation.
   *
   * @see jaxlib.col.XCollection#removeEach(Object)
   * @see jaxlib.closure.Closures#removeEachFrom(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Closure<T> doRemoveEach();

    /**
   * Returns a procedure which returns <tt>true</tt> always and removes each element from this collection the argument given to the 
   * procedure is identical to.
   * <p><b>
   * This method should never call <tt>jaxlib.closure.Closures#removeEachIdenticalFrom(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>remove</tt> operation.
   *
   * @see jaxlib.col.XCollection#removeEachIdentical(Object)
   * @see jaxlib.closure.Closures#removeEachIdenticalFrom(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Closure<T> doRemoveEachIdentical();

    /**
   * Returns a procedure which returns <tt>true</tt> always, removing the first element of this collection which is identical to the
   * argument of the procedure (optional operation).
   * <p><b>
   * This method should never call <tt>jaxlib.closure.Closures#removeIdenticalFrom(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>remove</tt> operation.
   *
   * @see jaxlib.col.XCollection#removeIdentical(Object)
   * @see jaxlib.closure.Closures#removeIdenticalFrom(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Closure<T> doRemoveIdentical();

    /**
   * Returns a condition which simply returns the result of <tt>add(Object element)</tt> (optional operation).
   * <p><b>
   * This method should never call <tt>jaxlib.closure.If#addedTo(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>add</tt> operation.
   *
   * @see #add(Object element)
   * @see jaxlib.closure.If#addedTo(Collection)
   *
   * @since JaXLib 1.0
   */
    public Condition<? extends E> ifAdded();

    public Condition<? extends E> ifAddedIfAbsent();

    /**
   * Returns a condition which simply returns the result of <tt>contains(Object element)</tt>.
   * <p><b>
   * This method should never call <tt>jaxlib.closure.If#in(Collection)</tt>!
   * </b></p>
   *
   * @see #contains(Object)
   * @see jaxlib.closure.If#in(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Condition<T> ifContains();

    /**
   * Returns a condition which returns <tt>true</tt> if this collection contains an element which is identical to the argument
   * given to the condition.
   * <p><b>
   * This method should never call <tt>jaxlib.closure.If#identicalIn(Collection)</tt>!
   * </b></p>
   *
   * @see jaxlib.col.XCollection#containsIdentical(Object)
   * @see jaxlib.closure.If#identicalIn(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Condition<T> ifContainsIdentical();

    /**
   * Returns a condition which simply returns the result of <tt>remove(Object element)</tt> (optional operation).
   * <p><b>
   * This method should never call <tt>jaxlib.closure.If#removedFrom(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>remove</tt> operation.
   *
   * @see #remove(Object)
   * @see jaxlib.closure.If#removedFrom(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Condition<T> ifRemoved();

    /**
   * Returns a condition which removes one element which is identical to the condition's parameter, returning <tt>true</tt> if such an element
   * was found (optional operation).
   * <p><b>
   * This method should never call <tt>jaxlib.closure.If#removedIdenticalFrom(Collection)</tt>!
   * </b></p>
   *
   * @throws UnsupportedOperationException if this collection does not support the <tt>remove</tt> operation.
   *
   * @see jaxlib.col.XCollection#removeIdentical(Object)
   * @see jaxlib.closure.If#removedIdenticalFrom(Collection)
   *
   * @since JaXLib 1.0
   */
    public <T> Condition<T> ifRemovedIdentical();

    /**
   * Returns a function which adds an object to this collection if it isn't already contained in this collection.
   * If the object already is contained, then the already existing element is returned by the funtion, otherwise the argument object.
   *
   * @since JaXLib 1.0
   */
    public Function<? extends E, ? extends E> toInternalized();
}
