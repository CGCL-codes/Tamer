package com.byjyate.rssdreamwork;

public class RemoveAfterIncSelfTransformer extends RssContentTransformer {

    private String endText;

    public RemoveAfterIncSelfTransformer(String endText) {
        this.endText = endText;
    }

    @Override
    protected String processContent(String source) {
        if (endText == null || endText.isEmpty()) return source;
        int position = source.indexOf(endText);
        if (position != -1) source = source.substring(0, position);
        return source;
    }
}
