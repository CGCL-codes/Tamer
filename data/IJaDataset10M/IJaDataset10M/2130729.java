package org.linkedgeodata.mapping;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Jens Lehmann
 * 
 */
public class Files {

    public static boolean debug = false;

    /**
	 * Reads in a file.
	 * 
	 * @param file
	 *            The file to read.
	 * @return Content of the file.
	 */
    public static String readFile(File file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer content = new StringBuffer();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return content.toString();
    }

    /**
	 * Reads in a file as Array
	 * 
	 * @param file
	 *            The file to read.
	 * @return StringArray with lines
	 */
    public static String[] readFileAsArray(File file) throws FileNotFoundException, IOException {
        String content = readFile(file);
        StringTokenizer st = new StringTokenizer(content, System.getProperty("line.separator"));
        List<String> l = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            l.add((String) st.nextToken());
        }
        return l.toArray(new String[l.size()]);
    }

    /**
	 * writes a serializable Object to a File.
	 * @param obj
	 * @param file
	 */
    public static void writeObjectToFile(Object obj, File file) {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Object readObjectfromFile(File file) {
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
	 * Creates a new file with the given content or replaces the content of a
	 * file.
	 * 
	 * @param file
	 *            The file to use.
	 * @param content
	 *            Content of the file.
	 */
    public static void createFile(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (debug) {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (debug) {
                System.exit(0);
            }
        }
    }

    /**
	 * Appends content to a file.
	 * 
	 * @param file
	 *            The file to create.
	 * @param content
	 *            Content of the file.
	 */
    public static void appendFile(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (debug) {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (debug) {
                System.exit(0);
            }
        }
    }

    public static void clearFile(File file) {
        try {
            createFile(file, "");
        } catch (Exception e) {
            e.printStackTrace();
            if (debug) {
                System.exit(0);
            }
        }
    }

    public static void deleteFile(String file) {
        deleteFile(new File(file));
    }

    public static void deleteFile(File file) {
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            if (debug) {
                System.exit(0);
            }
        }
    }

    public static void mkdir(String dir) {
        if (!new File(dir).exists()) {
            String parentDir = new File(dir).getParent();
            if (parentDir != null && !new File(parentDir).exists()) mkdir(new File(dir).getParent());
            try {
                boolean success = new File(dir).mkdir();
                if (!success) if (debug) {
                    throw new RuntimeException("Directory <" + dir + "> could not be created");
                } else System.err.println("Directory <" + dir + "> could not be created");
            } catch (Exception e) {
                e.printStackTrace();
                if (debug) {
                    System.exit(0);
                }
            }
        }
    }

    /**
	 * deletes all Files in the dir, does not delete the dir itself
	 * no warning is issued, use with care, cannot undelete files
	 *
	 * @param dir without a separator e.g. tmp/dirtodelete
	 */
    public static void deleteDir(String dir) {
        File f = new File(dir);
        if (debug) {
            System.out.println(dir);
            System.exit(0);
        }
        String[] files = f.list();
        for (int i = 0; i < files.length; i++) {
            Files.deleteFile(new File(dir + File.separator + files[i]));
        }
    }

    /**
	 * lists all files in a directory
	 * 
	 *
	 * @param dir without a separator e.g. tmp/dir
	 * @return a string array with filenames
	 */
    public static String[] listDir(String dir) {
        File f = new File(dir);
        if (debug) {
            System.out.println(dir);
            System.exit(0);
        }
        return f.list();
    }

    /**
	 * copies all files in dir to "tmp/"+System.currentTimeMillis()
	 * @param dir the dir to be backupped
	 */
    public static void backupDirectory(String dir) {
        File f = new File(dir);
        String backupDir = "tmp/" + System.currentTimeMillis();
        mkdir("tmp");
        mkdir(backupDir);
        if (debug) {
            System.out.println(dir);
            System.exit(0);
        }
        String[] files = f.list();
        try {
            for (int i = 0; i < files.length; i++) {
                File target = new File(dir + File.separator + files[i]);
                if (!target.isDirectory()) {
                    String s = readFile(target);
                    createFile(new File(backupDir + File.separator + files[i]), s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
