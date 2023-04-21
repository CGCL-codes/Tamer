package AdaptadorSistemaReportes;

import DTO.DTOFallaTecnica;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author diego
 */
public class FallasTecnicasDataSource implements JRDataSource {

    private int indicadorOrdenActual = -1;

    private List<DTOFallaTecnica> listaFallas;

    public FallasTecnicasDataSource() {
    }

    public FallasTecnicasDataSource(List<DTOFallaTecnica> nuevaListaDtoFallas) {
        listaFallas = new ArrayList<DTOFallaTecnica>();
        listaFallas.addAll(nuevaListaDtoFallas);
    }

    public void addFalla(DTOFallaTecnica nuevaFalla) {
        if (listaFallas == null) {
            listaFallas = new ArrayList<DTOFallaTecnica>();
        }
        listaFallas.add(nuevaFalla);
    }

    public void addAllFallas(List<DTOFallaTecnica> nuevaLista) {
        if (listaFallas == null) {
            listaFallas = new ArrayList<DTOFallaTecnica>();
        }
        listaFallas.addAll(nuevaLista);
    }

    public boolean next() throws JRException {
        ++indicadorOrdenActual;
        if (indicadorOrdenActual < listaFallas.size()) {
            return true;
        } else {
            return false;
        }
    }

    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;
        if (jrf.getName().equals("codFalla")) {
            valor = listaFallas.get(indicadorOrdenActual).getCodigoFalla();
        } else if (jrf.getName().equals("nombreFalla")) {
            valor = listaFallas.get(indicadorOrdenActual).getNombreFalla();
        }
        return valor;
    }
}
