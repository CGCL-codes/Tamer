package net.sf.staccatocommons.iterators;

import net.sf.staccatocommons.restrictions.check.NonNull;

/**
 * @author flbulgarelli
 * 
 */
public class CharSequenceThriterator extends IndexedThriterator<Character> {

    private final CharSequence charSequence;

    /**
   * Creates a new {@link CharSequenceThriterator}
   * 
   * @param charSequence
   *          the sequence to wrap
   */
    public CharSequenceThriterator(@NonNull CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    protected int length() {
        return charSequence.length();
    }

    protected Character elementAt(int position) {
        return charSequence.charAt(position);
    }
}
