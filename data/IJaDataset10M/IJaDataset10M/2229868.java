package view;

import javax.swing.JButton;
import javax.swing.JToolBar;
import view.ActionListener;
import view.Controller;

public class ToolBar extends JToolBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Controller ctrl;

    public ToolBar(Controller controller) {
        super();
        ctrl = controller;
        setFloatable(false);
        add(getButton("Abrir...", "open"));
        add(getButton("Guardar", "save"));
        add(getButton("Generar Foto-Mosaico", "photomosaic"));
        add(getButton("Acercar", "zoomIn"));
        add(getButton("Alejar", "zoomOut"));
        add(getButton("Ver en horizontal", "viewHorizontal"));
        add(getButton("Ver en vertical", "viewVertical"));
        add(getButton("Ver en pestañas", "viewTabs"));
    }

    private JButton getButton(String label, String action) {
        JButton button = new JButton(label);
        button.setToolTipText(label);
        button.addActionListener(new ActionListener(ctrl, action));
        return button;
    }
}
