package huntingmole.game;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Instructions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions_view);
        TextView instructionTitle = (TextView) findViewById(R.id.instruction_title);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/LITHOSPRO-BLACK.OTF");
        instructionTitle.setTypeface(font);
    }
}
