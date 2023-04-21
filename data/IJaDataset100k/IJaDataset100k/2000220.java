package com.android.wallpaper.nexus;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;
import com.android.wallpaper.RenderScriptWallpaper;
import com.android.wallpaper.RenderScriptScene;

public class NexusWallpaper extends RenderScriptWallpaper {

    Context context = null;

    protected RenderScriptScene createScene(int width, int height) {
        try {
            context = createPackageContext("com.android.settings", MODE_WORLD_READABLE);
        } catch (NameNotFoundException e) {
            Toast.makeText(NexusWallpaper.this, "PROBLEM LOADING SHARED PREFERENCES", Toast.LENGTH_SHORT).show();
        }
        return new NexusRS(context, width, height);
    }
}
