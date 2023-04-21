package org.openXpertya.process;

import java.math.BigDecimal;
import org.openXpertya.model.AbstractRetencionProcessor;
import org.openXpertya.model.MAllocationHdr;
import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MDocType;
import org.openXpertya.model.MInvoice;
import org.openXpertya.model.MInvoiceLine;
import org.openXpertya.model.MRetSchemaConfig;
import org.openXpertya.model.MRetencionSchema;
import org.openXpertya.model.MRole;
import org.openXpertya.model.X_M_Retencion_Invoice;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

public class RetencionIIBB extends AbstractRetencionProcessor {

    /** ID de la tasa de impuesto exenta */
    private static Integer taxExenc = 0;

    /** [INI] Importe No Imponible. */
    private BigDecimal importeNoImponible = Env.ZERO;

    /** [T] Porcentaje de la Retención. */
    private BigDecimal porcentajeRetencion = Env.ZERO;

    /** [MR] Importe mínimo a retener en cada pago. */
    private BigDecimal importeMinimoRetencion = Env.ZERO;

    /** [PAA] Importe de pagos anteriores acumulados en el mes. */
    private BigDecimal pagosAnteriores = Env.ZERO;

    /** [RAA] Retenciones anteriores acumuladas (en el mes). */
    private BigDecimal retencionesAnteriores = Env.ZERO;

    /** [BI] Base imponible. */
    private BigDecimal baseImponible = Env.ZERO;

    /** [ID] Importe determinado. */
    private BigDecimal importeDeterminado = Env.ZERO;

    /** [N] DescuentoNeto. */
    private BigDecimal descuentoNeto = Env.ZERO;

    /** [P] FromPadron. */
    private String fromPadron = "N";

    private X_M_Retencion_Invoice retencion = null;

    public void loadConfig(MRetencionSchema retSchema) {
        setRetencionSchema(retSchema);
        String sql = MRole.getDefault().addAccessSQL(" SELECT C_Tax_ID FROM C_Tax WHERE isactive = 'Y' AND istaxexempt = 'Y' AND to_country_id IS NULL AND rate = 0.0 ", "C_Tax", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
        ;
        taxExenc = DB.getSQLValue(null, sql);
        setImporteNoImponible(getParamValueBigDecimal(MRetSchemaConfig.NAME_ImporteNoImponible, Env.ZERO));
        setPorcentajeRetencion(getParamValueBigDecimal(MRetSchemaConfig.NAME_PorcentajeARetener, Env.ZERO));
        setImporteMinimoRetencion(getParamValueBigDecimal(MRetSchemaConfig.NAME_MinimoARetener, Env.ZERO));
        setDescuentoNeto(getParamValueBigDecimal(MRetSchemaConfig.NAME_DescuentoNeto, Env.ZERO));
        setFromPadron(getParamValueString(MRetSchemaConfig.NAME_DesdePadron, "N"));
    }

    public boolean clearAll() {
        super.clearAll();
        setPagosAnteriores(Env.ZERO);
        setRetencionesAnteriores(Env.ZERO);
        setBaseImponible(null);
        setImporteDeterminado(null);
        return true;
    }

    protected BigDecimal calculateAmount() {
        BigDecimal baseImponible = Env.ZERO;
        BigDecimal importeDeterminado = Env.ZERO;
        BigDecimal importeRetenido = Env.ZERO;
        BigDecimal descuentoNeto = Env.ZERO;
        BigDecimal saldo = Env.ZERO;
        BigDecimal porcentajeRetencion = getPorcentajeRetencion();
        BigDecimal total = getPayNetAmt();
        saldo = total.subtract(getImporteNoImponible());
        baseImponible = (saldo.compareTo(Env.ZERO) < 0 ? Env.ZERO : total);
        descuentoNeto = baseImponible.multiply(getDescuentoNeto()).divide(Env.ONEHUNDRED);
        baseImponible = baseImponible.subtract(descuentoNeto);
        if (getFromPadron().equals("S")) {
            BigDecimal porcentajePadron = MBPartner.getRetencionSegunPadronBsAS(getBPartner().getC_BPartner_ID());
            if (!porcentajePadron.equals(Env.ZERO)) porcentajeRetencion = porcentajePadron;
        }
        importeRetenido = baseImponible.multiply(porcentajeRetencion).divide(Env.ONEHUNDRED);
        if (importeRetenido.compareTo(getImporteMinimoRetencion()) < 0) importeRetenido = Env.ZERO;
        setImporteDeterminado(importeDeterminado);
        setBaseImponible(baseImponible);
        return importeRetenido;
    }

    public boolean save(MAllocationHdr alloc) throws Exception {
        if (getAmount().compareTo(Env.ZERO) <= 0) return false;
        setAllocationHrd(alloc);
        retencion = new X_M_Retencion_Invoice(Env.getCtx(), 0, getTrxName());
        if (alloc != null) {
            retencion.setC_AllocationHdr_ID(getAllocationHrd().getC_AllocationHdr_ID());
        }
        MInvoice factura_Recaudador = crearFacturaRecaudador();
        MInvoice credito_proveedor = crearCreditoProveedor();
        retencion.setamt_retenc(getAmount());
        retencion.setC_RetencionSchema_ID(getRetencionSchema().getC_RetencionSchema_ID());
        retencion.setC_Currency_ID(getCurrency().getC_Currency_ID());
        retencion.setC_Invoice_ID(credito_proveedor.getC_Invoice_ID());
        retencion.setC_Invoice_Retenc_ID(factura_Recaudador.getC_Invoice_ID());
        retencion.setpagos_ant_acumulados_amt(getPagosAnteriores());
        retencion.setretenciones_ant_acumuladas_amt(getRetencionesAnteriores());
        retencion.setpago_actual_amt(getPayNetAmt());
        retencion.setimporte_no_imponible_amt(getImporteNoImponible());
        retencion.setretencion_percent(getPorcentajeRetencion());
        retencion.setimporte_determinado_amt(getImporteDeterminado());
        retencion.setbaseimponible_amt(getBaseImponible());
        retencion.setIsSOTrx(isSOTrx());
        return retencion.save();
    }

    private MInvoice crearFacturaRecaudador() throws Exception {
        MInvoice recaudador_fac = new MInvoice(Env.getCtx(), 0, getTrxName());
        Integer nrolinea = 10;
        int locationID = DB.getSQLValue(getTrxName(), " select C_BPartner_Location_ID from C_BPartner_Location where C_BPartner_id = ? ", getRetencionSchema().getC_BPartner_Recaudador_ID());
        if (locationID == -1) throw new Exception("@NoCollectorLocation@");
        int docTypeID = getRetencionSchema().getCollectorInvoiceDocType();
        if (docTypeID > 0) recaudador_fac.setC_DocTypeTarget_ID(docTypeID); else recaudador_fac.setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APInvoice);
        recaudador_fac.setC_BPartner_ID(getRetencionSchema().getC_BPartner_Recaudador_ID());
        recaudador_fac.setDateInvoiced(Env.getContextAsDate(Env.getCtx(), "#Date"));
        recaudador_fac.setC_Currency_ID(getCurrency().getC_Currency_ID());
        recaudador_fac.setIsSOTrx(isSOTrx());
        recaudador_fac.setDocStatus(MInvoice.DOCSTATUS_Drafted);
        recaudador_fac.setDocAction(MInvoice.DOCACTION_Complete);
        recaudador_fac.setC_BPartner_Location_ID(locationID);
        recaudador_fac.setCUIT(null);
        recaudador_fac.setPaymentRule(MInvoice.PAYMENTRULE_Check);
        if (!recaudador_fac.save()) throw new Exception("@CollectorInvoiceSaveError@");
        MInvoiceLine fac_linea = new MInvoiceLine(Env.getCtx(), 0, getTrxName());
        fac_linea.setC_Invoice_ID(recaudador_fac.getC_Invoice_ID());
        fac_linea.setM_Product_ID(getRetencionSchema().getProduct());
        fac_linea.setLineNetAmt(getPayNetAmt());
        fac_linea.setC_Tax_ID(taxExenc);
        fac_linea.setLine(nrolinea);
        fac_linea.setQty(1);
        fac_linea.setPriceEntered(getAmount());
        fac_linea.setPriceActual(getAmount());
        if (!fac_linea.save()) throw new Exception("@CollectorInvoiceLineSaveError@");
        recaudador_fac.processIt(DocAction.ACTION_Complete);
        recaudador_fac.save();
        return recaudador_fac;
    }

    private MInvoice crearCreditoProveedor() throws Exception {
        MInvoice credito_prov = new MInvoice(Env.getCtx(), 0, getTrxName());
        Integer nrolinea = 10;
        int locationID = DB.getSQLValue(null, " select C_BPartner_Location_ID from C_BPartner_Location where C_BPartner_id = ? ", getBPartner().getC_BPartner_ID());
        if (locationID == -1) {
            throw new Exception("@NoVendorLocation@");
        }
        int docTypeID = getRetencionSchema().getRetencionCreditDocType();
        if (docTypeID > 0) credito_prov.setC_DocTypeTarget_ID(docTypeID); else credito_prov.setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APCreditMemo);
        credito_prov.setC_BPartner_ID(getBPartner().getC_BPartner_ID());
        credito_prov.setDateInvoiced(Env.getContextAsDate(Env.getCtx(), "#Date"));
        credito_prov.setC_Currency_ID(getCurrency().getC_Currency_ID());
        credito_prov.setIsSOTrx(isSOTrx());
        credito_prov.setDocStatus(MInvoice.DOCSTATUS_Drafted);
        credito_prov.setDocAction(MInvoice.DOCACTION_Complete);
        credito_prov.setC_BPartner_Location_ID(locationID);
        credito_prov.setCUIT(getBPartner().getTaxID());
        credito_prov.setPaymentRule(MInvoice.PAYMENTRULE_Check);
        if (getRetencionNumber() != null && !getRetencionNumber().trim().equals("")) credito_prov.setDocumentNo(getRetencionNumber());
        if (!credito_prov.save()) throw new Exception("@VendorRetencionDocSaveError@");
        MInvoiceLine cred_linea = new MInvoiceLine(Env.getCtx(), 0, getTrxName());
        cred_linea.setC_Invoice_ID(credito_prov.getC_Invoice_ID());
        cred_linea.setM_Product_ID(getRetencionSchema().getProduct());
        cred_linea.setLineNetAmt(getAmount());
        cred_linea.setC_Tax_ID(taxExenc);
        cred_linea.setLine(nrolinea);
        cred_linea.setQty(1);
        cred_linea.setPriceEntered(getAmount());
        cred_linea.setPriceActual(getAmount());
        if (!cred_linea.save()) throw new Exception("@VendorRetencionDocLineSaveError@");
        credito_prov.processIt(DocAction.ACTION_Complete);
        credito_prov.save();
        retencion.setC_InvoiceLine_ID(cred_linea.getC_InvoiceLine_ID());
        return credito_prov;
    }

    /**
	 * @return Returns the importeMinimoRetencion.
	 */
    public BigDecimal getImporteMinimoRetencion() {
        return importeMinimoRetencion;
    }

    /**
	 * @param importeMinimoRetencion The importeMinimoRetencion to set.
	 */
    protected void setImporteMinimoRetencion(BigDecimal importeMinimoRetencion) {
        this.importeMinimoRetencion = importeMinimoRetencion;
    }

    /**
	 * @param value The importeMinimoRetencion to set.
	 */
    protected void setDescuentoNeto(BigDecimal value) {
        this.descuentoNeto = value;
    }

    /**
	 * @return Returns the descuentoNeto.
	 */
    protected BigDecimal getDescuentoNeto() {
        return this.descuentoNeto;
    }

    /**
	 * @param value The importeMinimoRetencion to set.
	 */
    protected void setFromPadron(String value) {
        this.fromPadron = value;
    }

    /**
	 * @return Returns the descuentoNeto.
	 */
    protected String getFromPadron() {
        return this.fromPadron;
    }

    /**
	 * @return Returns the importeNoImponible.
	 */
    public BigDecimal getImporteNoImponible() {
        return importeNoImponible;
    }

    /**
	 * @param importeNoImponible The importeNoImponible to set.
	 */
    protected void setImporteNoImponible(BigDecimal importeNoImponible) {
        this.importeNoImponible = importeNoImponible;
    }

    /**
	 * @return Returns the pagosAnteriores.
	 */
    public BigDecimal getPagosAnteriores() {
        return pagosAnteriores;
    }

    /**
	 * @param pagosAnteriores The pagosAnteriores to set.
	 */
    protected void setPagosAnteriores(BigDecimal pagosAnteriores) {
        this.pagosAnteriores = pagosAnteriores;
    }

    /**
	 * @return Returns the porcentajeRetencion.
	 */
    public BigDecimal getPorcentajeRetencion() {
        return porcentajeRetencion;
    }

    /**
	 * @param porcentajeRetencion The porcentajeRetencion to set.
	 */
    protected void setPorcentajeRetencion(BigDecimal porcentajeRetencion) {
        this.porcentajeRetencion = porcentajeRetencion;
    }

    /**
	 * @return Returns the retencionesAnteriores.
	 */
    public BigDecimal getRetencionesAnteriores() {
        return retencionesAnteriores;
    }

    /**
	 * @param retencionesAnteriores The retencionesAnteriores to set.
	 */
    protected void setRetencionesAnteriores(BigDecimal retencionesAnteriores) {
        this.retencionesAnteriores = retencionesAnteriores;
    }

    /**
	 * @return Returns the baseImponible.
	 */
    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    /**
	 * @param baseImponible The baseImponible to set.
	 */
    protected void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    /**
	 * @return Returns the importeDeterminado.
	 */
    public BigDecimal getImporteDeterminado() {
        return importeDeterminado;
    }

    /**
	 * @param importeDeterminado The importeDeterminado to set.
	 */
    protected void setImporteDeterminado(BigDecimal importeDeterminado) {
        this.importeDeterminado = importeDeterminado;
    }
}
