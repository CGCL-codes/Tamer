package org.gocha.textbox;

import java.awt.Component;
import java.io.Closeable;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.gocha.gui.CloseableTabHeader;
import org.gocha.gui.TabHeader;
import org.gocha.gui.TabPane;

/**
 * Документ
 * @author gocha
 */
public class Document extends JComponent implements SingleFrame.FrameContext, Closeable, CustomTabHeader {

    private SingleFrame frame = null;

    /**
     * Вызывется при присоединении к окну SingleFrame (или его потомкам)
     */
    protected void attachFrame() {
    }

    /**
     * Вызывется при отсоединении от окна SingleFrame (или его потомков)
     */
    protected void detachFrame() {
    }

    @Override
    public void attach(SingleFrame frame) {
        if (this.frame != null) {
            detachFrame();
        }
        this.frame = frame;
        if (this.frame != null) {
            attachFrame();
        }
    }

    @Override
    public void detach(SingleFrame frame) {
        if (this.frame != null) {
            detachFrame();
        }
        this.frame = null;
    }

    @Override
    public SingleFrame getFrame() {
        return frame;
    }

    /**
     * Возвращает ссылку на DockedFrame
     * @return Ссылка или null
     */
    public DockedFrame getDockedFrame() {
        Object o = getFrame();
        return (o instanceof DockedFrame) ? (DockedFrame) o : null;
    }

    /**
     * Возвращает индекс вкладки для документа
     * @return Индекс или -1
     */
    public int getTabIndex() {
        int r = -1;
        DockedFrame df = getDockedFrame();
        if (df != null) {
            r = df.getTabPane().indexOfComponent(this);
        }
        return r;
    }

    /**
     * Заголовок документа
     */
    protected TabHeader tabHeader = null;

    /**
     * Создает заголовок для документа
     * @param tabPane Tab панель
     * @return Вкладка
     */
    @Override
    public Component createTabHeader(TabPane tabPane) {
        if (tabHeader != null) return tabHeader;
        tabHeader = new TabHeader(tabPane, this, true);
        initTabHeader();
        return tabHeader;
    }

    /**
     * Возвращает заголовок для документа
     * @return Заголовок
     */
    public TabHeader getTabHeader() {
        return tabHeader;
    }

    /**
     * Настраивает заголовок
     */
    protected void initTabHeader() {
        JTabbedPane tp = tabHeader.getTabbedPane();
        Component owner = tabHeader.getTabOwner();
        tabHeader.setFontBold(!saved);
        tabHeader.getActions().add(new CloseableTabHeader.CloseAction(tp, owner));
    }

    @Override
    public void close() throws IOException {
        if (frame != null) {
            detach(frame);
            frame = null;
        }
        if (tabHeader != null) {
            tabHeader.close();
            tabHeader = null;
        }
    }

    /**
     * Флаг сохранен документ
     */
    protected boolean saved = true;

    /**
     * Флаг документ сохранен или нет
     * @return true - сохранен
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Флаг документ сохранен или нет
     * @param saved true - сохранен
     */
    public void setSaved(boolean saved) {
        Object old = this.saved;
        this.saved = saved;
        if (tabHeader != null) tabHeader.setFontBold(!saved);
        firePropertyChange("saved", old, saved);
    }
}
