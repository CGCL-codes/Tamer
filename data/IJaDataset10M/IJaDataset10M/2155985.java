package net.sf.fallfair.judge;

import java.sql.SQLException;
import java.util.List;
import net.sf.fallfair.view.*;
import net.sf.fallfair.context.FairContext;
import net.sf.fallfair.division.Division;
import net.sf.fallfair.division.DivisionCRUD;
import net.sf.fallfair.division.DivisionIBatisCRUD;
import net.sf.fallfair.province.ProvinceCRUD;
import net.sf.fallfair.province.ProvinceIBatisCRUD;
import net.sf.fallfair.view.utils.DivisionListModel;
import net.sf.fallfair.view.utils.ProvinceComboBoxModel;

public class JudgeInitCommand implements FairMaintenanceCommand<JudgeMaintenance, Judge> {

    private ProvinceCRUD provinceCRUD = new ProvinceIBatisCRUD();

    @Override
    public boolean execute(JudgeMaintenance fairView, FairContext context) {
        try {
            fairView.getProvinceComboBox().setModel(new ProvinceComboBoxModel(this.provinceCRUD.getAll(context)));
            fairView.getProvinceComboBox().setSelectedIndex(0);
        } catch (SQLException ex) {
            fairView.updateStatus(new ExceptionStatusLabel(ex));
            return false;
        }
        fairView.render();
        return true;
    }
}
