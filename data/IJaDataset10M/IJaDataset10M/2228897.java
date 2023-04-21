package com.schmeedy.relaxng.contentassist;

import org.kohsuke.rngom.binary.Pattern;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * 
 * 
 * @author Martin Schmied, schmeedy.com
 */
public interface IRngResolver {

    static final String KEY_RNG_PATTERN = "com.schmeedy.relaxng.contentassist:element-pattern";

    Pattern getRngPattern(Element element);

    Pattern getRngPattern(Text unfinishedElement);

    Pattern getRngPatternForContent(Element element);

    Pattern matchAttributes(Element element, Pattern elementContentPattern, Attr except);
}
