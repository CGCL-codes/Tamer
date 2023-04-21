package dk.hewison.client.component;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.Widget;
import dk.hewison.client.Log;
import dk.hewison.client.domain.Photo;

/**
 * @author John Hewison
 * @author $LastChangedBy: john.hewison $:  $ $LastChangedDate: 2009-02-07 15:27:07 -0500 (Sat, 07 Feb 2009) $:
 * @version $Revision: 362 $:
 */
public class MyImage extends Image {

    private boolean loaded = false;

    private int origWidth;

    private int origHeight;

    private Photo photo;

    private LoadListener loadListener;

    public MyImage(Photo photo) {
        this(photo, true);
    }

    public MyImage(Photo photo, boolean thumbnail, LoadListener loadListener) {
        this.photo = photo;
        this.loadListener = loadListener;
        if (thumbnail) {
            setUrl(photo.getThumbnailUrl());
        } else {
            setUrl(photo.getFileNameUrl());
        }
        init();
    }

    public MyImage(Photo photo, boolean thunbnail) {
        this(photo, thunbnail, null);
    }

    public MyImage(MyImage myImage) {
        this.photo = myImage.photo;
        init();
    }

    private void init() {
        addLoadListener(new LoadListener() {

            public void onError(Widget sender) {
            }

            public void onLoad(Widget sender) {
                origWidth = getWidth();
                origHeight = getHeight();
                Log.debug("MyImage.onLoad " + origWidth + " x " + +origHeight);
                loaded = true;
                if (loadListener != null) {
                    loadListener.onLoad(sender);
                }
            }
        });
        setTitle(photo.getName());
    }

    public void showImage() {
        loaded = false;
        setUrl(photo.getFileNameUrl());
    }

    public boolean isLoaded() {
        origWidth = getWidth();
        origHeight = getHeight();
        return loaded;
    }

    @Override
    public void setPixelSize(int width, int height) {
        super.setPixelSize(width, height);
    }

    public Photo getPhoto() {
        return photo;
    }
}
