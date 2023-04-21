package vinculacionEducador;

import utilidad.vo.ColumnaTablaVO;
import utilidad.clasesBase.*;
import utilidad.componentes.*;
import java.util.*;

public class ListadoVinculacionesEducador extends BaseListado {

    public ListadoVinculacionesEducador() {
        super();
        inicializarForm();
    }

    public ListadoVinculacionesEducador(String modo) {
        super();
        this.modo = modo;
        inicializarForm();
    }

    public ListadoVinculacionesEducador(String modo, VinculacionEducadorParametros parametros) {
        super();
        this.modo = modo;
        if (parametros != null) this.parametros = parametros;
        inicializarForm();
    }

    public ListadoVinculacionesEducador(String modo, VinculacionEducadorParametros parametros, VinculacionEducadorParametros parametrosFijos, ArrayList<ColumnaTablaVO> listaCols) {
        super();
        this.modo = modo;
        if (parametros != null) this.parametros = parametros;
        this.parametrosFijos = parametrosFijos;
        this.listaColumnas = listaCols;
        inicializarForm();
    }

    public void inicializarComponentes() {
        ajustarTamaño(800, 600);
        this.setTitle("Listado Educadores Vinculados");
        jLTitulo.setText("Listado Educadores Vinculados");
        this.jBBuscar.setVisible(false);
        this.jBNuevo.setVisible(false);
        this.jBModificar.setVisible(false);
    }

    public void crearModelo() {
        this.modelo = new MiTableModel("vinculacionEducador.VinculacionEducadorVO", this.modo);
    }

    public void cargarListaObjetos() {
        this.parametros.asignarValoresFijos(this.parametrosFijos);
        ArrayList listaUnidades = VinculacionEducadorUtil.listado(this.parametros);
        modelo.setListaObjetos(listaUnidades);
    }

    public void calcularTotales() {
        jLError.setText("Existen " + modelo.getRowCount() + " Educador(es)");
    }

    public void cargarListaColumnas() {
        if (listaColumnas == null) {
            listaColumnas = VinculacionEducadorUtil.listadoColumnasTablaGrupo();
        }
        modelo.setListaColumnas(listaColumnas);
    }

    public void buscar() {
    }

    public void modificar() {
    }

    public void nuevo() {
    }

    private VinculacionEducadorParametros parametros = new VinculacionEducadorParametros();

    private VinculacionEducadorParametros parametrosFijos = null;

    private ArrayList<ColumnaTablaVO> listaColumnas = null;
}
