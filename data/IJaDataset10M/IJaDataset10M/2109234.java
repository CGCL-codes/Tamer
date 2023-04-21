package net.sourceforge.obexftpfrontend.command;

import java.util.LinkedList;
import java.util.List;
import net.sourceforge.obexftpfrontend.model.Configuration;
import net.sourceforge.obexftpfrontend.model.ConfigurationHolder;
import net.sourceforge.obexftpfrontend.model.OBEXElement;
import net.sourceforge.obexftpfrontend.model.TransportType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * RemoveFilesCommand test cases.
 * @author Daniel F. Martins
 */
public class RemoveFilesCommandTest {

    private Mockery context = new Mockery();

    @Test
    public void testCreate() {
        final ConfigurationHolder configHolder = context.mock(ConfigurationHolder.class);
        List<OBEXElement> elements = new LinkedList<OBEXElement>();
        elements.add(new OBEXElement("somefile"));
        context.checking(new Expectations() {

            {
                one(configHolder).getConfiguration();
            }
        });
        new RemoveFilesCommand(configHolder, new OBEXElement("something"), elements);
        context.assertIsSatisfied();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNulLParentFolder() {
        final ConfigurationHolder configHolder = context.mock(ConfigurationHolder.class);
        List<OBEXElement> elements = new LinkedList<OBEXElement>();
        elements.add(new OBEXElement("somefile"));
        context.checking(new Expectations() {

            {
                one(configHolder).getConfiguration();
            }
        });
        new RemoveFilesCommand(configHolder, null, elements);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullFiles() {
        final ConfigurationHolder configHolder = context.mock(ConfigurationHolder.class);
        context.checking(new Expectations() {

            {
                one(configHolder).getConfiguration();
            }
        });
        new RemoveFilesCommand(configHolder, new OBEXElement("something"), null);
    }

    @Test
    public void testRemoveFiles() throws Exception {
        final OBEXFTPCommandLineRunner runner = context.mock(OBEXFTPCommandLineRunner.class);
        final ConfigurationHolder configHolder = context.mock(ConfigurationHolder.class);
        final List<String> expectedParams = new LinkedList<String>();
        expectedParams.add("obexftp");
        expectedParams.add("--usb");
        expectedParams.add("dev");
        expectedParams.add("--chdir");
        expectedParams.add("/");
        expectedParams.add("--delete");
        expectedParams.add("somefile");
        List<OBEXElement> elements = new LinkedList<OBEXElement>();
        elements.add(new OBEXElement("somefile"));
        context.checking(new Expectations() {

            {
                one(configHolder).getConfiguration();
                will(returnValue(new Configuration("obexftp", TransportType.USB, "dev", 0, true, true)));
                one(runner).run(null, expectedParams, 0L);
            }
        });
        RemoveFilesCommand command = new RemoveFilesCommand(configHolder, new OBEXElement("/"), elements);
        command.setCommandLineRunner(runner);
        Object result = command.execute();
        assertNull(result);
        context.assertIsSatisfied();
    }
}
