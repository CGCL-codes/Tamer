package br.com.progepe.dao;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import br.com.progepe.entity.Lotacao;
import br.com.progepe.entity.SolicitacaoAdicionalNoturno;

public class AdicionalNoturnoDAO extends DAO {

    private static AdicionalNoturnoDAO instance;

    private AdicionalNoturnoDAO() {
    }

    public static AdicionalNoturnoDAO getInstance() {
        if (instance == null) {
            instance = new AdicionalNoturnoDAO();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<SolicitacaoAdicionalNoturno> carregarSolicitacaoAdicionalNoturno(Lotacao codigoLotacao, Boolean indDocente) {
        HibernateUtility.getSession().clear();
        Query query = HibernateUtility.getSession().createQuery("from SolicitacaoAdicionalNoturno s where s.lotacao= :codigoLotacao and s.statusSolicitacao is null and s.indDocente = :indDocente");
        query.setParameter("codigoLotacao", codigoLotacao);
        query.setParameter("indDocente", indDocente);
        HibernateUtility.commitTransaction();
        return (List<SolicitacaoAdicionalNoturno>) query.list();
    }

    public void saveOrUpdateAdicional(Object objeto) {
        try {
            HibernateUtility.getSession().clear();
            HibernateUtility.beginTransaction();
            HibernateUtility.getSession().merge(objeto);
            HibernateUtility.commitTransaction();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item salvo com sucesso!", "Item salvo com sucesso!");
            FacesContext.getCurrentInstance().addMessage("", message);
        } catch (Exception e) {
            HibernateUtility.rollbackTransaction();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao comunicar com o servidor!", "Erro ao comunicar com o servidor!");
            FacesContext.getCurrentInstance().addMessage("", message);
        }
    }
}
