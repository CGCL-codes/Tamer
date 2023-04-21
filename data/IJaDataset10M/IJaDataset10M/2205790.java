package org.aspencloud.simple9.builder.views.dom;

import org.aspencloud.simple9.builder.gen.src.SourceFile;

public class EmptyLineBlock extends SingleLineBlock {

    public EmptyLineBlock(Block parent, int line, int level) {
        super(parent, line, level, null);
    }

    @Override
    public String createSource(SourceFile sf) {
        return "";
    }
}
