package com.google.gwt.inject.rebind.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder that composes source snippets from other source snippets.
 */
public class SourceSnippetBuilder {

    List<SourceSnippet> snippets = new ArrayList<SourceSnippet>();

    /**
   * Convenience routine to append a constant string to this snippet.
   */
    public SourceSnippetBuilder append(String snippet) {
        snippets.add(SourceSnippets.forText(snippet));
        return this;
    }

    public SourceSnippetBuilder append(SourceSnippet snippet) {
        snippets.add(snippet);
        return this;
    }

    public SourceSnippet build() {
        final List<SourceSnippet> snippetsCopy = new ArrayList<SourceSnippet>(snippets);
        return new SourceSnippet() {

            public String getSource(InjectorWriteContext writeContext) {
                StringBuilder resultBuilder = new StringBuilder();
                for (SourceSnippet snippet : snippetsCopy) {
                    resultBuilder.append(snippet.getSource(writeContext));
                }
                return resultBuilder.toString();
            }
        };
    }
}
