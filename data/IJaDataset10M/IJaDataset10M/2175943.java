package components.neuralnetwork;

import gui.EasyBotApp;
import utils.tracer.TraceState;
import utils.tracer.Traceable;
import components.Component;

/**
 * Implementaci�n de una red neuronal de Schmajuk y Thieme, seg�n se
 * especifica en <i>Schmajuk, NA, & Thieme, AD (1992). Purposive behaviour
 * and cognitive mapping: A neural network model.</i> <br><br>
 * 
 * La red trabaja con un grafo de lugares (laberinto). El primer lugar
 * (partida) es aquel con identificador 1, mientras que el �ltimo lugar
 * (llegada u objetivo) es el de mayor valor (identificador m�s grande).
 */
public class NeuralNetwork extends Component implements Traceable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    NeuralNetworkState lastestLoadedState;

    ExternalNeuralNetwork externalNeuralNetwork;

    public NeuralNetwork() {
        super("", "");
        externalNeuralNetwork = EasyBotApp.getInstance().getExternalNetwork();
    }

    public int load(String file) {
        return externalNeuralNetwork.load_network(file);
    }

    public int unload() {
        return externalNeuralNetwork.unload_network();
    }

    public void setPlaces(int[][] places) throws NeuralNetworkException {
        if (places == null) throw new NeuralNetworkException("La matr�z de lugares es nula.");
        externalNeuralNetwork.start(places);
    }

    /**
	 * Esta versi�n del m�todo <code>move</code> es similar a
	 * {@link #move(int[]) move(int[])}, salvo que permite establecer el lugar
	 * <i>actual</i>. As� es m�s versatil, ya que en un caso real, el robot
	 * podr�a haber alterado su curso (por razones varias), encontr�ndose
	 * ahora en un lugar distinto al que supone la red neuronal.
	 * @param currentViews	lugares siendo vistos desde la posici�n actual.
	 * @param currentPlace	lugares actual.
	 */
    public void move(int currentViews[], int currentPlace) {
        if (externalNeuralNetwork.isEnabled()) {
            externalNeuralNetwork.move(currentViews, currentPlace);
            setChanged();
            notifyObservers();
        }
    }

    /**
	 * Reinicializa la red neuronal, estableciendo el origen como lugar actual.
	 * No altera los pesos sin�pticos calculados en ejecuciones previas.
	 */
    public void reset() {
        if (externalNeuralNetwork.isEnabled()) {
            externalNeuralNetwork.reset();
            setChanged();
            notifyObservers();
        }
    }

    /**
	 * Retorna el identificador del lugar actual.
	 * @return lugar actual.
	 */
    public int getCurrentPlace() {
        if (externalNeuralNetwork.isEnabled()) {
            return externalNeuralNetwork.getCurrentPlace();
        }
        return 0;
    }

    /**
	 * Retorna la cantidad de movimientos efectuados desde que se parti� del
	 * origen. Esto es, la cantidad de caminos atravesados (lo que equivale a
	 * las decisiones tomadas).
	 * @return cantidad de movimientos.
	 */
    public int getNumMovements() {
        if (externalNeuralNetwork.isEnabled()) {
            return externalNeuralNetwork.getNumMovements();
        }
        return 0;
    }

    public String getXMLConfig(String ident) {
        String xmlConfig = new String();
        String tabulator = new String();
        tabulator = tabulator.concat(ident);
        xmlConfig = xmlConfig.concat(tabulator + "<neuralnetwork name=\"" + getName() + "\" description=\"" + getDescription() + "\" />");
        return xmlConfig;
    }

    public NeuralNetworkState getWeights() {
        if (externalNeuralNetwork.isEnabled()) {
            double[][] pesos = externalNeuralNetwork.getWeights();
            return new NeuralNetworkState(pesos);
        }
        return lastestLoadedState;
    }

    public void setNeuralNetworkState(TraceState state) {
        if (state == null) return;
        lastestLoadedState = (NeuralNetworkState) state;
        double[][] vals = lastestLoadedState.getValues();
        if (vals == null || vals.length == 0) return;
        if (externalNeuralNetwork.isEnabled()) {
            externalNeuralNetwork.setWeights(vals);
        }
    }

    public TraceState getTraceableState() {
        return getWeights();
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return false;
    }
}
