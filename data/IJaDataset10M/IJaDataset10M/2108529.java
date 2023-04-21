package net.sf.kengoo.ui.console;

import net.sf.kengoo.ui.CallbackHandler;
import net.sf.kengoo.util.FileSystemUtils;
import java.io.File;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA. User: maestro Date: 16.10.2007 Time: 1:01:52 To change this template use File | Settings |
 * File Templates.
 */
public class ConsoleCallbackHandler implements CallbackHandler {

    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public void error(String message, Throwable cause) {
        System.out.println("[ERROR] " + message);
        cause.printStackTrace(System.out);
    }

    public Object choice(String message, Object... variants) {
        if (message != null) {
            System.out.println(message);
        }
        if (variants.length == 1 && variants[0] instanceof Collection) {
            variants = ((Collection) variants[0]).toArray();
        }
        int index = 0;
        for (Object variant : variants) {
            System.out.println((index + 1) + ". " + variant);
        }
        int i;
        do {
            System.out.print("Please, enter [1-" + variants.length + "]:");
            try {
                Scanner sc = new Scanner(System.in);
                i = sc.nextInt() - 1;
            } catch (Exception e) {
                i = -1;
            }
        } while (i < 0 || i >= variants.length);
        return variants[i];
    }

    public File chooseFile(String message, boolean isDirectory, boolean checkExists, File... defaultPath) {
        if (message != null) {
            System.out.println(message);
        }
        File file;
        StringBuilder prompt = new StringBuilder("Please, enter ");
        if (isDirectory) {
            prompt.append("directory");
        } else {
            prompt.append("filepath");
        }
        if (defaultPath.length == 1) {
            prompt.append(" [").append(defaultPath[0].getAbsolutePath()).append(']');
        }
        prompt.append(": ");
        boolean fileAccepted;
        do {
            System.out.print(prompt);
            try {
                Scanner sc = new Scanner(System.in);
                file = new File(sc.nextLine());
                fileAccepted = FileSystemUtils.checkFile(file, isDirectory, checkExists);
            } catch (Exception e) {
                file = null;
                fileAccepted = false;
            }
        } while (!fileAccepted);
        return file;
    }

    public int askInt(String message, Integer defaultValue) {
        Integer answer = null;
        StringBuilder prompt = new StringBuilder(message);
        if (defaultValue != null) {
            prompt.append(" [");
            prompt.append(defaultValue);
            prompt.append("]");
        }
        prompt.append(": ");
        while (answer == null) {
            System.out.print(prompt);
            try {
                Scanner sc = new Scanner(System.in);
                String str = sc.nextLine();
                if (str.trim().length() == 0) {
                    answer = defaultValue;
                } else {
                    answer = Integer.parseInt(str);
                }
            } catch (Exception e) {
                answer = null;
            }
        }
        assert (answer != null);
        return answer;
    }

    public String askString(String message, String... defaultValue) {
        String answer = null;
        StringBuilder prompt = new StringBuilder(message);
        if (defaultValue.length == 1 && defaultValue[0] != null) {
            prompt.append(" [");
            prompt.append(defaultValue[0]);
            prompt.append("]");
        }
        prompt.append(": ");
        while (answer == null) {
            System.out.print(prompt);
            try {
                Scanner sc = new Scanner(System.in);
                String str = sc.nextLine();
                if (str.trim().length() == 0) {
                    answer = defaultValue[0];
                } else {
                    answer = str;
                }
            } catch (Exception e) {
                answer = null;
            }
        }
        assert (answer != null);
        return answer;
    }

    public boolean askBoolean(String message, Boolean defaultAnswer) {
        Boolean answer = null;
        StringBuilder prompt = new StringBuilder(message).append(" (Y/N)");
        if (defaultAnswer != null) {
            prompt.append(" [");
            if (defaultAnswer) {
                prompt.append("Y");
            } else {
                prompt.append("N");
            }
            prompt.append(']');
        }
        prompt.append(": ");
        while (answer == null) {
            System.out.print(prompt);
            try {
                Scanner sc = new Scanner(System.in);
                String str = sc.nextLine();
                if ("y".equalsIgnoreCase(str)) {
                    answer = Boolean.TRUE;
                } else if ("n".equalsIgnoreCase(str)) {
                    answer = Boolean.FALSE;
                } else if (str.trim().length() == 0) {
                    answer = defaultAnswer;
                }
            } catch (Exception e) {
                answer = null;
            }
        }
        assert (answer != null);
        return answer;
    }
}
