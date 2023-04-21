package br.com.cefetrn.apoena.dominio.home;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import br.com.cefetrn.apoena.dominio.entity.AreasAtuacaoEntidadeFinanciadora;
import br.com.cefetrn.apoena.dominio.entity.AreasDeAtuacao;
import br.com.cefetrn.apoena.dominio.entity.AreasDeAtuacaoEntidadeExecutora;
import br.com.cefetrn.apoena.dominio.entity.EntidadeFinanciadora;
import br.com.cefetrn.apoena.dominio.entity.QualificacaoProfissional;
import br.com.cefetrn.apoena.util.HibernateUtil;

/**
 * Home object for domain model class EntidadeFinanciadora.
 * @see br.com.cefetrn.apoena.dominio.entity.EntidadeFinanciadora
 * @author Gilmar P.S.L.
 */
public class Home4EntidadeFinanciadora extends AxHomeGeneric<EntidadeFinanciadora, Integer> {

    @Override
    public EntidadeFinanciadora persist(EntidadeFinanciadora instance) {
        EntidadeFinanciadora entidade = null;
        if (instance != null) {
            Home4Login hl = new Home4Login();
            hl.setSession(getSession());
            hl.persist(instance.getLogin());
            Home4Endereco he = new Home4Endereco();
            he.setSession(getSession());
            he.persist(instance.getEndereco());
            Home4Pessoa contato = new Home4Pessoa();
            contato.setSession(getSession());
            contato.persist(instance.getPessoaByIdContato());
            entidade = super.persist(instance);
            if (instance.getAreasAtuacaoEntidadeFinanciadoras() != null && !instance.getAreasAtuacaoEntidadeFinanciadoras().isEmpty()) {
                Home4AreasAtuacaoEntidadeFinanciadora hap = new Home4AreasAtuacaoEntidadeFinanciadora();
                hap.setSession(getSession());
                for (AreasAtuacaoEntidadeFinanciadora area : instance.getAreasAtuacaoEntidadeFinanciadoras()) {
                    hap.persist(area);
                }
            }
        }
        return entidade;
    }

    public List<EntidadeFinanciadora> findEntidadesFinanciadoras(AreasDeAtuacao areaFinanciamento, String localidade) {
        String sql;
        StringBuilder tabelas = new StringBuilder("Select ef from EntidadeFinanciadora ef");
        StringBuilder filtros = new StringBuilder(" where ");
        if (StringUtils.isNotBlank(localidade)) {
            tabelas.append(" , Endereco e");
            filtros.append(" e.id = ef.endereco and e.pais like '%" + localidade + "%'");
        } else {
            tabelas.append(" , AreasAtuacaoEntidadeFinanciadora af");
            filtros.append(" af.entidadeFinanciadora = ef.id and af.id =" + areaFinanciamento.getId());
        }
        if (StringUtils.isNotBlank(localidade) && areaFinanciamento != null && StringUtils.isNotBlank(areaFinanciamento.getNome())) {
            tabelas.append(" , AreasAtuacaoEntidadeFinanciadora af");
            filtros.append(" and af.entidadeFinanciadora = ef.id and af.id =" + areaFinanciamento.getId());
        }
        if (areaFinanciamento != null || StringUtils.isNotBlank(localidade)) {
            sql = tabelas.toString() + filtros.toString();
            Query query = getSession().createQuery(sql);
            System.out.println(query.list());
            System.out.println(sql);
            return query.list();
        }
        return null;
    }

    @Override
    public EntidadeFinanciadora delete(EntidadeFinanciadora persistentInstance) {
        Home4Pessoa pessoaHome = new Home4Pessoa();
        pessoaHome.setSession(getSession());
        pessoaHome.delete(persistentInstance.getPessoaByIdContato());
        pessoaHome.delete(persistentInstance.getPessoaById());
        return super.delete(persistentInstance);
    }

    public static void main(String[] args) {
        QualificacaoProfissional q = new QualificacaoProfissional();
        q.setNome("gilmar");
        AreasDeAtuacao a = new AreasDeAtuacao();
        a.setId(2);
        a.setNome("ASSISTÊNCIA E SERVIÇOS SOCIAIS");
        Home4EntidadeFinanciadora ef = new Home4EntidadeFinanciadora();
        ef.setSession(HibernateUtil.currentSession());
        ef.findEntidadesFinanciadoras(a, null);
    }
}
