package org.xhtmlrenderer.css.parser;

import java.io.StringReader;

public class ParserTest {

    public static void main(String[] args) throws Exception {
        String test = "div { background-image: url('something') }\n";
        StringBuffer longTest = new StringBuffer();
        for (int i = 0; i < 10000; i++) {
            longTest.append(test);
        }
        CSSErrorHandler errorHandler = new CSSErrorHandler() {

            public void error(String uri, String message) {
                System.out.println(message);
            }
        };
        long total = 0;
        for (int i = 0; i < 40; i++) {
            long start = System.currentTimeMillis();
            CSSParser p = new CSSParser(errorHandler);
            p.parseStylesheet(null, 0, new StringReader(longTest.toString()));
            long end = System.currentTimeMillis();
            total += (end - start);
        }
        System.out.println("Average " + (total / 10) + " ms");
        total = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            CSSParser p = new CSSParser(errorHandler);
            p.parseStylesheet(null, 0, new StringReader(longTest.toString()));
            long end = System.currentTimeMillis();
            total += (end - start);
        }
        System.out.println("Average " + (total / 10) + " ms");
        CSSParser p = new CSSParser(errorHandler);
        total = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < 10000; j++) {
                p.parseStylesheet(null, 0, new StringReader(test.toString()));
            }
            long end = System.currentTimeMillis();
            total += (end - start);
        }
        System.out.println("Average " + (total / 10) + " ms");
    }
}
