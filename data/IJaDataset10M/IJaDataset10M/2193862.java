package controlador;

import java.util.List;
import modelo.Item;
import persistencia.DAOFactory;
import persistencia.ItemDAO;

/**
 *
 * @author chico
 */
public class ControleItem {

    public void atualizaItem(Item i) throws MiakException {
        DAOFactory.DEFAULT.buildItemDAO().update(i);
    }

    public void saveItem(Item i) throws MiakException {
        try {
            ItemDAO dao = DAOFactory.DEFAULT.buildItemDAO();
            if (dao.getByRegistro(i.getNRegistro()) == null) {
                throw new MiakException("Por favor, insira um número de registro.");
            } else {
                DAOFactory.DEFAULT.buildItemDAO().save(i);
            }
        } catch (Exception erro) {
            throw new MiakException("Erro ao salvar o item." + erro);
        }
    }

    public Item getItemById(int id) throws MiakException {
        try {
            return DAOFactory.DEFAULT.buildItemDAO().getById(id);
        } catch (Exception erro) {
            throw new MiakException("Erro ao pesquisar item" + erro);
        }
    }

    public Item getItemByRegistro(String reg) throws MiakException {
        try {
            return (Item) DAOFactory.DEFAULT.buildItemDAO().getByRegistro(reg);
        } catch (Exception erro) {
            throw new MiakException("Erro ao pesquisar item" + erro);
        }
    }

    public List<Item> pesquisaNome(String titulo) {
        return DAOFactory.DEFAULT.buildItemDAO().getByTitulo(titulo);
    }

    public List<Item> pesquisaClassificacao(String classificacao) {
        return DAOFactory.DEFAULT.buildItemDAO().getByClassificacao(classificacao);
    }

    public List<Item> pesquisaCurso(String curso) {
        return DAOFactory.DEFAULT.buildItemDAO().getByCurso(curso);
    }

    public void deleteItem(int id) {
        DAOFactory.DEFAULT.buildItemDAO().deleteById(id);
    }
}
