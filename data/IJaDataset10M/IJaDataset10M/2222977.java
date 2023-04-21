package jbible.mobile;

import javax.microedition.lcdui.*;

/**
 * ��������� ��� ������� � ������� �� ������ ������� 
 * @author Konstantin K. Beliak, <a href="mailto:smbadm@yandex.ru">smbadm@yandex.ru</a>
 */
public interface MidletInterface {

    /**
     * ��������� ������ �������
     */
    public void quit();

    /**
     * ���������� �� ������ ������ 
     * @param d ������������ ������(�����)
     */
    public void show(Displayable d);

    /**
     * ���������� ���������� ��������� � ������� ��������� �� ������
     * @param e �������� ������
     * @param msg ��������� �� ������
     */
    public void fatalError(Exception e, String msg);

    /**
     * ������� ���������
     * @param a ���������
     * @param next ������ ��� ����������� ����� ����, ��� ��������� ����� ��������� �������������
     */
    public void showAlert(Alert a, Displayable next);

    /**
     * ����������� ������� ����� � ������ ����������� (������ ��� ������������ ������ � �����)
     */
    public void reloadScreens();
}
