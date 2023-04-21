package aamfetch;

public interface ArticleHandler {

    public void handleArticle(String article, String bucket);

    public void postingsLost(int count);

    public void finished(boolean newHeaders, int newArticles, int bytesLoaded, int bytesSaved);

    public void stopped();

    public void startHeaders(int from, int to);

    public void doneHeaders(int headersFetched, int bodiesToFetch, int bytes);

    public void articleFetched();

    public void headerFetched();

    public void setFetcher(Fetcher f);
}
