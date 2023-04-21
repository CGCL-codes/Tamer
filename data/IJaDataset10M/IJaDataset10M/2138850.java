package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Values.ProductoValue;

public abstract class ServletTemplate extends HttpServlet {

    /**
     * Maneja un pedido GET de un cliente
     * 
     * @param request Pedido del cliente
     * @param response Respuesta
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarPedido(request, response);
    }

    /**
     * Maneja un pedido POST de un cliente
     * 
     * @param request Pedido del cliente
     * @param response Respuesta
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarPedido(request, response);
    }

    /**
     * Procesa el pedido de igual manera para todos
     * 
     * @param request Pedido del cliente
     * @param response Respuesta
     * @throws IOException Excepci�n de error al escribir la respuesta
     */
    private void procesarPedido(HttpServletRequest request, HttpServletResponse response) throws IOException {
        imprimirHeader(request, response);
        escribirContenido(request, response);
        imprimirFooter(response);
    }

    /**
     * Imprime el Header del dise�o de la p�gina
     * 
     * @param request Pedido del cliente
     * @param response Respuesta
     * @throws IOException Excepci�n al imprimir en el resultado
     */
    private void imprimirHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter respuesta = response.getWriter();
        respuesta.write(" <html> \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" <head> \r\n");
        respuesta.write(" <meta http-equiv=\"Content-Language\" content=\"es-co\"> \r\n");
        respuesta.write(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"> \r\n");
        respuesta.write(" <title>Virtual Marche - Principal</title> \r\n");
        respuesta.write(" <style type=\"text/css\"> \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" .Estilo1 {color: #FFFFFF} \r\n");
        respuesta.write(" .Estilo2 {color: #000066} \r\n");
        respuesta.write(" body { \r\n");
        respuesta.write(" 	background-color: #000066; \r\n");
        respuesta.write(" } \r\n");
        respuesta.write(" .Estilo3 {color: #000066; font-weight: bold; } \r\n");
        respuesta.write("  \r\n");
        respuesta.write(" </style> \r\n");
        respuesta.write(" </head> \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" <body> \r\n");
        respuesta.write(" <div align=center> \r\n");
        respuesta.write(" <center> \r\n");
        respuesta.write(" <form method=\"POST\" action=\"resultado.htm\"> \r\n");
        respuesta.write(" <table border=\"0\" width=\"720\" id=\"table1\"> \r\n");
        respuesta.write(" <tr> \r\n");
        respuesta.write(" <td>&nbsp;</td> \r\n");
        respuesta.write(" 	</tr> \r\n");
        respuesta.write(" 	<tr> \r\n");
        respuesta.write(" 		<td align=\"center\"><span class=\"style2\">VIRTUALMARCHE</span></td> \r\n");
        respuesta.write(" 	</tr> \r\n");
        respuesta.write(" 	<tr bgcolor=\"#FFFFFF\"> \r\n");
        respuesta.write(" 		<td> \r\n");
        respuesta.write(" 		<table border=\"1\" width=\"100%\" style=\"border-collapse: collapse\" bordercolor=\"#999999\" id=\"table2\"> \r\n");
        respuesta.write(" 			<tr> \r\n");
        respuesta.write(" 				<td bgcolor=\"#FFFFFF\"><table border=\"0\" width=\"89%\" id=\"table5\"> \r\n");
        respuesta.write(" 								<tr> \r\n");
        respuesta.write(" 									<td width=\"103\" height=\"39\" align=\"right\"><span class=\"Estilo2\">Ciudad:</span></td> \r\n");
        respuesta.write(" 								  <td height=\"39\"><label></label> \r\n");
        respuesta.write("                                     <p align=\"center\"> \r\n");
        respuesta.write(" 									  <label></label> \r\n");
        respuesta.write(" 									  <label></label> \r\n");
        respuesta.write(" 									  <label></label> \r\n");
        respuesta.write(" \r\n");
        respuesta.write("                                       <select name=\"ciudad\" size=\"1\"> \r\n");
        respuesta.write("                                         <option>--Elija la Ciudad--</option> \r\n");
        respuesta.write("                                         <option>Bucaramanga</option> \r\n");
        respuesta.write("                                         <option>Cali</option> \r\n");
        respuesta.write("                                         <option>Barranquilla</option> \r\n");
        respuesta.write("                                         <option>Bogota</option> \r\n");
        respuesta.write("                                         <option>Medellin</option> \r\n");
        respuesta.write("                                         <option>Armenia</option> \r\n");
        respuesta.write("                                         <option>Cartagena</option> \r\n");
        respuesta.write("                                         <option>Monteria</option> \r\n");
        respuesta.write("                                         <option>Pereira</option> \r\n");
        respuesta.write("                                                                             </select> \r\n");
        respuesta.write("                                     </p></td> \r\n");
        respuesta.write(" 							    </tr> \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" 								<tr> \r\n");
        respuesta.write(" 								  <td width=\"103\" height=\"14\" align=\"right\"><strong class=\"Estilo2\">Categor&iacute;a:</strong></td> \r\n");
        respuesta.write(" 								  <td height=\"14\"><p align=\"center\"> \r\n");
        respuesta.write(" 								    <label></label>\r\n");
        respuesta.write(" 								    <label></label>\r\n");
        respuesta.write(" 								    <select name=\"categoria\" size=\"1\"> \r\n");
        respuesta.write(" 								      <option>--Elija la categor&iacute;a--</option> \r\n");
        respuesta.write(" 								      <option>Aseo</option> \r\n");
        respuesta.write(" 								      <option>Mascotas</option> \r\n");
        respuesta.write(" 								      <option>Frutas</option> \r\n");
        respuesta.write(" 								      <option>Aseo Hogar</option> \r\n");
        respuesta.write("                                                                                                             </select> \r\n");
        respuesta.write(" </p></td> \r\n");
        respuesta.write(" 						      </tr> \r\n");
        respuesta.write(" 								<tr> \r\n");
        respuesta.write(" 								  <td height=\"14\" align=\"right\"><strong class=\"Estilo2\">M&iacute;nimo Existencias::</strong></td> \r\n");
        respuesta.write(" 								  <td height=\"14\"><label> \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" 							        <div align=\"center\"> \r\n");
        respuesta.write(" 								        <select name=\"existencias\" size=\"1\"> \r\n");
        respuesta.write(" 								          <option>--Elija Existencias--</option> \r\n");
        respuesta.write(" 								          <option>01</option> \r\n");
        respuesta.write(" 								          <option>02</option> \r\n");
        respuesta.write(" 								          <option>03</option> \r\n");
        respuesta.write(" 								          <option>04</option> \r\n");
        respuesta.write(" 								          <option>05</option> \r\n");
        respuesta.write(" 								          <option>06</option> \r\n");
        respuesta.write(" 								          <option>07</option> \r\n");
        respuesta.write(" 								          <option>08</option> \r\n");
        respuesta.write(" 								          <option>09</option> \r\n");
        respuesta.write(" 								          <option>10</option> \r\n");
        respuesta.write(" 								          <option>11</option> \r\n");
        respuesta.write(" 								          <option>12</option> \r\n");
        respuesta.write(" 							            </select> \r\n");
        respuesta.write(" 							          </label> \r\n");
        respuesta.write(" 						          </div></td> \r\n");
        respuesta.write(" 						      </tr> \r\n");
        respuesta.write(" 								<tr> \r\n");
        respuesta.write(" 								  <td height=\"14\" align=\"right\">&nbsp;</td> \r\n");
        respuesta.write(" 								  <td height=\"14\"><div align=\"center\"> \r\n");
        respuesta.write(" 								    <input type=\"submit\" value=\"Buscar\" name=\"B1\" class=\"normal\"> \r\n");
        respuesta.write(" 							      </div></td> \r\n");
        respuesta.write(" 						      </tr> \r\n");
        respuesta.write(" 							</table> \r\n");
        respuesta.write(" 						</form></td> \r\n");
        respuesta.write(" 			</tr> \r\n");
        respuesta.write(" 		</table> \r\n");
        respuesta.write(" 		</td> \r\n");
        respuesta.write(" 	</tr> \r\n");
        respuesta.write(" </table> \r\n");
        respuesta.write(" \r\n");
        respuesta.write(" 					<tr> \r\n");
        respuesta.write(" 						<td width=\" 42\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 						<td width=\" 572\"  colspan=\" 2\"  bgcolor=\"#FFFFFF\" ><p align=\"center\"  class=\" Estilo2\" >&nbsp;</p> \r\n");
        respuesta.write(" 					    </td> \r\n");
        respuesta.write(" 						<td width=\" 82\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 					</tr> \r\n");
        respuesta.write(" 					<tr aling=\"center\"> \r\n");
        respuesta.write(" 						<td width=\" 42\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 						<td width=\" 25\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 						<td  bgcolor=\"#FFFFFF\" > \r\n");
    }

    /**
     * Imprime el Footer del dise�o de la p�gina
     * 
     * @param response Respuesta
     * @throws IOException Excepci�n al escribir en la respuesta
     */
    private void imprimirFooter(HttpServletResponse response) throws IOException {
        PrintWriter respuesta = response.getWriter();
        respuesta.write("</td>  \r\n");
        respuesta.write(" 						<td width=\" 82\"  bgcolor=\"#FFFFFF\" ></td> \r\n");
        respuesta.write(" 					</tr> \r\n");
        respuesta.write(" 					<tr> \r\n");
        respuesta.write(" 						<td width=\" 42\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 						<td width=\" 25\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 						<td width=\" 543\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 						<td width=\" 82\"  bgcolor=\"#FFFFFF\" >&nbsp;</td> \r\n");
        respuesta.write(" 					</tr> \r\n");
        respuesta.write(" 				</table> \r\n");
        respuesta.write(" 				</td> \r\n");
        respuesta.write(" 			</tr> \r\n");
        respuesta.write(" 			<tr> \r\n");
        respuesta.write(" 				<td bgcolor=\"#FFFFFF\"  ><a href=index.htm>Ir a Home</a></td> \r\n");
        respuesta.write(" 			</tr> \r\n");
        respuesta.write(" 			<tr> \r\n");
        respuesta.write(" 				<td bgcolor=\"#FFFFFF\" > \r\n");
        respuesta.write(" 							<table border=\" 0\"  width=\" 100%\"  id=\" table6\" > \r\n");
        respuesta.write(" 								<tr> \r\n");
        respuesta.write(" 									<td>&nbsp;</td> \r\n");
        respuesta.write(" 								  <td><p class=\" Estilo2\" >&nbsp;</p> \r\n");
        respuesta.write(" 							      </td> \r\n");
        respuesta.write(" 								</tr> \r\n");
        respuesta.write(" 							</table> \r\n");
        respuesta.write("	<table width=\"100%\"  border=\" 0\" > \r\n");
        respuesta.write("                   <tr> \r\n");
        respuesta.write("                     <td width=\" 51\"  rowspan=\" 2\" >&nbsp;</td> \r\n");
        respuesta.write("                     <td width=\" 51\"  rowspan=\" 2\" >&nbsp;</td> \r\n");
        respuesta.write("                     <td width=\" 489\"  rowspan=\" 2\" ><p align=\"center\"  class=\" Estilo2\" ></p> \r\n");
        respuesta.write("                     <p align=\"center\"  class=\" Estilo2\"></p></td> \r\n");
        respuesta.write("                     <td width=\" 100\" ><div align=\"center\" > \r\n");
        respuesta.write("                     </div></td> \r\n");
        respuesta.write("                   </tr> \r\n");
        respuesta.write("                 </table> \r\n");
        respuesta.write(" 			</tr> \r\n");
        respuesta.write(" 		</table> \r\n");
        respuesta.write(" 		</td> \r\n");
        respuesta.write(" 	</tr> \r\n");
        respuesta.write(" </table> \r\n");
        respuesta.write("  \r\n");
        respuesta.write(" </center> \r\n");
        respuesta.write(" </div> \r\n");
        respuesta.write(" </body> \r\n");
        respuesta.write("  \r\n");
        respuesta.write(" </html> \r\n");
    }

    /**
     * Imprime un mensaje de error
     * 
     * @param respuesta Respuesta al cliente
     * @param titulo T�tulo del error
     * @param mensaje Mensaje del error
     */
    protected void imprimirMensajeError(HttpServletResponse response, String titulo, String mensaje) {
        PrintWriter respuesta;
        try {
            respuesta = response.getWriter();
            respuesta.write("                      <p class=\"error\"><b>Ha ocurrido un error!:<br>\r\n");
            respuesta.write("                      </b>" + titulo + "</p><p>" + mensaje + ". </p>\r\n");
            respuesta.write("                      <p>Intente la \r\n");
            respuesta.write("                      operaci�n nuevamente. Si el problema persiste, contacte \r\n");
            respuesta.write("                      al administrador del sistema.</p>\r\n");
            respuesta.write("                      <p><a href=\"index.htm\">Volver a la p�gina principal</a>\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imprime un mensaje de �xito
     * 
     * @param respuesta Respuesta al cliente
     * @param titulo T�tulo del mensaje
     * @param mensaje Contenido del mensaje
     */
    protected void imprimirMensajeOk(PrintWriter respuesta, String titulo, String mensaje) {
        respuesta.write("                      <p class=\"ok\"><b>Operaci�n exitosa:<br>\r\n");
        respuesta.write("                      </b>" + titulo + "</p><p>" + mensaje + ". </p>\r\n");
        respuesta.write("                      <p><a href=\"index.htm\">Volver a la p�gina principal</a>\r\n");
    }

    /**
     * Devuelve el t�tulo de la p�gina para el Header
     * 
     * @param request Pedido del cliente
     * @return T�tulo de la p�gina para el Header
     */
    public abstract String darTituloPagina(HttpServletRequest request);

    /**
     * Devuelve el nombre de la im�gen para el t�tulo de la p�gina en el Header
     * 
     * @param request Pedido del cliente
     * @return Nombre de la im�gen para el t�tulo de la p�gina en el Header
     */
    public abstract String darImagenTitulo(HttpServletRequest request);

    /**
     * Escribe el contenido de la p�gina
     * 
     * @param request Pedido del cliente
     * @param response Respuesta
     * @throws IOException Excepci�n de error al escribir la respuesta
     */
    public abstract void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
