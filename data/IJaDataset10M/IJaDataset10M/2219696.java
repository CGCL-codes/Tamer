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
import wii.edu.core.dao.FakultasDAO;
import wii.edu.core.dao.MahasiswaDAO;
import wii.edu.core.dao.MatakuliahDAO;
import wii.edu.core.dao.ObjectDAO;
import wii.edu.core.dao.ProgramStudiDAO;
import wii.edu.core.model.Fakultas;
import wii.edu.core.model.User;

/**
 *
 * @author Retha@wii
 */
public class FakultasJSON extends HttpServlet {

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
            if (task.equals("LISTING")) output = new FakultasDAO().getAllFakultasJSONObject().toString(); else if (task.equals("DELETE")) {
                String jsonIds = request.getParameter("ids");
                System.out.println(jsonIds);
                Object ids = JSONValue.parse(jsonIds);
                JSONArray array = (JSONArray) ids;
                int ind = 0;
                for (int a = 0; a < array.size(); a++) {
                    System.out.println(array.get(a));
                    Fakultas fak = new FakultasDAO().getFakultas((Long) array.get(a));
                    List listMhs = new MahasiswaDAO().getSemuaMahasiswa(fak);
                    List listMatkul = new MatakuliahDAO().getAllMatakuliah(fak);
                    List listProgdi = new ProgramStudiDAO().getAllProgramStudi(fak);
                    if ((listMhs == null || listMhs.size() <= 0) && (listMatkul == null || listMatkul.size() <= 0) && (listProgdi == null || listProgdi.size() <= 0) && (Long) array.get(a) != 1) {
                        new ObjectDAO().delete(fak);
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
                String kode = request.getParameter("kode");
                FakultasDAO dao = new FakultasDAO();
                if (dao.kodeIsUsed(kode) && dao.getFakultas(kode).getId() != id) {
                    output = "-1";
                } else {
                    String nama = request.getParameter("nama");
                    Fakultas fak = new Fakultas();
                    fak.setId(id);
                    fak.setKode(kode);
                    fak.setNama(nama);
                    output = Integer.toString(new ObjectDAO().update(fak));
                }
            } else if (task.equals("CREATE")) {
                String kode = request.getParameter("kode");
                if (new FakultasDAO().kodeIsUsed(kode)) {
                    output = "-1";
                } else {
                    String nama = request.getParameter("nama");
                    Fakultas fak = new Fakultas();
                    fak.setKode(kode);
                    fak.setNama(nama);
                    output = Integer.toString(new ObjectDAO().insert(fak));
                }
            }
            System.out.println(output);
            out.println(output);
        } catch (Exception ex) {
            Logger.getLogger(FakultasJSON.class.getName()).log(Level.SEVERE, null, ex);
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
