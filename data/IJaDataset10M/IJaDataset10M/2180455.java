package saadadb.meta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import saadadb.collection.Category;
import saadadb.collection.SaadaInstance;
import saadadb.database.Database;
import saadadb.exceptions.FatalException;
import saadadb.exceptions.SaadaException;
import saadadb.generationclass.SaadaClassReloader;
import saadadb.sqltable.SQLQuery;
import saadadb.util.Messenger;

/** * @version $Id: MetaCollection.java 257 2012-02-24 16:33:22Z laurent.mistahl $

 * @author michel
 * 05/2001: add methods getUCDs
 */
public class MetaCollection extends MetaObject {

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_table = new LinkedHashMap<String, AttributeHandler>();

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_entry = new LinkedHashMap<String, AttributeHandler>();

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_image = new LinkedHashMap<String, AttributeHandler>();

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_spectrum = new LinkedHashMap<String, AttributeHandler>();

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_misc = new LinkedHashMap<String, AttributeHandler>();

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_cube = new LinkedHashMap<String, AttributeHandler>();

    private static final LinkedHashMap<String, AttributeHandler> attribute_handlers_flatfile = new LinkedHashMap<String, AttributeHandler>();

    private String description = "";

    private boolean has_flatfiles = false;

    /**
	 * @param name
	 * @throws SQLException
	 */
    public MetaCollection(String name) {
        super(name, -1);
    }

    /**
	 * @param rs
	 * @throws SQLException 
	 */
    public void update(ResultSet rs) throws SQLException {
        String name = rs.getString("name").trim();
        if (name.startsWith(this.name) == false) {
            Messenger.printMsg(Messenger.ERROR, "Fatal error when loading meta collection: <" + name + "> doesn't match <" + this.name + ">");
            System.exit(1);
        }
        this.name = name;
        this.id = rs.getInt("id");
        this.description = rs.getString("description");
    }

    /**
	 * add the attribute hanlder read in the current row of rs
	 * in the attributes of the given category
	 * @param rs
	 * @param cat
	 * @throws SQLException 
	 * @throws SQLException
	 */
    public void update(ResultSet rs, int cat) throws FatalException {
        try {
            this.id = rs.getInt("id_collect");
            LinkedHashMap<String, AttributeHandler> lah = null;
            switch(cat) {
                case Category.CUBE:
                    lah = MetaCollection.attribute_handlers_cube;
                    break;
                case Category.ENTRY:
                    lah = MetaCollection.attribute_handlers_entry;
                    break;
                case Category.TABLE:
                    lah = MetaCollection.attribute_handlers_table;
                    break;
                case Category.IMAGE:
                    lah = MetaCollection.attribute_handlers_image;
                    break;
                case Category.SPECTRUM:
                    lah = MetaCollection.attribute_handlers_spectrum;
                    break;
                case Category.MISC:
                    lah = MetaCollection.attribute_handlers_misc;
                    break;
                case Category.FLATFILE:
                    lah = MetaCollection.attribute_handlers_flatfile;
                    break;
                default:
                    FatalException.throwNewException(SaadaException.WRONG_PARAMETER, "Internal error: category<" + cat + "> unknown");
            }
            AttributeHandler ah = new AttributeHandler(rs);
            lah.put(ah.getNameattr(), ah);
            this.description = rs.getString("description");
        } catch (Exception e) {
            FatalException.throwNewException(SaadaException.DB_ERROR, e);
        }
    }

    /**
	 * Set a flag true if the collection contains flatfiles. That avoid the SWING widget to run SQL queries at any time
	 * @throws Exception  
	 */
    public void lookAtFlatfiles() throws Exception {
        SQLQuery squery = new SQLQuery();
        ResultSet rs = squery.run("select oidsaada from " + Database.getWrapper().getCollectionTableName(this.name, Category.FLATFILE) + " limit 1");
        this.has_flatfiles = false;
        while (rs.next()) {
            this.has_flatfiles = true;
            squery.close();
            return;
        }
        squery.close();
    }

    /**
	 * Make an internal join in the collection attribute table (saada_metacoll_*)
	 * to build links between attribute handlers and their associated errors
	 * @throws SQLException
	 */
    public void bindAssociatedAttributeHandler() throws FatalException {
        try {
            LinkedHashMap<String, AttributeHandler> lah = null;
            for (int cat = 1; cat < Category.NB_CAT; cat++) {
                switch(cat) {
                    case Category.CUBE:
                        lah = MetaCollection.attribute_handlers_cube;
                        break;
                    case Category.ENTRY:
                        lah = MetaCollection.attribute_handlers_entry;
                        break;
                    case Category.TABLE:
                        lah = MetaCollection.attribute_handlers_table;
                        break;
                    case Category.IMAGE:
                        lah = MetaCollection.attribute_handlers_image;
                        break;
                    case Category.SPECTRUM:
                        lah = MetaCollection.attribute_handlers_spectrum;
                        break;
                    case Category.MISC:
                        lah = MetaCollection.attribute_handlers_misc;
                        break;
                    case Category.FLATFILE:
                        lah = MetaCollection.attribute_handlers_flatfile;
                        break;
                    default:
                        FatalException.throwNewException(SaadaException.WRONG_PARAMETER, "Internal error: category<" + cat + "> unknown");
                }
                String str_cat = Category.NAMES[cat].toLowerCase();
                SQLQuery squery = new SQLQuery();
                ResultSet rs = squery.run("SELECT mc.name_attr, mc.ass_error, mc2.name_attr " + " FROM saada_metacoll_" + str_cat + " mc, saada_metacoll_" + str_cat + " mc2 " + " WHERE mc.name_coll = '" + this.name + "' " + " AND mc.ass_error IS NOT NULL  AND mc2.pk = mc.ass_error");
                while (rs.next()) {
                    AttributeHandler ah_prim = lah.get(rs.getString(1));
                    AttributeHandler ah_ass = lah.get(rs.getString(3));
                    ah_prim.setAss_error(ah_ass);
                }
                squery.close();
            }
        } catch (Exception e) {
            FatalException.throwNewException(SaadaException.DB_ERROR, e);
        }
    }

    /**
	 * @return
	 */
    @Override
    public String getName() {
        return this.name;
    }

    /**
	 * @return Returns the entry_coll_id.
	 */
    @Override
    public int getId() {
        return id;
    }

    /**
	 * @param prefix
	 * @throws FatalException 
	 */
    public void show(String prefix) throws FatalException {
        try {
            LinkedHashMap<String, AttributeHandler> lah = null;
            for (int cat = 1; cat < Category.NB_CAT; cat++) {
                String str_cat = Category.NAMES[cat];
                System.out.print(prefix + str_cat + ": ");
                switch(cat) {
                    case Category.CUBE:
                        lah = MetaCollection.attribute_handlers_cube;
                        break;
                    case Category.ENTRY:
                        lah = MetaCollection.attribute_handlers_entry;
                        break;
                    case Category.TABLE:
                        lah = MetaCollection.attribute_handlers_table;
                        break;
                    case Category.IMAGE:
                        lah = MetaCollection.attribute_handlers_image;
                        break;
                    case Category.SPECTRUM:
                        lah = MetaCollection.attribute_handlers_spectrum;
                        break;
                    case Category.MISC:
                        lah = MetaCollection.attribute_handlers_misc;
                        break;
                    case Category.FLATFILE:
                        lah = MetaCollection.attribute_handlers_flatfile;
                        break;
                    default:
                        FatalException.throwNewException(SaadaException.WRONG_PARAMETER, "Internal error: category<" + cat + "> unknown");
                }
                Iterator it = lah.keySet().iterator();
                while (it.hasNext()) {
                    System.out.print(it.next() + " ");
                }
                System.out.println("");
            }
        } catch (Exception e) {
            FatalException.throwNewException(SaadaException.DB_ERROR, e);
        }
    }

    /**
	 * @param attrName
	 * @param category
	 * @return
	 */
    public static final boolean attributeExistIn(String attrName, int category) {
        switch(category) {
            case Category.CUBE:
                return MetaCollection.getAttribute_handlers_cube().containsKey(attrName);
            case Category.ENTRY:
                return MetaCollection.getAttribute_handlers_entry().containsKey(attrName);
            case Category.FLATFILE:
                return MetaCollection.getAttribute_handlers_flatfile().containsKey(attrName);
            case Category.IMAGE:
                return MetaCollection.getAttribute_handlers_image().containsKey(attrName);
            case Category.MISC:
                return MetaCollection.getAttribute_handlers_misc().containsKey(attrName);
            case Category.SPECTRUM:
                return MetaCollection.getAttribute_handlers_spectrum().containsKey(attrName);
            case Category.TABLE:
                return MetaCollection.getAttribute_handlers_table().containsKey(attrName);
        }
        return false;
    }

    /**
	 * @param category
	 * @return
	 */
    public static final HashMap<String, AttributeHandler> getAttribute_handlers(int category) {
        switch(category) {
            case Category.CUBE:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_cube().clone();
            case Category.ENTRY:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_entry().clone();
            case Category.FLATFILE:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_flatfile().clone();
            case Category.IMAGE:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_image().clone();
            case Category.MISC:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_misc().clone();
            case Category.SPECTRUM:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_spectrum().clone();
            case Category.TABLE:
                return (HashMap<String, AttributeHandler>) MetaCollection.getAttribute_handlers_table().clone();
        }
        return null;
    }

    /**
	 * @param category
	 * @return
	 */
    public static final Set<String> getAttribute_handlers_names(int category) {
        switch(category) {
            case Category.CUBE:
                return MetaCollection.getAttribute_handlers_cube().keySet();
            case Category.ENTRY:
                return MetaCollection.getAttribute_handlers_entry().keySet();
            case Category.FLATFILE:
                return MetaCollection.getAttribute_handlers_flatfile().keySet();
            case Category.IMAGE:
                return MetaCollection.getAttribute_handlers_image().keySet();
            case Category.MISC:
                return MetaCollection.getAttribute_handlers_misc().keySet();
            case Category.SPECTRUM:
                return MetaCollection.getAttribute_handlers_spectrum().keySet();
            case Category.TABLE:
                return MetaCollection.getAttribute_handlers_table().keySet();
        }
        return null;
    }

    /**
	 * return AttributeHandlers with UCDs
	 * @param category : serached category
	 * @param queriable_only : returns only queriables AH if true
	 * @return
	 */
    public AttributeHandler[] getUCDs(int category, boolean queriable_only) {
        ArrayList<AttributeHandler> retour = new ArrayList<AttributeHandler>();
        Map<String, AttributeHandler> ahmap = getAttribute_handlers(category);
        for (AttributeHandler ah : ahmap.values()) {
            if (ah.getUcd() != null && ah.getUcd().length() > 0) {
                if ((queriable_only && ah.isQueriable()) || !queriable_only) {
                    retour.add(ah);
                }
            }
        }
        return retour.toArray(new AttributeHandler[0]);
    }

    /**
	 * Returns true if all subclasses of the category implement the DM
	 * @param vor
	 * @param category
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
    public boolean implementsDM(VOResource vor, int category) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (String cls : Database.getCachemeta().getClassesOfCollection(this.name, category)) {
            SaadaInstance si = (SaadaInstance) SaadaClassReloader.forGeneratedName(cls).newInstance();
            try {
                si.activateDataModel(vor.getJavaName());
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
	 * @param ucd
	 * @param b
	 * @return
	 */
    public AttributeHandler getUCDField(String ucd, boolean b) {
        return null;
    }

    /**
	 * @return Returns the attribute_handlers_cube.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_cube() {
        return attribute_handlers_cube;
    }

    /**
	 * @return Returns the attribute_handlers_entry.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_entry() {
        return attribute_handlers_entry;
    }

    /**
	 * @return Returns the attribute_handlers_flatfile.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_flatfile() {
        return attribute_handlers_flatfile;
    }

    /**
	 * @return Returns the attribute_handlers_image.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_image() {
        return attribute_handlers_image;
    }

    /**
	 * @return Returns the attribute_handlers_misc.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_misc() {
        return attribute_handlers_misc;
    }

    /**
	 * @return Returns the attribute_handlers_spectrum.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_spectrum() {
        return attribute_handlers_spectrum;
    }

    /**
	 * @return Returns the attribute_handlers_table.
	 */
    public static LinkedHashMap<String, AttributeHandler> getAttribute_handlers_table() {
        return attribute_handlers_table;
    }

    /**
	 * @return Returns the comment.
	 */
    public String getDescription() {
        return this.description;
    }

    public boolean hasFlatFiles() {
        return this.has_flatfiles;
    }
}
