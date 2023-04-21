package com.xtech.xerp;

import java.awt.BorderLayout;
import javax.swing.*;
import com.xtech.common.ui.*;

/**
 * @author jscruz
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PhonesDisplay extends JPanel {

    JButton bPhoneDelete;

    JTable tPhones;

    JButton bPhoneEdit;

    javax.swing.JButton bPhoneNew;

    PhonesModel model;

    PhoneNewAction pna;

    EntityDeleteAction pda;

    PhoneEditAction pea;

    public PhonesDisplay(PhonesModel model) {
        this.model = model;
        pna = new PhoneNewAction(model);
        pda = new EntityDeleteAction(model);
        pea = new PhoneEditAction(model);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        tPhones = new JTable(model);
        model.calculateWidth();
        bPhoneEdit = new javax.swing.JButton(pea);
        bPhoneNew = new javax.swing.JButton(pna);
        bPhoneDelete = new javax.swing.JButton(pda);
        setBorder(new javax.swing.border.TitledBorder("Telefonos"));
        JScrollPane scroll = new JScrollPane(tPhones, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tPhones.setPreferredScrollableViewportSize(tPhones.getPreferredSize());
        tPhones.getSelectionModel().addListSelectionListener(model);
        add(scroll, BorderLayout.CENTER);
        JPanel botonera = new JPanel();
        botonera.setLayout(new BoxLayout(botonera, BoxLayout.Y_AXIS));
        botonera.add(bPhoneEdit);
        botonera.add(bPhoneNew);
        botonera.add(bPhoneDelete);
        add(botonera, BorderLayout.EAST);
    }

    /**
	 * @return
	 * @author jscruz
	 * @since XERP
	 */
    public PhonesModel getModel() {
        return model;
    }

    /**
	 * @param model
	 * @author jscruz
	 * @since XERP
	 */
    public void setModel(PhonesModel model) {
        this.model = model;
        tPhones.setModel(model);
    }
}
