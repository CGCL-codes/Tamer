package org.freelords.forms.info;

import java.util.EnumMap;
import java.util.EnumSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.freelords.armies.Hero;
import org.freelords.armies.Item;
import org.freelords.armies.ItemType;
import org.freelords.client.Client;
import org.freelords.forms.SmartStack.CardFactory;
import org.freelords.ui.skin.ImageButton;
import org.freelords.util.EntityDnD;
import org.freelords.xml.XMLHelper;

/** Displays a hero with his name and equipped items.
  *
  * @author Ulf Lorenz
  */
class HeroElement implements CardFactory {

    /** A client object */
    private Client client;

    /** A loader for objects */
    private XMLHelper loader;

    /** Initialises the class and its variables */
    protected HeroElement(Client client, XMLHelper loader) {
        this.client = client;
        this.loader = loader;
    }

    /** Creates a new widget that displays the hero's items and his name.
	  *
	  * @param key the hero to create a widget presentation for
	  * @param parent the parent widget of the designed new widget
	  * @throws IllegalArgumentException if key does not describe a hero or various other
	  * problems with this parameter occur (hero does not exist etc.)
	  */
    public Control createCard(Object key, Composite parent) {
        if (key == null) {
            return new Composite(parent, SWT.NONE);
        }
        if (!(key instanceof Hero)) {
            throw new IllegalArgumentException("Cannot create a hero card for non-Hero object!");
        }
        Hero hero = (Hero) key;
        Composite area = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 5;
        area.setLayout(gl);
        EnumMap<ItemType, Item> equipment = client.getGame().getOwners().get(hero.getPlayer()).getEquipment(hero);
        for (ItemType type : EnumSet.allOf(ItemType.class)) {
            ImageButton button;
            if (equipment.get(type) == null) {
                try {
                    button = new ImageButton(loader, area, "items/nobutton.png");
                } catch (Exception e) {
                    throw new IllegalArgumentException("Empty button image not found");
                }
            } else {
                try {
                    button = new ImageButton(loader, area, "items/" + equipment.get(type).getInfo().getItemClass() + ".png");
                    button.getWidget().setToolTipText(equipment.get(type).getDescription());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Image for item " + equipment.get(type).getName() + " not available.");
                }
                DragSource ds = new DragSource(button.getWidget(), DND.DROP_COPY);
                ds.setTransfer(new Transfer[] { EntityDnD.getInstance() });
                ds.addDragListener(new ItemSourceListener(equipment.get(type), button.getImage()));
            }
            GridData gd = new GridData(GridData.CENTER, GridData.END, false, false);
            gd.minimumWidth = 66;
            gd.widthHint = 66;
            gd.minimumHeight = 66;
            gd.heightHint = 66;
            button.getWidget().setLayoutData(gd);
        }
        Label l = new Label(area, SWT.RIGHT);
        l.setText(hero.getName());
        l.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, true, false));
        l.setForeground(new Color(null, 255, 255, 255));
        DropTarget dt = new DropTarget(area, DND.DROP_COPY);
        dt.setTransfer(new Transfer[] { EntityDnD.getInstance() });
        dt.addDropListener(new ItemTargetListener(hero, client));
        return area;
    }
}
