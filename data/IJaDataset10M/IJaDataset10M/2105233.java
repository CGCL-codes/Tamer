package net.sf.osadm.linedata.verify;

import java.util.Arrays;
import java.util.List;
import net.sf.osadm.linedata.LineDataMessageItemVisitor;
import net.sf.osadm.linedata.LineDataMessageStoreImpl;
import net.sf.osadm.linedata.TableDataFactoryImpl;
import net.sf.osadm.linedata.TextLineDataMessageItemVisitor;
import net.sf.osadm.linedata.domain.FieldNameRowImpl;
import net.sf.osadm.linedata.domain.TableData;
import net.sf.osadm.linedata.domain.TableDataFactory;
import net.sf.osadm.linedata.verify.ValueVerify;
import net.sf.osadm.linedata.verify.ValueVerifyByRegExp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ValueVerifyByRegExpTest {

    private String fieldFirstName = "first-name";

    private String fieldLastName = "last-name";

    private List<String> fieldNameList;

    private TableDataFactory factory;

    private LineDataMessageStoreImpl messageStore;

    @Before
    public void setUp() {
        fieldNameList = Arrays.asList(fieldFirstName, fieldLastName);
        factory = new TableDataFactoryImpl(fieldNameList);
        messageStore = new LineDataMessageStoreImpl();
    }

    @Test
    public void testIsValid() {
        TableData donaldLineData = factory.create(null, 1, Arrays.asList("Donald", "Duck"));
        TableData goofyLineData = factory.create(null, 2, Arrays.asList("Goofy", null));
        TableData mickeyLineData = factory.create(null, 3, Arrays.asList("Mickey", "Mouse"));
        ValueVerify verify = new ValueVerifyByRegExp(".*y$", "Value should end with character 'y'.");
        Assert.assertFalse(verify.isValid(donaldLineData, fieldFirstName, messageStore));
        Assert.assertTrue(verify.isValid(goofyLineData, fieldFirstName, messageStore));
        Assert.assertTrue(verify.isValid(mickeyLineData, fieldFirstName, messageStore));
        Assert.assertFalse(messageStore.isEmpty());
        LineDataMessageItemVisitor visitor = new TextLineDataMessageItemVisitor();
        messageStore.accept(visitor);
        System.out.println(visitor);
    }
}
