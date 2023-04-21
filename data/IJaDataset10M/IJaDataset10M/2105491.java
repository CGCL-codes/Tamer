package br.uneb.tebd.local;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.uneb.tebd.R;
import br.uneb.tebd.model.Guitar;
import br.uneb.tebd.model.GuitarRepository;
import br.uneb.tebd.util.Constants;

public class UpdateLocalGuitar extends Activity {

    private Button update;

    private EditText id;

    private EditText description;

    private EditText model;

    private EditText brand;

    private GuitarRepository guitarRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.update_guitar);
        this.guitarRepository = new GuitarRepository(this);
        this.update = (Button) findViewById(R.id.registerButtonUpdate);
        this.id = (EditText) findViewById(R.id.idUpdate);
        this.description = (EditText) findViewById(R.id.descriptionUpdate);
        this.model = (EditText) findViewById(R.id.modelUpdate);
        this.brand = (EditText) findViewById(R.id.brandUpdate);
        this.update.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (haveAnyBlankField() == false) {
                    Guitar guitar = new Guitar();
                    guitar.setId(Integer.parseInt(id.getText().toString().trim()));
                    guitar.setModel(model.getText().toString().trim());
                    guitar.setDescription(description.getText().toString().trim());
                    guitar.setBrand(brand.getText().toString().trim());
                    Guitar guitarFromDb = guitarRepository.findGuitarById(guitar.getId());
                    if (guitarFromDb != null) {
                        guitarRepository.update(guitar);
                        Toast.makeText(UpdateLocalGuitar.this, Constants.MSG_GUITAR_UPDATED, Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(UpdateLocalGuitar.this, Constants.MSG_GUITAR_NOT_FOUND, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void clearFields() {
        this.id.setText("");
        this.description.setText("");
        this.model.setText("");
        this.brand.setText("");
    }

    private boolean haveAnyBlankField() {
        boolean result = false;
        String id = this.id.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(this, "E preciso preencher todos os campos.", Toast.LENGTH_SHORT).show();
            result = true;
        }
        String description = this.description.getText().toString().trim();
        if (description.equals("")) {
            Toast.makeText(this, "E preciso preencher todos os campos.", Toast.LENGTH_SHORT).show();
            result = true;
        }
        String model = this.model.getText().toString().trim();
        if (model.equals("")) {
            Toast.makeText(this, "E preciso preencher todos os campos.", Toast.LENGTH_SHORT).show();
            result = true;
        }
        String brand = this.brand.getText().toString().trim();
        if (brand.equals("")) {
            Toast.makeText(this, "E preciso preencher todos os campos.", Toast.LENGTH_SHORT).show();
            result = true;
        }
        return result;
    }
}
