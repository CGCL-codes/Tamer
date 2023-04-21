package pub.test;

import pub.db.command.AddGene;
import pub.db.GeneTable;
import pub.utils.StringUtils;
import pub.beans.BeanFactory;
import pub.beans.GeneBean;

public class AddGeneTest extends DatabaseTestCase {

    public void testAddingGene() {
        AddGene cmd = new AddGene(conn);
        cmd.setGeneName("dyoo");
        cmd.setUserId(MAINTENANCE_USER_ID);
        String newGeneId = cmd.execute();
        assertTrue(StringUtils.isNotEmpty(newGeneId));
        GeneTable table = new GeneTable(conn);
        assertEquals(newGeneId, "" + table.getGeneIdFromName("dyoo"));
    }

    public void testNameCaseSensitivity() {
        AddGene cmd = new AddGene(conn);
        String weirdName = "uPpErCaSe MaTtErS";
        cmd.setGeneName(weirdName);
        cmd.setUserId(MAINTENANCE_USER_ID);
        String newGeneId = cmd.execute();
        assertTrue(StringUtils.isNotEmpty(newGeneId));
        GeneBean geneBean = BeanFactory.getGeneBean(conn, newGeneId);
        assertEquals(weirdName, geneBean.getName());
    }
}
