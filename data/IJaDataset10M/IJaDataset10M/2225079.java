package org.rlmud.amud.interfaz.ANSI;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Gonzalo Ortiz Jaureguizar (Golthiryus)
 */
class ANSIReader {

    StyledDocument doc;

    ANSIStylesNames ansiNames;

    private static final int TAMAÑO_BUFFER = 1024;

    private static final int ESC = 27;

    private enum Estado {

        TEXT, ESC, ANSI
    }

    ;

    public ANSIReader(StyledDocument styledDocument) {
        doc = styledDocument;
        ansiNames = new ANSIStylesNames();
        ansiNames.setColor(ANSIStylesNames.WHITE);
    }

    void readFromStream(InputStream in) throws IOException, BadLocationException {
        Reader r = new InputStreamReader(in);
        readFromReader(r);
    }

    void readFromReader(Reader in) throws IOException, BadLocationException {
        Estado estado = Estado.TEXT;
        CharBuffer entrada = CharBuffer.allocate(TAMAÑO_BUFFER);
        CharBuffer salida = CharBuffer.allocate(TAMAÑO_BUFFER);
        entrada.clear();
        int numChars = in.read(entrada);
        int numero = 0;
        ArrayList<Integer> codigosAnsi = new ArrayList<Integer>(1);
        while (numChars != -1) {
            entrada.position(0);
            System.err.println(Thread.currentThread().getName() + ":" + entrada.toString());
            while (entrada.hasRemaining()) {
                char c = entrada.get();
                switch(estado) {
                    case TEXT:
                        if (c == ESC) estado = Estado.ESC; else {
                            añadirCaracter(salida, c);
                            if (c == '\n') escribirDocumento(salida);
                        }
                        break;
                    case ESC:
                        if (c == '[') {
                            estado = Estado.ANSI;
                            codigosAnsi.clear();
                            numero = 0;
                            escribirDocumento(salida);
                        } else {
                            estado = Estado.TEXT;
                            añadirCaracter(salida, (char) ESC);
                            añadirCaracter(salida, c);
                        }
                        break;
                    case ANSI:
                        switch(c) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                numero = 10 * numero + (int) c - (int) '0';
                                break;
                            case ';':
                                codigosAnsi.add(numero);
                                numero = 0;
                                break;
                            default:
                                if (codigosAnsi.size() == 0) codigosAnsi.add(numero);
                                ansiHandler(c, codigosAnsi);
                                estado = Estado.TEXT;
                        }
                        break;
                }
            }
            entrada.clear();
            numChars = in.read(entrada);
            entrada.limit(numChars);
        }
    }

    /**
     * Añade un caracter al buffer. Si este se desborda envia su contenido al
     * documento con el formato adecuado
     * @param salida
     * @param c
     */
    private void añadirCaracter(CharBuffer salida, char c) throws BadLocationException {
        try {
            salida.put(c);
        } catch (BufferOverflowException ex) {
            escribirDocumento(salida);
            salida.put(c);
        }
    }

    void escribirDocumento(CharBuffer salida) throws BadLocationException {
        salida.limit(salida.position());
        salida.position(0);
        doc.insertString(doc.getLength(), salida.toString(), ansiNames.getStyle());
        salida.clear();
    }

    private void ansiHandler(char tipo, ArrayList<Integer> numeros) {
        if (tipo == 'm') {
            final int tope = numeros.size();
            for (int i = 0; i < tope; i++) {
                ansiNames.transitar(numeros.get(i));
            }
        }
    }
}
