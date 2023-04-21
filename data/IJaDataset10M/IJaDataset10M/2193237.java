package uk.org.sgj.YAT;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JFrame;
import uk.org.sgj.YAT.Tests.VocabTestDefinition;
import uk.org.sgj.YAT.Tests.YATTestRun;

/**
 *
 * @author Steffen
 */
public class YATWindows {

    static YATTestRun testRunDialog;

    static YATVocabEditorDialog editorDialog;

    static JFrame masterFrame;

    protected static void initWindows(JFrame masterF) {
        masterFrame = masterF;
        initTestRunDialog(masterFrame);
    }

    public static void updateTestFonts() {
        initTestRunDialog(masterFrame);
        editorDialog.updateFonts(YAT.getProject().getFontsForTestRuns());
    }

    static void cannotEdit() {
        editorDialog.cannotEdit();
        YAT.toggleCanEditHebrewUnpointed();
    }

    private static void initTestRunDialog(JFrame masterFrame) {
        testRunDialog = new YATTestRun(masterFrame, YAT.getProject().getFontsForTestRuns());
        testRunDialog.setVisible(false);
    }

    public static YATVocabEditorDialog initEditorDialog() {
        editorDialog = new YATVocabEditorDialog(YAT.getProject().getFontsForTestRuns());
        editorDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        editorDialog.pack();
        editorDialog.setVisible(false);
        return (editorDialog);
    }

    public static YATTestRun showTestRun(VocabTestDefinition v, YATFontSet y) {
        testRunDialog.startNewTest(v, y);
        return (testRunDialog);
    }

    public static void setMaxSizeOfComponentToLimitWindow(Window window, Component compToResize) {
        window.pack();
        Dimension total = window.getSize();
        Dimension internal = compToResize.getSize();
        Dimension difference = new Dimension(total.width - internal.width, total.height - internal.height);
        Dimension screen = YAT.getInitialFrameSize();
        Dimension maximisedInternal = new Dimension(screen.width - difference.width, screen.height - difference.height);
        maximisedInternal.height -= 5;
        maximisedInternal.width -= 5;
        compToResize.setMaximumSize(maximisedInternal);
        window.pack();
    }

    public static void setSizeOfComponentToMaximiseWindow(Window window, Component compToResize) {
        window.pack();
        Dimension total = window.getSize();
        Dimension internal = compToResize.getSize();
        Dimension difference = new Dimension(total.width - internal.width, total.height - internal.height);
        Dimension screen = YAT.getInitialFrameSize();
        Dimension maximisedInternal = new Dimension(screen.width - difference.width, screen.height - difference.height);
        maximisedInternal.height -= 5;
        maximisedInternal.width -= 5;
        compToResize.setPreferredSize(maximisedInternal);
        window.pack();
    }
}
