package com2.sun.source.tree;

import java.util.List;
import java.util.Set;
import javax2.lang.model.element.Modifier;

/**
 * A tree node for the modifiers, including annotations, for a declaration.
 *
 * For example:
 * <pre>
 *   <em>flags</em> 
 *
 *   <em>flags</em> <em>annotations</em>
 * </pre>
 *
 * @see "The Java Language Specification, 3rd ed, sections
 * 8.1.1, 8.3.1, 8.4.3, 8.5.1, 8.8.3, 9.1.1, and 9.7"
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public interface ModifiersTree extends Tree {

    Set<Modifier> getFlags();

    List<? extends AnnotationTree> getAnnotations();
}
