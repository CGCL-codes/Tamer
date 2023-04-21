package org.riverock.generic.test;

import java.net.URL;

/**
 * User: Admin
 * Date: Sep 14, 2003
 * Time: 10:00:05 PM
 *
 * $Id: TestURI.java,v 1.5 2005/05/18 07:31:43 serg_main Exp $
 */
public class TestURI {

    private static String addr = "http://me.askmore";

    public static void main(String args[]) throws Exception {
        URL url = new URL(addr);
        System.out.println(url.toString());
        System.out.println("getProtocol " + url.getProtocol());
        System.out.println("getHost " + url.getHost());
        System.out.println("getPort " + url.getPort());
        System.out.println("getPath " + url.getPath());
        System.out.println("getFile " + url.getFile());
        System.out.println("getQuery " + url.getQuery());
        System.out.println("getRef " + url.getRef());
    }
}
