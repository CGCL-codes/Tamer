package org.ln.millesimus.vo.base;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.ln.dataset.vo.BaseVo;
import org.ln.millesimus.vo.InvoiceFamily;
import org.ln.millesimus.vo.InvoiceItem;
import org.ln.millesimus.vo.Work;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Base domain model class InvoiceBase.
 * @see org.ln.dataset.vo.BaseVo
 * @author Luca Noale
 */
@Root
public class InvoiceBase extends BaseVo {

    private static final long serialVersionUID = 1L;

    @Element
    protected Date itemDate;

    @Element
    protected Integer number;

    protected Long workId;

    protected Work work;

    @Element
    protected BigDecimal cost;

    protected Set<InvoiceFamily> invoiceFamilySet = new HashSet<InvoiceFamily>();

    protected Set<InvoiceItem> invoiceItemSet = new HashSet<InvoiceItem>();

    public InvoiceBase() {
        super();
    }

    public InvoiceBase(Long id) {
        super(id);
    }

    /**
     * @return the itemDate
     */
    public Date getItemDate() {
        return itemDate;
    }

    /**
     * @param itemDate the itemDate to set
     */
    public void setItemDate(Date itemDate) {
        this.itemDate = itemDate;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return the workId
     */
    public Long getWorkId() {
        return workId;
    }

    /**
     * @param workId the workId to set
     */
    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    /**
     * @return the work
     */
    public Work getWork() {
        return work;
    }

    /**
     * @param work the work to set
     */
    public void setWork(Work work) {
        this.work = work;
    }

    /**
     * @return the invoiceFamilySet
     */
    public Set<InvoiceFamily> getInvoiceFamilySet() {
        return invoiceFamilySet;
    }

    /**
     * @param invoiceFamilySet the invoiceFamilySet to set
     */
    public void setInvoiceFamilySet(Set<InvoiceFamily> invoiceFamilySet) {
        this.invoiceFamilySet = invoiceFamilySet;
    }

    /**
     * @return the invoiceFamilyList
     */
    public List<InvoiceFamily> getInvoiceFamilyList() {
        return new ArrayList<InvoiceFamily>(invoiceFamilySet);
    }

    /**
     * @param invoiceFamilyList the invoiceFamilyList to set
     */
    public void setInvoiceFamilyList(List<InvoiceFamily> invoiceFamilyList) {
        this.invoiceFamilySet = new HashSet<InvoiceFamily>(invoiceFamilyList);
    }

    /**
     * @return the invoiceItemSet
     */
    public Set<InvoiceItem> getInvoiceItemSet() {
        return invoiceItemSet;
    }

    /**
     * @param invoiceItemSet the invoiceItemSet to set
     */
    public void setInvoiceItemSet(Set<InvoiceItem> invoiceItemSet) {
        this.invoiceItemSet = invoiceItemSet;
    }

    /**
     * @return the invoiceItemList
     */
    public List<InvoiceItem> getInvoiceItemList() {
        ArrayList<InvoiceItem> list = new ArrayList<InvoiceItem>(invoiceItemSet);
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    /**
     * @param invoiceItemList the invoiceItemList to set
     */
    public void setInvoiceItemList(List<InvoiceItem> invoiceItemList) {
        this.invoiceItemSet = new HashSet<InvoiceItem>(invoiceItemList);
    }

    public int compareTo(BaseVo other) {
        return 1;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
	 * @return the cost
	 */
    public BigDecimal getCost() {
        return cost;
    }

    /**
	 * @param cost the cost to set
	 */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
