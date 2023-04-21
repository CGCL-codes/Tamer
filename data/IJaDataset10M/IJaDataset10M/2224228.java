package org.databene.benerator.consumer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.databene.benerator.consumer.ConsoleExporter;
import org.databene.benerator.test.ModelTest;
import org.databene.commons.Patterns;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.TimestampFormatter;
import org.databene.model.data.Entity;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link ConsoleExporter}.<br/><br/>
 * Created at 11.04.2008 06:58:53
 * @since 0.5.2
 * @author Volker Bergmann
 */
public class ConsoleExporterTest extends ModelTest {

    private static final String LF = SystemInfo.getLineSeparator();

    @Test
    public void testSimpleTypes() {
        check("Test" + LF, "Test");
        check("1" + LF, 1);
        check("1" + LF, 1.);
        check("true" + LF, true);
    }

    @Test
    public void testDate() {
        Date date = new Date(((60 + 2) * 60 + 3) * 1000);
        check(new SimpleDateFormat("yyyy-MM-dd").format(date) + LF, date);
    }

    @Test
    public void testTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        TimestampFormatter formatter = new TimestampFormatter(Patterns.DEFAULT_DATETIME_SECONDS_PATTERN + '.');
        check(formatter.format(timestamp) + LF, timestamp);
    }

    @Test
    public void testEntity() {
        Entity entity = createEntity("e", "i", 3, "d", 5., "s", "sss");
        check("e[i=3, d=5, s=sss]" + LF, entity);
    }

    @Test
    public void testLimit() {
        Entity entity = createEntity("e", "i", 3, "d", 5., "s", "sss");
        check(new ConsoleExporter(1L), "e[i=3, d=5, s=sss]" + LF + '.', entity, entity);
    }

    @Test
    public void testIndent() {
        Entity entity = createEntity("e", "i", 3, "d", 5., "s", "sss");
        check(new ConsoleExporter(-1L, "xxx"), "xxxe[i=3, d=5, s=sss]" + LF, entity);
    }

    private void check(String expectedOut, Object... ins) {
        check(new ConsoleExporter(), expectedOut, ins);
    }

    private void check(ConsoleExporter exporter, String expectedOut, Object... ins) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        exporter.setOut(new PrintStream(stream));
        try {
            for (Object in : ins) {
                exporter.startProductConsumption(in);
                exporter.finishProductConsumption(in);
            }
            exporter.flush();
            assertEquals(expectedOut, stream.toString());
        } finally {
            exporter.close();
        }
    }
}
