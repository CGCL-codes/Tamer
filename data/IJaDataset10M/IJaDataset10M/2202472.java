package org.gocha.textbox;

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import org.gocha.files.StreamStore;
import org.gocha.gui.ApplicationGlobal;
import org.gocha.gui.FlatTextEditor;

/**
 * Настройки плоского текста.<br/>
 * Шрифт, Перенос строк, Цвет, Отступы
 * @author gocha
 */
public class DefaultFlatTextConfig implements FlatTextConfig, Serializable {

    private Font font = null;

    private boolean wordWrap = false;

    private int tabSpaceSize = 4;

    private boolean saved = false;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener cl) {
        if (cl == null) return;
        listeners.add(cl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener cl) {
        listeners.remove(cl);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return listeners.toArray(new PropertyChangeListener[] {});
    }

    private Collection<PropertyChangeListener> listeners = new HashSet<PropertyChangeListener>();

    /**
     * Уведомляет подписчиков о измении свойства
     * @param name Имя свйоства
     * @param old Старое значение
     * @param _new Новое значение
     */
    protected void fireChanged(String name, Object old, Object _new) {
        PropertyChangeEvent e = new PropertyChangeEvent(this, name, old, _new);
        for (PropertyChangeListener l : listeners) {
            l.propertyChange(e);
        }
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) {
        Object old = this.font;
        this.font = font;
        this.saved = false;
        fireChanged("font", old, font);
    }

    @Override
    public int getTabSpaceSize() {
        return tabSpaceSize;
    }

    @Override
    public void setTabSpaceSize(int tabSpaceSize) {
        Object old = this.tabSpaceSize;
        this.tabSpaceSize = tabSpaceSize;
        this.saved = false;
        fireChanged("tabSpaceSize", old, tabSpaceSize);
    }

    @Override
    public boolean isWordWrap() {
        return wordWrap;
    }

    @Override
    public void setWordWrap(boolean wordWrap) {
        Object old = this.wordWrap;
        this.wordWrap = wordWrap;
        this.saved = false;
        fireChanged("wordWrap", old, wordWrap);
    }

    public static File getConfigFile() {
        File appDir = ApplicationGlobal.instance().getLocalApplicationDirectory();
        if (appDir == null) return appDir;
        return new File(appDir, "flatTextConfig.xml");
    }

    private static DefaultFlatTextConfig _inst = null;

    /**
     * Единичный экземпляр
     * @return экземпляр
     */
    public static DefaultFlatTextConfig instance() {
        if (_inst != null) return _inst;
        File f = getConfigFile();
        if (f != null) {
            StreamStore ss = new StreamStore();
            Object o = ss.read(f);
            if (o instanceof DefaultFlatTextConfig) {
                _inst = ((DefaultFlatTextConfig) o);
                _inst.saved = true;
            } else {
                _inst = new DefaultFlatTextConfig();
            }
        } else {
            _inst = new DefaultFlatTextConfig();
        }
        return _inst;
    }

    /**
     * Применяет настройки
     * @param editor редактор
     */
    @Override
    public void apply(FlatTextEditor editor) {
        if (editor == null) {
            throw new IllegalArgumentException("editor==null");
        }
        int tabSize = getTabSpaceSize();
        if (tabSize >= 1) editor.setSpacePerTab(tabSize);
        boolean ww = isWordWrap();
        boolean ww2 = editor.isWordWrap();
        if (ww2 != ww) editor.setWordWrap(ww);
        Font fontCur = editor.textPane().getFont();
        Font fontNeed = getFont();
        if (fontNeed != null && !fontNeed.equals(fontCur)) {
            editor.textPane().setFont(font);
        }
    }

    @Override
    public void save() {
        File f = getConfigFile();
        if (f == null) return;
        StreamStore ss = new StreamStore();
        ss.write(f, this);
        saved = true;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }
}
