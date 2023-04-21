package opwoco;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import opwoco_webservice.WebToMobileClient;
import org.netbeans.microedition.lcdui.LoginScreen;
import org.netbeans.microedition.lcdui.SimpleTableModel;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;

/**
 * @author Christofer
 */
public class opwoco extends MIDlet implements CommandListener {

    private boolean midletPaused = false;

    private String theVersion = "0.3alpha";

    private Command exitCommand;

    private Command LoginCommand;

    private Command backCommand;

    private Command UpdateCommand;

    private Command AboutCommand;

    private Command backCommand1;

    private Command backCommand3;

    private Command backCommand2;

    private Command BookmarkCommand;

    private Command PricewatchCommand;

    private Command backCommand4;

    private Form mainForm;

    private StringItem stringItem;

    private SplashScreen splashScreen;

    private LoginScreen loginScreen;

    private WaitScreen waitScreen;

    private Form SearchScreen;

    private TextField txtProductCode;

    private Form UpdateForm;

    private StringItem stringItem2;

    private Form About;

    private StringItem stringItem3;

    private StringItem stringItem4;

    private StringItem stringItem1;

    private WaitScreen saveIT_and_wait_screen;

    private WaitScreen PriceWatch_andWaitScreen;

    private Form ProductDetails;

    private StringItem txtProductDetails_Code;

    private StringItem txtProductDetails_Price;

    private StringItem txtProductDetails_Distributor;

    private StringItem txtProductDetails_Description;

    private Image image2;

    private SimpleTableModel tableModel1;

    private SimpleCancellableTask task;

    private SimpleCancellableTask task1;

    private SimpleCancellableTask task2;

    /**
     * The opwoco constructor.
     */
    public opwoco() {
    }

    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {
        switchDisplayable(null, getSplashScreen());
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {
    }

    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = getDisplay();
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == About) {
            if (command == backCommand2) {
                switchDisplayable(null, getMainForm());
            }
        } else if (displayable == PriceWatch_andWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {
                switchDisplayable(null, getSearchScreen());
            } else if (command == WaitScreen.SUCCESS_COMMAND) {
                switchDisplayable(null, getProductDetails());
            }
        } else if (displayable == ProductDetails) {
            if (command == backCommand4) {
                switchDisplayable(null, getSearchScreen());
            }
        } else if (displayable == SearchScreen) {
            if (command == BookmarkCommand) {
                switchDisplayable(null, getSaveIT_and_wait_screen());
            } else if (command == PricewatchCommand) {
                switchDisplayable(null, getPriceWatch_andWaitScreen());
            } else if (command == backCommand3) {
                switchDisplayable(null, getMainForm());
            }
        } else if (displayable == UpdateForm) {
            if (command == backCommand1) {
                switchDisplayable(null, getMainForm());
            }
        } else if (displayable == loginScreen) {
            if (command == LoginScreen.LOGIN_COMMAND) {
                switchDisplayable(null, getWaitScreen());
            } else if (command == backCommand) {
                switchDisplayable(null, getMainForm());
            }
        } else if (displayable == mainForm) {
            if (command == AboutCommand) {
                switchDisplayable(null, getAbout());
            } else if (command == LoginCommand) {
                switchDisplayable(null, getLoginScreen());
            } else if (command == UpdateCommand) {
                switchDisplayable(null, getUpdateForm());
            } else if (command == exitCommand) {
                exitMIDlet();
            }
        } else if (displayable == saveIT_and_wait_screen) {
            if (command == WaitScreen.FAILURE_COMMAND) {
                switchDisplayable(null, getSearchScreen());
            } else if (command == WaitScreen.SUCCESS_COMMAND) {
                switchDisplayable(null, getSearchScreen());
            }
        } else if (displayable == splashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {
                switchDisplayable(null, getMainForm());
            }
        } else if (displayable == waitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {
                switchDisplayable(null, getLoginScreen());
            } else if (command == WaitScreen.SUCCESS_COMMAND) {
                switchDisplayable(null, getSearchScreen());
            }
        }
    }

    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {
            exitCommand = new Command("Exit", Command.EXIT, 0);
        }
        return exitCommand;
    }

    /**
     * Returns an initiliazed instance of mainForm component.
     * @return the initialized component instance
     */
    public Form getMainForm() {
        if (mainForm == null) {
            mainForm = new Form("Welcome", new Item[] { getStringItem() });
            mainForm.addCommand(getExitCommand());
            mainForm.addCommand(getLoginCommand());
            mainForm.addCommand(getUpdateCommand());
            mainForm.addCommand(getAboutCommand());
            mainForm.setCommandListener(this);
        }
        return mainForm;
    }

    /**
     * Returns an initiliazed instance of stringItem component.
     * @return the initialized component instance
     */
    public StringItem getStringItem() {
        if (stringItem == null) {
            stringItem = new StringItem("Please use the menu to choose the wished option", "");
            stringItem.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND | Item.LAYOUT_2);
        }
        return stringItem;
    }

    /**
     * Returns an initiliazed instance of LoginCommand component.
     * @return the initialized component instance
     */
    public Command getLoginCommand() {
        if (LoginCommand == null) {
            LoginCommand = new Command("Login", Command.SCREEN, 0);
        }
        return LoginCommand;
    }

    /**
     * Returns an initiliazed instance of image2 component.
     * @return the initialized component instance
     */
    public Image getImage2() {
        if (image2 == null) {
            try {
                image2 = Image.createImage("/logo.png");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return image2;
    }

    /**
     * Returns an initiliazed instance of splashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreen() {
        if (splashScreen == null) {
            splashScreen = new SplashScreen(getDisplay());
            splashScreen.setTitle("Open World Code");
            splashScreen.setCommandListener(this);
            splashScreen.setImage(getImage2());
            splashScreen.setText(" ");
            splashScreen.setTimeout(1000);
        }
        return splashScreen;
    }

    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {
            backCommand = new Command("Back", Command.BACK, 0);
        }
        return backCommand;
    }

    /**
     * Returns an initiliazed instance of loginScreen component.
     * @return the initialized component instance
     */
    public LoginScreen getLoginScreen() {
        if (loginScreen == null) {
            loginScreen = new LoginScreen(getDisplay());
            loginScreen.setLabelTexts("Username", "Password");
            loginScreen.setTitle("loginScreen");
            loginScreen.addCommand(LoginScreen.LOGIN_COMMAND);
            loginScreen.addCommand(getBackCommand());
            loginScreen.setCommandListener(this);
            loginScreen.setBGColor(-3355444);
            loginScreen.setFGColor(0);
            loginScreen.setUseLoginButton(false);
            loginScreen.setLoginButtonText("Login");
        }
        return loginScreen;
    }

    /**
     * Returns an initiliazed instance of tableModel1 component.
     * @return the initialized component instance
     */
    public SimpleTableModel getTableModel1() {
        if (tableModel1 == null) {
            tableModel1 = new SimpleTableModel(new java.lang.String[][] { new java.lang.String[] { "Login" }, new java.lang.String[] { "Help" }, new java.lang.String[] { "Search" }, new java.lang.String[] { "Ecit" } }, null);
        }
        return tableModel1;
    }

    /**
     * Returns an initiliazed instance of waitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getWaitScreen() {
        if (waitScreen == null) {
            waitScreen = new WaitScreen(getDisplay());
            waitScreen.setTitle("waitScreen");
            waitScreen.setCommandListener(this);
            waitScreen.setText("OpWoCo is trying to log in....");
            waitScreen.setTask(getTask());
        }
        return waitScreen;
    }

    /**
     * Returns an initiliazed instance of task component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getTask() {
        if (task == null) {
            task = new SimpleCancellableTask();
            task.setExecutable(new org.netbeans.microedition.util.Executable() {

                public void execute() throws Exception {
                    login();
                }
            });
        }
        return task;
    }

    /**
     * Returns an initiliazed instance of SearchScreen component.
     * @return the initialized component instance
     */
    public Form getSearchScreen() {
        if (SearchScreen == null) {
            SearchScreen = new Form("OpWoCo - Welcome", new Item[] { getTxtProductCode() });
            SearchScreen.addCommand(getBackCommand3());
            SearchScreen.addCommand(getBookmarkCommand());
            SearchScreen.addCommand(getPricewatchCommand());
            SearchScreen.setCommandListener(this);
        }
        return SearchScreen;
    }

    /**
     * Returns an initiliazed instance of txtProductCode component.
     * @return the initialized component instance
     */
    public TextField getTxtProductCode() {
        if (txtProductCode == null) {
            txtProductCode = new TextField("Enter product code:", null, 32, TextField.ANY);
        }
        return txtProductCode;
    }

    /**
     * Returns an initiliazed instance of UpdateForm component.
     * @return the initialized component instance
     */
    public Form getUpdateForm() {
        if (UpdateForm == null) {
            UpdateForm = new Form("OpWoCo Update", new Item[] { getStringItem2() });
            UpdateForm.addCommand(getBackCommand1());
            UpdateForm.setCommandListener(this);
            check4Update();
        }
        return UpdateForm;
    }

    /**
     * Returns an initiliazed instance of UpdateCommand component.
     * @return the initialized component instance
     */
    public Command getUpdateCommand() {
        if (UpdateCommand == null) {
            UpdateCommand = new Command("Update", Command.SCREEN, 0);
        }
        return UpdateCommand;
    }

    /**
     * Returns an initiliazed instance of AboutCommand component.
     * @return the initialized component instance
     */
    public Command getAboutCommand() {
        if (AboutCommand == null) {
            AboutCommand = new Command("About", Command.SCREEN, 0);
        }
        return AboutCommand;
    }

    /**
     * Returns an initiliazed instance of About component.
     * @return the initialized component instance
     */
    public Form getAbout() {
        if (About == null) {
            About = new Form("About", new Item[] { getStringItem1(), getStringItem3(), getStringItem4() });
            About.addCommand(getBackCommand2());
            About.setCommandListener(this);
        }
        return About;
    }

    /**
     * Returns an initiliazed instance of stringItem1 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem1() {
        if (stringItem1 == null) {
            stringItem1 = new StringItem("Version: ", getVersion());
        }
        return stringItem1;
    }

    /**
     * Returns an initiliazed instance of stringItem3 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem3() {
        if (stringItem3 == null) {
            stringItem3 = new StringItem("License:", "This Software is published under the GNU license GPL v2");
        }
        return stringItem3;
    }

    /**
     * Returns an initiliazed instance of stringItem4 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem4() {
        if (stringItem4 == null) {
            stringItem4 = new StringItem("Further information: ", "For further information take a look at www.opwoco.net");
        }
        return stringItem4;
    }

    /**
     * Returns an initiliazed instance of backCommand1 component.
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {
            backCommand1 = new Command("Back", Command.BACK, 0);
        }
        return backCommand1;
    }

    /**
     * Returns an initiliazed instance of backCommand2 component.
     * @return the initialized component instance
     */
    public Command getBackCommand2() {
        if (backCommand2 == null) {
            backCommand2 = new Command("Back", Command.BACK, 0);
        }
        return backCommand2;
    }

    /**
     * Returns an initiliazed instance of backCommand3 component.
     * @return the initialized component instance
     */
    public Command getBackCommand3() {
        if (backCommand3 == null) {
            backCommand3 = new Command("Back", Command.BACK, 0);
        }
        return backCommand3;
    }

    /**
     * Returns an initiliazed instance of BookmarkCommand component.
     * @return the initialized component instance
     */
    public Command getBookmarkCommand() {
        if (BookmarkCommand == null) {
            BookmarkCommand = new Command("Bookmark", Command.OK, 0);
        }
        return BookmarkCommand;
    }

    /**
     * Returns an initiliazed instance of saveIT_and_wait_screen component.
     * @return the initialized component instance
     */
    public WaitScreen getSaveIT_and_wait_screen() {
        if (saveIT_and_wait_screen == null) {
            saveIT_and_wait_screen = new WaitScreen(getDisplay());
            saveIT_and_wait_screen.setTitle("Saving product code");
            saveIT_and_wait_screen.setCommandListener(this);
            saveIT_and_wait_screen.setText("opwoco is saving the product code. You van find it at your personal account at www.opwoco.net");
            saveIT_and_wait_screen.setTask(getTask1());
        }
        return saveIT_and_wait_screen;
    }

    /**
     * Returns an initiliazed instance of task1 component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getTask1() {
        if (task1 == null) {
            task1 = new SimpleCancellableTask();
            task1.setExecutable(new org.netbeans.microedition.util.Executable() {

                public void execute() throws Exception {
                    BookmarkProductCode();
                }
            });
        }
        return task1;
    }

    /**
     * Returns an initiliazed instance of PricewatchCommand component.
     * @return the initialized component instance
     */
    public Command getPricewatchCommand() {
        if (PricewatchCommand == null) {
            PricewatchCommand = new Command("Pricewatch", Command.OK, 0);
        }
        return PricewatchCommand;
    }

    /**
     * Returns an initiliazed instance of PriceWatch_andWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getPriceWatch_andWaitScreen() {
        if (PriceWatch_andWaitScreen == null) {
            PriceWatch_andWaitScreen = new WaitScreen(getDisplay());
            PriceWatch_andWaitScreen.setTitle("Pricewatch");
            PriceWatch_andWaitScreen.setCommandListener(this);
            PriceWatch_andWaitScreen.setText("Looking up for your product...");
            PriceWatch_andWaitScreen.setTask(getTask2());
        }
        return PriceWatch_andWaitScreen;
    }

    /**
     * Returns an initiliazed instance of task2 component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getTask2() {
        if (task2 == null) {
            task2 = new SimpleCancellableTask();
            task2.setExecutable(new org.netbeans.microedition.util.Executable() {

                public void execute() throws Exception {
                    searchForProduct();
                }
            });
        }
        return task2;
    }

    /**
     * Returns an initiliazed instance of backCommand4 component.
     * @return the initialized component instance
     */
    public Command getBackCommand4() {
        if (backCommand4 == null) {
            backCommand4 = new Command("Back", Command.BACK, 0);
        }
        return backCommand4;
    }

    /**
     * Returns an initiliazed instance of stringItem2 component.
     * @return the initialized component instance
     */
    public StringItem getStringItem2() {
        if (stringItem2 == null) {
            stringItem2 = new StringItem("Comming soon", "We are working hard, so the aut-update funtion will be here in a short time. For so far go to www.opwoco.net");
        }
        return stringItem2;
    }

    /**
     * Returns an initiliazed instance of ProductDetails component.
     * @return the initialized component instance
     */
    public Form getProductDetails() {
        if (ProductDetails == null) {
            ProductDetails = new Form("Product Details", new Item[] { getTxtProductDetails_Code(), getTxtProductDetails_Price(), getTxtProductDetails_Distributor(), getTxtProductDetails_Description() });
            ProductDetails.addCommand(getBackCommand4());
            ProductDetails.setCommandListener(this);
        }
        return ProductDetails;
    }

    /**
     * Returns an initiliazed instance of txtProductDetails_Code component.
     * @return the initialized component instance
     */
    public StringItem getTxtProductDetails_Code() {
        if (txtProductDetails_Code == null) {
            txtProductDetails_Code = new StringItem("ProductCode", null);
        }
        return txtProductDetails_Code;
    }

    /**
     * Returns an initiliazed instance of txtProductDetails_Price component.
     * @return the initialized component instance
     */
    public StringItem getTxtProductDetails_Price() {
        if (txtProductDetails_Price == null) {
            txtProductDetails_Price = new StringItem("Price", null);
        }
        return txtProductDetails_Price;
    }

    /**
     * Returns an initiliazed instance of txtProductDetails_Distributor component.
     * @return the initialized component instance
     */
    public StringItem getTxtProductDetails_Distributor() {
        if (txtProductDetails_Distributor == null) {
            txtProductDetails_Distributor = new StringItem("Distributor", null);
        }
        return txtProductDetails_Distributor;
    }

    /**
     * Returns an initiliazed instance of txtProductDetails_Description component.
     * @return the initialized component instance
     */
    public StringItem getTxtProductDetails_Description() {
        if (txtProductDetails_Description == null) {
            txtProductDetails_Description = new StringItem("Description", null);
        }
        return txtProductDetails_Description;
    }

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    private void login() throws Exception {
        WebToMobileClient mySoap = new WebToMobileClient();
        Boolean myLoginResult = mySoap.checkLogin(loginScreen.getUsername(), loginScreen.getPassword());
        if (myLoginResult.booleanValue()) {
            return;
        }
        waitScreen.setText("login failed for User  " + loginScreen.getUsername());
        Thread.sleep(2 * 1000);
        waitScreen.setText("Im trying to log in...");
        throw new Exception("niet gelukt");
    }

    private String getVersion() {
        return theVersion;
    }

    private void check4Update() {
    }

    private void BookmarkProductCode() throws Exception {
        WebToMobileClient mySoap = new WebToMobileClient();
        mySoap.bookmarkCode(txtProductCode.getString(), loginScreen.getUsername());
        saveIT_and_wait_screen.setText("Bookmarking of " + txtProductCode.getString() + " done.");
        Thread.sleep(2 * 1000);
        throw new Exception("niet gelukt");
    }

    private void searchForProduct() throws Exception {
        WebToMobileClient mySoap = new WebToMobileClient();
        String data = mySoap.getProductInfo(txtProductCode.getString());
        String mySeperator = ";";
        if (data.toLowerCase().compareTo("noinfo") == 0) {
            PriceWatch_andWaitScreen.setText("Sorry, no product info found :(");
            Thread.sleep(3 * 1000);
            throw new Exception("no product info");
        }
        int take1 = data.indexOf(mySeperator, 0);
        int take2 = data.indexOf(mySeperator, take1 + 1);
        int take3 = data.indexOf(mySeperator, take2 + 1);
        getProductDetails();
        txtProductDetails_Code.setText(data.substring(0, take1));
        txtProductDetails_Price.setText(data.substring(take1 + 1, take2).toString());
        txtProductDetails_Distributor.setText(data.substring(take2 + 1, take3).toString());
        txtProductDetails_Description.setText(data.substring(take3 + 1).toString());
    }
}
