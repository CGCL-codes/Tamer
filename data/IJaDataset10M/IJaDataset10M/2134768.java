package db.JDBC;

public interface SQLQuerryConstants {

    public static final String ADD_DAY = "INSERT INTO day (id, name) VALUES (id,'";

    public static final String GET_DAY = "SELECT * FROM day";

    public static final String GET_DAY_BY_ID = "SELECT * FROM day WHERE (id='";

    public static final String GET_DAY_BY_NAME = "SELECT * FROM day WHERE (name='";

    public static final String DELETE_ALL_RECORD_DAY = "DELETE FROM day ";

    public static final String DELETE_RECORD_DAY_BY_ID = "DELETE FROM day WHERE (id='";

    public static final String DELETE_RECORD_DAY_BY_NAME = "DELETE FROM day WHERE (name = '";

    public static final String ADD_USER = "INSERT INTO user (id, daytOfBirth, familia, name, otchestvo, " + "nikName, userType, pasvord) values (id,'";

    public static final String GET_USER = "SELECT * FROM user";

    public static final String GET_USER_BY_ID = "SELECT * FROM user WHERE (id='";

    public static final String GET_USER_BY_DAYT_OF_BIRTH = "SELECT * FROM user WHERE (daytOfBirth='";

    public static final String GET_USER_BY_FAMILIA = "SELECT * FROM user WHERE (familia='";

    public static final String GET_USER_BY_NAME = "SELECT * FROM user WHERE (name='";

    public static final String GET_USER_BY_OTCHESTVO = "SELECT * FROM user WHERE (otchestvo='";

    public static final String GET_USER_BY_NIK_NAME = "SELECT * FROM user WHERE (nikName='";

    public static final String GET_USER_BY_USERTYPE = "SELECT * FROM user WHERE (userType='";

    public static final String GET_USER_BY_PASVORD = "SELECT * FROM user WHERE (pasvord='";

    public static final String DELETE_ALL_RECORD_USER = "DELETE FROM user ";

    public static final String DELETE_RECORD_USER_BY_ID = "DELETE FROM user WHERE (id='";

    public static final String DELETE_RECORD_USER_BY_DAYT_OF_BIRTH = "DELETE FROM user WHERE (daytOfBirth='";

    public static final String DELETE_RECORD_USER_BY_FAMILIA = "DELETE FROM user WHERE (familia='";

    public static final String DELETE_RECORD_USER_BY_NAME = "DELETE FROM user WHERE (name = '";

    public static final String DELETE_RECORD_USER_BY_OTCHESTVO = "DELETE FROM user WHERE (otchestvo='";

    public static final String DELETE_RECORD_USER_BY_NIK_NAME = "DELETE FROM user WHERE (nikName='";

    public static final String DELETE_RECORD_USER_BY_USERTYPE = "DELETE FROM user WHERE (userType='";

    public static final String DELETE_RECORD_USER_BY_PASVORD = "DELETE  FROM user WHERE (pasvord='";

    public static final String ADD_TASK = "INSERT INTO task (id, viewTask) values (id,'";

    public static final String GET_TASK = "SELECT * FROM task";

    public static final String GET_TASK_BY_ID = "SELECT * FROM task WHERE (id='";

    public static final String GET_TASK_BY_VIEWTASK = "SELECT * FROM task WHERE (viewTask='";

    public static final String DELETE_ALL_RECORD_TASK = "DELETE FROM task ";

    public static final String DELETE_RECORD_TASK_BY_ID = "DELETE FROM task WHERE (id='";

    public static final String DELETE_RECORD_TASK_BY_VIEWTASK = "DELETE FROM task WHERE (viewTask = '";

    public static final String ADD_STATE_TASK = "INSERT INTO statTask (id, stateTask) values (id,'";

    public static final String GET_STATE_TASK = "SELECT * FROM stateTask";

    public static final String GET_STATE_TASK_DAY_BY_ID = "SELECT * FROM stateTask WHERE (id='";

    public static final String GET_STATE_BY_STSTE = "SELECT * FROM stateTask WHERE (stateTask='";

    public static final String DELETE_ALL_RECORD_STATE_TASK = "DELETE FROM stateTask ";

    public static final String DELETE_RECORD_STATE_TASK_DAY_BY_ID = "DELETE FROM stateTask WHERE (id='";

    public static final String DELETE_RECORD_STATE_TASK_DAY_BY_STATE = "DELETE FROM stateTask WHERE (stateTask = '";

    public static final String ADD_TIMETABLE = "INSERT INTO timetable (id,	idStagingTableDay, idStateTask, idTask, idUsers, " + "nameTasca, textTasca, startDateTasca, stopDateTasca) values (id,'";

    public static final String GET_TIMETABLE = "SELECT * FROM timetable";

    public static final String GET_TIMETABLE_BY_ID = "SELECT * FROM timetable WHERE (id='";

    public static final String GET_TIMETABLE_BY_IDDAY = "SELECT * FROM timetable WHERE (idDay='";

    public static final String GET_TIMETABLE_BY_IDSTATETASK = "SELECT * FROM timetable WHERE (idStateTask='";

    public static final String GET_TIMETABLE_BY_IDTASK = "SELECT * FROM timetable WHERE (idTask='";

    public static final String GET_TIMETABLE_BY_IDUSER = "SELECT * FROM timetable WHERE (idUsers='";

    public static final String GET_TIMETABLE_BY_NAMETASKA = "SELECT * FROM timetable WHERE (nameTasca='";

    public static final String GET_TIMETABLE_BY_TEXTTASKA = "SELECT * FROM timetable WHERE (textTasca'";

    public static final String GET_TIMETABLE_BY_STARTDATETASKA = "SELECT * FROM timetable WHERE (startDateTasca='";

    public static final String GET_TIMETABLE_BY_STOPDATETASKA = "SELECT * FROM timetable WHERE (stopDateTasca='";

    public static final String GET_TIMETABLE_BY_STARTTIMETASKA = "SELECT * FROM timetable WHERE (startTimeTasca='";

    public static final String GET_TIMETABLE_BY_STOPTIMETASCA = "SELECT * FROM timetable WHERE (stopTimeTasca='";

    public static final String UPDATA_RECORD_TIMETABLE_BY_ID = "UPDATE timetable SET idStagingTableDay= '";

    public static final String DELETE_ALL_RECORD_TIMETABLE = "DELETE FROM timetable ";

    public static final String DELETE_RECORD_TIMETABLE_BY_ID = "DELETE FROM timetable WHERE (id='";

    public static final String DELETE_RECORD_TIMETABLE_BY_NAME_TASCA = "DELETE FROM timetable WHERE (nameTasca = '";

    public static final String ADD_STAGING_TABLE_DAY = "INSERT INTO stagingTableDay (id, idDay, idUser,	working) values (id,'";

    public static final String GET_STAGING_TABLE_DAY = "SELECT * FROM stagingTableDay";

    public static final String GET_STAGING_TABLE_DAY_BY_ID = "SELECT * FROM stagingTableDay WHERE (id='";

    public static final String GET_STAGING_TABLE_DAY_BY_IDDAY = "SELECT * FROM stagingTableDay WHERE (idDay='";

    public static final String GET_STAGING_TABLE_DAY_BY_IDUSER = "SELECT * FROM stagingTableDay WHERE (idUser='";

    public static final String GET_STAGING_TABLE_DAY_BY_WORKING = "SELECT * FROM stagingTableDay WHERE (working='";

    public static final String UPDATA_RECORD_STAGING_TABLE_DAY_BY_ID = "UPDATE stagingTableDay SET working=";

    public static final String DELETE_ALL_RECORD_STAGING_TABLE_DAY = "DELETE FROM stagingTableDay ";

    public static final String DELETE_RECORD_STAGING_TABLE_DAY_BY_ID = "DELETE FROM stagingTableDay WHERE (id='";

    public static final String DELETE_RECORD_STAGING_TABLE_DAY_BY_WORKING = "DELETE FROM stagingTableDay WHERE (working= '";
}
