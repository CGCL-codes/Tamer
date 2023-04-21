package com.idna.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataSerializationManager {

    private static final String saveDirectory = "/C:/Temp/java/serialobjects";

    private static final String fileExtension = ".ser";

    public static String doSave(Object objectToSave) {
        String fileName = createFileName(objectToSave);
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(objectToSave);
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static Object doLoad(String fileName) {
        Object loadObject = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadObject = (Object) in.readObject();
            in.close();
            fileIn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadObject;
    }

    private static void checkSaveDir() {
        File file = new File(saveDirectory);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new RuntimeException("File exists and is not a directory: " + file.getAbsolutePath());
            }
        } else {
            if (!file.mkdir()) {
                throw new RuntimeException("Directory cannot be created " + file.getAbsolutePath());
            }
        }
    }

    private static String createFileName(Object objectToSave) {
        checkSaveDir();
        String className = objectToSave.getClass().toString().split(" ")[1];
        int classId = System.identityHashCode(objectToSave);
        long random = new Date().getTime();
        String fileName = saveDirectory + "/" + className + "-" + classId + "-" + random + fileExtension;
        return fileName;
    }

    public static void main(String[] args) {
        Map<String, Serializable> objectToSave = new HashMap<String, Serializable>();
        objectToSave.put("string", "My Test String");
        objectToSave.put("int", new Integer(36));
        objectToSave.put("double", new Double(Math.PI));
        String fileName = doSave(objectToSave);
        Object loadedObj = doLoad(fileName);
    }
}
