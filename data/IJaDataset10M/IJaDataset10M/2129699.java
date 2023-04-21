package quizgame.common;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author rheo
 */
@Deprecated
public class DepricatedQuestion implements Serializable {

    public String question;

    public Set<String> answers;

    public boolean hasBeenUsed = false;
}
