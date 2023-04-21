package corina.cross;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import corina.Build;
import corina.core.App;
import corina.cross.sigscores.SignificantScoresView;
import corina.gui.Layout;
import corina.gui.PrintableDocument;
import corina.gui.XFrame;
import corina.gui.menus.EditMenu;
import corina.gui.menus.FileMenu;
import corina.gui.menus.HelpMenu;
import corina.gui.menus.WindowMenu;
import corina.map.MapFrame;
import corina.prefs.PrefsEvent;
import corina.prefs.PrefsListener;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteNotFoundException;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.util.Center;
import corina.util.JLinedLabel;
import corina.util.OKCancel;
import corina.util.Sort;

/**
 * A window which displays a crossdate. Displays all scores and significant scores, and lets
 * the user step forward/backward through the Sequence, graphing any desired cross.
 * 
 * <p>
 * The crossdate window operates on two levels. First, it provides 3 different views for
 * crossdating Sequences: normal view, table view, and grid view. Second, for the normal view,
 * it has 3 tabs for different views of the current Crossdate from that Sequence: significant
 * scores, all scores, and a histogram.
 * </p>
 * 
 * <h2>Left to do</h2>
 * <ul>
 * <pre>
 * 
 *  -- get rid of refreshFromPrefs(), HasPreferences
 *  -- implement prefsListener - what's the jframe equiv of addNotify()/removeNotify()?
 *  -- note, also, i might not even need this!
 *  -- only run new crossdate once, if needed.
 *  -- each view: if corina.cross.overlap!=cross.overlap, re-run.
 *  -- need: if cross is running, wait for it
 *  -- need: a &quot;min overlap&quot; field in crossdate
 * 
 *  -- View-&gt;range menuitems aren't working
 *  -- the JMenuBar should be CrossdateMenuBar extends CorinaMenuBar, so i get file/window/help for free
 *  -- dim lower part of view menu (ranges) if top part is not &quot;crossdate&quot;
 *  
 *  -- error handling.
 *  -- extract methods viewAsCrossdate(), viewAsTable(), viewAsGrid().  (then i can extract menus)
 *  -- use new CorinaMenuBar abstraction
 *  -- need jumpToCrossdate(f,m) method for views
 *  -- if crossdating a 25-year sample against a 35-year sample with
 *  minimum_overlap = 50, instead of the normal tabs, put up a label
 *  explaining why it can't compute any scores for this crossdate,
 *  and provide an easy way out (if len1&gt;5&amp;&amp;len2&gt;5, add button
 *  &quot;compute anyway, with minimum overlap = popup([5,10,25,...])&quot;,
 *  else say &quot;this sample is only 5 years long, there's no way you'll
 *  get anything useful out of that.&quot;)
 *  -- for short samples, don't spawn a new thread.  assume toggling
 *  button-enabled and thread spawning take zero time (the latter is
 *  around 1ms), and that we want to keep response time under 50ms on
 *  a 500mhz computer.  figure out how long the samples can be for that.
 *  -- ...it depends on the i/o speed, naturally.  load all samples on c'ton
 *  so it doesn't.
 *  -- if you can't run the WJ cross because fixed/moving are backwards,
 *  put up a label saying why it can't run the crossdate, and how to
 *  change it (cmd-E, swap).
 *  -- (crossdate kit:) mark files with WJ in lists, so this is easier.
 *  -- do ScoreRenderer here and index/DecimalRenderer do just the same thing?
 *  no: DR centers around &quot;.&quot;, SR hilites sig scores.  can the latter
 *  extend the former, though?
 *  -- extract CrossdateView (=tabs)
 *  -- extract menus?
 *  -- start renaming things to be nicer: &quot;cross&quot; =&gt; &quot;crossdate&quot;, etc.
 *  -- get rid of GridFrame (is there anything in there i want?)
 *  
 * </pre>
 * </ul>
 * 
 * <p>
 * Future: after extraction, the Crossdate views will only exist as part of CrossdateView(?),
 * so I won't even need to talk about them here.
 * </p>
 * 
 * @see corina.cross.TableView
 * @see corina.cross.GridView
 * @see corina.cross.sigscores.SignificantScoresView
 * @see corina.cross.AllScoresView
 * @see corina.cross.HistogramView
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at </i> cornell <i style="color: gray">dot </i> edu&gt;
 * @version $Id: CrossdateWindow.java,v 1.9 2006/10/08 09:53:56 lucasmo Exp $
 */
public class CrossdateWindow extends XFrame implements PrintableDocument, PrefsListener {

    private final class EditAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            dispose();
            new CrossdateKit(sequence);
        }
    }

    private final class NextPrevAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            disableButtons();
            try {
                if (e.getSource() == prevButton) sequence.prevPairing(); else sequence.nextPairing();
                crossdate = sequence.makeCross();
            } catch (IOException ioe) {
                System.out.println("ioe! -- " + ioe);
            }
            new Thread(new CrossdateRunner()).start();
        }
    }

    private final class MapAction extends AbstractAction {

        public void actionPerformed(ActionEvent ae) {
            try {
                Site s1 = SiteDB.getSiteDB().getSite(crossdate.getFixed());
                Site s2 = SiteDB.getSiteDB().getSite(crossdate.getMoving());
                new MapFrame(s1, s2);
            } catch (SiteNotFoundException e) {
                throw new IllegalArgumentException();
            }
        }
    }

    private final class DisableGraphForHistogramChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            Component component = tabPane.getSelectedComponent();
            boolean isHistogram = (component instanceof HistogramView);
            graphButton.setEnabled(!isHistogram);
        }
    }

    private final class GraphActionListener extends AbstractAction {

        public void actionPerformed(ActionEvent ae) {
            Component panel = tabPane.getSelectedComponent();
            if (panel instanceof SignificantScoresView) {
                ((SignificantScoresView) panel).graphSelectedCrossdate();
                return;
            } else if (panel instanceof AllScoresView) {
                ((AllScoresView) panel).graphSelectedCrossdate();
                return;
            }
        }
    }

    private JTabbedPane tabPane;

    private JButton graphButton;

    private JButton mapButton;

    private JButton prevButton, nextButton;

    private Sequence sequence;

    private Cross crossdate = null;

    private JLabel fixedTitle;

    private JLabel movingTitle;

    private JPanel defaultView;

    private boolean bad = false;

    private String sort = null;

    private HistogramView histo;

    private SignificantScoresView sigs;

    private AllScoresView all;

    private int view = 0;

    private JComponent gridView = null;

    private JComponent tableView = null;

    private JPanel cards;

    private CardLayout cardLayout;

    private boolean movingFloats = true;

    private boolean fixedFloats = false;

    private JLinedLabel errlabel = null;

    /**
   * Create a crossdate for a given Sequence.
   * 
   * @param sequence
   *          the Sequence to display
   */
    public CrossdateWindow(Sequence sequence) {
        this.sequence = sequence;
        try {
            crossdate = sequence.makeCross();
        } catch (IOException ioe) {
        }
        initGui();
        {
            JMenuBar menubar = new JMenuBar();
            menubar.add(new FileMenu(this));
            menubar.add(new CrossdateEditMenu());
            menubar.add(new CrossdateViewMenu());
            if (App.platform.isMac()) menubar.add(new WindowMenu(this));
            menubar.add(new HelpMenu());
            setJMenuBar(menubar);
        }
        disableButtons();
        new Thread(new CrossdateRunner()).start();
        pack();
        setSize(new Dimension(640, 480));
        show();
    }

    private String encode() {
        if (fixedFloats && !movingFloats) return "fixed"; else if (!fixedFloats && movingFloats) return "moving"; else return "both";
    }

    private void decode(String pref) {
        if (pref.equals("fixed")) {
            fixedFloats = true;
            movingFloats = false;
        } else if (pref.equals("both")) {
            fixedFloats = true;
            movingFloats = true;
        } else {
            fixedFloats = false;
            movingFloats = true;
        }
    }

    private void initGui() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards);
        defaultView = new JPanel(new BorderLayout());
        cards.add(defaultView, "default");
        prevButton = Builder.makeButton("prev");
        if (!App.platform.isMac()) prevButton.setIcon(Builder.getIcon("Back.png"));
        nextButton = Builder.makeButton("next");
        if (!App.platform.isMac()) nextButton.setIcon(Builder.getIcon("Next.png"));
        Action prevNext = new NextPrevAction();
        prevButton.addActionListener(prevNext);
        nextButton.addActionListener(prevNext);
        graphButton = Builder.makeButton("plot");
        graphButton.addActionListener(new GraphActionListener());
        mapButton = Builder.makeButton("map");
        mapButton.addActionListener(new MapAction());
        JPanel b = Layout.buttonLayout(graphButton, mapButton, null, prevButton, nextButton);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        defaultView.add(b, BorderLayout.SOUTH);
        tabPane = new JTabbedPane();
        defaultView.add(tabPane, BorderLayout.CENTER);
        tabPane.addChangeListener(new DisableGraphForHistogramChangeListener());
        fixedTitle = new JLabel(crossdate.getFixed().toString());
        JPanel f = Layout.flowLayoutL(new JLabel(I18n.getText("fixed") + ":"), fixedTitle);
        movingTitle = new JLabel(crossdate.getMoving().toString());
        JPanel m = Layout.flowLayoutL(new JLabel(I18n.getText("moving") + ":"), movingTitle);
        JPanel top = Layout.boxLayoutY(f, m);
        JButton edit = new JButton("Edit...");
        edit.addActionListener(new EditAction());
        JPanel center = Layout.borderLayout(edit, null, null, null, null);
        JPanel full = Layout.borderLayout(null, top, null, center, null);
        defaultView.add(full, BorderLayout.NORTH);
    }

    private class CrossdateRunner implements Runnable {

        public void run() {
            try {
                if (bad) {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            defaultView.remove(errlabel);
                            errlabel = null;
                            bad = false;
                            defaultView.add(tabPane, BorderLayout.CENTER);
                            defaultView.validate();
                        }
                    });
                }
                crossdate.run();
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        if (tabPane.getTabCount() == 0) {
                            initTables();
                        } else {
                            updateTables();
                        }
                    }
                });
            } catch (IllegalArgumentException iae) {
                final String message = iae.getMessage();
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        System.out.println(message);
                        errlabel = new JLinedLabel(message);
                        defaultView.remove(tabPane);
                        defaultView.add(errlabel, BorderLayout.CENTER);
                        defaultView.validate();
                        bad = true;
                        repaint();
                    }
                });
            } finally {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        fixedTitle.setText(crossdate.getFixed().toString());
                        movingTitle.setText(crossdate.getMoving().toString());
                        enableButtons();
                        setTitle(crossdate.toString() + " - " + Build.VERSION + " " + Build.TIMESTAMP);
                    }
                });
            }
        }
    }

    private void updateTables() {
        if (sort != null) Sort.sort(crossdate.getHighScores().getScores(), sort, !sort.equals("number"));
        sigs.setCrossdate(crossdate);
        all.setCrossdate(crossdate);
        histo.setCrossdate(crossdate);
    }

    private void initTables() {
        try {
            all = new AllScoresView(crossdate);
            sigs = new SignificantScoresView(crossdate);
            histo = new HistogramView(crossdate);
        } catch (Exception e) {
            new corina.gui.Bug(e);
        }
        tabPane.addTab(I18n.getText("sig_scores"), sigs);
        tabPane.addTab(I18n.getText("all_scores"), all);
        tabPane.addTab(I18n.getText("score_distro"), histo);
    }

    private boolean mapAvailable() {
        try {
            Site s1 = SiteDB.getSiteDB().getSite(crossdate.getFixed());
            Site s2 = SiteDB.getSiteDB().getSite(crossdate.getMoving());
            return (s1.getLocation() != null && s2.getLocation() != null);
        } catch (SiteNotFoundException snfe) {
            return false;
        }
    }

    private void enableButtons() {
        prevButton.setEnabled(!(sequence.isFirst()));
        nextButton.setEnabled(!(sequence.isLast()));
        graphButton.setEnabled(!(tabPane.getSelectedComponent() instanceof HistogramView));
        mapButton.setEnabled(mapAvailable());
    }

    private void disableButtons() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        graphButton.setEnabled(false);
        mapButton.setEnabled(false);
    }

    private class CrossdateEditMenu extends EditMenu {

        protected void init() {
            addUndoRedo();
            addSeparator();
            addClipboard();
            addSeparator();
            addSelectAll();
            addSeparator();
            addKit();
            addPreferences();
        }

        private void addKit() {
            JMenuItem kit = Builder.makeMenuItem("edit_crossdate");
            kit.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new CrossdateKit(sequence);
                }
            });
            add(kit);
        }
    }

    private class CrossdateViewMenu extends JMenu {

        CrossdateViewMenu() {
            super(I18n.getText("view"));
            JMenuItem asCross = Builder.makeRadioButtonMenuItem("cross_as_cross");
            asCross.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cards, "default");
                    setTitle(crossdate.toString());
                    view = 0;
                }
            });
            JMenuItem asTable = Builder.makeRadioButtonMenuItem("cross_as_table");
            asTable.setEnabled(true);
            asTable.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        if (tableView == null) {
                            tableView = new TableView(sequence);
                            cards.add(tableView, "table");
                        }
                        cardLayout.show(cards, "table");
                        setTitle(tableView.toString() + " - " + Build.VERSION + " " + Build.TIMESTAMP);
                        view = 1;
                    } catch (IOException ioe) {
                        System.out.println("ack -- " + ioe);
                    }
                }
            });
            JMenuItem asGrid = Builder.makeRadioButtonMenuItem("cross_as_grid");
            asGrid.setEnabled(true);
            asGrid.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    if (gridView == null) {
                        gridView = new GridView(sequence);
                        cards.add(gridView, "grid");
                    }
                    cardLayout.show(cards, "grid");
                    setTitle(gridView.toString() + " - " + Build.VERSION + " " + Build.TIMESTAMP);
                    view = 2;
                }
            });
            final JMenuItem moving = Builder.makeRadioButtonMenuItem("second_moving");
            final JMenuItem fixed = Builder.makeRadioButtonMenuItem("first_moving");
            final JMenuItem both = Builder.makeRadioButtonMenuItem("both_moving");
            if (App.prefs.getPref("corina.cross.dating") != null) {
                decode(App.prefs.getPref("corina.cross.dating"));
                if (!fixedFloats && movingFloats) moving.setSelected(true); else if (fixedFloats && !movingFloats) fixed.setSelected(true); else both.setSelected(true);
            } else {
                moving.setSelected(true);
            }
            Action a = new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    Object source = e.getSource();
                    if (source == moving) {
                        fixedFloats = false;
                        movingFloats = true;
                    } else if (source == fixed) {
                        fixedFloats = true;
                        movingFloats = false;
                    } else {
                        fixedFloats = true;
                        movingFloats = true;
                    }
                    repaint();
                    App.prefs.setPref("corina.cross.dating", encode());
                }
            };
            moving.addActionListener(a);
            fixed.addActionListener(a);
            both.addActionListener(a);
            {
                ButtonGroup views = new ButtonGroup();
                views.add(asCross);
                views.add(asTable);
                views.add(asGrid);
                asCross.setSelected(true);
            }
            {
                ButtonGroup ranges = new ButtonGroup();
                ranges.add(fixed);
                ranges.add(moving);
                ranges.add(both);
            }
            add(asCross);
            add(asTable);
            add(asGrid);
            addSeparator();
            add(moving);
            add(fixed);
            add(both);
        }
    }

    public void prefChanged(PrefsEvent e) {
        if (e.getPref().equals("corina.cross.overlap") || e.getPref().equals("corina.cross.d-overlap")) {
            new Thread(new CrossdateRunner()).start();
        } else {
            new Thread(new CrossdateRunner()).start();
        }
    }

    public Object getPrinter(PageFormat pf) {
        switch(view) {
            case 0:
                boolean sections[] = askSections();
                CrossdatePrinter printer = new CrossdatePrinter(crossdate, sections[0], sections[1], sections[2]);
                return printer;
            case 1:
                return ((TableView) tableView).print();
            case 2:
                return ((GridView) gridView).print(pf);
            default:
                throw new IllegalArgumentException("eek!");
        }
    }

    public String getPrintTitle() {
        return getTitle();
    }

    private boolean[] askSections() {
        final JDialog d = new JDialog(this, "Print Views", true);
        int tab = tabPane.getSelectedIndex();
        JCheckBox sigsCheckbox = new JCheckBox("Significant Scores");
        JCheckBox allCheckbox = new JCheckBox("All Scores");
        JCheckBox histoCheckbox = new JCheckBox("Histogram");
        sigsCheckbox.setSelected(tab == 0);
        allCheckbox.setSelected(tab == 1);
        histoCheckbox.setSelected(tab == 2);
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(10));
        center.add(sigsCheckbox);
        center.add(Box.createVerticalStrut(6));
        center.add(allCheckbox);
        center.add(Box.createVerticalStrut(6));
        center.add(histoCheckbox);
        center.add(Box.createVerticalStrut(12));
        JButton cancel = Builder.makeButton("cancel");
        JButton ok = Builder.makeButton("ok");
        Component spacer = Box.createHorizontalStrut(24);
        JPanel buttons = Layout.buttonLayout(spacer, cancel, ok);
        Component spacer2 = Box.createHorizontalStrut(20);
        JPanel p = Layout.borderLayout(new JLabel("Print which views?"), spacer2, center, null, buttons);
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        d.setContentPane(p);
        cancel.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        ok.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        OKCancel.addKeyboardDefaults(ok);
        d.setResizable(false);
        d.pack();
        Center.center(d, this);
        d.show();
        boolean sections[] = new boolean[] { sigsCheckbox.isSelected(), allCheckbox.isSelected(), histoCheckbox.isSelected() };
        return sections;
    }
}
