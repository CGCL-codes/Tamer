package com.germinus.xpression.cms.contents;

public class RemoteSribeContentSource extends ScribeContentSource implements ContentSource {

    private String host;

    public RemoteSribeContentSource(String host, String workspace, String contentId, String[] morePath, String query, String fragment) {
        this.host = host;
        this.workspace = workspace;
        this.contentId = contentId;
        this.morePath = morePath;
        this.query = query;
        this.fragment = fragment;
    }

    public Content getContent() {
        if (host != null) return null; else return null;
    }
}
