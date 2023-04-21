package logica;

import java.sql.Date;

public class Arquero extends Jugador {

    private int golesAtajados;

    public Arquero(int id, String n, String a, String e, int year, int month, int day, float p) {
        super(id, n, a, e, year, month, day, p);
        this.golesAtajados = 0;
    }

    public Arquero(String n, String a, String e, int year, int month, int day, float p) {
        super(n, a, e, year, month, day, p);
        this.golesAtajados = 0;
    }

    public Arquero(int id, String string, String string2, String string3, Date date, int int1, float float1) {
        super(id, string, string2, string3, date, int1, float1);
    }

    public int getGolesAtajados() {
        return golesAtajados;
    }

    public void setGolesAtajados(int golesAtajados) {
        this.golesAtajados = golesAtajados;
    }

    @Override
    public String getPosicion() {
        return "ARQ";
    }
}
