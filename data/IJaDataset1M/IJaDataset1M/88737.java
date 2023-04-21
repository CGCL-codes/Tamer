package edu.vub.at.doc.sub;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.RecognitionException;
import edu.vub.at.doc.elem.ATDocGeneral;
import edu.vub.at.parser.AnnotationLexer;
import edu.vub.at.parser.AnnotationParser;
import edu.vub.at.parser.AnnotationWalker;

/**
 * Wrapper class to facilitate using the Annotation walker and parser.
 * @author bcorne
 */
public class AnnotationProcessor {

    public static void process(String annotationText, ATDocGeneral doc) {
        AnnotationNode annotation = null;
        try {
            ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(annotationText.getBytes()));
            AnnotationLexer lexer = new AnnotationLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            AnnotationParser parser = new AnnotationParser(tokens);
            AnnotationParser.program_return r = parser.program();
            CommonTree t = (CommonTree) r.getTree();
            CommonTreeNodeStream nodes = new CommonTreeNodeStream(t);
            nodes.setTokenStream(tokens);
            AnnotationWalker walker = new AnnotationWalker(nodes);
            annotation = walker.program().doc;
            switch(annotation.getAnnotationType()) {
                case AUTHOR:
                    doc.acceptAuthor((AuthorDoc) annotation);
                    break;
                case CONNECTOR:
                    doc.acceptConnector((ConnectorDoc) annotation);
                    break;
                case DOCSTRING:
                    doc.acceptDocstring((DocstringDoc) annotation);
                    break;
                case EXCLUDE:
                    doc.acceptExclude((ExcludeDoc) annotation);
                    break;
                case JAVASEE:
                    doc.acceptJavaSee((SeeDoc) annotation);
                    break;
                case SEE:
                    doc.acceptSee((SeeDoc) annotation);
                    break;
                case MODULE:
                    doc.acceptModule((ModuleDoc) annotation);
                    break;
                case NAME:
                    doc.acceptName((NameDoc) annotation);
                    break;
                case PARAMETER:
                    doc.acceptParameter((ParameterDoc) annotation);
                    break;
                case RECEIVER:
                    doc.acceptReceiver((ReceiverDoc) annotation);
                    break;
                case RETURN:
                    doc.acceptReturn((ReturnDoc) annotation);
                    break;
                case THROW:
                    doc.acceptThrow((ThrowDoc) annotation);
                    break;
                case TYPE:
                    doc.acceptType((TypeDoc) annotation);
                    break;
            }
        } catch (IOException ioe) {
            System.err.println("Unexpected IO error on input:" + ioe.getMessage());
            ioe.printStackTrace();
        } catch (RecognitionException re) {
            System.err.println("Input not recognized:\n" + annotationText);
            re.printStackTrace();
        } catch (ClassCastException cce) {
            System.err.println("Input not recognized as " + annotation.getAnnotationType());
            cce.printStackTrace();
        }
    }
}
