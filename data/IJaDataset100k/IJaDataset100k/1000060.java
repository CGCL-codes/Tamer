package net.sf.jzeno.echo.databinding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.StringTokenizer;
import nextapp.echo.ListBox;
import nextapp.echo.ListCellRenderer;
import nextapp.echo.ListModel;
import nextapp.echo.ListSelectionModel;
import nextapp.echoservlet.ClientActionProducer;
import nextapp.echoservlet.ClientInputProducer;
import nextapp.echoservlet.ClientObjects;
import nextapp.echoservlet.ComponentPeer;
import nextapp.echoservlet.ComponentStyle;
import nextapp.echoservlet.EchoServer;
import nextapp.echoservlet.RenderingContext;
import nextapp.echoservlet.Service;
import nextapp.echoservlet.StaticText;
import nextapp.echoservlet.html.Element;
import nextapp.echoservlet.html.ElementNames;
import nextapp.echoservlet.html.Html;
import nextapp.echoservlet.resource.ResourceNames;

/**
 * A peer for <code>ListBox</code> components.
 */
public class DynaListBoxUI extends ComponentPeer implements ClientInputProducer, PropertyChangeListener, ClientActionProducer {

    public static final long serialVersionUID = 1L;

    /**
     * A service that will provide the JavaScript used to manage a list box's
     * state.
     */
    private static final Service SERVICE_LIST_BOX_SCRIPT = StaticText.createFromResource(ClientObjects.STD_PREFIX + "dynalistbox", ResourceNames.LIST_BOX_SCRIPT);

    static {
        EchoServer.addGlobalService(SERVICE_LIST_BOX_SCRIPT);
    }

    /**
     * Indicates whether the component was most recently rendered editable and
     * enabled.
     */
    protected boolean renderedActive = true;

    /**
     * @see nextapp.echoservlet.ClientInputProducer#clientInput(String)
     */
    public void clientInput(String input) {
        if (renderedActive) {
            ListBox listBox = (ListBox) getComponent();
            StringTokenizer st = new StringTokenizer(input, ",");
            int size = listBox.getModel().size();
            int nextSelectedIndex = -1;
            for (int index = 0; index < size; ++index) {
                if (nextSelectedIndex == -1 && st.hasMoreTokens()) {
                    nextSelectedIndex = Integer.parseInt(st.nextToken());
                }
                if (index == nextSelectedIndex) {
                    nextSelectedIndex = -1;
                    listBox.setSelectedIndex(index, true);
                } else {
                    listBox.setSelectedIndex(index, false);
                }
            }
        }
    }

    /**
     * Returns the JavaScript method call that will be invoked when the state of
     * the list box is altered by the user.
     * 
     * @return The JavaScript method call that will be invoked when the state of
     *         the list box is altered by the user.
     */
    private String getOnChangeScript() {
        StringBuffer sb = new StringBuffer(ClientObjects.STD_PREFIX);
        sb.append("listSelectionChanged(\'");
        sb.append(getId().toString());
        sb.append("\', this); ");
        DynaListBox listBox = (DynaListBox) getComponent();
        if (listBox.isDirectSubmitSelection()) {
            sb.append(ClientObjects.STD_PREFIX + "getController()." + ClientObjects.STD_PREFIX + "setAction('" + getId() + "', ''); ");
        }
        return sb.toString();
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent e) {
        redraw();
    }

    /**
     * @see nextapp.echoservlet.ComponentPeer#registered()
     */
    public void registered() {
        getComponent().addPropertyChangeListener(this);
    }

    /**
     * @see nextapp.echoservlet.ComponentPeer#render(RenderingContext, Element)
     */
    public void render(RenderingContext rc, Element parent) {
        rc.getDocument().addScriptInclude(SERVICE_LIST_BOX_SCRIPT);
        ListBox listBox = (ListBox) getComponent();
        renderedActive = listBox.isEnabled();
        ListCellRenderer renderer = listBox.getCellRenderer();
        ListModel model = listBox.getModel();
        ListSelectionModel selectionModel = listBox.getSelectionModel();
        int size = model.size();
        Object item;
        ComponentStyle style = ComponentStyle.forComponent(this, ComponentStyle.EXPLICIT_SET_ALL);
        if (listBox.getWidth() > 0) {
            switch(listBox.getWidthUnits()) {
                case ListBox.PIXEL_UNITS:
                    style.addAttribute(ElementNames.Attributes.WIDTH, listBox.getWidth(), ComponentStyle.PIXEL_UNITS);
                    break;
                case ListBox.PERCENT_UNITS:
                    style.addAttribute(ElementNames.Attributes.WIDTH, listBox.getWidth(), ComponentStyle.PERCENT_UNITS);
                    break;
            }
        }
        style.addElementType(ElementNames.SELECT);
        String styleName = rc.getDocument().addStyle(style);
        Element select = new Element(ElementNames.SELECT);
        select.addAttribute(ElementNames.Attributes.ID, getId().toString());
        Element option;
        select.addAttribute(ElementNames.Attributes.CLASS, styleName);
        select.addAttribute("name", ClientObjects.getObjectName(getId()));
        select.addAttribute("size", listBox.getVisibleRowCount());
        select.addAttribute("onchange", getOnChangeScript());
        if (listBox.getSelectionMode() == ListSelectionModel.MULTIPLE_SELECTION) {
            select.addAttribute("multiple");
        }
        if (!listBox.isEnabled()) {
            select.addAttribute("disabled");
        }
        if (listBox.getToolTipText() != null) {
            select.addAttribute("title", Html.encode(listBox.getToolTipText(), Html.NEWLINE_TO_SPACE));
        }
        for (int index = 0; index < size; ++index) {
            option = new Element(ElementNames.OPTION);
            item = model.get(index);
            Object renderedValue = renderer.getListCellRendererComponent(listBox, item, index);
            option.addText(renderedValue.toString());
            if (renderedValue instanceof ListCellRenderer.StyledListCell) {
                ComponentStyle cellStyle = new ComponentStyle();
                cellStyle.setBackground(((ListCellRenderer.StyledListCell) renderedValue).getBackground());
                cellStyle.setFont(((ListCellRenderer.StyledListCell) renderedValue).getFont());
                cellStyle.setForeground(((ListCellRenderer.StyledListCell) renderedValue).getForeground());
                cellStyle.addElementType(ElementNames.OPTION);
                String cellStyleName = rc.getDocument().addStyle(cellStyle);
                option.addAttribute("class", cellStyleName);
            }
            option.setWhitespaceRelevant(true);
            option.addAttribute("value", index);
            if (selectionModel.isSelectedIndex(index)) {
                option.addAttribute("selected");
            }
            select.add(option);
        }
        parent.add(select);
        rc.getDocument().setCursorOnNewLine(false);
    }

    /**
     * @see nextapp.echoservlet.ComponentPeer#unregistered()
     */
    public void unregistered() {
        getComponent().removePropertyChangeListener(this);
    }

    public void clientAction(String action) {
        DynaListBox listBox = (DynaListBox) getComponent();
        listBox.doAction();
    }
}
