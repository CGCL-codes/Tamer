package backend.param.args;

/**
 * 
 * @author hindlem
 *
 */
public interface NonContinuousArgumentDefinition extends ArgumentDefinition {

    public abstract Object[] getValidValues();
}
