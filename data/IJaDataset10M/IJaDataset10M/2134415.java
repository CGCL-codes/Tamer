package opennlp.tools.tokenize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import opennlp.tools.util.InvalidFormatException;
import org.junit.Test;

/**
 * Tests for the {@link TokenizerModel} class.
 */
public class TokenizerModelTest {

    @Test
    public void testSentenceModel() throws IOException, InvalidFormatException {
        TokenizerModel model = TokenizerTestUtil.createSimpleMaxentTokenModel();
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        model.serialize(arrayOut);
        arrayOut.close();
        model = new TokenizerModel(new ByteArrayInputStream(arrayOut.toByteArray()));
        arrayOut = new ByteArrayOutputStream();
        model.serialize(arrayOut);
        arrayOut.close();
        new TokenizerModel(new ByteArrayInputStream(arrayOut.toByteArray()));
    }
}
