package org.vramework.mvc.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.vramework.commons.config.VConf;
import org.vramework.commons.datatypes.Dtu;
import org.vramework.commons.datatypes.Strings;
import org.vramework.commons.datatypes.converters.IConverterRegistry;
import org.vramework.commons.logging.ICallLevelAwareLog;
import org.vramework.commons.resources.exceptions.ResourceErrors;
import org.vramework.commons.resources.exceptions.ResourceException;
import org.vramework.commons.utils.VSert;
import org.vramework.mvc.IAction;
import org.vramework.mvc.IActionRegistry;
import org.vramework.mvc.IConversation;
import org.vramework.mvc.IMarkupTag;
import org.vramework.mvc.INextStep;
import org.vramework.mvc.ITemplateResolver;
import org.vramework.mvc.IViewRendererGenerator;
import org.vramework.mvc.IViewRendererRegistry;
import org.vramework.mvc.IViewResolver;
import org.vramework.mvc.NextStepAction;
import org.vramework.mvc.NextStepDownload;
import org.vramework.mvc.NextStepRenderHelp;
import org.vramework.mvc.NextStepRenderView;
import org.vramework.mvc.conf.Mvc;
import org.vramework.mvc.exceptions.MvcErrors;
import org.vramework.mvc.exceptions.MvcException;
import org.vramework.mvc.impl.ViewRendererRegistry;

/**
 * <strong>Startup Phase</strong>
 * <ul>
 *   <li> {@link MvcServletContextListener}
 *     <ul>
 *       <li>
 *         Looks up the config object class from the servlet context attribute {@link MvcServletContextListener#ConfigClass}
 *         and instantiates it
 *        </li>
 *       <li>
           Looks up the config file from the servlet context attribute {@link MvcServletContextListener#ConfigFileName}
 *       </li>
 *       <li>Calls the <code>fill()</code> method of the config object which creates the beans and sets them into the config
 *         object
 *       </li>
 *     </ul>
 *   <li> 
 *     {@link #init()}: Retrieves the attributes from config object and sets them as members
 *   </li>
 * </ul> 
 * <br />
 * 
 * 
 * <strong>Request Rendering Cycle</Strong>
 * <ul>
 *   <li><strong>Conversation Ownership</strong> <br />
 *     MVC supports <strong>conversations</strong> within a session. A conversation is retrieved from the session (based on
 *     <code>conv</code> request parameter). If not found, a new one is created. The current request (= thread) takes
 *     ownership of the conversation. (It will release the ownership at the end of the request.) If two threads try to take
 *     ownership, an exception is thrown because this happens only in case the user makes "a mistake", e.g. copies an URL
 *     including the conversation id to another browser window and/or is quick enough to issue two requests with the same
 *     conversation id. See {@link IConversation}.
 *   </li>
 *   <li><strong>Action and view path extraction</strong> <br />
 *     The path of request is extracted. Sample: URL == <code>/MVC-Sample/crm/CustomerList</code>, the path will be
 *     <code> crm/CustomerList</code>.
 *   </li>
 *   <li><strong>Action Calling</strong>
 *     <ul>
 *       <li>{@link IActionRegistry#getOrLoadAction(String)} gets the cached action or to loads it. The default name of the
 *         action is constructed from the path, e.g. <code>crm.CustomerListAction</code>
 *       </li>
 *       <li>
 *         {@link IAction#action(IConversation, Map, org.vramework.mvc.ActionDef, org.vramework.mvc.ActionDef, org.vramework.mvc.ActionDef, Object)}
 *         performs the action. See this method's doc for a detailed description of how the HTTP parameters are processed and
 *         how they define the flow of the application.
 *       </li>
 *       <li>The action returns a {@link INextStep} which contains a model and:
 *         <ul>
 *           <li>The path of the view which should be rendered</li>
 *           <li>Or another {@link IAction} which should be called</li>
 *         </ul>
 *       </li>
 *     </ul> 
 *   </li>
 *   <li><strong>View Rendering</strong> <br />
 *     <ul>
 *       <li>
 *         A renderer for the request path (e.g. <code> crm/CustomerList</code>) is searched via
 *         {@link ViewRendererRegistry#getOrGenerateRenderer(String)} <br />
 *         If renderer not found:
 *         <ul>
 *           <li>Creates renderer, compiles it, loads it and registers it. The class name is constructed from the path, e.g.
 *             <code>crm.CustomerListRenderer</code>
 *           </li>
 *           <li>A page can contain <strong>compile time tags</strong> (they generate Java code which generates the target markup,
 *             e.g. HTML) and <strong>runtime tags</strong> (they create the target markup directly). <br />
 *             When MVC pages are parsed, the tags are converted to {@link IMarkupTag}s. The JavaDoc of this class describes
 *             the grammar for tags. <br />
 *             Compile time tags are evaluated through {@link ICompileTimeTagRenderer#render} which generates the Java code for
 *             rendering the tag. <br />
 *             See {@link IViewRendererGenerator#generate(String, String)} for details. <br />
 *           </li>
 *         </ul>  
 *       </li>
 *       <li>A page can be based on a <strong>template</strong>. The replacing of template markup by the page's markup is done
 *         during the creation of the {@link IViewRenderer} by an {@link ITemplateResolver}. For details on templates please see
 *         {@link ITemplateResolver}.
 *       </li>
 *       <li>Render view via
 *         {@link IViewRenderer#render(HttpServletRequest, HttpServletResponse, NextStepRenderView, IConversation)}.
 *         <strong>Note:</strong> No "forward" is used for rendering. It is a pure Java call. <br />
 *         The renderer renders the static HTML parts and calls {@link IRuntimeTagRenderer}s for each runtime tag
 *       </li>
 *     </ul>  
 *   </li>    
 * </ul> 
 * 
 * @author thomas.mahringer
 */
public class MvcServlet extends HttpServlet {

    private static final long serialVersionUID = 212284907725454055L;

    protected final ICallLevelAwareLog _log = VConf.getCallLevelAwareLogger(this);

    private AtomicLong _conversationIds = new AtomicLong();

    /**
   * These members are initialized in {@link #init()}.
   */
    private IViewResolver _viewResolver;

    private IActionRegistry _actionRegistry;

    private IConverterRegistry _converterRegistry;

    private IViewRendererRegistry _rendererRegistry;

    private IViewRendererGenerator _viewRendererGenerator;

    /**
   * Extracts the config settings which are set into the servlet context by {@link MvcServletContextListener},
   * 
   * @see javax.servlet.GenericServlet#init()
   */
    @Override
    public void init() throws ServletException {
        super.init();
        _log.enter("init");
        setViewResolver(Mvc.getViewResolver());
        setActionRegistry(Mvc.getActionRegistry());
        setConverterRegistry(Mvc.getConverterRegistry());
        setRendererRegistry(Mvc.getRendererRegistry());
        setViewRendererGenerator(Mvc.getViewRendererGenerator());
        System.out.println("------------------------------------");
        System.out.println("MvcServlet::init; ready initialized");
        System.out.println("------------------------------------");
    }

    /**
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        _log.enter("doGet", new Object[] { req.getRequestURI(), ", ", req.getQueryString() });
        _log.incCallLevel();
        Throwable alReadyThrowable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        try {
            IConversation conversation = null;
            if (alReadyThrowable == null) {
                conversation = getConversation(req);
            } else {
                HttpSession session = req.getSession(true);
                conversation = new HttpConversation(1, session);
                session.setAttribute("Conversation" + 1, conversation);
            }
            conversation.takeOwnership();
            try {
                resp.setContentType("text/html;charset=UTF-8");
                String contextPath = req.getContextPath();
                String uri = req.getRequestURI();
                String actionPath = uri.substring(contextPath.length() + 1);
                IAction action = getOrLoadAction(actionPath, resp);
                if (action == null) return;
                INextStep nextStep = callAction(req, action, actionPath, conversation, alReadyThrowable != null, req);
                if (nextStep instanceof NextStepDownload) {
                    NextStepDownload nextStepDownload = (NextStepDownload) nextStep;
                    download(nextStepDownload, resp);
                } else if (nextStep instanceof NextStepRenderView) {
                    NextStepRenderView nextStepRenderView = (NextStepRenderView) nextStep;
                    renderViewOrHelp(nextStepRenderView, req, resp, conversation);
                } else {
                    throw new MvcException(MvcErrors.ActionWrongNextStep, new Object[] { nextStep, "RenderView or Download" });
                }
            } finally {
                if (conversation != null) {
                    conversation.releaseOwnership();
                }
            }
        } catch (Throwable t) {
            handleException(req, resp, t, alReadyThrowable);
        } finally {
            _log.decCallLevel();
        }
    }

    /**
   * Extracts the conversation id from the request and looks up the conversation in the session attributes.<br />
   * <strong>Note</strong>: Creates a session if necessary.
   * 
   * @param req
   * @return The conversation
   */
    protected IConversation getConversation(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        String conversationStr = req.getParameter("conv");
        long convId;
        IConversation conversation = null;
        if (conversationStr == null) {
            convId = _conversationIds.incrementAndGet();
            conversation = new HttpConversation(convId, session);
            session.setAttribute("Conversation" + convId, conversation);
        } else {
            convId = Long.parseLong(conversationStr);
            conversation = (IConversation) session.getAttribute("Conversation" + convId);
            if (conversation == null) {
                throw new MvcException(MvcErrors.ConversationInvalidId, new Long(convId));
            }
        }
        return conversation;
    }

    /**
   * Loads the action from our action registry.
   * 
   * @param actionPath
   * @param resp
   * @return The action.
   */
    protected IAction getOrLoadAction(String actionPath, HttpServletResponse resp) {
        try {
            IAction action = getActionRegistry().getOrLoadAction(actionPath);
            return action;
        } catch (ResourceException ex) {
            if (ex.getKey() == ResourceErrors.ClassLoad_NotFound) {
                try {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return null;
                } catch (IOException e) {
                    throw new MvcException(e);
                }
            } else {
                throw ex;
            }
        }
    }

    /**
   * Calls the action.
   * 
   * @param req
   * @param action
   * @param actionPath
   * @param conversation
   * @param errorHandling
   * @param additionalData
   * @return the next step.
   */
    protected INextStep callAction(HttpServletRequest req, IAction action, String actionPath, IConversation conversation, boolean errorHandling, Object additionalData) {
        Map<String, String[]> params = Dtu.cast(req.getParameterMap());
        INextStep nextStep = action.action(conversation, actionPath, params, errorHandling, additionalData);
        if (nextStep instanceof NextStepAction) {
            NextStepAction nextStepAction = (NextStepAction) nextStep;
            actionPath = nextStepAction.getActionDef().getPath();
            IAction nextAction = getActionRegistry().getOrLoadAction(nextStepAction.getActionDef().getPath());
            nextStep = (NextStepRenderView) nextAction.action(conversation, params, nextStepAction.getActionDef(), null, null, additionalData);
        }
        return nextStep;
    }

    /**
   * Renders the view according to the passed relative request path and the viewAndModel. <br />
   * The details of the rendering lifecycle are described in the class description.
   * 
   * @param nextStepRenderView
   * @param req
   * @param resp
   * @param conversation
   */
    protected void renderViewOrHelp(NextStepRenderView nextStepRenderView, HttpServletRequest req, HttpServletResponse resp, IConversation conversation) {
        String viewPath = getViewResolver().getFullPathOfView(nextStepRenderView.getView());
        File viewFile = new File(viewPath);
        if (!viewFile.exists()) {
            if (nextStepRenderView instanceof NextStepRenderHelp) {
                NextStepRenderHelp nextStepRenderHelp = (NextStepRenderHelp) nextStepRenderView;
                viewPath = getViewResolver().getFullPathOfView(nextStepRenderHelp.getDefaultHelpPage());
                viewFile = new File(viewPath);
                if (!viewFile.exists()) {
                    try {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    } catch (IOException e) {
                        throw new MvcException(e);
                    }
                }
                nextStepRenderView.setView(nextStepRenderHelp.getDefaultHelpPage());
            } else {
                try {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                    throw new MvcException(e);
                }
                return;
            }
        }
        IViewRenderer renderer = getRendererRegistry().getOrGenerateRenderer(nextStepRenderView.getView());
        renderer.render(req, resp, nextStepRenderView, conversation);
    }

    /**
   * Downloads a binary content.
   * 
   * @param nextStepDownload
   *          The next step. It contains the binary contents.
   * @param resp
   */
    protected void download(NextStepDownload nextStepDownload, HttpServletResponse resp) {
        String type = Strings.concat("attachment; filename=\"", nextStepDownload.getName(), "\"");
        if (nextStepDownload.getSize() > Integer.MAX_VALUE) {
            throw new MvcException(MvcErrors.ActionDownloadCannotBeBigger, new Object[] { Integer.MAX_VALUE, nextStepDownload.getSize() });
        }
        resp.setContentLength((int) nextStepDownload.getSize());
        resp.setContentType(nextStepDownload.getMimeType());
        resp.setHeader("Content-Disposition", type);
        try {
            OutputStream os = resp.getOutputStream();
            try {
                os.write(nextStepDownload.getBytes());
            } finally {
                os.close();
            }
        } catch (Exception e) {
            throw new MvcException(e);
        }
    }

    /**
   * Test slow requests and correct conversation handling.
   */
    protected final void waitToTestCorrectConversationHandling() {
        for (int i = 0; i < 5000; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * Handles an exception
   * 
   * @param req
   * @param resp
   * @param t
   *          The current throwable to handle.
   * @param alReadyThrowable
   *          This parameter != null if the current request is already an error handling request. E.g. if web.xml
   *          defines an error page which is an action. In case we have an exception in the exception handler, we write
   *          directly to the output writer of the response.
   * @throws ServletException
   */
    protected void handleException(HttpServletRequest req, HttpServletResponse resp, Throwable t, Throwable alReadyThrowable) throws ServletException {
        if (alReadyThrowable != null) {
            PrintWriter out;
            try {
                out = resp.getWriter();
            } catch (IOException e) {
                throw new MvcException(e);
            }
            try {
                if (alReadyThrowable instanceof ExceptionInInitializerError) {
                    ExceptionInInitializerError eii = (ExceptionInInitializerError) alReadyThrowable;
                    out.write("<hr />An exception has occured while handling an exception.<br />");
                    out.write("It prevented the error page from showing correctly. The original exception was: <br />");
                    alReadyThrowable.printStackTrace(out);
                    out.write("Excpetion which happened in initializer: <br />");
                    eii.getException().printStackTrace(out);
                } else {
                    out.write("<hr />An exception has occured while handling an exception. <br />");
                    out.write("It prevented the error page from showing correctly. The orignal exception was: <br />");
                    alReadyThrowable.printStackTrace(out);
                    out.write("<hr />Exception while showing exception page: <br />");
                    t.printStackTrace(out);
                }
                out.write("<hr />The current exception is: <br />");
                t.printStackTrace(out);
            } finally {
                out.close();
            }
            return;
        } else {
            throw new ServletException(t);
        }
    }

    /**
   * @return the templateResolver
   */
    public final IViewResolver getViewResolver() {
        return _viewResolver;
    }

    /**
   * @param templateResolver
   *          the templateResolver to set
   */
    public final void setViewResolver(IViewResolver templateResolver) {
        VSert.argNotNull("templateResolver", templateResolver);
        this._viewResolver = templateResolver;
    }

    /**
   * @return the actionRegistry
   */
    public final IActionRegistry getActionRegistry() {
        return _actionRegistry;
    }

    /**
   * @param actionRegistry
   */
    public final void setActionRegistry(IActionRegistry actionRegistry) {
        VSert.argNotNull("actionRegistry", actionRegistry);
        _actionRegistry = actionRegistry;
    }

    /**
   * @return the converterRegistry
   */
    public final IConverterRegistry getConverterRegistry() {
        return _converterRegistry;
    }

    /**
   * @param converterRegistry
   *          the converterRegistry to set
   */
    public final void setConverterRegistry(IConverterRegistry converterRegistry) {
        VSert.argNotNull("converterRegistry", converterRegistry);
        _converterRegistry = converterRegistry;
    }

    /**
   * @return the rendererRegistry
   */
    public final IViewRendererRegistry getRendererRegistry() {
        return _rendererRegistry;
    }

    /**
   * @param rendererRegistry
   *          the rendererRegistry to set
   */
    public final void setRendererRegistry(IViewRendererRegistry rendererRegistry) {
        VSert.argNotNull("rendererRegistry", rendererRegistry);
        _rendererRegistry = rendererRegistry;
    }

    /**
   * @return the viewRendererGenerator
   */
    public final IViewRendererGenerator getViewRendererGenerator() {
        return _viewRendererGenerator;
    }

    /**
   * @param viewRendererGenerator
   *          the viewRendererGenerator to set
   */
    public final void setViewRendererGenerator(IViewRendererGenerator viewRendererGenerator) {
        VSert.argNotNull("viewRendererGenerator", viewRendererGenerator);
        _viewRendererGenerator = viewRendererGenerator;
    }
}
