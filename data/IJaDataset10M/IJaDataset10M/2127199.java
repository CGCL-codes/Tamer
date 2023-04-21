package windowsserver.scheduledtasksandservices;

public interface ServicesTasks {

    public String getAllRunningServices(String key);

    public boolean stopService(String serviceName, String key);

    public boolean startService(String serviceName, String key);

    public boolean resumeService(String serviceName, String key);

    public boolean pauseService(String serviceName, String key);

    public boolean restartService(String serivceName, String key);

    public boolean changeStartType(String serviceName, String startMode, String key);

    public boolean createDailyTask(String taskPath, String taskName, String startTime, String startDate, int everyNumOfDays, String key);

    public boolean createweeklyTask(String taskPath, String taskName, String startTime, int everyNumOfWeeks, String daysOfWeeks, String key);

    public boolean createmonthlyTask(String taskPath, String taskName, String startTime, int dayOfMonth, String months, String key);

    public boolean createoneTimeOnlyTask(String taskPath, String taskName, String startTime, String startDate, String key);

    public boolean createWhenComputerStartTask(String taskPath, String taskName, String key);

    public boolean createOnLoginTask(String taskPath, String taskName, String key);

    public boolean createOnIdleTask(String taskPath, String taskName, int idleTime, String key);

    public boolean deleteTask(String taskName, String key);

    public String listAllTasks(String key);
}
