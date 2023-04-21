package Cosmo.Editor.nbeditorlib.JavaEditor;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.netbeans.editor.Syntax;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.spi.editor.guards.GuardedEditorSupport;

public class JavaEditorKit extends ExtKit implements GuardedEditorSupport {

    /**
   * Creates a new instance of JavaEditorKit
   */
    public JavaEditorKit() {
        super();
    }

    public String getContentType() {
        return "text/java";
    }

    public Syntax createSyntax(Document document) {
        return new JavaSyntax();
    }

    public StyledDocument getDocument() {
        StyledDocument sdoc = new DefaultStyledDocument();
        return null;
    }
}
