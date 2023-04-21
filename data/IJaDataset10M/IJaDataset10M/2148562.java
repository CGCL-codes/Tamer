package cat.quadriga.parsers.code;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import cat.quadriga.parsers.Token;
import cat.quadriga.parsers.code.expressions.ExpressionNode;
import cat.quadriga.parsers.code.expressions.dataaccess.ArrayLengthAccess;
import cat.quadriga.parsers.code.expressions.dataaccess.DataAccess;
import cat.quadriga.parsers.code.expressions.dataaccess.LiteralData;
import cat.quadriga.parsers.code.expressions.dataaccess.LocalVarAccess;
import cat.quadriga.parsers.code.expressions.dataaccess.TypeDataAccess;
import cat.quadriga.parsers.code.expressions.qdg.ComponentFieldAccess;
import cat.quadriga.parsers.code.expressions.qdg.EventFieldAccess;
import cat.quadriga.parsers.code.proxy.ProxyDataAccess;
import cat.quadriga.parsers.code.symbols.BaseSymbol;
import cat.quadriga.parsers.code.symbols.LocalVariableSymbol;
import cat.quadriga.parsers.code.symbols.TypeSymbol;
import cat.quadriga.parsers.code.types.ArrayType;
import cat.quadriga.parsers.code.types.BaseType;
import cat.quadriga.parsers.code.types.JavaType;
import cat.quadriga.parsers.code.types.PrimitiveTypeRef;
import cat.quadriga.parsers.code.types.ReferenceTypeRef;
import cat.quadriga.parsers.code.types.ClassOrInterfaceTypeRef;
import cat.quadriga.parsers.code.types.UnknownType;
import cat.quadriga.parsers.code.types.qdg.QuadrigaComponent;
import cat.quadriga.parsers.code.types.qdg.QuadrigaEntity;
import cat.quadriga.parsers.code.types.qdg.QuadrigaEvent;
import cat.quadriga.runtime.ComputedValue;
import cat.quadriga.runtime.Entity;
import cat.quadriga.runtime.JavaReference;

public abstract class Utils {

    public static final String treeStringRepresentation(String operation, String... operands) {
        if (operands.length == 0) return operation;
        boolean lastLine = true;
        StringBuilder aux = new StringBuilder();
        for (int i = operands.length - 1; i >= 0; i--) {
            if (operands[i] != null) {
                if (lastLine) {
                    aux.append("\n+- ");
                    aux.append(operands[i].replace("\n", "\n   "));
                    lastLine = false;
                } else {
                    aux.insert(0, operands[i].replace("\n", "\n|  "));
                    aux.insert(0, "\n+- ");
                }
            }
        }
        aux.insert(0, operation);
        return aux.toString();
    }

    public static final String treeStringRepresentation(String operation, Collection<String> operands) {
        return treeStringRepresentation(operation, operands.toArray(new String[operands.size()]));
    }

    public static final String parametersRepresentation(List<ParameterClass> parameters) {
        List<String> aux = new LinkedList<String>();
        for (ParameterClass parameter : parameters) {
            aux.add(Utils.treeStringRepresentation("parameter", parameter.type.getBinaryName(), parameter.name, (parameter.init == null) ? null : Utils.treeStringRepresentation("init", parameter.init.treeStringRepresentation()), (parameter.semantic == null) ? null : "Semantic: " + parameter.semantic));
        }
        return (aux.size() == 0) ? null : Utils.treeStringRepresentation("parameters", aux);
    }

    public static final char convertImageToChar(String image) {
        image = image.substring(1, image.length() - 1);
        if (image.length() == 1) {
            return image.charAt(0);
        } else {
            return convertFirstScapeChar(image.substring(1)).charAt(0);
        }
    }

    public static final String convertImageToString(String image) {
        image = image.substring(1, image.length() - 1);
        String aux[] = image.split("\\\\");
        String result = aux[0];
        for (int i = 1; i < aux.length; i++) {
            result += convertFirstScapeChar(aux[i]);
        }
        return result;
    }

    private static String convertFirstScapeChar(String scape) {
        char aux;
        int len = 1;
        switch(scape.charAt(0)) {
            case 'n':
                aux = '\n';
                break;
            case 't':
                aux = '\t';
                break;
            case 'b':
                aux = '\b';
                break;
            case 'r':
                aux = '\r';
                break;
            case 'f':
                aux = '\f';
                break;
            case '\\':
                aux = '\\';
                break;
            case '\'':
                aux = '\'';
                break;
            case '"':
                aux = '"';
                break;
            case 'u':
                int hex = Integer.decode("0x" + scape.substring(1, 5));
                aux = (char) hex;
                len = 5;
                break;
            case '0':
            default:
                aux = '\0';
                break;
        }
        return aux + scape.substring(len);
    }

    public static DataAccess symbolToDataAccess(BaseSymbol symbol, Token first, Token last, String file) {
        return symbolToDataAccess(symbol, new CodeZoneClass(first, last, file));
    }

    public static DataAccess symbolToDataAccess(BaseSymbol symbol, CodeZone cz) {
        if (symbol instanceof LocalVariableSymbol) {
            return new LocalVarAccess((LocalVariableSymbol) symbol, cz);
        } else if (symbol instanceof TypeSymbol) {
            return new TypeDataAccess(((TypeSymbol) symbol).type, cz);
        }
        return new ProxyDataAccess("Proxy direct access [" + symbol.name + "]", cz);
    }

    public static DataAccess accessToMember(ExpressionNode expression, String member, Token t) {
        return accessToMember(expression, member, new CodeZoneClass(expression, t));
    }

    public static DataAccess accessToMember(ExpressionNode expression, String member, CodeZone cz) {
        if (expression instanceof TypeDataAccess) {
            TypeDataAccess tda = (TypeDataAccess) expression;
            BaseType type = tda.type;
            if ("class".compareTo(member) == 0) {
                return new LiteralData.ClassLiteral((JavaType) type, cz);
            } else if (type instanceof ReferenceTypeRef) {
                return ((ReferenceTypeRef) type).getAccess(member, cz);
            }
        }
        BaseType type = expression.getType();
        if (type instanceof ArrayType && member.compareTo("length") == 0) {
            return new ArrayLengthAccess(expression, cz);
        }
        if (type instanceof QuadrigaComponent) {
            return new ComponentFieldAccess(expression, member, cz);
        }
        if (type instanceof QuadrigaEvent) {
            return new EventFieldAccess(expression, member, cz);
        }
        if (type instanceof QuadrigaEntity) {
            type = ClassOrInterfaceTypeRef.getFromClass(Entity.class);
        }
        if (type instanceof ReferenceTypeRef) {
            return ((ReferenceTypeRef) type).getAccess(expression, member, cz);
        }
        return new ProxyDataAccess(member, expression, cz);
    }

    public static ExpressionNode resolveName(SymbolTable symbolTable, List<Token> identifiers, String file) {
        List<String> aux = new ArrayList<String>(identifiers.size());
        for (Token t : identifiers) {
            aux.add(t.image);
        }
        return resolveName(symbolTable, aux, new CodeZoneClass(identifiers.get(0), identifiers.get(identifiers.size() - 1), file));
    }

    public static ExpressionNode resolveName(SymbolTable symbolTable, String[] identifiers, CodeZone cz) {
        List<String> aux = new ArrayList<String>(identifiers.length);
        for (String t : identifiers) {
            aux.add(t);
        }
        return resolveName(symbolTable, aux, cz);
    }

    public static ExpressionNode resolveName(SymbolTable symbolTable, List<String> identifiers, CodeZone cz) {
        String actual;
        Iterator<String> it = identifiers.iterator();
        ExpressionNode result = null;
        actual = it.next();
        String aux = actual;
        BaseSymbol symbol = symbolTable.findSymbol(aux);
        if (symbol != null) {
            result = symbolToDataAccess(symbol, cz);
        }
        while (result == null && it.hasNext()) {
            actual = it.next();
            aux += '.' + actual;
            symbol = symbolTable.findSymbol(aux);
            if (symbol != null) {
                result = symbolToDataAccess(symbol, cz);
            }
        }
        if (result == null) {
            result = new ProxyDataAccess(aux, cz);
        } else {
            while (it.hasNext()) {
                actual = it.next();
                aux = actual;
                result = accessToMember((DataAccess) result, aux, cz);
            }
        }
        return result;
    }

    public static BaseType createType(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return PrimitiveTypeRef.getFromClass(clazz);
        } else if (clazz.isArray()) {
            return new ArrayType(createType(clazz.getComponentType()));
        } else if (clazz == Entity.class) {
            return QuadrigaEntity.baseEntity;
        } else {
            return ClassOrInterfaceTypeRef.getFromClass(clazz);
        }
    }

    public static boolean aplicableBySubtyping(BaseType origin, Class<?> destiny) {
        return aplicableBySubtyping(origin, createType(destiny));
    }

    public static boolean aplicableBySubtyping(Class<?> origin, BaseType destiny) {
        return aplicableBySubtyping(createType(origin), destiny);
    }

    public static boolean aplicableBySubtyping(Class<?> origin, Class<?> destiny) {
        return aplicableBySubtyping(createType(origin), createType(destiny));
    }

    public static boolean aplicableBySubtyping(BaseType origin, BaseType destiny) {
        if (origin instanceof UnknownType || destiny instanceof UnknownType) {
            return true;
        }
        return destiny.isAssignableFrom(origin);
    }

    public static int selectMethod(List<ExpressionNode> calledArgs, Class<?>[][] realArgs) {
        List<Integer> validMethods = new LinkedList<Integer>();
        for (int i = 0; i < realArgs.length; i++) {
            if (realArgs[i].length == calledArgs.size()) {
                validMethods.add(i);
            }
        }
        if (validMethods.size() == 0) {
            return -1;
        } else {
            List<Integer> validMethods2 = new LinkedList<Integer>();
            for (Integer validMethod : validMethods) {
                Class<?>[] declaredArgs = realArgs[validMethod];
                boolean selected = true;
                for (int i = 0; i < declaredArgs.length; i++) {
                    if (!Utils.aplicableBySubtyping(calledArgs.get(i).getType(), declaredArgs[i])) {
                        selected = false;
                        continue;
                    }
                }
                if (selected) {
                    validMethods2.add(validMethod);
                }
            }
            if (validMethods2.size() > 0) {
                return validMethods2.get(0);
            } else {
                return -1;
            }
        }
    }

    public static String getName(List<Token> t) {
        String aux = t.get(0).image;
        for (int i = 1; i < t.size(); i++) {
            aux += "." + t.get(i).image;
        }
        return aux;
    }

    public static final int PUBLIC = 0x0001;

    public static final int PROTECTED = 0x0002;

    public static final int PRIVATE = 0x0004;

    public static final int ABSTRACT = 0x0008;

    public static final int STATIC = 0x0010;

    public static final int FINAL = 0x0020;

    public static final int SYNCHRONIZED = 0x0040;

    public static final int NATIVE = 0x0080;

    public static final int TRANSIENT = 0x0100;

    public static final int VOLATILE = 0x0200;

    public static final int STRICTFP = 0x1000;
}
