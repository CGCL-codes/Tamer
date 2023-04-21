package org.jgentleframework.web.servlet;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jgentleframework.web.controlbean.ApplyRequest;
import org.jgentleframework.web.controlbean.InvokeActionController;
import org.jgentleframework.web.controlbean.ProcessValidate;
import org.jgentleframework.web.controlbean.RenderResponse;
import org.jgentleframework.web.controlbean.RestoreView;
import org.jgentleframework.web.controlbean.UpdateModel;

/**
 * Chịu trách nhiệm quản lý việc điều phối luồng xử lý request đến WebProvider,
 * điều hướng và đăng kí các handler cho việc xử lý request. Là thành phần trung
 * tâm nhận xử lý cũng như khởi tạo tất cả các tài nguyên hệ thống. Lõi xử lý
 * của {@link FrontDispatcher} chính là một JGentle Container, và các controller
 * bean được đăng kí xử lý bên trong.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Nov 20, 2007
 */
public class FrontDispatcher extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4544541761921903843L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        String configString = config.getInitParameter("config");
        String[] configClassesArray = configString.split(";");
        List<Class<?>> configClassList = new ArrayList<Class<?>>();
        for (String str : configClassesArray) {
            try {
                Class<?> clazz = Class.forName(str);
                configClassList.add(clazz);
            } catch (ClassNotFoundException e) {
                throw new ServletException(e.getMessage());
            }
        }
    }

    /**
	 * Chịu trách nhiệm khởi tạo chuỗi cung ứng xử lý request, handle
	 * request được chỉ định bởi webContext.
	 * 
	 * @param config
	 * @throws ServletException
	 */
    private void initChain(ServletConfig config) throws ServletException {
        RestoreView restoreView = new RestoreView("restoreView");
        ApplyRequest applyRequest = new ApplyRequest("applyRequest");
        ProcessValidate proccessValidate = new ProcessValidate("proccessValidate");
        UpdateModel updateModel = new UpdateModel("updateModel");
        InvokeActionController invokeAction = new InvokeActionController("invokeAction");
        RenderResponse renderReponse = new RenderResponse("renderReponse");
        restoreView.setSuccessor(applyRequest);
        applyRequest.setSuccessor(proccessValidate);
        proccessValidate.setSuccessor(updateModel);
        updateModel.setSuccessor(invokeAction);
        invokeAction.setSuccessor(renderReponse);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    public void destroy() {
    }
}
