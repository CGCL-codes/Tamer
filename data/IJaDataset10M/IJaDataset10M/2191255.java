package org.colombbus.tangara.ide.view.console;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import org.colombbus.tangara.ide.model.MessagesFactory;
import org.colombbus.tangara.ide.model.cmdline.CmdHistory;
import org.colombbus.tangara.ide.model.cmdline.DefaultCmdHistory;
import org.colombbus.tangara.ide.view.cmdline.CmdLine;
import org.colombbus.tangara.ide.view.cmdline.CmdLinePanel;
import org.colombbus.tangara.ide.view.cmdline.CmdLineToolBar;
import org.colombbus.tangara.ide.view.console.ConsoleArea;
import org.colombbus.tangara.util.bundle.TypedResourceLoaderLibrary;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
public class ConsoleAreaTest {

    static {
        ClassPathResource resource = new ClassPathResource("org/colombbus/tangara/util/bundle/resources/TypedResourceBundleTest.xml");
        ListableBeanFactory factory = new XmlBeanFactory(resource);
        TypedResourceLoaderLibrary library = (TypedResourceLoaderLibrary) factory.getBean("typedResourceLoaderFactory");
        MessagesFactory.setLibrary(library);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    public static void main(String[] argv) {
        ConsoleArea consoleArea = new ConsoleArea();
        CmdLinePanel cmdLinePanel = new CmdLinePanel(consoleArea);
        CmdHistory cmdHistory = new DefaultCmdHistory();
        CmdLine cmdLine = cmdLinePanel.getCmdLine();
        CmdLineToolBar cmdLineToolBar = new CmdLineToolBar(cmdHistory, cmdLine);
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(cmdLineToolBar, BorderLayout.NORTH);
        frame.add(cmdLinePanel, BorderLayout.CENTER);
        JPanel consoleAreaPanel = new JPanel();
        consoleAreaPanel.setBorder(new TitledBorder("Console"));
        consoleAreaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        consoleAreaPanel.add(consoleArea);
        frame.add(consoleAreaPanel, BorderLayout.SOUTH);
        frame.setSize(800, 220);
        updateLookAndFeel(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void updateLookAndFeel(Component component) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(component);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
