package com.aol.omp.common;

import com.aol.omp.base.debug.Logger;
import com.aol.omp.base.util.Util;
import java.util.Vector;

/**
 * 
 * @author abe
 * @author mags
 * 
 */
public class Navigate extends Util implements Runnable, Constants {

    private Logger logger = Logger.getInstatnce();

    private Turtle shell;

    public Page page;

    private Connect connect;

    private Layout layout;

    private Encode encode;

    private Services services;

    private Parse parse;

    private int horizKeySelect = -9999;

    private int vertKeySelect;

    private boolean renderingDone = true;

    private boolean pendingPost;

    private boolean markupProcessingStarted = false;

    private boolean dualTarget = false;

    private byte markupError = 0;

    private boolean connectionError = false;

    private boolean forceFetch = false;

    private Thread navThread = null;

    private Object navThreadLock = new Object();

    private String lastURL = null;

    private String pendingURL;

    private Vector pendingArgs = new Vector();

    private Vector prevArgs = null;

    private String pendingObjectName = null;

    private String currentURL;

    private String startMML;

    private Vector lastArgs = null;

    private String depKey = null;

    private Vector navList = null;

    private State.Navigation pendingNav;

    private State.PageElement pageElement;

    private Vector formList;

    private String responseString = null;

    private byte[] responseBuffer = null;

    private int connectError;

    public boolean lastKeyWasCancel = false;

    private String keyMapping[] = { "back", "softleft", "softright", "softcenter", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "star", "pound", "up", "down", "left", "right" };

    /** Creates a new instance of Navigate */
    public Navigate(Turtle shell) {
        super();
        this.shell = shell;
        this.page = shell.page;
        this.connect = shell.connect;
        this.layout = shell.layout;
        this.services = shell.services;
        this.parse = shell.parse;
        this.encode = new Encode();
        synchronized (navThreadLock) {
            navThread = new Thread(this);
            navThread.start();
        }
    }

    public synchronized void pauseThread() {
        try {
            navThread.wait();
        } catch (Exception e) {
        }
    }

    public synchronized void resumeThread() {
        navThread.notify();
    }

    public void loadAppletContext(String startPage) {
        startMML = startPage;
    }

    /**
     * Method: NavigateTo Description:
     */
    public void NavigateTo(int hSelect, int vSelect) {
        NavigateTo(hSelect, vSelect, null);
    }

    public void NavigateTo(int hSelect, int vSelect, Vector navList) {
        if (navList != null) parse.navList = navList;
        if ((hSelect == BACK_KEY) && connect.isPending()) {
            if (connect.cancelPendingConnection()) {
                horizKeySelect = hSelect;
                if (pendingPost) pendingPost = false;
                lastKeyWasCancel = true;
                return;
            }
        }
        lastKeyWasCancel = false;
        if (renderingDone) {
            horizKeySelect = hSelect;
            vertKeySelect = vSelect;
            synchronized (navThreadLock) {
                navThreadLock.notify();
            }
        }
    }

    public boolean renderingDone() {
        return renderingDone;
    }

    public void setRenderingDone(boolean done) {
        this.renderingDone = done;
    }

    public boolean navExists(int keySelect) {
        int select = KEY_SELECT_BASE - keySelect;
        if (select < keyMapping.length) {
            String key = keyMapping[select];
            State.Navigation nav = findNavigation(key);
            return ((nav == null) ? false : true);
        }
        return false;
    }

    /**
     * Method: run Description: Navigate thread
     */
    public void run() {
        while (true) {
            renderingDone = false;
            String url = null;
            String objectName = null;
            Vector args = null;
            navList = parse.getNavigationList();
            State.Navigation nav = null;
            State.Navigation selectNav = null;
            State.Navigation clickNav = null;
            State.Navigation urlLoadNav = null;
            byte button = State.B_NONE;
            pageElement = parse.getPageElement();
            pendingObjectName = objectName;
            pendingNav = nav;
            pendingPost = false;
            if (navList != null && navList.size() > 0) {
                nav = (State.Navigation) navList.elementAt(0);
            }
            BLOCK: {
                if (!isDirectNav) {
                    if (horizKeySelect == STARTKEY_SELECT) {
                        url = currentURL = startMML;
                        markupProcessingStarted = true;
                    } else if (horizKeySelect == RETURNFROM_GAME) {
                        url = lastURL;
                        args = lastArgs;
                    } else if (horizKeySelect == EXIT_KEY) {
                        logger.print(Logger.INFO, "Navigate::horizKeySelect=EXIT_KEY");
                        shell.exit();
                    } else if (horizKeySelect == -9999) {
                        System.gc();
                        break BLOCK;
                    } else if (horizKeySelect == BACK_KEY) {
                        nav = findNavigation("back");
                        if (nav == null) {
                            break BLOCK;
                        }
                        url = layout.evalArg(nav.url);
                        args = nav.argumentList;
                        if (url == null) {
                            Vector retVals = Shared.popNavStack(currentURL);
                            url = (String) retVals.elementAt(0);
                            prevArgs = (Vector) retVals.elementAt(1);
                        } else if (url.equals("#Exit")) {
                            shell.exit();
                            return;
                        }
                    } else if ((horizKeySelect <= KEY_SELECT_BASE) && (horizKeySelect >= RIGHT_KEY)) {
                        String key = keyMapping[KEY_SELECT_BASE - horizKeySelect];
                        nav = findNavigation(key);
                        if (nav == null) {
                            break BLOCK;
                        }
                        url = nav.url;
                        args = nav.argumentList;
                    } else if ((selectNav = findNavigation("#Select")) != null) {
                        url = selectNav.url;
                        State.MenuItem menuItem = layout.findMenuItemSelect(State.M_SELECT);
                        if (menuItem == null) {
                            if (((parse.menuList.size() > 0) && (selectNav.submit == null)) || (url == null)) {
                                break BLOCK;
                            }
                        } else button = menuItem.button;
                        if (url == null) {
                            url = menuItem.url;
                            if (url == null) {
                                objectName = menuItem.object;
                            }
                        }
                        nav = selectNav;
                        args = nav.argumentList;
                    } else if ((clickNav = findNavigation("#Click")) != null) {
                        url = clickNav.url;
                        State.MenuItem menuItem = layout.findMenuItemSelect(State.M_CLICK);
                        if (menuItem == null) {
                            if (((parse.menuList.size() > 0) && (clickNav.submit == null)) || (url == null)) {
                                break BLOCK;
                            }
                        } else button = menuItem.button;
                        if (url == null) {
                            url = menuItem.url;
                            if (url == null) {
                                objectName = menuItem.object;
                            }
                        }
                        nav = clickNav;
                        args = nav.argumentList;
                    } else if ((urlLoadNav = findNavigation("#URLload")) != null) {
                        url = urlLoadNav.url;
                        args = urlLoadNav.argumentList;
                        nav = urlLoadNav;
                    }
                } else {
                    isDirectNav = false;
                    nav = directNav;
                    url = nav.url;
                    args = nav.argumentList;
                }
                if ((url == null) && (objectName == null) && (button == State.B_NONE)) {
                    break BLOCK;
                }
                if (button != State.B_NONE) {
                    if (button == State.B_PLAY) {
                        layout.audioPlayer.playSound();
                    } else if (button == State.B_PAUSE) {
                        layout.audioPlayer.pauseSound();
                    } else if (button == State.B_STOP) {
                        layout.audioPlayer.stopSound();
                    }
                    break BLOCK;
                }
                layout.audioPlayer.closeSound();
                if (url != null) {
                    if (url.equals("#Return")) shell.ctxMgr.popContext();
                    if (url.equals("#Back") || url.equals("#Return")) {
                        Vector retVals = Shared.popNavStack(currentURL);
                        if (retVals.elementAt(0) != null) url = (String) retVals.elementAt(0); else url = currentURL;
                        if (retVals.elementAt(1) != null) args = (Vector) retVals.elementAt(1);
                        pendingArgs = prevArgs = null;
                    } else if ((url.indexOf("::") != -1) && url.endsWith("#Resume")) {
                        Vector retVals = shell.ctxMgr.resumeContext(url);
                        if (retVals != null) {
                            url = (String) retVals.elementAt(0);
                            args = (Vector) retVals.elementAt(1);
                        }
                    } else if (url.equals("#Self")) {
                        break BLOCK;
                    }
                }
                page.blockInputFieldActivity(true);
                page.blockNavInputActivity(true);
                page.cancelTimers();
                page.stopPendingThrobberAnimation();
                layout.throbber.startAnimation("//throbber.png");
                if (nav != null) {
                    if (nav.local != null && nav.local.length() > 0) {
                        String local = nav.local;
                        Vector svsList = stringSplit(local, '|');
                        int size = svsList.size();
                        for (int i = 0; i < size; i++) {
                            Vector argList = stringSplit((String) svsList.elementAt(i), ':');
                            String svcName = (String) argList.elementAt(0);
                            int isize = argList.size();
                            StringBuffer sargs = new StringBuffer();
                            if (isize > 1) {
                                Vector argv = stringSplit((String) argList.elementAt(1), ',');
                                int asize = argv.size();
                                for (int j = 0; j < asize; j++) {
                                    String arg = (String) argv.elementAt(j);
                                    arg = layout.evalArg(arg);
                                    arg = arg + ",";
                                    sargs.append(arg);
                                }
                                if (sargs.length() > 0) sargs = sargs.deleteCharAt(sargs.length() - 1);
                            }
                            String _sargs = sargs.toString();
                            if (svcName.equals("getRandomValue")) services.getRandomValue(_sargs); else if (svcName.equals("getDate")) services.getDate(); else if (svcName.equals("setLocale")) services.setLocaleService(_sargs);
                        }
                    }
                }
                pendingObjectName = objectName;
                pendingNav = nav;
                pendingPost = false;
                if (prevArgs != null) pendingArgs = prevArgs; else if (args != null && args.size() > 1) pendingArgs = args; else if (args != null) pendingArgs = null;
                State.Form form = null;
                if (url != null || nav.submit != null) {
                    url = layout.evalArg(url);
                    if ((nav != null) && (nav.submit != null)) {
                        int formIdx = -1;
                        Vector submit = null;
                        int formListStart = 0;
                        int formListEnd = formList.size();
                        int indexDollarInput;
                        if ((indexDollarInput = nav.submit.indexOf("#Input")) != -1) {
                            int index = nav.submit.indexOf(".");
                            String serviceName = null;
                            if (index != -1) {
                                if (indexDollarInput == 0) serviceName = nav.submit.substring(index + 1); else serviceName = nav.submit.substring(0, index);
                                for (int i = 0; ((formList != null) && (i < formList.size())); i++) {
                                    form = (State.Form) formList.elementAt(i);
                                    if (serviceName.equals(form.id)) {
                                        formIdx = i;
                                        break;
                                    }
                                }
                            }
                            if (formIdx != -1) {
                                formListStart = formIdx;
                                formListEnd = formIdx + 1;
                            }
                        } else {
                            formListEnd = -1;
                        }
                        for (int i = formListStart; i < formListEnd; i++) {
                            submit = collectInput(i);
                            form = (State.Form) formList.elementAt(i);
                            if (submit != null && submit.size() > 0) {
                                {
                                    if (((Turtle) shell).paused() && nav != null && nav.submit != null) {
                                        url = lastURL;
                                        if (pageElement != null && pageElement.addToHistory == 0) {
                                            horizKeySelect = BACK_KEY;
                                            markupError = 1;
                                        }
                                        break BLOCK;
                                    }
                                    if (form.access == State.F_ASYNC) {
                                        connect.addAsyncForm(form);
                                        continue;
                                    }
                                    pendingPost = true;
                                    responseBuffer = connect.addSyncForm(form);
                                    connectError = (responseBuffer != null) ? 0 : -1;
                                    if (connectError != 0) {
                                        if (pendingPost) {
                                            layout.throbber.stopAnimation("//throbber.png");
                                            page.setError(Page.SYSERR_NONET);
                                            connectionError = true;
                                            objectName = null;
                                            pendingPost = false;
                                        } else {
                                            logger.print(Logger.INFO, "Navigate::run: regained control from cancelled http connection");
                                            page.render();
                                        }
                                        break BLOCK;
                                    }
                                    pendingPost = false;
                                }
                                if (responseBuffer == null || (responseBuffer.length == 0)) {
                                    responseString = "No response from service.";
                                    responseBuffer = responseString.getBytes();
                                }
                                layout.updateSpecialVariableList(responseBuffer, 0, responseBuffer.length);
                            }
                        }
                    }
                }
                if (nav != null) {
                    String object = pendingObjectName;
                    String keys = "";
                    if (nav.key.indexOf(":") != -1) {
                        keys = stringExtract(nav.key, ':', '^');
                    }
                    if (url != null) {
                        url = findTarget(keys, url);
                        if (url == null) break BLOCK;
                        if (!(url.endsWith(".mml") || (url.indexOf("#") != -1) || (url.indexOf("::") != -1))) objectName = url;
                    } else if (object != null) {
                        objectName = findTarget(keys, object);
                        if (objectName == null) break BLOCK;
                        if (objectName.endsWith(".mml") || (objectName.indexOf("#") != -1) || (objectName.indexOf("::") != -1)) url = objectName;
                    }
                    layout.assignVariables(VAR_EVAL_NAVIGATE);
                }
                url = layout.evalArg(url);
                layout.throbber.stopAnimation("//throbber.png");
                if (url != null) {
                    if (url.equals("#Return")) shell.ctxMgr.popContext();
                    if (url.equals("#Exit")) {
                        logger.print(Logger.INFO, "Navigate::url=" + url);
                        shell.exit();
                        return;
                    } else if (url.equals("#Back") || url.equals("#Return")) {
                        Vector retVals = Shared.popNavStack(currentURL);
                        url = (String) retVals.elementAt(0);
                        prevArgs = (Vector) retVals.elementAt(1);
                    } else if ((url.indexOf("::") != -1) && url.endsWith("#Resume")) {
                        Vector retVals = shell.ctxMgr.resumeContext(url);
                        if (retVals != null) {
                            url = (String) retVals.elementAt(0);
                            args = (Vector) retVals.elementAt(1);
                        }
                    } else if (url.equals("#Self")) {
                        layout.throbber.stopAnimation("//throbber.png");
                        page.initInputLists(false);
                        page.redraw(true);
                        break BLOCK;
                    }
                }
                if ((pendingArgs != null) && (prevArgs != null)) pendingArgs = (prevArgs.size() == 0) || !(pendingArgs.size() == 0) ? pendingArgs : prevArgs;
                args = pendingArgs;
                String expandedURL = shell.ctxMgr.getExpandedMarkupName(url);
                String targetPage = null;
                if (url.equals("#Self")) targetPage = currentURL; else targetPage = expandedURL;
                args = layout.parseArgs(targetPage, args);
                logger.print(Logger.INFO, "Navigate:: Arguments : " + args);
                if (!url.equals(currentURL)) {
                    int dot = currentURL.indexOf(".mm");
                    int appletPrefix = currentURL.indexOf("::");
                    String pageId = (appletPrefix != -1) ? currentURL.substring(appletPrefix + 2, dot) : currentURL.substring(0, dot);
                    pageId += ":args.";
                    Shared.removeLocalVariables();
                    Shared.removeLocalArgs(pageId);
                }
                if (objectName != null) {
                    page.blockInputFieldActivity(true);
                    shell.launchVendorApp(objectName);
                } else {
                    boolean refresh = false;
                    if ((nav != null) && (nav.action != null) && (nav.action.equals("refresh"))) {
                        refresh = true;
                    }
                    page.syncOnPageReady();
                    logger.print(Logger.INFO, "Navigate::syncing to SV udpate");
                    connect.commDispatcher.syncOnSpecialVariableUpdate();
                    logger.print(Logger.INFO, "Navigate::have SV use lock");
                    page.cancelTimers();
                    url = shell.ctxMgr.getExpandedMarkupName(url);
                    markupError = parse.readMarkup(url, refresh);
                    lastArgs = args;
                    formList = parse.getFormList();
                    pageElement = parse.getPageElement();
                    if (markupError == 1) {
                        page.blockNavInputActivity(true);
                        lastURL = layout.lastURL;
                    } else if (markupError == Constants.P_VERSION_MISMATCH) {
                        markupError = parse.readMarkup("//sys_err.mml", false);
                        lastArgs = args;
                        args = new Vector();
                        args.addElement(parse.parseError);
                        formList = parse.getFormList();
                        pageElement = parse.getPageElement();
                    }
                    if (lastURL != null) lastURL = layout.evalArg(lastURL);
                }
                if ((url != null) && (this.pageElement != null) && (url.endsWith(".mml")) || (url.indexOf("::") != -1)) {
                    if (this.pageElement.addToHistory == 1) Shared.pushNavStack(url, args);
                    currentURL = lastURL = url;
                    pendingArgs = args;
                }
            }
            renderingDone = true;
            if (markupError == 1) {
                markupError = 0;
                if (horizKeySelect != BACK_KEY) horizKeySelect = -3333;
                try {
                    java.lang.Thread.sleep(1000);
                } catch (Exception e) {
                    logger.print(Logger.ERROR, "navigate::run:Error calling sleep function: " + e.toString());
                }
                continue;
            } else if (markupError < 0) {
                horizKeySelect = BACK_KEY;
                continue;
            }
            if ((objectName == null || connectionError) && markupProcessingStarted) {
                page.blockInputFieldActivity(false);
                page.blockNavInputActivity(false);
                connectionError = false;
            }
            try {
                synchronized (navThreadLock) {
                    navThreadLock.wait();
                }
            } catch (InterruptedException ie) {
            } catch (Exception e) {
                logger.print(Logger.ERROR, "Navigate::run = " + e.toString());
                e.printStackTrace();
            }
        }
    }

    private String findTarget(String keys, String targets) {
        String target = targets;
        Vector keyList = stringSplit(keys, ',');
        int ksize = keyList.size();
        Vector urlList = stringSplit(targets, ',');
        int usize = urlList.size();
        if (ksize == 0) {
            target = (String) urlList.elementAt(0);
        } else if (ksize == 1) {
            String s = Shared.getVariable((String) keyList.elementAt(0));
            if (s.equals("false") || (s.length() == 0)) s = "0"; else if (s.equals("true")) s = "1";
            int intValue = Integer.parseInt(s);
            if (intValue >= usize) {
                logger.print(Logger.ERROR, "Navigate::Error in switch:target=" + intValue + ";size=" + usize);
                return null;
            }
            target = (String) urlList.elementAt(intValue);
        } else {
            if (usize >= (ksize + 1)) {
                int i = 0;
                for (i = 0; i < ksize - 1; i++) {
                    String s = Shared.getVariable((String) keyList.elementAt(i));
                    if (s.equals("true") || (s.equals("1"))) {
                        target = (String) urlList.elementAt(i);
                        break;
                    }
                }
                if (i == (ksize - 1)) {
                    String s = Shared.getVariable((String) keyList.elementAt(i));
                    if (s.equals("true") || (s.equals("1"))) target = (String) urlList.elementAt(usize - 2); else target = (String) urlList.elementAt(usize - 1);
                }
            } else {
                logger.print(Logger.ERROR, "Navigate::Error in prioritized goto:#targets=" + ksize + ";size=" + usize);
                return null;
            }
        }
        return target;
    }

    /**
     * Method: findNavigation Description:
     */
    private State.Navigation findNavigation(String key) {
        State.Navigation nav = null;
        if (key == null) {
            return null;
        }
        Vector navList = parse.navList;
        if (navList == null) {
            return null;
        }
        byte select = (key.equals("#Select")) ? State.M_SELECT : State.M_CLICK;
        for (int i = 0; i < navList.size(); i++) {
            nav = (State.Navigation) navList.elementAt(i);
            String navKey = nav.key;
            String id = null;
            String selectId = null;
            int dotIndex;
            if ((dotIndex = navKey.indexOf('.')) != -1) {
                navKey = navKey.substring(0, dotIndex);
                State.MenuItem menuItem = layout.findMenuItemSelect(select);
                if (menuItem == null) {
                    continue;
                }
                id = menuItem.id;
                selectId = nav.key;
                int colonIndex = selectId.indexOf(':');
                if (colonIndex == -1) {
                    colonIndex = selectId.length();
                }
                dotIndex = selectId.indexOf('.');
                if (dotIndex != -1) {
                    selectId = selectId.substring(dotIndex + 1, colonIndex);
                } else {
                    selectId = selectId.substring(1, selectId.length());
                }
            }
            if (navKey.startsWith(key)) {
                if (selectId == null) return nav; else if (selectId.equals(id)) return nav;
            }
        }
        return null;
    }

    public void collectNonUserInput(State.Form form, Vector submitData) {
        Vector tmpSumbitData = collectInput(form);
        for (int i = 0; i < form.inputList.size(); i++) {
            State.Input input = (State.Input) form.inputList.elementAt(i);
            if (input.type != State.I_CHECKBOXTYPE && input.type != State.I_RADIO && input.type != State.I_BUTTON) if (!input.edit.startsWith("#Input")) submitData.setElementAt(tmpSumbitData.elementAt(i * 2 + 1), i * 2 + 1);
        }
    }

    private Vector collectInput(int i) {
        State.Form form = (State.Form) formList.elementAt(i);
        return collectInput(form);
    }

    public Vector collectInput(State.Form form) {
        Vector collection = new Vector();
        Vector inputList = form.inputList;
        for (int j = 0; j < inputList.size(); j++) {
            State.Input input = (State.Input) inputList.elementAt(j);
            String editVal;
            if (input.type == State.I_CHECKBOXTYPE || input.type == State.I_RADIO) {
                if (input.state == State.I_CHECKED) {
                    editVal = input.edit.trim();
                    editVal = layout.evalArg(editVal);
                } else {
                    continue;
                }
            } else if (input.type == State.I_BUTTON) {
                continue;
            } else {
                editVal = input.edit.trim();
                editVal = layout.evalArg(editVal);
            }
            if (input.type == State.I_PASSWORD) {
                String passwordKey = "6ffde36da409f6b5bba3a2b97f33ea9b";
                if (form.encode == State.F_MD5) editVal = encode.getMD5Hash(editVal, passwordKey); else if (form.encode == State.F_BASE64) editVal = encode.getBase64Hash(editVal, passwordKey);
            } else if (input.type == State.I_GAME) {
                String gameKey = "dd41efef5c4bd2f638d3d774b4d6dad1";
                if (form.encode == State.F_MD5) editVal = encode.getMD5Hash(editVal, gameKey); else if (form.encode == State.F_BASE64) editVal = encode.getBase64Hash(editVal, gameKey);
            }
            collection.addElement((String) input.name);
            collection.addElement(editVal);
            StringBuffer dbgOut = new StringBuffer(36 + input.name.length() + editVal.length());
            dbgOut.append("layout::collectInput:name = ").append(input.name).append(" edit = ").append(editVal);
        }
        return collection;
    }

    public boolean isDirectNav = false;

    public State.Navigation directNav;

    public void NavigateTo(State.Navigation nav) {
        if ((nav.url != null && nav.url.equals("#Back")) && connect.isPending()) {
            if (connect.cancelPendingConnection()) {
                lastKeyWasCancel = true;
                return;
            }
        }
        lastKeyWasCancel = false;
        if (renderingDone) {
            synchronized (navThreadLock) {
                navThreadLock.notify();
                isDirectNav = true;
                directNav = nav;
            }
        }
    }
}
