package mipt.crec.lab.io;

import java.io.*;

/**
 * ����� IO, ���������� �� ������ ������ �� ������ ����� � ������ ��������� ���������
 *   � ������ ������� � ����� ������. ��� ���� ����� � ����� �������� �� �����������
 * @author Evdokimov
 */
public interface IODelegate {

    Object readObject(InputStream input) throws IOException;

    void writeObject(OutputStream output, Object object) throws IOException;
}
