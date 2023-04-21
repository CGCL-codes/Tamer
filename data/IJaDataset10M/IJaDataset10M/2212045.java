package org.maveryx.demo.junit;

import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.maveryx.bootstrap.Bootstrap;
import org.maveryx.core.guiApi.GuiFrame;
import org.maveryx.core.guiApi.GuiToggleButton;

/**
 * This Test Class provides some examples of testing Toggle Buttons using the relevant Maveryx APIs. 
 * @author Maveryx
 */
public class ToggleButtonTest {

    /**
	 * Change this path to your current working directory, if necessary.
	 */
    private final String MAVERYX_DEMO_DIR = "C:\\Maveryx\\demo\\";

    private final String AUTLaunchFilePath = MAVERYX_DEMO_DIR + "AUT\\ToggleButtonSample.xml";

    /**
	 * Default constructor.
	 * @throws Exception
	 */
    public ToggleButtonTest() throws Exception {
        super();
    }

    /**
	 * Start the application-under-test by loading the launch parameters from an XML file.
	 * @param xmlFile - pathname string of the XML file containing the launch parameters  
	 * @throws Exception 
	 */
    private void startApp(String xmlFile) {
        try {
            Bootstrap.startApplication(xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
    }

    /**
	 * Close the application-under-test.
	 * @throws Exception
	 */
    @After
    public void tearDown() throws Exception {
        GuiFrame f = new GuiFrame();
        f.close();
        Bootstrap.stop(AUTLaunchFilePath);
    }

    /**
	 * This Test Case provides an example of managing toggle buttons using the relavant Maveryx APIs.
	 * @throws Exception
	 */
    @Test
    public void click() throws Exception {
        startApp(AUTLaunchFilePath);
        GuiToggleButton north = new GuiToggleButton("North");
        assertTrue(!north.isDown() && north.isEnabled());
        north.click();
        assertTrue(north.isDown());
        north.click();
        assertTrue(!north.isDown());
    }
}
