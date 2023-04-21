package org.databene.contiperf;

import org.databene.contiperf.report.LoggerModuleAdapter;
import org.databene.contiperf.report.ReportModule;

/**
 * Deprecated Observer interface for ContiPerf 1.x.<br/><br/>
 * Created: 12.10.2009 08:11:23
 * @since 1.0
 * @author Volker Bergmann
 * @deprecated Replaced with {@link ReportModule}. 
 * When using a predefined ExecutionLogger, replace it with the corresponding ReportModule.
 * If the old version was
 * <pre>
 *     @Rule public ContiPerfRule = new ContiPerfRule(new ConsoleExecutionLogger());
 * </pre>
 * the new version would be
 * <pre>
 *     @Rule public ContiPerfRule = new ContiPerfRule(new ConsoleReportModule());
 * </pre>
 * Custom ExecutionLogger implementations still can be used by wrapping them with a {@link LoggerModuleAdapter}.
 * If the old version was
 * <pre>
 *     @Rule public ContiPerfRule = new ContiPerfRule(new MyCustomLogger());
 * </pre>
 * the new version would be
 * <pre>
 *     @Rule public ContiPerfRule = new ContiPerfRule(new LoggerModuleAdapter(new MyCustomLogger()));
 * </pre>
 */
@Deprecated
public interface ExecutionLogger {

    void logInvocation(String id, int latency, long startTime);

    void logSummary(String id, long elapsedTime, long invocationCount, long startTime);
}
