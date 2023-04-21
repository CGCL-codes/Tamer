package cc.carnero.ctwee;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Dialog;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.view.View;
import android.view.LayoutInflater;
import android.content.Context;
import android.util.Log;

public class ctactions extends Dialog {

    private Context context;

    private String name;

    private ArrayList<ctAction> actions = new ArrayList<ctAction>();

    public ctactions(Context context, String name, ArrayList<ctAction> actions) {
        super(context);
        this.context = context;
        this.name = name;
        this.actions = actions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Boolean revert = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actions);
        getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = this.getLayoutInflater();
        View buttonView;
        Button button;
        this.setTitle(getContext().getResources().getString(R.string.action_title));
        try {
            LinearLayout layout = (LinearLayout) this.findViewById(R.id.tweet_buttons);
            for (final ctAction action : actions) {
                revert = false;
                if (action.getType() == ctGlobal.ACTION_THREAD && ctGlobal.expanded.contains(action.getTweetId()) == true) {
                    revert = true;
                }
                buttonView = (View) inflater.inflate(R.layout.action_button, null);
                button = (Button) buttonView.findViewById(R.id.button);
                if (revert == false) {
                    button.setText(action.getTitle());
                } else {
                    button.setText(action.getRevertTitle());
                }
                button.setClickable(true);
                buttonAction buttonActionListener = new buttonAction(action, revert);
                button.setOnClickListener(buttonActionListener);
                layout.addView(buttonView);
            }
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctactions.onCreate: " + e.toString());
        }
    }

    public class buttonAction implements Button.OnClickListener {

        private ctAction action = null;

        private Boolean revert = false;

        public buttonAction(ctAction action, Boolean revert) {
            this.action = action;
            this.revert = revert;
        }

        @Override
        public void onClick(View arg0) {
            if (revert == false) {
                action.getAction().go();
            } else {
                action.getAction().revert();
            }
            dismiss();
        }
    }
}
