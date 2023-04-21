package org.nexopenframework.ide.eclipse.jee.generators;

import java.util.List;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.nexopenframework.ide.eclipse.jee.wizards.JeeLifecycleWizard;
import org.nexopenframework.ide.eclipse.ui.util.ServiceComponentUtil;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>
 * generates a JEE 5.0 lifecycle methods with related JSR-250 metadata  in a Service Component
 * </p>
 * 
 * @see WizardGenerator
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class JeeLifecycleGenerator extends WizardGenerator {

    /**
	 * <p>Checks if it is a Business Service component (Facade or Application Service)</p>
	 * 
	 * @see org.nexopenframework.ide.eclipse.jee.generators.WizardGenerator#asserType(org.eclipse.jdt.core.IType)
	 */
    protected void asserType(final IType type) throws JavaModelException, IllegalStateException {
        if (!ServiceComponentUtil.isBusinessImplementor(type)) {
            throw new IllegalStateException("Not a Business Service Component");
        }
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.jee.generators.WizardGenerator#handleWizardGeneration(org.eclipse.jface.wizard.Wizard, org.eclipse.swt.widgets.Shell, org.eclipse.jdt.core.ICompilationUnit, org.eclipse.jdt.core.dom.CompilationUnit)
	 */
    protected void handleWizardGeneration(final Wizard _wizard, final Shell shell, final ICompilationUnit cu, final CompilationUnit cunit) throws JavaModelException, IllegalStateException {
        final IImportDeclaration importDeclarations[] = cu.getImports();
        final JeeLifecycleWizard wizard = (JeeLifecycleWizard) _wizard;
        final String fname = wizard.getPage().getMethodName();
        final String fmodifier = wizard.getPage().getModifier();
        final String flifecycle = wizard.getPage().getOperation();
        final AST ast = cunit.getAST();
        if (!ServiceComponentUtil.isSimpleClassNamePresent(importDeclarations, "PostConstruct") && "PostConstruct".equals(flifecycle)) {
            ImportDeclaration importDecl = ast.newImportDeclaration();
            importDecl.setName(ast.newName(new String[] { "javax", "annotation", "PostConstruct" }));
            importDecl.setOnDemand(false);
            cunit.imports().add(importDecl);
        }
        if (!ServiceComponentUtil.isSimpleClassNamePresent(importDeclarations, "PreDestroy") && "PreDestroy".equals(flifecycle)) {
            ImportDeclaration importDecl = ast.newImportDeclaration();
            importDecl.setStatic(false);
            importDecl.setName(ast.newName(new String[] { "javax", "annotation", "PreDestroy" }));
            importDecl.setOnDemand(false);
            cunit.imports().add(importDecl);
        }
        final MethodDeclaration md = ast.newMethodDeclaration();
        md.setName(ast.newSimpleName(fname));
        final Javadoc jdoc = ast.newJavadoc();
        final TagElement tag = ast.newTagElement();
        final TextElement element = ast.newTextElement();
        element.setText("<p>The method on which the PostConstruct/PreDestroy annotation is applied MUST \n" + "fulfill all of the following criteria:</p> \n <ul>" + "<li>The method MUST NOT have any parameters except in the case of EJB interceptors \n" + "in which case it takes an InvocationContext " + "object as defined by the EJB specification.</li> \n" + "<li>The return type of the method MUST be void</li>. \n" + "<li>The method MUST NOT throw a checked exception.</li> \n" + "<li>The method on which is applied MAY be public, protected, package private or private.</li> \n" + "<li>The method MUST NOT be static.</li> \n" + "<li>The method MAY be final.</li> \n" + "<li>If the method throws an unchecked exception it is ignored except " + "in the case of EJBs where the EJB can handle exceptions.</li> \n </ul>");
        tag.fragments().add(element);
        jdoc.tags().add(tag);
        md.setJavadoc(jdoc);
        md.setBody(ast.newBlock());
        if (!fmodifier.equals("")) {
            if (fmodifier.equals("public")) {
                md.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
            } else if (fmodifier.equals("private")) {
                md.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
            } else if (fmodifier.equals("protected")) {
                md.modifiers().add(ast.newModifier(ModifierKeyword.PROTECTED_KEYWORD));
            }
        }
        final NormalAnnotation annotation = ast.newNormalAnnotation();
        annotation.setTypeName(ast.newSimpleName(flifecycle));
        md.modifiers().add(0, annotation);
        List types = cunit.types();
        TypeDeclaration type = (TypeDeclaration) types.get(0);
        type.bodyDeclarations().add(md);
    }

    protected Wizard newWizard(IType type) {
        return new JeeLifecycleWizard(type);
    }
}
