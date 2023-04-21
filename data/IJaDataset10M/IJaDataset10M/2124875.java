package Visitor;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import Sintactico.Arbol.*;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author Javier
 */
public class imprimir_visitor implements visitor {

    public void imprimir(AST raiz) {
        if (raiz != null) {
            DefaultMutableTreeNode root;
            root = new DefaultMutableTreeNode("AST");
            root.add((MutableTreeNode) raiz.visit(this));
            final DefaultTreeModel model = new DefaultTreeModel(root);
            final JTree tree = new JTree(model);
            JPanel addPanel = new JPanel(new GridLayout(2, 1));
            tree.addTreeSelectionListener(new TreeSelectionListener() {

                public void valueChanged(TreeSelectionEvent e) {
                    TreePath tp = e.getNewLeadSelectionPath();
                }
            });
            JFrame frame = new JFrame("Arbol de Sintaxis Abstracta");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            frame.getContentPane().add(new JScrollPane(tree));
            frame.getContentPane().add(addPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        } else {
            System.out.println("No se puede imprimir el arbol debido a que no existen nodos");
            JOptionPane.showMessageDialog(null, "No se puede imprimir el arbol debido a que no existen nodos", "Imposible imprimir", 1);
        }
    }

    public Object visitBodyDecl_Lista(AST_BodyDecl_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_BodyDecl.visit(this));
        v.add((MutableTreeNode) N.sig.visit(this));
        return v;
    }

    public Object visitClassDecl_Lista(AST_ClassDecl_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N.visit(this));
        v.add((MutableTreeNode) N.extN.visit(this));
        return v;
    }

    public Object visitClassDecl_Simple(AST_ClassDecl_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_BodyDecl.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitClassDecl_Simple_E(AST_ClassDecl_Simple_E N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_BodyDecl.visit(this));
        v.add(new DefaultMutableTreeNode("Extends-id -> " + N.extends_id));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitClassDecl_Simple_I(AST_ClassDecl_Simple_I N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_BodyDecl.visit(this));
        v.add(new DefaultMutableTreeNode("Implements-id -> " + N.implements_id));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitConstrDecl(AST_ConstrDecl N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitConstrDecl_S(AST_ConstrDecl_S N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_Statement.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitConstrDecl_V(AST_ConstrDecl_V N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_VarDecl.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitConstrDecl_VS(AST_ConstrDecl_VS N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.N_FormalList != null) v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_Statement.visit(this));
        v.add((MutableTreeNode) N.N_VarDecl.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpList_Lista(AST_ExpList_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N.visit(this));
        v.add((MutableTreeNode) N.sig.visit(this));
        return v;
    }

    public Object visitExpList_Simple(AST_ExpList_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_False(AST_ExpSimpl_False N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_Id(AST_ExpSimpl_Id N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitExpSimpl_IntegerLiteral(AST_ExpSimpl_IntegerLiteral N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode(String.valueOf(N.N_IntegerLiteral)));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_Negacion(AST_ExpSimpl_Negacion N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_New(AST_ExpSimpl_New N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.N_ExpList != null) v.add((MutableTreeNode) N.N_ExpList.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitExpSimpl_NewInt(AST_ExpSimpl_NewInt N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_Parentesis(AST_ExpSimpl_Parentesis N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_StringConstant(AST_ExpSimpl_StringConstant N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("String-Constant -> " + N.N_StringConstant));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_This(AST_ExpSimpl_This N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExpSimpl_True(AST_ExpSimpl_True N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExp_Exp(AST_Exp_Exp N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp2.visit(this));
        return v;
    }

    public Object visitExp_Id(AST_Exp_Id N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.N_ExpList != null) v.add((MutableTreeNode) N.N_ExpList.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitExp_Length(AST_Exp_Length N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Length"));
        v.add(new DefaultMutableTreeNode("tipo-datos: Tinteger"));
        return v;
    }

    public Object visitExp_Op(AST_Exp_Op N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        v.add((MutableTreeNode) N.N_Op.visit(this));
        v.add(new DefaultMutableTreeNode("tipo-datos: Tinteger"));
        return v;
    }

    public Object visitExp_Terminal(AST_Exp_Terminal N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_ExpSimpl.visit(this));
        v.add((MutableTreeNode) N.N_Body.visit(this));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitExp_TerminalBody_Lista(AST_Exp_TerminalBody_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp_Terminal.visit(this));
        v.add((MutableTreeNode) N.extN.visit(this));
        return v;
    }

    public Object visitFormalList_Simple(AST_FormalList_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Type.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitFormalList_Lista(AST_FormalList_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N.visit(this));
        v.add((MutableTreeNode) N.sig.visit(this));
        return v;
    }

    public Object visitImport_Asterisco(AST_Import_Asterisco N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_TypeName.visit(this));
        return v;
    }

    public Object visitImport_Lista(AST_Import_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Import.visit(this));
        v.add((MutableTreeNode) N.extN.visit(this));
        return v;
    }

    public Object visitImport_Simple(AST_Import_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_TypeName.visit(this));
        return v;
    }

    public Object visitMain(AST_Main N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Statement.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        v.add(new DefaultMutableTreeNode("Args -> " + N.args));
        return v;
    }

    public Object visitMethodDecl(AST_MethodDecl N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_MethodDecl_Body.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitMethodDecl_Body(AST_MethodDecl_Body N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        return v;
    }

    public Object visitMethodDecl_Body_S(AST_MethodDecl_Body_S N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.S.visit(this));
        return v;
    }

    public Object visitMethodDecl_Body_VS(AST_MethodDecl_Body_VS N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.S.visit(this));
        v.add((MutableTreeNode) N.V.visit(this));
        return v;
    }

    public Object visitMethodDecl_Body_V(AST_MethodDecl_Body_V N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.V.visit(this));
        return v;
    }

    public Object visitMethodDecl_Type(AST_MethodDecl_Type N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_MethodDecl_Body.visit(this));
        v.add((MutableTreeNode) N.N_Type.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitMethodDecl_Type_R(AST_MethodDecl_Type_R N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_MethodDecl_Body.visit(this));
        v.add((MutableTreeNode) N.N_Type.visit(this));
        v.add((MutableTreeNode) N.R_Exp.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitMethodDecl_Void(AST_MethodDecl_Void N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_MethodDecl_Body.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitMethodDecl_Void_R(AST_MethodDecl_Void_R N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_FormalList.visit(this));
        v.add((MutableTreeNode) N.N_MethodDecl_Body.visit(this));
        v.add((MutableTreeNode) N.R_Exp.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitOp(AST_Op N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        switch(N.Num_Op) {
            case 30:
                v.add(new DefaultMutableTreeNode("+"));
                break;
            case 31:
                v.add(new DefaultMutableTreeNode("-"));
                break;
            case 32:
                v.add(new DefaultMutableTreeNode("/"));
                break;
            case 33:
                v.add(new DefaultMutableTreeNode("*"));
                break;
            case 34:
                v.add(new DefaultMutableTreeNode("<>"));
                break;
            case 35:
                v.add(new DefaultMutableTreeNode("=="));
                break;
            case 36:
                v.add(new DefaultMutableTreeNode("<"));
                break;
            case 37:
                v.add(new DefaultMutableTreeNode(">"));
                break;
            case 38:
                v.add(new DefaultMutableTreeNode("<="));
                break;
            case 39:
                v.add(new DefaultMutableTreeNode(">="));
                break;
            case 40:
                v.add(new DefaultMutableTreeNode("or"));
                break;
            case 41:
                v.add(new DefaultMutableTreeNode("and"));
                break;
        }
        return v;
    }

    public Object visitProgram_IM(AST_Program_IM N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Imports.visit(this));
        v.add((MutableTreeNode) N.N_Main.visit(this));
        return v;
    }

    public Object visitProgram_IMC(AST_Program_IMC N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_ClassDecl.visit(this));
        v.add((MutableTreeNode) N.N_Imports.visit(this));
        v.add((MutableTreeNode) N.N_Main.visit(this));
        return v;
    }

    public Object visitProgram_M(AST_Program_M N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Main.visit(this));
        return v;
    }

    public Object visitProgram_MC(AST_Program_MC N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Main.visit(this));
        v.add((MutableTreeNode) N.N_ClassDecl.visit(this));
        return v;
    }

    public Object visitStatement_Id(AST_Statement_Id N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitStatement_Id_Id(AST_Statement_Id_Id N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.Expl_final.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        v.add(new DefaultMutableTreeNode("Id2 -> " + N.id2));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        if (N.Ndecl2 != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl2.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitStatement_If(AST_Statement_If N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Else_Statement.visit(this));
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        v.add((MutableTreeNode) N.N_Statement.visit(this));
        return v;
    }

    public Object visitStatement_Lista(AST_Statement_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N.visit(this));
        v.add((MutableTreeNode) N.extN.visit(this));
        return v;
    }

    public Object visitStatement_SE(AST_Statement_SE N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode(String.valueOf(N.Int_Lit)));
        return v;
    }

    public Object visitStatement_SOP(AST_Statement_SOP N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        return v;
    }

    public Object visitStatement_Statement_Lista(AST_Statement_Statement_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Statement.visit(this));
        v.add((MutableTreeNode) N.extN.visit(this));
        return v;
    }

    public Object visitStatement_Statement_Simple(AST_Statement_Statement_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        if (N.N_Statement != null) v.add((MutableTreeNode) N.N_Statement.visit(this));
        return v;
    }

    public Object visitStatement_This(AST_Statement_This N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_ExpList.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitStatement_While(AST_Statement_While N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        v.add((MutableTreeNode) N.N_Statement.visit(this));
        return v;
    }

    public Object visitType_I(AST_Type_I N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitType_T(AST_Type_T N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Nombre-tipo -> " + N.tipo.name()));
        if (N.tipo_datos != null) v.add(new DefaultMutableTreeNode("tipo-datos: " + N.tipo_datos));
        return v;
    }

    public Object visitTypeName_Lista(AST_TypeName_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N.visit(this));
        v.add((MutableTreeNode) N.ext.visit(this));
        return v;
    }

    public Object visitTypeName_Simple(AST_TypeName_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitVarDecl_Lista(AST_VarDecl_Lista N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_VarDecl.visit(this));
        v.add((MutableTreeNode) N.extN.visit(this));
        return v;
    }

    public Object visitVarDecl_Simple(AST_VarDecl_Simple N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Type.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitStatement_Asign_Exp(AST_Statement_Asign_Exp N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Final_Exp.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        v.add((MutableTreeNode) N.N_Final_Exp.visit(this));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitStatement_Asign_Read(AST_Statement_Asign N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        return v;
    }

    public Object visitStatement_Asign_Compuesto_Exp(AST_Statement_Asign_Compuesto_Exp N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        v.add((MutableTreeNode) N.N_Final_Exp.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitStatement_Asign(AST_Statement_Asign N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }

    public Object visitStatement_Asign_Compuesto(AST_Statement_Asign_Compuesto N) {
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(N.getClass().getName());
        v.add((MutableTreeNode) N.N_Exp.visit(this));
        v.add(new DefaultMutableTreeNode("Id -> " + N.id));
        if (N.Ndecl != null) v.add(new DefaultMutableTreeNode("Puntero de declaracion -> " + N.Ndecl.getClass().getName())); else v.add(new DefaultMutableTreeNode("declaracion por default en el sistema"));
        return v;
    }
}
