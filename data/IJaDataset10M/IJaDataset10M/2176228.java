package com.etnetera.midlet.gps.ui;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.TextField;
import com.etnetera.midlet.gps.Context;
import com.etnetera.midlet.gps.GPSForm;
import com.etnetera.midlet.gps.SettingsModel;

public class SettingsScreen extends GPSForm {

    ChoiceGroup positionProviderGroup;

    ChoiceGroup coordsFormat;

    TextField initial_latitude;

    TextField initial_longitude;

    public SettingsScreen(Context ctx, String text, CommandListener cl) {
        super(ctx, text, cl);
        String[] positionProv = { "Internal GPS", "Simulator" };
        String[] posFormats = { "WGS84 (X.Z)", "WGS84 (X�Y.Z)", "WGS84(X�Y'Z.S)" };
        positionProviderGroup = new ChoiceGroup("Position provider", ChoiceGroup.EXCLUSIVE, positionProv, null);
        if (context.getSettings().getPositionProviderType() == SettingsModel.POSITION_PROVIDER_INTERNAL) {
            positionProviderGroup.setSelectedIndex(0, true);
        } else {
            positionProviderGroup.setSelectedIndex(1, true);
        }
        this.append(positionProviderGroup);
        this.append("Initial simulator location:");
        this.append(initial_latitude = new TextField("latitude :", String.valueOf(context.getSettings().getInitial_latitude()), 10, TextField.DECIMAL));
        this.append(initial_longitude = new TextField("longitude:", String.valueOf(context.getSettings().getInitial_longitude()), 10, TextField.DECIMAL));
        coordsFormat = new ChoiceGroup("Coordinates format", ChoiceGroup.EXCLUSIVE, posFormats, null);
        coordsFormat.setSelectedIndex(context.getSettings().getCoordsFormat(), true);
        this.append(coordsFormat);
        this.addCommand(Commands.settings_save_command);
    }

    public void save() {
        SettingsModel model = context.getSettings();
        model.setCoordsFormat(coordsFormat.getSelectedIndex());
        model.setPositionProviderType(positionProviderGroup.getSelectedIndex());
    }

    public int getPositionProvider() {
        return positionProviderGroup.getSelectedIndex();
    }

    public double getInitialLatitude() {
        return Double.parseDouble(initial_latitude.getString());
    }

    public double getInitialLongitude() {
        return Double.parseDouble(initial_longitude.getString());
    }

    public int getCoordsFormat() {
        return coordsFormat.getSelectedIndex();
    }
}
