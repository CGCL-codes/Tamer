package org.speech.asr.gui.dao.jcr.callback.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.common.exception.AsrRuntimeException;
import org.speech.asr.gui.constant.JcrConstants;
import static org.speech.asr.gui.constant.JcrDictionaryProperties.CONTENT_CHILD;
import org.speech.asr.gui.dao.jcr.callback.BaseUuidJcrCallback;
import org.speech.asr.common.entity.Word;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class GetDictionaryContentCallback extends BaseUuidJcrCallback {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(GetDictionaryContentCallback.class.getName());

    public GetDictionaryContentCallback(String uuid) {
        super(uuid);
    }

    public Object doInJcr(Session session) throws IOException, RepositoryException {
        Node node = session.getNodeByUUID(getUuid());
        List<Word> words = new LinkedList();
        if (node.hasNode(CONTENT_CHILD)) {
            Node content = node.getNode(CONTENT_CHILD);
            String mimeType = content.getProperty(JcrConstants.JCR_MIME_TYPE_PROPERTY).getString();
            String encoding = content.getProperty(JcrConstants.JCR_ENCODING_PROPERTY).getString();
            InputStream contentObjStream = content.getProperty(JcrConstants.JCR_DATA_PROPERTY).getStream();
            ObjectInputStream deserializer = new ObjectInputStream(contentObjStream);
            try {
                words = (List) deserializer.readObject();
            } catch (ClassNotFoundException e) {
                throw new AsrRuntimeException(e);
            } finally {
                deserializer.close();
            }
        }
        return words;
    }
}
