package org.olga.test;

/**
 * ��������� - ����. ������������ ��� ������������ ���������
 * ������ ���������. 
 * 
 * @author Olga
 *
 */
public interface ITest {

    /**
	 * �����, ������������ ��� �����.
	 * 
	 * @return ��� �����.
	 */
    String name();

    /**
	 * �����, ����������� ����.
	 * 
	 * @return true ���� ���� ������ ������.
	 */
    boolean run();
}
