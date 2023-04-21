package org.fudaa.fudaa.modeleur.resource;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import com.memoire.bu.BuResource;
import com.memoire.fu.FuLib;
import org.fudaa.fudaa.ressource.FudaaResource;

/**
 * Des ressources pour Fudaa-Modeleur, en particulier la traduction de langage.
 * @version $Id: MdlResource.java 6221 2011-04-20 14:28:21Z bmarchan $
 * @author Bertrand Marchand
 */
public final class MdlResource extends FudaaResource {

    public static final MdlResource MDL = new MdlResource(FudaaResource.FUDAA);

    private MdlResource(final BuResource _b) {
        super(_b);
    }

    /**
   * Traduit et retourne la chaine traduite, avec ou sans valeurs � ins�rer.
   *
   * @param _s La chaine � traduire.
   * @param _vals Les valeurs, de n'importe quelle type.
   * @return La chaine traduite.
   */
    public static String getS(String _s, Object... _vals) {
        String r = MDL.getString(_s);
        if (r == null) {
            return r;
        }
        for (int i = 0; i < _vals.length; i++) {
            r = FuLib.replace(r, "{" + i + "}", _vals[i].toString());
        }
        return r;
    }

    /** Couleur pour un message d'aide dans les dialogues */
    public static final Color HELP_FORGROUND_COLOR = new Color(0, 0, 180);

    /** Font pour un message d'aide dans les dialogues */
    public static final Font HELP_FONT = new JLabel().getFont().deriveFont(Font.ITALIC | Font.BOLD);
}
