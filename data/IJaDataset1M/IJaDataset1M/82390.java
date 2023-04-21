package org.ndx.majick.ui.file;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.fixture.JTextComponentFixture;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ndx.majick.properties.Property;
import org.ndx.majick.properties.util.PropertyUtils;
import org.ndx.majick.ui.IPropertyUIMetadata;
import org.ndx.majick.ui.IUIProvider;

public class FileUIProviderTest {

    private static class Bean {

        private final transient PropertyChangeSupport support = new PropertyChangeSupport(this);

        Property<File> path = PropertyUtils.create(File.class, "File", support, new File("."));
    }

    private FrameFixture window;

    private FilePropertyMetadata metadata;

    private Bean tested;

    @Before
    public void setUp() throws Exception {
        try {
            tested = new Bean();
            FileUIProvider provider = new FileUIProvider();
            metadata = provider.createMetadata();
            JComponent pathEditor = provider.getEditor(metadata, tested.path);
            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
            frame.getContentPane().add(pathEditor);
            window = new FrameFixture(frame);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testPathEditor() {
        JTextComponentFixture pathField = window.textBox(tested.path.getName() + IUIProvider.EDITOR + FileUIProvider.PATH_FIELD);
        JButtonFixture editButton = window.button(tested.path.getName() + IUIProvider.EDITOR + FileUIProvider.EDIT_BUTTON);
        pathField.requireNotEditable();
        pathField.requireEnabled();
        pathField.requireText(tested.path.get().getAbsolutePath());
        editButton.requireEnabled();
        File parentFolder = new File("..");
        tested.path.set(parentFolder);
        pathField.requireText(parentFolder.getAbsolutePath());
        editButton.click();
        JFileChooserFixture fileChooser = window.fileChooser(tested.path.getName() + IUIProvider.EDITOR + FilePathEditor.FILE_CHOOSER);
        File currentDir = new File(parentFolder, "majick-properties");
        fileChooser.selectFile(currentDir);
        fileChooser.approve();
        try {
            Assert.assertThat(tested.path.get(), CoreMatchers.is(currentDir.getCanonicalFile()));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("weird exception throwed");
        }
    }

    @Test
    public void testPathEditorDisabled() {
        JPanelFixture panel = window.panel(tested.path.getName() + IUIProvider.EDITOR);
        JTextComponentFixture pathField = window.textBox(tested.path.getName() + IUIProvider.EDITOR + FileUIProvider.PATH_FIELD);
        JButtonFixture editButton = window.button(tested.path.getName() + IUIProvider.EDITOR + FileUIProvider.EDIT_BUTTON);
        panel.requireEnabled();
        pathField.requireNotEditable();
        pathField.requireEnabled();
        editButton.requireEnabled();
        metadata.getReadOnly().set(Boolean.TRUE);
        panel.requireDisabled();
        pathField.requireNotEditable();
        pathField.requireDisabled();
        editButton.requireDisabled();
    }
}
