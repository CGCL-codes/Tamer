package com.android.quicksearchbox;

/**
 * The result of getting suggestions from a single source.
 */
public interface SourceResult extends SuggestionCursor {

    Source getSource();
}
