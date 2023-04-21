package dinamica;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.sql.*;
import java.io.PrintWriter;
import dinamica.xml.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Central controller to dispatch all requests
 * recevied by a Web Application built with this framework.
 * This class represents the Coordinator of all activities in
 * the application, it is the base for the advanced MVC mechanism implemented
 * by this framework. This servlet should be configured in WEB.XML to intercept
 * requests targeting /trans/... URLs.<br>
 * <br>
 * The application template provided with the framework includes all
 * the required configuration settings in WEB.XML, use that as a starting
 * point for your own applications.
 * <br>
 * Please read the Howto documents and technical articles included with
 * this framework in order to understand and master the inner working mechanisms
 * of the framework and the role of this servlet in particular.
 * <br>
 * Creation date: 3/10/2003<br>
 * Last Update: 3/10/2003<br>
 * (c) 2003 Martin Cordova<br>
 * This code is released under the LGPL license<br>
 * @author Martin Cordova
 */
public class Controller extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * JNDI prefix (java:comp/env/ or "")
	 */
    String _jndiPrefix = null;

    /**
	 * Servlet context
	 */
    ServletContext _ctx = null;

    /**
	 * Default JDBC data source
	 */
    DataSource _ds = null;

    /**
	 * trace log file for requests and JDBC
	 */
    String _logFile = null;

    /**
	 * validation error action URI
	 */
    String _validationErrorAction = null;

    /**
	 * default app-level request encoding
	 */
    String _requestEncoding = null;

    /**
	 * default app-level file encoding
	 */
    String _fileEncoding = null;

    /**
	 * Central point of control to intercept
	 * all transaction requests (the Controller in the MVC mechanism)
	 */
    @SuppressWarnings("unchecked")
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod().toUpperCase();
        if (!method.equals("GET") && !method.equals("POST")) {
            res.sendError(501, method + " not supported");
            return;
        }
        GenericTransaction t = null;
        GenericOutput o = null;
        Recordset inputValues = null;
        int returnCode = 0;
        long t1 = 0;
        long t2 = 0;
        boolean saveMvcLog = false;
        boolean saveJdbcLog = false;
        StringWriter logWriter = new StringWriter();
        PrintWriter logPrinter = new PrintWriter(logWriter);
        Config config = null;
        try {
            String path = getPath(req);
            String configData = null;
            try {
                configData = StringUtil.getResource(_ctx, path + "config.xml");
            } catch (Throwable notFound) {
                res.sendError(404);
                return;
            }
            configData = replaceMacros(req, configData);
            config = new Config(configData, path);
            if (config.requestEncoding != null) {
                req.setCharacterEncoding(config.requestEncoding);
            } else {
                if (_requestEncoding != null) req.setCharacterEncoding(_requestEncoding);
            }
            setRequestValues(req, config);
            if (config.jdbcLog != null && config.jdbcLog.equals("true")) saveJdbcLog = true;
            if (config.mvcLog.equals("true")) {
                saveMvcLog = true;
                logPrinter.println("--REQUEST-START");
                logPrinter.println("Request: " + path);
                logPrinter.println("Summary: " + config.summary);
                logPrinter.println("Date: " + new java.util.Date(System.currentTimeMillis()));
                logPrinter.println("Thread: " + Thread.currentThread().getName());
                ;
                logPrinter.println("Session ID: " + req.getSession(true).getId());
            }
            if (config.transValidator != null && config.transValidator.equals("true")) {
                Connection con = null;
                try {
                    if (config.transDataSource != null) con = Jndi.getDataSource(_jndiPrefix + config.transDataSource).getConnection(); else con = _ds.getConnection();
                    inputValues = validateInput(req, config, con, saveJdbcLog, logPrinter);
                } catch (Throwable verror) {
                    throw verror;
                } finally {
                    if (con != null) con.close();
                }
            }
            if (config.validatorInSession != null && config.validatorInSession.equals("true")) {
                Iterator i = inputValues.getFields().values().iterator();
                while (i.hasNext()) {
                    RecordsetField f = (RecordsetField) i.next();
                    if (!inputValues.isNull(f.getName())) req.getSession(true).setAttribute(f.getName(), inputValues.getString(f.getName()));
                }
            }
            t1 = System.currentTimeMillis();
            if (config.transClassName != null) {
                Connection con = null;
                try {
                    if (config.transDataSource != null) con = Jndi.getDataSource(_jndiPrefix + config.transDataSource).getConnection(); else con = _ds.getConnection();
                    t = (GenericTransaction) getObject(config.transClassName);
                    t.init(_ctx, req, res);
                    t.setConfig(config);
                    t.setConnection(con);
                    if (saveJdbcLog) t.setLogWriter(logPrinter);
                    if (config.transTransactions.equals("true")) {
                        if (config.isolationLevel != null && !config.isolationLevel.equals("")) {
                            if (config.isolationLevel.equalsIgnoreCase("READ_UNCOMMITTED")) con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED); else if (config.isolationLevel.equalsIgnoreCase("READ_COMMITTED")) con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED); else if (config.isolationLevel.equalsIgnoreCase("REPEATABLE_READ")) con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); else if (config.isolationLevel.equalsIgnoreCase("SERIALIZABLE")) con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); else throw new Throwable("Invalid transaction isolation level: " + config.isolationLevel);
                        }
                        con.setAutoCommit(false);
                    } else con.setAutoCommit(true);
                    IServiceWrapper w = null;
                    if (t instanceof dinamica.IServiceWrapper) {
                        w = (IServiceWrapper) t;
                        w.beforeService(inputValues);
                    }
                    returnCode = t.service(inputValues);
                    if (w != null) w.afterService(inputValues);
                    if (!con.getAutoCommit()) con.commit();
                } catch (Throwable e) {
                    if (con != null && !con.getAutoCommit()) con.rollback();
                    throw e;
                } finally {
                    if (con != null) con.close();
                }
            }
            t2 = System.currentTimeMillis();
            if (saveMvcLog) logPrinter.println("Transaction performance (ms): " + (t2 - t1));
            String forwardUri = config.getUriForExitCode(returnCode);
            if (forwardUri != null) {
                forward(forwardUri, req, res);
                return;
            }
            t1 = System.currentTimeMillis();
            if (config.outClassName != null) {
                if (config.headers.equals("true")) {
                    String contentType = config.contentType;
                    if (contentType != null) {
                        if (config.contentType.startsWith("text")) {
                            if (contentType.indexOf("charset") < 0 && _fileEncoding != null) contentType = contentType + "; charset=" + _fileEncoding;
                        }
                        res.setContentType(contentType);
                    }
                    if (config.expiration != null) {
                        if (Integer.parseInt(config.expiration) == 0) {
                            res.setHeader("Cache-Control", "no-cache");
                            res.setHeader("Pragma", "no-cache");
                        } else res.setHeader("Cache-Control", "max-age=" + config.expiration);
                    }
                }
                o = (GenericOutput) getObject(config.outClassName);
                o.init(_ctx, req, res);
                o.setConfig(config);
                if (config.contentType != null && config.contentType.startsWith("text")) {
                    String template = o.getResource(config.outTemplate);
                    TemplateEngine te = new TemplateEngine(_ctx, req, template);
                    HttpSession s = req.getSession(true);
                    java.util.Locale l = (java.util.Locale) s.getAttribute("dinamica.user.locale");
                    te.setLocale(l);
                    if (config.templateEncoding != null) te.setEncoding(config.templateEncoding); else te.setEncoding(_fileEncoding);
                    o.print(te, t);
                    te.print(res);
                } else {
                    o.print(t);
                }
            }
            t2 = System.currentTimeMillis();
            if (saveMvcLog) {
                logPrinter.println("Output performance (ms): " + (t2 - t1));
            }
        } catch (RequestValidationException vex) {
            RequestDispatcher rd = _ctx.getRequestDispatcher(_validationErrorAction);
            req.setAttribute("dinamica.errors", vex.getErrors());
            rd.forward(req, res);
        } catch (Throwable e) {
            StringWriter s = new StringWriter();
            PrintWriter err = new PrintWriter(s);
            e.printStackTrace(err);
            req.setAttribute("dinamica.error.description", e.getMessage());
            req.setAttribute("dinamica.error.url", req.getRequestURL().toString());
            req.setAttribute("dinamica.error.stacktrace", s.toString());
            req.setAttribute("dinamica.error.user", req.getRemoteUser());
            req.setAttribute("dinamica.error.remote_addr", req.getRemoteAddr());
            req.setAttribute("dinamica.error.exception", e);
            if (config != null && config.onErrorAction != null) {
                log(e.getMessage(), e);
                _ctx.getRequestDispatcher(config.onErrorAction).forward(req, res);
            } else {
                throw new ServletException(e);
            }
        } finally {
            if (saveJdbcLog || saveMvcLog) saveLog(logWriter.toString());
        }
    }

    /**
	 * Extract path begining with /trans/...
	 * @param uri
	 * @return req Servlet request object
	 */
    String getPath(HttpServletRequest req) throws Throwable {
        String uri = null;
        uri = (String) req.getAttribute("javax.servlet.include.request_uri");
        if (uri == null) uri = req.getRequestURI();
        String find = getInitParameter("base-dir");
        int pos = uri.indexOf(find);
        if (pos >= 0) {
            String path = uri.substring(pos);
            req.setAttribute("dinamica.action.path", path);
            return "/WEB-INF" + path + "/";
        } else {
            throw new Throwable("Invalid base-dir parameter for this request (" + uri + ") - can't extract Transaction path from URI using this prefix: " + find);
        }
    }

    /**
	 * Controller initialization tasks.<br>
	 * Reads parameters jndi-prefix and def-datasource
	 * and creates a default datasource object with
	 * modular scope to be used by all threads from this servlet
	 */
    public void init() throws ServletException {
        try {
            _ctx = getServletContext();
            _jndiPrefix = _ctx.getInitParameter("jndi-prefix");
            if (_jndiPrefix == null) _jndiPrefix = "";
            String dsName = _jndiPrefix + _ctx.getInitParameter("def-datasource");
            if (dsName == null || dsName.trim().equals("")) throw new ServletException("Configuration problem detected in WEB.XML: def-datasource context parameter cannot be undefined!");
            _ds = Jndi.getDataSource(dsName);
            _logFile = _ctx.getInitParameter("log-file");
            if (_logFile == null || _logFile.trim().equals("")) throw new ServletException("Configuration problem detected in WEB.XML: log-file context parameter cannot be undefined!");
            _validationErrorAction = _ctx.getInitParameter("on-validation-error");
            if (_validationErrorAction == null || _validationErrorAction.trim().equals("")) throw new ServletException("Configuration problem detected in WEB.XML: on-validation-error context parameter cannot be undefined!");
            _requestEncoding = _ctx.getInitParameter("request-encoding");
            if (_requestEncoding != null && _requestEncoding.trim().equals("")) _requestEncoding = null;
            _fileEncoding = _ctx.getInitParameter("file-encoding");
            if (_fileEncoding != null && _fileEncoding.trim().equals("")) _fileEncoding = null;
            super.init();
        } catch (Throwable e) {
            log("Controller servlet failed on init!");
            throw new ServletException(e);
        }
    }

    /**
	 * Save message to filesystem, using the context parameter
	 * log-file defined in web.xml and stored in modular variable _logFile
	 * @param message String to append to file
	 */
    void saveLog(String message) {
        StringUtil.saveMessage(_logFile, message);
    }

    /**
	 * Auto-Validate request parameters for single value
	 * parameters - array parameters must be processed
	 * by the business logic using the Servlet Request object
	 * @param req Servlet Request
	 * @param config Configuration for the current Action
	 * @throws Throwable If any validation rule is violated
	 */
    Recordset validateInput(HttpServletRequest req, Config config, Connection conn, boolean jdbcLog, PrintWriter jdbcLogPrinter) throws Throwable {
        RequestValidationException errors = new RequestValidationException();
        String dateFormat = _ctx.getInitParameter("def-input-date");
        Recordset inputs = new Recordset();
        String file = config.path + "validator.xml";
        Document xml = new Document(StringUtil.getResource(_ctx, file));
        Element root = xml.getRoot();
        String sessionID = root.getAttribute("id");
        if (sessionID == null) sessionID = "";
        Element elements[] = xml.getElements("parameter");
        Element param;
        for (int i = 0; i < elements.length; i++) {
            String type = null;
            String id = null;
            String label = null;
            int sqlType = 0;
            String required = null;
            param = elements[i];
            id = param.getAttribute("id");
            if (id == null) throw new Exception("Invalid Validator. Attribute [id] not found: " + file);
            type = param.getAttribute("type");
            if (type == null) throw new Exception("Invalid Validator. Attribute [type] not found: " + file);
            required = param.getAttribute("required");
            if (required == null) throw new Exception("Invalid Validator. Attribute [required] not found: " + file);
            label = param.getAttribute("label");
            if (label == null) throw new Exception("Invalid Validator. Attribute [label] not found: " + file);
            if (type.equals("varchar")) sqlType = Types.VARCHAR; else if (type.equals("integer")) sqlType = Types.INTEGER; else if (type.equals("double")) sqlType = Types.DOUBLE; else if (type.equals("date")) sqlType = Types.DATE; else throw new Exception("Invalid validator data type (" + type + ") in file: " + file);
            inputs.append(id, sqlType);
        }
        inputs.addNew();
        for (int j = 0; j < elements.length; j++) {
            String type = null;
            String value = null;
            String id = null;
            String label = null;
            int sqlType = 0;
            String required = "";
            int maxLen = 0;
            String maxLenAttr = "";
            int minValue = 0;
            int maxValue = 0;
            String minValueAttr = "";
            String maxValueAttr = "";
            String regexp = null;
            String regexpError = null;
            param = elements[j];
            id = param.getAttribute("id");
            type = param.getAttribute("type");
            required = param.getAttribute("required");
            label = param.getAttribute("label");
            maxLenAttr = param.getAttribute("maxlen");
            minValueAttr = param.getAttribute("min");
            maxValueAttr = param.getAttribute("max");
            regexp = param.getAttribute("regexp");
            regexpError = param.getAttribute("regexp-error-label");
            label = "<b>" + label + "</b>";
            if (maxLenAttr != null) maxLen = Integer.parseInt(maxLenAttr);
            if (minValueAttr != null) minValue = Integer.parseInt(minValueAttr);
            if (maxValueAttr != null) maxValue = Integer.parseInt(maxValueAttr);
            if (type.equals("varchar")) sqlType = Types.VARCHAR; else if (type.equals("integer")) sqlType = Types.INTEGER; else if (type.equals("double")) sqlType = Types.DOUBLE; else if (type.equals("date")) sqlType = Types.DATE;
            String data[] = req.getParameterValues(id);
            if (data != null && !data[0].trim().equals("")) {
                if (data.length == 1) {
                    value = data[0].trim();
                    if (maxLen > 0) {
                        if (value.length() > maxLen) errors.addMessage(id, label + ": " + "${lbl:data_too_long}" + maxLen);
                    }
                    if (sqlType == Types.VARCHAR && regexp != null) {
                        boolean isMatch = Pattern.matches(regexp, value);
                        if (!isMatch) errors.addMessage(id, label + ": " + regexpError);
                    }
                    switch(sqlType) {
                        case Types.DATE:
                            java.util.Date d = ValidatorUtil.testDate(value, dateFormat);
                            if (d == null) errors.addMessage(id, label + ": " + "${lbl:invalid_date}"); else inputs.setValue(id, d);
                            break;
                        case Types.DOUBLE:
                            Double dbl = ValidatorUtil.testDouble(value);
                            if (dbl == null) errors.addMessage(id, label + ": " + "${lbl:invalid_double}"); else inputs.setValue(id, dbl);
                            if (minValueAttr != null && dbl != null && dbl.doubleValue() < Double.parseDouble(minValueAttr)) errors.addMessage(id, label + ": " + "${lbl:min_value_violation}" + minValue);
                            if (maxValueAttr != null && dbl != null && dbl.doubleValue() > Double.parseDouble(maxValueAttr)) errors.addMessage(id, label + ": " + "${lbl:max_value_violation}" + maxValue);
                            break;
                        case Types.INTEGER:
                            Integer i = ValidatorUtil.testInteger(value);
                            if (i == null) errors.addMessage(id, label + ": " + "${lbl:invalid_integer}"); else inputs.setValue(id, i);
                            if (minValueAttr != null && i != null && i.intValue() < minValue) errors.addMessage(id, label + ": " + "${lbl:min_value_violation}" + minValue);
                            if (maxValueAttr != null && i != null && i.intValue() > maxValue) errors.addMessage(id, label + ": " + "${lbl:max_value_violation}" + maxValue);
                            break;
                        case Types.VARCHAR:
                            inputs.setValue(id, value);
                            break;
                    }
                } else {
                    throw new Throwable("Invalid parameter. Generic validator can't process array parameters. Parameter (" + id + ") in file: " + file);
                }
            } else if (required.equals("true")) {
                errors.addMessage(id, label + ": " + "${lbl:parameter_required}");
            }
        }
        if (errors.getErrors().getRecordCount() == 0) {
            Element valds[] = xml.getElements("custom-validator");
            for (int i = 0; i < valds.length; i++) {
                HashMap<String, String> a = new HashMap<String, String>(5);
                Element validator = valds[i];
                String className = validator.getAttribute("classname");
                String onErrorLabel = validator.getAttribute("on-error-label");
                String id = validator.getAttribute("id");
                if (id == null) id = "";
                a = validator.getAttributes();
                AbstractValidator t = null;
                t = (AbstractValidator) getObject(className);
                t.init(_ctx, req, null);
                t.setConfig(config);
                t.setConnection(conn);
                if (jdbcLog) t.setLogWriter(jdbcLogPrinter);
                boolean b = t.isValid(req, inputs, a);
                if (!b) {
                    String err = t.getErrorMessage();
                    if (err == null) err = onErrorLabel;
                    errors.addMessage(id, err);
                }
            }
        }
        if (errors.getErrors().getRecordCount() > 0) {
            req.setAttribute("_request", inputs);
            throw errors;
        } else {
            if (!sessionID.equals("")) req.getSession().setAttribute(sessionID, inputs);
            return inputs;
        }
    }

    /**
	 * Forward request to another resource in the same context
	 * @param uri Absolute path to resource (a valid servlet mapping)
	 * @throws Throwable
	 */
    void forward(String uri, HttpServletRequest req, HttpServletResponse res) throws Throwable {
        RequestDispatcher rd = _ctx.getRequestDispatcher(uri);
        rd.forward(req, res);
    }

    /**
	 * Set request attributes if defined in config.xml
	 * @param req
	 * @param config
	 * @throws Throwable
	 */
    void setRequestValues(HttpServletRequest req, Config config) throws Throwable {
        Document doc = config.getDocument();
        Element e[] = doc.getElements("set-request-attribute");
        if (e != null) {
            for (int i = 0; i < e.length; i++) {
                Element r = e[i];
                String id = r.getAttribute("id");
                String value = r.getAttribute("value");
                if (value != null && value.startsWith("file:")) {
                    String x[] = StringUtil.split(value, ":");
                    String file = x[1];
                    if (file.startsWith("/")) {
                        value = StringUtil.getResource(this.getServletContext(), file);
                    } else {
                        value = StringUtil.getResource(this.getServletContext(), getPath(req) + file);
                    }
                }
                req.setAttribute(id, value);
            }
        }
    }

    /**
	 * Loads class and returns new instance of this class.
	 * @param className Name of class to load - include full package name
	 * @return New instance of the class
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
    Object getObject(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.loadClass(className).newInstance();
    }

    /**
	 * Replace markers like ${def:actionroot} in config.xml
	 * @param req Request object
	 * @param xmlData Body of config.xml
	 * @return Body of config.xml with all markers replaced by the corresponding values
	 * @throws Throwable
	 */
    String replaceMacros(HttpServletRequest req, String xmlData) throws Throwable {
        String actionPath = (String) req.getAttribute("dinamica.action.path");
        actionPath = actionPath.substring(0, actionPath.lastIndexOf("/"));
        xmlData = StringUtil.replace(xmlData, "${def:actionroot}", actionPath);
        return xmlData;
    }
}
