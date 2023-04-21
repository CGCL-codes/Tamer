package beans;

import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.component.menubar.MenuItem;
import entities.Event;
import gui.CellValue;
import java.util.ArrayList;
import javax.faces.event.ActionEvent;

/**
 *
 * @author IoanaC
 */
public class MondayBean extends DayBean {

    private boolean selected = false;

    private final String day = "MONDAY";

    private ArrayList<String> hoursEvents = new ArrayList<String>();

    ArrayList<CellValue> datatableValues = new ArrayList<CellValue>();

    public MondayBean() {
        super.setDefaultHours(datatableValues);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<String> getHoursEvents() {
        return hoursEvents;
    }

    public void setHoursEvents(ArrayList<String> hoursEvents) {
        this.hoursEvents = hoursEvents;
    }

    public String getDay() {
        return day;
    }

    @Override
    public ArrayList<CellValue> getDatatableValues() {
        return this.datatableValues;
    }

    @Override
    public void rowSelectionListener(RowSelectorEvent event) {
    }
}
