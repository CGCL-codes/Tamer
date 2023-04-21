package portal.presentation.base;

import com.lutris.appserver.server.httpPresentation.HttpPresentationComms;
import com.lutris.appserver.server.httpPresentation.ClientPageRedirectException;
import hambo.app.util.Link;
import hambo.app.core.ClientSideRedirect;
import hambo.app.core.PageRedirect;
import hambo.app.base.RedirectorBase;
import hambo.messaging.FolderException;
import hambo.pim.PortalException;

/**
 * Base class for presentation objects in PIM. You should only extend
 * this base class iff your presentation object can throw {@link
 * PortalExcption}. The PortalException is only used in the PIM
 * application module and therefore it is not known by the Hambo
 * ApplicationFramework.  
 * 
 * */
public abstract class PIMRedirectorBase extends RedirectorBase {

    /**
     * Construct a new PIMRedirectorBase page.
     * @param protect if true, anonymous access is denied.
     */
    protected PIMRedirectorBase(boolean protect) {
        super(protect);
    }

    /**
     * Construct a new PIMRedirectorBase page, with public access.
     */
    protected PIMRedirectorBase() {
        super();
    }

    /**
     * Do some initializing and call {@link #processPage}.
     * All exceptions here (i.e. (allmost) all exceptions that's not caught
     * elsewhere) results in the user being redirected to the error page.
     * This is a copy of {@link RedirectorBase#run} except that it handles
     * FolderException.
     * @param comms provides access to the request and the assigned session.
     */
    public void run(HttpPresentationComms comms) {
        this.comms = comms;
        try {
            setCommonVariables();
            checkAccess();
            processPage();
            logError("RedirectorBase: processPage returned.");
            getContext().removeSessionAttribute("temp1");
            getContext().removeSessionAttribute("body");
            throwRedirect("error");
        } catch (FolderException err) {
            Link link = removeLastHistoryLink();
            switch(err.getIntCause()) {
                case FolderException.FOLDER_ALREADY_EXIST:
                    link.addParam("err", "err_folder");
                    break;
                case FolderException.TOO_MANY_FOLDERS:
                    fireUserEvent("meMaxFolders");
                    link.addParam("err", "err_maxnroffoldersexceeded");
                    break;
                case FolderException.CANT_REMOVE_FOLDER:
                    link.addParam("err", "err_remove_folder");
                    break;
                case FolderException.NO_SUCH_FOLDER:
                    link.addParam("err", "err_no_folder");
                    break;
                default:
                    logError("Unknown kind of FolderException (" + err.getCause() + ")", err);
                    link = new Link("error");
            }
            doThrowRedirect(new ClientSideRedirect(link));
        } catch (ClientPageRedirectException redirect) {
            Link link = new Link(redirect.getUrl());
            doThrowRedirect(new ClientSideRedirect(link));
        } catch (PageRedirect redirect) {
            doThrowRedirect(redirect);
        } catch (Throwable err) {
            logError("Exception in PIMRedirectorBase.run", err);
            doThrowRedirect(new ClientSideRedirect(new Link("error")));
        } finally {
            try {
                unregisterContext();
            } catch (RuntimeException ex) {
            }
        }
    }

    /**
     * Overrides the {@link
     * portal.resentation.base.PresentationBase#doThrowRedirect} 
     *
     * <p>If the parameter <code>redir</code> isn't a instance of {@link
     * portal.business.exceptions.PortalException} the method is
     * equivalent to the super class method.
     *
     * <p>Otherwise handle a {@link
     * portal.business.exceptions.PortalException}. Different errors
     * results in redirect, back or to an error page, with different
     * parameters.  <p>Classes that need some specific error handling
     * should override this method (and end their implementation with
     * a call to <code>super.handlePortalException(err)</code>).
     *
     * @param redir the exception.
     * */
    protected void doThrowRedirect(PageRedirect redir) {
        if (redir instanceof PortalException) {
            PortalException err = (PortalException) redir;
            comms.exception = err;
            Link link = null;
            switch(err.getErrorCode()) {
                case PortalException.MISSING_INPUT_ERROR:
                case PortalException.REDIRECT:
                case PortalException.INPUT_FORMAT_ERROR:
                case PortalException.ATTACH_LIMIT_REACHED_ERROR:
                case PortalException.FOLDER_ALREADY_EXIST:
                case PortalException.REDIRECT_TO_REFERER:
                case PortalException.REDIRECT_WITH_OLD_PARAMS:
                    {
                        logDebug1("PortalException in PortalPage.run. Redirecting to referer.");
                        link = getHistoryLink();
                        String param = (String) getContext().getSessionAttribute("temp1");
                        if (param != null) {
                            link.removeParameters();
                            if (!param.equals("")) link.addParam(param);
                            getContext().removeSessionAttribute("temp1");
                        }
                        if (err.getMessage() != null) link.addParam("err", "(@" + err.getMessage() + "@)");
                        removeLastHistoryLink();
                        break;
                    }
                default:
                    logError("Unknown PortalException", err);
                    link = new Link();
                    link.addParam("err", "Internal error " + err);
            }
            doThrowRedirect(new ClientSideRedirect(link));
        } else {
            super.doThrowRedirect(redir);
        }
    }

    /**
     * Todo: Move this to AppFramework ...
     */
    protected long[] getLongParameters(String name, int radix) {
        String[] values = getParameterValues(name);
        if (values == null) return null;
        long[] result = new long[values.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Long.parseLong(values[i], radix);
        }
        return result;
    }

    /**
     * Todo: Move this to AppFramework ...
     */
    protected long[] getLongParameters(String name) {
        return getLongParameters(name, 10);
    }
}
