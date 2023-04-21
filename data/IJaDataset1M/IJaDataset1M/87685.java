package ArianneViewer;

import junit.framework.*;
import java.util.*;
import java.util.logging.*;
import java.sql.*;
import javax.swing.*;
import com.borland.dx.sql.dataset.*;
import java.awt.event.*;
import java.awt.*;
import ArianneUtil.*;
import javolution.util.FastTable;

/**
 * <p>Title: Guide Viewer</p>
 *
 * <p>Description: Visualizzatore per pagine create con Arianne Editor</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Itaco S.r.l.</p>
 *
 * @author Andrea Annibali
 * @version 1.0
 */
public class TestDrawingPanel extends TestCase {

    private DrawingPanel drawingPanel = null;

    private PictureViewer pv = null;

    public TestDrawingPanel(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        pv = createPictureViewer();
        drawingPanel = pv.getDrawingPanel();
    }

    protected void tearDown() throws Exception {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        try {
            drawingPanel.memDb.update("DROP USER root");
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        pv.setVisible(false);
        drawingPanel = null;
        pv = null;
        super.tearDown();
    }

    public void testCreateMemDb() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            drawingPanel.memDb.update("INSERT INTO IMAGE (NAME,WIDTH,HEIGHT,BACKGROUND) " + "VALUES ('Prova',100,100,null)");
            drawingPanel.memDb.update("INSERT INTO DBCONNECTION (ID,HOST,PORT,DBNAME,USERNAME,PASSWORD,NAME,IMAGE_NAME) " + "VALUES (1,'localhost',3306,'testdb','root','root','test','test')");
            drawingPanel.memDb.update("INSERT INTO LINK_SHAPES (ID_SH2,IMAGE_NAME_SH2,ID_SH1,IMAGE_NAME_SH1,LINE_NUMBER,LINK_ACTION) " + "VALUES (1,'test',2,'test',2,'test action')");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,1,20.3,30.2)");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,2,40.3,50.2)");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,3,60.3,70.2)");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,4,80.3,90.2)");
            drawingPanel.memDb.update("INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) " + "VALUES ('test',1,'Diamond',2,4,0,1,1)");
            drawingPanel.memDb.update("INSERT INTO SHAPE_PARAMETERS (IMAGE_NAME,SHAPE_ID,PARAMETER,VALUE) " + "VALUES ('test',1,'parametro','valore')");
            drawingPanel.memDb.update("INSERT INTO LINK_SHAPEQUERY (SHAPE_ID,IMAGE_NAME, QUERY_ID) " + "VALUES (1,'test',1)");
            drawingPanel.memDb.update("INSERT INTO SHAPEQUERY (ID,IMAGE_NAME,SQLQUERY) " + "VALUES (1,'test','query')");
            drawingPanel.memDb.update("INSERT INTO SYSCALL (SYS_CALL_NAME,ID,IMAGE_NAME,DESCR,FK_SHAPE_ID) " + "VALUES ('syscallname',1,'test','descrizione',1)");
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableIMAGE(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableDBCONNECTION(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableLINK_SHAPES(drawingPanel.memDb));
        assertEquals("return value", 4, ArianneUtil.Util.dumpTablePOINT(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableSHAPE_PARAMETERS(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableLINK_SHAPEQUERY(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableSHAPEQUERY(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableSYSCALL(drawingPanel.memDb));
    }

    public void testDropMemDb() {
        try {
            drawingPanel.memDb.update("INSERT INTO IMAGE (NAME,WIDTH,HEIGHT,BACKGROUND) " + "VALUES ('Prova',100,100,null)");
            drawingPanel.memDb.update("INSERT INTO DBCONNECTION (ID,HOST,PORT,DBNAME,USERNAME,PASSWORD,NAME,IMAGE_NAME) " + "VALUES (1,'localhost',3306,'testdb','root','root','test','test')");
            drawingPanel.memDb.update("INSERT INTO LINK_SHAPES (ID_SH2,IMAGE_NAME_SH2,ID_SH1,IMAGE_NAME_SH1,LINE_NUMBER,LINK_ACTION) " + "VALUES (1,'test',2,'test',1,'test action')");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,1,20.3,30.2)");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,2,40.3,50.2)");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,3,60.3,70.2)");
            drawingPanel.memDb.update("INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) " + "VALUES ('test',1,4,80.3,90.2)");
            drawingPanel.memDb.update("INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) " + "VALUES ('test',1,'Diamond',2,4,0,1,1)");
            drawingPanel.memDb.update("INSERT INTO SHAPE_PARAMETERS (IMAGE_NAME,SHAPE_ID,PARAMETER,VALUE) " + "VALUES ('test',1,'parametro','valore')");
            drawingPanel.memDb.update("INSERT INTO LINK_SHAPEQUERY (SHAPE_ID,IMAGE_NAME, QUERY_ID) " + "VALUES (1,'test',1)");
            drawingPanel.memDb.update("INSERT INTO SHAPEQUERY (ID,IMAGE_NAME,SQLQUERY) " + "VALUES (1,'test','query')");
            drawingPanel.memDb.update("INSERT INTO SYSCALL (SYS_CALL_NAME,ID,IMAGE_NAME,DESCR,FK_SHAPE_ID) " + "VALUES ('syscallname',1,'test','descrizione',1)");
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        boolean expectedReturn = false;
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("IMAGE", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("DBCONNECTION", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("LINK_SHAPES", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("POINT", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("SHAPE", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("SHAPE_PARAMETERS", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("LINK_SHAPEQUERY", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("SHAPEQUERY", drawingPanel.memDb));
        assertEquals("return value", expectedReturn, ArianneUtil.Util.existsTable("SYSCALL", drawingPanel.memDb));
    }

    public void testAddShape() {
        double[] xPnt = new double[] { 0, 100, 100, 0 };
        double[] yPnt = new double[] { 0, 0, 100, 100 };
        ViewerShapes s = new PolygonShape(1, xPnt, yPnt, 4, 1, "Countinuous", 1.0f, 10000, 30000, "Rectangle", "none", drawingPanel, true, false, 0xFF, true, 300, 10000, false);
        drawingPanel.addShape(s);
        int idFnd = -1;
        if (drawingPanel.getList() != null) {
            for (int i = 0; idFnd == -1 && i < drawingPanel.getList().size(); i++) {
                Shapes actS = (Shapes) drawingPanel.getList().get(i);
                if (actS.getElemId() == s.getElemId()) {
                    assertEquals("return value", actS, s);
                    idFnd = s.getElemId();
                }
            }
        }
        assertEquals("return value", "" + idFnd, "" + s.getElemId());
    }

    /**
   * @todo aggiungere tutti i tipi di shape
   */
    public void testArrangeConnection() {
        double[] xPnt = new double[] { 0, 100, 100, 0 };
        double[] yPnt = new double[] { 0, 0, 100, 100 };
        ViewerShapes s1 = new PolygonShape(1, xPnt, yPnt, 4, 1, "Countinuous", 1.0f, 10000, 30000, "Rectangle", "none", drawingPanel, true, false, 0xFF, true, 300, 10000, false);
        drawingPanel.addShape(s1);
        String hostName = "localhost";
        int port = 3306;
        String databaseName = "wamsdb";
        String userName = "wams";
        String password = "wams";
        int refreshPeriod = 0;
        int dbconnId = 1;
        String[] sqlQuery = { "select module from tm_snapshot where measurand_id=1" };
        String qId = "1";
        drawingPanel.arrangeConnection(s1, hostName, port, databaseName, userName, password, refreshPeriod, dbconnId, sqlQuery, qId);
        assertEquals("return value", hostName, s1.getHostName());
        assertEquals("return value", port, s1.getConnectionPort());
        assertEquals("return value", databaseName, s1.getDbName());
        assertEquals("return value", userName, s1.getUserName());
        assertEquals("return value", password, s1.getPassword());
        assertEquals("return value", refreshPeriod, s1.getRefreshPeriod());
        assertEquals("return value", dbconnId, s1.getConnectionId());
        assertEquals("return value", sqlQuery[0], s1.getSqlQuery());
        assertEquals("return value", qId, s1.getQueryId());
        String connStr = "jdbc:mysql://" + hostName + ":" + port + "/" + databaseName + "?user=" + userName + (password == null ? "" : "&password=" + password) + "&relaxAutocommit=true";
        assertEquals("return value", connStr, drawingPanel.getConnectString());
        assertEquals("return value", true, s1.getDataProvider() != null);
        sqlQuery = new String[] { "select module from tm_snapshot where measurand_id=2" };
        qId = "2";
        xPnt = new double[] { 0, 200, 200, 0 };
        yPnt = new double[] { 0, 0, 200, 200 };
        ViewerShapes s2 = new PolygonShape(2, xPnt, yPnt, 4, 1, "Countinuous", 1.0f, 10000, 30000, "Rectangle", "none", drawingPanel, true, false, 0xFF, true, 300, 10000, false);
        drawingPanel.arrangeConnection(s2, hostName, port, databaseName, userName, password, refreshPeriod, dbconnId, sqlQuery, qId);
        assertEquals("return value", hostName, s2.getHostName());
        assertEquals("return value", port, s2.getConnectionPort());
        assertEquals("return value", databaseName, s2.getDbName());
        assertEquals("return value", userName, s2.getUserName());
        assertEquals("return value", password, s2.getPassword());
        assertEquals("return value", refreshPeriod, s2.getRefreshPeriod());
        assertEquals("return value", dbconnId, s2.getConnectionId());
        assertEquals("return value", sqlQuery[0], s2.getSqlQuery());
        assertEquals("return value", qId, s2.getQueryId());
        assertEquals("return value", connStr, drawingPanel.getConnectString());
        assertEquals("return value", true, s1.getDataProvider() == s1.getDataProvider());
    }

    public void testCloseImg() {
        double[] xPnt1 = new double[] { 0, 100, 100, 0 };
        double[] yPnt1 = new double[] { 0, 0, 100, 100 };
        double[] xPnt2 = new double[] { 100, 100, 200, 0 };
        double[] yPnt2 = new double[] { 100, 0, 200, 100 };
        ViewerShapes s1 = new PolygonShape(1, xPnt1, yPnt1, 4, 1, "Countinuous", 1.0f, 10000, 30000, "Rectangle", "none", drawingPanel, true, false, 0xFF, true, 300, 10000, false);
        ViewerShapes s2 = new ButtonShape(2, "Test Button", xPnt2, yPnt2, 10000, 30000, 40000, new Font("Courier", Font.ITALIC, 12), "none", "", drawingPanel, true, false, SwingConstants.CENTER, SwingConstants.CENTER, 0xFF, true, 300, 10000);
        drawingPanel.addShape(s1);
        drawingPanel.addShape(s2);
        drawingPanel.closeImg();
        assertEquals("return value", 0, drawingPanel.getList().size());
        assertEquals("return value", 0, drawingPanel.getComponentCount());
        assertEquals("return value", 0, drawingPanel.dbConnections.size());
    }

    public void testCloseImg1() {
        double[] xPnt1 = new double[] { 0, 100, 100, 0 };
        double[] yPnt1 = new double[] { 0, 0, 100, 100 };
        double[] xPnt2 = new double[] { 200, 100, 300, 0 };
        double[] yPnt2 = new double[] { 200, 0, 300, 100 };
        ViewerShapes s1 = new PolygonShape(1, xPnt1, yPnt1, 4, 1, "Countinuous", 1.0f, 10000, 30000, "Rectangle", "none", drawingPanel, true, false, 0xFF, true, 300, 10000, false);
        ViewerShapes s2 = new ButtonShape(2, "Test Button", xPnt2, yPnt2, 10000, 30000, 40000, new Font("Courier", Font.ITALIC, 12), "none", "", drawingPanel, true, false, SwingConstants.CENTER, SwingConstants.CENTER, 0xFF, true, 300, 10000);
        drawingPanel.addShape(s1);
        drawingPanel.addShape(s2);
        drawingPanel.closeImg(drawingPanel);
        assertEquals("return value", 0, drawingPanel.getList().size());
        assertEquals("return value", 0, drawingPanel.getComponentCount());
        assertEquals("return value", 0, drawingPanel.dbConnections.size());
    }

    public void testDirectLoadImg() {
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String selectedValue = "Test1";
        ArianneUtil.Util.preLoadImage(path, selectedValue, drawingPanel.memDb);
        drawingPanel.directLoadImg(selectedValue);
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableIMAGE(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableDBCONNECTION(drawingPanel.memDb));
        assertEquals("return value", 0, ArianneUtil.Util.dumpTableLINK_SHAPES(drawingPanel.memDb));
        assertEquals("return value", 964, ArianneUtil.Util.dumpTablePOINT(drawingPanel.memDb));
        assertEquals("return value", 241, ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb));
        assertEquals("return value", 4331, ArianneUtil.Util.dumpTableSHAPE_PARAMETERS(drawingPanel.memDb));
        assertEquals("return value", 241, ArianneUtil.Util.dumpTableLINK_SHAPEQUERY(drawingPanel.memDb));
        assertEquals("return value", 1, ArianneUtil.Util.dumpTableSHAPEQUERY(drawingPanel.memDb));
        assertEquals("return value", 0, ArianneUtil.Util.dumpTableSYSCALL(drawingPanel.memDb));
    }

    public void testGetAbsolutePositionX() {
        int expectedReturn = 0;
        int actualReturn = drawingPanel.getAbsolutePositionX();
        assertEquals("return value", expectedReturn, actualReturn);
        drawingPanel.setLocation(100, 200);
        expectedReturn = 0;
        actualReturn = drawingPanel.getAbsolutePositionX();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetAbsolutePositionY() {
        int expectedReturn = 59;
        int actualReturn = drawingPanel.getAbsolutePositionY();
        assertEquals("return value", expectedReturn, actualReturn);
        drawingPanel.setLocation(100, 200);
        expectedReturn = 59;
        actualReturn = drawingPanel.getAbsolutePositionY();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetAntialiasing() {
        boolean expectedReturn = false;
        boolean actualReturn = drawingPanel.getAntialiasing();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetBooleanParam() {
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        Hashtable parameters = drawingPanel.getParameters(imgName, 1);
        String paramName = "OPAQUE";
        boolean expectedReturn = true;
        boolean actualReturn = drawingPanel.getBooleanParam(parameters, paramName);
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetDouble() {
        String expectedReturn = "53.0";
        String actualReturn = null;
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        String q = "SELECT X FROM POINT WHERE IMAGE_NAME='" + imgName + "' AND SHAPE_ID=1 AND POINT_ID=1";
        try {
            Statement st = null;
            ResultSet rs = null;
            st = drawingPanel.memDb.getConnection().createStatement();
            rs = st.executeQuery(q);
            if (rs.next()) actualReturn = ArianneUtil.Util.getDouble(rs, "X"); else assertEquals("return value", false, true);
            rs.close();
            st.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetDoubleParam() {
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        Hashtable parameters = drawingPanel.getParameters(imgName, 264);
        String paramName = "MAXSLIDER";
        double expectedReturn = 100.0;
        double actualReturn = drawingPanel.getDoubleParam(parameters, paramName);
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetElem() {
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        int id = 264;
        double[] xPnt = new double[] { 158, 324, 324, 158 };
        double[] yPnt = new double[] { 574, 574, 589, 589 };
        SliderShape expectedReturn = new SliderShape(id, "Normal", xPnt, yPnt, 4, 1, "Countinuous", 1.0f, -16776961, -65536, 0, 100, 10, new Font("Courier", Font.PLAIN, 10), -65536, false, true, true, true, true, true, true, drawingPanel, true, false, 0xFF, "Test1", true, 0, 0);
        Shapes actualReturn = drawingPanel.getElem(id);
        assertEquals("return value", true, (actualReturn instanceof SliderShape));
        assertEquals("return value", expectedReturn.getElemId(), actualReturn.getElemId());
        assertEquals("return value", expectedReturn.getXCoordinates()[0], actualReturn.getXCoordinates()[0]);
        assertEquals("return value", expectedReturn.getXCoordinates()[1], actualReturn.getXCoordinates()[1]);
        assertEquals("return value", expectedReturn.getXCoordinates()[2], actualReturn.getXCoordinates()[2]);
        assertEquals("return value", expectedReturn.getXCoordinates()[3], actualReturn.getXCoordinates()[3]);
        assertEquals("return value", expectedReturn.getYCoordinates()[0], actualReturn.getYCoordinates()[0]);
        assertEquals("return value", expectedReturn.getYCoordinates()[1], actualReturn.getYCoordinates()[1]);
        assertEquals("return value", expectedReturn.getYCoordinates()[2], actualReturn.getYCoordinates()[2]);
        assertEquals("return value", expectedReturn.getYCoordinates()[3], actualReturn.getYCoordinates()[3]);
        assertEquals("return value", expectedReturn.getNumVertex(), actualReturn.getNumVertex());
        assertEquals("return value", expectedReturn.getShapeBorderColorRGB(), actualReturn.getShapeBorderColorRGB());
        assertEquals("return value", expectedReturn.getShapeFillColorRGB(), (actualReturn instanceof FillableShape ? ((FillableShape) actualReturn).getShapeFillColorRGB() : -1));
        assertEquals("return value", ((SliderShape) expectedReturn).getMinSlider(), ((SliderShape) actualReturn).getMinSlider());
        assertEquals("return value", ((SliderShape) expectedReturn).getMaxSlider(), ((SliderShape) actualReturn).getMaxSlider());
        assertEquals("return value", expectedReturn.getFatherPanel(), actualReturn.getFatherPanel());
        assertEquals("return value", expectedReturn.isOpaque(), actualReturn.isOpaque());
        assertEquals("return value", expectedReturn.isInBackground(), actualReturn.isInBackground());
        assertEquals("return value", expectedReturn.getOverlay(), actualReturn.getOverlay());
        assertEquals("return value", expectedReturn.getImgName(), actualReturn.getImgName());
        assertEquals("return value", expectedReturn.isPolling(), actualReturn.isPolling());
        assertEquals("return value", expectedReturn.getPollInterval(), actualReturn.getPollInterval());
        assertEquals("return value", expectedReturn.getBckCycle(), actualReturn.getBckCycle());
    }

    public void testGetImgName() {
        String expectedReturn = "Test1";
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        String actualReturn = drawingPanel.getImgName();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    /**
   * @todo spostare nei test di ArianneUtil
   */
    public void testGetInt() {
        String expectedReturn = "4";
        String actualReturn = null;
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        String q = "SELECT NUMVERTEX FROM SHAPE WHERE IMAGE_NAME='" + imgName + "' AND ID=4 ";
        try {
            Statement st = null;
            ResultSet rs = null;
            st = drawingPanel.memDb.getConnection().createStatement();
            rs = st.executeQuery(q);
            if (rs.next()) actualReturn = ArianneUtil.Util.getInt(rs, "NUMVERTEX"); else assertEquals("return value", false, true);
            rs.close();
            st.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetIntParam() {
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        Hashtable parameters = drawingPanel.getParameters(imgName, 264);
        String paramName = "OVERLAY";
        int expectedReturn = 255;
        int actualReturn = drawingPanel.getIntParam(parameters, paramName);
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetParameters() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE_PARAMETERS (IMAGE_NAME,SHAPE_ID,PARAMETER,VALUE) " + "VALUES ";
            String q2 = "('Test',1,'TABBGCOLCOLOR0','" + Integer.MIN_VALUE + "')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'TABBGCOLCOLOR1','" + Integer.MAX_VALUE + "')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'TABFGCOLCOLOR0','0')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'TABFGCOLCOLOR1','22222')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SHAPECOLOR','33333')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'GRIDCOLOR','44444')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'BORDERCOLOR','55555')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'FILLCOLOR','66666')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'TEXTCOLOR','77777')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SELECTEDTEXTCOLOR','88888')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SELECTEDBACKCOLOR','88888')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'FOCUSTEXTCOLOR','99999')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'FOCUSBACKCOLOR','30303')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'DISPLAYHEADER','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'BLINKING','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SHOWVGRID','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SHOWHGRID','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'DISPLAYSHAPES','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'OPAQUE','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SHOWBORDER','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'BACKGROUND','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'ENABLED','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'POLLING','true')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'FONTNAME','Courier')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'SQLQUERY','SELECT * FROM CAT')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'VALUE','prova')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'DEFAULTIMAGEPATH','c:\\')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'FORMATTER','##.##')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'CHARTTITLE','Prova')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'MAXSLIDER','0.0')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q2 = "('Test',1,'MINSLIDER','" + Double.MAX_VALUE + "')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Hashtable params = drawingPanel.getParameters("Test", 1);
        assertEquals("return value", Integer.MIN_VALUE, ((Integer) params.get("TABBGCOLCOLOR0")).intValue());
        assertEquals("return value", Integer.MAX_VALUE, ((Integer) params.get("TABBGCOLCOLOR1")).intValue());
        assertEquals("return value", 0, ((Integer) params.get("TABFGCOLCOLOR0")).intValue());
        assertEquals("return value", 22222, ((Integer) params.get("TABFGCOLCOLOR1")).intValue());
        assertEquals("return value", 33333, ((Integer) params.get("SHAPECOLOR")).intValue());
        assertEquals("return value", 44444, ((Integer) params.get("GRIDCOLOR")).intValue());
        assertEquals("return value", 55555, ((Integer) params.get("BORDERCOLOR")).intValue());
        assertEquals("return value", 66666, ((Integer) params.get("FILLCOLOR")).intValue());
        assertEquals("return value", 77777, ((Integer) params.get("TEXTCOLOR")).intValue());
        assertEquals("return value", 88888, ((Integer) params.get("SELECTEDTEXTCOLOR")).intValue());
        assertEquals("return value", 88888, ((Integer) params.get("SELECTEDBACKCOLOR")).intValue());
        assertEquals("return value", 99999, ((Integer) params.get("FOCUSTEXTCOLOR")).intValue());
        assertEquals("return value", 30303, ((Integer) params.get("FOCUSBACKCOLOR")).intValue());
        assertEquals("return value", true, ((Boolean) params.get("DISPLAYHEADER")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("BLINKING")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("SHOWVGRID")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("SHOWHGRID")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("DISPLAYSHAPES")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("OPAQUE")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("SHOWBORDER")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("BACKGROUND")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("ENABLED")).booleanValue());
        assertEquals("return value", true, ((Boolean) params.get("POLLING")).booleanValue());
        assertEquals("return value", "Courier", (String) params.get("FONTNAME"));
        assertEquals("return value", "SELECT * FROM CAT", (String) params.get("SQLQUERY"));
        assertEquals("return value", "prova", (String) params.get("VALUE"));
        assertEquals("return value", "c:\\", (String) params.get("DEFAULTIMAGEPATH"));
        assertEquals("return value", "##.##", (String) params.get("FORMATTER"));
        assertEquals("return value", "Prova", (String) params.get("CHARTTITLE"));
        assertEquals("return value", 0.0, ((Double) params.get("MAXSLIDER")).doubleValue());
        assertEquals("return value", Double.MAX_VALUE, ((Double) params.get("MINSLIDER")).doubleValue());
    }

    public void testGetQuery() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO LINK_SHAPEQUERY (SHAPE_ID,IMAGE_NAME,QUERY_ID) VALUES ";
            String q2 = "(1,'Test1',1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO SHAPEQUERY (ID,IMAGE_NAME,SQLQUERY) VALUES ";
            q2 = "(1,'Test1','SELECT * FROM CAT')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO LINK_SHAPEQUERY (SHAPE_ID,IMAGE_NAME,QUERY_ID) VALUES ";
            q2 = "(2,'Test1',2)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO SHAPEQUERY (ID,IMAGE_NAME,SQLQUERY) VALUES ";
            q2 = "(2,'Test1','SELECT * FROM AAA')";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("return value", "SELECT * FROM CAT", drawingPanel.getQuery(1, "Test1"));
        assertEquals("return value", "SELECT * FROM AAA", drawingPanel.getQuery(2, "Test1"));
    }

    public void testGetRefreshPeriod() {
        int expectedReturn = 0;
        int actualReturn = drawingPanel.getRefreshPeriod();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    /**
   * @todo Spostare nei test di ArianneUtil
   */
    public void testGetString() {
        String expectedReturn = "'Helvetica Grassetto'";
        String actualReturn = null;
        String path = ".\\test\\ArianneViewer\\Test1.xml";
        String imgName = "Test1";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        String q = "SELECT VALUE FROM SHAPE_PARAMETERS WHERE IMAGE_NAME='" + imgName + "' AND PARAMETER='FONTNAME' AND SHAPE_ID=263 ";
        try {
            Statement st = null;
            ResultSet rs = null;
            st = drawingPanel.memDb.getConnection().createStatement();
            rs = st.executeQuery(q);
            if (rs.next()) actualReturn = ArianneUtil.Util.getString(rs, "VALUE"); else assertEquals("return value", false, true);
            rs.close();
            st.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetTimerButton() {
        boolean expectedReturn = false;
        boolean actualReturn = drawingPanel.getTimerButton();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetTimerToggle() {
        drawingPanel.getFather().timerToggleButton.setSelected(false);
        pressGoButton(drawingPanel.getFather());
        boolean expectedReturn = true;
        boolean actualReturn = drawingPanel.getTimerToggle();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testGetlastSelect() {
        int actualReturn = drawingPanel.getlastSelect();
        assertEquals("return value", -1, actualReturn);
        String path = ".\\test\\ArianneViewer\\Test3.xml";
        String imgName = "Test3";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        try {
            ButtonShape b = (ButtonShape) drawingPanel.getList().get(1);
            MouseEvent e = new MouseEvent((Component) drawingPanel, MouseEvent.BUTTON1, System.currentTimeMillis(), MouseEvent.BUTTON1_DOWN_MASK, b.getButton().getX() + (int) Math.round(b.getButton().getWidth() / 2), b.getButton().getY() + (int) Math.round(b.getButton().getHeight() / 2), 1, false, MouseEvent.BUTTON1);
            drawingPanel.this_mouseClicked(e);
            if (b.isInsideArea(e.getPoint())) b.button_actionPerformed(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("return value", 2, drawingPanel.getlastSelect());
    }

    public void testLastSelect() {
        int ids = 30;
        drawingPanel.lastSelect(ids);
        int actualReturn = drawingPanel.getlastSelect();
        int expectedReturn = ids;
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testLoadArrowLineShape() {
        int id = 1;
        String imgName = "Test";
        int nVertex = 7;
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = ArianneUtil.Shapes.NO_COLOR;
        int ovl = 0xFF;
        int pollIntervall = 300;
        int bckCycle = 10000;
        String type = "ArrowLine";
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean isPolling = true;
        boolean is3d = false;
        ArrowLineShape actualReturn = (ArrowLineShape) drawingPanel.loadArrowLineShape(id, imgName, nVertex, 1, "Countinuous", 1.0f, sColor, fColor, type, isOpaque, isInBck, ovl, isPolling, pollIntervall, bckCycle, is3d);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", nVertex, actualReturn.getNumVertex());
        assertEquals("return value", sColor, actualReturn.getShapeBorderColorRGB());
        assertEquals("return value", fColor, actualReturn.getShapeFillColorRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", 0xFF, actualReturn.getOverlay());
        assertEquals("return value", true, actualReturn.isPolling());
        assertEquals("return value", 300, actualReturn.getPollInterval());
        assertEquals("return value", 10000, actualReturn.getBckCycle());
    }

    public void testLoadButtonShape() {
        int id = 0;
        String imgName = "Test";
        String textVal = "Bottone1";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = ArianneUtil.Shapes.NO_COLOR;
        int tColor = ArianneUtil.Shapes.NO_COLOR;
        String fName = "Courier";
        int fStyle = Font.BOLD;
        int fSize = 12;
        String precision = "##.##";
        boolean en = true;
        boolean blinking = false;
        boolean isOpaque = true;
        boolean isInBck = false;
        int tHAlign = SwingConstants.CENTER;
        int tVAlign = SwingConstants.BOTTOM;
        ButtonShape actualReturn = (ButtonShape) drawingPanel.loadButtonShape(id, imgName, textVal, sColor, fColor, tColor, fName, fStyle, fSize, precision, en, blinking, tHAlign, tVAlign, 0xFF, true, 300, 10000);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 4, actualReturn.getNumVertex());
        assertEquals("return value", textVal, actualReturn.getButtonLabel());
        assertEquals("return value", sColor, actualReturn.getShapeBorderColorRGB());
        assertEquals("return value", fColor, actualReturn.getShapeFillColorRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", fName, actualReturn.getFont().getName());
        assertEquals("return value", fStyle, actualReturn.getFont().getStyle());
        assertEquals("return value", fSize, actualReturn.getFont().getSize());
        assertEquals("return value", tHAlign, actualReturn.getTextHAlignMent());
        assertEquals("return value", tVAlign, actualReturn.getTextVAlignMent());
        assertEquals("return value", 0xFF, actualReturn.getOverlay());
        assertEquals("return value", true, actualReturn.isPolling());
        assertEquals("return value", 300, actualReturn.getPollInterval());
        assertEquals("return value", 10000, actualReturn.getBckCycle());
    }

    public void testLoadChartShape() {
        int id = 100;
        String imgName = "Test";
        boolean ds = false;
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = ArianneUtil.Shapes.NO_COLOR;
        int fntColor = ArianneUtil.Shapes.NO_COLOR;
        String fName = "Courier";
        int fStyle = Font.ITALIC;
        int fSize = 14;
        int ovl = 0xFF;
        String sqlQry = "SELECT module FROM tm";
        boolean isOpaque = false;
        boolean isInBck = false;
        ChartShape actualReturn = (ChartShape) drawingPanel.loadChartShape(id, "Chart", 4, 1, "Countinuous", 1.0f, 0.0, 100.0, imgName, ds, sColor, fColor, fntColor, fName, Font.ITALIC, fSize, sqlQry, "", isOpaque, isInBck, ovl, "Prova", "MW", "Time", "Times", Font.ITALIC, 14, Color.black.getRGB(), null, 10000, false, true, true, 300, 10000, true, false);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 4, actualReturn.getNumVertex());
        assertEquals("return value", sColor, actualReturn.getShapeBorderColorRGB());
        assertEquals("return value", fColor, actualReturn.getShapeFillColorRGB());
        assertEquals("return value", fntColor, actualReturn.getFontColor().getRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", fName, actualReturn.getFont().getName());
        assertEquals("return value", fStyle, actualReturn.getFont().getStyle());
        assertEquals("return value", fSize, actualReturn.getFont().getSize());
        assertEquals("return value", 0xFF, actualReturn.getOverlay());
        assertEquals("return value", true, actualReturn.isPolling());
        assertEquals("return value", 300, actualReturn.getPollInterval());
        assertEquals("return value", 10000, actualReturn.getBckCycle());
    }

    public void testLoadGroup() {
        String path = ".\\test\\ArianneViewer\\Test4.xml";
        String imgName = "Test4";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        int id = 10;
        boolean isInBck = false;
        boolean isOpaque = true;
        Group actualReturn = (Group) drawingPanel.getElem(id);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 4, actualReturn.getNumVertex());
        assertEquals("return value", ArianneUtil.Shapes.NO_COLOR, actualReturn.getShapeBorderColorRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", 0xFF, actualReturn.getOverlay());
        assertEquals("return value", true, actualReturn.isPolling());
        assertEquals("return value", 500, actualReturn.getPollInterval());
        assertEquals("return value", 500, actualReturn.getBckCycle());
    }

    public void testLoadIconShape() {
        String path = ".\\test\\ArianneViewer\\Test4.xml";
        String imgName = "Test4";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        int id = 11;
        boolean isInBck = false;
        boolean isOpaque = true;
        String pth = ".\\images\\GLOBES_BLUE.png";
        String defPth = ".\\images\\GLOBES_BLACK.png";
        IconShape actualReturn = (IconShape) drawingPanel.getElem(id);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 4, actualReturn.getNumVertex());
        assertEquals("return value", -16777216, actualReturn.getShapeBorderColorRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", 0xFF, actualReturn.getOverlay());
        assertEquals("return value", true, actualReturn.isPolling());
        assertEquals("return value", 500, actualReturn.getPollInterval());
        assertEquals("return value", 500, actualReturn.getBckCycle());
        assertEquals("return value", pth, actualReturn.getPath());
        assertEquals("return value", defPth, actualReturn.getDefaultPath());
    }

    public void testLoadImage() {
        String path = ".\\test\\ArianneViewer\\Test3.xml";
        String imgName = "Test3";
        ArianneUtil.Util.preLoadImage(path, imgName, drawingPanel.memDb);
        drawingPanel.directLoadImg(imgName);
        assertEquals("return value", 2, drawingPanel.getList().size());
    }

    public void testLoadLineShape() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Line',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        int pollIntervall = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        LineShape actualReturn = (LineShape) drawingPanel.loadLineShape(id, "Line", 2, imgName, 1, "Continuous", 14, 10, true, true, sColor, isOpaque, isInBck, ovl, polling, pollIntervall, bckCycle);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 100, actualReturn.getXCoordinates()[0]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[0]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[1]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[1]);
        assertEquals("return value", sColor, actualReturn.getShapeBorderColor().getRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", ovl, actualReturn.getOverlay());
        assertEquals("return value", polling, actualReturn.isPolling());
        assertEquals("return value", pollIntervall, actualReturn.getPollInterval());
        assertEquals("return value", bckCycle, actualReturn.getBckCycle());
    }

    public void testLoadOvalShape() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Oval',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,3,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,4,100,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = 10000;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        int pollInterval = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        Shapes actualReturn = drawingPanel.loadOvalShape(id, imgName, true, 1, "Countinuous", 1.0f, sColor, fColor, isOpaque, isInBck, ovl, polling, pollInterval, bckCycle);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 100, actualReturn.getXCoordinates()[0]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[0]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[1]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[1]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[2]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[2]);
        assertEquals("return value", 100, actualReturn.getXCoordinates()[3]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[3]);
        assertEquals("return value", sColor, actualReturn.getShapeBorderColor().getRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", ovl, actualReturn.getOverlay());
        assertEquals("return value", polling, actualReturn.isPolling());
        assertEquals("return value", pollInterval, actualReturn.getPollInterval());
        assertEquals("return value", bckCycle, actualReturn.getBckCycle());
    }

    public void testLoadPolygonShape() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Rectangle',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,3,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,4,100,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String type = "Rectangle";
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = 10000;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        boolean is3d = false;
        int nVertex = 4;
        int pollInterval = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        Shapes actualReturn = drawingPanel.loadPolygonShape(id, imgName, nVertex, 1, "Countinuous", 1.0f, sColor, fColor, type, isOpaque, isInBck, ovl, polling, pollInterval, bckCycle, is3d);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", 100, actualReturn.getXCoordinates()[0]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[0]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[1]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[1]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[2]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[2]);
        assertEquals("return value", 100, actualReturn.getXCoordinates()[3]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[3]);
        assertEquals("return value", sColor, actualReturn.getShapeBorderColor().getRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", ovl, actualReturn.getOverlay());
        assertEquals("return value", polling, actualReturn.isPolling());
        assertEquals("return value", pollInterval, actualReturn.getPollInterval());
        assertEquals("return value", bckCycle, actualReturn.getBckCycle());
    }

    public void testLoadSliderShape() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Slider',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,3,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,4,100,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String type = "Rectangle";
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = 10000;
        int nVertex = 4;
        int pollInterval = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        boolean showScUp = true;
        boolean showSubScUp = true;
        boolean showValUp = true;
        boolean showScDown = true;
        boolean showSubScDown = true;
        boolean showValDown = true;
        double cMin = 10.0;
        double cMax = 120.0;
        int nTicks = 5;
        String st = "Normal";
        SliderShape actualReturn = (SliderShape) drawingPanel.loadSliderShape(id, st, imgName, nVertex, 1, "Countinuous", 1.0f, sColor, fColor, cMin, cMax, nTicks, "Courier", Font.PLAIN, 10, -65536, false, showScUp, showSubScUp, showValUp, showScDown, showSubScDown, showValDown, type, isOpaque, isInBck, ovl, polling, pollInterval, bckCycle);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", cMin, actualReturn.getMinSlider());
        assertEquals("return value", cMax, actualReturn.getMaxSlider());
        assertEquals("return value", 100, actualReturn.getXCoordinates()[0]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[0]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[1]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[1]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[2]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[2]);
        assertEquals("return value", 100, actualReturn.getXCoordinates()[3]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[3]);
        assertEquals("return value", sColor, actualReturn.getShapeBorderColor().getRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", ovl, actualReturn.getOverlay());
        assertEquals("return value", polling, actualReturn.isPolling());
        assertEquals("return value", pollInterval, actualReturn.getPollInterval());
        assertEquals("return value", bckCycle, actualReturn.getBckCycle());
    }

    public void testLoadTabularShape() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Tabular',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,3,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,4,100,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = 10000;
        int gColor = ArianneUtil.Shapes.NO_COLOR;
        int tColor = ArianneUtil.Shapes.NO_COLOR;
        int stColor = ArianneUtil.Shapes.NO_COLOR, sbColor = ArianneUtil.Shapes.NO_COLOR, ftColor = ArianneUtil.Shapes.NO_COLOR, fbColor = ArianneUtil.Shapes.NO_COLOR;
        Color[] bgColColors = { Color.yellow, Color.cyan, Color.gray }, fgColColors = { Color.green, Color.red, Color.green };
        int[] colAlignment = null;
        int nVertex = 4;
        int pollInterval = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        boolean dh = true;
        boolean dr = true;
        boolean svg = true;
        boolean shg = true;
        String fName = "Courier";
        int fStyle = Font.ITALIC;
        int fSize = 12;
        int cWidth = 10;
        int rHeight = 20;
        int rMargin = 10;
        int ispw = 0;
        int isph = 0;
        int bWidth = 10;
        int maxFracDigit = 2;
        int minFracDigit = 1;
        String obr = null;
        String ebr = null;
        String ofr = null;
        String efr = null;
        int arm = JTable.AUTO_RESIZE_ALL_COLUMNS;
        TabularShape actualReturn = (TabularShape) drawingPanel.loadTabularShape(id, imgName, sColor, gColor, fColor, tColor, stColor, sbColor, ftColor, fbColor, fName, fStyle, fSize, cWidth, rHeight, rMargin, ispw, isph, bWidth, obr, ebr, ofr, efr, bgColColors, fgColColors, colAlignment, dh, dr, svg, shg, maxFracDigit, minFracDigit, arm, ovl, polling, pollInterval, bckCycle);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", maxFracDigit, actualReturn.getMaxFracDigit());
        assertEquals("return value", minFracDigit, actualReturn.getMinFracDigit());
        assertEquals("return value", fName, actualReturn.getFont().getName());
        assertEquals("return value", fStyle, actualReturn.getFont().getStyle());
        assertEquals("return value", fSize, actualReturn.getFont().getSize());
        assertEquals("return value", dh, actualReturn.getDisplayHeader());
        assertEquals("return value", svg, actualReturn.showVerticalGrid());
        assertEquals("return value", svg, actualReturn.showHorizontalGrid());
        assertEquals("return value", bgColColors[0], actualReturn.getBackColColors()[0]);
        assertEquals("return value", bgColColors[1], actualReturn.getBackColColors()[1]);
        assertEquals("return value", fgColColors[0], actualReturn.getFgColColors()[0]);
        assertEquals("return value", fgColColors[1], actualReturn.getFgColColors()[1]);
        assertEquals("return value", 100, actualReturn.getXCoordinates()[0]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[0]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[1]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[1]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[2]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[2]);
        assertEquals("return value", 100, actualReturn.getXCoordinates()[3]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[3]);
        assertEquals("return value", sColor, actualReturn.getShapeBorderColor().getRGB());
        assertEquals("return value", actualReturn.getShapeFillColorRGB());
        assertEquals("return value", (new Color(gColor)).getRGB(), actualReturn.getGridColor().getRGB());
        assertEquals("return value", (new Color(tColor)).getRGB(), actualReturn.getTextColor().getRGB());
        assertEquals("return value", (new Color(stColor)).getRGB(), actualReturn.getSelectedTextColor().getRGB());
        assertEquals("return value", (new Color(sbColor)).getRGB(), actualReturn.getSelectedBackColor().getRGB());
        assertEquals("return value", (new Color(fbColor)).getRGB(), actualReturn.getFocusBackColorRGB());
        assertEquals("return value", (new Color(ftColor)).getRGB(), actualReturn.getFocusTextColorRGB());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", ovl, actualReturn.getOverlay());
        assertEquals("return value", polling, actualReturn.isPolling());
        assertEquals("return value", pollInterval, actualReturn.getPollInterval());
        assertEquals("return value", bckCycle, actualReturn.getBckCycle());
    }

    public void testLoadTextShape() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Tabular',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,3,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,4,100,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        int fColor = 10000;
        int tColor = ArianneUtil.Shapes.NO_COLOR;
        int pollInterval = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        boolean is3d = false;
        String fName = "Courier";
        int fStyle = Font.ITALIC;
        int fSize = 14;
        int tHAlignment = Align.RIGHT.value();
        int tVAlignment = Align.VCENTER.value();
        String textVal = "Prova";
        String precision = "##.##";
        TextShape actualReturn = (TextShape) drawingPanel.loadTextShape(id, imgName, textVal, 1, "Countinuous", 1.0f, sColor, fColor, tColor, tHAlignment, tVAlignment, fName, fStyle, fSize, 0.0, precision, isOpaque, isInBck, ovl, polling, pollInterval, bckCycle, is3d);
        assertEquals("return value", id, actualReturn.getElemId());
        assertEquals("return value", imgName, actualReturn.getImgName());
        assertEquals("return value", fName, actualReturn.getFont().getName());
        assertEquals("return value", fStyle, actualReturn.getFont().getStyle());
        assertEquals("return value", fSize, actualReturn.getFont().getSize());
        assertEquals("return value", 100, actualReturn.getXCoordinates()[0]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[0]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[1]);
        assertEquals("return value", 105, actualReturn.getYCoordinates()[1]);
        assertEquals("return value", 200, actualReturn.getXCoordinates()[2]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[2]);
        assertEquals("return value", 100, actualReturn.getXCoordinates()[3]);
        assertEquals("return value", 205, actualReturn.getYCoordinates()[3]);
        assertEquals("return value", sColor, actualReturn.getShapeBorderColor().getRGB());
        assertEquals("return value", (new Color(fColor)).getRGB(), actualReturn.getShapeFillColorRGB());
        assertEquals("return value", (new Color(tColor)).getRGB(), actualReturn.getTextColor().getRGB());
        assertEquals("return value", tHAlignment, actualReturn.getHAlignment());
        assertEquals("return value", tVAlignment, actualReturn.getVAlignment());
        assertEquals("return value", true, actualReturn.getTextWidth() > 0);
        assertEquals("return value", true, actualReturn.getTextHeight() > 0);
        assertEquals("return value", precision, actualReturn.getFormatter());
        assertEquals("return value", textVal, actualReturn.getTextVal());
        assertEquals("return value", isOpaque, actualReturn.isOpaque());
        assertEquals("return value", isInBck, actualReturn.isInBackground());
        assertEquals("return value", ovl, actualReturn.getOverlay());
        assertEquals("return value", polling, actualReturn.isPolling());
        assertEquals("return value", pollInterval, actualReturn.getPollInterval());
        assertEquals("return value", bckCycle, actualReturn.getBckCycle());
    }

    public void testPaint() {
        drawingPanel.paint(drawingPanel.getGraphics());
    }

    public void testPaintBckCanvas() {
        drawingPanel.paintBckCanvas((Graphics2D) drawingPanel.getGraphics());
    }

    public void testPrintImg() {
    }

    public void testResetAll() {
        ArianneUtil.Util.dropMemDb(drawingPanel.memDb);
        ArianneUtil.Util.createMemDb(drawingPanel.memDb);
        try {
            String q1 = "INSERT INTO SHAPE (IMAGE_NAME,ID,TYPE,FATHER_SHAPE_ID,NUMVERTEX,REFRESHPERIOD,DBCONNECTION_ID,SEQ) VALUES ";
            String q2 = "('Test',1,'Line',NULL,2,0,0,1)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            ArianneUtil.Util.dumpTableSHAPE(drawingPanel.memDb);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,1,100,105)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
            q1 = "INSERT INTO POINT (IMAGE_NAME,SHAPE_ID,POINT_ID,X,Y) VALUES ";
            q2 = "('Test',1,2,200,205)";
            drawingPanel.memDb.getConnection().createStatement().executeUpdate(q1 + q2);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        int id = 1;
        String imgName = "Test";
        int sColor = ArianneUtil.Shapes.NO_COLOR;
        boolean isOpaque = true;
        boolean isInBck = false;
        boolean polling = true;
        int pollIntervall = 333;
        int bckCycle = 1111;
        int ovl = 0xFF;
        LineShape line = (LineShape) drawingPanel.loadLineShape(id, "Line", 2, imgName, 1, "Continuous", 14, 10, true, true, sColor, isOpaque, isInBck, ovl, polling, pollIntervall, bckCycle);
        drawingPanel.addShape(line);
        drawingPanel.resetAll();
        assertEquals("return value", 0, drawingPanel.getList().size());
    }

    public void testSetAntialiasing() {
        boolean v = false;
        drawingPanel.setAntialiasing(v);
        assertEquals("return value", v, drawingPanel.getAntialiasing());
        v = true;
        drawingPanel.setAntialiasing(v);
        assertEquals("return value", v, drawingPanel.getAntialiasing());
    }

    public void testSetConnectString() {
        String cs = "Prova connect string";
        drawingPanel.setConnectString(cs);
        assertEquals("return value", cs, drawingPanel.getConnectString());
    }

    public void testSetFatherFrame() {
        PictureViewer f = null;
        drawingPanel.setFatherFrame(f);
    }

    public void testSetImageName() {
        String imgN = "Test";
        drawingPanel.setImageName(imgN);
        assertEquals("return value", imgN, drawingPanel.getImgName());
    }

    public void testSetLocalDb() {
        Database db = null;
        drawingPanel.setLocalDb(db);
    }

    public void testSetTimer() {
        drawingPanel.setTimer();
        assertEquals("return value", true, (drawingPanel.timer != null && !drawingPanel.timer.isRunning()));
    }

    public void testStartPrinting() {
        drawingPanel.startPrinting();
    }

    public void testThis_componentShown() {
        ComponentEvent e = null;
        drawingPanel.this_componentShown(e);
    }

    public void testThis_mouseClicked() {
        MouseEvent e = null;
        drawingPanel.this_mouseClicked(e);
    }

    public void testDrawingPanel() {
        PictureViewer f = null;
        int W = 0;
        int H = 0;
        drawingPanel = new DrawingPanel(f, W, H, false);
    }

    public PictureViewer createPictureViewer() {
        LogHandler Log = new LogHandler(Level.ALL);
        PictureViewer frame = new PictureViewer(false);
        boolean packFrame = false;
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        return frame;
    }

    public void pressGoButton(PictureViewer f) {
        f.setAlwaysOnTop(true);
        pressToggleButton(f.timerToggleButton);
    }

    public void pressToggleButton(JToggleButton tb) {
        try {
            Robot robot = new Robot();
            Point p = tb.getLocationOnScreen();
            robot.mouseMove((int) p.getX() + (int) Math.round(tb.getSize(null).getWidth() / 2), (int) p.getY() + (int) Math.round(tb.getSize(null).getHeight() / 2));
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.delay(1000);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException awtEx) {
            awtEx.printStackTrace();
        }
    }
}
