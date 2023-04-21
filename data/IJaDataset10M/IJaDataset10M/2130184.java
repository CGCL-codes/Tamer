package de.fraunhofer.isst.axbench.axlang.visitor;

import de.fraunhofer.isst.axbench.axlang.syntaxtree.*;
import java.util.*;

/**
 * All void visitors must implement this interface.
 */
public interface Visitor {

    public void visit(NodeList n);

    public void visit(NodeListOptional n);

    public void visit(NodeOptional n);

    public void visit(NodeSequence n);

    public void visit(NodeToken n);

    /**
    * f0 -> ( Model() | FeatureModel() | Configuration() | ApplicationModel() | Component() | ResourceModel() | F2AMapping() | F2RMapping() | A2RMapping() )
    * f1 -> <EOF>
    */
    public void visit(StartElement n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> ( "." <IDENTIFIER> )*
    */
    public void visit(FeaturePath n);

    /**
    * f0 -> <INCLUDE>
    * f1 -> <FILENAMESTARTDELIMITER>
    * f2 -> <FILENAME>
    * f3 -> <FILENAMEENDDELIMITER>
    * f4 -> <FILENAMEEND>
    */
    public void visit(IncludeFile n);

    /**
    * f0 -> "["
    * f1 -> ( Attribute() )*
    * f2 -> "]"
    */
    public void visit(Attributes n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "="
    * f2 -> ( <NUMBER> | <IDENTIFIER> )
    * f3 -> ";"
    */
    public void visit(Attribute n);

    /**
    * f0 -> <MODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> [ Attributes() ]
    * f3 -> "{"
    * f4 -> ( FeatureModel() | Configuration() | ApplicationModel() | ResourceModel() | F2AMapping() | F2RMapping() | A2RMapping() | TransactionModel() | AspectMapping() | AspectComposition() | IncludeFile() )*
    * f5 -> "}"
    */
    public void visit(Model n);

    /**
    * f0 -> <FEATUREMODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> [ Features() ]
    * f4 -> "}"
    */
    public void visit(FeatureModel n);

    /**
    * f0 -> <FEATURES>
    * f1 -> "{"
    * f2 -> ( AtomicFeature() | FeatureGroup() )+
    * f3 -> "}"
    */
    public void visit(Features n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> [ <XOR> | <OR> Cardinality() ]
    * f2 -> <IDENTIFIER>
    * f3 -> "{"
    * f4 -> ( AtomicFeature() | FeatureGroup() )+
    * f5 -> "}"
    */
    public void visit(FeatureGroup n);

    /**
    * f0 -> "["
    * f1 -> <NUMBER>
    * f2 -> ".."
    * f3 -> ( <NUMBER> | <AST> )
    * f4 -> "]"
    */
    public void visit(Cardinality n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <IDENTIFIER>
    * f2 -> ";"
    */
    public void visit(AtomicFeature n);

    /**
    * f0 -> <CONFIGURATION>
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> "{"
    * f4 -> [ SelectConfiguration() ]
    * f5 -> [ DeSelectConfiguration() ]
    * f6 -> "}"
    */
    public void visit(Configuration n);

    /**
    * f0 -> <YES>
    * f1 -> "{"
    * f2 -> ( FeaturePath() ";" )+
    * f3 -> "}"
    */
    public void visit(SelectConfiguration n);

    /**
    * f0 -> <NO>
    * f1 -> "{"
    * f2 -> ( FeaturePath() ";" )+
    * f3 -> "}"
    */
    public void visit(DeSelectConfiguration n);

    /**
    * f0 -> <APPLICATIONMODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> ( Components() | IncludeFile() )*
    * f4 -> "}"
    */
    public void visit(ApplicationModel n);

    /**
    * f0 -> ( Component() )+
    */
    public void visit(Components n);

    /**
    * f0 -> [ <TOP> ]
    * f1 -> <COMPONENT>
    * f2 -> <IDENTIFIER>
    * f3 -> [ Attributes() ]
    * f4 -> "{"
    * f5 -> [ DataElements() ]
    * f6 -> [ Ports() ]
    * f7 -> [ Storages() ]
    * f8 -> [ Functions() ]
    * f9 -> [ Subcomponents() ]
    * f10 -> [ Connections() ]
    * f11 -> [ Decompositions() ]
    * f12 -> [ Cardinalities() ]
    * f13 -> "}"
    */
    public void visit(Component n);

    /**
    * f0 -> <PORTS>
    * f1 -> "{"
    * f2 -> ( Port() )*
    * f3 -> "}"
    */
    public void visit(Ports n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <DIRECTION>
    * f2 -> [ PortSignature() ]
    * f3 -> <IDENTIFIER>
    * f4 -> [ <BINDINGTIME> ]
    * f5 -> ";"
    */
    public void visit(Port n);

    /**
    * f0 -> "<"
    * f1 -> <IDENTIFIER>
    * f2 -> ( "," <IDENTIFIER> )*
    * f3 -> ">"
    */
    public void visit(PortSignature n);

    /**
    * f0 -> <SUBCOMPONENTS>
    * f1 -> "{"
    * f2 -> ( AtomicSubcomponent() | XORSubcomponent() )*
    * f3 -> "}"
    */
    public void visit(Subcomponents n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> ( "," <IDENTIFIER> )*
    * f4 -> [ <BINDINGTIME> ]
    * f5 -> [ Attributes() ]
    * f6 -> ";"
    */
    public void visit(AtomicSubcomponent n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <XOR>
    * f2 -> <IDENTIFIER>
    * f3 -> [ <BINDINGTIME> ]
    * f4 -> [ Attributes() ]
    * f5 -> "{"
    * f6 -> ( AtomicSubcomponent() )+
    * f7 -> "}"
    */
    public void visit(XORSubcomponent n);

    /**
    * f0 -> <CONNECTIONS>
    * f1 -> "{"
    * f2 -> ( Connection() )*
    * f3 -> "}"
    */
    public void visit(Connections n);

    /**
    * f0 -> [ "\'" <IDENTIFIER> "\'" ]
    * f1 -> ( <IDENTIFIER> | <THIS> )
    * f2 -> "."
    * f3 -> <IDENTIFIER>
    * f4 -> "->"
    * f5 -> ( <IDENTIFIER> | <THIS> )
    * f6 -> "."
    * f7 -> <IDENTIFIER>
    * f8 -> "{"
    * f9 -> DataElementLinks()
    * f10 -> "}"
    */
    public void visit(Connection n);

    /**
    * f0 -> <AST>
    *       | ( DataElementLink() )*
    */
    public void visit(DataElementLinks n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "->"
    * f2 -> <IDENTIFIER>
    * f3 -> ";"
    */
    public void visit(DataElementLink n);

    /**
    * f0 -> <DECOMPOSITION>
    * f1 -> "{"
    * f2 -> ( Decomposition() )*
    * f3 -> "}"
    */
    public void visit(Decompositions n);

    /**
    * f0 -> StorageDecomposition()
    *       | FunctionDecomposition()
    */
    public void visit(Decomposition n);

    /**
    * f0 -> <STORAGES>
    * f1 -> "{"
    * f2 -> ( Storage() )*
    * f3 -> "}"
    */
    public void visit(Storages n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <DATA_TYPE>
    * f2 -> <IDENTIFIER>
    * f3 -> [ "=" ( <NUMBER> | <TRUE> | <FALSE> | "\"" ( <IDENTIFIER> | <NUMBER> | <TRUE> | <FALSE> ) "\"" ) ]
    * f4 -> ";"
    */
    public void visit(Storage n);

    /**
    * f0 -> <STORAGE>
    * f1 -> StorageLink()
    */
    public void visit(StorageDecomposition n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "->"
    * f2 -> LocalStorageInstance()
    * f3 -> ( "," LocalStorageInstance() )*
    * f4 -> ";"
    */
    public void visit(StorageLink n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "."
    * f2 -> <IDENTIFIER>
    */
    public void visit(LocalStorageInstance n);

    /**
    * f0 -> <FUNCTIONS>
    * f1 -> "{"
    * f2 -> ( AtomicFunction() | XORFunction() )*
    * f3 -> "}"
    */
    public void visit(Functions n);

    /**
    * f0 -> <FUNCTION>
    * f1 -> <IDENTIFIER>
    * f2 -> [ Attributes() ]
    * f3 -> "{"
    * f4 -> [ Triggers() ]
    * f5 -> [ Reads() ]
    * f6 -> [ Writes() ]
    * f7 -> [ Behavior() ]
    * f8 -> [ Execute() ]
    * f9 -> "}"
    */
    public void visit(AtomicFunction n);

    /**
    * f0 -> ( <TRIGGER> <IDENTIFIER> "." <IDENTIFIER> ( "," <IDENTIFIER> "." <IDENTIFIER> )* ";" )+
    */
    public void visit(Triggers n);

    /**
    * f0 -> ( <READ> ReadAccess() ( "," ReadAccess() )* ";" )+
    */
    public void visit(Reads n);

    /**
    * f0 -> <IDENTIFIER> "." <IDENTIFIER>
    *       | <IDENTIFIER>
    */
    public void visit(ReadAccess n);

    /**
    * f0 -> ( <WRITE> WriteAccess() ( "," WriteAccess() )* ";" )+
    */
    public void visit(Writes n);

    /**
    * f0 -> <IDENTIFIER> "." <IDENTIFIER>
    *       | <IDENTIFIER>
    */
    public void visit(WriteAccess n);

    /**
    * f0 -> <BEHAVIOR>
    * f1 -> "{"
    * f2 -> [ Purpose() ]
    * f3 -> [ Precondition() ]
    * f4 -> [ Interaction() ]
    * f5 -> [ Postcondition() ]
    * f6 -> "}"
    */
    public void visit(Behavior n);

    /**
    * f0 -> <PURPOSE>
    * f1 -> "{"
    * f2 -> "}"
    */
    public void visit(Purpose n);

    /**
    * f0 -> <PRECONDITION>
    * f1 -> "{"
    * f2 -> "}"
    */
    public void visit(Precondition n);

    /**
    * f0 -> <INTERACTION>
    * f1 -> "{"
    * f2 -> "}"
    */
    public void visit(Interaction n);

    /**
    * f0 -> <INTERACTION>
    * f1 -> "{"
    * f2 -> InteractionStatement()
    * f3 -> "."
    * f4 -> "}"
    */
    public void visit(InteractionXXX n);

    /**
    * f0 -> ( <IDENTIFIER> )*
    */
    public void visit(InteractionStatement n);

    /**
    * f0 -> <POSTCONDITION>
    * f1 -> "{"
    * f2 -> "}"
    */
    public void visit(Postcondition n);

    /**
    * f0 -> <FUNCTION>
    * f1 -> FunctionLink()
    */
    public void visit(FunctionDecomposition n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "->"
    * f2 -> LocalFunctionInstance()
    * f3 -> ( "," LocalFunctionInstance() )*
    * f4 -> ";"
    */
    public void visit(FunctionLink n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "."
    * f2 -> <IDENTIFIER>
    */
    public void visit(LocalFunctionInstance n);

    /**
    * f0 -> <EXECUTE>
    * f1 -> Block()
    */
    public void visit(Execute n);

    /**
    * f0 -> <XOR>
    * f1 -> <FUNCTION>
    * f2 -> <IDENTIFIER>
    * f3 -> [ <BINDINGTIME> ]
    * f4 -> "{"
    * f5 -> ( AtomicFunction() )+
    * f6 -> "}"
    */
    public void visit(XORFunction n);

    /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
    public void visit(Block n);

    /**
    * f0 -> Storage()
    *       | Assignment()
    *       | IfStatement()
    *       | ForStatement()
    *       | WhileStatement()
    *       | DoStatement()
    *       | WritePort()
    *       | OperationCallStatement() ";"
    *       | ReturnStatement()
    */
    public void visit(Statement n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "="
    * f2 -> ( Expression() | OperationCallStatement() )
    * f3 -> ";"
    */
    public void visit(Assignment n);

    /**
    * f0 -> <IF>
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Block()
    * f5 -> [ <ELSE> Block() ]
    */
    public void visit(IfStatement n);

    /**
    * f0 -> <FOR>
    * f1 -> "("
    * f2 -> Storage()
    * f3 -> Expression()
    * f4 -> ";"
    * f5 -> Expression()
    * f6 -> ")"
    * f7 -> Block()
    */
    public void visit(ForStatement n);

    /**
    * f0 -> <WHILE>
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Block()
    */
    public void visit(WhileStatement n);

    /**
    * f0 -> <DO>
    * f1 -> Block()
    * f2 -> <WHILE>
    * f3 -> "("
    * f4 -> Expression()
    * f5 -> ")"
    * f6 -> ";"
    */
    public void visit(DoStatement n);

    /**
    * f0 -> <WRITE>
    * f1 -> "("
    * f2 -> <IDENTIFIER>
    * f3 -> "."
    * f4 -> <IDENTIFIER>
    * f5 -> [ "," Expression() ]
    * f6 -> ")"
    * f7 -> ";"
    */
    public void visit(WritePort n);

    /**
    * f0 -> <CALL>
    * f1 -> "("
    * f2 -> <IDENTIFIER>
    * f3 -> "."
    * f4 -> OperationCallExpression()
    * f5 -> ")"
    */
    public void visit(OperationCallStatement n);

    /**
    * f0 -> <RETURN>
    * f1 -> "("
    * f2 -> <IDENTIFIER>
    * f3 -> "."
    * f4 -> <IDENTIFIER>
    * f5 -> [ "," Expression() ]
    * f6 -> ")"
    * f7 -> ";"
    */
    public void visit(ReturnStatement n);

    /**
    * f0 -> PrefixExpression()
    *       | AtomicOrPostfixOrInfixExpression()
    */
    public void visit(Expression n);

    /**
    * f0 -> ( <NOT> | <INCREMENT> | <DECREMENT> )
    * f1 -> AtomicExpression()
    */
    public void visit(PrefixExpression n);

    /**
    * f0 -> AtomicExpression()
    * f1 -> [ <INCREMENT> | <DECREMENT> | BinaryOperator() Expression() ]
    */
    public void visit(AtomicOrPostfixOrInfixExpression n);

    /**
    * f0 -> ConstantExpression()
    *       | VariableExpression()
    *       | BracketsExpression()
    *       | ReadPort()
    */
    public void visit(AtomicExpression n);

    /**
    * f0 -> <NUMBER>
    *       | <TRUE>
    *       | <FALSE>
    *       | <NULL>
    */
    public void visit(ConstantExpression n);

    /**
    * f0 -> <IDENTIFIER>
    */
    public void visit(VariableExpression n);

    /**
    * f0 -> ArithmeticBinaryOperator()
    *       | BooleanBinaryOperator()
    *       | ComparisonOperator()
    */
    public void visit(BinaryOperator n);

    /**
    * f0 -> <PLUS>
    *       | <MINUS>
    *       | <AST>
    *       | <DIV>
    *       | <MOD>
    */
    public void visit(ArithmeticBinaryOperator n);

    /**
    * f0 -> <ANDOPERATOR>
    *       | <OROPERATOR>
    */
    public void visit(BooleanBinaryOperator n);

    /**
    * f0 -> <EQUAL>
    *       | <NOTEQUAL>
    *       | <LESS>
    *       | <LESSOREQUAL>
    *       | <GREATER>
    *       | <GREATEROREQUAL>
    */
    public void visit(ComparisonOperator n);

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public void visit(BracketsExpression n);

    /**
    * f0 -> <READ>
    * f1 -> "("
    * f2 -> <IDENTIFIER>
    * f3 -> "."
    * f4 -> <IDENTIFIER>
    * f5 -> [ "," <IDENTIFIER> ]
    * f6 -> ")"
    */
    public void visit(ReadPort n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "("
    * f2 -> [ AtomicExpression() ( "," AtomicExpression() )* ]
    * f3 -> ")"
    */
    public void visit(OperationCallExpression n);

    /**
    * f0 -> ( ( Signals() | Operations() ) )+
    */
    public void visit(DataElements n);

    /**
    * f0 -> <SIGNALS>
    * f1 -> "{"
    * f2 -> ( Signal() )*
    * f3 -> "}"
    */
    public void visit(Signals n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <DATA_TYPE>
    * f2 -> <IDENTIFIER>
    * f3 -> ( "," <IDENTIFIER> )*
    * f4 -> [ <BINDINGTIME> ]
    * f5 -> ";"
    */
    public void visit(Signal n);

    /**
    * f0 -> <OPERATIONS>
    * f1 -> "{"
    * f2 -> ( Operation() )*
    * f3 -> "}"
    */
    public void visit(Operations n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <DATA_TYPE>
    * f2 -> <IDENTIFIER>
    * f3 -> "("
    * f4 -> [ <DATA_TYPE> <IDENTIFIER> ( "," <DATA_TYPE> <IDENTIFIER> )* ]
    * f5 -> ")"
    * f6 -> [ <BINDINGTIME> ]
    * f7 -> ";"
    */
    public void visit(Operation n);

    /**
    * f0 -> <CARDINALITIES>
    * f1 -> "{"
    * f2 -> ( CardinalityAssignment() )*
    * f3 -> "}"
    */
    public void visit(Cardinalities n);

    /**
    * f0 -> Cardinality()
    * f1 -> "->"
    * f2 -> LocalInstance()
    * f3 -> ( "," LocalInstance() )*
    * f4 -> ";"
    */
    public void visit(CardinalityAssignment n);

    /**
    * f0 -> <SUBCOMPONENT> <IDENTIFIER>
    *       | <PORT> LocalPortInstance()
    */
    public void visit(LocalInstance n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> "."
    * f2 -> <IDENTIFIER>
    */
    public void visit(LocalPortInstance n);

    /**
    * f0 -> GlobalComponentInstance()
    *       | GlobalConnectionInstance()
    *       | GlobalPortInstance()
    *       | GlobalOperationAtPortInstance()
    *       | GlobalFunctionInstance()
    *       | GlobalSignalAtPortInstance()
    *       | GlobalSubComponentInstance()
    */
    public void visit(GlobalInstanceInAspectRelation n);

    /**
    * f0 -> <COMPONENT>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalComponentInstance n);

    /**
    * f0 -> <CONNECTION>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalConnectionInstance n);

    /**
    * f0 -> <HW_CONNECTION>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalHWConnectionInstance n);

    /**
    * f0 -> <HW_BUS>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalHWBusInstance n);

    /**
    * f0 -> <HW_SUBCOMPONENT>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalHWSubComponentInstance n);

    /**
    * f0 -> <HW_PORT>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalHWPortInstance n);

    /**
    * f0 -> <PORT>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalPortInstance n);

    /**
    * f0 -> <OPERATION>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalOperationInstance n);

    /**
    * f0 -> <OPERATION>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalOperationAtPortInstance n);

    /**
    * f0 -> <STORAGE>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalStorageInstance n);

    /**
    * f0 -> <FUNCTION>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalFunctionInstance n);

    /**
    * f0 -> <FUNCTION>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalFunctionAlternativeInstance n);

    /**
    * f0 -> <SIGNAL>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalSignalInstance n);

    /**
    * f0 -> <SIGNAL>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalSignalAtPortInstance n);

    /**
    * f0 -> <SUBCOMPONENT>
    * f1 -> GlobalInstancePath()
    */
    public void visit(GlobalSubComponentInstance n);

    /**
    * f0 -> <IDENTIFIER>
    * f1 -> ( "." <IDENTIFIER> )*
    */
    public void visit(GlobalInstancePath n);

    /**
    * f0 -> <RESOURCEMODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> ( HWComponent() )*
    * f4 -> "}"
    */
    public void visit(ResourceModel n);

    /**
    * f0 -> [ <TOP> ]
    * f1 -> <HW_COMPONENT>
    * f2 -> <IDENTIFIER>
    * f3 -> [ Attributes() ]
    * f4 -> "{"
    * f5 -> [ HWPorts() ]
    * f6 -> [ HWSubcomponents() ]
    * f7 -> [ HWConnections() ]
    * f8 -> [ HWBusses() ]
    * f9 -> "}"
    */
    public void visit(HWComponent n);

    /**
    * f0 -> <HW_PORTS>
    * f1 -> "{"
    * f2 -> ( HWPort() )+
    * f3 -> "}"
    */
    public void visit(HWPorts n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <DIRECTION>
    * f2 -> "<"
    * f3 -> ( <HW_BUSTYPE> | <ANALOG> | <DIGITAL> )
    * f4 -> ">"
    * f5 -> <IDENTIFIER>
    * f6 -> ( "," <IDENTIFIER> )*
    * f7 -> ";"
    */
    public void visit(HWPort n);

    /**
    * f0 -> <HW_SUBCOMPONENTS>
    * f1 -> "{"
    * f2 -> ( HWAtomicSubcomponent() | HWXORSubcomponent() )+
    * f3 -> "}"
    */
    public void visit(HWSubcomponents n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> ( "," <IDENTIFIER> )*
    * f4 -> ";"
    */
    public void visit(HWAtomicSubcomponent n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <XOR>
    * f2 -> <IDENTIFIER>
    * f3 -> "{"
    * f4 -> ( HWAtomicSubcomponent() )+
    * f5 -> "}"
    */
    public void visit(HWXORSubcomponent n);

    /**
    * f0 -> <HW_CONNECTIONS>
    * f1 -> "{"
    * f2 -> ( HWConnection() )*
    * f3 -> "}"
    */
    public void visit(HWConnections n);

    /**
    * f0 -> [ "\'" <IDENTIFIER> "\'" ]
    * f1 -> [ ( <IDENTIFIER> | <THIS> ) "." ]
    * f2 -> <IDENTIFIER>
    * f3 -> "--"
    * f4 -> ( <IDENTIFIER> | <THIS> )
    * f5 -> "."
    * f6 -> <IDENTIFIER>
    * f7 -> ";"
    */
    public void visit(HWConnection n);

    /**
    * f0 -> <HW_BUSSES>
    * f1 -> "{"
    * f2 -> ( HWBus() )+
    * f3 -> "}"
    */
    public void visit(HWBusses n);

    /**
    * f0 -> <HW_BUSTYPE>
    * f1 -> <IDENTIFIER>
    * f2 -> ";"
    */
    public void visit(HWBus n);

    /**
    * f0 -> <F2A_MAPPING>
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> "->"
    * f4 -> <IDENTIFIER>
    * f5 -> "{"
    * f6 -> ( F2ALink() )*
    * f7 -> "}"
    */
    public void visit(F2AMapping n);

    /**
    * f0 -> <FEATURE>
    * f1 -> FeaturePath()
    * f2 -> "->"
    * f3 -> GlobalInstanceInF2ALink()
    * f4 -> ( "," GlobalInstanceInF2ALink() )*
    * f5 -> ";"
    */
    public void visit(F2ALink n);

    /**
    * f0 -> GlobalOperationInstance()
    *       | GlobalPortInstance()
    *       | GlobalFunctionAlternativeInstance()
    *       | GlobalSignalInstance()
    *       | GlobalStorageInstance()
    *       | GlobalSubComponentInstance()
    */
    public void visit(GlobalInstanceInF2ALink n);

    /**
    * f0 -> <F2R_MAPPING>
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> "->"
    * f4 -> <IDENTIFIER>
    * f5 -> "{"
    * f6 -> ( F2RLink() )*
    * f7 -> "}"
    */
    public void visit(F2RMapping n);

    /**
    * f0 -> <FEATURE>
    * f1 -> FeaturePath()
    * f2 -> "->"
    * f3 -> GlobalInstanceInF2RLink()
    * f4 -> ( "," GlobalInstanceInF2RLink() )*
    * f5 -> ";"
    */
    public void visit(F2RLink n);

    /**
    * f0 -> GlobalHWSubComponentInstance()
    *       | GlobalHWPortInstance()
    */
    public void visit(GlobalInstanceInF2RLink n);

    /**
    * f0 -> <A2R_MAPPING>
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> "->"
    * f4 -> <IDENTIFIER>
    * f5 -> "{"
    * f6 -> ( SC2SC_Link() | P2P_Link() | Con2Con_Link() )*
    * f7 -> "}"
    */
    public void visit(A2RMapping n);

    /**
    * f0 -> GlobalSubComponentInstance()
    * f1 -> "->"
    * f2 -> GlobalHWSubComponentInstance()
    * f3 -> ";"
    */
    public void visit(SC2SC_Link n);

    /**
    * f0 -> GlobalPortInstance()
    * f1 -> "->"
    * f2 -> GlobalHWPortInstance()
    * f3 -> ";"
    */
    public void visit(P2P_Link n);

    /**
    * f0 -> GlobalConnectionInstance()
    * f1 -> "->"
    * f2 -> GlobalHWConnectionInstance()
    * f3 -> ";"
    */
    public void visit(Con2Con_Link n);

    /**
    * f0 -> <TRANSACTIONMODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> <IDENTIFIER>
    * f3 -> "{"
    * f4 -> [ Transactions() ]
    * f5 -> [ Activations() ]
    * f6 -> [ ActivitiesAttributes() ]
    * f7 -> "}"
    */
    public void visit(TransactionModel n);

    /**
    * f0 -> <TRANSACTIONS>
    * f1 -> "{"
    * f2 -> ( Transaction() )*
    * f3 -> "}"
    */
    public void visit(Transactions n);

    /**
    * f0 -> <TRANSACTION>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> [ Activity() ( "," Activity() )* ";" ]
    * f4 -> "}"
    */
    public void visit(Transaction n);

    /**
    * f0 -> ( GlobalFunctionInstance() | GlobalHWSubComponentInstance() | GlobalHWBusInstance() )
    */
    public void visit(Activity n);

    /**
    * f0 -> <ACTIVATIONS>
    * f1 -> "{"
    * f2 -> ( Activation() )*
    * f3 -> "}"
    */
    public void visit(Activations n);

    /**
    * f0 -> ActivationTrigger()
    * f1 -> "{"
    * f2 -> [ <PATTERNLENGTH> <NUMBER> ";" ]
    * f3 -> [ <OFFSET> <NUMBER> [ <UNIT> ] ( "," <NUMBER> )* ";" ]
    * f4 -> [ <PERIOD> <NUMBER> <UNIT> ";" ]
    * f5 -> [ <MININTERARRIVALTIME> <NUMBER> <UNIT> ";" ]
    * f6 -> [ <MAXINTERARRIVALTIME> <NUMBER> <UNIT> ";" ]
    * f7 -> [ <JITTER> <NUMBER> <UNIT> ";" ]
    * f8 -> "}"
    */
    public void visit(Activation n);

    /**
    * f0 -> GlobalFunctionInstance() ":" <IDENTIFIER> "." <IDENTIFIER> <ACTIVATIONKIND>
    *       | ( GlobalHWSubComponentInstance() | GlobalHWBusInstance() ) <ACTIVATIONKIND>
    */
    public void visit(ActivationTrigger n);

    /**
    * f0 -> <ACTIVITIESATTRIBUTES>
    * f1 -> "{"
    * f2 -> ( ActivityAttribute() )*
    * f3 -> "}"
    */
    public void visit(ActivitiesAttributes n);

    /**
    * f0 -> ( GlobalFunctionInstance() | GlobalHWSubComponentInstance() | GlobalHWBusInstance() )
    * f1 -> "{"
    * f2 -> [ <PRIORITY> <NUMBER> ";" ]
    * f3 -> [ <WCET> <NUMBER> <UNIT> ";" ]
    * f4 -> [ <JITTER> <NUMBER> <UNIT> ";" ]
    * f5 -> [ <DEADLINE> <NUMBER> <UNIT> ";" ]
    * f6 -> "}"
    */
    public void visit(ActivityAttribute n);

    /**
    * f0 -> <ASPECT_MAPPING>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> ( AspectRelationCopy() | AspectRelationIdentity() | AspectRelationInner() | AspectRelationReplace() )*
    * f4 -> "}"
    */
    public void visit(AspectMapping n);

    /**
    * f0 -> <COPY>
    * f1 -> AspectRelationLink()
    * f2 -> ";"
    */
    public void visit(AspectRelationCopy n);

    /**
    * f0 -> <IDENTITY>
    * f1 -> AspectRelationLink()
    * f2 -> ";"
    */
    public void visit(AspectRelationIdentity n);

    /**
    * f0 -> <INNER>
    * f1 -> AspectRelationLink()
    * f2 -> ";"
    */
    public void visit(AspectRelationInner n);

    /**
    * f0 -> <REPLACE>
    * f1 -> AspectRelationLink()
    * f2 -> ";"
    */
    public void visit(AspectRelationReplace n);

    /**
    * f0 -> GlobalInstanceInAspectRelation()
    * f1 -> "->"
    * f2 -> GlobalInstanceInAspectRelation()
    * f3 -> ( "," GlobalInstanceInAspectRelation() )*
    */
    public void visit(AspectRelationLink n);

    /**
    * f0 -> <ASPECT_COMPOSITION>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> ( AspectCompositionLinkApplication() | AspectCompositionLinkAspectMapping() )*
    * f4 -> "}"
    */
    public void visit(AspectComposition n);

    /**
    * f0 -> <APPLICATIONMODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> ";"
    */
    public void visit(AspectCompositionLinkApplication n);

    /**
    * f0 -> <ASPECT_MAPPING>
    * f1 -> <IDENTIFIER>
    * f2 -> ";"
    */
    public void visit(AspectCompositionLinkAspectMapping n);
}
