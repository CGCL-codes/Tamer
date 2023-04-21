package eclient;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import net.sf.eBus.client.EBus;
import net.sf.eBus.client.ECondition;
import net.sf.eBus.client.ESubscriber;
import net.sf.eBus.messages.EAddress;
import net.sf.eBus.messages.FeedStatus;
import net.sf.eBus.messages.Message;
import net.sf.eBus.messages.Message.MessageType;
import net.sf.eBus.messages.MessageId;
import net.sf.eBus.messages.MessageKey;
import net.sf.eBus.messages.Status;
import net.sf.eBus.messages.Version;
import net.sf.eBus.xml.XmlTag;
import net.sf.eBusx.ui.StatusLabel;

/**
 * The subscriber client frame.
 *
 * @author <a href="mailto:rapp@acm.org">Charles Rapp</a>
 */
public class SubscriberFrame extends EFrame implements ESubscriber {

    /**
     * Creates new form SubscriberFrame using default
     * configuration.
     * @param frameIndex The unique frame index.
     */
    public SubscriberFrame(final int frameIndex) {
        super(frameIndex, (FRAME_NAME + Integer.toString(frameIndex)), FrameType.SUBSCRIBER, DEFAULT_LOCATION, DEFAULT_SIZE);
        _msgKey = null;
        _condition = null;
        _subId = NO_ID;
        initComponents();
        _publishersList.setModel(new DefaultListModel());
        _status = new StatusLabel(_statusLabel);
    }

    /**
     * Creates a subscriber frame from the stored XML.
     * @param frameIndex The unique frame index.
     * @param rootTag Extract the configuration from here.
     * @param saveFile Save the configuration to this file.
     */
    public SubscriberFrame(final int frameIndex, final XmlTag rootTag, final File saveFile) {
        super(frameIndex, rootTag, saveFile);
        final XmlTag tag = (rootTag.tagValues(KEY_TAG)).get(0);
        _msgKey = new MessageKey(new MessageId(tag.attribute(NAME_ATTRIBUTE), new Version(tag.attribute(MSG_VERSION_ATTRIBUTE))), tag.attribute(SUBJECT_ATTRIBUTE));
        _subId = NO_ID;
        initComponents();
        _publishersList.setModel(new DefaultListModel());
        _status = new StatusLabel(_statusLabel);
    }

    /**
     * Returns the XML tags containing this frame's
     * configuration.
     * @return the XML configuration tags.
     */
    @Override
    public List<XmlTag> save() {
        final MessageId id = _msgKey.id();
        final Map<String, String> attributes = new HashMap<String, String>();
        final List<XmlTag> retval = new ArrayList<XmlTag>();
        attributes.put(NAME_ATTRIBUTE, id.name());
        attributes.put(MSG_VERSION_ATTRIBUTE, (id.version()).toString());
        attributes.put(SUBJECT_ATTRIBUTE, _msgKey.subject());
        retval.add(new XmlTag(KEY_TAG, attributes, null));
        return (retval);
    }

    /**
     * Retracts subscription before closing this frame.
     */
    @Override
    public void close() {
        _mutex.lock();
        try {
            if (_subId >= 0) {
                unsubscribe();
            }
        } finally {
            _mutex.unlock();
        }
        undisplay();
        return;
    }

    /**
     * Cancels the subscription.
     */
    @Override
    public void doClose() {
        _mutex.lock();
        try {
            if (_subId >= 0) {
                unsubscribe();
            }
        } finally {
            _mutex.unlock();
        }
        undisplay();
        return;
    }

    /**
     * Displays the open dialog to get the subscription message
     * key.
     */
    @Override
    public void doMessageKey() {
        final OpenSubscribeDialog dialog = new OpenSubscribeDialog(this, "Subscribe Dialog", true, MessageType.NOTIFICATION);
        dialog.display();
        if (dialog.isOK() == true) {
            final MessageKey key = dialog.messageKey();
            if (_msgKey == null || _msgKey.equals(key) == false) {
                _mutex.lock();
                try {
                    if (_subId >= 0) {
                        unsubscribe();
                    }
                    _msgKey = key;
                    _condition = dialog.condition();
                    subscribe();
                } catch (Exception jex) {
                    final StringWriter sw = new StringWriter();
                    final PrintWriter pw = new PrintWriter(sw);
                    final String reason = jex.getMessage();
                    pw.print(key);
                    pw.print(" subscription failed - ");
                    if (reason == null || reason.length() == 0) {
                        pw.print("no reason given.");
                    } else {
                        pw.print(reason);
                        pw.print('.');
                    }
                    _status.setText(sw.toString());
                    _msgKey = null;
                    _subId = NO_ID;
                } finally {
                    _mutex.unlock();
                }
            }
        }
        return;
    }

    /**
     * Displays the subscription reply status.
     * @param requestId The subscription identifier.
     * @param status The subscription status.
     * @param reason Text explaining an error status.
     */
    public void subscribeReply(final short requestId, final Status status, final String reason) {
        _mutex.lock();
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            String message;
            pw.print(_msgKey);
            pw.print(" subscription ");
            if (status == Status.OK) {
                pw.print("succeeded.");
            } else {
                pw.print("failed, ");
                if (reason == null || reason.length() == 0) {
                    pw.print("no reason given.");
                } else {
                    pw.print(reason);
                    pw.print('.');
                }
            }
            message = sw.toString();
            SwingUtilities.invokeLater(new MessageUpdate(message, _status));
        } finally {
            _mutex.unlock();
        }
        return;
    }

    /**
     * Display the subscription's feed status.
     * @param requestId The subscription identifier.
     * @param messageKey The subscribe message key.
     * @param feedStatus The subscription's feed status.
     * @param publisher Add or remove this publisher.
     * @param publishers The list of all publishers.
     */
    public void feedStatus(final short requestId, final MessageKey messageKey, final FeedStatus feedStatus, final EAddress publisher, final Collection<EAddress> publishers) {
        _mutex.lock();
        try {
            if (_subId == NO_ID) {
            } else if (requestId != _subId) {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                pw.print("Feed status for unknown subscription (");
                pw.print(requestId);
                pw.print(").");
                SwingUtilities.invokeLater(new MessageUpdate(sw.toString(), _status));
            } else {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                final DefaultListModel model = (DefaultListModel) _publishersList.getModel();
                PublisherUpdate update;
                if (feedStatus == FeedStatus.UP) {
                    pw.print(_msgKey);
                    pw.print(" feed is up, publisher is ");
                    pw.print(publisher);
                    pw.print('.');
                    update = new PublisherUpdate(UpdateType.ADD_ALL, publishers, model);
                } else if (feedStatus == FeedStatus.DOWN) {
                    pw.print(_msgKey);
                    pw.print(" feed is down.");
                    update = new PublisherUpdate(UpdateType.CLEAR, (Collection<EAddress>) null, model);
                } else if (feedStatus == FeedStatus.ADD) {
                    pw.print("New publisher ");
                    pw.print(publisher);
                    pw.print(" added for ");
                    pw.print(_msgKey);
                    pw.print('.');
                    update = new PublisherUpdate(UpdateType.ADD, publisher, model);
                } else {
                    pw.print("Publisher ");
                    pw.print(publisher);
                    pw.print(" removed for ");
                    pw.print(_msgKey);
                    pw.print('.');
                    update = new PublisherUpdate(UpdateType.REMOVE, publisher, model);
                }
                SwingUtilities.invokeLater(new MessageUpdate(sw.toString(), _status));
                SwingUtilities.invokeLater(update);
            }
        } finally {
            _mutex.unlock();
        }
        return;
    }

    /**
     * Displays the latest notification.
     * @param subject The notification subject.
     * @param msg The notification message.
     */
    public void notify(final String subject, final Message msg) {
        _mutex.lock();
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            pw.println(msg);
            SwingUtilities.invokeLater(new OutputUpdate(sw.toString(), _outputTextArea));
        } finally {
            _mutex.unlock();
        }
        return;
    }

    /**
     * Makes this frame visible.
     */
    @Override
    public void display() {
        super.display();
        if (_msgKey != null) {
            _mutex.lock();
            try {
                subscribe();
            } catch (Exception jex) {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                final String reason = jex.getMessage();
                pw.print(_msgKey);
                pw.print(" subscription failed - ");
                if (reason == null || reason.length() == 0) {
                    pw.print("no reason given.");
                } else {
                    pw.print(reason);
                    pw.print('.');
                }
                _status.setText(sw.toString());
                _msgKey = null;
                _subId = NO_ID;
            } finally {
                _mutex.unlock();
            }
        }
        return;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        _publishersPanel = new javax.swing.JPanel();
        _publishersLabel = new javax.swing.JLabel();
        _publisherScrollPane = new javax.swing.JScrollPane();
        _publishersList = new javax.swing.JList();
        _outputScrollPane = new javax.swing.JScrollPane();
        _outputTextArea = new javax.swing.JTextArea();
        _statusLabel = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("Form");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                onClose(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                updateSize(evt);
            }

            public void componentMoved(java.awt.event.ComponentEvent evt) {
                updateLocation(evt);
            }
        });
        _publishersPanel.setName("_publishersPanel");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(eclient.EclientApp.class).getContext().getResourceMap(SubscriberFrame.class);
        _publishersLabel.setFont(resourceMap.getFont("_publishersLabel.font"));
        _publishersLabel.setText(resourceMap.getString("_publishersLabel.text"));
        _publishersLabel.setName("_publishersLabel");
        _publisherScrollPane.setName("_publisherScrollPane");
        _publishersList.setName("_publishersList");
        _publisherScrollPane.setViewportView(_publishersList);
        _outputScrollPane.setName("_outputScrollPane");
        _outputTextArea.setColumns(20);
        _outputTextArea.setFont(resourceMap.getFont("_outputTextArea.font"));
        _outputTextArea.setRows(5);
        _outputTextArea.setName("_outputTextArea");
        _outputScrollPane.setViewportView(_outputTextArea);
        javax.swing.GroupLayout _publishersPanelLayout = new javax.swing.GroupLayout(_publishersPanel);
        _publishersPanel.setLayout(_publishersPanelLayout);
        _publishersPanelLayout.setHorizontalGroup(_publishersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(_publishersPanelLayout.createSequentialGroup().addContainerGap().addGroup(_publishersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(_outputScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, _publishersPanelLayout.createSequentialGroup().addComponent(_publishersLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(_publisherScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))).addContainerGap()));
        _publishersPanelLayout.setVerticalGroup(_publishersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(_publishersPanelLayout.createSequentialGroup().addContainerGap().addGroup(_publishersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_publishersLabel).addComponent(_publisherScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(_outputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE).addContainerGap()));
        _statusLabel.setFont(resourceMap.getFont("_statusLabel.font"));
        _statusLabel.setText(resourceMap.getString("_statusLabel.text"));
        _statusLabel.setName("_statusLabel");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_publishersPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(6, 6, 6).addComponent(_statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(_publishersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE).addComponent(_statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void onClose(final WindowEvent evt) {
        _mutex.lock();
        try {
            if (_subId >= 0) {
                unsubscribe();
            }
        } finally {
            _mutex.unlock();
        }
        return;
    }

    private void subscribe() {
        _status.setText("Subscribing to " + _msgKey + ".");
        _subId = EBus.subscribe(_msgKey, _condition, this);
        enableSave();
        return;
    }

    private void unsubscribe() {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        final DefaultListModel model = (DefaultListModel) _publishersList.getModel();
        pw.print("Unsubscribed from ");
        pw.print(_msgKey);
        pw.print('.');
        EBus.unsubscribe(_subId, this);
        _subId = NO_ID;
        _msgKey = null;
        model.removeAllElements();
        _status.setText(sw.toString());
        return;
    }

    /**
     * Main routine used for testing this component.
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new SubscriberFrame(0).setVisible(true);
            }
        });
    }

    private enum UpdateType {

        ADD, REMOVE, ADD_ALL, CLEAR
    }

    private javax.swing.JScrollPane _outputScrollPane;

    private javax.swing.JTextArea _outputTextArea;

    private javax.swing.JScrollPane _publisherScrollPane;

    private javax.swing.JLabel _publishersLabel;

    private javax.swing.JList _publishersList;

    private javax.swing.JPanel _publishersPanel;

    private javax.swing.JLabel _statusLabel;

    private MessageKey _msgKey;

    private ECondition _condition;

    private short _subId;

    private StatusLabel _status;

    private static final long serialVersionUID = 0x010000L;

    private static final short NO_ID = -1;

    private static final String FRAME_NAME = "Subscriber ";

    private static final Point DEFAULT_LOCATION = new Point(200, 200);

    private static final Dimension DEFAULT_SIZE = new Dimension(500, 600);

    private final class PublisherUpdate implements Runnable {

        private PublisherUpdate(final UpdateType updateType, final EAddress publisher, final DefaultListModel model) {
            _updateType = updateType;
            _publisher = publisher;
            _publishers = null;
            _model = model;
        }

        private PublisherUpdate(final UpdateType updateType, final Collection<EAddress> publishers, final DefaultListModel model) {
            _updateType = updateType;
            _publisher = null;
            _publishers = publishers;
            _model = model;
        }

        public void run() {
            try {
                if (_updateType == UpdateType.ADD) {
                    _model.addElement(_publisher);
                } else if (_updateType == UpdateType.REMOVE) {
                    _model.removeElement(_publisher);
                } else if (_updateType == UpdateType.ADD_ALL) {
                    for (EAddress publisher : _publishers) {
                        _model.addElement(publisher);
                    }
                } else {
                    _model.removeAllElements();
                }
            } catch (Exception jex) {
            }
            return;
        }

        private final UpdateType _updateType;

        private final EAddress _publisher;

        private Collection<EAddress> _publishers;

        private final DefaultListModel _model;
    }
}
