package org.parallelj.mda.rt.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Vector;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.parallelj.mda.rt.server.mbeans.ServerShutdownRemote;

public class TelnetService implements Service {

    public void serve(InputStream i, OutputStream o) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(i));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(o)));
        out.println("Welcome to the remote composite task invoker ...");
        out.println("Enter ? to get the list of available command");
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        for (; ; ) {
            out.print("> ");
            out.flush();
            String line = in.readLine();
            if (line == null || "q".equalsIgnoreCase(line)) break; else if ("?".equalsIgnoreCase(line) || "help".equalsIgnoreCase(line)) {
                printUsage(out);
            } else if ("ll".equalsIgnoreCase(line)) {
                printTaskList(out, mbs);
            } else if ("stop".equalsIgnoreCase(line)) {
                out.println("Trying to shutdown the server ...");
                try {
                    String shutdownMBeanName = ServerShutdownRemote.class.getCanonicalName();
                    String domain = shutdownMBeanName.substring(0, shutdownMBeanName.lastIndexOf('.'));
                    String type = shutdownMBeanName.substring(shutdownMBeanName.lastIndexOf('.') + 1);
                    ObjectName stopMBean = new ObjectName(domain + ":type=" + type);
                    mbs.invoke(stopMBean, "shutdownParallelJServer", null, null);
                    out.println("Server shut down successfully !");
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println(e.getMessage());
                    out.println("An error occurs when trying to shutdown the server ");
                }
            } else if (isCommand(line)) {
                parseCommandAndLaunchTask(line, mbs, out);
            } else if (line.startsWith("signature")) {
                displayMethodSignature(line, mbs, out);
            } else {
                out.println("Unknown command.");
            }
            out.println();
        }
        out.close();
        in.close();
    }

    private boolean isCommand(String line) {
        return line.startsWith("asynclaunch") || line.startsWith("al") || line.startsWith("synclaunch") || line.startsWith("sl") || line.startsWith("asyncqueue") || line.startsWith("aq") || line.startsWith("syncqueue") || line.startsWith("sq") || line.startsWith("stopAll") || line.startsWith("sa") || line.startsWith("stopBatch") || line.startsWith("sb") || line.startsWith("listBatch") || line.startsWith("lb");
    }

    private void displayMethodSignature(String line, MBeanServer mbs, PrintWriter out) {
        out.println();
        String[] chunks = line.replace("  ", " ").split(" ");
        if (chunks.length < 2) {
            out.println("Could not display signature as no ID is provided !");
            return;
        }
        int id = -1;
        try {
            id = Integer.parseInt(chunks[1]);
        } catch (NumberFormatException nfe) {
            out.println("Could not display signature as failed to parse ID (" + chunks[1] + ")");
            return;
        }
        List<ObjectName> taskMBeans = ParallelJServer.getInstance().getTaskMBeans();
        if (id < 0 || id > taskMBeans.size() - 1) {
            out.println("Could not display signature as ID is out of range (" + id + ")");
            return;
        }
        ObjectName objectName = ParallelJServer.getInstance().getTaskMBeans().get(id);
        out.println("Input parameters defined for Task: " + objectName.getDomain() + "." + objectName.getKeyProperty("type"));
        String methodName = "asyncLaunch";
        MBeanParameterInfo[] methodSignature = getMethodSignature(objectName, methodName, mbs, out, 0);
        for (MBeanParameterInfo info : methodSignature) {
            out.println("\t<" + info.getType() + "> " + info.getName());
        }
        out.println("If method used is synchronous, you can add the following parameter:");
        out.println("\t<int> pollingTime");
        out.println();
    }

    /**
	 * @param out
	 */
    private void printUsage(PrintWriter out) {
        out.println("            ? : Print this help.");
        out.println("            q : Quit.");
        out.println("         stop : Shutdown the Server.");
        out.println();
        out.println("           ll : " + "List of available tasks and their associated IDs.");
        out.println();
        out.println("  signature x : " + "displays the possible input parameters of a Task");
        out.println();
        out.println("asynclaunch x : " + "Launches a new Task instance, " + "and returns (asynchronous launch).");
        out.println("         al x : asynclaunch alias");
        out.println(" synclaunch x : " + "Launches a new Task instance, " + "and waits till return status (synchronous launch).");
        out.println("         sl x : synclaunch alias");
        out.println(" asyncqueue x : " + "Queues a new Task instance in ParallelJ Queuer, " + "and returns (asynchronous queuing).");
        out.println("         aq x : asyncqueue alias");
        out.println("  syncqueue x : " + "Queues a new Task instance in ParallelJ Queuer, " + "and waits till return status (synchronous queuing).");
        out.println("         sq x : syncqueue alias");
        out.println("  listBatch x : " + "List all running batches.");
        out.println("         lb x : listBatch alias");
        out.println("  stopBatch x : " + "Stop a running batch. Take a parameter wich is ID batch");
        out.println("         sb x : stopBatch alias");
        out.println("    stopAll x : " + "Stop all running batches");
        out.println("         sa x : stopAll alias");
        out.println();
    }

    /**
	 * @param out
	 * @param mbs
	 */
    private void printTaskList(PrintWriter out, MBeanServer mbs) {
        List<ObjectName> mbeanlist = ParallelJServer.getInstance().getTaskMBeans();
        for (int i = 0; i < mbeanlist.size(); i++) {
            ObjectName element = mbeanlist.get(i);
            if (mbs.isRegistered(element)) out.println(" Id: " + i + " _ Composite task name: " + element.getDomain() + "." + element.getKeyProperty("type"));
        }
        out.println();
    }

    /**
	 * Reads command line, and launches a new Task instance corresponding to
	 * command line
	 * 
	 * @param commandLine
	 * @param mbs
	 * @param out
	 */
    private void parseCommandAndLaunchTask(String commandLine, MBeanServer mbs, PrintWriter out) {
        out.println();
        while (commandLine.indexOf("  ") > -1) commandLine = commandLine.replace("  ", " ");
        String[] chunks = parseParameters(commandLine);
        if (chunks.length < 2) {
            out.println("Cannot launch Task as no ID is provided.");
            return;
        }
        String methodName = getExecutionMethodName(chunks[0].trim());
        boolean needPollingArgument = methodName.startsWith("sync");
        int taskID = -1;
        try {
            taskID = Integer.parseInt(chunks[1]);
        } catch (NumberFormatException nfe) {
            out.println("Cannot parse provided ID. Format Error (" + chunks[1] + ")");
        }
        List<ObjectName> taskMBeansList = ParallelJServer.getInstance().getTaskMBeans();
        if (taskID < 0 || taskID > taskMBeansList.size() - 1) {
            out.println("Provided ID out of range. Quitting.");
            return;
        }
        ObjectName element = taskMBeansList.get(taskID);
        MBeanParameterInfo[] parameterInfo = getMethodSignature(element, methodName, mbs, out, chunks.length - 2);
        Object[] methodParameters = new Object[parameterInfo.length];
        boolean isValid = true;
        for (int i = 0; i < parameterInfo.length && isValid; i++) {
            if ("java.lang.String".equals(parameterInfo[i].getType())) {
                if (i + 2 < chunks.length) methodParameters[i] = chunks[i + 2];
            } else if ("int".equals(parameterInfo[i].getType()) && i == parameterInfo.length - 1 && needPollingArgument) {
                if (i + 2 < chunks.length) try {
                    methodParameters[i] = Integer.parseInt(chunks[i + 2]);
                } catch (NumberFormatException nfe) {
                    out.println("Could not parse polling time. Using default: 1000 ms");
                    methodParameters[i] = 1000;
                } else methodParameters[i] = 1000;
            } else isValid = false;
        }
        if (isValid) launchTask(element, methodName, methodParameters, parameterInfo, mbs, out); else out.println("Signature could not be managed (Wrong number of arguments). Quitting.");
    }

    private String[] parseParameters(String commandLine) {
        if (!commandLine.contains("\"")) return commandLine.split(" ");
        Vector<String> v = new Vector<String>();
        boolean opQuote = false;
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < commandLine.length(); i++) {
            char c = commandLine.charAt(i);
            if (c == '"') opQuote = !opQuote; else if (c == ' ' && !opQuote) {
                v.add(sb.toString());
                sb = new StringBuffer("");
            } else sb.append(c);
        }
        if (!"".equals(sb.toString())) v.add(sb.toString());
        return v.toArray(new String[0]);
    }

    private void launchTask(ObjectName element, String methodName, Object[] methodParamValues, MBeanParameterInfo[] methodParamInfos, MBeanServer mbs, PrintWriter out) {
        try {
            String[] methodParamsTypes = new String[methodParamInfos.length];
            for (int i = 0; i < methodParamsTypes.length; i++) {
                methodParamsTypes[i] = methodParamInfos[i].getType();
            }
            Object object = mbs.invoke(element, methodName, methodParamValues, methodParamsTypes);
            if (methodName.startsWith("async") || object == null) out.println("OK"); else out.println(object.toString().replace("\n", "\r\n"));
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }

    private MBeanParameterInfo[] getMethodSignature(ObjectName element, String methodName, MBeanServer mbs, PrintWriter out, int supposedCount) {
        MBeanOperationInfo operationInfo = null;
        boolean doContinue = true;
        try {
            MBeanOperationInfo[] operations = mbs.getMBeanInfo(element).getOperations();
            for (int i = 0; i < operations.length && doContinue; i++) {
                MBeanOperationInfo temp = operations[i];
                if (temp.getName().equals(methodName)) {
                    operationInfo = temp;
                    if (temp.getSignature().length == supposedCount) {
                        doContinue = false;
                    }
                }
            }
            if (operationInfo == null) {
                out.println("No method found with proposed name. Returning");
                return new MBeanParameterInfo[0];
            }
            MBeanParameterInfo[] signature = new MBeanParameterInfo[operationInfo.getSignature().length];
            for (int i = 0; i < signature.length; i++) {
                signature[i] = operationInfo.getSignature()[i];
            }
            return signature;
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error");
        }
        return new MBeanParameterInfo[0];
    }

    private String getExecutionMethodName(String command) {
        if (command.trim().equalsIgnoreCase("syncLaunch") || command.trim().equalsIgnoreCase("sl")) return "syncLaunch"; else if (command.trim().equalsIgnoreCase("asyncLaunch") || command.trim().equalsIgnoreCase("al")) return "asyncLaunch"; else if (command.trim().equalsIgnoreCase("syncQueue") || command.trim().equalsIgnoreCase("sq")) return "syncQueue"; else if (command.trim().equalsIgnoreCase("asyncQueue") || command.trim().equalsIgnoreCase("aq")) return "asyncQueue"; else if (command.trim().equalsIgnoreCase("stopAll") || command.trim().equalsIgnoreCase("sa")) return "stopAllEngines"; else if (command.trim().equalsIgnoreCase("stopBatch") || command.trim().equalsIgnoreCase("sb")) return "stopEngine"; else if (command.trim().equalsIgnoreCase("listBatch") || command.trim().equalsIgnoreCase("lb")) return "listRunningEngines"; else return null;
    }
}
