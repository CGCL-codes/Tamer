package org.larozanam.arq.base.persistencia.cookie;

import static org.apache.tapestry5.ioc.internal.util.Defense.notBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.Cookie;
import org.apache.log4j.Logger;
import org.apache.tapestry5.internal.services.PersistentFieldChangeImpl;
import org.apache.tapestry5.services.PersistentFieldChange;
import org.apache.tapestry5.services.PersistentFieldStrategy;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.larozanam.arq.excecoes.ArquiteturaException;

public class CookiePersistentField implements PersistentFieldStrategy {

    public static final String COOKIE = "cookie";

    public static final String FLASHCOOKIE = "flashcookie";

    private String persistenceType;

    private static final String SEPERATOR = "|";

    private RequestGlobals requestGlobals;

    private Request request;

    private EncoderBase64 encoder;

    private static final Logger log = Logger.getLogger(CookiePersistentField.class);

    public CookiePersistentField(RequestGlobals requestGlobals, Request request, String persistenceType) {
        this.requestGlobals = requestGlobals;
        this.request = request;
        this.persistenceType = persistenceType;
        encoder = new EncoderBase64();
    }

    public void postChange(String pageName, String componentId, String fieldName, Object newValue) {
        notBlank(pageName, "pageName");
        notBlank(fieldName, "fieldName");
        StringBuilder builder = new StringBuilder(persistenceType);
        builder.append(SEPERATOR);
        builder.append(pageName);
        builder.append(SEPERATOR);
        if (componentId != null) builder.append(componentId);
        builder.append(SEPERATOR);
        builder.append(fieldName);
        String key = builder.toString();
        if (newValue != null) {
            String value = encoder.toClient(newValue);
            createCookie(key, value);
        } else {
            deleteCookie(key);
        }
    }

    private PersistentFieldChange buildChange(Cookie cookie) {
        String value = cookie.getValue();
        if (value == null || value.isEmpty()) return null;
        String[] chunks = cookie.getName().split("\\" + SEPERATOR);
        String componentId = chunks[2];
        String fieldName = chunks[3];
        Object attribute = encoder.toValue(value);
        return new PersistentFieldChangeImpl(componentId, fieldName, attribute);
    }

    public Collection<PersistentFieldChange> gatherFieldChanges(String pageName) {
        Collection<PersistentFieldChange> changes = new ArrayList<PersistentFieldChange>();
        String fullPrefix = persistenceType + SEPERATOR + pageName + SEPERATOR;
        log.trace(pageName);
        for (Cookie cookie : getCookiesStartingWith(fullPrefix)) {
            try {
                PersistentFieldChange fieldChange = buildChange(cookie);
                if (fieldChange != null) changes.add(fieldChange);
                if (persistenceType.equals(FLASHCOOKIE)) deleteCookie(cookie.getName());
            } catch (RuntimeException e) {
                throw new ArquiteturaException("Erro ao destruir o cookie:" + cookie.getName(), e);
            }
        }
        return changes;
    }

    public void discardChanges(String pageName) {
        log.trace(pageName);
        String fullPrefix = persistenceType + SEPERATOR + pageName + SEPERATOR;
        for (Cookie cookie : getCookiesStartingWith(fullPrefix)) {
            deleteCookie(cookie.getName());
        }
    }

    private List<Cookie> getCookiesStartingWith(String prefix) {
        List<Cookie> cookieList = new ArrayList<Cookie>();
        Cookie cookies[] = requestGlobals.getHTTPServletRequest().getCookies();
        if (cookies != null) for (int i = 0; i < cookies.length; i++) if (cookies[i].getName().startsWith(prefix)) cookieList.add(cookies[i]);
        return cookieList;
    }

    private void createCookie(String name, String value) {
        log.trace(name + " - " + value);
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(request.getContextPath());
        requestGlobals.getHTTPServletResponse().addCookie(cookie);
    }

    private void deleteCookie(String name) {
        log.trace(name);
        Cookie cookie = new Cookie(name, "_");
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath());
        requestGlobals.getHTTPServletResponse().addCookie(cookie);
    }
}
