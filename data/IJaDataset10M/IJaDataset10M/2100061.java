package com.android.plagas;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.plagas.dao.Establecimiento;

public class adapEstablecimiento extends BaseAdapter implements OnClickListener {

    private Context context;

    private List<Establecimiento> list;

    public adapEstablecimiento(Context context, List<Establecimiento> list) {
        this.context = context;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Establecimiento entry = list.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemestablecimiento, null);
        }
        TextView nombre = (TextView) convertView.findViewById(R.id.name_entry);
        nombre.setText(entry.getNombre());
        return convertView;
    }

    public void onClick(View view) {
        Establecimiento entry = (Establecimiento) view.getTag();
        list.remove(entry);
        notifyDataSetChanged();
    }

    private void showDialog(Establecimiento entry) {
    }
}
