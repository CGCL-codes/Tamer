package org.openremote.web.console.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.openremote.web.console.domain.Group;
import org.openremote.web.console.domain.PanelXmlEntity;

/**
 * The Class XmlParserUtilTest.
 */
public class XmlParserUtilTest {

    /**
    * Test get panel names from panels.xml.
    */
    @Test
    public void testGetPanelNames() {
        String path = getClass().getResource("fixture").getPath() + "/panels.xml";
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> panels = XmlParserUtil.parsePanelNamesFromInputStream(fis);
        Assert.assertEquals(6, panels.size());
    }

    @Test
    public void testGetPanelXmlEntity() {
        String path = getClass().getResource("fixture").getPath() + "/panel_absolute_screen_backgroundimage.xml";
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PanelXmlEntity panelXmlEntity = XmlParserUtil.parsePanelXmlFromInputStream(fis);
        Map<Integer, Group> groups = panelXmlEntity.getGroups();
        Assert.assertEquals(2, groups.size());
        Assert.assertEquals(2, panelXmlEntity.getScreens().size());
        Assert.assertEquals(1, groups.get(1).getScreens().size());
    }
}
