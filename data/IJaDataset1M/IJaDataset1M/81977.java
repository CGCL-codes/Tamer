package plugin.lsttokens.gamemode;

import java.net.URI;
import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;

/**
 * This class handles the PREVIEWDIR game mode token.
 * 
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.13.2
 */
public class PreviewDirToken implements GameModeLstToken {

    public boolean parse(GameMode gameMode, String value, URI source) {
        gameMode.setPreviewDir(value);
        return true;
    }

    /**
	 * Returns the name of the token this class handles.
	 * 
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
    public String getTokenName() {
        return "PREVIEWDIR";
    }
}
