package sicav.jpa.persistencia;

import java.util.Calendar;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sicav.jpa.modelo.Punto;
import sicav.jpa.modelo.PuntosAcumulados;

@Repository
@Transactional(readOnly = true)
public class PuntosAcumuladosServiceImpl implements PuntosAcumuladosService {

    @Override
    public PuntosAcumulados obsequiarPuntos(int numPuntos, PuntosAcumulados lstPuntos) {
        for (int i = 0; i <= numPuntos; i++) {
            Punto punto = new Punto();
            lstPuntos.adicionarPuntos(punto);
        }
        return lstPuntos;
    }

    @Override
    public void eliminarPuntos(String fechaVencimiento, PuntosAcumulados lstPuntos) {
        Calendar fechaActual = Calendar.getInstance();
        String dia = Integer.toString(fechaActual.get(Calendar.DATE));
        String mes = Integer.toString(fechaActual.get(Calendar.MONTH));
        String ano = Integer.toString(fechaActual.get(Calendar.YEAR));
        String fecha = dia + "-" + mes + "-" + ano;
        if (fecha.equals(fechaVencimiento)) {
            lstPuntos.eliminarPuntos();
        } else System.out.println("La vigencia de sus puntos no ha expirado.");
    }
}
