package net.sf.freesimrc.apps.flightstrips;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import net.sf.freesimrc.vatsim.Aircraft;
import net.sf.freesimrc.vatsim.AircraftListener;
import net.sf.freesimrc.vatsim.AircraftManager;
import net.sf.freesimrc.vatsim.Flightplan;

public class StripDraw extends JPanel implements AircraftListener {

    private static final long serialVersionUID = 1L;

    private Flightplan plan;

    private Aircraft aircraft;

    private String station;

    private String squawk;

    public StripDraw(Aircraft aircraft, String station, AircraftManager manager) {
        super();
        setSize(550, 150);
        this.plan = aircraft.getFlightplan();
        this.aircraft = aircraft;
        this.station = station;
        this.squawk = Integer.toString(this.aircraft.getLastEcho().getSquawk());
        manager.addAircraftListener(this);
    }

    @Override
    public void paint(Graphics graph) {
        Graphics2D comp = (Graphics2D) graph;
        if (plan.getOrigin().equals(station)) {
            comp.setColor(Color.GRAY);
        } else {
            comp.setColor(Color.DARK_GRAY);
        }
        Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 530, 90);
        comp.fill(rect);
        comp.setColor(Color.BLACK);
        comp.draw3DRect(0, 0, 530, 90, true);
        comp.drawLine(30, 0, 30, 90);
        comp.drawLine(0, 30, 190, 30);
        comp.drawLine(65, 0, 65, 90);
        comp.drawLine(0, 60, 65, 60);
        comp.drawLine(160, 60, 530, 60);
        comp.drawLine(160, 0, 160, 90);
        comp.drawLine(190, 0, 190, 90);
        comp.drawString(plan.getDestination(), 5, 20);
        if (plan.isIfr()) {
            comp.drawString("I", 15, 50);
        } else {
            comp.drawString("V", 15, 50);
        }
        comp.drawString(squawk, 5, 80);
        comp.drawString(Integer.toString(plan.getCruiseAltitude()), 35, 20);
        comp.drawString(plan.getOrigin(), 35, 50);
        comp.drawString(plan.getAlternate(), 35, 80);
        comp.drawString(plan.getAircraftType(), 100, 20);
        comp.drawString(plan.getComments(), 195, 80);
        comp.drawString(Integer.toString(plan.getCruiseSpeed()), 165, 20);
        comp.drawString(plan.getRoute(), 195, 20);
        Font f = new Font("Helvetica", Font.BOLD, 16);
        comp.setFont(f);
        comp.drawString(aircraft.getCallsign(), 75, 70);
    }

    public void aircraftAdded(Aircraft aircraft) {
    }

    public void aircraftRemoved(Aircraft aircraft) {
    }

    public void aircraftUpdated(Aircraft aircraft) {
        if (aircraft.getCallsign().equals(this.aircraft.getCallsign())) {
            squawk = Integer.toString(aircraft.getLastEcho().getSquawk());
            repaint();
        }
    }

    public void extraRemoved(Aircraft aircraft, String name) {
    }

    public void extraSet(Aircraft aircraft, String name) {
    }

    public void flightplanUpdated(Aircraft aircraft) {
        if (aircraft.getCallsign().equals(this.aircraft.getCallsign())) {
            this.plan = aircraft.getFlightplan();
            repaint();
        }
    }
}
