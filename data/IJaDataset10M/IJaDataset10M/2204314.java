package ffe;

import java.net.URL;
import demo.codeanalyzer.main.MaxCardinality;
import demo.codeanalyzer.main.NotVisible;
import demo.codeanalyzer.main.RdfLabel;

@NotVisible
public class CommonFooterItem {

    @MaxCardinality("1")
    @RdfLabel("Footer Heading")
    String articleFooterHeading;

    @MaxCardinality("1")
    @RdfLabel("Article Image URL")
    URL articleImageURL;

    @MaxCardinality("1")
    @RdfLabel("Article Headline")
    String articleHeadline;

    @MaxCardinality("1")
    @RdfLabel("Article Summary")
    String articleSummary;

    @MaxCardinality("1")
    @RdfLabel("Article Link")
    String articleLink;
}
