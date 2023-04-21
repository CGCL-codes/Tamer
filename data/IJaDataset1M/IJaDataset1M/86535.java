package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;

/**
 * Standalone look-and-feel that uses the <code>Mist Silver</code> skin from
 * {@link MistSilverSkin}. You can set this look-and-feel by:
 * <ul>
 * <li>
 * -Dswing.defaultlaf=org.pushingpixels.substance.api.skin.
 * SubstanceMistSilverLookAndFeel</li>
 * <li>UIManager.setLookAndFeel(
 * "org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel" );</li>
 * <li>UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());</li>
 * </ul>
 * 
 * @author Kirill Grouchnikov
 * @since version 4.0
 */
public class SubstanceMistSilverLookAndFeel extends SubstanceLookAndFeel {

    /**
	 * Creates a new <code>Mist Silver</code> look-and-feel.
	 */
    public SubstanceMistSilverLookAndFeel() {
        super(new MistSilverSkin());
    }
}
