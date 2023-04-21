package com.enoram.training2.delegate;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.enoram.training2.domain.ErrorMessage;
import com.enoram.training2.domain.HelpItem;
import com.enoram.training2.domain.NewsItem;

public class TestMessageGuice extends BaseGuiceTest {

    MessageService messageService;

    @Before
    public void setUp() {
        super.setUp();
        messageService = injector.getInstance(MessageService.class);
    }

    @Test
    public void testSaveNewsItem() {
        NewsItem news = new NewsItem();
        news.setAppName("EFilingGuice2");
        news.setMessage("This is Test Message" + System.currentTimeMillis());
        messageService.save(news);
        Assert.assertTrue(news.getId() != 0);
        Assert.assertNotNull(news.getAppName());
        Assert.assertNotNull(news.getMessage());
        messageService.delete(news);
    }

    @Test
    public void testSaveHelpItem() {
        HelpItem help = new HelpItem();
        help.setScreenName("EFilingGuice2");
        help.setMessage("This is Test Message" + System.currentTimeMillis());
        messageService.save(help);
        Assert.assertTrue(help.getId() != 0);
        Assert.assertNotNull(help.getScreenName());
        Assert.assertNotNull(help.getMessage());
        messageService.delete(help);
    }

    @Test
    public void testSaveErrorMessage() {
        ErrorMessage error = new ErrorMessage();
        error.setErrorClassName("EfilingGuice2");
        error.setMessage("This is Test Message" + System.currentTimeMillis());
        messageService.save(error);
        Assert.assertTrue(error.getId() != 0);
        Assert.assertNotNull(error.getErrorClassName());
        Assert.assertNotNull(error.getMessage());
        ErrorMessage error2 = (ErrorMessage) messageService.load(ErrorMessage.class, error.getId());
        messageService.delete(error2);
    }
}
