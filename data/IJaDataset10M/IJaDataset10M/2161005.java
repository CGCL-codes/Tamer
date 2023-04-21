package be.kuleuven.peno3.mobiletoledo.view.queue;

import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import be.kuleuven.peno3.mobiletoledo.R;
import be.kuleuven.peno3.mobiletoledo.Data.Client.QueueClient;
import be.kuleuven.peno3.mobiletoledo.model.QueueInfo;
import be.kuleuven.peno3.mobiletoledo.model.User;

public class InsertQueueLength extends Activity {

    private Spinner servicespinner;

    private EditText queuelengthtxt;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.insertqueuelength);
        TextView bar = (TextView) findViewById(R.id.queue_length_bar);
        bar.setText("Wachtrij lengte");
        queuelengthtxt = (EditText) findViewById(R.id.queuelengthedittext);
        servicespinner = (Spinner) findViewById(R.id.servicesspinner);
        Button b = (Button) findViewById(R.id.insertqueuelengthbutton);
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String service = (String) InsertQueueLength.this.servicespinner.getSelectedItem();
                int length;
                try {
                    length = Integer.parseInt(InsertQueueLength.this.queuelengthtxt.getText().toString());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    Toast.makeText(InsertQueueLength.this, "Gelieve een getal in te vullen", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (length > 100 || length < 0) {
                    Toast.makeText(InsertQueueLength.this, "Gelieve een realistische waarde in te vullen", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (User.getInstance().getUsername() != null && User.getInstance().getUsername().length() != 0) {
                    if (QueueClient.addLength(service, lengthAvg(length, service), Calendar.getInstance(), User.getInstance().getIdNumber())) {
                        Toast.makeText(InsertQueueLength.this, "Bedankt voor uw bijdrage", Toast.LENGTH_SHORT).show();
                        InsertQueueLength.this.finish();
                        return;
                    } else {
                        Toast.makeText(InsertQueueLength.this, "Er is geen internetverbinding", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InsertQueueLength.this, "Je hebt geen gebruikersnaam ingegeven.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicespinner.setAdapter(adapter);
    }

    private int lengthAvg(int length, String location) {
        final int weight = 2;
        QueueInfo[] qi = QueueClient.getLengthHistory(location);
        Calendar now = Calendar.getInstance();
        if (qi[0].getCal().get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)) {
            int nowTime = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
            int previousTime = qi[0].getCal().get(Calendar.HOUR_OF_DAY) * 60 + qi[0].getCal().get(Calendar.MINUTE);
            int timeDiff = Math.abs(3 + nowTime - previousTime);
            double calc = (length - ((length - qi[0].getLength()) * Math.pow((1 / Math.sqrt(weight)), timeDiff)));
            length = (int) calc;
        }
        return length;
    }
}
