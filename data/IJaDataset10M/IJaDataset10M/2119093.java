package org.blueoxygen.cimande2.ma.gene.adapter;

import org.blueoxygen.cimande2.ma.gene.R;
import org.blueoxygen.cimande2.ma.gene.entity.Person;
import org.blueoxygen.cimande2.ma.gene.manager.DbManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonAdapterView extends LinearLayout {

    DbManager dbManager;

    public PersonAdapterView(Context context, Person entry) {
        super(context);
        this.setOrientation(VERTICAL);
        this.setTag(entry);
        dbManager = new DbManager(context);
        View v = inflate(context, R.layout.phone_row, null);
        TextView tvContact = (TextView) v.findViewById(R.id.name);
        tvContact.setText(entry.getFirstName() + " " + entry.getLastName());
        TextView tvMail = (TextView) v.findViewById(R.id.email);
        tvMail.setText(entry.getEmail());
        ImageView imageView = (ImageView) v.findViewById(R.id.image);
        BitmapDrawable image = null;
        if (image == null) {
            image = ImageUtils.ImageOperations(context, dbManager.getConnectionUrl() + "Cimande2/person/" + entry.getId() + "/photo");
        }
        imageView.setImageBitmap(ImageUtils.getResizedBitmap(image.getBitmap(), 200, 200));
        addView(v);
    }
}
