package gnu.xml.validation.relaxng;

/**
 * A RELAX NG choice element (in the context of a name class).
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class ChoiceNameClass extends NameClass {

    NameClass name1;

    NameClass name2;

    boolean matchesName(String uri, String localName) {
        return name1.matchesName(uri, localName) || name2.matchesName(uri, localName);
    }
}
