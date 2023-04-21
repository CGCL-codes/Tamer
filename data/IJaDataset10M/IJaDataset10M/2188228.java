package br.rafael.ladderWin.facade.impl;

import br.rafael.ladderWin.dao.JogadorDAO;
import br.rafael.ladderWin.dao.impl.JogadorDAOImpl;
import br.rafael.ladderWin.domain.JogadorBean;
import br.rafael.ladderWin.facade.CadastroJogadorFacade;

public class CadastroJogadorFacadeImpl implements CadastroJogadorFacade {

    private JogadorDAO jogadorDAO = new JogadorDAOImpl();

    public void cadastrar(JogadorBean jogador) throws Exception {
        jogadorDAO.add(jogador);
        System.out.println("Jogador cadastrado");
    }
}
