package cat.jm.cru.database.beans;

import java.util.ArrayList;
import java.util.List;

public class ForeignsList {

    private List<ForeignKey> foreigns = new ArrayList<ForeignKey>();

    public ForeignsList() {
    }

    public ForeignsList(List<ForeignKey> foreigns) {
        this.foreigns = foreigns;
    }

    public List<ForeignKey> getForeigns() {
        return foreigns;
    }

    public void setForeigns(List<ForeignKey> foreigns) {
        this.foreigns = foreigns;
    }
}
