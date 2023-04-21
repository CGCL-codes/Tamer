package org.gocha.textbox;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import org.gocha.common.Reciver;
import org.gocha.files.FileUtil;
import org.gocha.gui.GuiKeeper;

/**
 * Поиск классов Action по classPath указанных в путях
 * @author gocha
 */
public class ActionsFinderFrame extends javax.swing.JFrame implements SingleFrame.FrameContext {

    private SingleFrame frame = null;

    @Override
    public void attach(SingleFrame frame) {
        this.frame = frame;
    }

    @Override
    public void detach(SingleFrame frame) {
        this.frame = null;
    }

    @Override
    public SingleFrame getFrame() {
        return frame;
    }

    /** Creates new form ActionsFinder */
    public ActionsFinderFrame() {
        initComponents();
        GuiKeeper.keepLocationAndSize(this);
        stopButton.setEnabled(false);
    }

    private void addClassAWT(String className) {
        try {
            Class c = Class.forName(className);
            boolean isAction = Action.class.isAssignableFrom(c);
            boolean isFrameCtx = SingleFrame.FrameContext.class.isAssignableFrom(c);
            boolean incF = allCheckBox.isSelected();
            StringBuilder sb = new StringBuilder();
            if (isFrameCtx && isAction) {
                sb.append(className);
                if (classesTextArea.getLineCount() > 0) sb.insert(0, "\n");
            } else {
                if (incF) {
                    sb.append("#");
                    sb.append(className);
                    if (classesTextArea.getLineCount() > 0) sb.insert(0, "\n");
                }
            }
            classesTextArea.append(sb.toString());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ActionsFinderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean start() {
        if (!stop()) return false;
        classesTextArea.setText("");
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        thread = new Thread(search);
        thread.setDaemon(true);
        thread.start();
        return true;
    }

    private void finished() {
        if (SwingUtilities.isEventDispatchThread()) {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        finished();
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(ActionsFinderFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ActionsFinderFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean stop() {
        boolean res = false;
        if (thread == null) {
            res = true;
        } else {
            if (thread.isAlive()) {
                thread.interrupt();
                res = false;
            } else {
                thread = null;
                res = true;
            }
        }
        if (!res) {
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
        }
        return res;
    }

    private Thread thread = null;

    private void addClass(String className) {
        if (SwingUtilities.isEventDispatchThread()) {
            addClassAWT(className);
        } else {
            try {
                final String arg = className;
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        addClass(arg);
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(ActionsFinderFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ActionsFinderFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Runnable search = new Runnable() {

        @Override
        public void run() {
            final Reciver<String> classNameReciver = new Reciver<String>() {

                @Override
                public void recive(String className) {
                    addClass(className);
                }
            };
            ParseClassPath.searchClasses(classNameReciver, ActionsFinderFrame.class.getClassLoader(), true);
            finished();
        }
    };

    private void save() {
        SingleFrame fr = getFrame();
        if (fr == null) return;
        File f = fr.getPresetActionClassesFile();
        if (f == null) return;
        String[] lines = new String[classesTextArea.getLineCount()];
        ArrayList<String> c = new ArrayList<String>();
        try {
            for (int i = 0; i < lines.length; i++) {
                int b = classesTextArea.getLineStartOffset(i);
                int e = classesTextArea.getLineEndOffset(i);
                String line = classesTextArea.getDocument().getText(b, e - b).trim();
                lines[i] = line;
                if (!line.contains("#") && !line.startsWith(" ")) {
                    c.add(line);
                }
            }
            fr.getPresetActionClasses().clear();
            fr.getPresetActionClasses().addAll(c);
            if (FileUtil.writeAllLines(f, FileUtil.UTF8(), lines, "\n")) {
                JOptionPane.showMessageDialog(fr, "Сохранено");
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(ActionsFinderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        classesTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        allCheckBox = new javax.swing.JCheckBox();
        saveButton = new javax.swing.JButton();
        setTitle("Поиск Actions");
        classesTextArea.setColumns(20);
        classesTextArea.setRows(5);
        jScrollPane1.setViewportView(classesTextArea);
        jLabel1.setText("Классы");
        startButton.setText("Начать поиск");
        startButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        stopButton.setText("Стоп");
        stopButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        allCheckBox.setText("Отображать все классы");
        saveButton.setText("Сохранить");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(startButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(stopButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(allCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE).addComponent(saveButton)).addComponent(jLabel1)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(startButton).addComponent(stopButton).addComponent(allCheckBox).addComponent(saveButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        start();
    }

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        stop();
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        save();
    }

    private javax.swing.JCheckBox allCheckBox;

    private javax.swing.JTextArea classesTextArea;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton saveButton;

    private javax.swing.JButton startButton;

    private javax.swing.JButton stopButton;
}
