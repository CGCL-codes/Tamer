package org.waveprotocol.box.server.robots.operations;

import static org.mockito.Mockito.mock;
import com.google.wave.api.JsonRpcResponse;
import com.google.wave.api.OperationRequest;
import com.google.wave.api.data.converter.EventDataConverter;
import junit.framework.TestCase;
import org.waveprotocol.box.server.robots.OperationContextImpl;
import org.waveprotocol.box.server.robots.util.ConversationUtil;
import org.waveprotocol.box.server.waveserver.WaveletProvider;
import org.waveprotocol.wave.model.wave.ParticipantId;

/**
 * Unit test for the {@link DoNothingService}.
 *
 * @author ljvderijk@google.com (Lennard de Rijk)
 */
public class DoNothingServiceTest extends TestCase {

    private static final ParticipantId BOB = ParticipantId.ofUnsafe("bob@example.com");

    private OperationService operationService;

    @Override
    protected void setUp() throws Exception {
        operationService = DoNothingService.create();
    }

    public void testReturnsEmptyResponse() throws Exception {
        OperationRequest request = new OperationRequest("wavelet.fetch", "op1");
        WaveletProvider waveletProvider = mock(WaveletProvider.class);
        EventDataConverter converter = mock(EventDataConverter.class);
        ConversationUtil conversationUtil = mock(ConversationUtil.class);
        OperationContextImpl context = new OperationContextImpl(waveletProvider, converter, conversationUtil);
        operationService.execute(request, context, BOB);
        JsonRpcResponse response = context.getResponse(request.getId());
        assertFalse("Expected non error response", response.isError());
        assertTrue("Empty Response must be set", response.getData().isEmpty());
    }
}
