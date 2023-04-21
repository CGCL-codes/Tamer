package com.enerjy.analyzer.java.rules;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import com.enerjy.analyzer.IGuiFactory;
import com.enerjy.analyzer.java.AbortRuleException;
import com.enerjy.analyzer.java.IFixCapability;
import com.enerjy.analyzer.java.IJavaRule;
import com.enerjy.analyzer.java.IJavaRuleManager;
import com.enerjy.analyzer.java.JavaProblem;
import com.enerjy.common.jdt.IExternalBindingResolver;
import com.enerjy.common.util.IPropertyStore;

public class RuleBase extends ASTVisitor implements IJavaRule {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("com.enerjy.analyzer.java.rules.JavaRuleBundle");

    private static final IFixCapability defaultFixCapability = new DefaultFixCapability();

    private final String bundleKey;

    private final String category;

    private IJavaRuleManager manager = null;

    static String getRawString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    /**
     * Construct a Java rule.
     */
    protected RuleBase() {
        String name = getClass().getName();
        name = name.substring(name.lastIndexOf('.') + 1);
        if (name.startsWith("T")) {
            name = "JAVA" + name.substring(1);
        }
        bundleKey = name;
        String loadedCategory = getRawString(bundleKey + ".category");
        if (loadedCategory.startsWith("!")) {
            loadedCategory = "general";
        }
        category = getRawString("category_" + loadedCategory);
    }

    public ASTVisitor getVisitor() {
        return this;
    }

    public final void setManager(IJavaRuleManager manager) {
        this.manager = manager;
    }

    public final String getName() {
        return getString("name");
    }

    public final String getKey() {
        return bundleKey;
    }

    public final String getCategory() {
        return category;
    }

    public Object createPreferencePanel(IGuiFactory gui) {
        return null;
    }

    public void readProperties(IPropertyStore store) {
    }

    public void writeProperties(IPropertyStore store) {
    }

    public IFixCapability getFixCapability() {
        return defaultFixCapability;
    }

    public boolean shouldProcess(CompilationUnit unit) {
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RuleBase)) {
            return false;
        }
        RuleBase other = (RuleBase) obj;
        return bundleKey.equals(other.bundleKey);
    }

    @Override
    public int hashCode() {
        return bundleKey.hashCode();
    }

    /**
     * @return The singleton abort exception to throw.
     */
    protected static AbortRuleException abort() {
        return RuleUtils.abortException;
    }

    /**
     * Register a problem on the given node.
     * 
     * @param node Node that contains the problem.
     * @param args Arguments to use in constructing the rule message.
     * @return the newly created problem.
     */
    protected JavaProblem addProblem(ASTNode node, Object... args) {
        if (null == node) {
            throw new IllegalArgumentException("Can not log on an empty node in " + getKey());
        }
        return manager.addProblem(getKey(), node, makeMessage(args));
    }

    /**
     * Obtain a unique property name based on this rule's key and the given property key.
     * 
     * @param key Property to make unique.
     * @return A new property key unique among all rules.
     */
    protected String makeUnique(String key) {
        return bundleKey + "." + key;
    }

    /**
     * Obtain a node lookup object for a compilation unit.
     * 
     * @param unit Compilation unit to query.
     * @return Node lookup for the compilation unit.
     */
    protected static NodeLookup getNodeLookup(CompilationUnit unit) {
        return RuleUtils.getNodeLookup(unit);
    }

    /**
     * Obtain a string from the localization bundle.
     * 
     * @param key String key from the bundle; it will be prefixed with the rule key automatically.
     * @param args Any arguments to be applied via MessageFormat.
     * @return The requested string from the localization bundle, or '!key!' if it could not be found.
     */
    protected String getString(String key, Object... args) {
        String answer = null;
        try {
            answer = bundle.getString(makeUnique(key));
        } catch (MissingResourceException e) {
            return "!" + e.getKey() + "!";
        }
        if (0 != args.length) {
            return MessageFormat.format(answer, args);
        }
        return answer;
    }

    /**
     * Resolve a type binding.
     * 
     * @param jvmSig JVM signature of the type to resolve.
     * @return Type binding for the given signature, or <code>null</code> if it could not be found.
     */
    protected ITypeBinding resolveType(String jvmSig) {
        return manager.resolveType(jvmSig);
    }

    /**
     * Returns the binding resolver used by this rule.
     * 
     * @return The binding resolver used by this rule.
     */
    protected IExternalBindingResolver getResolver() {
        return manager.getResolver();
    }

    /**
     * Determine if a type is an unchecked exception.
     * 
     * @param binding Type binding to verify.
     * @return Whether or not the given type is derived from Error or RuntimeException. Aborts the rule if the given binding is
     *         <code>null</code>.
     */
    protected final boolean isUncheckedException(ITypeBinding binding) {
        return RuleUtils.isUncheckedException(binding, manager.getResolver());
    }

    /**
     * Determine if a method is one of the magic serialization methods. Also checks that the declaring type is Serializable. Aborts
     * the rule if method is null.
     * 
     * @param method Method to check.
     * @return Whether or not the method is a magic serialization method.
     */
    protected final boolean isSerializationMethod(IMethodBinding method) {
        return isSerializationMethod(method, true);
    }

    /**
     * Determine if a method is one of the magic serialization methods. Aborts the rule if method is null.
     * 
     * @param method Method to check.
     * @param checkType Whether or not to check the declaring type to see if it's Serializable. If false, this method will only
     *            check the method signatures.
     * @return Whether or not the method is a magic serialization method.
     */
    protected final boolean isSerializationMethod(IMethodBinding method, boolean checkType) {
        if (null == method) {
            throw abort();
        }
        final String[] magicMethods = new String[] { "writeObject(Ljava/io/ObjectOutputStream;)V", "readObject(Ljava/io/ObjectInputStream;)V", "readObjectNoData()V", "writeReplace()Ljava/lang/Object;", "readResolve()Ljava/lang/Object;" };
        if (RuleUtils.isSignatureEqual(method, magicMethods)) {
            if (!checkType) {
                return true;
            }
            ITypeBinding serializable = resolveType("Ljava/io/Serializable;");
            if (method.getDeclaringClass().isAssignmentCompatible(serializable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return A type binding for Serializable. Aborts the rule if it could not be found.
     */
    protected final ITypeBinding getSerializable() {
        ITypeBinding answer = resolveType("Ljava/io/Serializable;");
        if (null == answer) {
            throw abort();
        }
        return answer;
    }

    /**
     * Create an AST node for a specific position.
     * 
     * @param source AST node to use in creating the new node; this can be any node in the same Compilation Unit.
     * @param startPosition Start position of the node to create.
     * @param length Length of the node to create.
     * @return An AST node suitable for logging.
     */
    protected final ASTNode createNode(ASTNode source, int startPosition, int length) {
        ASTNode fake = source.getAST().newLineComment();
        fake.setSourceRange(startPosition, length);
        return fake;
    }

    private String makeMessage(Object... args) {
        if (0 != args.length) {
            return getString("message", args);
        }
        return getString("name");
    }
}
