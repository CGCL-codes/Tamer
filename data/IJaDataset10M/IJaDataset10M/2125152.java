package norma.norma19;

import centroGestion.CentroGestionUtil;
import centroGestion.CentroGestionVO;
import cuota.CuotaParametros;
import cuota.CuotaUtil;
import cuota.CuotaVO;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import norma.NormaUtil;
import remesaBancaria.RemesaBancariaVO;
import utilidad.MiExcepcion;
import utilidad.Util;
import utilidad.clasesBase.BaseUtil;

public class Norma19 extends BaseUtil {

    private static final long serialVersionUID = 1L;

    private RemesaBancariaVO remesa;

    private Date fechaCargo;

    private String sufijoPresentador;

    private String sufijoOrdenante;

    private String codigoAlfanumerico;

    private String codRefInterna;

    private String codDevoluciones;

    private CabeceraPresentador objCabeceraPresentador = new CabeceraPresentador();

    private CabeceraOrdenante objCabeceraOrdenante = new CabeceraOrdenante();

    private List<RegistrosIndividualesObligatorios> listaRegistrosIndividuales = new ArrayList();

    private TotalClienteOrdenante objTotalOrdenante = new TotalClienteOrdenante();

    private TotalGeneral objTotalGeneral = new TotalGeneral();

    public Norma19(RemesaBancariaVO remesa) {
        try {
            inicializarComponetes(remesa);
        } catch (Exception ex) {
        }
    }

    private void crearPresentador() {
        objCabeceraPresentador.setCodReg("51");
        objCabeceraPresentador.setCodDato("80");
        objCabeceraPresentador.setNif(remesa.getCifCentroGestion());
        objCabeceraPresentador.setSufijo(sufijoPresentador);
        objCabeceraPresentador.setFechaSoporte(remesa.getFecha());
        objCabeceraPresentador.setNombrePresentador(remesa.getNombreCentroGestion());
        objCabeceraPresentador.setEntidadReceptora(remesa.getBanco());
        objCabeceraPresentador.setOficinaReceptora(remesa.getSucursal());
        objCabeceraPresentador.setCodigoAlfanumerico(codigoAlfanumerico);
    }

    private void crearOrdenante() {
        objCabeceraOrdenante.setCodReg("53");
        objCabeceraOrdenante.setCodDato("80");
        objCabeceraOrdenante.setNif(remesa.getCifCentroGestion());
        objCabeceraOrdenante.setSufijo(sufijoOrdenante);
        objCabeceraOrdenante.setFechaSoporte(remesa.getFecha());
        objCabeceraOrdenante.setFechaCargo(fechaCargo);
        objCabeceraOrdenante.setNombreOrdenante(remesa.getNombreCentroGestion());
        objCabeceraOrdenante.setCCCAbono(remesa.getNumeroCuentaCompleto());
        objCabeceraOrdenante.setProcedimiento("01");
    }

    private void crearListadoRegistrosIndividuales() throws MiExcepcion {
        listaRegistrosIndividuales.clear();
        CuotaParametros param = new CuotaParametros();
        param.idRemesa = remesa.getId();
        param.ordenSQL = "cuentasbancarias.banco ASC, cuentasbancarias.sucursal ASC, usuarios.id ASC";
        ArrayList<CuotaVO> listaCuotas = CuotaUtil.listado(param);
        for (CuotaVO cuota : listaCuotas) {
            if (cuota.getIdCuentaBancaria() == -1) {
                MiExcepcion miEx = new MiExcepcion("La Cuota Con Importe '" + cuota.getImporte() + "' NO Tiene Asignado Una Cuenta Bancaria.");
            }
            crearRegistrosIndividuales(cuota);
        }
    }

    private void crearRegistrosIndividuales(CuotaVO cuota) throws MiExcepcion {
        try {
            RegistrosIndividualesObligatorios objRegIndObligatorios = new RegistrosIndividualesObligatorios();
            objRegIndObligatorios.objIndividualObligatorio.setCodReg("56");
            objRegIndObligatorios.objIndividualObligatorio.setCodDato("80");
            objRegIndObligatorios.objIndividualObligatorio.setNif(remesa.getCifCentroGestion());
            objRegIndObligatorios.objIndividualObligatorio.setSufijo(sufijoOrdenante);
            objRegIndObligatorios.objIndividualObligatorio.setCodReferencia(Util.ObjetToString(cuota.getIdUsuario()));
            objRegIndObligatorios.objIndividualObligatorio.setNombreTitular(cuota.getTitularCB());
            objRegIndObligatorios.objIndividualObligatorio.setCccAdeudo(cuota.getNumeroCuentaCompleto());
            objRegIndObligatorios.objIndividualObligatorio.setImporte(Util.transformaBigDecimal(cuota.getImporte(), "0"));
            objRegIndObligatorios.objIndividualObligatorio.setCodDevoluciones(codDevoluciones);
            objRegIndObligatorios.objIndividualObligatorio.setCodRefInterna(codRefInterna);
            String actividad = Util.pad(cuota.getNombreActividadInscripcion(), 24, " ");
            String fechaActividad = Util.pad(Util.fechaMesToString(cuota.getFechaInicio()), 7, " ");
            String importeActividad = Util.pad(Util.floatToString(cuota.getImporte()), 7, " ");
            objRegIndObligatorios.objIndividualObligatorio.setConcepto1(actividad + " " + fechaActividad + " " + importeActividad);
            this.listaRegistrosIndividuales.add(objRegIndObligatorios);
        } catch (Exception ex) {
            throw new MiExcepcion("Excepcion Al CrearRegistrosIndividuales: Recibo con Importe: " + cuota.getImporte() + ". " + ex.toString());
        }
    }

    private void crearTotalOrdenante() {
        objTotalOrdenante.setCodReg("58");
        objTotalOrdenante.setCodDato("80");
        objTotalOrdenante.setNif(remesa.getCifCentroGestion());
        objTotalOrdenante.setSufijo(sufijoOrdenante);
        objTotalOrdenante.setSumaImportesOrdenante(Util.transformaBigDecimal(remesa.getImporteCuotas(), "0"));
        objTotalOrdenante.setNumDomiciliacionesOrdenante(remesa.getNumeroCuotas().toString());
        Integer numeroCuotas = ((remesa.getNumeroCuotas() * 1) + 2);
        objTotalOrdenante.setNumTotalRegistrosOrdenante(numeroCuotas.toString());
    }

    private void crearTotalGeneral() {
        objTotalGeneral.setCodReg("59");
        objTotalGeneral.setCodDato("80");
        objTotalGeneral.setNif(remesa.getCifCentroGestion());
        objTotalGeneral.setSufijo(sufijoPresentador);
        objTotalGeneral.setNumOrdenantes("1");
        objTotalGeneral.setSumaTotalImportes(Util.transformaBigDecimal(remesa.getImporteCuotas(), "0"));
        objTotalGeneral.setNumTotalDomiciliaciones(remesa.getNumeroCuotas().toString());
        Integer numeroCuotas = ((remesa.getNumeroCuotas() * 1) + 2 + 2);
        objTotalGeneral.setNumTotalRegistrosFichero(numeroCuotas.toString());
    }

    public void inicializarComponetes(RemesaBancariaVO remesaB) throws MiExcepcion {
        remesa = remesaB;
        fechaCargo = remesaB.getFecha();
        CentroGestionVO centroGestion = CentroGestionUtil.buscarCentroGestion(remesaB.getIdCentroGestion());
        sufijoPresentador = centroGestion.getSufijoPresentador();
        sufijoOrdenante = centroGestion.getSufijoOrdenante();
        codigoAlfanumerico = centroGestion.getCodigoAlfanumerico();
        codRefInterna = centroGestion.getCodRefInterna();
        codDevoluciones = centroGestion.getCodDevoluciones();
        crearPresentador();
        crearOrdenante();
        crearListadoRegistrosIndividuales();
        crearTotalOrdenante();
        crearTotalGeneral();
    }

    public void escribirFichero(FileWriter wText) {
        try {
            BufferedWriter out = new BufferedWriter(wText);
            out.write(objCabeceraPresentador.formatoLinea());
            out.newLine();
            out.write(objCabeceraOrdenante.formatoLinea());
            for (RegistrosIndividualesObligatorios objRegIndOb : listaRegistrosIndividuales) {
                out.newLine();
                objRegIndOb.escribirFichero(wText, out);
            }
            out.newLine();
            out.write(objTotalOrdenante.formatoLinea());
            out.newLine();
            out.write(objTotalGeneral.formatoLinea());
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
