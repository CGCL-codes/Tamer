package watij;

import static watij.finders.SymbolFactory.*;
import watij.dialogs.PromptDialog;
import watij.dialogs.FileDownloadDialog;
import watij.dialogs.AlertDialog;
import watij.dialogs.ConfirmDialog;
import watij.runtime.ie.IE;
import watij.utilities.WatijResourceLoader;
import watij.utilities.WatijResources;
import static watij.finders.FinderFactory.*;

public class PopupsTest extends WatijTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ie.goTo(HTML_ROOT + "popups1.html");
    }

    private void clickButtonOnSeparateThread(final String value) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    ie.button(value).click();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void testManySimple() throws Exception {
        for (int i = 0; i < 2; i++) {
            ie.goTo(HTML_ROOT + "popups1.html");
            testSimple();
        }
    }

    public void testSimple() throws Exception {
        clickButtonOnSeparateThread("Alert");
        AlertDialog alertDialog = ie.alertDialog();
        assertEquals(IE.TITLE, alertDialog.title());
        assertTrue("This showed up instead: " + alertDialog.text(), alertDialog.text().contains("This is an alert box"));
        alertDialog.ok();
        assertFalse(alertDialog.exists());
    }

    public void testConfirmOK() throws Exception {
        clickButtonOnSeparateThread("Confirm");
        ConfirmDialog confirmDialog = ie.confirmDialog();
        assertEquals(IE.TITLE, confirmDialog.title());
        assertTrue(confirmDialog.text().contains("Do you really want to do this"));
        confirmDialog.ok();
        assertFalse(confirmDialog.exists());
        assertTrue(ie.textField(name, "confirmtext").verifyContains("OK"));
    }

    public void testConfirmCancel() throws Exception {
        clickButtonOnSeparateThread("Confirm");
        ConfirmDialog confirmDialog = ie.confirmDialog();
        assertEquals(IE.TITLE, confirmDialog.title());
        assertTrue(confirmDialog.text().contains("Do you really want to do this"));
        confirmDialog.cancel();
        assertFalse(confirmDialog.exists());
        assertTrue(ie.textField(name, "confirmtext").verifyContains("Cancel"));
    }

    public void testPromptCancel() throws Exception {
        clickButtonOnSeparateThread("Prompt");
        PromptDialog promptDialog = ie.promptDialog();
        assertEquals("Explorer User Prompt", promptDialog.title());
        assertTrue(promptDialog.text().contains("Enter something delightful"));
        promptDialog.cancel();
        assertFalse(promptDialog.exists());
        assertTrue(ie.textField(name, "prompttext").verifyContains("Cancel"));
    }

    public void testPromptEnterValue() throws Exception {
        clickButtonOnSeparateThread("Prompt");
        PromptDialog promptDialog = ie.promptDialog();
        assertEquals("Explorer User Prompt", promptDialog.title());
        assertTrue(promptDialog.text().contains("Enter something delightful"));
        assertEquals("hmm", promptDialog.value());
        promptDialog.value("wow");
        assertEquals("wow", promptDialog.value());
        promptDialog.ok();
        assertFalse(promptDialog.exists());
        assertTrue(ie.textField(name, "prompttext").verifyContains("wow"));
    }

    public void testFileDownladDialogCancel() throws Exception {
        FileDownloadDialog fileDownloadDialog = ie.fileDownloadDialog("http://watij.com/_media/wiki:test.zip");
        fileDownloadDialog.cancel();
        assertFalse(fileDownloadDialog.exists());
    }

    public void testFileDownladDialogOpen() throws Exception {
        ie.fileDownloadDialog("http://watij.com/_media/wiki:test.zip").open();
    }

    public void testFileDownladDialogSaveAutoClose() throws Exception {
        FileDownloadDialog fileDownloadDialog = ie.fileDownloadDialog("http://watij.com/_media/wiki:test.zip");
        fileDownloadDialog.closeThisDialogBoxWhenDownloadCompletes(true);
        fileDownloadDialog.save("c:\\wow.zip");
    }

    public void testFileDownladDialogSaveNoAutoClose() throws Exception {
        FileDownloadDialog fileDownloadDialog = ie.fileDownloadDialog("http://watij.com/_media/wiki:test.zip");
        fileDownloadDialog.closeThisDialogBoxWhenDownloadCompletes(false);
        fileDownloadDialog.save("c:\\wow.zip");
        fileDownloadDialog.waitUntilDownloadComplete();
        fileDownloadDialog.close();
    }
}
