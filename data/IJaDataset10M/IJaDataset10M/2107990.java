package com.gwtaf.ext.core.client.field;

import com.google.gwt.user.client.DOM;
import com.gwtaf.core.client.datacontext.IDataContext;
import com.gwtaf.core.client.fieldadapter.RichTextAreaAdapter;
import com.gwtaf.core.shared.meta.IFieldInfoEx;
import com.gwtaf.core.shared.util.StringUtil;
import com.gwtaf.ext.core.client.widget.LabelPanel;
import com.gwtext.client.widgets.Panel;

public class RichTextAreaAdapterExt<M> extends RichTextAreaAdapter<M> {

    public <V> RichTextAreaAdapterExt(IFieldInfoEx<M, V> fi, IDataContext<M> ctx) {
        super(fi, ctx);
    }

    public <V> RichTextAreaAdapterExt(IFieldInfoEx<M, V> fi, IDataContext<M> ctx, boolean toolBarActive) {
        super(fi, ctx, toolBarActive);
    }

    public <V> RichTextAreaAdapterExt(IFieldInfoEx<M, V> fi, IDataContext<M> ctx, Panel parentPanel) {
        this(fi, ctx);
        if (StringUtil.isValid(fi.getDisplayName())) {
            LabelPanel widget = new LabelPanel(fi.getDisplayName());
            DOM.setStyleAttribute(widget.getElement(), "padding", "10px 0px 0px 0px");
            parentPanel.add(widget);
        }
    }

    public <V> RichTextAreaAdapterExt(IFieldInfoEx<M, V> fi, IDataContext<M> ctx, Panel parentPanel, boolean toolBarActive) {
        this(fi, ctx, toolBarActive);
        LabelPanel widget = new LabelPanel(fi.getDisplayName());
        if (StringUtil.isValid(fi.getDisplayName())) {
            DOM.setStyleAttribute(widget.getElement(), "padding", "10px 0px 0px 0px");
            parentPanel.add(widget);
        }
    }
}
