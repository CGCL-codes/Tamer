package org.japura.examples.checklist;

import java.awt.Component;
import java.util.List;
import javax.swing.JScrollPane;
import org.japura.examples.AbstractExample;
import org.japura.examples.Country;
import org.japura.gui.CheckList;
import org.japura.gui.model.DefaultListCheckModel;
import org.japura.gui.model.ListCheckModel;

public class Example1 extends AbstractExample {

    @Override
    protected Component buildExampleComponent() {
        List<String> countries = Country.getCountries();
        CheckList checkList = new CheckList();
        ListCheckModel model = new DefaultListCheckModel();
        for (String country : countries) {
            model.addElement(country);
        }
        model.addCheck("Anguilla");
        model.addCheck("Algeria");
        model.addLock("Algeria");
        model.addLock("Albania");
        checkList.setModel(model);
        return new JScrollPane(checkList);
    }

    public static void main(String[] args) {
        Example1 example = new Example1();
        example.runExample();
    }
}
