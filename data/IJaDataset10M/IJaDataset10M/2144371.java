package ModuloReferenciaRemota_Cliente;

import java.util.ArrayList;
import java.util.List;
import Administrador_Cliente.AdministradorCliente;
import ModuloComunicacion_Cliente.ComunicacionCliente;

public class ModuloReferenciaRemota {

    private List<Referencia> referencias;

    private Referencia referencia;

    private String Alias;

    private ComunicacionCliente comCliente;

    private AdministradorCliente admCliente;

    public ModuloReferenciaRemota(AdministradorCliente adm, ComunicacionCliente com) {
        admCliente = adm;
        comCliente = com;
        referencias = new ArrayList<Referencia>();
    }

    public boolean BuscarReferencia(String alias) {
        boolean encontrado = false;
        String mensaje;
        Alias = alias;
        for (int i = 0; i < referencias.size(); i++) {
            if (referencias.get(i).getAlias().equals(Alias)) {
                referencia = new Referencia(referencias.get(i).getDireccion(), referencias.get(i).getPuerto(), referencias.get(i).getAlias());
                return true;
            }
        }
        comCliente.IniciarConexion(admCliente.getPuertoMailbox(), admCliente.getIpMailbox());
        encontrado = comCliente.Buscar(alias);
        if (encontrado) {
            mensaje = comCliente.getReferencia();
            if (!mensaje.equals("No")) {
                Registrar(mensaje.split(",")[0], mensaje.split(",")[1], mensaje.split(",")[2]);
                return true;
            }
        }
        return false;
    }

    public void Registrar(String ip, String puerto, String alias) {
        referencias.add(new Referencia(ip, puerto, alias));
        referencia = new Referencia(ip, puerto, alias);
    }

    public Referencia getReferencia() {
        return referencia;
    }
}
