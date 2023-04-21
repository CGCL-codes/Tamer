package org.adempierelbr.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import org.adempierelbr.util.BPartnerUtil;
import org.adempierelbr.util.NFeEmail;
import org.adempierelbr.util.NFeUtil;
import org.adempierelbr.util.TaxBR;
import org.adempierelbr.util.TextUtil;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCost;
import org.compiere.model.MCountry;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MProduct;
import org.compiere.model.MRegion;
import org.compiere.model.MSequence;
import org.compiere.model.MShipper;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.w3c.dom.Node;

/**
 *	MNotaFiscal
 *
 *	Model for X_LBR_NotaFiscal
 *
 *	@author Mario Grigioni (Kenos, www.kenos.com.br)
 *	@version $Id: MNotaFiscal.java, 08/01/2008 10:56:00 mgrigioni
 */
public class MNotaFiscal extends X_LBR_NotaFiscal {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(MNotaFiscal.class);

    /**	Process Message */
    private String m_processMsg = null;

    /** REFERENCE */
    public Map<String, String> m_refNCM = new HashMap<String, String>();

    public Map<String, String> m_refCFOP = new HashMap<String, String>();

    public ArrayList<Integer> m_refLegalMessage = new ArrayList<Integer>();

    /** STRING */
    String m_NCMReference = "";

    String m_CFOPNote = "";

    String m_CFOPReference = "";

    String m_LegalMessage = "";

    /** CONSTANT */
    public static final int BRAZIL = 139;

    public String getProcessMsg() {
        if (m_processMsg == null) m_processMsg = "";
        return m_processMsg;
    }

    /**************************************************************************
	 *  Default Constructor
	 *  @param Properties ctx
	 *  @param int ID (0 create new)
	 *  @param String trx
	 */
    public MNotaFiscal(Properties ctx, int ID, String trx) {
        super(ctx, ID, trx);
    }

    /**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
    public MNotaFiscal(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * Retorna as Notas Fiscais por período (compra e venda)
	 * @param dateFrom
	 * @param dateTo
	 * @return MNotaFiscal[]
	 */
    public static MNotaFiscal[] get(Timestamp dateFrom, Timestamp dateTo, String trxName) {
        return get(dateFrom, dateTo, null, trxName);
    }

    /**
	 * Retorna as Notas Fiscais por período (compra, venda ou ambos)
	 * @param dateFrom
	 * @param dateTo
	 * @param isSOTrx: true = venda, false = compra, null = ambos
	 * @return MNotaFiscal[]
	 */
    public static MNotaFiscal[] get(Timestamp dateFrom, Timestamp dateTo, Boolean isSOTrx, String trxName) {
        String whereClause = "AD_Client_ID=? AND " + "(CASE WHEN IsSOTrx='Y' THEN TRUNC(DateDoc) " + "ELSE TRUNC(NVL(lbr_DateInOut, DateDoc)) END) BETWEEN ? AND ?";
        String orderBy = "(CASE WHEN IsSOTrx='Y' THEN TRUNC(DateDoc) ELSE TRUNC(NVL(lbr_DateInOut, DateDoc)) END)";
        if (isSOTrx != null) whereClause += " AND IsSOTrx='" + (isSOTrx ? "Y" : "N") + "'";
        MTable table = MTable.get(Env.getCtx(), MNotaFiscal.Table_Name);
        Query q = new Query(Env.getCtx(), table, whereClause.toString(), trxName);
        q.setOrderBy(orderBy);
        q.setParameters(new Object[] { Env.getAD_Client_ID(Env.getCtx()), dateFrom, dateTo });
        List<MNotaFiscal> list = q.list();
        MNotaFiscal[] nfs = new MNotaFiscal[list.size()];
        return list.toArray(nfs);
    }

    public boolean beforeSave(boolean newRecord) {
        if (getC_DocType_ID() != getC_DocTypeTarget_ID()) setC_DocType_ID(getC_DocTypeTarget_ID());
        return true;
    }

    public boolean beforeDelete() {
        String trxName = get_TrxName();
        int C_Invoice_ID = getC_Invoice_ID();
        if (C_Invoice_ID > 0) {
            MInvoice invoice = new MInvoice(getCtx(), C_Invoice_ID, trxName);
            invoice.set_ValueOfColumn("LBR_NotaFiscal_ID", null);
            if (!invoice.save(trxName)) return false;
        }
        if (!deleteLBR_NFTax(get_ID(), trxName)) return false;
        if (!deleteLBR_NFLineTax(get_ID(), trxName)) return false;
        if (!deleteLBR_NotaFiscalLine(get_ID(), trxName)) return false;
        return true;
    }

    public int getLBR_DocTypeNF_ID(MInvoice invoice) {
        Integer LBR_DocTypeNF_ID = 0;
        MDocType docType = new MDocType(getCtx(), invoice.getC_DocTypeTarget_ID(), get_TrxName());
        LBR_DocTypeNF_ID = (Integer) docType.get_Value("LBR_DocTypeNF_ID");
        if (LBR_DocTypeNF_ID == null) LBR_DocTypeNF_ID = 0;
        return LBR_DocTypeNF_ID.intValue();
    }

    public void setSiscomexTax() {
        BigDecimal TotalLinesAmt = getTotalLines();
        if (TotalLinesAmt.signum() == 0) return;
        BigDecimal TotalSiscomexAmt = getlbr_TotalSISCOMEX();
        if (TotalSiscomexAmt.signum() == 0) return;
        MNotaFiscalLine[] nfLines = getLines("Line");
        for (MNotaFiscalLine nfLine : nfLines) {
            BigDecimal lineAmt = nfLine.getLineTotalAmt();
            BigDecimal siscomexAmt = lineAmt.divide(TotalLinesAmt, TaxBR.MCROUND);
            siscomexAmt = TotalSiscomexAmt.multiply(siscomexAmt);
            nfLine.setlbr_LineTotalSISCOMEX(siscomexAmt.setScale(TaxBR.SCALE, TaxBR.ROUND));
            nfLine.save();
            BigDecimal ICMSRate = nfLine.getICMSRate();
            if (ICMSRate == null || ICMSRate.signum() == 0) continue;
            BigDecimal TaxBaseAmt = siscomexAmt.divide((Env.ONE.subtract((ICMSRate.divide(Env.ONEHUNDRED, TaxBR.MCROUND)))), TaxBR.MCROUND);
            BigDecimal TaxAmt = TaxBaseAmt.multiply((ICMSRate.divide(Env.ONEHUNDRED, TaxBR.MCROUND)));
            int LBR_TaxGroup_ID = TaxBR.getTaxGroup_ID("ICMS");
            if (LBR_TaxGroup_ID > 0) {
                TaxBR.setNFLineTax(getCtx(), LBR_TaxGroup_ID, nfLine.get_ID(), TaxBaseAmt.setScale(TaxBR.SCALE, TaxBR.ROUND), TaxAmt.setScale(TaxBR.SCALE, TaxBR.ROUND), ICMSRate, Env.ZERO, "SISCOMEX", get_TrxName());
            }
        }
    }

    public void setFreightTax() {
        BigDecimal TotalLinesAmt = getTotalLines();
        if (TotalLinesAmt.signum() == 0) return;
        BigDecimal TotalFreightAmt = getFreightAmt();
        if (TotalFreightAmt.signum() == 0) return;
        MNotaFiscalLine[] nfLines = getLines("Line");
        for (MNotaFiscalLine nfLine : nfLines) {
            BigDecimal ICMSRate = nfLine.getICMSRate();
            if (ICMSRate == null || ICMSRate.signum() == 0) continue;
            BigDecimal freightAmt = nfLine.getFreightAmt(TotalLinesAmt, TotalFreightAmt);
            BigDecimal TaxBaseAmt = freightAmt.divide((Env.ONE.subtract((ICMSRate.divide(Env.ONEHUNDRED, TaxBR.MCROUND)))), TaxBR.MCROUND);
            BigDecimal TaxAmt = TaxBaseAmt.multiply((ICMSRate.divide(Env.ONEHUNDRED, TaxBR.MCROUND)));
            int LBR_TaxGroup_ID = TaxBR.getTaxGroup_ID("ICMS");
            if (LBR_TaxGroup_ID > 0) {
                TaxBR.setNFLineTax(getCtx(), LBR_TaxGroup_ID, nfLine.get_ID(), TaxBaseAmt.setScale(TaxBR.SCALE, TaxBR.ROUND), TaxAmt.setScale(TaxBR.SCALE, TaxBR.ROUND), ICMSRate, Env.ZERO, "FRETE", get_TrxName());
            }
        }
    }

    /**************************************************************************
	 *  getLines
	 *  @return MNotaFiscalLine[] lines
	 *  @deprecated
	 */
    public MNotaFiscalLine[] getLines() {
        return getLines(null);
    }

    public void setDescription(String description) {
        if (description == null) super.setDescription(""); else {
            description = description.replaceAll("null", " ");
            super.setDescription(description.trim());
        }
    }

    public MBPartnerLocation getBPartnerLocation(MOrder order, MInvoice invoice, MInOut shipment) {
        MBPartnerLocation bpLocation = null;
        if (shipment != null && shipment.getC_BPartner_Location_ID() != 0) {
            bpLocation = new MBPartnerLocation(getCtx(), shipment.getC_BPartner_Location_ID(), get_TrxName());
        } else if (order != null && order.getC_BPartner_Location_ID() != 0) {
            bpLocation = new MBPartnerLocation(getCtx(), order.getC_BPartner_Location_ID(), get_TrxName());
        } else if (invoice != null) {
            bpLocation = new MBPartnerLocation(getCtx(), invoice.getC_BPartner_Location_ID(), get_TrxName());
        }
        return bpLocation;
    }

    public void setOrgInfo(int AD_Org_ID) {
        MOrg org = MOrg.get(getCtx(), AD_Org_ID);
        MOrgInfo orgInfo = MOrgInfo.get(getCtx(), AD_Org_ID, get_TrxName());
        MLocation orgLoc = new MLocation(getCtx(), orgInfo.getC_Location_ID(), get_TrxName());
        MCountry orgCountry = new MCountry(getCtx(), orgLoc.getC_Country_ID(), get_TrxName());
        String legalEntity = orgInfo.get_ValueAsString("lbr_LegalEntity");
        if (legalEntity == null || legalEntity.length() < 1) legalEntity = org.getName();
        setOrg_Location_ID(orgLoc.getC_Location_ID());
        setlbr_OrgAddress1(orgLoc.getAddress1());
        setlbr_OrgAddress2(orgLoc.getAddress2());
        setlbr_OrgAddress3(orgLoc.getAddress3());
        setlbr_OrgAddress4(orgLoc.getAddress4());
        setlbr_OrgName(legalEntity);
        setlbr_OrgCity(orgLoc.getCity());
        setlbr_OrgPostal(orgLoc.getPostal());
        setlbr_OrgCountry(orgCountry.getCountryCode());
        setlbr_OrgRegion(orgLoc.getRegionName(true));
        setlbr_OrgCCM(orgInfo.get_ValueAsString("lbr_CCM"));
        setlbr_CNPJ(orgInfo.get_ValueAsString("lbr_CNPJ"));
        setlbr_IE(orgInfo.get_ValueAsString("lbr_IE"));
    }

    public void setBPartner(MBPartner bpartner, MBPartnerLocation bpLocation) {
        if (bpartner == null || bpLocation == null) return;
        setC_BPartner_ID(bpartner.getC_BPartner_ID());
        setC_BPartner_Location_ID(bpLocation.getC_BPartner_Location_ID());
        setBPName(bpartner.getName());
        setlbr_BPPhone(bpLocation.getPhone());
        setlbr_BPCNPJ(BPartnerUtil.getCNPJ(bpartner, bpLocation));
        setlbr_BPIE(BPartnerUtil.getIE(bpartner, bpLocation));
        setlbr_BPSuframa(BPartnerUtil.getSuframa(bpartner, bpLocation));
        MLocation location = new MLocation(getCtx(), bpLocation.getC_Location_ID(), get_TrxName());
        setlbr_BPAddress1(location.getAddress1());
        setlbr_BPAddress2(location.getAddress2());
        setlbr_BPAddress3(location.getAddress3());
        setlbr_BPAddress4(location.getAddress4());
        setlbr_BPCity(location.getCity());
        setlbr_BPPostal(location.getPostal());
        MCountry country = new MCountry(getCtx(), location.getC_Country_ID(), get_TrxName());
        setlbr_BPCountry(country.getCountryCode());
        if (country.get_ID() != BRAZIL) setlbr_BPRegion("EX"); else {
            MRegion region = new MRegion(getCtx(), location.getC_Region_ID(), get_TrxName());
            setlbr_BPRegion(region.getName());
        }
    }

    public void setInvoiceBPartner(MInvoice invoice) {
        if (invoice == null) return;
        setBill_Location_ID(invoice.getC_BPartner_Location_ID());
        setC_PaymentTerm_ID(invoice.getC_PaymentTerm_ID());
        MBPartnerLocation bpLocation = new MBPartnerLocation(getCtx(), invoice.getC_BPartner_Location_ID(), get_TrxName());
        MLocation location = new MLocation(getCtx(), bpLocation.getC_Location_ID(), get_TrxName());
        setlbr_BPInvoiceCNPJ(BPartnerUtil.getCNPJ(getCtx(), invoice.getC_BPartner_ID(), bpLocation.getC_BPartner_Location_ID()));
        setlbr_BPInvoiceIE(BPartnerUtil.getIE(getCtx(), invoice.getC_BPartner_ID(), bpLocation.getC_BPartner_Location_ID()));
        setlbr_BPInvoiceAddress1(location.getAddress1());
        setlbr_BPInvoiceAddress2(location.getAddress2());
        setlbr_BPInvoiceAddress3(location.getAddress3());
        setlbr_BPInvoiceAddress4(location.getAddress4());
        setlbr_BPInvoiceCity(location.getCity());
        setlbr_BPInvoicePostal(location.getPostal());
        MCountry country = new MCountry(getCtx(), location.getC_Country_ID(), get_TrxName());
        setlbr_BPInvoiceCountry(country.getCountryCode());
        if (country.get_ID() != BRAZIL) setlbr_BPInvoiceRegion("EX"); else {
            MRegion region = new MRegion(getCtx(), location.getC_Region_ID(), get_TrxName());
            setlbr_BPInvoiceRegion(region.getName());
        }
    }

    public void setShipmentBPartner(MInOut shipment, MInvoice invoice) {
        MBPartnerLocation bpLocation = null;
        MLocation location = null;
        if (shipment != null && shipment.getM_InOut_ID() != 0) {
            bpLocation = new MBPartnerLocation(getCtx(), shipment.getC_BPartner_Location_ID(), get_TrxName());
            location = new MLocation(getCtx(), bpLocation.getC_Location_ID(), get_TrxName());
            setlbr_BPDeliveryCNPJ(BPartnerUtil.getCNPJ(getCtx(), shipment.getC_BPartner_ID(), bpLocation.getC_BPartner_Location_ID()));
            setlbr_BPDeliveryIE(BPartnerUtil.getIE(getCtx(), shipment.getC_BPartner_ID(), bpLocation.getC_BPartner_Location_ID()));
        } else if (invoice != null) {
            bpLocation = new MBPartnerLocation(getCtx(), invoice.getC_BPartner_Location_ID(), get_TrxName());
            location = new MLocation(getCtx(), bpLocation.getC_Location_ID(), get_TrxName());
            setlbr_BPDeliveryCNPJ(BPartnerUtil.getCNPJ(getCtx(), invoice.getC_BPartner_ID(), bpLocation.getC_BPartner_Location_ID()));
            setlbr_BPDeliveryIE(BPartnerUtil.getIE(getCtx(), invoice.getC_BPartner_ID(), bpLocation.getC_BPartner_Location_ID()));
        } else return;
        setlbr_BPDeliveryAddress1(location.getAddress1());
        setlbr_BPDeliveryAddress2(location.getAddress2());
        setlbr_BPDeliveryAddress3(location.getAddress3());
        setlbr_BPDeliveryAddress4(location.getAddress4());
        setlbr_BPDeliveryCity(location.getCity());
        setlbr_BPDeliveryPostal(location.getPostal());
        MCountry country = new MCountry(getCtx(), location.getC_Country_ID(), get_TrxName());
        setlbr_BPDeliveryCountry(country.getCountryCode());
        if (country.get_ID() != BRAZIL) setlbr_BPDeliveryRegion("EX"); else {
            MRegion region = new MRegion(getCtx(), location.getC_Region_ID(), get_TrxName());
            setlbr_BPDeliveryRegion(region.getName());
        }
        setFreightCostRule(shipment.getFreightCostRule());
    }

    public void setShipper(MShipper shipper, String LicensePlate) {
        setlbr_BPShipperLicensePlate(LicensePlate);
        if (shipper == null) return;
        setM_Shipper_ID(shipper.getM_Shipper_ID());
        setlbr_BPShipperName(shipper.getName());
        MBPartner transp = new MBPartner(getCtx(), shipper.getC_BPartner_ID(), get_TrxName());
        MBPartnerLocation[] transpLocations = transp.getLocations(false);
        MLocation location = null;
        if (transpLocations.length > 0) {
            location = new MLocation(getCtx(), transpLocations[0].getC_Location_ID(), get_TrxName());
            setlbr_BPShipperCNPJ(BPartnerUtil.getCNPJ(transp, transpLocations[0]));
            setlbr_BPShipperIE(BPartnerUtil.getIE(transp, transpLocations[0]));
            setlbr_BPShipperAddress1(location.getAddress1());
            setlbr_BPShipperAddress2(location.getAddress2());
            setlbr_BPShipperAddress3(location.getAddress3());
            setlbr_BPShipperAddress4(location.getAddress4());
            setlbr_BPShipperCity(location.getCity());
            setlbr_BPShipperPostal(location.getPostal());
            MCountry country = new MCountry(getCtx(), location.getC_Country_ID(), get_TrxName());
            setlbr_BPShipperCountry(country.getCountryCode());
            if (country.get_ID() != BRAZIL) setlbr_BPShipperRegion("EX"); else {
                MRegion region = new MRegion(getCtx(), location.getC_Region_ID(), get_TrxName());
                setlbr_BPShipperRegion(region.getName());
            }
        } else {
            setlbr_BPShipperCNPJ(BPartnerUtil.getCNPJ(transp, null));
            setlbr_BPShipperIE(BPartnerUtil.getIE(transp, null));
        }
    }

    /**
	 * Retorna o NCM ou a Referência do NCM
	 * 	de acordo com a configuração do sistema.
	 *
	 * @param LBR_NCM_ID
	 * @return
	 */
    public String getNCM(Integer LBR_NCM_ID) {
        if (LBR_NCM_ID == null || LBR_NCM_ID.intValue() == 0) return null;
        X_LBR_NCM ncm = new X_LBR_NCM(getCtx(), LBR_NCM_ID, get_TrxName());
        String ncmName = ncm.getValue();
        if (!(ncmName == null || ncmName.equals(""))) {
            if (m_refNCM.containsKey(ncmName)) {
                if (!MSysConfig.getBooleanValue("LBR_REF_NCM", true, Env.getAD_Client_ID(Env.getCtx()))) return ncmName; else return m_refNCM.get(ncmName).toString();
            } else {
                String cl = TextUtil.ALFAB[m_refNCM.size()];
                m_refNCM.put(ncmName, cl);
                setNCMReference(ncmName, cl, true);
                if (!MSysConfig.getBooleanValue("LBR_REF_NCM", true, Env.getAD_Client_ID(Env.getCtx()))) return ncmName; else return cl;
            }
        }
        return null;
    }

    /**
	 * Retorna o valor do Imposto
	 *
	 * @return BigDecimal amt
	 */
    public BigDecimal getTaxAmt(String taxIndicator) {
        if (taxIndicator == null) return Env.ZERO;
        String sql = "SELECT SUM(lbr_TaxAmt) FROM LBR_NFTax " + "WHERE LBR_NotaFiscal_ID = ? AND LBR_TaxGroup_ID IN " + "(SELECT LBR_TaxGroup_ID FROM LBR_TaxGroup WHERE UPPER(Name)=?)";
        BigDecimal result = DB.getSQLValueBD(null, sql, new Object[] { getLBR_NotaFiscal_ID(), taxIndicator.toUpperCase() });
        return result == null ? Env.ZERO : result;
    }

    /**
	 * Retorna a Base de Cálculo do Imposto
	 *
	 * @return BigDecimal amt
	 */
    public BigDecimal getTaxBaseAmt(String taxIndicator) {
        if (taxIndicator == null) return Env.ZERO;
        String sql = "SELECT SUM(lbr_TaxBaseAmt) FROM LBR_NFTax " + "WHERE LBR_NotaFiscal_ID = ? AND LBR_TaxGroup_ID IN " + "(SELECT LBR_TaxGroup_ID FROM LBR_TaxGroup WHERE UPPER(Name)=?)";
        BigDecimal result = DB.getSQLValueBD(null, sql, new Object[] { getLBR_NotaFiscal_ID(), taxIndicator.toUpperCase() });
        return result == null ? Env.ZERO : result;
    }

    /**
	 * Retorna a Alíquota do Imposto
	 *
	 * @return BigDecimal amt
	 */
    public BigDecimal getTaxRate(String taxIndicator) {
        if (taxIndicator == null) return Env.ZERO;
        String sql = "SELECT AVG(lbr_TaxRate) FROM LBR_NFLineTax " + "WHERE LBR_NotaFiscalLine_ID IN " + "(SELECT LBR_NotaFiscalLine_ID FROM LBR_NotaFiscalLine WHERE LBR_NotaFiscal_ID = ?) " + "AND LBR_TaxGroup_ID IN (SELECT LBR_TaxGroup_ID FROM LBR_TaxGroup WHERE UPPER(Name)=?)";
        BigDecimal result = DB.getSQLValueBD(get_TrxName(), sql, new Object[] { getLBR_NotaFiscal_ID(), taxIndicator.toUpperCase() });
        return result == null ? Env.ZERO : result;
    }

    public BigDecimal getCost(int C_AcctSchema_ID, int C_CostElement_ID) {
        BigDecimal currentCost = Env.ZERO;
        MNotaFiscalLine[] lines = getLines("line");
        for (MNotaFiscalLine line : lines) {
            int M_Product_ID = line.getM_Product_ID();
            if (M_Product_ID == 0) continue;
            MProduct product = new MProduct(getCtx(), M_Product_ID, get_TrxName());
            MAcctSchema as = MAcctSchema.get(getCtx(), C_AcctSchema_ID);
            MCost cost = MCost.get(product, 0, as, 0, C_CostElement_ID, get_TrxName());
            if (cost != null) currentCost = currentCost.add(cost.getCurrentCostPrice().multiply(line.getQty()));
        }
        return currentCost.setScale(TaxBR.SCALE, TaxBR.ROUND);
    }

    /**
	 *  Retorno o valor da Base de ICMS
	 *
	 *  @return	BigDecimal	Base ICMS
	 */
    public BigDecimal getICMSBase() {
        return getTaxBaseAmt("ICMS");
    }

    /**
	 *  Retorno o valor da Base de ICMSST
	 *
	 *  @return	BigDecimal	Base ICMSST
	 */
    public BigDecimal getICMSSTBase() {
        return getTaxBaseAmt("ICMSST");
    }

    /**
	 *  Retorno o valor do ICMS
	 *
	 *  @return	BigDecimal	ICMS
	 */
    public BigDecimal getICMSAmt() {
        return getTaxAmt("ICMS");
    }

    public BigDecimal getICMSDebAmt() {
        String sql = "SELECT SUM(ValorICMS) FROM lbr_SitICMS_V " + "WHERE LBR_NotaFiscal_ID = ?";
        BigDecimal result = DB.getSQLValueBD(null, sql, new Object[] { getLBR_NotaFiscal_ID() });
        return result == null ? Env.ZERO : result;
    }

    /**
	 *  Retorno a média das alíquotas do ICMS
	 *
	 *  @return	BigDecimal	ICMS Rate
	 */
    public BigDecimal getICMSRate() {
        return getTaxRate("ICMS");
    }

    /**
	 * Retorno o valor do ICMS
	 * @param ctx
	 * @param LBR_NotaFiscal_ID
	 * @param trx
	 * @return BigDecimal ICMS
	 * @deprecated
	 */
    public static BigDecimal getICMSAmt(Properties ctx, int LBR_NotaFiscal_ID, String trx) {
        MNotaFiscal nf = new MNotaFiscal(ctx, LBR_NotaFiscal_ID, trx);
        return nf.getICMSAmt();
    }

    /**
	 *  Retorno o valor do ICMSST
	 *
	 *  @return	BigDecimal	ICMSST
	 */
    public BigDecimal getICMSSTAmt() {
        return getTaxAmt("ICMSST");
    }

    /**
	 *  Retorno o valor do IPI
	 *
	 *  @return	BigDecimal	IPI
	 */
    public BigDecimal getIPIAmt() {
        return getTaxAmt("IPI");
    }

    /**
	 *  Retorno o valor do PIS
	 *
	 *  @return	BigDecimal	PIS
	 */
    public BigDecimal getPISAmt() {
        return getTaxAmt("PIS");
    }

    /**
	 *  Retorno o valor do COFINS
	 *
	 *  @return	BigDecimal	COFINS
	 */
    public BigDecimal getCOFINSAmt() {
        return getTaxAmt("COFINS");
    }

    public static int getLBR_NotaFiscal_ID(String DocumentNo, boolean IsSOTrx, String trx) {
        String sql = "SELECT LBR_NotaFiscal_ID FROM LBR_NotaFiscal " + "WHERE DocumentNo = ? AND AD_Client_ID = ? " + "AND IsSOTrx = ? " + "ORDER BY LBR_NotaFiscal_ID desc";
        Integer LBR_NotaFiscal_ID = DB.getSQLValue(trx, sql, new Object[] { DocumentNo, Env.getAD_Client_ID(Env.getCtx()), IsSOTrx });
        return LBR_NotaFiscal_ID;
    }

    public static int getNFB(int AD_Org_ID) {
        return getNFB(AD_Org_ID, true);
    }

    public static int getNFB(int AD_Org_ID, boolean isSOTrx) {
        String sql = "SELECT C_DocType_ID FROM C_DocType " + "WHERE DocBaseType = 'NFB' " + "AND AD_Client_ID = ? AND AD_Org_ID IN (0,?) " + "AND IsSOTrx = ? " + "order by C_DocType_ID, AD_Org_ID desc";
        int C_DocType_ID = DB.getSQLValue(null, sql, new Object[] { Env.getAD_Client_ID(Env.getCtx()), AD_Org_ID, isSOTrx });
        return C_DocType_ID;
    }

    /**
	 * 	Retorna o CFOP das linhas, no caso de mais de 1 CFOP,
	 * 		retorna o ref. ao Maior Valor
	 *
	 * @return CFOP
	 */
    public String getCFOP() {
        String sql = "SELECT lbr_CFOPName " + "FROM LBR_NotaFiscalLine " + "WHERE LBR_NotaFiscal_ID=? ORDER BY LineTotalAmt DESC";
        return DB.getSQLValueString(null, sql, getLBR_NotaFiscal_ID());
    }

    /**
	 * Retorna o CFOP ou a Referência do CFOP
	 * 	de acordo com a configuração do sistema.
	 *
	 * @param LBR_CFOP_ID
	 * @return CFOP ou Ref. do CFOP
	 */
    public String getCFOP(Integer LBR_CFOP_ID) {
        if (LBR_CFOP_ID == null || LBR_CFOP_ID.intValue() == 0) return null;
        X_LBR_CFOP cfop = new X_LBR_CFOP(getCtx(), LBR_CFOP_ID, get_TrxName());
        String cfopName = cfop.getValue();
        if (!(cfopName == null || cfopName.equals(""))) {
            if (m_refCFOP.containsKey(cfopName)) {
                if (!MSysConfig.getBooleanValue("LBR_REF_CFOP", true, Env.getAD_Client_ID(Env.getCtx()))) return cfopName; else return m_refCFOP.get(cfopName).toString();
            } else {
                String cl = TextUtil.ALFAB[m_refCFOP.size()];
                m_refCFOP.put(cfopName, cl);
                setCFOPNote(cfop.getDescription() + ", ", true);
                setCFOPReference(cfopName, cl);
                if (!MSysConfig.getBooleanValue("LBR_REF_CFOP", true, Env.getAD_Client_ID(Env.getCtx()))) return cfopName; else return cl;
            }
        }
        return null;
    }

    public void setLegalMessage(Integer LBR_LegalMessage_ID) {
        if (LBR_LegalMessage_ID == null || LBR_LegalMessage_ID.intValue() == 0) return;
        X_LBR_LegalMessage lMessage = new X_LBR_LegalMessage(getCtx(), LBR_LegalMessage_ID, get_TrxName());
        if (!m_refLegalMessage.contains(LBR_LegalMessage_ID)) {
            m_refLegalMessage.add(LBR_LegalMessage_ID);
            setLegalMessage(lMessage.getTextMsg() + ", ", true);
        }
    }

    /**************************************************************************
	 *  getLines
	 *  @param String orderBy or null
	 *  @return MNotaFiscalLine[] lines
	 */
    public MNotaFiscalLine[] getLines(String orderBy) {
        String whereClause = "LBR_NotaFiscal_ID = ?";
        Object[] parameters = new Object[] { getLBR_NotaFiscal_ID() };
        return getLines(parameters, whereClause, orderBy);
    }

    /**
	 * getLines
	 * @param Object[] parameters
	 * @param String whereClause
	 * @param String orderBy
	 * @return MNotaFiscalLine[] lines
	 */
    public MNotaFiscalLine[] getLines(Object[] parameters, String whereClause, String orderBy) {
        MTable table = MTable.get(getCtx(), MNotaFiscalLine.Table_Name, get_TrxName());
        Query query = new Query(getCtx(), table, whereClause, get_TrxName());
        query.setParameters(parameters);
        orderBy = TextUtil.checkOrderBy(orderBy);
        if (orderBy != null) query.setOrderBy(orderBy);
        List<MNotaFiscalLine> list = query.list();
        return list.toArray(new MNotaFiscalLine[list.size()]);
    }

    /**************************************************************************
	 *  lastPrinted
	 *  @return int documentno
	 */
    public int lastPrinted() {
        String sql = "SELECT max(DocumentNo) FROM LBR_NotaFiscal " + "WHERE AD_Org_ID = ? AND C_DocType_ID = ? " + "AND IsSOTrx = ? AND IsPrinted = 'Y'";
        int documentno = DB.getSQLValue(get_TrxName(), sql, new Object[] { getAD_Org_ID(), getC_DocType_ID(), isSOTrx() });
        return documentno;
    }

    /**
	 * Convert Amt
	 * @throws Exception
	 */
    public BigDecimal convertAmt(BigDecimal Amt, int C_Currency_ID) throws Exception {
        Amt = MConversionRate.convertBase(getCtx(), Amt, C_Currency_ID, getDateDoc(), 0, Env.getAD_Client_ID(getCtx()), Env.getAD_Org_ID(getCtx()));
        if (Amt == null) {
            log.log(Level.SEVERE, "null if no rate = " + getDateDoc() + " / Currency = " + C_Currency_ID);
            throw new Exception();
        }
        return Amt;
    }

    /**
	 * 	Void Document.
	 * 	@return true if success
	 */
    public boolean voidIt() {
        log.info(toString());
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_VOID);
        if (m_processMsg != null) return false;
        if (isCancelled()) return false;
        if (isPrinted()) {
            MInvoice invoice = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
            if (invoice.getDocStatus().equals(MInvoice.DOCSTATUS_Voided) || invoice.getDocStatus().equals(MInvoice.DOCSTATUS_Reversed)) ; else if (invoice.voidIt()) {
                invoice.save(get_TrxName());
            } else {
                m_processMsg = invoice.getProcessMsg();
                return false;
            }
            if (getM_InOut_ID() != 0) {
                MInOut shipment = new MInOut(getCtx(), getM_InOut_ID(), get_TrxName());
                if (shipment.getDocStatus().equals(MInOut.DOCSTATUS_Voided) || shipment.getDocStatus().equals(MInOut.DOCSTATUS_Reversed)) ; else if (shipment.voidIt()) {
                    shipment.save(get_TrxName());
                } else {
                    m_processMsg = shipment.getProcessMsg();
                    return false;
                }
            }
        } else {
            MInvoice invoice = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
            invoice.set_ValueOfColumn("LBR_NotaFiscal_ID", null);
            invoice.save(get_TrxName());
            if (getC_DocTypeTarget_ID() != 0) {
                MDocType docType = new MDocType(getCtx(), getC_DocTypeTarget_ID(), get_TrxName());
                if (docType.getDocNoSequence_ID() != 0) {
                    MSequence sequence = new MSequence(getCtx(), docType.getDocNoSequence_ID(), get_TrxName());
                    int actual = sequence.getCurrentNext() - 1;
                    if (actual == Integer.parseInt(getDocumentNo())) {
                        sequence.setCurrentNext(sequence.getCurrentNext() - 1);
                        sequence.save(get_TrxName());
                    } else {
                        log.log(Level.SEVERE, "Existem notas com numeração superior " + "no sistema. Nota: " + getDocumentNo());
                        return false;
                    }
                }
            }
        }
        setIsCancelled(true);
        m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_VOID);
        if (m_processMsg != null) return false;
        return true;
    }

    public static boolean deleteLBR_NotaFiscalLine(int LBR_NotaFiscal_ID, String trx) {
        StringBuffer sql = new StringBuffer("DELETE FROM ").append(X_LBR_NotaFiscalLine.Table_Name).append(" WHERE LBR_NotaFiscal_ID = ").append(LBR_NotaFiscal_ID);
        if (DB.executeUpdate(sql.toString(), trx) == -1) return false;
        return true;
    }

    public static boolean deleteLBR_NFTax(int LBR_NotaFiscal_ID, String trx) {
        StringBuffer sql = new StringBuffer("DELETE FROM ").append(X_LBR_NFTax.Table_Name).append(" WHERE LBR_NotaFiscal_ID = ").append(LBR_NotaFiscal_ID);
        if (DB.executeUpdate(sql.toString(), trx) == -1) return false;
        return true;
    }

    public static boolean deleteLBR_NFLineTax(int LBR_NotaFiscal_ID, String trx) {
        StringBuffer sql = new StringBuffer("DELETE FROM ").append(X_LBR_NFLineTax.Table_Name).append(" WHERE LBR_NotaFiscalLine_ID IN ").append("(SELECT LBR_NotaFiscalLine_ID FROM ").append(X_LBR_NotaFiscalLine.Table_Name).append(" WHERE LBR_NotaFiscal_ID = ").append(LBR_NotaFiscal_ID).append(")");
        if (DB.executeUpdate(sql.toString(), trx) == -1) return false;
        return true;
    }

    public String getNCMReference() {
        return TextUtil.retiraPontoFinal(m_NCMReference);
    }

    /**
	 * 	Set NCM Reference
	 *
	 * 	A-0000.00.00, B-9999.99.99
	 *
	 * @param ncmName
	 * @param cl
	 * @param concat
	 */
    private void setNCMReference(String ncmName, String cl, boolean concat) {
        if (concat) {
            if (m_NCMReference.equals("")) m_NCMReference += "Classif: ";
            m_NCMReference += cl + "-" + ncmName + ", ";
        } else m_NCMReference = ncmName;
    }

    public String getCFOPNote() {
        return TextUtil.retiraPontoFinal(m_CFOPNote);
    }

    private void setCFOPNote(String cfopNote, boolean concat) {
        if (concat) {
            m_CFOPNote += cfopNote;
        } else {
            m_CFOPNote = cfopNote;
        }
    }

    public String getCFOPReference() {
        return TextUtil.retiraPontoFinal(m_CFOPReference);
    }

    /**
	 * 	Set CFOP Reference.
	 *
	 * 	A-5.101, B-5.102
	 *
	 * @param cfopName
	 * @param cl
	 * @param concat
	 */
    private void setCFOPReference(String cfopName, String cl) {
        if (m_CFOPReference == null || m_CFOPReference.equals("")) {
            if (MSysConfig.getBooleanValue("LBR_REF_CFOP", true, Env.getAD_Client_ID(Env.getCtx()))) m_CFOPReference = cl + "-" + cfopName; else m_CFOPReference = cfopName;
        } else {
            if (MSysConfig.getBooleanValue("LBR_REF_CFOP", true, Env.getAD_Client_ID(Env.getCtx()))) m_CFOPReference += ", " + cl + "-" + cfopName; else m_CFOPReference += ", " + cfopName;
        }
    }

    public String getLegalMessage() {
        return TextUtil.retiraPontoFinal(m_LegalMessage);
    }

    private void setLegalMessage(String legalMessage, boolean concat) {
        if (concat) {
            m_LegalMessage += legalMessage;
        } else {
            m_LegalMessage = legalMessage;
        }
    }

    /**************************************************************************
	 *  getTaxes
	 *  @return X_LBR_NFLineTax[] taxes
	 */
    public X_LBR_NFTax[] getTaxes() {
        String whereClause = "LBR_NotaFiscal_ID = ?";
        MTable table = MTable.get(getCtx(), X_LBR_NFTax.Table_Name);
        Query query = new Query(getCtx(), table, whereClause, get_TrxName());
        query.setParameters(new Object[] { getLBR_NotaFiscal_ID() });
        List<X_LBR_NFLineTax> list = query.list();
        return list.toArray(new X_LBR_NFTax[list.size()]);
    }

    /**************************************************************************
	 *  getDIs
	 *  @return X_LBR_NFDI[] taxes
	 */
    public X_LBR_NFDI[] getDIs() {
        String whereClause = "LBR_NotaFiscal_ID = ?";
        MTable table = MTable.get(getCtx(), X_LBR_NFDI.Table_Name);
        Query query = new Query(getCtx(), table, whereClause, get_TrxName());
        query.setParameters(new Object[] { getLBR_NotaFiscal_ID() });
        List<X_LBR_NFDI> list = query.list();
        return list.toArray(new X_LBR_NFDI[list.size()]);
    }

    /**
	 * Atualiza autorização NF-e e XML de distribuicao
	 *
	 * return null (success) or error message
	 * @throws Exception
	 */
    public static String authorizeNFe(Node node, String trxName) {
        String error = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String chNFe = NFeUtil.getValue(node, "chNFe");
            String xMotivo = NFeUtil.getValue(node, "xMotivo");
            String digVal = NFeUtil.getValue(node, "digVal");
            String dhRecbto = NFeUtil.getValue(node, "dhRecbto");
            String cStat = NFeUtil.getValue(node, "cStat");
            String nProt = NFeUtil.getValue(node, "nProt");
            MNotaFiscal nf = getNFe(chNFe, trxName);
            if (nf == null) {
                error = "NF não encontrada";
                log.severe(error);
                return error;
            }
            if (nf.getlbr_NFeStatus() != null && nf.getlbr_NFeStatus().equals(NFeUtil.AUTORIZADA)) {
                log.fine("NF já processada. " + nf.getDocumentNo());
                return error;
            }
            Timestamp ts = NFeUtil.stringToTime(dhRecbto);
            String nfeDesc = "[" + dhRecbto.replace('T', ' ') + "] " + xMotivo + "\n";
            if (nf.getlbr_NFeDesc() == null) nf.setlbr_NFeDesc(nfeDesc); else nf.setlbr_NFeDesc(nf.getlbr_NFeDesc() + nfeDesc);
            nf.setlbr_DigestValue(digVal);
            nf.setlbr_NFeStatus(cStat);
            nf.setlbr_NFeProt(nProt);
            nf.setDateTrx(ts);
            nf.setProcessed(true);
            nf.save(trxName);
            try {
                if (!NFeUtil.updateAttach(nf, NFeUtil.generateDistribution(nf))) error = "Problemas ao atualizar o XML para o padrão de distribuição";
                if (error == null && (nf.getlbr_NFeStatus().equals(NFeUtil.AUTORIZADA) || nf.getlbr_NFeStatus().equals(NFeUtil.CANCELADA))) {
                    NFeEmail.sendMail(nf);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "", e);
            }
        }
        return error;
    }

    /**
	 * 	Encontra a NF pelo ID de NF-e
	 *
	 * @param NFeID
	 * @return
	 */
    public static MNotaFiscal getNFe(String NFeID, String trxName) {
        String sql = "SELECT LBR_NotaFiscal_ID FROM LBR_NotaFiscal " + "WHERE lbr_NFeID = ? AND AD_Client_ID = ?";
        int LBR_NotaFiscal_ID = DB.getSQLValue(trxName, sql, new Object[] { NFeID, Env.getAD_Client_ID(Env.getCtx()) });
        if (LBR_NotaFiscal_ID > 0) return new MNotaFiscal(Env.getCtx(), LBR_NotaFiscal_ID, trxName); else {
            log.warning("NFe " + NFeID);
            return null;
        }
    }

    /**
	 * 	Verifica se existe NF registrada com este número para Cliente/Fornecedor
	 *
	 * @param String DocumentNo
	 * @param int C_BPartner_ID
	 * @return true if exists or false if not
	 */
    public static boolean ifExists(String documentno, int C_BPartner_ID, boolean isSOTrx) {
        String sql = "SELECT LBR_NotaFiscal_ID FROM LBR_NotaFiscal " + "WHERE DocumentNo = ? AND C_BPartner_ID = ? " + "AND AD_Client_ID = ? AND IsSOTrx = ?";
        int LBR_NotaFiscal_ID = DB.getSQLValue(null, sql, new Object[] { documentno, C_BPartner_ID, Env.getAD_Client_ID(Env.getCtx()), isSOTrx });
        return LBR_NotaFiscal_ID == -1 ? false : true;
    }

    /**
	 * Extrai o número da NF
	 *
	 * @param	String	No da NF com a Série
	 * @return	String	No da NF sem a Série
	 */
    public static String getDocNo(String documentNo) {
        if (documentNo == null || documentNo.startsWith("-")) return "";
        if (documentNo.indexOf('-') == -1) return documentNo;
        return documentNo.substring(0, documentNo.indexOf('-'));
    }

    public String getDocNo() {
        return getDocNo(getDocumentNo());
    }

    /**
	 * Extrai a Série da NF
	 *
	 * @param	String	No da NF com a Série
	 * @return	String	Série da NF
	 */
    public static String getSerieNo(String documentNo) {
        if (documentNo == null || documentNo.indexOf('-') == -1 || documentNo.endsWith("-")) return "";
        return documentNo.substring(1 + documentNo.indexOf('-'), documentNo.length());
    }

    public String getSerieNo() {
        return getSerieNo(getDocumentNo());
    }
}
