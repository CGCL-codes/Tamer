package org.apache.myfaces.custom.skin.renderkit.custom;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.custom.skin.AdapterSkinRenderer;
import org.apache.myfaces.custom.skin.SkinConstants;
import org.apache.myfaces.trinidad.context.SkinRenderingContext;

public class HtmlInputCalendarSkinRenderer extends AdapterSkinRenderer {

    public HtmlInputCalendarSkinRenderer() {
        super("t", "inputCalendar");
    }

    @Override
    protected void _addStyleClassesToComponent(FacesContext context, UIComponent component, SkinRenderingContext arc) throws IOException {
        _addStyleDisabledReadOnlyRequired(context, component, arc);
        String baseStyleClass = this.getBaseStyleName(component);
        String displayValueOnlyStyleClass = baseStyleClass + "::displayValueOnly";
        _renderStyleClass(component, context, arc, displayValueOnlyStyleClass, "displayValueOnlyStyleClass");
        String currentDayCellStyleClass = baseStyleClass + "::currentDayCell";
        String dayCellStyleClass = baseStyleClass + "::dayCell";
        String monthYearRowStyleClass = baseStyleClass + "::monthYearRow";
        String popupButtonStyleClass = baseStyleClass + "::popupButton";
        String weekRowStyleClass = baseStyleClass + "::weekRow";
        _renderStyleClass(component, context, arc, currentDayCellStyleClass, "currentDayCellClass");
        _renderStyleClass(component, context, arc, dayCellStyleClass, "dayCellClass");
        _renderStyleClass(component, context, arc, monthYearRowStyleClass, "monthYearRowClass");
        _renderStyleClass(component, context, arc, popupButtonStyleClass, "popupButtonStyleClass");
        _renderStyleClass(component, context, arc, weekRowStyleClass, "weekRowClass");
    }
}
