/*
 * PipeTemplate.java
 *
 * Brazil project web application toolkit,
 * export version: 2.3 
 * Copyright (c) 2008 Sun Microsystems, Inc.
 *
 * Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License Version 
 * 1.0 (the "License"). You may not use this file except in compliance with 
 * the License. A copy of the License is included as the file "license.terms",
 * and also available at http://www.sun.com/
 * 
 * The Original Code is from:
 *    Brazil project web application toolkit release 2.3.
 * The Initial Developer of the Original Code is: suhler.
 * Portions created by suhler are Copyright (C) Sun Microsystems, Inc.
 * All Rights Reserved.
 * 
 * Contributor(s): suhler.
 *
 * Version:  1.1
 * Created by suhler on 08/03/19
 * Last modified by suhler on 08/03/19 14:17:05
 *
 * Version Histories:
 *
 * 1.2 70/01/01-00:00:02 (Codemgr)
 *   SunPro Code Manager data about conflicts, renames, etc...
 *   Name history : 1 0 sunlabs/PipeTemplate.java
 *
 * 1.1 08/03/19-14:17:05 (suhler)
 *   date and time created 08/03/19 14:17:05 by suhler
 *
 */

package sunlabs.brazil.sunlabs;

import java.io.OutputStream;
import java.io.IOException;;
import sunlabs.brazil.server.Server;
import sunlabs.brazil.server.Request;
import sunlabs.brazil.template.RewriteContext;
import sunlabs.brazil.template.Template;
import sunlabs.brazil.util.http.HttpInputStream;
import sunlabs.brazil.util.StringMap;
import sunlabs.brazil.template.QueueTemplate;

/**
 * Template to run a program, in a pipe, feed data to its stdin, and
 * get data back from its stdout.
 * <p>This template processes the <code>&lt;pipe ...&gt;</code> tag.
 * The following attributes are supported. ${...} substitutions are
 * preformed before the command is run.  This template implements a
 * "coprocess" model if IPC, where a process is started, then communicates
 * with templates via its stdin and stdout.  The "Stdout" of the co-process
 * (e.g. the "command", must have line-buffered input and output for this to
 * work.
 * <dl class=attrib>
 * <dt>command
 * <dd>
 * The command to run.  The environment (and path) are inherited
 * from the server.  This is a required parameter.
 * <dt>stdoutQ
 * <dd>The name of the Queue to listen on for output (required)
 * <dt>stdinQ
 * <dd>The name of the Queue that accepts stdin (required)
 * </dl>
 * <p>
 * Each line of output is placed into the "stdoutQ" queue as a single element
 * map named "line".  If the process terminates, the "error" element of the 
 * map is set instead. Lines destined for the "stdin" of the process are placed
 * in the "line" element of a map, end enqueued to the "stdinQ" useing the 
 * QueueTemplate.
 * <p>
 * There is currently no provision for killing a process once it has started:
 * it needs to die on its own.
 * <p>Example:<br>
 * <dl>
 * <dt> &lt;pipe command="runme" stdinQ=in stdoutQ=out&gt;
 * <dd> Start the command "runme" in a pipeline
 * <dt> &lt;enqueue name=in data=line#to_runme&gt; 
 * <dd> send the line "to_runme" to the stdin of the "runme" process
 * <dt> &lt;dequeue name=out prepend=q. timelimit=5&gt;
 * <dd> Read a line from the stdout of the "runme" process
 * </dl>
 * @author      Stephen Uhler
 * @version		1.1
 */

public class
PipeTemplate extends Template {

    public void tag_pipe(RewriteContext hr) {
        debug(hr);
	hr.killToken();
	String command = hr.get("command");
	String stdinQ = hr.get("stdinQ");	// write to process stdin
	String stdoutQ = hr.get("stdoutQ");	// read from process stdout
	if (command == null) {
	    debug(hr, "Missing command attribute");
	    return;
	}

	if (stdinQ == null || stdoutQ == null) {
	    debug(hr, "in or output Queue");
	    return;
	}
	hr.request.log(Server.LOG_DIAGNOSTIC, hr.prefix, "Running: " + command);
	try {
	    Process process = Runtime.getRuntime().exec(command);
	    new Pipe(hr, process, stdoutQ, true).start();
	    new Pipe(hr, process, stdinQ, false).start();
	} catch (Exception e) {
	    hr.request.log(Server.LOG_WARNING, hr.prefix, e.getMessage());
	}
    }

    static class Pipe extends Thread {
	Process process;
	RewriteContext hr;	// used for diagnostics only
	HttpInputStream in = null;
	Request.HttpOutputStream out = null;
	String qName;		// either our input or output Queues

	/**
	 * Start either a reader or writer.  The reader reads lines from
	 * the process, and sends them to the Queu, the writer listens from
	 * the Queue, and writes the output line to the process.
	 */

	public Pipe(RewriteContext hr, Process process, String qName, boolean isIn) {
	    this.hr = hr;
	    this.process = process; 
	    this.qName = qName;
	    if (isIn) {
	       in = new HttpInputStream(process.getInputStream());
	    } else {
	       out = new  Request.HttpOutputStream(process.getOutputStream());
	    }
	}

	public void run() {
	    // setDaemon(true);
	    boolean alive = true;
	    while(alive) {
		if (out==null) {
		    StringMap map = new StringMap();
		    try {
			String line = in.readLine();
			hr.request.log(Server.LOG_DIAGNOSTIC, hr.prefix,
				"Read: (" + line + ") Q to " + qName);
			if (line == null) {
			    throw new IOException("output closed");
			}
			map.add("line", line);
		    } catch (IOException e) {
			map.add("error", "terminated");
			alive=false;
			hr.request.log(Server.LOG_DIAGNOSTIC, hr.prefix,
				"lost output, ending");
			try {
			    // out.close();
			    process.waitFor();
			} catch (Exception e2) {
			    System.out.println("Oops: " + e2);
			}
		    }
		    QueueTemplate.enqueue(qName, "piper", map, false, false);
		} else {
		    QueueTemplate.QueueItem item=QueueTemplate.dequeue(qName, 10);
		    hr.request.log(Server.LOG_DIAGNOSTIC, hr.prefix,
			    "deq'd: " + item);
		    if (item != null) {
			StringMap map = (StringMap) item.data;
			try {
			    out.writeBytes(map.get("line") + "\n");
			    out.flush();
			} catch (IOException e) {
			    hr.request.log(Server.LOG_WARNING, hr.prefix, 
					"write failed " + e.getMessage());
			    try {out.close();} catch (Exception e2) {}
			    alive=false;
			}
		    }
		}
	    }
	}
    }
}
