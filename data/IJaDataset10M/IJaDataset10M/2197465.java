package org.jcvi.vics.shared.lucene;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: lkagan
 * Date: Mar 7, 2008
 * Time: 10:21:08 AM
 */
public class SimpleOut {

    public static SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss.SSS");

    public static void sysOut(String out) {
        System.out.println(sdf.format(new Date()) + ": " + out);
    }
}
