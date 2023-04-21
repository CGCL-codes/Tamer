package com.cwb3.moto.aankondigingencontrollerview;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.cwb3.moto.R;
import com.cwb3.moto.domeinmodel.Aankondiging;

public class AankondigingAdapter extends ArrayAdapter<Aankondiging> {

    private Aankondiging[] items;

    private Context ctx;

    private int tvrId;

    public AankondigingAdapter(Context context, int textViewResourceId, Aankondiging[] items) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.ctx = context;
        this.tvrId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(tvrId, null);
        }
        Aankondiging o = items[position];
        if (o != null) {
            TextView beschrijving = (TextView) v.findViewById(R.id.aankondingbeschrijving);
            TextView datum = (TextView) v.findViewById(R.id.aankondingdatum);
            TextView vak = (TextView) v.findViewById(R.id.aankondingvak);
            TextView tekst = (TextView) v.findViewById(R.id.aankondingtekstgr);
            if (beschrijving != null) {
                beschrijving.setText(o.geefTitel());
            }
            if (vak != null) {
                vak.setText(o.geefVak().geefNaam());
            }
            if (datum != null) {
                java.util.Locale locale = new java.util.Locale("nl", "BE");
                Format startDateFormat = new SimpleDateFormat("EEEEEEEEE dd MMM, HH:mm", locale);
                datum.setText(startDateFormat.format(o.geefDatum()));
            }
            if (tekst != null) {
                tekst.setText(o.geefTekst());
            }
        }
        return v;
    }
}
