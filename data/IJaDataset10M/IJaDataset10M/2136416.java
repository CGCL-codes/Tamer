package br.com.dotec.persistence.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import br.com.caelum.vraptor.ioc.Component;
import br.com.dotec.model.Cliente;
import br.com.dotec.model.Movimentacao;

@Component
public class MovimentacaoDao {

    private Session session;

    static Logger logger = Logger.getLogger(MovimentacaoDao.class);

    public MovimentacaoDao(Session session) {
        this.session = session;
    }

    public void salva(Movimentacao mov) {
        this.session.save(mov);
    }

    public void remove(Movimentacao objtect) {
        this.session.delete(objtect);
    }

    @SuppressWarnings("unchecked")
    public List<Movimentacao> lista(Cliente cliente, Movimentacao movimentacao) {
        String strQuery = "select m from Movimentacao as m, Caixa as c, Cliente as cl Where c.movimentacoes.id = m.id and cl.elementos.id = c.id  ";
        Long AllDates = -2208977612000L;
        DateTime last30days = DateTime.now().plusDays(-30);
        if (cliente != null) {
            strQuery += " and cl = :cliente ";
        }
        if (movimentacao != null) {
            if (movimentacao.getTipoDeMovimentacao() != null) {
                strQuery += " and m.tipoDeMovimentacao = :tipoDeMovimentacao ";
            }
            if (movimentacao.getStatusDaMovimentacao() != null) {
                strQuery += " and m.statusDaMovimentacao = :statusDaMovimentacao ";
            }
            if (movimentacao.getDataDeCriacao() != null && movimentacao.getDataDeCriacao().getMillis() != AllDates) {
                strQuery += " and m.dataDeCriacao > :dataDeCriacao ";
            }
        } else {
            strQuery += " and m.dataDeCriacao > :dataDeCriacao ";
        }
        Query query = (Query) this.session.createQuery(strQuery);
        if (cliente != null) {
            query.setParameter("cliente", cliente);
        }
        if (movimentacao == null) {
            query.setParameter("dataDeCriacao", last30days);
        } else {
            if (movimentacao.getTipoDeMovimentacao() != null) {
                query.setParameter("tipoDeMovimentacao", movimentacao.getTipoDeMovimentacao());
            }
            if (movimentacao.getStatusDaMovimentacao() != null) {
                query.setParameter("statusDaMovimentacao", movimentacao.getStatusDaMovimentacao());
            }
            if (movimentacao.getDataDeCriacao() != null && movimentacao.getDataDeCriacao().getMillis() != AllDates) {
                query.setParameter("dataDeCriacao", movimentacao.getDataDeCriacao());
            }
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Movimentacao> lista(Cliente cliente) {
        String strQuery = "select m from Movimentacao as m, Caixa as c, Cliente as cl Where c.movimentacoes.id = m.id and cl.elementos.id = c.id  ";
        if (cliente != null) {
            strQuery += " and cl = :cliente ";
        }
        Query query = (Query) this.session.createQuery(strQuery);
        if (cliente != null) {
            query.setParameter("cliente", cliente);
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Movimentacao> lista() {
        Query query = (Query) this.session.createQuery("select m from Movimentacao");
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Movimentacao> listaMovimentacaoSemBoleto(Long clienteId) {
        Query query = (Query) this.session.createQuery("select m from Movimentacao as m,  Caixa as c, Cliente as cl Where c.movimentacoes.id = m.id and cl.elementos.id = c.id and  cl.id = :clienteId and m.boletoGerado=:boletoGerado");
        query.setLong("clienteId", clienteId);
        query.setInteger("boletoGerado", 0);
        return query.list();
    }

    public Movimentacao carrega(Long id) {
        return (Movimentacao) this.session.load(Movimentacao.class, id);
    }

    public Movimentacao carregaByIdProprio(String idProprio) {
        Query query = (Query) this.session.createQuery("select c from Movimentacao as c, BoletoItem bi, Boleto b where c.id = bi.movimentacao.id and b.itens.id=bi.id and b.idProprio= :idProprio");
        query.setString("idProprio", idProprio);
        return (Movimentacao) query.uniqueResult();
    }

    public void altera(Movimentacao object) {
        this.session.update(object);
    }
}
