package ecskernel.visualise.generate.forms;

import java.util.ArrayList;
import easyformlib.AFormPanel;
import easyformlib.components.AInfoEntryComponentAndLabel;
import easyformlib.components.ComboBoxAndLabel;
import easyformlib.components.TextBoxAndLabel;

public class GisiniOptionsForm extends AFormPanel {

    ArrayList<AInfoEntryComponentAndLabel> components;

    TextBoxAndLabel fireBrigs, policeForces, ambulances, fireStations, policeStations, hospitals, civilians;

    ComboBoxAndLabel firesOn;

    @Override
    protected ArrayList<AInfoEntryComponentAndLabel> getComponentsArrayList() {
        return components;
    }

    @Override
    public void initComponents() {
        components = new ArrayList<AInfoEntryComponentAndLabel>();
        fireBrigs = new TextBoxAndLabel("Fire Brigades", "0");
        policeForces = new TextBoxAndLabel("Police Forces", "0");
        ambulances = new TextBoxAndLabel("Ambulances", "6");
        fireStations = new TextBoxAndLabel("Fire Stations", "0");
        policeStations = new TextBoxAndLabel("Police Stations", "0");
        hospitals = new TextBoxAndLabel("Hospitals", "1");
        civilians = new TextBoxAndLabel("Civilians", "70");
        String[] onoff = { "On", "Off" };
        firesOn = new ComboBoxAndLabel("Fires", onoff);
        components.add(fireBrigs);
        components.add(policeForces);
        components.add(ambulances);
        components.add(fireStations);
        components.add(policeStations);
        components.add(hospitals);
        components.add(civilians);
        components.add(firesOn);
    }
}
