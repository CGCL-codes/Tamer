package com.odontosis.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.odontosis.as.OdontosisDataAccessObject;
import com.odontosis.entidade.FormaPagamento;
import com.odontosis.entidade.Recibo;
import com.odontosis.entidade.TipoRecibo;
import com.odontosis.entidade.Usuario;
import com.odontosis.util.HibernateUtil;
import com.odontosis.util.StringUtilsOdontosis;

public class ReciboDAO extends OdontosisDataAccessObject<Recibo> {

    @SuppressWarnings("unchecked")
    public Collection<Recibo> pesquisarPorPaciente(String paciente, Date dataInicial, Date dataFinal, TipoRecibo tipo) {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Criteria criteria = session.createCriteria(Recibo.class);
        criteria.createAlias("pagamento", "p");
        criteria.createAlias("p.servico", "s");
        criteria.createAlias("s.pacienteServico", "pac");
        if (!StringUtilsOdontosis.isVazia(paciente)) {
            criteria.add(Restrictions.or(Restrictions.like("pac.nome", paciente, MatchMode.ANYWHERE), Restrictions.eq("pac.numeroPasta", paciente)));
        }
        if (dataInicial != null && dataFinal != null) {
            criteria.add(Restrictions.between("data", dataInicial, dataFinal));
        }
        if (tipo != null) {
            criteria.add(Restrictions.eq("tipoRecibo", tipo));
        }
        criteria.add(Restrictions.eq("ativo", true));
        criteria.addOrder(Order.desc("id"));
        criteria.setMaxResults(300);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Recibo> relatorioPorPacientePeridoTipo(String paciente, Date dataInicial, Date dataFinal, TipoRecibo tipo) {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Criteria criteria = session.createCriteria(Recibo.class);
        criteria.createAlias("pagamento", "p");
        criteria.createAlias("p.servico", "s");
        criteria.createAlias("s.pacienteServico", "pac");
        if (!StringUtilsOdontosis.isVazia(paciente)) {
            criteria.add(Restrictions.or(Restrictions.like("pac.nome", paciente, MatchMode.ANYWHERE), Restrictions.eq("pac.numeroPasta", paciente)));
        }
        if (dataInicial != null && dataFinal != null) {
            criteria.add(Restrictions.between("data", dataInicial, dataFinal));
        }
        if (tipo != null) {
            criteria.add(Restrictions.eq("tipoRecibo", tipo));
        }
        criteria.add(Restrictions.eq("valido", true));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<Recibo> relatorioPagamentosEfetuados(String paciente, Date dataInicial, Date dataFinal, TipoRecibo tipo, Usuario usuario) {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Criteria criteria = session.createCriteria(Recibo.class);
        criteria.createAlias("pagamento", "p");
        criteria.createAlias("p.servico", "s");
        criteria.createAlias("s.pacienteServico", "pac");
        if (!StringUtilsOdontosis.isVazia(paciente)) {
            criteria.add(Restrictions.or(Restrictions.eq("pac.numeroPasta", paciente), Restrictions.like("pac.nome", paciente, MatchMode.ANYWHERE)));
        }
        if (dataInicial != null && dataFinal != null) {
            criteria.add(Restrictions.between("data", dataInicial, dataFinal));
        }
        if (tipo != null) {
            criteria.add(Restrictions.eq("tipoRecibo", tipo));
        }
        if (usuario != null) {
            criteria.add(Restrictions.eq("usuario", usuario));
        }
        criteria.add(Restrictions.eq("ativo", true));
        criteria.add(Restrictions.eq("valido", true));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    public String buscarCpf() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select cpf from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarCnpj() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select cnpj from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarCidade() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select cidade from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarBairro() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select bairro from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarEnd() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select endereco from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarDentista() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select dentista from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarCRO() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select cro from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public String buscarClinica() {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Query query = session.createSQLQuery("select nomeclinica from parametros where id = 1");
        return (String) query.uniqueResult();
    }

    public List<Recibo> buscarCheques(String emitente, String banco, String numeroCheque) {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Criteria criteria = session.createCriteria(Recibo.class);
        criteria.add(Restrictions.eq("formaPagamento", FormaPagamento.CHEQUE));
        if (!StringUtilsOdontosis.isVazia(emitente)) {
            criteria.add(Restrictions.like("nomeEmitente", emitente, MatchMode.ANYWHERE));
        }
        if (!StringUtilsOdontosis.isVazia(banco)) {
            criteria.add(Restrictions.like("banco", banco, MatchMode.ANYWHERE));
        }
        if (!StringUtilsOdontosis.isVazia(numeroCheque)) {
            criteria.add(Restrictions.like("numeroCheque", numeroCheque, MatchMode.ANYWHERE));
        }
        criteria.add(Restrictions.eq("valido", true));
        criteria.add(Restrictions.eq("ativo", true));
        criteria.addOrder(Order.desc("data"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
