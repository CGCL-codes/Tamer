package com.doculibre.wicket.panels.crud;

import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.link.IPageLink;
import wicket.markup.html.list.ListItem;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import wicket.model.Model;
import wicket.util.lang.PropertyResolver;
import com.doculibre.intelligid.wicket.panels.mesdossiers.commun.champs.consultation.LinkPanel;
import com.doculibre.wicket.panels.LabelPanel;

@SuppressWarnings("serial")
public abstract class LinkCrudTableColumn implements ICRUDTableColumn {

    public static final String DEFAULT_STYLE_CLASS = "crudColumn";

    private IModel titleModel;

    private IModel styleClassModel;

    public LinkCrudTableColumn(String title) {
        this(new Model(title), new Model(DEFAULT_STYLE_CLASS));
    }

    public LinkCrudTableColumn(IModel titleModel) {
        this(titleModel, new Model(DEFAULT_STYLE_CLASS));
    }

    public LinkCrudTableColumn(String title, String styleClass) {
        this(new Model(title), new Model(styleClass));
    }

    public LinkCrudTableColumn(IModel titleModel, IModel styleClassModel) {
        this.titleModel = titleModel;
        this.styleClassModel = styleClassModel;
    }

    public WebMarkupContainer getTitle(String id) {
        return new LabelPanel(id, titleModel);
    }

    public WebMarkupContainer getData(String id, ListItem rowItem) {
        Object item = rowItem.getModelObject();
        return (WebMarkupContainer) new LinkPanel(id, getTitleModel(item), getIPageLink(item)) {

            @Override
            protected String getStyleClass() {
                return getLinkStyleClass();
            }
        }.setVisible(isVisible(item));
    }

    protected abstract IPageLink getIPageLink(Object item);

    protected abstract IModel getTitleModel(Object item);

    protected String getLinkStyleClass() {
        return null;
    }

    protected boolean isVisible(Object item) {
        return true;
    }

    public String getStyleClass() {
        Object styleObject = styleClassModel.getObject(null);
        return styleObject != null ? styleObject.toString() : null;
    }

    public abstract static class ByProperty extends LinkCrudTableColumn {

        private String propertyName;

        public ByProperty(IModel titleModel, String propertyName, IModel styleClassModel) {
            super(titleModel, styleClassModel);
            this.propertyName = propertyName;
        }

        public ByProperty(IModel titleModel, String propertyName) {
            super(titleModel);
            this.propertyName = propertyName;
        }

        public ByProperty(String title, String propertyName, String styleClass) {
            super(title, styleClass);
            this.propertyName = propertyName;
        }

        public ByProperty(String title, String propertyName) {
            super(title);
            this.propertyName = propertyName;
        }

        @Override
        protected IModel getTitleModel(final Object item) {
            return new LoadableDetachableModel() {

                protected Object load() {
                    String title;
                    if (propertyName != null) {
                        Object propertyValue = PropertyResolver.getValue(propertyName, item);
                        title = propertyValue != null ? propertyValue.toString() : "";
                    } else {
                        title = item != null ? item.toString() : "";
                    }
                    return title;
                }
            };
        }
    }
}
