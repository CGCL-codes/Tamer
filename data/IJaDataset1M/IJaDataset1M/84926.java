package org.genxdm.xs.types;

import org.genxdm.xs.components.AttributeDefinition;
import org.genxdm.xs.components.ElementDefinition;

/**
 * Visitor pattern for {@link SequenceType}.
 * 
 */
public interface SequenceTypeVisitor {

    void visit(AttributeDefinition schemaAttribute);

    void visit(AttributeNodeType attributeType);

    void visit(ChoiceType choiceType);

    void visit(CommentNodeType commentNodeType);

    void visit(ComplexType atomicType);

    void visit(ComplexUrType complexUrType);

    void visit(ConcatType concatType);

    void visit(DocumentNodeType documentNodeType);

    void visit(ElementDefinition schemaElement);

    void visit(ElementNodeType elementNodeType);

    void visit(EmptyType emptyType);

    void visit(InterleaveType interleaveType);

    void visit(ListSimpleType atomicType);

    void visit(MultiplyType multiplyType);

    void visit(NamespaceNodeType namespaceNodeType);

    void visit(NodeUrType nodeType);

    void visit(NoneType noneType);

    void visit(ProcessingInstructionNodeType processingInstructionNodeType);

    void visit(SimpleType simpleType);

    void visit(SimpleUrType simpleUrType);

    void visit(TextNodeType textNodeType);

    void visit(UnionSimpleType unionType);
}
