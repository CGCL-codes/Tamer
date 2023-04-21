package it.unicaradio.android.adapters;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Paolo Cortis
 * @param <T>
 * 
 */
public class ArrayAlternatedColoursAdapter<T> extends ArrayAdapter<T> {

    private final int[] colours = new int[] { 0xFF000000, 0xFF333333 };

    /**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
    public ArrayAlternatedColoursAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    /**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
    public ArrayAlternatedColoursAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    /**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 */
    public ArrayAlternatedColoursAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    /**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
    public ArrayAlternatedColoursAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    /**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
    public ArrayAlternatedColoursAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
    }

    /**
	 * @param context
	 * @param textViewResourceId
	 */
    public ArrayAlternatedColoursAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int colourPos = position % colours.length;
        view.setBackgroundColor(colours[colourPos]);
        return view;
    }
}
