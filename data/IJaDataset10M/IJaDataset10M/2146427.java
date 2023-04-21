package com.mainatom.utils;

import java.util.*;

/**
 * Список строк
 */
public class StringList extends ListObject<String> {

    protected boolean _ignoreCase = true;

    public StringList() {
    }

    /**
     * Учитывать ли регистр при сравнении строк
     *
     * @return true - регистр игнорируется, false - учитывается
     */
    public boolean isIgnoreCase() {
        return _ignoreCase;
    }

    /**
     * Игнорировать ли регистр при сравнении строк
     *
     * @param ignoreCase true - игнорировать регистр (по умолчанию),
     *                   false - учитывать регистр
     */
    public void setIgnoreCase(boolean ignoreCase) {
        _ignoreCase = ignoreCase;
    }

    /**
     * Поиск строки
     *
     * @param s строка
     * @return Индекс найденного или -1, если не найден
     */
    public int find(String s) {
        if (s == null) {
            return -1;
        }
        int cnt = size();
        for (int i = 0; i < cnt; i++) {
            if (_ignoreCase) {
                if (s.compareToIgnoreCase(get(i)) == 0) {
                    return i;
                }
            } else {
                if (s.compareTo(get(i)) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Удалить строку
     *
     * @param s удаляемая строка. Если не найдена, ничего не происходит
     */
    public boolean remove(String s) {
        int n = find(s);
        if (n == -1) {
            return false;
        } else {
            remove(n);
            return true;
        }
    }

    /**
     * Объединение строк в одну через разделитель
     *
     * @param delimiter разделитель
     * @return строка с разделителями
     */
    public String join(String delimiter) {
        StringBuilder sb = new StringBuilder();
        int cnt = size();
        for (int i = 0; i < cnt; i++) {
            sb.append(get(i));
            if (i < cnt - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * Объединение строк в одну через разделитель с кавычками
     *
     * @param delimiter разделитель
     * @param quote     кавычка
     * @return строка с разделителями
     */
    public String join(String delimiter, String quote) {
        StringBuilder sb = new StringBuilder();
        int cnt = size();
        for (int i = 0; i < cnt; i++) {
            sb.append(quote);
            sb.append(get(i));
            sb.append(quote);
            if (i < cnt - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    /**
     * Сортировка списка
     */
    public void sort() {
        Collections.sort(this, new ThisComparator());
    }

    /**
     * Добавить строки из массива
     *
     * @param ar
     */
    public void add(String[] ar) {
        for (String s : ar) {
            add(s);
        }
    }

    class ThisComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            if (_ignoreCase) {
                return o1.compareToIgnoreCase(o2);
            } else {
                return o1.compareTo(o2);
            }
        }
    }
}
