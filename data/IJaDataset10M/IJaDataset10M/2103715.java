package com.sibyl.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ListActivity;
import android.database.sqlite.SQLiteDiskIOException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.sibyl.Music;
import com.sibyl.MusicDB;
import com.sibyl.R;

/**
 * Activité - Interface Utilisateur - permettant l'ajout de répertoires contenant des musiques
 * L'interface se présente comment une liste d'éléments
 * Elle se présente comme un navigateur dans l'arboressence d'android
 * 
 * @author Sibyl-project
 *
 */
public class AddDirUI extends ListActivity {

    private static final int ADD_ID = Menu.FIRST;

    private static final int BACK_ID = Menu.FIRST + 1;

    private static final String TAG = "ADD_DIR";

    private String parent;

    private String path;

    private MusicDB mdb;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTitle(R.string.add_dir_title);
        setContentView(R.layout.add_dir);
        path = Music.MUSIC_DIR;
        fillBD();
        try {
            mdb = new MusicDB(this);
        } catch (SQLiteDiskIOException ex) {
        }
    }

    @Override
    protected void onDestroy() {
        mdb.close();
        super.onDestroy();
    }

    /**
     * Méthode gérant les actions a effectuer en fonction de l'objet de la liste sélectionné
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = ((Map<String, String>) getListAdapter().getItem(position)).get(1);
        if (item == "..") {
            path = parent;
            fillBD();
        } else {
            if (!path.equals("/")) {
                item = path + "/" + item;
            } else {
                item = path + item;
            }
            Log.v(TAG, item);
            File f = new File(item);
            if (f.isDirectory()) {
                path = item;
                fillBD();
            } else {
                setSelection(0);
            }
        }
    }

    /**
     * Fill the table Song with mp3 found in path 
     * @param path chemin du répertoire a afficher dans le navigateur
     * @return liste iconnifiée contenant tous les élément a afficher
     */
    private void fillBD() {
        String[] colName = { "image", "file" };
        List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
        File dir = new File(path);
        String parent = dir.getParent();
        if (parent != null) {
            this.parent = parent;
            Map<String, String> curMap = new HashMap<String, String>();
            rows.add(curMap);
            curMap.put(colName[0], "" + R.drawable.folder);
            curMap.put(colName[1], "..");
        }
        Log.v(TAG, " " + path + " " + parent + " " + dir.getAbsoluteFile() + " " + dir.getAbsolutePath());
        File[] listeFile = dir.listFiles();
        if (listeFile != null) {
            for (File f : listeFile) {
                if (f.isDirectory()) {
                    Map<String, String> curMap = new HashMap<String, String>();
                    rows.add(curMap);
                    curMap.put(colName[0], "" + R.drawable.folder);
                    curMap.put(colName[1], f.getName());
                } else {
                    for (String s : Music.SUPPORTED_FILE_FORMAT) {
                        if (f.getName().endsWith(s)) {
                            Map<String, String> curMap = new HashMap<String, String>();
                            rows.add(curMap);
                            curMap.put(colName[0], "" + R.drawable.audio);
                            curMap.put(colName[1], f.getName());
                            break;
                        }
                    }
                }
            }
        }
        int[] to = { R.id.imgFile, R.id.file };
        SimpleAdapter adapter = new SimpleAdapter(this.getApplicationContext(), rows, R.layout.add_dir_row, colName, to);
        setListAdapter(adapter);
        ((TextView) findViewById(R.id.add_dir_location)).setText(getText(R.string.dir) + " " + dir.getAbsolutePath());
    }

    /**
     * Création du menu et ajout des différentes options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ADD_ID, Menu.NONE, R.string.menu_add);
        menu.add(0, BACK_ID, Menu.NONE, R.string.menu_back);
        return true;
    }

    /**
     * Appellé lorsqu'un élément du ménu est sélectionné.
     * Gère les actions mises sur les éléments du menu
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        switch(item.getItemId()) {
            case ADD_ID:
                mdb.insertDir(path);
                finish();
                break;
            case BACK_ID:
                finish();
                break;
        }
        return true;
    }
}
