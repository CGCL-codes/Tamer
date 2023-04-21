package org.plazmaforge.framework.datawarehouse.convert.dataimport;

import org.plazmaforge.framework.core.exception.ApplicationException;
import org.plazmaforge.framework.datawarehouse.IDataSet;
import org.plazmaforge.framework.datawarehouse.ITransferService;
import org.plazmaforge.framework.datawarehouse.TransferInfo;
import org.plazmaforge.framework.datawarehouse.TransferResult;

public interface IDataSetImporter {

    ITransferService getTransferService();

    void setTransferService(ITransferService transferService);

    IDataSet getDataSet();

    void setDataSet(IDataSet dataSet);

    TransferInfo getTransferInfo();

    void setTransferInfo(TransferInfo transferInfo);

    TransferResult getTransferResult();

    void setTransferResult(TransferResult transferResult);

    void importDataSet(IImporter importer) throws ApplicationException;

    boolean isEnableClearTable();

    void setEnableClearTable(boolean enableClearTable);

    boolean isEnableAutoPrimaryKey();

    void setEnableAutoPrimaryKey(boolean enableAutoPrimaryKey);

    boolean isClearTable();

    void setClearTable(boolean clearTable);

    boolean isAutoPrimaryKey();

    void setAutoPrimaryKey(boolean autoPrimaryKey);
}
