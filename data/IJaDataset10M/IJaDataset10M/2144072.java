package de.janbusch.jhashpassword.impexp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.janbusch.hashpassword.core.CoreInformation;
import de.janbusch.jhashpassword.R;

public class HPImpExp extends Activity {

    private static final int REQUESTCODE_IMPORT = 0;

    private EditText etImport;

    private Button btnImport;

    private Button btnExport;

    private Button btnChooseFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imp_exp);
        etImport = (EditText) findViewById(R.id.etImport);
        btnImport = (Button) findViewById(R.id.btnImport);
        btnExport = (Button) findViewById(R.id.btnExport);
        btnChooseFile = (Button) findViewById(R.id.btnChooseFileImport);
        if (Environment.getExternalStorageState().matches(Environment.MEDIA_MOUNTED)) {
            btnExport.setEnabled(true);
            btnChooseFile.setEnabled(true);
        } else {
            btnExport.setEnabled(false);
            btnChooseFile.setEnabled(false);
            Toast.makeText(getBaseContext(), getString(R.string.msgNotMounted), Toast.LENGTH_LONG).show();
        }
    }

    /**
	 * This method specifies what to do if a button was clicked.
	 * 
	 * @param btn
	 *            Button that has been clicked as {@link View}.
	 * @return True if the button is known otherwise false.
	 */
    public boolean onButtonClicked(View btn) {
        Intent choosFileIntent;
        Builder inputDialog;
        switch(btn.getId()) {
            case R.id.btnChooseFileImport:
                choosFileIntent = new Intent(getBaseContext(), HPChooseFile.class);
                startActivityForResult(choosFileIntent, REQUESTCODE_IMPORT);
                return true;
            case R.id.btnImport:
                inputDialog = new AlertDialog.Builder(this);
                inputDialog.setTitle(R.string.titleImport);
                inputDialog.setMessage(R.string.msgImport);
                inputDialog.setPositiveButton(getString(R.string.Yes), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File newFile = new File(etImport.getText().toString());
                        importFile(newFile);
                        Log.d(this.toString(), "Changes saved!");
                        setResult(Activity.RESULT_OK, new Intent());
                        Toast.makeText(getBaseContext(), getString(R.string.msgFileImported), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                inputDialog.setNegativeButton(getString(R.string.No), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(this.toString(), "Changes canceled!");
                        setResult(Activity.RESULT_CANCELED, new Intent());
                        finish();
                    }
                });
                inputDialog.show();
                return true;
            case R.id.btnExport:
                if (backupFile()) {
                    AlertDialog.Builder infoDialog = new AlertDialog.Builder(this);
                    infoDialog.setTitle(R.string.titleExport);
                    infoDialog.setMessage(R.string.msgExportSuccess);
                    infoDialog.setPositiveButton(getString(R.string.OK), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(this.toString(), "Changes saved!");
                            setResult(Activity.RESULT_OK, new Intent());
                            finish();
                        }
                    });
                    infoDialog.show();
                }
                return true;
            case R.id.btnDelete:
                inputDialog = new AlertDialog.Builder(this);
                inputDialog.setTitle(R.string.titleXMLFile);
                inputDialog.setMessage(R.string.msgDelete);
                inputDialog.setPositiveButton(getString(R.string.Yes), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getBaseContext().deleteFile(CoreInformation.HASH_PASSWORD_XML);
                        Log.d(this.toString(), "Changes saved!");
                        setResult(Activity.RESULT_OK, new Intent());
                        finish();
                    }
                });
                inputDialog.setNegativeButton(getString(R.string.No), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(this.toString(), "Changes canceled!");
                        setResult(Activity.RESULT_CANCELED, new Intent());
                        finish();
                    }
                });
                inputDialog.show();
                return true;
            default:
                Log.d(this.toString(), "Clicked button has no case.");
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUESTCODE_IMPORT:
                switch(resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(this.toString(), "Result okay, reloading!");
                        String path = data.getStringExtra(getString(R.string.selectedFile));
                        etImport.setText(path);
                        btnImport.setEnabled(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        if (etImport.getText() == null || etImport.getText().toString().length() == 0) {
                            btnImport.setEnabled(true);
                        }
                        Log.d(this.toString(), "Result canceled!");
                        break;
                    default:
                        break;
                }
                break;
            default:
                Log.d(toString(), "Unknown request code: " + requestCode);
                break;
        }
    }

    private void importFile(File from) {
        FileInputStream fInStream = null;
        FileOutputStream fOutStream = null;
        try {
            fInStream = new FileInputStream(from);
            fOutStream = getBaseContext().openFileOutput(CoreInformation.HASH_PASSWORD_XML, Context.MODE_PRIVATE);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fInStream.read(buf)) != -1) {
                fOutStream.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fInStream != null) fInStream.close();
                if (fOutStream != null) fOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean backupFile() {
        FileInputStream fInStream = null;
        FileOutputStream fOutStream = null;
        String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "_" + CoreInformation.HASH_PASSWORD_XML;
        File outputPath = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.ext_storage_dir) + "/");
        File outputFile = new File(outputPath, fileName);
        if (!outputPath.exists()) {
            if (!outputPath.mkdirs()) {
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(this);
                infoDialog.setTitle(getString(R.string.titleExport) + ": " + outputPath.getPath());
                infoDialog.setMessage(R.string.msgCantWrite);
                infoDialog.setPositiveButton(getString(R.string.OK), null);
                infoDialog.show();
                return false;
            }
        }
        try {
            fInStream = getBaseContext().openFileInput(CoreInformation.HASH_PASSWORD_XML);
            outputFile.createNewFile();
            fOutStream = new FileOutputStream(outputFile);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fInStream.read(buf)) != -1) {
                fOutStream.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder infoDialog = new AlertDialog.Builder(this);
            infoDialog.setTitle(getString(R.string.titleExport) + " to " + outputFile.getPath());
            infoDialog.setMessage(R.string.msgCantWrite);
            infoDialog.setPositiveButton(getString(R.string.OK), null);
            infoDialog.show();
            return false;
        } finally {
            try {
                if (fInStream != null) fInStream.close();
                if (fOutStream != null) fOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
