package org.td4j.swing.internal.workbench;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.td4j.core.binding.model.IndividualDataConnector;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.binding.model.ListDataProxy;
import org.td4j.core.internal.binding.model.converter.DefaultConverterRepository;
import org.td4j.core.internal.binding.model.converter.IConverter;
import org.td4j.core.reflect.IndividualProperty;
import org.td4j.core.reflect.ListProperty;
import org.td4j.swing.binding.ButtonController;
import org.td4j.swing.binding.LabelController;
import org.td4j.swing.binding.LinkController;
import org.td4j.swing.binding.ListController;
import org.td4j.swing.binding.TableController;
import org.td4j.swing.binding.TextController;
import org.td4j.swing.binding.WidgetBuilder;
import org.td4j.swing.internal.binding.TableControllerFactory;
import ch.miranet.commons.TK;

public class GenericPanelBuilder {

    private final WidgetBuilder<?> wBuilder;

    private final JPanel panel;

    public GenericPanelBuilder(final WidgetBuilder<?> wBuilder) {
        this.wBuilder = TK.Objects.assertNotNull(wBuilder, "wBuilder");
        this.panel = new JPanel(new GridBagLayout());
    }

    public JPanel getPanel() {
        return panel;
    }

    public void addIndividualProperty(final IndividualProperty property) {
        final Class<?> valueType = property.getValueType();
        final IndividualDataProxy proxy = createProxy(property, property.getName(), valueType);
        final JLabel label = addLabel(property);
        if (property.canChoose()) {
            addSelectionWidget(label, proxy, property);
        } else if (valueType == Boolean.class || valueType == boolean.class) {
            addBooleanWidget(label, proxy);
        } else if (valueType == String.class || proxy.getConverter() != null) {
            if (property.canWrite()) {
                addTextWidget(label, proxy);
            } else {
                addLabelWidget(label, proxy);
            }
        } else if (wBuilder.getNavigator().isTypeNavigatable(valueType)) {
            addLinkWidget(label, proxy);
        } else {
            addLabelWidget(label, proxy);
        }
        wBuilder.getMediator().addContextSocket(proxy);
    }

    public void addIndividualDataConnector(final IndividualDataConnector connector, final String name, final Class<?> valueType, final String labelTooltipText) {
        final IndividualDataProxy proxy = createProxy(connector, name, valueType);
        final JLabel label = addLabel(name);
        label.setToolTipText(labelTooltipText);
        if (valueType == Boolean.class || valueType == boolean.class) {
            addBooleanWidget(label, proxy);
        } else if (valueType == String.class || proxy.getConverter() != null) {
            addTextWidget(label, proxy);
        }
        wBuilder.getMediator().addContextSocket(proxy);
    }

    protected void postAddWidget(Component comp) {
    }

    private IndividualDataProxy createProxy(IndividualDataConnector dataConnector, String name, Class<?> valueType) {
        final IConverter toStringConverter = DefaultConverterRepository.INSTANCE.getConverter(valueType, String.class);
        final IndividualDataProxy proxy = new IndividualDataProxy(dataConnector, name, toStringConverter);
        return proxy;
    }

    private JLabel addLabel(IndividualProperty property) {
        final int anchor = property.canChoose() ? GridBagConstraints.NORTHWEST : GridBagConstraints.WEST;
        return addLabel(anchor);
    }

    private JLabel addLabel(ListProperty property) {
        return addLabel(GridBagConstraints.NORTHWEST);
    }

    private JLabel addLabel(String labelText) {
        return addLabel(GridBagConstraints.WEST);
    }

    private JLabel addLabel(final int anchor) {
        final JLabel label = new JLabel();
        panel.add(label, new GridBagConstraints(0, -1, 1, 1, 0.0, 0.0, anchor, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        return label;
    }

    private void addSelectionWidget(JLabel label, IndividualDataProxy selectionProxy, IndividualProperty property) {
        final ListController choiceController = wBuilder.caption(label).list().bindConnector(property.getChoiceOptions(), property.getName());
        final JList list = choiceController.getWidget();
        wBuilder.selection(list).bind(selectionProxy);
        panel.add(list, new GridBagConstraints(1, -1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        postAddWidget(list);
    }

    protected void addBooleanWidget(JLabel label, IndividualDataProxy proxy) {
        final ButtonController btnController = wBuilder.caption(label).button().bind(proxy);
        final AbstractButton btn = btnController.getWidget();
        panel.add(btn, new GridBagConstraints(1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        postAddWidget(btn);
    }

    private void addTextWidget(JLabel label, IndividualDataProxy proxy) {
        final TextController textController = wBuilder.caption(label).text().bind(proxy);
        final JTextField text = textController.getWidget();
        panel.add(text, new GridBagConstraints(1, -1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        postAddWidget(text);
    }

    private void addLinkWidget(JLabel label, IndividualDataProxy proxy) {
        final LinkController linkController = wBuilder.caption(label).link().bind(proxy);
        final JLabel link = linkController.getWidget();
        panel.add(link, new GridBagConstraints(1, -1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        postAddWidget(link);
    }

    private void addLabelWidget(JLabel label, IndividualDataProxy proxy) {
        final LabelController<JLabel> labelController = wBuilder.caption(label).label().bind(proxy);
        final JLabel valueLabel = labelController.getWidget();
        panel.add(valueLabel, new GridBagConstraints(1, -1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        postAddWidget(valueLabel);
    }

    public void addListProperty(ListProperty property) {
        final ListDataProxy listProxy = new ListDataProxy(property, property.getName(), property.getNestedProperties());
        wBuilder.getMediator().addContextSocket(listProxy);
        final JLabel label = addLabel(property);
        final TableControllerFactory tableCtrlFactory = wBuilder.caption(label).table();
        final TableController tableController = tableCtrlFactory.bind(listProxy);
        final JTable table = tableController.getWidget();
        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(100, 100));
        panel.add(scrollPane, new GridBagConstraints(1, -1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        postAddWidget(table);
    }
}
