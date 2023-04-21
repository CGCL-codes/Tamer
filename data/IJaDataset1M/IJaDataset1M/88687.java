package com.googlecode.sarasvati.rubric.lang;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class ErrorReportingRubricLexer extends RubricLexer {

    private ErrorReporter errorReporter;

    public ErrorReportingRubricLexer(final ErrorReporter errorReporter) {
        this.errorReporter = errorReporter;
    }

    public ErrorReportingRubricLexer(final CharStream input, final RecognizerSharedState state, final ErrorReporter errorReporter) {
        super(input, state);
        this.errorReporter = errorReporter;
    }

    public ErrorReportingRubricLexer(final CharStream input, final ErrorReporter errorReporter) {
        super(input);
        this.errorReporter = errorReporter;
    }

    @Override
    public String getErrorMessage(final RecognitionException re, final String[] arg1) {
        String message = super.getErrorMessage(re, arg1);
        return errorReporter.reportError(re, message);
    }

    @Override
    public void emitErrorMessage(final String arg0) {
    }
}
