package test.ncalvo;

import java.io.IOException;
import java.util.ArrayList;
import ar.com.datos.compresion.methods.lz78.*;
import ar.com.datos.dataaccess.exceptions.NoExisteArchivoException;
import ar.com.datos.dataaccess.exceptions.NoSePuedeGrabarRegistroException;
import ar.com.datos.dataaccess.exceptions.NoSePuedeLeerRegistroException;

public class testCompirmir {

    /**
	 * @param args
	 * @throws IOException 
	 * @throws NoSePuedeGrabarRegistroException 
	 * @throws NoSePuedeLeerRegistroException 
	 * @throws NoExisteArchivoException 
	 */
    public static void main(String[] args) throws NoSePuedeGrabarRegistroException, IOException, NoSePuedeLeerRegistroException, NoExisteArchivoException {
        LZ78 compresor = new LZ78(17);
        String data = "La primera computadora fue la m�quina anal�tica creada por Charles Babbage, profesor matem�tico de la Universidad de Cambridge en el siglo XIX. La idea que tuvo Charles Babbage sobre un computador naci� debido a que la elaboraci�n de las tablas matem�ticas era un proceso tedioso y propenso a errores. En 1823 el gobierno Brit�nico lo apoyo para crear el proyecto de una m�quina de diferencias, un dispositivo mec�nico para efectuar sumas repetidas. " + "Mientras tanto Charles Jacquard (franc�s), fabricante de tejidos, hab�a creado un telar que pod�a reproducir autom�ticamente patrones de tejidos leyendo la informaci�n codificada en patrones de agujeros perforados en tarjetas de papel r�gido. Al enterarse de este m�todo Babbage abandon� la m�quina de diferencias y se dedico al proyecto de la m�quina anal�tica que se pudiera programar con tarjetas perforadas para efectuar cualquier c�lculo con una precisi�n de 20 d�gitos. La tecnolog�a de la �poca no bastaba para hacer realidad sus ideas." + "El mundo no estaba listo, y no lo estar�a por cien a�os m�s. En 1944 se construy� en la Universidad de Harvard, la Mark I, dise�ada por un equipo encabezado por Howard H. Aiken. Esta m�quina no est� considerada como computadora electr�nica debido a que no era de prop�sito general y su funcionamiento estaba basado en dispositivos electromec�nicos llamados relevadores." + "En 1947 se construy� en la Universidad de Pennsylvania la ENIAC (Electronic Numerical Integrator And Calculator) que fue la primera computadora electr�nica, el equipo de dise�o lo encabezaron los ingenieros John Mauchly y John Eckert. Esta m�quina ocupaba todo un s�tano de la Universidad, ten�a m�s de 18 000 tubos de vac�o, consum�a 200 KW de energ�a el�ctrica y requer�a todo un sistema de aire acondicionado, pero ten�a la capacidad de realizar cinco mil operaciones aritm�ticas en un segundo." + "En 1947 se construy� en la Universidad de Pennsylvania la ENIAC (Electronic Numerical Integrator And Calculator) que fue la primera computadora electr�nica, el equipo de dise�o lo encabezaron los ingenieros John Mauchly y John Eckert. Esta m�quina ocupaba todo un s�tano de la Universidad, consum�a 200 KW de energ�a el�ctrica y requer�a todo un sistema de aire acondicionado, pero ten�a la capacidad de realizar cinco mil operaciones aritm�ticas en un segundo." + "El proyecto, auspiciado por el departamento de Defensa de los Estados Unidos, culmin� dos a�os despu�s, cuando se integr� a ese equipo el ingeniero y matem�tico h�ngaro John von Neumann (1903 - 1957). Las ideas de von Neumann resultaron tan fundamentales para su desarrollo posterior, que es considerado el padre de las computadoras." + "La EDVAC (Electronic Discrete Variable Automatic Computer) fue dise�ada por este nuevo equipo. Ten�a aproximadamente cuatro mil bulbos y usaba un tipo de memoria basado en tubos llenos de mercurio por donde circulaban se�ales el�ctricas sujetas a retardos." + "La idea fundamental de von Neumann fue: permitir que en la memoria coexistan datos con instrucciones, para que entonces la computadora pueda ser programada en un lenguaje, y no por medio de alambres que el�ctricamente interconectaban varias secciones de control, como en la ENIAC." + "Todo este desarrollo de las computadoras suele divisarse por generaciones y el criterio que se determin� para determinar." + "No todo son microcomputadoras, por su puesto, las minicomputadoras y los grandes sistemas contin�an en desarrollo. De hecho las m�quinas peque�as rebasaban por mucho la capacidad de los grandes sistemas de 10 o 15 a�os antes, que requer�an de instalaciones costosas y especiales, pero ser�a equivocado suponer que las grandes computadoras han desaparecido; por el contrario, su presencia era ya ineludible en pr�cticamente todas las esferas de control gubernamental, militar y de la gran industria.";
        byte[] comprimido = compresor.compress(data.toCharArray());
        System.out.println(comprimido);
        System.out.println("********************************************************************************************************************");
        StringBuffer salida2 = compresor.descompress(comprimido);
        System.out.println(salida2);
    }
}
