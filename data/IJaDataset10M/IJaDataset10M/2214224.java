package org.jcyclone.core.basic;

import org.jcyclone.core.boot.JCyclone;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url = Main.class.getClassLoader().getResource("basic-jcyclone.cfg");
        new JCyclone(url.getFile());
    }
}
