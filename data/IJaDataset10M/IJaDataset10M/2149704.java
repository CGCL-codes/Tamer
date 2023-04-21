package br.com.appestoque.dao.cadastro;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import br.com.appestoque.TipoBusca;
import br.com.appestoque.dao.DAOException;
import br.com.appestoque.dao.DAOGenerico;
import br.com.appestoque.dominio.cadastro.Bairro;
import br.com.appestoque.dominio.cadastro.Cidade;

@SuppressWarnings("unchecked")
public class BairroDAO extends DAOGenerico<Bairro, Long> {

    public BairroDAO(PersistenceManager pm) {
        this.setPm(pm);
    }

    public Bairro pesquisar(Long id, TipoBusca tipoBusca) {
        Bairro objeto = null;
        if (id != null) {
            Key k = KeyFactory.createKey(Bairro.class.getSimpleName(), id.intValue());
            objeto = (Bairro) getPm().getObjectById(Bairro.class, k);
            if (tipoBusca.equals(TipoBusca.ANSIOSA)) {
                CidadeDAO cidadeDAO = new CidadeDAO(getPm());
                if (objeto.getIdCidade() != null) {
                    objeto.setCidade(cidadeDAO.pesquisar(objeto.getIdCidade()));
                }
            }
        }
        return objeto;
    }

    public List<Bairro> pesquisar(Long idCidade, Long idEmpresa) {
        List<Bairro> objetos;
        Query query = getPm().newQuery(Bairro.class);
        query.setFilter("idCidade == p_cidade && idEmpresa == p_empresa ");
        query.declareParameters("Long p_cidade, Long p_empresa");
        objetos = (List<Bairro>) query.execute(idCidade, idEmpresa);
        return objetos;
    }

    public List<Bairro> pesquisar(String nome, Long idEmpresa, long ini, long qtd, TipoBusca tipoBusca) {
        Query query = getPm().newQuery(Bairro.class);
        query.setRange(ini, qtd);
        List<Bairro> objetos = null;
        if (nome != null) {
            query.setFilter("nome == p_nome && idEmpresa == p_empresa ");
            query.declareParameters("String p_nome , Long p_empresa");
            objetos = (List<Bairro>) query.execute(nome, idEmpresa);
        } else {
            query.setFilter("idEmpresa == p_empresa ");
            query.declareParameters("String p_empresa");
            objetos = (List<Bairro>) query.execute(idEmpresa);
        }
        if (tipoBusca.equals(TipoBusca.ANSIOSA)) {
            CidadeDAO cidadeDAO = new CidadeDAO(this.getPm());
            for (int i = 0; i < objetos.size(); i++) {
                objetos.get(i).setCidade(cidadeDAO.pesquisar(objetos.get(i).getIdCidade()));
            }
        }
        return objetos;
    }

    public int contar(String nome, Long idEmpresa) {
        Query query = getPm().newQuery(Bairro.class);
        List<Bairro> objetos = null;
        if (nome != null) {
            query.setFilter("nome == p_nome && idEmpresa == p_empresa ");
            query.declareParameters("String p_nome , Long p_empresa");
            objetos = (List<Bairro>) query.execute(nome, idEmpresa);
        } else {
            query.setFilter("idEmpresa == p_empresa ");
            query.declareParameters("String p_empresa");
            objetos = (List<Bairro>) query.execute(idEmpresa);
        }
        return objetos.size();
    }

    public boolean pesquisar(Cidade cidade) {
        Query query = getPm().newQuery(Bairro.class);
        query.setFilter("idCidade == p_cidade ");
        query.declareParameters("Long p_cidade");
        List<Bairro> bairros = (List<Bairro>) query.execute(cidade.getId());
        return (bairros.size() > 0);
    }

    public void excluir(Bairro bairro) throws DAOException {
        ClienteDAO clienteDAO = new ClienteDAO(getPm());
        RepresentanteDAO representanteDAO = new RepresentanteDAO(getPm());
        if (!clienteDAO.pesquisar(bairro)) {
            if (!representanteDAO.pesquisar(bairro)) {
                try {
                    getPm().currentTransaction().begin();
                    getPm().deletePersistent(bairro);
                    getPm().currentTransaction().commit();
                } finally {
                    if (getPm().currentTransaction().isActive()) {
                        getPm().currentTransaction().rollback();
                    }
                }
            } else {
                throw new DAOException(bundle.getString("representante.mensagem.bairro.vinculado"));
            }
        } else {
            throw new DAOException(bundle.getString("cliente.mensagem.bairro.vinculado"));
        }
    }
}
