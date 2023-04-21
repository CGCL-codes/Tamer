package net.sf.dpdesktop.module.submit;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.MalformedURLException;
import net.sf.dpdesktop.gui.submit.LogPane;
import net.sf.dpdesktop.gui.TrackingPane;
import net.sf.dpdesktop.gui.submit.ApplicationClosedBar;
import net.sf.dpdesktop.gui.submit.ContainerChangedBar;
import net.sf.dpdesktop.gui.submit.DefaultBar;
import net.sf.dpdesktop.gui.submit.DefaultSubmitDialog;
import net.sf.dpdesktop.module.message.MessageModel;
import net.sf.dpdesktop.module.guistate.ApplicationStateListener;
import net.sf.dpdesktop.module.guistate.ApplicationStateModel;
import net.sf.dpdesktop.module.guistate.VetoableExitListener;
import net.sf.dpdesktop.module.settings.LanguageModel;
import net.sf.dpdesktop.module.tracking.Tracker;
import net.sf.dpdesktop.module.tracking.TrackingController;
import net.sf.dpdesktop.module.tracking.TrackingManager;
import net.sf.dpdesktop.module.tracking.VetoableContainerChangedListener;
import net.sf.dpdesktop.module.update.UpdateController;
import net.sf.dpdesktop.service.ContextProvider;
import net.sf.dpdesktop.service.ServiceAuthException;
import net.sf.dpdesktop.service.ServiceDataException;
import net.sf.dpdesktop.service.container.Container;
import net.sf.dpdesktop.service.log.LogItem;
import net.sf.dpdesktop.service.log.LogItemService;
import net.sf.dpdesktop.service.log.LogItemServiceFactory;

/**
 *
 * @author Heiner Reinhardt
 */
public class SubmitController implements SubmitListener, VetoableExitListener, VetoableContainerChangedListener {

    private final DefaultSubmitDialog m_defaultSubmitDialog;

    private final LogPane m_logPane;

    private final TrackingManager m_trackingManager;

    private final DefaultBar m_defaultBar;

    private final ContextProvider m_contextProvider;

    private final UpdateController m_updateController;

    private final MessageModel m_messagModel;

    private final LogItemServiceFactory m_logItemServiceFactory;

    private final LanguageModel m_languageModel;

    private final SubmitHandler m_submitHandler;

    @Inject
    public SubmitController(DefaultSubmitDialog defaultSubmitDialog, DefaultBar defaultBar, LogPane logPane, TrackingPane trackingPane, final ApplicationStateModel applicationStateModel, ContextProvider contextProvider, UpdateController updateController, MessageModel messageModel, LogItemServiceFactory logItemServiceFactory, TrackingManager trackingManager, LanguageModel languageModel, SubmitHandler submitHandler) {
        this.m_submitHandler = submitHandler;
        this.m_logPane = logPane;
        this.m_languageModel = languageModel;
        this.m_defaultSubmitDialog = defaultSubmitDialog;
        this.m_trackingManager = trackingManager;
        this.m_defaultBar = defaultBar;
        this.m_contextProvider = contextProvider;
        this.m_updateController = updateController;
        this.m_messagModel = messageModel;
        this.m_logItemServiceFactory = logItemServiceFactory;
        m_trackingManager.addVetoableContainerChangedListener(this);
        defaultSubmitDialog.setLogPane(logPane);
        applicationStateModel.addVetoableExitListener(this);
        trackingPane.addSubmitListener(new SubmitListener() {

            @Override
            public boolean submit() {
                m_trackingManager.stop();
                SubmitController.this.m_defaultSubmitDialog.setButtonBar(SubmitController.this.m_defaultBar);
                SubmitController.this.m_defaultSubmitDialog.setVisible(true);
                m_trackingManager.start();
                return true;
            }
        });
        defaultBar.setCloseListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SubmitController.this.m_defaultSubmitDialog.setVisible(false);
            }
        });
        defaultBar.addSubmitListener(this);
    }

    @Override
    public boolean submit() {
        return m_submitHandler.submit(m_logItemServiceFactory, m_trackingManager, m_messagModel, m_logPane, m_contextProvider, m_updateController, m_languageModel.getString("SubmitController.message.submitFailed.headline"));
    }

    private class IsExitingAllowedController {

        private volatile boolean isExitingAllowed = false;

        private IsExitingAllowedController() {
            ApplicationClosedBar applicationClosedBar = new ApplicationClosedBar(m_languageModel);
            m_defaultSubmitDialog.setButtonBar(applicationClosedBar);
            applicationClosedBar.setCancelListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    isExitingAllowed = false;
                    m_defaultSubmitDialog.setVisible(false);
                }
            });
            applicationClosedBar.setQuitListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    isExitingAllowed = true;
                    m_defaultSubmitDialog.setVisible(false);
                }
            });
            applicationClosedBar.addSubmitListener(new SubmitListener() {

                @Override
                public boolean submit() {
                    if (SubmitController.this.submit()) {
                        isExitingAllowed = true;
                    } else {
                        isExitingAllowed = false;
                        m_defaultSubmitDialog.setVisible(true);
                    }
                    return true;
                }
            });
        }

        private boolean isExitingAllowed() {
            return isExitingAllowed;
        }
    }

    @Override
    public boolean isExitingAllowed() {
        if (m_trackingManager.isCleared()) {
            return true;
        }
        if (m_trackingManager.isUnready()) {
            return m_messagModel.question(m_languageModel.getString("SubmitController.question.exitVeto.headline"), m_languageModel.getString("SubmitController.question.exitVeto.message"));
        }
        IsExitingAllowedController isExitingAllowedController = new IsExitingAllowedController();
        m_defaultSubmitDialog.setVisible(true);
        return isExitingAllowedController.isExitingAllowed();
    }

    private class IsContainerChangeAllowedController {

        private boolean isContainerChangeAllowed = true;

        public IsContainerChangeAllowedController() {
            ContainerChangedBar containerChangedBar = new ContainerChangedBar(m_languageModel);
            containerChangedBar.setCloseListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    isContainerChangeAllowed = false;
                    m_defaultSubmitDialog.setVisible(false);
                }
            });
            containerChangedBar.setJustSwitchListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    isContainerChangeAllowed = true;
                    m_defaultSubmitDialog.setVisible(false);
                }
            });
            containerChangedBar.addSubmitListener(new SubmitListener() {

                @Override
                public boolean submit() {
                    if (SubmitController.this.submit()) {
                        isContainerChangeAllowed = true;
                    }
                    isContainerChangeAllowed = false;
                    m_defaultSubmitDialog.setVisible(false);
                    return true;
                }
            });
            m_defaultSubmitDialog.setButtonBar(containerChangedBar);
            m_defaultSubmitDialog.setVisible(true);
        }

        private boolean isContainerChangeAllowed() {
            return isContainerChangeAllowed;
        }
    }

    @Override
    public boolean isContainerChangeAllowed(Container container) {
        if (m_trackingManager.isCleared() || m_trackingManager.isUnready()) {
            return true;
        }
        return new IsContainerChangeAllowedController().isContainerChangeAllowed();
    }
}
