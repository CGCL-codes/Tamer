package de.xirp.ui.widgets.custom;

import java.util.HashMap;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import de.xirp.ui.event.LocaleChangedEvent;
import de.xirp.ui.event.LocaleChangedListener;
import de.xirp.ui.util.ApplicationManager;
import de.xirp.ui.util.ressource.ColorManager;
import de.xirp.util.Constants;
import de.xirp.util.I18n;
import de.xirp.util.II18nHandler;
import de.xirp.util.Variables;

/**
 * This widget is enabled with on-the-fly translation and on-the-fly
 * color change capabilities. Furthermore the color is changed on
 * focus.
 * 
 * @author Matthias Gernand
 * @author Rabea Gransberger
 * @see org.eclipse.swt.widgets.Combo
 */
public class XCombo extends Combo {

    /**
	 * Listener which is called if the {@link java.util.Locale} used
	 * by the application has changed.
	 */
    private LocaleChangedListener localeListener;

    /**
	 * The handler to use for internationalization.
	 */
    private II18nHandler handler;

    /**
	 * Arguments for the parameters for the tooltip texts.
	 */
    private Object[] toolTipArgs;

    /**
	 * Key for the tooltip translations.
	 */
    private String toolTipKey;

    /**
	 * Arguments for the combobox items.
	 */
    private HashMap<Integer, Object[]> args = new HashMap<Integer, Object[]>();

    /**
	 * Keys for translation of the combobox items
	 */
    private HashMap<Integer, String> keys = new HashMap<Integer, String>();

    /**
	 * Constructs a new combo. This constructor should be used in
	 * application-UI environment.
	 * 
	 * @param parent
	 *            the parent of this combo box
	 * @param style
	 *            the style for this combo box
	 */
    public XCombo(Composite parent, int style) {
        super(parent, style);
        this.handler = I18n.getGenericI18n();
        init();
    }

    /**
	 * Constructs a new combo. This constructor should be used in
	 * plugin-UI environment, because the
	 * {@link de.xirp.util.II18nHandler handler} is
	 * needed for translations.
	 * 
	 * @param parent
	 * @param style
	 * @param handler
	 * @see de.xirp.util.II18nHandler
	 */
    public XCombo(Composite parent, int style, II18nHandler handler) {
        super(parent, style);
        this.handler = handler;
        init();
    }

    /**
	 * Initializes the control and sets the appearance listeners.
	 */
    private void init() {
        addFocusListener(new FocusListener() {

            public void focusGained(@SuppressWarnings("unused") FocusEvent e) {
                setBackground(ColorManager.getColor(Variables.FOCUS_COLOR));
            }

            public void focusLost(@SuppressWarnings("unused") FocusEvent e) {
                setBackground(ColorManager.getColor(Constants.DEFAULT_COLOR_WHITE));
            }
        });
        localeListener = new LocaleChangedListener() {

            public void localeChanged(@SuppressWarnings("unused") LocaleChangedEvent event) {
                for (int i = 0; i < getItemCount(); i++) {
                    String key = keys.get(i);
                    if (key != null) {
                        Object[] objects = args.get(i);
                        if (objects == null) {
                            setItemForLocaleKey(i, key);
                        } else {
                            setItemForLocaleKey(i, key, objects);
                        }
                    }
                }
                if (toolTipArgs == null) {
                    setToolTipTextForLocaleKey(toolTipKey);
                } else {
                    setToolTipTextForLocaleKey(toolTipKey, toolTipArgs);
                }
            }
        };
        ApplicationManager.addLocaleChangedListener(localeListener);
        addDisposeListener(new DisposeListener() {

            public void widgetDisposed(@SuppressWarnings("unused") DisposeEvent e) {
                ApplicationManager.removeLocaleChangedListener(localeListener);
            }
        });
    }

    /**
	 * Adds an item to this combo box using a key which is translated
	 * for the current locale with the given arguments for the
	 * parameters of the translation.
	 * 
	 * @param key
	 *            the key for translation
	 * @param objects
	 *            the arguments for the translation parameters (if
	 *            any).
	 * @see org.eclipse.swt.widgets.Combo#add(String)
	 */
    public void addForLocaleKey(String key, Object... objects) {
        keys.put(getItemCount(), key);
        args.put(getItemCount(), objects);
        super.add(handler.getString(key, objects));
    }

    /**
	 * Adds an item at the given index to this combo box using a key
	 * which is translated for the current locale with the given
	 * arguments for the parameters of the translation.
	 * 
	 * @param key
	 *            the key for translation
	 * @param index
	 *            the index at which the item is inserted
	 * @param objects
	 *            the arguments for the translation parameters (if
	 *            any).
	 * @see org.eclipse.swt.widgets.Combo#add(String, int)
	 */
    public void addForLocaleKey(String key, int index, Object... objects) {
        if (index == getItemCount()) {
            addForLocaleKey(key, objects);
        }
        moveItems(index, 1);
        keys.put(index, key);
        args.put(index, objects);
        super.add(handler.getString(key, objects));
    }

    /**
	 * If a new item is added or an item is removed, the entries for
	 * the key and argument maps for the items have to change.
	 * 
	 * @param fromIndex
	 *            the index at which the item was added or removed
	 * @param add
	 *            the number of items which were added or removed.
	 */
    private void moveItems(int fromIndex, int add) {
        int end = getItemCount();
        for (int i = fromIndex; i < end; i++) {
            String oldKey = keys.get(i);
            if (oldKey != null) {
                keys.remove(i);
                keys.put(i + add, oldKey);
            }
            Object[] oldArgs = args.get(i);
            if (oldArgs != null) {
                args.remove(i);
                args.put(i + add, oldArgs);
            }
        }
    }

    /**
	 * Sets the item at the given index using the given key for
	 * translation and the arguments for replacing the parameters for
	 * the translated string.
	 * 
	 * @param index
	 *            the index at which the item should be set
	 * @param key
	 *            the key for translation
	 * @param objects
	 *            the arguments for the translation parameters (if
	 *            any).
	 * @see org.eclipse.swt.widgets.Combo#setItem(int, String)
	 */
    public void setItemForLocaleKey(int index, String key, Object... objects) {
        args.put(index, objects);
        keys.put(index, key);
        super.setItem(index, handler.getString(key, objects));
    }

    /**
	 * Sets the items of the combo box using keys for translation.<br>
	 * If you want to use parameters for the translations use
	 * {@link #setItemForLocaleKey(int, String, Object...)} or
	 * {@link #addForLocaleKey(String, Object...)}.
	 * 
	 * @param items
	 *            array with the translation keys for the items.
	 * @see org.eclipse.swt.widgets.Combo#setItems(String[])
	 */
    public void setKeyItems(String[] items) {
        keys.clear();
        args.clear();
        String[] newItems = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            String key = items[i];
            keys.put(i, key);
            newItems[i] = handler.getString(key);
        }
        super.setItems(newItems);
    }

    /**
	 * Translates the given key using the given arguments and sets it
	 * as current text for the combobox.
	 * 
	 * @param key
	 *            the key for translation
	 * @param objects
	 *            the arguments for the translation parameters (if
	 *            any).
	 * @see org.eclipse.swt.widgets.Combo#setText(String)
	 */
    public void setTextForLocaleKey(String key, Object... objects) {
        int index = getSelectionIndex();
        keys.put(index, key);
        args.put(index, objects);
        super.setText(handler.getString(key, objects));
    }

    /**
	 * Sets the corresponding tool tip text using the given key and
	 * arguments for translation.
	 * 
	 * @param key
	 *            the key for translation
	 * @param objects
	 *            the arguments for the translation parameters (if
	 *            any).
	 */
    public void setToolTipTextForLocaleKey(String key, Object... objects) {
        this.toolTipKey = key;
        this.toolTipArgs = objects;
        super.setToolTipText(handler.getString(key, objects));
    }

    @Override
    public void add(String strg, int index) {
        moveItems(index, 1);
        super.add(strg, index);
    }

    @Override
    public void remove(int start, int end) {
        int length = end - start;
        moveItems(end + 1, -length);
        super.remove(start, end);
    }

    @Override
    public void remove(int index) {
        moveItems(index, -1);
        super.remove(index);
    }

    @Override
    public void remove(String string) {
        int index = 0;
        String[] items = getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(string)) {
                index = i;
                break;
            }
        }
        moveItems(index, -1);
        super.remove(string);
    }

    @Override
    public void removeAll() {
        keys.clear();
        args.clear();
        super.removeAll();
    }

    /**
	 * Selects the item with the given string (not a translation key).
	 * If the given string has no corresponding item nothing is done.
	 * 
	 * @param selection
	 *            the string to select
	 */
    public void select(String selection) {
        for (int i = 0; i < this.getItemCount(); i++) {
            if (selection.equals(this.getItem(i))) {
                this.select(i);
                break;
            }
        }
    }

    /**
	 * Overridden, because we want to create a specialization of the
	 * class.
	 * 
	 * @see org.eclipse.swt.widgets.Widget#checkSubclass()
	 */
    @Override
    protected void checkSubclass() {
    }
}
