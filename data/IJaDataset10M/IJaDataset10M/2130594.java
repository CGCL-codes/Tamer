package com.tredart.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tredart.gwt.client.result.Result;

/**
 * Creates a transaction.
 * 
 * @author fdegrazia
 * @author gnicoll
 */
public interface TransactionBusinessFunction extends RemoteService {

    /**
	 * Create a new transaction.
	 * 
	 * @param isBuy
	 *            the side of the transaction (buy or sell)
	 * @param betSize
	 *            the side of the bet
	 * @param productName
	 *            the name of product to trade
	 * @param initialPrice
	 *            the initial price of the product
	 * @param stopLoss
	 *            the stop loss
	 * @param executionTimeStamp
	 *            is when the trade has been executed
	 * 
	 * @return the success or failure of the creation
	 */
    Result create(boolean isBuy, int betSize, String productName, double initialPrice, double stopLoss, String executionTimeStamp);
}
