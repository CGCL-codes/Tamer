package galerias.daos.interfaces;

import galerias.entidades.TipoObra;
import java.util.List;
import javax.ejb.Local;

@Local
public interface TipoObraDaoLocalInterface {

    public List<TipoObra> obtenerTipoObras();
}
