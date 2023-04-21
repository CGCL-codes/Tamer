package br.ufmg.ubicomp.droidguide.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import br.ufmg.ubicomp.droidguide.R;
import br.ufmg.ubicomp.droidguide.communication.ProfileCommunicationService;
import br.ufmg.ubicomp.droidguide.profile.UserProfile;
import br.ufmg.ubicomp.droidguide.utils.AndroidUtils;

public class Profile extends DroidGuideScreenActivity {

    private static final char FEMALE = 'F';

    private static final char MALE = 'M';

    private static final char ADVENTUROUS = 'A';

    private static final char TRADITIONAL = 'T';

    /** Called when the activity is first created. */
    EditText txtNome, txtBirthYear;

    RadioGroup grpGenre, grpStyle;

    RadioButton rdnMale, rdnFemale, rdnTrad, rdnAdvent;

    Spinner spnEstCivil, spnConsumer, spnHistorical, spnEcological, spnGastronomic, spnCultural, spnBoemio;

    CheckBox chkComida, chkBebida, chkWeather, chkTraffic;

    Button btnOk;

    Intent intentPerfil;

    UserProfile profile;

    private static final String[] arrmarital_en = { "Single", "Married", "Divorced", "Widow" };

    private static final String[] arrmarital_br = { "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viuvo(a)" };

    private static final Byte[] arrperfil = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        initializeComponents();
    }

    public void initializeComponents() {
        intentPerfil = new Intent(this, Profile.class);
        txtNome = (EditText) findViewById(R.id.txtnome);
        txtBirthYear = (EditText) findViewById(R.id.txtidade);
        grpGenre = (RadioGroup) findViewById(R.id.grpsexo);
        rdnMale = (RadioButton) findViewById(R.id.radiomasc);
        rdnFemale = (RadioButton) findViewById(R.id.radiofem);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrmarital_en);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEstCivil = (Spinner) findViewById(R.id.spnestcivil);
        spnEstCivil.setAdapter(adapter);
        ArrayAdapter<Byte> scale;
        scale = new ArrayAdapter<Byte>(this, android.R.layout.simple_spinner_item, arrperfil);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConsumer = (Spinner) findViewById(R.id.spncons);
        spnConsumer.setAdapter(scale);
        spnHistorical = (Spinner) findViewById(R.id.spnhist);
        spnHistorical.setAdapter(scale);
        spnEcological = (Spinner) findViewById(R.id.spneco);
        spnEcological.setAdapter(scale);
        spnGastronomic = (Spinner) findViewById(R.id.spngastro);
        spnGastronomic.setAdapter(scale);
        spnCultural = (Spinner) findViewById(R.id.spncult);
        spnCultural.setAdapter(scale);
        spnBoemio = (Spinner) findViewById(R.id.spnboemio);
        spnBoemio.setAdapter(scale);
        chkComida = (CheckBox) findViewById(R.id.chkcomida);
        chkBebida = (CheckBox) findViewById(R.id.chkbebida);
        chkWeather = (CheckBox) findViewById(R.id.chkclima);
        chkTraffic = (CheckBox) findViewById(R.id.chktrafego);
        grpStyle = (RadioGroup) findViewById(R.id.grpestilo);
        rdnTrad = (RadioButton) findViewById(R.id.radiotrad);
        rdnAdvent = (RadioButton) findViewById(R.id.radioaven);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new clicker());
    }

    private void loadPreExistingProfileIfExists() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    profile = ProfileCommunicationService.getUserProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (profile != null) {
                    profile.setForUpdate(false);
                    showAlert("Loading pre-existing profile . . .");
                    txtNome.setText(profile.getName());
                    txtBirthYear = (EditText) findViewById(R.id.txtidade);
                    txtBirthYear.setText(String.valueOf(profile.getBirthyear()));
                    rdnMale.setChecked(profile.getGender() == MALE);
                    rdnFemale.setChecked(profile.getGender() == FEMALE);
                    int pos = getMaritalStatusPosition(profile.getMaritalstatus());
                    if (pos != -1) {
                        spnEstCivil.setSelection(pos);
                    }
                    setSelection(spnConsumer, profile.getConsumer());
                    setSelection(spnHistorical, profile.getHistorical());
                    setSelection(spnEcological, profile.getEcological());
                    setSelection(spnGastronomic, profile.getGastronomic());
                    setSelection(spnCultural, profile.getCultural());
                    setSelection(spnBoemio, profile.getBohemian());
                    chkBebida.setSelected(profile.getDrink());
                    chkComida.setSelected(profile.getFood());
                    chkWeather.setSelected(profile.getWeather());
                    chkTraffic.setSelected(profile.getTraffic());
                    rdnTrad.setSelected(profile.getStyle() == TRADITIONAL);
                    rdnAdvent.setSelected(profile.getStyle() == ADVENTUROUS);
                } else {
                    profile = new UserProfile();
                }
            }
        };
        AndroidUtils.runTask(t, 0);
    }

    private void setSelection(Spinner spinner, int value) {
        spinner.getAdapter().getItem(value);
        spinner.setSelection(value);
    }

    private int getMaritalStatusPosition(String value) {
        int pos = 0;
        for (int i = 0; i < arrmarital_en.length; i++) {
            if (arrmarital_en[i].equals(value)) {
                pos = i;
                return pos;
            }
        }
        for (int i = 0; i < arrmarital_br.length; i++) {
            if (arrmarital_br[i].equals(value)) {
                pos = i;
                return pos;
            }
        }
        return -1;
    }

    class clicker implements Button.OnClickListener {

        public void onClick(View v) {
            if (profile == null) {
                profile = new UserProfile();
            }
            profile.setForUpdate(true);
            profile.setName(txtNome.getText().toString());
            profile.setBirthyear(Integer.parseInt(txtBirthYear.getText().toString()));
            if (v == btnOk) {
                if (rdnMale.isChecked()) {
                    profile.setGender(MALE);
                }
                if (rdnFemale.isChecked()) {
                    profile.setGender(FEMALE);
                }
            }
            profile.setMaritalstatus((String) spnEstCivil.getSelectedItem());
            if (chkComida.isChecked()) {
                profile.setFood(true);
            }
            if (chkBebida.isChecked()) {
                profile.setDrink(true);
            }
            if (v == btnOk) {
                if (rdnTrad.isChecked()) {
                    profile.setStyle(TRADITIONAL);
                }
                if (rdnAdvent.isChecked()) {
                    profile.setStyle(ADVENTUROUS);
                }
            }
            profile.setConsumer((Byte) spnConsumer.getSelectedItem());
            profile.setHistorical((Byte) spnHistorical.getSelectedItem());
            profile.setEcological((Byte) spnEcological.getSelectedItem());
            profile.setGastronomic((Byte) spnGastronomic.getSelectedItem());
            profile.setCultural((Byte) spnCultural.getSelectedItem());
            profile.setBohemian((Byte) spnBoemio.getSelectedItem());
            if (chkWeather.isChecked()) {
                profile.setWeather(true);
            }
            if (chkTraffic.isChecked()) {
                profile.setTraffic(true);
            }
            boolean error = false;
            String msg = "";
            try {
                msg = ProfileCommunicationService.saveUserProfile(profile);
                error = (msg == null);
            } catch (Exception e) {
                msg = e.getMessage();
                error = true;
            }
            Toast.makeText(getBaseContext(), error ? "ERRO\n" + msg : "OK\n" + msg, Toast.LENGTH_SHORT).show();
            if (!error) {
                startMapActivity();
            } else {
                showErrorMessage(msg);
            }
        }
    }

    private void showErrorMessage(String msg) {
        AndroidUtils.notifyError(msg, this, "Profile");
    }

    private void startMapActivity() {
        finish();
    }
}
