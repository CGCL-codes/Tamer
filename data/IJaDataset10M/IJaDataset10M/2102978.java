package servlets;

import beans.Menu;
import engine.model.AbstractDataProvider;
import engine.model.AbstractFacade;
import engine.model.MetaRow;
import engine.model.DataModel;
import engine.mvc.Messages.Pair;
import engine.mvc.RequestMaster;
import engine.mvc.Res;
import engine.mvc.Res.UI;
import engine.view.Html;
import engine.view.TableView;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dosER
 */
@WebServlet(name = "View", urlPatterns = { "/View" })
public class ViewServlet extends HttpServlet {

    AbstractDataProvider provider = AbstractDataProvider.create();

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestMaster master = new RequestMaster(request, response);
        String context = request.getContextPath();
        if (!master.popPostback()) {
            master.dispatch(Res.Url.MAIN);
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String table = master.getParam(Res.Params.TABLE).toString();
            AbstractFacade facade = provider.getFacade(table);
            DataModel model = new DataModel(facade.findAll(), facade.build().getClass());
            String sorts = master.getParam("sort").toString();
            String search = master.getParam("search").toString();
            model.sort(sorts).filter(search);
            TableView view = new TableView(model, context);
            MetaRow meta = new MetaRow(facade.build().getClass());
            if (!master.getParam(Res.Params.TABLE).equals(meta.table())) {
                master.message(UI.TABLE_NOT_FOUND);
            }
            out.println(Html.header(UI.MAIN_TITLE));
            String s = "";
            s += "<h1>" + meta.visibleName() + "</h1><br/>";
            s += "<form action='controller' method='get'>" + "<input type='text' name='search' />&nbsp;" + "<input type='submit' value='" + UI.SEARCH_LINK + "'/>" + "<input type='hidden' name='table' value='" + table + "' />&nbsp;" + "<input type='hidden' name='command' value='view' />&nbsp;" + "</form>";
            Pair[] msgs = master.messages().pop();
            for (Pair p : msgs) {
                out.println("<div class='message'> " + Html.message(p.message) + "</div> <br />");
            }
            s += view.print();
            s += "<br/>";
            s += Html.Link.create("controller", UI.ADD_LINK).command("add").table(table).setClass("btn").build();
            out.println(Html.content(Menu.menu(), s));
            out.println(Html.footer());
        } catch (Exception e) {
            if (out != null) {
                out.print(Html.error(UI.EXCEPTION_TITLE + e.getMessage()));
            }
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
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
