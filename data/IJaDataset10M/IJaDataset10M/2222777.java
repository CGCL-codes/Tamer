package brickcad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

public abstract class View extends JPanel implements Observer, ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected CommandProcessor processor;

    protected Brick theModel;

    @Override
    public abstract void update(Observable arg0, Object arg1);

    @Override
    public abstract void actionPerformed(ActionEvent arg0);
}
