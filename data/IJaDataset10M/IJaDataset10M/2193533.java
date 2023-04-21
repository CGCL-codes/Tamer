package br.usp.ime.ingpos.services;

import java.util.ArrayList;
import java.util.List;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.usp.ime.ingpos.modelo.Inscricao;
import br.usp.ime.ingpos.modelo.Interesse;
import br.usp.ime.ingpos.modelo.dao.InteresseDAO;

@RequestScoped
@Component
public class InteresseService {

    private final InteresseDAO interesseDAO;

    public InteresseService(final InteresseDAO interesseDAO) {
        this.interesseDAO = interesseDAO;
    }

    public void salvaInteresse(Long idProfessor, List<Long> inscricoesInteressantes, List<Long> todasAsInscricoes) {
        if (inscricoesInteressantes != null) todasAsInscricoes.removeAll(inscricoesInteressantes);
        for (Long ins : todasAsInscricoes) {
            Interesse interesse = interesseDAO.procuraInteresse(idProfessor, ins);
            if (interesse != null) {
                if (!interesse.isSugeridoPeloCoordenador()) interesseDAO.delete(interesse); else {
                    interesse.setInteressePeloOrientador(false);
                    interesseDAO.saveOrUpdate(interesse);
                }
            }
        }
        if (inscricoesInteressantes == null) return;
        for (Long ins : inscricoesInteressantes) {
            Interesse interesse = interesseDAO.procuraInteresse(idProfessor, ins);
            if (interesse == null) {
                interesse = new Interesse();
                interesse.setIdInscricao(ins);
                interesse.setIdOrientador(idProfessor);
            }
            interesse.setInteressePeloOrientador(true);
            interesseDAO.saveOrUpdate(interesse);
        }
    }

    public List<Long> getInscricoesComInteressePorOrientadorID(Long id) {
        List<Interesse> lista = interesseDAO.filtraPorOrientadorID(id);
        List<Long> listaRetorno = new ArrayList<Long>();
        for (Interesse interesse : lista) {
            listaRetorno.add(interesse.getIdInscricao());
        }
        return listaRetorno;
    }

    public void salvaSugestaoCoordenador(Long inscricao, List<Long> orientadores) {
        for (Long id : orientadores) {
            Interesse interesse = interesseDAO.procuraInteresse(id, inscricao);
            if (interesse == null) {
                interesse = new Interesse();
                interesse.setIdInscricao(inscricao);
                interesse.setIdOrientador(id);
                interesse.setInteressePeloOrientador(false);
            }
            interesse.setSugeridoPeloCoordenador(true);
            interesseDAO.saveOrUpdate(interesse);
        }
    }
}
