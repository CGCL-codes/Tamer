package br.ufrj.dcc.sistemasoperacionais.passagensaereas.servidor.controle;

public class Reserva {

    private Object cliente;

    private int numeroDeAssentos;

    private TimerLiberacaoReserva timer = null;

    public Reserva(Object cliente, int numeroDeAssentos) {
        this.cliente = cliente;
        this.numeroDeAssentos = numeroDeAssentos;
    }

    public Object GetCliente() {
        return this.cliente;
    }

    public int GetNumeroAssentos() {
        return this.numeroDeAssentos;
    }

    public void SetNumeroAssentos(int numeroDeAssentos) {
        this.numeroDeAssentos = numeroDeAssentos;
    }

    public void setTimerLiberacao(TimerLiberacaoReserva timer) {
        if (this.timer != null) this.timer.CancelaTimer();
        this.timer = timer;
    }
}
