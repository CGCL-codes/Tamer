package ru.yandex.strictweb.example.sampleajax;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.yandex.strictweb.ajaxtools.AjaxService;
import ru.yandex.strictweb.ajaxtools.BeanProvider;
import ru.yandex.strictweb.scriptjava.CommonCompiler;
import ru.yandex.strictweb.scriptjava.base.ajax.Ajax;
import ru.yandex.strictweb.scriptjava.compiler.Compiler;

public class SampleAjaxCompile extends CommonCompiler {

    @Override
    public void addToCompiler(Compiler compiler) throws Exception {
        Ajax.prepareCompiler(compiler);
        compiler.parseClass(SampleHelperBean.class).parseClass(SampleUiForm.class).parseClass(SomeModel.class);
    }

    public static void main(String[] args) throws Exception {
        try {
            new SampleAjaxCompile().setJsGenPath("src/ru/yandex/strictweb/example/www-root/sample-ajax.js").setBasePath("src/").build(args);
        } catch (Throwable th) {
            System.out.println("\n\nЗапускайте этот файл из папочки strict-web: корневой папки проекта\n\n");
            th.printStackTrace();
        }
        startJetty();
    }

    private static void startJetty() throws Exception {
        Server server = new Server(3128);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setResourceBase("src/ru/yandex/strictweb/example/www-root/");
        servletContextHandler.addServlet(new ServletHolder(new DefaultServlet()), "/*");
        AjaxService ajaxService = new AjaxService();
        ajaxService.setBeanProvider(new BeanProvider() {

            public Object getBeanInstance(String beanName) {
                try {
                    beanName = Character.toUpperCase(beanName.charAt(0)) + beanName.substring(1);
                    return Class.forName(SampleHelperBean.class.getPackage().getName() + "." + beanName).newInstance();
                } catch (Throwable th) {
                    throw new RuntimeException(th);
                }
            }
        });
        servletContextHandler.addServlet(new ServletHolder(ajaxService), "/ajax");
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
