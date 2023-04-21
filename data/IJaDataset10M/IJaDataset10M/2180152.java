package cz.cuni.mff.ksi.jinfer.base.objects.xquery.types;

import cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.Type;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.AtomicTypeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Cardinality;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.ItemTypeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.KindTestNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.TypeNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;
import org.apache.log4j.Logger;

/**
 * Abstract base class of all implementation classes representing types.
 * 
 * It provides default implementation of methods from {@link Type} interface and
 * some utility static methods working with types.
 * Every implementation class has to override a methods that does not suit.
 * 
 * @author rio
 */
public abstract class AbstractType implements Type {

    private static final Logger LOG = Logger.getLogger(AbstractType.class);

    @Override
    public boolean isNodeType() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isPathType() {
        return false;
    }

    @Override
    public boolean isUnknownType() {
        return false;
    }

    @Override
    public boolean isXsdBuiltinType() {
        return false;
    }

    /**
   * Creates an instance from the specified {@link TypeNode}. Its purpose is to
   * convert TypeNodes from the syntax trees to out representation of types.
   * 
   * @param typeNode TypeNode to create an instance of Type.
   * @return An instance of Type.
   */
    public static Type createType(final TypeNode typeNode) {
        final ItemTypeNode itemTypeNode = typeNode.getItemTypeNode();
        if (itemTypeNode == null) {
            return new UnknownType();
        } else {
            final Cardinality cardinality = typeNode.getCardinality();
            if (AtomicTypeNode.class.isInstance(itemTypeNode)) {
                final AtomicTypeNode itomicTypeNode = (AtomicTypeNode) itemTypeNode;
                final String typeName = itomicTypeNode.getTypeName();
                return new XSDType(typeName, cardinality);
            } else if (KindTestNode.class.isInstance(itemTypeNode)) {
                LOG.warn("Method \"createType\" does not implement KindTestNodes.");
                return new UnknownType();
            } else {
                LOG.warn("Method \"createType\" does not implement AnyItemNodes nor NameTestNode.");
                return new UnknownType();
            }
        }
    }

    /**
   * When a type is bound to a variable in a for clause, its semantic is slightly
   * changed. This method provides the conversion from a type to a type bound
   * in a for clause.
   * 
   * @param forBindingExprType A type of expression in a binding for clause.
   * @return A type of the for bound variable.
   */
    public static Type createForBoundType(final Type forBindingExprType) {
        switch(forBindingExprType.getCategory()) {
            case UNKNOWN:
                return new UnknownType();
            case PATH:
                {
                    final PathType pathType = (PathType) forBindingExprType;
                    return new PathType(pathType.getSteps(), pathType.getInitialStep(), pathType.getSubpaths(), true);
                }
            case XSD_BUILT_IN:
                {
                    assert (((XSDType) forBindingExprType).getCardinality() != Cardinality.ONE);
                    assert (((XSDType) forBindingExprType).getCardinality() != Cardinality.ZERO);
                    final XSDType xsdType = (XSDType) forBindingExprType;
                    final XSDBuiltinAtomicType atomicType = xsdType.getAtomicType();
                    return new XSDType(atomicType, Cardinality.ONE);
                }
            case NODE:
                {
                    final NodeType nodeType = (NodeType) forBindingExprType;
                    return new NodeType(nodeType.getNodeTypeCategory(), Cardinality.ONE);
                }
            default:
                throw new IllegalStateException();
        }
    }

    /**
   * Does the reverse operation as {@link #createForBoundType(cz.cuni.mff.ksi.jinfer.base.interfaces.xquery.xqueryprocessor.Type)}.
   * @param returnClauseType A type of the return clause to perform the type unbind.
   * @return An unbound type.
   */
    public static Type createForUnboundType(final Type returnClauseType) {
        switch(returnClauseType.getCategory()) {
            case UNKNOWN:
                return new UnknownType();
            case PATH:
                {
                    final PathType pathType = (PathType) returnClauseType;
                    return new PathType(pathType.getSteps(), pathType.getInitialStep(), pathType.getSubpaths(), false);
                }
            case XSD_BUILT_IN:
                {
                    final XSDType xsdType = (XSDType) returnClauseType;
                    final XSDBuiltinAtomicType atomicType = xsdType.getAtomicType();
                    return new XSDType(atomicType, Cardinality.ZERO_OR_MORE);
                }
            case NODE:
                {
                    final NodeType nodeType = (NodeType) returnClauseType;
                    return new NodeType(nodeType.getNodeTypeCategory(), Cardinality.ZERO_OR_MORE);
                }
            default:
                throw new IllegalStateException();
        }
    }
}
