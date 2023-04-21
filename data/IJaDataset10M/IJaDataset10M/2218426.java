package com.leantell.lp.annotation;

import java.util.List;
import junit.framework.TestCase;
import com.leantell.lp.annotation.Annotater;
import com.leantell.lp.annotation.Annotation;
import com.leantell.lp.annotation.AnnotationModel;
import com.leantell.lp.lexical.Token;
import com.leantell.lp.ps.EagerReplacingParser;
import com.leantell.lp.spi.Document;
import com.leantell.lp.tabular.language.TabularTestingLanguage;

/**
 * @author ozgur.tumer@gmail.com
 */
public class AnnotationTest extends TestCase {

    private Document document;

    private String code;

    private Annotater annotater;

    public AnnotationTest() {
        TabularTestingLanguage tabularTestingLanguage = new TabularTestingLanguage();
        tabularTestingLanguage.getLanguage().getGrammer().propagateParseStrategy(EagerReplacingParser.INSTANCE);
        document = new Document(tabularTestingLanguage);
        annotater = new Annotater();
    }

    @Override
    protected void setUp() throws Exception {
        code = "<html>" + "\n" + "<tasyntaxError2ble>" + "\n" + "<tr><td colSpan='3'>com.leantell.lp.tabular.test.model.Book</td></tr>" + "\n" + "<tr><td>variable</td><td>isbn</td><td>Name</td></tr>" + "\n" + "<tr><td>cLisp</td><td>0133708756</td><td>\"ANSI Common LISP\"</td></tr>" + "\n" + "<tr><tsyntaxError2>sBook</td><td>0399152970</td><td>\"S is for Silence\"</td></tr>" + "\n" + "</table>" + "\n" + "</html>";
    }

    public void testAnnotatesSyntaxErrors() throws Exception {
        document.setCode(code);
        document.build();
        AnnotationModel annotationModel = new AnnotationModel();
        List<Token> tokens = document.getTokens();
        annotater.annotateSyntaxErrors(tokens, annotationModel);
        assertEquals(2, annotationModel.getAnnotations().size());
    }

    public void testAnnotatesParsingErrors() throws Exception {
        document.setCode(code);
        document.build();
        AnnotationModel annotationModel = new AnnotationModel();
        annotater.annotateParsingErrors(document.getParseResult(), annotationModel);
        assertEquals(3, annotationModel.getAnnotations().size());
    }

    public void testAnnotatesErrorsByLine() throws Exception {
        document.setCode(code);
        document.build();
        AnnotationModel annotate = document.annotate();
        System.out.println(annotate);
        List<Annotation> line2 = annotate.getAnnotations(2);
        assertNotNull(line2);
        assertEquals(4, line2.size());
        List<Annotation> line6 = annotate.getAnnotations(6);
        assertNotNull(line6);
        assertEquals(1, line6.size());
    }
}
