package com.bluesky.javawebbrowser.domain.html.tags;

public class Div extends Tag {

    public Div() {
        super(TagType.DIV);
    }

    public Div(Tag tag) {
        super(tag);
        tagType = TagType.DIV;
    }
}
