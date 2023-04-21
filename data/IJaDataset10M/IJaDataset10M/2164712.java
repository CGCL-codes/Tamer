package lpd2i.ApplicationAndroid;

import java.util.ArrayList;
import lpd2i.gestionMateriel.Batiment;
import lpd2i.gestionMateriel.Equipement;
import lpd2i.gestionMateriel.Espace;
import lpd2i.gestionMateriel.Organisation;
import lpd2i.gestionMateriel.Piece;
import lpd2i.outils.stockage.GestionConfiguation;
import lpd2i.ApplicationAndroid.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activit� lanc�e lorsque l'utilisaeur choisi de modifier equipement
 * contenu dans un espace
 * 
 * @author Maxime Riviere
 */
public class ModifierEquipement extends Activity implements OnItemSelectedListener {

    /**
	 * organisation que l'on est en train de g�rer
	 */
    private Organisation organisation;

    /**
	 * espace auquel on veut ajouter un equipement
	 */
    private Espace courant;

    /**
	 * designation de l'equipement a ajouter
	 */
    private EditText etiquette;

    /**
	 * boutons radio
	 */
    private RadioButton mRadioPresent;

    private RadioButton mRadioAbsent;

    /**
	 * liste des etats possibles a affecter a un equipement
	 */
    private Spinner etats;

    /**
	 * bouton de validation de l'ajout de l'equipement
	 */
    private Button btn_valider;

    /**
	 * bouton d'annulation de l'ajout de l'equipement
	 * et retour a la page de l'activit� mere
	 */
    private Button btn_annuler;

    /**
	 * liste des etats possibles d'un objet
	 */
    private ArrayList<String> lesEtats;

    /**
	 * equipement que l'on souhaite modifier
	 */
    private Equipement equipement;

    /**
	 * espace dans lequel se situe l'equipement
	 */
    private Piece espace;

    /**
	 * batiment auquel se situe l'etage
	 */
    public Batiment batiment;

    /**
	 * indice de l'element selectionn� dans la liste deroulante
	 */
    private int choisi;

    /**
	 * Differents �tats possibles d'un equipement
	 */
    public static final String[] ETAT_POSSIBLES = { "Rien � signaler", "DEGRADE", "HORS_SERVICE" };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifierequipement);
        recuperationWidgets();
        recuperationParametres();
    }

    private void recuperationWidgets() {
        etiquette = (EditText) findViewById(R.id.editDesignation);
        etats = (Spinner) findViewById(R.id.listEtats);
        mRadioPresent = (RadioButton) this.findViewById(R.id.radioPresent);
        mRadioAbsent = (RadioButton) this.findViewById(R.id.radioAbsent);
        setBtn_valider((Button) findViewById(R.id.btnAjout));
        setBtn_annuler((Button) findViewById(R.id.btnRetour));
        etats.setOnItemSelectedListener(this);
        lesEtats = new ArrayList<String>();
        for (String element : ETAT_POSSIBLES) {
            lesEtats.add(element);
        }
        ArrayAdapter<String> listeEtat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lesEtats);
        listeEtat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etats.setAdapter(listeEtat);
    }

    /**
	 * Recupere le batiment et l'espace dans lequel on souhaite ajouter un equipement
	 */
    private void recuperationParametres() {
        this.organisation = GestionConfiguation.recuperationConfiguration(this);
        Bundle recuperationParametres = this.getIntent().getExtras();
        int indiceBatiment = recuperationParametres.getInt("keyBatiment");
        this.batiment = (Batiment) this.organisation.get(indiceBatiment);
        int indiceEspace = recuperationParametres.getInt("keyEspace");
        this.espace = (Piece) this.batiment.getEspaceIdStatic(indiceEspace);
        int indiceEquipement = recuperationParametres.getInt("keyEquipement");
        this.equipement = this.espace.getEquipement(indiceEquipement);
        etiquette.setText(equipement.getDescription());
        if (equipement.isPresent()) {
            mRadioPresent.setChecked(true);
        } else {
            mRadioAbsent.setChecked(true);
        }
        if (equipement.getEtat() == Equipement.ETAT_POSSIBLES.DEGRADE) {
            etats.setSelection(1);
        } else if (equipement.getEtat() == Equipement.ETAT_POSSIBLES.HORS_SERVICE) {
            etats.setSelection(2);
        }
    }

    /**
	 * Appell� au clic sur le bouton de validation de la modification
	 * Valide la modification et ferme la fenetre
	 * @param lebouton
	 */
    public void onValidationModification(View lebouton) {
        String saisie = etiquette.getText().toString();
        equipement.setDescription(saisie);
        equipement.setPresence(mRadioPresent.isChecked());
        equipement.setEtat(choisi > 0 ? Equipement.ETAT_POSSIBLES.valueOf(ETAT_POSSIBLES[choisi]) : null);
        GestionConfiguation.sauvegardeConfiguration(this, this.organisation);
        String present = "oui";
        if (mRadioAbsent.isChecked()) {
            present = "non";
        }
        Toast t = Toast.makeText(this, "Element modifi� : \t" + saisie + "\npresent : " + present + "\netat : \t" + ETAT_POSSIBLES[choisi], Toast.LENGTH_LONG);
        t.show();
        this.finish();
    }

    /**
	 * Appell� au clic sur le bouton d'annulation de la modification
	 * Annule la modification et ferme la fenetre
	 * @param lebouton
	 */
    public void onAnnulerEquipement(View lebouton) {
        this.finish();
    }

    /**
	 * Appell� au clic sur le bouton supprimer
	 * Supprime l'element
	 * @param lebouton
	 */
    public void onSuppressionElement(View lebouton) {
        AlertDialog.Builder alertBuild = new AlertDialog.Builder(this);
        alertBuild.setMessage("Voulez vous vraiment supprimer?").setCancelable(false);
        alertBuild.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                suppression();
                dialog.cancel();
            }
        });
        alertBuild.setNegativeButton("NON", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                annuler();
                dialog.cancel();
            }
        });
        AlertDialog alert = alertBuild.create();
        alert.show();
    }

    /**
	 * supprime l'equipement et serialize la nouvelle configuration
	 */
    private void suppression() {
        System.out.println("suppression");
        espace.removeEquipement(equipement);
        GestionConfiguation.sauvegardeConfiguration(this, organisation);
        Toast t = Toast.makeText(this, "supprim�", Toast.LENGTH_SHORT);
        t.show();
        this.finish();
    }

    /**
	 * affiche un pop-up confirmant l'annulation et ferme l'activit�
	 */
    private void annuler() {
        Toast t = Toast.makeText(this, "annul�", Toast.LENGTH_SHORT);
        t.show();
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        this.choisi = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    /**
	 * @param courant the courant to set
	 */
    public void setCourant(Espace courant) {
        this.courant = courant;
    }

    /**
	 * @return the courant
	 */
    public Espace getCourant() {
        return courant;
    }

    /**
	 * @param btn_valider the btn_valider to set
	 */
    public void setBtn_valider(Button btn_valider) {
        this.btn_valider = btn_valider;
    }

    /**
	 * @return the btn_valider
	 */
    public Button getBtn_valider() {
        return btn_valider;
    }

    /**
	 * @param btn_annuler the btn_annuler to set
	 */
    public void setBtn_annuler(Button btn_annuler) {
        this.btn_annuler = btn_annuler;
    }

    /**
	 * @return the btn_annuler
	 */
    public Button getBtn_annuler() {
        return btn_annuler;
    }
}
