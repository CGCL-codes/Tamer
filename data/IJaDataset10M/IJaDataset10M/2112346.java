package com.ivis.xprocess.ui.util.dates;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import com.ivis.xprocess.ui.diagram.HyperlinkLabel;
import com.ivis.xprocess.ui.properties.DateMessages;
import com.ivis.xprocess.util.Day;

public class DateLabelFigure extends RectangleFigure {

    private static final int BLOB_SIZE = 7;

    public static final int LINK_OFFSET_X = 4;

    public static final int LINK_OFFSET_Y = 5;

    private final Day myDay;

    private final String myTitle;

    private final Figure myBlob;

    private final Label myDayLabel;

    public DateLabelFigure(final String title, final Day day, boolean topDown, boolean editable) {
        myDay = day;
        myTitle = title;
        setOutline(false);
        setFill(false);
        FlowLayout mainLayout = new FlowLayout(false);
        mainLayout.setMinorSpacing(0);
        setLayoutManager(mainLayout);
        Figure caption = new Figure();
        FlowLayout captionLayout = new FlowLayout(true);
        captionLayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
        captionLayout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
        caption.setLayoutManager(captionLayout);
        myBlob = new Ellipse();
        myBlob.setPreferredSize(BLOB_SIZE, BLOB_SIZE);
        caption.add(myBlob);
        final IFigure titleLabel;
        if (editable) {
            titleLabel = new HyperlinkLabel(myTitle);
            myDayLabel = new HyperlinkLabel((myDay != null) ? DateType.toString(myDay) : DateMessages.target_end_not_set);
        } else {
            titleLabel = new Label(myTitle);
            myDayLabel = new Label((myDay != null) ? DateType.toString(myDay) : "");
        }
        caption.add(myDayLabel);
        if (topDown) {
            add(titleLabel);
            add(caption);
        } else {
            add(caption);
            add(titleLabel);
        }
        setForegroundColor(ColorConstants.black);
    }

    public IFigure getLinkAttachFigure() {
        return myBlob;
    }

    public void setBlobColor(Color color) {
        myBlob.setBackgroundColor(color);
        myBlob.setForegroundColor(color);
    }

    public String getDisplayDate() {
        return myDayLabel.getText();
    }

    public static Point getLinkAttachPoint(Rectangle bounds, boolean topDown) {
        if (topDown) {
            return bounds.getBottomLeft().getTranslated(LINK_OFFSET_X, -LINK_OFFSET_Y);
        }
        return bounds.getTopLeft().getTranslated(LINK_OFFSET_X, LINK_OFFSET_Y);
    }
}
