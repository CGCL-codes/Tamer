package com.patientis.data.interfaces;

import java.util.List;
import java.util.ArrayList;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.common.BaseModel;
import com.patientis.model.interfaces.ITransactionModel;
import com.patientis.model.interfaces.InterfaceMessageModel;
import com.patientis.model.interfaces.InterfaceProcessorModel;
import com.patientis.model.interfaces.InterfaceTransactionModel;
import com.patientis.model.inventory.ItemModel;
import com.patientis.model.reference.InterfaceTransactionStateReference;
import com.patientis.data.common.BaseData;
import com.patientis.data.common.CodeUtil;
import com.patientis.data.common.IDataMethod;
import com.patientis.data.common.ISParameter;
import com.patientis.data.common.ISParameterQuery;
import com.patientis.ejb.common.ChainStore;
import com.patientis.ejb.common.IChainStore;
import com.patientis.ejb.common.StoreCommand;

/**
 * ReportData provides CRUD functionality for Record objects.
 *
 * Design Patterns: <a href="/functionality/rm/1000065.html">Queries</a>
 * <br/>
 */
public class InterfacesData extends InterfacesBaseData {

    /**
	 * Return all interface processors.  TODO this will ultimately be by node/instance
	 * 
	 * @param call service call
	 * @return list of processors
	 * @throws Exception
	 */
    public static List<InterfaceProcessorModel> getProcessors(ServiceCall call) throws Exception {
        InterfaceProcessorModel criteria = new InterfaceProcessorModel();
        List<BaseModel> list = BaseData.getReference().getBaseSearchResults(criteria, call);
        List<InterfaceProcessorModel> processors = new ArrayList<InterfaceProcessorModel>(list.size());
        BaseModel.toModelList(list, processors);
        return processors;
    }

    /**
	 * Get all transactions of the specified type that are unprocessed, 
	 * change to a queued state and return.
	 * 
	 * @param transactionTypeRefId type
	 * @param call service call
	 * @return non null list of transactions
	 * @throws Exception
	 */
    public static void queueTransaction(ITransactionModel itm, ServiceCall call) throws Exception {
        IChainStore chain = new ChainStore();
        itm.setStateRef(new DisplayModel(InterfaceTransactionStateReference.QUEUED.getRefId()));
        itm.setUpdateDt(DateTimeModel.getNow());
        chain.add(new StoreCommand(itm, call));
        chain.execute();
    }

    /**
	 * Execute a single update statement to error out the transaction
	 * 
	 * @param transaction transaction to error
	 * @param call service call
	 * @return nbr rows updated
	 * @throws Exception
	 */
    public static void setTransactionInError(ITransactionModel transaction, ServiceCall call) throws Exception {
        IChainStore chain = new ChainStore();
        transaction.setStateRef(new DisplayModel(InterfaceTransactionStateReference.ERROR.getRefId()));
        chain.add(new StoreCommand(transaction, call));
        chain.execute();
    }

    /**
	 * Get the interface processors
	 * 
	 * @return
	 * @throws Exception
	 */
    public static List<InterfaceProcessorModel> getInterfaceProcessors(final ServiceCall call) throws Exception {
        String hql = "from InterfaceProcessorModel";
        hql += " where processCustomControllerRef.id > 0";
        ISParameterQuery qry = new ISParameterQuery();
        qry.setHql(hql);
        List list = query(qry, call);
        List<InterfaceProcessorModel> processors = new ArrayList<InterfaceProcessorModel>();
        BaseModel.toAnyList(list, processors);
        return processors;
    }

    /**
	 * 
	 * @param stateRefid
	 * @param transactionTypeRefId
	 * @param maxNbrResults
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public static List<InterfaceTransactionModel> getTransactionsByStateType(final long stateRefid, final long transactionTypeRefId, final int maxNbrResults, final ServiceCall call) throws Exception {
        IDataMethod method = new IDataMethod() {

            public Object execute() throws Exception {
                String hql = "from InterfaceTransactionModel where stateRef.id = :stateRefid " + " and transactionTypeRef.id = :transactionTypeRefId " + " and activeInd = 1 ";
                ISParameterQuery query = new ISParameterQuery();
                query.setHql(hql);
                query.setParameters(ISParameter.createList(new ISParameter("stateRefid", stateRefid), new ISParameter("transactionTypeRefId", transactionTypeRefId)));
                call.setMaxNbrQueryResults(maxNbrResults);
                List list = BaseData.query(query, call);
                List<InterfaceTransactionModel> transactions = new ArrayList<InterfaceTransactionModel>(list.size());
                for (Object m : list) {
                    transactions.add((InterfaceTransactionModel) m);
                }
                return transactions;
            }
        };
        return (List<InterfaceTransactionModel>) call(method, call);
    }

    /**
	 * 
	 * @param stateRefid
	 * @param transactionTypeRefId
	 * @param maxNbrResults
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public static List<InterfaceMessageModel> getMessagesByStateType(final long stateRefid, final long transactionTypeRefId, final int outboundInd, final int maxNbrResults, final ServiceCall call) throws Exception {
        IDataMethod method = new IDataMethod() {

            public Object execute() throws Exception {
                String hql = "from InterfaceMessageModel where stateRef.id = :stateRefid " + " and transactionTypeRef.id = :transactionTypeRefId " + " and outboundInd = :outboundInd " + " and activeInd = 1 ";
                ISParameterQuery query = new ISParameterQuery();
                query.setHql(hql);
                query.setParameters(ISParameter.createList(new ISParameter("stateRefid", stateRefid), new ISParameter("transactionTypeRefId", transactionTypeRefId), new ISParameter("outboundInd", outboundInd)));
                call.setMaxNbrQueryResults(maxNbrResults);
                List list = BaseData.query(query, call);
                List<InterfaceMessageModel> transactions = new ArrayList<InterfaceMessageModel>(list.size());
                for (Object m : list) {
                    transactions.add((InterfaceMessageModel) m);
                }
                return transactions;
            }
        };
        return (List<InterfaceMessageModel>) call(method, call);
    }
}
