package org.rubypeople.rdt.refactoring.core.extractconstant;

import org.jruby.ast.Node;
import org.rubypeople.rdt.core.RubyConventions;
import org.rubypeople.rdt.internal.core.util.ASTUtil;
import org.rubypeople.rdt.refactoring.core.IRefactoringConfig;
import org.rubypeople.rdt.refactoring.core.NodeFactory;
import org.rubypeople.rdt.refactoring.core.SelectionInformation;
import org.rubypeople.rdt.refactoring.core.SelectionNodeProvider;
import org.rubypeople.rdt.refactoring.documentprovider.DocumentProvider;
import org.rubypeople.rdt.refactoring.documentprovider.IDocumentProvider;

public class ExtractConstantConfig implements IRefactoringConfig {

    private static final String DEFAULT_CONSTANT_NAME = "CONSTANT";

    private IDocumentProvider docProvider;

    private SelectionInformation selectionInfo;

    private Node selectedNodes;

    private Node rootNode;

    private String constName = DEFAULT_CONSTANT_NAME;

    public ExtractConstantConfig(DocumentProvider docProvider, SelectionInformation selectionInfo) {
        this.docProvider = docProvider;
        this.selectionInfo = optimizeSelection(selectionInfo);
    }

    private SelectionInformation optimizeSelection(SelectionInformation selectionInfo) {
        int start = selectionInfo.getStartOfSelection();
        int end = selectionInfo.getEndOfSelection() + 1;
        String selectedText = docProvider.getActiveFileContent().substring(start, end);
        String trimedSelectionInformation = selectedText.trim();
        start += selectedText.indexOf(trimedSelectionInformation);
        end = start + trimedSelectionInformation.length() - 1;
        return new SelectionInformation(start, end, selectionInfo.getSource());
    }

    public IDocumentProvider getDocumentProvider() {
        return docProvider;
    }

    public SelectionInformation getSelection() {
        return selectionInfo;
    }

    public Node getSelectedNodes() {
        return selectedNodes;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Node getConstantCallNode() {
        return NodeFactory.createConstNode(constName);
    }

    public void setConstantName(String name) {
        constName = name;
    }

    public Node getConstantDeclNode() {
        return NodeFactory.createConstDeclNode(constName, selectedNodes);
    }

    public String getConstantName() {
        return constName;
    }

    public void init() {
        rootNode = getDocumentProvider().getActiveFileRootNode();
        selectedNodes = SelectionNodeProvider.getSelectedNodes(rootNode, getSelection());
        constName = extractConstantName(selectedNodes);
    }

    private String extractConstantName(Node node) {
        String name = ASTUtil.stringRepresentation(node);
        name = trim(name);
        if (RubyConventions.validateConstant(name).isOK()) return name;
        return DEFAULT_CONSTANT_NAME;
    }

    private String trim(String name) {
        name = name.trim();
        name = name.toUpperCase();
        name = name.replace(' ', '_');
        while (true) {
            if (name.length() == 0) break;
            char c = name.charAt(0);
            if (!(Character.isUpperCase(c) && Character.isLetter(c))) {
                name = name.substring(1);
            } else {
                break;
            }
        }
        while (true) {
            if (name.length() == 0) break;
            char c = name.charAt(name.length() - 1);
            if (!Character.isLetter(c) && c != '_') {
                name = name.substring(0, name.length() - 1);
            } else {
                break;
            }
        }
        return name;
    }

    public void setDocumentProvider(IDocumentProvider doc) {
        this.docProvider = doc;
    }
}
