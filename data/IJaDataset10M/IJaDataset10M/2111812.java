package com.tinywebgears.webapp;

import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class WebApplication extends Application implements ApplicationContext.TransactionListener {

    private static ThreadLocal<WebApplication> currentApplication = new ThreadLocal<WebApplication>();

    private final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    private Window mainWindow;

    private HorizontalLayout headerLayout;

    private VerticalLayout mainLayout;

    private VerticalLayout browserLayout;

    public WebApplication() {
    }

    /**
     * This method is called by Vaadin application manager once the application is accessed by user.
     */
    public void init() {
        setTheme("tinywebgears");
        getContext().addTransactionListener(this);
    }

    /**
     * See @ApplicationContext.TransactionListener
     */
    public void transactionStart(Application application, Object servletRequest) {
        if (application == WebApplication.this) {
            currentApplication.set(this);
            initUiComponents();
        }
    }

    /**
     * See @ApplicationContext.TransactionListener
     */
    public void transactionEnd(Application application, Object o) {
    }

    /**
     * This method is called by Vaadin once the application is closed. This will be called on every request when
     * application is run on GAE.
     */
    @Override
    public void close() {
        logger.trace("Application is closing....");
        super.close();
    }

    /**
     * This method is used by internal components to access global resources/services.
     * 
     * @return The current instance of web application
     */
    public static WebApplication getInstance() {
        return currentApplication.get();
    }

    public void showMessage(String description) {
        if (mainWindow != null) mainWindow.showNotification("", description, Notification.TYPE_HUMANIZED_MESSAGE);
    }

    private void initUiComponents() {
        mainWindow = new Window("Simple GAE Application using Vaadin");
        mainWindow.setSizeFull();
        mainWindow.getContent().setSizeFull();
        setMainWindow(mainWindow);
        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setSizeFull();
        mainWindow.addComponent(mainLayout);
        headerLayout = new HorizontalLayout();
        headerLayout.setSpacing(true);
        Button tuataraButton = new Button("Tuatara");
        Button blogButton = new Button("Blog");
        Button samplesButton = new Button("Samples");
        headerLayout.addComponent(tuataraButton);
        headerLayout.setComponentAlignment(tuataraButton, Alignment.MIDDLE_LEFT);
        headerLayout.addComponent(blogButton);
        headerLayout.setComponentAlignment(blogButton, Alignment.MIDDLE_LEFT);
        headerLayout.addComponent(samplesButton);
        headerLayout.setComponentAlignment(samplesButton, Alignment.MIDDLE_LEFT);
        mainLayout.addComponent(headerLayout);
        browserLayout = new VerticalLayout();
        browserLayout.setSizeFull();
        mainLayout.addComponent(browserLayout);
        mainLayout.setExpandRatio(browserLayout, 1.0f);
        loadTuatara();
        tuataraButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                loadTuatara();
            }
        });
        blogButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                loadBlog();
            }
        });
        samplesButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                loadSamples();
            }
        });
    }

    private void loadTuatara() {
        loadUrl("http://tuatara-addressbook.appspot.com/");
    }

    private void loadBlog() {
        loadUrl("http://beansgocrazy.blogspot.com/");
    }

    private void loadSamples() {
        loadUrl("http://code.google.com/p/tinywebgears-samples/source/browse/trunk/");
    }

    private void loadUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            Embedded browser = new Embedded("", new ExternalResource(url));
            browser.setType(Embedded.TYPE_BROWSER);
            browser.setSizeFull();
            browserLayout.removeAllComponents();
            browserLayout.addComponent(browser);
        } catch (MalformedURLException e) {
        }
    }
}
