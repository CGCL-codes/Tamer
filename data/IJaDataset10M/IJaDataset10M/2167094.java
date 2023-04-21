package com.googlecode.multiagentsys.agent;

/**
 * ����� ���������� ������������ ������.
 * ����� ����������� ����� doAction(), ������������ ��������� �������� ������
 * @author Tom-Trix
 *
 */
public abstract class AbstractAgent {

    /**
	 * ���������� (TODO: �� ������ ������� �������)
	 */
    private int _x, _y;

    /**
	 * ���������� ��������� �������� ������
	 */
    public abstract void doAction();

    /**
	 * @return �-���������� ������
	 */
    public int get_x() {
        return _x;
    }

    /**
	 * ������������� �-���������� ������
	 * @param _x
	 */
    public void set_x(int _x) {
        this._x = _x;
    }

    /**
	 * @return y-���������� ������
	 */
    public int get_y() {
        return _y;
    }

    /**
	 * ������������� y-���������� ������
	 * @param _x
	 */
    public void set_y(int _y) {
        this._y = _y;
    }
}
