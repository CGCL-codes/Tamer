package net.suberic.pooka.gui;

import net.suberic.pooka.Pooka;
import net.suberic.pooka.AddressBookEntry;
import net.suberic.pooka.AddressMatcher;
import net.suberic.util.gui.IconManager;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.mail.internet.InternetAddress;

/**
 *<p> This is a JTextArea which uses an AddressMatcher to fill in completed
 * addresses.  It also will store 
 */
public class AddressEntryTextArea extends net.suberic.util.swing.EntryTextArea implements java.awt.event.FocusListener {

    static Thread updateThread;

    boolean useCompletion = false;

    static java.util.WeakHashMap areaList = new java.util.WeakHashMap();

    static int ADDRESS_MATCH = 0;

    static int VALID = 1;

    static int INVALID = 2;

    LinkedList addressList = new LinkedList();

    HashMap addressStatusMap = new HashMap();

    String lastUpdatedValue = "";

    boolean automaticallyDisplay = false;

    boolean completeNow = false;

    javax.swing.KeyStroke completionKey = javax.swing.KeyStroke.getKeyStroke(net.suberic.pooka.Pooka.getProperty("Pooka.addressComplete", "control D"));

    MessageProxy messageProxy;

    long lastKeyTime = 0;

    long lastMatchedTime;

    int delayInMilliSeconds = 1000;

    Color incompleteColor = Color.red;

    Color matchedColor = Color.green;

    Color validColor = Color.blue;

    /**
   * Creates a new AddressEntryTextArea using the given NewMessageUI.
   */
    public AddressEntryTextArea(NewMessageUI ui, int rows, int columns) {
        super(rows, columns);
        messageProxy = ui.getMessageProxy();
        areaList.put(this, null);
        this.addFocusListener(this);
        if (updateThread == null) createUpdateThread();
    }

    /**
   * Creates a new AddressEntryTextArea using the given NewMessageUI.
   */
    public AddressEntryTextArea(NewMessageUI ui, String text, int rows, int columns) {
        super(text, rows, columns);
        messageProxy = ui.getMessageProxy();
        areaList.put(this, null);
        useCompletion = Pooka.getProperty("Pooka.useAddressCompletion", "false").equalsIgnoreCase("true");
        this.addFocusListener(this);
    }

    /**
   * Makes it so that we listen for key events.  On a key event, we update
   * the last time a key was pressed.
   */
    protected void processComponentKeyEvent(KeyEvent e) {
        super.processComponentKeyEvent(e);
        if (useCompletion) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                int keyCode = e.getKeyCode();
                switch(keyCode) {
                    case KeyEvent.VK_TAB:
                        break;
                    case KeyEvent.VK_UP:
                        selectNextEntry();
                        break;
                    case KeyEvent.VK_DOWN:
                        selectPreviousEntry();
                        break;
                    case KeyEvent.VK_LEFT:
                        break;
                    case KeyEvent.VK_RIGHT:
                        break;
                    default:
                        lastKeyTime = System.currentTimeMillis();
                        if (updateThread != null) updateThread.interrupt(); else createUpdateThread();
                        if (keyCode == completionKey.getKeyCode() && e.getModifiers() == completionKey.getModifiers()) {
                            completeNow = true;
                        }
                }
            }
        }
    }

    /**
   * After a sufficient amount of time has passed, updates the entry area
   * with a found value.  Called by the updateThread.
   */
    protected void updateTextValue() {
        final long lastModifiedTime = lastKeyTime;
        net.suberic.pooka.AddressMatcher matcher = getNewMessageUI().getSelectedProfile().getAddressMatcher();
        if (matcher != null) {
            final String entryString = getAddressText();
            if (needToMatch(entryString)) {
                final net.suberic.pooka.AddressBookEntry[] matchedEntries = matcher.match(entryString);
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            if (lastModifiedTime == lastKeyTime) {
                                if (matchedEntries.length > 0) {
                                    String newAddress = matchedEntries[0].getID();
                                    if (!newAddress.equalsIgnoreCase(entryString)) updateAddressText(newAddress);
                                } else {
                                    updateAddressText(entryString + Pooka.getProperty("error.noMatchingAddresses", "<no matching addresses>"));
                                }
                                lastMatchedTime = System.currentTimeMillis();
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }
        }
    }

    /**
   * This updates the list of matched addresses in this entry field.
   */
    protected synchronized void updateAddressList() {
        updateAddressList(false);
    }

    /**
   * This updates the list of matched addresses in this entry field.
   */
    protected synchronized void updateAddressList(boolean inThread) {
        final String currentText = getText();
        if (!currentText.equals(lastUpdatedValue)) {
            LinkedList newAddressList = new LinkedList();
            int beginOffset = 0;
            boolean done = false;
            while (!done) {
                int endOffset = currentText.indexOf(',', beginOffset);
                if (endOffset == -1) {
                    endOffset = currentText.length();
                    done = true;
                }
                Selection currentSelection = new Selection(beginOffset, endOffset, currentText.substring(beginOffset, endOffset));
                currentSelection.status = parseStatus(currentSelection);
                newAddressList.add(currentSelection);
                beginOffset = endOffset + 1;
                if (beginOffset >= currentText.length()) done = true;
            }
            final LinkedList toUpdateList = newAddressList;
            if (!inThread) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        updateParsedSelections(toUpdateList, currentText);
                    }
                });
            } else {
                if (SwingUtilities.isEventDispatchThread()) updateParsedSelections(toUpdateList, currentText); else try {
                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            updateParsedSelections(toUpdateList, currentText);
                        }
                    });
                } catch (Exception e) {
                }
            }
        }
    }

    /**
   * <p>Checks a selection to see if it's a correctly parsed address, an
   * address book entry, or neither.</p>
   *
   * <p>This method also updates the addressStatusMap with any parsed
   * values.</p>
   */
    public SelectionStatus parseStatus(Selection current) {
        String addressText = current.text.trim();
        Object value = addressStatusMap.get(addressText);
        if (value != null) {
            return (SelectionStatus) value;
        } else {
            SelectionStatus status = null;
            net.suberic.pooka.AddressMatcher matcher = getNewMessageUI().getSelectedProfile().getAddressMatcher();
            if (matcher != null) {
                AddressBookEntry[] matchedEntries = matcher.matchExactly(addressText);
                if (matchedEntries != null && matchedEntries.length > 0) {
                    status = new SelectionStatus(matchedEntries[0].getAddressString(), ADDRESS_MATCH);
                }
            }
            if (status == null) {
                try {
                    InternetAddress newAddress = new InternetAddress(addressText);
                    status = new SelectionStatus(addressText, VALID);
                } catch (javax.mail.internet.AddressException ae) {
                    status = new SelectionStatus(addressText, INVALID);
                }
            }
            addressStatusMap.put(addressText, status);
            return status;
        }
    }

    /**
   * <p>This redraws the text with the fonts and colors to represent the 
   * separate fields' statuses.</p>
   *
   * <p>The method will first check to make sure that the text hasn't
   * changed since the last update.  If it hasn't, then it will update the
   * text, and then write in the new addressList and lastUpdatedValue
   * fields.</p>
   *
   * <p>This method should only be called from the SwingEvent thread.</p>
   */
    public void updateParsedSelections(LinkedList newAddressList, String parsedText) {
        if (parsedText.equals(getText())) {
            Iterator iter = newAddressList.iterator();
            while (iter.hasNext()) {
                Selection current = (Selection) iter.next();
                changeSelectionFont(current.beginOffset, current.endOffset, current.status.status);
            }
            lastUpdatedValue = parsedText;
            addressList = newAddressList;
        }
    }

    /**
   * This validates that the current addressList matches the actual
   * text in the field.
   */
    public boolean validateAddressList() {
        String currentText = getText();
        int caretPos = getCaretPosition();
        java.util.Iterator iter = addressList.iterator();
        boolean matches = true;
        while (matches && iter.hasNext()) {
            Selection current = (Selection) iter.next();
            if (!currentText.substring(current.beginOffset, current.endOffset).equals(current.text)) {
                matches = false;
            }
        }
        return matches;
    }

    /**
   * <p>Changes the text in the area between the <code>beginOffset<code> and 
   * <code>endOffset</code>
   * to the font appropriate for status <code>status</code>.
   */
    void changeSelectionFont(int beginOffset, int endOffset, int status) {
    }

    /**
   * This tests to see if the given string needs to be matched or not.
   */
    public boolean needToMatch(String entry) {
        if (entry.length() == 0) return false; else return true;
    }

    /**
   * This gets the currently selected address field.
   */
    public String getAddressText() {
        Selection currentSelection = getCurrentSelection();
        return currentSelection.text;
    }

    /**
   * This gets the parsed address text for this feel.
   */
    public String getParsedAddresses() {
        updateAddressList(true);
        StringBuffer returnBuffer = new StringBuffer();
        Iterator iter = addressList.iterator();
        while (iter.hasNext()) {
            Selection current = (Selection) iter.next();
            returnBuffer.append(current.status.addressText);
            if (iter.hasNext()) returnBuffer.append(", ");
        }
        return returnBuffer.toString();
    }

    /**
   * Gets the current Selection.
   */
    Selection getCurrentSelection() {
        int caretPosition = getCaretPosition();
        String currentText = getText();
        int beginOffset = 0;
        if (caretPosition > 0) beginOffset = currentText.lastIndexOf(',', caretPosition - 1) + 1; else beginOffset = 0;
        int endOffset = currentText.indexOf(',', caretPosition);
        if (beginOffset < 0) beginOffset = 0;
        if (endOffset < 0) endOffset = currentText.length();
        while (beginOffset < endOffset && Character.isWhitespace(currentText.charAt(beginOffset))) beginOffset++;
        return new Selection(beginOffset, endOffset, currentText.substring(beginOffset, endOffset));
    }

    /**
   * This updates the currently selected address field with the new value.
   */
    public void updateAddressText(String newAddress) {
        Selection current = getCurrentSelection();
        int length = current.text.length();
        this.insert(newAddress.substring(length), current.beginOffset + length);
        this.setSelectionStart(current.beginOffset + length);
        this.setSelectionEnd(current.beginOffset + newAddress.length());
    }

    /**
   * This updates the currently selected address field with the new value.
   */
    public void replaceAddressText(Selection current, String newAddress) {
        int length = current.text.length();
        try {
            getDocument().remove(current.beginOffset, current.endOffset - current.beginOffset);
            getDocument().insertString(current.beginOffset, newAddress, null);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
        this.setSelectionStart(current.beginOffset);
        this.setSelectionEnd(current.beginOffset + newAddress.length());
    }

    /**
   * Selects the next available address entry.
   */
    public void selectNextEntry() {
        Selection currentSelection = getCurrentSelection();
        net.suberic.pooka.AddressMatcher matcher = getNewMessageUI().getSelectedProfile().getAddressMatcher();
        AddressBookEntry newValue = matcher.getNextMatch(currentSelection.text);
        if (newValue != null) {
            replaceAddressText(currentSelection, newValue.getID());
        }
    }

    /**
   * Selects the previous available address entry.
   */
    public void selectPreviousEntry() {
        Selection currentSelection = getCurrentSelection();
        net.suberic.pooka.AddressMatcher matcher = getNewMessageUI().getSelectedProfile().getAddressMatcher();
        if (matcher != null) {
            AddressBookEntry newValue = matcher.getPreviousMatch(currentSelection.text);
            if (newValue != null) {
                replaceAddressText(currentSelection, newValue.getID());
            }
        }
    }

    /**
   * Adds the given AddressBookEntries to the address field.
   */
    public void addAddresses(AddressBookEntry[] newEntries) {
        if (newEntries == null || newEntries.length < 1) return;
        String currentValue = getText();
        boolean addComma = false;
        boolean found = false;
        for (int i = currentValue.length() - 1; !found && i >= 0; i--) {
            char currentChar = currentValue.charAt(i);
            if (!Character.isWhitespace(currentChar)) {
                found = true;
                if (currentChar != ',') addComma = true;
            }
        }
        StringBuffer newValue = new StringBuffer(currentValue);
        if (addComma) newValue.append(", ");
        for (int i = 0; i < newEntries.length; i++) {
            newValue.append(newEntries[i].getAddressString());
            if (i < newEntries.length - 1) newValue.append(", ");
        }
        setText(newValue.toString());
    }

    static java.util.HashMap buttonImageMap = new java.util.HashMap();

    /**
   * Creates a button that pulls up an editor dialog for addresses.
   */
    public JButton createAddressButton(int width, int height) {
        java.awt.Dimension key = new java.awt.Dimension(width, height);
        java.awt.Image defaultImage = (java.awt.Image) buttonImageMap.get(key);
        if (defaultImage == null) {
            ImageIcon addressIcon = Pooka.getUIFactory().getIconManager().getIcon(Pooka.getProperty("AddressBook.button", "Book"));
            if (addressIcon != null) {
                java.awt.Image addressImage = addressIcon.getImage();
                defaultImage = addressImage.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
                addressIcon.setImage(defaultImage);
                buttonImageMap.put(key, defaultImage);
            }
        }
        JButton returnValue = null;
        if (defaultImage != null) {
            ImageIcon addressIcon = new ImageIcon(defaultImage);
            returnValue = new JButton(addressIcon);
        } else {
            returnValue = new JButton();
        }
        returnValue.setFocusable(false);
        returnValue.addActionListener(new AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                getNewMessageUI().showAddressWindow(AddressEntryTextArea.this);
            }
        });
        returnValue.setToolTipText(Pooka.getProperty("AddressBookEditor.buttonText", "Open Address Book"));
        returnValue.setMargin(new java.awt.Insets(1, 1, 1, 1));
        returnValue.setSize(width, height);
        return returnValue;
    }

    /**
   * Returns the parent MessageUI.
   */
    public NewMessageUI getNewMessageUI() {
        return (NewMessageUI) messageProxy.getMessageUI();
    }

    private class Selection {

        int beginOffset;

        int endOffset;

        String text;

        SelectionStatus status = null;

        Selection(int newBegin, int newEnd, String newText) {
            beginOffset = newBegin;
            endOffset = newEnd;
            text = newText;
        }
    }

    private class SelectionStatus {

        String addressText = null;

        int status;

        SelectionStatus(String newAddressText, int newStatus) {
            addressText = newAddressText;
            status = newStatus;
        }
    }

    /**
   * a no-op -- don't do anything on focusGained.
   */
    public void focusGained(java.awt.event.FocusEvent e) {
    }

    /**
   *
   */
    public void focusLost(java.awt.event.FocusEvent e) {
        lastMatchedTime = System.currentTimeMillis();
    }

    static synchronized void createUpdateThread() {
        if (updateThread == null) {
            updateThread = new Thread(new Updater(), "AddressEntryTextArea - Update Thread");
            updateThread.start();
        }
    }

    static class Updater implements Runnable {

        long sleepTime = 60000;

        Updater() {
        }

        public void run() {
            sleepTime = 0;
            java.util.Set entrySet = areaList.entrySet();
            while (!entrySet.isEmpty()) {
                sleepTime = 60000;
                java.util.Iterator entryIter = entrySet.iterator();
                while (entryIter.hasNext()) {
                    long currentTime = System.currentTimeMillis();
                    AddressEntryTextArea area = (AddressEntryTextArea) ((java.util.Map.Entry) entryIter.next()).getKey();
                    if (area.lastKeyTime > area.lastMatchedTime) {
                        if (area.lastKeyTime + area.delayInMilliSeconds < currentTime) {
                            if (area.completeNow || area.automaticallyDisplay) {
                                area.completeNow = false;
                                area.updateTextValue();
                            }
                            area.updateAddressList();
                        } else {
                            sleepTime = Math.min(sleepTime, (area.delayInMilliSeconds + area.lastKeyTime) - currentTime);
                        }
                    }
                }
                try {
                    Thread.currentThread().sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
