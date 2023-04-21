package org.itsocial.test;

import org.itsocial.framework.ui.ajax.grid.html.Attribute;
import org.itsocial.framework.ui.ajax.grid.html.Tag;

public class HTMLTagHelloWorld {

    public static void main(String[] args) throws Exception {
        Tag title = new Tag("title");
        title.add("HTML generator example");
        Tag h1 = new Tag("h1");
        h1.add("HTML Generator demo");
        Tag div = new Tag("div", "class='contentBlock' style='width:auto;padding:10px;margin:0'");
        Tag script = new Tag("Script");
        Attribute src = new Attribute("src", "src='dojo.js'");
        script.add(src);
        src.setValue("DOJO.js");
        div.add(script);
        Tag table = new Tag("table", "border=1 cellpadding=0 cellspacing=0");
        Tag row1 = new Tag("tr");
        Tag row2 = new Tag("tr");
        for (int j = 0; j < 5; j++) {
            Tag cell = new Tag("td");
            cell.add(Integer.toString(j));
            row1.add(cell);
            row2.add(cell);
        }
        table.add(row1);
        table.add(row2);
        Tag cell = new Tag("td");
        cell.add("*");
        row2.set(3, cell);
        div.add(table);
        System.out.println(div);
    }
}
