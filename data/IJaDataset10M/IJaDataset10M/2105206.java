package pauker.program.gui.swing;

import java.awt.Dimension;
import java.io.File;
import junit.framework.TestCase;
import org.netbeans.jemmy.ClassReference;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JFileChooserOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.util.NameComponentChooser;
import screenshots.Screenshots;

/**
 * Whenever a lesson gets loaded, the window was pack()'ed.
 * This ensured that all batches were visible without scrolling.
 * On the other hand it sucked because whenever you enlarged the window pack()
 * reduces its size back to the preferred size.
 * 
 * Here we test if the window never gets smaller when opening lessons.
 * 
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public class PackTest extends TestCase {

    public void testPack() throws Exception {
        assertFalse("Only screenshots are generated!", Screenshots.UPDATE_SCREENSHOTS);
        String internalPath = "/testLessons/1.pau.gz";
        String testFilePath = getClass().getResource(internalPath).getPath();
        testFilePath = testFilePath.replace("%20", " ");
        File testFile = new File(testFilePath);
        ClassReference classReference = new ClassReference("pauker.program.gui.swing.PaukerFrame");
        classReference.startApplication();
        JFrameOperator frameOperator = new JFrameOperator();
        JButtonOperator openButtonOperator = new JButtonOperator(frameOperator, new NameComponentChooser("openButton"));
        frameOperator.setSize(100, 100);
        openLesson(testFile, openButtonOperator);
        Dimension packedSize = frameOperator.getSize();
        assertFalse(packedSize.width == 100);
        assertFalse(packedSize.height == 100);
        Dimension enlargedSize = new Dimension(packedSize.width + 100, packedSize.height + 100);
        frameOperator.setSize(enlargedSize);
        openLesson(testFile, openButtonOperator);
        Dimension secondSize = frameOperator.getSize();
        assertEquals(enlargedSize, secondSize);
    }

    private void openLesson(File testFile, JButtonOperator openButtonOperator) throws InterruptedException {
        assertTrue(openButtonOperator.isEnabled());
        openButtonOperator.pushNoBlock();
        JDialogOperator dialogOperator = new JDialogOperator();
        JFileChooserOperator fileChooserOperator = new JFileChooserOperator();
        fileChooserOperator.setSelectedFile(testFile);
        JButtonOperator dialogOpenButtonOperator = new JButtonOperator(dialogOperator, new NameComponentChooser("openButton"));
        Tools.doClick(dialogOpenButtonOperator);
        Thread.sleep(500);
    }
}
