package KwiKoL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.util.Translate;

/**
 * Central class of the library
 */
public class KoLClient {

    public static String GHOSTLY_SHELL = "2007";

    public static String REPTILIAN_FORTITUDE = "2008";

    public static String EMPATHY_OF_THE_NEWT = "2009";

    public static String TENACITY_OF_THE_SNAPPER = "2010";

    public static String ASTRAL_SHELL = "3012";

    public static String ELEMENTAL_SAUCOSPHERE = "4007";

    public static String JALAPENO_SAUCOSPHERE = "4008";

    public static String JABANERO_SAUCOSPHERE = "4011";

    public static String HOOJIWATS_HYMN_OF_HEALTHINESS = "6003";

    public static String THE_MOXIOUS_MADRIGAL = "6004";

    public static String CLETUSS_CANTICLE_OF_CELERITY = "6005";

    public static String THE_POLKA_OF_PLENTY = "6006";

    public static String THE_MAGICAL_MOJOMUSCULAR_MELODY = "6007";

    public static String THE_POWER_BALLAD_OF_THE_ARROWSMITH = "6008";

    public static String BRAWNEES_ANTHEM_OF_ABSORPTION = "6009";

    public static String FAT_LEONS_PHAT_LOOT_LYRIC = "6010";

    public static String THE_PSALM_OF_POINTINESS = "6011";

    public static String JACKASSES_SYMPHONY_OF_DESTRUCTION = "6012";

    public static String STEVEDAVES_SHANTY_OF_SUPERIORITY = "6013";

    public static String THE_ODE_TO_BOOZE = "6014";

    private HttpClient client;

    private String[] availableHosts;

    private String username;

    private String password;

    private boolean compactMode;

    private boolean hasMallAccess;

    private boolean hasStore;

    private boolean loggedIn;

    private int currentHost;

    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }

    public KoLClient() {
        this.availableHosts = new String[2];
        this.availableHosts[0] = new String("http://www2.kingdomofloathing.com/");
        this.availableHosts[1] = new String("http://www3.kingdomofloathing.com/");
        this.currentHost = 0;
        this.client = new HttpClient();
        this.loggedIn = false;
    }

    private String executeMethod(HttpMethod method) throws KoLBadConnectionException, KoLSiteDownException, KoLInvalidSessionException {
        String currentServer;
        String location;
        String response;
        currentServer = this.getCurrentServer();
        try {
            if (this.client.executeMethod(method) == -1) {
                throw new KoLBadConnectionException(currentServer);
            }
        } catch (IOException e) {
            throw new KoLBadConnectionException(currentServer);
        }
        if (method.getResponseHeader("location") != null) {
            location = method.getResponseHeader("location").getValue();
            if (location.equals("maint.php") == true) {
                throw new KoLSiteDownException();
            }
        }
        response = method.getResponseBodyAsString();
        method.releaseConnection();
        if (response.indexOf("Invalid Session ID") != -1) {
            throw new KoLInvalidSessionException();
        }
        return response;
    }

    /**
	 * Gets the number of adventures available to the character currently 
	 * logged into the site
	 * 
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLInternalErrorException		If the number of adventures 
	 *											cannot be parsed successfully
	 */
    public int getAdventures() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String patternString;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "charpane.php");
        get.addRequestHeader(new Header("Referer", currentServer + "main.html"));
        response = this.executeMethod(get);
        if (this.compactMode == true) {
            patternString = "Adv</a>:</td><td align=left><b>([0-9]+)";
        } else {
            patternString = "hourglass\\.gif\" width=30 height=30><br><b><font size=2>([0-9]+)";
        }
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("getAdventures");
        }
        return new Integer(matcher.group(1)).intValue();
    }

    /**
	 * Purchases an item in a store in the mall
	 *
	 * @param	storeID							ID of the store within the 
	 *											game
	 * @param	itemID							ID of the item within the 
	 *											game
	 * @param	quantity						Quantity of the item to 
	 *											purchase
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLMallAccessDeniedException	If the current character does 
	 *											not have access to the mall
	 * @throws	KoLItemNotPresentException		If the item is not present in 
	 *											the inventory of the 
	 *											specified store
	 * @throws	KoLItemLimitExceededException	If purchasing the specified 
	 *											quantity of the item would 
	 *											exceed that item's purchase
	 *											quantity limit
	 * @throws	KoLNotEnoughMeatException		If current character does not 
	 *											have enough meat to make the 
	 *											specified purchase
	 * @throws	KoLInternalErrorException		If the transaction ID cannot 
	 *											be parsed successfully
	 */
    public void buyItem(String storeID, String itemID, int quantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLMallAccessDeniedException, KoLItemNotPresentException, KoLItemLimitExceededException, KoLNotEnoughMeatException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String patternString;
        String pwd;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasMallAccess == false) {
            throw new KoLMallAccessDeniedException(this.username);
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "mallstore.php?whichstore=" + storeID);
        get.addRequestHeader(new Header("Referer", currentServer + "searchmall.php"));
        response = this.executeMethod(get);
        if (response.indexOf("<input name=whichitem type=radio value=" + itemID + ">") == -1) {
            throw new KoLItemNotPresentException(itemID, "Store " + storeID);
        }
        patternString = "<input type=\"hidden\" name=\"pwd\" value=\"([^\"]+)\">";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("buyItem");
        }
        pwd = matcher.group(1);
        post = new PostMethod(currentServer + "mallstore.php?whichstore=" + storeID);
        post.addRequestHeader(new Header("Referer", currentServer + "mallstore.php?whichstore=" + storeID));
        post.addParameter("pwd", pwd);
        post.addParameter("buying", "Yep.");
        post.addParameter("whichstore", storeID);
        post.addParameter("whichitem", itemID);
        post.addParameter("quantity", String.valueOf(quantity));
        response = this.executeMethod(post);
        patternString = "You may only buy ([0-9]+) of this item per day from this store.";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == true) {
            throw new KoLItemLimitExceededException(storeID, itemID, quantity);
        }
        if (response.indexOf("You can't afford that item.") != -1) {
            throw new KoLNotEnoughMeatException(storeID, itemID, quantity);
        }
    }

    /**
	 * Makes toast using a cheap toaster from The Shore if it has been 
	 * acquired
	 * 
	 * @param	quantity						Number of pieces of toast to 
	 * 											make (max 3) 
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLCantUseSkillException		If the character currently 
	 * 											logged into the site does not 
	 * 											have the skill needed to 
	 * 											make toast
	 */
    public void makeToast(int quantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLCantUseSkillException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "campground.php");
        get.addRequestHeader(new Header("Referer", currentServer + "compactmenu.php"));
        response = this.executeMethod(get);
        for (int x = 0; x < quantity; x++) {
            if (response.indexOf("Make Toast") == -1) {
                throw new KoLCantUseSkillException("Make Toast");
            }
            post = new PostMethod(currentServer + "campground.php");
            post.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
            post.addParameter("maketoast", "Yep.");
            response = this.executeMethod(post);
        }
    }

    /**
	 * Conjures dry noodles if the needed skill is available
	 *
	 * @param	quantity							Number of dry noodles to 
	 * 												make (max 3)
	 *
	 * @throws	KoLBadConnectionException			If a connection to the 
	 *												server cannot be 
	 *												established
	 * @throws	KoLSiteDownException				If the site is down for 
	 *												nightly maintenance
	 * @throws	KoLNotLoggedInException				If the logIn() method was 
	 *												not called prior to this 
	 *												method being called
	 * @throws	KoLInvalidSessionException			If the current session has 
	 *												been terminated by an 
	 *												outside source
	 * @throws	KoLInternalErrorException			If the number of adventures 
	 *												cannot be parsed 
	 *												successfully to confirm 
	 *												that the character 
	 *												currently logged into the 
	 *												site has enough adventures
	 * 												to conjure the specified 
	 * 												quantity of noodles 
	 * @throws	KoLNotEnoughAdventuresException		If the character currently 
	 * 												logged into the site does 
	 * 												not have enough adventures 
	 * 												to conjure the specified 
	 * 												quantity of noodles 
	 * @throws	KoLCantUseSkillException			If the character currently 
	 * 												logged into the site does 
	 * 												not have the skill needed 
	 * 												to conjure noodles
	 */
    public void conjureNoodles(int quantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLInternalErrorException, KoLNotEnoughAdventuresException, KoLCantUseSkillException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        if (quantity > this.getAdventures()) {
            throw new KoLNotEnoughAdventuresException();
        }
        get = new GetMethod(currentServer + "campground.php");
        get.addRequestHeader(new Header("Referer", currentServer + "compactmenu.php"));
        response = this.executeMethod(get);
        for (int x = 0; x < quantity; x++) {
            if (response.indexOf("Conjure some Noodles(1)") == -1) {
                throw new KoLCantUseSkillException("Conjure Noodles");
            }
            post = new PostMethod(currentServer + "campground.php");
            post.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
            post.addParameter("action", "skill2");
            response = this.executeMethod(post);
        }
    }

    /**
	 * Order evil food using the Evil Golden Arches if they have been acquired
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLCantUseSkillException		If the character currently 
	 * 											logged into the site does not 
	 * 											currently have the ability to 
	 *											order evil food
	 */
    public void orderEvilFood() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLCantUseSkillException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "campground.php");
        get.addRequestHeader(new Header("Referer", currentServer + "compactmenu.php"));
        response = this.executeMethod(get);
        if (response.indexOf("Order some Evil Food") == -1) {
            throw new KoLCantUseSkillException("Order Evil Food");
        }
        post = new PostMethod(currentServer + "campground.php");
        post.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
        post.addParameter("order", "Yep.");
        this.executeMethod(post);
    }

    /**
	 * Makes a Scrumptious Reagent if the needed skill is available
	 *
	 * @param	quantity							Number of Scrumptious 
	 *												Reagents to make (max 3)
	 *
	 * @throws	KoLBadConnectionException			If a connection to the 
	 *												server cannot be 
	 *												established
	 * @throws	KoLSiteDownException				If the site is down for 
	 *												nightly maintenance
	 * @throws	KoLNotLoggedInException				If the logIn() method was 
	 *												not called prior to this 
	 *												method being called
	 * @throws	KoLInvalidSessionException			If the current session has 
	 *												been terminated by an 
	 *												outside source
	 * @throws	KoLNotEnoughAdventuresException		If the character currently 
	 * 												logged into the site does 
	 * 												not have enough adventures 
	 * 												to conjure the specified 
	 * 												quantity of Scrumptious 
	 *												Reagents 
	 * @throws	KoLInternalErrorException			If the amount of Meat in 
	 *												the inventory of the 
	 *												character currently logged 
	 *												in to the site cannot be 
	 *												parsed successfully
	 * @throws	KoLNotEnoughMeatException			If the character currently 
	 *												logged into the site does 
	 *												not have enough meat to 
	 *												make the specified number 
	 *												of Scrumptious Reagents
	 * @throws	KoLCantUseSkillException			If the character currently 
	 * 												logged into the site does 
	 * 												not have the skill needed 
	 * 												to make Scrumptious 
	 *												Reagents
	 */
    public void makeScrumptiousReagent(int quantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLCantUseSkillException, KoLNotEnoughAdventuresException, KoLInternalErrorException, KoLNotEnoughMeatException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (quantity > this.getAdventures()) {
            throw new KoLNotEnoughAdventuresException();
        }
        if ((quantity * 100) > this.getCharacterMeat()) {
            throw new KoLNotEnoughMeatException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "campground.php");
        get.addRequestHeader(new Header("Referer", currentServer + "compactmenu.php"));
        response = this.executeMethod(get);
        for (int x = 0; x < quantity; x++) {
            if (response.indexOf("Make a Scrumptious Reagent (100 Meat)") == -1) {
                throw new KoLCantUseSkillException("Make Scrumptious Reagent");
            }
            post = new PostMethod(currentServer + "campground.php");
            post.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
            post.addParameter("action", "skill2");
            response = this.executeMethod(post);
        }
    }

    /**
	 * Returns a list of items in the inventory of the character currently 
	 * logged in to the site
	 * 
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is down for nightly 
	 *										maintenance
	 * @throws	KoLNotLoggedInException		If the logIn() method was not 
	 *										called prior to this method being 
	 *										called
	 * @throws	KoLInvalidSessionException	If the current session has been 
	 *										terminated by an outside source
	 *
	 * @return								List of inventory items
	 */
    public KoLItem[] getCharacterInventory() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException {
        ArrayList matches;
        GetMethod get;
        KoLItem[] inventory;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String itemID;
        String itemName;
        String patternString;
        String response;
        int itemPrice;
        int itemQuantity;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "sellstuff.php");
        get.addRequestHeader(new Header("Referer", currentServer + "inventory.php"));
        response = this.executeMethod(get);
        patternString = "<option value=([0-9]+)>([^\\(>]+)(\\(([0-9]+)\\) )?\\(([0-9]+)( Meat)?( each)?\\)";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        matches = new ArrayList();
        while (matcher.find() == true) {
            itemID = matcher.group(1);
            itemName = Translate.decode(new String(matcher.group(2)).trim());
            itemPrice = new Integer(matcher.group(5)).intValue();
            try {
                itemQuantity = new Integer(matcher.group(4)).intValue();
            } catch (NumberFormatException e) {
                itemQuantity = 1;
            }
            matches.add(new KoLItem(itemID, itemName, itemPrice, itemQuantity));
        }
        inventory = new KoLItem[matches.size()];
        for (int x = 0; x < matches.size(); x++) {
            inventory[x] = (KoLItem) matches.get(x);
        }
        return inventory;
    }

    /**
	 * Returns the amount of Meat currently held by the character currently 
	 * logged into the site (not including Meat in the character's closet)
	 *
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is down for nightly 
	 *										maintenance
	 * @throws	KoLNotLoggedInException		If the logIn() method was not 
	 *										called prior to this method being 
	 *										called
	 * @throws	KoLInvalidSessionException	If the current session has been 
	 *										terminated by an outside source
	 * @throws	KoLInternalErrorException	If the amount of Meat cannot be 
	 *										parsed successfully
	 *
	 * @return								Amount of Meat
	 */
    public int getCharacterMeat() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String patternString;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "charpane.php");
        get.addRequestHeader(new Header("Referer", currentServer + "main_c.html"));
        response = this.executeMethod(get);
        if (this.compactMode == true) {
            patternString = "Meat:</td><td align=left><b>([0-9]+)";
        } else {
            patternString = "meat\\.gif\" width=30 height=30><br><b><font size=2>([0-9]+)";
        }
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == true) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new KoLInternalErrorException("getCharacterMeat");
        }
    }

    /**
	 * Returns a list of items in the closet of the character currently 
	 * logged in to the site (discluding autosell prices for the moment 
	 * because of a limitation in the site interface)
	 * 
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is down for nightly 
	 *										maintenance
	 * @throws	KoLNotLoggedInException		If the logIn() method was not 
	 *										called prior to this method being 
	 *										called
	 * @throws	KoLInvalidSessionException	If the current session has been 
	 *										terminated by an outside source
	 *
	 * @return								List of items in the closet
	 */
    public KoLItem[] getClosetInventory() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException {
        ArrayList matches;
        GetMethod get;
        KoLItem[] inventory;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String itemID;
        String itemName;
        String patternString;
        String response;
        int itemQuantity;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "closet.php");
        get.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
        response = this.executeMethod(get);
        patternString = "<td valign=center><b>([^<]+)</b> (\\(([0-9]+)\\))?<font size=1><br><a href=\"closet\\.php\\?action=take&howmany=1&whichitem=([0-9]+)";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        matches = new ArrayList();
        while (matcher.find() == true) {
            itemID = matcher.group(4);
            itemName = matcher.group(1);
            try {
                itemQuantity = new Integer(matcher.group(3)).intValue();
            } catch (NumberFormatException e) {
                itemQuantity = 1;
            }
            matches.add(new KoLItem(itemID, itemName, 0, itemQuantity));
        }
        inventory = new KoLItem[matches.size()];
        for (int x = 0; x < matches.size(); x++) {
            inventory[x] = (KoLItem) matches.get(x);
        }
        return inventory;
    }

    /**
	 * Returns the amount of Meat currently contained in the closet of the 
	 * character currently logged onto the site
	 *
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is down for nightly 
	 *										maintenance
	 * @throws	KoLNotLoggedInException		If the logIn() method was not 
	 *										called prior to this method being 
	 *										called
	 * @throws	KoLInvalidSessionException	If the current session has been 
	 *										terminated by an outside source
	 * @throws	KoLInternalErrorException	If the amount of Meat cannot be 
	 *										parsed successfully
	 *
	 * @return								Amount of Meat
	 */
    public int getClosetMeat() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String patternString;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "closet.php");
        get.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
        response = this.executeMethod(get);
        patternString = "Your closet contains ([0-9]+) Meat";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("getClosetMeat");
        }
        return Integer.parseInt(matcher.group(1));
    }

    /**
	 * Returns a list of items for sale in the store of the character 
	 * currently logged in to the site
	 * 
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is down for nightly 
	 *										maintenance
	 * @throws	KoLNotLoggedInException		If the logIn() method was not 
	 *										called prior to this method being 
	 *										called
	 * @throws	KoLInvalidSessionException	If the current session has been 
	 *										terminated by an outside source
	 * @throws	KoLNoStoreException			If the current character does not 
	 *										have a store in the mall
	 *
	 * @return								List of store items
	 */
    public KoLItem[] getStoreInventory() throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLNoStoreException {
        ArrayList matches;
        GetMethod get;
        KoLItem[] inventory;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String itemID;
        String itemName;
        String patternString;
        String response;
        int itemPrice;
        int itemQuantity;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasStore == false) {
            throw new KoLNoStoreException(this.username);
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "managestore.php");
        get.addRequestHeader(new Header("Referer", currentServer + "mall.php"));
        response = this.executeMethod(get);
        patternString = "<td>([^\\(<]+)\\(([0-9]+)\\)</td><td>([0-9,]+)</td><td><font size=1>(\\(unlimited\\)|[0-9]+)</font>&nbsp;&nbsp;</td><td><a href=\"managestore.php\\?action=take&whichitem=([0-9]+)";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        matches = new ArrayList();
        while (matcher.find() == true) {
            itemID = matcher.group(5);
            itemName = Translate.decode(new String(matcher.group(1)).trim());
            itemPrice = new Integer(matcher.group(3).replaceAll(",", "")).intValue();
            itemQuantity = new Integer(matcher.group(2)).intValue();
            matches.add(new KoLItem(itemID, itemName, itemPrice, itemQuantity));
        }
        inventory = new KoLItem[matches.size()];
        for (int x = 0; x < matches.size(); x++) {
            inventory[x] = (KoLItem) matches.get(x);
        }
        return inventory;
    }

    /**
	 * Returns a list of items for sale in a specified store
	 *
	 * @param	storeID							ID of the store
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the username and password 
	 *											were recognized to be invalid 
	 *											by the site
	 * @throws	KoLInvalidSessionException		If the previous session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLMallAccessDeniedException	If the current character does 
	 *											not have access to the mall
	 */
    public KoLItem[] getStoreInventory(String storeID) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLMallAccessDeniedException {
        ArrayList matches;
        GetMethod get;
        KoLItem[] inventory;
        Matcher matcher;
        Pattern pattern;
        String currentServer;
        String itemID;
        String itemName;
        String patternString;
        String response;
        int itemPrice;
        int itemQuantity;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasMallAccess == false) {
            throw new KoLMallAccessDeniedException(this.username);
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "mallstore.php?whichstore=" + storeID);
        get.addRequestHeader(new Header("Referer", currentServer + "searchmall.php"));
        response = this.executeMethod(get);
        patternString = "<tr><td><input name=whichitem type=radio value=([^>]+)></td><td>(<img[^<]+)?</td><td valign=center><b>([^<]+)</b> \\(([0-9]+)\\) ( \\(Limit [0-9]+ / day\\))?</td><td>([0-9,]+) Meat</td></tr>";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        matches = new ArrayList();
        while (matcher.find() == true) {
            itemID = matcher.group(1);
            itemName = Translate.decode(new String(matcher.group(3)));
            itemPrice = new Integer(matcher.group(6).replaceAll(",", "")).intValue();
            itemQuantity = new Integer(matcher.group(4)).intValue();
            matches.add(new KoLItem(itemID, itemName, itemPrice, itemQuantity));
        }
        inventory = new KoLItem[matches.size()];
        for (int x = 0; x < matches.size(); x++) {
            inventory[x] = (KoLItem) matches.get(x);
        }
        return inventory;
    }

    /**
	 * Returns whether or not a character is currently logged into the site
	 *
	 * @return								Whether or not a character is 
	 *										currently logged into the site
	 */
    public boolean loggedIn() {
        return this.loggedIn;
    }

    /**
	 * Logs a character into the site
	 *
	 * @param	username					Username for the character
	 * @param	password					Password for the character
	 *
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is currently down for 
	 *										nightly maintenance
	 * @throws	KoLBadLoginException		If the username and password were 
	 *										recognized to be invalid by the 
	 *										site
	 * @throws	KoLInvalidSessionException	If the previous session has been 
	 *										terminated by an outside source
	 */
    public void logIn(String username, String password) throws KoLBadConnectionException, KoLSiteDownException, KoLBadLoginException, KoLInvalidSessionException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String location;
        String response;
        currentServer = this.getCurrentServer();
        post = new PostMethod(currentServer + "login.php");
        post.addParameter("loggingin", "Yup.");
        post.addParameter("loginname", username);
        post.addParameter("password", password);
        response = this.executeMethod(post);
        if (response.indexOf("Login failed.  Bad password") != -1) {
            throw new KoLBadLoginException(username, password);
        }
        location = post.getResponseHeader("location").getValue();
        this.compactMode = location.equals("main_c.html");
        get = new GetMethod(currentServer + "mall.php");
        response = this.executeMethod(get);
        this.hasMallAccess = (response.indexOf("you gotta be at least level 5") == -1);
        this.hasStore = (response.indexOf("Manage your store") != -1);
        this.loggedIn = true;
        this.username = username;
        this.password = password;
    }

    /**
	 * Logs the character currently logged in out of the site
	 *
	 * @throws	KoLBadConnectionException	If a connection to the server 
	 *										cannot be established
	 * @throws	KoLSiteDownException		If the site is down for nightly 
	 *										maintenance
	 */
    public void logOut() throws KoLBadConnectionException, KoLSiteDownException {
        GetMethod get;
        String currentServer;
        if (this.loggedIn == true) {
            currentServer = this.getCurrentServer();
            try {
                get = new GetMethod(currentServer + "logout.php");
                get.addRequestHeader(new Header("Referer", currentServer + "compactmenu.php"));
                this.executeMethod(get);
            } catch (KoLInvalidSessionException e) {
            }
            this.loggedIn = false;
        }
    }

    /**
	 * Moves an item in the closet of the character currently logged into the 
	 * site to the character inventory
	 *
	 * @param	itemID							ID of the item
	 * @param	moveAll							TRUE to move all items, FALSE 
	 *											to move only one
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLItemNotPresentException		If the item is not present in 
	 *											the closet of the character 
	 *											currently logged into the 
	 *											site
	 */
    public void moveClosetItemToInventory(String itemID, boolean moveAll) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLItemNotPresentException {
        GetMethod get;
        String currentServer;
        String location;
        String patternString;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "closet.php");
        response = this.executeMethod(get);
        patternString = "closet.php?action=take&howmany=1&whichitem=" + itemID;
        if (response.indexOf(patternString) == -1) {
            throw new KoLItemNotPresentException(itemID, "Character Closet");
        }
        location = currentServer + "closet.php?action=take&howmany=";
        if (moveAll == true) {
            location += "all";
        } else {
            location += "1";
        }
        location += "&whichitem=" + itemID;
        get = new GetMethod(location);
        this.executeMethod(get);
    }

    /**
	 * Moves an item in the character inventory of the character currently 
	 * logged into the site to the closet
	 *
	 * @param	itemID							ID of the item
	 * @param	moveAll							TRUE to move all items, FALSE 
	 *											to move only one
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLItemNotPresentException		If the item is not present in 
	 *											the character inventory of 
	 *											the character currently 
	 *											logged into the site
	 */
    public void moveInventoryItemToCloset(String itemID, boolean moveAll) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLItemNotPresentException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String location;
        String patternString;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "closet.php");
        response = this.executeMethod(get);
        patternString = "<option value=" + itemID + ">";
        if (response.indexOf(patternString) == -1) {
            throw new KoLItemNotPresentException(itemID, "Character Inventory");
        }
        post = new PostMethod(currentServer + "closet.php");
        post.addRequestHeader(new Header("Referer", currentServer + "closet.php"));
        post.addParameter("action", "put");
        post.addParameter("whichitem", itemID);
        if (moveAll == true) {
            post.addParameter("howmany", "all");
        } else {
            post.addParameter("howmany", "one");
        }
        this.executeMethod(post);
    }

    /**
	 * Moves an item in the character inventory of the character currently 
	 * logged into the site to the store inventory
	 *
	 * @param	itemID								ID of the item within the 
	 *												game
	 * @param	itemPrice							Price for which to sell 
	 *												the item
	 * @param	itemQuantity						Quantity of the item to 
	 *												sell
	 * @param	limitPerDay							Purchase quantity limit 
	 *												for the item per player 
	 *												per day or 0 for no limit
	 *
	 * @throws	KoLBadConnectionException			If a connection to the 
	 *												server cannot be 
	 *												established
	 * @throws	KoLSiteDownException				If the site is down for 
	 *												nightly maintenance
	 * @throws	KoLNotLoggedInException				If the logIn() method was 
	 *												not called prior to this 
	 *												method being called
	 * @throws	KoLInvalidSessionException			If the current session 
	 *												has been terminated by an 
	 *												outside source
	 * @throws	KoLNoStoreException					If the current character 
	 *												does not have a store in 
	 *												the mall
	 * @throws	KoLInvalidItemPriceException		If the specified item 
	 *												price is not at least 10
	 *												and less than 10^9
	 * @throws	KoLItemNotPresentException			If the item is not 
	 *												present in the character  
	 *												inventory of the 
	 *												character currently 
	 *												logged into the site
	 * @throws	KoLInternalErrorException			If the transaction ID 
	 *												cannot be parsed 
	 *												successfully
	 * @throws	KoLItemQuantityExceededException	If the quantity of the 
	 *												specified item available 
	 *												is less than the quantity 
	 *												specified for transfer
	 */
    public void moveInventoryItemToStore(String itemID, int itemPrice, int limitPerDay, int itemQuantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLNoStoreException, KoLInvalidItemPriceException, KoLItemNotPresentException, KoLInternalErrorException, KoLItemQuantityExceededException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String patternString;
        String pwd;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasStore == false) {
            throw new KoLNoStoreException(this.username);
        }
        if (itemPrice < 10 || itemPrice >= 1000000000) {
            throw new KoLInvalidItemPriceException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "managestore.php");
        get.addRequestHeader(new Header("Referer", currentServer + "mall.php"));
        response = this.executeMethod(get);
        if (response.indexOf("<option value=" + itemID + ">") == -1) {
            throw new KoLItemNotPresentException(itemID, "Character Inventory");
        }
        patternString = "<input type=hidden name=pwd value='([^']+)'>";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("moveInventoryItemToStore");
        }
        pwd = matcher.group(1);
        post = new PostMethod(currentServer + "managestore.php");
        post.addRequestHeader(new Header("Referer", currentServer + "managestore.php"));
        post.addParameter("whichitem", itemID);
        post.addParameter("sellprice", String.valueOf(itemPrice));
        post.addParameter("limit", String.valueOf(limitPerDay));
        post.addParameter("addtype", "addquantity");
        post.addParameter("quantity", String.valueOf(itemQuantity));
        post.addParameter("action", "additem");
        post.addParameter("pwd", pwd);
        response = this.executeMethod(post);
        if (response.indexOf("You don't have that many of that item.") != -1) {
            throw new KoLItemQuantityExceededException(itemID, itemQuantity, "Character Inventory");
        }
    }

    /**
	 * Transfers Meat from the character's inventory to the character's 
	 * closet
	 *
	 * @param	quantity						Amount of Meat to be 
	 *											transferred
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLInternalErrorException		If the transaction ID cannot 
	 *											be parsed successfully
	 * @throws	KoLNotEnoughMeatException		If the amount of Meat to 
	 *											transfer exceeds the amount 
	 *											available in the characters' 
	 *											inventory
	 */
    public void moveMeatToCloset(int quantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLNotEnoughMeatException, KoLInternalErrorException, KoLNotEnoughMeatException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String patternString;
        String pwd;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "closet.php");
        get.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
        response = this.executeMethod(get);
        patternString = "<input type=hidden name=pwd value=\"([^\"]+)\">";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("moveMeatToCloset");
        }
        pwd = matcher.group(2);
        post = new PostMethod(currentServer + "closet.php");
        post.addRequestHeader(new Header("Referer", currentServer + "closet.php"));
        post.addParameter("pwd", pwd);
        post.addParameter("action", "addmeat");
        post.addParameter("addmeat", String.valueOf(quantity));
        response = this.executeMethod(post);
        if (response.indexOf("You don't have that much.") != -1) {
            throw new KoLNotEnoughMeatException(quantity);
        }
    }

    /**
	 * Transfers Meat from the character's closet to the character's 
	 * inventory
	 *
	 * @param	quantity						Amount of Meat to be 
	 *											transferred
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLInternalErrorException		If the transaction ID cannot 
	 *											be parsed successfully
	 * @throws	KoLNotEnoughMeatException		If the amount of Meat to 
	 *											transfer exceeds the amount 
	 *											available in the characters' 
	 *											closet
	 */
    public void moveMeatToInventory(int quantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLNotEnoughMeatException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String patternString;
        String pwd;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "closet.php");
        get.addRequestHeader(new Header("Referer", currentServer + "campground.php"));
        response = this.executeMethod(get);
        patternString = "<input type=hidden name=pwd value=\"([^\"]+)\">";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("moveMeatToInventory");
        }
        pwd = matcher.group(2);
        post = new PostMethod(currentServer + "closet.php");
        post.addRequestHeader(new Header("Referer", currentServer + "closet.php"));
        post.addParameter("pwd", pwd);
        post.addParameter("action", "takemeat");
        post.addParameter("takemeat", String.valueOf(quantity));
        response = this.executeMethod(post);
        if (response.indexOf("You don't have that much in the Closet.") != -1) {
            throw new KoLNotEnoughMeatException(quantity);
        }
    }

    /**
	 * Moves an item in the store inventory of the character currently logged 
	 * into the site to the character inventory
	 *
	 * @param	itemID							ID of the item within the 
	 *											game
	 * @param	moveAll							TRUE to move all items, FALSE 
	 *											to move only one
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLNoStoreException				If the current character does 
	 *											not have a store in the mall
	 * @throws	KoLItemNotPresentException		If the item is not present in 
	 *											the store inventory of the 
	 *											character currently logged 
	 *											into the site
	 */
    public void moveStoreItemToInventory(String itemID, boolean moveAll) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLNoStoreException, KoLItemNotPresentException {
        GetMethod get;
        String action;
        String currentServer;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasStore == false) {
            throw new KoLNoStoreException(this.username);
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "managestore.php");
        get.addRequestHeader(new Header("Referer", currentServer + "mall.php"));
        response = this.executeMethod(get);
        if (response.indexOf("&whichitem=" + itemID + "\"") == -1) {
            throw new KoLItemNotPresentException(itemID, "Character Store");
        }
        if (moveAll == true) {
            action = "takeall";
        } else {
            action = "take";
        }
        get = new GetMethod(currentServer + "managestore.php?action=" + action + "&whichitem=" + itemID);
        get.addRequestHeader(new Header("Referer", currentServer + "managestore.php"));
        this.executeMethod(get);
    }

    /**
	 * Performs a search in the mall for an item given its name
	 *
	 * @param	searchTerm						Term to search for
	 * @param	maxResults						Maximum number of results to 
	 *											return or 0 for no limit
	 * @param	strict							TRUE if the results should 
	 *											match the search term exactly, 
	 *											FALSE otherwise
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 *
	 * @return									Search results for the 
	 *											specified item
	 */
    public KoLMallSearchResult[] searchMall(String searchTerm, int maxResults, boolean strict) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLMallAccessDeniedException {
        ArrayList matches;
        KoLMallSearchResult[] results;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String itemName;
        String patternString;
        String response;
        String storeID;
        String storeName;
        String whichitem;
        boolean found;
        boolean limitReached;
        int index;
        int itemPrice;
        int itemQuantity;
        int limitPerDay;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasMallAccess == false) {
            throw new KoLMallAccessDeniedException(this.username);
        }
        currentServer = this.getCurrentServer();
        whichitem = new String(searchTerm);
        index = whichitem.indexOf("&");
        if (index != -1) {
            whichitem = whichitem.substring(0, index);
        }
        post = new PostMethod(currentServer + "searchmall.php");
        post.addRequestHeader(new Header("Referer", currentServer + "searchmall.php"));
        post.addParameter("whichitem", whichitem);
        post.addParameter("cheaponly", "on");
        post.addParameter("shownum", String.valueOf(maxResults));
        response = this.executeMethod(post);
        whichitem = Translate.decode(new String(searchTerm));
        patternString = "<tr><td( style='color: gray;')?><b>([^<]+)</b> \\(([0-9]+)\\)( \\(([0-9]+) / day\\))?</td><td><a  href=\"mallstore.php\\?whichstore=([0-9]+)\">([^<]+)</a></td><td>([0-9,]+)&nbsp;Meat</td></tR>";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        matches = new ArrayList();
        while (matcher.find() == true) {
            if (strict == true) {
                found = matcher.group(2).equalsIgnoreCase(whichitem);
            } else {
                found = (matcher.group(2).toLowerCase().indexOf(whichitem.toLowerCase()) != -1);
            }
            if (found == true) {
                itemName = matcher.group(2);
                itemPrice = new Integer(matcher.group(8).replaceAll(",", "")).intValue();
                itemQuantity = new Integer(matcher.group(3)).intValue();
                storeID = matcher.group(6);
                storeName = matcher.group(7);
                limitReached = (matcher.group(1) != null);
                try {
                    limitPerDay = new Integer(matcher.group(5)).intValue();
                } catch (NumberFormatException e) {
                    limitPerDay = 0;
                }
                matches.add(new KoLMallSearchResult(storeID, storeName, itemName, itemPrice, itemQuantity, limitPerDay, limitReached));
            }
        }
        results = new KoLMallSearchResult[matches.size()];
        for (int x = 0; x < matches.size(); x++) {
            results[x] = (KoLMallSearchResult) matches.get(x);
        }
        return results;
    }

    /**
	 * Sells a quantity of an item in the inventory of the character 
	 * currently logged into the site back to the system
	 *
	 * @param	itemID								ID of the item
	 * @param	itemQuantity						Quantity of the item to 
	 *												sell
	 *
	 * @throws	KoLBadConnectionException			If a connection to the 
	 *												server cannot be 
	 *												established
	 * @throws	KoLSiteDownException				If the site is down for 
	 *												nightly maintenance
	 * @throws	KoLNotLoggedInException				If the logIn() method was 
	 *												not called prior to this 
	 *												method being called
	 * @throws	KoLInvalidSessionException			If the current session 
	 *												has been terminated by an 
	 *												outside source
	 * @throws	KoLItemNotPresentException			If the item is not 
	 *												present in the character 
	 *												inventory of the 
	 *												character currently 
	 *												logged into the site
	 * @throws	KoLItemQuantityExceededException	If the quantity of the 
	 *												specified item available 
	 *												is less than the quantity 
	 *												specified for transfer
	 */
    public void sellItem(String itemID, int itemQuantity) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLItemNotPresentException, KoLItemQuantityExceededException {
        GetMethod get;
        PostMethod post;
        String currentServer;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "sellstuff.php");
        get.addRequestHeader(new Header("Referer", currentServer + "inventory.php"));
        response = this.executeMethod(get);
        if (response.indexOf("<option value=" + itemID + ">") == -1) {
            throw new KoLItemNotPresentException(itemID, "Character Inventory");
        }
        post = new PostMethod(currentServer + "sellstuff.php");
        post.addRequestHeader(new Header("Referer", currentServer + "sellstuff.php"));
        post.addParameter("action", "sell");
        post.addParameter("whichitem", itemID);
        post.addParameter("type", "quant");
        post.addParameter("howmany", String.valueOf(itemQuantity));
        response = this.executeMethod(post);
        if (response.indexOf("You don't have that many of that item.") != -1) {
            throw new KoLItemQuantityExceededException(itemID, itemQuantity, "Character Inventory");
        }
    }

    /**
	 * Updates the price and purchase quantity limit for an item in the store 
	 * of the character currently logged into the site
	 *
	 * @param	itemID							ID of the item
	 * @param	itemPrice						New price for the item
	 * @param	limitPerDay						New limit for the item
	 *
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLNoStoreException				If the current character does 
	 *											not have a store in the mall
	 * @throws	KoLItemNotPresentException		If the item is not present in 
	 *											the store inventory of the 
	 *											character currently logged 
	 *											into the site
	 * @throws	KoLInternalErrorException		If the transaction ID cannot 
	 *											be parsed successfully
	 */
    public void updateStoreItem(String itemID, int itemPrice, int limitPerDay) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLNoStoreException, KoLItemNotPresentException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String patternString;
        String pwd;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        if (this.hasStore == false) {
            throw new KoLNoStoreException(this.username);
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "manageprices.php");
        get.addRequestHeader(new Header("Referer", currentServer + "managestore.php"));
        response = this.executeMethod(get);
        patternString = "<input type=hidden name=pwd value=\"([^\"]+)\">";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("updateStoreItem");
        }
        pwd = matcher.group(2);
        if (response.indexOf("name=price" + itemID + ">") == -1) {
            throw new KoLItemNotPresentException(itemID, "Character Store");
        }
        post = new PostMethod(currentServer + "manageprices.php");
        post.addRequestHeader(new Header("Referer", currentServer + "manageprices.php"));
        post.addParameter("action", "update");
        post.addParameter("pwd", pwd);
        post.addParameter("price" + itemID, String.valueOf(itemPrice));
        post.addParameter("limit" + itemID, String.valueOf(limitPerDay));
        this.executeMethod(post);
    }

    /**
	 * Casts a buff on a player
	 *
	 * @param	skillID			Class constant representing the buff skill to use
	 * @param	playerName		Name of the player to cast the buff on
	 * @param	numberOfTimes	Number of times to cast the buff on the player
	 * 
	 * @throws	KoLBadConnectionException		If a connection to the server 
	 *											cannot be established
	 * @throws	KoLSiteDownException			If the site is down for nightly 
	 *											maintenance
	 * @throws	KoLNotLoggedInException			If the logIn() method was not 
	 *											called prior to this method 
	 *											being called
	 * @throws	KoLInvalidSessionException		If the current session has 
	 *											been terminated by an outside 
	 *											source
	 * @throws	KoLCantUseSkillException		If the specified buff skill is 
	 *											not available for use
	 * @throws	KoLInvalidBuffTargetException	If the specified player does 
	 *											not exist
	 * @throws	KoLNotEnoughMPException			If the casting player does not 
	 *											have enough MP to cast the buff 
	 *											the specified number of times
	 * @throws	KoLInternalErrorException		If the transaction ID cannot 
	 *											be parsed successfully
	 */
    public void castBuff(String skillID, String playerName, int numberOfTimes) throws KoLBadConnectionException, KoLSiteDownException, KoLNotLoggedInException, KoLInvalidSessionException, KoLCantUseSkillException, KoLInvalidBuffTargetException, KoLNotEnoughMPException, KoLInternalErrorException {
        GetMethod get;
        Matcher matcher;
        Pattern pattern;
        PostMethod post;
        String currentServer;
        String patternString;
        String pwd;
        String response;
        if (this.loggedIn == false) {
            throw new KoLNotLoggedInException();
        }
        currentServer = this.getCurrentServer();
        get = new GetMethod(currentServer + "skills.php");
        get.addRequestHeader(new Header("Referer", currentServer + "compactmenu.php"));
        response = this.executeMethod(get);
        patternString = "<input type=hidden name=pwd value='([^']+)'>";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(response);
        if (matcher.find() == false) {
            throw new KoLInternalErrorException("castBuff");
        }
        pwd = matcher.group(1);
        post = new PostMethod(currentServer + "skills.php");
        post.addRequestHeader(new Header("Referer", currentServer + "skills.php"));
        post.addParameter("pwd", pwd);
        post.addParameter("action", "Skillz");
        post.addParameter("whichskill", skillID);
        post.addParameter("specificplayer", playerName);
        post.addParameter("bufftimes", String.valueOf(numberOfTimes));
        response = this.executeMethod(post);
        if (response.indexOf("You don't have that skill.") != -1) {
            throw new KoLCantUseSkillException("Buff " + skillID);
        }
        if (response.indexOf("Invalid target player selected.") != -1) {
            throw new KoLInvalidBuffTargetException(playerName);
        }
        if (response.indexOf("You don't have enough Muscularity Points") != -1) {
            throw new KoLNotEnoughMPException(skillID);
        }
    }

    /**
	 * Returns the address of the current server being accessed
	 *
	 * @return	Server address
	 */
    public String getCurrentServer() {
        return this.availableHosts[this.currentHost];
    }

    /**
	 * Switches to the next available server, meant for instances in which 
	 * the current server cannot be accessed
	 */
    public void switchServers() {
        this.currentHost = (this.currentHost + 1) % this.availableHosts.length;
    }
}
