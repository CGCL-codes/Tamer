package org.aspencloud.simple9.builder.views.dom;

import java.util.ArrayList;
import java.util.List;
import org.aspencloud.simple9.builder.gen.src.SourceFile;
import org.aspencloud.simple9.server.enums.Tag;

public class HtmlJavaPart extends HtmlPart {

    public HtmlJavaPart(HtmlElementPart part, int pos, String text) {
        super(part.getParentElement(), Part.Java, pos);
        append(text);
        part.addJavaPart(this);
    }

    @Override
    public String createSource(SourceFile sf) {
        return getText();
    }

    @Override
    public List<Tag> getPossibleCompletions(int pos) {
        return new ArrayList<Tag>();
    }
}
