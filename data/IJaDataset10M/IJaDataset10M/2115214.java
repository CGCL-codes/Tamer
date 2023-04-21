package poa.game.item;

import java.io.File;
import poa.game.data.DataElement;
import poa.util.io.PoAParser;
import poa.util.io.Resource;
import poa.util.Image;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.render.NiftyRenderEngine;

/**
 *  PagesOfAdventure: A top-down puzzle adventure.
 *  Copyright (C) 2010, 2011  PagesOfAdventure
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * @author Edward_Lii
 */
public class Item extends DataElement implements Resource {

    /**
	 * Boolean if the item has been loaded.
	 */
    private boolean loaded = false;

    /**
	 * The NiftyRenderEngine used to create NiftyImages.
	 */
    private NiftyRenderEngine renderEngine = null;

    /**
	 * The name of the item.
	 */
    private String name = null;

    /**
	 * The category of the item.
	 */
    private String category = null;

    /**
	 * The description of the item.
	 * If none then the name will be used.
	 */
    private String[] description = new String[4];

    /**
	 * The size of the item.
	 */
    private int size = 1;

    /**
	 * The weight of the item.
	 */
    private int weight = 1;

    /**
	 * The action this item can do.
	 */
    private String action = null;

    /**
	 * The inventory image.
	 * This image is used in the inventory.
	 */
    private Image image;

    /**
	 * Boolean if the item is prepared.
	 */
    private boolean prepared = false;

    /**
	 * Class constructor.
	 * @param name The name of the item.
	 */
    public Item(String name, NiftyRenderEngine renderEngine) {
        this.name = name;
        this.renderEngine = renderEngine;
    }

    /**
	 * @return Returns the name of the item.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the inventory image of the item.
	 */
    public NiftyImage getInventoryImage() {
        return image.getInternalNiftyImage();
    }

    /**
	 * @return Returns the description of the item.
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * @return Returns the description of the item.
	 */
    public String[] getDescription() {
        return description;
    }

    /**
	 * @return Returns the size of the item.
	 */
    public int getSize() {
        return size;
    }

    /**
	 * @return Returns the weight of the item.
	 */
    public int getWeight() {
        return weight;
    }

    /**
	 * @return Returns the action of the item.
	 */
    public String getAction() {
        return action;
    }

    @Override
    public void load() {
        PoAParser parser = new PoAParser("Data" + File.separator + "Items" + File.separator + name + File.separator + name + ".item");
        while (!parser.next()) {
            if (parser.pair()) {
                if (parser.getKey().startsWith("category")) {
                    this.category = parser.getValue();
                    continue;
                }
                if (parser.getKey().startsWith("size")) {
                    this.size = Integer.valueOf(parser.getTrimmedValue());
                    continue;
                }
                if (parser.getKey().startsWith("weight")) {
                    this.weight = Integer.valueOf(parser.getTrimmedValue());
                    continue;
                }
                if (parser.getKey().startsWith("action")) {
                    this.action = parser.getTrimmedValue();
                    continue;
                }
            }
            if (parser.block()) {
                if (parser.getBlock().startsWith("description")) {
                    int line = 0;
                    while (!parser.nextBlock()) {
                        if (line > 3) continue;
                        description[line] = parser.getLine();
                        line++;
                    }
                }
            }
        }
        image = new Image(renderEngine, "Data" + File.separator + "Items" + File.separator + name + File.separator + name + ".png");
        loaded = true;
    }

    @Override
    public void prepare() {
        image.prepare();
        prepared = true;
    }

    @Override
    public boolean loaded() {
        return loaded;
    }

    @Override
    public boolean prepared() {
        return prepared;
    }

    @Override
    public DataElement getChild(String name) {
        return null;
    }

    @Override
    public void set(String key, String value) {
    }

    @Override
    public void run(String command, String[] parameters) {
    }

    @Override
    public String get(String key) {
        return null;
    }
}
