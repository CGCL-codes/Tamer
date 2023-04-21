package br.com.geostore.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import br.com.geostore.entity.Loja;
import br.com.geostore.entity.Usuario;

@Name("lojaDAO")
public class LojaDAO {

    @In
    private EntityManager entityManager;

    @Logger
    private Log log;

    public void incluir(Loja loja) throws Exception {
        try {
            log.info("Incluir Loja: #0", loja.getId());
            entityManager.persist(loja);
            entityManager.flush();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void alterar(Loja loja) throws Exception {
        try {
            log.info("Alterar Loja: #0", loja.getDocumento());
            entityManager.merge(loja);
            entityManager.merge(loja.getEndereco());
            entityManager.flush();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void salvar(Loja loja) throws Exception {
        try {
            log.info("Persistir loja: #0", loja.getDocumento());
            entityManager.persist(loja);
            entityManager.flush();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void excluir(Loja loja) throws Exception {
        try {
            if (!entityManager.contains(loja)) loja = entityManager.merge(loja);
            log.info("Remover Empresa: #0", loja.getId());
            entityManager.remove(loja);
            entityManager.flush();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Loja buscarPorId(Long id) throws Exception {
        try {
            return entityManager.find(Loja.class, id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Loja> buscarTodos(Usuario usuarioLogado) throws Exception {
        try {
            String sQuery;
            log.info("Buscando Lista de Lojas do Banco de Dados");
            sQuery = " from Loja as l ";
            if (usuarioLogado.getTipoUsuario().getId().longValue() != 1) sQuery += " where l.empresaSuperior.id = :idEmpresaUsuario ";
            sQuery += " order by l.id ";
            Query query = entityManager.createQuery(sQuery);
            if (usuarioLogado.getTipoUsuario().getId().longValue() != 1) query.setParameter("idEmpresaUsuario", usuarioLogado.getEmpresaVinculo().getId());
            return query.getResultList();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public boolean buscarPorCNPJ(Loja loja, String acao) throws Exception {
        try {
            log.info("Buscando se CNPJ j� existe no Banco de Dados: " + loja.getDocumento());
            String sQuery;
            if (acao == "NOVA") {
                sQuery = " from Loja as l ";
                sQuery += " where l.documento = :empresaCNPJ ";
                sQuery += " order by l.id ";
                Query query = entityManager.createQuery(sQuery);
                query.setParameter("empresaCNPJ", loja.getDocumento());
                if (query.getResultList() == null || query.getResultList().isEmpty()) return false;
            }
            if (acao == "EDITAR") {
                sQuery = " from Loja as l ";
                sQuery += " where l.documento = :empresaCNPJ ";
                sQuery += " and l.id <> :empresaId ";
                sQuery += " order by l.id ";
                Query query = entityManager.createQuery(sQuery);
                query.setParameter("empresaCNPJ", loja.getDocumento());
                query.setParameter("empresaId", loja.getId());
                if (query.getResultList() == null || query.getResultList().isEmpty()) return false;
            }
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
