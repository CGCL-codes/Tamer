package org.magicdroid.server.common;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.magicdroid.commons.Injector;
import org.magicdroid.commons.MethodLookupMap;
import org.magicdroid.commons.Structures.Lazy;
import org.magicdroid.serializer.MagicdroidSerializer;
import org.magicdroid.server.impl.ServerMagicdroidEnvironment;
import org.magicdroid.services.RPCService;

@SuppressWarnings("serial")
public class ServiceServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(ServiceServlet.class);

    public static final RequestParameter METHOD = new RequestParameter("m");

    public static final RequestParameter SERVICE = new RequestParameter("svc");

    private final Lazy<Injector> injector = new Lazy<Injector>() {

        @Override
        protected Injector load() {
            return env.getInjector();
        }
    };

    private ServerMagicdroidEnvironment env;

    public ServiceServlet() {
    }

    @Override
    public void init() throws ServletException {
        this.env = new ServerMagicdroidEnvironment();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    public enum CallStatus {

        OK, EXCEPTION
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.env.notify(request);
        MagicdroidSerializer serializer = this.injector.get().create(MagicdroidSerializer.class);
        try {
            Object paramsDecoded = serializer.decode(RPCService.RPCUtil.decode(request.getInputStream()));
            Class<?> serviceType = Class.forName(SERVICE.get(request));
            Object service = this.injector.get().create(serviceType);
            Method method = new MethodLookupMap(serviceType).get(METHOD.get(request));
            Object result = method.invoke(service, ((Object[]) paramsDecoded));
            respond(response, serializer.encode(result), CallStatus.OK);
        } catch (ClassNotFoundException e) {
            log.error(e, e);
            respond(response, serializer.encode(e), CallStatus.EXCEPTION);
        } catch (IllegalArgumentException e) {
            log.error(e, e);
            respond(response, serializer.encode(e), CallStatus.EXCEPTION);
        } catch (IllegalAccessException e) {
            log.error(e, e);
            respond(response, serializer.encode(e), CallStatus.EXCEPTION);
        } catch (InvocationTargetException e) {
            log.error(e, e);
            respond(response, serializer.encode(e.getTargetException()), CallStatus.EXCEPTION);
        }
    }

    private void respond(HttpServletResponse response, String data, CallStatus status) throws IOException {
        OutputStream writer = (response.getOutputStream());
        response.setHeader("CALL", status.name());
        RPCService.RPCUtil.encode(response.getOutputStream(), data);
    }
}
