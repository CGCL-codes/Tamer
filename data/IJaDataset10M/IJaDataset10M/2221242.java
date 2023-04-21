package reversi;

import java.util.List;

public class AvaliacaoTotal {

    private GameState estado;

    private int indiceJogador;

    private int indiceAdversario;

    private List<Estrategia> estrategias;

    private Move movimento;

    private int estagio;

    private static final int INICIO = 0, MEIO = 1, FIM = 2;

    public AvaliacaoTotal(List<Estrategia> estrategias) {
        setEstrategias(estrategias);
    }

    public double avalia() {
        double novaAvaliacao = 0;
        for (Estrategia estrategia : getEstrategias()) {
            switch(estagio) {
                case INICIO:
                    novaAvaliacao += estrategia.getPesoComeco() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                    break;
                case MEIO:
                    novaAvaliacao += estrategia.getPesoMeio() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                    break;
                case FIM:
                    novaAvaliacao += estrategia.getPesoFim() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                    break;
                default:
                    break;
            }
        }
        return novaAvaliacao;
    }

    public double avaliaErro() {
        double novaAvaliacao = 0;
        Estrategia estrategia = getEstrategias().get(0);
        switch(estagio) {
            case INICIO:
                novaAvaliacao += estrategia.getPesoComeco() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                break;
            case MEIO:
                novaAvaliacao += estrategia.getPesoMeio() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                break;
            case FIM:
                novaAvaliacao += estrategia.getPesoFim() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                break;
            default:
                break;
        }
        estrategia = getEstrategias().get(1);
        switch(estagio) {
            case INICIO:
                novaAvaliacao += estrategia.getPesoComeco() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                break;
            case MEIO:
                novaAvaliacao += estrategia.getPesoMeio() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                break;
            case FIM:
                novaAvaliacao += estrategia.getPesoFim() * estrategia.avaliaEstadoNormalizado(getEstado(), getIndiceJogador(), getMovimento());
                break;
            default:
                break;
        }
        return novaAvaliacao;
    }

    private Move getMovimento() {
        return movimento;
    }

    public boolean estadoTerminal(GameState estado) {
        return false;
    }

    public GameState getEstado() {
        return estado;
    }

    public void setEstado(GameState estado) {
        this.estado = estado;
        estagio = (estado.getMarkCount(indiceJogador) + estado.getMarkCount(indiceAdversario)) / 22;
    }

    public int getIndiceJogador() {
        return indiceJogador;
    }

    public void setIndiceJogador(int indiceJogador) {
        this.indiceJogador = indiceJogador;
    }

    public int getIndiceAdversario() {
        return indiceAdversario;
    }

    public void setIndiceAdversario(int indiceAdversario) {
        this.indiceAdversario = indiceAdversario;
    }

    public List<Estrategia> getEstrategias() {
        return estrategias;
    }

    public void setEstrategias(List<Estrategia> estrategias) {
        this.estrategias = estrategias;
    }

    public void setMove(Move lastMove) {
        this.movimento = lastMove;
    }
}
