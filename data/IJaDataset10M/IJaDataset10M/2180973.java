package pe.com.bn.sach.mantenimiento.controller;

import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import pe.com.bn.sach.common.Util;
import pe.com.bn.sach.controller.BaseController;
import pe.com.bn.sach.domain.Bnchf14CentroHipotecario;
import pe.com.bn.sach.domain.Bnchf20Producto;
import pe.com.bn.sach.domain.Bnchf54ParamDocumentTitular;
import pe.com.bn.sach.domain.Bnchf54ParamDocumentTitularId;
import pe.com.bn.sach.domain.Bnchf54ParamReqsDesgrav;
import pe.com.bn.sach.domain.Bnchf54ParamReqsDesgravId;
import pe.com.bn.sach.domain.Bnchf54ParamReqsFam;
import pe.com.bn.sach.domain.Bnchf54ParamReqsFamId;
import pe.com.bn.sach.domain.Bnchf54ParamReqsTit;
import pe.com.bn.sach.domain.Bnchf54ParamReqsTitId;
import pe.com.bn.sach.domain.Bnchf54ParamTasaInteres;
import pe.com.bn.sach.domain.Bnchf54ParamTasaInteresId;
import pe.com.bn.sach.domain.Bnchf54ParametroProducto;
import pe.com.bn.sach.domain.Bnchf54ParametroProductoId;
import pe.com.bn.sach.domain.BnchfCatalogoProducto;
import pe.com.bn.sach.domain.BnchfDesgravamen;
import pe.com.bn.sach.domain.BnchfTarProd;
import pe.com.bn.sach.domain.BnchfTarifa;
import pe.com.bn.sach.domain.BnchfTasaInteres;
import pe.com.bn.sach.domain.MantenimientoHost;
import pe.com.bn.sach.mantenimiento.form.ParametroFinancieroForm;
import pe.com.bn.sach.seguridad.DatosSesion;
import pe.com.bn.sach.service.MantenimientoService;
import pe.com.bn.sach.service.ParametroService;
import pe.com.bn.sach.service.SelectService;

/**
 * @author ce_dpcreditos04
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class ParametroFinancieroController extends BaseController {

    private String listar;

    MantenimientoHost MantenimientoHost = new MantenimientoHost();

    private MantenimientoService servicioMantenimiento;

    private SelectService servicioSelect;

    private String commandName;

    private Class commandClass;

    private ParametroService parametroService;

    private MantenimientoService mantenimientoService;

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public ParametroFinancieroController() {
        ParameterMethodNameResolver resolver = new ParameterMethodNameResolver();
        resolver.setParamName("method");
        resolver.setDefaultMethodName("cargaListaParametroFinanciero");
        setMethodNameResolver(resolver);
    }

    public MantenimientoHost formBuscarInit(HttpServletRequest request) {
        MantenimientoHost MantenimientoHost = new MantenimientoHost();
        MantenimientoHost.setF06CodCentroHip(Util.copyStringc("0"));
        MantenimientoHost.setF06CodTarifa(null);
        return MantenimientoHost;
    }

    public MantenimientoHost formBuscar(HttpServletRequest request) {
        MantenimientoHost MantenimientoHost = new MantenimientoHost();
        MantenimientoHost.setF06CodCentroHip(Util.copyStringc(request.getParameter("txtCodigoParametroFinanciero")));
        MantenimientoHost.setF06CodCentroHip("" + request.getParameter("selCentroHipotecario"));
        MantenimientoHost.setF06CodTarifa("" + request.getParameter("txtCodTarifa"));
        return MantenimientoHost;
    }

    MantenimientoHost getForm(ParametroFinancieroForm institucionForm) {
        MantenimientoHost MantenimientoHost = new MantenimientoHost();
        MantenimientoHost.setF06CodTarifa(Util.copyString("" + institucionForm.getTxtCodTarifa()));
        MantenimientoHost.setF06DesTarifa(Util.copyString("" + institucionForm.getTxtDesTarifa()));
        MantenimientoHost.setF06CodCentroHip(Util.copyString("" + institucionForm.getSelCentroHipotecario()));
        MantenimientoHost.setF06SegInmbl(Util.copyDoublec("" + institucionForm.getTxtSegInmbl()));
        MantenimientoHost.setF06Portes(Util.copyDoublec("" + institucionForm.getTxtPortes()));
        MantenimientoHost.setF06MtoComiEval(Util.copyDoublec("" + institucionForm.getTxtMntoComiEval()));
        MantenimientoHost.setF06CostoChqGer(Util.copyDoublec("" + institucionForm.getTxtCostoChqGer()));
        MantenimientoHost.setF06Itf(Util.copyDoublec("" + institucionForm.getTxtItf()));
        return MantenimientoHost;
    }

    public ModelAndView consultaParametrosFinancieros(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/parametroProducto/par_parametroFinanciero");
        String idProducto = request.getParameter("idProducto");
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        bnchf20Producto = parametroService.obtenerProducto(bnchf20Producto);
        List listCentroHipotecario = mantenimientoService.listaCentroHipotecarioActivos();
        mav.addObject("idProducto", idProducto);
        mav.addObject("bnchf20Producto", bnchf20Producto);
        mav.addObject("listCentroHipotecario", listCentroHipotecario);
        return mav;
    }

    public ModelAndView detalleParametrosFinancieros(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection cnx = null;
        try {
            Object command = getCommandObject(request);
            ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
            institucionForm = (ParametroFinancieroForm) command;
            String idProducto = request.getParameter("idProducto");
            String idCH = request.getParameter("idCH");
            String idNroSecHost = "";
            String idProductoHost = "";
            String idCHHost = "";
            if (idProducto.trim().length() >= 3) {
                idNroSecHost = Util.cortar(idProducto, 2, false);
                idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
            } else {
                response.setHeader("responde", "true");
                response.setHeader("result", "El producto no es v�lido");
                response.setHeader("tipo", "1");
                return null;
            }
            idCHHost = Util.autocompletar(idCH, '0', 6, false);
            Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
            Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
            Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
            bnchf14CentroHipotecario.setF14IdHipotecario(new Long(idCH));
            Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
            bnchf20Producto.setF20IdProducto(new Long(idProducto));
            bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
            bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
            bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
            bnchf54ParametroProducto = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
            BnchfCatalogoProducto bnchfCatalogoProducto = new BnchfCatalogoProducto();
            bnchfCatalogoProducto.setF01CodCentroHipotec(idCHHost);
            bnchfCatalogoProducto.setF01NroSecuencia(Util.parseLong(idNroSecHost));
            bnchfCatalogoProducto.setF01CodProd(idProductoHost);
            cnx = getPoolConnection();
            bnchfCatalogoProducto = parametroService.obtenerCatalogoProducto(bnchfCatalogoProducto, cnx);
            Util.JSON json = new Util.JSON();
            boolean inconsistencia = false;
            if (bnchf54ParametroProducto != null && bnchfCatalogoProducto != null) {
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54PrctFinanc()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01PrcntjFincmto()).doubleValue())) inconsistencia = true;
                json.put("PRCTFINANC", bnchf54ParametroProducto.getF54PrctFinanc(), 2);
                json.put("PRCTFINANC_VAL", bnchfCatalogoProducto.getF01PrcntjFincmto(), 2);
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54MntoFinancMin()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01MontoMin()).doubleValue())) inconsistencia = true;
                json.put("MNTOFINANCMIN", bnchf54ParametroProducto.getF54MntoFinancMin(), 2);
                json.put("MNTOFINANCMIN_VAL", bnchfCatalogoProducto.getF01MontoMin(), 2);
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54MntoFinancMax()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01MontoMax()).doubleValue())) inconsistencia = true;
                json.put("MNTOFINANCMAX", bnchf54ParametroProducto.getF54MntoFinancMax(), 2);
                json.put("MNTOFINANCMAX_VAL", bnchfCatalogoProducto.getF01MontoMax(), 2);
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54RatCredGarant()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01RatioRiesgo()).doubleValue())) inconsistencia = true;
                json.put("RATCREDGARANT", bnchf54ParametroProducto.getF54RatCredGarant(), 2);
                json.put("RATCREDGARANT_VAL", bnchfCatalogoProducto.getF01RatioRiesgo(), 2);
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54RatCuotaIngrNeto()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01RatCuotaIngNet()).doubleValue())) inconsistencia = true;
                json.put("RATCUOTAINGRNETO", bnchf54ParametroProducto.getF54RatCuotaIngrNeto(), 2);
                json.put("RATCUOTAINGRNETO_VAL", bnchfCatalogoProducto.getF01RatCuotaIngNet(), 2);
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54RatCuotaIngrBn()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01RatCuotaIngBn()).doubleValue())) inconsistencia = true;
                json.put("RATCUOTAINGRBN", bnchf54ParametroProducto.getF54RatCuotaIngrBn(), 2);
                json.put("RATCUOTAINGRBN_VAL", bnchfCatalogoProducto.getF01RatCuotaIngBn(), 2);
                if (!(Util.parseLong(bnchf54ParametroProducto.getF54PlazoMin()).longValue() == Util.parseLong(bnchfCatalogoProducto.getF01PlazoMin()).longValue())) inconsistencia = true;
                json.put("PLAZOMIN", bnchf54ParametroProducto.getF54PlazoMin());
                json.put("PLAZOMIN_VAL", bnchfCatalogoProducto.getF01PlazoMin());
                if (!(Util.parseLong(bnchf54ParametroProducto.getF54PlazoMax()).longValue() == Util.parseLong(bnchfCatalogoProducto.getF01PlazoMax()).longValue())) inconsistencia = true;
                json.put("PLAZOMAX", bnchf54ParametroProducto.getF54PlazoMax());
                json.put("PLAZOMAX_VAL", bnchfCatalogoProducto.getF01PlazoMax());
                if (!(Util.parseLong(bnchf54ParametroProducto.getF54PerGracia()).longValue() == Util.parseLong(bnchfCatalogoProducto.getF01PeriodoGracia()).longValue())) inconsistencia = true;
                json.put("PERGRACIA", bnchf54ParametroProducto.getF54PerGracia());
                json.put("PERGRACIA_VAL", bnchfCatalogoProducto.getF01PeriodoGracia());
                json.put("PLAZODIASPAGO", bnchfCatalogoProducto.getF01PlazoDiasPago());
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54MntoMinPrepago()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01MontoMinPrpg()).doubleValue())) inconsistencia = true;
                json.put("MNTOMINPREPAGO", bnchf54ParametroProducto.getF54MntoMinPrepago(), 2);
                json.put("MNTOMINPREPAGO_VAL", bnchfCatalogoProducto.getF01MontoMinPrpg(), 2);
                if (!(Util.parseLong(bnchf54ParametroProducto.getF54AcepPrepago()).longValue() == Util.parseLong(Util.replaceSi(bnchfCatalogoProducto.getF01PrepagoI(), "S", 1, 2)).longValue())) inconsistencia = true;
                json.put("PREPAGOI", Util.replaceSi(Util.parseLong(bnchf54ParametroProducto.getF54AcepPrepago()).toString(), 1, "S", "N"));
                json.put("PREPAGOI_VAL", bnchfCatalogoProducto.getF01PrepagoI());
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54PorctPenalPrepago()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01Penalidad()).doubleValue())) inconsistencia = true;
                json.put("PENALPREPAGO", bnchf54ParametroProducto.getF54PorctPenalPrepago(), 2);
                json.put("PENALPREPAGO_VAL", bnchfCatalogoProducto.getF01Penalidad(), 2);
                json.put("PERDONAINSTRSI", bnchfCatalogoProducto.getF01PerdonaIntrsI());
                json.put("PARTTIT ", bnchf54ParametroProducto.getF54PartTit(), 2);
                json.put("PARTFAM1", bnchf54ParametroProducto.getF54PartFam1(), 2);
                json.put("PARTFAM2", bnchf54ParametroProducto.getF54PartFam2(), 2);
                json.put("PARTFAMTIT", bnchf54ParametroProducto.getF54PartFamTit(), 2);
                json.put("NROCUOTA", bnchf54ParametroProducto.getF54NroCuota());
                json.put("FINCSEGPERGRAC", bnchf54ParametroProducto.getF54FincSegPerGrac());
                json.put("RNGOEDADDESGRAV", bnchf54ParametroProducto.getF54RngoEdadDesgrav());
                json.put("IDTARIFA", bnchf54ParametroProducto.getF54IdTarifa());
                json.put("TIMESTAMP", bnchf54ParametroProducto.getF54IdTimestamp());
                json.put("MNTOMINAPROB", bnchf54ParametroProducto.getF54MntoMinAprob(), 2);
                if (!(Util.parseLong(bnchf54ParametroProducto.getF54AcepCancAnti()).longValue() == Util.parseLong(Util.replaceSi(bnchfCatalogoProducto.getF01AceptCancAnti(), "S", 1, 2)).longValue())) inconsistencia = true;
                json.put("ACEPCANCANTI", Util.replaceSi(Util.parseLong(bnchf54ParametroProducto.getF54AcepCancAnti()).toString(), 1, "S", "N"));
                json.put("ACEPCANCANTI_VAL", bnchfCatalogoProducto.getF01AceptCancAnti());
                if (!(Util.parseDouble(bnchf54ParametroProducto.getF54MntoPenalCancAnti()).doubleValue() == Util.parseDouble(bnchfCatalogoProducto.getF01MntoPenalCancAnti()).doubleValue())) inconsistencia = true;
                json.put("MNTOPENALCANCANTI", bnchf54ParametroProducto.getF54MntoPenalCancAnti(), 2);
                json.put("MNTOPENALCANCANTI_VAL", bnchfCatalogoProducto.getF01MntoPenalCancAnti(), 2);
            } else if (bnchf54ParametroProducto == null && bnchfCatalogoProducto != null) {
                json.put("PRCTFINANC", bnchfCatalogoProducto.getF01PrcntjFincmto(), 2);
                json.put("MNTOFINANCMIN ", bnchfCatalogoProducto.getF01MontoMin(), 2);
                json.put("MNTOFINANCMAX", bnchfCatalogoProducto.getF01MontoMax(), 2);
                json.put("RATCREDGARANT", bnchfCatalogoProducto.getF01RatioRiesgo(), 2);
                json.put("RATCUOTAINGRNETO", bnchfCatalogoProducto.getF01RatCuotaIngNet(), 2);
                json.put("RATCUOTAINGRBN", bnchfCatalogoProducto.getF01RatCuotaIngBn(), 2);
                json.put("PLAZOMIN", bnchfCatalogoProducto.getF01PlazoMin());
                json.put("PLAZOMAX", bnchfCatalogoProducto.getF01PlazoMax());
                json.put("PERGRACIA", bnchfCatalogoProducto.getF01PeriodoGracia());
                json.put("PLAZODIASPAGO", bnchfCatalogoProducto.getF01PlazoDiasPago());
                json.put("MNTOMINPREPAGO", bnchfCatalogoProducto.getF01MontoMinPrpg(), 2);
                json.put("PREPAGOI", bnchfCatalogoProducto.getF01PrepagoI());
                json.put("PENALPREPAGO", bnchfCatalogoProducto.getF01Penalidad(), 2);
                json.put("PERDONAINSTRSI", bnchfCatalogoProducto.getF01PerdonaIntrsI());
                json.put("ACEPCANCANTI", bnchfCatalogoProducto.getF01AceptCancAnti());
                json.put("MNTOPENALCANCANTI", bnchfCatalogoProducto.getF01MntoPenalCancAnti(), 2);
                inconsistencia = true;
            } else if (bnchf54ParametroProducto != null && bnchfCatalogoProducto == null) {
                json.put("PRCTFINANC", bnchf54ParametroProducto.getF54PrctFinanc(), 2);
                json.put("MNTOFINANCMIN ", bnchf54ParametroProducto.getF54MntoFinancMin(), 2);
                json.put("MNTOFINANCMAX", bnchf54ParametroProducto.getF54MntoFinancMax(), 2);
                json.put("RATCREDGARANT", bnchf54ParametroProducto.getF54RatCredGarant(), 2);
                json.put("RATCUOTAINGRNETO", bnchf54ParametroProducto.getF54RatCuotaIngrNeto(), 2);
                json.put("RATCUOTAINGRBN", bnchf54ParametroProducto.getF54RatCuotaIngrBn(), 2);
                json.put("PLAZOMIN", bnchf54ParametroProducto.getF54PlazoMin());
                json.put("PLAZOMAX", bnchf54ParametroProducto.getF54PlazoMax());
                json.put("PERGRACIA", bnchf54ParametroProducto.getF54PerGracia());
                json.put("MNTOMINPREPAGO", bnchf54ParametroProducto.getF54MntoMinPrepago(), 2);
                json.put("PREPAGOI", Util.replaceSi(Util.parseLong(bnchf54ParametroProducto.getF54AcepPrepago()).toString(), 1, "S", "N"));
                json.put("PENALPREPAGO", bnchf54ParametroProducto.getF54PorctPenalPrepago(), 2);
                json.put("PARTTIT ", bnchf54ParametroProducto.getF54PartTit(), 2);
                json.put("PARTFAM1", bnchf54ParametroProducto.getF54PartFam1(), 2);
                json.put("PARTFAM2", bnchf54ParametroProducto.getF54PartFam2(), 2);
                json.put("PARTFAMTIT", bnchf54ParametroProducto.getF54PartFamTit(), 2);
                json.put("NROCUOTA", bnchf54ParametroProducto.getF54NroCuota());
                json.put("FINCSEGPERGRAC", bnchf54ParametroProducto.getF54FincSegPerGrac());
                json.put("RNGOEDADDESGRAV", bnchf54ParametroProducto.getF54RngoEdadDesgrav());
                json.put("IDTARIFA", bnchf54ParametroProducto.getF54IdTarifa());
                json.put("TIMESTAMP", bnchf54ParametroProducto.getF54IdTimestamp());
                json.put("MNTOMINAPROB", bnchf54ParametroProducto.getF54MntoMinAprob(), 2);
                json.put("ACEPCANCANTI", Util.replaceSi(Util.parseLong(bnchf54ParametroProducto.getF54AcepCancAnti()).toString(), 1, "S", "N"));
                json.put("MNTOPENALCANCANTI", bnchf54ParametroProducto.getF54MntoPenalCancAnti(), 2);
                inconsistencia = true;
            } else {
            }
            idCHHost = Util.autocompletar(idCH, '0', 6, false);
            BnchfTarifa bnchfTarifa = new BnchfTarifa();
            bnchfTarifa.setF06CodCentroHip(idCHHost);
            List tarifa = parametroService.obtenerTarifas(bnchfTarifa, cnx);
            if (cnx != null) freePoolConnection(cnx);
            Util.JSONArray array = new Util.JSONArray();
            BnchfTarifa x = new BnchfTarifa();
            if (tarifa != null) {
                Iterator s = tarifa.iterator();
                List tarifaFiltrado = new ArrayList();
                while (s.hasNext()) {
                    BnchfTarifa t = (BnchfTarifa) s.next();
                    if (bnchf54ParametroProducto != null) {
                        x.setF06CodTarifa(bnchf54ParametroProducto.getF54IdTarifa());
                        x.setF06Timestamp(bnchf54ParametroProducto.getF54IdTimestamp());
                        if (t.getF06CodTarifa().equals(x.getF06CodTarifa()) && t.getF06Timestamp().equals(x.getF06Timestamp())) {
                            x.setF06DesTarifa(t.getF06DesTarifa());
                        }
                    }
                    if (existe(t.getF06CodTarifa(), tarifaFiltrado) == false) {
                        tarifaFiltrado.add(t);
                    }
                }
                if (bnchf54ParametroProducto != null) {
                    if (!existe_seleccionado(bnchf54ParametroProducto.getF54IdTarifa(), bnchf54ParametroProducto.getF54IdTimestamp(), tarifaFiltrado)) {
                        BnchfTarifa t = new BnchfTarifa();
                        t.setF06CodTarifa(bnchf54ParametroProducto.getF54IdTarifa());
                        t.setF06Timestamp(bnchf54ParametroProducto.getF54IdTimestamp());
                        t.setF06DesTarifa(x.getF06DesTarifa());
                        tarifaFiltrado.add(t);
                    }
                }
                s = tarifaFiltrado.iterator();
                while (s.hasNext()) {
                    BnchfTarifa t = (BnchfTarifa) s.next();
                    Util.JSON jsonItem = new Util.JSON();
                    boolean selected = false;
                    if (bnchf54ParametroProducto != null) {
                        if ((bnchf54ParametroProducto.getF54IdTarifa() + "|" + bnchf54ParametroProducto.getF54IdTimestamp()).equals(Util.parseString(t.getF06CodTarifa()) + "|" + Util.parseString(t.getF06Timestamp()))) {
                            selected = true;
                        }
                    }
                    jsonItem.put("value", Util.parseString(t.getF06CodTarifa()) + "|" + Util.parseString(t.getF06Timestamp()));
                    jsonItem.put("text", Util.parseString(t.getF06CodTarifa()) + "|" + Util.parseString(t.getF06Timestamp()) + "|" + Util.parseString(t.getF06DesTarifa()));
                    if (selected) jsonItem.put("selected", "true");
                    array.put(jsonItem);
                }
            }
            response.setHeader("responde", "true");
            if (json != null) response.setHeader("X-JSON", json.getFormat());
            if (array != null) response.setHeader("Tarifas", array.getFormat());
            response.setHeader("inconsistencia", String.valueOf(inconsistencia));
            response.setHeader("Cache-Control", "no-cache");
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("mensaje", e.toString());
        } finally {
            if (cnx != null) freePoolConnection(cnx);
        }
        return null;
    }

    public boolean existe(String CT, List en) {
        boolean result = false;
        try {
            Iterator i = en.iterator();
            while (i.hasNext()) {
                BnchfTarifa item = (BnchfTarifa) i.next();
                if (item.getF06CodTarifa().trim().equals(CT.trim())) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean existe_seleccionado(String CT, String TS, List en) {
        boolean result = false;
        try {
            Iterator i = en.iterator();
            while (i.hasNext()) {
                BnchfTarifa item = (BnchfTarifa) i.next();
                if (item.getF06CodTarifa().trim().equals(CT.trim()) && item.getF06Timestamp().trim().equals(TS.trim())) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ModelAndView guardarParametrosFinancieros(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        String idTarifa = request.getParameter("lstTarifario");
        String idTimestamp = request.getParameter("lstTarifario");
        String idNroSecHost = "";
        String idProductoHost = "";
        if (idProducto.trim().length() >= 3) {
            idNroSecHost = Util.cortar(idProducto, 2, false);
            idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
        } else {
            response.setHeader("responde", "true");
            response.setHeader("result", "El producto no es v�lido");
            response.setHeader("tipo", "1");
            return null;
        }
        if (Util.parseString(idTarifa).length() > 0) {
            int i = idTarifa.indexOf("|");
            idTimestamp = idTarifa.substring(i + 1, idTarifa.length());
            idTarifa = idTarifa.substring(0, i);
        }
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(new Long(idProducto));
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(new Long(idCH));
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        bnchf54ParametroProducto.setF54IdTarifa(idTarifa);
        bnchf54ParametroProducto.setF54IdTimestamp(idTimestamp);
        bnchf54ParametroProducto.setF54Stdo(new Long(1));
        bnchf54ParametroProducto.setF54IdUsuaCrea(datosSesion.getCodigoHost());
        bnchf54ParametroProducto.setF54IdUsuaModi(datosSesion.getCodigoHost());
        BnchfCatalogoProducto bnchfCatalogoProducto = new BnchfCatalogoProducto();
        bnchfCatalogoProducto.setF01CodCentroHipotec(idCHHost);
        bnchfCatalogoProducto.setF01CodProd(idProductoHost);
        bnchfCatalogoProducto.setF01NroSecuencia(Util.parseLong(idNroSecHost));
        bnchfCatalogoProducto.setF01CodEstado("00");
        bnchfCatalogoProducto.setF01CodUsuario(Util.parseLong(datosSesion.getCodigoUsuario()));
        bnchfCatalogoProducto.setF01Timestamp(idTimestamp);
        BnchfTarProd bnchfTarProd = new BnchfTarProd();
        bnchfTarProd.setF08CodCentroHip(idCHHost);
        bnchfTarProd.setF08CodProd(idProductoHost);
        bnchfTarProd.setF08NroSecuencia(Util.parseLong(idNroSecHost));
        bnchfTarProd.setF08Timestamp(idTimestamp);
        bnchfTarProd.setF08CodTarifa(idTarifa);
        bnchfTarProd.setF08CodUsuario(datosSesion.getCodigoHost());
        bnchf54ParametroProducto.setF54PrctFinanc(Util.parseDouble(request.getParameter("txtPrctFinanc")));
        bnchf54ParametroProducto.setF54MntoFinancMin(Util.parseDouble(request.getParameter("txtMntoFinancMin")));
        bnchf54ParametroProducto.setF54MntoFinancMax(Util.parseDouble(request.getParameter("txtMntoFinancMax")));
        bnchf54ParametroProducto.setF54RatCredGarant(Util.parseDouble(request.getParameter("txtRatCredGarant")));
        bnchf54ParametroProducto.setF54RatCuotaIngrNeto(Util.parseDouble(request.getParameter("txtRatCuotaIngrNeto")));
        bnchf54ParametroProducto.setF54RatCuotaIngrBn(Util.parseDouble(request.getParameter("txtRatCuotaIngrBn")));
        bnchf54ParametroProducto.setF54PlazoMin(Util.parseLong(request.getParameter("txtPlazoMin")));
        bnchf54ParametroProducto.setF54PlazoMax(Util.parseLong(request.getParameter("txtPlazoMax")));
        bnchf54ParametroProducto.setF54PerGracia(Util.parseLong(request.getParameter("txtPerGracia")));
        bnchfCatalogoProducto.setF01PrcntjFincmto(Util.parseDouble(request.getParameter("txtPrctFinanc")));
        bnchfCatalogoProducto.setF01MontoMin(Util.parseDouble(request.getParameter("txtMntoFinancMin")));
        bnchfCatalogoProducto.setF01MontoMax(Util.parseDouble(request.getParameter("txtMntoFinancMax")));
        bnchfCatalogoProducto.setF01RatioRiesgo(Util.parseDouble(request.getParameter("txtRatCredGarant")));
        bnchfCatalogoProducto.setF01RatCuotaIngNet(Util.parseDouble(request.getParameter("txtRatCuotaIngrNeto")));
        bnchfCatalogoProducto.setF01RatCuotaIngBn(Util.parseDouble(request.getParameter("txtRatCuotaIngrBn")));
        bnchfCatalogoProducto.setF01PlazoMin(Util.parseLong(request.getParameter("txtPlazoMin")));
        bnchfCatalogoProducto.setF01PlazoMax(Util.parseLong(request.getParameter("txtPlazoMax")));
        bnchfCatalogoProducto.setF01PeriodoGracia(Util.parseLong(request.getParameter("txtPerGracia")));
        bnchfCatalogoProducto.setF01PlazoDiasPago(Util.parseLong(request.getParameter("txtPlazoDiasPago")));
        bnchf54ParametroProducto.setF54MntoMinPrepago(Util.parseDouble(request.getParameter("txtMntoMinPrepago")));
        bnchf54ParametroProducto.setF54AcepPrepago(Util.parseLong(Util.replaceSi(request.getParameter("txtAcepPrepago"), "S", "1", "2")));
        bnchf54ParametroProducto.setF54PorctPenalPrepago(Util.parseDouble(request.getParameter("txtPenalPrepago")));
        bnchfCatalogoProducto.setF01MontoMinPrpg(Util.parseDouble(request.getParameter("txtMntoMinPrepago")));
        bnchfCatalogoProducto.setF01PrepagoI(Util.replaceSi(request.getParameter("txtAcepPrepago"), "S", "S", "N"));
        bnchfCatalogoProducto.setF01Penalidad(Util.parseDouble(request.getParameter("txtPenalPrepago")));
        bnchfCatalogoProducto.setF01PerdonaIntrsI(Util.replaceSi(request.getParameter("txtPerdonaIntrsI"), "S", "S", "N"));
        bnchf54ParametroProducto.setF54PartTit(Util.parseDouble(request.getParameter("txtPartTtt")));
        bnchf54ParametroProducto.setF54PartFam1(Util.parseDouble(request.getParameter("txtPartFam1")));
        bnchf54ParametroProducto.setF54PartFam2(Util.parseDouble(request.getParameter("txtPartFam2")));
        bnchf54ParametroProducto.setF54PartFamTit(Util.parseDouble(request.getParameter("txtPartFamTtt")));
        bnchf54ParametroProducto.setF54NroCuota(new Long(12));
        bnchf54ParametroProducto.setF54FincSegPerGrac(new Long(2));
        bnchf54ParametroProducto.setF54RngoEdadDesgrav(Util.parseLong(request.getParameter("txtRngoEdadDesgrav")));
        bnchf54ParametroProducto.setF54PorctSegInmueb(Util.parseDouble(request.getParameter("txtPorctSegInmueb")));
        bnchf54ParametroProducto.setF54MntoPortes(Util.parseDouble(request.getParameter("txtMntoPortes")));
        bnchf54ParametroProducto.setF54MntoComisEval(Util.parseDouble(request.getParameter("txtMntoComisEval")));
        bnchf54ParametroProducto.setF54MntoMinAprob(Util.parseDouble(request.getParameter("txtMntoMinAprob")));
        bnchf54ParametroProducto.setF54AcepCancAnti(Util.replaceSi(request.getParameter("txtAcepCancAnti"), "S", 1, 2));
        bnchf54ParametroProducto.setF54MntoPenalCancAnti(Util.parseDouble(request.getParameter("txtMntoPenalCancAnti")));
        bnchfCatalogoProducto.setF01AceptCancAnti(Util.replaceSi(request.getParameter("txtAcepCancAnti"), "S", "S", "N"));
        bnchfCatalogoProducto.setF01MntoPenalCancAnti(Util.parseDouble(request.getParameter("txtMntoPenalCancAnti")));
        bnchf54ParametroProducto.setF54IdUsuaCrea(datosSesion.getCodigoHost());
        Connection cnx = null;
        try {
            cnx = getPoolConnection();
            if (parametroService.obtenerCatalogoProducto(bnchfCatalogoProducto, cnx) == null) {
                parametroService.guardarCatalogoProducto(bnchfCatalogoProducto, cnx);
            } else {
                parametroService.actualizarCatalogoProducto(bnchfCatalogoProducto, cnx);
            }
            if (parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto) != null) {
                parametroService.actualizaParametroFinancieroOpen(bnchf54ParametroProducto);
            } else {
                parametroService.guardarParametroFinancieroOpen(bnchf54ParametroProducto);
            }
            if (parametroService.obtenerTarifaProducto(bnchfTarProd, cnx) == null) {
                parametroService.insertTarifaProducto(bnchfTarProd, cnx);
            } else {
                parametroService.updateTarifaProducto(bnchfTarProd, cnx);
            }
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
            String mensaje = actualizarDesgravamentDesdeHost(bnchf54ParametroProducto, datosSesion);
            if (mensaje == null) response.setHeader("result", "Los cambios se guardaron satisfactoriamente");
            if (mensaje != null) response.setHeader("result", "Ocurri� un problema al intentar actualizar datos del Desgramen. Sin embargo, Los parametros Financieros han sido guardados.");
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("result", "Ocurri� un problema al guardar los datos.");
            response.setHeader("tipo", "1");
        } finally {
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
        }
        response.setHeader("responde", "true");
        return null;
    }

    public ModelAndView consultaTarifa(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idTarifario = request.getParameter("idTarifario");
        String idCH = request.getParameter("idCH");
        String idCHHost = "";
        idCHHost = Util.autocompletar(idCH, '0', 6, false);
        int i = idTarifario.indexOf("|");
        String idTimestamp = idTarifario.substring(i + 1, idTarifario.length());
        idTarifario = idTarifario.substring(0, i);
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Connection cnx = null;
        Util.JSON json = new Util.JSON();
        try {
            BnchfTarifa bnchfTarifa = new BnchfTarifa();
            bnchfTarifa.setF06CodCentroHip(idCHHost);
            bnchfTarifa.setF06CodTarifa(idTarifario);
            bnchfTarifa.setF06Timestamp(idTimestamp);
            cnx = getPoolConnection();
            bnchfTarifa = parametroService.obtenerTarifa(bnchfTarifa, cnx);
            if (cnx != null) freePoolConnection(cnx);
            json.put("PORCTSEGINMUEB", bnchfTarifa.getF06SegInmbl(), 4);
            json.put("MNTOCOMISEVAL", bnchfTarifa.getF06MtoComiEval(), 2);
            json.put("MNTOPORTES", bnchfTarifa.getF06Portes(), 2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cnx != null) freePoolConnection(cnx);
        }
        response.setHeader("responde", "true");
        if (json != null) response.setHeader("X-JSON", json.getFormat());
        return null;
    }

    public ModelAndView consultaParametrosNoFinancieros(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/parametroProducto/par_parametroNoFinanciero");
        String idProducto = request.getParameter("idProducto");
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        bnchf20Producto = parametroService.obtenerProducto(bnchf20Producto);
        List listCentroHipotecario = mantenimientoService.listaCentroHipotecarioActivos();
        mav.addObject("idProducto", idProducto);
        mav.addObject("bnchf20Producto", bnchf20Producto);
        mav.addObject("listCentroHipotecario", listCentroHipotecario);
        return mav;
    }

    public ModelAndView detalleParametrosNoFinancieros(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParamDocumentTitular bnchf54ParamDocumentTitular = new Bnchf54ParamDocumentTitular();
        Bnchf54ParamDocumentTitularId bnchf54ParamDocumentTitularId = new Bnchf54ParamDocumentTitularId();
        bnchf54ParamDocumentTitularId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        Bnchf54ParamReqsFam bnchf54ParamReqsFam = new Bnchf54ParamReqsFam();
        Bnchf54ParamReqsFamId bnchf54ParamReqsFamId = new Bnchf54ParamReqsFamId();
        bnchf54ParamReqsFamId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        Bnchf54ParamReqsTit bnchf54ParamReqsTit = new Bnchf54ParamReqsTit();
        Bnchf54ParamReqsTitId bnchf54ParamReqsTitId = new Bnchf54ParamReqsTitId();
        bnchf54ParamReqsTitId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        bnchf54ParamDocumentTitular.setId(bnchf54ParamDocumentTitularId);
        bnchf54ParamReqsFam.setId(bnchf54ParamReqsFamId);
        bnchf54ParamReqsTit.setId(bnchf54ParamReqsTitId);
        Util.JSON json = new Util.JSON();
        Bnchf54ParamDocumentTitular bnchf54ParamDocumentTitulartemp = parametroService.getParamDocumentoTitular(bnchf54ParamDocumentTitular);
        if (bnchf54ParamDocumentTitulartemp != null) {
            json.put("COPIA_DNI_01", bnchf54ParamDocumentTitulartemp.getF54CopiaDni());
            json.put("NRO_BOLT_VER_BN_01", bnchf54ParamDocumentTitulartemp.getF54NroBoltVerBn());
            json.put("NRO_PAGARE_01", bnchf54ParamDocumentTitulartemp.getF54NroPagare());
            json.put("CONST_NOMBRM_01", bnchf54ParamDocumentTitulartemp.getF54ConstNombrm());
            json.put("NRO_DOC_SERV_01", bnchf54ParamDocumentTitulartemp.getF54NroDocServ());
            json.put("ESTDO_MOV_TARJ_01", bnchf54ParamDocumentTitulartemp.getF54EstdoMovTarj());
            json.put("SOLIC_CREDITO_01", bnchf54ParamDocumentTitulartemp.getF54SolicCredito());
            json.put("ESTDO_CTA_OTROS_01", bnchf54ParamDocumentTitulartemp.getF54EstdoCtaOtros());
            json.put("CERTF_RENTA_01", bnchf54ParamDocumentTitulartemp.getF54CertfRenta());
            json.put("DPS_01", bnchf54ParamDocumentTitulartemp.getF54Dps());
        }
        Bnchf54ParamReqsFam bnchf54ParamReqsFamtemp = parametroService.getParamReqsFam(bnchf54ParamReqsFam);
        if (bnchf54ParamReqsFamtemp != null) {
            json.put("COPIA_DNI_02", bnchf54ParamReqsFamtemp.getF54CopiaDni());
            json.put("NRO_BOLT_VER_BN_02", bnchf54ParamReqsFamtemp.getF54NroBoltVerBn());
            json.put("NRO_BOLT_OTROS_02", bnchf54ParamReqsFamtemp.getF54NroBoltFisico());
            json.put("CONST_NOMBRM_02", bnchf54ParamReqsFamtemp.getF54ConstNombrm());
            json.put("ESTDO_MOV_TARJ_02", bnchf54ParamReqsFamtemp.getF54EstdoMovTarj());
            json.put("ESTDO_CTA_OTROS_02", bnchf54ParamReqsFamtemp.getF54EstdoCtaOtros());
            json.put("CERTF_RENTA_02", bnchf54ParamReqsFamtemp.getF54CertfRenta());
            json.put("DPS_02", bnchf54ParamReqsFamtemp.getF54Dps());
            json.put("RET_CAT5_02", bnchf54ParamReqsFamtemp.getF54RetCat5());
        }
        Bnchf54ParamReqsTit bnchf54ParamReqsTittemp = parametroService.getParamReqsTit(bnchf54ParamReqsTit);
        if (bnchf54ParamReqsTittemp != null) {
            json.put("ANTIG_LAB_03", bnchf54ParamReqsTittemp.getF54AntigLab());
            json.put("EDAD_MIN_03", bnchf54ParamReqsTittemp.getF54EdadMin());
            json.put("EDAD_MAX_03", bnchf54ParamReqsTittemp.getF54EdadMax());
            json.put("INGR_MIN_03", bnchf54ParamReqsTittemp.getF54IngrMin());
            json.put("TARJ_MULTRED_03", bnchf54ParamReqsTittemp.getF54TarjMultred());
            json.put("CTA_CTE_CERR_03", bnchf54ParamReqsTittemp.getF54CtaCteCerr());
            json.put("TARJ_CRED_CERR_03", bnchf54ParamReqsTittemp.getF54TarjCredCerr());
            json.put("COBR_COAC_SUNAT_03", bnchf54ParamReqsTittemp.getF54CobrCoacSunat());
            json.put("LETRA_PROTEST_03", bnchf54ParamReqsTittemp.getF54LetraProtest());
        }
        response.setHeader("responde", "true");
        if (json != null) response.setHeader("X-JSON", json.getFormat());
        response.setHeader("Cache-Control", "no-cache");
        return null;
    }

    public ModelAndView guardarParametrosNoFinancieros(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParamDocumentTitular bnchf54ParamDocumentTitular = new Bnchf54ParamDocumentTitular();
        Bnchf54ParamDocumentTitularId bnchf54ParamDocumentTitularId = new Bnchf54ParamDocumentTitularId();
        bnchf54ParamDocumentTitularId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        Bnchf54ParamReqsFam bnchf54ParamReqsFam = new Bnchf54ParamReqsFam();
        Bnchf54ParamReqsFamId bnchf54ParamReqsFamId = new Bnchf54ParamReqsFamId();
        bnchf54ParamReqsFamId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        Bnchf54ParamReqsTit bnchf54ParamReqsTit = new Bnchf54ParamReqsTit();
        Bnchf54ParamReqsTitId bnchf54ParamReqsTitId = new Bnchf54ParamReqsTitId();
        bnchf54ParamReqsTitId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        bnchf54ParamDocumentTitular.setId(bnchf54ParamDocumentTitularId);
        bnchf54ParamReqsFam.setId(bnchf54ParamReqsFamId);
        bnchf54ParamReqsTit.setId(bnchf54ParamReqsTitId);
        bnchf54ParamDocumentTitular.setF54IdUsuaCrea(datosSesion.getCodigoHost());
        bnchf54ParamDocumentTitular.setF54IdUsuaModi(datosSesion.getCodigoHost());
        bnchf54ParamReqsFam.setF54IdUsuaCrea(datosSesion.getCodigoHost());
        bnchf54ParamReqsFam.setF54IdUsuaModi(datosSesion.getCodigoHost());
        bnchf54ParamReqsTit.setF54IdUsuaCrea(datosSesion.getCodigoHost());
        bnchf54ParamReqsTit.setF54IdUsuaModi(datosSesion.getCodigoHost());
        bnchf54ParametroProducto = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        bnchf54ParamDocumentTitular.setF54CopiaDni(Util.replaceSi(request.getParameter("txtCopiaDNI01"), "1", 1, 2));
        bnchf54ParamDocumentTitular.setF54NroBoltVerBn(Util.parseLong(request.getParameter("txtNroBoletaBN01")));
        bnchf54ParamDocumentTitular.setF54NroPagare(Util.parseLong(request.getParameter("txtNroPagare01")));
        bnchf54ParamDocumentTitular.setF54ConstNombrm(Util.replaceSi(request.getParameter("txtConstNombre01"), "1", 1, 2));
        bnchf54ParamDocumentTitular.setF54NroDocServ(Util.parseLong(request.getParameter("txtNroDocServ01")));
        bnchf54ParamDocumentTitular.setF54EstdoMovTarj(Util.replaceSi(request.getParameter("txtEstadoMovimientoTarjeta01"), "1", 1, 2));
        bnchf54ParamDocumentTitular.setF54SolicCredito(Util.replaceSi(request.getParameter("txtSolicitaCredito01"), "1", 1, 2));
        bnchf54ParamDocumentTitular.setF54EstdoCtaOtros(Util.replaceSi(request.getParameter("txtEstadoCtaOtros01"), "1", 1, 2));
        bnchf54ParamDocumentTitular.setF54CertfRenta(Util.replaceSi(request.getParameter("txtCertRenta01"), "1", 1, 2));
        bnchf54ParamDocumentTitular.setF54Dps(Util.replaceSi(request.getParameter("txtDPS01"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54CopiaDni(Util.replaceSi(request.getParameter("txtCopiaDNI02"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54NroBoltVerBn(Util.parseLong(request.getParameter("txtNroBoletaBN02")));
        bnchf54ParamReqsFam.setF54NroBoltFisico(Util.parseLong(request.getParameter("txtNroBoletaOtros02")));
        bnchf54ParamReqsFam.setF54ConstNombrm(Util.replaceSi(request.getParameter("txtConstNombre02"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54EstdoMovTarj(Util.replaceSi(request.getParameter("txtEstadoMovimientoTarjeta02"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54EstdoCtaOtros(Util.replaceSi(request.getParameter("txtEstadoCtaOtros02"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54CertfRenta(Util.replaceSi(request.getParameter("txtCertRenta02"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54Dps(Util.replaceSi(request.getParameter("txtDPS02"), "1", 1, 2));
        bnchf54ParamReqsFam.setF54RetCat5(Util.replaceSi(request.getParameter("txtRetCat502"), "1", 1, 2));
        bnchf54ParamReqsTit.setF54AntigLab(Util.parseLong(request.getParameter("txtAntigLab03")));
        bnchf54ParamReqsTit.setF54EdadMin(Util.parseLong(request.getParameter("txtEdadMinima03")));
        bnchf54ParamReqsTit.setF54EdadMax(Util.parseLong(request.getParameter("txtEdadMaxima03")));
        bnchf54ParamReqsTit.setF54IngrMin(Util.parseDouble(request.getParameter("txtIngrMin03")));
        bnchf54ParamReqsTit.setF54TarjMultred(Util.replaceSi(request.getParameter("txtTarjetaMultired03"), "1", 1, 2));
        bnchf54ParamReqsTit.setF54CtaCteCerr(Util.parseLong(request.getParameter("ctaCteCerrada03")));
        bnchf54ParamReqsTit.setF54TarjCredCerr(Util.parseLong(request.getParameter("tarjetaCreditoCerrada03")));
        bnchf54ParamReqsTit.setF54CobrCoacSunat(Util.parseLong(request.getParameter("cobrCoacSunat03")));
        bnchf54ParamReqsTit.setF54LetraProtest(Util.parseLong(request.getParameter("letraProtest03")));
        try {
            if (bnchf54ParametroProducto != null) {
                if (parametroService.getParamDocumentoTitular(bnchf54ParamDocumentTitular) != null) {
                    parametroService.updateParamDocumentoTitular(bnchf54ParamDocumentTitular);
                } else {
                    parametroService.insertParamDocumentoTitular(bnchf54ParamDocumentTitular);
                }
                Bnchf54ParamReqsFam fam = parametroService.getParamReqsFam(bnchf54ParamReqsFam);
                if (fam != null) {
                    parametroService.updateParamReqsFam(bnchf54ParamReqsFam);
                } else {
                    parametroService.insertParamReqsFam(bnchf54ParamReqsFam);
                }
                Bnchf54ParamReqsTit tit = parametroService.getParamReqsTit(bnchf54ParamReqsTit);
                if (tit != null) {
                    parametroService.updateParamReqsTit(bnchf54ParamReqsTit);
                } else {
                    parametroService.insertParamReqsTit(bnchf54ParamReqsTit);
                }
                response.setHeader("result", "Los cambios se guardaron satisfactoriamente");
            } else {
                response.setHeader("result", "No ha registrado valores en la opci�n 'parametros Financieros' para el producto y centro hipotecario seleccionado.");
                response.setHeader("tipo", "1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("result", "Ocurri� un problema al guardar los datos.");
            response.setHeader("tipo", "1");
        }
        response.setHeader("responde", "true");
        return null;
    }

    /*** PARAMETROS TASA DE INTERES ****/
    public ModelAndView consultaTasaInteres(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/parametroProducto/par_tasainteres");
        String idProducto = request.getParameter("idProducto");
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        bnchf20Producto = parametroService.obtenerProducto(bnchf20Producto);
        List listCentroHipotecario = mantenimientoService.listaCentroHipotecarioActivos();
        mav.addObject("idProducto", idProducto);
        mav.addObject("bnchf20Producto", bnchf20Producto);
        mav.addObject("listCentroHipotecario", listCentroHipotecario);
        return mav;
    }

    public ModelAndView detalleTasaInteres(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParamTasaInteres bnchf54ParamTasaInteres = new Bnchf54ParamTasaInteres();
        Bnchf54ParamTasaInteresId bnchf54ParamTasaInteresId = new Bnchf54ParamTasaInteresId();
        bnchf54ParamTasaInteresId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        bnchf54ParamTasaInteres.setId(bnchf54ParamTasaInteresId);
        bnchf54ParametroProducto = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        BnchfTasaInteres bnchfTasaInteres = null;
        if (bnchf54ParametroProducto != null) {
            bnchfTasaInteres = new BnchfTasaInteres();
            bnchfTasaInteres.setF07CodTarifa(bnchf54ParametroProducto.getF54IdTarifa());
            bnchfTasaInteres.setF07CodCentroHip(idCHHost);
        }
        Util.JSONArray jsonarray = new Util.JSONArray();
        List tasaInteresObtenida = parametroService.getTasaInteres(bnchf54ParamTasaInteres);
        if (tasaInteresObtenida != null) {
            for (int i = 0; i < 4; i++) {
                Util.JSON json1 = new Util.JSON();
                json1.put("TIPO", "NORMAL");
                if (i < tasaInteresObtenida.size()) {
                    Bnchf54ParamTasaInteres tasaInteres = (Bnchf54ParamTasaInteres) tasaInteresObtenida.get(i);
                    json1.put("INDEX", tasaInteres.getId().getF54IdPlazInt());
                    json1.put("PLAZOMIN", tasaInteres.getF54PlazoMin());
                    json1.put("PLAZOMAX", tasaInteres.getF54PlazoMax());
                    json1.put("TASAFIJA", tasaInteres.getF54TasaFija());
                    json1.put("TASAVAR", tasaInteres.getF54TasaVar());
                    json1.put("TASACOM", tasaInteres.getF54TasaCom());
                } else {
                    json1.put("INDEX", "0");
                    json1.put("PLAZOMIN", "0");
                    json1.put("PLAZOMAX", "0");
                    json1.put("TASAFIJA", "0");
                    json1.put("TASAVAR", "0");
                    json1.put("TASACOM", "0");
                }
                jsonarray.put(json1);
            }
        }
        Connection cnx = null;
        Util.JSONArray historial = null;
        try {
            if (bnchfTasaInteres != null) {
                cnx = getPoolConnection();
                List tasasInteres = parametroService.getTasaInteresDatacom(bnchfTasaInteres, cnx);
                if (cnx != null) freePoolConnection(cnx);
                if (tasasInteres != null) {
                    for (int i = 0; i < 4; i++) {
                        Util.JSON json1 = new Util.JSON();
                        json1.put("TIPO", "VALIDACION");
                        if (i < tasasInteres.size()) {
                            BnchfTasaInteres tasaInteres = (BnchfTasaInteres) tasasInteres.get(i);
                            json1.put("INDEX_VAL", tasaInteres.getF07Plazo());
                            json1.put("TASAFIJA_VAL", tasaInteres.getF07TasaFija());
                            json1.put("TASAVAR_VAL", tasaInteres.getF07TasaVar());
                            json1.put("TASACOM_VAL", tasaInteres.getF07TasaCom());
                        } else {
                            json1.put("INDEX_VAL", "0");
                            json1.put("PLAZOMIN_VAL", "0");
                            json1.put("PLAZOMAX_VAL", "0");
                            json1.put("TASAFIJA_VAL", "0");
                            json1.put("TASAVAR_VAL", "0");
                            json1.put("TASACOM_VAL", "0");
                        }
                        jsonarray.put(json1);
                    }
                }
                List lstH = parametroService.getHistorial(bnchfTasaInteres, cnx);
                if (lstH != null) {
                    historial = new Util.JSONArray();
                    for (int i = 0; i < lstH.size(); i++) {
                        Util.JSON it = new Util.JSON();
                        BnchfTasaInteres item = (BnchfTasaInteres) lstH.get(i);
                        it.put("tarifa", item.getF07CodTarifa());
                        it.put("plazo", item.getF07Plazo());
                        it.put("tasafija", Util.formatearNumero(new BigDecimal(Util.parseDouble(item.getF07TasaFija()).doubleValue()), "#0.0000"));
                        it.put("tasavar", Util.formatearNumero(new BigDecimal(Util.parseDouble(item.getF07TasaVar()).doubleValue()), "#0.0000"));
                        it.put("tasacom", Util.formatearNumero(new BigDecimal(Util.parseDouble(item.getF07TasaCom()).doubleValue()), "#0.0000"));
                        it.put("estado", item.getF07Estado());
                        it.put("timestamp", item.getF07Timestamp());
                        it.put("usuario", item.getF07CodUsuario());
                        historial.put(it);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cnx != null) freePoolConnection(cnx);
        }
        response.setHeader("responde", "true");
        if (jsonarray != null) response.setHeader("X-JSON", jsonarray.getFormat());
        if (historial != null) response.setHeader("HISTORIAL", historial.getFormat());
        response.setHeader("Cache-Control", "no-cache");
        return null;
    }

    public ModelAndView guardarTasaInteres(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        String idNroSecHost = "";
        String idProductoHost = "";
        if (idProducto.trim().length() >= 3) {
            idNroSecHost = Util.cortar(idProducto, 2, false);
            idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
        }
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParametroProducto bnchf54ParametroProductoTarifa = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        Connection cnx = null;
        String timestamp = Util.getTimestamp();
        try {
            cnx = getPoolConnection();
            for (int i = 1; i <= 4; i++) {
                Bnchf54ParamTasaInteres bnchf54ParamTasaInteres = new Bnchf54ParamTasaInteres();
                Bnchf54ParamTasaInteresId bnchf54ParamTasaInteresId = new Bnchf54ParamTasaInteresId();
                bnchf54ParamTasaInteresId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
                bnchf54ParamTasaInteres.setId(bnchf54ParamTasaInteresId);
                bnchf54ParamTasaInteresId.setF54IdPlazInt(new Long(request.getParameter("index0" + i)));
                bnchf54ParamTasaInteres.setF54PlazoMin(Util.parseLong(request.getParameter("min0" + i)));
                bnchf54ParamTasaInteres.setF54PlazoMax(Util.parseLong(request.getParameter("max0" + i)));
                bnchf54ParamTasaInteres.setF54TasaFija(Util.parseDouble(request.getParameter("fij0" + i)));
                bnchf54ParamTasaInteres.setF54TasaVar(Util.parseDouble(request.getParameter("var0" + i)));
                bnchf54ParamTasaInteres.setF54TasaCom(Util.parseDouble(request.getParameter("com0" + i)));
                BnchfTasaInteres bnchfTasaInteres = new BnchfTasaInteres();
                bnchfTasaInteres.setF07CodCentroHip(idCHHost);
                bnchfTasaInteres.setF07CodTarifa(bnchf54ParametroProductoTarifa.getF54IdTarifa());
                bnchfTasaInteres.setF07TasaFija(Util.parseDouble(request.getParameter("fij0" + i)));
                bnchfTasaInteres.setF07TasaVar(Util.parseDouble(request.getParameter("var0" + i)));
                bnchfTasaInteres.setF07TasaCom(Util.parseDouble(request.getParameter("com0" + i)));
                bnchfTasaInteres.setF07Timestamp(timestamp);
                bnchfTasaInteres.setF07Plazo(new Long(request.getParameter("index0" + i)));
                bnchfTasaInteres.setF07CodUsuario(datosSesion.getCodigoHost());
                boolean existe = false;
                if (parametroService.getTasaInteresxIdPlazo(bnchf54ParamTasaInteres) != null) existe = true;
                if (existe) {
                    bnchf54ParamTasaInteres.setF54IdUsuaModi(datosSesion.getCodigoHost());
                    parametroService.updateTasaInteres(bnchf54ParamTasaInteres);
                } else {
                    bnchf54ParamTasaInteres.setF54IdUsuaCrea(datosSesion.getCodigoHost());
                    parametroService.insertTasaInteres(bnchf54ParamTasaInteres);
                }
                parametroService.insertTasaDatacom(bnchfTasaInteres, cnx);
            }
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
            response.setHeader("result", "Los cambios se guardaron satisfactoriamente");
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("result", "Ocurri� un problema al guardar los datos.");
            response.setHeader("tipo", "1");
        } finally {
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
        }
        response.setHeader("responde", "true");
        return null;
    }

    public ModelAndView actualizarTasaInteres(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        String idNroSecHost = "";
        String idProductoHost = "";
        if (idProducto.trim().length() >= 3) {
            idNroSecHost = Util.cortar(idProducto, 2, false);
            idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
        }
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParametroProducto bnchf54ParametroProductoTarifa = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        Connection cnx = null;
        try {
            cnx = getPoolConnection();
            BnchfTasaInteres bnchfTemporal = new BnchfTasaInteres();
            bnchfTemporal.setF07CodCentroHip(idCHHost);
            bnchfTemporal.setF07CodTarifa(bnchf54ParametroProductoTarifa.getF54IdTarifa());
            String timestamp = parametroService.maxTimestampTasaInteres(bnchfTemporal, cnx);
            if (timestamp == null) {
                response.setHeader("existe", "no");
            } else {
                for (int i = 1; i <= 4; i++) {
                    Bnchf54ParamTasaInteres bnchf54ParamTasaInteres = new Bnchf54ParamTasaInteres();
                    Bnchf54ParamTasaInteresId bnchf54ParamTasaInteresId = new Bnchf54ParamTasaInteresId();
                    bnchf54ParamTasaInteresId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
                    bnchf54ParamTasaInteres.setId(bnchf54ParamTasaInteresId);
                    bnchf54ParamTasaInteresId.setF54IdPlazInt(new Long(request.getParameter("index0" + i)));
                    bnchf54ParamTasaInteres.setF54PlazoMin(Util.parseLong(request.getParameter("min0" + i)));
                    bnchf54ParamTasaInteres.setF54PlazoMax(Util.parseLong(request.getParameter("max0" + i)));
                    bnchf54ParamTasaInteres.setF54TasaFija(Util.parseDouble(request.getParameter("fij0" + i)));
                    bnchf54ParamTasaInteres.setF54TasaVar(Util.parseDouble(request.getParameter("var0" + i)));
                    bnchf54ParamTasaInteres.setF54TasaCom(Util.parseDouble(request.getParameter("com0" + i)));
                    BnchfTasaInteres bnchfTasaInteres = new BnchfTasaInteres();
                    bnchfTasaInteres.setF07CodCentroHip(idCHHost);
                    bnchfTasaInteres.setF07CodTarifa(bnchf54ParametroProductoTarifa.getF54IdTarifa());
                    bnchfTasaInteres.setF07TasaFija(Util.parseDouble(request.getParameter("fij0" + i)));
                    bnchfTasaInteres.setF07TasaVar(Util.parseDouble(request.getParameter("var0" + i)));
                    bnchfTasaInteres.setF07TasaCom(Util.parseDouble(request.getParameter("com0" + i)));
                    bnchfTasaInteres.setF07Timestamp(timestamp);
                    bnchfTasaInteres.setF07Plazo(new Long(request.getParameter("index0" + i)));
                    bnchfTasaInteres.setF07CodUsuario(datosSesion.getCodigoHost());
                    boolean existe = false;
                    if (parametroService.getTasaInteresxIdPlazo(bnchf54ParamTasaInteres) != null) existe = true;
                    if (existe) {
                        bnchf54ParamTasaInteres.setF54IdUsuaModi(datosSesion.getCodigoHost());
                        parametroService.updateTasaInteres(bnchf54ParamTasaInteres);
                    } else {
                        bnchf54ParamTasaInteres.setF54IdUsuaCrea(datosSesion.getCodigoHost());
                        parametroService.insertTasaInteres(bnchf54ParamTasaInteres);
                    }
                    if (parametroService.existeTasaInteres(bnchfTasaInteres, cnx) != null) {
                        parametroService.updateTasaDatacom(bnchfTasaInteres, cnx);
                    } else {
                        parametroService.insertTasaDatacom(bnchfTasaInteres, cnx);
                    }
                }
                if (cnx != null) {
                    cnx.commit();
                    freePoolConnection(cnx);
                }
                response.setHeader("result", "Los cambios se guardaron satisfactoriamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("result", "Ocurri� un problema al guardar los datos.");
            response.setHeader("tipo", "1");
        } finally {
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
        }
        response.setHeader("responde", "true");
        return null;
    }

    /*** PARAMETROS DESGRAVAMEN ****/
    public ModelAndView consultaDesgravamen(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        ModelAndView mav = new ModelAndView("mantenimiento/parametroProducto/par_desgravamen");
        String idProducto = request.getParameter("idProducto");
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        bnchf20Producto = parametroService.obtenerProducto(bnchf20Producto);
        List listCentroHipotecario = mantenimientoService.listaCentroHipotecarioActivos();
        mav.addObject("idProducto", idProducto);
        mav.addObject("bnchf20Producto", bnchf20Producto);
        mav.addObject("listCentroHipotecario", listCentroHipotecario);
        return mav;
    }

    public ModelAndView detalleDesgravamen(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Connection cnx = null;
        Bnchf54ParamReqsDesgrav bnchf54ParamReqsDesgrav = new Bnchf54ParamReqsDesgrav();
        Bnchf54ParamReqsDesgravId bnchf54ParamReqsDesgravId = new Bnchf54ParamReqsDesgravId();
        bnchf54ParamReqsDesgravId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
        bnchf54ParamReqsDesgrav.setId(bnchf54ParamReqsDesgravId);
        bnchf54ParametroProducto = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        BnchfDesgravamen bnchfDesgravamen = null;
        if (bnchf54ParametroProducto != null) {
            bnchfDesgravamen = new BnchfDesgravamen();
            bnchfDesgravamen.setF49CodTarifa(bnchf54ParametroProducto.getF54IdTarifa());
        }
        Util.JSON json = new Util.JSON();
        try {
            json.put("RANGOEDAD", bnchf54ParametroProducto.getF54RngoEdadDesgrav());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.JSONArray jsonarray = new Util.JSONArray();
        List desgravamen = parametroService.getDesgravamen(bnchf54ParamReqsDesgrav);
        if (desgravamen != null) {
            for (int i = 0; i < 4; i++) {
                Util.JSON json1 = new Util.JSON();
                json1.put("TIPO", "NORMAL");
                if (i < desgravamen.size()) {
                    Bnchf54ParamReqsDesgrav des = (Bnchf54ParamReqsDesgrav) desgravamen.get(i);
                    json1.put("INDEX", des.getId().getF54IdDesgrav());
                    json1.put("IDRANGOEDAD", des.getF54IdRngoEdad());
                    json1.put("PORCEDESGRAV", des.getF54PorcDesgrav());
                } else {
                    json1.put("INDEX", "0");
                    json1.put("IDRANGOEDAD", "0");
                    json1.put("PORCEDESGRAV", "0");
                }
                jsonarray.put(json1);
            }
        }
        Util.JSONArray historial = null;
        try {
            if (bnchfDesgravamen != null) {
                cnx = getPoolConnection();
                List desgravamenDatacom = parametroService.getDesgravamenDatacom(bnchfDesgravamen, cnx);
                if (cnx != null) freePoolConnection(cnx);
                if (desgravamenDatacom != null) {
                    for (int i = 0; i < 4; i++) {
                        Util.JSON json1 = new Util.JSON();
                        json1.put("TIPO", "VALIDACION");
                        if (i < desgravamenDatacom.size()) {
                            BnchfDesgravamen des = (BnchfDesgravamen) desgravamenDatacom.get(i);
                            json1.put("INDEX_VAL", des.getF49TipoDesgravamen());
                            json1.put("IDRANGOEDAD_VAL", des.getF49RangoEdad());
                            json1.put("PORCEDESGRAV_VAL", des.getF49Tasa());
                        } else {
                            json1.put("INDEX_VAL", "0");
                            json1.put("IDRANGOEDAD_VAL", "0");
                            json1.put("PORCEDESGRAV_VAL", "0");
                        }
                        jsonarray.put(json1);
                    }
                }
                List lstH = parametroService.getHistorial(bnchfDesgravamen, cnx);
                if (lstH != null) {
                    historial = new Util.JSONArray();
                    for (int i = 0; i < lstH.size(); i++) {
                        Util.JSON it = new Util.JSON();
                        BnchfDesgravamen item = (BnchfDesgravamen) lstH.get(i);
                        it.put("tarifa", item.getF49CodTarifa());
                        if (Util.parseLong(item.getF49TipoDesgravamen()).longValue() == 1) {
                            it.put("tipo", "Mancomunado");
                        } else if (Util.parseLong(item.getF49TipoDesgravamen()).longValue() == 2) {
                            it.put("tipo", "Individual");
                        }
                        if (Util.parseLong(item.getF49RangoEdad()).longValue() == 1) {
                            it.put("rango", "Menor o igual a " + bnchf54ParametroProducto.getF54RngoEdadDesgrav() + " a�os ");
                        } else if (Util.parseLong(item.getF49RangoEdad()).longValue() == 2) {
                            it.put("rango", "Mayor a " + bnchf54ParametroProducto.getF54RngoEdadDesgrav() + " a�os ");
                        }
                        it.put("porcentaje", Util.formatearNumero(new BigDecimal(Util.parseDouble(item.getF49Tasa()).doubleValue()), "#0.0000"));
                        it.put("timestamp", item.getF49Timestamp());
                        it.put("usuario", item.getF49CodUsuario());
                        historial.put(it);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cnx != null) freePoolConnection(cnx);
        }
        response.setHeader("responde", "true");
        if (json != null) response.setHeader("edad", json.getFormat());
        if (jsonarray != null) response.setHeader("X-JSON", jsonarray.getFormat());
        if (historial != null) response.setHeader("HISTORIAL", historial.getFormat());
        response.setHeader("Cache-Control", "no-cache");
        return null;
    }

    public String actualizarDesgravamentDesdeHost(Bnchf54ParametroProducto bnchf54ParametroProducto, DatosSesion datosSesion) {
        String mensaje = null;
        Connection cnx = null;
        try {
            BnchfDesgravamen bnchfDesgravamen = new BnchfDesgravamen();
            bnchfDesgravamen.setF49CodTarifa(bnchf54ParametroProducto.getF54IdTarifa());
            cnx = getPoolConnection();
            List desgravamenDatacom = parametroService.getDesgravamenDatacom(bnchfDesgravamen, cnx);
            if (cnx != null) freePoolConnection(cnx);
            boolean[] combinaciones = { false, false, false, false };
            if (desgravamenDatacom != null) {
                for (int i = 0; i < 4; i++) {
                    Util.JSON json1 = new Util.JSON();
                    json1.put("TIPO", "VALIDACION");
                    Bnchf54ParamReqsDesgrav bnchf54ParamReqsDesgrav = new Bnchf54ParamReqsDesgrav();
                    Bnchf54ParamReqsDesgravId bnchf54ParamReqsDesgravId = new Bnchf54ParamReqsDesgravId();
                    bnchf54ParamReqsDesgravId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
                    bnchf54ParamReqsDesgrav.setId(bnchf54ParamReqsDesgravId);
                    BnchfDesgravamen des = new BnchfDesgravamen();
                    if (i < desgravamenDatacom.size()) {
                        des = (BnchfDesgravamen) desgravamenDatacom.get(i);
                        int actualizarCombinacion = -1;
                        if ((des.getF49TipoDesgravamen().equals(new Long(1)) && des.getF49RangoEdad().equals(new Long(1)) && combinaciones[0] == false) || (des.getF49TipoDesgravamen().equals(new Long(1)) && des.getF49RangoEdad().equals(new Long(2)) && combinaciones[1] == false) || (des.getF49TipoDesgravamen().equals(new Long(2)) && des.getF49RangoEdad().equals(new Long(1)) && combinaciones[2] == false) || (des.getF49TipoDesgravamen().equals(new Long(2)) && des.getF49RangoEdad().equals(new Long(2)) && combinaciones[3] == false)) {
                            bnchf54ParamReqsDesgravId.setF54IdDesgrav(des.getF49TipoDesgravamen());
                            bnchf54ParamReqsDesgrav.setF54IdRngoEdad(des.getF49RangoEdad().toString());
                            bnchf54ParamReqsDesgrav.setF54PorcDesgrav(des.getF49Tasa());
                            boolean existe = false;
                            if (parametroService.getDesgravamenXId(bnchf54ParamReqsDesgrav) != null) existe = true;
                            if (existe) {
                                bnchf54ParamReqsDesgrav.setF54IdUsuaModi(datosSesion.getCodigoHost());
                                parametroService.updateDesgravamen(bnchf54ParamReqsDesgrav);
                            } else {
                                bnchf54ParamReqsDesgrav.setF54IdUsuaCrea(datosSesion.getCodigoHost());
                                parametroService.insertDesgravamen(bnchf54ParamReqsDesgrav);
                            }
                            if (des.getF49TipoDesgravamen().equals(new Long(1)) && des.getF49RangoEdad().equals(new Long(1))) actualizarCombinacion = 0;
                            if (des.getF49TipoDesgravamen().equals(new Long(1)) && des.getF49RangoEdad().equals(new Long(2))) actualizarCombinacion = 1;
                            if (des.getF49TipoDesgravamen().equals(new Long(2)) && des.getF49RangoEdad().equals(new Long(1))) actualizarCombinacion = 2;
                            if (des.getF49TipoDesgravamen().equals(new Long(2)) && des.getF49RangoEdad().equals(new Long(2))) actualizarCombinacion = 3;
                        }
                        if (actualizarCombinacion > -1) {
                            combinaciones[actualizarCombinacion] = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = e.toString();
        } finally {
            if (cnx != null) freePoolConnection(cnx);
        }
        return mensaje;
    }

    public ModelAndView guardarDesgravamen(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        String idNroSecHost = "";
        String idProductoHost = "";
        if (idProducto.trim().length() >= 3) {
            idNroSecHost = Util.cortar(idProducto, 2, false);
            idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
        }
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParametroProducto bnchf54ParametroProductoDesgrav = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        Connection cnx = null;
        try {
            String timestamp = Util.getTimestamp();
            try {
                cnx = getPoolConnection();
                for (int i = 1; i <= 4; i++) {
                    Bnchf54ParamReqsDesgrav bnchf54ParamReqsDesgrav = new Bnchf54ParamReqsDesgrav();
                    Bnchf54ParamReqsDesgravId bnchf54ParamReqsDesgravId = new Bnchf54ParamReqsDesgravId();
                    bnchf54ParamReqsDesgravId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
                    bnchf54ParamReqsDesgrav.setId(bnchf54ParamReqsDesgravId);
                    if (i == 1) {
                        bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(1));
                        bnchf54ParamReqsDesgrav.setF54IdRngoEdad("1");
                        bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad01")));
                    } else if (i == 2) {
                        bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(1));
                        bnchf54ParamReqsDesgrav.setF54IdRngoEdad("2");
                        bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad02")));
                    } else if (i == 3) {
                        bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(2));
                        bnchf54ParamReqsDesgrav.setF54IdRngoEdad("1");
                        bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad11")));
                    } else {
                        bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(2));
                        bnchf54ParamReqsDesgrav.setF54IdRngoEdad("2");
                        bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad12")));
                    }
                    BnchfDesgravamen bnchfDesgravamen = new BnchfDesgravamen();
                    bnchfDesgravamen.setF49CodTarifa(bnchf54ParametroProductoDesgrav.getF54IdTarifa());
                    bnchfDesgravamen.setF49Timestamp(timestamp);
                    if (i == 1) {
                        bnchfDesgravamen.setF49TipoDesgravamen(new Long(1));
                        bnchfDesgravamen.setF49RangoEdad(new Long(1));
                        bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad01")));
                    } else if (i == 2) {
                        bnchfDesgravamen.setF49TipoDesgravamen(new Long(1));
                        bnchfDesgravamen.setF49RangoEdad(new Long(2));
                        bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad02")));
                    } else if (i == 3) {
                        bnchfDesgravamen.setF49TipoDesgravamen(new Long(2));
                        bnchfDesgravamen.setF49RangoEdad(new Long(1));
                        bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad11")));
                    } else {
                        bnchfDesgravamen.setF49TipoDesgravamen(new Long(2));
                        bnchfDesgravamen.setF49RangoEdad(new Long(2));
                        bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad12")));
                    }
                    bnchfDesgravamen.setF49CodUsuario(datosSesion.getCodigoHost());
                    boolean existe = false;
                    if (parametroService.getDesgravamenXId(bnchf54ParamReqsDesgrav) != null) existe = true;
                    if (existe) {
                        bnchf54ParamReqsDesgrav.setF54IdUsuaModi(datosSesion.getCodigoHost());
                        parametroService.updateDesgravamen(bnchf54ParamReqsDesgrav);
                    } else {
                        bnchf54ParamReqsDesgrav.setF54IdUsuaCrea(datosSesion.getCodigoHost());
                        parametroService.insertDesgravamen(bnchf54ParamReqsDesgrav);
                    }
                    parametroService.insertDesgravamenDatacom(bnchfDesgravamen, cnx);
                }
                if (cnx != null) {
                    cnx.commit();
                    freePoolConnection(cnx);
                }
                response.setHeader("result", "Los cambios se guardaron satisfactoriamente");
            } catch (Exception e) {
                e.printStackTrace();
                response.setHeader("result", "Ocurri� un problema al guardar los datos.");
                response.setHeader("tipo", "1");
            }
            response.setHeader("responde", "true");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
        }
        return null;
    }

    public ModelAndView actualizaDesgravamen(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = getCommandObject(request);
        ParametroFinancieroForm institucionForm = new ParametroFinancieroForm();
        institucionForm = (ParametroFinancieroForm) command;
        String idProducto = request.getParameter("idProducto");
        String idCH = request.getParameter("idCH");
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        String idNroSecHost = "";
        String idProductoHost = "";
        if (idProducto.trim().length() >= 3) {
            idNroSecHost = Util.cortar(idProducto, 2, false);
            idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
        }
        DatosSesion datosSesion = (DatosSesion) request.getSession().getAttribute("datosSesion");
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        Bnchf54ParametroProducto bnchf54ParametroProductoDesgrav = parametroService.getParametrosFinancieroOpen(bnchf54ParametroProducto);
        Connection cnx = null;
        try {
            String timestamp = null;
            BnchfDesgravamen bnchfTemporal = new BnchfDesgravamen();
            if (bnchf54ParametroProductoDesgrav != null) {
                bnchfTemporal.setF49CodTarifa(bnchf54ParametroProductoDesgrav.getF54IdTarifa());
                cnx = getPoolConnection();
                timestamp = parametroService.maxTimestampDesgravamen(bnchfTemporal, cnx);
            }
            if (timestamp == null) {
                response.setHeader("existe", "no");
            } else {
                try {
                    for (int i = 1; i <= 4; i++) {
                        Bnchf54ParamReqsDesgrav bnchf54ParamReqsDesgrav = new Bnchf54ParamReqsDesgrav();
                        Bnchf54ParamReqsDesgravId bnchf54ParamReqsDesgravId = new Bnchf54ParamReqsDesgravId();
                        bnchf54ParamReqsDesgravId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
                        bnchf54ParamReqsDesgrav.setId(bnchf54ParamReqsDesgravId);
                        if (i == 1) {
                            bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(1));
                            bnchf54ParamReqsDesgrav.setF54IdRngoEdad("1");
                            bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad01")));
                        } else if (i == 2) {
                            bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(1));
                            bnchf54ParamReqsDesgrav.setF54IdRngoEdad("2");
                            bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad02")));
                        } else if (i == 3) {
                            bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(2));
                            bnchf54ParamReqsDesgrav.setF54IdRngoEdad("1");
                            bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad11")));
                        } else {
                            bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(2));
                            bnchf54ParamReqsDesgrav.setF54IdRngoEdad("2");
                            bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(request.getParameter("edad12")));
                        }
                        BnchfDesgravamen bnchfDesgravamen = new BnchfDesgravamen();
                        bnchfDesgravamen.setF49CodTarifa(bnchf54ParametroProductoDesgrav.getF54IdTarifa());
                        bnchfDesgravamen.setF49Timestamp(timestamp);
                        if (i == 1) {
                            bnchfDesgravamen.setF49TipoDesgravamen(new Long(1));
                            bnchfDesgravamen.setF49RangoEdad(new Long(1));
                            bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad01")));
                        } else if (i == 2) {
                            bnchfDesgravamen.setF49TipoDesgravamen(new Long(1));
                            bnchfDesgravamen.setF49RangoEdad(new Long(2));
                            bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad02")));
                        } else if (i == 3) {
                            bnchfDesgravamen.setF49TipoDesgravamen(new Long(2));
                            bnchfDesgravamen.setF49RangoEdad(new Long(1));
                            bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad11")));
                        } else {
                            bnchfDesgravamen.setF49TipoDesgravamen(new Long(2));
                            bnchfDesgravamen.setF49RangoEdad(new Long(2));
                            bnchfDesgravamen.setF49Tasa(Util.parseDouble(request.getParameter("edad12")));
                        }
                        bnchfDesgravamen.setF49CodUsuario(datosSesion.getCodigoHost());
                        boolean existe = false;
                        if (parametroService.getDesgravamenXId(bnchf54ParamReqsDesgrav) != null) existe = true;
                        if (existe) {
                            bnchf54ParamReqsDesgrav.setF54IdUsuaModi(datosSesion.getCodigoHost());
                            parametroService.updateDesgravamen(bnchf54ParamReqsDesgrav);
                        } else {
                            bnchf54ParamReqsDesgrav.setF54IdUsuaCrea(datosSesion.getCodigoHost());
                            parametroService.insertDesgravamen(bnchf54ParamReqsDesgrav);
                        }
                        if (parametroService.existeDesgravamen(bnchfDesgravamen, cnx) != null) {
                            parametroService.updateDesgravamenDatacom(bnchfDesgravamen, cnx);
                        } else {
                            parametroService.insertDesgravamenDatacom(bnchfDesgravamen, cnx);
                        }
                    }
                    if (cnx != null) {
                        cnx.commit();
                        freePoolConnection(cnx);
                    }
                    response.setHeader("result", "Los cambios se guardaron satisfactoriamente");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setHeader("result", "Ocurri� un problema al guardar los datos.");
                    response.setHeader("tipo", "1");
                }
            }
            response.setHeader("responde", "true");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cnx != null) {
                cnx.commit();
                freePoolConnection(cnx);
            }
        }
        return null;
    }

    public void guardarDesgravamen(String idProducto, String idCH, DatosSesion datosSesion, ArrayList lstDesgrav) throws Exception {
        String idCHHost = Util.autocompletar(idCH, '0', 6, false);
        String idNroSecHost = "";
        String idProductoHost = "";
        if (idProducto.trim().length() >= 3) {
            idNroSecHost = Util.cortar(idProducto, 2, false);
            idProductoHost = Util.autocompletar(Util.cortar(idProducto, idProducto.length() - 2, true), '0', 4, false);
        }
        Bnchf14CentroHipotecario bnchf14CentroHipotecario = new Bnchf14CentroHipotecario();
        bnchf14CentroHipotecario.setF14IdHipotecario(Util.parseLong(idCH));
        Bnchf20Producto bnchf20Producto = new Bnchf20Producto();
        bnchf20Producto.setF20IdProducto(Util.parseLong(idProducto));
        Bnchf54ParametroProducto bnchf54ParametroProducto = new Bnchf54ParametroProducto();
        Bnchf54ParametroProductoId bnchf54ParametroProductoId = new Bnchf54ParametroProductoId();
        bnchf54ParametroProductoId.setBnchf14CentroHipotecario(bnchf14CentroHipotecario);
        bnchf54ParametroProductoId.setBnchf20Producto(bnchf20Producto);
        bnchf54ParametroProducto.setId(bnchf54ParametroProductoId);
        try {
            for (int i = 1; i <= 4; i++) {
                Bnchf54ParamReqsDesgrav bnchf54ParamReqsDesgrav = new Bnchf54ParamReqsDesgrav();
                Bnchf54ParamReqsDesgravId bnchf54ParamReqsDesgravId = new Bnchf54ParamReqsDesgravId();
                bnchf54ParamReqsDesgravId.setBnchf54ParametroProducto(bnchf54ParametroProducto);
                bnchf54ParamReqsDesgrav.setId(bnchf54ParamReqsDesgravId);
                BnchfDesgravamen bnchfDesgravamen = new BnchfDesgravamen();
                if (lstDesgrav != null && lstDesgrav.get(i - 1) != null) {
                    bnchfDesgravamen = (BnchfDesgravamen) lstDesgrav.get(i - 1);
                }
                if (i == 1) {
                    bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(1));
                    bnchf54ParamReqsDesgrav.setF54IdRngoEdad("1");
                    bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(bnchfDesgravamen.getF49Tasa()));
                } else if (i == 2) {
                    bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(1));
                    bnchf54ParamReqsDesgrav.setF54IdRngoEdad("2");
                    bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(bnchfDesgravamen.getF49Tasa()));
                } else if (i == 3) {
                    bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(2));
                    bnchf54ParamReqsDesgrav.setF54IdRngoEdad("1");
                    bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(bnchfDesgravamen.getF49Tasa()));
                } else {
                    bnchf54ParamReqsDesgravId.setF54IdDesgrav(new Long(2));
                    bnchf54ParamReqsDesgrav.setF54IdRngoEdad("2");
                    bnchf54ParamReqsDesgrav.setF54PorcDesgrav(Util.parseDouble(bnchfDesgravamen.getF49Tasa()));
                }
                boolean existe = false;
                if (parametroService.getDesgravamenXId(bnchf54ParamReqsDesgrav) != null) existe = true;
                if (existe) {
                    bnchf54ParamReqsDesgrav.setF54IdUsuaModi(datosSesion.getCodigoHost());
                    parametroService.updateDesgravamen(bnchf54ParamReqsDesgrav);
                } else {
                    bnchf54ParamReqsDesgrav.setF54IdUsuaCrea(datosSesion.getCodigoHost());
                    parametroService.insertDesgravamen(bnchf54ParamReqsDesgrav);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @return Devuelve commandName.
	 */
    public String getCommandName() {
        return commandName;
    }

    /**
	 * @param commandName El commandName a establecer.
	 */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
	 * @return Devuelve commandClass.
	 */
    public Class getCommandClass() {
        return commandClass;
    }

    /**
	 * @param commandClass El commandClass a establecer.
	 */
    public void setCommandClass(Class commandClass) {
        this.commandClass = commandClass;
    }

    /**
	 * @return Devuelve servicioMantenimiento.
	 */
    public MantenimientoService getServicioMantenimiento() {
        return servicioMantenimiento;
    }

    /**
	 * @param servicioMantenimiento El servicioMantenimiento a establecer.
	 */
    public void setServicioMantenimiento(MantenimientoService servicioMantenimiento) {
        this.servicioMantenimiento = servicioMantenimiento;
    }

    /**
	 * @return Devuelve servicioSelect.
	 */
    public SelectService getServicioSelect() {
        return servicioSelect;
    }

    /**
	 * @param servicioSelect El servicioSelect a establecer.
	 */
    public void setServicioSelect(SelectService servicioSelect) {
        this.servicioSelect = servicioSelect;
    }

    public Object getCommandObject(HttpServletRequest request) throws Exception {
        Object command = formBackingObject(request);
        bind(request, command);
        return command;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return commandClass.newInstance();
    }

    /**
	 * @return Devuelve mantenimientoService.
	 */
    public MantenimientoService getMantenimientoService() {
        return mantenimientoService;
    }

    /**
	 * @param mantenimientoService El mantenimientoService a establecer.
	 */
    public void setMantenimientoService(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    /**
	 * @return Devuelve parametroService.
	 */
    public ParametroService getParametroService() {
        return parametroService;
    }

    /**
	 * @param parametroService El parametroService a establecer.
	 */
    public void setParametroService(ParametroService parametroService) {
        this.parametroService = parametroService;
    }
}
