package mms;

import java.util.ArrayList;

public class Sensor extends EventHandler implements Sensing {

    ArrayList<Reasoning> listeners = new ArrayList<Reasoning>();

    public Sensor(String myName, MusicalAgent myAgent, String eventType) {
        super(myName, myAgent, Constants.COMP_SENSOR, eventType);
        comm.sensing = true;
    }

    public void registerListener(Reasoning reasoning) {
        listeners.add(reasoning);
    }

    public void sense(Event evt) {
        if (evt.eventType.equals(eventType)) {
            MusicalAgent.logger.info("[" + getAgent().getLocalName() + ":" + getName() + "] " + "Recebi um evento");
            process(evt);
            getAgent().getKB().writeEventRepository(Constants.REP_TYPE_INPUT, eventType, getName(), evt);
            for (Reasoning reasoning : listeners) {
                reasoning.newSense(this.eventType);
            }
            if (getAgent().getProperty(Constants.PROCESS_MODE, null).equals(Constants.MODE_BATCH)) {
                Command cmd = new Command(Constants.CMD_BATCH_EVENT_ACK);
                getAgent().sendMessage(getAgent().environmentAgent, cmd);
            }
            MusicalAgent.logger.info("[" + getAgent().getLocalName() + ":" + getName() + "] " + "Processei evento " + evt.timestamp);
        }
    }

    protected void process(Event evt) {
    }
}
