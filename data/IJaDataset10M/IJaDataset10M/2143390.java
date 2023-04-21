package net.medienablage.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JTextField;
import net.medienablage.domain.Medium;
import net.medienablage.service.IMedienService;
import net.medienablage.service.impl.MedienServiceFactory;

public class SearchAction implements ActionListener {

    private JTextField searchField;

    private MedienTableModel medienModel;

    public SearchAction(JTextField searchField, MedienTableModel medienModel) {
        this.searchField = searchField;
        this.medienModel = medienModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMedienService service = MedienServiceFactory.getServiceInstance();
        Collection<Medium> neueMedien;
        if ("".equals(searchField.getText())) {
            neueMedien = service.getAll();
        } else {
            neueMedien = new ArrayList<Medium>(1);
            Medium found = service.findMediumByName(searchField.getText());
            if (found != null) neueMedien.add(found);
        }
        medienModel.addAll(neueMedien);
    }
}
