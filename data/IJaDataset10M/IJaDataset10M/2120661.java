package com.mainatom.utils;

/**
 * Формирователь для массива символов, по аналогии со StringBuilder.
 * StringBuilder не возвращает массив символов без дополнительных расходов
 * на преобразование, что не есть руль при определенных условиях.
 */
class ChrBuilder {

    private static final int BUFFER_SIZE = 16;

    private char[] _data;

    private int _length;

    public ChrBuilder() {
        _data = new char[BUFFER_SIZE];
    }

    public ChrBuilder(int size) {
        _data = new char[size];
    }

    public void setSize(int value) {
        if (value > _data.length) {
            char[] newData = new char[value];
            if (_length > value) {
                _length = value;
            }
            System.arraycopy(_data, 0, newData, 0, _length);
            _data = newData;
        }
    }

    protected void expand() {
        setSize(_data.length + BUFFER_SIZE);
    }

    public int getSize() {
        return _data.length;
    }

    public int getLength() {
        return _length;
    }

    public char[] getData() {
        return _data;
    }

    public void append(char c) {
        int n = _length + 1;
        if (n > _data.length) {
            expand();
        }
        _data[_length++] = c;
    }

    public void setLength(int value) {
        if (value > _data.length) {
            setSize(value);
        }
        _length = value;
    }

    public void append(CharSequence s) {
        for (int i = 0; i < s.length(); i++) {
            append(s.charAt(i));
        }
    }
}
