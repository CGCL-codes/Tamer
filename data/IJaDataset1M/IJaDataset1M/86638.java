package atp.reporter.items;

import atp.reporter.factory.RFactory;

/**
 * @author Shteinke_KE
 * �������� ����� ������
 */
public class RRows extends RLines {

    /**
	 * ��� � XML-������������ ������
	 */
    public static final String TEG = "ROWS";

    public String getDescription() {
        return "������ ������";
    }

    public String getTeg() {
        return TEG;
    }

    /**
	 * ��������� ����������
	 */
    public static RFactory factoryOfRows = new RFactory() {

        public boolean checkItem(RItem item) {
            return item instanceof RRows;
        }

        public RItem createItemByType(String type) {
            return new RRows();
        }

        protected String[] getTypeNames() {
            return new String[] {};
        }

        protected String getFactoryTeg() {
            return TEG;
        }
    };
}
