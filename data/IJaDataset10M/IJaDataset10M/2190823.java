package jsystem.runner.agent.publisher;

import java.util.Map;

/**
 * The publisher is used to manage the output results. You can move them to an
 * Ftp server like FtpPublisher or put them in an SQL server ...
 * 
 * @author guy.arieli
 * 
 */
public interface Publisher {

    /**
	 * publish the changed report. Usually used from the runner JVM.
	 * 
	 */
    void publish();

    /**
	 * Publish the changed report with the given HashMap. Usually used from the
	 * tests JVM
	 * 
	 * @param publishProperties
	 * @return publish information. Used for the email and other purposes.
	 * @throws Exception
	 */
    public jsystem.runner.remote.Message publish(Map<String, Object> publishProperties) throws Exception;

    /**
	 * This method will validate the publisher according to the setting received
	 * from the DB settings dialog.
	 * 
	 * @param Object
	 *            need to be cast to DBConnectionListener in case of
	 *            DefaultPublisher else if report server publisher will not use
	 *            this parameter. Passed as object due to dependency problem
	 *            between agent and app;
	 * @param dbSettingParams
	 *            represents the following: (host, port, driver, type, dbHost,
	 *            dbName, dbUser, dbPassword);
	 * @return
	 */
    boolean validatePublisher(Object object, String... dbSettingParams);
}
