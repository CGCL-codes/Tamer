package org.xmlcml.cmlimpl.jumbo;

import java.util.Vector;
import java.io.File;
import java.net.URL;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import javax.swing.event.EventListenerList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLBond;
import org.xmlcml.cml.CMLDocument;
import org.xmlcml.cml.CMLMolecule;
import org.xmlcml.cml.mvc.Controller;
import org.xmlcml.cml.mvc.MoleculeAdapter;
import org.xmlcml.cml.mvc.MoleculeEvent;
import org.xmlcml.cml.mvc.MoleculeListener;
import org.xmlcml.cml.normalise.NormalMolecule;
import org.xmlcml.noncml.JCAMP;
import org.xmlcml.noncml.NonCMLDocument;
import org.xmlcml.noncml.NonCMLDocumentImpl;
import org.xmlcml.noncml.SMILESImpl;
import jumbo.xml.util.Util;

/** first pass at a central Controller (MVC) for Jumbo. */
public class JumboController implements Controller {

    protected JumboTextPanel messageArea;

    protected JumboModel jumboModel;

    protected JumboFilePanel jumboFilePanel;

    protected CMLMolecule currentMolecule;

    protected Jumbo2D3DPanel jumbo2d3dPanel;

    protected CMLDocument currentDocument;

    protected String currentFileName;

    protected JumboTree jumboTree;

    protected JumboPlotPanel jumboPlot;

    protected JCAMP currentSpectrum;

    EventListenerList listenerList = new EventListenerList();

    /** do not use unless essential */
    public JumboController() {
    }

    public JumboController(JumboModel jumboModel) {
        this.jumboModel = jumboModel;
        messageArea = new JumboTextPanel();
    }

    public JumboModel getJumboModel() {
        return jumboModel;
    }

    public CMLMolecule getCurrentMolecule() {
        return currentMolecule;
    }

    public void addJumboListener(JumboListener l) {
        listenerList.add(JumboListener.class, l);
    }

    public void removeJumboListener(JumboListener l) {
        listenerList.remove(JumboListener.class, l);
    }

    public void fireMessageSent(JumboEvent JumboEvent) {
        System.out.println("Jumbo...: " + JumboEvent.getMessage());
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == JumboListener.class) {
                ((JumboListener) listeners[i + 1]).messageSent(JumboEvent);
            }
        }
    }

    public void addSMILESListener(SMILESAdapter l) {
        listenerList.add(SMILESAdapter.class, l);
    }

    public void removeSMILESListener(SMILESAdapter l) {
        listenerList.remove(SMILESAdapter.class, l);
    }

    public void fireSMILESEntered(SMILESEvent smilesEvent) {
        fireMessageSent(smilesEvent);
        System.out.println("Smiles...: " + smilesEvent.getSMILES());
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SMILESAdapter.class) {
                ((SMILESAdapter) listeners[i + 1]).SMILESEntered(smilesEvent);
            }
            if (listeners[i] == JumboListener.class) {
                ((JumboListener) listeners[i + 1]).messageSent(smilesEvent);
            }
        }
    }

    public void addMoleculeListener(MoleculeListener l) {
        listenerList.add(MoleculeListener.class, l);
    }

    public void removeMoleculeListener(MoleculeListener l) {
        listenerList.remove(MoleculeListener.class, l);
    }

    public void fireMoleculeLoaded(MoleculeEvent moleculeEvent) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MoleculeListener.class) {
                ((MoleculeListener) listeners[i + 1]).moleculeLoaded(moleculeEvent);
            }
            if (listeners[i] == JumboListener.class) {
                ((JumboListener) listeners[i + 1]).messageSent(moleculeEvent);
            }
        }
    }

    public void fireMoleculeClicked(MoleculeEvent moleculeEvent) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MoleculeListener.class) {
                ((MoleculeListener) listeners[i + 1]).moleculeClicked(moleculeEvent);
            }
            if (listeners[i] == JumboListener.class) {
                ((JumboListener) listeners[i + 1]).messageSent(moleculeEvent);
            }
        }
    }

    public void fireAtomClicked(MoleculeEvent moleculeEvent) {
        CMLAtom atom = moleculeEvent.getAtom();
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MoleculeListener.class) {
                ((MoleculeListener) listeners[i + 1]).atomClicked(moleculeEvent);
            }
            if (listeners[i] == JumboListener.class) {
                ((JumboListener) listeners[i + 1]).messageSent(moleculeEvent);
            }
        }
    }

    public void fireBondClicked(MoleculeEvent moleculeEvent) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MoleculeListener.class) {
                ((MoleculeListener) listeners[i + 1]).bondClicked(moleculeEvent);
            }
            if (listeners[i] == JumboAdapter.class) {
                ((JumboAdapter) listeners[i + 1]).messageSent(moleculeEvent);
            }
        }
    }

    protected Hashtable file2MoleculeTable;

    void readFile(JumboFilePanel jfp, String fileName) throws Exception {
        currentFileName = fileName;
        JumboEvent je = new JumboEvent(this);
        addMessageText("FILE: " + fileName);
        je.setMessage("Reading file: " + fileName);
        fireMessageSent(je);
        currentDocument = NonCMLDocumentImpl.createCMLDocument(new URL(Util.makeAbsoluteURL(fileName)));
        jfp.addCMLDocument(currentDocument, fileName);
    }

    public void addMessageText(String line) {
        messageArea.addLine(line);
    }

    public void fireJumboException(Exception e) {
        String text = "Exception: " + e;
        JOptionPane.showMessageDialog(new JFrame("EXCEPTION"), text, "EXCEPTION!!!", JOptionPane.INFORMATION_MESSAGE);
        e.printStackTrace();
    }

    public void fireJumboWarning(String message) {
        String text = "WARNING: " + message;
        JOptionPane.showMessageDialog(new JFrame("WARNING"), text, "WARNING!!!", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(text);
    }

    public void fireJumboMessage(String message) {
        String text = "INFO: " + message;
        JOptionPane.showMessageDialog(new JFrame("MESSAGE"), text, "", JOptionPane.INFORMATION_MESSAGE);
        System.out.println(text);
    }

    public void testSearch(SMILESEvent se) {
        System.out.println("smiles: " + se);
        try {
            CMLMolecule molecule = SMILESImpl.createMolecule(se.getSMILES());
            molecule.debug();
            jumbo2d3dPanel.showMolecule(molecule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
