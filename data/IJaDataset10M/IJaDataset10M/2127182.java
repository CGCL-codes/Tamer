package org.ungoverned.radical;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import org.ungoverned.radical.util.Constants;
import org.ungoverned.radical.util.GuiUtility;

public class Main {

    private transient Radical m_radical = null;

    private transient RadicalPanel m_radicalPanel = null;

    private transient JFrame m_frame = null;

    /**
     * This static block changes some default UI properties
     * for the entire application and should probably not
     * be used when Radical is embedded in another application
     * as a plugin. In order to use Radical's default UI
     * properties, call this method before using Radical or
     * creating any Swing components.
     * <p>
     * Currently, this method changes all component fonts
     * to a simple, plain font and removes some borders
     * from the split pane component.
    **/
    static {
        Integer fontPlain = new Integer(Font.PLAIN);
        Integer fontSize = new Integer(11);
        Object lazyFont = new UIDefaults.ProxyLazyValue("javax.swing.plaf.FontUIResource", null, new Object[] { "SansSerif", fontPlain, fontSize });
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, lazyFont);
            }
        }
        defaults.remove("SplitPane.border");
        defaults.remove("SplitPaneDivider.border");
    }

    public Main() {
        ShutdownHandler hdlr = new ShutdownHandler() {

            public void prepareForShutdown() {
                Point p = m_frame.getLocation();
                Radical.setGlobalProperty(Constants.FRAME_XPOS_PROP, Integer.toString(p.x));
                Radical.setGlobalProperty(Constants.FRAME_YPOS_PROP, Integer.toString(p.y));
                Dimension d = m_frame.getSize();
                Radical.setGlobalProperty(Constants.FRAME_WIDTH_PROP, Integer.toString(d.width));
                Radical.setGlobalProperty(Constants.FRAME_HEIGHT_PROP, Integer.toString(d.height));
            }
        };
        m_radical = new Radical(hdlr);
        m_radicalPanel = new RadicalPanel(m_radical);
        m_radical.setRadicalPanel(m_radicalPanel);
        m_frame = new JFrame("Radical");
        m_frame.getContentPane().setLayout(new BorderLayout());
        m_frame.getContentPane().add(m_radicalPanel);
        m_frame.pack();
        m_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        m_frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                m_radicalPanel.performExitAction();
            }
        });
        if (Radical.getGlobalProperty(Constants.FRAME_XPOS_PROP) != null) {
            try {
                m_frame.setLocation(Integer.parseInt(Radical.getGlobalProperty(Constants.FRAME_XPOS_PROP)), Integer.parseInt(Radical.getGlobalProperty(Constants.FRAME_YPOS_PROP)));
                m_frame.setSize(Integer.parseInt(Radical.getGlobalProperty(Constants.FRAME_WIDTH_PROP)), Integer.parseInt(Radical.getGlobalProperty(Constants.FRAME_HEIGHT_PROP)));
            } catch (Exception ex) {
                m_frame.setSize(400, 400);
                GuiUtility.positionWindow(m_frame, GuiUtility.CENTER);
            }
        } else {
            m_frame.setSize(400, 400);
            GuiUtility.positionWindow(m_frame, GuiUtility.CENTER);
        }
        m_frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
