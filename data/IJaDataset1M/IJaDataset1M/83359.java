package au.gov.qld.dnr.dss.view;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import org.swzoo.log2.core.*;
import au.gov.qld.dnr.dss.event.*;
import au.gov.qld.dnr.dss.view.support.CancelPanel;
import au.gov.qld.dnr.dss.view.support.OkPanel;
import au.gov.qld.dnr.dss.view.support.TitlePanel;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.net.URL;
import java.awt.event.*;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

/**
 *  Window which displays the status of processing.
 */
public class TrashRunsGUI extends JDialog {

    boolean _proceed = false;

    String _titleString;

    String _warningString;

    Color _warningColor;

    String _msgProp;

    String _messageLoc;

    String _textDeleteButton;

    String _textCancelButton;

    TitlePanel _panelTitle;

    JEditorPane _display;

    JScrollPane _scroller;

    JPanel _panelButton;

    JButton _buttonDelete;

    JButton _buttonCancel;

    BorderLayout _layoutMain;

    FlowLayout _layoutTitle;

    GridLayout _layoutMessage;

    GridBagLayout _layoutButtons;

    GridBagConstraints _c;

    /** Run count value. */
    int _runCount;

    /**
     * Constructor.
     *
     * @param frame a reference frame.
     * @param runCount the run count (how many runs are going to get trashed).
     */
    public TrashRunsGUI(Frame frame, int runCount) {
        super(frame, true);
        _runCount = runCount;
        initConfig();
        initGUI();
        wireListeners();
    }

    void initConfig() {
        _titleString = resources.getProperty("dss.gui.runs.trash.title", "WARNING");
        _warningString = resources.getProperty("dss.gui.runs.trash.banner", "YOU ARE ABOUT TO LOSE DATA!");
        _warningColor = resources.getColorProperty("dss.gui.runs.trash.banner.color", Color.red);
        _msgProp = "dss.gui.runs.trash.content.html";
        _messageLoc = resources.getProperty(_msgProp, null);
        _textDeleteButton = resources.getProperty("button.runs.trash.delete", "DELETE");
        _textCancelButton = resources.getProperty("button.runs.trash.cancel", "CANCEL");
    }

    void wireListeners() {
        addWindowListener(new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                setIt();
            }
        });
        _buttonCancel.addActionListener(new ActionAdapter() {

            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        _buttonDelete.addActionListener(new ActionAdapter() {

            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
    }

    protected void setIt() {
        _buttonCancel.requestFocus();
    }

    protected void cancel() {
        _proceed = false;
        dispose();
    }

    protected void delete() {
        _proceed = true;
        dispose();
    }

    public boolean proceed() {
        return _proceed;
    }

    protected void initGUI() {
        setTitle(_titleString);
        _display = new JEditorPane();
        _display.setEditable(false);
        if (_messageLoc != null) {
            try {
                URL url = resources.getSystemResource(_messageLoc);
                _display.setPage(url);
            } catch (MissingResourceException ex) {
                String msg = "Error:  Could not find resource " + _messageLoc;
                _display.setText(msg);
                LogTools.warn(logger, "TrashRunsGUI.initGUI() - " + msg);
            } catch (IOException iex) {
                String msg = "Error:  Failed opening resource " + _messageLoc + ".  Reason: " + iex.getMessage();
                _display.setText(msg);
                LogTools.warn(logger, "TrashRunsGU.initGUI() - " + msg);
            }
        } else {
            String msg = "Property " + _msgProp + " not set.  Cannot display message.";
            _display.setText(msg);
            LogTools.warn(logger, "TrashRunsGU.initGUI() - " + msg);
        }
        _scroller = new JScrollPane(_display);
        _scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        int width = resources.getIntProperty("dss.gui.runs.trash.scroller.width", 385);
        int height = resources.getIntProperty("dss.gui.runs.trash.scroller.height", 300);
        _scroller.setPreferredSize(new Dimension(width, height));
        _panelTitle = new TitlePanel(_warningString);
        _panelTitle.setTextColor(_warningColor);
        _panelButton = new JPanel();
        _buttonDelete = new JButton(_textDeleteButton);
        _buttonCancel = new JButton(_textCancelButton);
        _layoutMain = new BorderLayout();
        this.getContentPane().setLayout(_layoutMain);
        _panelButton.setBorder(getBorder());
        _layoutButtons = new GridBagLayout();
        _panelButton.setLayout(_layoutButtons);
        _c = new GridBagConstraints();
        _c.gridx = 0;
        _c.gridy = 0;
        _c.weightx = 1.0;
        _c.weighty = 1.0;
        _c.anchor = GridBagConstraints.WEST;
        _layoutButtons.setConstraints(_buttonDelete, _c);
        _panelButton.add(_buttonDelete);
        _c.gridx = 1;
        _c.gridy = 0;
        _c.weightx = 1.0;
        _c.weighty = 1.0;
        _c.anchor = GridBagConstraints.EAST;
        _layoutButtons.setConstraints(_buttonCancel, _c);
        _panelButton.add(_buttonCancel);
        this.getContentPane().add(BorderLayout.NORTH, _panelTitle);
        this.getContentPane().add(BorderLayout.CENTER, _scroller);
        this.getContentPane().add(BorderLayout.SOUTH, _panelButton);
    }

    private Border getBorder() {
        return BorderFactory.createBevelBorder(BevelBorder.RAISED);
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource manager. */
    ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}
