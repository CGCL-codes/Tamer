package net.grinder.engine.process;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import net.grinder.common.GrinderBuild;
import net.grinder.common.GrinderException;
import net.grinder.common.GrinderProperties;
import net.grinder.common.Logger;
import net.grinder.common.processidentity.WorkerProcessReport;
import net.grinder.communication.ClientSender;
import net.grinder.communication.CommunicationException;
import net.grinder.communication.ConnectionType;
import net.grinder.communication.Message;
import net.grinder.communication.MessageDispatchSender;
import net.grinder.communication.MessagePump;
import net.grinder.communication.QueuedSender;
import net.grinder.communication.QueuedSenderDecorator;
import net.grinder.communication.Receiver;
import net.grinder.engine.common.ConnectorFactory;
import net.grinder.engine.common.EngineException;
import net.grinder.engine.communication.ConsoleListener;
import net.grinder.engine.messages.InitialiseGrinderMessage;
import net.grinder.engine.process.jython.JythonScriptEngine;
import net.grinder.messages.console.RegisterTestsMessage;
import net.grinder.statistics.ExpressionView;
import net.grinder.statistics.StatisticsServicesImplementation;
import net.grinder.statistics.StatisticsTable;
import net.grinder.statistics.TestStatisticsMap;
import net.grinder.util.JVM;
import net.grinder.util.thread.BooleanCondition;
import net.grinder.util.thread.Condition;

/**
 * The controller for a worker process.
 *
 * <p>Package scope.</p>
 *
 * @author Paco Gomez
 * @author Philip Aston
 * @version $Revision: 3878 $
 * @see net.grinder.engine.process.GrinderThread
 */
final class GrinderProcess {

    private final ProcessContext m_context;

    private final LoggerImplementation m_loggerImplementation;

    private final InitialiseGrinderMessage m_initialisationMessage;

    private final ConsoleListener m_consoleListener;

    private final TestStatisticsMap m_accumulatedStatistics;

    private final Condition m_eventSynchronisation = new Condition();

    private final MessagePump m_messagePump;

    private boolean m_shutdownTriggered;

    private boolean m_communicationShutdown;

    /**
   * Creates a new <code>GrinderProcess</code> instance.
   *
   * @param agentReceiver
   *          Receiver used to listen to the agent.
   * @exception GrinderException
   *          If the process could not be created.
   */
    public GrinderProcess(Receiver agentReceiver) throws GrinderException {
        m_initialisationMessage = (InitialiseGrinderMessage) agentReceiver.waitForMessage();
        if (m_initialisationMessage == null) {
            throw new EngineException("No control stream from agent");
        }
        final GrinderProperties properties = m_initialisationMessage.getProperties();
        m_loggerImplementation = new LoggerImplementation(m_initialisationMessage.getWorkerIdentity().getName(), properties.getProperty("grinder.logDirectory", "."), properties.getBoolean("grinder.logProcessStreams", true), properties.getInt("grinder.numberOfOldLogs", 1));
        final Logger processLogger = m_loggerImplementation.getProcessLogger();
        processLogger.output("The Grinder version " + GrinderBuild.getVersionString());
        processLogger.output(JVM.getInstance().toString());
        processLogger.output("time zone is " + new SimpleDateFormat("z (Z)").format(new Date()));
        final QueuedSender consoleSender;
        if (m_initialisationMessage.getReportToConsole()) {
            consoleSender = new QueuedSenderDecorator(ClientSender.connect(new ConnectorFactory(ConnectionType.WORKER).create(properties)));
        } else {
            consoleSender = new QueuedSender() {

                public void send(Message message) {
                }

                public void flush() {
                }

                public void queue(Message message) {
                }

                public void shutdown() {
                }
            };
        }
        m_context = new ProcessContextImplementation(m_initialisationMessage.getWorkerIdentity(), properties, processLogger, m_loggerImplementation.getFilenameFactory(), consoleSender, StatisticsServicesImplementation.getInstance());
        try {
            java.net.InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }
        m_consoleListener = new ConsoleListener(m_eventSynchronisation, processLogger);
        m_accumulatedStatistics = new TestStatisticsMap(m_context.getStatisticsServices().getStatisticsSetFactory());
        final MessageDispatchSender messageDispatcher = new MessageDispatchSender();
        m_consoleListener.registerMessageHandlers(messageDispatcher);
        m_messagePump = new MessagePump(agentReceiver, messageDispatcher, 1);
    }

    /**
   * The application's main loop. This is split from the constructor as
   * theoretically it might be called multiple times. The constructor sets up
   * the static configuration, this does a single execution.
   *
   * <p>
   * This method is interruptible, in the same sense as
   * {@link net.grinder.util.thread.InterruptibleRunnable#interruptibleRun()}.
   * We don't implement that method because we want to be able to throw
   * exceptions.
   * </p>
   *
   * @throws GrinderException
   *           If something went wrong.
   */
    public void run() throws GrinderException {
        final Logger logger = m_context.getProcessLogger();
        final Timer timer = new Timer(true);
        timer.schedule(new TickLoggerTimerTask(), 0, 1000);
        final ScriptEngine scriptEngine = new JythonScriptEngine(m_context.getScriptContext());
        m_context.getTestRegistry().setInstrumenter(scriptEngine);
        final StringBuffer numbers = new StringBuffer("worker process ");
        numbers.append(m_initialisationMessage.getWorkerIdentity().getNumber());
        final int agentNumber = m_initialisationMessage.getWorkerIdentity().getAgentIdentity().getNumber();
        if (agentNumber >= 0) {
            numbers.append(" of agent number ");
            numbers.append(agentNumber);
        }
        logger.output(numbers.toString());
        logger.output("executing \"" + m_initialisationMessage.getScript() + "\" using " + scriptEngine.getDescription());
        scriptEngine.initialise(m_initialisationMessage.getScript());
        final GrinderProperties properties = m_context.getProperties();
        final short numberOfThreads = properties.getShort("grinder.threads", (short) 1);
        final int reportToConsoleInterval = properties.getInt("grinder.reportToConsole.interval", 500);
        final int duration = properties.getInt("grinder.duration", 0);
        final PrintWriter dataWriter = m_loggerImplementation.getDataWriter();
        dataWriter.print("Thread, Run, Test, Start time (ms since Epoch)");
        final ExpressionView[] detailExpressionViews = m_context.getStatisticsServices().getDetailStatisticsView().getExpressionViews();
        for (int i = 0; i < detailExpressionViews.length; ++i) {
            dataWriter.print(", " + detailExpressionViews[i].getDisplayName());
        }
        dataWriter.println();
        final QueuedSender consoleSender = m_context.getConsoleSender();
        consoleSender.send(m_context.createStatusMessage(WorkerProcessReport.STATE_STARTED, (short) 0, numberOfThreads));
        logger.output("starting threads", Logger.LOG | Logger.TERMINAL);
        final GrinderThread[] runnable = new GrinderThread[numberOfThreads];
        final ThreadSynchronisation threadSynchronisation = new ThreadSynchronisation(m_eventSynchronisation, numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            runnable[i] = new GrinderThread(threadSynchronisation, m_context, m_loggerImplementation, scriptEngine, i);
            final Thread t = new Thread(runnable[i], "Grinder thread " + i);
            t.setDaemon(true);
            t.start();
        }
        threadSynchronisation.startThreads();
        m_context.setExecutionStartTime();
        logger.output("start time is " + m_context.getExecutionStartTime() + " ms since Epoch");
        final TimerTask reportTimerTask = new ReportToConsoleTimerTask(threadSynchronisation);
        final TimerTask shutdownTimerTask = new ShutdownTimerTask();
        reportTimerTask.run();
        timer.schedule(reportTimerTask, reportToConsoleInterval, reportToConsoleInterval);
        try {
            if (duration > 0) {
                logger.output("will shutdown after " + duration + " ms", Logger.LOG | Logger.TERMINAL);
                timer.schedule(shutdownTimerTask, duration);
            }
            synchronized (m_eventSynchronisation) {
                while (!threadSynchronisation.isFinished()) {
                    if (m_consoleListener.checkForMessage(ConsoleListener.ANY ^ ConsoleListener.START)) {
                        break;
                    }
                    if (m_shutdownTriggered) {
                        logger.output("specified duration exceeded, shutting down", Logger.LOG | Logger.TERMINAL);
                        break;
                    }
                    m_eventSynchronisation.waitNoInterrruptException();
                }
            }
            synchronized (m_eventSynchronisation) {
                if (!threadSynchronisation.isFinished()) {
                    logger.output("waiting for threads to terminate", Logger.LOG | Logger.TERMINAL);
                    m_context.shutdown();
                    final long time = System.currentTimeMillis();
                    final long maximumShutdownTime = 10000;
                    while (!threadSynchronisation.isFinished()) {
                        if (System.currentTimeMillis() - time > maximumShutdownTime) {
                            logger.output("ignoring unresponsive threads", Logger.LOG | Logger.TERMINAL);
                            break;
                        }
                        m_eventSynchronisation.waitNoInterrruptException(maximumShutdownTime);
                    }
                }
            }
        } finally {
            reportTimerTask.cancel();
            shutdownTimerTask.cancel();
        }
        scriptEngine.shutdown();
        reportTimerTask.run();
        m_loggerImplementation.getDataWriter().close();
        if (!m_communicationShutdown) {
            consoleSender.send(m_context.createStatusMessage(WorkerProcessReport.STATE_FINISHED, (short) 0, (short) 0));
        }
        consoleSender.shutdown();
        logger.output("Final statistics for this process:");
        final StatisticsTable statisticsTable = new StatisticsTable(m_context.getStatisticsServices().getSummaryStatisticsView(), m_accumulatedStatistics);
        statisticsTable.print(logger.getOutputLogWriter());
        timer.cancel();
        logger.output("finished", Logger.LOG | Logger.TERMINAL);
    }

    public void shutdown(boolean inputStreamIsStdin) {
        if (!inputStreamIsStdin) {
            m_messagePump.shutdown();
        }
        m_loggerImplementation.close();
    }

    public Logger getLogger() {
        return m_context.getProcessLogger();
    }

    private class ReportToConsoleTimerTask extends TimerTask {

        private final ThreadSynchronisation m_threads;

        public ReportToConsoleTimerTask(ThreadSynchronisation threads) {
            m_threads = threads;
        }

        public void run() {
            m_loggerImplementation.getDataWriter().flush();
            if (!m_communicationShutdown) {
                final QueuedSender consoleSender = m_context.getConsoleSender();
                try {
                    final TestStatisticsMap sample = m_context.getTestRegistry().getTestStatisticsMap().reset();
                    m_accumulatedStatistics.add(sample);
                    final Collection newTests = m_context.getTestRegistry().getNewTests();
                    if (newTests != null) {
                        consoleSender.queue(new RegisterTestsMessage(newTests));
                    }
                    if (sample.size() > 0) {
                        consoleSender.queue(m_context.createReportStatisticsMessage(sample));
                    }
                    consoleSender.send(m_context.createStatusMessage(WorkerProcessReport.STATE_RUNNING, m_threads.getNumberOfRunningThreads(), m_threads.getTotalNumberOfThreads()));
                } catch (CommunicationException e) {
                    final Logger logger = m_context.getProcessLogger();
                    logger.output("Report to console failed: " + e.getMessage(), Logger.LOG | Logger.TERMINAL);
                    e.printStackTrace(logger.getErrorLogWriter());
                    m_communicationShutdown = true;
                }
            }
        }
    }

    private class ShutdownTimerTask extends TimerTask {

        public void run() {
            synchronized (m_eventSynchronisation) {
                m_shutdownTriggered = true;
                m_eventSynchronisation.notifyAll();
            }
        }
    }

    private static class TickLoggerTimerTask extends TimerTask {

        public void run() {
            LoggerImplementation.tick();
        }
    }

    /**
   * Implement {@link WorkerThreadSynchronisation}. I looked hard at JSR 166's
   * <code>CountDownLatch</code> and <code>CyclicBarrier</code>, but neither
   * of them allow for the waiting thread to be interrupted by other events.
   *
   * <p>Package scope for unit tests.</p>
   *
   * @author Philip Aston
   * @version $Revision: 3878 $
   */
    static class ThreadSynchronisation implements WorkerThreadSynchronisation {

        private final BooleanCondition m_started = new BooleanCondition();

        private final Condition m_threadEventCondition;

        private final short m_totalNumberOfThreads;

        private short m_numberAwaitingStart;

        private short m_numberFinished;

        ThreadSynchronisation(Condition condition, short numberOfThreads) {
            m_threadEventCondition = condition;
            m_totalNumberOfThreads = numberOfThreads;
            m_numberAwaitingStart = 0;
        }

        /**
     * The number of worker threads that have been started but not run to
     * completion.
     */
        public short getNumberOfRunningThreads() {
            synchronized (m_threadEventCondition) {
                return (short) (m_totalNumberOfThreads - m_numberFinished);
            }
        }

        public boolean isReadyToStart() {
            synchronized (m_threadEventCondition) {
                return m_numberAwaitingStart >= getNumberOfRunningThreads();
            }
        }

        public boolean isFinished() {
            return getNumberOfRunningThreads() <= 0;
        }

        /**
     * The number of worker threads that have been created.
     */
        public short getTotalNumberOfThreads() {
            return m_totalNumberOfThreads;
        }

        public void startThreads() {
            synchronized (m_threadEventCondition) {
                while (!isReadyToStart()) {
                    m_threadEventCondition.waitNoInterrruptException();
                }
                m_numberAwaitingStart = 0;
            }
            m_started.set(true);
        }

        public void awaitStart() {
            synchronized (m_threadEventCondition) {
                ++m_numberAwaitingStart;
                if (isReadyToStart()) {
                    m_threadEventCondition.notifyAll();
                }
            }
            m_started.await(true);
        }

        public void threadFinished() {
            synchronized (m_threadEventCondition) {
                ++m_numberFinished;
                if (isReadyToStart() || isFinished()) {
                    m_threadEventCondition.notifyAll();
                }
            }
        }
    }
}
