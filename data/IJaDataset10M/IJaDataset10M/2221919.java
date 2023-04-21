package lv.odylab.evemanage.application.background.priceset;

import com.googlecode.objectify.Key;
import lv.odylab.evemanage.domain.priceset.PriceSet;
import lv.odylab.evemanage.domain.priceset.PriceSetDao;
import lv.odylab.evemanage.domain.user.CharacterInfo;
import lv.odylab.evemanage.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePriceSetTaskServletTest {

    @Mock
    private PriceSetDao priceSetDao;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    private UpdatePriceSetTaskServlet updatePriceSetTaskServlet;

    @Before
    public void setUp() {
        updatePriceSetTaskServlet = new UpdatePriceSetTaskServlet(priceSetDao);
    }

    @Test
    public void testDoPost() throws Exception {
        PriceSet priceSet = new PriceSet();
        when(priceSetDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(priceSet);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("priceSetID")).thenReturn("2");
        when(httpServletRequest.getParameter("characterID")).thenReturn("3");
        when(httpServletRequest.getParameter("characterName")).thenReturn("characterName");
        when(httpServletRequest.getParameter("corporationID")).thenReturn("4");
        when(httpServletRequest.getParameter("corporationName")).thenReturn("corporationName");
        when(httpServletRequest.getParameter("corporationTicker")).thenReturn("corporationTicker");
        when(httpServletRequest.getParameter("allianceID")).thenReturn("5");
        when(httpServletRequest.getParameter("allianceName")).thenReturn("allianceName");
        updatePriceSetTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(priceSetDao).putWithoutChecks(priceSet);
        CharacterInfo characterInfo = priceSet.getAttachedCharacterInfo();
        assertNotNull(characterInfo);
        assertEquals(Long.valueOf(3), characterInfo.getCharacterID());
        assertEquals("characterName", characterInfo.getName());
        assertEquals(Long.valueOf(4), characterInfo.getCorporationID());
        assertEquals("corporationName", characterInfo.getCorporationName());
        assertEquals("corporationTicker", characterInfo.getCorporationTicker());
        assertEquals(Long.valueOf(5), characterInfo.getAllianceID());
        assertEquals("allianceName", characterInfo.getAllianceName());
    }

    @Test
    public void testDoPost_NoCorporation() throws Exception {
        PriceSet priceSet = new PriceSet();
        when(priceSetDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(priceSet);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("priceSetID")).thenReturn("2");
        when(httpServletRequest.getParameter("characterID")).thenReturn("3");
        when(httpServletRequest.getParameter("characterName")).thenReturn("characterName");
        updatePriceSetTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(priceSetDao).putWithoutChecks(priceSet);
        CharacterInfo characterInfo = priceSet.getAttachedCharacterInfo();
        assertNotNull(characterInfo);
        assertEquals(Long.valueOf(3), characterInfo.getCharacterID());
        assertEquals("characterName", characterInfo.getName());
        assertNull(characterInfo.getCorporationID());
        assertNull(characterInfo.getCorporationName());
        assertNull(characterInfo.getCorporationTicker());
        assertNull(characterInfo.getAllianceID());
        assertNull(characterInfo.getAllianceName());
    }

    @Test
    public void testDoPost_NoAlliance() throws Exception {
        PriceSet priceSet = new PriceSet();
        when(priceSetDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(priceSet);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("priceSetID")).thenReturn("2");
        when(httpServletRequest.getParameter("characterID")).thenReturn("3");
        when(httpServletRequest.getParameter("characterName")).thenReturn("characterName");
        when(httpServletRequest.getParameter("corporationID")).thenReturn("4");
        when(httpServletRequest.getParameter("corporationName")).thenReturn("corporationName");
        when(httpServletRequest.getParameter("corporationTicker")).thenReturn("corporationTicker");
        updatePriceSetTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(priceSetDao).putWithoutChecks(priceSet);
        CharacterInfo characterInfo = priceSet.getAttachedCharacterInfo();
        assertNotNull(characterInfo);
        assertEquals(Long.valueOf(3), characterInfo.getCharacterID());
        assertEquals("characterName", characterInfo.getName());
        assertEquals(Long.valueOf(4), characterInfo.getCorporationID());
        assertEquals("corporationName", characterInfo.getCorporationName());
        assertEquals("corporationTicker", characterInfo.getCorporationTicker());
        assertNull(characterInfo.getAllianceID());
        assertNull(characterInfo.getAllianceName());
    }

    @Test
    public void testDoPost_Detach() throws Exception {
        PriceSet priceSet = new PriceSet();
        when(priceSetDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(priceSet);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("priceSetID")).thenReturn("2");
        updatePriceSetTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(priceSetDao).putWithoutChecks(priceSet);
        assertNull(priceSet.getAttachedCharacterInfo());
    }

    @Test
    public void testDoPost_Exception() throws Exception {
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("priceSetID")).thenReturn("2");
        doThrow(new RuntimeException()).when(priceSetDao).putWithoutChecks(any(PriceSet.class));
        updatePriceSetTaskServlet.doPost(httpServletRequest, httpServletResponse);
    }

    @Test
    public void testDoPost_Throwable() throws Exception {
        updatePriceSetTaskServlet.doPost(httpServletRequest, httpServletResponse);
    }
}
