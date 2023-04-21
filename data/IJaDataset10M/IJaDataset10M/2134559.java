package org.libertya.attributeSet.process;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import org.openXpertya.JasperReport.MJasperReport;
import org.openXpertya.model.MBPartner;
import org.openXpertya.model.MBPartnerLocation;
import org.openXpertya.model.MCategoriaIva;
import org.openXpertya.model.MCurrency;
import org.openXpertya.model.MInOut;
import org.openXpertya.model.MInvoice;
import org.openXpertya.model.MLocation;
import org.openXpertya.model.MOrder;
import org.openXpertya.model.MPaymentTerm;
import org.openXpertya.model.MProcess;
import org.openXpertya.model.MRefList;
import org.openXpertya.model.MRegion;
import org.openXpertya.model.MShipper;
import org.openXpertya.model.MUser;
import org.openXpertya.process.ProcessInfo;
import org.openXpertya.process.ProcessInfoParameter;
import org.openXpertya.process.SvrProcess;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

public class LaunchInOut extends SvrProcess {

    /** Jasper Report			*/
    private int AD_JasperReport_ID;

    /** Table					*/
    private int AD_Table_ID;

    /** Record					*/
    private int AD_Record_ID;

    /** Tipo de impresion		*/
    private String printType;

    @Override
    protected void prepare() {
        ProcessInfo base_pi = getProcessInfo();
        int AD_Process_ID = base_pi.getAD_Process_ID();
        MProcess proceso = MProcess.get(Env.getCtx(), AD_Process_ID);
        if (proceso.isJasperReport() != true) return;
        AD_JasperReport_ID = proceso.getAD_JasperReport_ID();
        AD_Table_ID = getTable_ID();
        AD_Record_ID = getRecord_ID();
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) ; else if (name.equalsIgnoreCase("TipoDeImpresion")) {
                printType = (String) para[i].getParameter();
            }
        }
    }

    @Override
    protected String doIt() throws Exception {
        return createReport();
    }

    private String createReport() throws Exception {
        MInOut inout = new MInOut(getCtx(), AD_Record_ID, null);
        MBPartner bpartner = new MBPartner(getCtx(), inout.getC_BPartner_ID(), null);
        MJasperReport jasperwrapper = new MJasperReport(getCtx(), AD_JasperReport_ID, get_TrxName());
        MJasperReport jasperASWrapper = getJasperReport("Detail_AS");
        DocumentAttributeSetDataSource asds = new InOutAttributeDataSource(inout, false, get_TrxName());
        InOutDataSource ds = new InOutDataSource(getCtx(), inout, get_TrxName());
        try {
            ds.loadData();
            asds.loadData();
        } catch (RuntimeException e) {
            throw new RuntimeException("No se pueden cargar los datos del informe", e);
        }
        MOrder order = null;
        if (inout.getC_Order_ID() > 0) order = new MOrder(getCtx(), inout.getC_Order_ID(), null);
        MBPartnerLocation BPLocation = new MBPartnerLocation(getCtx(), inout.getC_BPartner_Location_ID(), null);
        MLocation location = new MLocation(getCtx(), BPLocation.getC_Location_ID(), null);
        MRegion region = null;
        if (location.getC_Region_ID() > 0) region = new MRegion(getCtx(), location.getC_Region_ID(), null);
        jasperwrapper.addParameter("TIPOCOMPROBANTE", "REMITO");
        jasperwrapper.addParameter("FECHA", inout.getCreated());
        jasperwrapper.addParameter("RAZONSOCIAL", bpartner.getName());
        jasperwrapper.addParameter("CODIGO", bpartner.getValue());
        jasperwrapper.addParameter("DIRECCION", location.getAddress1() + ". " + location.getCity() + ". (" + location.getPostal() + "). " + region.getName());
        jasperwrapper.addParameter("TIPO_IVA", coalesce((new MCategoriaIva(getCtx(), bpartner.getC_Categoria_Iva_ID(), null)).getName(), ""));
        jasperwrapper.addParameter("CUIT", bpartner.getTaxID());
        jasperwrapper.addParameter("INGBRUTO", bpartner.getIIBB());
        jasperwrapper.addParameter("VENDEDOR", coalesce((new MUser(getCtx(), inout.getSalesRep_ID(), null).getName()), ""));
        jasperwrapper.addParameter("NRODOCORIG", coalesce(inout.getPOReference(), ""));
        jasperwrapper.addParameter("NRO_OC", order == null ? "" : coalesce(inout.getPOReference(), ""));
        jasperwrapper.addParameter("VCTO", "Vencimiento: " + (order == null || inout.getDateOrdered() == null ? "" : (inout.getDateOrdered().toString().substring(0, 11))));
        jasperwrapper.addParameter("NROREMITO", inout.getDocumentNo());
        jasperwrapper.addParameter("TIPOORIGEN", "ORIGINAL");
        jasperwrapper.addParameter("TOTAL", order == null ? new BigDecimal(0) : order.getGrandTotal());
        jasperwrapper.addParameter("CODVTA", order == null ? "" : MRefList.getListName(getCtx(), MInvoice.PAYMENTRULE_AD_Reference_ID, order.getPaymentRule()) + "-" + (new MPaymentTerm(getCtx(), order.getC_PaymentTerm_ID(), null)).getName());
        jasperwrapper.addParameter("NROCOMPROBANTE", "");
        if (inout.getC_Invoice_ID() > 0) {
            MInvoice invoice = new MInvoice(getCtx(), inout.getC_Invoice_ID(), null);
            jasperwrapper.addParameter("NROCOMPROBANTE", invoice.getDocumentNo());
        }
        jasperwrapper.addParameter("TRANSPORTISTA", getTransportistaData(inout));
        if (inout.getC_Currency_ID() > 0) jasperwrapper.addParameter("MONEDA", (MCurrency.get(getCtx(), inout.getC_Currency_ID())).getISO_Code());
        jasperwrapper.addParameter("TIPOORIGEN", printType);
        jasperwrapper.addParameter("COMPILED_SUBREPORT_ATTRIBUTESETS", new ByteArrayInputStream(jasperASWrapper.getBinaryData()));
        jasperwrapper.addParameter("SUBREPORT_ATTRIBUTESETS_DATASOURCE", asds);
        boolean existLinesWithoutAS = ds.total_lines > 0;
        boolean existLinesWithAS = asds.getRecordsCount() > 0;
        jasperwrapper.addParameter("EXISTLINESWITHOUTAS", existLinesWithoutAS);
        jasperwrapper.addParameter("EXISTLINESWITHAS", existLinesWithAS);
        jasperwrapper.addParameter("SHOW_PRICE", false);
        if (!existLinesWithoutAS && existLinesWithAS) {
            ds.setForceDetail();
        }
        try {
            jasperwrapper.fillReport(ds);
            jasperwrapper.showReport(getProcessInfo());
        } catch (RuntimeException e) {
            throw new RuntimeException("No se ha podido rellenar el informe.", e);
        }
        return "doIt";
    }

    public static Object coalesce(Object object, Object defValue) {
        if (object == null) return defValue;
        return object;
    }

    private String getTransportistaData(MInOut inout) {
        String res = "";
        MShipper shipper = null;
        MBPartner shipperBP = null;
        MLocation location = null;
        if (inout.getM_Shipper_ID() > 0) {
            shipper = new MShipper(getCtx(), inout.getM_Shipper_ID(), null);
            res += shipper.getName() + ". ";
            if (shipper.getC_BPartner_ID() > 0) {
                shipperBP = new MBPartner(getCtx(), shipper.getC_BPartner_ID(), null);
                MBPartnerLocation BPLocation = (MBPartnerLocation.getForBPartner(getCtx(), shipper.getC_BPartner_ID()))[0];
                if (BPLocation != null) {
                    location = new MLocation(getCtx(), BPLocation.getC_Location_ID(), null);
                    res += coalesce(location.getAddress1(), "") + ". " + coalesce(location.getRegionName(), "") + ". ";
                }
                res += coalesce(shipperBP.getTaxID(), "");
            }
        }
        return res;
    }

    /**
	 * @return Retorna el MJasperReport con el nombre indicado.
	 */
    private MJasperReport getJasperReport(String name) throws Exception {
        Integer jasperReport_ID = (Integer) DB.getSQLObject(get_TrxName(), "SELECT AD_JasperReport_ID FROM AD_JasperReport WHERE Name ilike ?", new Object[] { name });
        if (jasperReport_ID == null || jasperReport_ID == 0) throw new Exception("Jasper Report not found - " + name);
        MJasperReport jasperReport = new MJasperReport(getCtx(), jasperReport_ID, get_TrxName());
        return jasperReport;
    }
}
