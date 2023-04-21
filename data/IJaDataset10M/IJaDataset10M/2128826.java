package serv;

import entity.Emprestimo;
import entity.Item;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author bruno
 */
public class Devolver extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        assert emf != null;
        EntityManager em = null;
        try {
            utx.begin();
            em = emf.createEntityManager();
            List<Emprestimo> emprestimo = em.createQuery("select p from Emprestimo p where p.id =" + request.getParameter("empId")).getResultList();
            emprestimo.iterator().next().setDatein(new Date().toString());
            List<Item> item = em.createQuery("select p.item from Emprestimo p where p.id =" + request.getParameter("empId")).getResultList();
            item.iterator().next().setEstado("Disponivel");
            em.persist(item.iterator().next());
            em.persist(emprestimo.iterator().next());
            utx.commit();
            request.setAttribute("header", "Item Devolvido com Sucesso!");
            request.setAttribute("id", item.iterator().next().getId());
            request.setAttribute("title", item.iterator().next().getTitle());
            request.getRequestDispatcher("ListaItens.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            if (em != null) {
                em.close();
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
        return "Short description";
    }
}
