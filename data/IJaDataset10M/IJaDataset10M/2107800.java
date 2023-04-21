package de.tuc.in.sse.weit.export.steuerung;

import java.io.File;
import java.util.Map;
import java.util.ResourceBundle;
import de.tuc.in.sse.weit.export.steuerung.FormatHandler;
import de.tuc.in.sse.weit.export.steuerung.TransformerObserver;
import de.tuc.in.sse.weit.export.steuerung.exception.ExportException;
import de.tuc.in.sse.weit.export.steuerung.exception.MisconfigurationException;

/**
 * FormatHandler interface for using document converters.
 * 
 * @author Dragan Sunjka
 * 
 */
public interface FormatHandler {

    /**
     * Provides a map of all available FormatHandlers. This map could be used by
     * this handler if it depends on some other handlers.
     * 
     * @param allHandlers
     */
    public void setHandlerMap(Map<String, FormatHandler> allHandlers);

    /**
     * Sets the imageMap.
     * 
     * @param imageFolders
     */
    public void setImagesMap(Map<String, File> imageFolders);

    /**
     * Just gets the name of this handler.
     * 
     * @return
     */
    public String getName();

    /**
     * Converts a file from in to out.
     * 
     * @param in
     * @param out
     * @throws ExportException
     */
    public void convert(File in, File out, Map<String, String> params) throws ExportException;

    /**
     * Sets options for the handler.
     * 
     * @param name
     * @param value
     * @throws MisconfigurationException
     *             if there is no such option for this handler
     */
    public void setParameter(String name, String value) throws MisconfigurationException;

    /**
     * Sets the observer for this transformer.
     * 
     * @param observer
     */
    public void setObserver(TransformerObserver observer);

    /**
     * Sets the templates root folder.
     * 
     * @param path
     */
    public void setTemplatesFolder(File path);

    /**
	 * Sets the ResourceBundle needed for internationalized strings.
	 * 
	 * @param resBundle
	 */
    public void setResourceBundle(ResourceBundle resBundle);
}
