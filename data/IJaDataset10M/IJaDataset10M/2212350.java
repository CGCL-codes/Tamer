package com.sts.webmeet.server;

import com.sun.star.uno.UnoRuntime;
import com.sun.star.lang.*;
import com.sun.star.drawing.*;
import com.sun.star.presentation.*;
import com.sun.star.beans.XPropertySet;
import com.sun.star.awt.Size;
import com.sun.star.container.XNamed;
import org.apache.log4j.Logger;

public class PageHelper {

    private static Logger logger = Logger.getLogger(PageHelper.class);

    /** get the page count for standard pages
    */
    public static int getDrawPageCount(XComponent xComponent) {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return xDrawPages.getCount();
    }

    public static String getDrawPageName(XDrawPage xComponent) {
        XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class, xComponent);
        if (null == xNamed) {
            logger.info("PageHelper.getDrawPageName() " + " XNamed not found.");
            return null;
        } else {
            return xNamed.getName();
        }
    }

    /** get draw page by index
    */
    public static XDrawPage getDrawPageByIndex(XComponent xComponent, int nIndex) throws com.sun.star.lang.IndexOutOfBoundsException, com.sun.star.lang.WrappedTargetException {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return (XDrawPage) UnoRuntime.queryInterface(XDrawPage.class, xDrawPages.getByIndex(nIndex));
    }

    /** creates and inserts a draw page into the giving position,
        the method returns the new created page
    */
    public static XDrawPage insertNewDrawPageByIndex(XComponent xComponent, int nIndex) throws Exception {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return xDrawPages.insertNewByIndex(nIndex);
    }

    /** removes the given page
    */
    public static void removeDrawPage(XComponent xComponent, XDrawPage xDrawPage) {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        xDrawPages.remove(xDrawPage);
    }

    /** get size of the given page
    */
    public static Size getPageSize(XDrawPage xDrawPage) throws com.sun.star.beans.UnknownPropertyException, com.sun.star.lang.WrappedTargetException {
        XPropertySet xPageProperties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xDrawPage);
        return new Size(((Integer) xPageProperties.getPropertyValue("Width")).intValue(), ((Integer) xPageProperties.getPropertyValue("Height")).intValue());
    }

    /** get the page count for master pages
    */
    public static int getMasterPageCount(XComponent xComponent) {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        return xDrawPages.getCount();
    }

    /** get master page by index
    */
    public static XDrawPage getMasterPageByIndex(XComponent xComponent, int nIndex) throws com.sun.star.lang.IndexOutOfBoundsException, com.sun.star.lang.WrappedTargetException {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        return (XDrawPage) UnoRuntime.queryInterface(XDrawPage.class, xDrawPages.getByIndex(nIndex));
    }

    /** creates and inserts a new master page into the giving position,
        the method returns the new created page
    */
    public static XDrawPage insertNewMasterPageByIndex(XComponent xComponent, int nIndex) {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        return xDrawPages.insertNewByIndex(nIndex);
    }

    /** removes the given page
    */
    public static void removeMasterPage(XComponent xComponent, XDrawPage xDrawPage) {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        xDrawPages.remove(xDrawPage);
    }

    /** return the corresponding masterpage for the giving drawpage
    */
    public static XDrawPage getMasterPage(XDrawPage xDrawPage) {
        XMasterPageTarget xMasterPageTarget = (XMasterPageTarget) UnoRuntime.queryInterface(XMasterPageTarget.class, xDrawPage);
        return xMasterPageTarget.getMasterPage();
    }

    /** sets given masterpage at the drawpage
    */
    public static void setMasterPage(XDrawPage xDrawPage, XDrawPage xMasterPage) {
        XMasterPageTarget xMasterPageTarget = (XMasterPageTarget) UnoRuntime.queryInterface(XMasterPageTarget.class, xDrawPage);
        xMasterPageTarget.setMasterPage(xMasterPage);
    }

    /** test if a Presentation Document is supported.
        This is important, because only presentation documents
        have notes and handout pages
    */
    public static boolean isImpressDocument(XComponent xComponent) {
        XServiceInfo xInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, xComponent);
        return xInfo.supportsService("com.sun.star.presentation.PresentationDocument");
    }

    /** in impress documents each normal draw page has a corresponding notes page
    */
    public static XDrawPage getNotesPage(XDrawPage xDrawPage) {
        XPresentationPage aPresentationPage = (XPresentationPage) UnoRuntime.queryInterface(XPresentationPage.class, xDrawPage);
        return aPresentationPage.getNotesPage();
    }

    /** in impress each documents has one handout page
    */
    public static XDrawPage getHandoutMasterPage(XComponent xComponent) {
        XHandoutMasterSupplier aHandoutMasterSupplier = (XHandoutMasterSupplier) UnoRuntime.queryInterface(XHandoutMasterSupplier.class, xComponent);
        return aHandoutMasterSupplier.getHandoutMasterPage();
    }
}
