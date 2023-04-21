package fr.vtt.gattieres.gcs.gui.common;

import java.awt.*;
import javax.swing.*;

/**
 *  JPanel impl�mentant Scrollable.<br/>
 *  Le panel n'est jamais plus petit que sa taille minimale (<code>minimumSize
 *</code>), et peut �tre configur� pour suivre en largeur et/ou en hauteur le
 *  viewport.
 *
 * @author     ESR
 * @created    4 juin 2002
 */
public class JScrollablePanel extends JPanel implements Scrollable {

    /**
     *  Le panel doit-il suivre en largeur le viewport ?
     */
    protected boolean tracksViewportWidth;

    /**
     *  Le panel doit-il suivre en hauteur le viewport ?
     */
    protected boolean tracksViewportHeight;

    /**
     *  Constructeur.<br/>
     *  Cr�� un nouveau panel avec un buffer double et un FlowLayoutManager.
     *
     * @see    javax.swing.JPanel#JPanel()
     */
    public JScrollablePanel() {
        super();
        setTracksViewportWidth(true);
        setTracksViewportHeight(true);
    }

    /**
     *  Constructeur.<br/>
     *  Cr�� un nouveau Panel avec un FlowLayoutManager, et la strat�gie de
     *  bufferisation sp�cifi�e.
     *
     * @param  isDoubleBuffered  Si vrai, utilise un double buffer
     * @see                      javax.swing.JPanel#JPanel(boolean)
     */
    public JScrollablePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setTracksViewportWidth(true);
        setTracksViewportHeight(true);
    }

    /**
     *  Constructeur.<br/>
     *  Cr�� un nouveau panel utilisant le Layout Manager sp�cifi�.
     *
     * @param  layout  Le Layout Manager a utiliser
     * @see            javax.swing.JPanel#JPanel(java.awt.LayoutManager)
     */
    public JScrollablePanel(LayoutManager layout) {
        super(layout);
        setTracksViewportWidth(true);
        setTracksViewportHeight(true);
    }

    /**
     *  Constructeur.<br/>
     *  Cr�� un nouveau panel utilisant le Layout Manager et la strat�gie de
     *  bufferisation sp�cifi�s.
     *
     * @param  layout            Le Layout Manager a utiliser
     * @param  isDoubleBuffered  Si vrai, utilise un double buffer
     * @see                      javax.swing.JPanel#JPanel(java.awt.LayoutManager,
     *      boolean)
     */
    public JScrollablePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setTracksViewportWidth(true);
        setTracksViewportHeight(true);
    }

    /**
     *  Configure le panel pour suivre ou non le viewport en largeur.
     *
     * @param  tracks  Le panel doit-il suivre le viewport en largeur ?
     * @see            javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    public void setTracksViewportWidth(boolean tracks) {
        tracksViewportWidth = tracks;
        invalidate();
    }

    /**
     *  Retourne vrai si le panel doit suivre le viewport en largeur.
     *
     * @return    Vrai si le panel doit suivre le viewport en largeur
     * @see       javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getTracksViewportWidth() {
        return tracksViewportWidth;
    }

    /**
     *  Configure le panel pour suivre ou non le viewport en hauteur.
     *
     * @param  tracks  Le panel doit-il suivre le viewport en hauteur ?
     * @see            javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    public void setTracksViewportHeight(boolean tracks) {
        tracksViewportHeight = tracks;
        invalidate();
    }

    /**
     *  Retourne vrai si le panel doit suivre le viewport en hauteur.
     *
     * @return    Vrai si le panel doit suivre le viewport en hauteur
     * @see       javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getTracksViewportHeight() {
        return tracksViewportHeight;
    }

    /**
     *  Retourne la taille pr�f�r�e du viewport pour cette vue.
     *
     * @return    La taille pr�f�r�e du viewport
     * @see       javax.swing.Scrollable#getPreferredScrollableViewportSize()
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /**
     *  Retourne vrai si le viewport doit forcer la largeur du panel.<br/>
     *  Retourne vrai si la taille du viewport est sup�rieure � la taille
     *  minimale du panel, et si <code>tracksViewportWidth</code> est vrai.
     *
     * @return    Vrai si le viewport doit forcer la taille du panel
     * @see       javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getScrollableTracksViewportWidth() {
        try {
            JViewport parent = (JViewport) getParent();
            int width = parent.getExtentSize().width;
            if (width < getMinimumSize().width) {
                return false;
            }
            return getTracksViewportWidth();
        } catch (ClassCastException classex) {
            return true;
        }
    }

    /**
     *  Retourne vrai si le viewport doit forcer la hauteur du panel.<br/>
     *  Retourne vrai si la taille du viewport est sup�rieure � la taille
     *  minimale du panel, et si <code>tracksViewportHeight</code> est vrai.
     *
     * @return    Vrai si le viewport doit forcer la taille du panel
     * @see       javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight() {
        try {
            JViewport parent = (JViewport) getParent();
            int height = parent.getExtentSize().height;
            if (height < getMinimumSize().height) {
                return false;
            }
            return getTracksViewportHeight();
        } catch (ClassCastException classex) {
            return true;
        }
    }

    /**
     *  Retourne le d�placement pour un scroll de type block.<br/>
     *  Retourne la taille de la partie visible (hauteur ou largeur suivant le
     *  cas).
     *
     * @param  visibleRect  La partie visible du viewport
     * @param  orientation  L'orientation du d�placement
     *      (SwingConstants.HORIZONTAL ou SwingConstants.VERTICAL)
     * @param  direction    La direction (< 0 pour les d�placements haut/gauche,
     *      et > 0 pour les d�placements bas/droite)
     * @return              La valeur du d�placement
     * @see                 javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle,
     *      int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        int amount;
        int maxAmount;
        if (orientation == SwingConstants.HORIZONTAL) {
            amount = visibleRect.width;
            if (direction > 0) {
                maxAmount = getSize().width - visibleRect.x - visibleRect.width;
            } else {
                maxAmount = visibleRect.x;
            }
        } else {
            amount = visibleRect.height;
            if (direction > 0) {
                maxAmount = getSize().height - visibleRect.y - visibleRect.height;
            } else {
                maxAmount = visibleRect.y;
            }
        }
        return Math.min(amount, maxAmount);
    }

    /**
     *  Retourne le d�placement pour un scroll de type unitaire.<br/>
     *  Retourne la taille de la partie visible (hauteur ou largeur suivant le
     *  cas) divis�e par 10.
     *
     * @param  visibleRect  La partie visible du viewport
     * @param  orientation  L'orientation du d�placement
     *      (SwingConstants.HORIZONTAL ou SwingConstants.VERTICAL)
     * @param  direction    La direction (< 0 pour les d�placements haut/gauche,
     *      et > 0 pour les d�placements bas/droite)
     * @return              La valeur du d�placement
     * @see                 javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle,
     *      int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int amount;
        int maxAmount;
        if (orientation == SwingConstants.HORIZONTAL) {
            amount = visibleRect.width / 10;
            if (direction > 0) {
                maxAmount = getSize().width - visibleRect.x - visibleRect.width;
            } else {
                maxAmount = visibleRect.x;
            }
        } else {
            amount = visibleRect.height / 10;
            if (direction > 0) {
                maxAmount = getSize().height - visibleRect.y - visibleRect.height;
            } else {
                maxAmount = visibleRect.y;
            }
        }
        return Math.min(amount, maxAmount);
    }
}
