package yam.engine;

import java.util.ArrayList;

public class Cartela {

    private ArrayList<Coluna> colunas;

    private int totalDePontos;

    public Cartela() {
        this.colunas = new ArrayList<Coluna>();
        this.colunas.add(new Coluna(TipoDeColuna.desce));
        this.colunas.add(new Coluna(TipoDeColuna.sobe));
        this.colunas.add(new Coluna(TipoDeColuna.desordem));
        this.colunas.add(new Coluna(TipoDeColuna.seco));
        this.setTotalDePontos(0);
    }

    public ArrayList<Coluna> getColunas() {
        return colunas;
    }

    public void setColunas(ArrayList<Coluna> val) {
        this.colunas = val;
    }

    public int getTotalDePontos() {
        return totalDePontos;
    }

    public void setTotalDePontos(int totalDePontos) {
        this.totalDePontos = totalDePontos;
    }

    public int getPontos(TipoDeColuna coluna, TipoDeLinha linha) {
        return colunas.get(coluna.ordinal()).getPontos(linha);
    }

    public StatusDaLinha getStatus(TipoDeColuna coluna, TipoDeLinha linha) {
        return colunas.get(coluna.ordinal()).getStatus(linha);
    }

    public void setPontos(TipoDeColuna coluna, TipoDeLinha linha, int pontos) {
        this.colunas.get(coluna.ordinal()).setPontos(linha, pontos);
    }

    public boolean marcaPontos(TipoDeColuna coluna, TipoDeLinha linha, Jogada jog) {
        if (colunas.get(coluna.ordinal()).marcaPontos(linha, jog)) {
            sumarizaTotais();
            return true;
        } else {
            return false;
        }
    }

    public void sumarizaTotais() {
        int temp = 0;
        for (TipoDeColuna col : TipoDeColuna.values()) {
            temp += getPontos(col, TipoDeLinha.segundoEterceiroTotais);
        }
        this.setTotalDePontos(temp);
    }

    public boolean cartelaCheia() {
        boolean cheia = true;
        for (TipoDeColuna col : TipoDeColuna.values()) {
            if (!colunas.get(col.ordinal()).colunaCheia()) {
                cheia = false;
                break;
            }
        }
        return cheia;
    }

    public void limpaStatus() {
        for (Coluna col : colunas) {
            for (TipoDeLinha lin : TipoDeLinha.values()) {
                if (col.getStatus(lin) == StatusDaLinha.marcavel | col.getStatus(lin) == StatusDaLinha.riscavel) {
                    col.setStatus(lin, StatusDaLinha.livre);
                }
            }
        }
    }

    public void limpaPontos() {
        for (Coluna col : colunas) {
            col.limpaMaxeMin();
            for (TipoDeLinha lin : TipoDeLinha.values()) {
                col.limpaPontos(lin);
                col.setStatus(lin, StatusDaLinha.livre);
            }
        }
    }

    public void limpaCartela() {
        limpaPontos();
        limpaStatus();
        this.setTotalDePontos(0);
    }

    public int[][] getArrPontos() {
        int[][] retArray = new int[colunas.size()][colunas.get(0).getLinhas().size() + 1];
        for (TipoDeColuna col : TipoDeColuna.values()) {
            for (TipoDeLinha lin : TipoDeLinha.values()) {
                retArray[col.ordinal()][lin.ordinal()] = getPontos(col, lin);
            }
        }
        retArray[2][18] = getTotalDePontos();
        return retArray;
    }

    public StatusDaLinha[][] getArrStatus() {
        StatusDaLinha[][] retArray = new StatusDaLinha[colunas.size()][colunas.get(0).getLinhas().size() + 1];
        for (TipoDeColuna col : TipoDeColuna.values()) {
            for (TipoDeLinha lin : TipoDeLinha.values()) {
                if ((lin == TipoDeLinha.primeiroTotal | lin == TipoDeLinha.bonus | lin == TipoDeLinha.segundoTotal | lin == TipoDeLinha.terceiroTotal | lin == TipoDeLinha.segundoEterceiroTotais) & getPontos(col, lin) > 0) {
                    retArray[col.ordinal()][lin.ordinal()] = StatusDaLinha.marcada;
                } else {
                    retArray[col.ordinal()][lin.ordinal()] = getStatus(col, lin);
                }
            }
        }
        if (totalDePontos > 0) {
            retArray[2][18] = StatusDaLinha.marcada;
        }
        return retArray;
    }

    public void verificarMarcacoes(Jogada jogada) {
        for (Coluna c : colunas) {
            c.verificarMarcacoes(jogada);
        }
    }
}
