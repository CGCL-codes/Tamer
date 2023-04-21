//============================================================================
//
//	Copyright (c) 1999 . All Rights Reserved.
//
//----------------------------------------------------------------------------
//
//	Fichero: TPDUDatosRtx.java  1.0 9/9/99
//
// 	Autores: M. Alejandro Garc�a Dom�nguez (MAlejandroGarcia@wanadoo.es)
//      	 Antonio Berrocal Piris (AntonioBP@wanadoo.es)
//
//	Descripci�n: Clase TPDUDatosRtx.
//
//
//----------------------------------------------------------------------------

package es.realtimesystems.simplemulticast;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Vector;


/**
 * Clase TPDU Datos Rtx.<br>
 * Hereda de la clase TPDUDatos.<br>
 *
 * Para crear un objeto de esta clase se tienen que usar los m�todos est�ticos.
 * Una vez creado no puede ser modicado.<br>
 *
 * El formato completo del TPDU Datos Rtx es: <br>
 *<br>
 *                      1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3<br>
 * +0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1-2-3-4-5-6-7-8-9-0-1<br>
 * +---------------------------------------------------------------+<br>
 * +      Puerto Mulitcast         |          Puerto Unicast       +<br>
 * +---------------------------------------------------------------+<br>
 * +                        ID_GRUPO_LOCAL                         +<br>
 * +                      (4 bytes primeros)                       +<br>
 * +---------------------------------------------------------------+<br>
 * +      ID_GRUPO_LOCAL           |            Longitud           +<br>
 * +    (2 bytes �ltimos)          |                               +<br>
 * +---------------------------------------------------------------+<br>
 * +                               |   | | | | | |A|I|F|F|         +<br>
 * +           Cheksum             | V |0|1|0|0|1|C|R|I|I| / / / / +<br>
 * +                               |   | | | | | |K| |N|N|         +<br>
 * +                               |   | | | | | | | |C|T|         +<br>
 * +---------------------------------------------------------------+<br>
 * +        Tama�o Ventana Fuente  |     N�mero R�faga Fuente      +<br>
 * +---------------------------------------------------------------+<br>
 * +                     ID_GRUPO_LOCAL Fuente                     +<br>
 * +                       (4 primeros bytes)                      +<br>
 * +---------------------------------------------------------------+<br>
 * +ID_GRUPO_LOCAL Fuente (2 bytes)| Direcci�n IP Fuente (2 bytes) +<br>
 * +---------------------------------------------------------------+<br>
 * +  Direcci�n IP Fuente (2 bytes)|  Puerto Unicast Fuente        +<br>
 * +---------------------------------------------------------------+<br>
 * +                    N�mero de Secuencia Fuente                 +<br>
 * +---------------------------------------------------------------+<br>
 * + N�mero de     |   N�mero de   |    Direcci�n IP 1 (2 bytes)   +<br>
 * +   Socket      |    IDGLs      |    (no enviado asentimiento)  +<br>
 * +---------------------------------------------------------------+<br>
 * +    Direcci�n IP1   (2 bytes)  |         Puerto Unicast 1      +<br>
 * +    no enviado asentimiento)   |                               +<br>
 * +---------------------------------------------------------------+<br>
 * +                              ....                             +<br>
 * +---------------------------------------------------------------+<br>
 * +                   IDGL Hijo 1   (4 primeros bytes)            +<br>
 * +---------------------------------------------------------------+<br>
 * + IDGL Hijo 1 (2 �ltimos bytes) | IDGL Hijo 2 (2 primeros bytes)+<br>
 * +---------------------------------------------------------------+<br>
 * +                  IDGL Hijo 2 (4 bytes �ltimos)                +<br>
 * +---------------------------------------------------------------+<br>
 * +                              ...                              +<br>
 * +---------------------------------------------------------------+<br>
 * +                            Datos                              +<br>
 * +---------------------------------------------------------------+<br>
 * +                              ...                              +<br>
 * +---------------------------------------------------------------+<br>
 *<br>
 * <br>
 * Esta clase no es thread-safe.<br>
 * @see      Buffer
 * @version  1.0
 * @author M. Alejandro Garc�a Dom�nguez
 * <A HREF="mailto:AlejandroGarcia@wanadoo.es">(AlejandroGarcia@wanadoo.es)</A><p>
 * Antonio Berrocal Piris
 * <A HREF="mailto:AntonioBP.wanadoo.es">(AntonioBP@wanadoo.es)</A><p>
 */
public class TPDUDatosRtx extends TPDUDatos
{
  // ATRIBUTOS

  /** Longitud de los datos comunes a todos los TPDUDatosRtx*/
  private static final int LONGHEADER = (9 * 4) +2;


  /**
   * Tama�o de ventana (16 bits):

   */

  int TAMA�O_VENTANA = 0;


  /**
   * IR (1 bit): Inicio de r�faga
   */
  private byte IR = 0;

  /**
   * FIN_CONEXION (1 bit): Fin de la conexi�n
   */
  byte FIN_CONEXION = 0;

  /**
   * FIN_TRANSMISION (1 bit): Fin de la transmisi�n
   */
  byte FIN_TRANSMISION = 0;

  /**
   * ACK (1 bit):
   */
  private byte ACK = 0;

  /**
   * N�mero de ID_Sockets (8 bits): n�mero de id_sockets que contiene la lista
   * de no asentidos.
   */
  private byte NUMERO_DE_SOCKET = 0;

  /**
   * N�mero de IDGLS (8 bits):n�mero de idgls que contiene la lista
   * de no asentidos.
   */
  private byte NUMERO_IDGL = 0;

  /**
   * IDGL Fuente (48 bits)
   */
  private IDGL IDGL_FUENTE = null;


  /**
   * ID TPDU Fuente : (10 bytes)
   *                    ID Socket Fuente (6 byte)
   *                            Direcci�n IP Fuente (4 byte)
   *                            Puerto Unicast Fuente (2 byte)
   *                    N�mero secuencia Fuente (4 bytes)
   */
   private ID_TPDU ID_TPDU_FUENTE = null;

   /**
    *  N�mero de r�faga fuente (16 bits)
    */
   private int NUMERO_RAFAGA_FUENTE = 0;

   /**
    * Lista con los id_sockets de los que no ha recibido ACK.
    * <table border=1>
    *  <tr>  <td><b>Key:</b></td>
    *	    <td>{@link ID_Socket}.</td>
    *  </tr>
    *  <tr>  <td><b>Value:</b></td>
    *	    <td>NULL</td>
    *  </tr>
    * </table>
    */
   private TreeMap LISTA_ORD_ID_SOCKET = null;

   /**
    * Lista con los idgls de los que no ha recibido HACK o HSACK.
    * <table border=1>
    *  <tr>  <td><b>Key:</b></td>
    *	    <td>{@link IDGL}.</td>
    *  </tr>
    *  <tr>  <td><b>Value:</b></td>
    *	    <td>NULL</td>
    *  </tr>
    * </table>
    */
   private TreeMap LISTA_ORD_IDGL = null;


   /** Datos */
   private Buffer BUFFERDATOS = null;

  //==========================================================================
  /**
   * Constructor utilizado para crear un TPDUDatosRtx.
   * @param socketPTMFImp Objeto SocketPTMFImp del que obtiene el valor de los
   * campos de la cabecera com�n.
   * @exception PTMFExcepcion
   * @exception ParametroInvalidoExcepcion lanzada si socketPTMFImp es null
   */
  private TPDUDatosRtx (SocketPTMFImp socketPTMFImp)
    throws PTMFExcepcion,ParametroInvalidoExcepcion
  {
   super (socketPTMFImp);
  }



  //==========================================================================

  /**
   * Constructor por defecto.
   * Este constructor es para crear TPDUS a partir del parser de un Buffer
   * @exception PTMFExcepcion
   * @exception ParametroInvalido Se lanza en el constructor de la clase TPDU.
   */
  private TPDUDatosRtx ()

      throws ParametroInvalidoExcepcion,PTMFExcepcion

  {

   super();

  }

 //============================================================================
 /**
  * Crea un vector de TPDUDatosRtx con la informaci�n facilitada.
  * El buffer de datos tiene que caber en el TPDU Datos rtx. <br>
  * Todos los TPDU Datos Rtx creados son iguales, excepto en la lista de
  * identificadores de id_socket e idgl que no han enviado asentimiento.<br>
  * La uni�n de todas las listas de id_sockets e idgls de los TPDUDatosRtx del
  * vector creado es igual a las listas pasadas por argumentos, sin repeticiones.
  * @param socketPTMFImp utilzado para obtener informaci�n de la cabecera
  * com�n a todos los TPDU.

  * @param setIR

  * @param setACK
  * @param setFIN_CONEXION
  * @param setFIN_TRANSMISION
  * @param numeroRafagaFuente
  * @param idglFuente
  * @param id_tpduFuente
  * @param treeMapID_Socket contiene los id_sockets que no han enviado ACK
  * para el TPDU Datos Rtx.

  * @param treeMapIDGL contiene los idgls que no han enviado HACK o HSACK

  * para el TPDU Datos Rtx.

  * @param datos

  * @return vector con los TPDUDatosRtx formados

  * @exception ParametroInvalidoExcepcion si alguno de los par�metros es err�neo.

  * @exception PTMFExcepcion si hay un error al crear los TPDUDatosRtx a partir

  * de la informaci�n facilitada en los argumentos.

  */

 static Vector crearVectorTPDUDatosRtx (SocketPTMFImp socketPTMFImp,
                                        boolean setIR,
                                        boolean setACK,
                                        boolean setFIN_CONEXION,
                                        boolean setFIN_TRANSMISION,
                                        int numeroRafagaFuente,
                                        IDGL idglFuente,
                                        ID_TPDU id_tpduFuente,
                                        TreeMap treeMapID_Socket,
                                        TreeMap treeMapIDGL,
                                        Buffer datos)
   throws ParametroInvalidoExcepcion, PTMFExcepcion
 {
  final String mn = "TPDUDatosRtx.crearVectorTPDUDatosRtx (socketPTMFImp,...)";

   int capacidad = 0;

   Vector vectorTPDURtx = new Vector();
   Iterator iteradorID_Socket = null;
   Iterator iteradorIDGL = null;

   if (datos!=null)
     capacidad = PTMF.TPDU_MAX_SIZE - datos.getMaxLength() - TPDUDatosRtx.LONGHEADER;
   else capacidad = PTMF.TPDU_MAX_SIZE - TPDUDatosRtx.LONGHEADER;
   // N�mero de identificadores que caben.

   boolean incluirNoAsentido = false;

   if (treeMapID_Socket!=null)
       if (!treeMapID_Socket.isEmpty())
         {
          iteradorID_Socket = treeMapID_Socket.keySet().iterator();
          incluirNoAsentido = true;
         }

   if (treeMapIDGL!=null)
        if (!treeMapIDGL.isEmpty())
          {
           iteradorIDGL      = treeMapIDGL.keySet().iterator();
           incluirNoAsentido = true;
          }

   // Al menos capacidad deber� ser mayor o igual a 6 para que quepa un IDGL o
   // ID_Socket.
   if (incluirNoAsentido && (capacidad < 6))
     throw new PTMFExcepcion ("No hay espacio para a�adir ning�n identificador.");

   boolean finWhile = false;
   while (!finWhile)
   {
    TreeMap treeMapID_SocketAux = null;
    TreeMap treeMapIDGLAux = null;
    int numIDCaben = capacidad / 6;

    // Incluir todos los id_socket que quepan.
    if ((iteradorID_Socket!=null) && (numIDCaben > 0))
    {
     treeMapID_SocketAux = new TreeMap ();
     while (iteradorID_Socket.hasNext())
      {
       treeMapID_SocketAux.put (iteradorID_Socket.next(),null);
       numIDCaben --;
       if (numIDCaben <= 0)
           continue;
      } // Fin del while de iteradorID_Socket
    } // Fin del if iteradorID_Socket

    // Incluir todos los idgl que quepan.
    if (iteradorIDGL!=null && numIDCaben > 0)
     {
      treeMapIDGLAux = new TreeMap();
      while (iteradorIDGL.hasNext())
       {
        treeMapIDGLAux.put (iteradorIDGL.next(),null);
        numIDCaben --;
        if (numIDCaben <= 0)
            continue;
       } // Fin del while de iteradorIDGL
      } // Fin del if de iteradorIDGL


     // Formar el TPDU y a�adirlo al vector
     TPDUDatosRtx tpduDatosRtx;

     // Crear el TPDUDatosRtx vacio
     tpduDatosRtx = new TPDUDatosRtx (socketPTMFImp);

     // Guardar los datos en la cabecera para cuando sean pedidos
     if (treeMapID_SocketAux!=null)
       {
        tpduDatosRtx.NUMERO_DE_SOCKET = (byte)treeMapID_SocketAux.size ();
        tpduDatosRtx.LISTA_ORD_ID_SOCKET = treeMapID_SocketAux;
       }
     else
         {
          tpduDatosRtx.NUMERO_DE_SOCKET = 0;
          tpduDatosRtx.LISTA_ORD_ID_SOCKET = null;
         }

     if (treeMapIDGLAux!=null)
      {
       tpduDatosRtx.NUMERO_IDGL = (byte)treeMapIDGLAux.size ();
       tpduDatosRtx.LISTA_ORD_IDGL = treeMapIDGLAux;
      }
     else
         {
          tpduDatosRtx.NUMERO_IDGL = 0;
          tpduDatosRtx.LISTA_ORD_IDGL = null;
         }

     tpduDatosRtx.IR  = (byte)(setIR  ? 1 : 0);
     tpduDatosRtx.ACK = (byte)(setACK ? 1 : 0);
     tpduDatosRtx.FIN_CONEXION = (byte)(setFIN_CONEXION ? 1 : 0);
     tpduDatosRtx.FIN_TRANSMISION = (byte)(setFIN_TRANSMISION ? 1 : 0);
     tpduDatosRtx.NUMERO_RAFAGA_FUENTE = numeroRafagaFuente;
     tpduDatosRtx.IDGL_FUENTE = idglFuente;

     tpduDatosRtx.ID_TPDU_FUENTE = id_tpduFuente; // No es necesario clonarlo,
                                                  // no puede ser modificado.

     tpduDatosRtx.BUFFERDATOS = (Buffer)datos;

     // A�adir al vector
     vectorTPDURtx.add (tpduDatosRtx);

     // Evaluar condici�n de finalizaci�n
     finWhile = (iteradorID_Socket == null || !iteradorID_Socket.hasNext())
                &&
                (iteradorIDGL == null || !iteradorIDGL.hasNext());
   } // Fin del while (true)

  return vectorTPDURtx;
 }

  //==========================================================================
  /**
   * Construir el TPDU Datos Rtx, devuelve un buffer con el contenido del TPDUDatosRtx,
   * seg�n el formato especificado en el protocolo.
   * <b>Este buffer no debe de ser modificado.</B>
   * @return un buffer con el TPDUDatosRtx.
   * @exception PTMFExcepcion Se lanza si ocurre alg�n error en la construcci�n
   * del TPDU
   * @exception ParametroInvalidoExcepcion lanzada si ocurre alg�n error en la
   * construcci�n del TPDU
   */
 Buffer construirTPDUDatosRtx () throws PTMFExcepcion,ParametroInvalidoExcepcion
 {
  final String mn = "TPDU.construirTPDUDatosRtx";
  int offset = 14;


   int tama�o = TPDUDatosRtx.LONGHEADER + this.NUMERO_DE_SOCKET*6 + this.NUMERO_IDGL*6;
   if (this.BUFFERDATOS!=null)
      tama�o += this.BUFFERDATOS.getMaxLength();


   // Crear la cabecera com�n a todos los TPDU
   Buffer bufferResult = this.construirCabeceraComun (PTMF.SUBTIPO_TPDU_DATOS_RTX,tama�o);

   // 15� BYTE : ACK : (1 bit)
   short anterior = bufferResult.getByte (offset);
   // anterior : XXXX XXXX
   //      and : 1111 1110 = 0xFE
   //           ----------
   //            XXXX XXX0
   //   ACK    : 0000 000X = 0x01
   //           ----------
   //            XXXX XXXX
   anterior &= 0xFE;
   bufferResult.addByte((byte)((this.ACK & 0x01) | anterior),offset);
   offset++;


   // 16� BYTE : IR (1 bit) y FIN_CONEXION (1bit)

   //                   IR : X000  0000

   //          FIN_CONEXION: 0X00  0000

   //       FIN_TRANSMISION: 00X0  0000

   bufferResult.addByte ((byte)(((this.IR<<7)&0x80) | ((this.FIN_CONEXION<<6)&0x40)  | ((this.FIN_TRANSMISION<<5)&0x20)),offset);

   offset++;


   // 17� Y 18� BYTE : TAMA�O VENTANA

   bufferResult.addShort (this.TAMA�O_VENTANA,offset);
   offset+=2;


   // 19� Y 20� BYTE : N�mero de R�faga Fuente
   bufferResult.addShort (this.NUMERO_RAFAGA_FUENTE,offset);
   offset+=2;

   // 21� , 22�, 23� y 24� BYTE : IDGL Fuente
   if ( ( this.IDGL_FUENTE == null)
           || ( this.IDGL_FUENTE.id.getMaxLength() != 6))
      {
        bufferResult.addInt(0,offset);
        bufferResult.addShort(0,offset+4);
      }
      else
      {
        bufferResult.addBytes( this.IDGL_FUENTE.id,0,offset,6 /*longitud*/);
      }

   offset+=6;




   // 25�, 26�, 27� y 28� BYTE : Direcci�n IP Fuente
   bufferResult.addBytes (this.ID_TPDU_FUENTE.getID_Socket().getDireccion().ipv4,
                                                                    0,offset,4);
   offset+=4;

   // 29� y 30� BYTE : Puerto Unicast Fuente
   bufferResult.addShort (this.ID_TPDU_FUENTE.getID_Socket().getPuertoUnicast(),
                                                                        offset);
   offset+=2;


   // 31�, 32�, 33� y 34� BYTE : N�mero de Secuencia Fuente
   bufferResult.addInt (this.ID_TPDU_FUENTE.getNumeroSecuencia().tolong(),offset);
   offset+=4;


   // 35� BYTE : N�mero de socket
   bufferResult.addByte (this.NUMERO_DE_SOCKET,offset);
   offset++;

   // 36� BYTE : N�mero de IDGLs
   bufferResult.addByte (this.NUMERO_IDGL,offset);
   offset++;

   // [37� ...  this.NUMERO_DE_SOCKET] BYTE : [IP,Puerto Unicast]
   if (this.NUMERO_DE_SOCKET>0)
     {
      Iterator iteradorID_Socket = this.LISTA_ORD_ID_SOCKET.keySet().iterator();
      ID_Socket id_SocketNext = null;
      while (iteradorID_Socket.hasNext())
       {
        id_SocketNext = (ID_Socket)iteradorID_Socket.next();
        // A�adir IP
        bufferResult.addBytes (id_SocketNext.getDireccion().ipv4,0,offset,4);
        offset += 4;
        // A�adir Puerto Unicast
        bufferResult.addShort (id_SocketNext.getPuertoUnicast(),offset);
        offset += 2;
       } // Fin del while
     } // Fin del if


    // A�adir treeMapIDGL
    if((this.NUMERO_IDGL > 0) && (this.LISTA_ORD_IDGL!= null))
    {
      Iterator iterator = this.LISTA_ORD_IDGL.keySet().iterator();

      while(iterator.hasNext())
      {
         IDGL idgl = (IDGL)iterator.next();

         //A�adir idgl
         bufferResult.addBytes(idgl.id,0,offset,6);
         offset+=6;
      }
    }


   // Sucesivos BYTE : Datos
   // offset apunta al byte donde tienen que ir los datos
   if (this.BUFFERDATOS!=null)
      bufferResult.addBytes (this.BUFFERDATOS,0,offset,
                      this.BUFFERDATOS.getMaxLength());


   return bufferResult;
 }

  //==========================================================================
  /**
   * Parse un Buffer de datos recibidos y crea un TPDU Datos Rtx que lo encapsule.
   * El buffer debe de contener un TPDU Datos Rtx.
   * @param buf Un buffer que contiene el TPDU Datos Rtx recibido.
   * @param ipv4Emisor direcci�n IP unicast del emisor.
   * @exception PTMFExcepcion El buffer pasado no contiene una cabecera TPDU
   * correcta, el mensaje de la excepci�n especifica el tipo de error.
   * @exception ParametroInvalidoExcepcion Se lanza si el buffer pasado no
   * contiene un TPDUDatosRtx v�lido.
   */
 static  TPDUDatosRtx parserBuffer (Buffer buffer,IPv4 ipv4Emisor)
   throws PTMFExcepcion,ParametroInvalidoExcepcion
 {

  final String mn = "TPDUDatosRtx.parserBuffer (buffer,ipv4)";
  int aux;
  int offset = 14;

  if (buffer==null)
     throw new ParametroInvalidoExcepcion (mn + "Buffer nulo");

  // La longitud del buffer deber� ser al menos igual a LONGHEADER
  if (TPDUDatosRtx.LONGHEADER > buffer.getMaxLength())
    throw new ParametroInvalidoExcepcion (mn + "Buffer incorrecto");

  // Crear el TPDUDatosRtx.
  TPDUDatosRtx tpduDatosRtx = new TPDUDatosRtx ();


  // Analizar los datos comunes
  TPDUDatos.parseCabeceraComun (buffer,tpduDatosRtx,ipv4Emisor);


  // Comprobar si el subtipo es correcto
  if (tpduDatosRtx.SUBTIPO != PTMF.SUBTIPO_TPDU_DATOS_RTX)
     throw new PTMFExcepcion ("Subtipo del TPDU Datos no v�lido");


  // 15� BYTE : ACK (1 BIT)
  aux = buffer.getByte (offset);
  //     ACK:   XXXX XXXX
  //     And:   0000 0001 = 0x01
  //            ---------
  //            0000 000X
  tpduDatosRtx.ACK = (byte)(aux & 0x01);
  offset++;


  // 16� BYTE : IR (1 bit)  N�mero de IP (7 bits)
  aux = buffer.getByte (offset);
  //      IR:   XXXX XXXX
  //     And:   1000 0000 = 0x80
  //            ---------
  //            X000 0000
  //     >>>:   0000 000X
  tpduDatosRtx.IR = (byte) ((aux & 0x80) >>> 7);
  //     FIN_CONEXION:   XXXX XXXX
  //     And:   0100 0000 = 0x40
  //            ---------
  //            0X00 0000
  //     >>>:   0000 000X
  tpduDatosRtx.FIN_CONEXION = (byte) ((aux & 0x40) >>> 6);
  //     FIN_TRANSMISION:   XXXX XXXX
  //     And:   0100 0000 = 0x40
  //            ---------
  //            00X0 0000
  //     >>>:   0000 000X
  tpduDatosRtx.FIN_TRANSMISION = (byte) ((aux & 0x20) >>> 5);
  offset ++;

  //
  // 17� y 18� BYTE : N�mero de r�faga Fuente
  //
  tpduDatosRtx.NUMERO_RAFAGA_FUENTE = buffer.getShort (offset);
  offset+=2;

  //
  // 17� y 18� BYTE : N�mero de r�faga Fuente
  //
  tpduDatosRtx.NUMERO_RAFAGA_FUENTE = buffer.getShort (offset);
  offset+=2;

  // 19�, 20�, 21� ,22�, 23� y 24� BYTE : IDGL Fuente
  tpduDatosRtx.IDGL_FUENTE =
                   new IDGL (new Buffer(buffer.getBytes(offset,(byte) 6)),(byte)0);
  offset+=6;



  // 25�, 26�, 27� y 28� BYTE : Direcci�n IP Fuente
  IPv4 ipFuente = new IPv4 (new Buffer (buffer.getBytes(offset,4)));
  offset+=4;

  // 29� y 30� BYTE : Puerto Unicast Fuente
  int puertoUnicastFuente = buffer.getShort (offset);

  offset+=2;

  //
  // 31�, 32�, 33� y 34� BYTE : N�mero de Secuencia Fuente
  //
  NumeroSecuencia nSecFuente = new NumeroSecuencia (buffer.getInt (offset));
  offset+=4;

  // Construir el ID_TPDU_FUENTE
  tpduDatosRtx.ID_TPDU_FUENTE = new ID_TPDU (

                                new ID_Socket (ipFuente,puertoUnicastFuente),
                                nSecFuente);

  // 35� BYTE : N�mero de Socket
  tpduDatosRtx.NUMERO_DE_SOCKET = (byte)buffer.getByte (offset);
  offset++;

  // 36� BYTE : N�mero de IDGLs
  tpduDatosRtx.NUMERO_IDGL = (byte)buffer.getByte (offset);
  offset++;

  // [37� ...  tpduDatosRtx.NUMERO_DE_SOCKET] BYTE : [IP,Puerto Unicast]
  // Comprueba la longitud. Cada ID_Socket ocupa 6 bytes
  if (buffer.getMaxLength()<(tpduDatosRtx.LONGHEADER + tpduDatosRtx.NUMERO_DE_SOCKET*6))
        throw new PTMFExcepcion (mn+"Error en el parser");

  // Crear la lista
  if (tpduDatosRtx.NUMERO_DE_SOCKET>0)
       tpduDatosRtx.LISTA_ORD_ID_SOCKET = new TreeMap ();

  ID_Socket id_Socket = null;
  IPv4           ipv4 = null;
  for (int i=0;i<tpduDatosRtx.NUMERO_DE_SOCKET;i++)
   {
    ipv4 = new IPv4 (new Buffer (buffer.getBytes(offset,4)));
    offset += 4;
    id_Socket = new ID_Socket (ipv4,buffer.getShort (offset));
    offset += 2;

    tpduDatosRtx.LISTA_ORD_ID_SOCKET.put (id_Socket,null);
   }

  if( tpduDatosRtx.NUMERO_IDGL > 0)
   {
    tpduDatosRtx.LISTA_ORD_IDGL = new TreeMap();

    //Obtener IDGLs
    for(int i = 0 ; i < tpduDatosRtx.NUMERO_IDGL; i++)
     {
      tpduDatosRtx.LISTA_ORD_IDGL.put(new IDGL(new Buffer(buffer.getBytes(offset,6)),(byte)0) ,null);
      offset+=6;
     }
    }

  // tpduDatosRtx.NUMERO_DE_IP  y sucesivos BYTE : Datos
  // offset apunta al byte donde tienen que ir los datos
  int tama�oDatos = buffer.getMaxLength() - tpduDatosRtx.LONGHEADER -
                   tpduDatosRtx.NUMERO_DE_SOCKET*6 - tpduDatosRtx.NUMERO_IDGL*6;


  if (tama�oDatos>0)

     tpduDatosRtx.BUFFERDATOS = new Buffer (buffer.getBytes (offset,tama�oDatos));

  else tpduDatosRtx.BUFFERDATOS = null;

  return tpduDatosRtx;
 }



 //============================================================================

 /**

  * Construye un TPDUDatosNormal.

  */

 TPDUDatosNormal convertirATPDUDatosNormal ()

                             throws ParametroInvalidoExcepcion, PTMFExcepcion

 {

  final String mn = "TPDUDatosRtx.convertirATPDUDatosNormal()";


  return TPDUDatosNormal.crearTPDUDatosNormal (

                          this.PUERTO_MULTICAST,

                          this.ID_TPDU_FUENTE.getID_Socket().getPuertoUnicast(),

                          this.IDGL_FUENTE,

                          this.ID_TPDU_FUENTE.getID_Socket().getDireccion(),

                          ((this.IR  == 1) ? true : false),

                          ((this.ACK == 1) ? true : false),

                          ((this.FIN_CONEXION == 1) ? true : false),

                          ((this.FIN_TRANSMISION == 1) ? true : false),

                          this.NUMERO_RAFAGA_FUENTE,

                          this.ID_TPDU_FUENTE.getNumeroSecuencia(),

                          this.BUFFERDATOS);

 }


 //============================================================================

 /**

  * Devuelve el n�mero de secuencia.

  */

 NumeroSecuencia getNumeroSecuencia()

 {
   return this.ID_TPDU_FUENTE.getNumeroSecuencia();
 }


 //============================================================================

 /**

  * Devuelve true si el bit IR vale 1.

  */

 boolean getIR ()

 {
   return (this.IR==1 ? true : false);
 }


 //============================================================================

 /**

  * Devuelve true si el bit ACK vale 1.

  */

 boolean getACK()

 {
   return (this.ACK==1 ? true:false);
 }


 //============================================================================

 /**

  * Devuelve true si el bit FIN_CONEXION vale 1.

  */

 boolean getFIN_CONEXION()

 {

   return (this.FIN_CONEXION==1 ? true:false);


 }


 //============================================================================
 /**

  * Devuelve true si el bit FIN_TRANSMISION vale 1.

  */

  boolean getFIN_TRANSMISION()

 {

   return (this.FIN_TRANSMISION==1 ? true:false);


 }


//===========================================================================
 /**
  * Devuelve el ID_TPDU del que mando originalmente los datos (la fuente).
  */
 ID_TPDU getID_TPDUFuente ()
 {
   return this.ID_TPDU_FUENTE;
 }


 //===========================================================================
 /**
  * Devuelve el n�mero de r�faga fuente.

  */

 int getNumeroRafagaFuente ()
 {
  return this.NUMERO_RAFAGA_FUENTE;
 }

 //===========================================================================
 /**
  * Devuelve la lista de id_socket que no han enviado ACK.

  * @return lista con id_Sockets que no han enviado ACK

  * <table border=1>

  *  <tr>  <td><b>Key:</b></td>
  *	    <td>{@link ID_Socket}.</td>
  *  </tr>
  *  <tr>  <td><b>Value:</b></td>
  *	    <td>NULL</td>
  *  </tr>
  * </table>
  */

 TreeMap getListaID_Socket ()
 {
   if (this.LISTA_ORD_ID_SOCKET==null)
     return new TreeMap ();

   return this.LISTA_ORD_ID_SOCKET;
 }


 //===========================================================================
 /**
  * Devuelve la lista de idgls que no han enviado HACK o HSACK.

  * @return lista con id_Sockets que no han enviado HACK o HSACK.

  * <table border=1>

  *  <tr>  <td><b>Key:</b></td>
  *	    <td>{@link IDGL}.</td>
  *  </tr>
  *  <tr>  <td><b>Value:</b></td>
  *	    <td>NULL</td>
  *  </tr>
  * </table>
  */

 TreeMap getListaIDGL ()
 {
  if (this.LISTA_ORD_IDGL==null)
        return new TreeMap();

  return this.LISTA_ORD_IDGL;
 }


 //===========================================================================
 /**
  * Devuelve el tama�o de ventana.

  */

 int getTama�oVentana ()
 {
  return PTMF.TAMA�O_VENTANA_RECEPCION;
 }

 //===========================================================================
 /**
  * Devuelve el idgl al que pertenece el emisor fuente de los datos.

  * @return idgl fuente

  */

 IDGL getIDGLFuente ()
 {
  return this.IDGL_FUENTE;
 }

 //===========================================================================
 /**

  * Devuelve una cadena informativa del TPDU Datos

  */

 public String toString()

 {

   String result = new String (

          "Puerto Multicast: " + this.getPuertoMulticast() +

          "\nPuerto Unicast: " + this.getPuertoUnicast() +

          "\nIDGL: " + this.ID_GRUPO_LOCAL +

          "\nLongitud: " + this.LONGITUD +

          "\nCHECKSUM: " + this.CHEKSUM +

          "\nVersion: " + this.VERSION +

          "\nTipo: " + this.TIPO +

          "\nSubtipo: " + PTMF.SUBTIPO_TPDU_DATOS_RTX +

          "\nACK: " + this.ACK +

          "\nIR: " + this.IR +

          "\nFIN_CONEXION: " + this.FIN_CONEXION +

          "\nFIN_TRANSMISION: " + this.FIN_TRANSMISION +

          "\nN�mero R�faga Fuente: " + this.NUMERO_RAFAGA_FUENTE +

          "\nIDGL Fuente: " + this.IDGL_FUENTE +

          "\nID TPDU Fuente: " + this.ID_TPDU_FUENTE

         );

   // A�adir al String los id. socket

   if (this.LISTA_ORD_ID_SOCKET != null)

     result = result + "\nID_Sockets: " + this.LISTA_ORD_ID_SOCKET;


   // A�adir al String los IDGL

   if (this.LISTA_ORD_IDGL != null)

     result = result + "\nIDGLs: " + this.LISTA_ORD_IDGL;


   result += ("\nDatos: " + this.BUFFERDATOS);


   return result;

 }


} // Fin de la clase.


