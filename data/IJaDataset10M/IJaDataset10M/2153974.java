package com.blogspot.radialmind.xss.html;

import com.blogspot.radialmind.BaseTestCase;

public class ListStyleTest extends BaseTestCase {

    String listStyle = "<html><head><title>test</title></head><style>li {list-style-image: url(\"javascript:alert('XSS')\");}</style><body><ul><li>XSS</li></ul></body></html>";

    public void testListStyle() {
        testExecute(listStyle, "<html><head><title>test</title></head><body><ul><li>XSS</li></ul></body></html>");
    }
}
