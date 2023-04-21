package br.com.rmt.simplestgrid.agentes.observadores;

import br.com.rmt.simplestgrid.modelo.dto.Computador;

/**
 * Observador de computadores do Grid
 * @author Ronneesley Moura Teles
 * @since 18/08/2010 12:36
 */
public interface ComputadoresObserver {

    public void novoComputador(Computador computador);

    public void computadorRemovido(Computador computador);
}
