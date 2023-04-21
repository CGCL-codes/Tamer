package jgloss.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import jgloss.JGloss;
import jgloss.Preferences;
import jgloss.dictionary.SearchException;
import jgloss.dictionary.attribute.ReferenceAttributeValue;

/**
 * Frame which ties together a {@link LookupConfigPanel LookupConfigPanel} and a
 * {@link LookupResultList LookupResultList} to do dictionary lookups.
 *
 * @author Michael Koch
 */
public class LookupFrame extends JFrame implements ActionListener, HyperlinkListener, Dictionaries.DictionaryListChangeListener {

    protected LookupConfigPanel config;

    protected LookupModel model;

    protected AsynchronousLookupEngine engine;

    protected LookupResultList list;

    protected LookupResultCache currentResults;

    protected List history;

    protected int historyPosition;

    protected static final int MAX_HISTORY_SIZE = 20;

    protected Action historyBackAction;

    protected Action historyForwardAction;

    protected Dimension preferredSize;

    protected JFrame legendFrame;

    protected AttributeLegend legend;

    public LookupFrame(LookupModel _model) {
        super(JGloss.messages.getString("wordlookup.title"));
        getContentPane().setLayout(new BorderLayout());
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        getContentPane().add(center, BorderLayout.CENTER);
        model = _model;
        config = new LookupConfigPanel(model, this);
        list = new LookupResultList();
        currentResults = new LookupResultCache(list);
        engine = new AsynchronousLookupEngine(currentResults);
        config.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        center.add(config, BorderLayout.NORTH);
        list.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(JGloss.messages.getString("wordlookup.result")), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        list.addHyperlinkListener(this);
        center.add(list, BorderLayout.CENTER);
        getRootPane().setDefaultButton(config.getSearchButton());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                hide();
                if (JGloss.exit()) dispose();
            }
        });
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu(JGloss.messages.getString("main.menu.file"));
        createFileMenuItems(menu);
        bar.add(menu);
        final JMenu editMenu = new JMenu(JGloss.messages.getString("main.menu.edit"));
        XCVManager xcv = new XCVManager();
        xcv.addManagedComponent(config.getSearchExpressionField());
        xcv.addManagedComponent(config.getDistanceField());
        editMenu.add(xcv.getCutAction());
        editMenu.add(xcv.getCopyAction());
        editMenu.add(xcv.getPasteAction());
        editMenu.addMenuListener(xcv.getEditMenuListener());
        editMenu.addSeparator();
        editMenu.add(UIUtilities.createMenuItem(PreferencesFrame.showAction));
        bar.add(editMenu);
        menu = new JMenu(JGloss.messages.getString("main.menu.help"));
        menu.add(UIUtilities.createMenuItem(AboutFrame.getShowAction()));
        bar.add(menu);
        setJMenuBar(bar);
        config.getSearchExpressionField().requestFocus();
        pack();
        preferredSize = new Dimension(Math.max(super.getPreferredSize().width, JGloss.prefs.getInt(Preferences.WORDLOOKUP_WIDTH, 0)), Math.max(super.getPreferredSize().height + 150, JGloss.prefs.getInt(Preferences.WORDLOOKUP_HEIGHT, 0)));
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                JGloss.prefs.set(Preferences.WORDLOOKUP_WIDTH, getWidth());
                JGloss.prefs.set(Preferences.WORDLOOKUP_HEIGHT, getHeight());
            }
        });
        history = new ArrayList(MAX_HISTORY_SIZE);
        historyBackAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                historyBack();
            }
        };
        UIUtilities.initAction(historyBackAction, "wordlookup.history.back");
        historyBackAction.setEnabled(false);
        historyForwardAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                historyForward();
            }
        };
        UIUtilities.initAction(historyForwardAction, "wordlookup.history.forward");
        historyForwardAction.setEnabled(false);
        Action legendAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (legendFrame == null) createLegendFrame();
                legendFrame.show();
            }
        };
        UIUtilities.initAction(legendAction, "wordlookup.showlegend");
        legendAction.setEnabled(true);
        JToolBar toolbar = new JToolBar();
        toolbar.add(historyBackAction);
        toolbar.add(historyForwardAction);
        toolbar.add(legendAction);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        setSize(getPreferredSize());
    }

    public void search(String text) {
        if (text == null || text.length() == 0) return;
        model.setSearchExpression(text);
        actionPerformed(null);
    }

    protected void createFileMenuItems(JMenu menu) {
        Action closeAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                hide();
                if (JGloss.exit()) dispose();
            }
        };
        UIUtilities.initAction(closeAction, "main.menu.close");
        menu.add(UIUtilities.createMenuItem(closeAction));
    }

    public void actionPerformed(ActionEvent event) {
        if (currentResults.isEmpty()) engine.doLookup((LookupModel) model.clone()); else {
            HistoryItem hi = createHistoryItem();
            addToHistory(hi);
            engine.doLookup(hi.lookupModel);
        }
    }

    public void dispose() {
        super.dispose();
        engine.dispose();
        Dictionaries.removeDictionaryListChangeListener(this);
        if (legendFrame != null) legendFrame.dispose();
    }

    public Dimension getPreferredSize() {
        if (preferredSize == null) return super.getPreferredSize(); else return preferredSize;
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            int colon = e.getDescription().indexOf(':');
            String protocol = e.getDescription().substring(0, colon);
            String refKey = e.getDescription().substring(colon + 1);
            followReference(protocol, refKey);
        }
    }

    protected void followReference(String type, String refKey) {
        if (LookupResultList.Hyperlinker.REFERENCE_PROTOCOL.equals(type)) {
            ReferenceAttributeValue ref = (ReferenceAttributeValue) ((HyperlinkAttributeFormatter.ReferencedAttribute) list.getReference(refKey)).getValue();
            if (ref != null) try {
                addToHistory(createHistoryItem());
                currentResults.setData(JGloss.messages.getString("wordlookup.reference", new Object[] { ref.getReferenceTitle() }), ref.getReferencedEntries());
                currentResults.replay();
            } catch (SearchException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected synchronized void createLegendFrame() {
        if (legend != null) return;
        legendFrame = new JFrame(JGloss.messages.getString("wordlookup.legendframe.title"));
        legend = new AttributeLegend();
        legend.setDictionaries(Dictionaries.getDictionaries(false));
        Dictionaries.addDictionaryListChangeListener(this);
        legendFrame.getContentPane().add(legend);
        legendFrame.pack();
        legendFrame.setSize(legendFrame.getPreferredSize());
    }

    public void dictionaryListChanged() {
        if (legend != null) {
            synchronized (legend) {
                legend.setDictionaries(Dictionaries.getDictionaries(false));
            }
        }
    }

    protected void historyBack() {
        historyPosition--;
        HistoryItem hi = (HistoryItem) history.get(historyPosition);
        history.set(historyPosition, createHistoryItem());
        if (historyPosition == 0) historyBackAction.setEnabled(false);
        historyForwardAction.setEnabled(true);
        showHistoryItem(hi);
    }

    protected void historyForward() {
        HistoryItem hi = (HistoryItem) history.get(historyPosition);
        historyPosition++;
        history.set(historyPosition - 1, createHistoryItem());
        if (historyPosition == history.size()) historyForwardAction.setEnabled(false);
        historyBackAction.setEnabled(true);
        showHistoryItem(hi);
    }

    protected void addToHistory(HistoryItem hi) {
        history.add(historyPosition, hi);
        historyPosition++;
        history = history.subList(0, historyPosition);
        if (history.size() > MAX_HISTORY_SIZE) {
            if (historyPosition * 2 > MAX_HISTORY_SIZE) {
                history.remove(0);
                historyPosition--;
            } else history.remove(history.size() - 1);
        }
        historyBackAction.setEnabled(true);
        historyForwardAction.setEnabled(false);
    }

    protected void showHistoryItem(HistoryItem hi) {
        model = hi.lookupModel;
        config.setModel(model);
        currentResults = hi.resultCache;
        currentResults.replay();
        list.restoreViewState(hi.resultState);
    }

    protected HistoryItem createHistoryItem() {
        return new HistoryItem((LookupModel) model.clone(), (LookupResultCache) currentResults.clone(), list.saveViewState());
    }

    protected static class HistoryItem {

        private LookupModel lookupModel;

        private LookupResultCache resultCache;

        private LookupResultList.ViewState resultState;

        private HistoryItem(LookupModel _lookupModel, LookupResultCache _resultCache, LookupResultList.ViewState _resultState) {
            lookupModel = _lookupModel;
            resultCache = _resultCache;
            resultState = _resultState;
        }
    }
}
