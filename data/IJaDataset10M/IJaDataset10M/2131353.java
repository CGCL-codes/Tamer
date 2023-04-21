package br.ufrj.cad.model.disciplina;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import br.ufrj.cad.fwk.model.ObjetoPersistente;
import br.ufrj.cad.fwk.model.hibernate.CriteriaUtils;
import br.ufrj.cad.model.bo.AnoBase;
import br.ufrj.cad.model.bo.Departamento;
import br.ufrj.cad.model.bo.Disciplina;

public class DisciplinaDAO {

    private static Logger logger = Logger.getLogger(DisciplinaDAO.class);

    public static List obtemListaDiciplinas(Disciplina disciplina, Session session) {
        Criteria criteria = session.createCriteria(Disciplina.class);
        Criteria anoBaseCrit = criteria.createCriteria(Departamento.COL_ANO_BASE);
        CriteriaUtils.adicionaCriterio(anoBaseCrit, CriteriaUtils.adicionaRestricaoIgual(AnoBase.COL_ID, disciplina.getAnoBase().getId()));
        CriteriaUtils.adicionaCriterio(anoBaseCrit, CriteriaUtils.adicionaRestricaoIgual(AnoBase.COL_DEPARTAMENTO, disciplina.getAnoBase().getDepartamento()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(Disciplina.COL_CODIGO_DISCIPLINA, disciplina.getCodigo()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoLike(Disciplina.COL_DESCRICAO, disciplina.getDescricao()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(Disciplina.COL_QUANTIDADE_CREDITOS, disciplina.getQuantidadeCreditos()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(Disciplina.COL_VERSAO, disciplina.getVersao()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual("tipo", disciplina.getTipo()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual("nivel", disciplina.getNivel()));
        CriteriaUtils.adicionaCriterioOrdenacaoAscendente(criteria, Disciplina.COL_CODIGO_DISCIPLINA);
        List<Disciplina> resultado = (ArrayList<Disciplina>) criteria.list();
        logger.info("Buscando Disciplinas");
        return resultado;
    }

    @SuppressWarnings("unchecked")
    public static List obtemListaAnoBaseComDisciplinasSelecionadas(ObjetoPersistente departamento, Session session) {
        Criteria criteria = session.createCriteria(Disciplina.class);
        String apelidoAnoBase = "ab";
        criteria.createAlias(Disciplina.COL_ANO_BASE, apelidoAnoBase);
        criteria.add(CriteriaUtils.adicionaRestricaoIgual(CriteriaUtils.createAssociationPath(new String[] { apelidoAnoBase, AnoBase.COL_DEPARTAMENTO, "id" }), departamento.getId()));
        criteria.setProjection(Projections.distinct(Projections.property(CriteriaUtils.createAssociationPath(new String[] { apelidoAnoBase, Disciplina.COL_ID }))));
        criteria.addOrder(Order.asc(CriteriaUtils.createAssociationPath(new String[] { apelidoAnoBase, AnoBase.COL_VALOR_ANO_BASE })));
        List<Long> r = (List<Long>) criteria.list();
        List<AnoBase> anoBaseList = new ArrayList<AnoBase>();
        if (r.size() > 0) {
            anoBaseList = obtemListaAnoBasePorId(r, session);
        }
        return anoBaseList;
    }

    @SuppressWarnings("unchecked")
    private static List obtemListaAnoBasePorId(List<Long> idList, Session session) {
        Criteria criteria = session.createCriteria(AnoBase.class);
        criteria.add(Restrictions.in(AnoBase.COL_ID, idList));
        criteria.addOrder(Order.desc(AnoBase.COL_VALOR_ANO_BASE));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public static List obtemListaDisciplinaPorAnoBaseId(Long anoBaseId, Session session) {
        Criteria criteria = session.createCriteria(Disciplina.class);
        String associationPath = CriteriaUtils.createAssociationPath(new String[] { Disciplina.COL_ANO_BASE, AnoBase.COL_ID });
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(associationPath, anoBaseId));
        CriteriaUtils.adicionaCriterioOrdenacaoAscendente(criteria, Disciplina.COL_CODIGO_DISCIPLINA);
        return criteria.list();
    }

    public static ObjetoPersistente insereDisciplina(ObjetoPersistente disciplina, Session session) {
        session.save(disciplina);
        return disciplina;
    }

    public static AnoBase obtemAnoBasePorId(AnoBase anoBase, Session session) {
        AnoBase resultado = (AnoBase) session.get(AnoBase.class, anoBase.getId());
        return resultado;
    }

    public static Disciplina obtemDisciplinaPorId(Long disciplinaId, Session session) {
        Disciplina disciplina = (Disciplina) session.get(Disciplina.class, disciplinaId);
        return disciplina;
    }

    public static ObjetoPersistente alteraDisciplina(ObjetoPersistente disciplina, Session session) {
        session.update(disciplina);
        return disciplina;
    }

    public static AnoBase preencheDisciplinasDeAnoBase(AnoBase anoBaseSemDisciplinas, Session session) {
        AnoBase anoBaseComDisciplinas = DisciplinaDAO.obtemAnoBasePorId(anoBaseSemDisciplinas, session);
        Hibernate.initialize(anoBaseComDisciplinas.getDisciplinas());
        return anoBaseComDisciplinas;
    }

    public static ObjetoPersistente excluirDisciplina(ObjetoPersistente disciplina, Session session) {
        session.delete(disciplina);
        return disciplina;
    }

    @SuppressWarnings("unchecked")
    public static ObjetoPersistente obtemDisciplinaComEscolhasEMinistradas(Disciplina disciplina, Session session) {
        Disciplina disciplinaComEscolhasEMinistradas = DisciplinaDAO.obtemDisciplinaPorId(disciplina.getId(), session);
        Hibernate.initialize(disciplinaComEscolhasEMinistradas.getEscolhas());
        Hibernate.initialize(disciplinaComEscolhasEMinistradas.getMinistradas());
        return disciplinaComEscolhasEMinistradas;
    }

    public static AnoBase criaAnoBase(AnoBase anoBase, Session session) {
        Long novoId = (Long) session.save(anoBase);
        anoBase.setId(novoId);
        return anoBase;
    }

    public static AnoBase obtemListaAnoBasePorValorAnoBase(AnoBase anoBase, Session session) {
        Criteria criteria = session.createCriteria(AnoBase.class);
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(AnoBase.COL_VALOR_ANO_BASE, anoBase.getValorAnoBase()));
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(AnoBase.COL_DEPARTAMENTO, anoBase.getDepartamento()));
        return (AnoBase) criteria.uniqueResult();
    }

    public static List obtemListaAnoBase(ObjetoPersistente departamento, Session session) {
        Criteria criteria = session.createCriteria(AnoBase.class);
        String associationPath = CriteriaUtils.createAssociationPath(new String[] { AnoBase.COL_DEPARTAMENTO, "id" });
        CriteriaUtils.adicionaCriterio(criteria, CriteriaUtils.adicionaRestricaoIgual(associationPath, departamento.getId()));
        criteria.addOrder(Order.desc(AnoBase.COL_VALOR_ANO_BASE));
        return criteria.list();
    }
}
