package L1;

import L1.Behavior;
import java.util.List;

/**
 * An behavior with implementation-specific semantics.
 */
public interface OpaqueBehavior extends Behavior {

    /**
    * Specifies the behavior in one or more languages.
    */
    public abstract List<String> getBody();

    /**
    * Specifies the behavior in one or more languages.
    */
    public abstract void setBody(List<String> body);

    /**
    * Languages the body strings use in the same order as the body strings.
    */
    public abstract List<String> getLanguage();

    /**
    * Languages the body strings use in the same order as the body strings.
    */
    public abstract void setLanguage(List<String> language);
}
