package com.gorillalogic.test.cases.gxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.io.File;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gorillalogic.gxml.GxmlException;
import com.gorillalogic.gxml.parser.GxmlDataEntity;
import com.gorillalogic.gxml.parser.GxmlMetaAssociation;
import com.gorillalogic.gxml.parser.GxmlMetaAttribute;
import com.gorillalogic.gxml.parser.GxmlMetaEntity;
import com.gorillalogic.gxml.parser.GxmlParser;

public class GxmlFootballReferences {

    private static GxmlParser parser;

    @BeforeClass
    public static void init() throws GxmlException {
        String path = System.getProperty("gorilla.config.gorillaHome") + "/scripts/model/gxml/";
        parser = new GxmlParser();
        parser.parse(new File(path + "football-references.gxml"));
    }

    @Test
    public void testMeta() {
        assertEquals(2, parser.getMetaEntities().size());
        GxmlMetaEntity team = parser.getMetaEntities().get("Team");
        assertEquals("Team", team.getName());
        GxmlMetaEntity player = parser.getMetaEntities().get("Player");
        assertEquals("Player", player.getName());
        assertEquals(2, team.getAttributes().size());
        GxmlMetaAttribute id = team.getAttributes().get("id");
        assertEquals("id", id.getName());
        assertEquals("@int", id.getType());
        assertFalse(id.isGCL());
        GxmlMetaAttribute name = team.getAttributes().get("name");
        assertEquals("name", name.getName());
        assertEquals("@string", name.getType());
        assertFalse(name.isGCL());
        assertEquals(4, player.getAttributes().size());
        id = player.getAttributes().get("id");
        assertEquals("id", id.getName());
        assertEquals("@int", id.getType());
        assertFalse(id.isGCL());
        name = player.getAttributes().get("name");
        assertEquals("name", name.getName());
        assertEquals("@string", name.getType());
        assertFalse(name.isGCL());
        GxmlMetaAttribute number = player.getAttributes().get("number");
        assertEquals("number", number.getName());
        assertEquals("@int", number.getType());
        assertFalse(number.isGCL());
        GxmlMetaAttribute salary = player.getAttributes().get("salary");
        assertEquals("salary", salary.getName());
        assertEquals("@int", salary.getType());
        assertFalse(salary.isGCL());
        assertEquals(1, team.getAssociations().size());
        GxmlMetaAssociation players = team.getAssociations().get("Team_Player_players");
        assertEquals("players", players.getName());
        assertEquals("Team", players.getSource());
        assertEquals("Player", players.getTarget());
        assertEquals(0, player.getAssociations().size());
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
        sb.append("    set name = \"Player\"\n");
        sb.append("    set abstract = false\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("  add new /Entity\n");
        sb.append("    set name = \"Team\"\n");
        sb.append("    set abstract = false\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("-- ATTRIBUTES ----\n");
        sb.append("  cd ::Player\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"id\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @int\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("    set key = \"id\"\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"name\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @string\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"number\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @int\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"salary\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @int\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("  back\n");
        sb.append("\n");
        sb.append("  cd ::Team\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"id\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @int\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("    set key = \"id\"\n");
        sb.append("    add new !Attr to owns\n");
        sb.append("      set name = \"name\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @string\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("    end\n");
        sb.append("  back\n");
        sb.append("\n");
        sb.append("-- ASSOCIATIONS ----\n");
        sb.append("  add new !Association\n");
        sb.append("    set name = \"Team_Player_players\"\n");
        sb.append("    set abstract = false\n");
        sb.append("    add new !Participant to parts\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @Team\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("      set kind = \"composite\"\n");
        sb.append("      set nav = true\n");
        sb.append("    end\n");
        sb.append("    add new !Participant to parts\n");
        sb.append("      set name = \"players\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @Player\n");
        sb.append("      set min = 0\n");
        sb.append("      set max = *\n");
        sb.append("      set kind = \"simple\"\n");
        sb.append("      set nav = true\n");
        sb.append("    end\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("end\n");
        assertEquals(sb.toString(), parser.getMeta());
    }

    @Test
    public void testData() {
        List<GxmlDataEntity> entities = parser.getDataEntities();
        assertEquals(9, entities.size());
        assertEquals("/Player@\"2\"", entities.get(0).getKey());
        assertEquals("/Player@\"3\"", entities.get(1).getKey());
        assertEquals("/Player@\"4\"", entities.get(2).getKey());
        assertEquals("/Player@\"5\"", entities.get(3).getKey());
        assertEquals("/Player@\"7\"", entities.get(4).getKey());
        assertEquals("/Player@\"8\"", entities.get(5).getKey());
        assertEquals("/Player@\"9\"", entities.get(6).getKey());
        assertEquals("/Team@\"1\"", entities.get(7).getKey());
        assertEquals("/Team@\"6\"", entities.get(8).getKey());
    }

    @Test
    public void testDataGosh() {
        StringBuilder sb = new StringBuilder();
        sb.append("cd /home\n");
        sb.append("begin\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"2\"\n");
        sb.append("    set name = \"Jay Cutler\"\n");
        sb.append("    set number = \"6\"\n");
        sb.append("    set salary = \"6497500\"\n");
        sb.append("  end\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"3\"\n");
        sb.append("    set name = \"Champ Bailey\"\n");
        sb.append("    set number = \"24\"\n");
        sb.append("    set salary = \"8003050\"\n");
        sb.append("  end\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"4\"\n");
        sb.append("    set name = \"Eddie Royal\"\n");
        sb.append("    set number = \"19\"\n");
        sb.append("    set salary = \"2539830\"\n");
        sb.append("  end\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"5\"\n");
        sb.append("    set name = \"Tony Scheffler\"\n");
        sb.append("    set number = \"88\"\n");
        sb.append("    set salary = \"612480\"\n");
        sb.append("  end\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"7\"\n");
        sb.append("    set name = \"JaMarcus Russell\"\n");
        sb.append("    set number = \"2\"\n");
        sb.append("    set salary = \"16872400\"\n");
        sb.append("  end\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"8\"\n");
        sb.append("    set name = \"Darren McFadden\"\n");
        sb.append("    set number = \"20\"\n");
        sb.append("    set salary = \"4375000\"\n");
        sb.append("  end\n");
        sb.append("  add new Player\n");
        sb.append("    set id = \"9\"\n");
        sb.append("    set name = \"Sebastian Janikowski\"\n");
        sb.append("    set number = \"11\"\n");
        sb.append("    set salary = \"2625000\"\n");
        sb.append("  end\n");
        sb.append("  add new Team\n");
        sb.append("    set id = \"1\"\n");
        sb.append("    set name = \"Denver Broncos\"\n");
        sb.append("  end\n");
        sb.append("  add new Team\n");
        sb.append("    set id = \"6\"\n");
        sb.append("    set name = \"Oakland Raiders\"\n");
        sb.append("  end\n");
        sb.append("  cd /Team@\"1\"\n");
        sb.append("    add /Player@\"2\" to players\n");
        sb.append("    add /Player@\"3\" to players\n");
        sb.append("    add /Player@\"4\" to players\n");
        sb.append("    add /Player@\"5\" to players\n");
        sb.append("  back\n");
        sb.append("  cd /Team@\"6\"\n");
        sb.append("    add /Player@\"7\" to players\n");
        sb.append("    add /Player@\"8\" to players\n");
        sb.append("    add /Player@\"9\" to players\n");
        sb.append("  back\n");
        sb.append("end\n");
        assertEquals(sb.toString(), parser.getData());
    }
}
