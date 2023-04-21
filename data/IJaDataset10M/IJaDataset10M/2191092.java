package GUI3D;

import java.awt.*;
import javax.swing.*;
import Dades.ParametresJoc;
import Servidor.BotoMenu;
import Servidor.GUI;

public class MenuParametres extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int PWIDTH = 800;

    private static final int PHEIGHT = 700;

    private BotoMenu seguent;

    private JLabel velcremar, intT, maxPersPis, velApagar, velsalvar, PProp, numA, numB, numP, numC;

    private JTextField textvelcremar, textintT, textmaxPersPis, textvelApagar, textvelsalvar, textPProp, textnumA, textnumB, textnumP, textnumC;

    public MenuParametres(GUI GUIListener) {
        Font font = new Font("Myriad Web Pro", Font.BOLD, 23);
        velcremar = new JLabel("Vel. Cremar :");
        velcremar.setForeground(new Color(255, 0, 0));
        velcremar.setFont(font);
        intT = new JLabel("T. Interval :");
        intT.setForeground(new Color(255, 0, 0));
        intT.setFont(font);
        maxPersPis = new JLabel("Max persones :");
        maxPersPis.setForeground(new Color(255, 0, 0));
        maxPersPis.setFont(font);
        velApagar = new JLabel("Vel. Apagar :");
        velApagar.setForeground(new Color(255, 0, 0));
        velApagar.setFont(font);
        velsalvar = new JLabel("Vel. Salvar :");
        velsalvar.setForeground(new Color(255, 0, 0));
        velsalvar.setFont(font);
        PProp = new JLabel("Propagaci� :");
        PProp.setForeground(new Color(255, 0, 0));
        PProp.setFont(font);
        numA = new JLabel("# Ambulancies :");
        numA.setForeground(new Color(255, 0, 0));
        numA.setFont(font);
        numB = new JLabel("# Bombers :");
        numB.setForeground(new Color(255, 0, 0));
        numB.setFont(font);
        numP = new JLabel("# Policies :");
        numP.setForeground(new Color(255, 0, 0));
        numP.setFont(font);
        numC = new JLabel("# Cotxes :");
        numC.setForeground(new Color(255, 0, 0));
        numC.setFont(font);
        textvelcremar = new JTextField(7);
        textvelcremar.setFont(font);
        textvelcremar.setText(new Integer(ParametresJoc.vel_cremar).toString());
        textintT = new JTextField(7);
        textintT.setFont(font);
        textintT.setText(new Integer(ParametresJoc.interval).toString());
        textmaxPersPis = new JTextField(7);
        textmaxPersPis.setFont(font);
        textmaxPersPis.setText(new Integer(ParametresJoc.maxPersPis).toString());
        textvelApagar = new JTextField(7);
        textvelApagar.setFont(font);
        textvelApagar.setText(new Integer(ParametresJoc.vel_apagar).toString());
        textvelsalvar = new JTextField(7);
        textvelsalvar.setFont(font);
        textvelsalvar.setText(new Integer(ParametresJoc.vel_salvar).toString());
        textPProp = new JTextField(7);
        textPProp.setFont(font);
        textPProp.setText(new Integer(ParametresJoc.vel_propagacio).toString());
        textnumA = new JTextField(7);
        textnumA.setFont(font);
        textnumA.setText(new Integer(ParametresJoc.Nambulancies).toString());
        textnumB = new JTextField(7);
        textnumB.setFont(font);
        textnumB.setText(new Integer(ParametresJoc.Nbombers).toString());
        textnumP = new JTextField(7);
        textnumP.setFont(font);
        textnumP.setText(new Integer(ParametresJoc.Npolicies).toString());
        textnumC = new JTextField(7);
        textnumC.setFont(font);
        textnumC.setText(new Integer(ParametresJoc.Ncotxes).toString());
        add(velcremar);
        add(intT);
        add(maxPersPis);
        add(velApagar);
        add(velsalvar);
        add(PProp);
        add(numA);
        add(numB);
        add(numP);
        add(numC);
        add(textvelcremar);
        add(textintT);
        add(textmaxPersPis);
        add(textvelApagar);
        add(textvelsalvar);
        add(textPProp);
        add(textnumA);
        add(textnumB);
        add(textnumP);
        add(textnumC);
        seguent = new BotoMenu("Imatges/seg1.gif", "Imatges/seg2.gif", "seguent");
        seguent.addActionListener(GUIListener);
        add(seguent);
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
    }

    public void paintComponent(Graphics g) {
        ImageIcon imatgeFons = new ImageIcon("Imatges/flatr2.jpg");
        intT.setLocation(70, 200);
        textintT.setLocation(220, 200);
        velcremar.setLocation(70, 250);
        textvelcremar.setLocation(220, 250);
        velApagar.setLocation(70, 300);
        textvelApagar.setLocation(220, 300);
        velsalvar.setLocation(70, 350);
        textvelsalvar.setLocation(220, 350);
        PProp.setLocation(70, 400);
        textPProp.setLocation(220, 400);
        maxPersPis.setLocation(400, 200);
        textmaxPersPis.setLocation(570, 200);
        numA.setLocation(400, 250);
        textnumA.setLocation(570, 250);
        numB.setLocation(400, 300);
        textnumB.setLocation(570, 300);
        numP.setLocation(400, 350);
        textnumP.setLocation(570, 350);
        numC.setLocation(400, 400);
        textnumC.setLocation(570, 400);
        seguent.setLocation(250, 550);
        setOpaque(false);
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        g.drawImage(imatgeFons.getImage(), 0, 0, PWIDTH + 2, PHEIGHT + 2, null);
    }

    public void recollidaParametres() {
        ParametresJoc.interval = new Integer(textintT.getText());
        ParametresJoc.vel_cremar = new Integer(textvelcremar.getText());
        ParametresJoc.vel_apagar = new Integer(textvelApagar.getText());
        ParametresJoc.vel_salvar = new Integer(textvelsalvar.getText());
        ParametresJoc.vel_propagacio = new Integer(textPProp.getText());
        ParametresJoc.Nambulancies = new Integer(textnumA.getText());
        ParametresJoc.Nbombers = new Integer(textnumB.getText());
        ParametresJoc.Npolicies = new Integer(textnumP.getText());
        ParametresJoc.Ncotxes = new Integer(textnumC.getText());
        System.out.println("N COTXES: " + ParametresJoc.Ncotxes);
        ParametresJoc.maxPersPis = new Integer(textmaxPersPis.getText());
    }
}
