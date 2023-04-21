package phex.gui.common;

import javax.swing.JComboBox;
import phex.utils.Localizer;

public class BandwidthComboBox extends JComboBox {

    /** 
     * Modem = 56K Modem
     * ISDN = 64K ISDN
     * DualISDN = 128K Dual ISDN
     * DSLCable1 = 512Kbps DSL / Cable
     * DSLCable2 = 1024Kbps DSL / Cable
     * T1 = 1.5Mbps T1
     * DSLCable3 = 6Mbps DSL / Cable
     * 10LAN = 10Mbps LAN
     * T3 = 44 Mbps T3
     * 100LAN = 100Mbps LAN
     * 1000LAN = 1Gbps LAN
     */
    public static final SpeedDefinition[] SPEED_DEFINITIONS = { new SpeedDefinition("Modem", 56), new SpeedDefinition("ISDN", 64), new SpeedDefinition("DualISDN", 128), new SpeedDefinition("DSLCable1", 1024), new SpeedDefinition("T1", 1544), new SpeedDefinition("DSLCable2", 2048), new SpeedDefinition("DSLCable3", 6144), new SpeedDefinition("10LAN", 10000), new SpeedDefinition("DSLCable4", 16384), new SpeedDefinition("T3", 44736), new SpeedDefinition("100LAN", 100000), new SpeedDefinition("1000LAN", 1000000) };

    public BandwidthComboBox() {
        super(SPEED_DEFINITIONS);
    }

    public SpeedDefinition getSelectedSpeedDefinition() {
        return (SpeedDefinition) getSelectedItem();
    }

    public static class SpeedDefinition {

        private String representation;

        /**
         * The speed of the connection in kilo bits per second.
         */
        private int speedInKbps;

        /**
         * @param aRepresentation the not localized string representation
         */
        public SpeedDefinition(String aRepresentation, int aSpeedInKbps) {
            representation = Localizer.getString(aRepresentation);
            speedInKbps = aSpeedInKbps;
        }

        /**
         * Returns the speed of the connection in kilo bytes per second.
         */
        public double getSpeedInKB() {
            return speedInKbps / 8.0;
        }

        public int getSpeedInKbps() {
            return speedInKbps;
        }

        @Override
        public String toString() {
            return representation;
        }
    }
}
