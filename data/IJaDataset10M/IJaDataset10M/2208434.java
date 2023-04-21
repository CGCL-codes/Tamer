package pl.omtt.compiler.reporting;

import java.net.URI;
import java.net.URISyntaxException;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.RecognitionException;
import pl.omtt.lang.analyze.SemanticException;

public abstract class AbstractProblemCollector implements IProblemCollector {

    int fProblemLevel = NO_PROBLEMS;

    int fNumberOfErrors = 0;

    public void reportError(URI uri, String message) {
        doCollect(Problem.fromMessage(Problem.ERROR, uri, message));
    }

    public void reportError(String path, Exception e) {
        URI uri = null;
        try {
            uri = new URI(path);
        } catch (URISyntaxException urie) {
            urie.printStackTrace();
            return;
        }
        reportError(uri, e);
    }

    public void reportError(URI uri, Exception e) {
        report(Problem.fromException(Problem.ERROR, uri, e));
    }

    public void reportError(String path, SemanticException e) {
        URI uri = null;
        try {
            uri = new URI(path);
        } catch (URISyntaxException urie) {
            urie.printStackTrace();
            return;
        }
        reportError(uri, e);
    }

    public void reportError(URI uri, SemanticException e) {
        report(Problem.fromSemanticException(uri, e));
    }

    @Override
    public void reportError(URI uri, RecognitionException e, String message) {
        report(Problem.fromRecognitionException(Problem.ERROR, uri, e, message));
    }

    @Override
    public void reportError(URI uri, RecognitionException e) {
        reportError(uri, e, e.getMessage());
    }

    @Override
    public void reportError(String path, RecognitionException e, String message) {
        URI uri = null;
        try {
            uri = new URI(path.replaceAll(" ", "%20"));
        } catch (URISyntaxException urie) {
            urie.printStackTrace();
            return;
        }
        reportError(uri, e, message);
    }

    @Override
    public void reportError(URI uri, CommonToken token, String message) {
        report(Problem.fromCommonToken(Problem.ERROR, uri, message, token));
    }

    protected void report(Problem problem) {
        if (fProblemLevel > problem.fSeverity) fProblemLevel = problem.fSeverity;
        doCollect(problem);
    }

    private void doCollect(Problem problem) {
        fNumberOfErrors++;
        collect(problem);
    }

    protected abstract void collect(Problem problem);

    @Override
    public boolean errors() {
        return fProblemLevel <= Problem.ERROR;
    }

    @Override
    public int numberOfErrors() {
        return fNumberOfErrors;
    }

    static final int NO_PROBLEMS = 0xffff;
}
