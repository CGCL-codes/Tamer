package com.mclub.client.forms.changeStatus;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.gmvc.client.base.AbstractBrowser;
import com.gmvc.client.base.IController;
import com.gmvc.client.util.ColumnFactory;
import com.gmvc.client.util.Tags;
import com.mclub.client.model.ChangeStatusDetailDTO;

/**
 * Durumu degisien filmler altindaki ekli filmler kismi, Browser sinifi
 * 
 * @see AbstractBrowser
 * 
 * @author mdpinar
 * 
 */
public class ChangeStatusMiniBrowser extends AbstractBrowser<ChangeStatusDetailDTO> {

    public ChangeStatusMiniBrowser(IController<ChangeStatusDetailDTO> controller) {
        super(controller);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void init() {
        getPanel().setLayout(new RowLayout(Orientation.VERTICAL));
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ToolBar getToolBar() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ColumnFactory getColumnFactory() {
        return new ColumnFactory().addColumn("movie.name", Tags.get("movie"), 200).addColumn("movie.copyNo", Tags.get("copy"), 50, HorizontalAlignment.RIGHT).addColumn("movie.lastStatus", Tags.get("lastStatus"), 80).addColumn("deleteButton", Tags.get("delete"), 35, ColumnFactory.getRemoveButtonCellRenderer(getController()));
    }
}
