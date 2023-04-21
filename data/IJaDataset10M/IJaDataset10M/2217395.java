package fr.harlie.merge_pdf.action.pdf;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdesktop.swingworker.SwingWorker;
import com.itextpdf.text.DocumentException;
import fr.harlie.merge_pdf.PdfMergerContext;
import fr.harlie.merge_pdf.action.AbstractPdfMergerAction;
import fr.harlie.merge_pdf.action.PdfActionEvent;
import fr.harlie.merge_pdf.gui.itext.ProgressBarDialog;
import fr.harlie.merge_pdf.itext.IPdfMergerListener;
import fr.harlie.merge_pdf.itext.PdfMergingTool;
import fr.harlie.merge_pdf.tree.INode;

public class GenerateFileAction extends AbstractPdfMergerAction {

    private static final long serialVersionUID = 1L;

    private static GenerateFileAction action;

    private JFileChooser saveFileChooser;

    private class Task extends SwingWorker<Void, Void> implements IPdfMergerListener {

        private PdfMergingTool tool;

        private INode rootNode;

        private File file;

        private int steps;

        private Task() throws DocumentException, IOException {
            tool = new PdfMergingTool();
            tool.addIPdfMergerListener(this);
        }

        public void setRootNode(INode rootNode) {
            this.rootNode = rootNode;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public void jobDone() {
        }

        public void init(int numberOfSteps) {
            steps = numberOfSteps;
            setProgress(0);
        }

        public void nextSteps(int step, String title) {
            setProgress((int) (100.0f * ((float) step / (float) steps)));
            firePropertyChange("title", "", title);
        }

        @Override
        public Void doInBackground() {
            try {
                tool.generatePdf(rootNode, file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void done() {
            firePropertyChange("done", "", "done");
        }
    }

    public static GenerateFileAction getAction() {
        if (GenerateFileAction.action == null) {
            GenerateFileAction.action = new GenerateFileAction();
        }
        return GenerateFileAction.action;
    }

    private GenerateFileAction() {
        super("G�n�rer", new ImageIcon(PdfMergerContext.class.getClassLoader().getResource("icons/Export16.gif")));
        this.putValue(Action.SHORT_DESCRIPTION, "Fusionner les fichiers.");
        this.putValue(Action.LONG_DESCRIPTION, "Fusionne tous les fichiers dans un document PDF.");
        this.rootNode = null;
        this.saveFileChooser = new JFileChooser();
        this.saveFileChooser.setFileFilter(PdfMergerContext.getContext().getPdfFilter());
    }

    protected PdfActionEvent executeAction(final ActionEvent e) {
        if (this.rootNode != null && this.rootNode.getChildCount() > 0) {
            int returnVal = saveFileChooser.showSaveDialog(getActiveFrame(e.getSource()));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    Task task = new Task();
                    File file = saveFileChooser.getSelectedFile();
                    if (file.exists()) {
                        if (JOptionPane.showConfirmDialog(getActiveFrame(e.getSource()), "Le fichier " + file.getName() + " existe d�j�.\nVoulez-vous l'�craser ?", "Enregistrement", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return null;
                    }
                    final ProgressBarDialog dialog = new ProgressBarDialog(getActiveFrame(e.getSource()));
                    task.addPropertyChangeListener(new PropertyChangeListener() {

                        public void propertyChange(PropertyChangeEvent evt) {
                            if (evt.getPropertyName().equals("progress")) {
                                dialog.setProgressValue((Integer) evt.getNewValue());
                            }
                            if (evt.getPropertyName().equals("title")) {
                                dialog.addMessage(evt.getNewValue().toString());
                            }
                            if (evt.getPropertyName().equals("done")) {
                                JOptionPane.showMessageDialog(getActiveFrame(e.getSource()), "Le fichier PDF a �t� g�n�r�", "G�n�ration", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    });
                    task.setRootNode(this.rootNode);
                    task.setFile(file);
                    task.execute();
                    dialog.setVisible(true);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getActiveFrame(e.getSource()), "Impossible de g�n�rer le fichier PDF", "G�n�ration", JOptionPane.ERROR_MESSAGE);
                } catch (DocumentException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getActiveFrame(e.getSource()), "Impossible de g�n�rer le fichier PDF", "G�n�ration", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getActiveFrame(e.getSource()), "Impossible de g�n�rer le fichier PDF", "G�n�ration", JOptionPane.ERROR_MESSAGE);
                }
            }
            return new PdfActionEvent(this.rootNode);
        }
        return null;
    }

    private INode rootNode;

    public void setRootNode(INode currentNode) {
        boolean enabled = isEnabled();
        this.rootNode = currentNode;
        setEnabled(this.rootNode != null && this.rootNode.getChildCount() > 0);
        if (enabled != isEnabled()) {
            firePropertyChange("enabled", enabled, !enabled);
        }
    }

    public INode getRootNode() {
        return rootNode;
    }
}
