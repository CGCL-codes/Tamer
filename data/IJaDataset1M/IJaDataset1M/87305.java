package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import tools.Identity;
import core.InputFile;
import core.Tools.Tool;
import exceptions.AlreadyReservedException;
import exceptions.NotReservedByToolException;

public class Test_InputFile {

    private Tool t = null;

    private InputFile iF = null;

    private final File f = new File("http://www.google.com/index.html");

    @Test
    public void earlyAccess() {
        try {
            this.iF.getFile(this.t);
            fail("expected NotReservedByToolException");
        } catch (final NotReservedByToolException e) {
            assertTrue(true);
        }
    }

    @Test
    public void getFileBack() {
        try {
            this.iF.reserveFor(this.t);
            final File test = this.iF.getFile(this.t);
            assertTrue(test == this.f);
        } catch (final AlreadyReservedException e) {
            fail("Exception in InputFile");
        } catch (final NotReservedByToolException e) {
            fail("Exception in InputFile");
        }
    }

    @Test
    public void returnToSoon() {
        try {
            this.iF.returnFrom(this.t);
            fail("expected NotReservedByToolException");
        } catch (final NotReservedByToolException e) {
            assertTrue(true);
        }
    }

    @Before
    public void setup() {
        this.t = new Identity();
        this.iF = new InputFile(this.f);
    }

    @Test
    public void wrongTool() {
        try {
            this.iF.getFile(new Identity());
            fail("expected NotReservedByToolException");
        } catch (final NotReservedByToolException e) {
            assertTrue(true);
        }
    }
}
