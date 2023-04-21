package org.deft.operation.astdecoration;

import org.deft.artifacttypesupport.codefile.LanguageSpecifics;
import org.deft.artifacttypesupport.codefile.PlainCodeFileArtifactType;
import org.deft.operation.EmptyOperationConfiguration;
import org.deft.operation.Operation;
import org.deft.operation.OperationConfiguration;
import org.deft.operation.exception.DeftInvalidOperationChainException;
import org.deft.repository.artifacttype.ArtifactType;
import org.deft.repository.ast.TreeNodeRoot;
import org.deft.repository.ast.decoration.DecoratorSelection;
import org.deft.repository.ast.decoration.TreeNodeDecorationApplier;
import org.deft.repository.datamodel.ArtifactRepresentation;
import org.deft.representation.treenode.TreeNodeRepresentation;

public class AstDecorationOperation implements Operation {

    private ArtifactType type;

    @Override
    public TreeNodeRepresentation executeOperation(ArtifactRepresentation representation, ArtifactType type, OperationConfiguration config) {
        this.type = type;
        TreeNodeRoot root = checkAndUnwrap(representation);
        decorateAst(root);
        TreeNodeRepresentation tr = new TreeNodeRepresentation(root);
        return tr;
    }

    private TreeNodeRoot checkAndUnwrap(ArtifactRepresentation rep) {
        if (rep.getType().equals(TreeNodeRepresentation.TYPE)) {
            TreeNodeRepresentation tnr = (TreeNodeRepresentation) rep;
            return tnr.getRoot();
        } else {
            throw new DeftInvalidOperationChainException();
        }
    }

    private void decorateAst(TreeNodeRoot root) {
        if (root.hasChildren()) {
            DecoratorSelection decorators = getDecorators();
            TreeNodeDecorationApplier applier = new TreeNodeDecorationApplier(root);
            applier.applyAstDecorators(decorators);
        }
    }

    private DecoratorSelection getDecorators() {
        DecoratorSelection decorators = new DecoratorSelection();
        if (type instanceof PlainCodeFileArtifactType) {
            PlainCodeFileArtifactType codeType = (PlainCodeFileArtifactType) type;
            LanguageSpecifics ls = codeType.getLanguageSpecifics();
            decorators.addDecorator(ls.getTokenTypeDecorator());
        }
        return decorators;
    }

    @Override
    public OperationConfiguration createConfiguration() {
        return new EmptyOperationConfiguration();
    }

    @Override
    public String getInputRepresentationType() {
        return TreeNodeRepresentation.TYPE;
    }

    @Override
    public String getResultingRepresentationType() {
        return TreeNodeRepresentation.TYPE;
    }
}
