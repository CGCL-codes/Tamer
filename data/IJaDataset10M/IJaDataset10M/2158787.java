package fi.tuska.jalkametri.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;
import fi.tuska.jalkametri.IMainControl;
import fi.tuska.jalkametri.data.SingleDrink;
import fi.tuska.jalkametri.gui.command.ItemSelectedAction;

/**
 * Implements a screen for editing a drink.
 * 
 * @author Tuukka Haapasalo
 * @created 23.10.2005
 * 
 * $Id$
 */
public class DrinkEditScreen extends AScreen implements CommandListener {

    private final Form form;

    private final TextField nameField;

    private final TextField sizeNameField;

    private final TextField categoryField;

    private final ChoiceGroup existingCategories;

    private final TextField strengthField;

    private final TextField volumeField;

    private final TextField hourField;

    private final TextField minField;

    private String comment;

    private boolean askTime;

    private Object[] extraParams;

    private final ItemSelectedAction action;

    private SingleDrink originalDrink;

    private ItemStateListener formListener = new ItemStateListener() {

        public void itemStateChanged(Item item) {
            if (item == existingCategories) {
                categoryField.setString(existingCategories.getString(existingCategories.getSelectedIndex()));
            }
        }
    };

    /**
     * @param action the action will be called with the new drink information.
     * The parameters will contain 1. the time of drinking (if asked); and 2.
     * the original drink.
     */
    public DrinkEditScreen(IMainControl mainControl, IScreen parent, boolean askTime, ItemSelectedAction action) {
        super(mainControl, parent);
        this.askTime = askTime;
        this.action = action;
        this.comment = "";
        form = new Form("");
        form.addCommand(Commands.BACK);
        form.addCommand(Commands.OK);
        form.setCommandListener(this);
        nameField = new TextField("Nimi", "", 30, 0);
        sizeNameField = new TextField("Koon nimi", "", 30, 0);
        strengthField = new TextField("Vahvuus (%)", "", 8, TextField.DECIMAL);
        volumeField = new TextField("Koko (l)", "", 8, TextField.DECIMAL);
        existingCategories = new ChoiceGroup("Kategoria", ChoiceGroup.POPUP);
        categoryField = new TextField("(muokkaa)", "", 30, 0);
        form.setItemStateListener(formListener);
        if (askTime) {
            hourField = new TextField("Tunti", "", 3, TextField.NUMERIC);
            minField = new TextField("Minuutti", "", 3, TextField.NUMERIC);
        } else {
            hourField = null;
            minField = null;
        }
        form.append(volumeField);
        form.append(strengthField);
        form.append(nameField);
        form.append(sizeNameField);
        if (askTime) {
            form.append(hourField);
            form.append(minField);
        }
        form.append(existingCategories);
        form.append(categoryField);
        initializeDrink(new SingleDrink("Juoma", 8.5f, "T�r�ys", 0.35f, "Sekalaiset", "Sekalainen juoma"), "", parent);
    }

    public Displayable getDisplayable() {
        return form;
    }

    public void commandAction(Command command, Displayable d) {
        if (handleCommand(command)) {
            return;
        }
        if (command == Commands.OK) {
            SingleDrink drink = readDrink();
            if (drink != null) {
                if (askTime) {
                    Date date = readDate();
                    if (date != null) {
                        Object[] params = null;
                        if (extraParams != null) {
                            params = new Object[2 + extraParams.length];
                            params[0] = date;
                            params[1] = originalDrink;
                            System.arraycopy(extraParams, 0, params, 2, extraParams.length);
                        } else {
                            params = new Object[] { date, originalDrink };
                        }
                        action.itemSelected(drink, parent, params);
                    }
                } else {
                    Object[] params = null;
                    if (extraParams != null) {
                        params = new Object[1 + extraParams.length];
                        params[0] = originalDrink;
                        System.arraycopy(extraParams, 0, params, 1, extraParams.length);
                    } else {
                        params = new Object[] { originalDrink };
                    }
                    action.itemSelected(drink, parent, params);
                }
            }
        }
    }

    public void initializeDrink(String title, IScreen parent) {
        super.parent = parent;
        form.setTitle(title);
        originalDrink = null;
    }

    public void initializeDrink(SingleDrink drink, String title, IScreen parent) {
        initializeDrink(drink, null, title, parent, null);
    }

    public void initializeDrink(SingleDrink drink, Date time, String title, IScreen parent, Object[] extraParams) {
        super.parent = parent;
        this.extraParams = extraParams;
        originalDrink = drink;
        form.setTitle(title);
        Calendar cal = mainControl.getDateUtils().getCalendar();
        if (time != null) {
            cal.setTime(time);
        }
        volumeField.setString(String.valueOf(drink.getVolume()));
        strengthField.setString(String.valueOf(drink.getStrength()));
        nameField.setString(drink.getName());
        sizeNameField.setString(drink.getSizeName());
        categoryField.setString(drink.getCategory());
        populateCategories(drink.getCategory());
        if (askTime) {
            hourField.setString(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
            minField.setString(String.valueOf(cal.get(Calendar.MINUTE)));
        }
        this.comment = drink.getComment();
    }

    private void populateCategories(String selected) {
        Vector existing = mainControl.getDrinkLibrary().getCategories();
        existingCategories.deleteAll();
        if (selected == null) selected = "";
        existingCategories.append(selected, null);
        for (int i = 0; i < existing.size(); i++) {
            String cur = (String) existing.elementAt(i);
            if (!selected.equals(cur)) existingCategories.append(cur, null);
        }
    }

    private SingleDrink readDrink() {
        String name = nameField.getString();
        String sizeName = sizeNameField.getString();
        String category = categoryField.getString();
        try {
            float strength = Float.parseFloat(strengthField.getString());
            float volume = Float.parseFloat(volumeField.getString());
            int index = originalDrink != null ? originalDrink.getOriginatingIndex() : 0;
            return new SingleDrink(name, strength, sizeName, volume, category, comment, index);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Date readDate() {
        if (!askTime) return null;
        try {
            int hour = Integer.parseInt(hourField.getString());
            int minute = Integer.parseInt(minField.getString());
            Calendar cal = mainControl.getDateUtils().getCalendar();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            Calendar now = mainControl.getDateUtils().getCalendar();
            if (cal.after(now)) {
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
            }
            return cal.getTime();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void activate() {
    }
}
