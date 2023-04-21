package servidor.ejecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.SocketException;
import servidor.catalog.tipo.Conversor;
import servidor.conexion.FabricaServidorTcp;
import servidor.conexion.ServidorTcp;
import servidor.ejecutor.xql.XStatement;
import servidor.ejecutor.xql.XStatementFactory;
import servidor.excepciones.ParseException;
import servidor.excepciones.VictimaDeadlockRuntimeException;
import servidor.parser.impl.MainParser;
import servidor.tabla.Columna;
import servidor.tabla.Registro;
import servidor.tabla.Tabla;
import servidor.util.Iterador;
import Zql.ZStatement;

/**
 * Clase principal que atiende los pedidos de un cliente.
 * Realiza un ciclo sobre cada sentencia que llega por la conexion.
 * El ciclo se compone de: parsear la sentencia, ejecutar el comando correspondiente y devolver el resultado o error.
 * @see XStatement
 */
public class Ejecutor implements Runnable {

    /**
	 * Constante con el primer mensaje que se le envia al cliente para corroborar la conexion.
	 */
    private static final String OK = "OK";

    /**
     * Socket conectado al cliente.
     */
    private Socket socket;

    /**
     * Variable con el Servidor de conexiones para avisarle cuando hay una nueva conexion y cuando se termina la misma.
     * Tambien se lo consulta para saber si el motor esta siendo apagado (comun o crash).
     */
    private ServidorTcp servidorTcp;

    /**
     * Constructor de la clase.
     * @param socket el socket conectado al cliente.
     */
    public Ejecutor(Socket socket) {
        this.socket = socket;
        this.servidorTcp = FabricaServidorTcp.dameInstancia();
        this.servidorTcp.nuevoSocketActivo(this.socket);
    }

    /**
     * Metodo principal del thread con el ciclo que atiende los pedidos.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salidaPrintWriter = new PrintWriter(socket.getOutputStream());
            salidaPrintWriter.println(OK);
            salidaPrintWriter.flush();
            StringWriter stringWriter = new StringWriter();
            PrintWriter bufferIntermedio = new PrintWriter(stringWriter);
            String linea = null;
            try {
                do {
                    linea = bufferedReader.readLine();
                    if (linea != null) {
                        boolean errorGrave = this.procesarLinea(bufferIntermedio, linea);
                        if (errorGrave) {
                            bufferIntermedio.flush();
                            salidaPrintWriter.write(stringWriter.toString());
                            salidaPrintWriter.flush();
                            stringWriter.getBuffer().setLength(0);
                            while (bufferedReader.ready()) {
                                bufferedReader.read();
                            }
                        } else if (!bufferedReader.ready()) {
                            bufferIntermedio.flush();
                            salidaPrintWriter.write(stringWriter.toString());
                            salidaPrintWriter.flush();
                            stringWriter.getBuffer().setLength(0);
                        }
                    }
                } while (linea != null && !this.servidorTcp.apagado());
            } catch (SocketException ignorado) {
            } finally {
                bufferIntermedio.close();
                salidaPrintWriter.close();
                bufferedReader.close();
                this.socket.close();
                this.servidorTcp.quitarSocket(this.socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Llama al parser para que analice la linea y luego ejecuta el comando correspondiente.
	 * La ejecucion es encapsulada en una transaccion si no existia ningna.
	 * @param printWriter el escritor donde se escribe el resultado del comando.
	 * @param linea la sentencia a procesar y ejecutar.
	 * @return true si hubo un error grave y no hay que seguir procesando.
	 */
    private boolean procesarLinea(PrintWriter printWriter, String linea) {
        XStatement xql;
        try {
            xql = this.parsear(linea);
        } catch (ParseException ex) {
            ex.printStackTrace();
            printWriter.println("ERROR: (PARSE) " + ex.getMessage());
            printWriter.flush();
            return false;
        } catch (RuntimeException e) {
            printWriter.println("ERROR: " + e.getMessage());
            printWriter.flush();
            throw e;
        } catch (Error e) {
            printWriter.println("ERROR: " + e.getMessage());
            printWriter.flush();
            throw e;
        }
        if (xql == null) {
            return false;
        }
        Resultado resultado = new Resultado();
        try {
            resultado = xql.execute();
        } catch (RuntimeException e) {
            printWriter.println("ERROR: " + e.getMessage());
            printWriter.flush();
            if (e instanceof VictimaDeadlockRuntimeException) {
                return true;
            } else {
                e.printStackTrace();
                return false;
            }
        } catch (Error e) {
            printWriter.println("ERROR: " + e.getMessage());
            printWriter.flush();
            throw e;
        }
        if (resultado.getMensaje() != null) {
            printWriter.println(resultado.getMensaje());
            printWriter.flush();
        } else {
            Tabla tabla = resultado.getTabla();
            printWriter.println(this.procesarTabla(tabla));
            printWriter.flush();
        }
        return false;
    }

    /**
	 * Se convierte una tabla en un formato String para ser mostrado por el cliente.
	 * @param tabla la tabla a transformar.
	 * @return una cadena con varias lineas que representan la tabla en un formato textual.
	 * @see StringWriter
	 * @see PrintWriter
	 * @see Ejecutor#fijarEn(String, int)
	 */
    private String procesarTabla(Tabla tabla) {
        final int TAMANIO = 20;
        Columna[] columnas = tabla.columnas();
        int[] tamanio = new int[columnas.length];
        for (int i = 0; i < tamanio.length; i++) {
            tamanio[i] = Math.min(columnas[i].nombre().length() + 4, TAMANIO);
        }
        Conversor conversor = Conversor.conversorATexto();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        for (int i = 0; i < columnas.length; i++) {
            printWriter.print(this.fijarEn(columnas[i].nombre(), tamanio[i]));
            printWriter.print('|');
        }
        printWriter.println();
        for (int i = 0; i < columnas.length; i++) {
            for (int j = 0; j < tamanio[i]; j++) {
                printWriter.print('-');
            }
            printWriter.print('+');
        }
        printWriter.println();
        Iterador<Registro.ID> iterador = tabla.registros();
        try {
            while (iterador.hayProximo()) {
                Registro.ID proximo = iterador.proximo();
                try {
                    Registro registro = tabla.registro(proximo);
                    for (int i = 0; i < columnas.length; i++) {
                        Object valorCrudo = registro.valor(i);
                        String valor = (String) conversor.convertir(columnas[i].campo(), valorCrudo);
                        printWriter.print(this.fijarEn(valor, tamanio[i]));
                        printWriter.print('|');
                    }
                    printWriter.println();
                } finally {
                    tabla.liberarRegistro(proximo);
                }
            }
        } finally {
            iterador.cerrar();
        }
        printWriter.flush();
        printWriter.close();
        try {
            stringWriter.close();
        } catch (IOException ignorada) {
        }
        return stringWriter.toString();
    }

    /**
     * Centra un texto entre espacio para obtener un nuevo texto de una determinada longitud.
     * Si la nueva longitud es menor a la del texto original, este es cortado para que quepa.
     * @param string el texto a centrar o recortar.
     * @param tamanio el largo del texto que se desea fijar.
     * @return una secuencia de caracteres del tamanio pasado por parametro.
     */
    private CharSequence fijarEn(String string, int tamanio) {
        string = string.trim();
        if (string.length() < tamanio) {
            int espacio = (tamanio - string.length()) / 2;
            int resto = (tamanio - string.length()) % 2;
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < espacio; j++) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(string);
            for (int j = 0; j < espacio + resto; j++) {
                stringBuilder.append(' ');
            }
            return stringBuilder;
        } else if (string.length() > tamanio) {
            return string.subSequence(0, tamanio);
        } else {
            return string;
        }
    }

    /**
	 * Llama al parser para que analice la linea y devuelve un error al cliente en caso de
	 * encotrarse uno en la parte de analisis.
	 * @param sentencia la linea a procesar.
	 * @return NULL si la sentencia estaba vacia, o un XStatement correspondiente segun la setencia con los datos descompuestos a partir de la misma.
	 * @see ZStatement
	 * @see XStatement
	 * @throws ParseException si hubo un error al analizar la sentencia.
	 */
    private XStatement parsear(String sentencia) throws ParseException {
        ZStatement zql = MainParser.initParser(sentencia);
        if (zql == null) {
            return null;
        }
        XStatement xql = XStatementFactory.getStatement(zql);
        xql.zqlToXql(zql);
        return xql;
    }
}
