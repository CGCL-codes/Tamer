package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.project.itemfinder.PHPItem;
import gatchan.phpparser.parser.PHPParser;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import org.gjt.sp.jedit.GUIUtilities;

/**
 * The ClassHeader is that : class ClassName [extends SuperClassName].
 *
 * @author Matthieu Casanova
 * @version $Id: ClassHeader.java 20078 2011-10-14 17:32:32Z kpouer $
 */
public class ClassHeader extends AstNode implements PHPItem, Serializable {

    /**
	 * The path of the file containing this class.
	 */
    private final String path;

    private final String namespace;

    /**
	 * The name of the class.
	 */
    private final ClassIdentifier className;

    private String nameLowerCase;

    /**
	 * The name of the superclass.
	 */
    private final ClassIdentifier superClassName;

    /**
	 * The implemented interfaces. It could be null.
	 */
    private final List<InterfaceIdentifier> interfaceNames;

    /**
	 * The methodsHeaders of the class.
	 */
    private final List<MethodHeader> methodsHeaders = new ArrayList<MethodHeader>();

    /**
	 * The constants of the class (for php5).
	 */
    private final List<ClassConstant> constants = new ArrayList<ClassConstant>();

    private final List<Modifier> modifiers = new ArrayList<Modifier>(3);

    /**
	 * The fields of the class.
	 * It contains {@link FieldDeclaration}
	 */
    private final List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();

    private static transient Icon icon;

    private transient String cachedToString;

    private static final long serialVersionUID = 8213003151739601011L;

    public ClassHeader(String namespace, String path, ClassIdentifier className, ClassIdentifier superClassName, List<InterfaceIdentifier> interfaceNames, int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.namespace = namespace;
        this.path = path;
        this.className = className;
        this.superClassName = superClassName;
        this.interfaceNames = interfaceNames;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public String toString(int tab) {
        StringBuilder buff = new StringBuilder(200);
        buff.append(tabString(tab));
        buff.append("class ");
        buff.append(className);
        if (superClassName != null) {
            buff.append(" extends ");
            buff.append(superClassName);
        }
        if (interfaceNames != null) {
            buff.append(" implements ");
            for (int i = 0; i < interfaceNames.size(); i++) {
                if (i != 0) buff.append(", ");
                buff.append(interfaceNames.get(i));
            }
        }
        return buff.toString();
    }

    public String toString() {
        if (cachedToString == null) {
            StringBuilder buff = new StringBuilder(200);
            buff.append(className);
            if (superClassName != null) {
                buff.append(':');
                buff.append(superClassName);
            }
            cachedToString = buff.toString();
        }
        return cachedToString;
    }

    @Override
    public void getOutsideVariable(List<VariableUsage> list) {
    }

    @Override
    public void getModifiedVariable(List<VariableUsage> list) {
    }

    @Override
    public void getUsedVariable(List<VariableUsage> list) {
    }

    public void addModifier(Modifier modifier) {
        modifiers.add(modifier);
    }

    /**
	 * Returns the name of the class.
	 *
	 * @return the name of the class
	 */
    public String getName() {
        if (className == null) return PHPParser.SYNTAX_ERROR_CHAR;
        return className.toString();
    }

    public String getNameLowerCase() {
        if (nameLowerCase == null) {
            nameLowerCase = getName().toLowerCase();
        }
        return nameLowerCase;
    }

    /**
	 * Returns the name of the superclass.
	 *
	 * @return the name of the superclass
	 */
    public String getSuperClassName() {
        if (superClassName == null) return PHPParser.SYNTAX_ERROR_CHAR;
        return superClassName.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ClassHeader)) return false;
        return ((ClassHeader) obj).getName().equals(className);
    }

    public String getPath() {
        return path;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = GUIUtilities.loadIcon(ClassHeader.class.getResource("/gatchan/phpparser/icons/class.png").toString());
        }
        return icon;
    }

    /**
	 * Add a method to the class.
	 *
	 * @param method the method declaration
	 */
    public void addMethod(MethodHeader method) {
        methodsHeaders.add(method);
    }

    /**
	 * Add a method to the class.
	 *
	 * @param field the method declaration
	 */
    public void addField(FieldDeclaration field) {
        fields.add(field);
    }

    /**
	 * Add a constant to the class.
	 *
	 * @param constant the constant
	 */
    public void addConstant(ClassConstant constant) {
        constants.add(constant);
    }

    public List<MethodHeader> getMethodsHeaders() {
        return methodsHeaders;
    }

    /**
	 * Returns the list of the field of this class.
	 * It contains {@link FieldDeclaration}
	 *
	 * @return the list of fields of the class
	 */
    public List<FieldDeclaration> getFields() {
        return fields;
    }

    public int getItemType() {
        return CLASS;
    }

    @Override
    public void analyzeCode(PHPParser parser) {
    }

    public List<InterfaceIdentifier> getInterfaceNames() {
        return interfaceNames;
    }

    @Override
    public AstNode subNodeAt(int line, int column) {
        if (className != null) {
            if (className.isAt(line + 1, column)) return className;
        }
        if (superClassName != null) {
            if (superClassName.isAt(line + 1, column)) return superClassName;
        }
        if (interfaceNames != null) {
            for (InterfaceIdentifier interfaceName : interfaceNames) {
                if (interfaceName.isAt(line, column)) return interfaceName;
            }
        }
        return null;
    }
}
