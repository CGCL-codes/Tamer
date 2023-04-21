package test.main;

import java.util.ArrayList;
import java.util.Comparator;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Main extends ListActivity {

    String[] maListe2;

    TextView selection;

    String[] maListe;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        System.out.println("=+> listeview r�cup�rable ok");
        maListe = new String[] { "aMachine", "bMachine", "cMachine", "dMachine", "eMachine", "fMachine", "gMachine", "hMachine", "iMachine", "jMachine", "kMachine", "lMachine", "mMachine", "nMachine", "oMachine", "pMachine", "qMachine", "rMachine", "sMachine" };
        maListe2 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" };
        ListAdapter adapter = new DoubleTextAdapter(this, R.layout.row, R.id.text1, R.id.text2, maListe, maListe2);
        setListAdapter(adapter);
        selection = (TextView) findViewById(R.id.empty);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selection.setText(maListe[position]);
    }

    private class DoubleTextAdapter extends ArrayAdapter implements OnClickListener {

        private Activity mContext;

        private View mLigne;

        private TextView mContent1;

        private TextView mContent2;

        private RadioGroup mRadioGroup;

        private int content1;

        private int content2;

        private String[] mList1;

        private String[] mList2;

        private boolean cocher = true;

        /**
		 * Constructeur.
		 *
		 * @param context
		 * @param resource
		 * @param textViewResourceId
		 * @param objects
		 */
        public DoubleTextAdapter(Activity context, int resource, int content1, int content2, String[] list1, String[] list2) {
            super(context, resource);
            this.mContext = context;
            this.content1 = content1;
            this.content2 = content2;
            this.mList1 = list1;
            this.mList2 = list2;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public int getCount() {
            return mList1.length;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public int getPosition(Object item) {
            System.out.println(" => position object : " + item.toString());
            return super.getPosition(item);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public void sort(Comparator comparator) {
            System.out.println(" ==> apel sort !!!");
            super.sort(comparator);
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mLigne = convertView;
            if (mLigne == null) {
                LayoutInflater inflater = mContext.getLayoutInflater();
                mLigne = inflater.inflate(R.layout.row, null);
                mContent1 = (TextView) mLigne.findViewById(content1);
                mContent2 = (TextView) mLigne.findViewById(content2);
            }
            mContent1.setText(mList1[position]);
            mContent2.setText(mList2[position]);
            return mLigne;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public void onClick(View v) {
            System.out.println("==> click chec Layout" + v.toString());
        }
    }
}
