package saadadb.admintool.tree;

import java.awt.Frame;

/**
 * @author laurentmichel
 * * @version $Id: VoUnitTree.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class VoUnitTree extends VoTree {

    String[] flat_units = { "Energy.erg", "Energy.eV", "Energy.keV", "Energy.MeV", "Energy.GeV", "Energy.TeV", "Energy.J", "Energy.ryd", "Frequency.Hz", "Frequency.KHz", "Frequency.MHz", "Frequency.GHz", "Frequency.THz", "Time.y", "Time.d", "Time.h", "Time.mn", "Time.sec", "Time.msec", "Time.nsec", "Length.kpc", "Length.pc", "Length.AU", "Length.km", "Length.m", "Length.cm", "Length.mm", "Length.um", "Length.nm", "Length.Angstroem", "Velocity.m/s", "Velocity.km/s", "Velocity.km/h", "Velocity.mas/yr", "Angle.deg", "Angle.arcmin", "Angle.arcsec", "Flux.erg/s/cm2", "Flux.Jy", "Flux.mJy", "Flux.ct/s", "Power.erg/s", "Power.W" };

    /**
	 * @param frame
	 * @param top_node
	 */
    public VoUnitTree(Frame frame) {
        super(frame, "Units (drag & drop to the meta-data panel)");
        this.flat_types = flat_units;
    }

    protected String[] getPathComponents(String string) {
        String[] unit_tokens = string.split("\\.");
        return unit_tokens;
    }

    protected void setDragFeatures() {
    }
}
