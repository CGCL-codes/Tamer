package com.sun.source.tree;

import java.util.List;
import javax.lang.model.element.Name;

/**
 * A tree node for a type parameter.
 *
 * For example:
 * <pre>
 *   <em>name</em>
 *
 *   <em>name</em> extends <em>bounds</em>
 * </pre>
 *
 * @jls section 4.4
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public interface TypeParameterTree extends Tree {

    Name getName();

    List<? extends Tree> getBounds();
}
