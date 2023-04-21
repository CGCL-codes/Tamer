package com.google.gdt.eclipse.designer.gwtext.model.layout.assistant;

import com.google.gdt.eclipse.designer.gwtext.model.layout.table.TableLayoutInfo;
import org.eclipse.wb.internal.core.utils.ui.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * Assistant for GWT-Ext {@link TableLayoutInfo}.
 * 
 * @author sablin_aa
 * @coverage GWTExt.model.layout.assistant
 */
public final class TableLayoutAssistantPage extends AbstractGwtExtAssistantPage {

    public TableLayoutAssistantPage(Composite parent, TableLayoutInfo selection) {
        super(parent, selection);
        GridLayoutFactory.create(this).columns(2);
        addIntegerProperty(this, "columns");
        addStringProperty(this, "spacing");
        addFiller(this);
        addBooleanProperty(this, "renderHidden");
    }
}
