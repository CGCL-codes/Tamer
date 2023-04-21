package calendario.avisos;

import aviso.AvisoParametros;
import aviso.AvisoUtil;
import aviso.AvisoVO;
import calendario.CalendarioUtil;
import calendario.Semana;
import utilidad.clasesBase.*;
import java.util.*;
import utilidad.ComparadorEnteros;
import utilidad.Util;

public class CalendarioAvisosUtil extends BaseUtil {

    public static List<AvisoVO> getListaAvisosDia(int dia, List<AvisoVO> listaAvisos) {
        Collections.sort(listaAvisos, new ComparadorEnteros("aviso.AvisoVO", "getDia"));
        List<AvisoVO> lista = new ArrayList<AvisoVO>();
        for (AvisoVO aviso : listaAvisos) {
            if (Util.getDia(aviso.getFecha()) == dia) {
                lista.add(aviso);
            }
            if (dia < Util.getDia(aviso.getFecha())) {
                break;
            }
        }
        return lista;
    }

    public static List<DiaAvisos> getListaDias(int mes, int ano) {
        List<DiaAvisos> lista = new ArrayList<DiaAvisos>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.YEAR, ano);
        while (cal.get(Calendar.MONTH) == mes) {
            DiaAvisos diaAvisos = new DiaAvisos();
            diaAvisos.setFecha(cal.getTime());
            lista.add(diaAvisos);
            cal.add(Calendar.DATE, 1);
        }
        return lista;
    }

    public static List<Semana> generarSemanas(CalendarioAvisosParametros parametros) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parametros.fecha);
        AvisoParametros param = new AvisoParametros();
        param.fechaDesde = Util.getPrimerDiaFecha(parametros.fecha);
        param.fechaHasta = Util.getUltimoDiaFecha(parametros.fecha);
        List<AvisoVO> listaAvisos = AvisoUtil.listado(param);
        List<DiaAvisos> listaDiasAvisos = getListaDias(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
        for (DiaAvisos diaAvisos : listaDiasAvisos) {
            diaAvisos.setListaAvisos(getListaAvisosDia(diaAvisos.getDia(), listaAvisos));
        }
        List<Semana> listaSemanas = CalendarioUtil.getListaSemanas(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
        for (Semana semana : listaSemanas) {
            semana.setDiasAvisos(listaDiasAvisos);
        }
        return listaSemanas;
    }
}
