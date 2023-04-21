package br.com.cinefilmes.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.seam.annotations.Name;
import br.com.cinefilmes.dao.EntregaDAO;
import br.com.cinefilmes.domain.Entrega;
import br.com.cinefilmes.domain.Filme;
import br.com.cinefilmes.exception.PesquisarException;
import br.com.cinefilmes.exception.SalvarException;

@Stateless
@Name("entregaBO")
public class EntregaBOImpl implements EntregaBO {

    @EJB
    EntregaDAO entregaDAO;

    public EntregaBOImpl() {
    }

    public EntregaBOImpl(EntregaDAO entregaDAO) {
        this.entregaDAO = entregaDAO;
    }

    public void realizarEntrega(Entrega entrega) {
        entrega.realizarEntrega();
        try {
            this.salvar(entrega);
        } catch (SalvarException e) {
            throw new SalvarException("Erro ao finalizar entrega do filme '" + entrega.getFilme().getTitulo() + "'", e);
        }
    }

    public void salvar(Entrega entrega) {
        try {
            entregaDAO.saveOrUpdate(entrega);
        } catch (Exception e) {
            throw new SalvarException("Erro ao salvar entrega do filme '" + entrega.getFilme().getTitulo() + "'", e);
        }
    }

    public Entrega buscarPorFilme(Filme filme) {
        try {
            return entregaDAO.buscarPorFilme(filme);
        } catch (Exception e) {
            throw new PesquisarException("Erro ao buscar entrega do filme '" + filme.getTitulo() + "'", e);
        }
    }
}
