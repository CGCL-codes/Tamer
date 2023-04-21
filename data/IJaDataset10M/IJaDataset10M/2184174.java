package com.adobe.ac.ncss.files;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import com.adobe.ac.ncss.utils.FileUtils;

public class TestAs3 extends TestCase {

    private List<String> abstractRowDataContent;

    private AbstractFlexFile abstractRowData;

    @Test
    public void testGetFunctionsCount() throws URISyntaxException {
        abstractRowDataContent = FileUtils.readFile(new File(this.getClass().getResource("/com/adobe/ac/ncss/flexunit/AbstractRowData.as").toURI()));
        abstractRowData = new As3(abstractRowDataContent);
        assertEquals(7, abstractRowData.getFunctionsCount());
    }

    @Test
    public void testGetLinesOfCode() throws URISyntaxException {
        abstractRowDataContent = FileUtils.readFile(new File(this.getClass().getResource("/com/adobe/ac/ncss/flexunit/AbstractRowData.as").toURI()));
        abstractRowData = new As3(abstractRowDataContent);
        assertEquals(18, abstractRowData.getLinesOfCode());
    }

    @Test
    public void testGetLinesOfComments() throws URISyntaxException {
        abstractRowDataContent = FileUtils.readFile(new File(this.getClass().getResource("/com/adobe/ac/ncss/flexunit/AbstractRowData.as").toURI()));
        abstractRowData = new As3(abstractRowDataContent);
        assertEquals(46, abstractRowData.getLinesOfComments());
    }
}
