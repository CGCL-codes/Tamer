package org.dicom4j.toolkit.ui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import org.dicom4j.io.media.DicomFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Specialize {@link JFileChooser} to select dicom files </p>
 *
 * @since 0.1
 * @since 24 janv. 2009
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class DicomFileChooser extends JFileChooser {

    private static final long serialVersionUID = 1L;

    /**
	 * the logger
	 */
    private static final Logger logger = LoggerFactory.getLogger(DicomFileChooser.class);

    public DicomFileChooser() {
        super();
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setMultiSelectionEnabled(false);
    }

    /**
	 * create new FileChoose
	 * @param allowMultiSelection true is Multi-selection must be allow
	 */
    public DicomFileChooser(boolean allowMultiSelection) {
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setMultiSelectionEnabled(allowMultiSelection);
    }

    /**
	 * show the OpenDialog to allow user to select files, and return the list of selected files
	 * @return the selected files (empty list if no files was selected)
	 */
    public List<DicomFile> showOpenDialog() {
        int returnVal = showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) return getDicomFile(getSelectedFiles()); else return new ArrayList<DicomFile>();
    }

    private List<DicomFile> getDicomFile(File[] aFiles) {
        List<DicomFile> result = new ArrayList<DicomFile>();
        for (int i = 0; i < aFiles.length; i++) {
            if (aFiles[i].isDirectory()) {
                List<DicomFile> files = getDicomFile(aFiles[i].listFiles());
                result.addAll(files);
            } else {
                if (!aFiles[i].isHidden()) {
                    logger.debug("result.add: " + aFiles[i].getPath());
                    result.add(new DicomFile(aFiles[i]));
                }
            }
        }
        return result;
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        setAccessory(new JTextArea("dfsdfsdfdfs"));
        return super.showSaveDialog(parent);
    }
}
