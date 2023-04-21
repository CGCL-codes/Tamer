package com.organic.maynard.util.crawler;

import java.io.*;
import java.util.*;
import com.organic.maynard.io.*;
import com.organic.maynard.util.string.*;

public class TabStripperFileContentsHandler extends FileContentsHandler {

    private int numOfTabs = 0;

    public TabStripperFileContentsHandler(int numOfTabs, String lineEnding) {
        super(lineEnding, false);
        setNumOfTabs(numOfTabs);
    }

    public int getNumOfTabs() {
        return numOfTabs;
    }

    public void setNumOfTabs(int numOfTabs) {
        this.numOfTabs = numOfTabs;
    }

    protected String processContents(String contents) {
        StringBuffer buf = new StringBuffer();
        StringSplitter splitter = new StringSplitter(contents, getLineEnding());
        while (splitter.hasMoreElements()) {
            String line = (String) splitter.nextElement();
            buf.append(StringTools.trimFront(line, "\t", numOfTabs)).append(getLineEnding());
        }
        return buf.toString();
    }
}
