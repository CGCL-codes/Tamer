package net.sf.fallfair.fairclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.sf.fallfair.CRUD.AbstractPersistent;
import net.sf.fallfair.context.FairContext;
import net.sf.fallfair.division.Division;
import net.sf.fallfair.utils.StringUtils;
import net.sf.fallfair.view.ExceptionStatusLabel;
import net.sf.fallfair.view.FairMaintenanceCommand;
import net.sf.fallfair.view.utils.DivisionComboBoxModel;
import net.sf.fallfair.view.utils.FairClassListModel;

public class FairClassSearchCommand implements FairMaintenanceCommand<FairClassMaintenance, FairClass> {

    private FairClassCRUD fairClassCRUD = new FairClassIBatisCRUD();

    @Override
    public boolean execute(FairClassMaintenance fairView, FairContext context) {
        List<FairClass> classes = new ArrayList<FairClass>();
        Division division = getSelectedDivision(fairView);
        if (StringUtils.isNotEmpty(fairView.getSearchTextField().getText()) || null != division) {
            try {
                classes = fairClassCRUD.findByDescription(division, fairView.getSearchTextField().getText(), context);
            } catch (SQLException ex) {
                fairView.updateStatus(new ExceptionStatusLabel(ex));
                return false;
            }
        }
        fairView.getSearchResultsList().setModel(new FairClassListModel(classes));
        fairView.getSearchResultsList().setSelectedIndex(0);
        return true;
    }

    private Division getSelectedDivision(FairClassMaintenance fairView) {
        int index = fairView.getDivisionComboBox().getSelectedIndex();
        Division division = ((DivisionComboBoxModel) fairView.getDivisionComboBox().getModel()).getData().get(index);
        if (AbstractPersistent.DEFAULT_ID != division.getId()) {
            return division;
        }
        return null;
    }
}
