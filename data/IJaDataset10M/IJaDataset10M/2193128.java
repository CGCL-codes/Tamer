package org.translationcomponent.api.impl.test.multithreaded.request;

public class PageData {

    private final String originalText;

    private final String original;

    private final long lastModified;

    private final String targetText;

    private final String target;

    private final int translationCount;

    private final String contextUrl;

    private final String relativeUrl;

    private final String queryString;

    public PageData(String originalText, String original, long lastModified, String targetText, String target, int translationCount, String contextUrl, String relativeUrl, String queryString) {
        super();
        this.originalText = originalText;
        this.original = original;
        this.lastModified = lastModified;
        this.targetText = targetText;
        this.target = target;
        this.translationCount = translationCount;
        this.contextUrl = contextUrl;
        this.relativeUrl = relativeUrl;
        this.queryString = queryString;
    }

    public String getOriginal() {
        return original;
    }

    public String getTarget() {
        return target;
    }

    public String getContextUrl() {
        return contextUrl;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getOriginalText() {
        return originalText;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getTargetText() {
        return targetText;
    }

    public int getTranslationCount() {
        return translationCount;
    }
}
