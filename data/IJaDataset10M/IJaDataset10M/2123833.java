package pe.com.bn.sach.calculoCuota;

import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.log4j.Logger;

public class TestCalculo {

    private static Logger log = Logger.getLogger(Util.class.getName());

    public static void main(String[] args) {
        CalculoCuotaHipotecario hipo = new CalculoCuotaHipotecario();
        Hashtable cuotaTabla = new Hashtable();
        double costoEfectivoAnual = 0.0;
        ArrayList cuota = null;
        cuotaTabla = hipo.CalculoPrestamoCuota(100540.5, 100, 30, 2008, BNDate.MES_SETIEMBRE, 10, 0.1, 150000, 0.03, 234, 5, 25, 6, 1, 1, 2);
        cuota = (ArrayList) cuotaTabla.get("calculoCuota");
        costoEfectivoAnual = ((Double) cuotaTabla.get("costoEfectivo")).doubleValue();
        Util.printCronograma(cuota);
        log.info("Costo Efectivo:" + costoEfectivoAnual);
    }
}
