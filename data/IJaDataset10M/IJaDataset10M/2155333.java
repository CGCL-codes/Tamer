package wilos.test.presentation.assistant.control;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import wilos.presentation.assistant.control.ServersListParser;
import wilos.presentation.assistant.model.WizardServer;

public class ServersListParserTest extends TestCase {

    public void testGetServersList() {
        ServersListParser slp1 = new ServersListParser("test/wilos/test/presentation/assistant/control/server_test_vide.xml");
        assertTrue(slp1.getServersList().isEmpty());
        ServersListParser slp0 = new ServersListParser("test/wilos/test/presentation/assistant/control/server_test_0.xml");
        assertTrue(slp0.getServersList().size() == 0);
        assertTrue(slp1.getServersList().isEmpty());
        ServersListParser slpbiz = new ServersListParser("test/wilos/test/presentation/assistant/control/server_test_bizarre.xml");
        assertTrue(slpbiz.getServersList().size() == 1);
        ServersListParser slp2 = new ServersListParser("test/wilos/test/presentation/assistant/control/servers_test_ok.xml");
        List<WizardServer> lspl2 = slp2.getServersList();
        assertTrue(lspl2.size() == 2);
        assertTrue(((WizardServer) (lspl2.get(0))).getAlias().equals("marine"));
        assertTrue(((WizardServer) (lspl2.get(0))).getAddress().equals("http://marine.edu.ups-tlse.fr:9014/wilos"));
        assertTrue(((WizardServer) (lspl2.get(1))).getAlias().equals("marine2"));
        assertTrue(((WizardServer) (lspl2.get(1))).getAddress().equals("http://marine.edu.ups-tlse.fr:9014/wilos/truc"));
    }

    public void testSaveServersList() {
        WizardServer ws1 = new WizardServer("lala", "http://marine.edu.ups-tlse.fr:9014/wi", 2);
        WizardServer ws2 = new WizardServer("hello", "http://www.youpi.com", 7);
        WizardServer ws3 = new WizardServer("mon_serveur", "http://blablabla.fr", 3);
        WizardServer ws4 = new WizardServer("le_ptit_dernier", "http://hohoho.fr", 5);
        ServersListParser slp = new ServersListParser("test/wilos/test/presentation/assistant/control/servers_test_save.xml");
        ArrayList<WizardServer> wsl1 = new ArrayList<WizardServer>();
        wsl1.add(ws1);
        wsl1.add(ws2);
        wsl1.add(ws3);
        wsl1.add(ws4);
        slp.saveServersList(wsl1);
        ArrayList<WizardServer> wsl2 = slp.getServersList();
        assertTrue(wsl2.get(0).getAlias().equals(ws1.getAlias()));
        assertTrue(wsl2.get(0).getAddress().equals(ws1.getAddress()));
        assertEquals(wsl2.get(0).getId(), 1);
        assertTrue(wsl2.get(1).getAlias().equals(ws2.getAlias()));
        assertTrue(wsl2.get(1).getAddress().equals(ws2.getAddress()));
        assertEquals(wsl2.get(1).getId(), 2);
        assertTrue(wsl2.get(2).getAlias().equals(ws3.getAlias()));
        assertTrue(wsl2.get(2).getAddress().equals(ws3.getAddress()));
        assertEquals(wsl2.get(2).getId(), 3);
        assertTrue(wsl2.get(3).getAlias().equals(ws4.getAlias()));
        assertTrue(wsl2.get(3).getAddress().equals(ws4.getAddress()));
        assertEquals(wsl2.get(3).getId(), 4);
        slp.lastUsedServer(ws3.getId());
        wsl2 = slp.getServersList();
        assertTrue(wsl2.get(0).getAlias().equals(ws3.getAlias()));
        assertTrue(wsl2.get(0).getAddress().equals(ws3.getAddress()));
        assertEquals(wsl2.get(0).getId(), 1);
        assertTrue(wsl2.get(1).getAlias().equals(ws2.getAlias()));
        assertTrue(wsl2.get(1).getAddress().equals(ws2.getAddress()));
        assertEquals(wsl2.get(1).getId(), 2);
        assertTrue(wsl2.get(2).getAlias().equals(ws1.getAlias()));
        assertTrue(wsl2.get(2).getAddress().equals(ws1.getAddress()));
        assertEquals(wsl2.get(2).getId(), 3);
        assertTrue(wsl2.get(3).getAlias().equals(ws4.getAlias()));
        assertTrue(wsl2.get(3).getAddress().equals(ws4.getAddress()));
        assertEquals(wsl2.get(3).getId(), 4);
    }
}
