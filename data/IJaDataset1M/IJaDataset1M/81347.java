package org.webguitoolkit.ui.controls.form;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.LI;
import org.apache.ecs.html.UL;
import org.webguitoolkit.ui.ajax.ContextElement;
import org.webguitoolkit.ui.ajax.IContext;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.util.JSUtil;
import org.webguitoolkit.ui.http.ResourceServlet;

/**
 * represents a HTML-select with rows>1
 * 
 * @author Benjamin Klug
 * 
 */
public class Multiselect extends FormControl implements IMultiselect {

    protected ISelectModel model;

    private static final int MULTISELECT_ENTRY_HEIGHT = 16;

    private Collection options;

    public Multiselect() {
    }

    public Multiselect(String id) {
        super(id);
    }

    protected void endHTML(PrintWriter out) {
        Div container;
        int rows = this.getRows();
        ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
        boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
        Input input;
        container = new Div();
        container.addAttribute("wgttype", "multi");
        if (ro) {
            container.setClass("wgtMultiselectContainer_dis");
            setDefaultCssClass("wgtMultiselectContainer_dis");
        } else {
            container.setClass("wgtMultiselectContainer");
            setDefaultCssClass("wgtMultiselectContainer");
        }
        if (rows != Integer.MIN_VALUE && rows > 0) {
            this.getStyle().addHeight((MULTISELECT_ENTRY_HEIGHT * rows) + "px ;");
        }
        if (this.hasStyle()) {
            container.setStyle(this.getStyle().getOutput());
        }
        this.addHTMLSelectList(container);
        input = new Input();
        input.setType("hidden");
        input.setValue("");
        input.setID(getId() + "_hidden");
        container.addElement(input);
        container.setID(this.getId());
        container.output(out);
    }

    public void setDefaultModel(DefaultSelectModel model) {
        this.model = model;
    }

    public DefaultSelectModel getDefaultModel() {
        return (DefaultSelectModel) getModel();
    }

    public void setModel(ISelectModel model) {
        this.model = model;
    }

    /**
	 * return the mode of this select list. Usually this is an DefaultSelectMode or AssociationSelectModel. There are various
	 * methods wich actually create the model for you. There is little use to call this method from an application.
	 * 
	 * @return model.
	 */
    public ISelectModel getModel() {
        if (model == null) {
            model = new DefaultSelectModel();
        }
        return model;
    }

    /**
	 * return the state
	 */
    public boolean isReadOnly() {
        ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
        boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
        return ro;
    }

    public void setRows(int newRows) {
        getContext().add(getId() + ".rows", String.valueOf(newRows), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
    }

    /**
	 * Use this parameter to set the maximum number of selection. Default behaviour is MultiSelect with no limitation
	 * (MAX_SELECTED_OPTIONS_UNLIMITED).
	 * 
	 * @param newCount number of
	 */
    public void setMaxSelectedOptions(int newCount) {
        getContext().add(getId() + ".mso", String.valueOf(newCount), IContext.TYPE_TXT, IContext.STATUS_NOT_EDITABLE);
    }

    private int getRows() {
        int rows = getContext().processInt(getId() + ".rows");
        return rows;
    }

    private int getMaxSelectedOptions() {
        int mso = getContext().processInt(getId() + ".mso");
        if (mso == Integer.MIN_VALUE) {
            mso = MAX_SELECTED_OPTIONS_UNLIMITED;
        }
        return mso;
    }

    public void loadList(Collection newOptions) {
        options = newOptions;
        if (getContext().getValue(getId() + ".value") == null) {
            setValue(StringUtils.EMPTY);
        }
    }

    protected void addHTMLSelectList(Div container) {
        if (options == null || options.isEmpty()) return;
        int mso = this.getMaxSelectedOptions();
        ContextElement ce = getContext().processContextElement(getId() + DOT_RO);
        boolean ro = ce != null && IContext.TYPE_READONLY.equals(ce.getType());
        UL list = new UL();
        list.setClass("wgtMultiselect_entries");
        list.setID(getId() + "_list");
        LI listElement = null;
        container.addElement(list);
        Iterator ioptions = options.iterator();
        while (ioptions.hasNext()) {
            String[] object = (String[]) ioptions.next();
            listElement = new LI(object[1]);
            if (ro) listElement.setClass("wgtMultiselect_entry_dis"); else listElement.setClass("wgtMultiselect_entry");
            listElement.setID(getId() + "_" + object[0]);
            if (hasActionListener()) {
                listElement.setOnClick("clickMultiselectEntry(this,'" + this.getId() + "'," + mso + ");" + JSUtil.jsEventParam(getId(), object) + JSUtil.jsFireEvent(getId(), ClientEvent.TYPE_ACTION));
            } else {
                listElement.setOnClick("javascript:clickMultiselectEntry(this,'" + this.getId() + "'," + mso + ");");
            }
            listElement.addAttribute("value", object[0]);
            list.addElement(listElement);
        }
    }

    public void setValue(String value) {
        getContext().add(getId() + ".value", value, IContext.TYPE_MULTI_SELECT_VALUE, IContext.STATUS_EDITABLE);
    }

    public void dispatch(ClientEvent event) {
        if (this.isReadOnly()) {
            return;
        } else {
            super.dispatch(event);
        }
    }

    public String getValue() {
        return getContext().getValue(getId() + ".value");
    }

    @Override
    protected void init() {
        super.init();
        getPage().addWgtJS(ResourceServlet.PREFIX_WGT_CONTROLLER + "multiselect.js");
    }
}
