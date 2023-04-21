package sirf.dao.yacimiento;

import java.util.ArrayList;
import java.util.List;
import sirf.bd.OperacionTabla;
import sirf.bd.Search;
import sirf.dominio.yacimiento.Pais;

public class PaisDAO extends OperacionTabla {

    public Integer insert(Pais elemento) {
        return super.insert(elemento);
    }

    public Pais select(Integer id) {
        return (Pais) select(id, Pais.class);
    }

    public void delete(Integer id) {
        super.delete(id, Pais.class);
    }

    public void update(Pais elemento) {
        super.update(elemento);
    }

    @SuppressWarnings("unchecked")
    public List<Pais> list() {
        return new ArrayList<Pais>(this.list(Pais.class));
    }

    public Integer count() {
        return this.count(Pais.class);
    }

    public Integer count(Search busqueda) {
        return this.count(Pais.class, busqueda);
    }

    @SuppressWarnings("unchecked")
    public List<Pais> find(Integer offset, Integer num_rows, String column, String tipoSort) {
        return new ArrayList<Pais>(this.find(offset, num_rows, column, tipoSort, Pais.class));
    }

    @SuppressWarnings("unchecked")
    public List<Pais> find(Integer offset, Integer num_rows, String column, String tipoSort, Search search) {
        return new ArrayList<Pais>(find(offset, num_rows, column, tipoSort, Pais.class, search));
    }
}
