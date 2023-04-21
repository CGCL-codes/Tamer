package com.calfater.mailcarbon.core.copythreadmanager;

import java.util.ArrayList;
import javax.mail.URLName;
import org.apache.log4j.Logger;
import com.calfater.mailcarbon.core.Copy;
import com.calfater.mailcarbon.core.foldermanager.FolderManagerFactory;
import com.calfater.mailcarbon.core.foldermanager.IFolderManager;
import com.calfater.mailcarbon.core.messagemanager.IMessageManager;
import com.calfater.mailcarbon.core.messagemanager.MessageManagerFactory;

public class CopyThreadManager {

    private static final Logger _logger = Logger.getLogger(CopyThreadManager.class);

    private final ArrayList<Copy> _threadList = new ArrayList<Copy>();

    private URLName _dst;

    private URLName _src;

    private IMessageManager _messageManagerService = MessageManagerFactory.getMessageManagerService();

    private IFolderManager _folderManagerService = FolderManagerFactory.getFolderManagerService();

    public CopyThreadManager() {
        _threadList.add(new Copy());
    }

    public void start() throws InterruptedException {
        for (Copy copy : _threadList) {
            copy.start();
        }
    }

    public void join() throws InterruptedException {
        for (Copy copy : _threadList) {
            try {
                copy.join();
            } catch (InterruptedException e) {
                _logger.fatal(null, e);
                throw e;
            }
        }
    }

    public void stop() {
        for (Copy copy : _threadList) {
            copy._stop();
        }
    }

    public boolean isAlive() {
        for (Copy copy : _threadList) {
            if (copy.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public int getThreadCount() {
        return _threadList.size();
    }

    public void setThreadCount(int threadCount) {
        if (0 >= threadCount) {
            throw new IllegalArgumentException("Thread number must be strictly greater than 0");
        }
        _threadList.clear();
        for (int threadNumber = 1; threadNumber <= threadCount; threadNumber++) {
            Copy copy = new Copy();
            copy.setName("Copy #" + threadNumber);
            _threadList.add(copy);
        }
        setSrc(_src);
        setDst(_dst);
        setMessageManagerService(_messageManagerService);
        setFolderManagerService(_folderManagerService);
    }

    public IMessageManager getMessageManagerService() {
        return _messageManagerService;
    }

    public void setMessageManagerService(IMessageManager messageManager) {
        _messageManagerService = messageManager;
        for (Copy copy : _threadList) {
            copy.setMessageManager(messageManager);
        }
    }

    public IFolderManager getFolderManagerService() {
        return _folderManagerService;
    }

    public void setFolderManagerService(IFolderManager folderManager) {
        _folderManagerService = folderManager;
        for (Copy copy : _threadList) {
            copy.setFolderManager(folderManager);
        }
    }

    public void setSrc(URLName src) {
        _src = src;
        for (Copy copy : _threadList) {
            copy.setSrc(src);
        }
    }

    public void setDst(URLName dst) {
        _dst = dst;
        for (Copy copy : _threadList) {
            copy.setDst(dst);
        }
    }
}
