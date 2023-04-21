package gui;

import anon.Anonymisation;
import data.DataManager;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GuiStep4 extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session;
        try {
            session = request.getSession();
            Object done = session.getAttribute("logon.isDone");
            StateBean state_bean = (StateBean) session.getAttribute("statebean");
            GuiBean guiBean = (GuiBean) session.getAttribute("guibean");
            if ((done == null) || (state_bean == null) || (guiBean == null)) {
                response.sendRedirect("/open_anonymizer/");
                return;
            }
            if (state_bean.getCounter() == null) {
                guiBean.setErrorMsg("Please choose at least one selection criteria");
                String url = "/guiStep3.jsp";
                RequestDispatcher dispatch = request.getRequestDispatcher(url);
                dispatch.forward(request, response);
                return;
            }
            int cnt = Integer.parseInt(state_bean.getCounter());
            if (cnt == 3) {
                guiBean.setErrorMsg("Please select at least 3 cases");
                String url = "/guiStep3.jsp";
                RequestDispatcher dispatch = request.getRequestDispatcher(url);
                dispatch.forward(request, response);
                return;
            }
            String reset = null;
            boolean not_reset = true;
            boolean not_back = true;
            if (((reset = request.getParameter("reset")) != null) && (reset.equals("true"))) {
                state_bean.remove_property_group("limit");
                not_reset = false;
            }
            if ((request.getParameter("back") != null) && (request.getParameter("back").equals("true"))) {
                not_back = false;
            }
            MiddleWare middleWare = MiddleWare.getInstance();
            guiBean.setErrorMsg(null);
            if ((not_reset) && (not_back)) {
                String[] selectedDataSources = guiBean.getSelectedDataSources();
                Anonymisation anon_instance = Anonymisation.getInstance();
                HashMap attribute_table = guiBean.getSelectedAttributes();
                for (int i = 0; i < selectedDataSources.length; ++i) {
                    Attribute[] attrs = (Attribute[]) attribute_table.get(selectedDataSources[i]);
                    for (int j = 0; j < attrs.length; ++j) {
                        int limit = attrs[j].getDimension();
                        attrs[j].initialise_hierarchy();
                        for (int k = 1; k < limit - 1; ++k) {
                            String[] res = preprocess_hierarchy(anon_instance.getDataManager().get_values_of_attribute(attrs[j].getSqlName(), k));
                            attrs[j].add_hierarchy_level(k, res);
                        }
                    }
                }
            }
            String url = "/guiStep4.jsp";
            RequestDispatcher dispatch = request.getRequestDispatcher(url);
            dispatch.forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServletException(ex.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }

    protected String[] preprocess_hierarchy(String[] hrch) {
        String[] result = null;
        int limit = -1;
        if (hrch.length > 11) {
            limit = 10;
            result = new String[limit + 1];
            result[limit] = "...";
        } else {
            int size = -1;
            if (hrch[0].indexOf("null") != -1) size = hrch.length - 1; else size = hrch.length;
            result = new String[size];
            limit = hrch.length;
        }
        int pos = 0;
        for (int i = 0; i < limit; ++i) {
            if (hrch[i].indexOf("null") == -1) {
                result[pos] = hrch[i];
                ++pos;
            }
        }
        return result;
    }
}
