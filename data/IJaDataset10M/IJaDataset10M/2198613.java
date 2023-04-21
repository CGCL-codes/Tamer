package com.sistemask.common.util;

import com.sistemask.common.constant.Common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropUtil extends AbstractUtil {

    /**
     * Carga un archivo de propiedad
     * @param file (Path)
     * @return Properties
     */
    public static Properties load(String file) {
        setLastException(null);
        Properties _prop = new Properties();
        InputStream _is = null;
        try {
            _is = new FileInputStream(file);
            _prop.load(_is);
            return _prop;
        } catch (FileNotFoundException ex) {
            setLastException(ex);
        } catch (IOException ex) {
            setLastException(ex);
        }
        return null;
    }

    /**
     * Salva el archivo de propiedad
     * @param file
     * @param prop
     * @param comment
     */
    public static void save(String file, Properties prop, String comment) {
        OutputStream _propOut = null;
        setLastException(null);
        try {
            _propOut = new FileOutputStream(new File(file));
            prop.store(_propOut, comment);
        } catch (FileNotFoundException ex) {
            setLastException(ex);
        } catch (IOException ex) {
            setLastException(ex);
        }
    }

    /**
     * Salva un archivo de propiedad sin comentario
     * @param file
     * @param prop
     */
    public static void save(String file, Properties prop) {
        save(file, prop, Common.BLANK);
    }
}
