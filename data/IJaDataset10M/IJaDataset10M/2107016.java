package org.bigbluebuttonproject.fileupload.document.impl;

import com.sun.star.uno.UnoRuntime;
import com.sun.star.lang.*;
import com.sun.star.drawing.*;
import com.sun.star.presentation.*;
import com.sun.star.beans.XPropertySet;
import com.sun.star.awt.Size;
import com.sun.star.container.XNamed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class PageHelper.
 */
public class PageHelper {

    /** The Constant logger. */
    private static final Log logger = LogFactory.getLog(PageHelper.class);

    /**
  * get the page count for standard pages.
  * 
  * @param xComponent the x component
  * 
  * @return the draw page count
  */
    public static int getDrawPageCount(XComponent xComponent) {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return xDrawPages.getCount();
    }

    /**
  * Gets the draw pages.
  * 
  * @param xComponent the x component
  * 
  * @return the draw pages
  */
    public static XDrawPages getDrawPages(XComponent xComponent) {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return xDrawPages;
    }

    /**
      * Gets the draw page name.
      * 
      * @param xComponent the x component
      * 
      * @return the draw page name
      */
    public static String getDrawPageName(XDrawPage xComponent) {
        XNamed xNamed = (XNamed) UnoRuntime.queryInterface(XNamed.class, xComponent);
        if (null == xNamed) {
            logger.info("PageHelper.getDrawPageName() " + " XNamed not found.");
            return null;
        } else {
            return xNamed.getName();
        }
    }

    /**
  * get draw page by index.
  * 
  * @param xComponent the x component
  * @param nIndex the n index
  * 
  * @return the draw page by index
  * 
  * @throws IndexOutOfBoundsException the index out of bounds exception
  * @throws WrappedTargetException the wrapped target exception
  */
    public static XDrawPage getDrawPageByIndex(XComponent xComponent, int nIndex) throws com.sun.star.lang.IndexOutOfBoundsException, com.sun.star.lang.WrappedTargetException {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return (XDrawPage) UnoRuntime.queryInterface(XDrawPage.class, xDrawPages.getByIndex(nIndex));
    }

    /**
  * creates and inserts a draw page into the giving position,
  * the method returns the new created page.
  * 
  * @param xComponent the x component
  * @param nIndex the n index
  * 
  * @return the x draw page
  * 
  * @throws Exception the exception
  */
    public static XDrawPage insertNewDrawPageByIndex(XComponent xComponent, int nIndex) throws Exception {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        return xDrawPages.insertNewByIndex(nIndex);
    }

    /**
  * removes the given page.
  * 
  * @param xComponent the x component
  * @param xDrawPage the x draw page
  */
    public static void removeDrawPage(XComponent xComponent, XDrawPage xDrawPage) {
        XDrawPagesSupplier xDrawPagesSupplier = (XDrawPagesSupplier) UnoRuntime.queryInterface(XDrawPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xDrawPagesSupplier.getDrawPages();
        xDrawPages.remove(xDrawPage);
    }

    /**
  * get size of the given page.
  * 
  * @param xDrawPage the x draw page
  * 
  * @return the page size
  * 
  * @throws UnknownPropertyException the unknown property exception
  * @throws WrappedTargetException the wrapped target exception
  */
    public static Size getPageSize(XDrawPage xDrawPage) throws com.sun.star.beans.UnknownPropertyException, com.sun.star.lang.WrappedTargetException {
        XPropertySet xPageProperties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xDrawPage);
        return new Size(((Integer) xPageProperties.getPropertyValue("Width")).intValue(), ((Integer) xPageProperties.getPropertyValue("Height")).intValue());
    }

    /**
  * get the page count for master pages.
  * 
  * @param xComponent the x component
  * 
  * @return the master page count
  */
    public static int getMasterPageCount(XComponent xComponent) {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        return xDrawPages.getCount();
    }

    /**
  * get master page by index.
  * 
  * @param xComponent the x component
  * @param nIndex the n index
  * 
  * @return the master page by index
  * 
  * @throws IndexOutOfBoundsException the index out of bounds exception
  * @throws WrappedTargetException the wrapped target exception
  */
    public static XDrawPage getMasterPageByIndex(XComponent xComponent, int nIndex) throws com.sun.star.lang.IndexOutOfBoundsException, com.sun.star.lang.WrappedTargetException {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        return (XDrawPage) UnoRuntime.queryInterface(XDrawPage.class, xDrawPages.getByIndex(nIndex));
    }

    /**
  * creates and inserts a new master page into the giving position,
  * the method returns the new created page.
  * 
  * @param xComponent the x component
  * @param nIndex the n index
  * 
  * @return the x draw page
  */
    public static XDrawPage insertNewMasterPageByIndex(XComponent xComponent, int nIndex) {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        return xDrawPages.insertNewByIndex(nIndex);
    }

    /**
  * removes the given page.
  * 
  * @param xComponent the x component
  * @param xDrawPage the x draw page
  */
    public static void removeMasterPage(XComponent xComponent, XDrawPage xDrawPage) {
        XMasterPagesSupplier xMasterPagesSupplier = (XMasterPagesSupplier) UnoRuntime.queryInterface(XMasterPagesSupplier.class, xComponent);
        XDrawPages xDrawPages = xMasterPagesSupplier.getMasterPages();
        xDrawPages.remove(xDrawPage);
    }

    /**
  * return the corresponding masterpage for the giving drawpage.
  * 
  * @param xDrawPage the x draw page
  * 
  * @return the master page
  */
    public static XDrawPage getMasterPage(XDrawPage xDrawPage) {
        XMasterPageTarget xMasterPageTarget = (XMasterPageTarget) UnoRuntime.queryInterface(XMasterPageTarget.class, xDrawPage);
        return xMasterPageTarget.getMasterPage();
    }

    /**
  * sets given masterpage at the drawpage.
  * 
  * @param xDrawPage the x draw page
  * @param xMasterPage the x master page
  */
    public static void setMasterPage(XDrawPage xDrawPage, XDrawPage xMasterPage) {
        XMasterPageTarget xMasterPageTarget = (XMasterPageTarget) UnoRuntime.queryInterface(XMasterPageTarget.class, xDrawPage);
        xMasterPageTarget.setMasterPage(xMasterPage);
    }

    /**
  * test if a Presentation Document is supported.
  * This is important, because only presentation documents
  * have notes and handout pages
  * 
  * @param xComponent the x component
  * 
  * @return true, if checks if is impress document
  */
    public static boolean isImpressDocument(XComponent xComponent) {
        XServiceInfo xInfo = (XServiceInfo) UnoRuntime.queryInterface(XServiceInfo.class, xComponent);
        return xInfo.supportsService("com.sun.star.presentation.PresentationDocument");
    }

    /**
  * in impress documents each normal draw page has a corresponding notes page.
  * 
  * @param xDrawPage the x draw page
  * 
  * @return the notes page
  */
    public static XDrawPage getNotesPage(XDrawPage xDrawPage) {
        XPresentationPage aPresentationPage = (XPresentationPage) UnoRuntime.queryInterface(XPresentationPage.class, xDrawPage);
        return aPresentationPage.getNotesPage();
    }

    /**
  * in impress each documents has one handout page.
  * 
  * @param xComponent the x component
  * 
  * @return the handout master page
  */
    public static XDrawPage getHandoutMasterPage(XComponent xComponent) {
        XHandoutMasterSupplier aHandoutMasterSupplier = (XHandoutMasterSupplier) UnoRuntime.queryInterface(XHandoutMasterSupplier.class, xComponent);
        return aHandoutMasterSupplier.getHandoutMasterPage();
    }
}
