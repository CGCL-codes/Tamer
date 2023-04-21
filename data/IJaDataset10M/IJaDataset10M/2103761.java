package com.moandjiezana.mewf;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.moandjiezana.mewf.conversion.RequestParameterConverter;
import com.moandjiezana.mewf.routing.Route;
import com.moandjiezana.mewf.routing.Routes;
import com.moandjiezana.mewf.utils.Optional;
import com.moandjiezana.mewf.utils.Optional.Present;
import com.moandjiezana.mewf.validation.RequestValidator;

@Singleton
@SuppressWarnings("serial")
public class MewfServlet extends HttpServlet {

    private final Injector injector;

    private final Routes routes;

    @Inject
    public MewfServlet(Routes routes, Injector injector) {
        this.routes = routes;
        this.injector = injector;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI().replaceFirst(req.getContextPath(), "");
        Optional<Route> optionalRoute = routes.getRoute(requestUri, req.getMethod().toLowerCase());
        if (optionalRoute.isAbsent()) {
            resp.setStatus(404);
            return;
        }
        Route route = ((Present<Route>) optionalRoute).get();
        Method controllerMethod = route.getControllerMethod();
        Object[] parameterValues = injector.getInstance(RequestParameterConverter.class).convert(route, req);
        RequestValidator requestValidator = injector.getInstance(RequestValidator.class);
        Set<ConstraintViolation<?>> violations = requestValidator.validate(controllerMethod, parameterValues);
        injector.getInstance(RequestProcessor.class).process(injector.getInstance(route.getControllerClass()), controllerMethod, parameterValues, violations, req, resp);
    }
}
