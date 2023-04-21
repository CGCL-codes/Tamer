package com.mainatom.ui.grids;

import com.mainatom.ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Управление быстрым поиском для гриды
 */
public class QuickFind {

    private static int KEY_FORRESET = 0;

    private static int KEY_FORFIND = 1;

    private static int KEY_FORFINDDEL = 2;

    private static int KEY_FORFINDNEXT = 4;

    private static int KEY_FORFINDPRIOR = 5;

    private static int KEY_FORIGNORE = 6;

    private AControl _owner;

    private String _text;

    private JToolTip _tooltip;

    private Popup _tooltipPopup;

    private boolean _found;

    private boolean _findEnable;

    private boolean _selectedMove;

    public QuickFind() {
        _text = "";
        _findEnable = false;
    }

    /**
     * Кому принадлежит
     *
     * @param owner
     */
    public void setOwner(AControl owner) {
        _owner = owner;
    }

    public AControl getOwner() {
        return _owner;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        _text = text;
    }

    protected boolean hasText() {
        return getText().length() > 0;
    }

    protected String getTextDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><b>Поиск:</b> ");
        if (!isFound()) {
            sb.append("<font color=red>");
        }
        sb.append(getText().replace(" ", "&nbsp;"));
        return sb.toString();
    }

    public boolean isFound() {
        return _found;
    }

    public void setFound(boolean found) {
        _found = found;
    }

    public void addChar(char c) {
        setText(getText() + c);
    }

    public void delChar() {
        if (hasText()) {
            setText(getText().substring(0, getText().length() - 1));
        }
    }

    protected void doShow() {
        doHide();
        _tooltip = getOwner().getCtrl().createToolTip();
        _tooltip.setTipText(getTextDisplay());
        Point p = new Point(0, 0);
        SwingUtilities.convertPointToScreen(p, getOwner().getCtrl());
        p.y = p.y - _tooltip.getPreferredSize().height;
        _tooltipPopup = PopupFactory.getSharedInstance().getPopup(getOwner().getCtrl(), _tooltip, p.x, p.y);
        _tooltipPopup.show();
    }

    protected void doHide() {
        if (_tooltipPopup != null) {
            _tooltipPopup.hide();
            _tooltip = null;
            _tooltipPopup = null;
        }
    }

    /**
     * Поиск разрешен
     */
    public boolean isFindEnable() {
        return _findEnable;
    }

    public void setFindEnable(boolean findEnable) {
        _findEnable = findEnable;
    }

    /**
     * При значении true объект осуществляет перемещение указателя
     */
    public boolean isSelectedMove() {
        return _selectedMove;
    }

    /**
     * Полностью сброшенное состояние
     */
    public boolean isReset() {
        return !_findEnable;
    }

    /**
     * Сбросить поиск
     */
    public void reset() {
        if (!isReset()) {
            _findEnable = false;
            _found = false;
            setText("");
            doHide();
        }
    }

    /**
     * Получить статус для клавиши KEY_xxx
     *
     * @param e событие с клавы
     * @return статус
     */
    private int getKeyState(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isLetterOrDigit(c) || (c >= 32 && c <= 127)) {
            return KEY_FORFIND;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            return KEY_FORFINDNEXT;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            return KEY_FORFINDPRIOR;
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            return KEY_FORFINDDEL;
        } else if ((e.getModifiers() & KeyEvent.SHIFT_MASK) > 0) {
            return KEY_FORIGNORE;
        } else {
            return KEY_FORRESET;
        }
    }

    /**
     * Инициализация процесса поиска. Сам поиск не начинается
     */
    protected void findStart() {
        reset();
        setFindEnable(onFindStart());
        if (isFindEnable()) {
        }
    }

    /**
     * Обработчик для findStart
     *
     * @return true, если поиск разрешен и проинициализирован
     */
    protected boolean onFindStart() {
        return false;
    }

    /**
     * Поиска первого
     */
    protected void findFirst() {
        if (isReset()) {
            return;
        }
        _selectedMove = true;
        try {
            setFound(onFindFirst(getText()));
        } finally {
            _selectedMove = false;
        }
    }

    /**
     * Обработчик для findFirst. Необходимо установить правильную позицию, если нашли.
     *
     * @param text текст для поиска
     * @return true, если нашли.
     */
    protected boolean onFindFirst(String text) {
        return false;
    }

    /**
     * Поиск следующего
     */
    protected void findNext() {
        if (isReset()) {
            return;
        }
        if (!isFound()) {
            return;
        }
        _selectedMove = true;
        try {
            if (onFindNext()) {
                setFound(true);
            }
        } finally {
            _selectedMove = false;
        }
    }

    /**
     * Обработчик для findNext. Необходимо установить правильную позицию, если нашли.
     *
     * @return true, если нашли.
     */
    protected boolean onFindNext() {
        return false;
    }

    /**
     * Поиск предыдущего
     */
    protected void findPrior() {
        if (isReset()) {
            return;
        }
        if (!isFound()) {
            return;
        }
        _selectedMove = true;
        try {
            if (onFindPrior()) {
                setFound(true);
            }
        } finally {
            _selectedMove = false;
        }
    }

    /**
     * Обработчик для findPrior. Необходимо установить правильную позицию, если нашли.
     *
     * @return true, если нашли.
     */
    protected boolean onFindPrior() {
        return false;
    }

    /**
     * Перерисовать на экране
     */
    private void refresh() {
        if (isReset()) {
            doHide();
        } else {
            doShow();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = getKeyState(e);
        if (isReset()) {
            if (key == KEY_FORFIND) {
                findStart();
            }
        }
        if (isFindEnable()) {
            if (key == KEY_FORFIND) {
                addChar(e.getKeyChar());
                findFirst();
            } else if (key == KEY_FORFINDDEL) {
                if (hasText()) {
                    e.setKeyCode(0);
                }
                delChar();
                if (hasText()) {
                    findFirst();
                } else {
                    reset();
                }
            } else if (key == KEY_FORFINDNEXT) {
                e.setKeyCode(0);
                findNext();
            } else if (key == KEY_FORFINDPRIOR) {
                e.setKeyCode(0);
                findPrior();
            } else if (key == KEY_FORIGNORE) {
            } else {
                if (!isReset()) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        e.setKeyCode(0);
                    }
                }
                reset();
            }
        }
        refresh();
    }
}
