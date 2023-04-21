package com.mgiandia.library.domain.fines;

import com.mgiandia.library.util.SimpleCalendar;

/**
 * ��������� ����� ��� ������� ��� ����������� ��� ��� ������ ��� ����������� ���������. 
 * ���������� ������������ ����������� ��������� �� ��� ����� �����������. <p> 
 * �� ����������� ��� ���������� ���������� ���� ����� ��� �������� ��� ���������� �����
 * 
 * �� ��������� ���������� ���������� � 1 ������� 2007 (����� ������).
 * � ������� ���������� ����� ������� �� �������� ��� ��������� ���������� ��� �������
 * ����������� ��� ������ ��� �������.
 * ��� ������ ��� 1�� ������� ����� � 2� ������� ��� � 28� �����������. ������ ��� �������� ������������
 * ��� ����� ���� � ��� ����� ���� ��� ��������� ����������. ������ ������� ��� ��� ����������� ���������
 * ����� ��� ��� �������� ��� ��������� ���� ���������� ��� ����� ����������� ��� ������. ��� 4� �������, �����
 * ������� �� � ��������� ����� ��� ��������� ��� ������� � ���������,  � 5� ������� �� � ����� ����� ��� 
 * �������� �������� ��� ����� � 11 ������� �� � ��������� ����� ��� �������� ���������. 
 * 
 * @author ����� �����������
 * @see "��� ��� ������� ���������� ����� ����� ������� 10.2.4 ��� �������"
 */
public class FineTestCalendar {

    static SimpleCalendar get1stMarchOf2007() {
        return new SimpleCalendar(2007, 3, 1);
    }

    static SimpleCalendar get2ndMarchOf2007() {
        return new SimpleCalendar(2007, 3, 2);
    }

    static SimpleCalendar get28thFebruaryOf2007() {
        return new SimpleCalendar(2007, 2, 28);
    }

    static SimpleCalendar get4thMarchOf2007() {
        return new SimpleCalendar(2007, 3, 4);
    }

    static SimpleCalendar get5thMarchOf2007() {
        return new SimpleCalendar(2007, 3, 5);
    }

    static SimpleCalendar get11thMarchOf2007() {
        return new SimpleCalendar(2007, 3, 11);
    }
}
