package net.sourceforge.jedit.jcompiler;

import net.sourceforge.jedit.buildtools.java.packagebrowser.*;
import net.sourceforge.jedit.buildtools.msg.*;
import java.io.*;

/**
A test suite for jcompiler

@author <A HREF="mailto:burton@relativity.yi.org">Kevin A. Burton</A>
@version $Id: Test.java 7895 2000-01-24 07:00:59Z burtonator $
*/
public class Test {

    public static void main(String[] args) {
        BuildMessage message = BuildMessage.getBuildMessage("        at org.apache.tomcat.core.Response.getWriter(Response.java:217)");
        message = BuildMessage.getBuildMessage("        at java.io.FileInputStream.read(FileInputStream.java)");
    }
}
