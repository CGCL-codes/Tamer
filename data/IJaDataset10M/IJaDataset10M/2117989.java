package Projet675.Sobjal;

import java.util.Vector;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class configuration extends ListActivity {

    private String[] SMARTOBJETS = { "asasas", "asas", "aasasas" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSmartObject(1);
        setListAdapter(new ArrayAdapter<String>(this, R.xml.configuration, SMARTOBJETS));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSmartObject(int iduser) {
        String URL = "http://153.109.124.79/webServicePHP/server.php";
        String NAMESPACE = "http://www.w3.org/2001/12/soap-envelope";
        String METHOD_NAME = "getSmartObjectsForUser";
        String SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        PropertyInfo pi_idUser = new PropertyInfo();
        pi_idUser.setName("idUser");
        pi_idUser.setValue(iduser);
        pi_idUser.setType(Integer.class);
        request.addProperty(pi_idUser);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = true;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Vector<SoapObject> result = (Vector<SoapObject>) envelope.getResponse();
            for (int i = 0; i < result.size(); i++) {
                SMARTOBJETS[i] = result.get(i).getProperty("nameSmartObject").toString();
            }
        } catch (Exception aE) {
            aE.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_config, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_action:
                Intent intent = new Intent();
                intent.setClass(configuration.this, action.class);
                startActivity(intent);
                configuration.this.finish();
                return true;
            case R.id.item_addObject:
                Intent intent1 = new Intent();
                intent1.setClass(configuration.this, add_object.class);
                startActivity(intent1);
                configuration.this.finish();
                return true;
            case R.id.item_editObject:
                return true;
            case R.id.item_removeObjects:
                return true;
            case R.id.item_logout:
                Intent intent2 = new Intent();
                intent2.setClass(configuration.this, Main.class);
                startActivity(intent2);
                configuration.this.finish();
                return true;
        }
        return false;
    }
}
