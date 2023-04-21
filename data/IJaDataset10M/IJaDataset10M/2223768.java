package ee.fctwister.cpanel.pages.fixtures;

import org.apache.tapestry.form.IPropertySelectionModel;
import ee.fctwister.cpanel.DTO.PitchConditionDTO;
import java.util.*;

public class PitchConditionSelectionModel implements IPropertySelectionModel {

    private List<PitchConditionDTO> venues;

    public PitchConditionSelectionModel(List<PitchConditionDTO> venues) {
        this.venues = venues;
    }

    public int getOptionCount() {
        return venues.size();
    }

    public Object getOption(int index) {
        return venues.get(index);
    }

    public String getLabel(int index) {
        return ((PitchConditionDTO) venues.get(index)).getCondition();
    }

    public String getValue(int index) {
        return Integer.toString(index);
    }

    public Object translateValue(String value) {
        return getOption(Integer.parseInt(value));
    }
}
