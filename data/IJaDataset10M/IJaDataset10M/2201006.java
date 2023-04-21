package org.wcb.gui;

import org.wcb.gui.forms.tabbed.LogbookTableDisplayForm;
import org.wcb.gui.forms.report.TotalFlightTimeByMonthReportForm;
import org.wcb.gui.forms.LogbookEntryDisplay;
import org.wcb.gui.component.SplashScreen;
import org.wcb.gui.component.JPilotMenu;
import org.wcb.gui.component.JXTitlePanel;
import org.wcb.gui.component.WaveButton;
import org.wcb.gui.component.JPilotIconMenu;
import org.wcb.gui.component.JStatusBar;
import org.wcb.gui.component.border.BookBorder;
import org.wcb.gui.component.border.LeftPageBookBorder;
import org.wcb.gui.event.ApplicationCloseListener;
import org.wcb.gui.event.FrameDialogListener;
import org.wcb.gui.util.UIHelper;
import org.wcb.gui.util.WindowStateSaver;
import org.wcb.gui.util.ApplicationPreferences;
import org.wcb.gui.dialog.JLoginDialog;
import org.wcb.resources.HSQLDatabaseInitializer;
import org.wcb.resources.MessageResourceRegister;
import org.wcb.resources.MessageKey;
import org.wcb.model.vo.hibernate.Logbook;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.VerticalLayout;
import org.wcb.model.service.impl.RecencyExperianceValidationService;
import org.wcb.model.service.IServicesConstants;
import org.wcb.model.service.ILogbookService;
import org.wcb.model.util.SpringUtil;
import org.wcb.gui.util.IPilotsLogApplicationPreferenceKeys;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.Border;
import java.awt.*;
import com.jgoodies.plaf.plastic.Plastic3DLookAndFeel;
import com.jgoodies.plaf.plastic.theme.SkyBlue;

/**
 * <small>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Mar 30, 2006 5:05:39 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Mar 30, 2006
 *          Time: 5:05:39 PM
 */
public class LogbookFrame extends JFrame {

    private JPanel glass;

    private JPanel eastPanel;

    private TotalFlightTimeByMonthReportForm summaryReport;

    private LogbookEntryDisplay logEntryform;

    private LogbookTableDisplayForm logbookTableView;

    public static final String LOGBOOK_VIEW = "Logbook";

    public static final String LOGBOOK_ENTRY_VIEW = "Entry";

    public static final String SUMMARY_REPORT_VIEW = "Summary";

    public LogbookFrame() {
        glass = (JPanel) getGlassPane();
        setTitle("Pilots Log");
        super.setIconImage(UIHelper.getIcon("org/wcb/resources/gui/wings.png").getImage());
        this.showSplashScreen();
        this.showRecencyMessages();
    }

    public JPanel getCardPanel() {
        return eastPanel;
    }

    private void setApplicationLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            Plastic3DLookAndFeel.setMyCurrentTheme(new SkyBlue());
            com.jgoodies.plaf.Options.setPopupDropShadowEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!System.getProperty("os.name").startsWith("Mac OS")) {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Pilots Log");
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
    }

    private void showSplashScreen() {
        SplashScreen splash = new SplashScreen(UIHelper.getIcon("org/wcb/resources/gui/safelog_cover_black_sm.jpg"));
        try {
            splash.showStatus("JPilot Logbook V " + MessageResourceRegister.getInstance().getValue(MessageKey.APPLICATION_VERSION));
            new HSQLDatabaseInitializer();
            splash.showStatus("created by");
            Thread.sleep(100);
            this.initComponents();
            this.setApplicationLookAndFeel();
            pack();
            splash.showStatus("Walter Bogaardt");
            Thread.sleep(500);
            setSize(800, 620);
            Toolkit tk = Toolkit.getDefaultToolkit();
            tk.addAWTEventListener(WindowStateSaver.getInstance(), AWTEvent.WINDOW_EVENT_MASK);
            loginUser();
            addWindowListener(new ApplicationCloseListener(this));
            splash.close();
        } catch (InterruptedException err) {
            err.printStackTrace();
        }
    }

    /**
     * This method does calls to see if the user has configured the application to have
     * user authentication. In the case of first time running this application this is turned
     * off. The user has to enable user authentication and then it gets turned on everytime until
     * they disable this feature.
     */
    private void loginUser() {
        if (ApplicationPreferences.getInstance().getBoolean(ApplicationPreferences.LOGON_ENABLE)) {
            JLoginDialog dialog = new JLoginDialog(this, "JPilot Login", true);
            dialog.setVisible(true);
        } else {
            setVisible(true);
        }
    }

    /**
     * Refreshes the instance of the time summary report
     */
    public void refreshSummaryReport() {
        summaryReport.refresh();
    }

    /**
     * Sets the log entry form to the value selected in the log book display
     * @param oValue Logbook Value object
     */
    public void setLogEntryForm(Logbook oValue) {
        logEntryform.setLogbookValue(oValue);
    }

    /**
     * From the logbook entry form need to get the LogBook value object
     * out of the form.
     * @return Logbook value object
     */
    public Logbook getLogEntryForm() {
        return logEntryform.getLogbookValue();
    }

    /**
     * Allows refreshing the table view of the logbook. This
     * in turn calls the table view's model to refresh from
     * the database, this is because a new entry has been made.
     */
    public void refreshLogbookView() {
        logbookTableView.refresh();
    }

    /**
     * This gets called everytime the application initally starts up. The dialog
     * will display only if the user has configured it to be displayed in the configuration
     * menu. The message will also only display when recency warnings or exceeding FAA minimums
     * are found. If the pilot is current then no message will be displayed.
     */
    private void showRecencyMessages() {
        if (ApplicationPreferences.getInstance().getBoolean(IPilotsLogApplicationPreferenceKeys.SHOW_WARN_ON_START)) {
            RecencyExperianceValidationService recencyValidator = (RecencyExperianceValidationService) SpringUtil.getApplicationContext().getBean(IServicesConstants.LOGBOOK_RECENCY_EXPERIANCE);
            ILogbookService delegateService = (ILogbookService) SpringUtil.getApplicationContext().getBean(IServicesConstants.LOGBOOK_SERVICE);
            recencyValidator.setObject(delegateService);
            if (!recencyValidator.validate()) {
                String messages = recencyValidator.getMessage();
                JOptionPane.showMessageDialog(this, messages, "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setJMenuBar(new JPilotMenu(this));
        ImageIcon titleIcon = UIHelper.getIcon("org/wcb/resources/gui/log_title.png");
        JLabel titleLabel = new JLabel(titleIcon);
        logbookTableView = new LogbookTableDisplayForm();
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0x8a84ab));
        titlePanel.setLayout(new BorderLayout());
        eastPanel = new JPanel(new CardLayout());
        eastPanel.setBorder(new CompoundBorder(new BookBorder(), eastPanel.getBorder()));
        eastPanel.add(logbookTableView, LOGBOOK_VIEW);
        JXTitlePanel summaryPanel = new JXTitlePanel("Summary of all Flights", new Color(0x3779ff));
        Border shadow = new DropShadowBorder(Color.BLACK, 2, 5, .3f, 10, false, true, true, true);
        summaryPanel.setBorder(new CompoundBorder(shadow, summaryPanel.getBorder()));
        summaryReport = new TotalFlightTimeByMonthReportForm();
        summaryPanel.add(summaryReport);
        eastPanel.add(summaryPanel, SUMMARY_REPORT_VIEW);
        logEntryform = new LogbookEntryDisplay(this);
        eastPanel.add(logEntryform, LOGBOOK_ENTRY_VIEW);
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new VerticalLayout(2));
        westPanel.setBorder(new CompoundBorder(new LeftPageBookBorder(), westPanel.getBorder()));
        JButton logbook = new WaveButton("Logbook");
        JButton summary = new WaveButton("Summary");
        JButton airport = new WaveButton("Airports");
        JButton aircraft = new WaveButton("Aircraft");
        logbook.addActionListener(new FrameDialogListener(this));
        summary.addActionListener(new FrameDialogListener(this));
        airport.addActionListener(new FrameDialogListener(this));
        aircraft.addActionListener(new FrameDialogListener(this));
        westPanel.add(logbook);
        westPanel.add(aircraft);
        westPanel.add(airport);
        westPanel.add(summary);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(eastPanel, BorderLayout.CENTER);
        titlePanel.add(new JPilotIconMenu(this), BorderLayout.NORTH);
        titlePanel.add(titleLabel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(new JStatusBar(), BorderLayout.SOUTH);
    }

    /**
     * Takes a JDialog box and allows it to be rendered onto an application glass pane
     * verses a popup box.
     * @param dialog The JDialog component
     * @return a Jcomponent.
     */
    public JComponent showJDialogAsSheet(JDialog dialog) {
        JComponent sheet = (JComponent) dialog.getContentPane();
        glass.setLayout(new GridBagLayout());
        glass.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        glass.add(sheet, gbc);
        gbc.gridy = 1;
        gbc.weighty = Integer.MAX_VALUE;
        glass.add(Box.createGlue(), gbc);
        glass.setVisible(true);
        return sheet;
    }

    /**
     * Interface to hide the main application glass Pane.
     */
    public void hideSheet() {
        glass.setVisible(false);
    }

    public static void main(String[] args) {
        new LogbookFrame();
    }
}
