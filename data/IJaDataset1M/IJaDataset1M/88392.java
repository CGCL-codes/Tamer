package test;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import pos.data.IVotarDAO;
import pos.data.JDBCVotarDAO;
import pos.domain.Voto;
import pos.domain.VotoImpl;

public class TestVoto {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        List<Voto> lv = new LinkedList<Voto>();
        IVotarDAO vdao = new JDBCVotarDAO();
        lv = vdao.selectAll();
        for (Voto v : lv) {
            System.out.print("Voto del usuario " + v.getUsuario() + " de la aplicacion " + v.getAplicacion() + "\n");
        }
        lv = vdao.selectVotosByUser("ju");
        for (Voto v : lv) {
            System.out.print("Voto del usuario X " + v.getUsuario() + " de la aplicacion " + v.getAplicacion() + "\n");
        }
        lv = vdao.selectVotoByAplicacion("VLC");
        for (Voto v : lv) {
            System.out.print("Voto de la aplicacion X " + v.getAplicacion() + " del usuario " + v.getUsuario() + "\n");
        }
        Voto v = new VotoImpl();
        v.setIDVoto("112432");
        v.setAplicacion("VLC");
        v.setUsuario("a");
        v.setValor(true);
        vdao.insertVoto(v);
    }
}
