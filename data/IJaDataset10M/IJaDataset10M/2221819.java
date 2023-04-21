package pl.wat.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import pl.wat.dp.DBManager;
import pl.wat.dp.entities.DefWykresu;

/**
 *
 * @author Marcin
 */
public class ChartServlet extends HttpServlet {

    private DBManager dbm;

    private EntityManager em;

    private DefToChart defToChart;

    /**
     * Inicjalizuje servlet s�
     * @param config
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);
            dbm = DBManager.getInstance();
            em = dbm.getEm();
            String dbHost = config.getInitParameter("dbHost");
            String dbPort = config.getInitParameter("dbPort");
            String dbName = config.getInitParameter("dbName");
            String dbUser = config.getInitParameter("dbUser");
            String dbPass = config.getInitParameter("dbPass");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (java.lang.ClassNotFoundException e) {
                e.printStackTrace();
                Logger.getLogger(ChartServlet.class.getName()).log(Level.SEVERE, e.getMessage());
            }
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
            defToChart = new DefToChart(connection);
        } catch (SQLException ex) {
            Logger.getLogger(ChartServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * W odpowiedzi na ��danie zapisuje do strumienia wyj�ciowego wykres w postaci pliku PNG
     *
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        DefWykresu defWykresu;
        JFreeChart chart = null;
        if (id != null) {
            defWykresu = em.find(DefWykresu.class, new Integer(id));
            if (defWykresu != null) {
                chart = defToChart.getChart(defWykresu);
                File image = File.createTempFile("image", "tmp");
                ChartUtilities.saveChartAsPNG(image, chart, 500, 300);
                FileInputStream fileInStream = new FileInputStream(image);
                OutputStream outStream = response.getOutputStream();
                long fileLength;
                byte[] byteStream;
                fileLength = image.length();
                byteStream = new byte[(int) fileLength];
                fileInStream.read(byteStream, 0, (int) fileLength);
                response.setContentType("image/png");
                response.setContentLength((int) fileLength);
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "no-cache");
                fileInStream.close();
                outStream.write(byteStream);
                outStream.flush();
                outStream.close();
            }
        }
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "W odpowiedzi na ��danie zapisuje do strumienia wyj�ciowego wykres w postaci pliku PNG";
    }
}
