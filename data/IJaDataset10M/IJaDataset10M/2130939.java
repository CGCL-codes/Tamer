package net.sourceforge.greenvine.reveng.impl.tests;

import java.io.File;
import junit.framework.Assert;
import net.sourceforge.greenvine.database.Column;
import net.sourceforge.greenvine.database.Table;
import net.sourceforge.greenvine.database.View;
import net.sourceforge.greenvine.reveng.ItemNameExtractor;
import net.sourceforge.greenvine.reveng.config.RevengConfig;
import net.sourceforge.greenvine.reveng.impl.ItemNameExtractorHelper;
import net.sourceforge.greenvine.reveng.impl.ItemNameExtractorImpl;
import net.sourceforge.greenvine.reveng.impl.RevengConfigLoader;
import org.junit.Before;
import org.junit.Test;

public class ItemNameExtractorTest {

    private ItemNameExtractor extractor;

    private ItemNameExtractorHelper helper;

    @Before
    public void setUp() throws Exception {
        extractor = new ItemNameExtractorImpl();
        File revengConfigFile = new File("src/test/resources/reveng.xml");
        RevengConfig revengConfig = RevengConfigLoader.getRevengConfig(revengConfigFile);
        helper = new ItemNameExtractorHelper(extractor, revengConfig);
    }

    @Test
    public void testProcessItemName() throws Exception {
        String original = "THIS_IS_THE_ORIGINAL";
        String expected = "thisIsTheOriginal";
        String processed = extractor.extractItemName(original, "_");
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testProcessItemNameLowerCase() throws Exception {
        String original = "this-is-the-original";
        String expected = "thisIsTheOriginal";
        String processed = extractor.extractItemName(original, "-");
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testProcessItemNamePascalCase() throws Exception {
        String original = "ThisIsTheOriginal";
        String expected = "thisIsTheOriginal";
        String processed = extractor.extractItemName(original, "");
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testProcessItemNamePascalCaseSeparators() throws Exception {
        String original = "This_Is_The_Original";
        String expected = "thisIsTheOriginal";
        String processed = extractor.extractItemName(original, "_");
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testProcessReferenceName() throws Exception {
        String original = "FK_THIS_IS_THE_ORIGINAL_ID";
        String expected = "thisIsTheOriginal";
        String processed = extractor.extractItemName(original, "_", "FK", "ID");
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testExtractTableName() throws Exception {
        String original = "TBL_THIS_IS_THE_ORIGINAL";
        String expected = "thisIsTheOriginal";
        Table table = new Table();
        table.setName(original);
        String processed = helper.extractItemNameFromTable(table);
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testExtractViewName() throws Exception {
        String original = "VIEW_THIS_IS_THE_ORIGINAL";
        String expected = "thisIsTheOriginal";
        View view = new View();
        view.setName(original);
        String processed = helper.extractItemNameFromView(view);
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testExtractPrimaryColumnName() throws Exception {
        String original = "PK_THIS_IS_THE_ORIGINAL";
        String expected = "thisIsTheOriginal";
        Column item = new Column();
        item.setName(original);
        String processed = helper.extractItemNameFromPrimaryColumn(item);
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testExtractForeignColumnName() throws Exception {
        String original = "FK_THIS_IS_THE_ORIGINAL";
        String expected = "thisIsTheOriginal";
        Column item = new Column();
        item.setName(original);
        String processed = helper.extractItemNameFromForeignColumn(item);
        Assert.assertEquals(expected, processed);
    }

    @Test
    public void testExtractDataColumnName() throws Exception {
        String original = "THIS_IS_THE_ORIGINAL";
        String expected = "thisIsTheOriginal";
        Column item = new Column();
        item.setName(original);
        String processed = helper.extractItemNameFromDataColumn(item);
        Assert.assertEquals(expected, processed);
    }
}
