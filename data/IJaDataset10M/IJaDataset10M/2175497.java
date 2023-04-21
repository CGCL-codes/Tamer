package cyber.note;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ListBookAdapter extends ArrayAdapter<NoteBook> {

    ArrayList<NoteBook> array;

    int resource;

    Context context;

    private DBAdapter mDB;

    public ListBookAdapter(Context context, int textViewResourceId, ArrayList<NoteBook> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resource = textViewResourceId;
        array = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View bookView = convertView;
        if (bookView == null) {
            bookView = new BookCustomViewGroup(getContext());
        }
        final NoteBook book = array.get(position);
        final int vitri = position;
        if (book != null) {
            TextView bookContent = ((BookCustomViewGroup) bookView).book_text;
            bookContent.setText(book.toString());
            ImageButton editButton = ((BookCustomViewGroup) bookView).editBut;
            editButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("value", book.getName());
                    sendBundle.putString("id", book.getID());
                    Context tmpContext = getContext();
                    Intent myIntent = new Intent(tmpContext, AddNotebook.class);
                    myIntent.putExtras(sendBundle);
                    tmpContext.startActivity(myIntent);
                }
            });
            ImageButton deleteButton = ((BookCustomViewGroup) bookView).deleteBut;
            deleteButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    mDB = new DBAdapter(getContext());
                    mDB.open();
                    new AlertDialog.Builder(getContext()).setTitle("Delete " + book.getName()).setMessage("Are you sure ??? ").setNeutralButton("No", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            mDB.deleteNotebook(book.getID());
                            mDB.close();
                            Context tmpContext = getContext();
                            Intent myIntent = new Intent(tmpContext, Loading.class);
                            tmpContext.startActivity(myIntent);
                        }
                    }).show();
                }
            });
        }
        return bookView;
    }
}
