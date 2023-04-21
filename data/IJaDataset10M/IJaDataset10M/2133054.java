package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.freedom.bmps.Icone;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.frame.FFilho;

public class FCompICMS extends FFilho implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JPanelPad pinCab = new JPanelPad(400, 100);

    private JPanelPad pinRod = new JPanelPad(400, 120);

    private JPanelPad pnRod = new JPanelPad(JPanelPad.TP_JPANEL, new BorderLayout());

    private JButtonPad btSair = new JButtonPad(Icone.novo("btSair.gif"));

    public FCompICMS() {
        super(false);
        setTitulo("Compara ICMS");
        setAtribos(50, 50, 400, 400);
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(pinCab, BorderLayout.NORTH);
        btSair.setPreferredSize(new Dimension(100, 30));
        pnRod.setPreferredSize(new Dimension(400, 30));
        pnRod.add(pinRod, BorderLayout.NORTH);
        pnRod.add(btSair, BorderLayout.EAST);
        c.add(pnRod, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent evt) {
    }
}
