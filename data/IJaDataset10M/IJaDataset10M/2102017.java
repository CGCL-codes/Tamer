package no.ugland.utransprod.util.excel;

import java.util.List;
import java.util.Map;
import no.ugland.utransprod.ProTransException;
import no.ugland.utransprod.gui.handlers.CheckObject;

public interface ExcelManager {

    List<?> findByParams(ExcelReportSetting params) throws ProTransException;

    String getInfoButtom(ExcelReportSetting params) throws ProTransException;

    String getInfoTop(ExcelReportSetting params);

    Map<Object, Object> getReportDataMap(ExcelReportSetting params) throws ProTransException;

    CheckObject checkExcel(ExcelReportSetting params);
}
