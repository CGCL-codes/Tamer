package gnu.javax.swing.text.html.parser.models;

import java.io.Serializable;

/**
 * Disallows a single given tag at the current content level only.
 * <p>@author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)</p>
 */
public class noTagModel extends node implements Serializable {

    private static final long serialVersionUID = 1;

    final String[] no;

    public noTagModel(String[] noTag) {
        super((char) 0, (char) 0, null);
        no = noTag;
    }

    public noTagModel(String noTag) {
        super((char) 0, (char) 0, null);
        no = new String[] { noTag };
    }

    public Object show(Object x) {
        for (int i = 0; i < no.length; i++) {
            if (x.toString().equalsIgnoreCase(no[i])) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
