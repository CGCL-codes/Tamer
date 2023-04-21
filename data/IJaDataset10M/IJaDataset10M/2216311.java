package no.ugland.utransprod.dao;

import java.util.List;
import no.ugland.utransprod.model.NotInvoicedV;
import no.ugland.utransprod.util.excel.ExcelReportSetting;

/**
 * Interface for DAO mot view NOT_INVOICED_V
 * 
 * @author atle.brekka
 * 
 */
public interface NotInvoicedVDAO extends DAO<NotInvoicedV> {

    /**
	 * Finner alle for gitte paramtetre
	 * 
	 * @param params
	 * @return ikke fakturerte ordre
	 */
    List<NotInvoicedV> findByParams(ExcelReportSetting params);
}
