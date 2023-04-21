package de.knowwe.core;

import java.util.regex.Pattern;
import de.d3web.we.kdom.AbstractType;
import de.d3web.we.kdom.rendering.StyleRenderer;
import de.d3web.we.kdom.sectionFinder.RegexSectionFinder;
import de.d3web.we.utils.Patterns;

/**
 * 
 * @author Reinhard Hatko Created on: 19.11.2009
 */
public class CommentLineType extends AbstractType {

    @Override
    protected void init() {
        super.init();
        setSectionFinder(new RegexSectionFinder(Patterns.COMMENTLINE, Pattern.MULTILINE));
        setCustomRenderer(StyleRenderer.COMMENT);
    }
}
