package reserva;

import foto.FotoUtil;
import foto.FotoVO;
import utilidad.clasesBase.*;
import java.util.*;
import utilidad.Util;

public class ReservaUtilWeb extends BaseUtil {

    public static int NumRegistrosPagina = 25;

    public static String divListadoReservas(int pagina, ReservaParametros paramReservas) {
        String html = "";
        ArrayList<ReservaVO> listado = ReservaUtil.listado(paramReservas);
        html = divListadoReservas(pagina, listado);
        return html;
    }

    public static String divListadoReservas(int pagina, ArrayList<ReservaVO> listado) {
        String html = "";
        html += "<div>";
        int totalRegistros = listado.size();
        int totalPaginas = totalRegistros / ReservaUtilWeb.NumRegistrosPagina;
        if ((totalPaginas * ReservaUtilWeb.NumRegistrosPagina) < totalRegistros) {
            totalPaginas++;
        }
        int desde = pagina * ReservaUtilWeb.NumRegistrosPagina;
        if (desde > totalRegistros) {
            desde = totalRegistros;
        }
        int hasta = (pagina + 1) * ReservaUtilWeb.NumRegistrosPagina;
        ;
        if (hasta > totalRegistros) {
            hasta = totalRegistros;
        }
        List<ReservaVO> listaPaginada = listado.subList(desde, hasta);
        for (ReservaVO reserva : listaPaginada) {
            FotoVO fotoPpl = FotoUtil.getPrimeraFotoPpl(FotoUtil.ENTIDAD_INSTALACION, reserva.getIdInstalacion());
            html += " <div id='centerContainerReserva'>";
            html += "<div id='topContainerReserva'>";
            html += "  <div id='nameReserva'>";
            html += "<span class='textoGenericoTitulos'>" + reserva.getNombreUsuarioCompleto() + ":   " + reserva.getNombreInstalacion() + "</span>";
            html += "</div>";
            html += "</div>";
            html += "<div id='datosContainerReserva'>";
            html += "<div id='fila1'><span class='textoGenericoTitulos'>Estado </span></div><div id='fila1'><span class='textoGenericoTitulos'>Fecha </span></div><div id='fila1'><span class='textoGenericoTitulos'>Hora Inicio </span></div><div id='fila1'><span class='textoGenericoTitulos'>Hora Fin </span></div><div id='fila1'><span class='textoGenericoTitulos'>Precio </span></div>";
            html += "<div id='fila1'><span class='textoGenericoTitulos'>" + reserva.getEstado() + " </span></div><div id='fila1'><span class='textoGenericoTitulos'>" + Util.fechaToString(reserva.getFecha()) + " </span></div><div id='fila1'><span class='textoGenericoTitulos'>" + Util.horaSimpleToString(reserva.getHoraInicio()) + " </span></div><div id='fila1'><span class='textoGenericoTitulos'>" + Util.horaSimpleToString(reserva.getHoraFin()) + " </span></div><div id='fila1'><span class='textoGenericoTitulos'>" + reserva.getImporteReserva() + " </span></div>";
            html += "</div>";
            html += " </div> ";
        }
        html += "<div>";
        for (int p = 0; p < totalPaginas; p++) {
            html += "<a class='textoEnlaces' href='javascript:irPagina(" + p + ")'>";
            if (pagina == p) {
                html += "<b><i>" + p + "</i></b>";
            } else {
                html += "" + p + "";
            }
            html += "</a>";
            if (p != (totalPaginas - 1)) {
                html += ",";
            }
        }
        html += "</div>";
        html += "</div>";
        return html;
    }
}
