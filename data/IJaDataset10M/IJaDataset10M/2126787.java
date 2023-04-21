package org.rubypeople.rdt.refactoring.core.mergewithexternalclassparts;

import org.jruby.ast.Node;
import org.rubypeople.rdt.refactoring.core.NodeFactory;
import org.rubypeople.rdt.refactoring.editprovider.InsertEditProvider;
import org.rubypeople.rdt.refactoring.nodewrapper.ClassNodeWrapper;
import org.rubypeople.rdt.refactoring.nodewrapper.PartialClassNodeWrapper;
import org.rubypeople.rdt.refactoring.offsetprovider.AfterLastMethodInClassOffsetProvider;

public class ClassInsertProvider extends InsertEditProvider {

    Node classBody;

    PartialClassNodeWrapper destinationClass;

    public ClassInsertProvider(Node classBody, PartialClassNodeWrapper classNodeWrapper) {
        super(true);
        this.classBody = classBody;
        this.destinationClass = classNodeWrapper;
    }

    @Override
    protected Node getInsertNode(int offset, String document) {
        boolean endNewLine = lastEditInGroup && !isNextLineEmpty(offset, document);
        return NodeFactory.createBlockNode(true, endNewLine, classBody);
    }

    @Override
    protected int getOffset(String document) {
        ClassNodeWrapper destClassWrapper = new ClassNodeWrapper(destinationClass);
        AfterLastMethodInClassOffsetProvider offsetProvider = new AfterLastMethodInClassOffsetProvider(destClassWrapper, document);
        return offsetProvider.getOffset();
    }
}
