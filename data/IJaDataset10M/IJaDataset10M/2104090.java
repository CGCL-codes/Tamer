package salto.fwk.mvc.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import salto.fwk.mvc.ContextException;
import salto.fwk.mvc.exception.ContextStackOverflowException;
import salto.fwk.mvc.exception.InvalidContextParameterException;

/**
 * Classe contenant tous les contexts en cours
 * 
 * @author e.loiez / salto-consulting.com
 */
public class ContextContainer implements Serializable {

    /**
	 * le logger
	 */
    public static final Log logger = LogFactory.getLog(ContextContainer.class);

    /**
	 * liste des contextes supprim�s
	 */
    protected HashSet removedContexts = new HashSet();

    /**
	 * compteur pour les identifiants de context (� modifier lorsque la
	 * persistence est autoris�e)
	 */
    protected long currContextId = 0;

    protected String contextName;

    /**
	 * liste des contextes de base Exemple : processus xxx pour le client CCC
	 * processus YYY pour le client DDD
	 */
    protected LinkedList contexts = new LinkedList();

    /**
	 * map contenant l'ensemble des contextes. Cette map permet de retrouver les
	 * contextes � partir de leurs identifiants unique
	 */
    protected HashMap contextMap = new HashMap();

    protected HashMap contextCounter = new HashMap();

    /**
	 * l'identifiant du contexte actuel
	 */
    protected long currentContextId = -1;

    /**
	 * contexte courant entre le d�but et la fin de la requ�te http.
	 */
    protected long requestContextId = -1;

    protected Context requestContext;

    /**
	 * le contexte de requ�te pr�c�dent
	 */
    private long previousRequestContextId;

    /**
	 * Met � jour le contexte courant avec le context pass� en param�tre
	 * 
	 * @param context
	 *            le nouveau contexte courant
	 */
    public void setContext2Current(Context context) {
        setCurrentContext(context);
        linkContext(context);
    }

    /**
	 * fonction permettant de raccrocher un context au container. Cette fonction
	 * met � jour les listes et les maps
	 * 
	 * @param ctx
	 */
    private void linkContext(Context ctx) {
        contexts.add(ctx);
        contextMap.put(new Long(ctx.contextId), ctx);
        ArrayList list = (ArrayList) contextCounter.get(ctx.contextName);
        if (list == null) {
            list = new ArrayList();
            contextCounter.put(ctx.contextName, list);
        }
        list.add(ctx);
        if (ctx.parentContext != null) {
            ctx.parentContext.addNextContext(ctx);
        }
    }

    /**
	 * m�thode utilitaire permettant de r�cup�rer le container de contexte de la
	 * session courante TODO:ne pas mettre le contexte dans la session http afin
	 * de ne pas surcharger la session
	 * 
	 * @param session
	 *            la session http
	 * @return le container de contexte
	 */
    public static ContextContainer get(HttpSession session) {
        ContextContainer container = (ContextContainer) session.getAttribute("ContextContainer");
        if (container == null) {
            container = new ContextContainer();
            session.setAttribute("ContextContainer", container);
        }
        return container;
    }

    /**
	 * @return Returns the currentContext.
	 */
    public Context getCurrentContext() {
        return (Context) contextMap.get(new Long(currentContextId));
    }

    /**
	 * Positionne l'identifiant du contexte en tant que contexte courant. Attention, <b>cette fonction
	 * ne remet pas � jour l'�tat du container</b> : suppression des contextes en �tat de travail, ...
	 * @param currentContext
	 *            The currentContext to set.
	 */
    public void setCurrentContext(Context currentContext) {
        this.currentContextId = currentContext.getContextId();
        currentContext.lastActionDate = System.currentTimeMillis();
        Context parent = currentContext.getParentContext();
        Context fils = currentContext;
        while (parent != null) {
            parent.lastChildActiveContextId = fils.contextId;
            parent.lastActionDate = fils.lastActionDate;
            fils = parent;
            parent = parent.getParentContext();
        }
        logger.debug("Setting current context " + currentContextId);
    }

    /**
	 * supprime le context courant
	 */
    public void removeContext(Context ctx) {
        removedContexts.add(new Long(ctx.getContextId()));
        if (logger.isDebugEnabled()) {
            logger.debug("Suppression du context " + ((ctx.isWorkingContext()) ? " de travail" : "") + " (" + ctx.contextName + "," + ctx.contextId + ")");
        }
        contexts.remove(ctx);
        contextMap.remove(new Long(ctx.getContextId()));
        ArrayList count = (ArrayList) contextCounter.get(ctx.contextName);
        if (count.size() == 1) {
            contextCounter.remove(ctx.contextName);
        } else {
            count.remove(ctx);
        }
        if (ctx.parentContext != null) {
            ctx.parentContext.nextContexts.remove(ctx);
        }
        if (ctx.nextContexts != null) {
            while (!ctx.nextContexts.isEmpty()) {
                removeContext((Context) ctx.nextContexts.get(0));
            }
        }
    }

    /**
	 * ajoute le contexte de requ�te en tant que fils du contexte courant. Le
	 * contexte de requ�te n'a pas encore �t� mis dans la liste des contextes.
	 * 
	 * @param previousContext
	 *            la liste des noms de contexte sur lequel le contexte courant
	 *            peut �tre ajout�
	 * @return le contexte de requ�te
	 */
    public Context addRequest2CurrentContext(ArrayList previousContext) throws InvalidContextParameterException {
        Context parentContext = getContext4Add(requestContext, previousContext);
        setContext2Current(requestContext);
        if (parentContext != null) {
            parentContext.addNextContext(requestContext);
        }
        removeRequestContext();
        return requestContext;
    }

    /**
	 * positionne le contexte de requ�te en tant que contexte courant. Le
	 * contexte de requ�te n'a pas encore �t� mis dans la liste des contextes.
	 */
    public void setRequest2CurrentContext() {
        Context currentContext = getCurrentContext();
        if (currentContext != null && currentContext.isWorkingContext() && currentContext != requestContext.parentContext) {
            removeCurrentContext();
        }
        setCurrentContext(requestContext);
        linkContext(requestContext);
        removeRequestContext();
    }

    /**
	 * Supprime le contexte de requ�te. Ceci doit normalement �tre fait � la fin
	 * de l'ex�cution d'une requ�te tomb�e en erreur.
	 */
    public void removeRequestContext() {
        requestContextId = -1;
        requestContext = null;
    }

    /**
	 * Suppression de l'objet de context. Il faut supprimer tout ce qui se
	 * trouve en session http.
	 * 
	 * @param object
	 */
    public void removeAll(Object object) {
    }

    /**
	 * supprime le contexte courant
	 */
    public void removeCurrentContext() {
        Context currentContext = getCurrentContext();
        if (currentContext == null) return;
        removeContext(currentContext);
        if (currentContext.parentContext != null) {
            currentContextId = currentContext.parentContext.contextId;
        } else {
            currentContextId = -1;
        }
    }

    /**
	 * @param prefix
	 * @return
	 */
    public int getNbContext(String ctxName) {
        ArrayList list = (ArrayList) contextCounter.get(ctxName);
        if (list == null) return 0;
        return list.size();
    }

    /**
	 * Retourne tous les contextes dont le nom est pass� en param�tre
	 * 
	 * @param ctxName
	 *            nom du contexte
	 * @return tous les contextes en cours
	 */
    public Context[] getContexts(String ctxName) {
        ArrayList list = (ArrayList) contextCounter.get(ctxName);
        if (list == null) return new Context[0];
        return (Context[]) list.toArray(new Context[list.size()]);
    }

    /**
	 * Cr�e un nouveau contexte et l'ajoute au contexte courant ou � un de ses
	 * p�res. Les noms des contextes autoris�s sont sp�cifi�s dans la liste
	 * previousContext
	 * 
	 * @param name
	 *            nom du nouveau context
	 * @param previousContext
	 *            liste des noms des parents autoris�s
	 * @return le nouveau context
	 */
    public Context addNextContext(String name, ArrayList previousContext) throws InvalidContextParameterException {
        Context newContext = new Context(this, name);
        addNextContext(newContext, previousContext);
        return newContext;
    }

    /**
	 * Ajoute le contexte pass� en param�tre au contexte courant ou � un de ses
	 * p�res. Les noms des contextes autoris�s sont sp�cifi�s dans la liste
	 * previousContext
	 * 
	 * @param previousContext
	 *            liste des noms des parents autoris�s
	 */
    public void addNextContext(Context newContext, ArrayList previousContext) throws InvalidContextParameterException {
        Context parentContext = getContext4Add(newContext, previousContext);
        setContext2Current(newContext);
        if (parentContext != null) {
            parentContext.addNextContext(newContext);
        }
    }

    /**
	 * Retourne le contexte sur lequel il faut ajouter le contexte
	 * 
	 * @param context
	 *            contexte � ajouter en tant que fils
	 * @param previousContext
	 *            liste des contextes autoris�s
	 * @return le contexte parent ou null si il n'existe pas
	 */
    private Context getContext4Add(Context newContext, ArrayList previousContext) throws InvalidContextParameterException {
        Context currentContext = getCurrentContext();
        String prefix = newContext.contextName;
        if (currentContext == null) return null; else {
            if (previousContext.size() == 0) throw new InvalidContextParameterException("Un context permettant de surcharger un context doit d�finir les contextes pr�c�dents");
            if ("*".equals(previousContext.get(0))) {
                return currentContext;
            } else {
                Context parentContext = currentContext;
                boolean found = false;
                for (; parentContext != null; parentContext = parentContext.parentContext) {
                    for (int i = 0; i < previousContext.size(); i++) {
                        if (prefix.equals(previousContext.get(i))) {
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    } else {
                        if (parentContext.isWorkingContext()) {
                            removeContext(parentContext);
                        }
                    }
                }
                if (found) return parentContext; else return null;
            }
        }
    }

    /**
	 * Positionne le contexte en tant que contexte de requ�te. Ce contexte sera rajout� � la liste des
	 * contextes uniquement si le traitement s'est correctement effectu�.
	 * 
	 * @param name
	 *            le nom du nouveau contexte de requ�te
	 * @return le contexte de requ�te
	 */
    public void setRequestContext(Context ctx) {
        requestContext = ctx;
        requestContextId = requestContext.contextId;
    }

    /**
	 * M�thode appel�e au d�but du traitement d'une requ�te. Cette m�thode
	 * v�rifie si le contexte peut �tre cr��
	 * <ol>
	 * <li>met � jour le contexte de requ�te pr�c�dent.
	 * <li>Cr�e une nouvelle requ�te si cela est autoris�
	 * </ol>
	 */
    public void verifyRequestContext(Context ctx, int maxCtx) throws ContextStackOverflowException {
        previousRequestContextId = requestContextId;
        ArrayList list = (ArrayList) contextCounter.get(ctx.contextName);
        if (list != null && list.size() >= maxCtx) {
            logger.debug("nombre de context trop important pour " + ctx.getUniqueSessionId());
            int nbContext = list.size();
            long parentCtxId = -1;
            if (ctx.parentContext != null) {
                parentCtxId = ctx.parentContext.contextId;
            }
            for (int i = 0; i < list.size(); i++) {
                Context existingContext = (Context) list.get(i);
                if (existingContext.parentContext != null) {
                    if (existingContext.parentContext.contextId == parentCtxId) {
                        if (existingContext.isWorkingContext()) {
                            removeContext(existingContext);
                            nbContext--;
                        }
                    } else {
                        nbContext--;
                    }
                } else if (parentCtxId == -1 && existingContext.isWorkingContext()) {
                    removeContext(existingContext);
                    nbContext--;
                }
            }
            if (nbContext >= maxCtx) {
                logger.warn("Nombre de context max atteint pour " + ctx.contextName);
                throw new ContextStackOverflowException();
            }
        }
        requestContext = ctx;
        requestContextId = ctx.contextId;
    }

    /**
	 * @return Returns the contexts.
	 */
    public LinkedList getContexts() {
        return contexts;
    }

    /**
	 * @return Returns the requestContext.
	 */
    public Context getRequestContext() {
        if (requestContextId == -1) return null;
        return requestContext;
    }

    /**
	 * retourne le contexte actuel de la requ�te. Ce contexte est soit le
	 * contexte de requ�te, soit le contexte courant.
	 */
    public Context getActualContext() {
        if (requestContextId != -1) return requestContext;
        return getCurrentContext();
    }

    /**
	 * Permet de sp�cifier le parent du contexte de requ�te. Cette fonction ne
	 * supprime les contextes en �tat de travail.
	 * 
	 * @param allowedPreviousCtx
	 *            liste des noms des contextes autoris�s
	 */
    public void setParentContext(Context ctx, ArrayList allowedPreviousCtx) {
        Context parentContext = getCurrentContext();
        if (parentContext == null) return;
        boolean found = false;
        boolean remove = true;
        for (; parentContext != null && !found; parentContext = parentContext.parentContext) {
            if (allowedPreviousCtx == null) {
                if (remove && parentContext.isWorkingContext()) {
                    removeContext(parentContext);
                } else {
                    break;
                }
            } else {
                if (allowedPreviousCtx.contains(parentContext.contextName)) {
                    ctx.setParentContext(parentContext);
                    found = true;
                } else if (remove && parentContext.isWorkingContext()) {
                    removeContext(parentContext);
                } else {
                    remove = false;
                }
                if (found) break;
            }
        }
    }

    /**
	 * Permet de supprimer les fils du contexte pass� en param�tre
	 * 
	 * @param context le contexte dont les fils sont � supprimer
	 * @param child le fils qu'il ne faut pas supprimer
	 */
    public void removeChildWorkingContext(Context context, Context child) {
        logger.debug(" suppression des fils en �tat 'travail' du context " + context.getUniqueSessionId());
        if (context == null) return;
        List childs = context.getNextContexts();
        if (childs == null) return;
        Context ctx;
        for (int i = 0; i < childs.size(); i++) {
            ctx = (Context) childs.get(i);
            if (ctx.getContextId() != child.getContextId() && ctx.isWorkingContext()) {
                removeContext(ctx);
            }
        }
    }

    /**
	 * Force le contexte courant comme �tant le context dont identifiant est
	 * pass� en param�tre.
	 * 
	 * Cette fonction remet le container en �tat : on supprime les contextes en �tat de 
	 * travail.
	 * 
	 * @param contextId l'identifiant du nouveau contextId
	 */
    public Context setCurrentContextId(long contextId) throws ContextException {
        Context curr = (Context) contextMap.get(new Long(contextId));
        if (curr == null) {
            throw new ContextException("Le context n'existe pas ou plus");
        }
        Context currentContext = getCurrentContext();
        while (currentContext != null && currentContext.contextId != contextId && currentContext.isWorkingContext()) {
            removeCurrentContext();
            currentContext = getCurrentContext();
        }
        setCurrentContext(curr);
        return curr;
    }

    /**
	 * supprime l'int�gralit� du context
	 */
    public void clear() {
        logger.warn("Suppression de tous les contexts");
        contextCounter.clear();
        contextMap.clear();
        contexts.clear();
        currentContextId = -1;
        requestContextId = -1;
    }

    /**
	 * retourne le contexte dont l'identifiant est pass� en param�tre
	 * 
	 * @param ctxId identifiant du contexte � retourver
	 */
    public Context getContext4id(long ctxId) {
        return (Context) contextMap.get(new Long(ctxId));
    }

    /**
	 * retourne la table des contextes supprim�s
	 * @return
	 */
    public HashSet getRemovedContexts() {
        return removedContexts;
    }
}
