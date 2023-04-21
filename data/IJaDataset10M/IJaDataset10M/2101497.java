package com.swapbytes.jrexportcmd;

import com.swapbytes.jrexportcmd.Types.*;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que contiene toda las funciones del programa para la línea de comandos.
 *
 * @author Nicola Strappazzon C., nicola51980@gmail.com
 * @version 1.1.0
 */
public class Cli {

    private static final String USAGE = "java -jar JRExportCmd.jar [OPTIONS]";

    private static final String HEADER = "JasperReport Export Command Line Tool, V-1.1.0";

    private static final String FOOTER = "";

    private CommandLine _cmd = null;

    private Options _options = new Options();

    /**
   * Prepara y valida todos los parametros capturadas por Java para ser procesados
   * correctamente por la aplicación. En caso de existir un valor incorrecto o requiere
   * de un parametro basico, se mostrara la ayuda.
   * 
   * @param args
   */
    public void setArgs(String[] args) {
        this._options.addOption("?", "help", false, "Print this help");
        this._options.addOption("D", true, "Database connector type: MYSQL or PGSQL");
        this._options.addOption("h", true, "Database host");
        this._options.addOption("d", true, "Database name");
        this._options.addOption("u", true, "Database user name");
        this._options.addOption("p", true, "Database password");
        this._options.addOption("f", true, "Format type output report, valid options:\nPDF, XLS, DOCX");
        this._options.addOption("i", true, "Input path report file JRXML or JASPER");
        this._options.addOption("o", true, "Output path report to file");
        this._options.addOption("P", true, "Parameters passing for report. It's CaseSensitive. Order not import. E.g.: \"p1=boolean:true|p2=string:ABC|p3=double:134.2|p4=integer:85\"");
        this._options.addOption("b64", false, "Encode base64 output stream");
        try {
            CommandLineParser clp = new PosixParser();
            this._cmd = clp.parse(this._options, args);
        } catch (ParseException ex) {
            this.showHelp();
        }
        if (!this._cmd.hasOption("D") || !this._cmd.hasOption("h") || !this._cmd.hasOption("d") || !this._cmd.hasOption("u") || !this._cmd.hasOption("p") || !this._cmd.hasOption("i") || !this._cmd.hasOption("f")) {
            this.showHelp();
        }
    }

    /**
   * Muestra la ayuda en la línea de comandos.
   */
    private void showHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(80);
        hf.printHelp(USAGE, HEADER, this._options, FOOTER);
        System.exit(0);
    }

    /**
   * Optimiza la verificacion boleana de como se prepresenta. Todos los valores
   * provienen de un tipo de dato String, que puede ser representado como un
   * 't', 'y' o '1', sin importar el case.
   */
    private boolean getBoolean(String value) {
        if (value == null) {
            return false;
        }
        if (value.toLowerCase().startsWith("t") || value.toLowerCase().startsWith("y") || value.startsWith("1")) {
            return true;
        } else {
            return false;
        }
    }

    /**
   * Devuelve el driver de la base de datos que sera usado en la conexion.
   *
   * @return
   */
    public Types.Driver getDrvier() {
        if (this._cmd.hasOption("D")) {
            String type = this._cmd.getOptionValue("D");
            try {
                return Types.Driver.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException ex) {
                this.showHelp();
            }
        }
        return null;
    }

    /**
   * Devuelve el nombre del servidor de base de datos.
   * 
   * @return
   */
    public String getHost() {
        if (this._cmd.hasOption("h")) {
            return this._cmd.getOptionValue("h");
        }
        return null;
    }

    /**
   * Devuelve el nombre de usuario del servidor de base de datos.
   *
   * @return
   */
    public String getUser() {
        if (this._cmd.hasOption("u")) {
            return this._cmd.getOptionValue("u");
        }
        return null;
    }

    /**
   * Devuelve la clave del usuario del servidor de base de datos.
   * 
   * @return
   */
    public String getPassword() {
        if (this._cmd.hasOption("p")) {
            return this._cmd.getOptionValue("p");
        }
        return null;
    }

    /**
   * Devuelve el nombre de la base de datos.
   *
   * @return
   */
    public String getDB() {
        if (this._cmd.hasOption("d")) {
            return this._cmd.getOptionValue("d");
        }
        return null;
    }

    /**
   * Devuelve la ruta del sistema de archivos de un reporte JRXML para ser procesado.
   *
   * @return
   */
    public String getInput() {
        if (this._cmd.hasOption("i")) {
            return this._cmd.getOptionValue("i");
        }
        return null;
    }

    /**
   * Devuelve la ruta del sistema de archivos como destino de un reporte ya procesado.
   * 
   * @return
   */
    public String getOutput() {
        if (this._cmd.hasOption("o")) {
            return this._cmd.getOptionValue("o");
        }
        return null;
    }

    /**
   * Devuelve el formato de salida de un reporte a procesar.
   * 
   * @return
   */
    public Types.Format getFormat() {
        if (this._cmd.hasOption("f")) {
            String type = this._cmd.getOptionValue("f");
            return Types.Format.valueOf(type.toUpperCase());
        }
        return null;
    }

    /**
   * Devuelve el formato de salida de un stream a procesar. Esta opción se ignora
   * al pasarle la opción "-o".
   *
   * @return
   */
    public Types.Stream getStream() {
        if (this._cmd.hasOption("b64")) {
            return Types.Stream.BASE64;
        }
        return Types.Stream.BIN;
    }

    /**
   * Devuelve un arreglo de todos los parametros según su tipo de dato y valor
   * que son necesarios para procesar un reporte.
   * 
   * @return
   */
    public Map getParameters() {
        if (this._cmd.hasOption("P")) {
            String params = this._cmd.getOptionValue("P");
            Map<String, Object> paramsMap = new HashMap();
            List<String> paramsList = Arrays.asList(params.split("\\|"));
            for (String param : paramsList) {
                String paramName = param.split("=", 2)[0];
                String paramTypeAndValue = param.split("=", 2)[1];
                String paramTypeString = paramTypeAndValue.split(":", 2)[0];
                String paramValueString = paramTypeAndValue.split(":", 2)[1];
                Types.Data paramType = Types.Data.valueOf(paramTypeString.toUpperCase());
                switch(paramType) {
                    case BOOLEAN:
                        paramsMap.put(paramName, this.getBoolean(paramValueString));
                        break;
                    case STRING:
                        paramsMap.put(paramName, paramValueString);
                        break;
                    case DOUBLE:
                        paramsMap.put(paramName, Double.valueOf(paramValueString));
                        break;
                    case INTEGER:
                        paramsMap.put(paramName, Integer.valueOf(paramValueString));
                        break;
                }
            }
            return paramsMap;
        }
        return null;
    }
}
