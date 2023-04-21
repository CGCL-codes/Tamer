package samples.swing.activesubmodel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextField;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2003/01/17 16:34:59 $
 * @created 05 September 2002
 */
public class ActivesubmodelView extends SPanel {

    /**
     * Constructor for the ActivesubmodelView object
     */
    public ActivesubmodelView() {
        setLayout(new BorderLayout(12, 12));
        SPanel namePanel = new SPanel();
        namePanel.add(new JLabel("Name: "));
        STextField personNameField = new STextField();
        personNameField.setSelector("person.name");
        namePanel.add(personNameField);
        add(namePanel, BorderLayout.NORTH);
        SPanel petPanel = new SPanel();
        petPanel.setLayout(new BorderLayout(12, 12));
        petPanel.setBorder(new TitledBorder("Pet"));
        add(petPanel, BorderLayout.CENTER);
        SPanel petNamePanel = new SPanel();
        petNamePanel.add(new JLabel("Name: "));
        STextField petNameField = new STextField();
        petNameField.setSelector("person.pet.name");
        petNamePanel.add(petNameField);
        SList toysList = new SList();
        toysList.setSelector("person.pet.toys");
        toysList.setSelectionSelector("selectedToy");
        petPanel.add(new JScrollPane(toysList), BorderLayout.CENTER);
        SPanel toyPanel = new SPanel();
        STextField newToyField = new STextField();
        newToyField.setSelector("newToy");
        toyPanel.add(newToyField);
        SButton addToyButton = new SButton();
        addToyButton.setControlID(ActivesubmodelController.ADD_TOY);
        toyPanel.add(addToyButton);
        SButton removeToyButton = new SButton();
        removeToyButton.setControlID(ActivesubmodelController.REMOVE_TOY);
        toyPanel.add(removeToyButton);
        SButton clearToysButton = new SButton();
        clearToysButton.setControlID(ActivesubmodelController.CLEAR_TOYS);
        toyPanel.add(clearToysButton);
        add(toyPanel, BorderLayout.SOUTH);
    }

    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "Activesubmodel - Edit Person";
    }

    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(ActivesubmodelController.EXIT_CONTROL_ID);
    }
}
