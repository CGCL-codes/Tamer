package br.uneb.tebd.local;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.uneb.tebd.R;
import br.uneb.tebd.model.GuitarRepository;
import br.uneb.tebd.util.Constants;

public class DeleteLocalGuitarByDescription extends Activity {

    private GuitarRepository guitarRepository;

    private Button deleteButton;

    private TextView description;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.find_guitar);
        this.guitarRepository = new GuitarRepository(this);
        this.deleteButton = (Button) findViewById(R.id.searchButton);
        this.deleteButton.setText("Delete");
        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setVisibility(View.INVISIBLE);
        this.description = (TextView) findViewById(R.id.descriptionSearch);
        this.deleteButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (guitarRepository.deleteGuitar(description.getText().toString()) == 1) {
                    Toast.makeText(DeleteLocalGuitarByDescription.this, Constants.MSG_GUITAR_REMOVED, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeleteLocalGuitarByDescription.this, Constants.MSG_GUITAR_NOT_FOUND, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
