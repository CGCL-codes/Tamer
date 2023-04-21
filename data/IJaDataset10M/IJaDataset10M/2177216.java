package jp.go.aist.six.oval.model.mitre;

import jp.go.aist.six.oval.model.OvalObject;

/**
 * A note.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: Ovalnotes.java 2202 2012-02-01 10:04:16Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
public class Ovalnotes implements OvalObject {

    private String content;

    /**
     * Constructor.
     */
    public Ovalnotes() {
    }

    /**
     */
    public void setContent(final String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "ovalnotes[" + getContent() + "]";
    }
}
