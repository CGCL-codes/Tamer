package org.gecko.jee.community.mobidick.batchprocess;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.launch.support.JvmSystemExiter;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.batch.core.launch.support.SystemExiter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

public class CommandLineJobRunner {

    /**
	 * Main method. Used to launch a batch process.<br>
	 * 
	 * @param args
	 *            3 arguments passed to configure batch process execution
	 */
    public static final void main(final String[] args) {
        final CommandLineJobRunner command = new CommandLineJobRunner();
        if (args.length < 3) {
            command.exit(1);
        }
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" Arguments:");
        for (final String element : args) {
            stringBuffer.append(" ");
            stringBuffer.append(element);
        }
        final String configurationFile = args[0];
        final String jobName = args[1];
        final String propertiesFile = args[1];
        final String[] parameters = new String[args.length - 3];
        System.arraycopy(args, 3, parameters, 0, args.length - 3);
        final int result = command.start(configurationFile, jobName, propertiesFile, parameters);
        command.exit(result);
    }

    /**
	 * Spring XML BeanFactory configuration.
	 */
    protected XmlBeanFactory beanFactory;

    /**
	 * Exit code mapper. Maps the exit of the batch to a defined code<br>
	 * Default: 0=OK, 1=KO.
	 */
    private ExitCodeMapper exitCodeMapper = new SimpleJvmExitCodeMapper();

    /**
	 * Launcher for the job.
	 */
    private JobLauncher jobLauncher;

    /**
	 * Locator for the job (if using a job registry).
	 */
    private JobLocator jobLocator;

    /**
	 * System handler to exit java batch process.
	 */
    private SystemExiter systemExiter = new JvmSystemExiter();

    /**
	 * Converter for batch's parameters. Permits access to parameters through
	 * the batch.
	 */
    private JobParametersConverter jobParametersConverter = new DefaultJobParametersConverter();

    /**
	 * Use the SystemExiter to do a clean exit (if possible) of the VM.
	 * 
	 * @param status
	 *            code for exit 0=OK, 1=KO.
	 */
    public final void exit(final int status) {
        systemExiter.exit(status);
    }

    /**
	 * Setter for Spring injection
	 * 
	 * @param exitCodeMapper
	 *            mapper
	 */
    public final void setExitCodeMapper(final ExitCodeMapper exitCodeMapper) {
        this.exitCodeMapper = exitCodeMapper;
    }

    /**
	 * @param launcher
	 */
    public final void setJobLauncher(final JobLauncher launcher) {
        jobLauncher = launcher;
    }

    /**
	 * @param jobLocator
	 */
    public final void setJobLocator(final JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }

    /**
	 * @param jobParametersConverter
	 */
    public final void setJobParametersConverter(final JobParametersConverter jobParametersConverter) {
        this.jobParametersConverter = jobParametersConverter;
    }

    /**
	 * @param systemExitor
	 */
    public final void setSystemExiter(final SystemExiter systemExitor) {
        systemExiter = systemExitor;
    }

    /**
	 * Main method to start the batch-process.
	 * 
	 * @param configurationFile
	 *            path of the Spring XML configuration file. This file should be
	 *            in the classpath.
	 * @param jobName
	 *            name of the job
	 * @param propertiesFile
	 *            path of the property file
	 * @param parameters
	 *            execution parameters
	 * @return int: exit code (defined by the exitCodeMapper)
	 */
    public final int start(final String configurationFile, final String jobName, final String propertiesFile, final String[] parameters) {
        try {
            beanFactory = new XmlBeanFactory(new ClassPathResource(configurationFile));
            final PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
            cfg.setLocation(new ClassPathResource(propertiesFile));
            cfg.postProcessBeanFactory(beanFactory);
            beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            Job job;
            if (jobLocator != null) {
                job = jobLocator.getJob(jobName);
            } else {
                job = (Job) beanFactory.getBean(jobName);
            }
            final JobParameters jobParameters = jobParametersConverter.getJobParameters(StringUtils.splitArrayElementsIntoProperties(parameters, "="));
            final JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            return exitCodeMapper.intValue(jobExecution.getExitStatus().getExitCode());
        } catch (final Throwable e) {
            return exitCodeMapper.intValue(ExitStatus.FAILED.getExitCode());
        } finally {
            if (beanFactory != null) {
            }
        }
    }
}
