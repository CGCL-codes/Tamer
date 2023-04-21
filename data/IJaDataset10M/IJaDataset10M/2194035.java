package gr.allamanis.randgen.backend;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/** 
 *  A RandomGenerator that produces float's in a uniform distribution
 */
public class UniformFloatGenerator extends RandomGenerator {

    /** 
   *  the smallest number of the uniform distribution
   */
    public double a = 0;

    /** 
   *  the biggest value of the uniform distribution
   */
    public double b = 0;

    private Dialog dialog;

    @Override
    public String getDescription() {
        return "Produces random decimal numbers that have the same (uniform) probability in a specified interval";
    }

    @Override
    public String getName() {
        return "Uniform Real";
    }

    @Override
    public String getNext() {
        if (a == 0 && b == 0) return "Parameters not set";
        return Double.toString(a + (b - a) * this.generator.nextDouble());
    }

    @Override
    public void setParameters(Activity myActivity) {
        dialog = new Dialog(myActivity);
        dialog.setTitle(gr.allamanis.randgen.R.string.unifFloatParam);
        dialog.setContentView(gr.allamanis.randgen.R.layout.uniformfloat);
        dialog.show();
        Button done = (Button) dialog.findViewById(gr.allamanis.randgen.R.id.uniFloatOK);
        OnClickListener doneButton = new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    EditText edit = (EditText) dialog.findViewById(gr.allamanis.randgen.R.id.uniformFLow);
                    a = Double.parseDouble(edit.getText().toString());
                    edit = (EditText) dialog.findViewById(gr.allamanis.randgen.R.id.uniformFHigh);
                    b = Double.parseDouble(edit.getText().toString());
                    dialog.dismiss();
                } catch (Exception e) {
                    TextView done = (TextView) dialog.findViewById(gr.allamanis.randgen.R.id.notification);
                    done.setVisibility(View.VISIBLE);
                }
            }
        };
        done.setOnClickListener(doneButton);
    }
}
