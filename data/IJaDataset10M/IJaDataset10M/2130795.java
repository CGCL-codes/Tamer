package org.ashkelon.pages;

import java.io.File;

/**
 * @author Eitan Suez
 */
public interface HtmlGenerator {

    public void initialize(File srcHtmlDir);

    public void produceHtml(String sourceFile, String realHtmlFile);
}
