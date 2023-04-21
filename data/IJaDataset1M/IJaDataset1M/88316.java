package net.sourceforge.chimeralibrary.core.bean.propertyeditor;

import net.sourceforge.chimeralibrary.core.AbstractSystemCache;

/**
 * PropertyEditor class cache.
 * 
 * @author Christian Cruz
 * @version 1.0.000
 * @since 1.0.000
 */
public class PropertyEditorCache extends AbstractSystemCache<Class<?>, PropertyEditor> {

    private static volatile PropertyEditorCache instance;

    /**
	 * Returns a singleton instance of this class.
	 * 
	 * @return a singleton instance of this class
	 */
    public static final PropertyEditorCache getInstance() {
        if (instance == null) {
            synchronized (PropertyEditorCache.class) {
                if (instance == null) {
                    instance = new PropertyEditorCache();
                }
            }
        }
        return instance;
    }

    private PropertyEditorCache() {
    }

    /**
	 * Appends a PropertyEditor to this cache.
	 * 
	 * @param editor the PropertyEditor to be included in this cache
	 */
    public void add(final PropertyEditor editor) {
        final Class<?> editorClass = editor.getEditorClass();
        if ((cache != null) && !cache.containsKey(editorClass)) {
            cache.put(editorClass, editor);
        }
    }

    @Override
    protected void addSystemCache() {
        add(new DoublePropertyEditor());
        add(new IntegerPropertyEditor());
        add(new LongPropertyEditor());
        add(new BooleanPropertyEditor());
        add(new DoubleArrayPropertyEditor());
        add(new IntegerArrayPropertyEditor());
        add(new StringArrayPropertyEditor());
        add(new ClassPropertyEditor());
        add(new FilePropertyEditor());
        add(new ClassArrayPropertyEditor());
        add(new PropertiesPropertyEditor());
    }
}
