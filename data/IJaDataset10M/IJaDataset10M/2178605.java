package sk.tuke.ess.lib.integrator.unsaturated;

import java.io.Serializable;
import java.util.List;
import sk.tuke.ess.lib.IntegrationMethod;
import sk.tuke.ess.lib.integrator.IExecCondition;
import sk.tuke.ess.lib.integrator.IntegrationMethods;
import sk.tuke.ess.sim.StartEvent;
import sk.tuke.ess.sim.event.Event;
import sk.tuke.ess.sim.event.Events;
import sk.tuke.ess.sim.event.Time;
import sk.tuke.ess.sim.scheme.InputValue;

/**
 * Vypocet konstant Eulerovou metodou
 * rovnina : Yt+1 = Yt + h * f
 * @author Marek
 */
public class VEuler implements Serializable, IntegrationMethod, IExecCondition {

    private double y_t, step = 0.001;

    private InputValue<Double> start;

    private double m_result;

    private Time time;

    private Events events;

    private InputValue<Double> input;

    private List list;

    /**
     * nastavenie bloku
     */
    @Override
    public void started(StartEvent event) {
        m_result = start.get();
        if (list.isEmpty()) {
            events.addEvent(new Intergration(time.getActualTime()));
        }
        list.add(this);
    }

    /**
     * vykonanie bloku
     * @return result
     */
    @Override
    public double execute() {
        return y_t;
    }

    /**
     * @return the m_result
     */
    @Override
    public InputValue<Double> getStart() {
        return start;
    }

    /**
     * @param m_result the m_result to set
     */
    @Override
    public void setStart(InputValue<Double> start) {
        this.start = start;
    }

    /**
     * @return method
     */
    @Override
    public String getMethod() {
        return IntegrationMethods.euler;
    }

    /**
     * @return the time
     */
    @Override
    public Time getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    @Override
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * @return the events
     */
    @Override
    public Events getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    @Override
    public void setEvents(Events events) {
        this.events = events;
    }

    /**
     * @return the input
     */
    @Override
    public InputValue<Double> getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    @Override
    public void setInput(InputValue<Double> input) {
        this.input = input;
    }

    /**
     * @return the step
     */
    @Override
    public double getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    @Override
    public void setStep(double step) {
        this.step = step;
    }

    /**
     * @return the list
     */
    @Override
    public List getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    @Override
    public void setList(List list) {
        this.list = list;
    }

    /**
     * Udalost predstavuje jeden integracny krok
     */
    private class Intergration extends Event {

        /**
         * konstruktor
         * @param time
         */
        public Intergration(double time) {
            super(time, 6);
        }

        /**
         * aktivacia udalosti
         */
        @Override
        public void activate() {
            double step = time.nextTime() - time.getActualTime();
            step = (step < VEuler.this.getStep()) ? step : VEuler.this.getStep();
            for (Object e : list) {
                ((VEuler) e).y_t = ((VEuler) e).m_result;
            }
            for (Object e : list) {
                ((VEuler) e).m_result = ((VEuler) e).m_result + step * ((InputValue<Double>) ((VEuler) e).input).get();
            }
            events.addEvent(new Intergration(time.getActualTime() + step));
        }
    }
}
