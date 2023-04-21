package br.com.srv.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import br.com.srv.componentes.cerca.util.PontosTOComparator;
import br.com.srv.componentes.cerca.util.PontosTODescricaoComparator;
import br.com.srv.model.CercaTO;
import br.com.srv.model.PontoTO;
import br.com.srv.model.VeiculoTO;

public class GoogleMapsAction extends BaseAction {

    private final String POPUP_MAP = "popup_map";

    private final String POPUP_MAP_CAD_PERIMETRO = "popup_map_cad_perimetro";

    private final String POPUP_MAP_CAD_ROTA = "popup_map_cad_rota";

    private final String IFRAME_GMAP_MONITOR_CERCA = "iframe_gmap_monitor_cerca";

    private ResourceBundle rb = ResourceBundle.getBundle("configuracoes");

    @SuppressWarnings("unchecked")
    public ActionForward gerarScript(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaValidatorForm frm = (DynaValidatorForm) form;
        Map<String, Object> params = frm.getMap();
        String latitude = (String) params.get("latitude");
        String longitude = (String) params.get("longitude");
        String descricao = (String) params.get("descricao");
        String mapsKey = rb.getString("googlemaps.key");
        String mapsZoom = rb.getString("googlemaps.zoom");
        request.setAttribute("latitude", latitude);
        request.setAttribute("longitude", longitude);
        request.setAttribute("descricao", descricao);
        request.setAttribute("mapsKey", mapsKey);
        request.setAttribute("mapsZoom", mapsZoom);
        return mapping.findForward(POPUP_MAP);
    }

    public ActionForward gerarCadPerimetro(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mapsKey = rb.getString("googlemaps.key");
        request.setAttribute("mapsKey", mapsKey);
        String x1 = request.getParameter("x1");
        String x2 = request.getParameter("x2");
        String x3 = request.getParameter("x3");
        String x4 = request.getParameter("x4");
        String y1 = request.getParameter("y1");
        String y2 = request.getParameter("y2");
        String y3 = request.getParameter("y3");
        String y4 = request.getParameter("y4");
        if (x1 != null && !x1.equals("")) {
            request.setAttribute("x1", x1);
            request.setAttribute("y1", y1);
        }
        if (x2 != null && !x2.equals("")) {
            request.setAttribute("x2", x2);
            request.setAttribute("y2", y2);
        }
        if (x3 != null && !x3.equals("")) {
            request.setAttribute("x3", x3);
            request.setAttribute("y3", y3);
        }
        if (x4 != null && !x1.equals("")) {
            request.setAttribute("x4", x4);
            request.setAttribute("y4", y4);
        }
        return mapping.findForward(POPUP_MAP_CAD_PERIMETRO);
    }

    public ActionForward gerarCadRota(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mapsKey = rb.getString("googlemaps.key");
        request.setAttribute("mapsKey", mapsKey);
        String pontosX = request.getParameter("pontosX");
        String pontosY = request.getParameter("pontosY");
        String pontosCod = request.getParameter("pontosCod");
        String pontosId = request.getParameter("pontosId");
        if (pontosX != null && !pontosX.equals("")) {
            String[] arrayX = pontosX.split(",");
            String[] arrayY = pontosY.split(",");
            String[] arrayDes = pontosCod.split(",");
            String[] arrayId = null;
            if (pontosId != null && !pontosId.equals("")) {
                arrayId = pontosId.split(",");
            }
            List<PontoTO> pontos = new ArrayList<PontoTO>();
            for (int i = 0; i < arrayDes.length; i++) {
                PontoTO pontoTO = new PontoTO();
                pontoTO.setCodigo(arrayDes[i]);
                pontoTO.setLongitude(new Double(arrayX[i]));
                pontoTO.setLatitude(new Double(arrayY[i]));
                if (arrayId != null && (arrayId[i] != null && !arrayId[i].equals(""))) {
                    pontoTO.setId(new Integer(arrayId[i]));
                }
                pontos.add(pontoTO);
            }
            Collections.sort(pontos, new PontosTOComparator());
            request.setAttribute("pontos", pontos);
        }
        String pontosXFis = request.getParameter("pontosXFis");
        String pontosYFis = request.getParameter("pontosYFis");
        String pontosCodFis = request.getParameter("pontosCodFis");
        String pontosDesFis = request.getParameter("pontosDesFis");
        String pontosIdFis = request.getParameter("pontosIdFis");
        if (pontosDesFis == null) {
            pontosDesFis = ",";
        }
        if (pontosXFis != null && !pontosXFis.equals("")) {
            String[] arrayX = pontosXFis.split(",");
            String[] arrayY = pontosYFis.split(",");
            String[] arrayCod = pontosCodFis.split(",");
            String[] arrayDes = pontosDesFis.split(",");
            String[] arrayId = null;
            if (pontosIdFis != null && !pontosIdFis.equals("")) {
                arrayId = pontosIdFis.split(",");
            }
            List<PontoTO> pontosFis = new ArrayList<PontoTO>();
            for (int i = 0; i < arrayX.length; i++) {
                PontoTO pontoTO = new PontoTO();
                pontoTO.setDescricao(arrayDes[i]);
                pontoTO.setLongitude(new Double(arrayX[i]));
                pontoTO.setLatitude(new Double(arrayY[i]));
                if ((arrayId != null) && (arrayId.length > i) && (arrayId[i] != null && !arrayId[i].equals(""))) {
                    pontoTO.setId(new Integer(arrayId[i]));
                }
                if ((arrayCod != null) && (arrayCod.length > i) && (arrayCod[i] != null && !arrayCod[i].equals(""))) {
                    pontoTO.setCodigo(arrayCod[i]);
                }
                pontosFis.add(pontoTO);
            }
            request.setAttribute("pontosFis", pontosFis);
        }
        return mapping.findForward(POPUP_MAP_CAD_ROTA);
    }

    public ActionForward gerarMonitorCerca(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mapsKey = rb.getString("googlemaps.key");
        request.setAttribute("mapsKey", mapsKey);
        CercaTO cercaTO = (CercaTO) request.getAttribute("cercaResult");
        if (cercaTO != null) {
            Set<PontoTO> pontos = cercaTO.getPontos();
            List<PontoTO> pontosOrdenados = new ArrayList<PontoTO>();
            pontosOrdenados.addAll(pontos);
            Collections.sort(pontosOrdenados, new PontosTODescricaoComparator());
            int count = 1;
            for (PontoTO pontoTO : pontosOrdenados) {
                String x = pontoTO.getLongitude().toString();
                String y = pontoTO.getLatitude().toString();
                if (x != null && !x.equals("")) {
                    request.setAttribute("x" + count, x);
                    request.setAttribute("y" + count, y);
                }
                count++;
            }
            List<VeiculoTO> veiculos = cercaTO.getVeiculos();
            request.setAttribute("veiculos", veiculos);
            request.setAttribute("cercaId", cercaTO.getId());
        }
        return mapping.findForward(IFRAME_GMAP_MONITOR_CERCA);
    }
}
