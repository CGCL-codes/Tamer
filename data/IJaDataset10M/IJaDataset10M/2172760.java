package com.aptana.ide.editors.unified;

import org.eclipse.jface.text.ITypedRegion;
import com.aptana.ide.editors.unified.errors.IFileError;
import com.aptana.ide.editors.unified.errors.IFileErrorListener;
import com.aptana.ide.lexer.LexemeList;
import com.aptana.ide.parsing.IParseState;

/**
 * Tools to work with files generically
 */
public interface IFileService {

    /**
	 * Retrieves tools specific to the language of this file
	 * 
	 * @param mimeType
	 * @return IFileLanguageService
	 */
    IFileLanguageService getLanguageService(String mimeType);

    /**
	 * Called whenever source content is updated
	 * 
	 * @param insertedSource
	 * @param offset
	 * @param removeLength
	 */
    void updateContent(String insertedSource, int offset, int removeLength);

    /**
	 * getLexemeList
	 * 
	 * @return LexemeList
	 */
    LexemeList getLexemeList();

    /**
	 * getParseState
	 * 
	 * @return IParseState
	 */
    IParseState getParseState();

    /**
	 * Supports language changes within a file
	 * 
	 * @return Partitions
	 */
    ITypedRegion[] getPartitions();

    /**
	 * Returns the partition at the given offset
	 * 
	 * @param offset
	 * @return Returns the partition at the given offset
	 */
    ITypedRegion getPartitionAtOffset(int offset);

    /**
	 * Adds file listener
	 * 
	 * @param fileListener
	 */
    void addFileListener(IFileContextListener fileListener);

    /**
	 * Removes file listener
	 * 
	 * @param fileListener
	 */
    void removeFileListener(IFileContextListener fileListener);

    /**
	 * Adds file listener that is not time sensitive. These can happen after the edit is complete, and the UI is
	 * updated, on a separate thread. eg. error marking
	 * 
	 * @param fileListener
	 */
    void addDelayedFileListener(IFileContextListener fileListener);

    /**
	 * Removes file listener that is not time sensitive. These can happen after the edit is complete, and the UI is
	 * updated, on a separate thread. eg. error marking
	 * 
	 * @param fileListener
	 */
    void removeDelayedFileListener(IFileContextListener fileListener);

    /**
	 * This file listener waits even longer than delayed, usesful for things that just don't need to happen in 'real
	 * time' or 'near real time', like problems and outliner
	 * 
	 * @param fileListener
	 */
    void addLongDelayedFileListener(IFileContextListener fileListener);

    /**
	 * removeLongDelayedFileListener
	 * 
	 * @param fileListener
	 */
    void removeLongDelayedFileListener(IFileContextListener fileListener);

    /**
	 * getFileListeners
	 * 
	 * @return IFileContextListener[]
	 */
    IFileContextListener[] getFileListeners();

    /**
	 * hasFileListenerAdded
	 * 
	 * @param listener
	 * @return boolean
	 */
    boolean hasFileListenerAdded(IFileContextListener listener);

    /**
	 * getDelayedFileListeners
	 * 
	 * @return IFileContextListener[]
	 */
    IFileContextListener[] getDelayedFileListeners();

    /**
	 * hasDelayedFileListenerAdded
	 * 
	 * @param listener
	 * @return boolean
	 */
    boolean hasDelayedFileListenerAdded(IFileContextListener listener);

    /**
	 * getLongDelayedFileListeners
	 * 
	 * @return IFileContextListener[]
	 */
    IFileContextListener[] getLongDelayedFileListeners();

    /**
	 * hasLongDelayedFileListenerAdded
	 * 
	 * @param listener
	 * @return boolean
	 */
    boolean hasLongDelayedFileListenerAdded(IFileContextListener listener);

    /**
	 * setFileErrors
	 * 
	 * @param markers
	 */
    void setFileErrors(IFileError[] markers);

    /**
	 * getFileErrors
	 * 
	 * @return IFileError[]
	 */
    IFileError[] getFileErrors();

    /**
	 * addErrorListener
	 * 
	 * @param listener
	 */
    void addErrorListener(IFileErrorListener listener);

    /**
	 * removeErrorListener
	 * 
	 * @param listener
	 */
    void removeErrorListener(IFileErrorListener listener);

    /**
	 * activateForEditing
	 */
    void activateForEditing();

    /**
	 * deactivateForEditing
	 */
    void deactivateForEditing();

    /**
	 * getSource
	 * 
	 * @return String
	 */
    String getSource();

    /**
	 * connectSourceProvider
	 * 
	 * @param sourceProvider
	 */
    void connectSourceProvider(IFileSourceProvider sourceProvider);

    /**
	 * disconnectSourceProvider
	 * 
	 * @param sourceProvider
	 */
    void disconnectSourceProvider(IFileSourceProvider sourceProvider);

    /**
	 * isConnected
	 * 
	 * @return boolean
	 */
    boolean isConnected();

    /**
	 * getDefaultLanguage
	 * 
	 * @return String
	 */
    String getDefaultLanguage();

    /**
	 * fireContentChangedEvent
	 * 
	 * @param insertedSource
	 * @param offset
	 * @param removeLength
	 */
    void fireContentChangedEvent(String insertedSource, int offset, int removeLength);

    /**
	 * forceContentChangedEvent
	 */
    void forceContentChangedEvent();

    /**
	 * getSourceProvider
	 * 
	 * @return IFileSourceProvider
	 */
    IFileSourceProvider getSourceProvider();
}
