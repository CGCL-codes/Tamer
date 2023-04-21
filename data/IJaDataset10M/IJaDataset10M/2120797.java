package de.miij.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import de.miij.Miij;
import de.miij.ui.WindowDescription;
import de.miij.ui.comp.MFrame;

public class MiijApplicationTest {

    private static Miij APP;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        APP = Miij.createApp("FTP Viewer");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMainWindow() throws InterruptedException {
        final WindowDescription wd = createWindowDescription();
        MFrame frm = APP.createWindow(wd);
        frm.setVisible(true);
        Thread.sleep(10000);
    }

    @SuppressWarnings("serial")
    private WindowDescription createWindowDescription() {
        WindowDescription wd = new WindowDescription() {

            @Override
            public void defineContent() {
            }

            @Override
            public void defineMenu() {
                menu("Datei");
            }

            @Override
            public void defineStatusbar() {
            }

            @Override
            public void defineToolbar() {
            }
        };
        return wd;
    }
}
