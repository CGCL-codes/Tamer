package org.netbeans.web;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.netbeans.modules.exceptions.entity.Submit;
import org.netbeans.server.snapshots.SnapshotManager;

/**
 *
 * @author Jindrich Sedek
 */
public class NPSLog extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final Integer id = new Integer(request.getParameter("id"));
        if (id == null) {
            return;
        }
        Utils.processPersistable(new Persistable.Query() {

            public TransactionResult runQuery(EntityManager em) throws IOException {
                Submit sb = Submit.getById(em, id);
                if (sb == null) {
                    return TransactionResult.NONE;
                }
                SnapshotManager sm = SnapshotManager.loadSnapshot(sb.getLogfileId());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);
                sm.save(dos);
                dos.close();
                String additionalS = "";
                if (sm.hasNpssContent()) {
                    additionalS = "s";
                }
                response.setContentType("application/x-nps" + additionalS);
                String fileName = String.format("snapshot-%1$s.nps", id.toString());
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName + additionalS);
                response.setContentLength(bos.size());
                response.getOutputStream().write(bos.toByteArray());
                return TransactionResult.NONE;
            }
        });
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
