package org.gamegineer.table.internal.ui.dialogs.selectremoteplayer;

import net.jcip.annotations.ThreadSafe;
import org.eclipse.osgi.util.NLS;

/**
 * A utility class to manage localized messages for the package.
 */
@ThreadSafe
final class NlsMessages extends NLS {

    /** A remote player is not selected. */
    public static String Model_remotePlayer_notSelected;

    /** The dialog banner title. */
    public static String SelectRemotePlayerDialog_bannerTitle;

    /** The dialog description. */
    public static String SelectRemotePlayerDialog_description;

    /** The remote players label mnemonic. */
    public static String SelectRemotePlayerDialog_remotePlayersLabel_mnemonic;

    /** The remote players label text. */
    public static String SelectRemotePlayerDialog_remotePlayersLabel_text;

    /** The dialog title. */
    public static String SelectRemotePlayerDialog_title;

    /**
     * Initializes the {@code NlsMessages} class.
     */
    static {
        NLS.initializeMessages(NlsMessages.class.getName(), NlsMessages.class);
    }

    /**
     * Initializes a new instance of the {@code NlsMessages} class.
     */
    private NlsMessages() {
    }
}
