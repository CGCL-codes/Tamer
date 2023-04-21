package model;

import model.media.Media;

/**
 *
 * @author dani
 */
public class MediaEnumType extends EnumUserType<Media.MEDIA_TYPE> {

    public MediaEnumType() {
        super(Media.MEDIA_TYPE.class);
    }
}
