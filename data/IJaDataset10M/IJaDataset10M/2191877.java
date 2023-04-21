package eu.more.diaball.messageviewer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import eu.more.diaball.interfaces.Message;
import eu.more.diaball.interfaces.MessageListener;
import eu.more.diaball.util.LoginCustomizer;
import eu.more.diaball.util.MessageReceiverCustomizer;

public class MessageViewerActivator implements BundleActivator {

    ServiceTracker loginTracker = null;

    ServiceTracker messageReceiverTracker = null;

    LoginCustomizer loginCustomizer = null;

    MessageReceiverCustomizer messageReceiverCustomizer = null;

    MessageViewerUIThread messageViewerUIThread = null;

    public void start(BundleContext context) throws Exception {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        System.out.println("Message Viewer UI Starting");
        loginCustomizer = new LoginCustomizer(context);
        loginTracker = new ServiceTracker(context, eu.more.diaball.interfaces.LoginInterface.class.getName(), loginCustomizer);
        loginTracker.open();
        MessageListener messageListener = new MessageListener() {

            public void newMessage(Message message) {
                System.err.println("Measurement has arrived: " + message);
                if (messageViewerUIThread != null) {
                    messageViewerUIThread.newMessage(message);
                }
            }
        };
        messageReceiverCustomizer = new MessageReceiverCustomizer(context, messageListener);
        messageReceiverTracker = new ServiceTracker(context, eu.more.diaball.interfaces.MessageProvider.class.getName(), messageReceiverCustomizer);
        messageReceiverTracker.open();
        messageViewerUIThread = new MessageViewerUIThread(messageReceiverCustomizer, loginCustomizer);
        messageViewerUIThread.start();
    }

    public void stop(BundleContext context) throws Exception {
        messageViewerUIThread.end();
        loginTracker.close();
        messageReceiverTracker.close();
    }
}
