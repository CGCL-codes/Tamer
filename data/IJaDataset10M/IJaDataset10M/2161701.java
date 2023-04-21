package org.fudaa.fudaa.tr.common;

import org.fudaa.ctulu.CtuluHtmlWriter;
import org.fudaa.ctulu.gui.CtuluHelpComponent;
import org.fudaa.ebli.courbe.EGFillePanel;
import org.fudaa.ebli.courbe.EGGraphe;
import org.fudaa.ebli.courbe.EGTableGraphePanel;
import org.fudaa.fudaa.commun.courbe.FudaaGrapheSimpleTimeFille;
import org.fudaa.fudaa.commun.impl.FudaaCommonImplementation;
import com.memoire.bu.BuInformationsDocument;

/**
 * @author Fred Deniger
 * @version $Id: TrGrapheSimpleTimeFille.java,v 1.10 2007-01-19 13:14:24 deniger Exp $
 */
public class TrGrapheSimpleTimeFille extends FudaaGrapheSimpleTimeFille implements CtuluHelpComponent {

    public TrGrapheSimpleTimeFille(final EGGraphe _g, final String _titre, final FudaaCommonImplementation _appli) {
        this(new EGFillePanel(_g), _titre, _appli, null, new EGTableGraphePanel());
    }

    public TrGrapheSimpleTimeFille(final EGGraphe _g, final String _titre, final FudaaCommonImplementation _appli, final BuInformationsDocument _id, final EGTableGraphePanel _t) {
        this(new EGFillePanel(_g), _titre, _appli, _id, _t);
    }

    public TrGrapheSimpleTimeFille(final EGFillePanel _g, final String _titre, final FudaaCommonImplementation _appli, final BuInformationsDocument _id, final EGTableGraphePanel _t) {
        super(_g, _titre, _appli, _id, _t);
    }

    public String getShortHtmlHelp() {
        final CtuluHtmlWriter buf = new CtuluHtmlWriter();
        buf.h2Center(getTitle());
        buf.close(buf.para(), TrResource.getS("Affiche des �volutions temporelles"));
        buf.h3(TrResource.getS("Documents associ�s"));
        buf.addTag("lu");
        buf.close(buf.addTag("li"), impl_.buildLink(TrResource.getS("Description du composant d'affichage des courbes"), "common-curves"));
        return buf.toString();
    }
}
