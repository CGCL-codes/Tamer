package fr.esstin;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class EnveloppeActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enveloppe);
        OnClickListener ButtonAccueil = new OnClickListener() {

            @Override
            public void onClick(View actuelView) {
                Intent intent = new Intent(EnveloppeActivity.this, PrixAffranchissementActivity.class);
                startActivity(intent);
            }
        };
        Button button1 = (Button) findViewById(R.id.Accueil);
        button1.setOnClickListener(ButtonAccueil);
        OnClickListener ButtonSuivant = new OnClickListener() {

            @Override
            public void onClick(View actuelView) {
                Spinner Simple = (Spinner) findViewById(R.id.SpinnerSimple);
                Spinner Mat = (Spinner) findViewById(R.id.SpinnerMat);
                Spinner Max = (Spinner) findViewById(R.id.SpinnerMax);
                Double poids;
                String taille;
                if (Simple.getSelectedItemId() != 0 && Mat.getSelectedItemId() == 0 && Max.getSelectedItemId() == 0) {
                    taille = Simple.getSelectedItem().toString().replace("mm", "");
                    String[] ListeTaille = taille.split("x");
                    poids = (Double.parseDouble(ListeTaille[0])) * (Double.parseDouble(ListeTaille[1]));
                    poids = poids * 0.00007 * 2;
                    Intent intent = new Intent(EnveloppeActivity.this, ContenanceActivity.class);
                    intent.putExtra("poids", (double) ((int) (poids * 100)) / 100);
                    startActivity(intent);
                } else if (Simple.getSelectedItemId() == 0 && Mat.getSelectedItemId() != 0 && Max.getSelectedItemId() == 0) {
                    taille = Mat.getSelectedItem().toString().replace("mm", "");
                    String[] ListeTaille = taille.split("x");
                    poids = (Double.parseDouble(ListeTaille[0])) * (Double.parseDouble(ListeTaille[1]));
                    poids = poids * 0.000384;
                    Intent intent = new Intent(EnveloppeActivity.this, ContenanceActivity.class);
                    intent.putExtra("poids", (double) ((int) (poids * 100)) / 100);
                    startActivity(intent);
                } else if (Simple.getSelectedItemId() == 0 && Mat.getSelectedItemId() == 0 && Max.getSelectedItemId() != 0) {
                    poids = 0.0;
                    ArrayList<Integer> ListePrix = new ArrayList<Integer>();
                    ListePrix.add(5);
                    ListePrix.add(8);
                    ListePrix.add(11);
                    ListePrix.add(13);
                    ListePrix.add(16);
                    ListePrix.add(23);
                    Double prix = Double.parseDouble(Integer.toString(ListePrix.get(Max.getSelectedItemPosition() - 1)));
                    Intent intent2 = new Intent(EnveloppeActivity.this, CalculActivity.class);
                    intent2.putExtra("price", prix);
                    startActivity(intent2);
                } else if (Simple.getSelectedItemId() == 0 && Mat.getSelectedItemId() == 0 && Max.getSelectedItemId() == 0) {
                    Context context = getApplicationContext();
                    CharSequence text = "Selectionnez une enveloppe ! ";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Ne selectionnez qu'une enveloppe ! ";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        };
        Button button2 = (Button) findViewById(R.id.verscontenance);
        button2.setOnClickListener(ButtonSuivant);
    }
}
