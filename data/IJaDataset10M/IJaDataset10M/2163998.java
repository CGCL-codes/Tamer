package es.caib.regtel.plugincaib.persistence.dao.registro;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.caib.regtel.plugincaib.model.DatosRegistroEntrada;
import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.util.StringUtil;

public class FakeRegistroDAO extends RegistroDAO {

    private static Log log = LogFactory.getLog(FakeRegistroDAO.class);

    private static String FAKE_OFICINA = "14";

    public DatosRegistroEntrada grabar(DatosRegistroEntrada entrada, boolean validarOficina) {
        log.debug("Validar oficina [" + validarOficina + "]");
        entrada.setNumeroEntrada(String.valueOf(System.currentTimeMillis()));
        entrada.setAnoEntrada(StringUtil.fechaACadena(new Date(), "yyyy"));
        log.debug(entrada);
        return entrada;
    }

    public DatosRegistroEntrada obtenerRegistroEntrada(String usuario, String oficina, String numeroEntrada, String anyoEntrada) throws Exception {
        DatosRegistroEntrada entrada = new DatosRegistroEntrada();
        entrada.setNumeroEntrada(numeroEntrada);
        entrada.setAnoEntrada(anyoEntrada);
        entrada.setOficina(oficina);
        log.debug("usuario " + usuario + " oficina " + oficina + " numeroEntrada " + numeroEntrada + " anyoEntrada " + anyoEntrada);
        return entrada;
    }

    public DatosRegistroSalida grabar(DatosRegistroSalida salida, boolean validarOficina) throws Exception {
        log.debug("Validar oficina [" + validarOficina + "]");
        salida.setNumeroSalida(String.valueOf(System.currentTimeMillis()));
        salida.setAnoSalida(StringUtil.fechaACadena(new Date(), "yyyy"));
        log.debug(salida);
        return salida;
    }

    public DatosRegistroSalida obtenerRegistroSalida(String usuario, String oficina, String numeroEntrada, String anyoEntrada) throws Exception {
        DatosRegistroSalida salida = new DatosRegistroSalida();
        salida.setNumeroSalida(numeroEntrada);
        salida.setAnoSalida(anyoEntrada);
        salida.setOficina(oficina);
        log.debug("usuario " + usuario + " oficina " + oficina + " numeroEntrada " + numeroEntrada + " anyoEntrada " + anyoEntrada);
        return salida;
    }

    public void anularRegistroEntrada(String usuario, String oficina, String numeroEntrada, String anyoEntrada, boolean validarOficina) throws Exception {
        log.debug("Anular entrada [" + "usuario " + usuario + " oficina " + oficina + " numeroEntrada " + numeroEntrada + " anyoEntrada " + anyoEntrada + "]");
    }
}
