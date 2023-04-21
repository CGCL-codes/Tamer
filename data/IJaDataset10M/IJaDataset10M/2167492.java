package dengues.system;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf Qiang.Zhang.Adolf@gmail.com 2008-4-23 qiang.zhang $
 * 
 */
public class OracleUtils {

    public static void main(String[] args) throws Exception {
        insertLongRaw();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "convertBLOB".
     * 
     * @param in
     * @return
     */
    public static Object bytesBLOB(Object in) {
        Object blob = in;
        try {
            if (in instanceof oracle.sql.BLOB) {
                oracle.sql.BLOB bo = ((oracle.sql.BLOB) in);
                java.io.InputStream bis = bo.getBinaryStream();
                java.io.BufferedInputStream ins = new java.io.BufferedInputStream(bo.getBinaryStream());
                int bufferSize = (int) bo.length();
                byte[] bt = new byte[bufferSize];
                ins.read(bt, 0, bufferSize);
                bis.close();
                blob = bt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blob;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "insertLongRaw".
     */
    public static void insertLongRaw() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:testdb", "dengues", "dengues");
            conn.setAutoCommit(false);
            String sql1 = "insert into T_LR(T_LR_ID,T_LR_PIC) values ('26',?)";
            java.sql.PreparedStatement ps1 = conn.prepareStatement(sql1);
            java.io.FileInputStream inputStream = new java.io.FileInputStream("c:/05.bmp");
            java.io.BufferedInputStream ins = new java.io.BufferedInputStream(inputStream);
            int bufferSize = inputStream.available();
            byte[] bt = new byte[bufferSize];
            ins.read(bt, 0, bufferSize);
            ps1.setObject(1, bt);
            ps1.executeUpdate();
            System.out.println("insert success.");
            ps1.close();
            conn.setAutoCommit(true);
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "createBLOB".
     */
    public static void insertBLOB() {
        java.io.OutputStream bos = null;
        java.io.FileInputStream inputStream = null;
        java.sql.ResultSet rs2 = null;
        java.sql.PreparedStatement ps2 = null;
        java.sql.Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:dengues", "root", "root");
            conn.setAutoCommit(false);
            String sql1 = "insert into T_IMAGE(T_IMAGE_ID,T_IMAGE_BLOB) values ('24',EMPTY_BLOB())";
            java.sql.PreparedStatement ps1 = conn.prepareStatement(sql1);
            ps1.executeUpdate();
            System.out.println("insert success.");
            ps1.close();
            String sql2 = "select T_IMAGE_BLOB from T_IMAGE where T_IMAGE_ID='24' for update";
            ps2 = conn.prepareStatement(sql2);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                oracle.sql.BLOB blob = (oracle.sql.BLOB) rs2.getBlob(1);
                bos = blob.getBinaryOutputStream();
                inputStream = new java.io.FileInputStream("c:/ibm.xml");
                byte[] b = new byte[blob.getBufferSize()];
                int len = 0;
                while ((len = inputStream.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                bos.flush();
                bos.close();
            }
            rs2.close();
            inputStream.close();
            ps2.close();
            conn.setAutoCommit(true);
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
