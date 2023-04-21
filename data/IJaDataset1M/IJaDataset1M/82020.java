package frsf.cidisi.faia.examples.search.snake;

import calculador.Calculador;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

public class AmbienteSnake extends Environment {

    private Calculador calculador;

    public AmbienteSnake(Calculador calculador) {
        this.calculador = calculador;
        this.environmentState = new EstadoAmbiente(this.calculador);
    }

    @Override
    public Perception getPercept() {
        PercepcionSnake p = new PercepcionSnake();
        p.setSensorNorte(this.getNorte());
        p.setSensorOeste(this.getOeste());
        p.setSensorEste(this.getEste());
        p.setSensorSur(this.getSur());
        return p;
    }

    public int getNorte() {
        return ((EstadoAmbiente) this.environmentState).getNorte();
    }

    public int getOeste() {
        return ((EstadoAmbiente) this.environmentState).getOeste();
    }

    public int getEste() {
        return ((EstadoAmbiente) this.environmentState).getEste();
    }

    public int getSur() {
        return ((EstadoAmbiente) this.environmentState).getSur();
    }

    public String toString() {
        return environmentState.toString();
    }
}
