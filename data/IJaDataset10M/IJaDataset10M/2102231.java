package com.aivik.wordspell.gui;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Kevin Ewig
 */
public class GTextAreaTest {

    /**
     * Test the "replace text" in the text area.
     */
    @Test
    public void test01() {
        try {
            String wrongString = "\"Using this drug for insommnia sort of like using a shotgun to kill an ant,\" Dr. Howard Nearman, department chairman of anesthesia at University Hospitals Case Medical Center in Ohio, told FOXNews.com. \"How someone could get a hold of this medication - and use it for the purpose that he allegedly used it for - is just incredible.\" " + "\"Using this drug for insommnia sort of like using a shotgun to kill an ant,\" Dr. Howard Nearman, department chairman of anesthesia at University Hospitals Case Medical Center in Ohio, told FOXNews.com. \"How someone could get a hold of this medication - and use it for the purpose that he allegedly used it for - is just incredible.";
            String correctString = "\"Using this drug for insomnia sort of like using a shotgun to kill an ant,\" Dr. Howard Nearman, department chairman of anesthesia at University Hospitals Case Medical Center in Ohio, told FOXNews.com. \"How someone could get a hold of this medication - and use it for the purpose that he allegedly used it for - is just incredible.\" " + "\"Using this drug for insomnia sort of like using a shotgun to kill an ant,\" Dr. Howard Nearman, department chairman of anesthesia at University Hospitals Case Medical Center in Ohio, told FOXNews.com. \"How someone could get a hold of this medication - and use it for the purpose that he allegedly used it for - is just incredible.";
            GTextArea area = new GTextArea();
            area.setText(wrongString);
            Thread.sleep(2000L);
            area.replace(354, "insomnia", "insommnia");
            Thread.sleep(500L);
            area.replace(21, "insomnia", "insommnia");
            Thread.sleep(500L);
            String correctedText = area.getText();
            Assert.assertTrue(correctedText.equals(correctString));
        } catch (Exception exception) {
            exception.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Test the misspelled words finder sub part of KTitledTextArea.
     */
    @Test
    public void test02() {
        try {
        } catch (Exception exception) {
            exception.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * Test the misspelled words finder.
     */
    @Test
    public void test03() {
        try {
        } catch (Exception exception) {
            exception.printStackTrace();
            Assert.fail();
        }
    }
}
