package org.itver.arm.models.log;

import java.util.ArrayList;
import org.itver.arm.models.elements.Element;
import org.itver.arm.models.elements.Joint;
import org.itver.arm.models.nodes.log.LogOrderNode;

/**
 * Modelo que contiene una lista de pasos a realizar de manera paralela.
 * El número de orden especifica la posición en la secuencia de log.
 * El tiempo especifica la espera ante de pasar al siguiente orden en la lista
 * del Log.
 * @author pablo
 */
public final class LogOrder extends Model {

    /**
     * Nombre de la propiedad que cambia al modificar el número de órden.
     * @see #setOrder(int) setOrder()
     */
    public static final String NUMBER = "number";

    /**
     * Nombre de la propiedad que cambia al modificar el tiempo de la órden.
     * @see #setTime(long) setTime()
     */
    public static final String TIME = "time";

    /**
     * Nombre de la propiedad que cambia al añadir o quitar pasos en la lista.
     * @see #addStep(org.itver.arm.models.log.LogStep) addStep()
     * @see #removeStep(org.itver.arm.models.log.LogStep) removeStep()
     */
    public static final String STEPS = "steps";

    private int order;

    private ArrayList<LogStep> steps;

    private LogOrderNode node;

    /**
     * Constructor default.
     */
    public LogOrder() {
        this.steps = new ArrayList<LogStep>();
        this.order = Element.DEFAULT_ID;
    }

    /**
     * Agrega un {@link LogStep Paso} a la lista de éste órden.
     * @param step Paso que forma parte de la orden.
     */
    public void addStep(LogStep step) {
        if (this.steps.contains(step) || step == null) return;
        LogStep aux = this.stepWithJoint(step.getJoint());
        if (aux != null) {
            aux.setValue(step.getValue());
            aux.setTime(step.getTime());
        } else this.steps.add(step);
        this.pcs.firePropertyChange(STEPS, null, step);
    }

    /**
     * Retira el {@link LogStep Paso} de la lista.
     * @param step Paso a retirar de la orden.
     */
    public void removeStep(LogStep step) {
        if (step == null) return;
        this.steps.remove(step);
        this.pcs.firePropertyChange(STEPS, step, null);
    }

    /**
     * Devuelve la lista de los pasos asociados a ésta órden.
     * @return Copia del arreglo con todos los pasos en la orden.
     */
    public LogStep[] getSteps() {
        LogStep[] result = new LogStep[this.steps.size()];
        return this.steps.toArray(result);
    }

    /**
     * Obtiene el número de orden
     * @return El número de orden.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Cambia el número de órden.
     * @param order El nuevo número de orden.
     */
    public void setOrder(int order) {
        int old = this.order;
        this.order = order;
        this.pcs.firePropertyChange(NUMBER, old, this.order);
    }

    /**
     * Obtiene el tiempo de espera de la orden.
     * @return el tiempo de espera de la orden, en milisegundos.
     */
    public long getTime() {
        long time = 0;
        for (LogStep step : this.steps) if (time < step.getTime()) time = step.getTime();
        return time;
    }

    /**
     * Obtiene el nodo con los datos del orden, para uso en la plataforma de
     * NetBeans.
     * @return nodo para su uso en la plataforma de NetBeans.
     */
    public LogOrderNode getNode() {
        return node;
    }

    /**
     * Asigna el nodo con los datos del orden, para uso en la plataforma de
     * NetBeans.
     * @param node el nodo relacionado con el modelo.
     */
    public void setNode(LogOrderNode node) {
        this.node = node;
    }

    public LogStep stepWithJoint(Joint joint) {
        LogStep result = null;
        for (LogStep step : this.steps) if (step.getJoint() == joint) result = step;
        return result;
    }
}
