package com.tensegrity.palowebviewer.modules.engine.server;

import java.text.NumberFormat;
import java.util.Locale;
import org.apache.log4j.Logger;
import org.palo.api.Connection;
import com.tensegrity.palowebviewer.modules.engine.client.IClientProperties;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.AuthenticationException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InternalErrorException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidLoginOrPasswordException;
import com.tensegrity.palowebviewer.modules.engine.client.exceptions.InvalidObjectPathException;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDefaultView;
import com.tensegrity.palowebviewer.modules.paloclient.client.XElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.XFolder;
import com.tensegrity.palowebviewer.modules.paloclient.client.XObject;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IXPoint;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XArrays;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XHelper;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XQueryPath;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XResult;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XViewPath;
import com.tensegrity.palowebviewer.modules.util.client.PerformanceTimer;
import com.tensegrity.palowebviewer.server.IConnectionPool;
import com.tensegrity.palowebviewer.server.IUser;
import com.tensegrity.palowebviewer.server.security.IAuthenificator;

public class EngineLogic {

    private static final long serialVersionUID = -4183989263192186885L;

    private static final Logger log = Logger.getLogger(EngineLogic.class);

    public IUser authenticate(String login, String password, boolean remember, IEngineContext context) throws InternalErrorException {
        log.info("authenticate(" + login + ", " + password + ", " + remember + ")");
        IAuthenificator authenificator = context.getAuthenificator();
        if (authenificator == null) {
            log.error("Authenificator is null");
            throw new InternalErrorException();
        }
        try {
            IUser user = authenificator.authentificate(login, password);
            return user;
        } catch (InvalidLoginOrPasswordException e) {
            return null;
        } catch (AuthenticationException e) {
            log.error("Can't authenticate user", e);
            throw new InternalErrorException();
        }
    }

    public void logoff(IEngineContext context) {
        log.debug("logoff");
        context.setUser(null);
    }

    public void forceReload(IEngineContext context) {
        log.debug("forceReload");
        IConnectionPool pool = context.getUserConnectionPool();
        pool.markForceReload();
    }

    public Boolean isAuthenticated(IEngineContext context) {
        log.debug("isAuthenticated()");
        IUser user = context.getUser();
        Boolean result = (user == null) ? Boolean.FALSE : Boolean.TRUE;
        return result;
    }

    public XResult[] query(final XQueryPath[] queries, IEngineContext context) throws InvalidObjectPathException {
        log.debug("query");
        IConnectionPool pool = context.getUserConnectionPool();
        PerformanceTimer timer = new PerformanceTimer("EnginedService.query");
        timer.start();
        CompositeDbTask compositeTask = constructCompositeQueryTask(queries, pool);
        compositeTask.execute();
        XResult[] result = getResultFromCompositeQuery(compositeTask);
        timer.report();
        return result;
    }

    private XResult[] getResultFromCompositeQuery(CompositeDbTask compositeTask) {
        int size = compositeTask.getTaskCount();
        XResult[] result = new XResult[size];
        for (int i = 0; i < size; i++) {
            DataFetcherTask task = (DataFetcherTask) compositeTask.getTask(i);
            result[i] = task.getResult();
        }
        return result;
    }

    private CompositeDbTask constructCompositeQueryTask(final XQueryPath[] queries, IConnectionPool pool) {
        CompositeDbTask compositeTask = new CompositeDbTask();
        compositeTask.setConnectioPool(pool);
        for (int i = 0; i < queries.length; i++) {
            DataFetcherTask task = new DataFetcherTask();
            task.setQuery(queries[i]);
            compositeTask.addTask(task);
        }
        return compositeTask;
    }

    public void updateData(final XPath cube, final IXPoint point, final IResultElement value, IEngineContext context) throws InvalidObjectPathException {
        log.debug("updateData");
        DataUpdateTask task = new DataUpdateTask();
        task.setConnectioPool(context.getUserConnectionPool());
        task.setCube(cube);
        task.setPoint(point);
        task.setValue(value);
        task.setLocale(context.getLocale());
        task.execute();
    }

    public String saveView(final XViewPath viewPath, IConnectionPool pool) throws InvalidObjectPathException {
        log.debug("saveView");
        SaveViewTask task = new SaveViewTask();
        task.setConnectioPool(pool);
        task.setViewPath(viewPath);
        task.execute();
        return task.getId();
    }

    public IClientProperties getClientProperties(IEngineContext context) {
        log.debug("getClientProperties");
        IClientProperties result = context.getConfiguration().getClientProperties();
        Locale locale = context.getLocale();
        NumberFormat format = NumberFormat.getInstance(locale);
        String number = format.format(1.1);
        result.setFloatSeparator(number.charAt(1));
        number = format.format(1000);
        char c = number.charAt(1);
        result.setDecimalSeparator(c + "");
        return result;
    }

    public XObject[] loadChildren(XPath path, int type, IEngineContext ctx) throws InvalidObjectPathException {
        log.debug("loading children of " + path + " type '" + XHelper.typeToString(type) + "'");
        LoadChildrenTask task = new LoadChildrenTask();
        task.setConnectioPool(ctx.getUserConnectionPool());
        task.setPath(path);
        task.setType(type);
        task.setConfiguration(ctx.getConfiguration());
        task.execute();
        return task.getResult();
    }

    public XElement[] getParentsOf(XPath contextPath, String elementId, IEngineContext ctx) throws InvalidObjectPathException {
        log.debug("requesting parents of " + elementId + " in context " + contextPath);
        GetParentsOfTask task = new GetParentsOfTask(contextPath, elementId);
        task.setConnectioPool(ctx.getUserConnectionPool());
        task.execute();
        return task.getResult();
    }

    public XDefaultView loadDefaultView(XPath path, IEngineContext context) throws InvalidObjectPathException {
        log.debug("load default view for " + path);
        DefaultViewConstuctorTask task = new DefaultViewConstuctorTask(path);
        task.setConnectioPool(context.getUserConnectionPool());
        task.execute();
        XDefaultView r = task.getResult();
        return r;
    }

    public boolean checkExistance(XPath contextPath, String elementId, IEngineContext ctx) throws InvalidObjectPathException {
        log.debug("checkExistance : " + contextPath + " for " + elementId);
        ElementExistanceCheckerTask task = new ElementExistanceCheckerTask(contextPath, elementId);
        task.setConnectioPool(ctx.getUserConnectionPool());
        task.execute();
        return task.getResult();
    }

    public boolean checkExistance(XPath path, IEngineContext ctx) {
        log.debug("checkExistance : " + path);
        ExistanceCheckerTask task = new ExistanceCheckerTask(path);
        task.setConnectioPool(ctx.getUserConnectionPool());
        try {
            task.execute();
        } catch (InvalidObjectPathException e) {
            throw new RuntimeException(e);
        }
        return task.getResult();
    }

    public XFolder loadFavoriteViews(IEngineContext context) {
        log.debug("loadFavoriteViews");
        XFolder result = new XFolder();
        try {
            IConnectionPool connectionPool = context.getUserConnectionPool();
            final int size = connectionPool.getServerCount();
            for (int i = 0; i < size; i++) {
                String host = connectionPool.getServerNames()[i];
                String service = connectionPool.getServerServices()[i];
                log.debug("loading favoriteViews for " + host);
                Connection connection = connectionPool.getConnection(host, service);
                LoadFavoriteViewsTask task = new LoadFavoriteViewsTask();
                task.setConnection(connection);
                task.execute();
                XFolder connectionFolder = task.getResult();
                if (connectionFolder != null) result.addChild(connectionFolder);
            }
        } catch (InvalidObjectPathException iope) {
            log.error("Fail to load all favorite views", iope);
        }
        return result;
    }

    public XObject loadChild(XPath path, String childId, int type, IEngineContext context) throws InvalidObjectPathException {
        log.debug("loadChild");
        XObject[] children = loadChildren(path, type, context);
        XObject result = XArrays.findById(children, childId);
        return result;
    }

    public XObject loadChildByName(XPath path, String name, int type, IEngineContext context) throws InvalidObjectPathException {
        log.debug("loadChildByName");
        XObject[] children = loadChildren(path, type, context);
        XObject result = XArrays.findByName(children, name);
        return result;
    }
}
