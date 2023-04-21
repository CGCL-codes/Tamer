package org.pojosoft.ria.gwt.client.ui.datepicker;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import java.util.Date;

public class TodayPanel extends AbsolutePanel implements ClickListener {

    DatePickerBox datePicker;

    private DateFormatter dateFormatter = new DateFormatter(DateFormatter.DATE_FORMAT_MMDDYYYY);

    public TodayPanel(DatePickerBox datePicker) {
        this.datePicker = datePicker;
        init();
    }

    public void init() {
        Label todayLink = new Label("Today");
        todayLink.setStyleName("todayLink");
        todayLink.addClickListener(this);
        this.add(todayLink);
        this.setWidth(165 + "px");
        this.setHeight(16 + "px");
        this.setStyleName("todayPanel");
    }

    public DateFormatter getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void onClick(Widget sender) {
        this.datePicker.setText(this.dateFormatter.formatDate(new Date()));
        this.datePicker.hide();
    }
}
