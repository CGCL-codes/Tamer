package fr.itris.glips.svgeditor.io.managers;

import javax.swing.*;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.print.*;
import org.w3c.dom.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the printing of files
 * @author Jordi SUC
 */
public class FilePrint {

    /**
	 * the labels
	 */
    private String errorTitle = "", errorMessage = "";

    /**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
    public FilePrint(IOManager ioManager) {
        errorTitle = ResourcesManager.bundle.getString("FilePrintErrorTitle");
        errorMessage = ResourcesManager.bundle.getString("FilePrintErrorMessage");
    }

    /**
	 * prints the svg document graphic representation denoted by the provided handle
	 * @param handle the handle whose graphic representation should be saved
	 */
    public void print(SVGHandle handle) {
        Document clonedDocument = (Document) handle.getCanvas().getDocument().cloneNode(true);
        TranscoderInput input = new TranscoderInput(clonedDocument);
        TranscoderOutput output = new TranscoderOutput();
        PrintTranscoder transcoder = new PrintTranscoder();
        transcoder.addTranscodingHint(PrintTranscoder.KEY_SHOW_PRINTER_DIALOG, new Boolean(true));
        try {
            transcoder.transcode(input, output);
            transcoder.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Editor.getParent(), errorMessage, errorTitle, JOptionPane.WARNING_MESSAGE);
        }
    }
}
