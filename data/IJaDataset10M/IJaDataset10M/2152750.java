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

public class GxmlBookBidirectional {

    private static GxmlParser parser;

    @BeforeClass
    public static void init() throws GxmlException {
        String path = System.getProperty("gorilla.config.gorillaHome") + "/scripts/model/gxml/";
        parser = new GxmlParser();
        parser.parse(new File(path + "book-bidirectional.gxml"));
    }

    @Test
    public void testMeta() {
        assertEquals(2, parser.getMetaEntities().size());
        GxmlMetaEntity author = parser.getMetaEntities().get("Author");
        assertEquals("Author", author.getName());
        GxmlMetaEntity book = parser.getMetaEntities().get("Book");
        assertEquals("Book", book.getName());
        assertEquals(2, author.getAttributes().size());
        GxmlMetaAttribute id = author.getAttributes().get("id");
        assertEquals("id", id.getName());
        assertEquals("@int", id.getType());
        assertFalse(id.isGCL());
        GxmlMetaAttribute name = author.getAttributes().get("name");
        assertEquals("name", name.getName());
        assertEquals("@string", name.getType());
        assertFalse(name.isGCL());
        assertEquals(2, book.getAttributes().size());
        id = book.getAttributes().get("id");
        assertEquals("id", id.getName());
        assertEquals("@int", id.getType());
        assertFalse(id.isGCL());
        name = book.getAttributes().get("name");
        assertEquals("name", name.getName());
        assertEquals("@string", name.getType());
        assertFalse(name.isGCL());
        assertEquals(1, author.getAssociations().size());
        GxmlMetaAssociation books = author.getAssociations().get("Author_Book_books");
        assertEquals("books", books.getName());
        assertEquals("Author", books.getSource());
        assertEquals("Book", books.getTarget());
        assertEquals(1, book.getAssociations().size());
        GxmlMetaAssociation authors = book.getAssociations().get("Book_Author_authors");
        assertEquals("authors", authors.getName());
        assertEquals("Book", authors.getSource());
        assertEquals("Author", authors.getTarget());
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
        sb.append("    set name = \"Author\"\n");
        sb.append("    set abstract = false\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("  add new /Entity\n");
        sb.append("    set name = \"Book\"\n");
        sb.append("    set abstract = false\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("-- ATTRIBUTES ----\n");
        sb.append("  cd ::Author\n");
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
        sb.append("  cd ::Book\n");
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
        sb.append("    set name = \"Author_Book_books\"\n");
        sb.append("    set abstract = false\n");
        sb.append("    add new !Participant to parts\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @Author\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("      set kind = \"composite\"\n");
        sb.append("      set nav = true\n");
        sb.append("    end\n");
        sb.append("    add new !Participant to parts\n");
        sb.append("      set name = \"books\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @Book\n");
        sb.append("      set min = 0\n");
        sb.append("      set max = *\n");
        sb.append("      set kind = \"simple\"\n");
        sb.append("      set nav = true\n");
        sb.append("    end\n");
        sb.append("  end\n");
        sb.append("\n");
        sb.append("  add new !Association\n");
        sb.append("    set name = \"Book_Author_authors\"\n");
        sb.append("    set abstract = false\n");
        sb.append("    add new !Participant to parts\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @Book\n");
        sb.append("      set min = 1\n");
        sb.append("      set max = 1\n");
        sb.append("      set kind = \"composite\"\n");
        sb.append("      set nav = true\n");
        sb.append("    end\n");
        sb.append("    add new !Participant to parts\n");
        sb.append("      set name = \"authors\"\n");
        sb.append("      set abstract = false\n");
        sb.append("      set type = @Author\n");
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
        assertEquals(6, entities.size());
        assertEquals("/Author@\"2\"", entities.get(0).getKey());
        assertEquals("/Author@\"3\"", entities.get(1).getKey());
        assertEquals("/Author@\"5\"", entities.get(2).getKey());
        assertEquals("/Author@\"6\"", entities.get(3).getKey());
        assertEquals("/Book@\"1\"", entities.get(4).getKey());
        assertEquals("/Book@\"4\"", entities.get(5).getKey());
    }

    @Test
    public void testDataGosh() {
        StringBuilder sb = new StringBuilder();
        sb.append("cd /home\n");
        sb.append("begin\n");
        sb.append("  add new Author\n");
        sb.append("    set id = \"2\"\n");
        sb.append("    set name = \"Chafic Kazoun\"\n");
        sb.append("  end\n");
        sb.append("  add new Author\n");
        sb.append("    set id = \"3\"\n");
        sb.append("    set name = \"Joey Lott\"\n");
        sb.append("  end\n");
        sb.append("  add new Author\n");
        sb.append("    set id = \"5\"\n");
        sb.append("    set name = \"Darron Schall\"\n");
        sb.append("  end\n");
        sb.append("  add new Author\n");
        sb.append("    set id = \"6\"\n");
        sb.append("    set name = \"Keith Peters\"\n");
        sb.append("  end\n");
        sb.append("  add new Book\n");
        sb.append("    set id = \"1\"\n");
        sb.append("    set name = \"Programming Flex 3\"\n");
        sb.append("  end\n");
        sb.append("  add new Book\n");
        sb.append("    set id = \"4\"\n");
        sb.append("    set name = \"ActionScript 3.0 Cookbook\"\n");
        sb.append("  end\n");
        sb.append("  cd /Author@\"2\"\n");
        sb.append("    add /Book@\"1\" to books\n");
        sb.append("  back\n");
        sb.append("  cd /Author@\"3\"\n");
        sb.append("    add /Book@\"1\" to books\n");
        sb.append("    add /Book@\"4\" to books\n");
        sb.append("  back\n");
        sb.append("  cd /Author@\"5\"\n");
        sb.append("    add /Book@\"4\" to books\n");
        sb.append("  back\n");
        sb.append("  cd /Author@\"6\"\n");
        sb.append("    add /Book@\"4\" to books\n");
        sb.append("  back\n");
        sb.append("  cd /Book@\"1\"\n");
        sb.append("    add /Author@\"2\" to authors\n");
        sb.append("    add /Author@\"3\" to authors\n");
        sb.append("  back\n");
        sb.append("  cd /Book@\"4\"\n");
        sb.append("    add /Author@\"3\" to authors\n");
        sb.append("    add /Author@\"5\" to authors\n");
        sb.append("    add /Author@\"6\" to authors\n");
        sb.append("  back\n");
        sb.append("end\n");
        assertEquals(sb.toString(), parser.getData());
    }
}
