package com.icteam.fiji.manager;

import java.util.List;
import com.icteam.fiji.Service;
import com.icteam.fiji.manager.exception.ManagerException;
import com.icteam.fiji.model.filemanager.FileDescriptor;

/**
 *  Gestore di directory di sistema
 */
public interface FileSystemManager extends Service {

    /**
     * Restituisce la cartella corrispondente al path <i>path</i>
     */
    FileDescriptor getDir(final String path) throws ManagerException;

    /**
     * Restituisce l'elenco delle cartelle contenute nella cartella dirName
     */
    List<FileDescriptor> getDirChildren(final String dirName) throws ManagerException;

    /**
     * Restituisce l'elenco dei file contenuti nella cartella dirName
     */
    List<FileDescriptor> getFileChildren(final String dirName, final boolean acceptHidden) throws ManagerException;

    /**
     * Restituisce l'elenco dei folder padri del folder avente l'Id passato come parametro
     */
    List<FileDescriptor> getDirAncestors(final String dirName) throws ManagerException;
}
