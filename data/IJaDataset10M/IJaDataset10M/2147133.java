package org.egavas.news.tags;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.egavas.base.*;
import org.egavas.xml.*;

public class AllNewsTEI extends TagExtraInfo {

    public VariableInfo[] getVariableInfo(TagData data) {
        String name = data.getAttributeString("name");
        String className = "org.egavas.news.NewsBean";
        VariableInfo v = new VariableInfo(name, className, true, VariableInfo.NESTED);
        return new VariableInfo[] { v };
    }
}
