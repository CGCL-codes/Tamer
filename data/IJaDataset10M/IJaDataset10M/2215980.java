package app.contatos.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContatoHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "contatos.db";

    private static final int VERSAO = 1;

    public ContatoHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contato ( _id INTEGER PRIMARY KEY AUTOINCREMENT," + "nome TEXT, telefone TEXT, email TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void criar(Contato contato) {
        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        getWritableDatabase().insert("contato", "telefone", values);
    }

    public Cursor listar() {
        return getReadableDatabase().rawQuery("SELECT _id, nome, telefone, email FROM contato ORDER BY nome", null);
    }

    public String getNome(Cursor c) {
        return c.getString(1);
    }

    public String getTelefone(Cursor c) {
        return c.getString(2);
    }

    public String getEmail(Cursor c) {
        return c.getString(3);
    }

    public Cursor ler(String id) {
        String[] params = { id };
        return getReadableDatabase().rawQuery("SELECT _id, nome, telefone, email FROM contato WHERE _id = ?", params);
    }

    public void atualizar(String id, Contato contato) {
        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        String[] params = { id };
        getWritableDatabase().update("contato", values, "_id = ?", params);
    }

    public int deletar(String id) {
        String whereClause = "_id = ?";
        String[] whereArgs = { id };
        return getWritableDatabase().delete("contato", whereClause, whereArgs);
    }
}
