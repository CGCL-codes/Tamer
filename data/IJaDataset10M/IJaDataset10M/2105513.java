package net.sourceforge.gedapi.view;

import java.util.ArrayList;
import java.util.List;

public class IndividualListView extends BaseView {

    public List<Individual> individuals;

    public IndividualListView() {
        super(ViewEnum.INDIVIDUAL_VIEW.view());
        individuals = new ArrayList<Individual>();
    }
}
