package android.client;

import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Classe che implementa le schermate per la visualizzazione e la modifica delle posizioni inserite dall'utente
 * @author  Nicolas Tagliani
 * @author  Vincenzo Frascino
 */
public class ViewLocations extends ListActivity implements OnClickListener {

    /**
	 * Intent a cui e' sensibile questa classe
	 */
    public static final String VIEW_LOCATIONS_ACTION = "android.client.action.VIEW_LOCATIONS";

    /**
	 * @uml.property  name="db"
	 * @uml.associationEnd  
	 */
    private DBHelper db;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        db = new DBHelper(this);
        setTitle("Default location is: " + db.getDefault());
        setListAdapter(new MyListAdapter(this, this));
    }

    public void onClick(View arg0) {
        MyView m = (MyView) ((Button) arg0).getParent();
        m.delete();
        setListAdapter(new MyListAdapter(this, this));
    }

    /**
	 * Classe per la rappresentazione dei dati in una listView.
	 * I valori da inserire sono ottenuti tramite DBHelper
	 * 
	 * @author Nicolas Tagliani
	 * @author Vincenzo Frascino
	 *
	 */
    class MyListAdapter extends BaseAdapter {

        private List<DBHelper.Location> list = db.fetchAllRows();

        private Context ctx;

        private OnClickListener listener;

        /**
		 * Costruttore dell'adapter
		 * 
		 * @param ctx Il contesto associato all'adapter
		 * @param listener Un listener per effettuare le operazioni quando si preme il tasto delete
		 */
        public MyListAdapter(Context ctx, OnClickListener listener) {
            this.ctx = ctx;
            this.listener = listener;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            MyView mv = new MyView(ctx, list.get(position));
            mv.setOnClickListener(listener);
            return mv;
        }
    }

    /**
	 * @author  gabrielknight
	 */
    private class MyView extends LinearLayout {

        private TextView t;

        private Button b;

        /**
		 * @uml.property  name="loc"
		 * @uml.associationEnd  
		 */
        private DBHelper.Location loc;

        public MyView(Context context, DBHelper.Location loc) {
            super(context);
            this.loc = loc;
            this.setOrientation(HORIZONTAL);
            t = new TextView(context);
            String text = "";
            if (!loc.address.equals("")) {
                text = text + loc.address + "\n";
            }
            text = text + loc.preferred + "\n" + loc.latitude + "," + loc.longitude;
            t.setText(text);
            t.setGravity(Gravity.CENTER_VERTICAL);
            addView(t, new LinearLayout.LayoutParams(250, LayoutParams.WRAP_CONTENT));
            b = new Button(context);
            b.setGravity(Gravity.CENTER_VERTICAL);
            b.setText("Delete");
            addView(b, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        public void setOnClickListener(OnClickListener clk) {
            b.setOnClickListener(clk);
        }

        public void delete() {
            db.deleteRow(loc);
        }
    }
}
