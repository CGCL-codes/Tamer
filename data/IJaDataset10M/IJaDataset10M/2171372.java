package com.darrylsite.ihm;

import com.darrylsite.core.MyEntityManager;
import com.darrylsite.entite.Message;
import com.darrylsite.entite.MsgStatus.Status;
import com.darrylsite.entite.TimedMessage;
import com.darrylsite.entite.TimedMessages;
import com.darrylsite.listener.MessageSentListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author nabster
 */
public class SenderIHM extends javax.swing.JPanel {

    String phoneNumberRegex = "(\\+|0{2})?[0-9]*";

    String dateregex = "[0-9]{2}/[0-9]{2}/[0-9]{2,4}";

    String hourRegex = "[0-9]{1,2}:[0-9]{1,2}";

    private List<String> numbers = new ArrayList<String>();

    private List<MessageSentListener> messageListener;

    /** Creates new form SenderIHM */
    public SenderIHM() {
        initComponents();
        init();
        dtSendDate.setDate(Calendar.getInstance().getTime());
    }

    public SenderIHM(String number, Message msg) {
        initComponents();
        init();
        dtSendDate.setDate(Calendar.getInstance().getTime());
        this.txtMessage.setText(msg.getMessage());
        this.txtNumbers.setText(number);
        Date date = msg.getDates();
        this.dtSendDate.setDate(date);
    }

    private void init() {
        messageListener = new ArrayList<MessageSentListener>();
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        this.txtSendHour.setText(hour + ":" + min);
        this.dtSendDate.setCalendar(cal);
    }

    public void addSentMesssageListener(MessageSentListener l) {
        messageListener.add(l);
    }

    public void removeSentMessageListener(MessageSentListener l) {
        messageListener.remove(l);
    }

    public void fireSentMessageListener(Message msg) {
        for (MessageSentListener l : messageListener) {
            l.messageSent(msg);
        }
    }

    public void processMessage() {
        if (lstReceiver.getModel().getSize() < 1 || txtMessage.getText() == null || txtMessage.getText().isEmpty()) {
            return;
        }
        Message msg = new Message();
        msg.setMessage(txtMessage.getText());
        String hourStr = txtSendHour.getText();
        String hour = hourStr.split(":")[0];
        String minute = hourStr.split(":")[1];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dtSendDate.getDate());
        calendar.set(calendar.get(Calendar.YEAR), Calendar.MONTH, Calendar.DATE, Integer.parseInt(hour), Integer.parseInt(minute));
        msg.setDates(calendar.getTime());
        for (String s : numbers) {
            msg.setNumero(s);
            fireSentMessageListener(msg);
        }
    }

    private void addNumber() {
        String n = this.txtNumbers.getText();
        if (n.length() < 1) return;
        Pattern p = Pattern.compile(phoneNumberRegex);
        if (p.matcher(n).matches()) {
            numbers.add(n);
            txtNumbers.setText("");
            lstReceiver.setListData(numbers.toArray());
        }
    }

    private void processAddcontact() {
        ContactChooser ch = new ContactChooser(Singame.Application, true);
        ch.setVisible(true);
        List<String> nbs = ch.getSelectedContact();
        if (nbs == null) return;
        for (String s : nbs) numbers.add(s);
        lstReceiver.setListData(numbers.toArray());
    }

    private void send() {
        int max = lstReceiver.getModel().getSize();
        EntityManager em = MyEntityManager.emf.createEntityManager();
        em.getTransaction().begin();
        String mess = this.txtMessage.getText();
        Date date = this.dtSendDate.getDate();
        String hour = this.txtSendHour.getText();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.split(":")[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(hour.split(":")[1]));
        Query query = em.createQuery("select m from TimedMessages m");
        List<TimedMessages> messageContainers = query.getResultList();
        TimedMessages messageContainer = (TimedMessages) messageContainers.get(0);
        for (int i = 0; i < max; i++) {
            String number = (String) lstReceiver.getModel().getElementAt(i);
            TimedMessage msg = new TimedMessage();
            msg.setMessage(mess);
            msg.setNumero(number);
            msg.setDates(cal.getTime());
            msg.setStatus(Status.unsent);
            em.persist(msg);
            messageContainer.addMessage(msg);
        }
        em.flush();
        em.getTransaction().commit();
        Singame.Application.showToSentIHM();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        txtNumbers = new javax.swing.JTextField();
        btAdd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstReceiver = new javax.swing.JList();
        lnkAddContact = new com.l2fprod.common.swing.JLinkButton();
        dtSendDate = new com.toedter.calendar.JDateChooser();
        txtSendHour = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        btSend = new javax.swing.JButton();
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabel1.setText("Numero");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 70, -1));
        txtNumbers.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumbersKeyReleased(evt);
            }
        });
        add(txtNumbers, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 220, -1));
        btAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add.png")));
        btAdd.setText("Ajouter");
        btAdd.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btAddMouseClicked(evt);
            }
        });
        add(btAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, -1, -1));
        lstReceiver.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                lstReceiverKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(lstReceiver);
        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 280, 80));
        lnkAddContact.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/brick_link.png")));
        lnkAddContact.setText("Ajouter des contact ou un groupe");
        lnkAddContact.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lnkAddContactMouseClicked(evt);
            }
        });
        add(lnkAddContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, -1, -1));
        dtSendDate.setDateFormatString("dd/MM/yyyy");
        add(dtSendDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 160, -1));
        txtSendHour.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));
        txtSendHour.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSendHour.setText("00:00");
        add(txtSendHour, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, 110, -1));
        jLabel2.setText("Date");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));
        jLabel3.setText("Heure");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 180, 40, -1));
        txtMessage.setColumns(20);
        txtMessage.setRows(5);
        jScrollPane2.setViewportView(txtMessage);
        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 530, 130));
        btSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tosend.png")));
        btSend.setText("Envoyer");
        btSend.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSendActionPerformed(evt);
            }
        });
        add(btSend, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, -1, -1));
    }

    private void btAddMouseClicked(java.awt.event.MouseEvent evt) {
        addNumber();
    }

    private void txtNumbersKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            addNumber();
        }
    }

    private void lstReceiverKeyTyped(java.awt.event.KeyEvent evt) {
        if ((lstReceiver.getSelectedIndices().length == 0) || (evt.getKeyChar() != KeyEvent.VK_DELETE)) {
            return;
        }
        int[] indices = lstReceiver.getSelectedIndices();
        int dec = 0;
        for (int i = 0; i < indices.length; i++) {
            numbers.remove(indices[i] - dec);
            dec++;
        }
        lstReceiver.setListData(numbers.toArray(new String[0]));
    }

    private void lnkAddContactMouseClicked(java.awt.event.MouseEvent evt) {
        processAddcontact();
    }

    private void btSendActionPerformed(java.awt.event.ActionEvent evt) {
        send();
    }

    private javax.swing.JButton btAdd;

    private javax.swing.JButton btSend;

    private com.toedter.calendar.JDateChooser dtSendDate;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private com.l2fprod.common.swing.JLinkButton lnkAddContact;

    private javax.swing.JList lstReceiver;

    private javax.swing.JTextArea txtMessage;

    private javax.swing.JTextField txtNumbers;

    private javax.swing.JFormattedTextField txtSendHour;
}
