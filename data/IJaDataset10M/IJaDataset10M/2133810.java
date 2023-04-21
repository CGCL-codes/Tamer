package javango.contrib.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javango.api.Settings;
import javango.core.AnonymousUser;
import javango.core.JavangoLifecycleListener;
import javango.core.servers.RequestProcessor;
import javango.http.Http404;
import javango.http.HttpException;
import javango.http.HttpRequest;
import javango.http.HttpResponse;
import javango.http.SimpleHttpResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;

public class Servlet extends HttpServlet implements Module, javango.core.JavangoLifecycle {

    public static final String BROKEN_LINK_LOG_LEVEL = "BROKEN_LINK_LOG_LEVEL";

    /**
	 * 
	 */
    private static final long serialVersionUID = 4173911300243443940L;

    private static final Log log = LogFactory.getLog(Servlet.class);

    Injector injector;

    @Inject
    RequestProcessor requestProcessor;

    @Inject(optional = true)
    @Named("javango.characterEncoding")
    String characterEncoding;

    List<JavangoLifecycleListener> listeners = new ArrayList<JavangoLifecycleListener>();

    protected Injector getInjector() throws ServletException {
        if (injector != null) {
            return injector;
        }
        try {
            String guiceModule = this.getServletConfig().getInitParameter("GuiceModules");
            String settings = this.getServletConfig().getInitParameter("Settings");
            List<Module> moduleList = new ArrayList<Module>();
            moduleList.add(this);
            if (!StringUtils.isBlank(guiceModule)) {
                for (String moduleName : guiceModule.split(";")) {
                    moduleList.add((Module) Class.forName(moduleName.trim()).newInstance());
                }
                return com.google.inject.Guice.createInjector(moduleList);
            } else if (!StringUtils.isBlank(settings)) {
                return ((Settings) Class.forName(settings.trim()).newInstance()).createInjector(moduleList);
            } else {
                throw new ServletException("You must provide GuiceModules or Settings as a parameter to the servlet");
            }
        } catch (Exception e) {
            log.error(e, e);
            throw new ServletException(e);
        }
    }

    @Override
    public void init() throws ServletException {
        log.debug("Entering Servlet.init");
        super.init();
        injector = getInjector();
        injector.injectMembers(this);
        for (JavangoLifecycleListener listener : listeners) {
            listener.start();
        }
    }

    public void configure(Binder binder) {
        binder.bind(Servlet.class).toInstance(this);
    }

    public void addListener(JavangoLifecycleListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void destroy() {
        requestProcessor.stop();
        for (JavangoLifecycleListener listener : listeners) {
            listener.stop();
        }
        LogFactory.releaseAll();
        super.destroy();
    }

    public void service(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws IOException, ServletException {
        if (characterEncoding != null) {
            servletRequest.setCharacterEncoding(characterEncoding);
            servletResponse.setCharacterEncoding(characterEncoding);
        }
        HttpRequest request = injector.getInstance(HttpRequest.class);
        String context = StringUtils.trimToEmpty(servletRequest.getContextPath());
        String path = StringUtils.trimToEmpty(servletRequest.getRequestURI());
        try {
            path = path.substring(context.length() + 1);
            request.setPath(path);
            request.setContext(context);
            request.setMethod(servletRequest.getMethod());
            request.setSession(new HttpServletSession(servletRequest));
            boolean isMultipart = ServletFileUpload.isMultipartContent(servletRequest);
            if (isMultipart) {
                Map<String, FileItem> files = new HashMap<String, FileItem>();
                Map<String, String[]> parameters = new HashMap<String, String[]>();
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(servletRequest);
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (item.isFormField()) {
                        parameters.put(item.getFieldName(), new String[] { item.getString() });
                    } else {
                        files.put(item.getFieldName(), item);
                    }
                }
                request.setFiles(files);
                request.setParameterMap(parameters);
            } else {
                request.setParameterMap(servletRequest.getParameterMap());
                request.setFiles(null);
            }
            if (servletRequest.getUserPrincipal() == null) {
                request.setUser(new AnonymousUser());
            } else {
                request.setUser(new PrincipalUser(servletRequest));
            }
            HttpResponse response = requestProcessor.service(request);
            response.render(servletRequest, servletResponse);
            request.close();
        } catch (Http404 e) {
            String level = injector.getInstance(Settings.class).get(BROKEN_LINK_LOG_LEVEL);
            if (level != null) {
                if ("trace".equalsIgnoreCase(level)) {
                    log.trace(e, e);
                } else if ("debug".equalsIgnoreCase(level)) {
                    log.debug(e, e);
                } else if ("info".equalsIgnoreCase(level)) {
                    log.info(e, e);
                } else if ("warn".equalsIgnoreCase(level)) {
                    log.warn(e, e);
                } else if ("error".equalsIgnoreCase(level)) {
                    log.error(e, e);
                } else if ("fatal".equalsIgnoreCase(level)) {
                    log.fatal(e, e);
                }
            }
            if (!injector.getInstance(Settings.class).isDebug()) {
                SimpleHttpResponse response = new SimpleHttpResponse("Page not found");
                response.setStatusCode(404);
                try {
                    response.render(servletRequest, servletResponse);
                } catch (HttpException e2) {
                    log.error(e2, e2);
                    throw new ServletException(e);
                }
            } else {
                throw new ServletException(e);
            }
        } catch (FileUploadException e) {
            log.error(e, e);
            try {
                request.close();
            } catch (HttpException e1) {
                log.error(e1, e1);
            }
            if (!injector.getInstance(Settings.class).isDebug()) {
                HttpResponse response = new SimpleHttpResponse("Unrecoverable error processing uploaded files, please contact support");
                try {
                    response.render(servletRequest, servletResponse);
                } catch (HttpException e2) {
                    log.error(e2, e2);
                    throw new ServletException(e);
                }
            }
        } catch (HttpException e) {
            log.error(String.format("HttpException while processing for user '%s' %s:%s", request.getUser(), context, path));
            log.error(e, e);
            try {
                request.close();
            } catch (HttpException e1) {
                log.error(e1, e1);
            }
            if (!injector.getInstance(Settings.class).isDebug()) {
                HttpResponse response = new SimpleHttpResponse("Unrecoverable error, please contact support");
                try {
                    response.render(servletRequest, servletResponse);
                } catch (HttpException e2) {
                    log.error(e2, e2);
                    throw new ServletException(e);
                }
            } else {
                throw new ServletException(e);
            }
        }
    }
}
