package org.jrazdacha.announce;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.jrazdacha.announce.BencodeUtils.decodeMapFrom;
import static org.jrazdacha.announce.BencodeUtils.decodePeersFrom;
import static org.jrazdacha.announce.BencodeUtils.decodeStringFrom;
import static org.jrazdacha.announce.Constants.FAILURE_REASON;
import static org.jrazdacha.announce.Constants.INFO_HASH;
import static org.jrazdacha.announce.Constants.MISSING_PARAMETER_IN_REQUEST;
import static org.jrazdacha.announce.Constants.TORRENT_NOT_FOUND;
import static org.jrazdacha.dao.creator.PeerCreator.expectedPeers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.jrazdacha.announce.exception.TorrentNotFoundException;
import org.jrazdacha.dao.creator.MockRequest;
import org.jrazdacha.model.Peer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:daos.xml" })
@Transactional
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class AnnounceHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    public static final String TEST_INFO_HASH = "B26138C2AB7CC8FEA657C6154A052D37DD1DA6D3";

    public static final String TEST_NON_EXISTING_INFO_HASH = "470644FA0157ED1882EAF7D2FD3AC4795742AFCD";

    public AnnounceHandlerTest() {
        super();
    }

    /**
	 * Tests peer seeding from bittorrent tracker
	 * 
	 * @throws Exception
	 */
    @Test
    public void test_Get_Peers_With_Same_Info_Hash() throws Exception {
        MockHttpServletRequest request = MockRequest.create();
        MockHttpServletResponse response = new MockHttpServletResponse();
        announceHandler(request).handleRequest(request, response);
        Map<String, Object> answer = decodeMapFrom(response.getContentAsByteArray());
        assertPeers(decodePeersFrom(answer), expectedPeers());
    }

    /**
	 * Tests tracker behavior with requesting non-existen torrent
	 * 
	 * @throws Exception
	 */
    @Test
    public void test_Requesting_Non_Existen_Torrent() throws Exception {
        MockHttpServletRequest request = MockRequest.createWithNonExistentHash();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AnnounceHandler handler = announceHandlerWithException(request);
        handler.handleRequest(request, response);
        String expectedFailureReason = TORRENT_NOT_FOUND + TEST_NON_EXISTING_INFO_HASH;
        Map<String, Object> answer = decodeMapFrom(response.getContentAsByteArray());
        String actualFailureReason = decodeStringFrom(answer.get(FAILURE_REASON));
        assertEquals(expectedFailureReason, actualFailureReason);
    }

    /**
	 * Tests tracker behavior with empty request
	 * 
	 * @throws Exception
	 */
    @Test
    public void test_Requesting_Without_Parameteres() throws Exception {
        MockHttpServletRequest request = MockRequest.createEmpty();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AnnounceHandler handler = new AnnounceHandler();
        handler.handleRequest(request, response);
        String expectedFailureReason = MISSING_PARAMETER_IN_REQUEST + INFO_HASH;
        Map<String, Object> answer = decodeMapFrom(response.getContentAsByteArray());
        String actualFailureReason = decodeStringFrom(answer.get(FAILURE_REASON));
        assertEquals(expectedFailureReason, actualFailureReason);
    }

    /**
	 * Tests tracker behavior with empty request
	 * 
	 * @throws Exception
	 */
    @Test
    public void test_Requesting_Without_No_Peer_Id() throws Exception {
        MockHttpServletRequest request = MockRequest.create();
        MockHttpServletResponse response = new MockHttpServletResponse();
        announceHandler(request).handleRequest(request, response);
        Map<String, Object> answer = decodeMapFrom(response.getContentAsByteArray());
        assertPeers(decodePeersFrom(answer), expectedPeers());
    }

    /**
	 * Tests tracker behavior with empty request
	 * 
	 * @throws Exception
	 */
    @Test
    public void test_Requesting_Without_Compact() throws Exception {
        MockHttpServletRequest request = MockRequest.create();
        MockHttpServletResponse response = new MockHttpServletResponse();
        announceHandler(request).handleRequest(request, response);
        Map<String, Object> answer = decodeMapFrom(response.getContentAsByteArray());
        assertPeers(decodePeersFrom(answer), expectedPeers());
    }

    /**
	 * Compares actual peers with expected test peers
	 * 
	 * @param expected
	 *            expected peers
	 * @param actual
	 *            actual peers
	 */
    public void assertPeers(Collection<Peer> expected, Collection<Peer> actual) {
        Iterator<Peer> expectedIterator = expected.iterator();
        Iterator<Peer> actualIterator = actual.iterator();
        while (expectedIterator.hasNext()) {
            Peer actualPeer = actualIterator.next();
            Peer expectedPeer = expectedIterator.next();
            assertEquals(expectedPeer.getIp(), actualPeer.getIp());
            assertEquals(expectedPeer.getPort(), actualPeer.getPort());
            assertEquals(expectedPeer.getPeerId(), actualPeer.getPeerId());
        }
        assertFalse(actualIterator.hasNext());
    }

    /**
	 * Parameterized factory method which returns announce http handler
	 * 
	 * @param request
	 * @return announce http handler
	 * @throws NoSuchMethodException
	 * @throws UnsupportedEncodingException
	 */
    protected AnnounceHandler announceHandler(MockHttpServletRequest request) throws NoSuchMethodException, UnsupportedEncodingException {
        AnnounceHandler handler = createAnnounceHandlerMock();
        expect(handler.formPeers(request)).andReturn(expectedPeers());
        replay(handler);
        return handler;
    }

    /**
	 * Creates announce http handler with mocked formPeers() method
	 * 
	 * @return announce http handler with mocked formPeers() method
	 * @throws NoSuchMethodException
	 */
    protected AnnounceHandler createAnnounceHandlerMock() throws NoSuchMethodException {
        AnnounceHandler servlet = createMock(AnnounceHandler.class, AnnounceHandler.class.getMethod("formPeers", HttpServletRequest.class));
        return servlet;
    }

    /**
	 * Parameterized factory method which returns announce http handler which
	 * throws exception on serching torrent with this info hash
	 * 
	 * @param request
	 * @return announce http handler which throws exception on serching torrent
	 *         with this info hash
	 * @throws NoSuchMethodException
	 * @throws UnsupportedEncodingException
	 */
    protected AnnounceHandler announceHandlerWithException(MockHttpServletRequest request) throws NoSuchMethodException, UnsupportedEncodingException {
        AnnounceHandler handler = createAnnounceHandlerMock();
        expect(handler.formPeers(request)).andThrow(new TorrentNotFoundException(TEST_NON_EXISTING_INFO_HASH));
        replay(handler);
        return handler;
    }
}
