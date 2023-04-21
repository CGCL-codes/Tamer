package org.chargecar.lcddisplay.menu.action;

import edu.cmu.ri.createlab.display.character.CharacterDisplay;
import edu.cmu.ri.createlab.display.character.menu.RepeatingActionCharacterDisplayMenuItemAction;
import edu.cmu.ri.createlab.menu.MenuItem;
import edu.cmu.ri.createlab.menu.MenuStatusManager;
import org.apache.log4j.Logger;
import org.chargecar.honda.bms.BMSAndEnergy;
import org.chargecar.lcddisplay.BMSManager;
import org.chargecar.lcddisplay.LCD;
import org.chargecar.lcddisplay.LCDConstants;
import org.chargecar.lcddisplay.LCDProxy;
import org.chargecar.lcddisplay.helpers.GeneralHelper;
import java.util.concurrent.TimeUnit;

/**
 * @author Paul Dille (pdille@andrew.cmu.edu)
 */
public final class BatteryTestMenuItemAction extends RepeatingActionCharacterDisplayMenuItemAction {

    private static final Logger LOG = Logger.getLogger(GPSMenuItemAction.class);

    private double previousLoadCurrent;

    private double previousTotalEnergyConsumed;

    private double previousAmpHours;

    private double previousMaxVoltage;

    private double previousMinVoltage;

    private short state = 0;

    private boolean markStart = false;

    private boolean markEnd = false;

    private BMSManager bmsManager = null;

    private BMSAndEnergy bmsData = null;

    private final LCD lcd = LCDProxy.getInstance();

    private static final int BMS_ERROR_CODE = 7;

    private static final double CELL_VOLTAGE_CUTOFF = 2.01;

    private static final double TOTAL_PACK_VOLTAGE_CUTOFF = 82.5;

    public BatteryTestMenuItemAction(final MenuItem menuItem, final MenuStatusManager menuStatusManager, final CharacterDisplay characterDisplay) {
        super(menuItem, menuStatusManager, characterDisplay, 0, 1, TimeUnit.SECONDS);
    }

    protected void postDeactivate() {
        previousLoadCurrent = 0.0;
        previousTotalEnergyConsumed = 0.0;
        previousAmpHours = 0.0;
        previousMaxVoltage = 0.0;
        previousMinVoltage = 0.0;
        state = 0;
        lcd.turnOffAirConditioning();
    }

    public final void start() {
        state = 1;
        lcd.turnOnAirConditioning();
        markStart = false;
        markEnd = false;
        bmsManager = BMSManager.getInstance();
        bmsData = (bmsManager == null) ? null : bmsManager.getData();
        if (bmsManager != null || bmsData != null) {
            bmsData.getEnergyEquation().reset();
        }
    }

    @Override
    protected void performAction() {
        bmsManager = BMSManager.getInstance();
        bmsData = (bmsManager == null) ? null : bmsManager.getData();
        if (bmsManager == null || bmsData == null) {
            LOG.error("BatteryTestMenuItemAction.performAction(): bms is null");
            getCharacterDisplay().setLine(0, "No connection to BMS.");
            getCharacterDisplay().setCharacter(LCDConstants.NUM_ROWS - 1, 0, " ");
            return;
        }
        if (state == 0) {
            getCharacterDisplay().setLine(0, "BATT TEST           ");
            getCharacterDisplay().setLine(1, LCDConstants.BLANK_LINE);
            getCharacterDisplay().setCharacter(2, 1, GeneralHelper.padLeft("[*] Begin Test Now ", 1));
            getCharacterDisplay().setLine(3, LCDConstants.BLANK_LINE);
        } else if (state == 1) {
            if (!markStart) {
                lcd.writeMarkerToFile();
                markStart = true;
            }
            final double loadCurrent = bmsData.getBmsState().getLoadCurrentAmps();
            final double totalEnergyConsumed = bmsData.getEnergyEquation().getKilowattHours();
            final double ampHours = (totalEnergyConsumed * 1000) / bmsData.getBmsState().getPackTotalVoltage();
            final double maxVoltage = bmsData.getBmsState().getMaximumCellVoltage();
            final double minVoltage = bmsData.getBmsState().getMinimumCellVoltage();
            final double packTotalVoltage = bmsData.getBmsState().getPackTotalVoltage();
            final int bmsErrorCode = bmsData.getBmsState().getBMSFault().getCode();
            if (packTotalVoltage < TOTAL_PACK_VOLTAGE_CUTOFF || minVoltage <= CELL_VOLTAGE_CUTOFF || bmsErrorCode == BMS_ERROR_CODE) {
                if (!markEnd) {
                    markEnd = true;
                    lcd.writeMarkerToFile();
                }
                lcd.turnOffAirConditioning();
                getCharacterDisplay().setLine(0, "TEST DONE           ");
                getCharacterDisplay().setCharacter(0, 9, GeneralHelper.padLeft(String.valueOf(previousLoadCurrent) + " amps", LCDConstants.NUM_COLS - 9));
                getCharacterDisplay().setCharacter(1, 0, GeneralHelper.padLeft(String.valueOf(previousTotalEnergyConsumed) + " kWh", LCDConstants.NUM_COLS));
                getCharacterDisplay().setCharacter(2, 0, GeneralHelper.padLeft(String.valueOf(previousAmpHours) + " Ah", LCDConstants.NUM_COLS));
                getCharacterDisplay().setCharacter(3, 0, GeneralHelper.padLeft(String.valueOf(previousMinVoltage) + "/" + String.valueOf(previousMaxVoltage), LCDConstants.NUM_COLS));
            } else {
                previousLoadCurrent = loadCurrent;
                previousTotalEnergyConsumed = GeneralHelper.round(totalEnergyConsumed, 2);
                previousAmpHours = GeneralHelper.round(ampHours, 2);
                previousMaxVoltage = maxVoltage;
                previousMinVoltage = minVoltage;
                getCharacterDisplay().setCharacter(0, 9, GeneralHelper.padLeft(String.valueOf(GeneralHelper.round(loadCurrent, 2)) + " amps", LCDConstants.NUM_COLS - 9));
                getCharacterDisplay().setCharacter(1, 0, GeneralHelper.padLeft(String.valueOf(GeneralHelper.round(totalEnergyConsumed, 2)) + " kWh", LCDConstants.NUM_COLS));
                getCharacterDisplay().setCharacter(2, 0, GeneralHelper.padLeft(String.valueOf(GeneralHelper.round(ampHours, 2)) + " Ah", LCDConstants.NUM_COLS));
                getCharacterDisplay().setCharacter(3, 0, GeneralHelper.padLeft(String.valueOf(GeneralHelper.round(minVoltage, 2)) + "/" + String.valueOf(GeneralHelper.round(maxVoltage, 2)) + "/" + String.valueOf(GeneralHelper.round(packTotalVoltage, 2)), LCDConstants.NUM_COLS));
            }
        }
    }
}
