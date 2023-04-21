package com.jquery.antilia.grid;

import java.io.Serializable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.jquery.antilia.grid.model.IColumn;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public class DefaultHeaderCell<E extends Serializable> extends Panel {

    private static final long serialVersionUID = 1L;

    private Table<E> table;

    private int column;

    /**
	 * @param id
	 * @param model
	 */
    public DefaultHeaderCell(String id, int column, Table<E> table, IModel<String> titleModel, final IColumn<E> columnModel) {
        super(id, columnModel);
        this.table = table;
        this.column = column;
        setRenderBodyOnly(true);
        add(new HiddenField<Integer>("colWidth", new Model<Integer>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Integer getObject() {
                return DefaultHeaderCell.this.getColumnModel().getWidth();
            }

            @Override
            public void setObject(Integer object) {
                if (object instanceof Integer) {
                    DefaultHeaderCell.this.getColumnModel().setWidth(((Integer) object).intValue());
                }
            }
        }, Integer.class));
        DraggableTitle<E> draggableTarget = new DraggableTitle<E>("dragger", column) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Table<E> getTable() {
                return DefaultHeaderCell.this.getTable();
            }
        };
        if (columnModel.isSortable()) {
            draggableTarget.add(new AjaxEventBehavior("ondblclick") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    if (target != null) {
                        IColumn<E> columnModel = DefaultHeaderCell.this.getColumnModel();
                        if (!columnModel.isSortable()) {
                            return;
                        }
                        IPageableComponent<E> component = getTable();
                        IDataProvider<E> dataProvider = component.getDataProvider();
                        if (dataProvider instanceof ISortStateLocator) {
                            ISortStateLocator locator = (ISortStateLocator) dataProvider;
                            ISortState sortState = locator.getSortState();
                            if (sortState != null) {
                                int sortOrder = sortState.getPropertySortOrder(columnModel.getSortProperty());
                                if (sortOrder == ISortState.DESCENDING) sortState.setPropertySortOrder(columnModel.getSortProperty(), ISortState.ASCENDING); else if (sortOrder == ISortState.ASCENDING) sortState.setPropertySortOrder(columnModel.getSortProperty(), ISortState.DESCENDING); else sortState.setPropertySortOrder(columnModel.getSortProperty(), ISortState.ASCENDING);
                            }
                            component.reset();
                        }
                        target.addComponent((Component) component);
                    }
                }
            });
        }
        draggableTarget.setOutputMarkupId(true);
        add(draggableTarget);
        Label title = new Label("title", titleModel);
        title.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                IColumn<E> columnModel = DefaultHeaderCell.this.getColumnModel();
                if (!columnModel.isSortable()) {
                    return "headerTitle";
                }
                IPageableComponent<E> component = getTable();
                IDataProvider<E> dataProvider = component.getDataProvider();
                if (dataProvider instanceof ISortStateLocator) {
                    ISortStateLocator locator = (ISortStateLocator) dataProvider;
                    ISortState sortState = locator.getSortState();
                    if (sortState != null) {
                        int sortOrder = sortState.getPropertySortOrder(columnModel.getSortProperty());
                        if (sortOrder == ISortState.DESCENDING) return "headerTitleDesc"; else if (sortOrder == ISortState.ASCENDING) return "headerTitleAsc";
                    }
                }
                return "headerTitle";
            }
        }));
        draggableTarget.add(title);
        WebMarkupContainer dragTd = new WebMarkupContainer("dragTd");
        dragTd.add(new AttributeModifier("id", new AbstractReadOnlyModel<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                if (!DefaultHeaderCell.this.getTable().isColumnsResizable()) return DefaultHeaderCell.this.getTable().getMarkupId() + "_cND_" + DefaultHeaderCell.this.getColumn();
                if (columnModel.isResizable()) return DefaultHeaderCell.this.getTable().getMarkupId() + "_c_" + DefaultHeaderCell.this.getColumn();
                return DefaultHeaderCell.this.getTable().getMarkupId() + "_cND_" + DefaultHeaderCell.this.getColumn();
            }
        }));
        dragTd.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                if (!DefaultHeaderCell.this.getTable().isColumnsResizable()) return "noResCol";
                if (columnModel.isResizable()) return "resCol";
                return "noResCol";
            }
        }));
        add(dragTd);
        WebMarkupContainer sortInfo = new WebMarkupContainer("sortInfo");
        sortInfo.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {

            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                IColumn<E> columnModel = DefaultHeaderCell.this.getColumnModel();
                if (!columnModel.isSortable()) {
                    return "";
                }
                IPageableComponent<E> component = getTable();
                IDataProvider<E> dataProvider = component.getDataProvider();
                if (dataProvider instanceof ISortStateLocator) {
                    ISortStateLocator locator = (ISortStateLocator) dataProvider;
                    ISortState sortState = locator.getSortState();
                    if (sortState != null) {
                        int sortOrder = sortState.getPropertySortOrder(columnModel.getSortProperty());
                        if (sortOrder == ISortState.DESCENDING) return "ui-icon ui-icon-triangle-1-s"; else if (sortOrder == ISortState.ASCENDING) return "ui-icon ui-icon-triangle-1-n";
                    }
                }
                return "";
            }
        }));
        draggableTarget.add(sortInfo);
    }

    @SuppressWarnings("unchecked")
    protected IColumn<E> getColumnModel() {
        return (IColumn<E>) getDefaultModel();
    }

    public Table<E> getTable() {
        return table;
    }

    public void setTable(Table<E> table) {
        this.table = table;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
