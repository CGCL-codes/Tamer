package yam.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jogo {

    public StatusDoJogo statusDoJogo;

    private ArrayList<Jogador> jogadores;

    private Jogada jogada;

    private boolean podeJogarDados;

    private boolean podeMarcarDados;

    private boolean podeMarcarPontos;

    private int jogadorAtual = 0;

    private int jogadorAnterior = 0;

    private int quantJogadores;

    private Recordes recordes;

    private boolean novosRecordesGerados;

    private boolean ultimoJogador;

    public Jogo() throws Exception {
        this.statusDoJogo = StatusDoJogo.inicializacao;
        this.jogadores = new ArrayList<Jogador>();
        this.jogada = new Jogada();
        this.podeJogarDados = false;
        this.podeMarcarDados = false;
        this.podeMarcarPontos = false;
        this.novosRecordesGerados = false;
        recordes = new Recordes();
        leArquivoDeRecordes();
    }

    public void definirJogadores(String[] nomes) {
        this.quantJogadores = nomes.length;
        this.jogadores.clear();
        for (String nom : nomes) {
            this.jogadores.add(new Jogador(nom));
        }
    }

    public void iniciarJogo() {
        this.statusDoJogo = StatusDoJogo.emAndamento;
        for (Jogador j : jogadores) {
            j.getCartela().limpaCartela();
        }
        jogada.desmarcarDados();
        jogada.zeraSeqJogada();
        this.podeJogarDados = true;
    }

    public void finalizarJogo() {
        this.statusDoJogo = StatusDoJogo.finalizacao;
        this.podeJogarDados = false;
        for (Jogador j : jogadores) {
            recordes.verificarRecorde(j.getNome(), j.getTotalDePontos());
        }
        try {
            gravaArquivoDeRecordes();
        } catch (Exception ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.statusDoJogo = StatusDoJogo.finalizado;
    }

    public StatusDoJogo getStatusDoJogo() {
        return statusDoJogo;
    }

    public void setStatusDoJogo(StatusDoJogo statusDoJogo) {
        this.statusDoJogo = statusDoJogo;
    }

    public String[][] getJogadores() {
        String[][] tmp = new String[quantJogadores][2];
        int i = 0;
        for (Jogador j : jogadores) {
            tmp[i][0] = j.getNome();
            tmp[i][1] = Integer.toString(j.getTotalDePontos());
            i++;
        }
        return tmp;
    }

    public Jogador getJogadorAtual() {
        return jogadores.get(jogadorAtual);
    }

    public Jogador getJogadorAnterior() {
        return jogadores.get(jogadorAnterior);
    }

    public int getIntJogadorAtual() {
        return jogadorAtual;
    }

    public void setJogadores(ArrayList<Jogador> val) {
        this.jogadores = val;
    }

    public void proximoJogador() {
        this.jogadorAnterior = this.jogadorAtual;
        this.jogadorAtual = (this.jogadorAtual + 1) % this.quantJogadores;
        if (jogadorAtual == quantJogadores - 1) {
            ultimoJogador = true;
        }
    }

    public void jogarDados() {
        if (jogada.jogarDados()) {
            getJogadorAtual().getCartela().verificarMarcacoes(jogada);
            if (jogada.getSeqJogada() > 0 & jogada.getSeqJogada() < 3) {
                podeMarcarPontos = true;
                podeMarcarDados = true;
            } else {
                podeJogarDados = false;
                podeMarcarDados = false;
            }
        }
    }

    public boolean marcarPontos(TipoDeColuna tpColuna, TipoDeLinha tpLinha) {
        if (getJogadorAtual().getCartela().marcaPontos(tpColuna, tpLinha, jogada)) {
            proximoJogador();
            jogada.zeraSeqJogada();
            podeJogarDados = true;
            podeMarcarDados = false;
            podeMarcarPontos = false;
            getJogadorAtual().getCartela().limpaStatus();
            if (ultimoJogador & getJogadorAtual().getCartela().cartelaCheia()) {
                finalizarJogo();
            }
            return true;
        }
        return false;
    }

    public void marcarDado(int posicao) {
        if (this.podeMarcarDados) {
            this.jogada.marcarDado(posicao);
        }
    }

    public boolean getPodeJogarDados() {
        return podeJogarDados;
    }

    public boolean getPodeMarcarDados() {
        return podeMarcarDados;
    }

    public int getQuantDadosLivres() {
        return jogada.getQuantDadosLivres();
    }

    public Jogada getJogada() {
        return jogada;
    }

    public int getTotalNosDados() {
        return jogada.getTotalNosDados();
    }

    public void leArquivoDeRecordes() {
        if (new File("recordes.bin").exists()) {
            try {
                ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("recordes.bin")));
                Recordes tmpRecordes = (Recordes) objectIn.readObject();
                objectIn.close();
                recordes = tmpRecordes;
            } catch (Exception e) {
                gravaArquivoDeRecordes();
                this.novosRecordesGerados = true;
            }
        } else {
            gravaArquivoDeRecordes();
        }
    }

    public void gravaArquivoDeRecordes() {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("recordes.bin")));
            objectOut.writeObject(recordes);
            objectOut.close();
        } catch (IOException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Recordes getRecordes() {
        return recordes;
    }

    public boolean novosRecordesGerados() {
        return novosRecordesGerados;
    }

    public boolean ultimoJogador() {
        return ultimoJogador;
    }

    public int getQuantJogadores() {
        return quantJogadores;
    }
}
