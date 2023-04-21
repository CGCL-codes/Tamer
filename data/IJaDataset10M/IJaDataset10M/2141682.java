package igor.mass.render.renderables;

import igor.mass.R;
import igor.mass.model.AndroidParam;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class BooleanRenderable extends TableLayout implements AndroidRenderable {

    AndroidParam param;

    TextView label;

    ToggleButton button;

    public BooleanRenderable(Context context, AndroidParam param, Object initialValue) {
        super(context);
        this.param = param;
        setBackgroundColor(R.color.background);
        TableRow row = new TableRow(context);
        row.setBackgroundResource(R.color.background);
        row.setPadding(0, 8, 0, 6);
        android.widget.TableRow.LayoutParams params = new android.widget.TableRow.LayoutParams(0);
        params.height = 38;
        params.weight = 0.5f;
        label = new TextView(context);
        label.setText(param.name);
        label.setTextSize(18);
        label.setPadding(8, 0, 0, 0);
        label.setBackgroundResource(R.color.background);
        label.setGravity(Gravity.LEFT);
        row.addView(label, params);
        android.widget.TableRow.LayoutParams params2 = new android.widget.TableRow.LayoutParams(1);
        button = new ToggleButton(context);
        button.setChecked((Boolean.parseBoolean(initialValue.toString())));
        button.setGravity(Gravity.CENTER);
        row.addView(button, params2);
        addView(row);
        setStretchAllColumns(true);
    }

    @Override
    public String getValue() {
        return button.isChecked() + "";
    }

    @Override
    public View getView() {
        return this;
    }
}
