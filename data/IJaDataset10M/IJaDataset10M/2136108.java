package net.woodstock.rockapi.anttasks.svn.common;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.Task;

/**
 * <p>
 * Base class of ALL Subversion Tasks. This class contains the commons attributes of all Subversion Tasks.
 * </p>
 * 
 * @author Lourival Sabino
 * @version 0.5
 * @since 16/01/2006
 */
public abstract class SubversionBaseTask extends Task {

    protected String executable = "svn";

    protected String command;

    protected String local;

    protected String protocol = "http";

    protected String host;

    protected String username;

    protected String password;

    protected String path;

    /**
	 * <p>
	 * Get svn subcommand. Subcommand can be one of(checkout, commit, export, list, log or update).
	 * </p>
	 * 
	 * @return The subversion sub command.
	 * @see #setCommand(String)
	 */
    public String getCommand() {
        return this.command;
    }

    /**
	 * <p>
	 * Set svn subcommand.
	 * </p>
	 * <p>
	 * Subcommand can be one of( <code>checkout</code>, <code>commit</code>, <code>export</code>,
	 * <code>list</code>, <code>log</code> or <code>update</code>).
	 * </p>
	 * 
	 * @param command
	 * @see #getCommand()
	 */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
	 * <p>
	 * Get path for svn executable. Default path is /usr/bin/svn.
	 * </p>
	 * 
	 * @return The subversion executable path.
	 * @see #setExecutable(String)
	 */
    public String getExecutable() {
        return this.executable;
    }

    /**
	 * <p>
	 * Set path for svn executable. Default path is <code>/usr/bin/svn</code>.
	 * </p>
	 * 
	 * @param executable
	 * @see #getExecutable()
	 */
    public void setExecutable(String executable) {
        this.executable = executable;
    }

    /**
	 * <p>
	 * Get the subversion protocol.
	 * </p>
	 * 
	 * @return The subversion protocol
	 * @see #setHost(String)
	 */
    public String getProtocol() {
        return this.protocol;
    }

    /**
	 * <p>
	 * Set the subversion protocol. Default is <code>http</code></code>.
	 * </p>
	 * 
	 * @param protocol
	 * @see #getProject()
	 */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
	 * <p>
	 * Get the subversion server name, or IP address.
	 * </p>
	 * 
	 * @return The subversion server host.
	 * @see #setHost(String)
	 */
    public String getHost() {
        return this.host;
    }

    /**
	 * <p>
	 * Set the subversion server name, or IP address.
	 * </p>
	 * <p>
	 * Examples : <code>svn.domain.com</code> or <code>192.168.0.1</code>.
	 * 
	 * @param host
	 * @see #getHost()
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * <p>
	 * Get the local subversion server repository.
	 * </p>
	 * <p>
	 * <strong>Used only when host isn�t used.</strong>
	 * </p>
	 * <p>
	 * We recommended use of <code>host="localhost" path="/svn/rep"</code> for local reposiories.
	 * </p>
	 * 
	 * @return The local subversion server repository.
	 * @see #setLocal(String)
	 */
    public String getLocal() {
        return this.local;
    }

    /**
	 * <p>
	 * Set the local subversion server repository.
	 * </p>
	 * <p>
	 * <strong>Used only when host isn�t used.</strong>
	 * </p>
	 * <p>
	 * We recommended use of <code>host="localhost" path="/svn/rep"</code> for local reposiories.
	 * </p>
	 * 
	 * @param local
	 * @see #getLocal()
	 */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
	 * <p>
	 * Get the password for connect to server.
	 * </p>
	 * 
	 * @return The password used for connection
	 * @see #getUsername()
	 * @see #setPassword(String)
	 * @see #setUsername(String)
	 */
    public String getPassword() {
        return this.password;
    }

    /**
	 * <p>
	 * Set the password for connect to server.
	 * </p>
	 * 
	 * @param password
	 * @see #getPassword()
	 * @see #getUsername()
	 * @see #setUsername(String)
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * <p>
	 * Get the path for repository into server. The path can be absolute to server.
	 * </p>
	 * <p>
	 * Example: for connect on <code>http://svn.domain.com/svn/rep</code> the values of subversion
	 * attributes can be <code>host="svn.domain.com/svn" path="/svn/rep"</code>
	 * </p>
	 * 
	 * @return The repository path
	 * @see #setPath(String)
	 */
    public String getPath() {
        return this.path;
    }

    /**
	 * <p>
	 * Set the path for repository into server. The path can be absolute to server.
	 * </p>
	 * <p>
	 * Example: for connect on <code>http://svn.domain.com/svn/rep</code> the values of subversion
	 * attributes can be <code>host="svn.domain.com/svn" path="/svn/rep"</code>.
	 * </p>
	 * 
	 * @param path
	 * @see #getPath()
	 */
    public void setPath(String path) {
        this.path = path;
    }

    /**
	 * <p>
	 * Get the username for connect to server.
	 * </p>
	 * 
	 * @return The usernamed used for connect.
	 * @see #getPassword()
	 * @see #setPassword(String)
	 * @see #setUsername(String)
	 */
    public String getUsername() {
        return this.username;
    }

    /**
	 * <p>
	 * Set the username for connect to server.
	 * </p>
	 * 
	 * @param username
	 * @see #getPassword()
	 * @see #getUsername()
	 * @see #setPassword(String)
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    protected String mountPath(String protocol, String host, String path) {
        StringBuilder b = new StringBuilder();
        b.append(protocol);
        b.append("//");
        b.append(host);
        b.append(path);
        return b.toString().replaceAll("\\/\\/", "\\/");
    }

    protected boolean hasHostData() {
        if (this.isEmpty(this.protocol)) {
            return false;
        }
        if (this.isEmpty(this.host)) {
            return false;
        }
        if (this.isEmpty(this.path)) {
            return false;
        }
        return true;
    }

    protected List<String> getDefaultParams() {
        List<String> cmd = new ArrayList<String>();
        cmd.add(this.executable);
        cmd.add(this.command);
        if ((!this.isEmpty(this.protocol)) && (!this.isEmpty(this.host)) && (!this.isEmpty(this.path))) {
            cmd.add(this.mountPath(this.protocol, this.host, this.path));
        }
        if (!this.isEmpty(this.username)) {
            cmd.add("--username");
            cmd.add(this.username);
        }
        if (!this.isEmpty(this.password)) {
            cmd.add("--password");
            cmd.add(this.password);
        }
        return cmd;
    }

    protected boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        if (s.trim().length() == 0) {
            return true;
        }
        return false;
    }

    protected boolean isTrue(Boolean b) {
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }
}
