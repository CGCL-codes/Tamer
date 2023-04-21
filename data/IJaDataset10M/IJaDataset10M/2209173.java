package org.awelements.table.test;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.awelements.table.web.HtmlInfoBox;

public class TestHtmlInfoBox extends HtmlInfoBox {

    public TestHtmlInfoBox() {
        super("testBox", "Test Info Box");
        setHeight(200);
        setWidth(300);
    }

    @Override
    public void appendHtml(Writer writer) throws IOException {
        final String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        writer.append("<span style=\"color: red\">hallo " + getParameter() + " at " + now + "</span>");
        writer.append("<br/><a href='http://www.google.de'>Google</a>");
        writer.append("<br/>Guten Abend");
        writer.append("<br/>Gute Nacht");
        writer.append("<br/>Mit");
        writer.append("<br/>Schlaf");
        writer.append("<br/>bedacht.");
    }
}
