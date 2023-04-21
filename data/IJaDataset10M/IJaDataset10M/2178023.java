package bg.invider.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToolBar;
import bg.invider.regex.PrepareSelection;
import bg.invider.script.Script;
import bg.invider.swing.listener.IconButtonMouseListener;
import bg.invider.util.StringUtils;

/**
 * The control panel of the Invider app. 
 * 
 * @author meddle, stitch
 * @version 1.0
 */
public class ControlToolBar extends JToolBar implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String BACK_COMMAND = "back";

    private static final String FORWARD_COMMAND = "forward";

    private static final String STOP_COMMAND = "stop";

    private static final String REFRESH_COMMAND = "refresh";

    private static final String GEN_COMMAND = "generate regex";

    private static final String MARK_COMMAND = "mark";

    private JButton back;

    private JButton forward;

    private JButton stop;

    private JButton refresh;

    private JButton generate;

    private JButton mark;

    private WebBrowsers browsers;

    private SourceControlPanel controlPanel;

    public ControlToolBar(WebBrowsers browsers, SourceControlPanel controlPanel) {
        super("Control Toolbar");
        this.controlPanel = controlPanel;
        this.browsers = browsers;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setFloatable(false);
        setPreferredSize(new Dimension(900, 48));
        back = Util.createButton("Back", "images/spiderback.jpg", "Back", 48, 48);
        back.setBorder(BorderFactory.createEmptyBorder());
        back.setActionCommand(BACK_COMMAND);
        back.addActionListener(this);
        back.addMouseListener(new IconButtonMouseListener(back.getIcon(), Util.createIcon("images/inverted/spiderback.jpg", "Back"), null));
        forward = Util.createButton("Forward", "images/spiderforward.jpg", "Back", 48, 48);
        forward.setBorder(BorderFactory.createEmptyBorder());
        forward.setActionCommand(FORWARD_COMMAND);
        forward.addActionListener(this);
        forward.addMouseListener(new IconButtonMouseListener(forward.getIcon(), Util.createIcon("images/inverted/spiderforward.jpg", "Forward"), null));
        stop = Util.createButton("Stop", "images/spiderstop.jpg", "Back", 48, 48);
        stop.setBorder(BorderFactory.createEmptyBorder());
        stop.setActionCommand(STOP_COMMAND);
        stop.addActionListener(this);
        stop.addMouseListener(new IconButtonMouseListener(stop.getIcon(), Util.createIcon("images/inverted/spiderstop.jpg", "Stop"), null));
        refresh = Util.createButton("Refresh", "images/spiderrefresf.jpg", "Back", 48, 48);
        refresh.setBorder(BorderFactory.createEmptyBorder());
        refresh.setActionCommand(REFRESH_COMMAND);
        refresh.addActionListener(this);
        refresh.addMouseListener(new IconButtonMouseListener(refresh.getIcon(), Util.createIcon("images/inverted/spiderrefresf.jpg", "Refresh"), null));
        generate = new JButton("Reg");
        generate.setActionCommand(GEN_COMMAND);
        generate.addActionListener(this);
        mark = new JButton("Mark");
        mark.setActionCommand(MARK_COMMAND);
        mark.addActionListener(this);
        add(back);
        add(Box.createHorizontalStrut(2));
        add(forward);
        add(Box.createHorizontalStrut(2));
        add(stop);
        add(Box.createHorizontalStrut(2));
        add(refresh);
        add(Box.createHorizontalGlue());
        add(generate);
        add(mark);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (BACK_COMMAND.equals(e.getActionCommand())) {
            browsers.getSelectedBrowser().back();
        }
        if (FORWARD_COMMAND.equals(e.getActionCommand())) {
            browsers.getSelectedBrowser().forward();
        }
        if (STOP_COMMAND.equals(e.getActionCommand())) {
            browsers.getSelectedBrowser().stop();
        }
        if (REFRESH_COMMAND.equals(e.getActionCommand())) {
            browsers.getSelectedBrowser().refresh();
        }
        if (GEN_COMMAND.equals(e.getActionCommand())) {
            try {
                Script script = controlPanel.getModelScript();
                script.setHtml(browsers.getSelectedBrowser());
                String baseHtmlCodeSource = script.getHtml();
                String javascriptCode = StringUtils.readFile("../javascript/viewSelection.js");
                String selection = browsers.getSelectedBrowser().executeScript(javascriptCode);
                String baseComponent = script.getCurrentComponent().getBase().toString();
                if (!baseComponent.equals("html")) {
                    baseHtmlCodeSource = script.getComponentByName(baseComponent).getContent();
                }
                script.setChoices(PrepareSelection.getRegChoices(baseHtmlCodeSource, selection));
                controlPanel.updateRegs();
            } catch (IOException ex) {
                Logger.getLogger(ControlToolBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(ControlToolBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (MARK_COMMAND.equals(e.getActionCommand())) {
        }
    }
}
