package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.TextBlockWidth;

public interface BlockMember {

    public TextBlockWidth asTextBlock(FontParam fontParam, ISkinParam skinParam);
}
