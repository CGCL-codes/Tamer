package edu.biik.visualizations.misczoomin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import edu.biik.framework.BiikVisualizationFrame;
import edu.biik.framework.Event;
import edu.biik.framework.LinkedVisualization;
import edu.biik.framework.LoadingState;
import edu.biik.framework.TimeSliceUnit;
import edu.biik.framework.VisualUnit;
import edu.biik.framework.VisualUnitType;
import edu.biik.framework.VizControlPane;
import edu.biik.visualizations.misczoomin.graphics.*;
import no.geosoft.cc.graphics.GStyle;

public class MiscZoominFrame extends BiikVisualizationFrame {

    private Map<String, ArrayList<Event>> histogramSet;

    private Map<String, HashMap<String, VisualUnit>> attackSet;

    private ArrayList<TimeSliceUnit> tsUnits;

    private HashMap<String, VisualUnit> attackVUnits;

    private HashMap<String, TimeSliceUnit> tsSet;

    private ArrayList<VisualUnit> sigSet;

    private HashMap<String, VisualUnit> destIPList;

    private ArrayList<String> attackList;

    public MiscZoominFrame(String titleWindow, Collection<LinkedVisualization> biikLinkedScreens) {
        super(titleWindow, biikLinkedScreens);
        infoPane.setLegendsPage("/help_docs/misczoomin_legends_small.html");
        infoPane.setHelpPage("/help_docs/misczoomin_help_small.html");
        VizControlPane vizControlPane = new VizControlPane();
        super.add(vizControlPane, BorderLayout.NORTH);
    }

    @Override
    public void processData() {
        showLoadingProgress(LoadingState.DATA_LOADING);
        scene.removeAll();
        GStyle sceneStyle = new GStyle();
        sceneStyle.setBackgroundColor(Color.black);
        sceneStyle.setForegroundColor(Color.black);
        scene.setStyle(sceneStyle);
        Set<Event> filteredEvents = new HashSet<Event>();
        String notMisc = "attempted-user succcessful-admin successful-user unsuccessful-user web-app-attack web-application-activity attempted-dos denial-of-service successful-dos atempted-recon successful-recon-largescale successful-recon-limited network-scan suspicious-filename-detect 	suspicious-login suspicious-call-detect	unusual-client-port-connection rpc-portmap-decode protocol-command-decode	non-standard-protocol shellcode-detect string-detect";
        for (Event e : eventList) {
            String str = e.getSignatureObject().getSignatureClass();
            if (!notMisc.contains(str)) {
                filteredEvents.add(e);
            }
        }
        histogramSet = new HashMap<String, ArrayList<Event>>();
        attackSet = new HashMap<String, HashMap<String, VisualUnit>>();
        tsSet = new HashMap<String, TimeSliceUnit>();
        sigSet = new ArrayList<VisualUnit>();
        attackVUnits = new HashMap<String, VisualUnit>();
        destIPList = new HashMap<String, VisualUnit>();
        attackList = new ArrayList<String>();
        Date startTime = ((SortedSet<Event>) eventList).first().getTimestamp();
        Date endTime = ((SortedSet<Event>) eventList).last().getTimestamp();
        int xLoc = 10;
        int width = 5;
        int yLoc = 10;
        showLoadingProgress(LoadingState.PROCESSING_VISUAL_UNITS);
        VisualUnit histo = new VisualUnit(VisualUnitType.DEST_IP_NODE);
        histo.setIdentifier("histostatus");
        StatusLabel histoS = new StatusLabel(histo, "start", xLoc, yLoc, startTime, endTime);
        scene.add(histoS);
        for (Event e : filteredEvents) {
            String sourceIP = e.getIpheader().getDestinationIP();
            String attack = e.getSignatureObject().getSignatureName();
            ArrayList<Event> ipSet = histogramSet.get(sourceIP);
            if (!attackList.contains(attack)) {
                attackList.add(attack);
            }
            if (ipSet == null) {
                ArrayList<Event> myArray = new ArrayList<Event>();
                HashMap myAttacks = new HashMap<String, VisualUnit>();
                ipSet = myArray;
                histogramSet.put(sourceIP, ipSet);
                attackSet.put(sourceIP, myAttacks);
            }
            ipSet.add(e);
        }
        showLoadingProgress(LoadingState.PREPARING_DISPLAY);
        for (String key : histogramSet.keySet()) {
            ArrayList<Event> ipSet = histogramSet.get(key);
            HashMap<String, VisualUnit> sigMap = attackSet.get(key);
            xLoc = 50;
            yLoc += 180;
            VisualUnit vUnitDbg = new VisualUnit(VisualUnitType.DEST_IP_NODE);
            vUnitDbg.setIdentifier(key);
            DestBG destBG = new DestBG(vUnitDbg, key, xLoc - 10, yLoc - 120, 270, 170);
            scene.add(destBG);
            VisualUnit vUnitD = new VisualUnit(VisualUnitType.DEST_IP_NODE);
            vUnitD.setIdentifier(key);
            vUnitD.setTooltipText("[Dest IP] " + key);
            destIPList.put(key, vUnitD);
            DestIPNode destIPNode = new DestIPNode(vUnitD, key, xLoc, yLoc - 110, 80, 15);
            long milliSecondDiff = endTime.getTime() - startTime.getTime();
            int numOfTimeSlices = 50;
            long timeIntervalPerSlice = milliSecondDiff / numOfTimeSlices;
            Date rovingDateOne = new Date(startTime.getTime());
            Date rovingDateTwo = new Date(startTime.getTime() + timeIntervalPerSlice);
            tsUnits = new ArrayList<TimeSliceUnit>();
            int numOfIntervals = 0;
            for (int i = 0; i < numOfTimeSlices; i++) {
                TimeSliceUnit tsUnit = new TimeSliceUnit(rovingDateOne, rovingDateTwo);
                tsUnit.setTooltipText("[Bar] " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(rovingDateOne) + " to " + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(rovingDateTwo));
                TimeHistogramBar tBar = new TimeHistogramBar(tsUnit, xLoc, yLoc, width, 0);
                scene.add(tBar);
                tsUnits.add(tsUnit);
                tsSet.put("key" + tsSet.size(), tsUnit);
                xLoc += width;
                rovingDateOne = new Date(rovingDateTwo.getTime());
                rovingDateTwo = new Date(rovingDateTwo.getTime() + timeIntervalPerSlice);
            }
            scene.add(destIPNode);
            int interval = 1;
            int current = 0;
            int numAttacks = attackList.size();
            int sigWidth = 200 / numAttacks - 2;
            int maxE = 0;
            for (Event e : ipSet) {
                vUnitD.addEvent(e);
                e.addVisualUnit(vUnitD);
                for (TimeSliceUnit tsUnit : tsUnits) {
                    if (tsUnit.isEventInRange(e)) {
                        TimeHistogramBar bar = ((TimeHistogramBar) tsUnit.getGraphicObject());
                        maxE = Math.max(maxE, bar.addHeight(interval));
                        e.addVisualUnit(tsUnit);
                        break;
                    }
                }
                String sigClass = e.getSignatureObject().getSignatureName();
                VisualUnit sigClassVUnit = sigMap.get(sigClass);
                if (sigClassVUnit == null) {
                    VisualUnit sigVUnit2 = new VisualUnit(VisualUnitType.SIGNATURE_NODE);
                    sigVUnit2.setIdentifier(sigClass);
                    SigNode sigNode = new SigNode(sigVUnit2, sigClass, xLoc - 250 + attackList.indexOf(sigClass) * 240 / numAttacks, yLoc - 80, 230 / numAttacks);
                    sigMap.put(sigClass, sigVUnit2);
                    sigSet.add(sigVUnit2);
                    sigClassVUnit = sigVUnit2;
                    scene.add(sigNode);
                }
                sigClassVUnit.setTooltipText("[Sig] " + sigClass);
                ((SigNode) sigClassVUnit.getGraphicObject()).addHeight();
                e.addVisualUnit(sigClassVUnit);
            }
            for (TimeSliceUnit tsUnit : tsUnits) {
                TimeHistogramBar bar = ((TimeHistogramBar) tsUnit.getGraphicObject());
                bar.adjustHeight(maxE);
            }
            desiredSize = new Dimension(550, yLoc + 150);
            scene.refresh();
        }
    }

    /**
	 * You must implement reset. Resets the visualization.
	 */
    @Override
    public void reset() {
        for (VisualUnit vUnit : tsSet.values()) vUnit.setHighlighted(false);
        for (VisualUnit vUnit : attackVUnits.values()) vUnit.setHighlighted(false);
        Iterator<VisualUnit> iter = sigSet.iterator();
        while (iter.hasNext()) iter.next().setHighlighted(false);
        for (VisualUnit vUnit : destIPList.values()) vUnit.setHighlighted(false);
    }
}
