package org.eclipse.ui.internal.texteditor.quickdiff.compare.equivalence;

/**
 * @since 3.2
 */
public class SystemHashFunction implements IHashFunction {

    public Hash computeHash(CharSequence seq) {
        return new IntHash(seq.toString().hashCode());
    }
}
