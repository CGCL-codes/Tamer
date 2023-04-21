package net.sf.opentranquera.integration.mf.format.getters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import net.sf.opentranquera.integration.mf.format.MessageFormatterException;

/**
 * Lee cada linea desde un InputStream y le pasa a otro mensage (dentro del mismo archivo de configuracion) la
 * responsabilidad de transformar el source en target<br>
 * Devuelve una <code>java.util.Collection</code> de objetos del tipo definido por el mensaje referido por
 * message-ref 
 *
 * <br>Created 03/08/2005
 * @author <a href="mailto:diego.campodonico@eds.com">Diego Campodonico</a>
 */
public class LineInputStreamGetter extends InputStreamGetter {

    private static final String FILE_EMPTY_MESSAGE = "otf.mf.empty.file";

    private static final String LINE_EMPTY_MESSAGE = "otf.mf.empty.line.in.file";

    private static final String ERROR_MESSAGE = "otf.mf.error";

    /**
     * Indica las lineas que deve ignorar.<br>
     * La primer linea es la numero cero
     */
    private int ignores[];

    private String[] ignoreStartWith;

    /**
     * Indica si se debe informar de un error si algunas de las lineas que contengan String vacios.
     */
    private boolean errorEmptyLine = true;

    public Object get(Object src, Map args) {
        BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) src));
        Collection results = new LinkedList();
        try {
            int cLine = 0;
            String line = br.readLine();
            if (line != null) {
                do {
                    if (!this.ignore(cLine++, line)) {
                        this.isEmptyLine(line);
                        Object obj = this.mf.format((String) args.get("message-ref"), line);
                        results.add(obj);
                    }
                    line = br.readLine();
                } while (line != null);
            } else {
                throw new MessageFormatterException(FILE_EMPTY_MESSAGE);
            }
            return results;
        } catch (IOException ioe) {
            throw new MessageFormatterException(ERROR_MESSAGE, ioe);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                throw new MessageFormatterException(ERROR_MESSAGE, e);
            }
        }
    }

    public Object transformInputObject(Object input, Map args) {
        Object obj = super.transformInputObject(input, args);
        String sIgnoreLines = (String) args.get("ignoreLines");
        if (sIgnoreLines != null) {
            String[] sIgnores = sIgnoreLines.split("[,//s]+");
            this.ignores = new int[sIgnores.length];
            for (int i = 0; i < sIgnores.length; i++) {
                this.ignores[i] = Integer.parseInt(sIgnores[i].trim());
            }
            Arrays.sort(this.ignores);
        } else {
            this.ignores = new int[0];
        }
        String sIgnoreStartWith = (String) args.get("ignoreStartWith");
        if (sIgnoreStartWith == null) this.ignoreStartWith = new String[0]; else this.ignoreStartWith = sIgnoreStartWith.split("[,//s]+");
        String sErrorEmptyLine = (String) args.get("errorEmptyLine");
        this.errorEmptyLine = (sErrorEmptyLine != null) ? BooleanUtils.toBoolean(sErrorEmptyLine) : this.errorEmptyLine;
        return obj;
    }

    protected boolean ignore(int counter, String line) {
        int index = Arrays.binarySearch(this.ignores, counter);
        boolean ignoreLines = (index >= 0);
        boolean ignoreStartWith = false;
        for (int i = 0; i < this.ignoreStartWith.length; i++) {
            if (line.startsWith(this.ignoreStartWith[i].trim())) {
                ignoreStartWith = true;
                break;
            }
        }
        return ignoreLines || ignoreStartWith;
    }

    protected void isEmptyLine(String line) {
        if ("".equals(line) && this.errorEmptyLine) {
            throw new MessageFormatterException(LINE_EMPTY_MESSAGE);
        }
    }
}
