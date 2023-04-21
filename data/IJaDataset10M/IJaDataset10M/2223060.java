package com.once.servicescout.rest;

import com.once.servicescout.config.impl.RestConfigImpl;
import com.once.servicescout.rest.client.SampleClient;
import com.once.servicescout.rest.pojo.QueryData;
import java.io.UnsupportedEncodingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author cwchen
 * @Created 2008-2-27
 * @Contact comain@gmail.com
 */
public class GetTest {

    public void testListService() {
        SampleClient client = new SampleClient();
        client.get(RestConfigImpl.url_base + "/services");
    }

    public void testShowService() throws UnsupportedEncodingException {
        SampleClient client = new SampleClient();
        System.out.println(new String(client.get(RestConfigImpl.url_base + "/services/00001"), "utf-8"));
    }

    @Before
    public void setUp() {
    }

    @After
    public void shutDown() {
    }

    @Test
    public void testAdvancedQuery() throws UnsupportedEncodingException {
        SampleClient client = new SampleClient();
        QueryData d = new QueryData();
        d.setFunc("借阅");
        d.setQos("AND(1>Reliability>0.8,0.3>ResponseTime>0)");
        d.setNum("2");
        d.setPref("3,3,3,1");
        System.out.println(new String(client.query("http://localhost:8080/servicescout/services", d), "utf-8"));
    }
}
