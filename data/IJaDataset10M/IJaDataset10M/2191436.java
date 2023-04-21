package net.ucef.server.kernel;

public class CKernelLoader implements java.lang.Runnable {

    private boolean m_KernelIsRunning = false;

    private java.lang.String[] m_Arguments;

    public CKernelLoader(java.lang.String[] Arguments) throws java.lang.Throwable {
        m_Arguments = Arguments;
    }

    public void run() {
        while (true) {
            try {
                if (m_KernelIsRunning) {
                    try {
                        synchronized (this) {
                            wait(5000);
                        }
                    } catch (java.lang.InterruptedException Ex) {
                    }
                } else {
                    System.out.println("Restarting kernel...");
                    java.lang.Class cls = CClassLoader.newInstance(m_Arguments, 3000, 1000, null).loadClass("net.ucef.server.kernel.CKernel");
                    cls.getConstructor(new java.lang.Class[] { getClass() }).newInstance(new java.lang.Object[] { this });
                    m_KernelIsRunning = true;
                }
            } catch (java.lang.Throwable T) {
                System.err.println(CKernelLoader.formatThrowable(T));
                System.err.flush();
            }
        }
    }

    public void kernelStopped() {
        m_KernelIsRunning = false;
    }

    public static final java.lang.String formatThrowable(java.lang.Throwable T) {
        java.lang.String res = T.getClass().getName() + "\r\n";
        java.lang.String prefix = "  ";
        res += prefix + T.getMessage() + "\r\n";
        java.lang.Throwable t;
        while ((t = T.getCause()) != null) {
            res += prefix + t.getClass().getName() + "\r\n";
            prefix += "  ";
            res += prefix + t.getMessage() + "\r\n";
        }
        java.lang.StackTraceElement[] ste = T.getStackTrace();
        for (int i = 0; i < ste.length; i++) {
            res += ste[i].getFileName() + ":" + ste[i].getClassName() + "." + ste[i].getMethodName() + "[" + ste[i].getLineNumber() + "]\r\n";
        }
        return res;
    }

    public static final void main(java.lang.String[] Arguments) {
        try {
            (new java.lang.Thread(new CKernelLoader(Arguments))).start();
        } catch (java.lang.Throwable T) {
            System.err.println(CKernelLoader.formatThrowable(T));
            System.err.flush();
        }
    }
}
