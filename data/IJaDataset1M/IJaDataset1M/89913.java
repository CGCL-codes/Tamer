package lv.odylab.evemanage.application.background.blueprint;

import com.googlecode.objectify.Key;
import lv.odylab.evemanage.domain.blueprint.Blueprint;
import lv.odylab.evemanage.domain.blueprint.BlueprintDao;
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
public class UpdateBlueprintTaskServletTest {

    @Mock
    private BlueprintDao blueprintDao;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    private UpdateBlueprintTaskServlet updateBlueprintTaskServlet;

    @Before
    public void setUp() {
        updateBlueprintTaskServlet = new UpdateBlueprintTaskServlet(blueprintDao);
    }

    @Test
    public void testDoPost() throws Exception {
        Blueprint blueprint = new Blueprint();
        when(blueprintDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(blueprint);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("blueprintID")).thenReturn("2");
        when(httpServletRequest.getParameter("characterID")).thenReturn("3");
        when(httpServletRequest.getParameter("characterName")).thenReturn("characterName");
        when(httpServletRequest.getParameter("corporationID")).thenReturn("4");
        when(httpServletRequest.getParameter("corporationName")).thenReturn("corporationName");
        when(httpServletRequest.getParameter("corporationTicker")).thenReturn("corporationTicker");
        when(httpServletRequest.getParameter("allianceID")).thenReturn("5");
        when(httpServletRequest.getParameter("allianceName")).thenReturn("allianceName");
        updateBlueprintTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(blueprintDao).putWithoutChecks(blueprint);
        CharacterInfo characterInfo = blueprint.getAttachedCharacterInfo();
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
        Blueprint blueprint = new Blueprint();
        when(blueprintDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(blueprint);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("blueprintID")).thenReturn("2");
        when(httpServletRequest.getParameter("characterID")).thenReturn("3");
        when(httpServletRequest.getParameter("characterName")).thenReturn("characterName");
        updateBlueprintTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(blueprintDao).putWithoutChecks(blueprint);
        CharacterInfo characterInfo = blueprint.getAttachedCharacterInfo();
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
        Blueprint blueprint = new Blueprint();
        when(blueprintDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(blueprint);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("blueprintID")).thenReturn("2");
        when(httpServletRequest.getParameter("characterID")).thenReturn("3");
        when(httpServletRequest.getParameter("characterName")).thenReturn("characterName");
        when(httpServletRequest.getParameter("corporationID")).thenReturn("4");
        when(httpServletRequest.getParameter("corporationName")).thenReturn("corporationName");
        when(httpServletRequest.getParameter("corporationTicker")).thenReturn("corporationTicker");
        updateBlueprintTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(blueprintDao).putWithoutChecks(blueprint);
        CharacterInfo characterInfo = blueprint.getAttachedCharacterInfo();
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
        Blueprint blueprint = new Blueprint();
        when(blueprintDao.get(2L, new Key<User>(User.class, 1L))).thenReturn(blueprint);
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("blueprintID")).thenReturn("2");
        updateBlueprintTaskServlet.doPost(httpServletRequest, httpServletResponse);
        verify(blueprintDao).putWithoutChecks(blueprint);
        assertNull(blueprint.getAttachedCharacterInfo());
    }

    @Test
    public void testDoPost_Exception() throws Exception {
        when(httpServletRequest.getParameter("userID")).thenReturn("1");
        when(httpServletRequest.getParameter("blueprintID")).thenReturn("2");
        doThrow(new RuntimeException()).when(blueprintDao).putWithoutChecks(any(Blueprint.class));
        updateBlueprintTaskServlet.doPost(httpServletRequest, httpServletResponse);
    }

    @Test
    public void testDoPost_Throwable() throws Exception {
        updateBlueprintTaskServlet.doPost(httpServletRequest, httpServletResponse);
    }
}
