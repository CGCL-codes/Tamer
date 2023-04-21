package br.com.storyteller.client;

import junit.framework.Assert;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestSample extends GWTTestCase {

    public String getModuleName() {
        return "br.com.storyteller.Application";
    }

    public void testSomething() {
        Assert.assertTrue(true);
    }
}
