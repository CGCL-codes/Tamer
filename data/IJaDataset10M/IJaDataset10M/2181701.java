package com.ubx1.pdpscanner.client;

import java.util.Date;
import java.util.HashMap;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.ubx1.pdpscanner.client.services.ProjectService;
import com.ubx1.pdpscanner.client.services.ProjectServiceAsync;
import com.ubx1.pdpscanner.client.services.UserService;
import com.ubx1.pdpscanner.client.services.UserServiceAsync;
import com.ubx1.pdpscanner.client.views.account.AccountView;
import com.ubx1.pdpscanner.client.views.home.HomeView;
import com.ubx1.pdpscanner.client.views.project.ProjectView;
import com.ubx1.pdpscanner.client.views.tips.TipsView;
import com.ubx1.pdpscanner.shared.Project;
import com.ubx1.pdpscanner.shared.exceptions.BadCellException;
import com.ubx1.pdpscanner.shared.exceptions.BadEmailException;
import com.ubx1.pdpscanner.shared.exceptions.BadLoginException;
import com.ubx1.pdpscanner.shared.exceptions.BadPasswordConfirmationException;
import com.ubx1.pdpscanner.shared.exceptions.BadPasswordLengthException;
import com.ubx1.pdpscanner.shared.exceptions.BadUsernameException;
import com.ubx1.pdpscanner.shared.exceptions.RequiredFieldException;
import com.ubx1.pdpscanner.shared.validation.Validator;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PdpScanner implements EntryPoint, ValueChangeHandler<String> {

    /**
	 * Error and success messages
	 */
    public static final String BAD_NAME_ERROR = "Nom du projet incorrect!\n" + "Les espaces sont interdits.\n" + "Ce nom de projet existe peut-être déjà.";

    public static final String BAD_URL_ERROR = "URL du dépôt incorrecte!\n" + "Veuillez vérifier l'URL du dépôt SVN.";

    public static final String BAD_PROJECT_REPOSITORY_CRENDENTIALS_ERROR = "Identifiants pour le dépôt incorrect!\n" + "Vous devez spécifier utilisateur et mot de passe.";

    public static final String BAD_USERNAME_ERROR_LOGIN = "Nom d'utilisateur incorrect!";

    public static final String BAD_USERNAME_ERROR_SIGNUP = "Nom d'utilisateur incorrect!\n" + "Ce nom d'utilisateur existe peut-être déjà.";

    public static final String BAD_PASSWORD_ERROR = "Mot de passe incorrect!";

    public static final String BAD_LOGIN_ERROR = "Nom d'utilisteur ou mot de passe incorrect!";

    public static final String FETCH_PROJECTS_ERROR = "Erreur lors de la récupération des projets!";

    public static final String ADD_PROJECT_SUCCESS = "Le projet a été ajouté avec succès.";

    public static final String ADD_PROJECT_ERROR = "Erreur lors de la création du nouveau projet!";

    public static final String CHECK_PROJECT_REPOSITORY_ERROR = "Le dépôt du projet n'a pas pu être atteint!\n" + "Veuillez vérifier l'URL et les identifiants du dépôt.";

    public static final String DELETE_PROJECT_CONFIRM = "Êtes-vous sûr de vouloir supprimer le projet?";

    public static final String DELETE_PROJECT_SUCCESS = "Le projet a été supprimé avec succès.";

    public static final String DELETE_PROJECT_ERROR = "Erreur lors de la suppression du projet!";

    public static final String FETCH_STATS_ERROR = "Erreur lors de la récupération des statistiques du projet!";

    public static final String LOGIN_USER_ERROR = "Impossible de vous identifier!\n" + "Veuillez réessayer ultérieurement.";

    public static final String UPDATE_PASSWORD_ERROR = "Impossible de mettre à jour le mot de passe!\n" + "Veuillez réessayer ultérieurement.";

    public static final String UPDATE_PASSWORD_SUCCESS = "Le mot de passe a été mis à jour avec succès.";

    public static final String SIGNUP_USER_ERROR = "Impossible d'enregistrer le nouveau compte!";

    public static final String LOGOUT_USER_ERROR = "Impossible de vous déconnecter!";

    public static final String REGISTER_USER_SUCCESS = "Le compte a été créé avec succès.";

    public static final String BAD_EMAIL_ERROR = "Adresse mail incorrecte!";

    public static final String BAD_CELL_ERROR = "Numéro de mobile incorrect!";

    public static final String BAD_PASSWORD_CONFIRMATION_ERROR = "Confirmation du mot de passe incorrecte!";

    public static final String REQUIRED_FIELDS_ERROR = "Les champs marqués d'une astérisque (*) sont obligatoires!";

    public static final String BAD_PASSWORD_LENGTH_ERROR = "Le mot de passe doit comporter au moins 6 caractères!";

    /**
	 * The History handler
	 */
    private HandlerRegistration historyHandler = null;

    /**
	 * Remote project service proxy to talk to the server-side project service
	 */
    public static final ProjectServiceAsync projectService = GWT.create(ProjectService.class);

    /**
	 * Remote user service proxy to talk to the server-side user service
	 */
    public static final UserServiceAsync userService = GWT.create(UserService.class);

    /**
	 * The application "pages"
	 */
    private HomeView homeView = null;

    private ProjectView projectView = null;

    private AccountView accountView = null;

    private TipsView tipsView = null;

    /**
	 * The login popup
	 */
    private final PopupPanel loginSignupPopup = new PopupPanel(false, true);

    /**
	 * The current Project objects in a map
	 */
    public static final HashMap<String, Project> projects = new HashMap<String, Project>();

    /**
	 * This is the entry point method
	 */
    public void onModuleLoad() {
        Runnable onLoadCallback = new Runnable() {

            public void run() {
                System.out.println("Visualization API loaded successfully.");
            }
        };
        VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE, PieChart.PACKAGE, AreaChart.PACKAGE);
        checkSessionValidity();
    }

    /**
	 * The application initializes after the user is safely logged in
	 */
    private void initApp() {
        buildMenuBar();
        History.newItem("Home");
        historyHandler = History.addValueChangeHandler(this);
        History.fireCurrentHistoryState();
    }

    /**
	 * The application closes when the user's session becomes invalid
	 */
    private void closeApp() {
        if (historyHandler != null) {
            historyHandler.removeHandler();
        }
        RootPanel.get("menuBar").clear();
        RootPanel.get().clear();
    }

    /**
	 * Construct the menu bar
	 */
    private void buildMenuBar() {
        addMenuItem("home.png", "Accueil", "Home");
        addMenuItem("user.png", "Mon compte", "Account");
        addMenuItem("tips.png", "Instructions", "Tips");
        addMenuItem("disconnect.png", "Déconnexion", "Disconnected");
    }

    /**
	 * Add a menu item to the menu bar
	 * 
	 * @param img
	 *            the name of the menu item's image in the "images" directory
	 * @param text
	 *            the text to display on the menu item
	 * @param historyState
	 *            history token, used to know what to do when the menu item is
	 *            clicked
	 */
    private void addMenuItem(String img, String text, final String historyState) {
        Image itemImg = new Image("images/" + img);
        itemImg.setTitle(text);
        itemImg.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                History.newItem(historyState);
            }
        });
        itemImg.setStyleName("menuItem");
        RootPanel.get("menuBar").add(itemImg);
    }

    /**
	 * Fetch the stats of a project, put them in the given Project object
	 * 
	 * @param p
	 *            the project to fetch the stats for
	 */
    private void fetchStats(Project p) {
        AsyncCallback<Project> callback = new AsyncCallback<Project>() {

            @Override
            public void onFailure(Throwable caught) {
                System.err.println(caught.getClass().getName() + " :: " + caught.getMessage());
                Window.alert(FETCH_STATS_ERROR);
            }

            @Override
            public void onSuccess(Project p) {
                projects.put(p.getName(), p);
                RootPanel.get().clear();
                projectView = new ProjectView(PdpScanner.this, p);
                projectView.buildView();
                RootPanel.get().add(projectView.getViewPanel());
            }
        };
        projectService.fetchStats(p, callback);
    }

    /**
	 * Log the user in, and create or renew his session id
	 * 
	 * @param username
	 *            the user's username
	 * @param password
	 *            the user's password
	 */
    private void logIn(String username, String password) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                System.err.println(caught.getClass().getName() + " :: " + caught.getMessage());
                if (caught instanceof BadLoginException) {
                    Window.alert(BAD_LOGIN_ERROR);
                } else {
                    Window.alert(LOGIN_USER_ERROR);
                }
            }

            @Override
            public void onSuccess(String result) {
                loginSignupPopup.hide();
                initApp();
            }
        };
        userService.logIn(username, password, callback);
    }

    /**
	 * Sign a new user up
	 * 
	 * @param name
	 *            the new user's name
	 * @param username
	 *            the new user's username
	 * @param password
	 *            the new user's password
	 * @param cell
	 *            the new user's cell
	 * @param creationDate
	 *            the new user's account creation date
	 */
    public void signUp(String name, String email, final String username, final String password, String cell) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                System.err.println(caught.getClass().getName() + " :: " + caught.getMessage());
                if (caught instanceof BadUsernameException) {
                    Window.alert(BAD_USERNAME_ERROR_SIGNUP);
                } else if (caught instanceof BadPasswordLengthException) {
                    Window.alert(BAD_PASSWORD_LENGTH_ERROR);
                } else if (caught instanceof BadEmailException) {
                    Window.alert(BAD_EMAIL_ERROR);
                } else if (caught instanceof BadCellException) {
                    Window.alert(BAD_CELL_ERROR);
                } else if (caught instanceof RequiredFieldException) {
                    Window.alert(REQUIRED_FIELDS_ERROR);
                } else {
                    Window.alert(SIGNUP_USER_ERROR);
                }
            }

            @Override
            public void onSuccess(String result) {
                Window.alert(REGISTER_USER_SUCCESS);
                logIn(username, password);
            }
        };
        DateTimeFormat dateFormatter = new DateTimeFormat("yyyy-MM-dd HH:mm:ss") {
        };
        String creationDate = dateFormatter.format(new Date());
        userService.signUp(name, email, username, password, cell, creationDate, callback);
    }

    /**
	 * Show the login popup so the user can log in
	 */
    private void showLoginPopup() {
        final VerticalPanel popupContents = new VerticalPanel();
        final Label usernameLabel = new Label("Nom d'utilisateur *");
        final TextBox usernameTextBox = new TextBox();
        usernameTextBox.setMaxLength(255);
        final Label passwordLabel = new Label("Mot de passe *");
        final PasswordTextBox passwordTextBox = new PasswordTextBox();
        passwordTextBox.setMaxLength(255);
        final HorizontalPanel buttonPanel = new HorizontalPanel();
        final Button okButton = new Button("OK");
        final Button signupButton = new Button("Inscription");
        popupContents.add(usernameLabel);
        popupContents.add(usernameTextBox);
        popupContents.add(passwordLabel);
        popupContents.add(passwordTextBox);
        buttonPanel.add(okButton);
        buttonPanel.add(signupButton);
        buttonPanel.setSpacing(5);
        popupContents.add(buttonPanel);
        loginSignupPopup.setWidget(popupContents);
        okButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String username = usernameTextBox.getText().trim();
                String password = passwordTextBox.getText().trim();
                try {
                    Validator.validateLogin(username, password);
                    logIn(username, password);
                } catch (Exception caught) {
                    if (caught instanceof RequiredFieldException) {
                        Window.alert(PdpScanner.REQUIRED_FIELDS_ERROR);
                    } else {
                        Window.alert(LOGIN_USER_ERROR);
                    }
                }
            }
        });
        passwordTextBox.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == 13) {
                    okButton.click();
                }
            }
        });
        signupButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showSignupPopup();
            }
        });
        loginSignupPopup.center();
        usernameTextBox.setFocus(true);
    }

    /**
	 * Show the sign up popup so the user can sign up
	 */
    private void showSignupPopup() {
        final VerticalPanel popupContents = new VerticalPanel();
        final Label nameLabel = new Label("Nom *");
        final TextBox nameTextBox = new TextBox();
        nameTextBox.setMaxLength(255);
        final Label mailLabel = new Label("Adresse mail *");
        final TextBox mailTextBox = new TextBox();
        mailTextBox.setMaxLength(255);
        final Label usernameLabel = new Label("Nom d'utilisateur *");
        final TextBox usernameTextBox = new TextBox();
        usernameTextBox.setMaxLength(255);
        final Label passwordLabel = new Label("Mot de passe *");
        final PasswordTextBox passwordTextBox = new PasswordTextBox();
        passwordTextBox.setMaxLength(255);
        final Label confirmPasswordLabel = new Label("Confirmation du mot de passe *");
        final PasswordTextBox confirmPasswordTextBox = new PasswordTextBox();
        confirmPasswordTextBox.setMaxLength(255);
        final Label cellLabel = new Label("Numéro de mobile");
        final TextBox cellTextBox = new TextBox();
        cellTextBox.setMaxLength(16);
        final HorizontalPanel buttonPanel = new HorizontalPanel();
        final Button okButton = new Button("OK");
        final Button logInButton = new Button("Connexion");
        popupContents.add(nameLabel);
        popupContents.add(nameTextBox);
        popupContents.add(mailLabel);
        popupContents.add(mailTextBox);
        popupContents.add(cellLabel);
        popupContents.add(cellTextBox);
        popupContents.add(usernameLabel);
        popupContents.add(usernameTextBox);
        popupContents.add(passwordLabel);
        popupContents.add(passwordTextBox);
        popupContents.add(confirmPasswordLabel);
        popupContents.add(confirmPasswordTextBox);
        buttonPanel.add(okButton);
        buttonPanel.add(logInButton);
        buttonPanel.setSpacing(5);
        popupContents.add(buttonPanel);
        loginSignupPopup.setWidget(popupContents);
        okButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String name = nameTextBox.getText().trim();
                String email = mailTextBox.getText().trim();
                String username = usernameTextBox.getText().trim();
                String password = passwordTextBox.getText().trim();
                String confirmPassword = confirmPasswordTextBox.getText().trim();
                String cell = cellTextBox.getText().trim();
                try {
                    Validator.validateSignUp(name, email, username, password, confirmPassword, cell);
                    signUp(name, email, username, confirmPassword, cell);
                } catch (Exception caught) {
                    System.err.println(caught.getClass().getName() + " :: " + caught.getMessage());
                    if (caught instanceof RequiredFieldException) {
                        Window.alert(PdpScanner.REQUIRED_FIELDS_ERROR);
                    } else if (caught instanceof BadEmailException) {
                        Window.alert(PdpScanner.BAD_EMAIL_ERROR);
                    } else if (caught instanceof BadCellException) {
                        Window.alert(PdpScanner.BAD_CELL_ERROR);
                    } else if (caught instanceof BadPasswordLengthException) {
                        Window.alert(PdpScanner.BAD_PASSWORD_LENGTH_ERROR);
                    } else if (caught instanceof BadPasswordConfirmationException) {
                        Window.alert(PdpScanner.BAD_PASSWORD_CONFIRMATION_ERROR);
                    } else {
                        Window.alert(PdpScanner.SIGNUP_USER_ERROR);
                    }
                }
            }
        });
        confirmPasswordTextBox.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == 13) {
                    okButton.click();
                }
            }
        });
        logInButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showLoginPopup();
            }
        });
        loginSignupPopup.center();
        nameTextBox.setFocus(true);
    }

    /**
	 * Check with the server whether the session is still valid
	 */
    private void checkSessionValidity() {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                System.err.println(caught.getClass().getName() + " :: " + caught.getMessage());
                showLoginPopup();
            }

            @Override
            public void onSuccess(String result) {
                System.out.println("Session was validated on the server.");
                initApp();
            }
        };
        userService.checkSessionValidity(callback);
    }

    /**
	 * Log the user out
	 */
    private void logOut() {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                System.err.println(caught.getClass().getName() + " :: " + caught.getMessage());
                Window.alert(LOGOUT_USER_ERROR);
            }

            @Override
            public void onSuccess(String result) {
                closeApp();
                showLoginPopup();
            }
        };
        userService.logOut(callback);
    }

    /**
	 * Handle history events
	 */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getValue().equals("Home")) {
            RootPanel.get().clear();
            homeView = new HomeView();
            homeView.buildView();
            RootPanel.get().add(homeView.getViewPanel());
            homeView.fetchProjects();
        } else if (event.getValue().startsWith("Project~")) {
            String projectName = event.getValue().substring(8);
            Project p = projects.get(projectName);
            fetchStats(p);
        } else if (event.getValue().equals("Tips")) {
            RootPanel.get().clear();
            this.tipsView = new TipsView();
            this.tipsView.buildView();
            RootPanel.get().add(this.tipsView.getViewPanel());
        } else if (event.getValue().equals("Account")) {
            RootPanel.get().clear();
            this.accountView = new AccountView(this);
            this.accountView.buildView();
            RootPanel.get().add(this.accountView.getViewPanel());
        } else if (event.getValue().equals("Disconnected")) {
            logOut();
        } else {
            System.err.println("Unknown history state fired!");
        }
    }
}
