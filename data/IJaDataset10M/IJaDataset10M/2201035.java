package net.pleso.framework.client.ui.controls.calendar;

import java.util.Date;
import net.pleso.framework.client.ui.custom.controls.data.CalendarDataControl;
import net.pleso.framework.client.util.DateUtil;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesKeyboardEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget with textbox for date and button to open calendar.
 * Using in {@link CalendarDataControl}
 * 
 * <h3>CSS Styles Rules</h3>
 * <ul>
 * <li>.pf-calendarPopup {widget itself}</li>
 * <li>.pf-calendar-icon-html {html with icon of calendar}</li>
 * </ul>
 * 
 * @author Scater
 *
 */
public class CalendarPopup extends Composite implements ClickListener, ChangeListener, SourcesKeyboardEvents, PopupListener {

    private FocusPanel icon;

    private HorizontalPanel holder = new HorizontalPanel();

    private PopupPanel calHolder;

    private CalendarWidget cal = new CalendarWidget();

    private boolean calVisible;

    private TextBox dateTextBox = new TextBox();

    private HTML iconImg = new HTML("&nbsp;");

    ;

    /**
     * Sets date 
     * @param date new date
     */
    public void setDate(Date date) {
        cal.setDate(date);
        dateTextBox.setText(DateUtil.formatDate(date));
    }

    /**
     * Gets text from date textbox
     * @return text
     */
    public String getText() {
        return dateTextBox.getText();
    }

    /**
     * Constructor
     */
    public CalendarPopup() {
        iconImg.setStyleName("pf-calendar-icon-html");
        icon = new FocusPanel(iconImg);
        icon.addClickListener(this);
        calVisible = false;
        holder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        holder.add(dateTextBox);
        holder.setCellWidth(this.dateTextBox, "100%");
        holder.add(icon);
        initWidget(holder);
        this.setStyleName("pf-calendarPopup");
    }

    public void onClick(Widget sender) {
        if (calHolder == null) {
            calHolder = new PopupPanel(true);
            calHolder.addPopupListener(this);
            cal = new CalendarWidget();
            cal.addChangeListener(this);
            calHolder.add(cal);
        }
        if (calVisible) {
            calHolder.hide();
            calVisible = false;
        } else {
            calHolder.setPopupPosition(dateTextBox.getAbsoluteLeft(), dateTextBox.getAbsoluteTop() + dateTextBox.getOffsetHeight());
            calHolder.show();
            calVisible = true;
        }
    }

    public void onChange(Widget sender) {
        calHolder.hide();
        calVisible = false;
        dateTextBox.setText(DateUtil.formatDate(cal.getDate()));
    }

    /**
     * Sets focus into date textbox
     * @param focused boolean focus parameter
     */
    public void setFocus(boolean focused) {
        this.dateTextBox.setFocus(focused);
    }

    public void addKeyboardListener(KeyboardListener listener) {
        this.dateTextBox.addKeyboardListener(listener);
    }

    public void removeKeyboardListener(KeyboardListener listener) {
        this.dateTextBox.removeKeyboardListener(listener);
    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        calVisible = false;
    }
}
