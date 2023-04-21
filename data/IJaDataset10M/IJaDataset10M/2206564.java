package com.gobynote.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.gobynote.android.R;
import com.gobynote.android.models.NoteRow;
import com.gobynote.android.util.PreferenceUtils;
import com.gobynote.android.util.WPHtml;

public class NoteListAdapter extends BaseAdapter implements OnClickListener {

    Context ctx;

    private LayoutInflater mInflater;

    private ArrayList<NoteRow> data;

    private HashMap<String, String> selectedItems = new HashMap<String, String>();

    private static final String TAG = "NoteListAdapter";

    private OnNoteSelectedListener onNoteSelectedListener;

    public NoteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        onNoteSelectedListener = (OnNoteSelectedListener) context;
        ctx = context;
    }

    public void setData(ArrayList<NoteRow> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int item) {
        return data.get(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.notelist_detail, null);
            holder = new ViewHolder();
            holder.mItemName = (TextView) view.findViewById(R.id.itemName);
            holder.mItemDesc = (TextView) view.findViewById(R.id.itemDesc);
            holder.mItemCheckBox = (CheckBox) view.findViewById(R.id.itemCheckBox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mItemName.setText(data.get(position).getTitle());
        holder.mItemName.setTextColor(PreferenceUtils.getImportanceColor(view.getContext(), data.get(position).getImportance()));
        String desc = data.get(position).getShortDesc();
        if (data.get(position).getShortDesc().length() == 0) {
            desc = "";
            holder.mItemDesc.setVisibility(View.GONE);
        } else {
            holder.mItemDesc.setText(WPHtml.fromHtml(desc, ctx));
            holder.mItemDesc.setVisibility(View.VISIBLE);
        }
        String idStr = Integer.toString(data.get(position).getNoteId());
        holder.mItemCheckBox.setTag(idStr);
        if (selectedItems.containsKey(idStr)) {
            holder.mItemCheckBox.setChecked(true);
        } else {
            holder.mItemCheckBox.setChecked(false);
        }
        holder.mItemCheckBox.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        CheckBox cBox = (CheckBox) v;
        String itemId = (String) cBox.getTag().toString();
        if (cBox.isChecked()) {
            selectedItems.put(itemId, itemId);
        } else {
            selectedItems.remove(itemId);
        }
        NoteRow curNote = null;
        for (NoteRow note : data) {
            String idStr = Integer.toString(note.getNoteId());
            if (idStr.equals(itemId)) {
                curNote = note;
                break;
            }
        }
        onNoteSelectedListener.onNoteSelected(curNote);
    }

    public ArrayList<NoteRow> getSelectedItems() {
        ArrayList<NoteRow> selected = new ArrayList<NoteRow>();
        for (NoteRow note : data) {
            String idStr = Integer.toString(note.getNoteId());
            if (selectedItems.containsKey(idStr)) {
                selected.add(note);
            }
        }
        return selected;
    }

    static class ViewHolder {

        TextView mItemName;

        TextView mItemDesc;

        CheckBox mItemCheckBox;
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public interface OnNoteSelectedListener {

        public void onNoteSelected(NoteRow note);
    }
}
