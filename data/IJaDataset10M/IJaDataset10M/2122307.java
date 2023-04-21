package com.bluebrim.gui.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.bluebrim.base.client.CoScrolledHorizontalFlowPanelLayout;
import com.bluebrim.swing.client.CoPanel;

/**
 * En abstrakt subklass till CoPanel som implementerar scrollning av en panel vars storlek
 * kan variera. En CoAbstractScrolledPanel m�ste ha en layout-manager som subklassats fr�n
 * CoAbstractScrolledPanelLayout.
 * N�r barn ska l�ggas till eller tas bort s� ska den container som returneras av
 * metoden getContentPane anv�ndas.
*/
public abstract class CoAbstractScrolledPanel extends CoPanel implements ChangeListener, ContainerListener, ComponentListener {

    protected JScrollBar m_horizontalScrollBar;

    protected JScrollBar m_verticalScrollBar;

    protected CoPanel m_view;

    protected int m_horizontalScrollValue;

    protected int m_verticalScrollValue;

    /**
 * Konstruktor
 * @param isDoubleBuffered @see CoPanel
 * @param layout @see CoPanel
 */
    public CoAbstractScrolledPanel(CoAbstractScrolledPanelLayout layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setBorder(BorderFactory.createLoweredBevelBorder());
        m_horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        m_verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        add(m_horizontalScrollBar);
        add(m_verticalScrollBar);
        m_horizontalScrollBar.getModel().addChangeListener(this);
        m_verticalScrollBar.getModel().addChangeListener(this);
        m_horizontalScrollValue = m_horizontalScrollBar.getValue();
        m_verticalScrollValue = m_verticalScrollBar.getValue();
        m_view = new CoPanel((LayoutManager2) null);
        m_view.setDoubleBuffered(isDoubleBuffered);
        add(m_view);
        m_view.addContainerListener(this);
    }

    /**
 * Implementation av ContainerListener-gr�nssnittet.
 * @param e inneh�ller bland annat det nya barnet
*/
    public void componentAdded(ContainerEvent e) {
        e.getChild().addComponentListener(this);
        if (getParent() != null) {
            doLayout();
        }
    }

    /**
 * Implementation av ComponentListener-gr�nssnittet.
 * @param e 
*/
    public void componentHidden(ComponentEvent e) {
        if (getParent() != null) repaint();
    }

    public void componentMoved(ComponentEvent e) {
    }

    /**
 * Implementation av ContainerListener-gr�nssnittet.
 * @param e inneh�ller bland annat det barn om tagits bort. 
*/
    public void componentRemoved(ContainerEvent e) {
        e.getChild().removeComponentListener(this);
        if (getParent() != null) {
            doLayout();
            repaint();
        }
    }

    /**
 * Implementation av ComponentListener-gr�nssnittet.
 * @param e 
*/
    public void componentResized(ComponentEvent e) {
        if (getParent() != null) {
            doLayout();
            repaint();
        }
    }

    /**
 * Implementation av ComponentListener-gr�nssnittet.
 * @param e 
*/
    public void componentShown(ComponentEvent e) {
        if (getParent() != null) repaint();
    }

    /**
 * Access-metod view-containern.
 * @return view-containern
*/
    public CoPanel getContentPane() {
        return m_view;
    }

    /**
 * Access-metod view-containerns storlekskrav.
 * Obs: ej n�dv�ndigtvis samma som getContentPane().getSize().
 * @return view-containerns  storlekskrav
*/
    public Dimension getContentPaneSize() {
        return ((CoAbstractScrolledPanelLayout) getLayout()).getViewSize();
    }

    /**
 * Access-metod till layout-managerns horisontella mellanrum.
 * @return layout-managerns horisontella mellanrum.
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public int getHorizontalSpacing() {
        return ((CoAbstractScrolledPanelLayout) getLayout()).getHorizontalSpacing();
    }

    /**
 * Access-metod till layout-managerns marginaler.
 * @return layout-managerns marginaler
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public Insets getInternalInsets() {
        return ((CoAbstractScrolledPanelLayout) getLayout()).getInternalInsets();
    }

    /**
 * Access-metod till layout-managerns vertikala mellanrum.
 * @return layout-managerns vertikala mellanrum.
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public int getVerticalSpacing() {
        return ((CoAbstractScrolledPanelLayout) getLayout()).getVerticalSpacing();
    }

    public void setBackground(Color bkgColor) {
        super.setBackground(bkgColor);
        if (m_view != null) m_view.setBackground(bkgColor);
    }

    public void setDoubleBuffered(boolean aFlag) {
        super.setDoubleBuffered(aFlag);
        if (m_view != null) m_view.setDoubleBuffered(aFlag);
    }

    /**
 * Set-metod f�r layout-managerns horisontella mellanrum.
 * @param s det nya mellanrumsv�rdet
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public int setHorizontalSpacing(int s) {
        return ((CoAbstractScrolledPanelLayout) getLayout()).setHorizontalSpacing(s);
    }

    /**
 * Set-metod f�r layout-managerns marginaler.
 * @param i de nya marginalerna
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public Insets setInternalInsets(Insets i) {
        return ((CoAbstractScrolledPanelLayout) getLayout()).setInternalInsets(i);
    }

    public void setOpaque(boolean isOpaque) {
        super.setOpaque(isOpaque);
        if (m_view != null) m_view.setOpaque(isOpaque);
    }

    /**
 * Set-metod f�r layout-managerns mellanrum.
 * @param s det nya mellanrumsv�rdet
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public void setSpacing(int hs, int vs) {
        ((CoAbstractScrolledPanelLayout) getLayout()).setSpacing(hs, vs);
    }

    /**
 * Set-metod f�r layout-managerns vertikala mmellanrum.
 * @param s det nya mellanrumsv�rdet
 * @see CoScrolledHorizontalFlowPanelLayout
*/
    public int setVerticalSpacing(int s) {
        return ((CoAbstractScrolledPanelLayout) getLayout()).setVerticalSpacing(s);
    }

    /**
 * Implementation av ChangeListener-gr�nssnittet.
 * Anropas n�r anv�ndaren drar i en scrollbar.
 * @param e information om scrollningsh�ndelsen.
*/
    public void stateChanged(ChangeEvent e) {
        int horizontalScrollValue = m_horizontalScrollBar.getValue();
        int verticalScrollValue = m_verticalScrollBar.getValue();
        int dx = horizontalScrollValue - m_horizontalScrollValue;
        int dy = verticalScrollValue - m_verticalScrollValue;
        if ((dx != 0) && (!m_horizontalScrollBar.isVisible())) {
            dx = 0;
            m_horizontalScrollValue = 0;
        }
        if ((dy != 0) && (!m_verticalScrollBar.isVisible())) {
            dy = 0;
            m_verticalScrollValue = 0;
        }
        if ((dx == 0) && (dy == 0)) return;
        Component[] children = m_view.getComponents();
        for (int i = 0; i < children.length; i++) {
            Point p = children[i].getLocation();
            p.x = p.x - dx;
            p.y = p.y - dy;
            children[i].setLocation(p);
        }
        m_horizontalScrollValue = horizontalScrollValue;
        m_verticalScrollValue = verticalScrollValue;
    }
}
