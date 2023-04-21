package org.slasoi.adhoc.workload.input;

import org.json.JSONObject;
import org.slasoi.ord.workloadgenerator.common.json.JSONFields;
import org.slasoi.ord.workloadgenerator.common.json.RandomStandardFactor;

/**
 * Read Delay JSON object to Delay object
 * @author ziga
 *
 */
public class JSONRandomStandardFactor {

    JSONObject obj;

    public JSONRandomStandardFactor(JSONObject obj) {
        super();
        this.obj = obj;
    }

    public JSONObject getObj() {
        return obj;
    }

    public int getWaitForCustomer() {
        return obj.getInt(JSONFields.waitForCustomer);
    }

    public int getScan() {
        return obj.getInt(JSONFields.scan);
    }

    public int getCaptureManually() {
        return obj.getInt(JSONFields.captureManually);
    }

    public int getUpdateRunningTotal() {
        return obj.getInt(JSONFields.updateRunningTotal);
    }

    public int getGiveCard() {
        return obj.getInt(JSONFields.giveCard);
    }

    public int getManualPayment() {
        return obj.getInt(JSONFields.manualPayment);
    }

    public int getWaitToProcess() {
        return obj.getInt(JSONFields.waitToProcess);
    }

    /**
	 * 
	 * @return
	 * Delay
	 */
    public RandomStandardFactor getRandomStandardFactor() {
        RandomStandardFactor r = new RandomStandardFactor();
        r.waitForCustomer = getWaitForCustomer();
        r.scan = getScan();
        r.captureManually = getCaptureManually();
        r.updateRunningTotal = getUpdateRunningTotal();
        r.giveCard = getGiveCard();
        r.manualPayment = getManualPayment();
        r.waitToProcess = getWaitToProcess();
        return r;
    }
}
