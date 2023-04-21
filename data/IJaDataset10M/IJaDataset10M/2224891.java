package si.cit.eprojekti.eproject.controller.browseStates;

import java.util.Locale;
import si.cit.eprojekti.eproject.ProjectSchema;
import com.jcorporate.expresso.core.controller.Block;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.i18n.Messages;

/**
 *	
 *	Navigation - external file for ListAssetState and ListAssetCategoryState
 *  It is used to generate navigation (etc. 1 2 3 4 Next)
 *
 * 	@author taks
 *	@version 1.0
 *
 */
public class Navigation {

    public Class controller = null;

    public String navigName = "";

    public String tranName = "";

    public String forward = "";

    public String back = "";

    public String state = "";

    /**
	 * 	Constructor
	 */
    public Navigation() {
        controller = null;
    }

    /**
	 * 	Constructor
	 *  @param controllerName - Class
	 */
    public Navigation(Class controllerName) {
        controller = controllerName;
    }

    /**
	 * 	Constructor
	 *  @param controllerName - Class
	 *  @param blockName - String
	 */
    public Navigation(Class controllerName, String blockName) {
        controller = controllerName;
        navigName = blockName;
    }

    /**
	 * 	Set up default controller
	 *  @param controllerName - Class
	 */
    public void setController(Class controllerName) {
        controller = controllerName;
    }

    /**
	 * 	Set up default navigation block name
	 *  @param blockName - String
	 */
    public void setNavigationBlockName(String blockName) {
        navigName = blockName;
    }

    /**
	 * 	Set up main tranzition name
	 *  @param tranzitionName - String
	 */
    public void setMainTranzitionName(String tranzitionName) {
        tranName = tranzitionName;
    }

    /**
	 * 	Set up default navigation forward and back buttons
	 *  @param forward - String
	 *  @param back - String
	 */
    public void setNavigationForwardAndBack(String forward, String back) {
        this.forward = forward;
        this.back = back;
    }

    public void setTranzitionState(String state) {
        this.state = state;
    }

    /**
	 * 	Get navigation block
	 *  @param navigationPerPage - int
	 * 	@param numOfItemsPerPage - int
	 * 	@param numOfPages - int
	 * 	@param pageNumber - int
	 * 	@param categoryName - int
	 * 	@return Block
	 */
    public Block getNavigationBlock(int navigationPerPage, int numOfItemsPerPage, int numOfPages, int pageNumber, int categoryId, int projectId, String classType) {
        int navPerPage = navigationPerPage;
        String perPage = String.valueOf(numOfItemsPerPage);
        String category = String.valueOf(categoryId);
        int min = (pageNumber / navPerPage) * navPerPage;
        if (pageNumber % navPerPage == 0) min = min - navPerPage;
        int max = min + navPerPage + 1;
        if (max > numOfPages) max = numOfPages + 1;
        Locale locale;
        locale = Locale.getDefault();
        Block navigation = new Block();
        navigation.setName(navigName);
        for (int ii = min; ii <= max; ii++) {
            int newOffset = ii * Integer.parseInt(perPage) + 1 - Integer.parseInt(perPage);
            Block navPage = new Block("Page: " + ii);
            Transition page = new Transition();
            page.setName(tranName);
            page.setLabel(Messages.getString(ProjectSchema.class.getName(), locale, "navigationPageLabel") + " " + ii);
            page.setControllerObject(controller);
            page.setState(state);
            page.addParam("category", category);
            page.addParam("projectId", String.valueOf(projectId));
            if (classType != null) page.addParam("type", classType);
            page.setAttribute("pageNumber", String.valueOf(ii));
            if ((numOfPages == 1) && (ii == 1)) {
                page.addParam("offset", String.valueOf(newOffset));
                page.setAttribute("pageLabel", String.valueOf(ii));
                navPage.add(page);
                navigation.addNested(navPage);
            }
            if (numOfPages > 1) {
                if (pageNumber == 1) {
                    if ((ii > min) && (ii <= numOfPages)) page.setAttribute("pageLabel", String.valueOf(ii));
                    if ((ii > min) && (ii == max)) {
                        newOffset = (pageNumber + 1) * Integer.parseInt(perPage) + 1 - Integer.parseInt(perPage);
                        page.setAttribute("pageLabel", forward);
                    }
                    page.addParam("offset", String.valueOf(newOffset));
                    navPage.add(page);
                    navigation.addNested(navPage);
                }
                if (pageNumber == numOfPages) {
                    if ((ii > min) && (ii <= numOfPages)) page.setAttribute("pageLabel", String.valueOf(ii));
                    if (ii == min) {
                        newOffset = (pageNumber - 1) * Integer.parseInt(perPage) + 1 - Integer.parseInt(perPage);
                        page.setAttribute("pageLabel", back);
                    }
                    page.addParam("offset", String.valueOf(newOffset));
                    navPage.add(page);
                    navigation.addNested(navPage);
                }
                if ((pageNumber > 1) && (pageNumber < numOfPages)) {
                    if ((pageNumber > 1) && (ii == min)) {
                        newOffset = (pageNumber - 1) * Integer.parseInt(perPage) + 1 - Integer.parseInt(perPage);
                        page.setAttribute("pageLabel", back);
                    }
                    if ((pageNumber > 1) && (ii > min) && (ii == max)) {
                        newOffset = (pageNumber + 1) * Integer.parseInt(perPage) + 1 - Integer.parseInt(perPage);
                        page.setAttribute("pageLabel", forward);
                    }
                    if ((pageNumber > 1) && (ii > min) && (ii <= max - 1)) page.setAttribute("pageLabel", String.valueOf(ii));
                    page.addParam("offset", String.valueOf(newOffset));
                    navPage.add(page);
                    navigation.addNested(navPage);
                }
            }
        }
        return navigation;
    }
}
