package Simulador.Job;

import Auxiliar.Fila;

/**
 *
 * @author Fernando/Matheus
 */
public class Processo {

    private int Id;

    private int totalTime;

    private int numSeg;

    private float momentoChegada;

    private Fila proc;

    /**
     * 
     * @param Id
     * Id do Job
     * @param totalTime
     * Tempo estimado para a finalizacao do Job
     * @param numProc
     * Numero de processos executados pelo Job
     * @param momentoChegada 
     * Momento em que o Job vai comecar a ser executado
     */
    public Processo(int Id, int totalTime, int numSeg, float momentoChegada) {
        this.Id = Id;
        this.totalTime = totalTime;
        this.numSeg = numSeg;
        this.momentoChegada = momentoChegada;
        proc = new Fila();
    }

    /**
     * Adiciona um segmento a fila de Segmentos a serem executados.
     * Só adiciona um processo se numProc > 0.
     * Decremente numProc cada vez que um processo e adicionado.
     * @param p
     * Processo a ser adicionado.
     */
    public void addSeg(Segmento p) {
        proc.addItem(p);
        p.setIdProcesso(Id);
    }

    /**
     * @return
     * proximo processo a ser executado.
     */
    public Segmento getSeg() {
        return (Segmento) proc.getItem();
    }

    /**
     * @return
     * momento em que o job comeca a ser executado
     */
    public float getMomentoChegada() {
        return momentoChegada;
    }
}
