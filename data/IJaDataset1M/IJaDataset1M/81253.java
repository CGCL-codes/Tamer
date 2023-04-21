package org.lineman;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Extracts fields by parsing an input source. Each
 * {@link CharSequence CharSequence[]} represents a single row of input fields.
 */
public interface FieldSource extends Iterator<CharSequence[]> {

    /**
     * Constant implementation with no data.
     */
    FieldSource EMPTY_SOURCE = new FieldSource() {

        /**
         * Always returns {@code false}.
         * @return Always returns {@code false}.
         */
        public boolean isHeaderRow() {
            return false;
        }

        /**
         * Always returns {@code false}.
         * @return Always returns {@code false}.
         */
        public boolean hasNext() {
            return false;
        }

        /**
         * Always throws {@link NoSuchElementException}.
         * @return Always throws {@link NoSuchElementException}.
         */
        public CharSequence[] next() {
            throw new NoSuchElementException("No elements in empty source");
        }

        /**
         * Always throws {@link UnsupportedOperationException}.
         * @return Always throws {@link UnsupportedOperationException}.
         */
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove from empty source");
        }

        /**
         * Always returns {@link Identification#UNIDENTIFIED}.
         * @return Always returns {@link Identification#UNIDENTIFIED}.
         */
        public Identification getIdentification() {
            return Identification.UNIDENTIFIED;
        }
    };

    /**
     * Returns {@code true} if the first row is headers.
     * @return whether the first row is headers.
     */
    boolean isHeaderRow();

    /**
     * Identifies the data supplied by this source.
     * @return identification of the data supplied by this source.
     */
    Identification getIdentification();
}
