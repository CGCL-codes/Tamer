package net.sf.jooreports.converter;

import java.io.File;
import java.util.ResourceBundle;
import com.sun.star.beans.PropertyValue;
import de.tu_clausthal.in.agrausch.weit.export.steuerung.TransformerObserver;

public interface DocumentConverter {

    /**
	 * Convert a document.
	 */
    public void convert(File inputFile, DocumentFormat inputFormat, File outputFile, DocumentFormat outputFormat, TransformerObserver observer, ResourceBundle resBundle) throws de.tu_clausthal.in.agrausch.weit.export.steuerung.exception.ExportException;

    /**
	 * Convert a document. The input format is guessed from the file extension.
	 */
    public void convert(File inputDocument, File outputDocument, TransformerObserver observer, ResourceBundle resBundle) throws de.tu_clausthal.in.agrausch.weit.export.steuerung.exception.ExportException;

    /**
	 * Convert a document. The input format is guessed from the file extension.
	 * Allows passing parameters to the output filter
	 */
    public void convert(File inputDocument, File outputDocument, TransformerObserver observer, ResourceBundle resBundle, PropertyValue[] filterProperties) throws de.tu_clausthal.in.agrausch.weit.export.steuerung.exception.ExportException;
}
