package StreetController;

import Agents.AgentMessageReceiver;
import Agents.agentFrame;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.Service;
import org.objectweb.proactive.core.config.ProActiveConfiguration;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.core.node.NodeFactory;
import org.objectweb.proactive.ext.migration.Destination;
import org.objectweb.proactive.ext.migration.MigrationStrategyImpl;
import carGeneration.Car;
import carGeneration.CarGeneratorAgent;
import event.CarArrival;
import event.Event;
import Bloques.*;
import event.*;

public class StreetControllerAgent implements org.objectweb.proactive.RunActive, java.io.Serializable {

    private boolean onItinerary;

    private boolean initialized;

    private transient agentFrame myFrame;

    private AgentMessageReceiver controler;

    private StreetControllerAgent otherStreetAgent;

    private javax.swing.ImageIcon face;

    private org.objectweb.proactive.ext.migration.MigrationStrategy myStrategy;

    private org.objectweb.proactive.ext.migration.MigrationStrategyManager myStrategyManager;

    private int index;

    private String name;

    private String[] itinerary;

    int EMPTY = -1;

    int VERDE = 1;

    int ROJO = 0;

    int AMBAR = 3;

    int SE = 1;

    int CE = 2;

    int LCE = 3;

    int CLE = 5;

    int CLEE = 6;

    int ALE = 7;

    int LLE = 8;

    int BE = 9;

    int AE = 10;

    int WSE = 11;

    int WBE = 12;

    Street[] sources = new Street[20];

    Event rootControl = new Event();

    Street str_1;

    Street str_2;

    Street str_3;

    Street str_4;

    Intersection itr_1;

    /**
	 * Empty constructor for ProActive
	 */
    public StreetControllerAgent() {
    }

    public StreetControllerAgent(Integer ind) {
        this.index = ind.intValue();
        this.name = "Agent " + index;
        rootControl.setTime(0.0);
        str_1 = new Street(1, rootControl, false);
        str_2 = new Street(2, rootControl, true);
        str_3 = new Street(3, rootControl, false);
        str_4 = new Street(4, rootControl, true);
        itr_1 = new Intersection(10, rootControl, false);
        str_1.addOut(itr_1);
        str_2.addIn(itr_1);
        str_3.addOut(itr_1);
        str_4.addIn(itr_1);
        str_1.setSize(132.0);
        str_2.setSize(132.0);
        str_3.setSize(132.0);
        str_4.setSize(132.0);
        itr_1.addOut(str_2);
        itr_1.addOut(str_4);
        itr_1.addIn(str_1);
        itr_1.addIn(str_3);
        itr_1.setSize(132.0);
        sources[0] = str_1;
        sources[1] = str_3;
        rootControl.izquierdo = rootControl;
        rootControl.derecho = rootControl;
        rootControl.anterior = rootControl;
        rootControl.siguiente = rootControl;
    }

    public void loop() {
        rebuild();
    }

    public void rebuild() {
        Body body = ProActive.getBodyOnThis();
        myFrame = new agentFrame(face, body.getNodeURL(), index);
        sendMessageToControler("I just got in node " + ProActive.getBodyOnThis().getNodeURL());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    public void clean() {
        if (myFrame != null) {
            myFrame.dispose();
            myFrame = null;
        }
    }

    public String toString() {
        return this.name;
    }

    public void initialize(String[] s) {
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            java.net.URL u = cl.getResource("Resources/agent-police-trafic.jpg");
            face = new javax.swing.ImageIcon(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myStrategyManager = new org.objectweb.proactive.ext.migration.MigrationStrategyManagerImpl((org.objectweb.proactive.core.body.migration.Migratable) org.objectweb.proactive.ProActive.getBodyOnThis());
        myStrategyManager.onDeparture("clean");
        myStrategy = new MigrationStrategyImpl();
        itinerary = s;
        for (int i = 0; i < s.length; i++) myStrategy.add(s[i], null);
    }

    public void setControler(AgentMessageReceiver c) {
        this.controler = c;
        this.initialized = true;
    }

    public void setOther(StreetControllerAgent penguin) {
        this.otherStreetAgent = penguin;
    }

    public Destination nextHop() {
        Destination r = myStrategy.next();
        if (r == null) {
            myStrategy.reset();
            r = myStrategy.next();
        }
        return r;
    }

    public void runActivity(Body b) {
        double time = 0.0;
        Service service = new Service(b);
        if (!initialized) {
            service.blockingServeOldest();
        }
        rebuild();
        Destination r = null;
        Event source;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("c:/temp/log.txt", true);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        PrintWriter miArchivo = new PrintWriter(out);
        miArchivo.write("objectID,");
        miArchivo.write("evTime,");
        miArchivo.write("segment,");
        miArchivo.write("evPos,");
        miArchivo.write("evType,");
        miArchivo.println();
        miArchivo.close();
        while (b.isActive()) {
            while (service.hasRequestToServe()) {
                service.serveOldest();
            }
            if (onItinerary) {
                if (rootControl.derecho != rootControl) {
                    try {
                        source = sacarEvento();
                        if (source.evTime() < time) {
                            System.out.println("At> " + time + "Error tipo evento:" + source.getEventType() + "At time> " + source.evTime());
                            throw new UnsupportedOperationException("new event is newer than actual time");
                        }
                        rootControl.setTime(source.evTime());
                        time = source.evTime();
                        if (source instanceof CarArrival) {
                            CarArrival evento = (CarArrival) source;
                            evento.putStreetAgent(this);
                            evento = null;
                            ProActive.turnActive(source);
                        }
                        if (source instanceof CarArrivalLink) {
                            CarArrivalLink evento = (CarArrivalLink) source;
                            evento.putStreetAgent(this);
                            evento = null;
                            ProActive.turnActive(source);
                        }
                        if (source instanceof CarLeaveLink) {
                            CarLeaveLink evento = (CarLeaveLink) source;
                            evento.putStreetAgent(this);
                            evento = null;
                            ProActive.turnActive(source);
                        }
                        if (source instanceof CarStart) {
                            CarStart evento = (CarStart) source;
                            evento.putStreetAgent(this);
                            evento = null;
                            ProActive.turnActive(source);
                        }
                        if (source instanceof CarStop) {
                            CarStop evento = (CarStop) source;
                            evento.putStreetAgent(this);
                            evento = null;
                            ProActive.turnActive(source);
                        }
                        if (source instanceof LightChangeEvent) {
                            LightChangeEvent evento = (LightChangeEvent) source;
                            evento.putStreetAgent(this);
                            evento = null;
                            ProActive.turnActive(source);
                        }
                        onItinerary = false;
                    } catch (ActiveObjectCreationException e) {
                        e.printStackTrace();
                    } catch (NodeException e) {
                        e.printStackTrace();
                    }
                }
            } else {
            }
        }
    }

    public String[] getItinerary() {
        return itinerary;
    }

    public void setItinerary(String[] newItinerary) {
        sendMessageToControler("I changed my itinerary", java.awt.Color.blue);
        myStrategy = new MigrationStrategyImpl();
        itinerary = newItinerary;
        for (int i = 0; i < newItinerary.length; i++) myStrategy.add(newItinerary[i], null);
    }

    public void start() {
        onItinerary = true;
    }

    public void listo() {
        onItinerary = true;
        System.out.println("listo");
    }

    public void suspend() {
        if (!onItinerary) {
            sendMessageToControler("I'm already suspended");
        } else {
            sendMessageToControler("I suspended my itinerary", java.awt.Color.red);
            onItinerary = false;
        }
    }

    public void resume() {
        if (onItinerary) {
            sendMessageToControler("I'm already on my itinerary");
        } else {
            sendMessageToControler("I'm resuming my itinerary", new java.awt.Color(0, 150, 0));
            onItinerary = true;
        }
    }

    public String call() {
        return "[" + name + "] : I am working on node " + ProActive.getBodyOnThis().getNodeURL();
    }

    public void chainedCall() {
        if (otherStreetAgent != null) {
            sendMessageToControler("I'm calling my peer agent");
            otherStreetAgent.chainedCall();
        } else {
            sendMessageToControler("I don't have a peer agent to call");
        }
    }

    private void sendMessageToControler(String message) {
        if (controler != null) {
            controler.receiveMessage("[" + name + "] : " + message);
        }
    }

    private void sendMessageToControler(String message, java.awt.Color color) {
        if (controler != null) {
            controler.receiveMessage("[" + name + "] : " + message, color);
        }
    }

    public static void main(String[] args) {
        ProActiveConfiguration.load();
        if (!(args.length > 1)) {
            System.out.println("Usage: java org.objectweb.proactive.examples.penguin.Penguin hostname1/NodeName1 hostname2/NodeName2 ");
            System.exit(-1);
        }
        try {
            StreetControllerAgent n = (StreetControllerAgent) org.objectweb.proactive.ProActive.newActive(StreetControllerAgent.class.getName(), null);
            n.initialize(args);
            Object[] param = new Object[1];
            param[0] = n;
            org.objectweb.proactive.ProActive.newActive(AgentMessageReceiver.class.getName(), param, (org.objectweb.proactive.core.node.Node) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ponerEvento(int segment, double evTime, int typeEvent, Object objectId, double eventPosition, int desiredStatus, int lane) {
        controler.receiveMessage("New time> " + evTime + "type" + typeEvent);
        if (typeEvent == LCE) if (segment == 1) eventPosition = str_1.getSize();
        if (segment == 1) str_1.agregarEvento(evTime, typeEvent, objectId, eventPosition, desiredStatus, lane);
        if (segment == 3) str_3.agregarEvento(evTime, typeEvent, objectId, eventPosition, desiredStatus, lane);
        if (segment == 2) str_2.agregarEvento(evTime, typeEvent, objectId, eventPosition, desiredStatus, lane);
        if (segment == 4) str_4.agregarEvento(evTime, typeEvent, objectId, eventPosition, desiredStatus, lane);
        if (segment == 10) itr_1.agregarEvento(evTime, typeEvent, objectId, eventPosition, desiredStatus, lane);
    }

    /**
     * Retornar el siguiente evento a ser ejecutado
     * Requerimiento: 3.2.SCA.3.1
     * @return El pr�ximo evento a ser ejecutado, por polimorfismo en tiempo de ejecuci�n se detectar� que tipo de evento es. 
     * Los campos de siguiente,anterior, izquierda y derecha son puestos a null
     */
    public Event sacarEvento() {
        Event proximoEvento = rootControl.derecho;
        Event z = null;
        Event x = rootControl.derecho.siguiente;
        if (!x.getName().equals("SQS")) {
            z = rootControl.derecho;
            if (z.evTime() < x.evTime()) {
                while (x.evTime() > z.evTime() && z.derecho != rootControl) z = z.derecho;
            } else {
                while (x.evTime() < z.evTime() && z.izquierdo != rootControl) z = z.izquierdo;
            }
            x.eventoPotencial = true;
            x.izquierdo = z.izquierdo;
            z.izquierdo = x;
            x.izquierdo.derecho = x;
            x.derecho = z;
        }
        rootControl.setTime(proximoEvento.evTime());
        proximoEvento.anterior.setTime(proximoEvento.evTime());
        proximoEvento.izquierdo.derecho = proximoEvento.derecho;
        proximoEvento.derecho.izquierdo = proximoEvento.izquierdo;
        proximoEvento.anterior.siguiente = proximoEvento.siguiente;
        proximoEvento.siguiente.anterior = proximoEvento.anterior;
        proximoEvento.anterior = null;
        proximoEvento.siguiente = null;
        proximoEvento.eventoPotencial = false;
        proximoEvento.derecho = null;
        proximoEvento.izquierdo = null;
        return proximoEvento;
    }
}
