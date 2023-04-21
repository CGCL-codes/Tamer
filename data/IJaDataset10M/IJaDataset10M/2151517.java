package de.uni_leipzig.lots.webfrontend.actions;

import de.uni_leipzig.lots.webfrontend.app.SetupConfig;
import static de.uni_leipzig.lots.webfrontend.app.SetupConfig.State;
import static de.uni_leipzig.lots.webfrontend.app.SetupConfig.State.*;
import de.uni_leipzig.lots.webfrontend.formbeans.BaseForm;
import de.uni_leipzig.lots.webfrontend.http.LOTSHttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jetbrains.annotations.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Diese Aktion führt die Installation des Webfrontendes aus.
 *
 * @author Alexander Kiel
 * @version $Id: InstallRedirectToStartAction.java,v 1.6 2007/10/23 06:29:56 mai99bxd Exp $
 */
public final class InstallRedirectToStartAction extends BaseInstallAction {

    @Nullable
    @Override
    protected ActionForward execute(ActionMapping mapping, BaseForm form, LOTSHttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SetupConfig.Method method = setupConfig.getMethod();
        SetupConfig.State state = setupConfig.getState();
        SetupConfig.State forwardName;
        switch(method) {
            case standard:
                switch(state) {
                    case notStarted:
                        forwardName = State.method;
                        break;
                    case method:
                        throw new IllegalArgumentException("Illegal state during Installation. Please shut " + "down tomcat, delete all files in the data dir and try again.");
                    case database:
                        throw new IllegalArgumentException("Illegal state during Installation. Please shut " + "down tomcat, delete all files in the data dir and try again.");
                    case content:
                        forwardName = admin;
                        break;
                    case admin:
                        forwardName = mail;
                        break;
                    case library:
                        throw new IllegalArgumentException("Illegal state during Installation. Please shut " + "down tomcat, delete all files in the data dir and try again.");
                    case mail:
                        forwardName = complete;
                        break;
                    default:
                        throw new IllegalStateException();
                }
                break;
            case custom:
                switch(state) {
                    case notStarted:
                        forwardName = State.method;
                        break;
                    case method:
                        forwardName = database;
                        break;
                    case database:
                        forwardName = content;
                        break;
                    case content:
                        forwardName = admin;
                        break;
                    case admin:
                        forwardName = library;
                        break;
                    case library:
                        forwardName = mail;
                        break;
                    case mail:
                        forwardName = complete;
                        break;
                    default:
                        throw new IllegalStateException();
                }
                break;
            default:
                throw new IllegalStateException();
        }
        return mapping.findForward(forwardName.toString());
    }
}
