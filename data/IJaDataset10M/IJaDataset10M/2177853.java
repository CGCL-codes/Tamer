package com.fisoft.phucsinh.phucsinhsrv.entity;

import com.fisoft.phucsinh.phucsinhsrv.exception.ERPIllegalArgumentException;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author me
 */
@Entity
@Table(name = "si_adv_netting_detail", schema = "")
@NamedQueries({ @NamedQuery(name = "SiAdvNettingDetail.findAll", query = "SELECT s FROM SiAdvNettingDetail s"), @NamedQuery(name = "SiAdvNettingDetail.findByDetailID", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.detailID = :detailID"), @NamedQuery(name = "SiAdvNettingDetail.findByDetailSeq", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.detailSeq = :detailSeq"), @NamedQuery(name = "SiAdvNettingDetail.findByDescription", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.description = :description"), @NamedQuery(name = "SiAdvNettingDetail.findByNettingAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.nettingAmt = :nettingAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByNettingTaxAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.nettingTaxAmt = :nettingTaxAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByNettingTotalAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.nettingTotalAmt = :nettingTotalAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByDiscountAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.discountAmt = :discountAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByRemainAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.remainAmt = :remainAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByRemainTaxAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.remainTaxAmt = :remainTaxAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByRemainTotalAmt", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.remainTotalAmt = :remainTotalAmt"), @NamedQuery(name = "SiAdvNettingDetail.findByActiveStatus", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.activeStatus = :activeStatus"), @NamedQuery(name = "SiAdvNettingDetail.findByVersion", query = "SELECT s FROM SiAdvNettingDetail s WHERE s.version = :version") })
public class SiAdvNettingDetail implements Serializable, ICommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DetailID", nullable = false)
    private Integer detailID;

    @Basic(optional = false)
    @Column(name = "DetailSeq", nullable = false)
    private int detailSeq;

    @Column(name = "Description", length = 200)
    private String description;

    @Column(name = "NettingAmt", precision = 22)
    private Double nettingAmt;

    @Column(name = "NettingTaxAmt", precision = 22)
    private Double nettingTaxAmt;

    @Basic(optional = false)
    @Column(name = "NettingTotalAmt", nullable = false)
    private double nettingTotalAmt;

    @Column(name = "DiscountAmt", precision = 22)
    private Double discountAmt;

    @Column(name = "OldDueDate")
    @Temporal(TemporalType.DATE)
    private Date oldDueDate;

    @Column(name = "OldAmountToPaid", precision = 22)
    private Double oldAmountToPaid;

    @Column(name = "RemainAmt", precision = 22)
    private Double remainAmt;

    @Column(name = "RemainTaxAmt", precision = 22)
    private Double remainTaxAmt;

    @Column(name = "RemainTotalAmt", precision = 22)
    private Double remainTotalAmt;

    @Basic(optional = false)
    @Column(name = "ActiveStatus", nullable = false)
    private Integer activeStatus = EntityStatus.ACTIVE.getValue();

    @Version
    @Column(name = "Version", nullable = false)
    private int version;

    @JoinColumn(name = "NettingID", referencedColumnName = "NettingID", nullable = false)
    @ManyToOne(optional = false)
    private SiAdvNetting nettingID;

    @JoinColumn(name = "InvoiceID", referencedColumnName = "InvoiceID", nullable = false)
    @ManyToOne(optional = false)
    private SiSaleInvoice invoiceID;

    @JoinColumn(name = "InvoiceCurrency", referencedColumnName = "CcyCode")
    @ManyToOne
    private AcCurrency invoiceCurrency;

    @Column(name = "AppliedAmt", precision = 22)
    private Double appliedAmt;

    @Column(name = "ExchangeRate", precision = 12)
    private Double exchangeRate;

    @Column(name = "ProfLossAmt", precision = 22)
    private Double profLossAmt;

    public SiAdvNettingDetail() {
    }

    public SiAdvNettingDetail(int detailSeq, double nettingTotalAmt, int activeStatus) {
        this.detailSeq = detailSeq;
        this.nettingTotalAmt = nettingTotalAmt;
        this.activeStatus = activeStatus;
    }

    public Object getID() {
        return detailID;
    }

    public void setID(Object detailID) {
        this.detailID = (Integer) detailID;
    }

    public int getDetailSeq() {
        return detailSeq;
    }

    public void setDetailSeq(int detailSeq) {
        this.detailSeq = detailSeq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getNettingAmt() {
        return nettingAmt;
    }

    public void setNettingAmt(Double nettingAmt) {
        this.nettingAmt = nettingAmt;
    }

    public Double getNettingTaxAmt() {
        return nettingTaxAmt;
    }

    public void setNettingTaxAmt(Double nettingTaxAmt) {
        this.nettingTaxAmt = nettingTaxAmt;
    }

    public double getNettingTotalAmt() {
        return nettingTotalAmt;
    }

    public void setNettingTotalAmt(double nettingTotalAmt) {
        this.nettingTotalAmt = nettingTotalAmt;
    }

    public Double getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(Double discountAmt) {
        this.discountAmt = discountAmt;
    }

    public Double getOldAmountToPaid() {
        return oldAmountToPaid;
    }

    public void setOldAmountToPaid(Double oldAmountToPaid) {
        this.oldAmountToPaid = oldAmountToPaid;
    }

    public Date getOldDueDate() {
        return oldDueDate;
    }

    public void setOldDueDate(Date oldDueDate) {
        this.oldDueDate = oldDueDate;
    }

    public Double getRemainAmt() {
        return remainAmt;
    }

    public void setRemainAmt(Double remainAmt) {
        this.remainAmt = remainAmt;
    }

    public Double getRemainTaxAmt() {
        return remainTaxAmt;
    }

    public void setRemainTaxAmt(Double remainTaxAmt) {
        this.remainTaxAmt = remainTaxAmt;
    }

    public Double getRemainTotalAmt() {
        return remainTotalAmt;
    }

    public void setRemainTotalAmt(Double remainTotalAmt) {
        this.remainTotalAmt = remainTotalAmt;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public SiAdvNetting getNettingID() {
        return nettingID;
    }

    public void setNettingID(SiAdvNetting nettingID) {
        this.nettingID = nettingID;
    }

    public SiSaleInvoice getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(SiSaleInvoice invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Double getAppliedAmt() {
        return appliedAmt;
    }

    public void setAppliedAmt(Double appliedAmt) {
        this.appliedAmt = appliedAmt;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public AcCurrency getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(AcCurrency invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public Double getProfLossAmt() {
        return profLossAmt;
    }

    public void setProfLossAmt(Double profLossAmt) {
        this.profLossAmt = profLossAmt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailID != null ? detailID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SiAdvNettingDetail)) {
            return false;
        }
        SiAdvNettingDetail other = (SiAdvNettingDetail) object;
        if ((this.detailID == null && other.detailID != null) || (this.detailID != null && !this.detailID.equals(other.detailID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fisoft.phucsinh.phucsinhsrv.entity.SiAdvNettingDetail[detailID=" + detailID + "]";
    }

    /**
     * Tinh so tien ap khi nhap ti gia
     */
    public void calculateAppliedAmt(SiAdvNetting parentAdvNetting) throws ERPIllegalArgumentException {
        Double appliedAmount = null;
        Double discountAmount;
        discountAmount = this.getDiscountAmt();
        if (discountAmount == null) {
            throw new ERPIllegalArgumentException("Chua cap nhat so tien chiet khau");
        }
        if (this.getInvoiceCurrency().equals(parentAdvNetting.getCurrency())) {
            appliedAmount = this.getNettingTotalAmt() - discountAmount;
        } else {
            Double tempAppliedAmount = this.getNettingTotalAmt() - discountAmount;
            if (parentAdvNetting.getCurrency().equals(this.getInvoiceCurrency())) {
                appliedAmount = tempAppliedAmount;
            } else {
                Double rate = this.getExchangeRate();
                String baseCurrency = parentAdvNetting.getBranchCode().getCompany().getLedger().getCurrency();
                if (rate == null || rate == 0d) {
                    throw new ERPIllegalArgumentException("Chua cap nhat ti gia");
                }
                if (baseCurrency.equalsIgnoreCase((String) this.getInvoiceCurrency().getID())) {
                    appliedAmount = tempAppliedAmount / rate;
                } else {
                    appliedAmount = tempAppliedAmount * rate;
                }
            }
        }
        this.setAppliedAmt(appliedAmount);
    }

    /**
     * Tinh ti gia khi nhap so tien ap
     */
    public void calculateExchangeRate(SiAdvNetting parentAdvNetting) throws ERPIllegalArgumentException {
        Double appliedAmount = null;
        Double discountAmount;
        Double rate;
        discountAmount = this.getDiscountAmt();
        if (discountAmount == null) {
            throw new ERPIllegalArgumentException("Chua cap nhat so tien chiet khau");
        }
        if (this.getInvoiceCurrency().equals(parentAdvNetting.getCurrency())) {
            rate = 1d;
        } else {
            appliedAmount = this.getAppliedAmt();
            if (appliedAmount == null || appliedAmount == 0d) {
                throw new ERPIllegalArgumentException("Chua cap nhat So tien ap");
            }
            Double tempAppliedAmount = this.getNettingTotalAmt() - discountAmount;
            String baseCurrency = parentAdvNetting.getBranchCode().getCompany().getLedger().getCurrency();
            if (baseCurrency.equalsIgnoreCase((String) this.getInvoiceCurrency().getID())) {
                rate = tempAppliedAmount / appliedAmount;
            } else {
                rate = appliedAmount / tempAppliedAmount;
            }
        }
        this.setExchangeRate(rate);
    }

    /**
     * Tinh Lai/Lo chenh lech ti gia
     */
    public void calculateProfLossAmt(SiAdvNetting parentAdvNetting) throws ERPIllegalArgumentException {
        Double appliedAmount = null;
        Double discountAmount;
        Double rate;
        Double invoiceRate;
        Double tempAppliedAmount;
        Double plAmt = 0d;
        discountAmount = this.getDiscountAmt();
        if (discountAmount == null) {
            throw new ERPIllegalArgumentException("Chua cap nhat so tien chiet khau");
        }
        tempAppliedAmount = this.getNettingTotalAmt() - discountAmount;
        invoiceRate = this.getInvoiceID().getExchangeRate();
        rate = this.getExchangeRate();
        appliedAmount = this.getAppliedAmt();
        if (appliedAmount == null || appliedAmount == 0d) {
            throw new ERPIllegalArgumentException("Chua cap nhat So tien ap");
        }
        String baseCurrency = parentAdvNetting.getBranchCode().getCompany().getLedger().getCurrency();
        String invCurrency = (String) this.getInvoiceCurrency().getID();
        String collCurrency = (String) parentAdvNetting.getCurrency().getID();
        if (collCurrency.equals(baseCurrency) && !invCurrency.equals(baseCurrency)) {
            plAmt = tempAppliedAmount * (rate - invoiceRate);
        } else if (!collCurrency.equals(baseCurrency) && invCurrency.equals(baseCurrency)) {
            plAmt = appliedAmount * (rate - parentAdvNetting.getExchangeRate());
        } else if (!collCurrency.equals(baseCurrency) && !invCurrency.equals(baseCurrency) && !collCurrency.equals(invCurrency)) {
            plAmt = tempAppliedAmount * (parentAdvNetting.getExchangeRate() - invoiceRate);
        } else if (!collCurrency.equals(baseCurrency) && !invCurrency.equals(baseCurrency) && collCurrency.equals(invCurrency)) {
            plAmt = appliedAmount * parentAdvNetting.getExchangeRate() - tempAppliedAmount * invoiceRate;
        }
        this.setProfLossAmt(plAmt);
    }
}
