package ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.history;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.sql.Timestamp;
import org.junit.Test;
import ca.ucalgary.cpsc.ase.util.FileUtils;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.structure.HTMLDoc;
import ca.ucalgary.cpsc.ebe.fitClipseRunner.core.data.xml.Converter;

public class ConverterTest {

    @Test
    public void whatGoesInMustComeOut() throws Exception {
        HTMLDoc table = new HTMLDoc(FileUtils.readAll(new File("testData/ConverterTest/table.html")));
        Object obj = new ResultTable(table.testObjects().get(0), new Timestamp(0L), new Timestamp(0L));
        assertEquals(obj, Converter.fromXml(Converter.toXml(obj)));
    }
}
