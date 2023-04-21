package net.sf.crispy.sample;

import java.util.Properties;
import net.sf.crispy.Property;
import net.sf.crispy.impl.ServiceManager;
import net.sf.crispy.impl.StaticBurlapProxy;
import net.sf.crispy.impl.StaticHessianProxy;
import net.sf.crispy.impl.caucho.MiniCauchoServer;
import net.sf.crispy.interceptor.StopWatchInterceptor;
import net.sf.crispy.util.Util;
import test.crispy.example.service.Calculator;
import test.crispy.example.service.CalculatorImpl;
import test.crispy.example.service.Echo;
import test.crispy.example.service.EchoImpl;

public class Caucho {

    public static void main(String[] args) {
        Util.initJdkLogger();
        MiniCauchoServer server = new MiniCauchoServer();
        try {
            server.addService(Echo.class.getName(), EchoImpl.class.getName());
            server.addService(Calculator.class.getName(), CalculatorImpl.class.getName());
            server.start();
            Properties prop = new Properties();
            prop.put(Property.DYNAMIC_PROXY_CLASS, Property.VALUE_FOR_JDK_DYNAMIC_PROXY);
            prop.put(Property.INTERCEPTOR_CLASS, StopWatchInterceptor.class.getName());
            prop.put(Property.REMOTE_URL_AND_PORT, "http://localhost:8091/crispy/burlap");
            prop.put(Property.STATIC_PROXY_CLASS, StaticBurlapProxy.class.getName());
            ServiceManager manager = new ServiceManager(prop);
            Echo echo = (Echo) manager.createService(Echo.class);
            System.out.println("Echo: " + echo.echo("Hello echo ..."));
            StopWatchInterceptor stopWatch = (StopWatchInterceptor) manager.getInterceptorByPos(0);
            System.out.println("StopWatch-newInstance: " + stopWatch.getStopTimeNewInstance());
            System.out.println("StopWatch-call: " + stopWatch.getStopTimeInvokeMethod() + "\n");
            prop = new Properties();
            prop.put(Property.DYNAMIC_PROXY_CLASS, Property.VALUE_FOR_JDK_DYNAMIC_PROXY);
            prop.put(Property.INTERCEPTOR_CLASS, StopWatchInterceptor.class.getName());
            prop.put(Property.REMOTE_URL_AND_PORT, "http://localhost:8091/crispy/hessian");
            prop.put(Property.STATIC_PROXY_CLASS, StaticHessianProxy.class.getName());
            manager = new ServiceManager(prop);
            Calculator calc = (Calculator) manager.createService(Calculator.class);
            System.out.println("Calc.add: " + calc.add(123, 345));
            stopWatch = (StopWatchInterceptor) manager.getInterceptorByPos(0);
            System.out.println("StopWatch-newInstance: " + stopWatch.getStopTimeNewInstance());
            System.out.println("StopWatch-call: " + stopWatch.getStopTimeInvokeMethod());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
