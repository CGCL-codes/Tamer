package modelo_objetos.recibocaja;

import eventos.IDocumento;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author carlos
 */
public class DataSourceRecibo implements JRDataSource {

    private List<IDocumento> documentos;

    private IDocumento docSeleccionado;

    private int i;

    /** Creates a new instance of DataSourceRecibo */
    public DataSourceRecibo(List<IDocumento> lista) {
        documentos = lista;
    }

    public boolean next() throws JRException {
        if (i < documentos.size()) {
            docSeleccionado = documentos.get(i);
            i++;
            return true;
        } else {
            return false;
        }
    }

    public Object getFieldValue(JRField jRField) throws JRException {
        String campo = jRField.getName();
        if (campo.equals("documento")) {
            return docSeleccionado.getDescripcionDocumento();
        }
        if (campo.equals("montoDocumento")) {
            return docSeleccionado.getMontoDocumento() + "";
        }
        if (campo.equals("montoPendiente")) {
            return docSeleccionado.getMontoPendiente() + "";
        }
        if (campo.equals("montoPagado")) {
            return docSeleccionado.getMontoPagado() + "";
        }
        return null;
    }
}
