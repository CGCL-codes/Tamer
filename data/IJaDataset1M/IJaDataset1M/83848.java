package inscripcion;

import inscripcion.dao.InscripcionDAO;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.SwingConstants;
import utilidad.Ctes;
import utilidad.ModalFrameUtil;
import utilidad.clasesBase.BaseUtil;
import utilidad.clasesBase.BaseVO;
import utilidad.vo.ColumnaTablaVO;
import utilidad.Util;
import cuota.CuotaUtil;
import cuota.CuotaVO;
import grupo.GrupoVO;
import inicio.InicioApp;
import java.util.Date;

public class InscripcionUtil extends BaseUtil {

    public static ResourceBundle bundle = ResourceBundle.getBundle("inscripcion/Bundle");

    public static String ESTADO_ANULADA = "ANULADA";

    public static String ESTADO_BAJA = "BAJA";

    public static String ESTADO_RESERVA = "RESERVA";

    public static String ESTADO_ACTIVA = "ACTIVA";

    public static String ESTADO_FINALIZADA = "FINALIZADA";

    public static ArrayList<InscripcionVO> listado(InscripcionParametros param) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.listadoInscripciones(param);
    }

    public static ArrayList<InscripcionVO> listadoInscripcionesAgrupadas(int idActividad, Integer idGrupo) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.listadoInscripcionesAgrupadas(idActividad, idGrupo);
    }

    public static ArrayList<InscripcionVO> listadoInscripcionesGruposSeleccionados(String idsGrupos) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.listadoInscripcionesGruposSeleccionados(idsGrupos);
    }

    public static ArrayList<ColumnaTablaVO> listadoColumnasTabla() {
        ArrayList<ColumnaTablaVO> listaColumnas = new ArrayList<ColumnaTablaVO>();
        int i = 0;
        ColumnaTablaVO col = null;
        col = new ColumnaTablaVO("Alta", "getFechaAlta", i++);
        col.setAnchoMin(80);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("NumTarjetaUsuario"), "getNumTarjetaUsuario", i++, SwingConstants.CENTER);
        col.setAnchoMin(70);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Usuario"), "getNombreUsuarioCompleto", i++, SwingConstants.LEFT);
        col.setAnchoMin(225);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Actividad"), "getNombreActividad", i++, SwingConstants.LEFT);
        col.setAnchoMin(190);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Grupo"), "getNombreGrupo", i++, SwingConstants.LEFT);
        col.setAnchoMin(140);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Inicio"), "getFechaInicioActividad", i++);
        col.setAnchoMin(80);
        col.setAnchoMax(80);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Fin"), "getFechaFinActividad", i++);
        col.setAnchoMin(80);
        col.setAnchoMax(80);
        listaColumnas.add(col);
        col = new ColumnaTablaVO(bundle.getString("Estado"), "getEstado", i++);
        col.setAnchoMin(100);
        listaColumnas.add(col);
        return listaColumnas;
    }

    public static InscripcionVO buscarInscripcion(int id) {
        InscripcionVO objInscripcion = new InscripcionVO();
        if (id != -1) {
            InscripcionParametros param = new InscripcionParametros();
            param.idInscripcion = id;
            InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
            ArrayList<InscripcionVO> lista = dao.listadoInscripciones(param);
            if (lista.size() > 0) {
                objInscripcion = lista.get(0);
            }
        }
        return objInscripcion;
    }

    public static int getNumInscripcionesGrupo(int idGrupo, String estado1, String estado2, String estado3) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.getNumInscripcionesGrupo(idGrupo, estado1, estado2, estado3);
    }

    public static int getNumInscripcionesGrupo(int idGrupo, String estado1, String estado2) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.getNumInscripcionesGrupo(idGrupo, estado1, estado2, null);
    }

    public static int getNumInscripcionesGrupo(int idGrupo, String estado1) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.getNumInscripcionesGrupo(idGrupo, estado1, null, null);
    }

    public static InscripcionVO seleccionarInscripcion(java.awt.Frame padre, InscripcionVO inscripcion) {
        return seleccionarInscripcion(padre, inscripcion, null, null);
    }

    public static InscripcionVO seleccionarInscripcion(java.awt.Frame padre, InscripcionVO inscripcion, InscripcionParametros paramIniciales, InscripcionParametros paramFijos) {
        ArrayList<BaseVO> listaInscripcionVOSel = seleccionarListaInscripciones(padre, inscripcion, paramIniciales, paramFijos);
        InscripcionVO inscripcionSel = null;
        if (listaInscripcionVOSel.size() > 0) {
            inscripcionSel = (InscripcionVO) listaInscripcionVOSel.get(0);
        }
        return inscripcionSel;
    }

    public static ArrayList<BaseVO> seleccionarListaInscripciones(java.awt.Frame padre, InscripcionParametros paramIniciales, InscripcionParametros paramFijos) {
        return seleccionarListaInscripciones(padre, null, paramIniciales, paramFijos);
    }

    public static ArrayList<BaseVO> seleccionarListaInscripciones(java.awt.Frame padre, InscripcionVO inscripcion, InscripcionParametros paramIniciales, InscripcionParametros paramFijos) {
        ListadoInscripciones listadoInscripciones = new ListadoInscripciones(Ctes.MODO_TABLA_SELECCIONAR, paramIniciales, paramFijos);
        if (inscripcion != null) {
            listadoInscripciones.seleccionarObjeto(inscripcion);
        }
        ModalFrameUtil.showAsModal(listadoInscripciones, padre);
        return listadoInscripciones.modelo.getListaObjetosSel();
    }

    public static ArrayList<BaseVO> getListaObjetosNoSeleccionados(ArrayList<BaseVO> lista, ArrayList<BaseVO> listaSeleccionados) {
        ArrayList<BaseVO> listaNoSeleccionados = new ArrayList();
        for (BaseVO inscr : lista) {
            if (!listaSeleccionados.contains(inscr)) {
                listaNoSeleccionados.add(inscr);
            }
        }
        return listaNoSeleccionados;
    }

    public static String calcularEstado(InscripcionVO inscripcion) {
        return calcularEstado(inscripcion.getEstado(), inscripcion.getFechaBaja(), inscripcion.esReserva(), inscripcion.getFechaFinActividad());
    }

    public static String calcularEstado(String estado, Date fechaBaja, Boolean esReserva, Date fechaFinActividad) {
        String estadoCalculado = ESTADO_ANULADA;
        if (!ESTADO_ANULADA.equals(estado)) {
            try {
                if (fechaBaja != null) {
                    estadoCalculado = ESTADO_BAJA;
                    throw new Exception();
                }
                if (esReserva) {
                    estadoCalculado = ESTADO_RESERVA;
                    throw new Exception();
                }
                if (Util.compararFechas(Util.getFechaBD(), fechaFinActividad) <= 0) {
                    estadoCalculado = ESTADO_ACTIVA;
                    throw new Exception();
                }
                if (Util.compararFechas(Util.getFechaBD(), fechaFinActividad) > 0) {
                    estadoCalculado = ESTADO_FINALIZADA;
                    throw new Exception();
                }
            } catch (Exception e) {
            }
        }
        return estadoCalculado;
    }

    public static int anularInscripciones(Integer idActividad, Integer idGrupo) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.anularInscripciones(idActividad, idGrupo);
    }

    public static boolean tieneCuotasPtes(ArrayList<CuotaVO> listadoCuotas) {
        boolean tienePtes = false;
        for (CuotaVO cuota : listadoCuotas) {
            if (CuotaUtil.ESTADO_PENDIENTE.equals(cuota.getEstado())) {
                tienePtes = true;
                break;
            }
        }
        return tienePtes;
    }

    public static int getPeriodoMesesCuota(String tipoInscripcion) {
        int meses = 0;
        if (CuotaUtil.TIPO_MENSUAL.equals(tipoInscripcion)) {
            meses = 1;
        }
        if (CuotaUtil.TIPO_BIMENSUAL.equals(tipoInscripcion)) {
            meses = 2;
        }
        if (CuotaUtil.TIPO_TRIMESTRAL.equals(tipoInscripcion)) {
            meses = 3;
        }
        if (CuotaUtil.TIPO_SEMESTRAL.equals(tipoInscripcion)) {
            meses = 6;
        }
        return meses;
    }

    public static Integer insertar(InscripcionVO inscripcion) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.insertar(inscripcion);
    }

    public static Integer actualizar(InscripcionVO inscripcion) {
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.actualizar(inscripcion);
    }

    public static int eliminar(InscripcionVO inscripcion) {
        CuotaUtil.eliminar(CuotaUtil.ENTIDAD_INSCRIPCION, inscripcion.getId());
        InscripcionDAO dao = InicioApp.factoriaDAO.getInscripcionDAO();
        return dao.eliminar(inscripcion);
    }

    public static void eliminar(GrupoVO grupo) {
        InscripcionParametros paramInscripcion = new InscripcionParametros();
        paramInscripcion.idGrupo = grupo.getId();
        ArrayList<InscripcionVO> listaInscripciones = InscripcionUtil.listado(paramInscripcion);
        for (InscripcionVO obj : listaInscripciones) {
            eliminar(obj);
        }
    }
}
