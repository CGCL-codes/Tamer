package org.omegat.filters3;

/**
 * Element of the translatable entry.
 * Can be a tag or a piece of text.
 *
 * @author Maxym Mykhalchuk
 */
public interface Element {

    /**
     * Returns shortcut string representation of the element. 
     * E.g. for &lt;strong&gt; tag should return &lt;s3&gt;.
     */
    String toShortcut();

    /**
     * Returns long XML-encoded representation of the element for storing in TMX. 
     * E.g. for &lt;strong&gt; tag should return 
     * &lt;bpt i="3"&gt;&amp;lt;strong&amp;gt;&lt;/bpt&gt;.
     */
    String toTMX();

    /**
     * Returns the element in its original form as it was in original document.
     * E.g. for &lt;strong&gt; tag should return 
     * &lt;bpt i="3"&gt;&amp;lt;strong&amp;gt;&lt;/bpt&gt;.
     */
    String toOriginal();
}
