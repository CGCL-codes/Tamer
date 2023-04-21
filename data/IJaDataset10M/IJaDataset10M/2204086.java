package com.narirelays.ems.utils;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJUnitRunListener extends RunListener {

    private static final Logger log = LoggerFactory.getLogger(MyJUnitRunListener.class);

    private boolean currentTestSucceed = true;

    public void testRunStarted(Description description) throws Exception {
        if (!"null".equalsIgnoreCase(description.getDisplayName())) {
            log.info(String.format("测试 \"%s\"启动", description.getDisplayName()));
        } else {
            if (description.getChildren() != null && description.getChildren().size() > 0) {
                log.info(String.format("测试 \"%s\"启动", description.getChildren().get(0).getDisplayName()));
            } else {
                log.info("测试启动时间");
            }
        }
    }

    public void testRunFinished(Result result) throws Exception {
        int total = result.getRunCount();
        int failCount = result.getFailureCount();
        int ignoreCount = result.getIgnoreCount();
        int succeedCount = total - failCount - ignoreCount;
        log.info(String.format("测试 结束,运行[%d] 成功[%d] 失败[%d] 忽略[%d] 持续%d毫秒", total, succeedCount, failCount, ignoreCount, result.getRunTime()));
        log.info("**********************测试报告开始**********************");
        log.info(String.format("测试 结束,运行[%d] 成功[%d] 失败[%d] 忽略[%d] 持续%d毫秒", total, succeedCount, failCount, ignoreCount, result.getRunTime()));
        log.info("+失败的测试");
        for (int i = 0; i < result.getFailureCount(); i++) {
            Failure failure = result.getFailures().get(i);
            log.info(String.format("%d.%s 原因:%s", i + 1, failure.getDescription(), failure.getMessage()));
            String className = failure.getDescription().getClassName();
            String methodName = failure.getDescription().getMethodName();
            String traceLog = failure.getTrace();
            log.info(String.format("\t%s %s", failure.getException(), getTraceFileLineDescription(traceLog, className + "." + methodName)));
        }
        log.info("**********************测试报告结束**********************");
    }

    private static String getTraceFileLineDescription(String traceLog, String classMethod) {
        if (traceLog != null) {
            if (classMethod != null) {
                int index = traceLog.indexOf(classMethod);
                if (index >= 0) {
                    int index1 = traceLog.indexOf("\n", index + 1);
                    if (index1 >= 0) {
                        String fileLocation = traceLog.substring(index, index1 - 1);
                        return fileLocation;
                    }
                }
            }
        }
        return traceLog;
    }

    public void testStarted(Description description) throws Exception {
        currentTestSucceed = true;
        if (description.getMethodName() != null) {
            log.info(String.format("\t测试 方法\"%s\"启动", description.getMethodName()));
        } else if (description.getDisplayName() != null) {
            log.info(String.format("\t测试 \"%s\"启动", description.getMethodName()));
        }
    }

    public void testFinished(Description description) throws Exception {
        if (description.getMethodName() != null) {
            if (currentTestSucceed) {
                log.info(String.format("\t测试 方法\"%s\"结束", description.getMethodName()));
            } else {
                log.info(String.format("\t测试 方法\"%s\"结束", description.getMethodName()));
            }
        } else if (description.getDisplayName() != null) {
            if (currentTestSucceed) {
                log.info(String.format("\t测试 \"%s\"结束", description.getMethodName()));
            } else {
                log.info(String.format("\t测试 \"%s\"结束", description.getMethodName()));
            }
        }
    }

    public void testFailure(Failure failure) throws Exception {
        currentTestSucceed = false;
        log.error(String.format("\t\t失败：%s %s", failure.getDescription(), failure.getMessage()));
    }

    public void testAssumptionFailure(Failure failure) {
        currentTestSucceed = false;
        log.error(String.format("\t\t失败：测试 \"%s\"条件未满足：%s", failure.getDescription().getMethodName(), failure.getMessage()));
    }

    public void testIgnored(Description description) throws Exception {
        log.info(String.format("\t测试 \"%s\"被忽略", description.getMethodName()));
    }
}
