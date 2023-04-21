package es.unav.informesgoogleanalytics.modelo.secciones;

import es.unav.informesgoogleanalytics.modelo.*;
import es.unav.informesgoogleanalytics.Utils;
import es.unav.informesgoogleanalytics.controladores.escuchadores.BitácoraEscuchador;
import java.util.Iterator;

/** 
 * Esta clase abstracta es la base para toda clase concreta que tiene 
 * la tarea de producir una presentación de las páginas más visitadas. Se hereda por clases como
 * CSVPáginasMásVisitadasSecciónInforme, que produce sus resultados en formato CSV.
 */
public class PáginasMásVisitadasSecciónInforme extends SecciónInforme {

    public PáginasMásVisitadasSecciónInforme(Informe p) {
        padre = p;
    }

    public void ejecutar() throws Exception {
        emitirComienzoMensaje(Utils.getMensajeLocalizado("PáginasMásVisitadasSecciónInforme.título"));
        String valoresCadena = Utils.getMensajeLocalizado("PáginasMásVisitadasSecciónInforme.título") + ":\n";
        for (Iterator<String> i = padre.getDatosDePáginas().getRutasOrdenadas(20).iterator(); i.hasNext(); ) {
            String ruta = i.next();
            int total = padre.getDatosDePáginas().getTotalPorRuta(ruta);
            valoresCadena += ruta + ": " + total + "\n";
        }
        padre.emitirMensajeBitácora(valoresCadena, BitácoraEscuchador.NIVEL_DEPURACIÓN);
    }
}
