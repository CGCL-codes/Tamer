package egy.rev;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class imagesView extends Activity {

    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageslay2);
        id = getIntent().getExtras().getInt("ID");
        ImageView immv = (ImageView) findViewById(R.id.imV);
        immv.setImageResource(ImageAdapter.mThumbIds[id]);
        Button prevButton = (Button) findViewById(R.id.prevBut);
        prevButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (id == 0) {
                    Button b = (Button) v;
                    b.setEnabled(false);
                } else {
                    if (id == ImageAdapter.mThumbIds.length - 1) {
                        Button b = (Button) findViewById(R.id.nextBut);
                        b.setEnabled(true);
                    }
                    id--;
                    ImageView immv = (ImageView) findViewById(R.id.imV);
                    immv.setImageResource(ImageAdapter.mThumbIds[id]);
                }
            }
        });
        Button nextButton = (Button) findViewById(R.id.nextBut);
        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (id == ImageAdapter.mThumbIds.length - 1) {
                    Button b = (Button) v;
                    b.setEnabled(false);
                } else {
                    if (id == 0) {
                        Button b = (Button) findViewById(R.id.prevBut);
                        b.setEnabled(true);
                    }
                    id++;
                    ImageView immv = (ImageView) findViewById(R.id.imV);
                    immv.setImageResource(ImageAdapter.mThumbIds[id]);
                }
            }
        });
    }
}
