package sk.naive.talker.props;

import sk.naive.talker.message.DefaultMessageFactory;
import java.util.*;

/**
 * Language property - enumerated.
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.5 $ $Date: 2004/04/23 21:54:10 $
 */
public class LangProperty extends EnumeratedProperty {

    public Object defaultValue() {
        return DefaultMessageFactory.LANG_EN;
    }

    {
        Set s = new TreeSet();
        s.add(DefaultMessageFactory.LANG_EN);
        s.add(DefaultMessageFactory.LANG_SK);
        validValues = Collections.unmodifiableSet(s);
    }
}
