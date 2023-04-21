package net.sf.komodo.lang.java.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import net.sf.komodo.core.blackboard.AstNode;
import net.sf.komodo.core.blackboard.SourceFile;
import net.sf.komodo.core.plugins.ParseException;
import net.sf.komodo.core.plugins.ParserPlugin;
import net.sf.komodo.lang.java.parser.jdt.JDTCompilationUnitVisitor;

/**
 * The JavaJDTParser is an implementation of a parser that uses the Eclipse JDT parser 
 * to generate the Java AST for a Java source file.
 *
 */
public class JavaJDTParser implements ParserPlugin {

    public AstNode parse(SourceFile file) throws ParseException {
        AstNode ret = null;
        try {
            char[] source = IOUtils.toCharArray(new FileInputStream(file.getFile()));
            CompilationUnit sourceUnit = new CompilationUnit(source, "AbstractClass.java", "");
            IErrorHandlingPolicy epoli = new IErrorHandlingPolicy() {

                public boolean proceedOnErrors() {
                    return true;
                }

                public boolean stopOnFirstError() {
                    return false;
                }
            };
            CompilerOptions options = new CompilerOptions();
            IProblemFactory pfactory = new DefaultProblemFactory();
            ProblemReporter problemReporter = new ProblemReporter(epoli, options, pfactory);
            Parser parser = new Parser(problemReporter, false);
            parser.javadocParser.checkDocComment = true;
            CompilationResult unitResult = new CompilationResult(sourceUnit, 1, 1, -1);
            CompilationUnitDeclaration parsedUnit = parser.parse(sourceUnit, unitResult);
            JDTCompilationUnitVisitor v = new JDTCompilationUnitVisitor(parsedUnit);
            v.start();
            ret = v.getCompilationUnitAst();
        } catch (IOException e) {
            throw new ParseException(e);
        }
        return ret;
    }

    public void setParameters(Map<String, String> parameters) {
    }
}
