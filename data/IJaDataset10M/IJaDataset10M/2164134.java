package at.HexLib.library;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HexLibFocusListener implements FocusListener {

    private BasicPanel basicPanel;

    public HexLibFocusListener(BasicPanel basicPanel) {
        this.basicPanel = basicPanel;
    }

    @Override
    public void focusGained(FocusEvent e) {
        basicPanel.repaint();
        if (!basicPanel.he.isOwnFocusComponent(e.getOppositeComponent())) {
            FocusEvent event = getAdaptedEvent(e);
            for (FocusListener curFocus : basicPanel.he.getFocusListeners()) {
                curFocus.focusGained(event);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        basicPanel.repaint();
        if (!basicPanel.he.isOwnFocusComponent(e.getOppositeComponent())) {
            FocusEvent event = getAdaptedEvent(e);
            for (FocusListener curFocus : basicPanel.he.getFocusListeners()) {
                curFocus.focusLost(event);
            }
        }
    }

    private FocusEvent getAdaptedEvent(FocusEvent e) {
        Component oppositeComponent = e.getOppositeComponent();
        if (oppositeComponent != null) {
            if (oppositeComponent instanceof HexLibASCII) {
                oppositeComponent = ((HexLibASCII) oppositeComponent).he;
            } else if (oppositeComponent instanceof HexLibHEX) {
                oppositeComponent = ((HexLibHEX) oppositeComponent).he;
            } else if (oppositeComponent instanceof HeaderLenPanel) {
                oppositeComponent = ((HeaderLenPanel) oppositeComponent).he;
            } else if (oppositeComponent instanceof ColumnsLeft) {
                oppositeComponent = ((ColumnsLeft) oppositeComponent).he;
            } else if (oppositeComponent instanceof HeaderColumnPanel) {
                oppositeComponent = ((HeaderColumnPanel) oppositeComponent).he;
            } else if (oppositeComponent instanceof HeaderChangedPanel) {
                oppositeComponent = ((HeaderChangedPanel) oppositeComponent).he;
            }
        }
        return new FocusEvent(basicPanel.he, e.getID(), e.isTemporary(), oppositeComponent);
    }
}
