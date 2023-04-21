package ch.ethz.dcg.spamato.filter.earlgrey.common.xml;

import java.io.BufferedInputStream;
import java.io.InputStream;
import ch.ethz.dcg.spamato.base.common.util.xml.XMLObjectInputStream;
import ch.ethz.dcg.spamato.base.common.util.xml.XMLTagAliaser;

/**
 * Wrapps the functionality to be able to deserialize Earlgrey Filter Objects
 * as XML.
 * 
 * @author simon schlachter
 */
public class EarlgreyFilterXMLInputStream extends XMLObjectInputStream {

    public EarlgreyFilterXMLInputStream() {
        super();
        init();
    }

    public EarlgreyFilterXMLInputStream(InputStream inputStream) {
        super(inputStream);
        init();
    }

    public EarlgreyFilterXMLInputStream(BufferedInputStream bufferedIn) {
        super(bufferedIn);
        init();
    }

    /**
    * Alias all serializable classes in order to prevent the use of fully qualified class names.
    * <p>
    * If this would not be done, resulting XML messages would, e.g., contain elements of name
    * <code>ch.ethz.dcg.spamato.base.common.util.msg.Message</code> instead of only <code>Message</code>.
    * <p>
    * This makes the communication less version dependent.
    */
    private void init() {
        XMLTagAliaser.setXMLAliases(new EarlgreyFilterXMLTagAliaser(), this);
    }
}
