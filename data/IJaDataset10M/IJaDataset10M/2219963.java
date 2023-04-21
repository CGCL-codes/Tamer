package org.xteam.cs.runtime.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import org.xteam.cs.grm.GrammarFile;
import org.xteam.cs.grm.build.ParserBuild;
import org.xteam.cs.grm.editor.ASTSyntaxReducer;
import org.xteam.cs.grm.editor.ASTTokenNode;
import org.xteam.cs.lex.build.LexerBuild;
import org.xteam.cs.model.ErrorMark;
import org.xteam.cs.model.FileResource;
import org.xteam.cs.model.IProgressMonitor;
import org.xteam.cs.model.Project;
import org.xteam.cs.model.ProjectManager;
import org.xteam.cs.model.ProjectResource;
import org.xteam.cs.runtime.CorrectingLRParser;
import org.xteam.cs.runtime.DefaultErrorReporter;
import org.xteam.cs.runtime.IStatedLexer;
import org.xteam.cs.runtime.ISyntaxHelper;
import org.xteam.cs.runtime.IToken;
import org.xteam.cs.runtime.ITokenFactory;
import org.xteam.cs.runtime.Span;
import org.xteam.cs.runtime.TableBasedLexer;

public class CorrectingLRParserTest {

    public static void main(String[] args) throws IOException {
        ProjectManager manager = new ProjectManager();
        Project project = new Project(manager);
        project.open(new File("examples/st80/st80.cpj"));
        manager.buildProject(project, new ProgressMonitor());
        boolean hasError = false;
        for (ProjectResource resource : project.getResources()) {
            List<ErrorMark> marks = resource.getMarks(ErrorMark.class);
            hasError |= marks.size() > 0;
            ErrorMarkConsoleDiagnostic diag = new ErrorMarkConsoleDiagnostic(System.out);
            diag.printDiagnostic(new FileReader(((FileResource) resource).getFile()), marks);
        }
        if (!hasError) {
            for (GrammarFile resource : project.getResources(GrammarFile.class)) {
                ParserBuild parserBuild = resource.getParserModel();
                if (parserBuild.isValidForAST()) {
                    testASTParser(parserBuild);
                }
            }
        }
    }

    private static String CONTENTS = "doIt\n" + "| rectangles aPoint |\n" + "rectangles := OrderedCollection\n" + "  with: (Rectangle left: 0 right: 10 top: 100 bottom: 200)\n" + "  with: (Rectangle left: 10 right: 10 top: 110 bottom: 210).\n" + "aPoint := Point x: 20 y: 20.\n" + "collisions := rectangles select: [:aRect | aRect containsPoint: aPoint].\n";

    private static void testASTParser(ParserBuild parserBuild) throws IOException {
        System.out.println("=======================================");
        DefaultErrorReporter reporter = new DefaultErrorReporter(System.out);
        final LexerBuild lexerBuild = parserBuild.getLexerBuild();
        ITokenFactory factory = new ITokenFactory() {

            @Override
            public IToken newToken(int type, Span span, Object content) {
                return new ASTTokenNode(type, lexerBuild.getMapping().getToken(type), span.start(), span.length(), content);
            }
        };
        IStatedLexer lexer = new TableBasedLexer(lexerBuild.getTables(), null, factory);
        lexer.skipComments(true);
        lexer.setErrorReporter(reporter);
        lexer.setInput(new StringReader(CONTENTS));
        ISyntaxHelper helper = new ISyntaxHelper() {

            @Override
            public String getTokenString(int t) {
                return lexerBuild.getMapping().getTokenName(t);
            }

            @Override
            public boolean isEof(int t) {
                return lexerBuild.getMapping().getTokenNumber("$EOF$") == t;
            }
        };
        CorrectingLRParser parser = new CorrectingLRParser(parserBuild.getTables(), lexer, factory, new ASTSyntaxReducer(parserBuild.getMapping(), lexer), helper, reporter);
        try {
            Object root = (Object) parser.parse();
            if (reporter.hasErrors()) {
                reporter.printDiagnostic(new StringReader(CONTENTS));
            } else {
                System.out.println(root);
            }
        } catch (IOException e) {
            reporter.printDiagnostic(new StringReader(CONTENTS));
        }
    }

    private static class ErrorMarkConsoleDiagnostic {

        private PrintStream out;

        public ErrorMarkConsoleDiagnostic(PrintStream out) {
            this.out = out;
        }

        public void printDiagnostic(Reader stream, List<ErrorMark> marks) throws IOException {
            int lineNumber = 1;
            int lineStart = 0;
            StringBuffer line = new StringBuffer();
            while (stream.ready() && marks.size() > 0) {
                int c = stream.read();
                if (c == -1) break;
                if (c == '\n') {
                    printError(lineNumber, lineStart, line, marks);
                    lineStart += line.length() + 1;
                    ++lineNumber;
                    line.setLength(0);
                } else line.append((char) c);
            }
            if (marks.size() > 0) printError(lineNumber, lineStart, line, marks);
        }

        private void printError(int lineNumber, int lineStart, StringBuffer line, List<ErrorMark> marks) {
            Iterator<ErrorMark> e = marks.iterator();
            while (e.hasNext()) {
                ErrorMark error = e.next();
                if (error.getSpan().start() >= lineStart && error.getSpan().start() <= (lineStart + line.length())) {
                    out.println(line.toString().replace('\t', ' '));
                    int offset = error.getSpan().start() - lineStart;
                    for (int i = 0; i < offset; ++i) {
                        out.print(" ");
                    }
                    for (int i = 0; i < error.getSpan().length() && i < (line.length() - offset); ++i) {
                        out.print("^");
                    }
                    out.println();
                    out.println("[" + lineNumber + "]: " + error.getMessage());
                    out.println();
                    e.remove();
                }
            }
        }
    }

    private static class ProgressMonitor implements IProgressMonitor {

        @Override
        public void beginTask(String msg, int amountOfWork) {
            System.out.println("=== " + msg);
        }

        @Override
        public void done() {
            System.out.println("=== done");
        }

        @Override
        public void subTask(String name) {
            System.out.println("*** " + name);
        }

        @Override
        public void worked(int amount) {
            System.out.println("+(" + amount + ")");
        }

        @Override
        public void internalWorked(double amount) {
            System.out.println("+(" + amount + ")");
        }
    }
}
