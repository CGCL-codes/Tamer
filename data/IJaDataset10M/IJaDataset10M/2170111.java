package galerias.negocios.interfaces;

import galerias.entidades.Evento;
import galerias.entidades.Obra;
import java.util.List;
import javax.ejb.Local;

@Local
public interface EventosLocalInterface {

    public List<Evento> listaEventos(String codigoEstado);

    public List<Evento> listaEventosPorArtista(String codigoArtista, String codigoEstado);

    public List<Obra> listarObrasPorEvento(String codigoEvento);
}
