package atp.reporter.product;

import atp.reporter.data.RDataItem;

/**
 * @author shteinke_ke
 * ����� ���������� � ��������� ���� 
 */
public interface ROutMethod {

    /**
	 * ��������� ������������� ��������
	 * @param dataItem ��������
	 * @return ��������� �������������
	 */
    public abstract String getText(RDataItem dataItem);
}
