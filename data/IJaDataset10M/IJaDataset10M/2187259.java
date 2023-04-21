package com.aimo.sked.bd;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.aimo.sked.AimoSked;
import com.aimo.sked.modelo.Evento;

public class AdaptadorBD {

    private AyudanteBD mBdHelper;

    private SQLiteDatabase mBd;

    public final SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    /**
     * Sentencia de creacion
     */
    public static final String CREACION_ASISTENTES = "CREATE TABLE asistentes (" + "asistente_id integer primary key," + "contacto_id text not null," + "evento_id integer not null);";

    public static final String CREACION_EVENTOS = "CREATE TABLE eventos (" + "evento_id integer primary key," + "fecha integer not null," + "asunto text not null," + "lugar text," + "detalles text);";

    private static final String NOMBRE_BD = "AimoSkedDB";

    private static final int VERSION_BD = 1;

    private final Context mCtx;

    private static class AyudanteBD extends SQLiteOpenHelper {

        AyudanteBD(Context context) {
            super(context, NOMBRE_BD, null, VERSION_BD);
        }

        @Override
        public void onCreate(SQLiteDatabase bd) {
            bd.execSQL(CREACION_EVENTOS);
            bd.execSQL(CREACION_ASISTENTES);
            Log.v("AimoSked", "Creada la base de datos AimoSkedDB");
        }

        @Override
        public void onUpgrade(SQLiteDatabase bd, int versionVieja, int versionNueva) {
            bd.execSQL("DROP TABLE IF EXISTS eventos");
            bd.execSQL("DROP TABLE IF EXISTS asistentes");
            onCreate(bd);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public AdaptadorBD(Context ctx) {
        this.mCtx = ctx;
    }

    public AdaptadorBD abrir() throws SQLException {
        mBdHelper = new AyudanteBD(mCtx);
        mBd = mBdHelper.getWritableDatabase();
        return this;
    }

    public void cerrar() {
        mBdHelper.close();
    }

    public void vaciar() {
        mBd.execSQL("delete from eventos");
        mBd.execSQL("delete from asistentes");
    }

    public boolean estaVacia() {
        Cursor c1 = this.leerEventos();
        Cursor c2 = this.leerAsistentes();
        return (c1.getCount() == 0 && c2.getCount() == 0);
    }

    public long crearEvento(Evento evento) {
        Log.v(AimoSked.TAG, "entramos a crear el evento");
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put("asunto", evento.getAsunto());
        valoresIniciales.put("fecha", evento.getFecha());
        valoresIniciales.put("lugar", evento.getLugar());
        valoresIniciales.put("detalles", evento.getDetalles());
        long indice = mBd.insert("eventos", null, valoresIniciales);
        guardarAsistentes(indice, evento.getAsistentes());
        return indice;
    }

    public void guardarAsistentes(long evento_id, ArrayList<String> asistentes) {
        for (int i = 0; i < asistentes.size(); i++) {
            guardarAsistente(evento_id, asistentes.get(i));
        }
    }

    public void guardarAsistente(long evento_id, String contacto_id) {
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put("contacto_id", contacto_id);
        valoresIniciales.put("evento_id", evento_id);
        mBd.insert("asistentes", null, valoresIniciales);
    }

    public boolean eliminarAsistente(long contacto_id, long evento_id) {
        String condicion = "contacto_id" + "=" + contacto_id + " and evento_id" + "=" + evento_id;
        return mBd.delete("asistentes", condicion, null) > 0;
    }

    public boolean eliminarEvento(long evento_id) {
        eliminarAsistentesEvento(evento_id);
        String condicion = "evento_id" + "=" + evento_id;
        return mBd.delete("eventos", condicion, null) > 0;
    }

    public void eliminarAsistentesEvento(long evento_id) {
        String condicion = "evento_id" + "=" + evento_id;
        mBd.delete("asistentes", condicion, null);
    }

    public Cursor leerEventos() {
        String condicion = "1=1";
        return mBd.query("eventos", new String[] { "evento_id", "fecha", "asunto", "lugar", "detalles" }, condicion, null, null, null, null);
    }

    public Cursor leerEvento(int evento_id) {
        String condicion = "evento_id = " + evento_id;
        return mBd.query("eventos", new String[] { "evento_id", "fecha", "asunto", "lugar", "detalles" }, condicion, null, null, null, null);
    }

    public Cursor leerEventosMes(GregorianCalendar date) {
        date.set(GregorianCalendar.DAY_OF_MONTH, 1);
        long iniMes = date.getTimeInMillis();
        date.set(GregorianCalendar.DAY_OF_MONTH, date.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        date.add(GregorianCalendar.DATE, 1);
        long finMes = date.getTimeInMillis();
        String condicion = "fecha >= " + iniMes + " and fecha < " + finMes;
        return mBd.query("eventos", new String[] { "evento_id", "asunto", "lugar", "detalles" }, condicion, null, null, null, null);
    }

    public Cursor leerEventosSemana(GregorianCalendar date) {
        date.add(GregorianCalendar.DATE, -date.get(GregorianCalendar.DAY_OF_WEEK) + 2);
        long iniSemana = date.getTimeInMillis();
        date.add(GregorianCalendar.DATE, 7);
        long finSemana = date.getTimeInMillis();
        String condicion = "fecha >= " + iniSemana + " and fecha <= " + finSemana;
        return mBd.query("eventos", new String[] { "evento_id", "asunto", "lugar", "detalles" }, condicion, null, null, null, null);
    }

    public Cursor leerEventosDia(GregorianCalendar date) {
        String condicion = "fecha = " + date.getTimeInMillis();
        return mBd.query("eventos", new String[] { "evento_id", "asunto", "lugar", "detalles" }, condicion, null, null, null, null);
    }

    public Cursor leerAsistentes() {
        String condicion = "1=1";
        return mBd.query("asistentes", new String[] { "contacto_id" }, condicion, null, null, null, null);
    }

    public Cursor leerAsistentesEvento(long evento_id) {
        String condicion = "evento_id = " + evento_id;
        return mBd.query("asistentes", new String[] { "contacto_id" }, condicion, null, null, null, null);
    }

    public void actualizarEvento(Evento evento) {
        ContentValues nuevosValores = new ContentValues();
        nuevosValores.put("asunto", evento.getAsunto());
        nuevosValores.put("fecha", evento.getFecha());
        nuevosValores.put("lugar", evento.getLugar());
        nuevosValores.put("detalles", evento.getDetalles());
        String condicion = "evento_id = " + evento.getEventoId();
        mBd.update("eventos", nuevosValores, condicion, null);
        actualizarAsistentes(evento.getEventoId(), evento.getAsistentes());
    }

    public void actualizarAsistentes(long evento_id, ArrayList<String> asistentes) {
        eliminarAsistentesEvento(evento_id);
        for (int i = 0; i < asistentes.size(); i++) {
            guardarAsistente(evento_id, asistentes.get(i));
        }
    }
}
