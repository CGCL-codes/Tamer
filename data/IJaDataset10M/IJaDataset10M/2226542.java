package org.td4j.swing.binding;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.td4j.core.binding.Mediator;
import org.td4j.core.binding.model.Caption;
import org.td4j.core.binding.model.DataConnectorFactory;
import org.td4j.core.internal.binding.model.JavaDataConnectorFactory;
import org.td4j.core.metamodel.MetaModel;
import org.td4j.swing.internal.binding.ButtonControllerFactory;
import org.td4j.swing.internal.binding.LabelControllerFactory;
import org.td4j.swing.internal.binding.LinkControllerFactory;
import org.td4j.swing.internal.binding.ListControllerFactory;
import org.td4j.swing.internal.binding.ListModelAdapter;
import org.td4j.swing.internal.binding.ListSelectionWidgetAdapter;
import org.td4j.swing.internal.binding.SelectionControllerFactory;
import org.td4j.swing.internal.binding.TableControllerFactory;
import org.td4j.swing.internal.binding.TableModelAdapter;
import org.td4j.swing.internal.binding.TableSelectionWidgetAdapter;
import org.td4j.swing.internal.binding.TextControllerFactory;
import org.td4j.swing.workbench.Navigator;
import ch.miranet.commons.TK;

public class WidgetBuilder<T> {

    private final Mediator<T> mediator;

    private final DataConnectorFactory connectorFactory = new JavaDataConnectorFactory();

    private final Navigator navigator;

    private boolean autoCaptions = true;

    private Caption currentCaption;

    private final MetaModel metaModel;

    public WidgetBuilder(Class<T> observableType) {
        this(observableType, null, null);
    }

    public WidgetBuilder(Class<T> observableType, MetaModel metaModel, Navigator navigator) {
        this(new Mediator<T>(observableType), metaModel, navigator);
    }

    public WidgetBuilder(Mediator<T> mediator, MetaModel metaModel, Navigator navigator) {
        this.mediator = TK.Objects.assertNotNull(mediator, "mediator");
        this.metaModel = metaModel;
        this.navigator = navigator;
    }

    public Mediator<T> getMediator() {
        return mediator;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public boolean isAutoCaptions() {
        return autoCaptions;
    }

    public void setAutoCaptions(boolean autoCaptions) {
        this.autoCaptions = autoCaptions;
    }

    public TextControllerFactory text() {
        widgetPreCreate();
        final JTextField widget = new JTextField();
        final FocusAdapter selectTextOnFocusGained = new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                widget.selectAll();
            }
        };
        widget.addFocusListener(selectTextOnFocusGained);
        return text(widget);
    }

    public TextControllerFactory text(JTextField widget) {
        final TextControllerFactory factory = new TextControllerFactory(mediator, connectorFactory, widget, useCurrentCaption());
        return factory;
    }

    public LinkControllerFactory link() {
        widgetPreCreate();
        final JLabel widget = new JLabel();
        adjustLabelFont(widget);
        return link(widget);
    }

    public LinkControllerFactory link(JLabel widget) {
        final LinkControllerFactory factory = new LinkControllerFactory(mediator, connectorFactory, widget, useCurrentCaption(), navigator);
        return factory;
    }

    public ButtonControllerFactory button() {
        widgetPreCreate();
        final JCheckBox widget = new JCheckBox();
        return button(widget);
    }

    public ButtonControllerFactory button(AbstractButton widget) {
        final ButtonControllerFactory factory = new ButtonControllerFactory(mediator, connectorFactory, widget, useCurrentCaption());
        return factory;
    }

    public WidgetBuilder<T> caption() {
        final JLabel label = new JLabel();
        return caption(label);
    }

    public WidgetBuilder<T> caption(JLabel widget) {
        if (currentCaption != null) throw new IllegalStateException("caption pending");
        this.currentCaption = new LabelCaption(widget);
        return this;
    }

    public WidgetBuilder<T> caption(Caption widget) {
        if (currentCaption != null) throw new IllegalStateException("caption pending");
        this.currentCaption = TK.Objects.assertNotNull(widget, "widget");
        return this;
    }

    public LabelControllerFactory<JLabel> label() {
        widgetPreCreate();
        final JLabel widget = new JLabel();
        adjustLabelFont(widget);
        return label(widget);
    }

    public <L extends Component> LabelControllerFactory<L> label(L widget) {
        final LabelControllerFactory<L> factory = new LabelControllerFactory<L>(mediator, connectorFactory, widget, useCurrentCaption());
        return factory;
    }

    public ListControllerFactory list() {
        widgetPreCreate();
        final JList widget = new JList();
        return list(widget);
    }

    public ListControllerFactory list(JList widget) {
        final ListControllerFactory factory = new ListControllerFactory(mediator, connectorFactory, widget, useCurrentCaption());
        return factory;
    }

    public SelectionControllerFactory selection(JList list) {
        final ListModelAdapter modelAdapter = new ListModelAdapter(list.getModel());
        final ListSelectionWidgetAdapter selectionAdapter = new ListSelectionWidgetAdapter(list);
        final SelectionControllerFactory factory = new SelectionControllerFactory(mediator, connectorFactory, list.getSelectionModel(), modelAdapter, selectionAdapter);
        return factory;
    }

    public TableControllerFactory table() {
        widgetPreCreate();
        final JTable widget = new JTable();
        widget.setRowHeight(22);
        final TableCellRenderer headerRenderer = widget.getTableHeader().getDefaultRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(JLabel.LEFT);
        }
        return table(widget);
    }

    public TableControllerFactory table(JTable widget) {
        final TableControllerFactory factory = new TableControllerFactory(mediator, connectorFactory, metaModel, widget, useCurrentCaption(), navigator);
        return factory;
    }

    public SelectionControllerFactory selection(JTable table) {
        final TableModel tableModel = table.getModel();
        if (!(tableModel instanceof TableController.MyTableModel)) {
            throw new IllegalStateException("tableModel must be " + TableController.MyTableModel.class.getName());
        }
        final TableModelAdapter modelAdapter = new TableModelAdapter((TableController.MyTableModel) tableModel);
        final TableSelectionWidgetAdapter selectionWidget = new TableSelectionWidgetAdapter(table);
        final SelectionControllerFactory factory = new SelectionControllerFactory(mediator, connectorFactory, table.getSelectionModel(), modelAdapter, selectionWidget);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return factory;
    }

    protected void widgetPreCreate() {
        if (autoCaptions && currentCaption == null) {
            caption();
        }
    }

    private Caption useCurrentCaption() {
        final Caption result = currentCaption;
        currentCaption = null;
        return result;
    }

    private static Font plainLabelFont;

    protected JLabel adjustLabelFont(JLabel label) {
        if (plainLabelFont == null) plainLabelFont = label.getFont().deriveFont(Font.PLAIN);
        label.setFont(plainLabelFont);
        return label;
    }
}
