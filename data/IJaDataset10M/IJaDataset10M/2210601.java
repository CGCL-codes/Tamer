package net.sf.layoutParser.processor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.layoutParser.to.BaseNameSpaceTO;
import net.sf.layoutParser.to.LayoutTO;
import net.sf.layoutParser.to.MaskTO;
import net.sf.layoutParser.typeHandler.TypeHandler;

/**
 * This singleton holds all instanced LayoutTO's, EntryTO's, TypeHandler's and MaskTO's.
 *
 * @author M�rio Valentim Junior
 * @since 1.0
 * @date 29/04/2008
 */
public class LayoutCatalog {

    private Map<String, LayoutTO> layouts;

    private Map<String, MaskTO> masks;

    @SuppressWarnings("unchecked")
    private Map<Class, TypeHandler> plugins;

    private Locale locale;

    private static LayoutCatalog instance = new LayoutCatalog();

    @SuppressWarnings("unchecked")
    private LayoutCatalog() {
        masks = new HashMap<String, MaskTO>();
        plugins = new HashMap<Class, TypeHandler>();
        layouts = new HashMap<String, LayoutTO>();
    }

    public static LayoutCatalog getInstance() {
        return instance;
    }

    /**
	 * Retrieve's a layout according to their full name:
	 * <ul>
	 * 	<li>namePrefix: The file name space</li>
	 * 	<li>nameSufix: The layout name</li>
	 * </ul>
	 * 
	 * @param namePrefix
	 * 		The first part of the name of the layout, which is the file nameSpace
	 * @param nameSufix
	 * 		The name of the layout
	 * @return
	 * 		The {@link LayoutTO}
	 */
    public LayoutTO getLayout(String namePrefix, String nameSufix) {
        return layouts.get(BaseNameSpaceTO.getFullName(namePrefix, nameSufix));
    }

    /**
	 * Same as {@link #getLayout(String, String)} but instead of passing both parts<br/>
	 * of the name separated, the "fullname" is passed. i.e.: 
	 * <ul>
	 * 	<li>namePrefix: "fooNameSpace"</li>
	 * 	<li>nameSufix: "barLayout"</li>
	 * 	<li>fullName: "fooNameSpace.barLayout"</li>
	 * </ul>
	 * This fullname can be assembled calling BaseNameSpaceTO#getFullName(namePrefix, nameSufix).
	 * 
	 * @param fullname
	 * 		The full name of the layout
	 * @return
	 * 		The {@link LayoutTO}
	 * @see 
	 * 		BaseNameSpaceTO#getFullName(namePrefix, nameSufix)
	 */
    public LayoutTO getLayout(String fullname) {
        return layouts.get(fullname);
    }

    /**
	 * Retrieve's a mask according to their full name:
	 * <ul>
	 * 	<li>namePrefix: The file name space</li>
	 * 	<li>nameSufix: The mask name</li>
	 * </ul>
	 * 
	 * The namePrefix for masks (and only masks) can be null when the mask is defined globally (for all the layout files)<br/>
	 * in the configuration file.
	 * 
	 * Because of this special rule, this method first searches for the mask with the full name, and in case a name prefix was
	 * informed and no mask is returned then the method will search for the mask globally which means that will search only with
	 * the name sufix.
	 * 
	 * @param namePrefix
	 * 		The first part of the name of the mask, which is the file nameSpace
	 * @param nameSufix
	 * 		The name of the layout
	 * @return
	 * 		The {@link MaskTO}
	 */
    public MaskTO getMask(String namePrefix, String nameSufix) {
        MaskTO maskTO = masks.get(BaseNameSpaceTO.getFullName(namePrefix, nameSufix));
        if (maskTO == null && namePrefix != null) {
            maskTO = masks.get(BaseNameSpaceTO.getFullName(null, nameSufix));
        }
        return maskTO;
    }

    /**
	 * 
	 * 
	 * @param name
	 * 		The type of class which the type handler should handle
	 * @return
	 * 		The TypeHandler for the class
	 */
    @SuppressWarnings("unchecked")
    public TypeHandler getPlugin(Class name) {
        return plugins.get(name);
    }

    /**
	 * Recupera o 
	 * 
	 * @return
	 *		
	 */
    public Map<String, LayoutTO> getLayouts() {
        return layouts;
    }

    /**
	 * Recupera o 
	 * 
	 * @return
	 *		
	 */
    public Map<String, MaskTO> getMasks() {
        return masks;
    }

    /**
	 * Recupera o 
	 * 
	 * @return
	 *		
	 */
    @SuppressWarnings("unchecked")
    public Map<Class, TypeHandler> getPlugins() {
        return plugins;
    }

    @SuppressWarnings("unchecked")
    public void addPlugin(Class type, TypeHandler plugin) {
        plugins.put(type, plugin);
    }

    @SuppressWarnings("unchecked")
    public void addAllPlugins(Map<Class, TypeHandler> in) {
        plugins.putAll(in);
    }

    public void addMask(MaskTO mask) {
        masks.put(mask.getFullName(), mask);
    }

    public void addAllMasks(Map<String, MaskTO> in) {
        masks.putAll(in);
    }

    public void addLayout(LayoutTO layout) {
        layouts.put(layout.getFullName(), layout);
    }

    public void addAllLayouts(Map<String, LayoutTO> in) {
        layouts.putAll(in);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setLocale(String locale) {
        this.locale = new Locale(locale);
    }

    public Locale getLocale() {
        return locale;
    }
}
