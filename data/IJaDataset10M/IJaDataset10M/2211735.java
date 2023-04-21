package com.mdt.toodledo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.mdt.toodledo.data.Context;
import com.mdt.toodledo.data.Folder;
import com.mdt.toodledo.data.Task;

/**
 * Represents the ToodleDo API.
 * 
 * @author Edouard Mercier
 * @since 2008.05.13
 */
public interface Service {

    public static final String SERVER_HOST_NAME = "www.toodledo.com";

    public static final int SERVER_PORT_NUMBER = 80;

    public static final String REST_SERVICE_URL_POSTFIX = "/api.php";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String getUserId(String email, String password) throws ServiceException;

    String getToken(String userId) throws ServiceException;

    String getToken(String userId, String applicationId) throws ServiceException;

    String computeMD5(String string) throws ServiceException;

    /**
   * @return the already internally computed key. Should only be invoked once {@link #getNewKey()} or {@link #setKey()} has already been invoked
   */
    String getKey();

    /**
   * When using that method, it is not necessary to invoke the {@link #getNewKey()} method.
   */
    void setKey(String key);

    String getKey(String userId, String password, String token) throws ServiceException;

    String getNewKey(String applicationId, String userId, String password) throws ServiceException;

    GregorianCalendar getServerDate() throws ServiceException;

    GregorianCalendar takeIntoAccountRemoteServerDate() throws ServiceException;

    @SuppressWarnings("unchecked")
    List<Task> getTasks(Param... parameters) throws ServiceException;

    List<Task> getTasksFromFolder(String folderId) throws ServiceException;

    List<Task> getTasksFromFolder(String folderId, boolean notCompleted) throws ServiceException;

    List<Task> getTasksFromFolderModifiedAfter(String folderId, Date date) throws ServiceException;

    List<Task> getTasksFromFolderModifiedAfter(String folderId, Date date, boolean notCompleted) throws ServiceException;

    Task getTask(String taskId) throws ServiceException;

    List<Task> getDeletedTasks(Date after) throws ServiceException;

    List<Folder> getFolders() throws ServiceException;

    Folder getFolder(String folderId) throws ServiceException;

    String createTask(String title, Param... parameters) throws ServiceException;

    boolean editTask(String taskId, Param... parameters) throws ServiceException;

    boolean deleteTask(String taskId) throws ServiceException;

    String createFolder(String title, Param... parameters) throws ServiceException;

    boolean deleteFolder(String folderId) throws ServiceException;

    List<Context> getContexts() throws ServiceException;
}
