package org.compiere.grid.ed;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.util.*;

/**
 * 	Binary Editor.
 * 	Shows length of data.
 *	
 *  @author Jorg Janke
 *  @version $Id: VBinary.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public class VBinary extends JButton implements VEditor, ActionListener {

    /**
	 *  Binary Editor
	 *  @param columnName column name
	 *  @param WindowNo
	 */
    public VBinary(String columnName, int WindowNo) {
        super("");
        m_columnName = columnName;
        m_WindowNo = WindowNo;
        super.addActionListener(this);
    }

    /**
	 *  Dispose
	 */
    public void dispose() {
        m_data = null;
    }

    /** Column Name             */
    private String m_columnName;

    /** WindowNo                */
    private int m_WindowNo;

    /** Data					*/
    private Object m_data = null;

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(VBinary.class);

    /**
	 *  Set Value
	 *  @param value
	 */
    public void setValue(Object value) {
        log.config("=" + value);
        m_data = value;
        if (m_data == null) setText("-"); else {
            String text = "?";
            if (m_data instanceof byte[]) {
                byte[] bb = (byte[]) m_data;
                text = "#" + bb.length;
            } else {
                text = m_data.getClass().getName();
                int index = text.lastIndexOf('.');
                if (index != -1) text = text.substring(index + 1);
            }
            setText(text);
        }
    }

    /**
	 *  Get Value
	 *  @return value
	 */
    public Object getValue() {
        return m_data;
    }

    /**
	 *  Get Display Value
	 *  @return image name
	 */
    public String getDisplay() {
        return getText();
    }

    /**
	 *  Set ReadWrite
	 *  @param rw
	 */
    public void setReadWrite(boolean rw) {
        if (isEnabled() != rw) setEnabled(rw);
    }

    /**
	 *  Get ReadWrite
	 *  @return true if rw
	 */
    public boolean isReadWrite() {
        return super.isEnabled();
    }

    /**
	 *  Set Mandatory
	 *  @param mandatory NOP
	 */
    public void setMandatory(boolean mandatory) {
    }

    /**
	 *  Get Mandatory
	 *  @return false
	 */
    public boolean isMandatory() {
        return false;
    }

    /**
	 *  Set Background - nop
	 *  @param color
	 */
    public void setBackground(Color color) {
    }

    /**
	 *  Set Background - nop
	 */
    public void setBackground() {
    }

    /**
	 *  Set Background - nop
	 *  @param error
	 */
    public void setBackground(boolean error) {
    }

    /**
	 *  Property Change
	 *  @param evt
	 */
    public void propertyChange(PropertyChangeEvent evt) {
        log.info(evt.toString());
        if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY)) setValue(evt.getNewValue());
    }

    /**
	 *  ActionListener - start dialog and set value
	 *  @param e event
	 */
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser("");
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = 0;
        boolean save = m_data != null;
        if (save) option = fc.showSaveDialog(this); else option = fc.showOpenDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();
        if (file == null) return;
        log.info(file.toString());
        try {
            if (save) {
                FileOutputStream os = new FileOutputStream(file);
                byte[] buffer = (byte[]) m_data;
                os.write(buffer);
                os.flush();
                os.close();
                log.config("Save to " + file + " #" + buffer.length);
            } else {
                FileInputStream is = new FileInputStream(file);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 8];
                int length = -1;
                while ((length = is.read(buffer)) != -1) os.write(buffer, 0, length);
                is.close();
                byte[] data = os.toByteArray();
                m_data = data;
                log.config("Load from " + file + " #" + data.length);
                os.close();
            }
        } catch (Exception ex) {
            log.log(Level.WARNING, "Save=" + save, ex);
        }
        try {
            fireVetoableChange(m_columnName, null, m_data);
        } catch (PropertyVetoException pve) {
        }
    }

    /**
	 *  Set Field/WindowNo for ValuePreference (NOP)
	 *  @param mField
	 */
    public void setField(org.compiere.model.GridField mField) {
    }
}
