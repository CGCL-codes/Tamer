package org.dom4j.samples;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.VisitorSupport;

/**
 * A sample program to demonstrate the use of the Visitor Pattern in DOM4J
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class VisitorDemo extends SAXDemo {

    public static void main(String[] args) {
        run(new VisitorDemo(), args);
    }

    public VisitorDemo() {
    }

    protected void process(Document document) throws Exception {
        Visitor visitor = new VisitorSupport() {

            public void visit(Document document) {
                println(document.toString());
            }

            public void visit(DocumentType documentType) {
                println(documentType.toString());
            }

            public void visit(Element node) {
                println(node.toString());
            }

            public void visit(Attribute node) {
                println(node.toString());
            }

            public void visit(CDATA node) {
                println(node.toString());
            }

            public void visit(Comment node) {
                println(node.toString());
            }

            public void visit(Entity node) {
                println(node.toString());
            }

            public void visit(Namespace node) {
                println(node.toString());
            }

            public void visit(ProcessingInstruction node) {
                println(node.toString());
            }

            public void visit(Text node) {
                println(node.toString());
            }
        };
        document.accept(visitor);
    }
}
