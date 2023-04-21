package tree.specific;

import imp.parser.antlr.HaxeParser;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import tree.HaxeTree;

public class DeclarationNode extends NodeWithModifier {

    private enum PropertyAccessors {

        FUNCTION, PRIVATE, PUBLIC, DYNAMIC, NEVER
    }

    private static final int VAR_INIT_TYPE = HaxeParser.VAR_INIT;

    private static final int TYPE_TAG_TYPE = HaxeParser.TYPE_TAG;

    private static final int PROPERTY_DECL = HaxeParser.PROPERTY_DECL;

    /** The name with type. */
    private String nameWithType = "";

    private PropertyAccessors setAccessor = null;

    private PropertyAccessors getAccessor = null;

    private boolean declaredWithoutType = false;

    protected DeclarationType declType = DeclarationType.VarDeclaration;

    public enum DeclarationType {

        ClassVarDeclaration, FunctionParameter, VarDeclaration
    }

    ;

    /**
	 * Gets the name with type.
	 * 
	 * @return the name with type
	 */
    public String getNameWithType() {
        if (nameWithType.equals("")) {
            nameWithType = getText() + ": " + getHaxeType().getShortTypeName();
        }
        return nameWithType;
    }

    public DeclarationNode(final int ttype, final Token token) {
        this(token);
    }

    public DeclarationNode(final Token token) {
        super(token);
    }

    /**
	 * Returns node correspond for var name (in var tmp:Int = foo+bar; it will
	 * return "tmp").
	 * 
	 * @return the var name
	 */
    public String getText() {
        return getToken().getText();
    }

    public boolean isDeclaredWithoutType() {
        return declaredWithoutType;
    }

    public boolean isProperty() {
        return setAccessor != null || getAccessor != null;
    }

    public boolean isField() {
        return setAccessor == null && getAccessor == null;
    }

    public void setDeclaratonType(DeclarationType type) {
        declType = type;
    }

    public DeclarationType getDeclaratonType() {
        return declType;
    }

    public void updateInfo() {
        HaxeTree init = getInitializationNode();
        if (init != null) {
            mostRightPosition = init.getMostRightPosition();
        }
        tryExtractType();
        updateModifier();
    }

    public HaxeTree getInitializationNode() {
        for (HaxeTree tree : getChildren()) {
            Token token = (CommonToken) tree.getToken();
            if (token.getType() == VAR_INIT_TYPE) {
                return tree.getChild(0);
            }
        }
        return null;
    }

    private void tryExtractType() {
        declaredWithoutType = true;
        for (HaxeTree tree : getChildren()) {
            int type = tree.getToken().getType();
            if (type == TYPE_TAG_TYPE && tree.getChildCount() != 0) {
                mostRightPosition = tree.getChild(0).getMostRightPosition();
                declaredWithoutType = false;
            } else if (type == PROPERTY_DECL && tree.getChildCount() != 0) {
                getAccessor = getAccessor(tree.getChild(0).getText());
                setAccessor = getAccessor(tree.getChild(1).getText());
                mostRightPosition = tree.getChild(1).getMostRightPosition();
            }
        }
    }

    private static PropertyAccessors getAccessor(String str) {
        if (str.equalsIgnoreCase("NULL")) {
            return PropertyAccessors.PRIVATE;
        } else if (str.equalsIgnoreCase("DYNAMIC")) {
            return PropertyAccessors.DYNAMIC;
        } else if (str.equalsIgnoreCase("DEFAULT")) {
            return PropertyAccessors.PUBLIC;
        } else if (str.equalsIgnoreCase("NEVER")) {
            return PropertyAccessors.NEVER;
        }
        return PropertyAccessors.FUNCTION;
    }

    @Override
    protected void calculateMostLeftPosition() {
        mostLeftPosition = ((CommonToken) token).getStartIndex();
    }

    @Override
    protected void calculateMostRightPosition() {
        tryExtractType();
        if (mostRightPosition == -1) {
            mostRightPosition = ((CommonToken) token).getStopIndex();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeclarationNode)) {
            return false;
        }
        DeclarationNode node = (DeclarationNode) obj;
        if (getText().equals(node.getText()) && token.equals(node.getToken())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return toString("declaration");
    }
}
