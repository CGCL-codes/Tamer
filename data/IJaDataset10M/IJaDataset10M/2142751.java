package com.leantell.lp.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import com.leantell.lp.Grammer;
import com.leantell.lp.InvalidGrammerException;
import com.leantell.lp.Language;
import com.leantell.lp.ParseContext;
import com.leantell.lp.ParseNode;
import com.leantell.lp.annotation.Annotater;
import com.leantell.lp.annotation.AnnotationModel;
import com.leantell.lp.lexical.LexicalAnalyser;
import com.leantell.lp.lexical.Token;

/**
 * @author ozgur.tumer@gmail.com
 */
public class Document {

    private static final int EOF = -1;

    protected String code;

    private final Language language;

    protected LexicalAnalyser lexicalAnalyser;

    protected Grammer grammer;

    protected List<Token> tokens;

    protected ParseNode parseResult;

    private Annotater annotater;

    public Document(LanguageDefinition languageDefinition) {
        super();
        language = languageDefinition.getLanguage();
        lexicalAnalyser = language.getLexicalAnalyser();
        grammer = language.getGrammer();
        annotater = new Annotater();
    }

    public Document(LanguageDefinition languageDefinition, String code) {
        this(languageDefinition);
        this.code = code;
    }

    public void extractSource(InputStream contents) throws IOException {
        InputStreamReader reader = new InputStreamReader(contents);
        int c = reader.read();
        StringBuffer buffer = new StringBuffer();
        while (c != EOF) {
            buffer.append((char) c);
            c = reader.read();
        }
        code = buffer.toString();
    }

    public ParseNode build() throws InvalidGrammerException {
        tokens = tokenize();
        ParseContext parseContext = new ParseContext();
        parseResult = grammer.parse(tokens, parseContext);
        return parseResult;
    }

    protected List<Token> tokenize() {
        return lexicalAnalyser.tokenizeIgnoringUnrecognizedTokens(code);
    }

    public void reconcile(Region region) {
    }

    public ParseNode build(String code) throws InvalidGrammerException {
        setCode(code);
        return build();
    }

    public List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public ParseNode getParseResult() {
        return parseResult;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AnnotationModel annotate() {
        AnnotationModel annotationModel = new AnnotationModel();
        annotater.annotateSyntaxErrors(tokens, annotationModel);
        annotater.annotateParsingErrors(parseResult, annotationModel);
        return annotationModel;
    }
}
