package capaEnlac2.excepciones2;

/**
 * Esta clase se encarga de disparar una excepcion de ModBus para las funciones 3, 6 y 16
 * Para ello analiza la trama de datos de entrada
 * @author TipituX
 *
 */
public class ManejadorExcepciones {

    public static final byte ERROR_CODE_RHR = (byte) 0x83;

    public static final byte ERROR_CODE_WR = (byte) 0x86;

    public static final byte ERROR_CODE_WMR = (byte) 0x90;

    public void verificarTrama(byte[] trama) throws Exception {
        byte error_code = trama[1];
        byte exception_code = trama[2];
        if (error_code == ERROR_CODE_RHR) {
            if (exception_code == FunctionCodeException.EXCEPTION_CODE) {
                throw new FunctionCodeException("Excepcin: Cdigo de funcin no soportado.");
            } else if (exception_code == AddressException.EXCEPTION_CODE) {
                throw new AddressException("Excepcin: La direccion es incorrecta.");
            } else if (exception_code == QuantityRegistersException.EXCEPTION_CODE) {
                throw new QuantityRegistersException("Excepcin: El nmero de registros a leer es incorrecto.");
            } else if (exception_code == ReadMultipleRegistersException.EXCEPTION_CODE) {
                throw new ReadMultipleRegistersException("Excepci�n: No se ha podido leer el/los registro/s.");
            }
        } else if (error_code == ERROR_CODE_WR) {
            if (exception_code == FunctionCodeException.EXCEPTION_CODE) {
                throw new FunctionCodeException("Excepci�n: C�digo de funci�n no soportado.");
            } else if (exception_code == RegisterValueException.EXCEPTION_CODE) {
                throw new RegisterValueException("Excepci�n: El valor es incorrecto, debe estar comprendido entre 0 y 65535.");
            } else if (exception_code == RegisterAddressException.EXCEPTION_CODE) {
                throw new RegisterAddressException("Excepci�n: La direccion del registro a escribir es incorrecta.");
            } else if (exception_code == WriteException.EXCEPTION_CODE) {
                throw new WriteException("Excepci�n: No se ha podido escribir el registro.");
            }
        } else if (error_code == ERROR_CODE_WMR) {
            if (exception_code == FunctionCodeException.EXCEPTION_CODE) {
                throw new FunctionCodeException("Excepci�n: C�digo de funci�n no soportado.");
            } else if (exception_code == AddressException.EXCEPTION_CODE) {
                throw new RegisterAddressException("Excepci�n: La direccion del registro a escribir es incorrecta.");
            } else if (exception_code == QuantityRegistersException.EXCEPTION_CODE) {
                throw new AddressException("Excepci�n: El numero de registros a escribir es incorrecto.");
            } else if (exception_code == ReadMultipleRegistersException.EXCEPTION_CODE) {
                throw new WriteException("Excepci�n: No se ha podido escribir el/los registro/s.");
            }
        }
    }
}
