package edu.calpoly.csc.plantidentification;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(this, QuestionDownloadService.class));
        initListeners();
    }

    protected void initListeners() {
        ((Button) this.findViewById(R.id.btnAddIdentification)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IdentificationActivity.class);
                startActivity(intent);
            }
        });
        ((Button) this.findViewById(R.id.btnViewIdentifications)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListSightingsActivity.class);
                startActivity(intent);
            }
        });
        ((Button) this.findViewById(R.id.btnViewMap)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlantMapActivity.class);
                startActivity(intent);
            }
        });
        ((Button) this.findViewById(R.id.btnBrowsePlants)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListPlantsActivity.class);
                startActivity(intent);
            }
        });
        ((Button) this.findViewById(R.id.btnQuickIdentification)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                SearchManager.startNewSearch(MainActivity.this, true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchManager.QUICK_IDENTIFICATION && resultCode == RESULT_OK) {
            if (data.getBooleanExtra(SearchManager.RESTART_SEARCH, false)) {
                SearchManager.restartSearch(this, data);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuOptions) {
            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menuHelp) {
            createHelp();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createHelp() {
        Map<Integer, Integer> helpItems = new HashMap<Integer, Integer>();
        Map<Integer, Integer> helpMenuItems = new HashMap<Integer, Integer>();
        helpItems.put(R.string.help_main_welcome, R.string.help_main_welcome_content);
        helpItems.put(R.string.add_id, R.string.help_add_id_content);
        helpItems.put(R.string.view_ids, R.string.help_view_ids_content);
        helpItems.put(R.string.view_map, R.string.help_view_map_content);
        helpItems.put(R.string.browse_plants, R.string.help_browse_plants_content);
        helpItems.put(R.string.btnQuickIdentification, R.string.help_btnQuickIdentification_content);
        helpMenuItems.put(R.string.btnOptions, R.string.help_options_content);
        HelpDialog hd = new HelpDialog(this, helpItems, helpMenuItems);
        hd.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
}
