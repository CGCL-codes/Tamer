package ng.ast;

/**
 * @author John
 * 
 */
public interface Identifier extends Expression {

    String getName();

    boolean isTyped();

    Class<?> getType();
}
