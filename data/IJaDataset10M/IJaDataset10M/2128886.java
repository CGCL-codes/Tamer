package net.mjrz.fm.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import net.mjrz.fm.Main;
import net.mjrz.fm.entity.FManEntityManager;
import net.mjrz.fm.entity.beans.Attachment;
import net.mjrz.fm.entity.beans.AttachmentRef;
import net.mjrz.fm.ui.dialogs.TransactionDialog;

public class AttachmentsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private DefaultListModel attachmentsListModel = null;

    private JList attachmentsList = null;

    private JButton saveB = null;

    private JButton addB = null, removeB = null;

    int mode = -1;

    private FManEntityManager entityManager = null;

    public AttachmentsPanel(int mode) {
        super();
        this.mode = mode;
        entityManager = new FManEntityManager();
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        attachmentsListModel = new DefaultListModel();
        attachmentsList = new JList();
        attachmentsList.setModel(attachmentsListModel);
        attachmentsList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        attachmentsList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtonState(e);
            }
        });
        initEditDialog();
    }

    private void initEditDialog() {
        addB = new JButton("Add");
        addB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new AttachmentsFileFilter());
                try {
                    int sel = fc.showOpenDialog(AttachmentsPanel.this);
                    if (sel == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        FileWrapper fw = new FileWrapper(file);
                        attachmentsListModel.addElement(fw);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addB.setPreferredSize(new Dimension(80, 20));
        removeB = new JButton("Remove");
        removeB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Object[] objs = attachmentsList.getSelectedValues();
                if (objs != null) {
                    for (Object obj : objs) {
                        attachmentsListModel.removeElement(obj);
                    }
                }
            }
        });
        removeB.setPreferredSize(new Dimension(80, 20));
        removeB.setEnabled(false);
        GridBagConstraints g1 = new GridBagConstraints();
        g1.gridx = 0;
        g1.gridy = 0;
        g1.anchor = GridBagConstraints.FIRST_LINE_START;
        g1.fill = GridBagConstraints.NONE;
        add(addB, g1);
        GridBagConstraints g2 = new GridBagConstraints();
        g2.gridx = 2;
        g2.gridy = 0;
        g2.anchor = GridBagConstraints.FIRST_LINE_START;
        g2.fill = GridBagConstraints.NONE;
        add(removeB, g2);
        GridBagConstraints g3 = new GridBagConstraints();
        g3.gridx = 0;
        g3.gridy = 1;
        g3.fill = GridBagConstraints.BOTH;
        g3.weightx = 1;
        g3.weighty = 1;
        g3.gridwidth = GridBagConstraints.REMAINDER;
        add(attachmentsList, g3);
    }

    private void initViewDialog() {
        saveB = new JButton("Save");
        saveB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                doSaveButtonActionPerformed();
            }
        });
        saveB.setPreferredSize(new Dimension(80, 20));
        saveB.setEnabled(false);
        GridBagConstraints g1 = new GridBagConstraints();
        g1.gridx = 0;
        g1.gridy = 0;
        g1.anchor = GridBagConstraints.FIRST_LINE_START;
        g1.fill = GridBagConstraints.NONE;
        add(saveB, g1);
        GridBagConstraints g3 = new GridBagConstraints();
        g3.gridx = 0;
        g3.gridy = 1;
        g3.fill = GridBagConstraints.BOTH;
        g3.weightx = 1;
        g3.weighty = 1;
        g3.gridwidth = GridBagConstraints.REMAINDER;
        add(attachmentsList, g3);
    }

    public java.util.List<String> getAttachmentsList() {
        int sz = attachmentsListModel.getSize();
        if (sz > 0) {
            ArrayList<String> ret = new ArrayList<String>();
            for (int i = 0; i < sz; i++) {
                FileWrapper fw = (FileWrapper) attachmentsListModel.get(i);
                ret.add(fw.file.getAbsolutePath());
            }
            return ret;
        }
        return null;
    }

    public void loadAttachments(Long txId) {
        try {
            attachmentsListModel.clear();
            List<AttachmentRef> atts = FManEntityManager.getAttachmentId(txId);
            if (atts != null && atts.size() > 0) {
                for (AttachmentRef r : atts) {
                    attachmentsListModel.addElement(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSaveButtonActionPerformed() {
        Object o = attachmentsList.getSelectedValue();
        if (o instanceof AttachmentRef) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            AttachmentRef ref = (AttachmentRef) o;
            try {
                int sel = fc.showSaveDialog(AttachmentsPanel.this);
                if (sel == JFileChooser.APPROVE_OPTION) {
                    File dir = fc.getSelectedFile();
                    File file = new File(dir.getAbsolutePath() + Main.PATH_SEPARATOR + ref.getFileName());
                    System.out.println("Creating file : " + file.getAbsolutePath());
                    Attachment a = (Attachment) entityManager.getObject("Attachment", "id = " + ref.getAttachmentId());
                    if (a == null) return;
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] arr = a.getAttachment();
                    fos.write(arr);
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateButtonState(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (e.getFirstIndex() >= 0) {
            removeB.setEnabled(true);
        } else {
            removeB.setEnabled(false);
        }
    }

    public static class FileWrapper {

        private File file;

        private long length;

        public FileWrapper(File file) {
            this.file = file;
            this.length = file.length();
        }

        public String toString() {
            return new StringBuilder(file.getAbsolutePath()).append(" ( ").append(length).append(" bytes) ").toString();
        }

        public File getFile() {
            return file;
        }

        public long getLength() {
            return length;
        }
    }

    class AttachmentsFileFilter extends FileFilter {

        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            int pos = f.getName().lastIndexOf('.');
            String ext = null;
            if (pos >= 0 && pos + 1 < f.getName().length()) ext = f.getName().substring(pos + 1);
            if (ext == null) return false;
            if (ext.equalsIgnoreCase("pdf")) {
                return true;
            } else if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpe")) {
                return true;
            } else if (ext.equalsIgnoreCase("bmp")) {
                return true;
            }
            return false;
        }

        public String getDescription() {
            return "Pdf, Jpeg, Bmp files";
        }
    }
}
