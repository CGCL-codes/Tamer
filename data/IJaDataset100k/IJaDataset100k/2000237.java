package de.axbench.axlang.text.visitor;

import de.axbench.axlang.text.syntaxtree.*;
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
    * f0 -> ( Model() )
    * f1 -> <EOF>
    */
    public void visit(StartElement n);

    /**
    * f0 -> <MODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> [ Attributes() ]
    * f3 -> "{"
    * f4 -> ( ApplicationModel() )*
    * f5 -> ( ResourceModel() )*
    * f6 -> "}"
    */
    public void visit(Model n);

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
    * f0 -> <APPLICATIONMODEL>
    * f1 -> <IDENTIFIER>
    * f2 -> "{"
    * f3 -> ( Components() )*
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
    * f7 -> [ Subcomponents() ]
    * f8 -> [ Connections() ]
    * f9 -> "}"
    */
    public void visit(Component n);

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
    * f4 -> ";"
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
    * f6 -> ";"
    */
    public void visit(Operation n);

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
    * f4 -> [ Attributes() ]
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
    * f4 -> [ Attributes() ]
    * f5 -> ";"
    */
    public void visit(AtomicSubcomponent n);

    /**
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <XOR>
    * f2 -> <IDENTIFIER>
    * f3 -> [ Attributes() ]
    * f4 -> "{"
    * f5 -> ( AtomicSubcomponent() )+
    * f6 -> "}"
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
    * f7 -> [ HWBusses() ]
    * f8 -> [ HWConnections() ]
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
    * f7 -> [ Attributes() ]
    * f8 -> ";"
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
    * f4 -> [ Attributes() ]
    * f5 -> ";"
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
    * f0 -> [ <OPTIONAL> ]
    * f1 -> <HW_BUSTYPE>
    * f2 -> <IDENTIFIER>
    * f3 -> ";"
    */
    public void visit(HWBus n);
}
