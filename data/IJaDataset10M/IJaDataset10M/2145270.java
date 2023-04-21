package com.protomatter.pas;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.*;
import com.protomatter.syslog.*;
import com.protomatter.pas.jndi.*;
import com.protomatter.pas.init.*;

/**
 *  A utility class for parsing properties files.
 */
public class PASPropertyUtil {

    /**
   *  Don't want people calling this constructor.
   */
    private PASPropertyUtil() {
        super();
    }

    /**
   *  Parse a string that looks like "foo=bar,this=that" into properties.
   */
    public static Properties getProperties(String value) {
        Properties p = new Properties();
        if (value == null) return p;
        StringTokenizer st = new StringTokenizer(value, ",");
        while (st.hasMoreTokens()) {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), "=");
            p.put(st2.nextToken(), st2.nextToken());
        }
        return p;
    }
}
