package org.nakedobjects.example.ecs.view;

import org.nakedobjects.applib.value.Date;
import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.util.UnknownTypeException;
import org.nakedobjects.nos.client.dnd.CompositeViewBuilder;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.builder.AbstractBuilderDecorator;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Size;

public class CalendarLayout extends AbstractBuilderDecorator {

    public CalendarLayout(CompositeViewBuilder design) {
        super(design);
    }

    public Size getRequiredSize(View view) {
        int type = ((CalendarAxis) view.getViewAxis()).getType();
        switch(type) {
            case CalendarTemplate.MONTH:
                return new Size(80 * 7, 250);
            case CalendarTemplate.WEEK:
                return new Size(80 * 7, 160);
            default:
                throw new UnknownTypeException(type);
        }
    }

    public void layout(View view, Size maximumSize) {
        int type = ((CalendarAxis) view.getViewAxis()).getType();
        Size size = view.getSize();
        size.contract(view.getPadding());
        int width;
        int height;
        switch(type) {
            case CalendarTemplate.MONTH:
                width = size.getWidth() / 7;
                height = size.getHeight() / 5;
                break;
            case CalendarTemplate.WEEK:
                width = size.getWidth() / 7;
                height = size.getHeight();
                break;
            default:
                throw new UnknownTypeException(type);
        }
        View subviews[] = view.getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            View v = subviews[i];
            Date date = findDate(v);
            if (date == null) {
                continue;
            }
            int day = date.getDay();
            switch(type) {
                case CalendarTemplate.MONTH:
                    layoutDayWithinMonth(v, day, width, height);
                    break;
                case CalendarTemplate.WEEK:
                    layoutDayWithinWeek(v, day, width, height);
                    break;
                default:
                    throw new UnknownTypeException("" + type);
            }
        }
    }

    private void layoutDayWithinMonth(View v, int day, int width, int height) {
        int col = (day - 1) % 7;
        int row = (day - 1) / 7;
        int x = width * col + 20;
        int y = height * row + 5;
        v.layout(new Size(width - 19, height - 6));
        v.setLocation(new Location(x, y));
        v.setSize(new Size(width - 19, height - 6));
    }

    private void layoutDayWithinWeek(View v, int day, int width, int height) {
        int col = (day - 1) % 7;
        int x = width * col;
        v.setLocation(new Location(x, 30));
        v.setSize(new Size(width, height));
    }

    private Date findDate(View view) {
        Content c = view.getContent();
        Naked adapter = c.getNaked();
        NakedObjectSpecification spec = adapter.getSpecification();
        NakedObjectField[] fields = spec.getFields();
        Date date = null;
        for (int j = 0; j < fields.length; j++) {
            if (fields[j].getSpecification() == NakedObjectsContext.getReflector().loadSpecification(Date.class)) {
                date = (Date) fields[j].get((NakedObject) adapter).getObject();
                break;
            }
        }
        return date;
    }
}
