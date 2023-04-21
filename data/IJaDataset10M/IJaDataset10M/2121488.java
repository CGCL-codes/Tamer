package org.attacmadrid.sgss.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.attacmadrid.sgss.application.SGSSView;
import org.attacmadrid.sgss.swing.JLine;
import org.attacmadrid.sgss.view.form.PersonForm;

/**
 *
 * @author ivan
 */
public class CreatePersonView extends SGSSView {

    private static final long serialVersionUID = 1L;

    private final EntityManager entityManager;

    private JLabel title = new JLabel("Dar de alta una persona");

    private JButton createButton = new JButton("Dar de alta");

    private JButton checkDupsButton = new JButton("Comprobar duplicados");

    private PersonForm form = new PersonForm();

    public CreatePersonView() {
        entityManager = Persistence.createEntityManagerFactory("sgss").createEntityManager();
        title.setFont(new Font("dialog", Font.BOLD, 24));
        setLayout(new MigLayout());
        add(title, "wrap");
        add(new JLine(), "h 1,growx,wrap");
        add(form, "wrap");
        add(new JLine(), "h 1,growx,wrap");
        add(createButton, "split");
        add(checkDupsButton, "split");
        createButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                entityManager.getTransaction().begin();
                entityManager.persist(form.getBean());
                entityManager.getTransaction().commit();
            }
        });
    }
}
