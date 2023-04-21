package org.fudaa.ebli.visuallibrary.actions;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.Predicate;
import org.fudaa.ctulu.CtuluCommand;
import org.fudaa.ctulu.CtuluResource;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.visuallibrary.EbliNode;
import org.fudaa.ebli.visuallibrary.EbliScene;
import org.fudaa.ebli.visuallibrary.EbliWidget;
import org.fudaa.ebli.visuallibrary.PredicateFactory;

/**
 * classe qui permet de redimensionnner les widgets selectionnes au min ou max selon choix du type dans le constructeur.
 * 
 * @author Adrien Hadoux
 */
public class EbliWidgetActionRetaillageVertical extends EbliWidgetActionFilteredAbstract {

    public static final int RETAIILLAGE_MAX = 0;

    public static final int RETAIILLAGE_MIN = 1;

    public int typeRetaillage_;

    public EbliWidgetActionRetaillageVertical(final EbliScene _scene, final int _typeRetaillage) {
        super(_scene, EbliResource.EBLI.getString("Premier plan"), CtuluResource.CTULU.getIcon("crystal_rangericones"), "FORGROUND");
        typeRetaillage_ = _typeRetaillage;
        if (_typeRetaillage == RETAIILLAGE_MAX) {
            putValue(NAME, EbliResource.EBLI.getString("Resize Vertical Max"));
            setIcon(EbliResource.EBLI.getToolIcon("aotallest"));
        } else {
            putValue(NAME, EbliResource.EBLI.getString("Resize Vertical Min"));
            setIcon(CtuluResource.CTULU.getIcon("crystal_rangerpalettes"));
        }
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected Predicate getAcceptPredicate() {
        return PredicateFactory.geMovablePredicate();
    }

    /**
   * @return le nombre d'objet minimal pour activer la selection
   */
    public int getMinObjectSelectedToEnableAction() {
        return 2;
    }

    @Override
    protected CtuluCommand act(Set<EbliNode> filteredNode) {
        final Iterator<EbliNode> it = filteredNode.iterator();
        Rectangle tailleAresize = null;
        if (it.hasNext()) {
            tailleAresize = it.next().getWidget().getPreferredBounds();
            for (; it.hasNext(); ) {
                final EbliNode currentNode = it.next();
                if (typeRetaillage_ == RETAIILLAGE_MAX && currentNode.hasWidget()) {
                    if (tailleAresize.height < currentNode.getWidget().getPreferredBounds().height) tailleAresize = currentNode.getWidget().getPreferredBounds();
                } else if (currentNode.hasWidget()) {
                    if (tailleAresize.height > currentNode.getWidget().getPreferredBounds().height) tailleAresize = currentNode.getWidget().getPreferredBounds();
                }
            }
        }
        if (tailleAresize == null) return null;
        final List<Rectangle> oldRectangle = new ArrayList<Rectangle>();
        final List<Rectangle> newRectangle = new ArrayList<Rectangle>();
        final java.util.List<EbliWidget> listeWidget = new ArrayList<EbliWidget>();
        for (final Iterator<EbliNode> it2 = filteredNode.iterator(); it2.hasNext(); ) {
            final EbliNode currentNode = it2.next();
            if (currentNode != null && currentNode.hasWidget()) {
                listeWidget.add(currentNode.getWidget());
                oldRectangle.add(currentNode.getWidget().getPreferredBounds());
                currentNode.getWidget().setPreferredBounds(new Rectangle(new Dimension(currentNode.getWidget().getPreferredBounds().width, tailleAresize.height)));
                newRectangle.add(currentNode.getWidget().getPreferredBounds());
            }
        }
        return new CommandRetaillage(listeWidget, oldRectangle, newRectangle);
    }
}
