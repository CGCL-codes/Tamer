package de.icehorsetools.iceoffice.state.testtimetable;

import java.util.Collection;
import java.util.HashMap;
import org.ugat.wiser.exceptions.UnsupportedObjectException;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.state.AUnState;
import de.icehorsetools.dataAccess.objects.Testtimetable;

public class TesttimetableSt extends AUnState {

    private Object data = null;

    public void initialize() {
        this.setContainerComponentByPluginName("testtimetable");
    }

    public Object getData() {
        return this.getContainerHandler().getData();
    }

    public void setData(Object xData) {
        this.data = xData;
        if (this.data instanceof Testtimetable) {
            this.getContainerHandler().setData(this.data);
        } else {
            throw new UnsupportedObjectException((String) this.data);
        }
    }

    public void setElementData(Collection xExcludes, HashMap xFilter) {
    }

    public String getTitle() {
        return Lang.get(this.getClass(), "title");
    }
}
