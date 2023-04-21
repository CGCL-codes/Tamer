package br.usjt.smartzap.manager;

import br.com.smartzap.service.ws.RankingService;
import br.com.smartzap.service.ws.ranking.Ranking;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Classe que implementa a lógica do jogo de TRUCO.
 * @author vbonifacio
 */
public class Jogo {

    public Integer idJogo = null;

    public Integer jogadorAtual = null;

    public Integer numeroRodada = null;

    public Integer pontuacaoDuplaUm = null;

    public Integer pontuacaoDuplaDois = null;

    public Integer valorTruco = null;

    public Integer valorAumentado = null;

    public Integer vencedorMao = null;

    public String mensagemPartida = null;

    public String mensagemChat = null;

    public EnumCarta cartaVirada = null;

    public Map<Integer, Integer> vencedorRodada = new HashMap<Integer, Integer>();

    public Map<Integer, Jogador> jogadores = new HashMap<Integer, Jogador>();

    public Map<Integer, Carta> cartasRodada = new HashMap<Integer, Carta>();

    public Boolean empateRodada = false;

    public Boolean maoDeOnzeDuplaUm = false;

    public Boolean maoDeOnzeDuplaDois = false;

    public Boolean pediuTruco = false;

    public Boolean aceitouTruco = false;

    public Jogo(Integer idJogo) {
        this.idJogo = idJogo;
        this.jogadores = new HashMap<Integer, Jogador>();
        this.cartasRodada = new HashMap<Integer, Carta>();
        this.vencedorRodada = new HashMap<Integer, Integer>();
    }

    public void limparJogo() {
        this.jogadorAtual = 1;
        this.empateRodada = false;
        this.cartaVirada = EnumCarta.CARTA_VAZIA;
        this.numeroRodada = 0;
        this.pontuacaoDuplaUm = 0;
        this.pontuacaoDuplaDois = 0;
        this.valorTruco = 1;
        this.valorAumentado = 3;
        this.maoDeOnzeDuplaUm = false;
        this.maoDeOnzeDuplaDois = false;
        this.pediuTruco = false;
        this.aceitouTruco = false;
        this.vencedorMao = 0;
        this.mensagemPartida = " ";
        this.mensagemChat = " ";
        inicializarJogadores();
        inicializarVencedorRodada();
        inicializarCartasRodada();
    }

    public Jogador getJogadorAtual() {
        return jogadores.get(jogadorAtual);
    }

    public Collection<Jogador> getJogadores() {
        return (Collection<Jogador>) jogadores.values();
    }

    public boolean isJogadorNesteJogo(Integer jogadorId) {
        for (Jogador jogador : getJogadores()) {
            if (jogadorId.equals(jogador.getIdJogador())) {
                return true;
            }
        }
        return false;
    }

    public boolean participarJogo(Jogador jogador) {
        if (aguardandoJogadores()) {
            if (!jogadores.containsKey(jogador.getIndiceJogador()) && !isJogadorNesteJogo(jogador.getIdJogador())) {
                jogadores.put(jogador.getIndiceJogador(), jogador);
                System.out.println("Adicionando Jogador: " + jogador.getIndiceJogador() + " : " + jogador.getIdJogador());
                if (quantidadeJogadoresJogo() == 4) {
                    iniciarJogo();
                }
                return true;
            }
        }
        return false;
    }

    protected void iniciarJogo() {
        jogadorAtual = 1;
        numeroRodada = 0;
        pontuacaoDuplaUm = 0;
        pontuacaoDuplaDois = 0;
        vencedorMao = 0;
        valorTruco = 1;
        valorAumentado = 3;
        maoDeOnzeDuplaUm = false;
        maoDeOnzeDuplaDois = false;
        pediuTruco = false;
        aceitouTruco = false;
        mensagemPartida = " ";
        mensagemChat = " ";
        distribuirCartas();
        inicializarCartasRodada();
        System.out.println("Iniciando Jogo -> " + idJogo);
    }

    public boolean pedirTruco() {
        if ((jogadorAtual % 2 == 1 && !maoDeOnzeDuplaUm) || (jogadorAtual % 2 == 0 && !maoDeOnzeDuplaDois)) {
            pediuTruco = true;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " Pediu Truco!";
            proximoJogadorRodada();
            return true;
        } else {
            return false;
        }
    }

    public boolean aceitarTruco() {
        if (valorAumentado == 3) {
            valorTruco = 3;
            aceitouTruco = true;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aceitou Truco!";
            jogadorAnteriorRodada();
            return true;
        } else if (valorAumentado == 6) {
            valorTruco = 6;
            aceitouTruco = true;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aceitou Truco!";
            return true;
        } else if (valorAumentado == 9) {
            valorTruco = 9;
            aceitouTruco = true;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aceitou Truco!";
            jogadorAnteriorRodada();
            return true;
        } else if (valorAumentado == 12) {
            valorTruco = 12;
            aceitouTruco = true;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aceitou Truco!";
            return true;
        }
        return false;
    }

    public boolean aumentarApostaTruco() {
        if (valorAumentado == 3) {
            valorAumentado = 6;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aumentou em " + valorAumentado + " o Truco!";
            jogadorAnteriorRodada();
            return true;
        } else if (valorAumentado == 6) {
            valorAumentado = 9;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aumentou em " + valorAumentado + " o Truco!";
            proximoJogadorRodada();
            return true;
        } else if (valorAumentado == 9) {
            valorAumentado = 12;
            mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " aumentou em " + valorAumentado + " o Truco!";
            jogadorAnteriorRodada();
            return true;
        }
        return false;
    }

    public boolean correrTruco() {
        valorTruco = 1;
        valorAumentado = 3;
        pediuTruco = false;
        aceitouTruco = false;
        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " correu do Truco!";
        if (jogadorAtual % 2 == 0) {
            pontuacaoDuplaUm += 1;
        } else {
            pontuacaoDuplaDois += 1;
        }
        if (valorAumentado == 3 || valorAumentado == 9) {
            jogadorAnteriorRodada();
        } else {
            proximoJogadorRodada();
        }
        return true;
    }

    public boolean jogarCarta(Integer jogador, Integer indiceCarta) {
        if (jogadores.get(jogadorAtual).getIdJogador().equals(jogador)) {
            Carta carta = jogadores.get(jogadorAtual).getCartasJogador().get(indiceCarta);
            jogadores.get(jogadorAtual).getCartasJogador().get(indiceCarta).setCartaJogada(true);
            cartasRodada.put(jogadorAtual, carta);
            mensagemPartida = " ";
            if (quatroCartasJogadasRodada()) {
                atualizaResultadoRodada();
                atualizarCartasJogadores();
                if (numeroRodada == 0) {
                    if (vencedorRodada.get(numeroRodada) == 0) {
                        jogadorAtual = 1;
                    } else {
                        jogadorAtual = vencedorRodada.get(numeroRodada);
                    }
                    numeroRodada++;
                } else if (numeroRodada == 1) {
                    if (vencedorRodada.get(0) == 0 && vencedorRodada.get(1) != 0) {
                        vencedorMao = vencedorRodada.get(1);
                        jogadorAtual = vencedorRodada.get(numeroRodada);
                        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " venceu a rodada!";
                        atualizarPlacar();
                        numeroRodada = 0;
                        inicializarVencedorRodada();
                        distribuirCartas();
                    } else if (vencedorRodada.get(1) == 0 && vencedorRodada.get(0) != 0) {
                        vencedorMao = vencedorRodada.get(0);
                        jogadorAtual = vencedorRodada.get(0);
                        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " venceu a rodada!";
                        atualizarPlacar();
                        numeroRodada = 0;
                        inicializarVencedorRodada();
                        distribuirCartas();
                    } else if (vencedorRodada.get(0) != 0 && ((vencedorRodada.get(0) % 2) == (vencedorRodada.get(1) % 2))) {
                        vencedorMao = vencedorRodada.get(1);
                        jogadorAtual = vencedorRodada.get(1);
                        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " venceu a rodada!";
                        atualizarPlacar();
                        numeroRodada = 0;
                        inicializarVencedorRodada();
                        distribuirCartas();
                    } else if (vencedorRodada.get(0) == 0 && vencedorRodada.get(1) == 0) {
                        mensagemPartida = "Rodada empatou!";
                        jogadorAtual = 1;
                        numeroRodada++;
                    } else {
                        jogadorAtual = vencedorRodada.get(1);
                        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " venceu a rodada!";
                        numeroRodada++;
                    }
                } else if (numeroRodada == 2) {
                    if (vencedorRodada.get(2) != 0) {
                        vencedorMao = vencedorRodada.get(2);
                        jogadorAtual = vencedorRodada.get(2);
                        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " venceu a rodada!";
                        atualizarPlacar();
                        numeroRodada = 0;
                        inicializarVencedorRodada();
                        distribuirCartas();
                    } else if (vencedorRodada.get(0) != 0 && vencedorRodada.get(2) == 0) {
                        vencedorMao = vencedorRodada.get(0);
                        jogadorAtual = vencedorRodada.get(0);
                        mensagemPartida = jogadores.get(jogadorAtual).getNomeJogador() + " venceu a rodada!";
                        atualizarPlacar();
                        numeroRodada = 0;
                        inicializarVencedorRodada();
                        distribuirCartas();
                    } else {
                        jogadorAtual = 1;
                        mensagemPartida = "Não há vencedores da mão!";
                        numeroRodada = 0;
                        inicializarVencedorRodada();
                        distribuirCartas();
                    }
                }
                inicializarCartasRodada();
                return true;
            } else {
                proximoJogadorRodada();
                return true;
            }
        } else {
            System.out.println("Jogador Inválido: " + jogador);
            return false;
        }
    }

    public void atualizaResultadoRodada() {
        if (!isManilha(EnumCarta.getCartaById(cartasRodada.get(1).getIdCarta(), cartasRodada.get(1).getNaipeCarta())) && !isManilha(EnumCarta.getCartaById(cartasRodada.get(2).getIdCarta(), cartasRodada.get(2).getNaipeCarta())) && !isManilha(EnumCarta.getCartaById(cartasRodada.get(3).getIdCarta(), cartasRodada.get(3).getNaipeCarta())) && !isManilha(EnumCarta.getCartaById(cartasRodada.get(4).getIdCarta(), cartasRodada.get(4).getNaipeCarta()))) {
            Integer maiorCartaDuplaUm = 0;
            Integer maiorCartaDuplaDois = 0;
            Integer maiorJogadorDuplaUm = 0;
            Integer maiorJogadorDuplaDois = 0;
            maiorCartaDuplaUm = cartasRodada.get(1).getIdCarta();
            maiorJogadorDuplaUm = 1;
            if (maiorCartaDuplaUm < cartasRodada.get(3).getIdCarta()) {
                maiorCartaDuplaUm = cartasRodada.get(3).getIdCarta();
                maiorJogadorDuplaUm = 3;
            }
            maiorCartaDuplaDois = cartasRodada.get(2).getIdCarta();
            maiorJogadorDuplaDois = 2;
            if (maiorCartaDuplaDois < cartasRodada.get(4).getIdCarta()) {
                maiorCartaDuplaDois = cartasRodada.get(4).getIdCarta();
                maiorJogadorDuplaDois = 4;
            }
            if (maiorCartaDuplaUm > maiorCartaDuplaDois) {
                vencedorRodada.put(numeroRodada, maiorJogadorDuplaUm);
            } else if (maiorCartaDuplaUm < maiorCartaDuplaDois) {
                vencedorRodada.put(numeroRodada, maiorJogadorDuplaDois);
            } else {
                vencedorRodada.put(numeroRodada, 0);
            }
        } else {
            vencedorRodada.put(numeroRodada, 1);
            if (isManilha(EnumCarta.getCartaById(cartasRodada.get(2).getIdCarta(), cartasRodada.get(2).getNaipeCarta())) && isManilha(EnumCarta.getCartaById(cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta(), cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()))) {
                if (cartasRodada.get(2).getNaipeCarta() > cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()) {
                    vencedorRodada.put(numeroRodada, 2);
                }
            } else if (isManilha(EnumCarta.getCartaById(cartasRodada.get(2).getIdCarta(), cartasRodada.get(2).getNaipeCarta()))) {
                vencedorRodada.put(numeroRodada, 2);
            } else if (!isManilha(EnumCarta.getCartaById(cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta(), cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()))) {
                if (cartasRodada.get(2).getIdCarta() > cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta()) {
                    vencedorRodada.put(numeroRodada, 2);
                }
            }
            if (isManilha(EnumCarta.getCartaById(cartasRodada.get(3).getIdCarta(), cartasRodada.get(3).getNaipeCarta())) && isManilha(EnumCarta.getCartaById(cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta(), cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()))) {
                if (cartasRodada.get(3).getNaipeCarta() > cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()) {
                    vencedorRodada.put(numeroRodada, 3);
                }
            } else if (isManilha(EnumCarta.getCartaById(cartasRodada.get(3).getIdCarta(), cartasRodada.get(3).getNaipeCarta()))) {
                vencedorRodada.put(numeroRodada, 3);
            } else if (!isManilha(EnumCarta.getCartaById(cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta(), cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()))) {
                if (cartasRodada.get(3).getIdCarta() > cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta()) {
                    vencedorRodada.put(numeroRodada, 3);
                }
            }
            if (isManilha(EnumCarta.getCartaById(cartasRodada.get(4).getIdCarta(), cartasRodada.get(4).getNaipeCarta())) && isManilha(EnumCarta.getCartaById(cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta(), cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()))) {
                if (cartasRodada.get(4).getNaipeCarta() > cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()) {
                    vencedorRodada.put(numeroRodada, 4);
                }
            } else if (isManilha(EnumCarta.getCartaById(cartasRodada.get(4).getIdCarta(), cartasRodada.get(4).getNaipeCarta()))) {
                vencedorRodada.put(numeroRodada, 4);
            } else if (!isManilha(EnumCarta.getCartaById(cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta(), cartasRodada.get(vencedorRodada.get(numeroRodada)).getNaipeCarta()))) {
                if (cartasRodada.get(4).getIdCarta() > cartasRodada.get(vencedorRodada.get(numeroRodada)).getIdCarta()) {
                    vencedorRodada.put(numeroRodada, 4);
                }
            }
        }
    }

    protected void atualizarPlacar() {
        Ranking rankingJogadorUm = null;
        Ranking rankingJogadorDois = null;
        Ranking rankingJogadorTres = null;
        Ranking rankingJogadorQuatro = null;
        try {
            rankingJogadorUm = RankingService.buscarRanking(jogadores.get(1).getIdJogador());
            rankingJogadorDois = RankingService.buscarRanking(jogadores.get(2).getIdJogador());
            rankingJogadorTres = RankingService.buscarRanking(jogadores.get(3).getIdJogador());
            rankingJogadorQuatro = RankingService.buscarRanking(jogadores.get(4).getIdJogador());
        } catch (Exception e) {
        }
        if (vencedorMao % 2 == 1) {
            pontuacaoDuplaUm += (1 * valorTruco);
            if (rankingJogadorUm != null) {
                BigDecimal valorRanking = rankingJogadorUm.getPontuacao();
                valorRanking = valorRanking.add(new BigDecimal((1 * valorTruco)));
                Integer vitorias = rankingJogadorUm.getVitorias();
                rankingJogadorUm.setVitorias(vitorias + 1);
                rankingJogadorUm.setPontuacao(valorRanking);
                RankingService.alterarRanking(rankingJogadorUm);
            } else {
                rankingJogadorUm = new Ranking();
                rankingJogadorUm.setIdUsuario(jogadores.get(1).getIdJogador());
                rankingJogadorUm.setNomeUsuario(jogadores.get(1).getNomeJogador());
                rankingJogadorUm.setVitorias(1);
                rankingJogadorUm.setDerrotas(0);
                rankingJogadorUm.setPontuacao(new BigDecimal((1 * valorTruco)));
                RankingService.criarRanking(rankingJogadorUm);
            }
            if (rankingJogadorDois != null) {
                Integer derrotas = rankingJogadorDois.getDerrotas();
                rankingJogadorDois.setDerrotas(derrotas + 1);
                RankingService.alterarRanking(rankingJogadorDois);
            } else {
                rankingJogadorDois = new Ranking();
                rankingJogadorDois.setIdUsuario(jogadores.get(2).getIdJogador());
                rankingJogadorDois.setNomeUsuario(jogadores.get(2).getNomeJogador());
                rankingJogadorDois.setVitorias(0);
                rankingJogadorDois.setDerrotas(1);
                rankingJogadorDois.setPontuacao(BigDecimal.ZERO);
                RankingService.criarRanking(rankingJogadorDois);
            }
            if (rankingJogadorTres != null) {
                BigDecimal valorRanking = rankingJogadorTres.getPontuacao();
                valorRanking = valorRanking.add(new BigDecimal((1 * valorTruco)));
                Integer vitorias = rankingJogadorTres.getVitorias();
                rankingJogadorTres.setVitorias(vitorias + 1);
                rankingJogadorTres.setPontuacao(valorRanking);
                RankingService.alterarRanking(rankingJogadorTres);
            } else {
                rankingJogadorTres = new Ranking();
                rankingJogadorTres.setIdUsuario(jogadores.get(3).getIdJogador());
                rankingJogadorTres.setNomeUsuario(jogadores.get(3).getNomeJogador());
                rankingJogadorTres.setVitorias(1);
                rankingJogadorTres.setDerrotas(0);
                rankingJogadorTres.setPontuacao(new BigDecimal((1 * valorTruco)));
                RankingService.criarRanking(rankingJogadorTres);
            }
            if (rankingJogadorQuatro != null) {
                Integer derrotas = rankingJogadorQuatro.getDerrotas();
                rankingJogadorQuatro.setDerrotas(derrotas + 1);
                RankingService.alterarRanking(rankingJogadorQuatro);
            } else {
                rankingJogadorQuatro = new Ranking();
                rankingJogadorQuatro.setIdUsuario(jogadores.get(4).getIdJogador());
                rankingJogadorQuatro.setNomeUsuario(jogadores.get(4).getNomeJogador());
                rankingJogadorQuatro.setVitorias(0);
                rankingJogadorQuatro.setDerrotas(1);
                rankingJogadorQuatro.setPontuacao(BigDecimal.ZERO);
                RankingService.criarRanking(rankingJogadorQuatro);
            }
        } else {
            pontuacaoDuplaDois += (1 * valorTruco);
            if (rankingJogadorUm != null) {
                Integer derrotas = rankingJogadorUm.getDerrotas();
                rankingJogadorUm.setDerrotas(derrotas + 1);
                RankingService.alterarRanking(rankingJogadorUm);
            } else {
                rankingJogadorUm = new Ranking();
                rankingJogadorUm.setIdUsuario(jogadores.get(1).getIdJogador());
                rankingJogadorUm.setNomeUsuario(jogadores.get(1).getNomeJogador());
                rankingJogadorUm.setVitorias(0);
                rankingJogadorUm.setDerrotas(1);
                rankingJogadorUm.setPontuacao(BigDecimal.ZERO);
                RankingService.criarRanking(rankingJogadorUm);
            }
            if (rankingJogadorDois != null) {
                BigDecimal valorRanking = rankingJogadorDois.getPontuacao();
                valorRanking = valorRanking.add(new BigDecimal((1 * valorTruco)));
                Integer vitorias = rankingJogadorDois.getVitorias();
                rankingJogadorDois.setVitorias(vitorias + 1);
                rankingJogadorDois.setPontuacao(valorRanking);
                RankingService.alterarRanking(rankingJogadorDois);
            } else {
                rankingJogadorDois = new Ranking();
                rankingJogadorDois.setIdUsuario(jogadores.get(2).getIdJogador());
                rankingJogadorDois.setNomeUsuario(jogadores.get(2).getNomeJogador());
                rankingJogadorDois.setVitorias(1);
                rankingJogadorDois.setDerrotas(0);
                rankingJogadorDois.setPontuacao(new BigDecimal((1 * valorTruco)));
                RankingService.criarRanking(rankingJogadorDois);
            }
            if (rankingJogadorTres != null) {
                Integer derrotas = rankingJogadorTres.getDerrotas();
                rankingJogadorTres.setDerrotas(derrotas + 1);
                RankingService.alterarRanking(rankingJogadorTres);
            } else {
                rankingJogadorTres = new Ranking();
                rankingJogadorTres.setIdUsuario(jogadores.get(3).getIdJogador());
                rankingJogadorTres.setNomeUsuario(jogadores.get(3).getNomeJogador());
                rankingJogadorTres.setVitorias(0);
                rankingJogadorTres.setDerrotas(1);
                rankingJogadorTres.setPontuacao(BigDecimal.ZERO);
                RankingService.criarRanking(rankingJogadorTres);
            }
            if (rankingJogadorQuatro != null) {
                BigDecimal valorRanking = rankingJogadorQuatro.getPontuacao();
                valorRanking = valorRanking.add(new BigDecimal((1 * valorTruco)));
                Integer vitorias = rankingJogadorQuatro.getVitorias();
                rankingJogadorQuatro.setVitorias(vitorias + 1);
                rankingJogadorQuatro.setPontuacao(valorRanking);
                RankingService.alterarRanking(rankingJogadorQuatro);
            } else {
                rankingJogadorQuatro = new Ranking();
                rankingJogadorQuatro.setIdUsuario(jogadores.get(4).getIdJogador());
                rankingJogadorQuatro.setNomeUsuario(jogadores.get(4).getNomeJogador());
                rankingJogadorQuatro.setVitorias(1);
                rankingJogadorQuatro.setDerrotas(0);
                rankingJogadorQuatro.setPontuacao(new BigDecimal((1 * valorTruco)));
                RankingService.criarRanking(rankingJogadorQuatro);
            }
        }
        vencedorMao = 0;
        valorAumentado = 3;
        valorTruco = 1;
        pediuTruco = false;
        aceitouTruco = false;
        inicializarVencedorRodada();
        if (pontuacaoDuplaUm >= 12) {
            limparJogo();
        } else if (pontuacaoDuplaDois >= 12) {
            limparJogo();
        }
    }

    protected void distribuirCartas() {
        Set<String> setCartas = new HashSet<String>();
        List<String> listaCartas = new ArrayList<String>();
        while (setCartas.size() != 13) {
            Integer id = (int) (Math.random() * 10) + 1;
            Integer naipe = (int) (Math.random() * 4) + 1;
            String chaveCarta = id + ":" + naipe;
            if (!setCartas.contains(chaveCarta)) {
                setCartas.add(chaveCarta);
                listaCartas.add(chaveCarta);
            }
        }
        StringTokenizer tokenizer = new StringTokenizer(listaCartas.get(0), ":");
        cartaVirada = EnumCarta.getCartaById(new Integer(tokenizer.nextToken()), new Integer(tokenizer.nextToken()));
        tokenizer = new StringTokenizer(listaCartas.get(1), ":");
        EnumCarta enumCarta = getCartaById(tokenizer);
        jogadores.get(1).adicionarCartaJogador(1, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(2), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(1).adicionarCartaJogador(2, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(3), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(1).adicionarCartaJogador(3, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(4), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(2).adicionarCartaJogador(1, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(5), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(2).adicionarCartaJogador(2, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(6), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(2).adicionarCartaJogador(3, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(7), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(3).adicionarCartaJogador(1, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(8), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(3).adicionarCartaJogador(2, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(9), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(3).adicionarCartaJogador(3, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(10), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(4).adicionarCartaJogador(1, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(11), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(4).adicionarCartaJogador(2, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
        tokenizer = new StringTokenizer(listaCartas.get(12), ":");
        enumCarta = getCartaById(tokenizer);
        jogadores.get(4).adicionarCartaJogador(3, new Carta(enumCarta.getNaipeCarta(), enumCarta.getIdCarta(), enumCarta.getDescricaoCarta(), false));
    }

    private EnumCarta getCartaById(StringTokenizer tokenizer) {
        return EnumCarta.getCartaById(new Integer(tokenizer.nextToken()), new Integer(tokenizer.nextToken()));
    }

    private boolean isManilha(EnumCarta carta) {
        if (cartaVirada.getIdCarta().equals(EnumCarta.QUATRO_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.CINCO_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.CINCO_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.SEIS_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.SEIS_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.SETE_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.SETE_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.DAMA_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.DAMA_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.VALETE_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.VALETE_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.REI_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.REI_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.AS_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.AS_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.DOIS_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.DOIS_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.TRES_OURO.getIdCarta());
        } else if (cartaVirada.getIdCarta().equals(EnumCarta.TRES_OURO.getIdCarta())) {
            return carta.getIdCarta().equals(EnumCarta.QUATRO_OURO.getIdCarta());
        } else {
            return false;
        }
    }

    public void enviarMensagemChat(Integer idJogador, String msg) {
        if (jogadores.get(1).getIdJogador().equals(idJogador)) {
            this.mensagemChat += jogadores.get(1).getNomeJogador() + "->" + msg + "\n";
        }
        if (jogadores.get(2).getIdJogador().equals(idJogador)) {
            this.mensagemChat += jogadores.get(2).getNomeJogador() + "->" + msg + "\n";
        }
        if (jogadores.get(3).getIdJogador().equals(idJogador)) {
            this.mensagemChat += jogadores.get(3).getNomeJogador() + "->" + msg + "\n";
        }
        if (jogadores.get(4).getIdJogador().equals(idJogador)) {
            this.mensagemChat += jogadores.get(4).getNomeJogador() + "->" + msg + "\n";
        }
    }

    public Jogador proximoJogadorRodada() {
        if (jogadorAtual == 1) {
            jogadorAtual = 2;
            return jogadores.get(2);
        } else if (jogadorAtual == 2) {
            jogadorAtual = 3;
            return jogadores.get(3);
        } else if (jogadorAtual == 3) {
            jogadorAtual = 4;
            return jogadores.get(4);
        } else if (jogadorAtual == 4) {
            jogadorAtual = 1;
            return jogadores.get(1);
        }
        return null;
    }

    public Jogador jogadorAnteriorRodada() {
        if (jogadorAtual == 1) {
            jogadorAtual = 4;
            return jogadores.get(4);
        } else if (jogadorAtual == 2) {
            jogadorAtual = 1;
            return jogadores.get(1);
        } else if (jogadorAtual == 3) {
            jogadorAtual = 2;
            return jogadores.get(2);
        } else if (jogadorAtual == 4) {
            jogadorAtual = 3;
            return jogadores.get(3);
        }
        return null;
    }

    public boolean aguardandoJogadores() {
        Integer count = 0;
        for (Jogador jogador : jogadores.values()) {
            if (jogador.getIdJogador() != 0) {
                count++;
            }
        }
        return count < 4;
    }

    private void inicializarCartasRodada() {
        cartasRodada = new HashMap<Integer, Carta>();
        cartasRodada.put(1, new Carta(EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        cartasRodada.put(2, new Carta(EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        cartasRodada.put(3, new Carta(EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        cartasRodada.put(4, new Carta(EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
    }

    private boolean quatroCartasJogadasRodada() {
        int count = 0;
        for (Carta carta : cartasRodada.values()) {
            if (carta.getIdCarta() != -1) {
                count++;
            }
        }
        return count == 4;
    }

    private void atualizarCartasJogadores() {
        if (jogadores.get(1).getCartasJogador().get(1).getCartaJogada()) {
            jogadores.get(1).removerCartaJogador(1);
        } else if (jogadores.get(1).getCartasJogador().get(2).getCartaJogada()) {
            jogadores.get(1).removerCartaJogador(2);
        } else if (jogadores.get(1).getCartasJogador().get(3).getCartaJogada()) {
            jogadores.get(1).removerCartaJogador(3);
        }
        if (jogadores.get(2).getCartasJogador().get(1).getCartaJogada()) {
            jogadores.get(2).removerCartaJogador(1);
        } else if (jogadores.get(2).getCartasJogador().get(2).getCartaJogada()) {
            jogadores.get(2).removerCartaJogador(2);
        } else if (jogadores.get(2).getCartasJogador().get(3).getCartaJogada()) {
            jogadores.get(2).removerCartaJogador(3);
        }
        if (jogadores.get(3).getCartasJogador().get(1).getCartaJogada()) {
            jogadores.get(3).removerCartaJogador(1);
        } else if (jogadores.get(3).getCartasJogador().get(2).getCartaJogada()) {
            jogadores.get(3).removerCartaJogador(2);
        } else if (jogadores.get(3).getCartasJogador().get(3).getCartaJogada()) {
            jogadores.get(3).removerCartaJogador(3);
        }
        if (jogadores.get(4).getCartasJogador().get(1).getCartaJogada()) {
            jogadores.get(4).removerCartaJogador(1);
        } else if (jogadores.get(4).getCartasJogador().get(2).getCartaJogada()) {
            jogadores.get(4).removerCartaJogador(2);
        } else if (jogadores.get(4).getCartasJogador().get(3).getCartaJogada()) {
            jogadores.get(4).removerCartaJogador(3);
        }
    }

    private void inicializarJogadores() {
        this.jogadores = new HashMap<Integer, Jogador>();
        jogadores.put(1, new Jogador(0, 1, "Jogador 1"));
        jogadores.put(2, new Jogador(0, 2, "Jogador 2"));
        jogadores.put(3, new Jogador(0, 3, "Jogador 3"));
        jogadores.put(4, new Jogador(0, 4, "Jogador 4"));
        jogadores.get(1).adicionarCartaJogador(1, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(1).adicionarCartaJogador(2, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(1).adicionarCartaJogador(3, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(2).adicionarCartaJogador(1, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(2).adicionarCartaJogador(2, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(2).adicionarCartaJogador(3, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(3).adicionarCartaJogador(1, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(3).adicionarCartaJogador(2, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(3).adicionarCartaJogador(3, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(4).adicionarCartaJogador(1, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(4).adicionarCartaJogador(2, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
        jogadores.get(4).adicionarCartaJogador(3, new Carta(EnumCarta.CARTA_VAZIA.getNaipeCarta(), EnumCarta.CARTA_VAZIA.getIdCarta(), EnumCarta.CARTA_VAZIA.getDescricaoCarta(), false));
    }

    private void inicializarVencedorRodada() {
        this.vencedorRodada = new HashMap<Integer, Integer>();
        vencedorRodada.put(0, 0);
        vencedorRodada.put(1, 0);
        vencedorRodada.put(2, 0);
    }

    private int quantidadeJogadoresJogo() {
        int count = 0;
        for (Jogador jogador : jogadores.values()) {
            if (jogador.getIdJogador() != 0) {
                count++;
            }
        }
        return count;
    }
}
