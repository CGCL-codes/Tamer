package projetosd.android.view;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;
import projetosd.android.R;
import projetosd.android.domain.DatabaseManager;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

public class LastPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_page);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveAction();
            }
        });
        Button discardButton = (Button) findViewById(R.id.discardButton);
        discardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    public void saveAction() {
        String xml = null;
        try {
            xml = createXmlAnswers();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gravarResultado(xml, DynamicForm.fichaIdToSave, DynamicForm.fichaNome);
        backToMain();
    }

    private String createXmlAnswers() throws IllegalArgumentException, IllegalStateException, IOException {
        int idPergunta = 0;
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);
        serializer.startDocument("UTF-8", true);
        for (Iterator iterator = DynamicForm.formViewMap.values().iterator(); iterator.hasNext(); ) {
            FormView formView = (FormView) iterator.next();
            List<String> answers = formView.getAnswers(idPergunta);
            for (int i = 0; i < answers.size(); i++) {
                String answer = answers.get(i);
                serializer.startTag("", "pergunta");
                serializer.attribute("", "id", Integer.toString(idPergunta));
                serializer.attribute("", "resposta", answer);
                serializer.endTag("", "pergunta");
            }
            idPergunta++;
        }
        serializer.endDocument();
        return writer.toString();
    }

    private void backToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void gravarResultado(String respostas, String fichaId, String nomeFicha) {
        DatabaseManager dbm = new DatabaseManager(this);
        SQLiteDatabase db = dbm.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseManager.getTableDadosResultado(), respostas);
        cv.put(DatabaseManager.getTableDadosFichaId(), fichaId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        cv.put("date_created", dateFormat.format(date));
        cv.put(DatabaseManager.TABLE_DADOS_NAME, nomeFicha);
        db.insert(DatabaseManager.getTableDados(), null, cv);
        db.close();
    }
}
