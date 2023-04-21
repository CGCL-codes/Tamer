package br.com.rafael.ladderWin.service;

import java.util.List;
import br.com.rafael.ladderWin.model.Campeonato;
import br.com.rafael.ladderWin.model.Jogador;

public interface CampeonatoService {

    void criarCampeonato(Campeonato campeonato);

    List<Jogador> listarJogadores(Campeonato campeonato);
}
