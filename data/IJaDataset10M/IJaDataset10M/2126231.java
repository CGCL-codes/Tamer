package domotica.house.automation;

import java.util.ArrayList;
import java.util.List;

/**
 *  Classe astratta che definisce l'interfaccia base che deve essere implementata da
 *  tutti i componenti collegati al software Domotica.
 */
public abstract class ActuatorSensor {

    private String name;

    private String measureName;

    private boolean isOn;

    protected List<Parameter> parametersNames;

    public ActuatorSensor(String name, String measureName) {
        this.name = name;
        this.measureName = measureName;
        this.parametersNames = new ArrayList<Parameter>();
        this.setParametersList();
    }

    /**
	 * Implementando questo metodo si impostano i <code>Parameter</code> richiesti dal componente 
	 */
    public abstract void setParametersList();

    /**
	 * Ritorna la lista di parametri necessari per la definizione di un task del componente
	 */
    public List<Parameter> getTaskDesiredParameterList() {
        return this.parametersNames;
    }

    /**
	 * Restituisce il nome del componente
	 */
    public String getName() {
        return name;
    }

    /**
	 * Restituisce il nome della variabile fisica monitorata dal componente
	 */
    public String getMeasureName() {
        return measureName;
    }

    public synchronized boolean isOn() {
        return this.isOn;
    }

    public synchronized void setOn(boolean bool) {
        this.isOn = bool;
    }

    /**
	 * Metodo astratto per la connessione via IP
	 */
    public abstract void connect(String ip, String port);

    /**
	 * Restituisce il valore della variabile fisica monitorata 
	 */
    public abstract double getMeasure();

    /**
	 * Metodo astratto per la definizione di un task per il componente.
	 */
    public abstract void addTask(List<String> parameterList);

    /**
	 * Metodo astratto per interrompere il task del componente.
	 */
    public abstract void stopTask();
}
