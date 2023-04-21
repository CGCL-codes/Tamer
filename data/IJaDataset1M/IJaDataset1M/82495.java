package annone.local.compiler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import annone.engine.ElementType;
import annone.engine.Visibility;
import annone.engine.defs.ArgumentDef;
import annone.engine.defs.ClassDef;
import annone.engine.defs.ComponentDef;
import annone.engine.defs.EntityDef;
import annone.engine.defs.EnumDef;
import annone.engine.defs.FieldDef;
import annone.engine.defs.InstanceDef;
import annone.engine.defs.InterfaceDef;
import annone.engine.defs.TypeDef;
import annone.util.Checks;
import annone.util.Text;

class Compiler {

    private final Builder builder;

    private final Source source;

    private final List<Token> tokens;

    private int index;

    private Token token;

    private String qualifiedName;

    private Visibility visibility;

    private Set<TokenType> modifiers;

    private ComponentDef componentDef;

    public Compiler(Builder builder, Source source, List<Token> tokens) {
        Checks.notNull("builder", builder);
        Checks.notNull("source", source);
        Checks.notEmpty("tokens", tokens);
        this.builder = builder;
        this.source = source;
        this.tokens = tokens;
    }

    public ComponentDef compile() {
        try {
            System.out.println(tokens);
            componentDef();
            return componentDef;
        } catch (ArrayIndexOutOfBoundsException xp) {
            throw newBuildException(Text.get("Unexpected end of source."));
        }
    }

    private void nextToken() {
        if (index >= tokens.size()) throw newBuildException(Text.get("Unexpected end of source."));
        token = tokens.get(index++);
    }

    private Visibility getVisibility() {
        switch(token.getType()) {
            case EXPOSED:
                return Visibility.EXPOSED;
            case PUBLIC:
                return Visibility.PUBLIC;
            case PROTECTED:
                return Visibility.PROTECTED;
            case LIBRARY:
                return Visibility.LIBRARY;
            case PACKAGE:
                return Visibility.PACKAGE;
            case PRIVATE:
                return Visibility.PRIVATE;
            default:
                return null;
        }
    }

    private void visibility() {
        visibility = getVisibility();
        if (visibility == null) visibility = Visibility.EXPOSED; else {
            nextToken();
            if (getVisibility() != null) throw newBuildException(Text.get("Only one visibility per declaration allowed."));
        }
    }

    private void qualifiedName() {
        StringBuilder b = new StringBuilder();
        identifier();
        b.append(token.getText());
        nextToken();
        while (token.getType() == TokenType.DOT) {
            nextToken();
            identifier();
            b.append('.');
            b.append(token.getText());
            nextToken();
        }
        qualifiedName = b.toString();
    }

    private void identifier() {
        if (token.getType() != TokenType.IDENTIFIER) throw newBuildException(Text.get("Identifier expected."));
    }

    private void uses() {
    }

    private void componentDef() {
        nextToken();
        uses();
        visibility();
        TokenType componentType = token.getType();
        switch(componentType) {
            case CLASS:
            case INTERFACE:
            case INSTANCE:
            case ENTITY:
            case ENUM:
                nextToken();
                identifier();
                String qualifiedName = source.getUri();
                if (!qualifiedName.endsWith("/" + token.getText())) throw newBuildException(Text.get("Component name ''{0}'' doesn''t match source name ''{1}''.", token.getText(), source.getUri()));
                switch(componentType) {
                    case CLASS:
                        componentDef = new ClassDef(qualifiedName);
                        break;
                    case INTERFACE:
                        componentDef = new InterfaceDef(qualifiedName);
                        break;
                    case INSTANCE:
                        componentDef = new InstanceDef(qualifiedName);
                        break;
                    case ENTITY:
                        componentDef = new EntityDef(qualifiedName);
                        break;
                    case ENUM:
                        componentDef = new EnumDef(qualifiedName);
                        break;
                }
                builder.registerComponentDef(componentDef);
                nextToken();
                switch(componentType) {
                    case CLASS:
                        classDef();
                        break;
                    case INTERFACE:
                        interfaceDef();
                        break;
                    case INSTANCE:
                        instanceDef();
                        break;
                    case ENTITY:
                        entityDef();
                        break;
                    case ENUM:
                        enumDef();
                        break;
                }
                break;
            default:
                throw newBuildException(Text.get("Component declaration expected ({0}, {1}, {2}, {3}, {4}, or {5}).", "class", "instance", "interface", "entity", "enum", "extension"));
        }
    }

    private void classDef() {
        ClassDef classDef = (ClassDef) componentDef;
        if (token.getType() == TokenType.INHERITS) {
            nextToken();
            qualifiedName();
            ComponentDef baseClassDef = builder.getComponentDef(qualifiedName);
            if (baseClassDef == null) throw newBuildException(Text.get("Base class ''{0}'' not found.", qualifiedName)); else if (baseClassDef instanceof ClassDef) classDef.setInheritsDef(baseClassDef); else throw newBuildException(Text.get("Component ''{0}'' not allowed as base class.", qualifiedName));
        }
        if (token.getType() == TokenType.IMPLEMENTS) do {
            nextToken();
            qualifiedName();
            ComponentDef interfaceDef = builder.getComponentDef(qualifiedName);
            if (interfaceDef == null) throw newBuildException(Text.get("Interface ''{0}'' not found.", qualifiedName)); else if (interfaceDef instanceof InterfaceDef) {
                if (!classDef.addInterfaceDef((InterfaceDef) interfaceDef)) throw newBuildException(Text.get("Duplicate interface ''{0}''.", qualifiedName));
            } else throw newBuildException(Text.get("Component ''{0}'' not allowed as interface.", qualifiedName));
        } while (token.getType() == TokenType.COMMA);
        memberDefs(EnumSet.of(ElementType.FIELD, ElementType.METHOD, ElementType.EVENT));
    }

    private void interfaceDef() {
    }

    private void instanceDef() {
        classDef();
    }

    private void entityDef() {
        EntityDef entityDef = (EntityDef) componentDef;
        if (token.getType() == TokenType.INHERITS) {
            nextToken();
            qualifiedName();
            ComponentDef baseClassDef = builder.getComponentDef(qualifiedName);
            if (baseClassDef == null) throw newBuildException(Text.get("Base class ''{0}'' not found.", qualifiedName)); else if (baseClassDef instanceof ClassDef) entityDef.setInheritsDef(baseClassDef); else throw newBuildException(Text.get("Component ''{0}'' not allowed as base class.", qualifiedName));
        }
        if (token.getType() == TokenType.IMPLEMENTS) do {
            nextToken();
            qualifiedName();
            ComponentDef interfaceDef = builder.getComponentDef(qualifiedName);
            if (interfaceDef == null) throw newBuildException(Text.get("Interface ''{0}'' not found.", qualifiedName)); else if (interfaceDef instanceof InterfaceDef) {
                if (!entityDef.addInterfaceDef((InterfaceDef) interfaceDef)) throw newBuildException(Text.get("Duplicate interface ''{0}''.", qualifiedName));
            } else throw newBuildException(Text.get("Component ''{0}'' not allowed as interface.", qualifiedName));
        } while (token.getType() == TokenType.COMMA);
        memberDefs(EnumSet.of(ElementType.FIELD, ElementType.METHOD, ElementType.EVENT));
    }

    private void enumDef() {
        classDef();
    }

    private void extensionDef() {
        classDef();
    }

    private void modifiers() {
        modifiers = EnumSet.noneOf(TokenType.class);
        if (token.getType() == TokenType.ABSTRACT) {
            modifiers.add(TokenType.ABSTRACT);
            nextToken();
        }
        if (token.getType() == TokenType.STATIC) {
            if (modifiers.contains(TokenType.ABSTRACT)) throw newBuildException(Text.get("Abstract methods can''t be static."));
            modifiers.add(TokenType.STATIC);
            nextToken();
        }
        if (token.getType() == TokenType.FINAL) {
            if (modifiers.contains(TokenType.ABSTRACT)) throw newBuildException(Text.get("Final methods can''t be static."));
            modifiers.add(TokenType.FINAL);
            nextToken();
        }
    }

    private void memberDefs(Set<ElementType> allowedMembers) {
        while (token.getType() != TokenType.END) {
            visibility();
            modifiers();
            identifier();
            String memberName = token.getText();
            nextToken();
            if (token.getType() == TokenType.OPEN_PARENTHESES) {
                nextToken();
                List<ArgumentDef> arguments = new ArrayList<ArgumentDef>(0);
                argumentDefs(arguments);
            } else {
                List<String> fieldNames = new ArrayList<String>(1);
                fieldNames.add(memberName);
                while (token.getType() == TokenType.COMMA) {
                    nextToken();
                    identifier();
                    nextToken();
                }
                if (token.getType() != TokenType.COLON) throw newBuildException(Text.get("''{0}'' expected.", ":"));
                nextToken();
                TypeDef typeDef = typeDef();
                List<FieldDef> fieldDefs = new ArrayList<FieldDef>(fieldNames.size());
                for (String fieldName : fieldNames) {
                    FieldDef fieldDef = new FieldDef(fieldName);
                    fieldDef.setTypeDef(typeDef);
                    fieldDefs.add(fieldDef);
                }
            }
        }
    }

    private void argumentDefs(List<ArgumentDef> argumentDefs) {
        List<String> argumentNames = new ArrayList<String>(0);
        while (true) {
            argumentNames.clear();
            do {
                identifier();
                argumentNames.add(token.getText());
                nextToken();
            } while (token.getType() == TokenType.COMMA);
            if (token.getType() != TokenType.COLON) throw newBuildException(Text.get("''{0}'' expected.", ":"));
            nextToken();
            TypeDef typeDef = typeDef();
            for (String argumentName : argumentNames) {
                ArgumentDef argumentDef = new ArgumentDef(argumentName);
                argumentDef.setTypeDef(typeDef);
                argumentDefs.add(argumentDef);
            }
            if (token.getType() == TokenType.CLOSE_PARENTHESES) break; else if (token.getType() != TokenType.SEMICOLON) throw newBuildException(Text.get("''{0}'' or ''{1}'' expected.", ";", ")"));
        }
    }

    private TypeDef typeDef() {
        boolean variable = (token.getType() == TokenType.VAR);
        if (variable) nextToken();
        qualifiedName();
        ComponentDef targetDef = builder.getComponentDef(qualifiedName);
        if (targetDef == null) throw newBuildException(Text.get("Component ''{0}'' not found.", qualifiedName));
        TypeDef typeDef = new TypeDef();
        typeDef.setTargetDef(targetDef);
        if (token.getType() == TokenType.OPEN_PARENTHESES) nextToken();
        return null;
    }

    private BuildException newBuildException(String message) {
        if (token != null) return new BuildException(source.getUri(), token.getLine(), token.getColumn(), token.getLength(), null, message); else return new BuildException(source.getUri(), 0, 0, 0, null, message);
    }
}
