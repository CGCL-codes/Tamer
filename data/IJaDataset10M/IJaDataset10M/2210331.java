package com.mdt.toodledo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import com.mdt.rtm.data.RtmData;
import com.mdt.toodledo.data.Context;
import com.mdt.toodledo.data.Folder;
import com.mdt.toodledo.data.Task;

public class ServiceImplementation implements Service {

    private static final Log log = LogFactory.getLog("Service");

    public static final SimpleDateFormat REMOTE_SERVICE_DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);

    private Invoker invoker;

    private String key;

    private GregorianCalendar remoteServerDate;

    private int betweenLocalAndRemoteLagInHours = 0;

    private static final Map<String, Class> GET_TASKS_ALLOWED_PARAMETERS;

    private static final Map<String, Class> CREATE_TASK_ALLOWED_PARAMETERS;

    private static final Map<String, Class> EDIT_TASK_ALLOWED_PARAMETERS;

    private static final Map<String, Class> CREATE_FOLDER_ALLOWED_PARAMETERS;

    static {
        CREATE_TASK_ALLOWED_PARAMETERS = new Hashtable<String, Class>();
        CREATE_TASK_ALLOWED_PARAMETERS.put("note", String.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("tag", String.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("folder", Integer.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("startdate", Date.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("duedate", Date.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("duetime", Date.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("priority", Task.Priority.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("completed", Boolean.class);
        CREATE_TASK_ALLOWED_PARAMETERS.put("context", String.class);
        EDIT_TASK_ALLOWED_PARAMETERS = new Hashtable<String, Class>();
        EDIT_TASK_ALLOWED_PARAMETERS.put("title", String.class);
        for (Entry<String, Class> entry : CREATE_TASK_ALLOWED_PARAMETERS.entrySet()) {
            EDIT_TASK_ALLOWED_PARAMETERS.put(entry.getKey(), entry.getValue());
        }
        GET_TASKS_ALLOWED_PARAMETERS = new Hashtable<String, Class>();
        GET_TASKS_ALLOWED_PARAMETERS.put("id", String.class);
        GET_TASKS_ALLOWED_PARAMETERS.put("folder", String.class);
        GET_TASKS_ALLOWED_PARAMETERS.put("modafter", String.class);
        GET_TASKS_ALLOWED_PARAMETERS.put("notcomp", Boolean.class);
        CREATE_FOLDER_ALLOWED_PARAMETERS = new Hashtable<String, Class>();
        CREATE_FOLDER_ALLOWED_PARAMETERS.put("private", Boolean.class);
    }

    public ServiceImplementation() {
        invoker = new Invoker(SERVER_HOST_NAME, SERVER_PORT_NUMBER, REST_SERVICE_URL_POSTFIX);
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) buf.append((char) ('0' + halfbyte)); else buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public String computeMD5(String string) throws ServiceException {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.reset();
            digest.update(string.getBytes(Invoker.ENCODING));
            return convertToHex(digest.digest());
        } catch (NoSuchAlgorithmException exception) {
            String message = "Could not create properly the MD5 digest";
            log.error(message, exception);
            throw new ServiceException(message, exception);
        } catch (UnsupportedEncodingException exception) {
            String message = "Could not encode properly the MD5 digest";
            log.error(message, exception);
            throw new ServiceException(message, exception);
        }
    }

    private void handleUnexpectedContents(String methodName, String expectedNodeName, Element result) throws ServiceException {
        String nodeName = result.getNodeName();
        if (nodeName.equals(expectedNodeName) == false) {
            String message = "The result contents of the call to the web method '" + methodName + "' is unexpected: was expecting a top node with name '" + expectedNodeName + "'";
            log.error(message);
            throw new ServiceException(message);
        }
    }

    public String getUserId(String email, String password) throws ServiceException {
        String methodName = "getUserid";
        Param<?> params[] = new Param<?>[] { new Param<String>("method", methodName), new Param<String>("email", email), new Param<String>("pass", password) };
        Element result = invoker.invoke(params);
        handleUnexpectedContents(methodName, "userid", result);
        String userId = Task.getTextNodeContent(result);
        if (userId.equals("0")) {
            throw new ServiceException("Cannot get the user id: the provided e-mail '" + email + "' or password '" + password + "' must be blank");
        }
        if (userId.equals("1")) {
            throw new ServiceException("Cannot get the user id: it seems that no Toodledo account corresponding to the e-mail '" + email + "' and password '" + password + "' exists");
        }
        return userId;
    }

    public String getToken(String userId) throws ServiceException {
        return getToken(userId, null);
    }

    public String getToken(String userId, String applicationId) throws ServiceException {
        final String methodName = "getToken";
        Param<?> params[];
        if (applicationId != null) {
            params = new Param<?>[] { new Param<String>("method", methodName), new Param<String>("userid", userId), new Param<String>("appid", applicationId) };
        } else {
            params = new Param<?>[] { new Param<String>("method", methodName), new Param<String>("userid", userId) };
        }
        Element result = invoker.invoke(params);
        handleUnexpectedContents(methodName, "token", result);
        return Task.getTextNodeContent(result);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey(String userId, String password, String token) throws ServiceException {
        String encodedPassword = computeMD5(password);
        return computeMD5(encodedPassword + token + userId);
    }

    public String getNewKey(String applicationId, String userId, String password) throws ServiceException {
        key = getKey(userId, password, getToken(userId, applicationId));
        return key;
    }

    private static void checkParameters(String methodName, Param<?>[] parameters, Map<String, Class> allowedParameters) throws ServiceException {
        for (Param<?> parameter : parameters) {
            if (allowedParameters.containsKey(parameter.getName()) == false) {
                throw new ServiceException("The method '" + methodName + "' does not support the parameter with name '" + parameter.getName() + "'");
            }
        }
    }

    /**
   * Also adds the "key" parameter, and the "appid" if defined.
   */
    private Param<?>[] computeAllParameters(Param<?>[] baseParameters, Param<?>[] additionalParameters) {
        int additionalLength = 0;
        if (additionalParameters != null) {
            additionalLength = additionalParameters.length;
        }
        Param<?>[] allParameters = new Param[baseParameters.length + 1 + additionalLength];
        int index = 0;
        for (Param<?> parameter : baseParameters) {
            allParameters[index] = parameter;
            index++;
        }
        allParameters[index] = new Param<String>("key", key);
        index++;
        if (additionalParameters != null) {
            for (Param<?> parameter : additionalParameters) {
                allParameters[index] = parameter;
                index++;
            }
        }
        return allParameters;
    }

    public GregorianCalendar getServerDate() throws ServiceException {
        String methodName = "getServerInfo";
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName) }, null);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "server", result);
        String rawDateAsString = Task.getTextNodeContent(RtmData.child(result, "date"));
        GregorianCalendar remoteDate = new GregorianCalendar();
        try {
            Date parsedRawDate = REMOTE_SERVICE_DATE_FORMAT.parse(rawDateAsString);
            remoteDate.setTime(parsedRawDate);
        } catch (ParseException exception) {
            throw new ServiceException("Could not parse properly the remote server date '" + rawDateAsString + "' with the pattern '" + REMOTE_SERVICE_DATE_FORMAT.toPattern() + "'");
        }
        return remoteDate;
    }

    public GregorianCalendar takeIntoAccountRemoteServerDate() throws ServiceException {
        if (remoteServerDate == null) {
            GregorianCalendar now = new GregorianCalendar();
            remoteServerDate = getServerDate();
            double gapInMilliseconds = now.getTime().getTime() - remoteServerDate.getTime().getTime();
            betweenLocalAndRemoteLagInHours = (int) Math.rint(gapInMilliseconds / (1000.0d * 3600.0d));
        }
        return remoteServerDate;
    }

    public List<Task> getTasks(Param... parameters) throws ServiceException {
        final String methodName = "getTasks";
        checkParameters(methodName, parameters, GET_TASKS_ALLOWED_PARAMETERS);
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName) }, parameters);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "tasks", result);
        return Task.getTasks(result, betweenLocalAndRemoteLagInHours);
    }

    public List<Task> getTasksFromFolder(String folderId) throws ServiceException {
        return getTasks(new Param("folder", folderId));
    }

    public List<Task> getTasksFromFolder(String folderId, boolean notCompleted) throws ServiceException {
        return getTasks(new Param("folder", folderId), new Param("notcomp", notCompleted));
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTasksFromFolderModifiedAfter(String folderId, Date date) throws ServiceException {
        final GregorianCalendar dateAdjusted = new GregorianCalendar();
        dateAdjusted.setTime(date);
        dateAdjusted.add(Calendar.HOUR, -betweenLocalAndRemoteLagInHours);
        return getTasks(new Param("folder", folderId), new Param("modafter", Service.DATE_FORMAT.format(dateAdjusted.getTime())));
    }

    public List<Task> getTasksFromFolderModifiedAfter(String folderId, Date date, boolean notCompleted) throws ServiceException {
        final GregorianCalendar dateAdjusted = new GregorianCalendar();
        dateAdjusted.setTime(date);
        dateAdjusted.add(Calendar.HOUR, -betweenLocalAndRemoteLagInHours);
        return getTasks(new Param("folder", folderId), new Param("modafter", Service.DATE_FORMAT.format(dateAdjusted.getTime())), new Param("notcomp", notCompleted));
    }

    public Task getTask(String taskId) throws ServiceException {
        List<Task> tasks = getTasks(new Param[] { new Param<String>("id", taskId) });
        if (tasks.size() == 0) {
            throw new ServiceException("There is no task with id '" + taskId + "'");
        }
        if (tasks.size() > 1) {
            throw new ServiceException("Toodledo internal error: there is more than one task with id '" + taskId + "'");
        }
        return tasks.get(0);
    }

    public List<Task> getDeletedTasks(Date after) throws ServiceException {
        final String methodName = "getDeleted";
        final GregorianCalendar afterAdjusted = new GregorianCalendar();
        afterAdjusted.setTime(after);
        afterAdjusted.add(Calendar.HOUR, -betweenLocalAndRemoteLagInHours);
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName) }, new Param[] { new Param<String>("after", DATE_FORMAT.format(afterAdjusted.getTime())) });
        final Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "deleted", result);
        return Task.getTasks(result, betweenLocalAndRemoteLagInHours, true);
    }

    public List<Folder> getFolders() throws ServiceException {
        String methodName = "getFolders";
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName) }, null);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "folders", result);
        return Folder.getFolders(result);
    }

    public Folder getFolder(String folderId) throws ServiceException {
        List<Folder> folders = getFolders();
        if (folders.size() == 0) {
            throw new ServiceException("There is no folder with id '" + folderId + "'");
        }
        Folder foundFolder = null;
        for (Folder folder : folders) {
            if (folder.getId().equals(folderId)) {
                if (foundFolder != null) {
                    throw new ServiceException("Toodledo internal error: there is more than one folder with id '" + folderId + "'");
                }
                foundFolder = folder;
            }
        }
        if (foundFolder == null) {
            throw new ServiceException("There is no folder with id '" + folderId + "'");
        }
        return foundFolder;
    }

    public String createTask(String title, Param... parameters) throws ServiceException {
        String methodName = "addTask";
        checkParameters(methodName, parameters, CREATE_TASK_ALLOWED_PARAMETERS);
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName), new Param<String>("title", title) }, parameters);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "added", result);
        return Task.getTextNodeContent(result);
    }

    public boolean editTask(String taskId, Param... parameters) throws ServiceException {
        String methodName = "editTask";
        checkParameters(methodName, parameters, EDIT_TASK_ALLOWED_PARAMETERS);
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName), new Param<String>("id", taskId) }, parameters);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "success", result);
        return Task.getTextNodeContent(result).equals("1") ? true : false;
    }

    public boolean deleteTask(String taskId) throws ServiceException {
        String methodName = "deleteTask";
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName), new Param<String>("id", taskId) }, null);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "success", result);
        return Task.getTextNodeContent(result).equals("1") ? true : false;
    }

    public String createFolder(String title, Param... parameters) throws ServiceException {
        String methodName = "addFolder";
        checkParameters(methodName, parameters, CREATE_FOLDER_ALLOWED_PARAMETERS);
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName), new Param<String>("title", title) }, parameters);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "added", result);
        return Task.getTextNodeContent(result);
    }

    public boolean deleteFolder(String folderId) throws ServiceException {
        String methodName = "deleteFolder";
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName), new Param<String>("id", folderId) }, null);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "success", result);
        return Task.getTextNodeContent(result).equals("1") ? true : false;
    }

    public List<Context> getContexts() throws ServiceException {
        String methodName = "getContexts";
        Param<?>[] allParameters = computeAllParameters(new Param[] { new Param<String>("method", methodName) }, null);
        Element result = invoker.invoke(allParameters);
        handleUnexpectedContents(methodName, "contexts", result);
        return Context.getContexts(result);
    }
}
