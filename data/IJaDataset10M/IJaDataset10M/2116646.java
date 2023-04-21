package vista.filtros;

import java.util.Iterator;
import java.util.List;
import modelo.entidades.Entidad;
import modelo.entidades.Usuario;

/**
 *
 * @author carlos
 *
 */
public class FiltranteApellido2 extends FiltroDecorador {

    Filtrante filtro;

    public FiltranteApellido2(Filtrante filtro) {
        this.filtro = filtro;
    }

    @Override
    public List filtra(Entidad entidad) {
        List<Entidad> fentidades = filtro.filtra(entidad);
        Usuario usuario = (Usuario) entidad;
        Iterator it = fentidades.iterator();
        while (it.hasNext()) {
            Usuario u = (Usuario) it;
            if (!u.getApellido2().equals(u.getApellido2())) {
                fentidades.remove(u);
            }
        }
        return fentidades;
    }

    @Override
    public String getNombre() {
        return getNombre() + "Nif ";
    }
}
