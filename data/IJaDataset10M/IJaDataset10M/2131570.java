package com.sunshine.web.base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.PropertyConfigurator;
import com.sunshine.web.controller.ShineController;
import com.sunshine.web.controller.ShinePam;
import com.sunshine.web.util.properties.SystemXMLtools;
import com.sunshine.web.view.ShineViewPam;
import com.sunshine.web.view.ViewFactory;
import com.sunshine.web.view.WebInput;
import com.sunshine.web.view.WebView;

/**
 * 总控制器
 * @author yinhu
 *
 */
public final class SunShineMainController extends BaseServlet {

    private ConfigPam configpam = null;

    /**
	 * 初始化配置文件
	 */
    public void init() throws ServletException {
        configpam = SystemXMLtools.getAll_ConfigPam(this.getServletContext());
    }

    @Override
    protected void SunLineDo(HttpServletRequest request, HttpServletResponse response) {
        ShinePam shinePam = ControllerFactory.getShinePmByname(configpam.getShinepamMap(), request);
        ShineController shineController = ControllerFactory.getShineController(shinePam.getClasspath());
        shineController.ShineControllerInit(request, response);
        String method = ControllerFactory.isParm(shinePam, request);
        WebView webview = null;
        if (method == null) {
            webview = shineController.ShineDo(request, response);
        } else {
            Class classType = shineController.getClass();
            Method addMethod = null;
            try {
                addMethod = classType.getMethod(method, new Class[] { HttpServletRequest.class, HttpServletResponse.class });
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                webview = (WebView) addMethod.invoke(shineController, request, response);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        ViewFactory.view_foward(request, response, webview, configpam.getShineviewpamMap());
    }
}
