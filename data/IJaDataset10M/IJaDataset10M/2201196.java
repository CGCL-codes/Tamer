package com.testrefactoring.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.dom.ASTNode;
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
import com.testrefactoring.interactionDiagramModel.SVGDiagramFactory;

public class ExportDiagramToSvg implements IJavaCompletionProposal {

    private final TagElement source;

    private static IPath path;

    private final TypeDeclaration typeDec;

    public static IJavaCompletionProposal get(IPath path, TypeDeclaration typeDef, ASTNode coveringNode) {
        ExportDiagramToSvg.path = path;
        if (TestRefactoringQuickFixProvider.getNodeType(ASTNode.JAVADOC, coveringNode) != null) {
            TagElement previous = UpdateRequirementsProposal.getExistingTagByName(typeDef, UpdateDiagram.INTERACTION_TAG);
            if (previous != null) {
                return new ExportDiagramToSvg(typeDef, previous);
            }
        }
        return null;
    }

    public ExportDiagramToSvg(TypeDeclaration typeDec, TagElement source) {
        this.typeDec = typeDec;
        this.source = source;
    }

    public int getRelevance() {
        return 3;
    }

    public void apply(IDocument document) {
        final ASTRewrite rewriter = ASTRewrite.create(source.getAST());
        try {
            updateDiagram(rewriter);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        final TextEdit edits = rewriter.rewriteAST(document, null);
        try {
            edits.apply(document);
        } catch (final MalformedTreeException e) {
            e.printStackTrace();
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void updateDiagram(ASTRewrite rewriter) throws IOException {
        List messages = UpdateDiagram.getMessages(source);
        String header = UpdateDiagram.getHeader(typeDec);
        String diagram = new SVGDiagramFactory().createDiagram(UpdateDiagram.getTitle(source), "", header, messages);
        saveStringToFile(diagram);
    }

    private void saveStringToFile(String diagram) throws IOException {
        FileWriter fileWriter = null;
        try {
            IPath ourPath = path.addFileExtension(".svg");
            Resource newResource = ((Workspace) ResourcesPlugin.getWorkspace()).newResource(ourPath, IResource.FILE);
            String location = newResource.getRawLocation().toOSString();
            fileWriter = new FileWriter(location);
            fileWriter.append(diagram);
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
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
        return "Export diagram to svg";
    }

    public Image getImage() {
        return null;
    }

    public Point getSelection(IDocument document) {
        return null;
    }
}
