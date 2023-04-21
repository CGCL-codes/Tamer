package org.enjavers.jethro.dspi.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alessandro Lombardi
 */
public class OracleQLAdapter extends QLAdapterSPI {

    private static OracleQLAdapter _SINGLETON_ADAPTER = null;

    public static OracleQLAdapter getAdapter() {
        if (_SINGLETON_ADAPTER == null) _SINGLETON_ADAPTER = new OracleQLAdapter();
        return _SINGLETON_ADAPTER;
    }

    public OracleQLAdapter() {
        super();
    }

    /**
	 * @see org.enjavers.jethro.dspi.adapters.QLAdapterSPI#formatDate(java.util.Date)
	 */
    protected String formatDate(Date i_date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "TO_DATE('" + sdFormat.format(i_date) + "', 'DD/MM/YYYY')";
    }
}
