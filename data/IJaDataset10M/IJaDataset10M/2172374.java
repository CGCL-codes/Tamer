package model.serialization;

import controller.ModuleFactory;
import java.beans.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * classe para efetuar serializa��o de objetos
 * @author Cassiano Cesar Casagrande
 */
public class Serializer {

    /** Construtor de inicializa��o*/
    public Serializer() {
    }

    /**
     * writer the object
     * @param obj - object to be serialized
     * @param endereco - location to save the serialized object
     */
    public static void Writer(Object obj, String endereco) {
        FileOutputStream f;
        try {
            f = new FileOutputStream(endereco);
            ObjectOutputStream s;
            try {
                s = new ObjectOutputStream(f);
                s.writeObject(obj);
                s.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * writer the object
     * @param obj - object to be serialized
     * @param endereco - location to save the serialized object
     */
    public static void Writer(Object obj, File endereco) {
        FileOutputStream f;
        try {
            f = new FileOutputStream(endereco);
            ObjectOutputStream s;
            try {
                s = new ObjectOutputStream(f);
                s.writeObject(obj);
                s.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * read serialized object
     * @param endereco - addres that contains the serialized object
     * @return
     */
    public static Object Reader(String endereco) {
        try {
            FileInputStream f;
            MyObjectInputStream obj = null;
            try {
                f = new FileInputStream(endereco);
                try {
                    obj = new MyObjectInputStream(ModuleFactory.getLoader(), f);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            return obj.readObject();
        } catch (IOException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * read serialized object
     * @param endereco - addres that contains the serialized object
     * @return
     */
    public static Object Reader(File endereco) {
        try {
            FileInputStream f;
            MyObjectInputStream obj = null;
            try {
                f = new FileInputStream(endereco);
                try {
                    obj = new MyObjectInputStream(ModuleFactory.getLoader(), f);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            return obj.readObject();
        } catch (IOException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Serializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * write object in xml
     * @param obj - object to be serialized 
     * @param endereco
     */
    public static void WriterXML(Object obj, String endereco) {
        FileOutputStream file;
        XMLEncoder xmlEnc;
        try {
            file = new FileOutputStream(endereco);
            try {
                xmlEnc = new XMLEncoder(file);
                try {
                    xmlEnc.writeObject(obj);
                    xmlEnc.flush();
                } finally {
                    xmlEnc.close();
                }
            } finally {
                file.close();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * read serialized object in xml
     * @param endereco - address that contains the serialized object
     * @return
     */
    public static Object ReaderXML(String endereco) {
        FileInputStream file;
        Object objeto = null;
        try {
            file = new FileInputStream(endereco);
            try {
                XMLDecoder xmlDec = new XMLDecoder(file);
                try {
                    objeto = xmlDec.readObject();
                    System.out.println(objeto);
                } finally {
                    xmlDec.close();
                }
            } finally {
                file.close();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return objeto;
    }
}
