package filtro;

import utilidad.vo.ColumnaTablaVO;
import utilidad.clasesBase.*;
import utilidad.componentes.*;
import java.util.*;
import utilidad.Util;

public class ListadoFiltros extends BaseListado {

    public ListadoFiltros() {
        super();
        inicializarForm();
    }

    public ListadoFiltros(String modo) {
        super();
        this.modo = modo;
        inicializarForm();
    }

    public ListadoFiltros(String modo, FiltroParametros parametros) {
        super();
        this.modo = modo;
        if (parametros != null) this.parametros = parametros;
        inicializarForm();
    }

    public ListadoFiltros(String modo, FiltroParametros parametros, FiltroParametros parametrosFijos) {
        super();
        this.modo = modo;
        if (parametros != null) this.parametros = parametros;
        this.parametrosFijos = parametrosFijos;
        inicializarForm();
    }

    public ListadoFiltros(String modo, ArrayList<ColumnaTablaVO> listaCols, FiltroParametros parametros, FiltroParametros parametrosFijos) {
        super();
        this.modo = modo;
        this.listaColumnas = listaCols;
        if (parametros != null) this.parametros = parametros;
        this.parametrosFijos = parametrosFijos;
        inicializarForm();
    }

    public void inicializarComponentes() {
        ajustarTamaño(800, 600);
        this.setTitle("Listado Filtros");
        jLTitulo.setText("Listado Filtros");
        if (parametrosFijos != null) {
            if ((parametrosFijos.plantilla != null) && parametrosFijos.plantilla) {
                this.setTitle("Plantillas de Filtros");
                jLTitulo.setText("Plantillas de  Filtros");
            }
        }
        jBBuscar.setVisible(false);
        jTFBusqueda.setVisible(true);
    }

    public void crearModelo() {
        this.modelo = new MiTableModel("filtro.FiltroVO", this.modo);
    }

    public void cargarListaObjetos() {
        this.parametros.asignarValoresFijos(this.parametrosFijos);
        ArrayList listaFiltros = FiltroUtil.listado(this.parametros);
        modelo.setListaObjetos(listaFiltros);
    }

    public void calcularTotales() {
        jLError.setText("Existen " + modelo.getRowCount() + " Filtro(s)");
    }

    public void cargarListaColumnas() {
        if (listaColumnas == null) {
            listaColumnas = FiltroUtil.listadoColumnasLE();
        }
        modelo.setListaColumnas(listaColumnas);
    }

    public void buscar() {
    }

    @Override
    protected void busquedaGenerica() {
        this.parametros.clases = jTFBusqueda.getText();
        cargarListaObjetos();
    }

    public void modificar() {
        FiltroVO filtroSel = (FiltroVO) this.getObjSeleccionadoTabla();
        if (filtroSel != null) {
            DatosFiltro frm = new DatosFiltro(filtroSel, parametrosFijos, jTable1);
            Util.mostrarVentana(frm);
        }
    }

    public void nuevo() {
        FiltroVO filtro = new FiltroVO();
        if (this.parametrosFijos != null) {
            if (parametrosFijos.tipo != null) filtro.setTipo(parametrosFijos.tipo);
            if (parametrosFijos.plantilla != null) filtro.setPlantilla(parametrosFijos.plantilla);
        }
        DatosFiltro frm = new DatosFiltro(filtro, parametrosFijos, jTable1);
        Util.mostrarVentana(frm);
    }

    private FiltroParametros parametros = new FiltroParametros();

    private FiltroParametros parametrosFijos = null;

    private ArrayList<ColumnaTablaVO> listaColumnas = null;
}
