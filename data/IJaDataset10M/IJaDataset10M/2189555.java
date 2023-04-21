package com.gwtaf.core.client.history;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.gwtaf.core.client.appstate.internal.IllegalAppStateException;
import com.gwtaf.core.client.util.RootPanelEx;
import com.gwtaf.core.shared.util.StringUtil;
import com.gwtaf.core.shared.util.URLUtil;

/**
 * Base class of applications which uses the URL token to identify special application views.<br>
 * 
 * @author Matthias Hübner
 * 
 * @param <T> Type of the application state.
 */
public abstract class AbstractHistoryManager<T> implements IHistoryManager {

    private static final class Item<T> {

        private HistoryToken token;

        private final T state;

        public Item(T state, HistoryToken token) {
            if (token == null) throw new IllegalArgumentException("token == null");
            if (state == null) throw new IllegalArgumentException("state == null");
            this.state = state;
            this.token = token;
        }

        public boolean update(HistoryToken token) {
            if (token.getPrimaryToken().equals(token.getPrimaryToken()) && !token.equals(token)) {
                this.token = token;
                return true;
            } else return false;
        }
    }

    private final java.util.List<Item<T>> activeStates = new LinkedList<Item<T>>();

    private Item<T> currentItem;

    private String contextId;

    private String defaultToken;

    public enum RunState {

        INIT, STARTUP, RUNNING
    }

    ;

    private RunState runstate = RunState.INIT;

    private boolean ignoreToken;

    private boolean goon = true;

    protected AbstractHistoryManager() {
        HistoryManager.init(this);
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                onHistoryChange(event.getValue());
            }
        });
    }

    public void startup(String defaultToken) {
        if (runstate != RunState.INIT) throw new IllegalStateException("unexpected startup");
        this.runstate = RunState.STARTUP;
        this.defaultToken = defaultToken;
        HistoryToken token = new HistoryToken(History.getToken());
        if (StringUtil.isNull(token.getId())) {
            show(URLUtil.assembleContextToken(token.getContextId(), defaultToken));
        } else show(token.getValue());
    }

    public void reset() {
        resetIntern();
        ignoreToken = true;
        runstate = RunState.INIT;
        History.newItem("");
        onReset();
    }

    private void resetIntern() {
        this.contextId = null;
        this.currentItem = null;
        this.activeStates.clear();
    }

    public void show(String id, Object param) {
        show(buildToken(id, param));
    }

    public void show(String token) {
        if (token == null) token = "";
        token = addContextId(token);
        if (token.equals(History.getToken())) onHistoryChange(token); else History.newItem(token);
    }

    public String addContextId(String token) {
        if (StringUtil.isValid(this.contextId) && token != null && !URLUtil.hasContextId(token)) return URLUtil.assembleContextToken(contextId, token);
        return token;
    }

    public void initContextId(String contextId) {
        if (this.contextId != null || this.currentItem != null) throw new IllegalStateException("initContext only allowed after reset or creation.");
        if (!URLUtil.isValidID(contextId)) throw new IllegalArgumentException("Invalid context identifier: " + contextId);
        this.contextId = contextId;
    }

    public String getContextId() {
        return contextId;
    }

    private void checkContext(HistoryToken token) throws IllegalAppStateException {
        if (!(token.getContextId() == null && contextId == null || contextId != null && token.getContextId() != null && token.getContextId().equals(contextId))) throw new IllegalAppStateException("Unexpected context change should=" + contextId + " is=" + token.getContextId());
    }

    public T getState() {
        return this.currentItem.state;
    }

    private void onHistoryChange(String token) {
        onHistoryChange(new HistoryToken(token));
    }

    private void onHistoryChange(HistoryToken token) {
        if (ignoreToken) {
            ignoreToken = !goon;
            return;
        }
        try {
            if (StringUtil.isNull(token.getId()) && StringUtil.isValid(defaultToken)) {
                History.newItem(URLUtil.assembleContextToken(token.getContextId(), defaultToken));
                return;
            }
            if (HistoryManager.checkInterceptors(token)) return;
            checkContext(token);
            if (runstate.ordinal() < RunState.RUNNING.ordinal()) {
                onStartup();
                runstate = RunState.RUNNING;
            }
            if (token.isEmpty()) return;
            if (currentItem != null) {
                if (currentItem.token.getPrimaryToken().equals(token.getPrimaryToken())) {
                    if (currentItem.update(token)) activate(currentItem.state, token, true);
                    return;
                }
                if (!deactivate(this.currentItem.state)) {
                    ignoreToken = true;
                    History.back();
                    return;
                }
            }
            currentItem = findState(token);
            if (currentItem == null) {
                T state = createState(token);
                if (state == null) throw new IllegalStateException("Unhandled token='" + token + "'");
                addState(token, state);
            }
            activate(currentItem.state, token, false);
        } catch (Exception e) {
            showError(token, e);
        }
    }

    protected void notifyActivation(T state) {
        if (state == null) return;
        Item<T> item = findItem(state);
        if (item != null) {
            currentItem = item;
            String token = URLUtil.assembleToken(item.token.getId(), item.token.getParams());
            String subTokens = buildSubStateTokens(item.token.getId(), state);
            if (subTokens != null) token += subTokens;
            token = URLUtil.assembleContextToken(getContextId(), token);
            if (!token.equals(History.getToken())) {
                ignoreToken = true;
                History.newItem(token);
            }
        }
    }

    protected void notifyRemove(T state) {
        if (state == null) return;
        Item<T> item = findItem(state);
        if (item != null) {
            if (currentItem != null && currentItem.equals(item)) {
                currentItem = null;
                ignoreToken = true;
                History.newItem("");
            }
            activeStates.remove(item);
        }
    }

    private void addState(HistoryToken token, T state) {
        currentItem = new Item<T>(state, token);
        activeStates.add(currentItem);
    }

    private Item<T> findState(HistoryToken token) {
        String mainToken = token.getPrimaryToken();
        for (Item<T> item : activeStates) if (mainToken.equals(item.token.getPrimaryToken())) return item;
        return null;
    }

    private Item<T> findItem(T state) {
        for (Item<T> item : activeStates) if (item.state.equals(state)) return item;
        return null;
    }

    protected List<T> getStates() {
        List<T> states = new ArrayList<T>(activeStates.size());
        for (Item<T> item : activeStates) states.add(item.state);
        return states;
    }

    private void showError(HistoryToken token, Throwable t) {
        try {
            GWT.log(t.getMessage(), t);
            T state = createErrorState(token, t);
            addState(token, state);
        } catch (Exception ignoreMe) {
            ignoreToken = goon = false;
            GWT.log(ignoreMe.getMessage(), ignoreMe);
            try {
                resetIntern();
            } catch (Exception ignoreMe2) {
                GWT.log(ignoreMe2.getMessage(), ignoreMe2);
            }
            RootPanelEx.setRoot(new FatalErrorPanel(token, t));
        }
    }

    protected void onStartup() {
    }

    protected void onReset() {
    }

    protected abstract T createErrorState(HistoryToken token, Throwable t);

    protected abstract T createState(HistoryToken token);

    protected abstract boolean deactivate(T target);

    protected abstract void activate(T target, HistoryToken token, boolean subChange);

    public abstract String buildToken(String id, Object param);

    public abstract String buildSubStateTokens(String id, T state);
}
