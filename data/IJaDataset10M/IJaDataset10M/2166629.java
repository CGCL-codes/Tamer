package tests.widgets;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class Widgets extends Activity implements OnCheckedChangeListener, OnItemSelectedListener, android.widget.CompoundButton.OnCheckedChangeListener {

    /**
	 * civilit� de la personne reunis dans des boutons radios
	 */
    RadioGroup civilites;

    /**
	 * champ de saisi du nom de la personne
	 */
    EditText nom;

    /**
	 * liste d�roulante des ann�es de naissance de la personne
	 */
    Spinner annees;

    Integer[] lesannees;

    /**
	 * cases a cocher contenant l'ensemble des cat�gories de personnes possibles
	 */
    CheckBox enseignant, etudiant, admin, autre;

    /**
	 * Bouton de validation des informations
	 */
    Button btn_ok;

    /**
	 * champ permettant d'afficher le message de bienvenue
	 */
    EditText message;

    /**
	 * permet d'identifier, apres selection de la civilit�
	 * le sexe de la personne de maniere a adapter le message
	 */
    Boolean homme;

    /**
	 * variables permettant de stocker les r�sultats saisis ou coch�s
	 */
    CharSequence lacivilite;

    Integer lannee;

    ArrayList<CharSequence> categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        civilites = (RadioGroup) findViewById(R.id.civilites);
        nom = (EditText) findViewById(R.id.nom);
        annees = (Spinner) findViewById(R.id.annee);
        enseignant = (CheckBox) findViewById(R.id.typeEns);
        etudiant = (CheckBox) findViewById(R.id.typeEt);
        admin = (CheckBox) findViewById(R.id.typeAd);
        autre = (CheckBox) findViewById(R.id.typeAu);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        message = (EditText) findViewById(R.id.message);
        civilites.setOnCheckedChangeListener(this);
        homme = true;
        annees.setOnItemSelectedListener(this);
        lesannees = new Integer[50];
        int an = 1950;
        for (int i = 0; i < lesannees.length; i++) {
            lesannees[i] = an;
            an++;
        }
        ArrayAdapter<Integer> aaaa = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lesannees);
        aaaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        annees.setAdapter(aaaa);
        categories = new ArrayList<CharSequence>();
        enseignant.setOnCheckedChangeListener(this);
        etudiant.setOnCheckedChangeListener(this);
        admin.setOnCheckedChangeListener(this);
        autre.setOnCheckedChangeListener(this);
    }

    public void onValidation(View lebouton) {
        CharSequence lenom = nom.getText();
        message.setText("Bienvenue " + lacivilite + " " + lenom + " " + (homme ? "n�" : "n�e") + " en " + lannee + (categories.size() == 0 ? "" : ", cat�gorie" + (categories.size() == 1 ? "" : "s") + " = " + categories.toString()));
    }

    /**
     * Permet d'ecouter un evenement sur un groupe de boutons radio
     * et de r�cup�rer le bouton choisi
     * @param group,		ensemble de boutons radio �cout�
     * @param checkedId
     */
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton choisi = (RadioButton) findViewById(checkedId);
        lacivilite = choisi.getText();
        String civ = lacivilite.toString();
        if (civ.compareTo("Mr") == 0) {
            homme = true;
            return;
        }
        homme = false;
    }

    /**
	 * Permet de r�cup�rer la ligne s�lectionn�e de la liste d�roulante
	 * ici on pourra r�cup�rer l'ann�e de naisssance qui a �t� choisi
	 */
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        lannee = lesannees[position];
    }

    /**
	 * Permet d'affecter une valeur par d�faut � la liste d�roulante
	 * si aucune ligne n'est selectionn�e
	 * Ici on affecte la derniere ligne de la liste
	 */
    public void onNothingSelected(AdapterView<?> parent) {
        lannee = lesannees[lesannees.length - 1];
    }

    /**
	 * Permet de savoir si une case a �t� coch�e
	 * on ajoutera a la liste l'intitul� de la case coch�e
	 */
    public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
        if (isChecked) {
            categories.add(btn.getText());
        }
    }
}
