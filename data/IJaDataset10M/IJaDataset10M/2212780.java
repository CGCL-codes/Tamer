package other;

import java.util.HashMap;
import java.util.Map;
import net.excel.report.IQueryDataListener;
import net.excel.report.ReportEntry;
import net.excel.report.config.ReportConfig;
import net.excel.report.datasource.BaseDataSource;
import net.excel.report.datasource.IDataSource;
import net.excel.report.datasource.StaticDataSource;

/**
 * @author juny
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupDemo extends ReportEntry {

    private static final String[] pro = new String[] { "�Ϻ���", "������", "����ʡ", "����ʡ", "�㶫ʡ", "�½�������", "����ʡ", "����ʡ", "�㽭ʡ", "����ʡ" };

    private static final String[][] products = new String[][] { { "VE192-;��2.0�ֶ���5����", "1000000478", "1000000" }, { "VC122-PASSAT��Ԧ2.0�Զ���׼��", "1000000479", "2200000" }, { "VT262-POLO����1.6�Զ�����", "1000000480", "1886000" }, { "VE192-;��2.0�ֶ���5��������", "1000000481", "2200000" }, { "VE292-;��1.8T�Զ���5�������", "1000000484", "1400000" }, { "VLAL2-ɣ����3000 1.8L�ֶ���", "1000000486", "1300000" }, { "VLAJ2-ɣ����3000 2.0L�ֶ���", "1000000487", "1200000" }, { "VQBD2-ɣ����3000 2.0L�Զ���", "1000000488", "1100000" }, { "VE202-;��2.0L�ֶ�����", "1000000489", "11100000" }, { "VQBE2-ɣ����3000 1.8�Զ�������", "1000000490", "2480000" } };

    public boolean execute(ReportConfig reportConfig, Map dataSources, Map params) throws Exception {
        int radom = 0;
        int radomp = 0;
        int randomProduct = 0, number = 0;
        Map product1 = new HashMap(10);
        StaticDataSource ds = (StaticDataSource) dataSources.get("groupDs");
        if (ds == null) {
            return false;
        }
        String province = "";
        try {
            for (int i = 0; i < 10; i++) {
                province = pro[i];
                radom = (int) Math.abs(Math.random() * 10);
                for (int j = 0; j < radom; j++) {
                    radomp = (int) Math.abs((Math.random() * 10));
                    randomProduct = (int) Math.abs(Math.random() * 10 - 1);
                    if (null == product1.get(String.valueOf(randomProduct))) {
                        product1.put(String.valueOf(randomProduct), "");
                        for (int k = 0; k < radomp; k++) {
                            ds.addField("province", province);
                            ds.addField("sub1", products[randomProduct][0]);
                            ds.addField("sub2", products[randomProduct][1]);
                            number = (int) Math.abs(Math.random() * 100);
                            ds.addField("sub3", new Integer(number));
                            ds.addField("sub4", new Integer(products[randomProduct][2]));
                            ds.addRecord();
                        }
                    }
                }
            }
            ds.setQueryDataListener(new IQueryDataListener() {

                public boolean beforeQuery(IDataSource dataSource) {
                    BaseDataSource baseDataSource = (BaseDataSource) dataSource;
                    baseDataSource.moveToFirst();
                    baseDataSource.setFieldValue("province", "ni hao �޸���ֵ");
                    return false;
                }

                public void afterQuery(IDataSource dataSource) {
                }
            });
        } catch (Exception e) {
            throw e;
        }
        return true;
    }
}
