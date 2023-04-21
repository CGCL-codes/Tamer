package websiteschema.analyzer.browser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.webrenderer.swing.*;

/**
 *
 * @author ray
 */
public final class TestBrowser {

    IMozillaBrowserCanvas browser;

    JTextField textfield;

    public TestBrowser() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(content());
        frame.setSize(750, 450);
        frame.setVisible(true);
    }

    public JPanel content() {
        JPanel panel = new JPanel(new BorderLayout());
        textfield = new JTextField();
        textfield.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                browser.loadURL(textfield.getText());
            }
        });
        BrowserFactory.setLicenseData("30dtrial", "2PS4MACGJHK0T6JP5F1101Q8");
        browser = BrowserFactory.spawnMozilla();
        RenderingOptimization renOps = new RenderingOptimization();
        renOps.setWindowlessFlashSmoothScrolling(true);
        browser.setRenderingOptimizations(renOps);
        browser.loadURL("http://www.baidu.com/");
        panel.add(BorderLayout.NORTH, textfield);
        panel.add(BorderLayout.CENTER, browser.getComponent());
        return panel;
    }

    public static void main(String[] args) {
        new TestBrowser();
    }
}
