package nl.flotsam.pecia.base;

import nl.flotsam.pecia.builder.ArticleDocument;

public class Documents {

    public static void produceSimpleDocument(ArticleDocument doc) {
        doc.itemizedList().item("whatever").item("foobar").item().para().code("whatever").text(" this means ").email("wilfred@agilejava.com").end().end().end().table3Cols().header().entry().para().text("Field1").end().entry().para().text("Field2").end().entry().para().text("Field3").end().end().row().entry().para().text("Whatever").end().entry().para().text("Yup").end().entry().para().text("Yup").xref("whatever").end().end().end().section("First section").mark("first-section").para().text("This is the first section.").end().end().end();
    }
}
