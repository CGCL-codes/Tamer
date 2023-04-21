package municipio;

import utilidad.vo.ColumnaTablaVO;
import utilidad.*;
import utilidad.clasesBase.*;
import utilidad.componentes.*;
import java.util.*;

/**
 *
 * @author Carlos
 */
public class ListadoMunicipios extends BaseListado {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("municipio/Bundle");

    public ListadoMunicipios() {
        super();
        inicializarForm();
    }

    public ListadoMunicipios(String modo) {
        super();
        this.modo = modo;
        inicializarForm();
    }

    public ListadoMunicipios(String modo, MunicipioParametros parametros) {
        super();
        this.modo = modo;
        if (parametros != null) this.parametros = parametros;
        inicializarForm();
    }

    public ListadoMunicipios(String modo, MunicipioParametros parametros, MunicipioParametros parametrosFijos) {
        super();
        this.modo = modo;
        if (parametros != null) this.parametros = parametros;
        this.parametrosFijos = parametrosFijos;
        inicializarForm();
    }

    public void inicializarComponentes() {
        ajustarTamaño(800, 600);
        this.setTitle(bundle.getString("Listado_Municipios"));
        jLTitulo.setText(bundle.getString("Listado_Municipios"));
    }

    public void crearModelo() {
        this.modelo = new MiTableModel("municipio.MunicipioVO", this.modo);
    }

    public void cargarListaObjetos() {
        this.parametros.asignarValoresFijos(this.parametrosFijos);
        ArrayList listaMunicipios = MunicipioUtil.listado(this.parametros);
        modelo.setListaObjetos(listaMunicipios);
    }

    public void calcularTotales() {
        jLError.setText(bundle.getString("Existen_") + modelo.getRowCount() + bundle.getString("_Municipio(s)"));
    }

    public void cargarListaColumnas() {
        ArrayList<ColumnaTablaVO> listaColumnas = MunicipioUtil.listadoColumnasTabla();
        modelo.setListaColumnas(listaColumnas);
    }

    public void buscar() {
        BuscarMunicipio frm = new BuscarMunicipio(this, this.parametrosFijos);
        Util.mostrarVentana(frm);
        if (frm.parametroSel != null) {
            this.parametros = frm.parametroSel;
            cargarListaObjetos();
        }
    }

    public void modificar() {
        MunicipioVO MunicipioSel = (MunicipioVO) this.getObjSeleccionadoTabla();
        if (MunicipioSel != null) {
            DatosMunicipio frm = new DatosMunicipio(MunicipioSel, jTable1);
            Util.mostrarVentana(frm);
        }
    }

    public void nuevo() {
        DatosMunicipio frm = new DatosMunicipio(municipio, jTable1);
        Util.mostrarVentana(frm);
    }

    private MunicipioParametros parametros = new MunicipioParametros();

    private MunicipioParametros parametrosFijos = null;

    private MunicipioVO municipio = new MunicipioVO();
}
