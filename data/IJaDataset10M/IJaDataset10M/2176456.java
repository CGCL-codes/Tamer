package parse;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;
import com.creawor.hz_market.t_advertisement.t_advertisement;
import com.creawor.hz_market.resource.net_resource.NetResource;
import com.creawor.km.util.DateUtil;

public class NetResHandle {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(NetResHandle.class);

    public ArrayList readExcel(InputStream is) {
        if (logger.isDebugEnabled()) {
            logger.debug("readExcel(InputStream) - start");
        }
        ArrayList al = new ArrayList();
        try {
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("UTF-8");
            Workbook rwb = Workbook.getWorkbook(is, workbookSettings);
            Sheet st = rwb.getSheet(0);
            int length = st.getRow(1).length;
            int rownum = st.getRows();
            System.out.println((new StringBuilder("rownum=")).append(rownum).toString());
            for (int i = 2; i < rownum; i++) {
                NetResource vo = new NetResource();
                Cell[] oldcell = st.getRow(i);
                if (length < oldcell.length) length = oldcell.length;
                Cell[] cell = new Cell[length];
                System.arraycopy(oldcell, 0, cell, 0, oldcell.length);
                if (cell[0] != null && cell[0].getContents().trim().length() != 0) {
                    vo.setCounty(cell[0].getContents());
                } else {
                    continue;
                }
                if (cell[1] != null && cell[1].getContents().length() != 0) {
                    vo.setType((cell[1].getContents()));
                } else {
                    vo.setType("");
                }
                if (cell[2] != null && cell[2] != null && cell[2].getContents().length() != 0) {
                    vo.setAddress((cell[2].getContents()));
                } else {
                    vo.setAddress("");
                }
                if (cell[3] != null && cell[3].getContents().length() != 0) {
                    vo.setNetValue((cell[3].getContents()));
                } else {
                    vo.setNetValue("");
                }
                if (cell[4] != null && cell[4].getContents().length() != 0) {
                    vo.setNetService((cell[4].getContents()));
                } else {
                    vo.setNetService("");
                }
                if (cell[5] != null && cell[5].getContents().length() != 0) {
                    vo.setTransService((cell[5].getContents()));
                } else {
                    vo.setTransService("");
                }
                if (cell[6] != null && cell[6] != null && cell[6].getContents().length() != 0) {
                    String dateStr = cell[6].getContents();
                    vo.setInsertDay(DateUtil.parse(dateStr, null));
                } else {
                    vo.setInsertDay(new Date());
                }
                al.add(vo);
            }
            System.out.println("\r\n");
            rwb.close();
        } catch (Exception e) {
            logger.error("readExcel(InputStream)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("readExcel(InputStream) - end");
        }
        return al;
    }

    public String insertData(ArrayList al) {
        if (logger.isDebugEnabled()) {
            logger.debug("insertData(ArrayList) - start");
        }
        Session session = null;
        Session ups = null;
        Session sels = null;
        String returnStr = "";
        try {
            Configuration conf = null;
            SessionFactory sf = (new Configuration()).configure().buildSessionFactory();
            SessionFactory sf2 = (new Configuration()).configure().buildSessionFactory();
            session = sf.openSession();
            sels = sf.openSession();
            System.out.println((new StringBuilder("al.size=")).append(al.size()).toString());
            java.sql.Connection con = sels.connection();
            for (int i = 0; i < al.size(); i++) {
                ups = sf2.openSession();
                Transaction tx = ups.beginTransaction();
                NetResource vo = (NetResource) al.get(i);
                try {
                    String county = vo.getCounty().trim();
                    Date day = vo.getInsertDay();
                    String dateStr = DateUtil.getStr(day, null);
                    dateStr = dateStr.substring(0, 7);
                    String sqlStr = "select * from net_resource where county='" + county + "' and left(convert(varchar(10),insert_day,120),7)='" + dateStr + "'";
                    ResultSet rs = con.createStatement().executeQuery(sqlStr);
                    if (rs.next()) {
                        String id = rs.getString("id");
                        vo.setId(Integer.parseInt(id));
                        ups.update(vo);
                    } else {
                        ups.save(vo);
                    }
                    rs.close();
                } catch (Exception e) {
                    logger.error("insertData(ArrayList)", e);
                    returnStr += vo.getInsertDay() + vo.getCounty() + "�������Ϣ�Ѿ�����<br>";
                    continue;
                }
                tx.commit();
            }
        } catch (HibernateException e) {
            logger.error("insertData(ArrayList)", e);
            e.printStackTrace();
        }
        try {
            session.close();
        } catch (HibernateException e) {
            logger.error("insertData(ArrayList)", e);
            e.printStackTrace();
        }
        if ("".equals(returnStr)) returnStr = "����ɹ�";
        if (logger.isDebugEnabled()) {
            logger.debug("insertData(ArrayList) - end");
        }
        return returnStr;
    }

    public ArrayList readExcel(String filePath) {
        if (logger.isDebugEnabled()) {
            logger.debug("readExcel(String) - start");
        }
        ArrayList al = null;
        try {
            InputStream is = new FileInputStream(filePath);
            al = readExcel(is);
        } catch (FileNotFoundException e) {
            logger.error("readExcel(String)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("readExcel(String) - end");
        }
        return al;
    }

    public static void main(String args[]) {
        if (logger.isDebugEnabled()) {
            logger.debug("main(String[]) - start");
        }
        try {
            NetResHandle ex = new NetResHandle();
            System.out.println("starting....");
            ArrayList al = ex.readExcel("E:\\ad_resource.xls");
            for (int i = 0; i < al.size(); i++) {
                NetResource vo = (NetResource) al.get(i);
                System.out.println("VO:::" + vo.getCounty());
                System.out.println("PublishdateVO:::" + vo.getCounty());
            }
            System.out.println("starting....");
            ex.insertData(al);
        } catch (Exception e) {
            logger.error("main(String[])", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("main(String[]) - end");
        }
    }
}
