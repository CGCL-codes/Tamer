package be.vds.jtbdive.client.view.core.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLEditorKit;
import org.jdesktop.swingx.VerticalLayout;
import be.vds.jtb.swing.layout.GridBagLayoutManager;
import be.vds.jtbdive.client.swing.component.ExceptionDialog;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.components.LicenceDialog;
import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.Version;
import be.vds.jtbdive.core.util.FileUtilities;

public class AboutWindow extends JWindow {

    private static final long serialVersionUID = 6192746910753186442L;

    private MouseAdapter mouseCloseAdapter;

    public AboutWindow() {
        init();
    }

    private void init() {
        mouseCloseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (1 == e.getClickCount()) {
                    close();
                }
            }
        };
        this.addMouseListener(mouseCloseAdapter);
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    close();
                }
            }
        });
        getContentPane().add(createContentPane());
        pack();
    }

    private JComponent createContentPane() {
        JPanel p = new JPanel();
        p.setBorder(new LineBorder(UIAgent.getInstance().getColorDetailPanelTop()));
        p.setLayout(new GridBagLayout());
        p.setBackground(UIAgent.getInstance().getColorBaseBackground());
        p.addMouseListener(mouseCloseAdapter);
        p.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
        p.setLayout(new BorderLayout());
        p.add(createLogoPane(), BorderLayout.NORTH);
        p.add(createCenterPanel(), BorderLayout.CENTER);
        return p;
    }

    private Component createCenterPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(createTabbedPane(), BorderLayout.CENTER);
        southPanel.add(createButtonsPane(), BorderLayout.SOUTH);
        return southPanel;
    }

    private Component createButtonsPane() {
        JButton closeBtn = new JButton(new AbstractAction("Close") {

            private static final long serialVersionUID = 4657598929134114638L;

            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.setOpaque(false);
        p.add(closeBtn);
        return p;
    }

    private JComponent createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Team", createProgrammingTeamPanel());
        tabbedPane.addTab("Technologies", createTechnologiesPanel());
        return tabbedPane;
    }

    private JComponent createLogoPane() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        GridBagLayoutManager.addComponent(panel, createJtbLogo(), c, 0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(panel, Box.createHorizontalGlue(), c, 1, 0, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
        GridBagLayoutManager.addComponent(panel, createVersion(), c, 2, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        return panel;
    }

    private JComponent createVersion() {
        JEditorPane label = new JEditorPane();
        label.setEditable(false);
        label.setOpaque(false);
        label.setBorder(null);
        label.setEditorKit(new HTMLEditorKit());
        StringBuffer sb = new StringBuffer();
        sb.append("<html><i>");
        sb.append("<p align='center'><font size='6'>");
        sb.append("Jt'B Dive LogBook");
        sb.append("</font></p>");
        sb.append("<p align='center'><font size='4'>");
        sb.append("Version: ").append(Version.getCurrentVersion().toString());
        sb.append("</font></p>");
        sb.append("<p align='center'><font size='2'>");
        sb.append("Java vendor: ").append(System.getProperty("java.vendor")).append("<br/>");
        sb.append("Java version: ").append(System.getProperty("java.version"));
        sb.append("</font></p></i></html>");
        label.setText(sb.toString());
        return label;
    }

    private JComponent createJtbLogo() {
        JLabel label = new JLabel(ResourceManager.getInstance().getImageIcon("logos/logo_125.png"));
        return label;
    }

    private JComponent createTechnologiesPanel() {
        TechnologiesPanel tp = new TechnologiesPanel();
        tp.setOpaque(false);
        JScrollPane scroll = new JScrollPane(tp);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(UIAgent.VERTICAL_UNIT_SCROLL);
        return scroll;
    }

    private JComponent createProgrammingTeamPanel() {
        JEditorPane label = new JEditorPane();
        label.setEditorKit(new HTMLEditorKit());
        label.setEditable(false);
        label.setOpaque(false);
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<p align='center'><font size='6'><b>").append("Programming team").append("</b></font></p>");
        sb.append("<p align='center'>").append("Gautier Vanderslyen (CMAS 3*)").append("</p>");
        sb.append("<p align='center'>").append("Laurent Joppart (CMAS Monitor 2*)").append("</p>");
        sb.append("<p align='center'>").append("http://jtbdivelogbook.sourceforge.net").append("</p>");
        sb.append("</html>");
        label.setText(sb.toString());
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(label, BorderLayout.CENTER);
        return p;
    }

    public void close() {
        this.dispose();
    }

    public static void main(String[] args) {
        AboutWindow aw = new AboutWindow();
        aw.setLocationRelativeTo(null);
        aw.setVisible(true);
        aw.setSize(500, 500);
    }
}
