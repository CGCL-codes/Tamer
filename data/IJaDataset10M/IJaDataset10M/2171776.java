package persistencia;

import java.io.Serializable;
import java.util.List;

public interface IDaoGenerico<T> {

    public void save(T objeto);

    public T load(Serializable id);

    public void update(T t);

    public void remover(T t);

    public void salvarOuAlterar(T t);

    public List<T> listarTudo();

    public List pegarPeloCampo(String campo, Object obj);

    public void removePeloCampo(String campo, Object valor);
}
