package org.nodal.nav;

/**
 * A Path that is a reference to a fragment of a Document.
 */
public interface FragmentPath extends Path {

    /**
   * The FragmentPath that is the parent of this path.
   * <p>N.B. The parent() of a FragmentPath is always a FragmentPath.</p>
   */
    FragmentPath fragmentParent();
}
