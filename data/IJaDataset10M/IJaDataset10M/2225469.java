package componente.consulta.view;

import javax.swing.*;
import org.jgenesis.swing.JGTextField;

public class PanelEctoscopia extends JPanel {

    public JGTextField pupilas, conjuntivas, palpebras, alacrimal;

    public PanelEctoscopia() {
        this.setLayout(null);
        this.add(new JLabel("Pupilas")).setBounds(160, 10, 100, 25);
        pupilas = new JGTextField("*************************************************");
        this.add(pupilas).setBounds(160, 40, 200, 25);
        this.add(new JLabel("Conjuntivas")).setBounds(370, 10, 100, 25);
        conjuntivas = new JGTextField("*************************************************");
        this.add(conjuntivas).setBounds(370, 40, 200, 25);
        this.add(new JLabel("P�lpebras")).setBounds(160, 100, 100, 25);
        palpebras = new JGTextField("*************************************************");
        this.add(palpebras).setBounds(160, 130, 200, 25);
        this.add(new JLabel("Aparelho Lacrimal")).setBounds(370, 100, 150, 25);
        alacrimal = new JGTextField("*************************************************");
        this.add(alacrimal).setBounds(370, 130, 200, 25);
    }
}
