package wii.edu.web.akademik;

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
import wii.edu.core.dao.FakultasDAO;
import wii.edu.core.dao.JadwalDAO;
import wii.edu.core.dao.MatakuliahDAO;
import wii.edu.core.dao.ObjectDAO;
import wii.edu.core.dao.ProgramStudiDAO;
import wii.edu.core.dao.RegistrasiMatakuliahDAO;
import wii.edu.core.dao.RuangDAO;
import wii.edu.core.dao.SemesterDAO;
import wii.edu.core.model.Dosen;
import wii.edu.core.model.Fakultas;
import wii.edu.core.model.Jadwal;
import wii.edu.core.model.Matakuliah;
import wii.edu.core.model.ProgramStudi;
import wii.edu.core.model.Ruang;
import wii.edu.core.model.Semester;
import wii.edu.core.model.User;

/**
 *
 * @author Retha@wii
 */
public class JadwalJSON extends HttpServlet {

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
                    if (user.getLevel() != 2) {
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
            Semester semester = new SemesterDAO().getCurrentSemester();
            if (task.equals("FAKULTAS")) output = new FakultasDAO().getAllFakultasJSONObject().toString(); else if (task.equals("PROGDI")) {
                String strFak = request.getParameter("fakultas");
                System.out.println("strFak : " + strFak);
                if (strFak == null || strFak.equals("")) {
                    output = new ProgramStudiDAO().getAllProgramStudiJSONObject().toString();
                } else {
                    long idFakultas = Long.parseLong(strFak);
                    Fakultas fak = new FakultasDAO().getFakultas(idFakultas);
                    output = new ProgramStudiDAO().getAllProgramStudiPlusJSONObject(fak).toString();
                }
            } else if (task.equals("MATAKULIAH_BY_FAKULTAS")) {
                if (request.getParameter("fakultas") == null || request.getParameter("fakultas").equals("")) {
                    output = new MatakuliahDAO().getAllMatakuliahMinusJSONObject().toString();
                } else {
                    long idFakultas = Long.parseLong(request.getParameter("fakultas"));
                    Fakultas fak = new FakultasDAO().getFakultas(idFakultas);
                    output = new MatakuliahDAO().getAllMatakuliahMinusJSONObject(fak).toString();
                }
            } else if (task.equals("DOSEN")) output = new DosenDAO().getAllDosenJSONObject().toString(); else if (task.equals("RUANG")) output = new RuangDAO().getAllRuangJSONObject().toString(); else if (task.equals("CURRENT")) {
                output = "Semester " + semester.getNama() + " Tahun Ajaran " + semester.getTahunAjaran();
            } else if (task.equals("LISTING_BY_FAKULTAS_LIMIT")) {
                int start = Integer.parseInt(request.getParameter("start"));
                int limit = Integer.parseInt(request.getParameter("limit"));
                if ((request.getParameter("fakultas") == null || request.getParameter("fakultas").equals("")) && (request.getParameter("progdi") == null || request.getParameter("progdi").equals(""))) {
                    output = new JadwalDAO().getJadwalJSONObject(semester, start, limit).toString();
                } else if (!(request.getParameter("fakultas") == null || request.getParameter("fakultas").equals("")) && (request.getParameter("progdi") == null || request.getParameter("progdi").equals(""))) {
                    long idFakultas = Long.parseLong(request.getParameter("fakultas"));
                    Fakultas fak = new FakultasDAO().getFakultas(idFakultas);
                    output = new JadwalDAO().getJadwalInFakultasJSONObject(semester, fak, start, limit).toString();
                } else if ((request.getParameter("fakultas") == null || request.getParameter("fakultas").equals("")) && !(request.getParameter("progdi") == null || request.getParameter("progdi").equals(""))) {
                    long idProgdi = Long.parseLong(request.getParameter("progdi"));
                    ProgramStudi prog = new ProgramStudiDAO().getProgramStudi(idProgdi);
                    Fakultas fak = prog.getFakultas();
                    output = new JadwalDAO().getJadwalInFakultasJSONObject(semester, fak, prog, start, limit).toString();
                } else {
                    long idFakultas = Long.parseLong(request.getParameter("fakultas"));
                    long idProgdi = Long.parseLong(request.getParameter("progdi"));
                    Fakultas fak = new FakultasDAO().getFakultas(idFakultas);
                    ProgramStudi prog = new ProgramStudiDAO().getProgramStudi(idProgdi);
                    output = new JadwalDAO().getJadwalInFakultasJSONObject(semester, fak, prog, start, limit).toString();
                }
            } else if (task.equals("DELETE")) {
                String jsonIds = request.getParameter("ids");
                System.out.println(jsonIds);
                Object ids = JSONValue.parse(jsonIds);
                JSONArray array = (JSONArray) ids;
                int ind = 0;
                for (int a = 0; a < array.size(); a++) {
                    System.out.println(array.get(a));
                    Jadwal jadwal = new JadwalDAO().getJadwal((Long) array.get(a));
                    List listReg = new RegistrasiMatakuliahDAO().getRegistrasiMatakuliah(jadwal);
                    if (listReg == null || listReg.size() <= 0) {
                        new ObjectDAO().delete(jadwal);
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
                long id = Long.parseLong(request.getParameter("id"));
                long matkul = Long.parseLong(request.getParameter("matkul"));
                String kelas = request.getParameter("kelas");
                long dosen = Long.parseLong(request.getParameter("dosen"));
                String hari = request.getParameter("hari");
                String mulai = request.getParameter("mulai");
                String selesai = request.getParameter("selesai");
                long ruang = Long.parseLong(request.getParameter("ruang"));
                int kapasitas = Integer.parseInt(request.getParameter("kapasitas"));
                Jadwal jadwal = new Jadwal();
                Matakuliah mat = new MatakuliahDAO().getMatakuliah(matkul);
                Dosen dos = new DosenDAO().getDosen(dosen);
                Ruang rng = new RuangDAO().getRuang(ruang);
                jadwal.setId(id);
                jadwal.setMatakuliah(mat);
                jadwal.setAksara(kelas);
                jadwal.setDosen(dos);
                jadwal.setHari(hari);
                jadwal.setJamMulai(mulai);
                jadwal.setJamSelesai(selesai);
                jadwal.setRuang(rng);
                jadwal.setKapasitas(kapasitas);
                jadwal.setSemester(new SemesterDAO().getCurrentSemester());
                output = Integer.toString(new ObjectDAO().update(jadwal));
            } else if (task.equals("CREATE")) {
                long matkul = Long.parseLong(request.getParameter("matkul"));
                String kelas = request.getParameter("kelas");
                long dosen = Long.parseLong(request.getParameter("dosen"));
                String hari = request.getParameter("hari");
                String mulai = request.getParameter("mulai");
                String selesai = request.getParameter("selesai");
                long ruang = Long.parseLong(request.getParameter("ruang"));
                int kapasitas = Integer.parseInt(request.getParameter("kapasitas"));
                Jadwal jadwal = new Jadwal();
                Matakuliah mat = new MatakuliahDAO().getMatakuliah(matkul);
                Dosen dos = new DosenDAO().getDosen(dosen);
                Ruang rng = new RuangDAO().getRuang(ruang);
                jadwal.setMatakuliah(mat);
                jadwal.setAksara(kelas);
                jadwal.setDosen(dos);
                jadwal.setHari(hari);
                jadwal.setJamMulai(mulai);
                jadwal.setJamSelesai(selesai);
                jadwal.setRuang(rng);
                jadwal.setKapasitas(kapasitas);
                jadwal.setSemester(new SemesterDAO().getCurrentSemester());
                output = Integer.toString(new ObjectDAO().insert(jadwal));
            }
            System.out.println(output);
            out.println(output);
        } catch (Exception ex) {
            Logger.getLogger(JadwalJSON.class.getName()).log(Level.SEVERE, null, ex);
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
