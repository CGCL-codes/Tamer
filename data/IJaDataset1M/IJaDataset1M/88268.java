package br.com.cinefilmes.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.seam.annotations.Name;
import br.com.cinefilmes.dao.EdicaoDAO;
import br.com.cinefilmes.domain.Edicao;
import br.com.cinefilmes.domain.Filme;
import br.com.cinefilmes.exception.PesquisarException;
import br.com.cinefilmes.exception.SalvarException;

@Stateless
@Name("edicaoBO")
public class EdicaoBOImpl implements EdicaoBO {

    @EJB
    EdicaoDAO edicaoDAO;

    public List<Edicao> buscarPorFilme(Filme filme) {
        try {
            return edicaoDAO.buscarPorFilme(filme);
        } catch (Exception e) {
            throw new PesquisarException("Erro ao pesquisar edições por filme.", e);
        }
    }

    public void finalizarEdicao(Edicao edicao) {
        this.salvar(edicao);
    }

    public void iniciarEdicao(Edicao edicao) {
        this.salvar(edicao);
    }

    public void salvar(Edicao edicao) {
        try {
            edicaoDAO.saveOrUpdate(edicao);
        } catch (Exception e) {
            throw new SalvarException(e);
        }
    }
}
