package com.testrefactoring.ui;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class RenameTest implements IJavaCompletionProposal {

    private final TypeDeclaration typeDef;

    private final List from;

    public RenameTest(TypeDeclaration typeDef, List from) {
        this.typeDef = typeDef;
        this.from = from;
    }

    private String[] methodName(String from) {
        return from.split("\\{\\@link\\s" + typeDef.getName().getIdentifier() + "\\#");
    }

    public static IJavaCompletionProposal get(TypeDeclaration typeDef) {
        Javadoc javadoc = typeDef.getJavadoc();
        if (!TestRefactoringQuickFixProvider.isTestCase(typeDef) || (javadoc == null)) {
            return null;
        }
        TagElement testsTag = getTestsTag(javadoc);
        if (testsTag == null) {
            return null;
        }
        List hrExistingRequirements = Requirements.getHumanReadableRequirements(typeDef);
        List hrRequirements = Requirements.getHumanReadableRequirements(testsTag);
        hrRequirements.removeAll(hrExistingRequirements);
        if (hrRequirements.isEmpty()) return null;
        return new RenameTest(typeDef, hrRequirements);
    }

    private static TagElement getTestsTag(Javadoc javadoc) {
        Iterator i = javadoc.tags().iterator();
        while (i.hasNext()) {
            TagElement tag = (TagElement) i.next();
            if (Requirements.TESTS_TAG_NAME.equals(tag.getTagName())) {
                return tag;
            }
        }
        return null;
    }

    public int getRelevance() {
        return 11;
    }

    public void apply(IDocument document) {
        final ASTRewrite rewriter = ASTRewrite.create(typeDef.getAST());
        renameRequirement(rewriter);
        final TextEdit edits = rewriter.rewriteAST(document, null);
        try {
            edits.apply(document);
        } catch (final MalformedTreeException e) {
            e.printStackTrace();
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void renameRequirement(ASTRewrite rewriter) {
        Iterator i = this.from.iterator();
        while (i.hasNext()) {
            String newName = (String) i.next();
            renameRequirement(rewriter, newName);
        }
    }

    private void renameRequirement(ASTRewrite rewriter, String from) {
        String[] methodName = methodName(from);
        String find = methodName[1].substring(0, methodName[1].length() - 1);
        MethodDeclaration[] methods = typeDef.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (find.equals(methods[i].getName().getIdentifier())) {
                rewriter.replace(methods[i].getName(), typeDef.getAST().newSimpleName(Requirements.toMachineReadableRepresentation(methodName[0])), null);
                return;
            }
        }
    }

    public String getAdditionalProposalInfo() {
        return null;
    }

    public IContextInformation getContextInformation() {
        return null;
    }

    public String getDisplayString() {
        return "Rename " + from;
    }

    public Image getImage() {
        return null;
    }

    public Point getSelection(IDocument document) {
        return null;
    }
}
