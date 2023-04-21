package org.processmining.converting;

import org.processmining.converting.*;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.mining.MiningResult;
import org.processmining.framework.models.petrinet.*;
import java.util.*;
import org.processmining.mining.petrinetmining.PetriNetResult;
import org.processmining.framework.models.pdm.*;
import org.processmining.framework.log.LogEvent;
import org.processmining.framework.ui.Message;

/**
 * <p>
 * Title: PDMtoPME
 * </p>
 * *
 * <p>
 * Description:
 * </p>
 * *
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * *
 * <p>
 * Company: TU/e and Pallas Athena
 * </p>
 * *
 * 
 * @author Johfra Kamphuis
 * @version 1.0
 */
public class PDMtoPME implements ConvertingPlugin {

    public PDMtoPME() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getName() {
        return "Product Data Model to Process Model algorithm Echo";
    }

    public String getHtmlDescription() {
        return "http://is.tm.tue.nl/staff/ivanderfeesten/ProM/documentation/PDM2PM.htm";
    }

    public MiningResult convert(ProvidedObject object) {
        PDMModel model = null;
        for (int i = 0; model == null && i < object.getObjects().length; i++) {
            if (object.getObjects()[i] instanceof PDMModel) {
                model = (PDMModel) object.getObjects()[i];
            }
        }
        if (model == null) {
            return null;
        }
        PetriNet petrinet = convert(model);
        return new PetriNetResult(petrinet);
    }

    public PetriNet convert(PDMModel model) {
        HashMap places = new HashMap();
        HashMap transitions = new HashMap();
        PetriNet result = new PetriNet();
        PDMDataElement root = model.getRootElement();
        Place ps = new Place("P_s", result);
        places.put(ps.getIdentifier(), ps);
        result.addPlace(ps);
        Place pf = new Place("P_f", result);
        places.put(pf.getIdentifier(), pf);
        result.addPlace(pf);
        Transition tinit = new Transition("T_init", result);
        transitions.put(tinit.getIdentifier(), tinit);
        result.addTransition(tinit);
        LogEvent fakeinit = new LogEvent(tinit.getIdentifier(), "complete");
        tinit.setLogEvent(fakeinit);
        PNEdge einit = new PNEdge(ps, tinit);
        result.addEdge(einit);
        Object[] dataArray = model.getDataElements().values().toArray();
        for (int i = 0; i < dataArray.length; i++) {
            PDMDataElement d = (PDMDataElement) dataArray[i];
            Place p1 = new Place("P_" + d.getID(), result);
            places.put(p1.getIdentifier(), p1);
            result.addPlace(p1);
            Transition t1 = new Transition("T_" + d.getID() + "_out", result);
            transitions.put(t1.getIdentifier(), t1);
            result.addTransition(t1);
            LogEvent fake1 = new LogEvent(t1.getIdentifier(), "complete");
            t1.setLogEvent(fake1);
            PNEdge e1 = new PNEdge(p1, t1);
            result.addEdge(e1);
        }
        HashMap ops = model.getOperations();
        Object[] opArray = ops.values().toArray();
        for (int j = 0; j < opArray.length; j++) {
            PDMOperation op = (PDMOperation) opArray[j];
            HashMap inputs = op.getInputElements();
            HashMap outputs = op.getOutputElements();
            Transition t2 = new Transition("T_" + op.getID(), result);
            transitions.put(t2.getIdentifier(), t2);
            result.addTransition(t2);
            LogEvent fake2 = new LogEvent(t2.getIdentifier(), "complete");
            t2.setLogEvent(fake2);
            Place p2;
            if (outputs.size() == 1) {
                Object[] outs = outputs.values().toArray();
                for (int k = 0; k < outs.length; k++) {
                    PDMDataElement data2 = (PDMDataElement) outs[k];
                    p2 = (Place) places.get("P_" + data2.getID());
                    PNEdge e2 = new PNEdge(t2, p2);
                    result.addEdge(e2);
                }
            } else Message.add("There is an operation with more or less than 1 output element!", Message.ERROR);
            if (inputs.isEmpty()) {
                Place p4a = new Place("P_ready.to.create_" + op.getID(), result);
                places.put(p4a.getIdentifier(), p4a);
                result.addPlace(p4a);
                PNEdge e4a = new PNEdge(p4a, t2);
                result.addEdge(e4a);
                PNEdge e4b = new PNEdge(tinit, p4a);
                result.addEdge(e4b);
            } else if (inputs.size() > 0) {
                Object[] ins = inputs.values().toArray();
                for (int k = 0; k < ins.length; k++) {
                    PDMDataElement data3 = (PDMDataElement) ins[k];
                    Place p3 = new Place("P_" + data3.getID() + "_" + op.getID(), result);
                    places.put(p3.getIdentifier(), p3);
                    result.addPlace(p3);
                    PNEdge e3a = new PNEdge(p3, t2);
                    result.addEdge(e3a);
                    Transition t3 = (Transition) transitions.get("T_" + data3.getID() + "_out");
                    PNEdge e3b = new PNEdge(t3, p3);
                    result.addEdge(e3b);
                }
            }
        }
        Transition t5 = (Transition) transitions.get("T_" + root.getID() + "_out");
        PNEdge e5 = new PNEdge(t5, pf);
        result.addEdge(e5);
        LogEvent fake5 = new LogEvent(t5.getIdentifier(), "complete");
        t5.setLogEvent(fake5);
        result = adminlayer(result, places, tinit, t5, "lazy");
        printTestOutput(result);
        return result;
    }

    public PetriNet adminlayer(PetriNet result, HashMap places, Transition start, Transition end, String WhatLayer) {
        if (WhatLayer == "lazy") {
            Place plazy = new Place("P_enforce.lazy.soundness", result);
            places.put(plazy.getIdentifier(), plazy);
            result.addPlace(plazy);
            PNEdge elazy0 = new PNEdge(start, plazy);
            result.addEdge(elazy0);
            PNEdge elazy1 = new PNEdge(plazy, end);
            result.addEdge(elazy1);
        } else {
        }
        return result;
    }

    /**
	 * This function tells the interface which results are accepted by this
	 * Plugin
	 * 
	 * @param original
	 *            The original mining result
	 * @return Whether or not this result is accepted
	 */
    public boolean accepts(ProvidedObject object) {
        for (int i = 0; i < object.getObjects().length; i++) {
            if (object.getObjects()[i] instanceof PDMModel) {
                return true;
            }
        }
        return false;
    }

    private void jbInit() throws Exception {
    }

    protected void printTestOutput(PetriNet model) {
        Message.add("<PDMtoPM Echo>", Message.TEST);
        Message.add("<Number of transitions = " + model.getTransitions().size() + " >", Message.TEST);
        Message.add("<Number of places = " + model.getPlaces().size() + " >", Message.TEST);
        Message.add("<Number of arcs = " + model.getEdges().size() + " >", Message.TEST);
        Message.add("</PDMtoPM Echo>", Message.TEST);
    }
}
