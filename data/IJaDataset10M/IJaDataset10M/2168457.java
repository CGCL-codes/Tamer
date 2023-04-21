package hoge.memoriesalbum;

import hoge.memoriesalbum.get.GetActivity;
import hoge.memoriesalbum.post.PostActivity;
import hoge.memoriesalbum.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MemoriesAlbumActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button postBtn = (Button) this.findViewById(R.id.btn_post);
        postBtn.setOnClickListener(this);
        Button getBtn = (Button) this.findViewById(R.id.btn_get);
        getBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_post == v.getId()) {
            Intent i = new Intent(MemoriesAlbumActivity.this, PostActivity.class);
            i.setAction(Intent.ACTION_VIEW);
            startActivity(i);
        } else if (R.id.btn_get == v.getId()) {
            Intent i = new Intent(MemoriesAlbumActivity.this, GetActivity.class);
            i.setAction(Intent.ACTION_VIEW);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Settings.class));
                return true;
        }
        return false;
    }
}
