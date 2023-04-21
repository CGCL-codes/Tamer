package com.gpfcomics.android.cryptnos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * The Edit Existing / Generate New Password activity.  This activity presents
 * the core user interface for generating passwords from a series of 
 * parameters.  When the incoming Intent includes a valid SiteParameters 
 * object, this view goes into Edit mode, allowing the user to modify those
 * settings and save them to the database.  If there are no SiteParameters
 * attached, we enter New mode, where a set of sane defaults allow us create
 * an all new set of parameters.  When the Generate button is tapped, the
 * parameters are saved to the database and the generated password is
 * displayed to the user.
 * @author Jeffrey T. Darlington
 * @version 1.3.1
 * @since 1.0
 */
public class EditParametersActivity extends Activity {

    /** A constant specifying that this activity is being called in new
	 *  (generate) mode. */
    public static final int MODE_NEW = 0;

    /** A constant specifying that this activity is being called in edit
	 *  mode. */
    public static final int MODE_EDIT = 1;

    /** A constant indicating the Help option menu item. */
    public static final int OPTMENU_HELP = Menu.FIRST;

    /** The current mode of this activity. */
    private int mode = MODE_NEW;

    /** The EditText view containing the site token. */
    private EditText txtSite = null;

    /** The EditText view containing the user's passphrase. */
    private EditText txtPassphrase = null;

    /** The Spinner view that allows the user to pick a cryptographic hash. */
    private Spinner hashSpinner = null;

    /** The EditText view containing the number of iterations of the has to
	 *  perform. */
    private EditText txtIterations = null;

    /** The Spinner view that lets the user choose what types of characters
	 *  to include or exclude from the final password. */
    private Spinner charTypesSpinner = null;

    /** The Spinner containing length restriction value. */
    private Spinner spinCharLimit = null;

    /** The EditText view that will eventually contain the final password. */
    private EditText txtOutput = null;

    /** The Button that actually generates the password and saves it to
	 *  the database. */
    private Button btnGenerate = null;

    /** A reference to our top-level application */
    private CryptnosApplication theApp = null;

    /** Our database adapter for manipulating the database. */
    private ParamsDbAdapter dbHelper = null;

    /** The database row ID of the current note if we are in edit mode. */
    private long rowID = ParamsDbAdapter.DB_ERROR;

    /** The site token used the last time the Generate button was pressed
	 *  in this session.  This is used to detect if the parameters have been
	 *  changed between button presses, to determine if we should save a
	 *  given set of parameters as a new item or to update an existing one. */
    private String lastSite = null;

    /** This flag determines whether or not we should clear the master and
	 *  generated password boxes when the activity is displayed.  There are
	 *  several reasons why this may or may not happen, depending on the user's
	 *  preferences and whether we're handling a configuration change like
	 *  rotating the screen.  This is the "final" flag that determines whether
	 *  or not the clearing takes place and this will be set to true or false
	 *  based on all these factors.  By default, this will be false. */
    private boolean clearPasswords = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        theApp = (CryptnosApplication) getApplication();
        dbHelper = theApp.getDBHelper();
        txtSite = (EditText) findViewById(R.id.txtSite);
        txtPassphrase = (EditText) findViewById(R.id.txtPassphrase);
        hashSpinner = (Spinner) findViewById(R.id.spinHashes);
        txtIterations = (EditText) findViewById(R.id.txtIterations);
        charTypesSpinner = (Spinner) findViewById(R.id.spinCharTypes);
        spinCharLimit = (Spinner) findViewById(R.id.spinCharLimit);
        txtOutput = (EditText) findViewById(R.id.txtOutput);
        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        if (theApp.showMasterPasswords()) txtPassphrase.setTransformationMethod(null);
        clearPasswords = theApp.clearPasswordsOnFocusLoss();
        hashSpinner.setPromptId(R.string.edit_hash_prompt);
        charTypesSpinner.setPromptId(R.string.edit_chartypes_prompt);
        spinCharLimit.setPromptId(R.string.edit_charlimit_prompt);
        final ParameterViewState state = (ParameterViewState) getLastNonConfigurationInstance();
        if (state != null) {
            mode = state.getMode();
            lastSite = state.getLastSite();
            rowID = state.getRowID();
            txtSite.setText(state.getSite());
            hashSpinner.setSelection(getHashPosition(state.getHash()));
            txtIterations.setText(state.getIterations());
            charTypesSpinner.setSelection(state.getCharTypes());
            rebuildCharLimitSpinner(state.getHash());
            spinCharLimit.setSelection(state.getCharLimit(), true);
            txtPassphrase.setText(state.getMasterPassword());
            txtOutput.setText(state.getGeneratedPassword());
            clearPasswords = false;
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mode = extras.getInt("mode");
                if (mode < MODE_NEW || mode > MODE_EDIT) mode = MODE_NEW;
                if (mode == MODE_EDIT) {
                    try {
                        String site = extras.getString(ParamsDbAdapter.DBFIELD_SITE);
                        if (site != null) {
                            Cursor c = dbHelper.fetchRecord(site);
                            if (c.getCount() == 1) {
                                rowID = c.getLong(0);
                                SiteParameters params = new SiteParameters(theApp, c.getString(1), c.getString(2));
                                txtSite.setText(params.getSite());
                                txtSite.setEnabled(false);
                                hashSpinner.setSelection(getHashPosition(params.getHash()));
                                txtIterations.setText(Integer.toString(params.getIterations()));
                                charTypesSpinner.setSelection(params.getCharTypes());
                                rebuildCharLimitSpinner(params.getHash());
                                if (params.getCharLimit() < 0 || params.getCharLimit() > theApp.getEncodedHashLength(params.getHash())) spinCharLimit.setSelection(0, true); else spinCharLimit.setSelection(params.getCharLimit(), true);
                                lastSite = params.getSite();
                            } else {
                                Toast.makeText(this, R.string.error_bad_restore, Toast.LENGTH_LONG).show();
                                mode = MODE_NEW;
                            }
                            c.close();
                        } else {
                            Toast.makeText(this, R.string.error_bad_restore, Toast.LENGTH_LONG).show();
                            mode = MODE_NEW;
                        }
                    } catch (Exception e1) {
                        Toast.makeText(this, R.string.error_bad_restore, Toast.LENGTH_LONG).show();
                        mode = MODE_NEW;
                    }
                }
            }
            if (mode == MODE_NEW) {
                hashSpinner.setSelection(1);
                txtIterations.setText("1");
                rebuildCharLimitSpinner("SHA-1");
                spinCharLimit.setSelection(0, true);
            }
        }
        switch(mode) {
            case MODE_EDIT:
                setTitle(R.string.edit_title);
                txtSite.setEnabled(false);
                break;
            default:
                setTitle(R.string.new_title);
        }
        btnGenerate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String site = txtSite.getText().toString();
                String passphrase = txtPassphrase.getText().toString();
                String hash = (String) hashSpinner.getSelectedItem();
                int charType = charTypesSpinner.getSelectedItemPosition();
                int charLimit = spinCharLimit.getSelectedItemPosition();
                int iterations = 1;
                if (mode == MODE_NEW && (lastSite == null || site.compareTo(lastSite) != 0)) rowID = ParamsDbAdapter.DB_ERROR;
                if (site == null || site.length() == 0) Toast.makeText(v.getContext(), R.string.error_edit_bad_site, Toast.LENGTH_LONG).show(); else if (site.contains("|")) Toast.makeText(v.getContext(), R.string.error_site_has_pipe, Toast.LENGTH_LONG).show(); else if (passphrase == null || passphrase.length() == 0) Toast.makeText(v.getContext(), R.string.error_edit_bad_password, Toast.LENGTH_LONG).show(); else {
                    String messages = new String();
                    try {
                        iterations = Integer.parseInt(txtIterations.getText().toString());
                        if (charLimit >= 0 && iterations > 0 && iterations <= CryptnosApplication.HASH_ITERATION_WARNING_LIMIT) {
                            SiteParameters params = new SiteParameters(theApp, site, charType, charLimit, hash, iterations);
                            String password = params.generatePassword(passphrase);
                            txtOutput.setText(password);
                            if (theApp.copyPasswordsToClipboard()) {
                                ClipboardManager clippy = ClipboardManager.newInstance(theApp);
                                clippy.setText(password);
                                messages = getResources().getString(R.string.edit_gen_success);
                            } else messages = getResources().getString(R.string.edit_gen_success_no_copy);
                            try {
                                boolean success = true;
                                if (rowID != ParamsDbAdapter.DB_ERROR) dbHelper.updateRecord(rowID, params); else {
                                    rowID = dbHelper.createRecord(params);
                                    theApp.setSiteListDirty();
                                }
                                if (success && rowID != ParamsDbAdapter.DB_ERROR) {
                                    messages = messages.concat(" ").concat(getResources().getString(R.string.edit_save_success));
                                    lastSite = site;
                                } else messages = messages.concat(" ").concat(getResources().getString(R.string.error_edit_params_not_saved));
                            } catch (Exception e1) {
                                messages = messages.concat(" " + e1.getMessage());
                            }
                            Toast.makeText(v.getContext(), messages, Toast.LENGTH_LONG).show();
                        } else {
                            if (iterations <= 0) Toast.makeText(v.getContext(), R.string.error_bad_iterations, Toast.LENGTH_LONG).show(); else if (iterations > CryptnosApplication.HASH_ITERATION_WARNING_LIMIT) Toast.makeText(v.getContext(), R.string.error_excessive_hashing, Toast.LENGTH_LONG).show(); else if (charLimit < 0) Toast.makeText(v.getContext(), R.string.error_bad_charlimit, Toast.LENGTH_LONG).show(); else Toast.makeText(v.getContext(), R.string.error_unknown, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e2) {
                        Toast.makeText(v.getContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        txtIterations.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        String itersString = txtIterations.getText().toString();
                        if (itersString != null && itersString.length() > 0) {
                            int iterations = Integer.parseInt(itersString);
                            if (iterations <= 0) {
                                Toast.makeText(v.getContext(), R.string.error_bad_iterations, Toast.LENGTH_LONG).show();
                                txtIterations.setText(String.valueOf(1));
                            } else if (iterations >= CryptnosApplication.HASH_ITERATION_WARNING_LIMIT) {
                                Toast.makeText(v.getContext(), R.string.error_excessive_hashing, Toast.LENGTH_LONG).show();
                                txtIterations.setText(String.valueOf(CryptnosApplication.HASH_ITERATION_WARNING_LIMIT));
                            }
                        } else {
                            Toast.makeText(v.getContext(), R.string.error_bad_iterations, Toast.LENGTH_LONG).show();
                            txtIterations.setText(String.valueOf(1));
                        }
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), R.string.error_bad_iterations, Toast.LENGTH_LONG).show();
                        txtIterations.setText(String.valueOf(1));
                    }
                }
            }
        });
        txtSite.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        String site = txtSite.getText().toString();
                        if (site == null || site.length() == 0) {
                            Toast.makeText(v.getContext(), R.string.error_site_empty, Toast.LENGTH_LONG).show();
                        } else if (site.contains("|")) {
                            Toast.makeText(v.getContext(), R.string.error_site_has_pipe, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
        hashSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String hash = (String) hashSpinner.getSelectedItem();
                rebuildCharLimitSpinner(hash);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        txtPassphrase.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    btnGenerate.performClick();
                    return true;
                } else return false;
            }
        });
    }

    @Override
    public void onResume() {
        if (clearPasswords) {
            txtPassphrase.setText("");
            txtOutput.setText("");
        }
        clearPasswords = theApp.clearPasswordsOnFocusLoss();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OPTMENU_HELP, Menu.NONE, R.string.optmenu_help).setIcon(android.R.drawable.ic_menu_help);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case OPTMENU_HELP:
                Intent i = new Intent(this, HelpActivity.class);
                if (mode == MODE_NEW) i.putExtra("helptext", R.string.help_text_start); else i.putExtra("helptext", R.string.help_text_existing);
                startActivity(i);
                return true;
        }
        return false;
    }

    public Object onRetainNonConfigurationInstance() {
        final ParameterViewState state = new ParameterViewState(txtSite.getText().toString(), txtPassphrase.getText().toString(), (String) hashSpinner.getSelectedItem(), txtIterations.getText().toString(), charTypesSpinner.getSelectedItemPosition(), spinCharLimit.getSelectedItemPosition(), txtOutput.getText().toString(), mode, lastSite, rowID);
        return state;
    }

    /**
     * Return the position of the specified hash name string in the hash
     * list array.
     * @param hash The hash name to look for.
     * @return The position of the hash in the array, or the default if
     * the hash could not be found.
     */
    private int getHashPosition(String hash) {
        String[] hashes = getResources().getStringArray(R.array.hashList);
        for (int i = 0; i < hashes.length; i++) if (hash.compareTo(hashes[i]) == 0) return i;
        return 1;
    }

    /**
     * Rebuild the items in the character limit spinner based on the hash algorithm
     * name specified
     * @param hash The name of the new hash algorithm
     */
    private void rebuildCharLimitSpinner(String hash) {
        int currentPosition = spinCharLimit.getSelectedItemPosition();
        int hashLength = theApp.getEncodedHashLength(hash);
        if (hashLength != -1) {
            String[] charLimits = new String[hashLength + 1];
            charLimits[0] = getResources().getString(R.string.edit_charlimit_none);
            for (int i = 1; i <= hashLength; i++) charLimits[i] = String.valueOf(i);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, charLimits);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinCharLimit.setAdapter(adapter);
            if (currentPosition < 0 || currentPosition > hashLength) spinCharLimit.setSelection(0, true); else spinCharLimit.setSelection(currentPosition, true);
        } else Toast.makeText(getBaseContext(), "ERROR: Invalid hash algorithm name", Toast.LENGTH_LONG).show();
    }
}
