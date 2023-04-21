package org.maveryx.demo.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.maveryx.bootstrap.Bootstrap;
import org.maveryx.core.guiApi.GuiButton;
import org.maveryx.core.guiApi.GuiFrame;
import org.maveryx.core.guiApi.GuiSpinBox;
import org.maveryx.core.guiApi.GuiText;

/**
 * This Test Class provides some examples of testing Text fields using the relevant Maveryx APIs.
 * @author Maveryx
 */
public class TextTest {

    /**
	 * Change this path to your current working directory, if necessary.
	 */
    private final String MAVERYX_DEMO_DIR = "C:\\Maveryx\\demo\\";

    private final String AUTLaunchFilePath = MAVERYX_DEMO_DIR + "AUT\\TextInputDemo.xml";

    String streetAddressField = "Street address:";

    String cityField = "City:";

    String zipCodeField = "Zip Code:";

    /**
	 * Default constructor.
	 * @throws Exception
	 */
    public TextTest() throws Exception {
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
	 * Enter a text in the specified text field.
	 * @param textField - the text field
	 * @param text - the text to enter
	 */
    public void setText(String textField, String text) {
        GuiText t = new GuiText(textField);
        assertTrue(t.isEditable() && t.isEnabled());
        t.setText(text);
        assertEquals(t.getText(), text);
    }

    /**
	 * This Test Case provides an example of entering a text in a Text field.
	 * @throws Exception
	 */
    @Test
    public void setText() throws Exception {
        startApp(AUTLaunchFilePath);
        String street = "350, 5th Ave";
        setText(streetAddressField, street);
        String city = "New York City";
        setText(cityField, city);
        GuiSpinBox spin = new GuiSpinBox();
        int increment = 32;
        spin.increment(increment);
        String expectedState = "New York (NY)";
        assertEquals(expectedState, spin.getText());
        String zipCode = "10118";
        setText(zipCodeField, zipCode);
        GuiButton setAddress = new GuiButton("Set address");
        setAddress.click();
    }

    /**
	 * This Test Case provides an example of appending a text in a Text field.
	 * @throws Exception
	 */
    @Test
    public void append() throws Exception {
        startApp(AUTLaunchFilePath);
        GuiText streetAddress = new GuiText(streetAddressField);
        String street = "350, 5th Ave";
        streetAddress.setText(street);
        String textToAppend = "  - Empire State Bldg";
        streetAddress.append(textToAppend);
        assertEquals(streetAddress.getText(), (street + textToAppend));
    }

    /**
	 * This Test Case provides an example of the copy & paste functionality in a Text field.
	 * @throws Exception
	 */
    @Test
    public void selectCopyPaste() throws Exception {
        startApp(AUTLaunchFilePath);
        GuiText streetAddress = new GuiText(streetAddressField);
        String street = "350, 5th Ave NYC";
        streetAddress.setText(street);
        int start = 13;
        int end = street.length();
        streetAddress.select(start, end);
        streetAddress.copy();
        GuiText city = new GuiText(cityField);
        city.setCursorAt(0);
        city.paste();
        assertEquals("NYC", city.getText());
    }

    /**
	 * This Test Case provides an example of the cut & paste functionality in a Text field.
	 * @throws Exception
	 */
    @Test
    public void selectCutPaste() throws Exception {
        startApp(AUTLaunchFilePath);
        GuiText streetAddress = new GuiText(streetAddressField);
        String street = "350, 5th Ave NYC";
        streetAddress.setText(street);
        int start = 13;
        int end = street.length();
        streetAddress.select(start, end);
        streetAddress.cut();
        String expectedStreet = "350, 5th Ave";
        assertEquals(streetAddress.getText().trim(), expectedStreet);
        String expectedCity = "NYC";
        GuiText city = new GuiText(cityField);
        city.setCursorAt(0);
        city.paste();
        assertEquals(city.getText(), expectedCity);
    }

    /**
	 * This Test Case provides an example of selecting and deleting a text in a Text field.
	 * @throws Exception
	 */
    @Test
    public void selectDelete() throws Exception {
        startApp(AUTLaunchFilePath);
        GuiText streetAddress = new GuiText(streetAddressField);
        String street = "350, 5th Ave NYC";
        streetAddress.setText(street);
        int start = 12;
        int end = street.length();
        streetAddress.select(start, end);
        streetAddress.delete();
        String expectedStreet = "350, 5th Ave";
        assertEquals(streetAddress.getText(), expectedStreet);
    }

    /**
	 * This Test Case provides an example of inserting a text in a Text field.
	 * @throws Exception
	 */
    @Test
    public void insertText() throws Exception {
        startApp(AUTLaunchFilePath);
        GuiText streetAddress = new GuiText(streetAddressField);
        String street = "350, Ave NYC";
        streetAddress.setText(street);
        int index = 5;
        streetAddress.setCursorAt(index);
        String textToInsert = "5th ";
        streetAddress.insertText(textToInsert);
        String expectedStreet = "350, 5th Ave NYC";
        assertEquals(streetAddress.getText(), expectedStreet);
    }
}
