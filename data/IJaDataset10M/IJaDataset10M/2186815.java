package org.fudaa.ebli.visuallibrary.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import com.memoire.bu.BuResource;
import org.fudaa.ctulu.gui.CtuluHtmlEditorPanel;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliNodeDefault;
import org.fudaa.ebli.visuallibrary.EbliScene;
import org.fudaa.ebli.visuallibrary.creator.EbliWidgetCreatorTextEditor;

/**
 * Classe qui genere une widget editeur de texte. TODO a enlever
 * 
 * @author Adrien Hadoux
 */
@SuppressWarnings("serial")
public class EbliWidgetActiontextEditor extends EbliWidgetActionAbstract {

    public EbliWidgetActiontextEditor(final EbliScene _widget) {
        super(_widget, EbliLib.getS("Editeur de texte"), BuResource.BU.getIcon("crystal_editer"), "INSERTIONTEXTE");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final EbliNode nodeText = new EbliNodeDefault();
        nodeText.setTitle("Texte");
        final CtuluHtmlEditorPanel editor = new CtuluHtmlEditorPanel("<html><body><font size=4>" + EbliLib.getS("Texte") + "</font></body></html>");
        EbliWidgetCreatorTextEditor creator = new EbliWidgetCreatorTextEditor(editor);
        nodeText.setCreator(creator);
        creator.setPreferredSize(new Dimension(200, 200));
        creator.setPreferredLocation(new Point(270, 225));
        scene_.addNode(nodeText);
        scene_.refresh();
    }
}
