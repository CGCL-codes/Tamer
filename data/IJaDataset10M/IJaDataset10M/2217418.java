package de.frewert.vboxj.gui.swing;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.AbstractTableModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A dialog box displaying some information.
 * <pre>
 * Copyright (C) 2001, 2003-2005 Carsten Frewert. All Rights Reserved.
 * 
 * The VBox/J package (de.frewert.vboxj.*) is distributed under
 * the terms of the Artistic license.
 * </pre>
 * @author Carsten Frewert
 * &lt;<a href="mailto:frewert@users.sourceforge.net">
 * frewert@users.sourceforge.net</a>&gt;
 * @version $Revision: 1.29 $
 */
class AboutBox extends JDialog {

    static final long serialVersionUID = -5339579097028350719L;

    private static final String BSD_LICENSE_URL = "http://www.opensource.org/licenses/bsd-license.html";

    private static final String LGPL_URL = "http://www.gnu.org/copyleft/lesser.html";

    private static final String ARTISTIC_LICENSE_URL = "http://www.opensource.org/licenses/artistic-license.html";

    private ResourceBundle messageBundle = VBoxGUI.getMsgBundle();

    /** button for closing the dialog */
    private JButton closeButton;

    private JTabbedPane tabbedPane;

    /**
     * @see #hyperlinkListener 
     * @see #buildPanel()
     */
    private Map license2TabIndex = new HashMap();

    /**
     * Create a new AboutBox.
     * @param frame the parent Frame
     */
    public AboutBox(final Frame frame) {
        super(frame, false);
        setTitle(Utils.getStringFromBundle(messageBundle, "AboutTitle"));
        setContentPane(buildPanel());
        pack();
        if (frame != null) {
            setLocationRelativeTo(frame);
        }
    }

    public void hide() {
        super.hide();
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(0);
        }
    }

    /**
     * Add a tab to the internal TabbedPane.
     * @param tabTitle the tab's title
     * @param resource the resource to display in a
     *        {@link TextLoadingPane}
     * @return index of the added tab
     */
    protected int addTab(final String tabTitle, final String resource) {
        URL url = AboutBox.class.getResource(resource);
        TextLoadingPane textPane = new TextLoadingPane();
        textPane.addHyperlinkListener(hyperlinkListener);
        textPane.load(url);
        int tabCount = tabbedPane.getTabCount();
        JScrollPane scrollPane = new JScrollPane(textPane);
        Utils.addToTabbedPane(tabbedPane, tabTitle, scrollPane);
        return tabCount;
    }

    /** Build a panel with the dialog's content */
    protected JComponent buildPanel() {
        FormLayout layout = new FormLayout("fill:default:grow", "pref, 10dlu, fill:min(150dlu;default):grow, 10dlu, min");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.addTitle(this.getTitle(), "1, 1, center, top");
        builder.nextLine(2);
        tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
        String text = Utils.getStringFromBundle(messageBundle, "aboutTabTitle");
        addTab(text, "/de/frewert/vboxj/readme.html");
        int idx = addTab("&Artistic License", "/de/frewert/vboxj/ARTISTIC");
        license2TabIndex.put(ARTISTIC_LICENSE_URL, new Integer(idx));
        idx = addTab("&LGPL", "/de/frewert/sound/LGPL");
        license2TabIndex.put(LGPL_URL, new Integer(idx));
        idx = addTab("&BSD License", "/de/frewert/vboxj/JGOODIES_LICENSE");
        license2TabIndex.put(BSD_LICENSE_URL, new Integer(idx));
        text = Utils.getStringFromBundle(messageBundle, "propertiesTabTitle");
        Utils.addToTabbedPane(tabbedPane, text, buildPropertiesPanel());
        builder.add(tabbedPane);
        builder.nextLine();
        closeButton = new JButton(Utils.getStringFromBundle(messageBundle, "closeButton"));
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AboutBox.this.hide();
            }
        });
        this.getRootPane().setDefaultButton(closeButton);
        builder.nextLine();
        builder.add(ButtonBarFactory.buildCenteredBar(closeButton));
        return builder.getPanel();
    }

    protected JComponent buildPropertiesPanel() {
        JTable table = new JTable(new PropertiesTableModel(System.getProperties()));
        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    protected class TextLoadingPane extends JEditorPane {

        static final long serialVersionUID = 1279521272328754910L;

        private URL textURL;

        private transient Thread textLoader;

        TextLoadingPane() {
            setEditable(false);
            setPreferredSize(new Dimension(500, 300));
        }

        /**
         * Load and display text at a given URL.
         * @param url the URL to load
         */
        public void load(final URL url) {
            textURL = url;
            setText(messageBundle.getString("loadingText"));
            getTextLoader().start();
        }

        /**
         * Get thread for loading the text in order to speed up the
         * display of the dialog.
         * @return text loading thread
         */
        private synchronized Thread getTextLoader() {
            if (textLoader != null) {
                return textLoader;
            }
            textLoader = new Thread("textLoader") {

                public void run() {
                    try {
                        TextLoadingPane.this.setPage(textURL);
                        TextLoadingPane.this.revalidate();
                    } catch (IOException e) {
                        TextLoadingPane.this.setText("Attempted to read a bad URL: " + textURL);
                    }
                    TextLoadingPane.this.textLoader = null;
                }
            };
            return textLoader;
        }
    }

    /**
     * This listener reacts to HyperlinkEvents of type ACTIVATED.
     * The URL's string representation is looked up in the
     * {@link #license2TabIndex} map, and the {@link #tabbedPane}'s tab with 
     * the respective index is selected.
     * Nothing is donoe if the lookup returns <code>null</code>. 
     */
    private HyperlinkListener hyperlinkListener = new HyperlinkListener() {

        public void hyperlinkUpdate(HyperlinkEvent he) {
            if (he.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                return;
            }
            Integer idx = (Integer) license2TabIndex.get(he.getURL().toString());
            if (idx != null) {
                tabbedPane.setSelectedIndex(idx.intValue());
            }
        }
    };

    public static class PropertiesTableModel extends AbstractTableModel {

        static final long serialVersionUID = -2759014136430746425L;

        private Properties props;

        private List propertyNames;

        private String[] columnNames;

        private List getPropertyNames(final Properties props) {
            List list = new ArrayList();
            if (props == null) {
                return list;
            }
            final Enumeration e = props.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                list.add(key);
            }
            Collections.sort(list);
            return list;
        }

        /**
         * Create a new PropertyTableModel instance.
         */
        public PropertiesTableModel(final Properties properties) {
            this.props = properties;
            this.propertyNames = getPropertyNames(properties);
            ResourceBundle bundle = VBoxGUI.getMsgBundle();
            columnNames = new String[] { Utils.getStringFromBundle(bundle, "property"), Utils.getStringFromBundle(bundle, "value") };
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return propertyNames.size();
        }

        public String getColumnName(final int column) {
            return columnNames[column];
        }

        public Object getValueAt(final int rowIndex, final int columnIndex) {
            String value = (String) propertyNames.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    break;
                case 1:
                    value = props.getProperty(value);
                    break;
                default:
                    throw new IndexOutOfBoundsException("columnIndex must be 0 or 1.");
            }
            return value;
        }

        /** Always returns <tt>false</tt> */
        public boolean isCellEditable(final int rowIndex, final int columnIndex) {
            return false;
        }

        /** Always returns <tt>String.class</tt> */
        public Class getColumnClass(int columnIndex) {
            return String.class;
        }
    }
}
