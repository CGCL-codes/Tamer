package com.oz.lanslim.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;
import com.oz.lanslim.model.SlimAvailabilityEnum;
import com.oz.lanslim.model.SlimContact;
import com.oz.lanslim.model.SlimContactList;
import com.oz.lanslim.model.SlimGroupContact;
import com.oz.lanslim.model.SlimKey;
import com.oz.lanslim.model.SlimModel;
import com.oz.lanslim.model.SlimTalk;
import com.oz.lanslim.model.SlimUserContact;

public class ContactTransferHandler extends TransferHandler {

    private static final String humanRepresentationOfFlavor = "SlimContact";

    private SlimTalk talk;

    private TalkSelector talkTabPane;

    private ContactTree tree;

    private SlimModel model;

    public ContactTransferHandler() {
    }

    public ContactTransferHandler(SlimTalk pTalk) {
        talk = pTalk;
    }

    public ContactTransferHandler(TalkSelector pTalkTabPane) {
        talkTabPane = pTalkTabPane;
    }

    public ContactTransferHandler(ContactTree pTree, SlimModel pModel) {
        tree = pTree;
        model = pModel;
    }

    public int getSourceActions(JComponent c) {
        return COPY;
    }

    /**
	 * This method indicates if a component would accept an import of the given
	 * set of data flavors prior to actually attempting to import it. 
	 *
	 * @param comp  The component to receive the transfer.  This
	 *  argument is provided to enable sharing of TransferHandlers by
	 *  multiple components.
	 * @param flavors  The data formats available
	 * @return  true if the data can be inserted into the component, false otherwise.
	 */
    public boolean canImport(JComponent comp, DataFlavor[] flavors) {
        boolean lContinue = false;
        for (int i = 0; i < flavors.length && !lContinue; i++) {
            if (flavors[i].getRepresentationClass().equals(SlimContact.class)) {
                lContinue = true;
            }
        }
        if (!lContinue) {
            return false;
        }
        if (talk != null || talkTabPane != null) {
            return true;
        } else if (tree != null) {
            return true;
        }
        return false;
    }

    /**
         * Creates a <code>Transferable</code> to use as the source for
         * a data transfer. Returns the representation of the data to
         * be transferred, or <code>null</code> if the component's
         * property is <code>null</code>
         *
         * @param c  the component holding the data to be transferred; this
         *  argument is provided to enable sharing of <code>TransferHandler</code>s
         *  by multiple components
         * @return  the representation of the data to be transferred, or
         *  <code>null</code> if the property associated with <code>c</code>
         *  is <code>null</code> 
         *  
         */
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof ContactView) {
            SlimContact[] scs = ((ContactView) c).getSelectedContacts();
            return new TransferableContact(scs);
        } else if (c instanceof JEditorPane) {
            return new StringSelection(((JEditorPane) c).getSelectedText());
        }
        ;
        return null;
    }

    /**
    	 * This method causes a transfer to a component from a clipboard or a 
    	 * DND drop operation.  The Transferable represents the data to be
    	 * imported into the component.  
    	 *
    	 * @param comp  The component to receive the transfer.  This
    	 *  argument is provided to enable sharing of TransferHandlers by
    	 *  multiple components.
    	 * @param t     The data to import
    	 * @return  true if the data was inserted into the component, false otherwise.
    	 */
    public boolean importData(JComponent comp, Transferable t) {
        SlimTalk st = null;
        if (talkTabPane != null && talkTabPane.getDisplayedTalk() != null) {
            st = talkTabPane.getDisplayedTalk();
        } else if (talk != null) {
            st = talk;
        }
        if (st != null) {
            try {
                SlimContact[] contactsArray = (SlimContact[]) t.getTransferData(new DataFlavor(SlimContact.class, humanRepresentationOfFlavor));
                Set cl = new HashSet();
                for (int i = 0; i < contactsArray.length; i++) {
                    SlimContact sc = contactsArray[i];
                    if (sc.isGroup()) {
                        cl.addAll(((SlimGroupContact) sc).getOnlineMembers());
                    } else if (sc.getAvailability() == SlimAvailabilityEnum.ONLINE) {
                        try {
                            SlimUserContact suc = (SlimUserContact) contactsArray[i];
                            if (suc.getKey() != null) {
                                suc.setKey(SlimKey.fromString(suc.getKey().toString()));
                            }
                            cl.add(suc);
                        } catch (SlimException e) {
                        }
                    }
                }
                if (cl.size() < 1) {
                    JOptionPane.showMessageDialog(comp.getRootPane().getParent(), Externalizer.getString("LANSLIM.19"), Externalizer.getString("LANSLIM.28"), JOptionPane.WARNING_MESSAGE);
                } else {
                    Iterator lIt = cl.iterator();
                    while (lIt.hasNext()) {
                        SlimUserContact suc = (SlimUserContact) lIt.next();
                        st.addPeople(suc);
                    }
                    return true;
                }
            } catch (SlimException se) {
                JOptionPane.showMessageDialog(comp.getRootPane().getParent(), Externalizer.getString("LANSLIM.25"), Externalizer.getString("LANSLIM.22"), JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioe) {
            } catch (UnsupportedFlavorException ufe) {
            }
        } else if (tree != null) {
            try {
                String lCatName = null;
                DefaultMutableTreeNode tn = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                if (tn.getUserObject() instanceof String) {
                    lCatName = (String) tn.getUserObject();
                    if (!SlimContactList.CATEGORY_GROUP.equals(lCatName)) {
                        SlimContact[] contactsArray = (SlimContact[]) t.getTransferData(new DataFlavor(SlimContact.class, humanRepresentationOfFlavor));
                        for (int i = 0; i < contactsArray.length; i++) {
                            if (!contactsArray[i].isGroup()) {
                                try {
                                    SlimUserContact suc = (SlimUserContact) contactsArray[i];
                                    if (suc.getKey() != null) {
                                        suc.setKey(SlimKey.fromString(suc.getKey().toString()));
                                    }
                                    model.getContacts().moveUserIntoCategory(suc, lCatName);
                                } catch (SlimException e) {
                                }
                            }
                        }
                        return true;
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (UnsupportedFlavorException ufe) {
            }
        }
        return false;
    }

    private class TransferableContact implements Transferable {

        private SlimContact[] contacts;

        public TransferableContact(SlimContact[] pContacts) {
            contacts = pContacts;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.getRepresentationClass().equals(SlimContact.class)) {
                return contacts;
            }
            throw new UnsupportedFlavorException(flavor);
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { new DataFlavor(SlimContact.class, humanRepresentationOfFlavor) };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (flavor.getRepresentationClass().equals(SlimContact.class)) {
                return true;
            }
            return false;
        }

        public SlimContact[] getContacts() {
            return contacts;
        }
    }
}
