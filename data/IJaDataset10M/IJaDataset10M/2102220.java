package bones.process;

/**
 * class Process.java.
 * Context of a Process: 
 * -execution
 * 	.ThreadGroup  : No interraction with other Threads
 * 	.SecurityManager : forbid the call to system resources
 * -Memory
 * 	.ClassLoader only some classes, and own instances of Class classes
 * 	.stack and heap size? (not prossible for instance)
 * 
 * some enhancements:
 * attach to a user (grant context)
 */
public interface Process {

    public long getId();

    public ClassLoader getClassLoader();

    public ThreadGroup getThreadGroup();

    public SecurityManager getSecurityManager();
}
