package net.paoding.rose.demo.controllers;

import net.paoding.rose.web.ControllerInterceptor;
import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Controller;
import net.paoding.rose.web.annotation.ReqMapping;
import net.paoding.rose.web.var.Model;

@Controller
public class AppController {

    public ControllerInterceptor trace = new ControllerInterceptorAdapter() {

        @Override
        public Object before(Invocation invocation) throws Exception {
            System.out.println("controller=" + invocation.getController());
            System.out.println("action=" + invocation.getMethod());
            return super.before(invocation);
        }
    };

    @ReqMapping(path = "${name}/${path:.*}")
    public String app(Model model) {
        System.out.println("name=" + model.get("name"));
        System.out.println("path=" + model.get("path"));
        return "@name=${name}; path=${path}";
    }
}
