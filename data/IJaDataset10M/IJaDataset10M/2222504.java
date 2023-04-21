package lv.odylab.evemanage.client;

import com.google.gwt.i18n.client.Messages;

public interface EveManageMessages extends Messages {

    @DefaultMessage("Sign in")
    String signIn();

    @DefaultMessage("Sign out")
    String signOut();

    @DefaultMessage("Help")
    String help();

    @Messages.DefaultMessage("По-русски")
    String russian();

    @Messages.DefaultMessage("English")
    String english();

    @Messages.DefaultMessage("User")
    String user();

    @Messages.DefaultMessage("Error")
    String error();

    @Messages.DefaultMessage("Loading...")
    String loading();

    @Messages.DefaultMessage("Suggesting...")
    String suggesting();

    @DefaultMessage("No results")
    String noResults();

    @DefaultMessage("You are not logged in")
    String notLoggedIn();

    @DefaultMessage("Dashboard")
    String dashboard();

    @DefaultMessage("Blueprints")
    String blueprints();

    @DefaultMessage("Price set")
    String priceSet();

    @DefaultMessage("Quick calculator")
    String quickCalculator();

    @DefaultMessage("Users")
    String users();

    @DefaultMessage("Preferences")
    String preferences();

    @DefaultMessage("About")
    String about();

    @DefaultMessage("Enter blueprint name")
    String enterBlueprintName();

    @DefaultMessage("Enter ME")
    String enterMe();

    @DefaultMessage("Enter PE")
    String enterPe();

    @DefaultMessage("Hint: there is no difference between BPO and BPC")
    String hintCanAddBothBpoAndBpc();

    @DefaultMessage("Waste")
    String waste();

    @DefaultMessage("API import")
    String apiImport();

    @DefaultMessage("You can import your blueprints from EVE API using <a target=\"blank\" href=\"http://www.eveonline.com/api/default.asp\">Full Access API Key</a>. Since you are not likely going to share it, you should use links below to retrieve data manually. Copy link, edit request attributes (apiKey, userID and characterID), execute and copy response into text box below")
    String descriptionYouCanImportXml();

    @DefaultMessage("Note: only BPOs from manufacturing jobs will be imported")
    String noteOnlyBPOs();

    @DefaultMessage("Note: since API does not provide enough details, each matched BPO will be looked up for additional details. It may take couple of minutes depending on the amount of blueprints")
    String noteSinceApiDoesNotProvide();

    @DefaultMessage("Hint: you can import corp blueprints as personal and vice-versa")
    String hintYouCanImport();

    @DefaultMessage("Hint: blueprint itemIDs are also imported, making them clickable when using IGB")
    String hintBlueprintItemIDs();

    @DefaultMessage("Post XML from API")
    String postXmlFromApi();

    @DefaultMessage("or if you are lazy to do that")
    String orIfYouAreLazy();

    @DefaultMessage("Note: full API key will not be stored or cached in application")
    String noteFullApiKeyWillNotBeStored();

    @DefaultMessage("or post CSV")
    String orPostCsv();

    @DefaultMessage("You can import also import CSV. Please read about it <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/wiki/ImportingCSV\">here</a>")
    String descriptionYouCanImportCsv();

    @DefaultMessage("or if you have full API key entered in preferences")
    String orIfYouHaveFullApiKeyEnteredInPreferences();

    @DefaultMessage("and attach to")
    String andAttachTo();

    @DefaultMessage("Attached character")
    String attachedCharacter();

    @DefaultMessage("Attach to character")
    String attachToCharacter();

    @DefaultMessage("Sharing")
    String sharing();

    @DefaultMessage("Sharing level")
    String sharingLevel();

    @DefaultMessage("Level")
    String level();

    @DefaultMessage("Character")
    String character();

    @DefaultMessage("Personal")
    String personal();

    @DefaultMessage("Corporation")
    String corporation();

    @DefaultMessage("Alliance")
    String alliance();

    @DefaultMessage("Import")
    String import_();

    @DefaultMessage("Library")
    String library();

    @DefaultMessage("Filter")
    String filter();

    @DefaultMessage("Reload")
    String reload();

    @DefaultMessage("Load")
    String load();

    @DefaultMessage("Hide")
    String hide();

    @DefaultMessage("Show")
    String show();

    @DefaultMessage("ME")
    String me();

    @DefaultMessage("Material level")
    String materialLevel();

    @DefaultMessage("PE")
    String pe();

    @DefaultMessage("Productivity level")
    String productivityLevel();

    @DefaultMessage("Item id")
    String itemID();

    @DefaultMessage("Are you sure?")
    String areYouSure();

    @DefaultMessage("Yes")
    String yes();

    @DefaultMessage("Details")
    String details();

    @DefaultMessage("Edit")
    String edit();

    @DefaultMessage("Time (station)")
    String timeStation();

    @DefaultMessage("Time (POS lab)")
    String timePos();

    @DefaultMessage("Researching time productivity")
    String researchingTimeProductivity();

    @DefaultMessage("Researching material productivity")
    String researchingMaterialProductivity();

    @DefaultMessage("Copying")
    String copying();

    @DefaultMessage("Invention")
    String invention();

    @DefaultMessage("Corporation library")
    String corporationLibrary();

    @DefaultMessage("Alliance library")
    String allianceLibrary();

    @DefaultMessage("Price sets")
    String priceSets();

    @DefaultMessage("Select price set")
    String selectPriceSet();

    @DefaultMessage("Other operations")
    String otherOperations();

    @DefaultMessage("Create")
    String create();

    @DefaultMessage("Copy current")
    String copyCurrent();

    @DefaultMessage("Rename current")
    String renameCurrent();

    @DefaultMessage("Delete current")
    String deleteCurrent();

    @DefaultMessage("Add basic minerals")
    String addBasicMinerals();

    @DefaultMessage("Add advanced moon components")
    String addAdvancedMoonComponents();

    @DefaultMessage("Price set contents")
    String priceSetContents();

    @DefaultMessage("Prices")
    String prices();

    @DefaultMessage("None")
    String none();

    @DefaultMessage("Save")
    String save();

    @DefaultMessage("Fetch prices from EVE-Central")
    String fetchPricesFromEveCentral();

    @DefaultMessage("or EVE Metrics")
    String orEveMetrics();

    @DefaultMessage("Price set is not available anymore")
    String priceSetIsNotAvailable();

    @DefaultMessage("Enter item name")
    String enterItemName();

    @DefaultMessage("Hint: minimum 3 characters")
    String hintSuggesting();

    @DefaultMessage("Hint: you can add any valid in-game item")
    String hintYouCanAdd();

    @DefaultMessage("Note: prices from EVE-Central and EVE Metrics are taken as buy and sell median price in The Forge region")
    String notePricesAreTakenFrom();

    @DefaultMessage("Delete")
    String delete();

    @DefaultMessage("Corporation price sets")
    String corporationPriceSets();

    @DefaultMessage("Alliance price sets")
    String alliancePriceSets();

    @DefaultMessage("Add")
    String add();

    @DefaultMessage("Set")
    String set();

    @DefaultMessage("Hint: if you do not want to be able to share your blueprints or price sets with your corpmates, you can don''t bother with characters and API keys")
    String hintOnlyForSharing();

    @DefaultMessage("Blueprint cost")
    String blueprintCost();

    @DefaultMessage("Characters")
    String characters();

    @DefaultMessage("click to set as main character")
    String clickToSetAsMainCharacter();

    @DefaultMessage("current main character")
    String currentMainCharacter();

    @DefaultMessage("Add characters")
    String addCharacter();

    @DefaultMessage("Hint: characters are needed to have access to corp/alliance shared blueprints and price sets")
    String hintCharactersAreNeeded();

    @DefaultMessage("Hint: all blueprints and price sets are attached to application user, not characters. So, deleting a character won''t delete any blueprints or price sets")
    String hintSameSharingLevel();

    @DefaultMessage("API keys")
    String apiKeys();

    @DefaultMessage("Enter user ID")
    String enterUserID();

    @DefaultMessage("Enter API key")
    String enterApiKey();

    @DefaultMessage("Note: API keys are checked every 24 hours. If key becomes invalid, corresponding characters will be detached from corporation and alliance. Your blueprints will be visible only for you despite having corp/alliance sharingLevel level")
    String noteKeysAreChecked();

    @DefaultMessage("Hint: go to {0} to get you API keys")
    String hintUserThisLink(String eveApiKeyManagementPageLinkHtml);

    @DefaultMessage("No valid API key found")
    String noValidApiKeyFound();

    @DefaultMessage("No corp titles")
    String noCorpTitles();

    @DefaultMessage("Last API check date")
    String lastApiCheckDate();

    @DefaultMessage("Limited API key")
    String limitedApiKey();

    @DefaultMessage("Full API key")
    String fullApiKey();

    @DefaultMessage("Character with full API key")
    String characterWithFullApiKey();

    @DefaultMessage("User ID")
    String userID();

    @DefaultMessage("Character ID")
    String characterID();

    @DefaultMessage("EVE API Key Management")
    String eveApiKeyManagement();

    @DefaultMessage("Apply")
    String apply();

    @DefaultMessage("Use all blueprints")
    String useAllBlueprints();

    @DefaultMessage("Stop using all blueprints")
    String stopUsingAllBlueprints();

    @DefaultMessage("Use blueprint")
    String useBlueprint();

    @DefaultMessage("Stop using blueprint")
    String stopUsingBlueprint();

    @DefaultMessage("Quantity")
    String quantity();

    @DefaultMessage("Empty")
    String empty();

    @DefaultMessage("Note: quick calculator is intended for quick calculations: it does not allow to save calculation result, use library blueprints and existing price sets")
    String noteQuickCalculatorIsIntended();

    @DefaultMessage("Create direct link")
    String createDirectLink();

    @DefaultMessage("Quicklook in EVE-Central")
    String eveCentralQuicklook();

    @DefaultMessage("Item price in EVE Metrics")
    String eveMetricsItemPrice();

    @DefaultMessage("Welcome to <strong>EveManage</strong>! Visit <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/\">homepage</a>. Read <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/wiki/News\">news</a>")
    String welcomeToEveManage();

    @DefaultMessage("Tab navigation")
    String tabNavigation();

    @DefaultMessage("<strong>Dashboard</strong>. Place to start. For now, it has only general information and some tips, but it will likely have something more useful in future")
    String tabDashboard();

    @DefaultMessage("<strong>Blueprints</strong>. Place to <em>create</em>, <em>browse</em> and <em>manage</em> blueprint library. It is possible to <em>import</em> blueprints from API. Each blueprint can be <em>attached</em> to any of user characters and <em>shared</em> between this character corporation or alliance members. It is possible to <em>browse</em> corporation and alliance library")
    String tabBlueprints();

    @DefaultMessage("<strong>Price sets</strong>. Place to <em>keep</em> and <em>manage</em> prices on minerals, materials or just items of interest. Each price set consists of price set items, can be <em>attached</em> to characters and <em>shared</em> on corporation or alliance level. It is possible to <em>fetch</em> prices from <a target=\"_blank\" href=\"http://eve-central.com/\">EVE-Central</a> or <a target=\"_blank\" href=\"http://www.eve-metrics.com/\">EVE Metrics</a>")
    String tabPriceSets();

    @DefaultMessage("<strong>Preferences</strong>. Home for API keys and characters. It is possible to <em>have many</em> API keys and characters. One of characters is considered <em>main</em> (can be <em>switched</em> by clicking on portrait). Corporation and alliance shared blueprints, price sets and calculations are shown <em>only for current</em> main character corporation and alliance")
    String tabPreferences();

    @DefaultMessage("<strong>Quick calculator</strong>. Simple yet <em>complex enough</em> calculator to do the math with blueprints. <em>Recursively builds</em> calculation tree, allows using any <em>dependency</em> blueprints with given ME/PE levels. <em>Does not</em> allow to store result, use blueprint library and existing price sets")
    String tabQuickCalculator();

    @DefaultMessage("<strong>About</strong>. Entry place for all not logged users")
    String tabAbout();

    @DefaultMessage("IGB integration")
    String igbIntegration();

    @DefaultMessage("EveManage truly shines when used through in-game browser (IGB). Here are examples")
    String igbExamples();

    @DefaultMessage("Clicking on item icons will open in-game <em>item info window</em>. In regular browsers does nothing")
    String igbClickingOnIcons();

    @DefaultMessage("If blueprint has item id then it is possible to click on it and to see <em>actual blueprint info window</em> (with ME and PE), otherwise basic item info opens. In regular browsers does nothing")
    String igbBlueprint();

    @DefaultMessage("Clicking on corporation icon will open corporation <em>info window</em>, click on character portrait (except characters section on preferences tab) will show character <em>information window</em>. In regular browsers opens corporation sheet on <a target=\"_blank\" href=\"http://evemaps.dotlan.net/\">DOTLAN</a> page and does nothing for character")
    String igbClickingOnCorporation();

    @DefaultMessage("Clicking on item text links will open in-game <em>market window</em> for this item. In regular browsers opens <a target=\"_blank\" href=\"http://wiki.eveonline.com/en/wiki/Main_Page\">EVElopedia</a> page for this item")
    String igbClickingOnLinks();

    @DefaultMessage("Contribute")
    String contribute();

    @DefaultMessage("Found a bug? Don''t let it go &mdash; report it! Read about <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/wiki/ReportingBugs\">reporting bugs</a>")
    String contributeBug();

    @DefaultMessage("Want a feature? Don''t be shy &mdash; request it! Read about <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/wiki/RequestingFeatures\">requesting features</a>")
    String contributeFeature();

    @DefaultMessage("This is <strong>EveManage</strong>, web-application for industry in <a target=\"_blank\" href=\"http://www.eveonline.com/\">EVE Online</a>. It is <em>free</em>, <em>open-source</em> and being actively <em>developed</em>")
    String thisIsEveManage();

    @DefaultMessage("Features")
    String features();

    @DefaultMessage("<strong>Blueprints</strong>. Ability to create own blueprint collection, share it with corporation and alliance. Get blueprint manufacturing and research information. <a target=\"_blank\" href=\"http://img808.imageshack.us/img808/1536/evem2.png\">Screenshot</a>")
    String featureBlueprints();

    @DefaultMessage("<strong>Price sets</strong>. Ability to create own collection of prices on various items. As with blueprints, it is possible to share them on corporation and alliance levels. <a target=\"_blank\" href=\"http://img190.imageshack.us/img190/8584/evem3.png\">Screenshot</a>")
    String featurePriceSets();

    @DefaultMessage("<strong>API-based</strong>. Character, corporation and alliance information is based on limited api-keys. This is optional, if you do not want to share anything you can ignore this feature. <a target=\"_blank\" href=\"http://img183.imageshack.us/img183/709/evem4.png\">Screenshot</a>")
    String featureApiBased();

    @DefaultMessage("<strong>Recursive calculations</strong>. Simple, clear, yet complex enough calculator to perform blueprint-related calculations. <a target=\"_blank\" href=\"http://img149.imageshack.us/img149/9052/evem5.png\">Screenshot</a>")
    String featureRecursiveCalculations();

    @DefaultMessage("<strong>IGB integration</strong>. Thanks to new in-game browser, there are new ways how web-application can interact with game. <a target=\"_blank\" href=\"http://img16.imageshack.us/img16/9894/screenshot6kx.png\">Screenshot1</a>, <a target=\"_blank\" href=\"http://img220.imageshack.us/img220/9235/screenshot7h.png\">screenshot2</a>, <a target=\"_blank\" href=\"http://img28.imageshack.us/img28/4364/screenshot8e.png\">screenshot3</a>")
    String featureIgbIntegration();

    @DefaultMessage("<strong>Power of web</strong>. While not evident, EveManage utilizes comprehensive and advanced web-technologies, aiming to be user-friendly and clear in it''s design. It is possible to use it at work, in-game or through you favorite browser on any platform")
    String featurePowerOfWeb();

    @DefaultMessage("<strong>OpenID</strong>. There is no need for yet-another registration process, you can log with existing Google account. It is secure and convenient. Read more about OpenID <a target=\"_blank\" href=\"http://openid.net/\">here</a>")
    String featureOpenID();

    @DefaultMessage("<strong>AppEngine</strong>. EveManage is designed to run smoothly on <a target=\"_blank\" href=\"http://code.google.com/appengine/\">Google infrastructure</a>, ensuring scalability and availability for everyone")
    String featureAppEngine();

    @DefaultMessage("So, what now?")
    String whatNow();

    @DefaultMessage("You can use quick calculator which is open for public, or you can log in and proceed to other application tabs. Also, you might want to see <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/wiki/Screenshots\">screenshots</a>, visit <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/\">homepage</a> or read <a target=\"_blank\" href=\"http://code.google.com/p/evemanage/wiki/News\">news</a>")
    String whatNowCalculator();

    @DefaultMessage("&copy; 2010 by {0} | All {1} materials are property of {2} | Powered by {3}")
    String footerText(String authorHtml, String eveOnlineHtml, String ccpGamesHtml, String appEngineHtml);

    @DefaultMessage("version: {0} | EveDb version: {1}")
    String footerVersionText(String eveManageVersion, String eveDbVersion);
}
