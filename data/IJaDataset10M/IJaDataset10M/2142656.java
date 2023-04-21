package ar.com.datos.ftrs.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import ar.com.datos.dataaccess.api.Archivo;
import ar.com.datos.dataaccess.api.ArchivoListaInvertida;
import ar.com.datos.dataaccess.api.ArchivoRLV;
import ar.com.datos.dataaccess.arbolbsharp.api.ArbolBSharp;
import ar.com.datos.dataaccess.arbolbsharp.api.ArbolBSharp.DatosBSharp;
import ar.com.datos.dataaccess.arbolbsharp.exceptions.ArbolBSharpException;
import ar.com.datos.dataaccess.arbolbsharp.exceptions.ArbolBSharpLecturaException;
import ar.com.datos.dataaccess.dto.RegistroDocumento;
import ar.com.datos.dataaccess.dto.RegistroListaInvertida;
import ar.com.datos.dataaccess.exceptions.ImposibleObtenerRegistroDocumentosException;
import ar.com.datos.dataaccess.exceptions.NoSePudoPosicionarException;
import ar.com.datos.dataaccess.exceptions.NoSePuedeLeerRegistroException;
import ar.com.datos.ftrs.exceptions.AnalizadorConsultasException;
import ar.com.datos.ftrs.exceptions.BufferAnalizadorLlenoException;

public class AnalizadorConsultas {

    protected StopwordAnalyzer analizadorPalabras;

    protected ArchivoListaInvertida archListas;

    protected ArrayList<TerminosBusqueda> listaTerminos;

    protected ArchivoRLV archDocumentos;

    protected String pathArchivoListas;

    protected String pathDocumentos;

    protected HashMap<Long, String> mapaDocumentos;

    protected HashMap<String, String> mapaTerminos;

    protected String pathArbolIndice;

    protected String pathArbolHoja;

    protected ArbolBSharp arbolbSharp;

    public AnalizadorConsultas(String pathArbolIndice, String pathArbolHoja, String pathArchivoListas, String pathDocumentos) throws AnalizadorConsultasException {
        this.analizadorPalabras = new StopwordAnalyzer();
        this.pathArchivoListas = pathArchivoListas;
        this.pathDocumentos = pathDocumentos;
        this.listaTerminos = new ArrayList<TerminosBusqueda>();
        this.mapaDocumentos = new HashMap<Long, String>();
        this.mapaTerminos = new HashMap<String, String>();
        this.pathArbolIndice = pathArbolIndice;
        this.pathArbolHoja = pathArbolHoja;
    }

    /**
	 * Devuelve la lista de documentos mas relevantes que hay indexados
	 * PRE: terminos de busqueda establecidos y agregados
	 * @param cantDoc N documentos que se retornan
	 * @return la lista ya cargada de documentos
	 * null si no hay documentos relevantess o terminos de busqueda
	 */
    public ArrayList<RegistroDocumento> obtenerDocRelevantes(int cantDoc) {
        ArrayList<RegistroDocumento> retorno = new ArrayList<RegistroDocumento>();
        int cantidadAgregada = 0;
        if (listaTerminos.isEmpty()) return null;
        for (int indice = 0; indice < this.listaTerminos.size(); indice++) {
            TerminosBusqueda termino = listaTerminos.get(indice);
            try {
                archListas.posicionarBloque(termino.getOffsetListaInvertida());
            } catch (NoSePudoPosicionarException e) {
            }
            RegistroListaInvertida reg = null;
            try {
                reg = archListas.leerPrimerRegistro();
            } catch (NoSePuedeLeerRegistroException e) {
            } catch (ImposibleObtenerRegistroDocumentosException e) {
            }
            long offsetDoc = reg.getOffsetDocumento();
            if (!this.mapaDocumentos.containsKey(new Long(offsetDoc))) {
                this.mapaDocumentos.put(new Long(offsetDoc), null);
                try {
                    archDocumentos.posicionar(offsetDoc);
                } catch (NoSePuedeLeerRegistroException e) {
                }
                RegistroDocumento regDocu = new RegistroDocumento();
                try {
                    archDocumentos.leerSiguiente(regDocu);
                } catch (NoSePuedeLeerRegistroException e) {
                }
                retorno.add(regDocu);
                cantidadAgregada++;
            }
            boolean fin = false;
            while (!fin && cantidadAgregada < cantDoc) {
                try {
                    reg = archListas.leerSiguienteRegistro();
                } catch (ImposibleObtenerRegistroDocumentosException e) {
                    fin = true;
                } catch (NoSePuedeLeerRegistroException e) {
                    fin = true;
                }
                if (!fin) {
                    offsetDoc = reg.getOffsetDocumento();
                    if (!this.mapaDocumentos.containsKey(new Long(offsetDoc))) {
                        this.mapaDocumentos.put(new Long(offsetDoc), null);
                        try {
                            archDocumentos.posicionar(offsetDoc);
                        } catch (NoSePuedeLeerRegistroException e) {
                        }
                        RegistroDocumento regDocu = new RegistroDocumento();
                        try {
                            archDocumentos.leerSiguiente(regDocu);
                        } catch (NoSePuedeLeerRegistroException e) {
                        }
                        retorno.add(regDocu);
                        cantidadAgregada++;
                    }
                }
            }
            if (cantidadAgregada == cantDoc) {
                break;
            }
        }
        if (retorno.isEmpty()) return null;
        return retorno;
    }

    /**
	 * Agrega una palabra a los t�rminos de b�squeda.
	 * Si es stopword no la agrega. No se aplica la l�gica de locuciones
	 * @param nueva
	 * 
	 */
    public void agregarPalabra(String nueva) {
        if (!this.mapaTerminos.containsKey(analizadorPalabras.sacarAcentos(nueva))) {
            try {
                analizadorPalabras.addWord(nueva);
            } catch (BufferAnalizadorLlenoException e) {
            }
            String aux = analizadorPalabras.getValidWord();
            if (aux == null) {
                return;
            } else {
                DatosBSharp registro = this.arbolbSharp.new DatosBSharp();
                registro.setTermino(aux);
                this.mapaTerminos.put(aux, aux);
                try {
                    registro = this.arbolbSharp.buscarTermino(registro.getTermino());
                } catch (ArbolBSharpLecturaException e) {
                    return;
                }
                TerminosBusqueda termino = new TerminosBusqueda(aux, registro.getPeso(), registro.getOffsetLista());
                agregarOrdenado(termino);
            }
        }
    }

    private void agregarOrdenado(TerminosBusqueda termino) {
        if (listaTerminos.isEmpty()) {
            this.listaTerminos.add(termino);
            return;
        }
        for (TerminosBusqueda registro : this.listaTerminos) {
            if (termino.getPesoGlobal() > registro.getPesoGlobal()) {
                this.listaTerminos.add(this.listaTerminos.indexOf(registro), termino);
                return;
            }
        }
        this.listaTerminos.add(termino);
        return;
    }

    /**
	 * Inicialiiza el analizador de consultas (abere los archivos, etc.)
	 * @throws AnalizadorConsultasException
	 */
    public void inicializar() throws AnalizadorConsultasException {
        try {
            this.archListas = new ArchivoListaInvertida(pathArchivoListas, Archivo.MODO_LECTURA_ESCRITURA);
        } catch (FileNotFoundException e2) {
            throw new AnalizadorConsultasException("No se pudo abrir el archivo de listas invertidas", e2);
        }
        try {
            this.arbolbSharp = new ArbolBSharp(this.pathArbolIndice, this.pathArbolHoja);
        } catch (ArbolBSharpException e1) {
            throw new AnalizadorConsultasException("No se pudo abrir el archivo del Arbol B#", e1);
        }
        try {
            archDocumentos = new ArchivoRLV(pathDocumentos);
        } catch (FileNotFoundException e) {
            throw new AnalizadorConsultasException("No se pudo abrir el archivo de Documentos", e);
        }
        this.mapaDocumentos.clear();
        this.mapaTerminos.clear();
        this.listaTerminos = new ArrayList<TerminosBusqueda>();
    }

    /**
	 * Finaliza el analizador de consultas (cierra archivos, etc.)
	 * @throws AnalizadorConsultasException
	 */
    public void finalizar() throws AnalizadorConsultasException {
        try {
            this.archListas.cerrar();
        } catch (IOException e2) {
            throw new AnalizadorConsultasException("No se pudo cerrar el archivo de listas invertidas", e2);
        }
        try {
            this.arbolbSharp.cerrar();
        } catch (IOException e) {
            throw new AnalizadorConsultasException("No se pudo cerrar el archivo del Arbol B#", e);
        }
        try {
            this.archDocumentos.cerrar();
        } catch (IOException e2) {
            throw new AnalizadorConsultasException("No se pudo cerrar el archivo de Documentos", e2);
        }
    }

    private class TerminosBusqueda {

        private String termino;

        private float pesoGlobal;

        private long offsetListaInvertida;

        public TerminosBusqueda() {
        }

        public TerminosBusqueda(String nTermino, float nPeso, long nOffset) {
            termino = nTermino;
            pesoGlobal = nPeso;
            offsetListaInvertida = nOffset;
        }

        public String getTermino() {
            return termino;
        }

        public void setTermino(String termino) {
            this.termino = termino;
        }

        public float getPesoGlobal() {
            return pesoGlobal;
        }

        public void setPesoGlobal(float pesoGlobal) {
            this.pesoGlobal = pesoGlobal;
        }

        public long getOffsetListaInvertida() {
            return offsetListaInvertida;
        }

        public void setOffsetListaInvertida(long offsetListaInvertida) {
            this.offsetListaInvertida = offsetListaInvertida;
        }
    }
}
