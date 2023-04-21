package com.sebulli.fakturama.calculate;

import com.sebulli.fakturama.data.DataSetVoucher;

/**
 * Stores one VatSummarySet object and provides some methods e.g. to add an
 * UniDataSet document
 * 
 * @author Gerd Bartelt
 */
public class VoucherSummarySetManager {

    VatSummarySet voucherSummarySet;

    /**
	 * Constructor Creates a new voucherSummarySet
	 */
    public VoucherSummarySetManager() {
        voucherSummarySet = new VatSummarySet();
    }

    /**
	 * Add an voucher to the voucherSummarySet
	 * 
	 * @param document
	 *            Document to add
	 * @param useCategory
	 *            If true, the category is also used for the vat summary as a
	 *            description
	 */
    public void add(DataSetVoucher voucher, boolean useCategory) {
        VoucherSummary summary = new VoucherSummary();
        summary.calculate(voucherSummarySet, voucher.getItems(), useCategory, voucher.getDoubleValueByKey("paid"), voucher.getDoubleValueByKey("total"), voucher.getBooleanValueByKey("discounted"));
    }

    /**
	 * Add an voucher to the voucherSummarySet
	 * 
	 * @param document
	 *            Document to add
	 * @param useCategory
	 *            If true, the category is also used for the vat summary as a
	 *            description
	 * @itemNr index of one item
	 */
    public void add(DataSetVoucher voucher, boolean useCategory, int itemNr) {
        VoucherSummary summary = new VoucherSummary();
        summary.calculate(voucherSummarySet, voucher.getItems(itemNr), useCategory, voucher.getDoubleValueByKey("paid"), voucher.getDoubleValueByKey("total"), voucher.getBooleanValueByKey("discounted"));
    }

    /**
	 * Getter for the voucherSummarySet
	 * 
	 * @return The voucherSummarySet
	 */
    public VatSummarySet getVoucherSummaryItems() {
        return voucherSummarySet;
    }

    /**
	 * Get the size of the
	 * 
	 * @return The size of the voucherSummarySet
	 */
    public int size() {
        return voucherSummarySet.size();
    }

    /**
	 * Get the index of a voucherSummaryItem
	 * 
	 * @param voucherSummaryItem
	 *            Item to search for
	 * @return Index of the item or -1, of none was found
	 */
    public int getIndex(VatSummaryItem voucherSummaryItem) {
        return voucherSummarySet.getIndex(voucherSummaryItem);
    }
}
