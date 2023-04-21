package com.manning.aip.brewmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.manning.aip.brewmap.model.BrewLocation;
import java.util.ArrayList;
import java.util.List;

public class BrewLocationOverlay extends ItemizedOverlay<OverlayItem> {

    private List<BrewLocation> brewLocations;

    private Context context;

    public BrewLocationOverlay(Context context, List<BrewLocation> brewLocations, Drawable marker) {
        super(boundCenterBottom(marker));
        this.context = context;
        this.brewLocations = brewLocations;
        if (brewLocations == null) {
            brewLocations = new ArrayList<BrewLocation>();
        }
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        BrewLocation brewLocation = brewLocations.get(i);
        GeoPoint point = new GeoPoint((int) (brewLocation.getLatitude() * 1e6), (int) (brewLocation.getLongitude() * 1e6));
        return new OverlayItem(point, brewLocation.getName(), null);
    }

    @Override
    public boolean onTap(final int index) {
        BrewLocation brewLocation = brewLocations.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("BrewLocation").setMessage(brewLocation.getName() + "\n\nVisit the pub detail page for more information?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(context, BrewLocationDetails.class);
                i.putExtra(BrewMapApp.PUB_INDEX, index);
                context.startActivity(i);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }

    @Override
    public int size() {
        return brewLocations.size();
    }
}
