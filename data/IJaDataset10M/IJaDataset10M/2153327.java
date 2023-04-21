package org.columba.mail.gui.config.mailboximport;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.javaprog.ui.wizard.AbstractStep;
import net.javaprog.ui.wizard.DataModel;
import net.javaprog.ui.wizard.DefaultDataLookup;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.PluginException;
import org.columba.core.gui.base.MultiLineLabel;
import org.columba.core.logging.Logging;
import org.columba.mail.folder.mailboximport.AbstractMailboxImporter;
import org.columba.mail.util.MailResourceLoader;

class PluginStep extends AbstractStep implements ListSelectionListener {

    protected DataModel data;

    protected MultiLineLabel descriptionLabel;

    private IExtensionHandler pluginHandler;

    public PluginStep(DataModel data) {
        super(MailResourceLoader.getString("dialog", "mailboximport", "plugin"), MailResourceLoader.getString("dialog", "mailboximport", "plugin_description"));
        this.data = data;
        pluginHandler = (IExtensionHandler) data.getData("Plugin.handler");
    }

    protected JComponent createComponent() {
        descriptionLabel = new MultiLineLabel("description");
        JList list = new JList(pluginHandler.getPluginIdList());
        list.setCellRenderer(new PluginListCellRenderer());
        JComponent component = new JPanel(new BorderLayout(0, 30));
        component.add(new MultiLineLabel(MailResourceLoader.getString("dialog", "mailboximport", "plugin_text")), BorderLayout.NORTH);
        JPanel middlePanel = new JPanel();
        middlePanel.setAlignmentX(1);
        GridBagLayout layout = new GridBagLayout();
        middlePanel.setLayout(layout);
        Method method = null;
        try {
            method = list.getClass().getMethod("getSelectedValue", null);
        } catch (NoSuchMethodException nsme) {
        }
        data.registerDataLookup("Plugin.ID", new DefaultDataLookup(list, method, null));
        list.addListSelectionListener(this);
        list.setSelectedIndex(0);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.weighty = 1.0;
        layout.setConstraints(scrollPane, c);
        middlePanel.add(scrollPane);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.6;
        c.gridx = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(0, 10, 0, 0);
        JScrollPane scrollPane2 = new JScrollPane(descriptionLabel);
        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        layout.setConstraints(scrollPane2, c);
        middlePanel.add(scrollPane2);
        component.add(middlePanel);
        return component;
    }

    public void valueChanged(ListSelectionEvent event) {
        AbstractMailboxImporter importer;
        try {
            IExtension extension = pluginHandler.getExtension((String) data.getData("Plugin.ID"));
            importer = (AbstractMailboxImporter) extension.instanciateExtension(null);
            String description = importer.getDescription();
            descriptionLabel.setText(description);
        } catch (PluginException e) {
            if (Logging.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    public void prepareRendering() {
    }
}
