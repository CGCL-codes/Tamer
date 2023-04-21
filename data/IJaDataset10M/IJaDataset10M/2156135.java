/*
 *  Centro de Publicaciones - UNL
 *  Copyright (C) 2000 Lucas Di Pentima <lucas@lunix.com.ar>
 *                     Nicolás César     <nico@lunix.com.ar>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * Esta clase representa el administrador de base de datos, se va a
 *  encargar de «hablar» con el RDBMS correspondiente para manejar 
 *  la persistencia de objetos del sistema.
 *  <p>
 *  Esta clase hará uso de un módulo del proyecto Castor, el módulo
 *  JDO (Java Data Objects) que utiliza JDBC para hacer el mapeo de
 *  objetos a tablas.
 *  <p>
 *  El sitio del proyecto Castor está en http://castor.exolab.org
 */

package sistema.db;

import sistema.clases.*; // Hago uso de nuestras clases

import org.exolab.castor.jdo.*; // Lo necesario para JDO
import java.sql.*; // Lo necesario para SQL
import java.util.*;

// <debug>
import org.exolab.castor.util.Logger;
import java.io.PrintWriter;
// </debug>

/**
 * @author Lucas Di Pentima
 * @version 0.3
 * 21/11/2000
 */
public class AdminDB {

    // Constantes para el modulo IndiceDB
    public static final int FACTURA = 1;

    // Atributos varios de instancia
    private JDO _jdo;
    private Database _db;
    private OQLQuery _oql;
    private QueryResults _resultados;
    private PrintWriter _writer;

    // Modulos del AdminDB...
    private BuscadorDB busc;
    private PlastificadorDB plas;
    private ChequeadorDB cheq;
    private IndiceDB indi;
    private BorradorDB borr;

    /**
     * Construye un nuevo administrador de puente base de datos, por defecto
     * va a utilizar el archivo database.xml al cargar la base de datos
     * y la base de datos «centropublicaciones»
     */
    public AdminDB() {
	
	// Seteo un logger al cual todos los mensajes le son enviados.
	// Útil para debugueo...
	_writer = new Logger(System.out).setPrefix("AdminDB");
	try {
	    run(_writer, "centropublicaciones", "database.xml");
	} catch (Exception e) {
	    _writer.println(e);
	    e.printStackTrace(_writer);
	}

	try {

	    // Configuro y abro la base de datos
	    _jdo = new JDO();
	    _jdo.setConfiguration("database.xml");
	    _jdo.setDatabaseName("centropublicaciones");
	    
	    // Le asigno a _jdo el logueador «writer», comentar cuando no
	    // haga falta.
	    _jdo.setLogWriter(_writer);
	    
	    /**
	     * Cargo la clase de JDO explícitamente...esto hay que agregarlo 
	     * siempre que se utilice JDO y se tenga el .jar del castor en 
	     * el de extensiones.
	     */
	    _jdo.setClassLoader(AdminDB.class.getClassLoader());
	    
	    _db = _jdo.getDatabase();

	    // Inicializo los modulos del AdminDB
	    busc = new BuscadorDB(_db);
	    plas = new PlastificadorDB(_db);
	    cheq = new ChequeadorDB(_db);
	    indi = new IndiceDB(_db);
	    borr = new BorradorDB(_db);

	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Construye un nuevo administrador de puente base de datos,
     * acepta como primer parámetro la base de datos a la cual
     * conectarse y como segundo parámetro el archivo .xml de
     * configuración de dicha base de datos.
     * @param baseDeDatos Nombre de la base de datos en PostgreSQL
     * @param archivoXMLDeConf Nombre del archivo .xml utilizado
     */
    public AdminDB(String baseDeDatos, String archivoXMLDeConf) {
	
	// Seteo un logger al cual todos los mensajes le son enviados.
	// Útil para debugueo...
	_writer = new Logger(System.out).setPrefix("AdminDB");
	try {
	    run(_writer, baseDeDatos, archivoXMLDeConf);
	} catch (Exception e) {
	    _writer.println(e);
	    e.printStackTrace(_writer);
	}
	
	try {
	    // Configuro y abro la base de datos
	    _jdo = new JDO();
	    _jdo.setConfiguration(archivoXMLDeConf);
	    _jdo.setDatabaseName(baseDeDatos);
	    
	    // Le asigno a _jdo el logueador «writer», comentar cuando no
	    // haga falta.
	    _jdo.setLogWriter(_writer);
	    
	    /**
	     * Cargo la clase de JDO explícitamente...esto hay que agregarlo 
	     * siempre que se utilice JDO y se tenga el .jar del castor en 
	     * el de extensiones.
	     */
	    _jdo.setClassLoader(AdminDB.class.getClassLoader());
	    
	    _db = _jdo.getDatabase();
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Método de testeo del logueador (?)
     * @param writer Logueador definido
     * @param baseDeDatos Base de datos a conectarse
     * @param archivoXMLDeConf Archivo de configuración de la base de datos
     */
    private static void run(PrintWriter writer, String baseDeDatos, String archivoXMLDeConf) throws Exception {
	System.out.println("Inicializando el logueador de AdminDB...");
	System.out.println("Conectando con base de datos: " + baseDeDatos);
	System.out.println("Archivo de configuración: "+ archivoXMLDeConf);
    } 
    
    /** 
     * Cierra la conexión con la base de datos.
     */
    public void finalizar() {
	try {
	    _db.close();
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
    }

    // Puertas de entrada a los servicios del AdminDB
    // Chequeador:

    public boolean existeDocumento(String documento, String tipo) {
	return cheq.existeDocumento(documento, tipo);
    }
    public boolean existeAutor(String nombre, String apellido) {
	return cheq.existeAutor(nombre, apellido);
    }

    /*******************************************************************/

    // Puertas de entrada a los servicios del AdminDB
    // Buscador:
    
    public HashSet buscarAutor (String nombre, String apellido) {
	return busc.buscarAutor (nombre, apellido);
    }
    public HashSet buscarObraOrig (String titulo) {
	return busc.buscarObraOrig (titulo);
    }
    public HashSet buscarTodasLasObras (String titulo) {
	return busc.buscarTodasLasObras (titulo);
    }
    public HashSet buscarLibro (String titulo) {
	return busc.buscarLibro (titulo);
    }
    public HashSet buscarObraAceptada (String titulo) {
	return busc.buscarObraAceptada (titulo);
    }
    public HashSet buscarObraRechazada (String titulo) {
	return busc.buscarObraRechazada (titulo);
    }
    public HashSet buscarObraCorregida (String titulo) {
	return busc.buscarObraCorregida (titulo);
    }
    public HashSet buscarObraCorrDisk (String titulo) {
	return busc.buscarObraCorrDisk (titulo);
    }
    public HashSet buscarRevista (String titulo) {
	return busc.buscarRevista (titulo);
    }
    public HashSet buscarEjemplar (String titulo) {
	return busc.buscarEjemplar (titulo);
    }
    public HashSet buscarEjemplarEnStock () {
	return busc.buscarEjemplarEnStock ();
    }
    public HashSet buscarEdicionImpresa (String titulo) {
	return busc.buscarEdicionImpresa (titulo);
    }
    public HashSet buscarEdicionImpresaEnStock () {
	return busc.buscarEdicionImpresaEnStock ();
    }
    public HashSet buscarPedidoEjemplar () {
	return busc.buscarPedidoEjemplar ();
    }
    public HashSet buscarPedidoEdicionImpresa () {
	return busc.buscarPedidoEdicionImpresa ();
    }
    public HashSet buscarImprenta () {
	return busc.buscarImprenta ();
    }
    public HashSet buscarCliente (String nombre) {
	return busc.buscarCliente (nombre);
    }
    public HashSet buscarRemitoNoFacturado () {
	return busc.buscarRemitoNoFacturado ();
    }
    public HashSet buscarBanco (String nombre) {
	return busc.buscarBanco (nombre);
    }
    public HashSet buscarFactCtaCorrienteSinPagar () {
	return busc.buscarFactCtaCorrienteSinPagar ();
    }

    /*******************************************************************/

    // Puertas de entrada a los servicios del AdminDB
    // Plastificador:

    public void plastificar ( Object objeto ) {
	plas.plastificar ( objeto );
    }
    public void plastificar(Object objeto1, Object objeto2) {
	plas.plastificar(objeto1, objeto2);
    }
    public void plastificarEnum(Enumeration enum, Object objeto2) {
	plas.plastificarEnum(enum, objeto2);
    }
    public void plastificarEnum(Enumeration enum, Object objeto2, 
				Object objeto3) {
	plas.plastificarEnum(enum, objeto2, objeto3);

    }
    public void plastificar(Enumeration enum, Object objeto2) {
	plas.plastificar(enum, objeto2);
    }
    public void plastificar(Enumeration enum, Object objeto1,
			    Object objeto2, Object objeto3) {
	plas.plastificar(enum, objeto1, objeto2, objeto3);
    }
    public void plastificar(Object objeto1, Object objeto2, Object objeto3) {
	plas.plastificar(objeto1, objeto2, objeto3);
    }
    public void plastificar(Object objeto1, 
			    Object objeto2, 
			    Object objeto3,
			    Object objeto4) {
	plas.plastificar(objeto1, objeto2, objeto3, objeto4);
    }
    public void plastificar( Object objeto1, 
			     Object objeto2, 
			     Object objeto3,
			     Object objeto4,
			     Object objeto5 ) {
	plas.plastificar(objeto1, objeto2, objeto3, objeto4, objeto5);
    }
    public void plastificar(Object objeto1,
			    Object objeto2,
			    Object objeto3,
			    Object objeto4,
			    Object objeto5,
			    Object objeto6, 
			    Object objeto7) {
	plas.plastificar(objeto1, objeto2, objeto3,
			 objeto4, objeto5, objeto6, objeto7);
    }
    public void actualizar(Object obj1, Object obj2) {
	plas.actualizar(obj1, obj2);
    }

    /*******************************************************************/

    // Puertas de entrada a los servicios del AdminDB
    // Indice:

    public int nuevoIndice(int categoria) {
	return indi.nuevoIndice(categoria);
    }
    public int getIndice(int categoria) {
	return indi.getIndice(categoria);
    }
    public String getIndiceStr(int categoria) {
	return indi.getIndiceStr(categoria);
    }

    /*******************************************************************/

    // Puertas de entrada a los servicios del AdminDB
    // Borrador:

    public void borrar(Object objeto) {
	borr.borrar(objeto);
    }
    public void borrar(Enumeration enum) {
	borr.borrar(enum);
    }
}
