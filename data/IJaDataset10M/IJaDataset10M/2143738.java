package com.aol.omp.common;

import com.aol.omp.base.debug.Logger;
import java.util.Vector;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author abe
 * @author mags
 *
 */
public class Parse extends State {

    private Logger logger = Logger.getInstatnce();

    private class Context {

        private boolean ready;

        private Global global;

        private PageElement pageElement;

        private LayoutElement layoutElement;

        private Vector menuList;

        private Vector bkgList;

        private Vector throbList;

        private Vector navList;

        private Vector progressbarList;

        private Vector formList;

        private Vector widgetList;

        private Vector assignVariableList;

        private Table table;
    }

    private Connect connect = null;

    private Turtle shell;

    private Layout layout;

    public Global global = null;

    public PageElement pageElement = null;

    public LayoutElement layoutElement = null;

    public Vector menuList = null;

    public Vector bkgList = null;

    public Vector throbList = null;

    public Vector navList = null;

    public Vector progressbarList = null;

    public Vector formList = null;

    public Vector asyncFormList = null;

    public Vector widgetList = null;

    public Vector assignVariableList = null;

    private ServicePrototype servicePrototype = null;

    private Vector servicePrototypeList = null;

    public static final String[] inputTypes = { "alphanumeric", "password", "game", "numeric", "auto-fill" };

    public String previousMarkupPage = null;

    private Object[] dataRefs = new Object[1];

    private byte[] buf = null;

    BufObj bufObj = null;

    int minorVersion = 1;

    int majorVersion = 2;

    public String parseError;

    public Global getGlobal() {
        return global;
    }

    public PageElement getPageElement() {
        return pageElement;
    }

    public LayoutElement getLayout() {
        return layoutElement;
    }

    public Vector getMenuList() {
        return menuList;
    }

    public Vector getBackgroundList() {
        return bkgList;
    }

    public Vector getThrobberList() {
        return throbList;
    }

    public Vector getNavigationList() {
        return navList;
    }

    public Vector getProgressBarList() {
        return progressbarList;
    }

    public Vector getFormList() {
        return formList;
    }

    public Parse(Turtle shell) {
        super();
        this.shell = shell;
        this.layout = shell.layout;
        this.connect = shell.connect;
        global = new Global();
        buf = new byte[6 * 1024];
        bufObj = new BufObj(buf);
        servicePrototype = new ServicePrototype();
        servicePrototypeList = new Vector();
    }

    private void printNameValueList(String title, String[] nameList, String[] valueList) {
        logger.print(Logger.INFO, title);
        for (int i = 0; i < nameList.length; i++) {
            logger.print(Logger.INFO, nameList[i] + "=" + valueList[i] + ";");
        }
    }

    /**
     * Method: SetParams() Description: Set Parse object's parameters
     */
    public void setParams(Connect connect) {
        this.connect = connect;
    }

    /**
     * Method: purgeResources() Description: Purge resources in previous page's
     * markup
     */
    public void purgeResources(String page) {
        if (previousMarkupPage == null) {
            previousMarkupPage = page;
            return;
        }
        boolean samePage = page.equals(previousMarkupPage);
        if (pageElement.hasGallery || (!shell.keepInMemory && !samePage)) {
            int size;
            Runtime runtime = Runtime.getRuntime();
            logger.print(Logger.SPECIAL, "parse::purgeResources:free mem[-purge] : " + runtime.freeMemory());
            try {
                size = bkgList.size();
                for (int i = 0; i < size; i++) {
                    Background bkg = (Background) bkgList.elementAt(i);
                    if (bkg.image != null) {
                        dataRefs[0] = bkg.image;
                        Turtle.memMgr.removeResourceFromBlock(dataRefs);
                        bkg.image = null;
                        bkg = null;
                    }
                }
                Turtle.memMgr.releasePoolBlocks();
                size = menuList.size();
                for (int i = 0; i < size; i++) {
                    Menu menu = (Menu) menuList.elementAt(i);
                    if (menu.cursorImage != null) {
                        dataRefs[0] = menu.cursorImage;
                        Turtle.memMgr.removeResourceFromBlock(dataRefs);
                        menu.cursorImage = null;
                    }
                    int iSize = menu.menuItemList.size();
                    for (int j = 0; j < iSize; j++) {
                        MenuItem menuItem = (MenuItem) menu.menuItemList.elementAt(j);
                        if (menuItem.image != null) {
                            logger.print(Logger.SPECIAL, "parse::purgeResources: menuItem.image=" + menuItem.imageName);
                            dataRefs[0] = menuItem.image;
                            Turtle.memMgr.removeResourceFromBlock(dataRefs);
                            menuItem.image = null;
                        }
                        if (menuItem.hilight_image != null) {
                            logger.print(Logger.SPECIAL, "parse::purgeResources: menuItem.hilightimage=" + menuItem.hilightImageName);
                            dataRefs[0] = menuItem.hilight_image;
                            Turtle.memMgr.removeResourceFromBlock(dataRefs);
                            menuItem.hilight_image = null;
                        }
                        menuItem = null;
                    }
                    menu = null;
                }
                size = throbList.size();
                for (int i = 0; i < size; i++) {
                    Throbber throbber = (Throbber) throbList.elementAt(i);
                    Throbator.ThrobberContext tc = null;
                    if ((tc = layout.throbber.getContext(throbber.imageName)) != null) {
                        dataRefs[0] = tc.throbberImage;
                        Turtle.memMgr.removeResourceFromBlock(dataRefs);
                        tc.throbberImage = null;
                    }
                }
                System.gc();
                Turtle.memMgr.printContents();
                logger.print(Logger.SPECIAL, "purged resources for page=" + previousMarkupPage);
                logger.print(Logger.SPECIAL, "free mem[+purge] : " + runtime.freeMemory());
                Context context = (Context) Shared.getMarkupCache(previousMarkupPage);
                context.ready = false;
                previousMarkupPage = page;
                return;
            } catch (Exception e) {
                logger.print(Logger.ERROR, "parse::purgeResources: Exception=" + e.toString());
            }
        }
        previousMarkupPage = page;
        return;
    }

    /**
     * Method: copyMenu() Description:
     */
    private void copyMenu(Menu src, Menu dst) {
        dst.borderColor = src.borderColor;
        dst.borderWidth = src.borderWidth;
        dst.cursorOffset = src.cursorOffset;
        dst.gap = src.gap;
        dst.z_index = src.z_index;
        dst.geomInfo.hAlign = src.geomInfo.hAlign;
        dst.geomInfo.vAlign = src.geomInfo.vAlign;
        dst.geomInfo.pCoord[0] = src.geomInfo.pCoord[0];
        dst.geomInfo.pCoord[1] = src.geomInfo.pCoord[1];
        dst.geomInfo.pDepCoord[0] = src.geomInfo.pDepCoord[0];
        dst.geomInfo.pDepCoord[1] = src.geomInfo.pDepCoord[1];
        dst.geomInfo.pDelta[0] = src.geomInfo.pDelta[0];
        dst.geomInfo.pDelta[1] = src.geomInfo.pDelta[1];
        dst.geomInfo.lCoord[0] = src.geomInfo.lCoord[0];
        dst.geomInfo.lCoord[1] = src.geomInfo.lCoord[1];
        dst.geomInfo.lDelta[0] = src.geomInfo.lDelta[0];
        dst.geomInfo.lDelta[1] = src.geomInfo.lDelta[1];
        dst.cursorDelta = src.cursorDelta;
        dst.attachScrollbar = src.attachScrollbar;
        dst.cursorPlacement = src.cursorPlacement;
        dst.groupSize = src.groupSize;
        dst.isTemplate = src.isTemplate;
        dst.linkable = src.linkable;
        dst.listType = src.listType;
        dst.scrollMode = src.scrollMode;
        dst.stickySelect = src.stickySelect;
        dst.styleAttribute = src.styleAttribute;
        dst.textHilight = src.textHilight;
        dst.type = src.type;
        dst.unlink = src.unlink;
        dst.visibility = src.visibility;
        dst.audioName = src.audioName;
        dst.cursorImageName = src.cursorImageName;
        dst.cursorPaletteName = src.cursorPaletteName;
        dst.defaultCarouselPositionID = src.defaultCarouselPositionID;
        dst.defaultMenuItemID = src.defaultMenuItemID;
        dst.fontName = src.fontName;
        dst.id = src.id;
        dst.geomInfo.aDepID[0] = src.geomInfo.aDepID[0];
        dst.geomInfo.aDepID[1] = src.geomInfo.aDepID[1];
        dst.geomInfo.pDepID[0] = src.geomInfo.pDepID[0];
        dst.geomInfo.pDepID[1] = src.geomInfo.pDepID[1];
        dst.geomInfo.lDepID[0] = src.geomInfo.lDepID[0];
        dst.geomInfo.lDepID[1] = src.geomInfo.lDepID[1];
        dst.templateCount = src.templateCount;
    }

    /**
     * Method: copyMenuItem() Description:
     */
    private void copyMenuItem(MenuItem src, MenuItem dst) {
        dst.textColor = src.textColor;
        dst.textHilightColor = src.textHilightColor;
        dst.drawRect.width = src.drawRect.width;
        dst.drawRect.height = src.drawRect.height;
        dst.animType = src.animType;
        dst.button = src.button;
        dst.isTemplate = src.isTemplate;
        dst.selectable = src.selectable;
        dst.efxDeselectThrobId = src.efxDeselectThrobId;
        dst.efxSelectThrobId = src.efxSelectThrobId;
        dst.hilightImageName = src.hilightImageName;
        dst.hilightPaletteName = src.hilightPaletteName;
        dst.id = src.id;
        dst.imageName = src.imageName;
        dst.paletteName = src.paletteName;
        dst.linkedMenuID = src.linkedMenuID;
        dst.object = src.object;
        dst.text = src.text;
        dst.templateCount = src.templateCount;
        dst.throbId = src.throbId;
        dst.url = src.url;
        dst.userid = src.userid;
        dst.oCoord[0] = src.oCoord[0];
        dst.oCoord[1] = src.oCoord[1];
        dst.oDelta[0] = src.oDelta[0];
        dst.oDelta[1] = src.oDelta[1];
        dst.oExpr[0] = src.oExpr[0];
        dst.oExpr[1] = src.oExpr[1];
    }

    /**
     * Method: setupTemplateMenuItems() Description:
     */
    private void setupTemplateMenuItems(Menu menu) {
        Vector keys = null;
        for (int i = 0; i < menu.menuItemList.size(); i++) {
            MenuItem menuItem = (MenuItem) menu.menuItemList.elementAt(i);
            if (menuItem.isTemplate && menuItem.templateIndexName.endsWith("_0")) {
                String count = menuItem.templateCount;
                String sindex = "0";
                String svalue = "0";
                if (count.endsWith("[]")) {
                    count = count.substring(0, count.length() - 2);
                    if (count.startsWith("{")) count = layout.evalArg(count);
                    Shared.VariableProperty varProp = Shared.getVariableProperty(count);
                    if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                        keys = Shared.getAssociativeVariableKeys(count);
                        if (keys.size() > 0) svalue = (String) keys.elementAt(0);
                    }
                }
                setMenuItemTemplateState(menuItem, menu, sindex, svalue);
                cloneTemplateMenuItems(menuItem, menu, 0);
            }
        }
    }

    /**
     * Method: createNewTemplates() Description:
     */
    private void createNewTemplates(boolean liveUpdate) {
        Vector keys = null;
        for (int i = 0; i < menuList.size(); i++) {
            Menu menu = (Menu) menuList.elementAt(i);
            if (menu.isTemplate) {
                pageElement.hasTemplate = true;
                if (menu.templateIndexName.endsWith("_0")) {
                    if (liveUpdate) setupTemplateMenuItems(menu);
                    String count = menu.templateCount;
                    String sindex = "0";
                    String svalue = "0";
                    if (count.endsWith("[]")) {
                        count = count.substring(0, count.length() - 2);
                        if (count.startsWith("{")) count = layout.evalArg(count);
                        Shared.VariableProperty varProp = Shared.getVariableProperty(count);
                        if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                            keys = Shared.getAssociativeVariableKeys(count);
                            if (keys.size() > 0) svalue = (String) keys.elementAt(0);
                        }
                    }
                    setMenuTemplateState(menu, sindex, svalue);
                    cloneTemplateMenus(menu);
                }
            } else if (liveUpdate) setupTemplateMenuItems(menu);
        }
    }

    /**
     * Method: cloneTemplateMenus() Description:
     */
    private void cloneTemplateMenus(Menu menu) {
        int tcount = 0;
        boolean isAssociativeArray = false;
        Vector keys = null;
        Vector mkeys = null;
        String count = menu.templateCount;
        Shared.VariableProperty varProp = Shared.getVariableProperty(count);
        if (count.endsWith("[]")) {
            tcount = Integer.parseInt(layout.evalArg(count));
            count = count.substring(0, count.length() - 2);
            if (count.endsWith("()")) count = layout.evalArg(count);
            varProp = Shared.getVariableProperty(count);
            if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                keys = Shared.getAssociativeVariableKeys(count);
                isAssociativeArray = true;
            }
        } else tcount = count.startsWith("$") ? Integer.parseInt(Shared.getScalarVariable(count)) : Integer.parseInt(count);
        int insertPos = 0;
        if (tcount > 1) {
            int size = menuList.size();
            for (int i = 0; i < size; i++) {
                if (menu == (Menu) menuList.elementAt(i)) {
                    insertPos = i + 1;
                    break;
                }
            }
        }
        MenuItem menuItem = (MenuItem) menu.menuItemList.elementAt(0);
        int toffset = 0;
        for (int j = 1; j < tcount; j++, insertPos++) {
            String si = new String("" + j);
            String sv = new String("" + j);
            if (isAssociativeArray) sv = (String) keys.elementAt(j);
            Menu m = new Menu();
            copyMenu(menu, m);
            setMenuTemplateState(m, si, sv);
            menuList.insertElementAt(m, insertPos);
            MenuItem mi = new MenuItem();
            copyMenuItem(menuItem, mi);
            m.menuItemList.addElement(mi);
            if (mi.isTemplate) {
                String tindex = mi.templateCount;
                char f[] = { '+', ']', ')', '^' };
                tindex = stringExtract(tindex, "$..tindex", f);
                if (tindex.length() > 0) {
                    String s = tindex.substring(0, tindex.length() - 1) + m.templateIndex;
                    mi.templateCount = stringReplace(mi.templateCount, "$" + tindex, "$" + s);
                }
                count = mi.templateCount;
                Menu mm = (Menu) menuList.elementAt(insertPos - 1);
                toffset += mm.menuItemList.size();
                String sindex = new String("" + toffset);
                String svalue = "0";
                if (count.endsWith("[]")) {
                    count = count.substring(0, count.length() - 2);
                    if (count.startsWith("{")) count = layout.evalArg(count);
                    varProp = Shared.getVariableProperty(count);
                    if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                        mkeys = Shared.getAssociativeVariableKeys(count);
                        if (mkeys.size() > 0) svalue = (String) mkeys.elementAt(0);
                    }
                }
                setMenuItemTemplateState(mi, m, sindex, svalue);
                cloneTemplateMenuItems(mi, m, toffset);
            }
        }
    }

    /**
     * Method: setMenuItemTemplateState() Description:
     */
    private void cloneTemplateMenuItems(MenuItem menuItem, Menu menu, int toffset) {
        int tcount = 0;
        boolean isAssociativeArray = false;
        Vector keys = null;
        String count = menuItem.templateCount;
        if (count.endsWith("[]")) {
            tcount = Integer.parseInt(layout.evalArg(count));
            count = count.substring(0, count.length() - 2);
            if (count.startsWith("{")) count = layout.evalArg(count);
            Shared.VariableProperty varProp = Shared.getVariableProperty(count);
            if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                keys = Shared.getAssociativeVariableKeys(count);
                isAssociativeArray = true;
            }
        } else tcount = count.startsWith("$") ? Integer.parseInt(Shared.getScalarVariable(count)) : Integer.parseInt(count);
        int insertPos = 0;
        if (tcount > 1) {
            int size = menuList.size();
            for (int i = 0; i < size; i++) {
                if (menuItem == (MenuItem) menu.menuItemList.elementAt(i)) {
                    insertPos = i + 1;
                    break;
                }
            }
        }
        for (int j = 1; j < tcount; j++, insertPos++) {
            int o = j + toffset;
            String si = new String("" + o);
            String sv = new String("" + o);
            if (isAssociativeArray) sv = (String) keys.elementAt(j);
            MenuItem mi = new MenuItem();
            copyMenuItem(menuItem, mi);
            setMenuItemTemplateState(mi, menu, si, sv);
            menu.menuItemList.insertElementAt(mi, insertPos);
        }
    }

    /***************************************************************************
     * Method: setMenuTemplateState() Description:
     **************************************************************************/
    private void setMenuTemplateState(Menu menu, String index, String value) {
        String tindex = menu.id;
        char f[] = { '+', '^' };
        tindex = stringExtract(tindex, "$__tindex", f);
        String s;
        if (tindex.length() > 0) {
            Shared.VariableProperty varProp = new Shared.VariableProperty(Constants.VAR_TYPE_STRING, Constants.VAR_STRUCT_SCALAR, Constants.VAR_SCOPE_LOCAL, Constants.VAR_ACCESS_PRIVATE);
            s = tindex.substring(0, tindex.length() - 1) + index;
            menu.templateIndexName = s;
            String indexVar = "$" + s;
            Shared.putVariableProperty(indexVar, varProp);
            Shared.putScalarVariable(indexVar, value);
            menu.id = stringReplace(menu.id, "$" + tindex, indexVar);
            logger.print(Logger.INFO, "Parse::setMenuTemplateState:var=" + indexVar + ";value=" + value);
            s = stringReplace(s, "__tindex", "..tindex");
            indexVar = "$" + s;
            Shared.putVariableProperty(indexVar, varProp);
            Shared.putScalarVariable(indexVar, value);
        }
        menu.templateIndex = index;
    }

    /**
     * Method: setMenuItemTemplateState() Description:
     */
    private void setMenuItemTemplateState(MenuItem mi, Menu menu, String index, String value) {
        char f[] = { '+', ']', ')', '^' };
        String tindex = mi.id;
        tindex = stringExtract(tindex, "$__tindex", f);
        String tindex_, s;
        if (tindex.length() > 0) {
            Shared.VariableProperty varProp = new Shared.VariableProperty(Constants.VAR_TYPE_STRING, Constants.VAR_STRUCT_SCALAR, Constants.VAR_SCOPE_LOCAL, Constants.VAR_ACCESS_PRIVATE);
            s = tindex.substring(0, tindex.length() - 1) + index;
            String indexVar = "$" + s;
            mi.templateIndexName = s;
            Shared.putVariableProperty(indexVar, varProp);
            Shared.putScalarVariable(indexVar, value);
            mi.id = stringReplace(mi.id, "$" + tindex, indexVar);
            logger.print(Logger.INFO, "Parse::setMenuItemTemplateState:var=" + indexVar + ";value=" + value);
        }
        tindex = mi.text;
        tindex = stringExtract(tindex, "$..tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + menu.templateIndex;
            mi.text = stringReplace(mi.text, "$" + tindex, "$" + s);
            tindex = mi.text;
        }
        tindex = stringExtract(mi.text, "$__tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + index;
            mi.text = stringReplace(mi.text, "$" + tindex, "$" + s);
        }
        tindex = mi.imageName;
        tindex = stringExtract(tindex, "$..tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + menu.templateIndex;
            mi.imageName = stringReplace(mi.imageName, "$" + tindex, "$" + s);
            tindex = mi.imageName;
        }
        tindex = stringExtract(mi.imageName, "$__tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + index;
            mi.imageName = stringReplace(mi.imageName, "$" + tindex, "$" + s);
        }
        tindex = mi.hilightImageName;
        tindex = stringExtract(tindex, "$..tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + menu.templateIndex;
            mi.hilightImageName = stringReplace(mi.hilightImageName, "$" + tindex, "$" + s);
            tindex = mi.hilightImageName;
        }
        tindex = stringExtract(mi.hilightImageName, "$__tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + index;
            mi.hilightImageName = stringReplace(mi.hilightImageName, "$" + tindex, "$" + s);
        }
        tindex = stringExtract(mi.linkedMenuID, "$__tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + index;
            mi.linkedMenuID = stringReplace(mi.linkedMenuID, "$" + tindex, "$" + s);
        }
        tindex = stringExtract(mi.oExpr[0], "$__tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + index;
            mi.oExpr[0] = stringReplace(mi.oExpr[0], "$" + tindex, "$" + s);
        }
        tindex = stringExtract(mi.oExpr[1], "$__tindex", f);
        if (tindex.length() > 0) {
            s = tindex.substring(0, tindex.length() - 1) + index;
            mi.oExpr[1] = stringReplace(mi.oExpr[1], "$" + tindex, "$" + s);
        }
    }

    /**
     * Method: updateTemplates() Description:
     */
    private boolean updateTemplates(Context context) {
        if (context.pageElement.hasTemplate) try {
            menuList = context.menuList;
            int size = menuList.size();
            for (int i = 0; i < size; ) {
                Menu menu = (Menu) menuList.elementAt(i);
                int isize = menu.menuItemList.size();
                boolean firstMenuTemplate = (menu.templateIndexName.endsWith("_0")) ? true : false;
                Vector indexList = new Vector();
                int numDeleted = 0;
                for (int j = 0; j < isize; j++) {
                    MenuItem menuItem = (MenuItem) menu.menuItemList.elementAt(j);
                    if (menuItem.isTemplate) if (!menuItem.templateIndexName.endsWith("_0") || (menu.isTemplate && !firstMenuTemplate)) {
                        logger.print(Logger.INFO, "Parse::updateTemplates:deleting menuItem; templateIndexName=" + menuItem.templateIndexName);
                        menuItem = null;
                        indexList.addElement(new Integer(j - numDeleted));
                        numDeleted++;
                    }
                }
                int lsize = indexList.size();
                for (int l = 0; l < lsize; l++) {
                    menu.menuItemList.removeElementAt(((Integer) indexList.elementAt(l)).intValue());
                }
                if (menu.isTemplate && !firstMenuTemplate) {
                    menu = null;
                    menuList.removeElementAt(i);
                    size--;
                } else i++;
            }
            createNewTemplates(true);
            return true;
        } catch (Exception e) {
            logger.print(Logger.INFO, "Parse::updateTemplates:exception=" + e.toString());
            e.printStackTrace();
        }
        return false;
    }

    boolean peekMarkup(String page) {
        Context ctx = (Context) Shared.getMarkupCache(page);
        if (ctx != null) {
            int size = ctx.pageElement.pageArgList.size();
            String prefix = "$" + page.substring(0, page.indexOf(".mm")) + ":args.";
            for (int i = 0; i < size; i++) {
                DeclareArgument declPageArg = (DeclareArgument) ctx.pageElement.pageArgList.elementAt(i);
                Shared.VariableProperty varProp = new Shared.VariableProperty(declPageArg.getType(), declPageArg.getStruct(), Constants.VAR_SCOPE_ARG, Constants.VAR_ACCESS_PRIVATE);
                Shared.putVariableProperty(prefix + declPageArg.getName(), varProp);
            }
        }
        String p = page;
        String localName;
        String pageID, pageArgPrefix, appletName = "";
        int appletPrefix = p.indexOf("::");
        if (appletPrefix != -1) {
            appletName = p.substring(0, appletPrefix);
            p = p.substring(appletPrefix + 2);
            Shared.setAppletName(appletName);
        }
        if (p.startsWith("//")) {
            p = p.substring(2);
            localName = p;
        }
        pageID = p.substring(0, p.indexOf(".mm"));
        pageArgPrefix = "$" + pageID + ":args.";
        localName = Shared.getAppletName() + "/markup/" + connect.getMarkupDir() + p;
        InputStream iStream = connect.open(localName, false);
        DataInputStream dis = new DataInputStream(iStream);
        bufObj.setIndex(0);
        try {
            dis.read(buf);
        } catch (IOException e) {
            logger.print(Logger.INFO, "Parse::peekMarkup: IOException : " + e.getMessage(), e);
            return false;
        }
        int fileSize = bufObj.readInt();
        bufObj.setEndOfData(fileSize);
        bufObj.readByte();
        bufObj.readByte();
        int version = bufObj.readShort();
        int segmentSize = 0;
        int segmentEnd = 0;
        int segmentType = 0;
        while (!bufObj.endOfData() && (segmentSize = bufObj.readShort()) > 0) {
            segmentEnd = bufObj.getIndex() + segmentSize - 2;
            segmentType = bufObj.readShort();
            switch(segmentType) {
                case DECLARE_PAGE:
                    {
                        DeclareArgument argument = buildDeclarePage();
                        pageElement.pageArgList.addElement(argument);
                        Shared.VariableProperty varProp = new Shared.VariableProperty(argument.getType(), argument.getStruct(), Constants.VAR_SCOPE_ARG, Constants.VAR_ACCESS_PRIVATE);
                        Shared.putVariableProperty(pageArgPrefix + argument.getName(), varProp);
                    }
                    break;
            }
            bufObj.setIndex(segmentEnd);
        }
        return true;
    }

    /**
     * Method: readMarkup Description: read binary markup (MMC)s from specified
     * page; create hierarchy tree; traverse tree to build presentation
     * elements.
     */
    public byte readMarkup(String page, boolean force) {
        byte error = 0;
        logger.print(Logger.INFO, "Parse::readMarkup; page = " + page);
        purgeResources(Shared.getFullyQualifiedName(page));
        Context context = (Context) Shared.getMarkupCache(page);
        String markupURL = page;
        String pageID = null, pageArgPrefix = null;
        if (context == null) {
            DataInputStream dataReader = null;
            int dotPos = markupURL.indexOf(".");
            if (dotPos != -1) {
                markupURL = markupURL.substring(0, dotPos);
                markupURL += ".mmc";
            }
            int appletPrefix = markupURL.indexOf("::");
            if (appletPrefix != -1) {
                shell.ctxMgr.switchApplet(markupURL);
                markupURL = markupURL.substring(appletPrefix + 2);
            }
            if (markupURL.startsWith("//")) markupURL = markupURL.substring(2);
            pageID = markupURL.substring(0, markupURL.indexOf(".mm"));
            pageArgPrefix = "$" + pageID + ":args.";
            markupURL = Shared.getAppletName() + "/markup/" + connect.getMarkupDir() + markupURL;
            logger.print(Logger.INFO, "Parse::readMarkup; creating new context; markup url = " + markupURL);
            layout.throbber.startAnimation("//throbber.png");
            try {
                InputStream is = connect.open(markupURL, force);
                dataReader = new DataInputStream(is);
                bufObj.setIndex(0);
                int bufSize = dataReader.read(buf);
            } catch (Exception e) {
                logger.print(Logger.ERROR, "error opening file: " + e.getMessage(), e);
                return -1;
            }
            int fileSize = bufObj.readInt();
            bufObj.setEndOfData(fileSize);
            bufObj.readByte();
            bufObj.readByte();
            int version = bufObj.readShort();
            int segmentSize = 0;
            int segmentType = 0;
            int segmentEnd = 0;
            Menu curMenu = null;
            Form curForm = null;
            Throbber curThrobber = null;
            pageElement = new PageElement();
            menuList = new Vector();
            bkgList = new Vector();
            throbList = new Vector();
            navList = new Vector();
            progressbarList = new Vector();
            formList = new Vector();
            widgetList = new Vector();
            assignVariableList = new Vector();
            Vector tmpFormList = new Vector();
            Vector keys = null;
            table = null;
            if (version > shell.shellVersion) {
                Shared.putScalarVariable("$__shellMismatch", "true");
                parseError = "Markup version mismatch! Markup version " + ((version >> 8) & 0xff) + "." + (version & 0xff) + " is newer than the shell!";
                logger.print(Logger.ERROR, parseError);
            } else Shared.putScalarVariable("$__shellMismatch", "false");
            while (!bufObj.endOfData() && (segmentSize = bufObj.readShort()) > 0) {
                segmentEnd = bufObj.getIndex() + segmentSize - 2;
                segmentType = bufObj.readShort();
                switch(segmentType) {
                    case PAGE:
                        pageElement = buildPage();
                        break;
                    case LAYOUT:
                        layoutElement = buildLayout();
                        break;
                    case INPUT:
                        curForm.inputList.addElement(buildInput(version));
                        break;
                    case FORM:
                        curForm = buildForm();
                        tmpFormList.addElement(curForm);
                        break;
                    case BACKGROUND:
                        bkgList.addElement(buildBackground());
                        break;
                    case NAVIGATION:
                        navList.addElement(buildNavigation(version));
                        break;
                    case THROBITEM:
                        break;
                    case THROBBER:
                        curThrobber = buildThrobber();
                        throbList.addElement(curThrobber);
                        break;
                    case MENUITEM:
                        MenuItem menuItem = buildMenuItem();
                        curMenu.menuItemList.addElement(menuItem);
                        if (menuItem.isTemplate) {
                            String count = menuItem.templateCount;
                            String sindex = "0";
                            String svalue = "0";
                            if (count.endsWith("[]")) {
                                count = count.substring(0, count.length() - 2);
                                if (count.startsWith("{")) count = layout.evalArg(count);
                                Shared.VariableProperty varProp = Shared.getVariableProperty(count);
                                if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                                    keys = Shared.getAssociativeVariableKeys(count);
                                    if (keys.size() > 0) {
                                        svalue = (String) keys.elementAt(0);
                                    }
                                }
                            }
                            setMenuItemTemplateState(menuItem, curMenu, sindex, svalue);
                            cloneTemplateMenuItems(menuItem, curMenu, 0);
                        }
                        break;
                    case MENU:
                        curMenu = buildMenu();
                        menuList.addElement(curMenu);
                        if (curMenu.isTemplate) {
                            String count = curMenu.templateCount;
                            String sindex = "0";
                            String svalue = "0";
                            if (count.endsWith("[]")) {
                                count = count.substring(0, count.length() - 2);
                                if (count.startsWith("{")) count = layout.evalArg(count);
                                Shared.VariableProperty varProp = Shared.getVariableProperty(count);
                                if (varProp.getStruct() == Constants.VAR_STRUCT_ASSOCIATIVE) {
                                    keys = Shared.getAssociativeVariableKeys(count);
                                    if (keys.size() > 0) {
                                        svalue = (String) keys.elementAt(0);
                                    }
                                }
                            }
                            setMenuTemplateState(curMenu, sindex, svalue);
                        }
                        break;
                    case GLOBAL:
                        global = buildGlobal();
                        break;
                    case WIDGET:
                        widgetList.addElement(buildWidget());
                        break;
                    case TABLE:
                        table = buildTable();
                        break;
                    case ROW:
                        table.rowList.addElement(buildRow());
                        break;
                    case CELL:
                        ((Row) (table.rowList.elementAt(table.rowList.size() - 1))).cellList.addElement(buildCell());
                        break;
                    case DECLARE_VARIABLE:
                        {
                            DeclareVariable variable = buildDeclareVariable();
                            pageElement.variableList.addElement(variable);
                            String name = variable.getName();
                            if (name.startsWith("{")) name = shell.layout.evalArg(name, true);
                            if (variable.getScope() == Constants.VAR_SCOPE_LOCAL) name = "$" + pageID + ":" + name.substring(1);
                            Shared.VariableProperty varProp = new Shared.VariableProperty(variable.getType(), variable.getStruct(), variable.getScope(), variable.getAccess());
                            Shared.putVariableProperty(name, varProp);
                        }
                        break;
                    case DECLARE_PAGE:
                        {
                            DeclareArgument argument = buildDeclarePage();
                            pageElement.pageArgList.addElement(argument);
                            Shared.VariableProperty varProp = new Shared.VariableProperty(argument.getType(), argument.getStruct(), Constants.VAR_SCOPE_ARG, Constants.VAR_ACCESS_PRIVATE);
                            Shared.putVariableProperty(pageArgPrefix + argument.getName(), varProp);
                        }
                        break;
                    case DECLARE_SERVICE:
                        buildDeclareService();
                        break;
                    case DECLARE_SERVICE_REQUEST:
                        DeclareArgument serviceRequest = buildDeclareServiceRequest();
                        servicePrototype.addRequest(serviceRequest);
                        break;
                    case DECLARE_SERVICE_RESPONSE:
                        DeclareVariable serviceResponse = buildDeclareVariable();
                        servicePrototype.addResponse(serviceResponse);
                        shell.ctxMgr.addToServicePrototypeList(servicePrototype);
                        servicePrototypeList.removeAllElements();
                        servicePrototypeList.addElement(servicePrototype);
                        break;
                    case ASSIGN_VARIABLE:
                        {
                            AssignVariable aVariable = buildAssignVariable();
                            assignVariableList.addElement(aVariable);
                        }
                        break;
                    case NAV_ARG:
                        buildNavigationArgument();
                        break;
                }
                bufObj.setIndex(segmentEnd);
            }
            createNewTemplates(false);
            for (int i = 0; i < tmpFormList.size(); i++) {
                curForm = (Form) tmpFormList.elementAt(i);
                if (curForm.access == State.F_ASYNC && curForm.period != -1) {
                    curForm.service = Shared.getAppletName() + "::" + curForm.service;
                    Vector inputList = curForm.inputList;
                    int isize = inputList.size();
                    for (int j = 0; j < isize; j++) {
                        Input input = (Input) inputList.elementAt(j);
                        int expIndex = input.edit.indexOf("${");
                        if (expIndex != -1) input.edit = "${" + Shared.getAppletName() + "::" + input.edit.substring(expIndex + 2);
                    }
                    shell.connect.addAsyncForm(curForm);
                } else formList.addElement(curForm);
            }
            context = new Context();
            context.bkgList = bkgList;
            context.formList = formList;
            context.global = global;
            context.layoutElement = layoutElement;
            context.menuList = menuList;
            context.navList = navList;
            context.pageElement = pageElement;
            context.progressbarList = progressbarList;
            context.throbList = throbList;
            context.widgetList = widgetList;
            context.table = table;
            context.assignVariableList = assignVariableList;
            Shared.putMarkupCache(page, context);
            layout.setPageVisit(true);
            error = layout.createLayout();
            context.ready = true;
        } else {
            logger.print(Logger.INFO, "Parse::readMarkup; found context in cache; markup url = " + markupURL);
            int appletPrefix = markupURL.indexOf("::");
            if (appletPrefix != -1) {
                shell.ctxMgr.switchApplet(markupURL);
            }
            layout.throbber.startAnimation("//throbber.png");
            pageElement = context.pageElement;
            if (pageElement != null) {
                int size = pageElement.pageArgList.size();
                String prefix = "$" + page.substring(0, page.indexOf(".mm")) + ":args.";
                int i;
                for (i = 0; i < size; i++) {
                    DeclareArgument declPageArg = (DeclareArgument) pageElement.pageArgList.elementAt(i);
                    Shared.VariableProperty varProp = new Shared.VariableProperty(declPageArg.getType(), declPageArg.getStruct(), Constants.VAR_SCOPE_ARG, Constants.VAR_ACCESS_PRIVATE);
                    Shared.putVariableProperty(prefix + declPageArg.getName(), varProp);
                }
                prefix = "$" + pageElement.id + ":";
                size = pageElement.variableList.size();
                for (i = 0; i < size; i++) {
                    DeclareVariable declVar = (DeclareVariable) pageElement.variableList.elementAt(i);
                    Shared.VariableProperty varProp = new Shared.VariableProperty(declVar.getType(), declVar.getStruct(), declVar.getScope(), declVar.getAccess());
                    String name = declVar.getName();
                    if (name.startsWith("{")) name = layout.evalArg(name, true);
                    if (declVar.getScope() == Constants.VAR_SCOPE_LOCAL) name = prefix + name.substring(1);
                    Shared.putVariableProperty(name, varProp);
                }
            }
            boolean hasTemplate = updateTemplates(context);
            global = context.global;
            layoutElement = context.layoutElement;
            menuList = context.menuList;
            bkgList = context.bkgList;
            throbList = context.throbList;
            navList = context.navList;
            progressbarList = context.progressbarList;
            formList = context.formList;
            widgetList = context.widgetList;
            table = context.table;
            assignVariableList = context.assignVariableList;
            layout.setPageVisit(false);
            error = (context.ready && !hasTemplate) ? layout.createLayout_FastPath() : layout.createLayout();
        }
        return (error);
    }

    public PageElement buildPage() {
        PageElement pageElement = new PageElement();
        bufObj.getIndex();
        pageElement.backColor = swapBytes(bufObj.readInt());
        pageElement.expire = bufObj.readInt();
        pageElement.addToHistory = bufObj.readByte();
        pageElement.cacheResources = bufObj.readByte();
        pageElement.flushRequests = (bufObj.readByte() == 1) ? true : false;
        pageElement.ignoreDynamic = (bufObj.readByte() == 1) ? true : false;
        pageElement.tab = (bufObj.readByte() == 1) ? true : false;
        bufObj.readShort();
        bufObj.readByte();
        pageElement.form = bufObj.readString();
        pageElement.id = bufObj.readString();
        pageElement.metaList = bufObj.readString();
        pageElement.prefetchList = bufObj.readString();
        return pageElement;
    }

    public LayoutElement buildLayout() {
        LayoutElement layoutElem = new LayoutElement();
        layoutElem.focus = bufObj.readByte();
        layoutElem.hAdvance = bufObj.readByte();
        layoutElem.play = bufObj.readByte();
        layoutElem.progressbar = (bufObj.readByte() == 1) ? true : false;
        layoutElem.vAdvance = bufObj.readByte();
        bufObj.readShort();
        bufObj.readByte();
        Background bkg = new Background();
        bkg.geomInfo.hAlign = (short) bufObj.readShort();
        bkg.geomInfo.vAlign = (short) bufObj.readShort();
        bkg.geomInfo.pCoord[0] = bufObj.readByte();
        bkg.geomInfo.pCoord[1] = bufObj.readByte();
        bkg.geomInfo.pDepCoord[0] = bufObj.readByte();
        bkg.geomInfo.pDepCoord[1] = bufObj.readByte();
        bkg.geomInfo.pDelta[0] = bufObj.readInt();
        bkg.geomInfo.pDelta[1] = bufObj.readInt();
        layoutElem.audioName = bufObj.readString();
        layoutElem.defaultMenuID = bufObj.readString();
        layoutElem.galleryName = bufObj.readString();
        bkg.id = bufObj.readString();
        bkg.imageName = bufObj.readString();
        bkg.geomInfo.aDepID[0] = bufObj.readString();
        bkg.geomInfo.aDepID[1] = bufObj.readString();
        bkg.geomInfo.pDepID[0] = bufObj.readString();
        bkg.geomInfo.pDepID[1] = bufObj.readString();
        bkgList.addElement(bkg);
        return layoutElem;
    }

    public Background buildBackground() {
        Background bkg = new Background();
        for (int j = 0; j < 4; j++) {
            bkg.fillColors[j] = swapBytes(bufObj.readInt());
        }
        bkg.textColor = swapBytes(bufObj.readInt());
        bufObj.readInt();
        bkg.gap = bufObj.readShort();
        bkg.z_index = Integer.toString((int) bufObj.readShort());
        bkg.geomInfo.hAlign = (short) bufObj.readShort();
        bkg.geomInfo.vAlign = (short) bufObj.readShort();
        bkg.geomInfo.pCoord[0] = bufObj.readByte();
        bkg.geomInfo.pCoord[1] = bufObj.readByte();
        bkg.geomInfo.pDepCoord[0] = bufObj.readByte();
        bkg.geomInfo.pDepCoord[1] = bufObj.readByte();
        bkg.geomInfo.pDelta[0] = bufObj.readInt();
        bkg.geomInfo.pDelta[1] = bufObj.readInt();
        bkg.geomInfo.lCoord[0] = (short) bufObj.readShort();
        bkg.geomInfo.lCoord[1] = (short) bufObj.readShort();
        bkg.geomInfo.lDelta[0] = bufObj.readInt();
        bkg.geomInfo.lDelta[1] = bufObj.readInt();
        bkg.attachScrollbar = bufObj.readByte();
        bkg.fillMode = bufObj.readByte();
        bkg.isTemplate = (bufObj.readByte() == 1) ? true : false;
        bkg.liveEval = (bufObj.readByte() == 1) ? true : false;
        bkg.mode = bufObj.readByte();
        bkg.scrollMode = bufObj.readByte();
        bkg.trackBuffer = bufObj.readByte();
        int padB = bufObj.readByte();
        bkg.fillRectNames[0] = bufObj.readString();
        bkg.fillRectNames[1] = bufObj.readString();
        bkg.fontName = bufObj.readString();
        bkg.id = bufObj.readString();
        bkg.imageName = bufObj.readString();
        bkg.paletteName = bufObj.readString();
        bkg.geomInfo.aDepID[0] = bufObj.readString();
        bkg.geomInfo.aDepID[1] = bufObj.readString();
        bkg.geomInfo.pDepID[0] = bufObj.readString();
        bkg.geomInfo.pDepID[1] = bufObj.readString();
        bkg.geomInfo.lDepID[0] = bufObj.readString();
        bkg.geomInfo.lDepID[1] = bufObj.readString();
        bkg.text = bufObj.readString();
        bkg.templateCount = bufObj.readString();
        if (bkg.isTemplate) pageElement.hasTemplate = true;
        return bkg;
    }

    public Widget buildWidget() {
        Widget widget = new Widget();
        widget.z_index = Integer.toString(bufObj.readShort());
        widget.geomInfo.hAlign = (short) bufObj.readShort();
        widget.geomInfo.vAlign = (short) bufObj.readShort();
        widget.geomInfo.pCoord[0] = bufObj.readByte();
        widget.geomInfo.pCoord[1] = bufObj.readByte();
        widget.geomInfo.pDepCoord[0] = bufObj.readByte();
        widget.geomInfo.pDepCoord[1] = bufObj.readByte();
        widget.geomInfo.pDelta[0] = bufObj.readInt();
        widget.geomInfo.pDelta[1] = bufObj.readInt();
        widget.geomInfo.lCoord[0] = (short) bufObj.readShort();
        widget.geomInfo.lCoord[1] = (short) bufObj.readShort();
        widget.geomInfo.lDelta[0] = bufObj.readInt();
        widget.geomInfo.lDelta[1] = bufObj.readInt();
        for (int i = 0; i < KEY_NUM_KEYS; i++) widget.keyBindings[i] = bufObj.readByte();
        bufObj.readShort();
        bufObj.readByte();
        widget.id = bufObj.readString();
        widget.className = bufObj.readString();
        widget.src = bufObj.readString();
        widget.args = bufObj.readString();
        widget.geomInfo.aDepID[0] = bufObj.readString();
        widget.geomInfo.aDepID[1] = bufObj.readString();
        widget.geomInfo.pDepID[0] = bufObj.readString();
        widget.geomInfo.pDepID[1] = bufObj.readString();
        widget.geomInfo.lDepID[0] = bufObj.readString();
        widget.geomInfo.lDepID[1] = bufObj.readString();
        return widget;
    }

    public Form buildForm() {
        Form form = new Form();
        form.box_color = swapBytes(bufObj.readInt());
        form.box_hilight_color = swapBytes(bufObj.readInt());
        form.box_fill_color = swapBytes(bufObj.readInt());
        form.gap = bufObj.readShort();
        form.z_index = Integer.toString((int) bufObj.readShort());
        form.geomInfo.hAlign = (short) bufObj.readShort();
        form.geomInfo.vAlign = (short) bufObj.readShort();
        form.geomInfo.pCoord[0] = bufObj.readByte();
        form.geomInfo.pCoord[1] = bufObj.readByte();
        form.geomInfo.pDepCoord[0] = bufObj.readByte();
        form.geomInfo.pDepCoord[1] = bufObj.readByte();
        form.geomInfo.pDelta[0] = bufObj.readInt();
        form.geomInfo.pDelta[1] = bufObj.readInt();
        form.geomInfo.lCoord[0] = (short) bufObj.readShort();
        form.geomInfo.lCoord[1] = (short) bufObj.readShort();
        form.geomInfo.lDelta[0] = bufObj.readInt();
        form.geomInfo.lDelta[1] = bufObj.readInt();
        form.period = bufObj.readInt();
        form.idlePeriod = bufObj.readInt();
        form.access = (byte) bufObj.readByte();
        form.encode = (byte) bufObj.readByte();
        form.placement = bufObj.readByte();
        form.textCase = bufObj.readByte();
        form.visible = bufObj.readByte();
        bufObj.readShort();
        bufObj.readByte();
        form.fontName = bufObj.readString();
        form.id = bufObj.readString();
        form.geomInfo.aDepID[0] = bufObj.readString();
        form.geomInfo.aDepID[1] = bufObj.readString();
        form.geomInfo.pDepID[0] = bufObj.readString();
        form.geomInfo.pDepID[1] = bufObj.readString();
        form.geomInfo.lDepID[0] = bufObj.readString();
        form.geomInfo.lDepID[1] = bufObj.readString();
        form.service = bufObj.readString();
        form.checkbox_checked_image = bufObj.readString();
        form.checkbox_unchecked_image = bufObj.readString();
        form.radio_checked_image = bufObj.readString();
        form.radio_unchecked_image = bufObj.readString();
        form.button_image = bufObj.readString();
        if (form.button_image != null) {
            int index = form.button_image.indexOf(",");
            if (index != -1) {
                form.button[0] = form.button_image.substring(0, index);
                form.button[1] = form.button_image.substring(index + 1);
            } else {
                form.button[1] = form.button[0] = form.button_image;
            }
        }
        return form;
    }

    private Input buildInput(int version) {
        int majorVersion = (version >> 8) & 0xff;
        int minorVersion = version & 0xff;
        Input input = new Input();
        input.textColor = swapBytes(bufObj.readInt());
        input.textHilightColor = swapBytes(bufObj.readInt());
        input.boxHeight = bufObj.readByte();
        input.editSize = getUnsignedByte(bufObj.readByte());
        input.enterMode = bufObj.readByte();
        input.type = (byte) bufObj.readByte();
        input.border = bufObj.readByte();
        input.shape = bufObj.readByte();
        input.font_face = bufObj.readByte();
        input.state = bufObj.readByte();
        input.edit = bufObj.readString();
        if (input.edit == null) input.edit = "";
        input.id = bufObj.readString();
        input.name = bufObj.readString();
        if (input.name == null) input.name = "";
        input.text = bufObj.readString();
        if (input.text == null) input.text = "";
        input.url = bufObj.readString();
        input.linkText = bufObj.readString();
        input.navigate = bufObj.readString();
        int len = bufObj.readByte();
        if (len != 0) {
            input.list = new String[len];
        }
        for (int tt = 0; tt < len; ++tt) {
            input.list[tt] = bufObj.readString();
        }
        return input;
    }

    private Menu buildMenu() {
        Menu menu = new Menu();
        menu.borderColor = swapBytes(bufObj.readInt());
        menu.borderWidth = bufObj.readShort();
        menu.cursorOffset = bufObj.readShort();
        menu.gap = bufObj.readShort();
        menu.z_index = Integer.toString(bufObj.readShort());
        menu.geomInfo.hAlign = (short) bufObj.readShort();
        menu.geomInfo.vAlign = (short) bufObj.readShort();
        menu.geomInfo.pCoord[0] = bufObj.readByte();
        menu.geomInfo.pCoord[1] = bufObj.readByte();
        menu.geomInfo.pDepCoord[0] = bufObj.readByte();
        menu.geomInfo.pDepCoord[1] = bufObj.readByte();
        menu.geomInfo.pDelta[0] = bufObj.readInt();
        menu.geomInfo.pDelta[1] = bufObj.readInt();
        menu.geomInfo.lCoord[0] = (short) bufObj.readShort();
        menu.geomInfo.lCoord[1] = (short) bufObj.readShort();
        menu.geomInfo.lDelta[0] = bufObj.readInt();
        menu.geomInfo.lDelta[1] = bufObj.readInt();
        for (int i = 0; i < KEY_NUM_KEYS; i++) menu.keyBindings[i] = bufObj.readByte();
        menu.cursorDelta = bufObj.readShort();
        menu.attachScrollbar = bufObj.readByte();
        menu.cursorPlacement = bufObj.readByte();
        menu.groupSize = bufObj.readByte();
        menu.isTemplate = (bufObj.readByte() == 1) ? true : false;
        menu.linkable = (bufObj.readByte() == 1) ? true : false;
        menu.listType = bufObj.readByte();
        menu.scrollMode = bufObj.readByte();
        menu.stickySelect = bufObj.readByte();
        menu.styleAttribute = bufObj.readByte();
        menu.textHilight = bufObj.readByte();
        menu.type = bufObj.readByte();
        menu.unlink = (bufObj.readByte() == 1) ? true : false;
        menu.visibility = bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        menu.audioName = bufObj.readString();
        menu.cursorImageName = bufObj.readString();
        menu.cursorPaletteName = bufObj.readString();
        menu.defaultCarouselPositionID = bufObj.readString();
        menu.defaultMenuItemID = bufObj.readString();
        menu.fontName = bufObj.readString();
        menu.id = bufObj.readString();
        menu.geomInfo.aDepID[0] = bufObj.readString();
        menu.geomInfo.aDepID[1] = bufObj.readString();
        menu.geomInfo.pDepID[0] = bufObj.readString();
        menu.geomInfo.pDepID[1] = bufObj.readString();
        menu.geomInfo.lDepID[0] = bufObj.readString();
        menu.geomInfo.lDepID[1] = bufObj.readString();
        menu.templateCount = bufObj.readString();
        if (menu.isTemplate) pageElement.hasTemplate = true;
        return menu;
    }

    private MenuItem buildMenuItem() {
        MenuItem menuItem = new MenuItem();
        menuItem.textColor = swapBytes(bufObj.readInt());
        bufObj.readInt();
        menuItem.textHilightColor = swapBytes(bufObj.readInt());
        bufObj.readInt();
        menuItem.oCoord[0] = (byte) bufObj.readShort();
        menuItem.oCoord[1] = (byte) bufObj.readShort();
        menuItem.oDelta[0] = bufObj.readInt();
        menuItem.oDelta[1] = bufObj.readInt();
        menuItem.drawRect.width = bufObj.readShort();
        menuItem.drawRect.height = bufObj.readShort();
        menuItem.animType = bufObj.readByte();
        menuItem.button = bufObj.readByte();
        menuItem.isTemplate = (bufObj.readByte() == 1) ? true : false;
        menuItem.selectable = (bufObj.readByte() == 1) ? true : false;
        menuItem.efxDeselectThrobId = bufObj.readString();
        menuItem.efxSelectThrobId = bufObj.readString();
        menuItem.hilightImageName = bufObj.readString();
        menuItem.hilightPaletteName = bufObj.readString();
        menuItem.id = bufObj.readString();
        menuItem.imageName = bufObj.readString();
        menuItem.paletteName = bufObj.readString();
        menuItem.linkedMenuID = bufObj.readString();
        menuItem.object = bufObj.readString();
        menuItem.text = bufObj.readString();
        menuItem.templateCount = bufObj.readString();
        menuItem.throbId = bufObj.readString();
        if (menuItem.throbId == null) menuItem.throbId = "";
        menuItem.url = bufObj.readString();
        menuItem.userid = bufObj.readString();
        menuItem.oExpr[0] = bufObj.readString();
        menuItem.oExpr[1] = bufObj.readString();
        if (menuItem.isTemplate) pageElement.hasTemplate = true;
        return menuItem;
    }

    public static int swapBytes(int i) {
        return ((((i & 0xff) << 24) | ((i & 0xff00) << 8) | ((i >> 8) & 0xff00) | ((i >> 24) & 0xff)));
    }

    private Navigation buildNavigation(int version) {
        int majorVersion = (version >> 8) & 0xff;
        int minorVersion = version & 0xff;
        Navigation nav = new Navigation();
        nav.delay = bufObj.readInt();
        nav.action = Integer.toString((int) bufObj.readByte());
        bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        nav.key = bufObj.readString();
        nav.local = bufObj.readString();
        nav.message = bufObj.readString();
        nav.submit = bufObj.readString();
        nav.url = bufObj.readString();
        nav.id = bufObj.readString();
        return nav;
    }

    private Throbber buildThrobber() {
        Throbber throb = new Throbber();
        throb.frameRate = bufObj.readInt();
        throb.delta = bufObj.readInt();
        throb.startTime = bufObj.readInt();
        throb.numFrames = bufObj.readShort();
        throb.z_index = Integer.toString(bufObj.readShort());
        throb.geomInfo.hAlign = (short) bufObj.readShort();
        throb.geomInfo.vAlign = (short) bufObj.readShort();
        throb.geomInfo.pCoord[0] = bufObj.readByte();
        throb.geomInfo.pCoord[1] = bufObj.readByte();
        throb.geomInfo.pDepCoord[0] = bufObj.readByte();
        throb.geomInfo.pDepCoord[1] = bufObj.readByte();
        throb.geomInfo.pDelta[0] = bufObj.readInt();
        throb.geomInfo.pDelta[1] = bufObj.readInt();
        throb.liveEval = (bufObj.readByte() == 1) ? true : false;
        bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        throb.id = bufObj.readString();
        if (throb.id == null) throb.id = "";
        throb.imageName = bufObj.readString();
        throb.geomInfo.aDepID[0] = bufObj.readString();
        throb.geomInfo.aDepID[1] = bufObj.readString();
        throb.geomInfo.pDepID[0] = bufObj.readString();
        throb.geomInfo.pDepID[1] = bufObj.readString();
        return throb;
    }

    private Global buildGlobal() {
        Global global = new Global();
        for (int j = 0; j < 3; j++) {
            global.scrollbarColors[j] = swapBytes(bufObj.readInt());
        }
        global.scrollbarIconGap = (short) bufObj.readShort();
        global.scrollbarRectGap = (short) bufObj.readShort();
        global.geomInfo.hAlign = (short) bufObj.readShort();
        global.geomInfo.vAlign = (short) bufObj.readShort();
        global.fontName = bufObj.readString();
        global.scrollbarName = bufObj.readString();
        return global;
    }

    private void buildNavigationArgument() {
        String name = bufObj.readString();
        String value = bufObj.readString();
        Navigation navigation = (Navigation) navList.elementAt(navList.size() - 1);
        navigation.argumentList.addElement(name);
        navigation.argumentList.addElement(value);
    }

    private DeclareArgument buildDeclarePage() {
        byte type = bufObj.readByte();
        byte struct = bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        String name = bufObj.readString();
        DeclareArgument argument = new DeclareArgument(name, type, struct);
        return argument;
    }

    private AssignVariable buildAssignVariable() {
        byte evalAt = bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        String name = bufObj.readString();
        String value = bufObj.readString();
        AssignVariable aVariable = new AssignVariable(name, value, evalAt);
        return aVariable;
    }

    private State.DeclareVariable buildDeclareVariable() {
        byte type = bufObj.readByte();
        byte struct = bufObj.readByte();
        byte scope = bufObj.readByte();
        byte access = bufObj.readByte();
        String name = bufObj.readString();
        DeclareVariable variable = new DeclareVariable(name, type, struct, scope, access);
        return variable;
    }

    private void buildDeclareService() {
        String name = bufObj.readString();
        servicePrototype.setName(name);
    }

    private DeclareArgument buildDeclareServiceRequest() {
        byte type = bufObj.readByte();
        byte struct = bufObj.readByte();
        bufObj.readByte();
        bufObj.readByte();
        String name = bufObj.readString();
        DeclareArgument serviceRequest = new DeclareArgument(name, type, struct);
        return serviceRequest;
    }

    class BufObj {

        byte[] buf;

        int index = 0;

        int endOfData = 0;

        public BufObj(byte[] byteArray) {
            this.buf = byteArray;
        }

        public void setEndOfData(int size) {
            endOfData = size;
        }

        public boolean endOfData() {
            return index >= endOfData;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public int readShort(byte buf[]) {
            int index = 0;
            int i0 = buf[index] & 0xff;
            int i1 = buf[index + 1] & 0xff;
            int convInt = i0 + i1 * 256;
            if ((convInt & 0x8000) == 0x8000) convInt = -(0x10000 - convInt);
            index += 2;
            return convInt;
        }

        private int readShort() {
            int i0 = buf[index] & 0xff;
            int i1 = buf[index + 1] & 0xff;
            int convInt = i0 + i1 * 256;
            if ((convInt & 0x8000) == 0x8000) convInt = -(0x10000 - convInt);
            index += 2;
            return convInt;
        }

        private int readInt() {
            int i0 = buf[index] & 0xff;
            int i1 = buf[index + 1] & 0xff;
            int i2 = buf[index + 2] & 0xff;
            int i3 = buf[index + 3] & 0xff;
            long convInt = i0 + i1 * 256 + i2 * 65536 + i3 * 16777216;
            if ((convInt & 0x80000000L) == 0x80000000L) convInt = -(0x100000000L - convInt);
            index += 4;
            return (int) convInt;
        }

        private byte readByte() {
            return buf[index++];
        }

        private String readString() {
            int startInd = index;
            int i;
            for (i = 0; buf[index++] != 0; i++) {
                ;
            }
            if (i == 0) return null; else {
                String s = null;
                try {
                    s = new String(buf, startInd, i, "UTF-8");
                } catch (UnsupportedEncodingException uee) {
                    logger.print(Logger.INFO, uee.getMessage());
                }
                return s;
            }
        }
    }

    public static int getUnsignedByte(byte value) {
        return value << 24 >>> 24;
    }

    private Table buildTable() {
        table = new Table();
        table.gap = bufObj.readByte();
        table.border = bufObj.readByte();
        table.geomInfo.pCoord[0] = bufObj.readByte();
        table.geomInfo.pCoord[1] = bufObj.readByte();
        table.geomInfo.pDepCoord[0] = bufObj.readByte();
        table.geomInfo.pDepCoord[1] = bufObj.readByte();
        table.geomInfo.pDelta[0] = bufObj.readInt();
        table.geomInfo.pDelta[1] = bufObj.readInt();
        table.borderColor = bufObj.readInt();
        int tmp = bufObj.readByte();
        table.align = new byte[tmp];
        for (int i = 0; i < tmp; ++i) {
            table.align[i] = bufObj.readByte();
        }
        table.id = bufObj.readString();
        table.fontName = bufObj.readString();
        table.geomInfo.pDepID[0] = bufObj.readString();
        table.geomInfo.pDepID[1] = bufObj.readString();
        return table;
    }

    private Row buildRow() {
        Row row = new Row();
        row.fontColor = bufObj.readInt();
        row.bgColor[0] = bufObj.readInt();
        row.bgColor[1] = bufObj.readInt();
        return row;
    }

    private Cell buildCell() {
        Cell cell = new Cell();
        cell.type = bufObj.readByte();
        cell.content = bufObj.readString();
        cell.nav = bufObj.readString();
        cell.url = bufObj.readString();
        return cell;
    }

    public Table table = null;
}
