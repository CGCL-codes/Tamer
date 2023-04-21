package playground.kai.urbansim.ids;

import org.apache.log4j.Logger;

/**
 * @author nagel
 *
 */
public class PersonIdFactory implements IdFactory {

    private static final Logger log = Logger.getLogger(PersonIdFactory.class);

    public PersonId createId(String str) {
        return new PersonId(str);
    }

    public PersonId createId(long ii) {
        return new PersonId(ii);
    }
}
