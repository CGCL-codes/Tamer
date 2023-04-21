package org.wportal.jspwiki.dbprovider;

import com.ecyrd.jspwiki.providers.WikiAttachmentProvider;
import com.ecyrd.jspwiki.providers.WikiPageProvider;
import org.wportal.core.ContextManager;
import org.wportal.testutil.JspWikiDatabaseTestCase;

/**
 * User: SimonLei
 * Date: 2004-8-8
 * Time: 22:04:04
 * $Id: TestDatabaseProvider.java,v 1.2 2004/11/07 07:24:43 simon_lei Exp $
 */
public abstract class TestDatabaseProvider extends JspWikiDatabaseTestCase {

    public TestDatabaseProvider(String name) {
        super(name);
    }

    protected WikiPageProvider provider;

    protected WikiAttachmentProvider attachProvider;

    protected void setUp() throws Exception {
        super.setUp();
        provider = (WikiPageProvider) ContextManager.getBean("pageProviderDao");
        attachProvider = (WikiAttachmentProvider) ContextManager.getBean("attachProviderDao");
    }
}
