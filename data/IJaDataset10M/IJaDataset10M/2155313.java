package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.Paragraph;

/**
 * Allows you to do additional processing on a Paragraph that contains a link.
 * @author  psoares
 * @since 5.0.6 (renamed)
 */
public interface LinkProcessor {

    /**
	 * Does additional processing on a link paragraph
     * @param current	the Paragraph that has the link
     * @param attrs		the attributes
     * @return	false if the Paragraph no longer needs processing
     */
    boolean process(Paragraph current, ChainedProperties attrs);
}
