package com.aptana.ide.rcp.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.intro.impl.IntroPlugin;
import com.aptana.ide.core.CoreStrings;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.core.ui.WorkbenchHelper;

/**
 * Manages the launching of the workbench files from the command line.
 * 
 * @author Paul Colton
 */
public class LaunchHelper {

    private String dotAptanaFile = null;

    private String[] initialFiles;

    /**
	 * openStartupFiles
	 * 
	 * @param window
	 */
    public void openStartupFiles(IWorkbenchWindow window) {
        if (initialFiles != null) {
            if (initialFiles.length > 0) {
                try {
                    IntroPlugin.closeIntro();
                } catch (Exception ex) {
                    IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_UnableToCLoseWelcome, ex);
                }
            }
            for (int i = 0; i < initialFiles.length; i++) {
                File file = new File(initialFiles[i]);
                try {
                    if (file.exists()) {
                        String editorID = getEditorID(file);
                        if (editorID == null) {
                            WorkbenchHelper.openFile(file, window);
                        } else {
                            WorkbenchHelper.openFile(editorID, file, window);
                        }
                    }
                } catch (Exception e) {
                    IdeLog.logError(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_ErrorOpeningFileOnStartup, initialFiles[i]), e);
                }
            }
            initialFiles = null;
        }
    }

    private String getEditorID(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".js") || name.endsWith(".css")) {
            return null;
        }
        String contents = getFileContents(file);
        final String HTML_EDITOR = "com.aptana.ide.editors.HTMLEditor";
        if (contents == null) {
            return null;
        } else {
            contents = contents.toLowerCase();
            if (contents.indexOf("<!doctype html") != -1 || contents.indexOf("<html") != -1) {
                return HTML_EDITOR;
            }
        }
        return null;
    }

    private String getFileContents(File file) {
        int fileLength = (int) file.length();
        if (fileLength == 0) {
            return null;
        }
        if (fileLength > 100) {
            fileLength = 100;
        }
        char[] chars = new char[fileLength];
        try {
            FileReader fr = new FileReader(file);
            fr.read(chars);
            fr.close();
        } catch (Exception e) {
            IdeLog.logError(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_UnableToGetFileContents, file.getAbsolutePath()), e);
            return null;
        }
        return new String(chars);
    }

    private static LaunchHelper _instance;

    /**
	 * getInstance
	 * 
	 * @return LaunchHelper
	 */
    public LaunchHelper getInstance() {
        if (_instance == null) {
            _instance = new LaunchHelper();
        }
        return _instance;
    }

    /**
	 * setLaunchFileCmdLineArgs
	 * 
	 * @param args
	 */
    public void setLaunchFileCmdLineArgs(String[] args) {
        int startIndex = 0;
        String[] fileList;
        String[] argList = args;
        if (argList.length > 0) {
            if (argList[0].toLowerCase().indexOf("aptana.exe") != -1) {
                startIndex = 1;
                String newDotAptanaFile = argList[0].toLowerCase().replaceAll("aptana.exe", ".aptana");
                if (dotAptanaFile == null) {
                    dotAptanaFile = newDotAptanaFile;
                    IdeLog.logInfo(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_AptanaPortCachedInFile, dotAptanaFile));
                } else {
                    IdeLog.logInfo(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_PortCacheFile, new String[] { dotAptanaFile, newDotAptanaFile }));
                }
            }
        }
        ArrayList filesList = new ArrayList();
        for (int i = startIndex; i < argList.length; i++) {
            if (!argList[i].startsWith("-")) {
                File file = new File(argList[i]);
                if (file.exists()) {
                    filesList.add(argList[i]);
                }
            }
        }
        fileList = (String[]) filesList.toArray(new String[0]);
        initialFiles = fileList;
    }

    /**
	 * checkForRunningInstance
	 * 
	 * @return boolean
	 */
    public boolean checkForRunningInstance() {
        int port = readCurrentPort();
        if (port != -1 && sendInitialFiles(port) == true) {
            return true;
        } else {
            CommandLineArgsServer server = null;
            try {
                server = new CommandLineArgsServer(this);
                server.start();
            } catch (IOException e) {
                IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_ErrorInChdeckingForCurrentInstance, e);
            }
            return false;
        }
    }

    private int readCurrentPort() {
        FileReader fr = null;
        if (dotAptanaFile == null) {
            dotAptanaFile = ".aptana";
        }
        try {
            fr = new FileReader(dotAptanaFile);
            BufferedReader br = new BufferedReader(fr);
            String sPort = br.readLine().trim();
            if (sPort.length() == 0) {
                return -1;
            } else {
                return Integer.parseInt(sPort);
            }
        } catch (FileNotFoundException e) {
            return -1;
        } catch (Exception e) {
            IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_UnableToFindCurrentPort, e);
            return CommandLineArgsServer.STARTING_PORT;
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_ErrorInClosingFileReader, e);
                }
            }
        }
    }

    private boolean sendInitialFiles(int port) {
        Socket socket = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            socket = new Socket(InetAddress.getByName("localhost"), port);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_UnknownLocalHost, e);
            return false;
        } catch (IOException e) {
            IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_CouldNotGetIOConnection, e);
            return false;
        }
        if (socket != null && os != null && is != null) {
            try {
                for (int i = 0; i < initialFiles.length; i++) {
                    os.writeBytes("\"" + initialFiles[i] + "\" ");
                }
                os.writeBytes("\n");
                os.flush();
                os.close();
                is.close();
                socket.close();
                return true;
            } catch (UnknownHostException e) {
                IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_TryingToConnectToUnknownHost, e);
            } catch (IOException e) {
                IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_IOExceptionEncountered, e);
            }
        }
        return false;
    }

    /**
	 * CommandLineArgsServer
	 * 
	 * @author Ingo Muschenetz
	 */
    class CommandLineArgsServer extends Thread {

        /**
		 * STARTING_PORT
		 */
        public static final int STARTING_PORT = 9980;

        LaunchHelper helper;

        ServerSocket server = null;

        String line;

        DataInputStream is;

        PrintStream os;

        Socket clientSocket = null;

        /**
		 * CommandLineArgsServer
		 * 
		 * @param helper
		 * @throws IOException
		 */
        public CommandLineArgsServer(LaunchHelper helper) throws IOException {
            super("CommandLineArgsServer");
            this.helper = helper;
            int port = getPort();
            if (port == -1) {
                throw new IOException(StringUtils.format(Messages.LaunchHelper_CouldNotFindOpenPort, new String[] { String.valueOf(STARTING_PORT), String.valueOf(STARTING_PORT + 10) }));
            } else {
                IdeLog.logInfo(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_BoundAptanaToPort, port));
            }
            try {
                FileWriter f = new FileWriter(dotAptanaFile);
                BufferedWriter out = new BufferedWriter(f);
                out.write(StringUtils.EMPTY + port);
                out.close();
                new File(dotAptanaFile).deleteOnExit();
            } catch (IOException e) {
            }
        }

        /**
		 * getPort
		 * 
		 * @return int
		 */
        public int getPort() {
            int tries = 10;
            int port = STARTING_PORT;
            while (tries > 0) {
                try {
                    server = new ServerSocket(port, 0, null);
                    server.setSoTimeout(1000);
                    return port;
                } catch (IOException e) {
                    IdeLog.logError(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_UnableToBindToPort, port), e);
                    tries--;
                    port++;
                }
            }
            return -1;
        }

        /**
		 * @see java.lang.Runnable#run()
		 */
        public void run() {
            while (server.isClosed() == false) {
                try {
                    clientSocket = server.accept();
                    BufferedReader r = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    BufferedWriter w = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    line = r.readLine().trim();
                    w.write("pong");
                    w.flush();
                    clientSocket.close();
                    if (line.length() > 0) {
                        helper.startupPerformed(line);
                    }
                } catch (SocketTimeoutException e) {
                } catch (Exception e) {
                    IdeLog.logError(MainPlugin.getDefault(), CoreStrings.ERROR, e);
                }
            }
        }
    }

    /**
	 * hookStartupListener
	 */
    public void hookStartupListener() {
        try {
            Class cls = ClassLoader.getSystemClassLoader().loadClass("com.aptana.ide.startup.WorkbenchStartupManager");
            Method startupListener;
            startupListener = cls.getMethod("setStartupListener", new Class[] { Object.class });
            startupListener.invoke(null, new Object[] { this });
        } catch (ClassNotFoundException e) {
            IdeLog.logInfo(MainPlugin.getDefault(), Messages.LaunchHelper_TheStartupListenerClassIsNotAvailable);
        } catch (Throwable e) {
            IdeLog.logError(MainPlugin.getDefault(), Messages.LaunchHelper_ErrorHookingStartupListener, e);
        }
    }

    /**
	 * startupPerformed
	 * 
	 * @param args
	 */
    public void startupPerformed(String args) {
        String[] startupArgs = parseCommandLineArgs(args);
        setLaunchFileCmdLineArgs(startupArgs);
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

            public void run() {
                openStartupFiles(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
            }
        });
    }

    private String[] parseCommandLineArgs(String cmdLine) {
        char quote = '"';
        char flagStart = '-';
        ArrayList args = new ArrayList();
        StringBuffer word = new StringBuffer();
        try {
            char[] chars = cmdLine.toCharArray();
            boolean quoteMode = false;
            for (int i = 0; i < chars.length; i++) {
                boolean endWord = false;
                char ch = chars[i];
                if (ch == quote) {
                    if (quoteMode) {
                        quoteMode = false;
                        endWord = true;
                    } else {
                        quoteMode = true;
                    }
                } else if (Character.isWhitespace(ch) && !quoteMode) {
                    endWord = true;
                } else {
                    word.append(ch);
                }
                if (endWord) {
                    if (word.length() > 0 && word.charAt(0) != flagStart) {
                        args.add(word.toString());
                    }
                    word.setLength(0);
                }
            }
        } catch (Exception e) {
            IdeLog.logError(MainPlugin.getDefault(), StringUtils.format(Messages.LaunchHelper_UnableToRecognizeCommandLineLaunchArguments, cmdLine));
        }
        if (word.length() > 0 && word.charAt(0) != flagStart) {
            args.add(word.toString());
        }
        String[] argArray = (String[]) args.toArray(new String[0]);
        return argArray;
    }
}
