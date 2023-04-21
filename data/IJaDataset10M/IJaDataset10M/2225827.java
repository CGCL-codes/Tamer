package junitmetrics.core.existingasserts;

import java.util.List;
import junitmetrics.configuration.files.ProjectFile;
import junitmetrics.core.io.TextFileHandler;
import junitmetrics.core.parser.MethodBodyModifier;
import junitmetrics.core.types.TestType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ExistingAssertsCounter {

    private ASTParser astParser = ASTParser.newParser(AST.JLS3);

    private ExistingAssertsCounterResult result;

    private CompilationUnit compilationUnit;

    private ProjectFile projectFile;

    public ExistingAssertsCounterResult count(String testType, ProjectFile projectFile) {
        try {
            this.projectFile = projectFile;
            this.result = new ExistingAssertsCounterResult();
            astParser = ASTParser.newParser(AST.JLS3);
            String content;
            if (testType.equals(TestType.CACTUS)) {
                content = TextFileHandler.readText(projectFile.getCactusSourceFileName());
            } else {
                content = TextFileHandler.readText(projectFile.getTestSourceFileName());
            }
            astParser.setSource(content.toCharArray());
            compilationUnit = (CompilationUnit) astParser.createAST(null);
            parseCompilationUnit();
        } catch (Exception e) {
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void parseCompilationUnit() {
        List<TypeDeclaration> types = compilationUnit.types();
        for (TypeDeclaration typeDeclaration : types) {
            if (isTestClassType(projectFile, typeDeclaration)) {
                parseTestClassType(typeDeclaration);
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void parseTestClassType(TypeDeclaration typeDeclaration) {
        List<BodyDeclaration> bodyDeclarations = typeDeclaration.bodyDeclarations();
        for (BodyDeclaration bodyDeclaration : bodyDeclarations) {
            if (bodyDeclaration.getNodeType() == ASTNode.METHOD_DECLARATION) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                parseMethodDeclaration(methodDeclaration);
            }
        }
    }

    protected void parseMethodDeclaration(MethodDeclaration methodDeclaration) {
        MethodBodyModifier.modify(methodDeclaration.getBody(), result);
    }

    private boolean isTestClassType(ProjectFile projectFile, TypeDeclaration typeDeclaration) {
        return !typeDeclaration.isInterface() && typeDeclaration.getName().toString().equals(projectFile.getShortTestClassName());
    }
}
