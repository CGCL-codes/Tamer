package com.google.gwt.junit.client;

/**
 * A benchmark category. {@link com.google.gwt.junit.client.Benchmark}s which
 * use the GWT annotation, <code>@gwt.benchmark.category</code>, must set it to
 * a class which implements this interface.
 *
 * <p>The following GWT annotations can be set on a <code>Category</code>:
 *
 * <ul>
 *   <li><code>@gwt.benchmark.name</code> The name of the <code>Category</code>
 * </li>
 *  <li><code>@gwt.benchmark.description</code> The description of the
 * <code>Category</code></li>
 * </ul>
 * </p>
 * 
 */
public interface Category {
}
