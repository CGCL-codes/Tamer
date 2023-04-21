package cartago.events;

import java.util.regex.*;
import cartago.IEventFilter;

/**
 * A Filter based on regular-expression pattern matching.
 *  
 * @author aricci
 *
 */
public class RegExFilter implements IEventFilter, java.io.Serializable {

    private Pattern pattern;

    public RegExFilter(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public boolean select(ArtifactObsEvent ev) {
        Matcher matcher = pattern.matcher(ev.getSignal().getLabel().toString());
        return matcher.matches();
    }
}
