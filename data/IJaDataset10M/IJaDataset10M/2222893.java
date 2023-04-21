/*
 * DetachedDomainQuery.java
 *
 * Created on August 7, 2007, 3:35 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.persistence;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jaxlib.col.ArrayXList;


/**
 * A detached query bound to a specific domain model.
 * In difference to the superclass a {@code DetachedDomainQuery} knows how to get an entity manager to execute the
 * query.
 *
 * @param <R>
 *  the type of query result rows.
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: DetachedDomainQuery.java 3033 2012-01-02 09:28:59Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public abstract class DetachedDomainQuery<R> extends DetachedQuery<R> implements Iterable<R>, Callable<List<R>>
{

  /**
   * @since JaXLib 1.0
   */
  private static final long serialVersionUID = 1L;


  protected DetachedDomainQuery(final String statement)
  {
    super(statement);
  }


  protected DetachedDomainQuery(final String statement, final Object... parameters)
  {
    super(statement, parameters);
  }


  protected DetachedDomainQuery(final QueryCustomizer defaultHint, final String statement)
  {
    super(defaultHint, statement);
  }


  protected DetachedDomainQuery(
    final QueryCustomizer     defaultHint,
    final String              statement,
    final Object...           parameters
  )
  {
    super(defaultHint, statement, parameters);
  }


  protected DetachedDomainQuery(
    final QueryCustomizer[]     defaultHints,
    final String                statement,
    final Object...             parameters
  )
  {
    super(defaultHints, statement, parameters);
  }


  protected DetachedDomainQuery(
    final int       maxResults,
    final String    statement,
    final Object... parameters
  )
  {
    super(maxResults, statement, parameters);
  }


  protected DetachedDomainQuery(
    final QueryCustomizer     defaultHint,
    final int                 maxResults,
    final String              statement,
    final Object...           parameters
  )
  {
    super(defaultHint, maxResults, statement, parameters);
  }


  protected DetachedDomainQuery(
    final QueryCustomizer[]     defaultHints,
    final int                   maxResults,
    final String                statement,
    final Object...             parameters
  )
  {
    super(defaultHints, maxResults, statement, parameters);
  }



  protected DetachedDomainQuery(
    final String[]    parameterNames,
    final int         maxResults,
    final String      statement,
    final Object[]    parameters
  )
  {
    super(parameterNames, maxResults, statement, parameters);
  }


  protected DetachedDomainQuery(
    final QueryCustomizer[] defaultHints,
    final int               maxResults,
    final String[]          parameterNames,
    final String            statement,
    final Object[]          parameters
  )
  {
    super(defaultHints, maxResults, parameterNames, statement, parameters);
  }


  public DetachedDomainQuery(
    final QuerySet  querySet,
    final String    queryName,
    final Object... parameters
  )
  {
    super(querySet, queryName, parameters);
  }



  public DetachedDomainQuery(
    final int       maxResults,
    final QuerySet  querySet,
    final String    queryName,
    final Object... parameters
  )
  {
    super(maxResults, querySet, queryName, parameters);
  }



  /**
   * Copy constructor, usually used only by subclasses.
   *
   * @since JaXLib 1.0
   */
  public DetachedDomainQuery(final DetachedQuery<? extends R> src)
  {
    super(src);
  }



  protected abstract EntityManager getEntityManager();



  public final EntityQuery<R> attach()
  {
    return attach(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final EntityQuery<R> attach(final QueryCustomizer hint)
  {
    return attach(getEntityManager(), hint);
  }


  public final EntityQuery<R> attach(final QueryCustomizer... hints)
  {
    return attach(getEntityManager(), hints);
  }



  /**
   * Execute this query or update.
   * For updates the result is a singleton list containing the update count as returned by {@link #update()}.
   * For queries the result is the same list as returned by {@link #select()}.
   *
   * @return
   *  the result of the execution of this statement.
   */
  @Override
  public List<R> call()
  {
    switch (getKind())
    {
      case DELETE:
      case INSERT:
      case UPDATE:
      {
        final int result = update();
        final List<Integer> list = new ArrayXList<>(1);
        list.add(result);
        return (List<R>) list;
      }

      case SELECT:
      return select();

      default:
      throw new IllegalStateException("don't know how to execute statement of kind " + getKind());
    }
  }



  public final Query createQuery()
  {
    return createQuery(getEntityManager(), 0, -1, (QueryCustomizer[]) null);
  }


  public final Query createQuery(final QueryCustomizer hint)
  {
    return createQuery(getEntityManager(), 0, -1 , new QueryCustomizer[] {hint});
  }


  public final Query createQuery(final int maxResults, final QueryCustomizer hint)
  {
    return createQuery(getEntityManager(), 0, maxResults, new QueryCustomizer[] {hint});
  }


  public final Query createQuery(
    final int             maxResults,
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return createQuery(getEntityManager(), 0, maxResults, new QueryCustomizer[] {hint0, hint1});
  }


  public final Query createQuery(
    final int                 firstResult,
          int                 maxResults,
    final QueryCustomizer...  hints
  )
  {
    return createQuery(getEntityManager(), firstResult, maxResults, hints);
  }


  @Override
  public Iterator<R> iterator()
  {
    return select(getEntityManager()).iterator();
  }


  public final List<R> select()
  {
    return select(getEntityManager(), 0, -1, (QueryCustomizer[]) null);
  }


  public final List<R> select(final QueryCustomizer hint)
  {
    return select(getEntityManager(), 0, -1, new QueryCustomizer[] {hint});
  }


  public final List<R> select(
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return select(getEntityManager(), 0, -1, hint0, hint1);
  }


  public final List<R> select(final int maxResults)
  {
    return select(getEntityManager(), 0, maxResults, (QueryCustomizer[]) null);
  }


  public final List<R> select(final int firstIndex, final int maxResults)
  {
    return select(getEntityManager(), firstIndex, maxResults, (QueryCustomizer[]) null);
  }


  public final List<R> select(final int maxResults, final QueryCustomizer hint)
  {
    return select(getEntityManager(), 0, maxResults, new QueryCustomizer[] {hint});
  }


  public final List<R> select(
    final int             maxResults,
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return select(getEntityManager(), 0, maxResults, new QueryCustomizer[] {hint0, hint1});
  }


  public final List<R> select(
    final int             firstIndex,
    final int             maxResults,
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return select(getEntityManager(), firstIndex, maxResults, new QueryCustomizer[] {hint0, hint1});
  }


  public final List<R> select(
    final int                 firstIndex,
    final int                 maxResults,
    final QueryCustomizer...  hints
  )
  {
    return select(getEntityManager(), firstIndex, maxResults, hints);
  }




  public final List<R> selectAndRemove()
  {
    return selectAndRemove(getEntityManager(), 0, -1, (QueryCustomizer[]) null);
  }


  public final List<R> selectAndRemove(final QueryCustomizer hint)
  {
    return selectAndRemove(getEntityManager(), 0, -1, new QueryCustomizer[] {hint});
  }


  public final List<R> selectAndRemove(
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectAndRemove(getEntityManager(), 0, -1, hint0, hint1);
  }


  public final List<R> selectAndRemove(final int maxResults)
  {
    return selectAndRemove(getEntityManager(), 0, maxResults, (QueryCustomizer[]) null);
  }


  public final List<R> selectAndRemove(
    final int             maxResults,
    final QueryCustomizer hint
  )
  {
    return selectAndRemove(getEntityManager(), 0, maxResults, new QueryCustomizer[] {hint});
  }


  public final List<R> selectAndRemove(
    final int             maxResults,
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectAndRemove(getEntityManager(), 0, maxResults, new QueryCustomizer[] {hint0, hint1});
  }


  public final List<R> selectAndRemove(
    final int             firstIndex,
    final int             maxResults,
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectAndRemove(getEntityManager(), firstIndex, maxResults, new QueryCustomizer[] {hint0, hint1});
  }


  public final List<R> selectAndRemove(
    final int                 firstIndex,
    final int                 maxResults,
    final QueryCustomizer...  hints
  )
  {
    return selectAndRemove(getEntityManager(), firstIndex, maxResults, hints);
  }



  public final R selectAndRemoveSingle()
  {
    return selectAndRemoveSingle(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final R selectAndRemoveSingle(final QueryCustomizer hint)
  {
    return selectAndRemoveSingle(getEntityManager(), new QueryCustomizer[] {hint});
  }


  public final R selectAndRemoveSingle(
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectAndRemoveSingle(getEntityManager(), new QueryCustomizer[] {hint0, hint1});
  }


  public final R selectAndRemoveSingle(final QueryCustomizer... hints)
  {
    return selectAndRemoveSingle(getEntityManager(), hints);
  }



  public final R selectAndRemoveTop()
  {
    return selectAndRemoveTop(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final R selectAndRemoveTop(final QueryCustomizer hint)
  {
    return selectAndRemoveTop(getEntityManager(), new QueryCustomizer[] {hint});
  }


  public final R selectAndRemoveTop(
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectAndRemoveTop(getEntityManager(), new QueryCustomizer[] {hint0, hint1});
  }


  public final R selectAndRemoveTop(final QueryCustomizer... hints)
  {
    return selectAndRemoveTop(getEntityManager(), hints);
  }



  public final R selectSingle()
  {
    return selectSingle(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final R selectSingle(final QueryCustomizer hint)
  {
    return selectSingle(getEntityManager(), new QueryCustomizer[] {hint});
  }


  public final R selectSingle(
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectSingle(getEntityManager(), new QueryCustomizer[] {hint0, hint1});
  }


  public final R selectSingle(final QueryCustomizer... hints)
  {
    return selectSingle(getEntityManager(), hints);
  }



  public final R selectTop()
  {
    return selectTop(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final R selectTop(final QueryCustomizer hint)
  {
    return selectTop(getEntityManager(), new QueryCustomizer[] {hint});
  }


  public final R selectTop(final QueryCustomizer hint0, final QueryCustomizer hint1)
  {
    return selectTop(getEntityManager(), new QueryCustomizer[] {hint0, hint1});
  }


  public final R selectTop(final QueryCustomizer... hints)
  {
    return selectTop(getEntityManager(), hints);
  }



  public final R selectUnique()
  {
    return selectUnique(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final R selectUnique(final QueryCustomizer hint)
  {
    return selectUnique(getEntityManager(), new QueryCustomizer[] {hint});
  }


  public final R selectUnique(
    final QueryCustomizer hint0,
    final QueryCustomizer hint1
  )
  {
    return selectUnique(getEntityManager(), new QueryCustomizer[] {hint0, hint1});
  }


  public final R selectUnique(final QueryCustomizer... hints)
  {
    return selectUnique(getEntityManager(), hints);
  }





  public final int update()
  {
    return update(getEntityManager(), (QueryCustomizer[]) null);
  }


  public final int update(final QueryCustomizer hint)
  {
    return update(getEntityManager(), new QueryCustomizer[] {hint});
  }


  public final int update(final QueryCustomizer hint0, final QueryCustomizer hint1)
  {
    return update(getEntityManager(), new QueryCustomizer[] {hint0, hint1});
  }


  public final int update(final QueryCustomizer... hints)
  {
    return update(getEntityManager(), hints);
  }


}
