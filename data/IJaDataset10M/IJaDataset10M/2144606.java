package sMySQLappTemplate.GUI;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class MyMenuBar extends JMenuBar {

    public MyMenuBar() {
        super();
        JMenu mFile = new JMenu("Archivo");
        this.add(mFile);
        JMenu mFiters = new JMenu("Filtros");
        this.add(mFiters);
        JMenu mActions = new JMenu("Acciones");
        this.add(mActions);
        JMenu mView = new JMenu("Ver");
        this.add(mView);
        JMenu mHelp = new JMenu("Ayuda");
        this.add(mHelp);
    }
}
