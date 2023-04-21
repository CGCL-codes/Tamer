package com.sgm.de.benchmark;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Queue;

/**
 * @author Sting<p>
 * TODO 这里描述该类的主要作用，以及使用时需要注意的事项
 */
public class ProcessContext {

    public JMSProcesser processor;

    public BufferedReader fromAgent;

    public PrintWriter response;

    public int threadCount = 0;

    public ProcessConfig config;

    public Queue<String[]> queuePool;
}
