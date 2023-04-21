package fhire;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import fhire.database.hibernate.ReferenceCityField;
import fhire.database.hibernate.RequiredField;
import fhire.database.hibernate.Resource;
import fhire.database.hibernate.ResourceCity;
import fhire.database.hibernate.util.DatabaseManager;
import fhire.exceptions.CityNotFoundException;
import fhire.exceptions.FieldNotFoundException;
import fhire.exceptions.NoHTTPSessionException;
import fhire.utils.Buildinglist;
import fhire.utils.Initialisator;
import fhire.utils.Buildings.Building;
import fhire.xml.MenuWorker;

/**
 * This class resolves all AJAX request.
 * 
 * @author Joerg Doppelreiter, Johann Zagler, Robert Maierhofer.
 * 
 */
public class AjaxResolver extends Resolver {

    private static final String NUMBER_OF_WORKERS = "numberOfWorkers";

    private Logger logLogger;

    private MenuWorker mwkWorker;

    /**
	 * 
	 * @param dbmDBManager
	 * @param hsnSession
	 */
    public AjaxResolver(DatabaseManager dbmDBManager, Session hsnSession) {
        super(dbmDBManager, hsnSession);
        init();
        mwkWorker = new MenuWorker();
    }

    public Document alter(Buildinglist bdlBuildinglist, String[] strParameter, int intCityID) {
        byte bytResourceID;
        int intDeAllocatingWorkers;
        Document docResponse;
        Element elmObjectName;
        Element elmObjectValue;
        Element elmRoot;
        Element elmTable;
        docResponse = new Document();
        elmObjectName = new Element("objectName");
        elmObjectValue = new Element("objectValue");
        elmRoot = new Element("response");
        elmTable = null;
        bytResourceID = new Byte(strParameter[1]).byteValue();
        intDeAllocatingWorkers = new Integer(strParameter[2]).intValue();
        if (strParameter[0].compareToIgnoreCase(NUMBER_OF_WORKERS) == 0) {
            bdlBuildinglist = alterNumberOfWorkers(bdlBuildinglist, intCityID, bytResourceID, intDeAllocatingWorkers, strParameter[3]);
            elmObjectName.setText(Initialisator.getResourcesByID().get(new Byte(bytResourceID)).getName() + "Worker");
            elmObjectValue.setText(new Integer(bdlBuildinglist.getWorker(bytResourceID)[1]).toString());
            elmTable = mwkWorker.createTable(workerMenuContent(bdlBuildinglist));
        }
        elmRoot.addContent(elmObjectName);
        elmRoot.addContent(elmObjectValue);
        if (elmTable != null) {
            elmRoot.addContent(elmTable);
        }
        docResponse.setRootElement(elmRoot);
        return docResponse;
    }

    /**
	 * Builds a building a given city on a given position (x, y)
	 * 
	 * @param ssnSession
	 * @param bdlBuildinglist
	 * @param intCityID
	 * @param intX
	 * @param intY
	 * @param bytCode
	 * @return Document
	 * @throws NoHTTPSessionException
	 */
    public Document build(HttpSession ssnSession, Buildinglist bdlBuildinglist, int intCityID, int intX, int intY, byte bytCode, byte bytExtensionState) throws NoHTTPSessionException {
        byte i;
        byte bytBaseType;
        int intFieldID;
        int intNewFieldID;
        int[][] intNeededResources;
        int[][] intResourcelistRestAmount;
        Iterator<ResourceCity> itrResourceCity;
        List<ResourceCity> lstResourceCity;
        HashMap<Byte, Integer> hamResourcelist;
        if (ssnSession == null) {
            throw new NoHTTPSessionException();
        }
        try {
            hamResourcelist = new HashMap<Byte, Integer>();
            lstResourceCity = getDBManager().getResourceCityList(getSession(), intCityID);
            itrResourceCity = lstResourceCity.iterator();
            while (itrResourceCity.hasNext()) {
                ResourceCity rscResource;
                rscResource = itrResourceCity.next();
                hamResourcelist.put(new Byte(rscResource.getResource().getResourceId()), new Integer(rscResource.getAmount()));
            }
            intFieldID = getDBManager().getField(getSession(), intCityID, (intY * 8) + intX).getFieldId().intValue();
            bytBaseType = (byte) ((intFieldID / 100) % 10);
            intNewFieldID = 1000 + (bytBaseType * 100) + bytCode;
            System.out.println("Old Field ID: " + intFieldID);
            System.out.println("New Field ID: " + intNewFieldID);
            if (bdlBuildinglist.contains(intNewFieldID)) {
                boolean booHasEnoughResources;
                booHasEnoughResources = true;
                intNeededResources = Initialisator.getBuildings().getBuilding(intNewFieldID).getPrerequisiteResources(bytExtensionState);
                intResourcelistRestAmount = new int[intNeededResources.length][2];
                for (i = 0; i < intNeededResources.length; i = (byte) (i + 1)) {
                    byte bytResourceID;
                    int intAmount;
                    bytResourceID = new Integer(intNeededResources[i][0]).byteValue();
                    if ((intAmount = hamResourcelist.get(new Byte(bytResourceID)).intValue()) < intNeededResources[i][1]) {
                        booHasEnoughResources = false;
                        break;
                    }
                    intResourcelistRestAmount[i][0] = bytResourceID;
                    intResourcelistRestAmount[i][1] = intAmount - intNeededResources[i][1];
                    System.out.println("Resource-ID: " + bytResourceID + " (" + Initialisator.getResourcesByID().get(new Byte(bytResourceID)).getName() + ")");
                    System.out.println("  Has: " + intAmount + ", needs: " + intNeededResources[i][1]);
                }
                if (booHasEnoughResources) {
                    short shoTicks;
                    Document docResponse;
                    Element elmBuilt;
                    Element elmContent;
                    Element elmResource;
                    Element elmResources;
                    docResponse = new Document();
                    elmBuilt = new Element("built");
                    elmResources = new Element("resources");
                    elmContent = new Element("content").setText(new Byte(bytBaseType).toString());
                    shoTicks = Initialisator.getBuildings().getBuilding(intNewFieldID).getNeededTicks((byte) 0);
                    getDBManager().updateField(getSession(), intCityID, (intY * 8) + intX, intNewFieldID, shoTicks, (byte) 1);
                    for (i = 0; i < intResourcelistRestAmount.length; i = (byte) (i + 1)) {
                        System.out.println("Session: " + getSession() + ", CityID: " + intCityID + ", ResNo: " + i + ", Amount: " + intResourcelistRestAmount[i]);
                        getDBManager().updateResourceCityAmount(getSession(), intCityID, new Integer(intResourcelistRestAmount[i][0]).byteValue(), intResourcelistRestAmount[i][1]);
                        elmResource = new Element(Initialisator.getResourcesByID().get(new Integer(intResourcelistRestAmount[i][0]).byteValue()).getName()).setText(new Integer(intResourcelistRestAmount[i][1]).toString());
                        elmResources.addContent(elmResource);
                    }
                    bdlBuildinglist.updateBuildinglist(getDBManager(), getSession(), intNewFieldID, bytExtensionState);
                    elmBuilt.addContent(elmContent);
                    elmBuilt.addContent(elmResources);
                    docResponse.setRootElement(elmBuilt);
                    return docResponse;
                } else {
                    return new Document(new Element("Du-hast-zu-wenig-Ressourcen"));
                }
            }
        } catch (CityNotFoundException cnex) {
            cnex.printStackTrace();
        } catch (FieldNotFoundException fnex) {
            fnex.printStackTrace();
        }
        return new Document(new Element("Du-hast-einen-Error-fabriziert"));
    }

    /**
	 * Simplify procedure (buildingslist (byte[]) to buildableObjects (String[]))
	 * 
	 * @param bdlBuildinglist
	 * @param intCityID
	 * @param intFieldID
	 * @return String[][]
	 * @throws CityNotFoundException
	 */
    public String[][] buildableObjects(Buildinglist bdlBuildinglist, int intCityID, int intFieldID) throws CityNotFoundException {
        byte i;
        byte bytBaseType;
        byte[][] bytBuildings;
        byte bytNumberOfBuildings;
        int intNewFieldID;
        TreeMap<Integer, Building> trmBuildings;
        String[][] strBuilding;
        bytBaseType = (byte) ((intFieldID / 100) % 10);
        bytBuildings = bdlBuildinglist.listBuildings(bytBaseType);
        bytNumberOfBuildings = (byte) bytBuildings.length;
        strBuilding = new String[bytNumberOfBuildings][2];
        trmBuildings = Initialisator.getBuildings().getBuildingsAsMap(bytBaseType);
        System.out.println(trmBuildings.entrySet());
        for (i = 0; i < bytNumberOfBuildings; i = (byte) (i + 1)) {
            intNewFieldID = 1000 + (bytBaseType * 100) + bytBuildings[i][0];
            System.out.println("FieldID of list: " + intNewFieldID);
            strBuilding[i][0] = new Integer(intNewFieldID).toString();
            if (trmBuildings.containsKey(new Integer(intNewFieldID))) {
                strBuilding[i][1] = trmBuildings.get(new Integer(intNewFieldID)).getName();
            } else {
                strBuilding[i][1] = "error retrieving " + intNewFieldID;
            }
        }
        return strBuilding;
    }

    /**
	 * Change a field ID in a given city on a specified postition (x, y).
	 * 
	 * @param intCityID
	 * @param intNewFieldID
	 * @param intX
	 * @param intY
	 * @return Document
	 */
    public Document changeFieldID(int intCityID, int intNewFieldID, int intX, int intY) {
        int intFieldID;
        int intSiteID;
        Document docResponse;
        Element elmContent;
        docResponse = new Document();
        try {
            intFieldID = getDBManager().getField(getSession(), intCityID, (intY * 8) + intX).getFieldId().intValue();
            intSiteID = (((intFieldID / 100) % 10) * 100) + ((intNewFieldID / 100) % 10);
            elmContent = new Element("content").setText(new Integer(intSiteID).toString());
            docResponse.setRootElement(elmContent);
        } catch (FieldNotFoundException fnex) {
            fnex.printStackTrace();
        }
        return docResponse;
    }

    /**
	 * Create the construction menu for the a field in a given city.
	 * 
	 * @param bdlBuildinglist
	 * @param intCityID
	 * @param intFieldID
	 * @param intX
	 * @param intY
	 * @param strContextPath
	 * @return Document
	 * @throws CityNotFoundException
	 */
    private Document constructionMenu(Buildinglist bdlBuildinglist, int intCityID, int intFieldID, int intX, int intY, String strContextPath) throws CityNotFoundException {
        Document docConstructionMenu;
        String[][] strEntries;
        String[][] strMenubarEntries;
        strEntries = buildableObjects(bdlBuildinglist, intCityID, intFieldID);
        strMenubarEntries = menubarObjects(intFieldID);
        if (strEntries.length == 0) {
            docConstructionMenu = new Document();
            docConstructionMenu.setRootElement(new Element("empty"));
            logLogger.debug("No buildable structure for this field");
        } else {
            docConstructionMenu = mwkWorker.menu(mwkWorker.createList(strEntries), strMenubarEntries, strContextPath, Initialisator.getConstructionMenuTitle(), Initialisator.getConstructionMenuTypeConstruct(), intX, intY, false);
        }
        return docConstructionMenu;
    }

    /**
	 * Deforest a forest field to a grass field
	 * 
	 * @param strContextPath
	 * @param intX
	 * @param intY
	 */
    public void deforest(String strContextPath, int intX, int intY) {
    }

    /**
	 * Returns the extended Menu.
	 * 
	 * @param strCode
	 * @param strContextPath
	 * @return Document
	 */
    public Document extendedMenu(Buildinglist bdlBuildinglist, String strContextPath, int intCityID, String strCode) {
        Document docExtendedMenu;
        if (strCode.compareToIgnoreCase("test") == 0) {
            String[][][] strContent;
            String[][] strMenuLinks;
            strContent = new String[1][1][2];
            strMenuLinks = new String[2][2];
            strContent[0][0][0] = "Test 1";
            strContent[0][0][1] = "test1";
            strMenuLinks[0][0] = "Tab 1";
            strMenuLinks[0][1] = "tab1";
            strMenuLinks[1][0] = "Tab 2";
            strMenuLinks[1][1] = "tab2";
            docExtendedMenu = mwkWorker.menu(mwkWorker.createTable(strContent), strMenuLinks, strContextPath, "extended Menu", "extended", 0, 0, true);
        } else if (strCode.compareToIgnoreCase("worker") == 0) {
            docExtendedMenu = workerMenu(bdlBuildinglist, strContextPath, intCityID);
        } else {
            docExtendedMenu = workerMenu(bdlBuildinglist, strContextPath, intCityID);
        }
        return docExtendedMenu;
    }

    /**
	 * Afforest a grass field to a forrest field.
	 * 
	 * @param strContextPath
	 * @param intCityID
	 * @param intX
	 * @param intY
	 */
    public void forest(String strContextPath, int intCityID, int intX, int intY) {
    }

    /**
	 * Get information about a field in a city.
	 * 
	 * @param intCityID
	 * @param intFieldID
	 * @param bytExtensionState
	 * @return Document
	 */
    public Document getInfo(int intCityID, int intFieldID, byte bytExtensionState) {
        int[][] intResources;
        short shoTicks;
        Building bldBuilding;
        Document docInfo;
        bldBuilding = Initialisator.getBuildings().getBuilding(intFieldID);
        shoTicks = bldBuilding.getNeededTicks(bytExtensionState);
        intResources = bldBuilding.getPrerequisiteResources(bytExtensionState);
        System.out.println("Info:\n  Resources: " + intResources.length + "\n  Ticks: " + shoTicks);
        for (int i = 0; i < intResources.length; i = i + 1) {
            System.out.println("Resource (" + i + "): " + intResources[i]);
        }
        docInfo = mwkWorker.info(intResources, shoTicks);
        return docInfo;
    }

    /**
	 * Returns the menu for a field in a city. 
	 * 
	 * @param bdlBuildinglist
	 * @param intCityID
	 * @param intX
	 * @param intY
	 * @param strContextPath
	 * @return Document
	 */
    public Document getMenu(Buildinglist bdlBuildinglist, int intCityID, int intX, int intY, String strContextPath) {
        int intFieldID;
        Document docMenu;
        ReferenceCityField rcfReferenceCityField;
        System.out.println("City-ID: " + intCityID + ", X: " + intX + ", Y: " + intY + "; by DBM: " + getDBManager());
        try {
            rcfReferenceCityField = getDBManager().getReferenceCityField(getSession(), intCityID, (intY * 8) + intX);
            intFieldID = rcfReferenceCityField.getField().getFieldId();
            if (intFieldID == 1715) {
                String strCode = "";
                docMenu = extendedMenu(bdlBuildinglist, strContextPath, intCityID, strCode);
            } else if (intFieldID / 1000 == Initialisator.BASE_FIELD) {
                docMenu = constructionMenu(bdlBuildinglist, intCityID, intFieldID, intX, intY, strContextPath);
            } else if (intFieldID / 1000 == Initialisator.BUILDING_FIELD) {
                docMenu = upgradeMenu(strContextPath, rcfReferenceCityField, intX, intY);
            } else {
                docMenu = null;
            }
        } catch (CityNotFoundException cnex) {
            cnex.printStackTrace();
            docMenu = new Document();
        }
        return docMenu;
    }

    private void init() {
        logLogger = Logger.getLogger(this.getClass());
    }

    /**
	 * Knock down a building.
	 * 
	 * @param strContextPath
	 * @param intX
	 * @param intY
	 */
    public void knockDown(String strContextPath, int intX, int intY) {
    }

    /**
	 * 
	 * @param intFieldID
	 * @return String[][]
	 */
    private String[][] menubarObjects(int intFieldID) {
        int i;
        Iterator<RequiredField> itrConvertableFields;
        List<RequiredField> lstConvertableFields;
        String[][] strMenubarObjects;
        i = 0;
        lstConvertableFields = Initialisator.getConvertableFields().get(new Integer(intFieldID));
        if (lstConvertableFields == null) {
            return new String[0][2];
        }
        itrConvertableFields = lstConvertableFields.iterator();
        strMenubarObjects = new String[lstConvertableFields.size()][2];
        while (itrConvertableFields.hasNext()) {
            int intNewFieldID;
            int intOldFieldID;
            RequiredField rqfField;
            rqfField = itrConvertableFields.next();
            intNewFieldID = (rqfField.getLevelSystem().getField().getFieldId() / 100) % 10;
            intOldFieldID = (intFieldID / 100) % 10;
            strMenubarObjects[i][0] = new Integer((intOldFieldID * 100) + (intNewFieldID)).toString();
            strMenubarObjects[i][1] = "cf.gif";
            i = i + 1;
        }
        return strMenubarObjects;
    }

    /**
	 * Upgrade a building
	 * 
	 * @param strContextPath
	 * @param intX
	 * @param intY
	 */
    public void upgrade(String strContextPath, int intX, int intY) {
    }

    /**
	 * Returns the upgrade menu for a specified field.
	 * 
	 * @param strContextPath
	 * @param rcfReferenceCityField
	 * @param intX
	 * @param intY
	 * @return Returns
	 */
    private Document upgradeMenu(String strContextPath, ReferenceCityField rcfReferenceCityField, int intX, int intY) {
        Document docUpgradeMenu;
        String[][] strEntries;
        String[][] strMenubarEntries;
        strEntries = new String[1][2];
        strMenubarEntries = new String[1][2];
        strEntries[0][0] = rcfReferenceCityField.getField().getName();
        strEntries[0][1] = new Byte(rcfReferenceCityField.getExtensionState()).toString();
        strMenubarEntries[0][0] = "upgrade";
        strMenubarEntries[0][1] = "upgrade";
        if (strEntries.length == 0) {
            docUpgradeMenu = new Document();
        } else {
            docUpgradeMenu = mwkWorker.menu(mwkWorker.createList(strEntries), strMenubarEntries, strContextPath, Initialisator.getUpgradeMenuTitle(), Initialisator.getUpgradeMenuTypeUpgrade(), intX, intY, false);
        }
        return docUpgradeMenu;
    }

    /**
	 * 
	 * @param bdlBuildinglist
	 * @param strContextPath
	 * @return Document
	 */
    private Document workerMenu(Buildinglist bdlBuildinglist, String strContextPath, int intCityID) {
        Document docExtendedMenu;
        String[][][] strContent;
        String[][] strMenuLinks;
        strMenuLinks = new String[3][2];
        strMenuLinks[0][0] = "Statistics";
        strMenuLinks[0][1] = "stats";
        strMenuLinks[1][0] = "Upgrade";
        strMenuLinks[1][1] = "upgrade";
        strMenuLinks[2][0] = "Worker";
        strMenuLinks[2][1] = "worker";
        strContent = workerMenuContent(bdlBuildinglist);
        docExtendedMenu = mwkWorker.menu(mwkWorker.createTable(strContent), strMenuLinks, strContextPath, "worker menu", "workermenu", 0, 0, true);
        return docExtendedMenu;
    }

    private String[][][] workerMenuContent(Buildinglist bdlBuildinglist) {
        byte bytNumberOfResources;
        int c, i, j;
        int intColumns;
        int intFreeWorkers;
        int intFreeWorkingPlace;
        int[] intQuantities = { 1, 5, 10, 20, 0 };
        int[][] intWorker;
        String[][][] strContent;
        String strLabel;
        String strResourceAmount;
        String strResourceName;
        intColumns = 12;
        strLabel = "workerMenuLabel";
        strResourceAmount = "workerMenuCellAmount";
        strResourceName = "workerMenuCellName";
        intFreeWorkers = bdlBuildinglist.getFreeWorker();
        System.out.println("Free workers: " + intFreeWorkers);
        intWorker = bdlBuildinglist.getWorker();
        for (int a = 0; a < intWorker.length; a = a + 1) {
            for (int b = 0; b < intWorker[a].length; b = b + 1) {
                System.out.println(intWorker[a][b]);
            }
        }
        bytNumberOfResources = (byte) intWorker.length;
        strContent = new String[bytNumberOfResources + 1][intColumns][5];
        strContent[0][0][0] = "Resource";
        strContent[0][0][1] = "";
        strContent[0][0][2] = strLabel;
        strContent[0][0][3] = "";
        strContent[0][0][4] = "";
        strContent[0][1][0] = "Actual Worker";
        strContent[0][1][1] = "";
        strContent[0][1][2] = strLabel;
        strContent[0][1][3] = "";
        strContent[0][1][4] = "";
        strContent[0][2][0] = "Add Worker";
        strContent[0][2][1] = "";
        strContent[0][2][2] = strLabel;
        strContent[0][2][3] = new Integer(intQuantities.length).toString();
        strContent[0][2][4] = "";
        strContent[0][3][0] = "Remove Worker";
        strContent[0][3][1] = "";
        strContent[0][3][2] = strLabel;
        strContent[0][3][3] = new Integer(intQuantities.length).toString();
        strContent[0][3][4] = "";
        for (i = 0; i < bytNumberOfResources; i = i + 1) {
            Resource resResource;
            resResource = Initialisator.getResourcesByID().get(new Byte(new Integer(intWorker[i][0]).byteValue()));
            if (resResource.getResourceType() < 0) {
                continue;
            }
            intFreeWorkingPlace = bdlBuildinglist.getWorker(resResource.getResourceId())[2] - bdlBuildinglist.getWorker(resResource.getResourceId())[1];
            strContent[i + 1][0][0] = resResource.getName();
            strContent[i + 1][0][1] = "";
            strContent[i + 1][0][2] = strResourceName;
            strContent[i + 1][0][3] = "";
            strContent[i + 1][0][4] = "";
            strContent[i + 1][1][0] = new Integer(intWorker[i][1]).toString();
            strContent[i + 1][1][1] = "";
            strContent[i + 1][1][2] = strResourceName;
            strContent[i + 1][1][3] = "";
            strContent[i + 1][1][4] = resResource.getName() + "Worker";
            for (c = 0; c < 2; c = c + 1) {
                String strCalc;
                if (c == 0) {
                    if (intFreeWorkers > intWorker[i][2]) {
                        intQuantities[intQuantities.length - 1] = intWorker[i][2];
                    } else {
                        intQuantities[intQuantities.length - 1] = intFreeWorkers;
                    }
                    strCalc = "plus";
                } else {
                    intQuantities[intQuantities.length - 1] = intWorker[i][1];
                    strCalc = "minus";
                }
                for (j = 0; j < intQuantities.length; j = j + 1) {
                    int intQuantity;
                    intQuantity = intQuantities[j % intQuantities.length];
                    strContent[i + 1][(c * intQuantities.length) + j + 2][0] = new Integer(intQuantity).toString();
                    if (c == 0) {
                        if ((intFreeWorkers >= intQuantity) && (intFreeWorkingPlace >= intQuantity) && (intQuantity > 0)) {
                            strContent[i + 1][(c * intQuantities.length) + j + 2][1] = NUMBER_OF_WORKERS + "," + new Integer(intWorker[i][0]) + "," + new Integer(intQuantity).toString() + "," + strCalc;
                        } else {
                            strContent[i + 1][(c * intQuantities.length) + j + 2][1] = "";
                        }
                        strContent[i + 1][(c * intQuantities.length) + j + 2][2] = strResourceAmount + strCalc;
                    } else {
                        if ((intWorker[i][1] >= intQuantity) && (intQuantity > 0)) {
                            strContent[i + 1][(c * intQuantities.length) + j + 2][1] = NUMBER_OF_WORKERS + "," + new Integer(intWorker[i][0]) + "," + new Integer(intQuantity).toString() + "," + strCalc;
                        } else {
                            strContent[i + 1][(c * intQuantities.length) + j + 2][1] = "";
                        }
                        strContent[i + 1][(c * intQuantities.length) + j + 2][2] = strResourceAmount + strCalc;
                    }
                    strContent[i + 1][(c * intQuantities.length) + j + 2][3] = "";
                    strContent[i + 1][(c * intQuantities.length) + j + 2][4] = "";
                }
            }
        }
        return strContent;
    }
}
