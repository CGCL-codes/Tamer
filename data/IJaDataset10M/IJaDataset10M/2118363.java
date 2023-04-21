package eu.fbk.hlt.edits.gui.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Milen Kouylekov
 */
public class TextFieldMouseListener implements MouseListener {

    public static final String PREFIX = "fielddkl:";

    private ActionListener ask;

    private String id;

    public TextFieldMouseListener(ActionListener ask, String id) {
        super();
        this.ask = ask;
        this.id = id;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2) {
            return;
        }
        ask.actionPerformed(new ActionEvent(e, 0, PREFIX + id));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
