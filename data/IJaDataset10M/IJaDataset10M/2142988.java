package org.nakedobjects.nos.client.dnd.value;

import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.Naked;
import org.nakedobjects.utility.NotImplementedException;
import org.nakedobjects.nos.client.dnd.Canvas;
import org.nakedobjects.nos.client.dnd.Color;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.InternalDrag;
import org.nakedobjects.nos.client.dnd.ObjectContent;
import org.nakedobjects.nos.client.dnd.Size;
import org.nakedobjects.nos.client.dnd.Style;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.ViewSpecification;
import org.nakedobjects.nos.client.dnd.core.AbstractFieldSpecification;
import java.sql.Time;
import org.apache.log4j.Logger;

public class TimePeriodBarField extends AbstractField {

    public static class Specification extends AbstractFieldSpecification {

        public View createView(Content content, ViewAxis axis) {
            return new TimePeriodBarField(content, this, axis);
        }

        public String getName() {
            return "Period graph";
        }

        public boolean canDisplay(Naked object) {
            return object instanceof TimePeriod;
        }
    }

    private static final Logger LOG = Logger.getLogger(TimePeriodBarField.class);

    private int endTime;

    protected TimePeriodBarField(Content content, ViewSpecification specification, ViewAxis axis) {
        super(content, specification, axis);
    }

    public void drag(InternalDrag drag) {
        float x = drag.getLocation().getX() - 2;
        float max = getSize().getWidth() - 4;
        if ((x >= 0) && (x <= max)) {
            int time = (int) (x / max * 3600 * 24);
            endTime = time;
            initiateSave();
        }
    }

    protected void save() {
        Time end = getPeriod().getEnd();
        end.setValue(endTime);
        Time start = getPeriod().getStart();
        TimePeriod tp = new TimePeriod();
        tp.setValue(start, end);
        try {
            parseEntry(tp.title().toString());
        } catch (InvalidEntryException e) {
            throw new NotImplementedException();
        }
        LOG.debug("adjust time " + endTime + " " + getPeriod());
        markDamaged();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Color color = getState().isObjectIdentified() ? Toolkit.getColor("primary2") : Toolkit.getColor("secondary1");
        Size size = getSize();
        int width = size.getWidth();
        int height = size.getHeight();
        canvas.drawRectangle(0, 0, width - 1, height - 1, color);
        TimePeriod p = getPeriod();
        int max = width - 4;
        int start = (int) ((p.isEmpty() ? 0 : (p.getStart().longValue() * max)) / (3600 * 24)) + 2;
        int end = (int) ((p.isEmpty() ? max : (p.getEnd().longValue() * max)) / (3600 * 24)) + 2;
        canvas.drawSolidRectangle(start, 2, end - start, height - 5, Toolkit.getColor("primary3"));
        canvas.drawRectangle(start, 2, end - start, height - 5, color);
        canvas.drawText(p.title().toString(), start + 3, height - 5 - Toolkit.getText("normal").getDescent(), color, Toolkit.getText("normal"));
    }

    private TimePeriod getPeriod() {
        ObjectContent content = ((ObjectContent) getContent());
        TimePeriod period = (TimePeriod) content.getObject().getObject();
        return period;
    }

    public Size getRequiredSize() {
        Size size = super.getRequiredSize();
        size.extendWidth(304);
        return size;
    }
}
