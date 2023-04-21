package reversi;

public abstract class Estrategia {

    private double pesoComeco, pesoMeio, pesoFim;

    private double escala;

    public Estrategia(double pesoComeco, double pesoMeio, double pesoFim, double escala) {
        setPesoComeco(pesoComeco);
        setPesoMeio(pesoMeio);
        setPesoFim(pesoFim);
        setEscala(escala);
    }

    public abstract int avaliaEstado(GameState estado, int indiceJogador, Move ultimoMovimento);

    public double avaliaEstadoNormalizado(GameState estado, int indiceJogador, Move ultimoMovimento) {
        return (avaliaEstado(estado, indiceJogador, ultimoMovimento) / getEscala());
    }

    protected int getIndiceAdversario(int indiceJogador) {
        if (indiceJogador == 0) return 1; else return 0;
    }

    public double getPesoComeco() {
        return pesoComeco;
    }

    public void setPesoComeco(double pesoComeco) {
        System.out.println(toString());
        this.pesoComeco = pesoComeco;
    }

    public double getPesoMeio() {
        return pesoMeio;
    }

    public void setPesoMeio(double pesoMeio) {
        System.out.println(toString());
        this.pesoMeio = pesoMeio;
    }

    public double getPesoFim() {
        return pesoFim;
    }

    public void setPesoFim(double pesoFim) {
        System.out.println(toString());
        this.pesoFim = pesoFim;
    }

    public double getEscala() {
        return escala;
    }

    public void setEscala(double escala) {
        this.escala = escala;
    }
}
