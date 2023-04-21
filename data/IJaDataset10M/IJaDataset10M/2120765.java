package org.identifylife.character.store.model.media;

import org.custommonkey.xmlunit.XMLAssert;
import org.identifylife.character.store.TestData;
import org.identifylife.character.store.model.AbstractMarshallerTest;
import org.junit.Test;

/**
 * @author dbarnier
 *
 */
public class MediaItemMarshallerTests extends AbstractMarshallerTest {

    @Test
    public void marshalMediaItem() throws Exception {
        MediaItem mediaItem = TestData.TEST_MEDIA_ITEM();
        String result = marshalObjectToXml(MediaItem.class, mediaItem);
        String expected = readXmlFromFile("/test-data/model/media/media_item.xml");
        if (logger.isDebugEnabled()) {
            logger.debug("result: " + result);
            logger.debug("expected: " + expected);
        }
        XMLAssert.assertXMLEqual(expected, result);
    }
}
