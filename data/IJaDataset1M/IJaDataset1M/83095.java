package com.sunshine.web.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sunshine.web.controller.ShineController;
import com.sunshine.web.view.WebView;

public class SunShineControllerShow extends ShineController {

    @Override
    public WebView ShineDo(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("name", "小白");
        request.setAttribute("address", "台湾");
        WebView webview = new WebView();
        webview.setView("show_view");
        return webview;
    }

    public WebView show(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("name", "自定义的方法！");
        WebView webview = new WebView();
        webview.setView("show_user");
        return webview;
    }
}
