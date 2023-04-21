package es.nom.morenojuarez.modulipse.core.lang.ast;

import java.util.ArrayList;
import java.util.List;
import antlr.collections.AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes;

/**
 * 
 */
public class ImplementationModule implements Node {

    private String name;

    private Expression protection;

    private Expression priority;

    private List<Import> imports = new ArrayList<Import>();

    private List<ConstantBlock> constants = new ArrayList<ConstantBlock>();

    private List<TypeDeclarationBlock> types = new ArrayList<TypeDeclarationBlock>();

    private List<VariableDefinitionBlock> variables = new ArrayList<VariableDefinitionBlock>();

    private List<ProcedureDeclaration> procedures = new ArrayList<ProcedureDeclaration>();

    private List<ModuleDeclaration> modules = new ArrayList<ModuleDeclaration>();

    private InitBody initBody;

    private FinalBody finalBody;

    public static ImplementationModule create(Modula2AST ast) {
        if (ast != null && ast.getType() == Modula2TokenTypes.IMPLEMENTATION) {
            ImplementationModule node = new ImplementationModule();
            AST currentAST = ast.getFirstChild();
            node.name = currentAST.getText();
            currentAST = currentAST.getNextSibling();
            if (currentAST.getType() != Modula2TokenTypes.SEMICOLON) {
                if (currentAST.getType() == Modula2TokenTypes.LBRACK) {
                    node.protection = Expression.create((Modula2AST) currentAST.getFirstChild());
                } else {
                    node.priority = Expression.create((Modula2AST) currentAST);
                }
                while (currentAST.getType() != Modula2TokenTypes.SEMICOLON) {
                    currentAST = currentAST.getNextSibling();
                }
            }
            currentAST = currentAST.getNextSibling();
            while (currentAST.getType() == Modula2TokenTypes.IMPORT) {
                node.getImports().add(Import.create((Modula2AST) currentAST));
                currentAST = currentAST.getNextSibling();
            }
            while (currentAST.getType() != Modula2TokenTypes.BEGIN) {
                switch(currentAST.getType()) {
                    case Modula2TokenTypes.CONST:
                        node.getConstants().add(ConstantBlock.create((Modula2AST) currentAST));
                        break;
                    case Modula2TokenTypes.TYPE:
                        node.getTypeBlocks().add(TypeDeclarationBlock.create((Modula2AST) currentAST));
                        break;
                    case Modula2TokenTypes.VAR:
                        node.getVariableBlocks().add(VariableDefinitionBlock.create((Modula2AST) currentAST));
                        break;
                    case Modula2TokenTypes.PROC_IMPL:
                        node.getProcedures().add(ProcedureDeclaration.create((Modula2AST) currentAST));
                        break;
                    case Modula2TokenTypes.MODULE:
                        node.getModules().add(ModuleDeclaration.create((Modula2AST) currentAST));
                        break;
                    case Modula2TokenTypes.BEGIN:
                        node.initBody = InitBody.create((Modula2AST) currentAST);
                        break;
                    case Modula2TokenTypes.FINALLY:
                        node.finalBody = FinalBody.create((Modula2AST) currentAST);
                        break;
                }
                currentAST = currentAST.getNextSibling();
            }
            return node;
        }
        return null;
    }

    public List<ModuleDeclaration> getModules() {
        return modules;
    }

    public List<ProcedureDeclaration> getProcedures() {
        return procedures;
    }

    public List<VariableDefinitionBlock> getVariableBlocks() {
        return variables;
    }

    public List<TypeDeclarationBlock> getTypeBlocks() {
        return types;
    }

    public List<ConstantBlock> getConstants() {
        return constants;
    }

    public List<Import> getImports() {
        return imports;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the priority
	 */
    public Expression getPriority() {
        return priority;
    }

    /**
	 * @return the protection
	 */
    public Expression getProtection() {
        return protection;
    }

    public String toFormattedString() {
        StringBuffer formattedString = new StringBuffer("IMPLEMENTATION MODULE " + getName());
        if (getPriority() != null) {
            formattedString.append(" " + getPriority());
        } else if (getProtection() != null) {
            formattedString.append(" " + getProtection());
        }
        formattedString.append(";\n");
        for (Import i : getImports()) {
            formattedString.append(i.toFormattedString());
        }
        for (ConstantBlock constant : getConstants()) {
            formattedString.append(constant.toFormattedString());
        }
        for (TypeDeclarationBlock type : getTypeBlocks()) {
            formattedString.append(type.toFormattedString());
        }
        for (VariableDefinitionBlock variable : getVariableBlocks()) {
            formattedString.append(variable.toFormattedString());
        }
        for (ModuleDeclaration module : getModules()) {
            formattedString.append(module.toFormattedString());
        }
        for (ProcedureDeclaration procedure : getProcedures()) {
            formattedString.append(procedure.toFormattedString());
        }
        formattedString.append("BEGIN\n");
        formattedString.append(getInitBody().toFormattedString());
        if (getFinalBody() != null) {
            formattedString.append("FINALLY\n");
            formattedString.append(getFinalBody().toFormattedString());
        }
        formattedString.append(getName() + ".\n");
        return formattedString.toString();
    }

    /**
	 * @return the finalBody
	 */
    public FinalBody getFinalBody() {
        return finalBody;
    }

    /**
	 * @return the initBody
	 */
    public InitBody getInitBody() {
        return initBody;
    }
}
