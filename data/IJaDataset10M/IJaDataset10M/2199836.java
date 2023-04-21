package org.tzi.ugt.model;

import java.util.List;
import org.tzi.ugt.main.Exception;
import org.tzi.ugt.model.expression.AbstractExpressionFactory;
import org.tzi.ugt.model.expression.Constant;
import org.tzi.ugt.model.expression.Expression;
import org.tzi.ugt.model.expression.ExpressionFactory;
import org.tzi.ugt.model.expression.OCLExpression;
import org.tzi.ugt.model.expression.Parameter;
import org.tzi.ugt.model.expression.Variable;
import org.tzi.ugt.model.uml.AbstractUMLFactory;
import org.tzi.ugt.model.uml.Association;
import org.tzi.ugt.model.uml.AssociationEnd;
import org.tzi.ugt.model.uml.AssociationRole;
import org.tzi.ugt.model.uml.AssociationRole.Enum;
import org.tzi.ugt.model.uml.AssociationRoleEnd;
import org.tzi.ugt.model.uml.Attribute;
import org.tzi.ugt.model.uml.AttributeLink;
import org.tzi.ugt.model.uml.Class;
import org.tzi.ugt.model.uml.Link;
import org.tzi.ugt.model.uml.LinkEnd;
import org.tzi.ugt.model.uml.Object;
import org.tzi.ugt.model.uml.Operation;
import org.tzi.ugt.model.uml.State;
import org.tzi.ugt.model.uml.Transition;
import org.tzi.ugt.model.uml.UMLFactory;
import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.uml.mm.MAssociationEnd;
import org.tzi.use.uml.mm.MAttribute;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MModel;
import org.tzi.use.uml.mm.MMultiplicity;
import org.tzi.use.uml.mm.MOperation;
import org.tzi.use.uml.ocl.expr.VarDeclList;
import org.tzi.use.uml.ocl.type.Type;

/**
 * Localizes the construction of the object for the UGT data model.
 * 
 * @author lschaps
 */
public class ModelFactory extends org.tzi.use.uml.mm.ModelFactory implements AbstractModelFactory {

    private AbstractExpressionFactory m_ExpressionFactory = new ExpressionFactory();

    private AbstractUMLFactory m_UMLFactory = new UMLFactory();

    public MAssociation createAssociation(String name) {
        return m_UMLFactory.createAssociation(name);
    }

    public MAssociationEnd createAssociationEnd(MClass cls, String rolename, MMultiplicity mult, int kind, boolean isOrdered) {
        return m_UMLFactory.createAssociationEnd(cls, rolename, mult, kind, isOrdered);
    }

    public AssociationRole createAssociationRole(String in_Name, Enum in_Kind, Association in_Assoc) {
        return m_UMLFactory.createAssociationRole(in_Name, in_Kind, in_Assoc);
    }

    public AssociationRoleEnd createAssociationRoleEnd(AssociationRole in_AssociationRole, AssociationEnd in_AssocEnd, Object in_Object, String in_Role) {
        return m_UMLFactory.createAssociationRoleEnd(in_AssociationRole, in_AssocEnd, in_Object, in_Role);
    }

    public MAttribute createAttribute(String name, Type t) {
        return m_UMLFactory.createAttribute(name, t);
    }

    public AttributeLink createAttributeLink(Object in_Object, Attribute in_Attrib, Expression in_Expr) {
        return m_UMLFactory.createAttributeLink(in_Object, in_Attrib, in_Expr);
    }

    public MClass createClass(String name, boolean isAbstract) {
        return m_UMLFactory.createClass(name, isAbstract);
    }

    public ClassDiagram createClassDiagram(String in_Name, MModel in_Model) {
        return new ClassDiagram(in_Name, in_Model);
    }

    public CollaborationDiagram createCollaborationDiagram(UseCase in_UseCase, Model in_Model) {
        return new CollaborationDiagram(in_UseCase, in_Model);
    }

    public CollaborationDiagram createCollaborationDiagram(Operation op, Model in_Model) {
        return new CollaborationDiagram(op, in_Model);
    }

    public Constant createConstant(String in_Value, Constant.Type in_Type) {
        return m_ExpressionFactory.createConstant(in_Value, in_Type);
    }

    public Link createLink(String in_Name, Association in_Association) {
        return m_UMLFactory.createLink(in_Name, in_Association);
    }

    public LinkEnd createLinkEnd(AssociationEnd in_AssocEnd, Link in_Link, Object in_Object) {
        return m_UMLFactory.createLinkEnd(in_AssocEnd, in_Link, in_Object);
    }

    public Message createMessage(Object in_Sender, Object in_Receiver, String in_SeqNo, String in_CondClause, String in_Assign, Process in_Process, AssociationRole in_AssociationRole) {
        return new Message(in_Sender, in_Receiver, in_SeqNo, in_CondClause, in_Assign, in_Process, in_AssociationRole);
    }

    public Object createObject(String in_Name, Class in_Class, State in_State) {
        return m_UMLFactory.createObject(in_Name, in_Class, in_State);
    }

    public ObjectDiagram createObjectDiagram(String in_Name) {
        return new ObjectDiagram(in_Name);
    }

    public OCLExpression createOCLExpression(String in_Expression) {
        return m_ExpressionFactory.createOCLExpression(in_Expression);
    }

    public MOperation createOperation(String name, VarDeclList varDeclList, Type resultType) {
        return m_UMLFactory.createOperation(name, varDeclList, resultType);
    }

    public Parameter createParameter(String in_Name, String in_Type, Expression in_Value) {
        return m_ExpressionFactory.createParameter(in_Name, in_Type, in_Value);
    }

    public Process createProcess(Process.Enum in_Type, Operation in_Operation, List in_Parameter) {
        return new Process(in_Type, in_Operation, in_Parameter);
    }

    public State createState(String in_Name, boolean in_Start, boolean in_Final) {
        return m_UMLFactory.createState(in_Name, in_Start, in_Final);
    }

    public StatechartDiagram createStatechartDiagram(Class in_Class) {
        return new StatechartDiagram(in_Class);
    }

    public Transition createTransition(State in_Source, State in_Target, Operation in_Operation, String in_OCLCond) throws Exception {
        return m_UMLFactory.createTransition(in_Source, in_Target, in_Operation, in_OCLCond);
    }

    public Model createUGTModel(String in_Name) {
        return new Model(in_Name);
    }

    public UseCase createUseCase(Operation in_Operation) {
        return new UseCase(in_Operation);
    }

    public UseCaseDiagram createUseCaseDiagram(String in_Name) {
        return new UseCaseDiagram(in_Name);
    }

    public Variable createVariable(String in_Name) {
        return m_ExpressionFactory.createVariable(in_Name);
    }
}
