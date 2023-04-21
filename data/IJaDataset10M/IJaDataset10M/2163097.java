package org.entomologistproject.trac;

import greendroid.app.GDActivity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends GDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.about);
        setTitle("About");
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String about = "<b>Entomologist Mobile/Trac " + versionName + "</b><br>" + "(C) 2011 <a href=\"mailto:matt@entomologist-project.org\">Matt Barringer</a><br><br>" + "Most of this software is released under the GPL.<br>" + "A portion of this software incorporates code from the android-xmlrpc project, released under " + "the Apache 2.0 License.  For information on android-xmlrpc, go to <a href=\"https://code.google.com/p/android-xmlrpc/\">their website</a>." + "<br><br>" + "For more information about Entomologist, or to download " + "the source code, go to <a href=\"http://www.entomologist-project.org/mobile/\">the project homepage</a>." + "<br><br>" + "Please report bugs using <a href=\"https://trac.entomologist-project.org\">our bug tracker</a>.";
        final TextView aboutText = (TextView) findViewById(R.id.about);
        aboutText.setText(Html.fromHtml(about));
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
