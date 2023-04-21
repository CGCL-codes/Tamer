package com.wrupple.muba.catalogs.client.widgets.editors.composite;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.inject.Inject;
import com.wrupple.muba.catalogs.client.cms.service.CatalogManagementSystem;
import com.wrupple.muba.catalogs.client.module.services.logic.CatalogDescriptionService;
import com.wrupple.muba.catalogs.client.module.services.logic.FieldConversionStrategyProvider;
import com.wrupple.muba.catalogs.client.module.services.logic.FieldDescriptionService;
import com.wrupple.muba.catalogs.client.module.services.presentation.GenericFieldFactory;
import com.wrupple.muba.catalogs.domain.JsCatalogKey;
import com.wrupple.muba.common.shared.State.StorageManager;
import com.wrupple.vegetate.domain.FieldDescriptor;

public class LayoutCatalogEditor extends CompositeCatalogEditor<JsCatalogKey> {

    public static final String PERCENTAGE_LAYOUT = "pct";

    public static final String PIXEL_LAYOUT = "px";

    public static final String FRAME_LAYOUT = "frame";

    public static final String POSITION_LAYOUT = "position";

    public static class CatalogLayoutDescriptor extends JavaScriptObject {

        public CatalogLayoutDescriptor() {
            super();
        }

        public native String getLayoutUnit();

        public native String getConstrainType();

        public native double getTop(String fieldId);

        public native double getLeft(String fieldId);

        public native double getHeight(String fieldId);

        public native double getWidth(String fieldId);

        public native double getBottom(String fieldId);

        public native double getRight(String fieldId);
    }

    protected CatalogLayoutDescriptor layoutDescriptor;

    private Unit layoutUnit;

    private LayoutPanel main;

    @Inject
    public LayoutCatalogEditor(CatalogManagementSystem cms, StorageManager sm, FieldDescriptionService fieldService, CatalogDescriptionService catalogSErvice, FieldConversionStrategyProvider conversion, GenericFieldFactory fieldDactory) {
        super(cms, sm, fieldService, catalogSErvice, conversion, fieldDactory);
        main = new LayoutPanel();
        initWidget(main);
    }

    @Override
    protected void maybeAddField(HasValue<Object> field, FieldDescriptor fdescriptor) {
        IsWidget widget = (IsWidget) field;
        String layoutConstrain = this.layoutDescriptor.getConstrainType();
        String fieldId = fdescriptor.getId();
        double top = layoutDescriptor.getTop(fieldId);
        double left = layoutDescriptor.getLeft(fieldId);
        if (main.getWidgetIndex(widget) < 0) {
            main.add(widget);
        }
        if (POSITION_LAYOUT.equals(layoutConstrain)) {
            double height = layoutDescriptor.getHeight(fieldId);
            double width = layoutDescriptor.getWidth(fieldId);
            main.setWidgetTopHeight(widget, top, layoutUnit, height, layoutUnit);
            main.setWidgetLeftWidth(widget, left, layoutUnit, width, layoutUnit);
        } else if (FRAME_LAYOUT.equals(layoutConstrain)) {
            double bottom = layoutDescriptor.getBottom(fieldId);
            double right = layoutDescriptor.getRight(fieldId);
            main.setWidgetTopBottom(widget, top, layoutUnit, bottom, layoutUnit);
            main.setWidgetLeftRight(widget, left, layoutUnit, right, layoutUnit);
        } else {
            throw new IllegalArgumentException("Unrecognized layout constrain type: " + layoutConstrain);
        }
    }

    public void setLayoutDescriptor(JavaScriptObject properties) {
        this.layoutDescriptor = properties.cast();
        String unit = this.layoutDescriptor.getLayoutUnit();
        if (PIXEL_LAYOUT.equals(unit)) {
            this.layoutUnit = Unit.PX;
        } else if (PERCENTAGE_LAYOUT.equals(unit)) {
            this.layoutUnit = Unit.PCT;
        } else {
            throw new IllegalArgumentException("Layout unit unidentified");
        }
    }
}
