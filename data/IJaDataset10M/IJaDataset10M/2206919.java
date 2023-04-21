package net.sf.josas.ui.swing.control;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTextField;
import net.sf.josas.model.ZipModel;
import net.sf.josas.om.ZipRecord;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 * Common controller for managing bound Zip and City text fields. When a value
 * is entered in the zip field, the city field is automatically completed with
 * the associated list of cities. When saving values from the corresponding
 * dialog, new couple of zip and city is recorded in the database if relevant.
 * 
 * @author frederic
 */
public class ZipCityController implements FocusListener {

    /** Zip text field. */
    private final JTextField zipField;

    /** City text field. */
    private final JTextField cityField;

    /** Zip model. */
    private final ZipModel model = new ZipModel();

    /**
     * Constructor.
     * 
     * @param aZipField
     *        zip field
     * @param aCityField
     *        city field
     */
    public ZipCityController(final JTextField aZipField, final JTextField aCityField) {
        zipField = aZipField;
        cityField = aCityField;
    }

    /**
     * Handler for focus gained event.
     * 
     * @param evt
     *        event to process
     */
    @Override
    public void focusGained(final FocusEvent evt) {
    }

    /**
     * Handler for focus lost event. Update accordingly the city field.
     * 
     * @param evt
     *        event to process
     */
    @Override
    public final void focusLost(final FocusEvent evt) {
        List<String> list = new ArrayList<String>();
        JComponent comp = (JComponent) evt.getSource();
        assert (comp == zipField);
        List<ZipRecord> codes = model.getList(zipField.getText());
        if (codes.size() != 0) {
            for (ZipRecord zipRecord : codes) {
                list.add(zipRecord.getCity());
            }
        }
        AutoCompleteDecorator.decorate(cityField, list, false);
        if (list.size() == 1) {
            cityField.setText(list.get(0));
        }
    }

    /**
     * Records new zip-city association if relevant. Should be invoked when a
     * record using zip and city is requested for recording or updating. The
     * following state are considered :
     * <ul>
     * <li>zip and city are not filled => no record/li>
     * <li>zip and city are filled and correspond to an existing record => no
     * record</li>
     * <li>zip and city are filled and do not correspond to an existing record
     * => create a new record</li>
     * <li>either zip or city is not filled => display an error message</li>
     * </ul>
     * Note that it is up to the calling controller function to display error
     * messages in case of incorrect values for zip and city.
     */
    public final void record() {
        final String zip = zipField.getText();
        final String city = cityField.getText();
        if (!zip.equals("     ") & !city.equals("")) {
            ZipRecord zipRec = model.getZipRecord(zip, city);
            if (zipRec == null) {
                model.save(new ZipRecord(zip, city), ZipModel.CREATION_MODE);
            }
        }
    }
}
