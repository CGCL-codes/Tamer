package com.googlecode.greenbridge.storyharvester.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.codehaus.swizzle.confluence.Page;

/**
 *
 * @author ryan
 */
public class ConfluencePages {

    public List storyPages() throws IOException {
        List list = new ArrayList();
        {
            Map data = new HashMap();
            data.put("content", getSampleStoryPageContent());
            data.put("url", "");
            data.put("title", "A bit of one");
            Page p = new Page(data);
            list.add(p);
        }
        return list;
    }

    public Page getSampleStoryPage() throws IOException {
        Map data = new HashMap();
        data.put("content", getSampleStoryPageContent());
        data.put("url", "http://google.com/A1234");
        data.put("title", "A1234");
        data.put("version", "1");
        Page p = new Page(data);
        return p;
    }

    public Page getSampleStoryPageWithBadTitle() throws IOException {
        Map data = new HashMap();
        data.put("content", getSampleStoryPageContent());
        data.put("url", "http://google.com/#Bad Story");
        data.put("title", "#Bad Story");
        data.put("version", "1");
        Page p = new Page(data);
        return p;
    }

    public Page getSampleScenarioPage() throws IOException {
        Map data = new HashMap();
        data.put("content", getSampleScenarioPageContent());
        data.put("url", "http://google.com/ABCD");
        data.put("title", "ABCD");
        data.put("version", "2");
        Page p = new Page(data);
        return p;
    }

    public String getSampleStoryPageContent() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/confluence_story.txt");
        return IOUtils.toString(stream);
    }

    public String getSampleScenarioPageContent() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/confluence_scenario.txt");
        return IOUtils.toString(stream);
    }
}
