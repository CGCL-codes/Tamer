package com.datas.form.modul.product.maloprodaja;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import com.datas.bean.common.ReportInfo;
import com.datas.bean.enums.PrintType;
import com.datas.bean.model.maloprodaja.TrgovackaKnjiga;
import com.datas.form.modul.AvailableReportsListener;
import com.datas.form.modul.common.SimpleModul;
import com.datas.form.util.DataUtility;

/**
 * @author kimi
 * 
 */
@SuppressWarnings("serial")
public class TrgovackaKnjigaModul extends SimpleModul implements AvailableReportsListener {

    public TrgovackaKnjigaModul() {
        init("s_trgovacka_knjiga");
    }

    @Override
    protected void initSimpleModul() {
    }

    @Override
    protected void setBehaviorSimpleModul() {
    }

    @Override
    protected void setLayoutSimpleModul() {
    }

    @Override
    protected void setLookAndFeelSimpleModul() {
    }

    @Override
    protected void focusFirstField() {
        fields.get("00020").getAdvancedTextField().requestFocusInWindow();
    }

    /** data */
    @Override
    protected List browseData() {
        TrgovackaKnjiga trgovackaKnjiga = (TrgovackaKnjiga) DataUtility.browse(TrgovackaKnjiga.class.getName(), fields);
        return getServiceContainer().getMaloprodajaService().selectList(trgovackaKnjiga);
    }

    @Override
    protected Object insertData() {
        TrgovackaKnjiga trgovackaKnjiga = (TrgovackaKnjiga) DataUtility.insert(TrgovackaKnjiga.class.getName(), fields);
        getServiceContainer().getMaloprodajaService().insert(trgovackaKnjiga);
        return trgovackaKnjiga;
    }

    @Override
    protected void updateData(Object object) {
        TrgovackaKnjiga trgovackaKnjiga = (TrgovackaKnjiga) object;
        DataUtility.update(trgovackaKnjiga, fields);
        getServiceContainer().getMaloprodajaService().update(trgovackaKnjiga);
    }

    @Override
    protected void deleteData(Object object) {
        TrgovackaKnjiga trgovackaKnjiga = (TrgovackaKnjiga) object;
        getServiceContainer().getMaloprodajaService().delete(trgovackaKnjiga);
    }

    /** available reports */
    public SortedMap<String, ReportInfo> getAvailableReports() {
        ReportInfo reportInfo = null;
        SortedMap<String, ReportInfo> reports = new TreeMap<String, ReportInfo>();
        reportInfo = new ReportInfo("maloprodaja/trgovackaKnjigaModul01", getServiceContainer().getLocalizationService().getTranslation(getClass(), "LASER_PRINT_01"), PrintType.LASER_PRINT);
        reports.put("01", reportInfo);
        return reports;
    }
}
