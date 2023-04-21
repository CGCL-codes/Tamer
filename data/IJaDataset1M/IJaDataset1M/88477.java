package fitnesse.components;

import fitnesse.wiki.WikiPage;

public interface PageReferencer {

    public WikiPage getReferencedPage() throws Exception;
}
