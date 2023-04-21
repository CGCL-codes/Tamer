package Enlace;

/**
 *
 * @author Daniel Gomez
 */
public class Trama_Acuse {

    private boolean estado;

    private String Flag = "11111111111111111111";

    String cargaUtil;

    String numSec;

    String trama;

    public Trama_Acuse(boolean b, int cual) {
        System.out.println("CUAL EN EL ACUSE " + cual);
        cargaUtil = Integer.toBinaryString(cual);
        for (int i = 0; cargaUtil.length() < 7; i++) {
            cargaUtil = "0" + cargaUtil;
        }
        for (int i = 0; cargaUtil.length() < 18; i++) {
            cargaUtil += "0";
        }
        estado = b;
        CRC c = new CRC();
        String crc = c.divi(cargaUtil);
        while (crc.length() < 17) {
            crc = "0" + crc;
        }
        if (estado) {
            trama = Flag + "1000" + cargaUtil + crc + Flag;
        } else {
            trama = Flag + "1001" + cargaUtil + crc + Flag;
        }
        System.out.println("SIZE ACUSE " + trama.length());
    }

    public String getTrama() {
        return trama;
    }
}
