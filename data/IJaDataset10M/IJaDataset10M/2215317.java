package samples.web.livrariademoiselle.persistence.dao;

import java.util.List;
import samples.web.livrariademoiselle.bean.implementation.Editora;
import br.gov.framework.demoiselle.core.layer.IDAO;
import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 */
public interface IEditoraDAO extends IDAO<Editora> {

    public PagedResult<Editora> listar(Page pagina);

    public List<Editora> listar();

    public PagedResult<Editora> filtrar(Editora Editora, Page pagina);

    public Editora buscar(Editora Editora);
}
