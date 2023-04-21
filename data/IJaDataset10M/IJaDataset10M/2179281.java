package jfan.fan;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import jfan.fan.padroes.CaracteristicaFAN;
import jfan.fan.padroes.PadraoFAN;
import jfan.fan.temperas.ITemperaSimulada;

/**
 * A classe RedeFAN faz todo papel de gerenciar o treinamento, teste, valida��o
 * e classifica��o. Essa classe mant�m a rede atual do treinamento e tamb�m a
 * melhor rede treinada, al�m de manter a taxa de acerto da melhor rede.
 * Esta classe deixou de ser um singleton a partir da vers�o 0.8.0.
 * @author Filipe Pais Lenfers
 * @version 0.8.0
 */
public class RedeFAN implements Serializable, Cloneable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4899207505553647220L;

    private transient Object lockTempera = new Object();

    /**
	 * O peso de base para ser usado quando a op��o de randomizar os pesos estiver
	 * ativa.
	 */
    private int pesoBase = 0;

    /**
	 * A T�mpera simulada que ser� usada no treinametno da rede.
	 */
    private ITemperaSimulada tempera = null;

    public ITemperaSimulada getTemperaSimulada() {
        return tempera;
    }

    /**
	 * Objeto Ramdom para captura de n�meros pseudo-aleat�rios.
	 */
    private transient Random random = new Random();

    /**
	 * Os neur�nios da rede FAN, chamados de Y na tese de doutorado. � iniciado
	 * com 2 de capacidade porque este � provavelmente (provavelmente n�o
	 * significa obrigatoriamente) o n�mero m�nimo de classes.
	 */
    private final NeuronioFAN[] neuronios;

    private int[] pesosNeuronios;

    /**
	 * O tamanho do raio para cria��o dos vizinhos, a vari�vel d na tese de
	 * doutorado.
	 */
    private int raioDifuso;

    /**
	 * O tamanho do conjunto difuso dos neur�nios, a vari�vel J na tese de
	 * doutorado "FAN 2002: UM MODELO NEURO-FUZZY PARA RECONHECIMENTO DE
	 * PADR�ES"
	 */
    private int suporteConjuntosDifusos;

    private int numeroCaracteristicas;

    @Override
    protected Object clone() {
        NeuronioFAN[] neuronios = new NeuronioFAN[this.neuronios.length];
        for (int i = 0; i < neuronios.length; i++) {
            neuronios[i] = (NeuronioFAN) this.neuronios[i].clone();
        }
        RedeFAN clone = new RedeFAN(neuronios);
        clone.numeroCaracteristicas = this.numeroCaracteristicas;
        clone.pesoBase = this.pesoBase;
        clone.pesosNeuronios = this.pesosNeuronios.clone();
        clone.raioDifuso = this.raioDifuso;
        clone.suporteConjuntosDifusos = this.suporteConjuntosDifusos;
        for (NeuronioFAN neuronioFAN : neuronios) {
            neuronioFAN.setRede(clone);
        }
        return clone;
    }

    private RedeFAN(NeuronioFAN[] neuronios) {
        this.neuronios = neuronios;
    }

    public RedeFAN(NeuronioFAN[] neuronios, int[] pesosNeuronios, int pesoBase, int raioDifuso, int suporteConjuntosDifusos, int numeroCaracteristicas) {
        for (NeuronioFAN n : neuronios) {
            n.setRede(this);
        }
        this.neuronios = neuronios;
        this.pesosNeuronios = pesosNeuronios;
        this.pesoBase = pesoBase;
        this.raioDifuso = raioDifuso;
        this.suporteConjuntosDifusos = suporteConjuntosDifusos;
        this.numeroCaracteristicas = numeroCaracteristicas;
    }

    /**
	 * Construtor da rede.
	 *  
	 * @param raioDifuso
	 *            o raio difuso que ser� usado
	 * @param suporteConjuntosDifusos
	 *            o suport de conjuntos difusos (J) que ser� usado
	 */
    public RedeFAN(int raioDifuso, int suporteConjuntosDifusos, int numeroCaracteristicas, int numeroClasses) {
        this.raioDifuso = raioDifuso;
        this.suporteConjuntosDifusos = suporteConjuntosDifusos;
        this.numeroCaracteristicas = numeroCaracteristicas;
        neuronios = new NeuronioFAN[numeroClasses];
        for (int i = 0; i < neuronios.length; i++) {
            neuronios[i] = new NeuronioFAN(this);
        }
        pesosNeuronios = new int[numeroClasses];
        Arrays.fill(pesosNeuronios, 1000);
    }

    /**
	 * Retorna o peso de base usado para randomizar os pesos.
	 * @return O valor do peso base.
	 */
    public int getPesoBase() {
        return pesoBase;
    }

    /**
	 * Treina a rede usando o conjunto de treinamento passado
	 * como par�metro.
	 * @param conjTreinamento O conjunto que ser� usado para treinar a rede.
	 */
    public void treinar(Collection<PadraoFAN> conjuntoTreinamento) {
        for (PadraoFAN p : conjuntoTreinamento) treinar(p);
    }

    /**
	 * Define o peso do neur�nio.
	 * @param classeNeuronio A classe que o neur�nio tem associada.
	 * @param peso O novo peso do neur�nio.
	 * @throws Exception 
	 */
    public void setPesoNeuronio(int neuronio, int peso) throws Exception {
        throw new Exception("Método não implementado");
    }

    /**
	 * Define a tempera que ser� usada no treinamento. Se passado null como
	 * par�metro a rede para de usar a t�mpera.
	 * @param t A t�mpera que o treinamento usar�. Se null a t�mpera deixa de ser usada.
	 */
    public void setTemperaSimulada(ITemperaSimulada t) {
        synchronized (lockTempera) {
            this.tempera = t;
        }
    }

    /**
	 * Classifica o conjunto de classifica��o.
	 */
    public void classificar(Collection<PadraoFAN> conjuntoClassificacao) {
        for (PadraoFAN p : conjuntoClassificacao) {
            p.setClasse(classificar(p));
        }
    }

    public NeuronioFAN[] getNeuronios() {
        return neuronios;
    }

    public int getNumeroCaracteristicas() {
        return this.numeroCaracteristicas;
    }

    public int getRaioDifuso() {
        return raioDifuso;
    }

    public int getNumeroSuporteConjuntosDifusos() {
        return suporteConjuntosDifusos;
    }

    private int classificar(PadraoFAN p) {
        double forcaAtual;
        int i = 0, melhorNeuronio = 0;
        double maiorForca = neuronios[i].determinarForca(p);
        for (i = 1; i < neuronios.length; i++) {
            forcaAtual = neuronios[i].determinarForca(p);
            if (maiorForca < forcaAtual) {
                melhorNeuronio = i;
                maiorForca = forcaAtual;
            }
        }
        return melhorNeuronio;
    }

    /**
	 * Testa o padrão fornecido.
	 * @param p Padrão a ser testado.
	 * @return Retorna false para erro e true para acerto.
	 */
    public boolean testar(PadraoFAN p) {
        int respostaRede = classificar(p);
        if (respostaRede == p.getClasse()) {
            return true;
        }
        return false;
    }

    public int[][] testar(Collection<PadraoFAN> padroes) {
        int[][] matrizErros = new int[neuronios.length][2];
        for (int[] i : matrizErros) Arrays.fill(i, 0);
        for (PadraoFAN p : padroes) {
            if (testar(p)) {
                matrizErros[p.getClasse()][0]++;
            } else {
                matrizErros[p.getClasse()][1]++;
            }
        }
        return matrizErros;
    }

    /**
	 * Treina os neur�nios usando o padr�o passado.
	 * @param p Padr�o que ensinar� os neur�nios.
	 */
    public void treinar(PadraoFAN p) {
        int respostaRede = classificar(p);
        if (respostaRede != p.getClasse()) {
            if (pesosNeuronios[respostaRede] >= 1000 || random.nextInt(1001) > pesosNeuronios[respostaRede]) {
                penalizar(neuronios[respostaRede], p);
            }
        }
        reforcar(neuronios[p.getClasse()], p);
    }

    /**
	 * Refor�a o neur�nio que possui a mesma classe que o padr�o passado como par�metro.
	 * @param p O padr�o usado de base para o refor�o.
	 */
    protected void reforcar(NeuronioFAN neuronio, PadraoFAN p) {
        CaracteristicaFAN caracFan;
        int k, fim;
        double[] conjDifuso;
        int carateristicas = numeroCaracteristicas;
        for (int i = 0; i < carateristicas; i++) {
            k = 0;
            caracFan = p.getCaracteristicaFAN(i);
            conjDifuso = caracFan.getConjuntoDifuso();
            fim = caracFan.getFim();
            fim++;
            for (int j = caracFan.getInicio(); j < fim; j++) {
                neuronio.adicionaValor(i, j, conjDifuso[k]);
                k++;
            }
        }
    }

    protected void penalizar(NeuronioFAN neuronio, PadraoFAN p) {
        double novoValor, alpha;
        int fim, k;
        CaracteristicaFAN caracFan;
        double[] conjDifuso;
        int carateristicas = numeroCaracteristicas;
        for (int i = 0; i < carateristicas; i++) {
            k = 0;
            caracFan = p.getCaracteristicaFAN(i);
            conjDifuso = caracFan.getConjuntoDifuso();
            fim = caracFan.getFim();
            fim++;
            for (int j = caracFan.getInicio(); j < fim; j++) {
                novoValor = neuronio.getValor(i, j);
                synchronized (lockTempera) {
                    alpha = 1.0;
                    if (this.tempera != null) {
                        alpha = tempera.getValor();
                    }
                }
                novoValor *= 1 - alpha * conjDifuso[k];
                if (novoValor < 0) {
                    novoValor = 0;
                }
                neuronio.setValor(i, j, novoValor);
                k++;
            }
        }
    }
}
