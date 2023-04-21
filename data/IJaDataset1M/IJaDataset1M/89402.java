package org.shestkoff.nimium.integration.tws;

import gnu.trove.TLongObjectHashMap;
import org.jetbrains.annotations.NotNull;
import org.shestkoff.nimium.integration.tws.client.ContractDetails;
import org.shestkoff.nimium.integration.tws.client.EClientSocket;
import org.shestkoff.nimium.common.security.SecurityType;
import org.shestkoff.nimium.service.ContractService;
import org.shestkoff.nimium.service.contract.ContractFilter;
import org.shestkoff.nimium.service.contract.ContractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Numium project
 * Time: 24.11.2009 18:18:42
 *
 * @author Vaily Shestkov
 */
class TWSContractService implements ContractService {

    private static final Logger logger = LoggerFactory.getLogger(TWSContractService.class);

    private final EClientSocket twsClient;

    private final TLongObjectHashMap<ContractListener> contractDetailsMap;

    private volatile int uniqueRequestId = 0;

    public TWSContractService(@NotNull EClientSocket twsClient) {
        this.twsClient = twsClient;
        this.contractDetailsMap = new TLongObjectHashMap<ContractListener>();
    }

    @Override
    public void getContract(ContractFilter contractFilter, ContractListener contractListener) {
        final org.shestkoff.nimium.integration.tws.client.Contract twsContract = prepareContractObject(contractFilter);
        contractDetailsMap.put(uniqueRequestId, contractListener);
        twsClient.reqContractDetails(uniqueRequestId++, twsContract);
    }

    private org.shestkoff.nimium.integration.tws.client.Contract prepareContractObject(ContractFilter contractFilter) {
        final org.shestkoff.nimium.integration.tws.client.Contract twsContract = new org.shestkoff.nimium.integration.tws.client.Contract();
        twsContract.m_symbol = contractFilter.getContractExchangeCode();
        twsContract.m_secType = ContractHelper.convertContractType(contractFilter.getContractType());
        if (SecurityType.FUTURES.equals(contractFilter.getContractType())) {
            twsContract.m_expiry = contractFilter.getExpirationDate().toString("yyyyMMdd");
        }
        final String contractExchange = contractFilter.getContractExchange();
        if (contractExchange != null) {
            twsContract.m_exchange = contractExchange;
        }
        final String currency = contractFilter.getCurrency();
        if (currency != null) {
            twsContract.m_currency = currency;
        }
        return twsContract;
    }

    void contractDetails(int reqId, ContractDetails contractDetails) {
        final ContractListener contractListener = contractDetailsMap.get(reqId);
        logger.debug("Notifying ContractListener about security");
        contractListener.contract(ContractHelper.convertContractDetails(contractDetails));
    }

    void contractDetailsEnd(int reqId) {
        final ContractListener contractListener = contractDetailsMap.get(reqId);
        contractListener.finished();
        contractDetailsMap.remove(reqId);
    }
}
