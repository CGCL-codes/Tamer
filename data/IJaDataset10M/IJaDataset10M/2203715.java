package wii.edu.web.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import wii.edu.core.dao.DosenDAO;
import wii.edu.core.dao.ObjectDAO;
import wii.edu.core.dao.DistrikDAO;
import wii.edu.core.dao.JadwalDAO;
import wii.edu.core.model.Dosen;
import wii.edu.core.model.Distrik;
import wii.edu.core.model.User;

/**
 *
 * @author Retha@wii
 */
public class DosenJSON extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession sesi = request.getSession();
            if (sesi.getAttribute("role") == null) {
                request.getRequestDispatcher("page/index.jsp").forward(request, response);
                return;
            } else {
                String role = (String) sesi.getAttribute("role");
                if (role.equals("user")) {
                    User user = (User) sesi.getAttribute("currentUser");
                    if (user.getLevel() != 1) {
                        response.sendRedirect("logout");
                        return;
                    }
                } else {
                    response.sendRedirect("logout");
                    return;
                }
            }
            String output = "";
            String task = request.getParameter("task");
            if (task.equals("DISTRIK")) output = new DistrikDAO().getAllDistrikJSONObject().toString(); else if (task.equals("LOGOUT")) {
                String jsonIds = request.getParameter("ids");
                System.out.println(jsonIds);
                Object ids = JSONValue.parse(jsonIds);
                JSONArray array = (JSONArray) ids;
                for (int a = 0; a < array.size(); a++) {
                    System.out.println(array.get(a));
                    Dosen dosen = new DosenDAO().getDosen((Long) array.get(a));
                    new DosenDAO().logout(dosen);
                }
                output = "1";
            } else if (task.equals("LISTING_LIMIT")) {
                int start = Integer.parseInt(request.getParameter("start"));
                int limit = Integer.parseInt(request.getParameter("limit"));
                output = new DosenDAO().getAllDosenJSONObject(start, limit).toString();
            } else if (task.equals("DELETE")) {
                String jsonIds = request.getParameter("ids");
                System.out.println(jsonIds);
                Object ids = JSONValue.parse(jsonIds);
                JSONArray array = (JSONArray) ids;
                int ind = 0;
                for (int a = 0; a < array.size(); a++) {
                    System.out.println(array.get(a));
                    Dosen dos = new DosenDAO().getDosen((Long) array.get(a));
                    List listJadwal = new JadwalDAO().getJadwal(dos);
                    if ((listJadwal == null || listJadwal.size() <= 0) && dos.getIsLogin() != 1) {
                        new ObjectDAO().delete(dos);
                        ind++;
                    }
                }
                if (ind == 0) {
                    output = "0";
                } else if (ind == array.size()) {
                    output = "1";
                } else if (ind < array.size()) {
                    output = "-1";
                }
            } else if (task.equals("UPDATE")) {
                Long id = Long.parseLong(request.getParameter("id"));
                String username = request.getParameter("userName");
                DosenDAO dao = new DosenDAO();
                if (dao.nopegIsUsed(username) && dao.getDosen(username).getId() != id) {
                    output = "-1";
                } else {
                    String password = request.getParameter("password");
                    String nama = request.getParameter("nama");
                    String alamat = request.getParameter("alamat");
                    String subDistrik = request.getParameter("subDistrik");
                    Long distrik = Long.parseLong(request.getParameter("distrik"));
                    String kodepos = request.getParameter("kodepos");
                    String telpon = request.getParameter("telpon");
                    String handphone = request.getParameter("handphone");
                    String email = request.getParameter("email");
                    int jenisKelamin = Integer.parseInt(request.getParameter("jenisKelamin"));
                    String pendidikanTerakhir = request.getParameter("pendidikanTerakhir");
                    Dosen dos = new DosenDAO().getDosen(id);
                    dos.setNomorPegawai(username);
                    dos.setPassword(password);
                    dos.setNama(nama);
                    dos.setJalan(alamat);
                    dos.setSubDistrik(subDistrik);
                    Distrik prov = new DistrikDAO().getDistrik(distrik);
                    dos.setDistrik(prov);
                    dos.setKodePos(kodepos);
                    dos.setTelepon(telpon);
                    dos.setHandphone(handphone);
                    dos.setEmail(email);
                    dos.setJenisKelamin(jenisKelamin);
                    dos.setPendidikanTerakhir(pendidikanTerakhir);
                    output = Integer.toString(new ObjectDAO().update(dos));
                }
            } else if (task.equals("CREATE")) {
                String username = request.getParameter("userName");
                if (new DosenDAO().nopegIsUsed(username)) {
                    output = "-1";
                } else {
                    String password = request.getParameter("password");
                    String nama = request.getParameter("nama");
                    String alamat = request.getParameter("alamat");
                    String subDistrik = request.getParameter("subDistrik");
                    Long distrik = Long.parseLong(request.getParameter("distrik"));
                    String kodepos = request.getParameter("kodepos");
                    String telpon = request.getParameter("telpon");
                    String handphone = request.getParameter("handphone");
                    String email = request.getParameter("email");
                    int jenisKelamin = Integer.parseInt(request.getParameter("jenisKelamin"));
                    String pendidikanTerakhir = request.getParameter("pendidikanTerakhir");
                    Dosen dos = new Dosen();
                    dos.setNomorPegawai(username);
                    dos.setPassword(password);
                    dos.setNama(nama);
                    dos.setJalan(alamat);
                    dos.setSubDistrik(subDistrik);
                    Distrik prov = new DistrikDAO().getDistrik(distrik);
                    dos.setDistrik(prov);
                    dos.setKodePos(kodepos);
                    dos.setTelepon(telpon);
                    dos.setHandphone(handphone);
                    dos.setEmail(email);
                    dos.setJenisKelamin(jenisKelamin);
                    dos.setPendidikanTerakhir(pendidikanTerakhir);
                    output = Integer.toString(new ObjectDAO().insert(dos));
                }
            }
            System.out.println(output);
            out.println(output);
        } catch (Exception ex) {
            Logger.getLogger(DosenJSON.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
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
        return "Short description";
    }
}
