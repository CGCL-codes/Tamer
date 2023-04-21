package com.gorillalogic.test.cases.gxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gorillalogic.gxml.GxmlException;
import com.gorillalogic.gxml.parser.GxmlDataEntity;
import com.gorillalogic.gxml.parser.GxmlMetaAttribute;
import com.gorillalogic.gxml.parser.GxmlMetaEntity;
import com.gorillalogic.gxml.parser.GxmlParser;

public class GxmlSimplest {

    private static GxmlParser parser;

    @BeforeClass
    public static void init() throws GxmlException {
        String path = System.getProperty("gorilla.config.gorillaHome") + "/scripts/model/gxml/";
        parser = new GxmlParser();
        parser.parse(new File(path + "simplest.gxml"));
    }

    @Test
    public void testMeta() {
        assertEquals(1, parser.getMetaEntities().size());
        assertTrue(parser.getMetaEntities().keySet().contains("Team"));
        GxmlMetaEntity team = parser.getMetaEntities().get("Team");
        assertEquals("Team", team.getName());
        assertEquals(1, team.getAttributes().size());
        assertTrue(team.getAttributes().keySet().contains("id"));
        GxmlMetaAttribute id = team.getAttributes().get("id");
        assertEquals("id", id.getName());
        assertEquals("@int", id.getType());
        assertEquals("0", id.getValue());
        assertEquals(0, team.getAssociations().size());
    }

    @Test
    public void testMetaGosh() {
        StringBuilder sb = new StringBuilder();
        sb.append("set -t\n");
        sb.append("cd /home/model\n");
        sb.append("begin\n");
        sb.append("\n");
        sb.append("-- CLASSES ----\n");
        sb.append("  add new /Entity\n");
        sb.append("    set name = \"Team\"\n");
        sb.append("    set abstract = false\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("-- ATTRIBUTES ----\n");
        sb.append("  cd ::Team\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"id\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @int\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("    set key = \"id\"\n");
        sb.append("  back\n");
        sb.append("\n");
        sb.append("-- ASSOCIATIONS ----\n");
        sb.append("end\n");
        assertEquals(sb.toString(), parser.getMeta());
    }

    @Test
    public void testData() {
        List<GxmlDataEntity> entities = parser.getDataEntities();
        assertEquals(1, entities.size());
        GxmlDataEntity teamData = entities.get(0);
        assertEquals("Team", teamData.getName());
        assertEquals("1", teamData.getId());
        assertEquals("/Team@\"1\"", teamData.getKey());
    }

    @Test
    public void testDataGosh() {
        StringBuilder sb = new StringBuilder();
        sb.append("cd /home\n");
        sb.append("begin\n");
        sb.append("  add new Team\n");
        sb.append("    set id = \"1\"\n");
        sb.append("  end\n");
        sb.append("end\n");
        assertEquals(sb.toString(), parser.getData());
    }
}
