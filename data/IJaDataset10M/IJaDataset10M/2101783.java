package org.colombbus.tangara.ide.view;

import java.awt.Rectangle;
import javax.swing.JOptionPane;
import org.colombbus.tangara.commons.resinject.ObjectResource;
import org.colombbus.tangara.commons.resinject.ResourceFactory;
import org.colombbus.tangara.commons.resinject.UIHelper;
import org.colombbus.tangara.ide.controller.CodeEditor;
import org.colombbus.tangara.ide.controller.DisplayConsole;
import org.colombbus.tangara.ide.controller.GraphicDisplay;
import org.colombbus.tangara.ide.controller.MainController;
import org.colombbus.tangara.ide.controller.MainDisplay;
import org.colombbus.tangara.ide.model.ApplicationProfile;
import org.colombbus.tangara.ide.model.MessageListener;
import org.colombbus.tangara.ide.model.MainModel;

public class MainViewImpl implements MainDisplay {

    private MainModel model;

    private MainController controller;

    private ObjectResource resources;

    private ActionManagerImpl actionMgr;

    private ProfileView profileView;

    private MainFrame frame;

    public MainViewImpl() {
        DisplayCommand.initialize();
        resources = ResourceFactory.getObjectResource(this);
    }

    @Override
    public void setMainModel(MainModel model) {
        this.model = model;
    }

    @Override
    public void setMainController(MainController controller) {
        this.controller = controller;
    }

    @Override
    public void showMainDisplay() {
        if (frame == null) {
            buildUI();
        }
        frame.setVisible(true);
    }

    private void buildUI() {
        initializeActionManager();
        frame = new MainFrame(getProfileView().getRootPane());
        frame.setController(controller);
        frame.pack();
        Rectangle bounds = model.getMainDisplayBounds();
        frame.setBounds(bounds);
    }

    private void initializeActionManager() {
        actionMgr = new ActionManagerImpl();
        actionMgr.setMainController(controller);
        actionMgr.setMainDisplay(this);
    }

    private ProfileView getProfileView() {
        if (profileView == null) {
            ProfileViewFactory profileFactory = new ProfileViewFactory();
            profileFactory.setController(controller);
            profileFactory.setModel(model);
            profileFactory.setActionManager(actionMgr);
            ApplicationProfile profile = model.getApplicationProfile();
            profileView = profileFactory.getProfileView(profile);
        }
        return profileView;
    }

    @Override
    public void hideDisplay() {
        frame.setVisible(false);
    }

    @Override
    public void disposeDisplay() {
        frame.setVisible(false);
        frame.dispose();
    }

    @Override
    public boolean confirmExitApplication() {
        String title = resources.getString("confirmExitApplication.title");
        String message = resources.getString("confirmExitApplication.message");
        int optionType = JOptionPane.YES_NO_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        int choice = JOptionPane.showConfirmDialog(frame, message, title, optionType, messageType);
        boolean confirmed = choice == JOptionPane.YES_OPTION;
        return confirmed;
    }

    @Override
    public void showAboutApplicationDialog() {
        AboutWindow aboutWindow = new AboutWindow(frame);
        aboutWindow.setVisible(true);
    }

    @Override
    public GraphicDisplay getGraphicDisplay() {
        GraphicDisplay graphicDisplay = profileView.getGraphicDisplay();
        return graphicDisplay;
    }

    @Override
    public DisplayConsole getConsole() {
        DisplayConsole console = profileView.getConsole();
        return console;
    }

    @Override
    public MessageListener getConsoleListener() {
        MessageListener console = profileView.getConsoleListener();
        return console;
    }

    @Override
    public CodeEditor getCodeEditor() {
        CodeEditor codeEditor = profileView.getCodeEditor();
        return codeEditor;
    }

    @Override
    public void showNotImplemented() {
        UIHelper.showNotImplementedDialog();
    }
}
